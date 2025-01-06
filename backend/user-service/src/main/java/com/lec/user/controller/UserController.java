package com.lec.user.controller;

import com.clockcommon.entity.Result;
import com.clockcommon.enums.SystemConstant;
import com.clockcommon.enums.SystemLog;
import com.clockcommon.utils.UserContext;
import com.lec.user.entity.dto.LoginUserDto;
import com.lec.user.entity.dto.RegisterUserDto;
import com.lec.user.entity.dto.UpdateUserDto;
import com.lec.user.entity.dto.UserDto;
import com.lec.user.entity.pojo.User;
import com.lec.user.service.DailyHistoryService;
import com.lec.user.service.UserService;
import com.lec.user.utils.MinioUtils;
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
import java.util.List;


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
        log.info("执行接口/login用户登录:{}",loginUserDto.getUsername());
        return userService.login(loginUserDto);
    }


    /**
     * 注册
     * @param registerUserDto
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody RegisterUserDto registerUserDto) {
        log.info("执行接口/register用户注册:{}",registerUserDto.getUsername());
        return userService.register(registerUserDto);
    }


    /**
     * 注册审核
     * @param choose
     * @return
     */
    @GetMapping("/register/audit")
    public Result register(String choose) {
        log.info("执行接口/register/audit注册审核:{}", choose);
        return userService.registerAudit(choose);
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
        log.info("执行接口/register/sendCode发送验证码:{}",email);
        return userService.sendCode(email);
    }

    /**
     * 登出
     * @return
     */
    @PostMapping("/logout")
    public Result logout(){
        log.info("执行接口/logout登出用户:{}", UserContext.getUser());
        return userService.logout();
    }


    /**
     * 获得个人信息
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public Result getUserInfoById(@PathVariable Long id){
        id=UserContext.getUser();
        log.info("执行接口/info/{id}获取个人信息:{}", id);
        return userService.getUserInfoById( id);
    }


    /**
     * 更新用户信息
     * @param updateUserDto
     * @return
     */
    @PutMapping("/info/update")
    public Result updateUserInfo(@RequestBody UpdateUserDto updateUserDto){
        log.info("执行接口/info/update更新用户信息:{}", updateUserDto);
        return userService.updateUserInfo(updateUserDto);
    }


    /**
     * 上传头像
     * @param image
     * @return
     */
    @PutMapping("/upload")
    public Result uploadAva(MultipartFile image){
        log.info("执行接口/upload上传头像Id：{}", image);
        String url = userService.uploadImage(image);
        return Result.okResult(url);
    }


//    @GetMapping("/test")
//    public Result uploads(){
//        Integer week = (Integer) redisTemplate.opsForValue().get(SystemConstant.REDIS_WEEK);
//        return Result.okResult(week);
//    }

    /**
     * 查询用户登录状态是否过期
     * @return
     */
    @GetMapping("/isDead")
    public Result isDead(){
        log.info("执行接口/isDead:查询用户是否过期");
        return userService.isDead();
    }


    /**
     * 获取用户列表
     * @param userDto
     * @return
     */
    @PostMapping("/getUsersByGrade")
    public Result getUsersByGrade(@RequestBody UserDto userDto){
        log.info("执行接口/getUsersByGrade:获取用户列表");
        return userService.getUserByGrade(userDto);
    }


    /**  这个是从clock移植到user的接口
     * 查看用户每日打卡时长
     * @return
     */
    @GetMapping("/day")
    public Result getDay() {
        log.info("执行接口/day:查看用户每日打卡时长");
        return dailyHistoryService.getDay();
    }


    @PostMapping("/weekOff")
    public Result dailyOff(){
        log.info("执行接口/weekOff:用户每周打卡清空");
        return dailyHistoryService.weekOff();
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
        log.info("执行RPC接口/calculateClock计算本日本次打卡时长:本次新增时长：{},星期数：{},用户id为{}",addTodayTime,day, UserContext.getUser());
        dailyHistoryService.setDaiyTime(addTodayTime,day);
    }

    /**
     * 查询未打满的用户
     * @param ids
     * @return
     */
    @GetMapping("/getUsers")//(废弃中)
    public List<User> getUsers(List<Long> ids) {
        log.info("执行RPC接口/getUsers查询未打满的用户:{}",ids);
        return userService.getUsers(ids);
    }


}
