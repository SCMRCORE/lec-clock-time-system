package com.example.lecapi.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("user-service")
public interface UserClient {
    @GetMapping("/api/user/calculateClock")
    void dailyclock(@RequestParam("addTodayTime") int addTodayTime,@RequestParam("day") String day);

}
