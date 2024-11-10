package com.lec.user.controller;

import com.clockcommon.entity.Result;
import com.clockcommon.enums.SystemConstant;
import com.clockcommon.enums.SystemLog;
import com.clockcommon.utils.UserContext;
import com.lec.user.entity.dto.LoginUserDto;
import com.lec.user.entity.dto.RegisterUserDto;
import com.lec.user.entity.dto.UpdateUserDto;
import com.lec.user.entity.dto.UserDto;
import com.lec.user.service.DailyHistoryService;
import com.lec.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


@RestController
@RequestMapping("/api/user")
@Api(tags = "用户相关接口")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    @Qualifier("RedisTemplate")
    RedisTemplate redisTemplate;
    @Resource
    DailyHistoryService dailyHistoryService;


    /**
     * 测试
     * @return
     */
    @GetMapping("/test1")
    public Result test(){
        log.info("测试连接");
        return Result.okResult();
    }


    /**
     * 登录
     * @param loginUserDto
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("登录")
    public Result login(@RequestBody LoginUserDto loginUserDto){
        log.info("用户登录，用户名：{}",loginUserDto.getUsername());
        return userService.login(loginUserDto);
    }


    /**
     * 注册
     * @param registerUserDto
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody RegisterUserDto registerUserDto) {
        return userService.register(registerUserDto);
    }


    /**
     * 发送验证码
     * @param email
     * @return
     */
    @PostMapping("/register/sendCode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名"),
    })
    public Result sendCode( String email) {
        return userService.sendCode(email);
    }

    /**
     * 登出
     * @return
     */
    @PostMapping("/logout")
    public Result logout(){
        return userService.logout();
    }


    /**
     * 获得个人信息
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public Result getUserInfoById(@PathVariable Long id){
        return userService.getUserInfoById( id);
    }


    /**
     * 更新用户信息
     * @param updateUserDto
     * @return
     */
    @PutMapping("/info/update")
    public Result updateUserInfo(@RequestBody UpdateUserDto updateUserDto){
        return userService.updateUserInfo(updateUserDto);
    }


    /**
     * 上传头像
     * @param image
     * @return
     */
    @PutMapping("/upload")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "image", value = "MultipartFile"),
    })
    public Result uploadAva(MultipartFile image){
        return userService.uploadAva(image);
    }

    @GetMapping("/test")
    public Result uploads(){
        Integer week = (Integer) redisTemplate.opsForValue().get(SystemConstant.REDIS_WEEK);
        return Result.okResult(week);
    }

    /**
     * 查询用户登录状态是否过期
     * @return
     */
    @GetMapping("/isDead")
    public Result isDead(){
        return userService.isDead();
    }


    /**
     * 获取用户列表
     * @param userDto
     * @return
     */
    @PostMapping("/getUsersByGrade")
    public Result getUsersByGrade(@RequestBody UserDto userDto){
        return userService.getUserByGrade(userDto);
    }

    /**
     * 查看用户每日打卡时长
     * @return
     */
    @GetMapping("/day")
    public Result getDay() {
        return dailyHistoryService.getDay();
    }


    //下面全是rpc

    /**
     * 计算今日(星期)打卡时长
     * @param addTodayTime
     * @param day
     * @return
     */
    @GetMapping("/calculateClock")
    public void dailyclock(int addTodayTime, String day) {
        log.info("计算本日本次打卡时长,本次新增时长：{},星期数：{}",addTodayTime,day);
        log.info("用户id为{}", UserContext.getUser());
        dailyHistoryService.setDaiyTime(addTodayTime,day);
    }

}
