package com.lec.clock.controller;


import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.clockcommon.entity.Result;
import com.lec.clock.service.ClockHistoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clockHistory")
@Api(tags = "打卡历史相关接口")
public class ClockHistoryController {

    @Autowired
    ClockHistoryService clockHistoryService;

    @GetMapping("/list")
    public Result list(Integer week, Integer grade, Integer pageNum, Integer pageSize){
        return clockHistoryService.list(week, grade, pageNum, pageSize);
    }

    @GetMapping("/{id}")
    public Result getClockHistoryById(@PathVariable("id") Long id, Integer pageNum, Integer pageSize) {
        return clockHistoryService.getClockHistoryById(id, pageNum, pageSize);
    }
}
