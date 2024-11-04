package com.lec.user.config;

import com.lec.user.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.KeyPair;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {		//将验证管理对象注入bean
        return super.authenticationManager();
    }

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)		//设置session策略，不使用session创建http对象
                .and()		//使用and()返回一个HttpSecurity对象继续配置
                .authorizeRequests()  //返回对请求的配置对象
                .anyRequest().permitAll();
//                .antMatchers("/user/login", "/user/register", "/user/register/sendCode").anonymous()	//设置匿名访问路径，已登录不能访问，未登录能访问
//                .antMatchers("/swagger-ui.html","/swagger-resources/**", "/webjars/**", "/v2/**", "/api/**", "/csrf").permitAll()	//设置都能通行的路径
//                .anyRequest().authenticated();	//anyRequest()其他任意请求对象，authenticated()已认证的都能访问

        http.logout().disable();

        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //允许跨域
        http.cors();
    }

    //test
    @Bean
    public KeyPair keyPair(JwtProperties properties){
        // 获取秘钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory =
                new KeyStoreKeyFactory(
                        properties.getLocation(),
                        properties.getPassword().toCharArray());
        //读取钥匙对
        return keyStoreKeyFactory.getKeyPair(
                properties.getAlias(),
                properties.getPassword().toCharArray());
    }
}
