package com.lec.user.service.impl;


import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clockcommon.entity.Result;
import com.clockcommon.enums.AppHttpCodeEnum;
import com.clockcommon.enums.SystemConstant;
import com.clockcommon.utils.BeanCopyUtils;
import com.lec.user.entity.dto.LoginUserDto;
import com.lec.user.entity.dto.RegisterUserDto;
import com.lec.user.entity.dto.UpdateUserDto;
import com.lec.user.entity.dto.UserDto;
import com.lec.user.entity.pojo.LoginUser;
import com.lec.user.entity.pojo.User;
import com.lec.user.entity.vo.LoginUserVo;
import com.lec.user.entity.vo.UserInfoVo;
import com.lec.user.config.JwtProperties;
import com.lec.user.mapper.DailyHistoryMapper;
import com.lec.user.mapper.UserMapper;
import com.lec.user.service.UserService;
import com.lec.user.utils.JwtTool;
import com.lec.user.utils.RedisUtil;
import com.lec.user.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2023-03-14 15:56:55
 */
@Slf4j
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    @Qualifier("RedisTemplate")
    RedisTemplate redisTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSenderImpl mailSender;

//    @Autowired
//    private ClockService clockService;
//
//    @Autowired
//    private OssUploadService ossUploadService;

//    SnowflakeIdWorker snowflakeIdWorkers=new SnowflakeIdWorker(0);

    @Autowired
    DailyHistoryMapper dailyHistoryMapper;

//    @Autowired
//    CardServiceImpl cardService;

    @Resource
    UserMapper userMapper;

    @Resource
    JwtTool jwtTool;
    @Resource
    JwtProperties jwtProperties;

    @Override
    public Result login(LoginUserDto loginUserDto) {

        /*
        UsernamePasswordAuthenticationToken
        它是一种Authentication实现，继承AbstractAuthenticationToken抽象类，旨在简单地表示用户名和密码。
        principal和credentials属性应设置为通过其toString方法提供相应属性的Object，最简单的就是String类型。
         * AuthenticatedPrincipal：一旦Authentication请求已通过AuthenticationManager.authenticate(Authentication)方法成功验证，
         * 则表示经过身份验证的Principal（实体）。实现者通常提供他们自己的Principal表示，其中通常包含描述Principal实体的信息，
         * 例如名字、地址、电子邮件、电话以及ID等，此接口允许实现者公开其自定义的特定属性以通用方式表示Principal。
         * Principal：该接口表示主体的抽象概念，可用于表示任何实体，是java.security包下的接口，并非由Spring Security提供。
         */
        log.info("用户登录信息:{}",loginUserDto);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUserDto.getUsername(),loginUserDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //校验用户密码
        if(Objects.isNull(authenticate)){
            log.info("用户名或密码错误");
            return Result.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
        }
        //获取用户实体并且将信息存入redis
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        User user = loginUser.getUser();
        redisTemplate.opsForValue().set(SystemConstant.REDIS_LOGIN_USER + user.getId(), loginUser, 60 * 24*3, TimeUnit.MINUTES);
        log.info("user:{}",SystemConstant.REDIS_LOGIN_USER + user.getId());
        //生成token
        Long userId = user.getId();
        String token = jwtTool.createToken(userId, jwtProperties.getTokenTTL());
        log.info("创建一个新的token:{}",token);
        //封装返回给vo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        LoginUserVo loginUserVo = new LoginUserVo(token, userInfoVo);
        log.info("登录成功，返回登录信息:{}",loginUserDto);
        return Result.okResult(loginUserVo);
    }





    @Override
    public Result register(RegisterUserDto registerUserDto) {
        Integer week = (Integer) redisTemplate.opsForValue().get(SystemConstant.REDIS_WEEK);

        //匹配验证码（通过email作为key）
        int code= (int) redisTemplate.opsForValue().get(registerUserDto.getEmail());
        if(code!=registerUserDto.getCode()){
            return Result.errorResult(AppHttpCodeEnum.CODE_FALSE);
        }
        //匹配验证码通过，将redis内的验证码删除
        redisTemplate.delete(registerUserDto.getEmail());


        String userName = registerUserDto.getUsername();
        if(StrUtil.isBlank(userName)) {
            return Result.errorResult(AppHttpCodeEnum.REQUIRE_USERNAME);
        }

        //获取数据库中是否有相同用户名
        User one = lambdaQuery().eq(User::getUsername, userName).one();
        if(one != null) {
            return Result.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
        }
        //加密用户密码
        registerUserDto.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

        //将注册信息注入到对应的user实体类里
        User user = BeanCopyUtils.copyBean(registerUserDto, User.class);

        //TODO 需要rpc
//        //为用户新建一个打卡类
//        Clock clock = new Clock(LocalDateTime.now(), SystemConstant.CLOCKED_STATUS, 0, SystemConstant.FIRST_GRADE_CLOCK_TARGET);
//        if(user.getGrade() == 2) {
//            clock.setTargetDuration(SystemConstant.SECOND_GRADE_CLOCK_TARGET);
//        }
//
//        user.setAvatar(SystemConstant.default_URL);
//        user.setSignature(SystemConstant.signature);
//        user.setId(snowflakeIdWorkers.nextId());
//        clock.setId(user.getId());
//
//        //为用户新建一个每日打卡类
//        DailyHistory dailyHistory=new DailyHistory(user.getId(),week,0,0,0,0,0,0,0);
//        //存入数据库
//
//        Card card=new Card();
//        card.setId(user.getId());
//        cardService.updateById(card);
//        dailyHistoryMapper.insert(dailyHistory);
//        userService.save(user);
//        clockService.save(clock);
        return Result.okResult();
    }

    @Override
    public Result sendCode(String email) {
        SimpleMailMessage smm = new SimpleMailMessage();		//创建邮件对象
        try {
            smm.setSubject("注册验证码");	//设置邮件主题
            int code= RandomUtil.randomInt(1000, 9999);
            smm.setText("您的验证码为：" + code);		//编辑邮件内容
            smm.setTo(email);	//设置邮件发送地址
            smm.setFrom(from);	//设置邮件发送源址
            redisTemplate.opsForValue().set(email,code);
            mailSender.send(smm); //发送邮件
            return Result.okResult();
        }catch (Exception e){
            e.printStackTrace();
            return Result.errorResult(AppHttpCodeEnum.CODE_SEND_ERROR);
        }
    }

    @Override
    public Result logout() {
        Long id = SecurityUtils.getUserId();
        Boolean flag = redisTemplate.delete(SystemConstant.REDIS_LOGIN_USER + id);
        if(BooleanUtil.isTrue(flag)) {
            return Result.okResult();
        }
        return Result.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public Result getUserInfoById(Long id) {
        User user = userMapper.getById(id);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return Result.okResult(userInfoVo);
    }

    @Override
    public Result updateUserInfo(UpdateUserDto updateUserDto) {
        updateUserDto.setId(SecurityUtils.getUserId());
        User user = BeanCopyUtils.copyBean(updateUserDto, User.class);
        boolean flag = updateById(user);
        if(!flag) return Result.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        return Result.okResult();
    }

    //上传头像
    @Override
    public Result uploadAva(MultipartFile file) {
       //TODO OSS待解决
//        try {
//            Long id=SecurityUtils.getUserId();
//            User user=userService.getById(id);
//            Result result=ossUploadService.uploadImg(file);
//            user.setAvatar((String) result.getData());
//            userService.updateById(user);
//            return  result;
//        } catch (IOException e) {
//            return Result.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
//        }
        return Result.okResult();
    }

    @Override
    public Result getUserByGrade(UserDto userDto) {
        int grade=userDto.getGrade();
        List<User> list=lambdaQuery().eq(User::getGrade,grade).list();
        List<UserDto> userDtoList=BeanCopyUtils.copyBeanList(list, UserDto.class);
        return Result.okResult(userDtoList);
    }

    @Override
    public Result isDead() {
        Long id= SecurityUtils.getUserId();
        LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get(SystemConstant.REDIS_LOGIN_USER + id);
        boolean isDead= ObjectUtil.isNull(loginUser);
        return Result.okResult(!isDead);
    }
}

