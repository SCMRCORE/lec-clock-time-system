package com.example.lecapi.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("clock-service")
public interface ClockClient {
    @GetMapping("/api/clock/create")
    void createClock(@RequestParam("userId") Long userId,@RequestParam("grade") Integer grade, @RequestParam("username") String username, @RequestParam("nickname") String nickname);
}
