package test.test;

import com.netflix.eureka.EurekaServerContext;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.cglib.proxy.Enhancer;

public class ProxyTest {
    private static final Map<String,Object> baseTypes = new HashMap<>();

         static    {
                baseTypes.put("int",0);
                baseTypes.put("short",0);
                baseTypes.put("long",0l);
                baseTypes.put("double",0d);
                baseTypes.put("float",0f);
                baseTypes.put("boolean",false);
                baseTypes.put("char",(char) 0);
                baseTypes.put("byte",Byte.valueOf((byte) 0));
            }


    public static void main(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, ServletException {
         //CGLIb();

        JDK();
    }

    private static void JDK() throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException, ServletException {


        Class<?> aClass = MyFilter.class;
        Constructor<?>[] constructors =
                aClass.getConstructors();
        Parameter[] parameters = constructors[0].getParameters();
        Object[] initargs = new Object[parameters.length];
        for (int i = 0; i < initargs.length; i++) {
            Class<?> type = parameters[i].getType();
            Object o = baseTypes.get(type.getSimpleName().toLowerCase());
            initargs[i] = o;
        }
//        MyFilter o = (MyFilter) constructors[0].newInstance(new Object[]{new HandlerInterceptorMethodInterceptor(null)});
//        o.doFilter(null,null,null);
        System.out.println("ProxyTest.main");
    }

    private static void CGLIb() {
        Enhancer enhancer = new Enhancer();
        Class<?> aClass = MyFilter.class;
        Constructor<?>[] constructors =
                aClass.getConstructors();
        enhancer.setSuperclass(aClass);
        //enhancer.setCallback(new HandlerInterceptorMethodInterceptor((null)));
        Parameter[] parameters = constructors[0].getParameters();
        Object[] initargs = new Object[parameters.length];
        Class[] paramsTypes = new Class[parameters.length];
        for (int i = 0; i < initargs.length; i++) {
            Class<?> type = parameters[i].getType();
            initargs[i] = baseTypes.get(type.getSimpleName().toLowerCase());
            paramsTypes[i] = type;
        }
        Object o = enhancer.create(paramsTypes, initargs);

        System.out.println("ProxyTest.main");
    }

    public static interface Filter {
        void doFilter(ServletRequest request, ServletResponse response,
                      FilterChain chain) throws IOException, ServletException;
    }

    public static class MyFilter implements Filter {

        private int s;

        public MyFilter(Float i, EurekaServerContext proxyTest){
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        }
    }



}
