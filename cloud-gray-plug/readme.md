
### 1链路接口实现
当所有服务接入灰jar后，想要测试一下链路效果，并不是很方便；所以
在所有服务接入依赖后，预先设定链路测试接口方，以便测试整体链路效果是否达到预期
```
/**
 * 路由链路测试
 *
 * @author xie
 */
@RestController
@RequestMapping("/gray/test")
public class GrayRouteTestController {

    private Logger logger = LoggerFactory.getLogger(GrayRouteTestController.class);

    @Resource
    private EurekaDiscoveryClient discoveryClient;

    @Resource()
    private ApplicationInfoManager applicationInfoManager;

    @Resource
    private RestTemplate restTemplate;

    /**
     * @param nodes  需要请求的顺序服务信息
     * @return
     */
    @RequestMapping(value = "linksTest", method = RequestMethod.POST)
    public TestResult nodeTest(@RequestBody List<String> nodes) {
        InstanceInfo startNodeServer = applicationInfoManager.getInfo();
        String currentServiceId = startNodeServer.getAppName().toLowerCase();
        if (!nodes.get(0).equals(currentServiceId)) {
            nodes.add(0,currentServiceId);
        }
        //处理链路节点信息
        RequestNode requestNode = convert2RequestNode(nodes);
        List<ResultNode> resultNodes = internalNodeTest(requestNode);
        ResultNode startNode = new ResultNode();
        startNode.setServiceId(startNodeServer.getAppName().toLowerCase());
        startNode.setHost(startNodeServer.getHostName());
        startNode.setPort(startNodeServer.getPort());
        String serverStatus = startNodeServer.getMetadata().get(Constant.INSTANCE_STATUS);
        startNode.setInstanceStatus(serverStatus);
        resultNodes.add(startNode);
        Comparator<ResultNode> tComparator = Collections.reverseOrder();
        resultNodes.sort(tComparator);
        TestResult testResult = new TestResult();
        ResultNode resultNode = resultNodes.get(resultNodes.size() - 1);
        testResult.setSuccess(resultNode.isSuccess());
        testResult.setMessage(resultNode.getMessage());
        testResult.setNodeList(resultNodes);
        testResult.setTotal(nodes.size());
        testResult.setRoute2Gray(GrayUtils.isGray());
        testResult.setFailureCount(resultNode.isSuccess() ? 0 : nodes.size() - (resultNodes.size() - 1));
        return testResult;

    }


    @RequestMapping(value = "internalNodeTest", method = RequestMethod.POST)
    public List<ResultNode> internalNodeTest(@RequestBody RequestNode requestNode) {
        //获取下一请求服务节点信息
        RequestNode next = requestNode.getNext();
        if (next == null) {
            logger.info("request is end");
            List<ResultNode> list = new ArrayList<>();
            return list;
        }

        GrayUtils.currentSelectServer.set(new Server(next.getServiceId()));
        try {
            ResultNode nextNodeServer = new ResultNode();
            nextNodeServer.setIndex(next.getIndex());
            nextNodeServer.setServiceId(next.getServiceId());
            List<ResultNode> resultNodes = new ArrayList<>();
            //处理服务请求url
            String serverUrl = getServerUrl(next.getServiceId());
            if (StringUtils.isEmpty(serverUrl)) {
                nextNodeServer.setMessage("无" + next.getServiceId() + "服务信息");
                nextNodeServer.setSuccess(false);
                resultNodes.add(nextNodeServer);
                return resultNodes;
            }
            HttpHeaders requestHeaders = new HttpHeaders();
            //传递灰度标签
            requestHeaders.add(Constant.ROUTE_TO_GRAY, String.valueOf(GrayUtils.isGray()));
            HttpEntity<RequestNode> requestEntity = new HttpEntity<>(next, requestHeaders);

            try {
                ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);
                //下一服务节点处理成功，处理返回结果
                resultNodes = JSON.parseArray(response.getBody(), ResultNode.class);
                Server testServer = GrayUtils.getTestServer();
                nextNodeServer.setHost(testServer.getHost());
                nextNodeServer.setPort(testServer.getPort());
                if (testServer instanceof DiscoveryEnabledServer) {
                    DiscoveryEnabledServer enabledServer = (DiscoveryEnabledServer) testServer;
                    InstanceInfo instanceInfo = enabledServer.getInstanceInfo();
                    String serverStatus = instanceInfo.getMetadata().get(Constant.INSTANCE_STATUS);
                    nextNodeServer.setInstanceStatus(serverStatus);
                    nextNodeServer.setIndex(next.getIndex());
                }
            } catch (Exception ex) {
                //下一服务节点请求失败
                logger.warn("links test failure:{}", ex.getMessage());
                nextNodeServer.setMessage("[route_to_gray:" + GrayUtils.isGray() + "]" + ex.getMessage() + serverUrl);
                nextNodeServer.setSuccess(false);
                resultNodes.add(nextNodeServer);
            }

            resultNodes.add(nextNodeServer);
            return resultNodes;
        } finally {
            GrayUtils.removeTestServer();
        }
    }


    private RequestNode convert2RequestNode(List<String> nodes) {
        RequestNode nextNode = null;
        for (int i = nodes.size() - 1; i >= 0; i--) {
            if (nextNode == null) {
                nextNode = new RequestNode(nodes.get(i), i);
            } else {
                RequestNode tNextNode = new RequestNode(nodes.get(i), i);
                tNextNode.setNext(nextNode);
                nextNode = tNextNode;
            }
        }
        return nextNode;
    }

    /**
     * 处理请求url
     * @param nextNodeId
     * @return
     */
    private String getServerUrl(String nextNodeId) {
        List<ServiceInstance> instances = discoveryClient.getInstances(nextNodeId);
        if (instances.isEmpty()) {
            return null;
        }
        ServiceInstance serviceInstance = instances.get(0);
        String contextPath = serviceInstance.getMetadata().get(CONTEXT_PATH);
        String temContextPath = contextPath == null ? "/" : contextPath;
        String url = "http://" + nextNodeId.toLowerCase() + temContextPath + "/gray/test/internalNodeTest";
        return url;
    }
}
```

### 2链路接口绕过拦截
通常服务接口访问都需要登录，未登录访问的接口将会拦截
结果实际，通常访问拦截的实现要么要filter实现，要么在HandlerInterceptor(mvc项目)
为了让测试接口不被拦截住，那么得想办法绕过这些拦截；在不修改业务代码的情况下，绕过
拦截，代理是常用的做法；结果spring代理特点，可以轻松实现

#### 2.1 创建filter 或HandlerInterceptor 代理
```
public class InterceptorBeanProcessor implements BeanPostProcessor {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final Map<String, Object> baseTypes = new HashMap<>();

    private Enhancer enhancer = new Enhancer();

    private boolean filterClassExist;

    private boolean interceptorClassExist;

    private GrayProperties grayProperties;

    public InterceptorBeanProcessor(GrayProperties grayProperties){
        this.grayProperties = grayProperties;
    }

    static {
        baseTypes.put("int", 0);
        baseTypes.put("short", 0);
        baseTypes.put("long", 0L);
        baseTypes.put("double", 0d);
        baseTypes.put("float", 0f);
        baseTypes.put("boolean", false);
        baseTypes.put("char", (char) 0);
        baseTypes.put("byte", Byte.valueOf((byte) 0));
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        //创建filter 的代理
        Object filterProxy = createFilterProxy(bean);
        if(bean.equals(filterProxy)){
            //创建Interceptor 代理
           return createInterceptorProxy(bean);
        }else {
          return filterProxy;
        }
    }
    
    private Object createFilterProxy(Object bean){
        if(!filterClassExist()) {
            return bean;
        }
        if (bean instanceof Filter) {
            Class<?> clazz = bean.getClass();
            try {
                return cglibProxy(clazz, new FilterMethodInterceptor(bean,grayProperties));
            } catch (Throwable e) {
                logger.warn("create {} filter proxy failure :{}", clazz.getName(), e.getMessage());
                return bean;
            }
        }
        return bean;
    }
    private Object createInterceptorProxy(Object bean){
        if(!interceptorClassExist()) {
            return bean;
        }
        if (bean instanceof HandlerInterceptor) {
            Class<?> clazz = bean.getClass();
            try {
                return cglibProxy(clazz, new HandlerInterceptorMethodInterceptor(bean,grayProperties));
            } catch (Throwable e) {
                logger.warn("create {} interceptor proxy failure:{}", clazz.getName(), e.getMessage());
                return bean;
            }
        }
        return bean;
    }

    /**
     * 如果不是servlet容器，filter 可能不存在
     * @return
     */
    private boolean filterClassExist() {
        if(filterClassExist){
            return true;
        }
        try {
            Class clzz =  Filter.class;
            filterClassExist = true;
        }catch (Throwable e){
            filterClassExist = false;
        }
        return false;
    }

    private boolean interceptorClassExist() {
        if(interceptorClassExist){
            return true;
        }
        try {
            Class clzz =  HandlerInterceptor.class;
            interceptorClassExist = true;
        }catch ( Throwable e){
            interceptorClassExist = false;
        }
        return false;
    }

    /**
     * final 方法没法创建代理(cglib代理为实现灰的子类，故无法覆盖其final方法)
     *
     * @param clazz
     * @return
     */
    private Object cglibProxy(Class clazz, Callback callback) {
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(callback);
        Constructor<?>[] constructors = clazz.getConstructors();
        Parameter[] parameters = constructors[0].getParameters();
        Object[] arguments = new Object[parameters.length];
        Class[] paramsTypes = new Class[parameters.length];
        for (int i = 0; i < arguments.length; i++) {
            Class<?> type = parameters[i].getType();
            arguments[i] = baseTypes.get(type.getSimpleName().toLowerCase());
            paramsTypes[i] = type;
        }
        return enhancer.create(paramsTypes, arguments);
    }

}

```
#### 2.2 处理是否跳过拦截
```
public  class InterceptorAdapter implements Callback {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final String FILTER_METHOD="doFilter";

    private final String INTERCEPTOR_METHOD="preHandle";

    PathMatcher pathMatcher = new AntPathMatcher();

    private Object target;

    private GrayProperties grayProperties;

    public InterceptorAdapter(Object target, GrayProperties grayProperties) {
        this.target = target;
        this.grayProperties = grayProperties;
    }



    /**
     * 如果是测度路径，跳过当前filter
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws ServletException
     */
    protected Object doFilter(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException, IOException, ServletException {
        //是否为doFilter 方法
        if (FILTER_METHOD.equals(method.getName())) {
            HttpServletRequest servletRequest = (HttpServletRequest) args[0];
            ServletResponse servletResponse = (ServletResponse) args[1];
            FilterChain filterChain = (FilterChain) args[2];
            String servletPath = servletRequest.getServletPath();
            //符合跳过规则，执行一个filter
            if (skipCurrentPath(servletPath)) {
                logger.debug("[{}] is gray test path ,skip filter of {}",servletPath,target.getClass().getSimpleName());
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                method.invoke(target, args);
            }
        } else {
            method.setAccessible(true);
            return method.invoke(target, args);
        }
        return null;
    }


    private boolean skipCurrentPath(String servletPath){

        Set<String> skipPaths = grayProperties.getSkipPaths();
        for(String pattern :skipPaths){
            if(pathMatcher.match(pattern,servletPath)){
                return true;
            }
        }
        return false;
    }

    /**
     * 如果是测试路径，跳过当前interceptor
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Exception
     */
    protected Object preHandle(Object proxy, Method method, Object[] args)
            throws Exception {
        //是否为preHandle 方法
        if (INTERCEPTOR_METHOD.equals(method.getName())) {
            HttpServletRequest servletRequest = (HttpServletRequest) args[0];
            String servletPath = servletRequest.getServletPath();
            //符合跳过规则，不执当前代理逻辑
            if (skipCurrentPath(servletPath)) {
                logger.debug("[{}] is gray test path ,skip interceptor of {}", servletPath, target.getClass().getSimpleName());
                return true;
            } else {
                return method.invoke(target, args);
            }
        } else {
            method.setAccessible(true);
            return method.invoke(target, args);
        }
    }

    /**
     * 如果是测试链路，跳过该过滤器
     * @param proxy
     * @param method
     * @param args
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        return doFilter(proxy,method,args);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return doFilter(proxy,method,args);
    }
}
```

