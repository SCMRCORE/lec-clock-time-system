package com.lec.clock.controller;

import com.clockcommon.entity.Page;
import com.clockcommon.entity.Result;
import com.clockcommon.enums.SystemLog;
import com.clockcommon.utils.UserContext;
import com.lec.clock.service.ClockService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@Slf4j
@RestController
@RequestMapping("/api/clock")
@Api(tags = "打卡相关接口")
public class ClockController {
    @Autowired
    private ClockService clockService;

//    /**
//     * 测试
//     * @return
//     */
//    @GetMapping("/test1")
//    public Result test(){
//        log.info("测试连接");
//        return Result.okResult();
//    }

    /**
     * 获取所有打卡信息
     * @param page
     * @return
     */
    @PostMapping ("/list")
    public Result listAllClock(@RequestBody Page page) {
        log.info("执行接口/list,用户ID为：{}", UserContext.getUser());
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
        Long userId = UserContext.getUser();
        log.info("执行接口/clock，id为：{}", userId);
        return clockService.clock(userId);
    }


    /**
     * 根据id获取打卡信息
     * @param id
     * @return
     */
    @GetMapping("/nowClock/{id}")
    public Result getClockById(@PathVariable("id") Long id) {
        id=UserContext.getUser();
        log.info("执行接口/nowClock/{id}，id为：{}", id);
        return clockService.getClockById(id);
    }


    /**
     * 更新打卡时长
     * @param id
     * @param duration
     * @return
     */
    @PutMapping("/update/{id}")
    public Result updateDuration(@PathVariable("id") Long id, Integer duration){
        log.info("执行接口/update/{id}，id为：{}");
        return clockService.updateDuration(id, duration);
    }


    /**
     * 添加可以打卡的ip
     * @return
     * @throws UnknownHostException
     */
    @PostMapping("/addIpv4")
    public Result addIpv4() throws UnknownHostException {
        log.info("执行接口/addIpv4");
        return clockService.addIpv4();
    }

//    /**
//     * 获取可以打卡的ip
//     * @param position
//     * @return
//     */
//    @PostMapping("/position")
//    public Result getPosition(@RequestBody Position position){
//        log.info("成功接受请求");
//        log.info("a:{}",position.getA());
//        log.info("b:{}",position.getB());
//        return Result.okResult();
//    }


    /**
     * 定时器
     * @return
     * @throws UnknownHostException
     */
    @PostMapping("/clockOff")
    public Result clockOff(){
        log.info("执行接口/clockOff");
        return clockService.clockOff();
    }



//这部分是rpc专区
    /**
     * 创建用户的clock对象
     * @param userId
     * @return
     */
    @GetMapping("/create")
    public void createClock(Long userId, Integer grade) {
        log.info("执行RPC接口/create:创建用户的clock对象");
        clockService.createClockByUserId(userId, grade);
    }
}
