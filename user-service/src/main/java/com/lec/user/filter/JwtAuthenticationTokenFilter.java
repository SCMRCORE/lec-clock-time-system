package com.lec.user.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.clockcommon.entity.Result;
import com.clockcommon.enums.AppHttpCodeEnum;
import com.clockcommon.enums.SystemConstant;
import com.clockcommon.utils.JWTUtils;
import com.lec.user.entity.pojo.LoginUser;
import com.lec.user.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@WebFilter(urlPatterns = "/api/*")
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("RedisTemplate")
    RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("开始拦截url:{},token:{}",request.getRequestURI(),request.getHeader("token"));
//        if(request.getRequestURI().contains("home")){
//            response.sendRedirect("/Lec/index.html");
//
//        }
//
//        if(request.getRequestURI().contains(".css")||request.getRequestURI().contains(".js")||
//                request.getRequestURI().contains(".html")||request.getRequestURI().contains(".png")
//                ||request.getRequestURI().contains("/login")||request.getRequestURI().contains(".ico")){
//            log.info("放行");
//            filterChain.doFilter(request,response);
//            return;
//        }


        String ipv4Address = null;

        try {
            // 获取客户端的IP地址
            String ipAddress = request.getHeader("X-Forwarded-For");

            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("X-Real-IP");
            }

            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }

            // 解析IPv4地址
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            if (inetAddress instanceof Inet4Address) {
                ipv4Address = inetAddress.getHostAddress();
            }
        } catch (UnknownHostException e) {
            // 处理异常
        }

        log.info("ip:{}",ipv4Address);

        String token = request.getHeader("token");
        if(StrUtil.isBlank(token)) {

            filterChain.doFilter(request, response);
            return;
        }
        Long userId = null;
        try {
            userId = JWTUtils.getSubject(token);
            redisTemplate.opsForValue().set(SystemConstant.REDIS_CLOCK_IPV4+userId,ipv4Address,10,TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            WebUtils.renderString(response, JSON.toJSONString(Result.errorResult(AppHttpCodeEnum.NEED_LOGIN)));
            return;
        }

        LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get(SystemConstant.REDIS_LOGIN_USER + userId);

        if(loginUser == null){
            WebUtils.renderString(response, JSON.toJSONString(Result.errorResult(AppHttpCodeEnum.NEED_LOGIN)));
            return;
        }

        redisTemplate.expire(SystemConstant.REDIS_LOGIN_USER + userId, 1, TimeUnit.DAYS );
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
