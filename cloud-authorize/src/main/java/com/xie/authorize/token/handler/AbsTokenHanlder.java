package com.xie.authorize.token.handler;

import static com.xie.authorize.constant.Constants.ABORT_FIND_TOKEN;
import static com.xie.authorize.constant.Constants.ROUT_CONTEXT_MESSAGE;
import static com.xie.authorize.constant.Constants.TOKEN_CREATE_TIME;
import static com.xie.authorize.constant.Constants.TOKEN_REFRESH_KEY;
import static com.xie.authorize.constant.Constants.USER_SERVICE_ID;

import com.xie.authorize.RoutContextHolder;
import com.xie.authorize.ServiceMatcher;
import com.xie.authorize.constant.Constants;
import com.xie.authorize.resource.handler.DefaultResourceHandler;
import com.xie.authorize.token.TokenHandler;
import com.xie.common.exception.XException;
import com.xie.gateway.api.AppManagerService;
import com.xie.gateway.api.authorize.AuthorInfo;
import com.xie.gateway.api.authorize.AuthorizeService;
import com.xie.gateway.api.authorize.ContextMessage;
import com.xie.gateway.api.authorize.RouteContext;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.PrematureJwtException;
import java.util.StringTokenizer;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * token处理器
 */
public abstract class AbsTokenHanlder implements TokenHandler {

    protected static final Logger logger = LoggerFactory.getLogger(AbsTokenHanlder.class);

    @Resource
    protected AuthorizeService authorizeService;

    @Resource
    private DefaultResourceHandler resourceHandler;


    @Resource
    private ServiceMatcher seviceMatcher;


    @Resource
    private AppManagerService appManagerService;

    private TokenHandler nextHandler;

    @Override
    public void setNextHandler(TokenHandler next) {
        this.nextHandler = next;
    }

    @Override
    public boolean isSupport(RouteContext ctx) {
        return seviceMatcher.match(ctx, getServiceMatchPattern());
    }

    @Override
    public String parseTokenDefault(RouteContext ctx) {
        if (logger.isDebugEnabled()) {
            logger.debug("{} 正在解释 token ", getHandlerName());
        }
        HttpServletRequest request = ctx.getRequest();
        String token = request.getHeader("token");
        return token;
    }


    @Override
    public void createToken(RouteContext ctx) {

    }

    public boolean findLoginInfo(RouteContext ctx) {
        boolean check = check(ctx);
        if (check) {
            return check;
        }
        if (abortFindLoginInfo()) {
            logger.debug("终止查找token 信息");
            return check;
        }
        if (nextHandler == null) {
            logger.warn("token handler chain is tail，but token not found");
            return false;
        }
        return nextHandler.findLoginInfo(ctx);
    }

    /**
     * 终止查找
     *
     * @return
     */
    private boolean abortFindLoginInfo() {
        RouteContext ctx = RoutContextHolder.getCurrentContext();
        Boolean abort = (Boolean) ctx.get(ABORT_FIND_TOKEN);
        if (abort != null) {
            return abort;
        }
        return false;
    }


    /**
     * 默认的实现
     *
     * @param ctx
     * @return
     */
    @Override
    public boolean check(RouteContext ctx) {
        boolean isLogin;
        String token = parseTokenDefault(ctx);
        if (StringUtils.isEmpty(token)) {
            isLogin = false;
        } else {
            ctx.put(ABORT_FIND_TOKEN, true);
            parseUserServiceIdInToken(ctx, token);
            try {
                AuthorInfo info = authorizeService.parseToken(token);
                ctx.put(USER_SERVICE_ID, info.getClientId());
                ctx.put(Constants.TOKEN_KEY, token);
                isLogin = true;
            } catch (ExpiredJwtException expireEx) {
                logger.debug("{} token 过期,尝试刷新", ctx.getServcieId());
                try {

                    String newToken = authorizeService.refreshToken(token);
                    if (newToken == null) {
                        isLogin = false;
                    } else {
                        AuthorInfo info = authorizeService.parseToken(newToken);
                        ctx.put(USER_SERVICE_ID, info.getClientId());
                        ctx.put(Constants.TOKEN_KEY, newToken);
                        ctx.put(TOKEN_CREATE_TIME, info.get(TOKEN_CREATE_TIME));
                        ctx.put(TOKEN_REFRESH_KEY, true);
                        isLogin = true;
                    }
                } catch (XException e) {
                    logger.error("刷新{} token失败:{}", ctx.getServcieId(), e.getMessage());
                    isLogin = false;
                } catch (Exception ex) {
                    logger.warn("刷新{} token 失败", ctx.getServcieId());
                    isLogin = false;
                }
            } catch (XException e) {
                logger.warn("解释{} token失败: {}", ctx.getServcieId(), e.getMessage());
                isLogin = false;
            } catch (PrematureJwtException e) {
                logger.error("解释{} token失败:{}", ctx.getServcieId(), e);
                isLogin = false;
            } catch (Exception e) {
                logger.error("解释{} token失败:{}", ctx.getServcieId(), e);
                isLogin = false;
            }
        }
        //保存处理结果
        putDefaultUnLoginInfo(ctx, isLogin);
        return isLogin;
    }

    protected void parseUserServiceIdInToken(RouteContext ctx, String token) {
        StringTokenizer st = new StringTokenizer(token, ":");
        if (st.countTokens() < 2) {
            logger.warn("当前token上没绑定所有服务id");
            return;
        }
        while (st.hasMoreTokens()) {
            String userServiceId = st.nextToken();
            ctx.put(USER_SERVICE_ID, userServiceId);
            logger.debug("当前token所属的服务id:{}", userServiceId);
            break;
        }
    }


    protected void putDefaultUnLoginInfo(RouteContext ctx, boolean isLogin) {
        if (isLogin || !isSupport(ctx)) {
            return;
        }
        ContextMessage contextMessage = new ContextMessage();
        contextMessage.setBzMessage("登录信息无效");
        ctx.put(ROUT_CONTEXT_MESSAGE, contextMessage);
    }

}
