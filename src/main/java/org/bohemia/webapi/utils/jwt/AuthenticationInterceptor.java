package org.bohemia.webapi.utils.jwt;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.bohemia.webapi.entity.User;
import org.bohemia.webapi.service.UserServiceApi;
import org.bohemia.webapi.utils.annotation.LoginToken;
import org.bohemia.webapi.utils.annotation.PassToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sun.nio.cs.ext.DoubleByte;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 拦截器去获取token并验证token
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserServiceApi userServiceApi;

    public static long EXPIRE_TIME = 60*60*1000;
    public static String TOKEN_SECRET = "BohemiaToken";
    public static String ISSUER = "Bohemia";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");//从头部取出token
        // 如果不是映射到方法直接通过
        if(! (handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if(method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //TODO 以下的return false均没有做错误处理
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        if (method.isAnnotationPresent(LoginToken.class)) {
            LoginToken userLoginToken = method.getAnnotation(LoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    json.put("success","false");
                    json.put("msg","无token,请重新登录");
                    json.put("code","50000");
                    response.getWriter().append(json.toJSONString());
                    return false;
                }
                String username = null;
                try {
                    username = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    json.put("success","false");
                    json.put("msg","token失效,请重新登录");
                    json.put("code","50000");
                    response.getWriter().append(json.toJSONString());
                    return false;
                }
                System.out.println("username:  "+ username);
                User user = userServiceApi.findUserByUsername(username);
                if (user == null) {
                    json.put("success","false");
                    json.put("msg","用户不存在，请重新登录");
                    json.put("code","50000");
                    response.getWriter().append(json.toJSONString());
                    return false;
                }
                // 验证 token
                if(!TokenUtil.verify(token)){
                    json.put("success","false");
                    json.put("msg","token失效,请重新登录");
                    json.put("code","50000");
                    response.getWriter().append(json.toJSONString());
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
