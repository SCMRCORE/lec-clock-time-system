package com.lecgateway.filter;

import com.lecgateway.config.AuthProperties;
import com.lecgateway.exceptions.UnauthorizedException;
import com.lecgateway.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LecGlobalFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;//记录不需要拦截的路径

    private final JwtTool jwtTool;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();//判断路径是否一直的工具

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取request
        ServerHttpRequest request = exchange.getRequest();
        //2.判断是否需要做登录拦截
        if(isExclude(request.getPath().toString())){
            //放行
            log.info("无需校验：放行");
            return chain.filter(exchange);
        }
        //3.获取token
        String token = null;
        //TODO 一次请求会有多个请求头,这个需要和前端约定好，前端请求的时候，把token放到请求头中，key为token
        List<String> headers=request.getHeaders().get("token");
        if(headers !=null && !headers.isEmpty()){
            token = headers.get(0);
        }
        //4.校验并解析token
        Long userId = null;
//        userId=jwtTool.parseToken(token);
//        log.info("第一次解析出userID为:{}",userId);
        log.info("LecGlobal:token为:{}",token);
        try{
            userId=jwtTool.parseToken(token);
            log.info("LecGlobal:token解析出userID为:{}",userId);
            //5.传递用户信息
            String userInfo = userId.toString();//为了后面的传入userId
            ServerWebExchange swe = exchange.mutate()
                    .request(builder -> builder.header("userInfo", userInfo))
                    //这里也是和其他后端开发者约好了关键词
                    .build();//获得新的exchange
            //6.放行
            log.info("放行");
            return chain.filter(swe);
        }catch (UnauthorizedException e){
            //因为报错了嘛，所以返回一个response
            //拦截的状态码设置为401，手写的UnauthorizedException里有的
            log.info("token校验失败,网关已拦截返回401");
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);//HttpStatus是框架自带
            return response.setComplete();
        }
//        //5.传递用户信息
//        String userInfo = userId.toString();//为了后面的传入userId
//        ServerWebExchange swe = exchange.mutate()
//                .request(builder -> builder.header("userInfo", userInfo))
//                //这里也是和其他后端开发者约好了关键词
//                .build();//获得新的exchange
//        //6.放行
//        log.info("放行");
//        return chain.filter(swe);
    }

    public boolean isExclude(String path){
        for(String excludePath : authProperties.getExcludePaths()){//不需要拦截的路径全在authProperties中配置里
            if (antPathMatcher.match(excludePath, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 1;//过滤器执行顺序，值越小，优先级越高
    }
}
