package com.clockcommon.config;

import com.clockcommon.intercepters.UserInfoInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnClass(DispatcherServlet.class)
/*
	这个注解是SpringMVC里的用于限制配置文件的注解，DispatcherServlet.class是SpringMVC里的关键类，只要是微服务就有SpringMVC就有DispatcherServlet.class。
	所以我们将限制条件改为DispatcherServlet.class就可以保证该配置出现在微服务而不是网关
*/
public class UserInfoMvcConfig implements WebMvcConfigurer {
    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInfoInterceptor());//这里就不再添加路径了，默认全部拦截
    }
}
