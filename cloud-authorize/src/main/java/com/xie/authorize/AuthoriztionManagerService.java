//package com.xhg.authorize;
//
//import JwtTokenProvider;
//import UAAClaims;
//import com.xhg.gateway.api.authorize.*;
//import XhgException;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.util.DigestUtils;
//import org.springframework.util.StringUtils;
//import redis.clients.jedis.JedisCluster;
//
//import javax.annotation.Resource;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import static Constants.TOKEN_CREATE_TIME;
//import static Constants.USER_SERVICE_ID;
//
//
//@Service
//public class AuthoriztionManagerService implements AuthorizeService {
//
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    private final static long TIME_SEC = 1000;
//
//    private static final String GLOBAL_LOGIN_TOKEN = "api_gateway";
//
//    private static final String LOGIN_TOKEN = ":login_token:";
//
//    @Resource
//    private ClientService oauth2ClientService;
//
//    @Resource
//    private JedisCluster jedisCluster;
//
//    @Resource
//    private JwtTokenProvider tokenProvider;
//
//    private volatile Map</*serviceId*/String,/*projectName*/String> serviceProjectNames = new HashMap<>();
//
//    @Override
//    public AuthorInfo authorize(AuthRequest authRequest) {
//
//        if (authRequest.getExpireInSec() <= 0) {
//            logger.warn("token 没有指定有效期，将长期有效 ");
//            authRequest.setExpireInSec(Integer.MAX_VALUE);
//        }
//        UAAClaims uaaClaims = new UAAClaims(authRequest.getMap());
//        uaaClaims.setExpiration(new Date(System.currentTimeMillis() + authRequest.getExpireInSec() * TIME_SEC));
//        uaaClaims.setExpireInSec(authRequest.getExpireInSec());
//        uaaClaims.setId(authRequest.getUserId());
//        uaaClaims.setSubject(authRequest.getUserId());
//        //正常的token
//        String jwtToken = tokenProvider.createToken(uaaClaims);
//
//        AuthorInfo info = new AuthorInfo();
//        info.setCode(0);
//        info.setExpireInSec(uaaClaims.getExpireInSec());
//        info.put(TOKEN_CREATE_TIME,uaaClaims.get(TOKEN_CREATE_TIME));
//        String newToken = saveToken(uaaClaims, jwtToken);
//        info.setToken(newToken);
//        return info;
//    }
//
//    @Override
//    public boolean logout(String token) {
//        try {
//            String key = getTokenKeyPrefix() + token;
//            jedisCluster.del(key);
//        }catch (Exception e){
//           logger.error("登录失败:{}",e);
//        }
//        return true;
//    }
//
//
//    private String getTokenKeyPrefix(){
//        RouteContext context = RoutContextHolder.getCurrentContext();
//        String userServiceId = (String) context.get(USER_SERVICE_ID);
//        if(StringUtils.isEmpty(userServiceId)){
//            userServiceId = context.getServcieId();
//            logger.warn("userServiceId 为空");
//        }
//        return userServiceId+LOGIN_TOKEN;
//    }
//
//    public String refreshToken(String token) {
//        String key = token;
//        String refreshToken = jedisCluster.get(key);
//        if (refreshToken == null) {
//            logger.warn("token 过了刷新期了");
//            return null;
//        }
//        Claims claims = tokenProvider.parseToken(refreshToken);
//        UAAClaims uaaClaims = new UAAClaims(claims);
//        String newJwtToken = tokenProvider.createToken(uaaClaims);
//        String newToken = saveToken(uaaClaims, newJwtToken);
//        jedisCluster.del(key);
//        return newToken;
//    }
//
//    /**
//     * 这里只保留refreshToken
//     *
//     * @param claims
//     * @param srcJwtToken
//     * @return
//     */
//    private String saveToken(UAAClaims claims, String srcJwtToken) {
//        String key = getTokenKeyPrefix() + DigestUtils.md5DigestAsHex(srcJwtToken.getBytes())+System.nanoTime();
//        String newToken = key;
//        if(logger.isDebugEnabled()){
//            logger.debug("生成的token:{}",newToken);
//        }
//        //获取原token有效时长
//        int expirtSec = claims.getExpireInSec();
//        //保存时间加倍
//        if (expirtSec != Integer.MAX_VALUE) {
//            expirtSec = claims.getExpireInSec() * 2;
//        }
//        logger.debug("token 有效时长:{} s", expirtSec / 2);
//        claims.setExpiration(new Date(System.currentTimeMillis() + expirtSec * TIME_SEC));
//        String newJwtToken = tokenProvider.createToken(claims);
//        jedisCluster.set(key, newJwtToken);
//        jedisCluster.expire(key, expirtSec);
//        return newToken;
//    }
//
//
//    public AuthorInfo parseToken(TokenParse request) {
//        int ckeck = checkClientInfo(request);
//        if (ckeck != 0) {
//            AuthorInfo info = new AuthorInfo();
//            info.setCode(ckeck);
//            // return info;
//        }
//        try {
//            return parseToken(request.getToken());
//        } catch (ExpiredJwtException expireEx) {
//            throw new XhgException("token过期");
//        } catch (Exception e) {
//            throw new XhgException("token无效");
//        }
//    }
//
//
//    private int checkClientInfo(TokenParse request) {
//        if (StringUtils.isEmpty(request.getClientId()) || StringUtils.isEmpty(request.getClientSecret()) || StringUtils.isEmpty(request.getToken())) {
//            return -1;
//        }
//        String key = request.getClientId() + request.getClientSecret();
//        String result = jedisCluster.get(key);
//        if (result == null) {
//            Oauth2ClientInfo client = oauth2ClientService.queryClientInfo(request.getClientId());
//            if (client == null) {
//                //客户端服务没注册
//                return -1;
//            }
//            if (!client.getClientSecret().equals(request.getClientSecret())) {
//                //安全码有误
//                return -2;
//            }
//        }
//        return 0;
//    }
//
//    @Override
//    public AuthorInfo parseToken(String token) {
//        String key = token;
//        if(logger.isDebugEnabled()){
//            logger.debug("获取 token 的key:{}", key);
//        }
//        String realToken = jedisCluster.get(key);
//        if (StringUtils.isEmpty(realToken)) {
//            throw new XhgException("token无效");
//        }
//        Claims claims = tokenProvider.parseToken(realToken);
//        AuthorInfo refreshInfo = new AuthorInfo();
//        refreshInfo.putAll(claims);
//        long cur = System.currentTimeMillis();
//        long createTime = 0;
//        Long  tokenCreateTime = (Long) refreshInfo.get(TOKEN_CREATE_TIME);
//        if(tokenCreateTime != null){
//            createTime =  tokenCreateTime;
//        }
//        long lef = refreshInfo.getExpireInSec() - (cur - createTime) / 1000;
//        logger.debug("token剩余有效秒数:{} s", lef);
//        if (lef <= 0) {
//            throw new ExpiredJwtException(null, null, "token过期");
//        }
//        return refreshInfo;
//
//    }
//
//    @Override
//    public String getToken(String key) {
//        return jedisCluster.get(key);
//    }
//
//
//}
