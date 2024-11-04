package com.example.lecapi.config;


import com.clockcommon.utils.UserContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@Slf4j
public class DefaultFeignConfig {
    @Bean
    public RequestInterceptor userInfoRequestInterceptor(){//实现这个接口
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                //网关发送请求把userId放进ThreadLocal了,而这个拦截器在OpenFeign发送前拦截，所以可以获取
                Long userId = UserContext.getUser();
                if( userId != null) {
                    requestTemplate.header("user-info", userId.toString());
                }
            }
        };
    }

}
