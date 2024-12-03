package com.lec.clock.controller;


import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.clockcommon.entity.Result;
import com.lec.clock.service.ClockHistoryService;
import com.lec.clock.utils.PageResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/clockHistory")
@Api(tags = "打卡历史相关接口")
public class ClockHistoryController {

    @Autowired
    ClockHistoryService clockHistoryService;

    @GetMapping("/list")
    public PageResult list(Integer pageNum, Integer pageSize){
        log.info("执行接口/list:获取打卡未满的同学");
        return clockHistoryService.list(pageNum, pageSize);
    }



//    @GetMapping("/{id}")
//    public Result getClockHistoryById(@PathVariable("id") Long id, Integer pageNum, Integer pageSize) {
//        return clockHistoryService.getClockHistoryById(id, pageNum, pageSize);
//    }
}
