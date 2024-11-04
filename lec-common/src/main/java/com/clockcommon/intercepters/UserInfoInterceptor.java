package com.clockcommon.intercepters;

import cn.hutool.core.util.StrUtil;
import com.clockcommon.utils.UserContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取用户ID
        String userInfo = request.getHeader("userInfo");
        //2.判断获取情况，然后存入ThreadLocal
        if(StrUtil.isBlank(userInfo)){
            UserContext.setUser(Long.valueOf(userInfo));
            //UserContext是手写的添加ThreadLocal的类,存入ThreadLocal
        }
        //3.放行
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清理用户
        UserContext.removeUser();
    }
}
