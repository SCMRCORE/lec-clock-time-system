package com.lec.clock.controller;

import com.clockcommon.entity.Page;
import com.clockcommon.entity.Result;
import com.clockcommon.enums.SystemLog;
import com.lec.clock.entity.pojo.Position;
import com.lec.clock.service.ClockService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@Slf4j
@RestController
@RequestMapping("/api/clock")
@Api(tags = "打卡相关接口")
public class ClockController {

    @Autowired
    private ClockService clockService;

    /**
     * 获取所有打卡信息
     * @param page
     * @return
     */
    @PostMapping ("/list")
    public Result listAllClock(@RequestBody Page page) {
        return clockService.listAllClock(page.getGrade(), page.getPageNum(), page.getPageSize());
    }

    /**
     * 上下卡
     * @return
     * @throws UnknownHostException
     */
    @PostMapping("/clock")
    @SystemLog(businessName = "上下卡")
    public Result clock() throws UnknownHostException {
        return clockService.clock();
    }


    /**
     * 根据id获取打卡信息
     * @param id
     * @return
     */
    @GetMapping("/nowClock/{id}")
    public Result getClockById(@PathVariable Long id) {
        return clockService.getClockById(id);
    }


    /**
     * 更新打卡时长
     * @param id
     * @param duration
     * @return
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("@ps.hasPermission('clock:update:duration')")
    public Result updateDuration(@PathVariable("id") Long id, Integer duration){
        return clockService.updateDuration(id, duration);
    }


    /**
     * 添加可以打卡的ip
     * @return
     * @throws UnknownHostException
     */
    @PostMapping("/addIpv4")
    public Result addIpv4() throws UnknownHostException {
        return clockService.addIpv4();
    }

    /**
     * 获取可以打卡的ip
     * @param position
     * @return
     */
    @PostMapping("/position")
    public Result getPosition(@RequestBody Position position){
        log.info("成功接受请求");
        log.info("a:{}",position.getA());
        log.info("b:{}",position.getB());
        return Result.okResult();
    }

//    @GetMapping("/day")
//    public Result getDay() {
//        return dailyHistoryService.getDay();
//    }

}
