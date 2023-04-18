package com.yiling.interceptor;

import com.alibaba.fastjson2.JSONObject;
import com.yiling.domain.User;
import com.yiling.utils.TokenUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author MisakiMikoto
 * @Date 2023/3/31 22:06
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //跨域请求会首先发送一个option请求，直接返回正常状态并拦截
        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("token");
        TokenUtils tokenUtils = new TokenUtils();
        if(token != null){
            String userId = tokenUtils.verify(token);//这里调用自己的Token检验
            if(userId != null){
//                System.out.println("通过拦截器");
                request.setAttribute("userId",userId);
                return true;
            }
        }
        response.setContentType("application/json; charset=utf-8");
        try{
            JSONObject json = new JSONObject();
            json.put("msg","token verify fail");
            json.put("code",401);
            response.getWriter().append(json.toString());
//            System.out.println("认证失败，未通过拦截器");
        }catch (Exception e){
            return false;
        }

        return false;
    }
}
