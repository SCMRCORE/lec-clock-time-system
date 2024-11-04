package com.lec.clock;

import com.example.lecapi.config.DefaultFeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.lecapi.clients", defaultConfiguration = DefaultFeignConfig.class)
public class ClockApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClockApplication.class, args);
    }

}
