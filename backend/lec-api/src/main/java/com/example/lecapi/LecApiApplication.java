package com.example.lecapi;

import com.example.lecapi.config.DefaultFeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.spring.web.plugins.DefaultConfiguration;

@SpringBootApplication
public class LecApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LecApiApplication.class, args);
    }

}
