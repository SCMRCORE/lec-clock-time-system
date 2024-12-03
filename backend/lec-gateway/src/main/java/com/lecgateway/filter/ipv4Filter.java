package com.lecgateway.filter;

import cn.hutool.core.util.StrUtil;

import com.lecgateway.enums.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ipv4Filter implements GlobalFilter, Ordered {

    @Resource
    RedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 请求
        ServerHttpRequest request = exchange.getRequest();
        // 获取 IP 地址
        String ip1 = getIP(request);
        int position=0;//:的位置
        for(int i=0; i<=ip1.length()-1; i++){
            if(ip1.charAt(i)==':'){
                 position=i;
                 break;
             }
        }
        //有时获取到的字符串是ip+端口，裁切端口让字符串为ipv4格式
        String ip = ip1.substring(0,position);
        log.info("========= 请求的IP地址： " + ip);
        //解析ipv4
        String ipv4Address = null;
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            if (inetAddress != null) {
                ipv4Address = inetAddress.getHostAddress();
                log.info("成功解析Ipv4:{}", ipv4Address);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //获取用户ID
        try {
            String userInfo =  request.getHeaders().get("userInfo").get(0);
            Long userId = Long.valueOf(userInfo);
            log.info("ip过滤器获取到用户id:{}", userId);
            redisTemplate.opsForValue().set(SystemConstant.REDIS_CLOCK_IPV4+userId, ipv4Address,2,TimeUnit.MINUTES);
            return chain.filter(exchange);
        }catch (Exception e){
//            e.printStackTrace();
            log.info("未获取到userInfo，有可能是无需登录的路径，直接放行");
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }

    // 多次反向代理后会有多个ip值 的分割符
    private final static String IP_UTILS_FLAG = ",";
    // 未知IP
    private final static String UNKNOWN = "unknown";
    // 本地 IP
    private final static String LOCALHOST_IP = "0:0:0:0:0:0:0:1";
    private final static String LOCALHOST_IP1 = "127.0.0.1";

    private static String getIP(ServerHttpRequest request){
        //获取header
        HttpHeaders headers = request.getHeaders();
//        log.info("========= 请求的headers： " + headers);
        // 根据 HttpHeaders 获取 请求 IP地址
        String ip = request.getHeaders().getFirst("X-Forwarded-For");
//        log.info("========= 请求的第一个IP地址： " + ip);
        if (ip==null || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("x-forwarded-for");
//            log.info("========= 请求的第二个IP地址： " + ip);
            if (ip != null && ip.length() != 0 && !UNKNOWN.equalsIgnoreCase(ip)) {
                // 多次反向代理后会有多个ip值，第一个ip才是真实ip
                if (ip.contains(IP_UTILS_FLAG)) {
                    ip = ip.split(IP_UTILS_FLAG)[0];
                }
            }
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().toString().substring(1);
        }
        log.info("========= 请求的代理或者路由IP地址： " + ip);
        //兼容k8s集群获取ip
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
            if (LOCALHOST_IP1.equalsIgnoreCase(ip) || LOCALHOST_IP.equalsIgnoreCase(ip)) {
                //根据网卡取本机配置的IP
                InetAddress iNet = null;
                try {
                    iNet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    log.error("getClientIp error: ", e);
                }
//                log.info("========= 请求的本地IP地址： " + iNet.getHostAddress());
                ip = iNet.getHostAddress();
            }
        }
        return ip;
    }

}
