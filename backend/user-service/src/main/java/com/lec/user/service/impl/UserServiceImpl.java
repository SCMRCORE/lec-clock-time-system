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
import com.clockcommon.utils.UserContext;
import com.example.lecapi.clients.ClockClient;
import com.lec.user.entity.dto.LoginUserDto;
import com.lec.user.entity.dto.RegisterUserDto;
import com.lec.user.entity.dto.UpdateUserDto;
import com.lec.user.entity.dto.UserDto;
import com.lec.user.entity.pojo.DailyHistory;
import com.lec.user.entity.pojo.User;
import com.lec.user.entity.vo.LoginUserVo;
import com.lec.user.entity.vo.UserInfoVo;
import com.lec.user.config.JwtProperties;
import com.lec.user.mapper.DailyHistoryMapper;
import com.lec.user.mapper.UserMapper;
import com.lec.user.service.UserService;
import com.lec.user.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
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

    @Resource
    ClockClient clockClient;

    SnowflakeIdWorker snowflakeIdWorkers=new SnowflakeIdWorker(0);

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

//    @Resource
//    private AliOSSUtils aliOSSUtils;

    @Resource
    MinioUtils minioUtils;

    @Autowired
    DailyHistoryMapper dailyHistoryMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    JwtTool jwtTool;
    @Resource
    JwtProperties jwtProperties;


    @Override
    public Result login(LoginUserDto loginUserDto) {
        //TODO 待解决登录超时
        log.info("用户登录信息:{}", loginUserDto);
        String username = loginUserDto.getUsername();
        String password = loginUserDto.getPassword();
        User user;
        try {
            user = userMapper.getUserByUsername(username);
            if (!passwordEncoder.matches(password, user.getPassword())) {
                log.info("用户名或密码错误");
                return Result.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
            }
        }catch (Exception e){
            return Result.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
        }
            //获取用户实体并且将信息存入redis
            redisTemplate.opsForValue().set(SystemConstant.REDIS_LOGIN_USER + user.getId(), user, 60 * 24 * 3, TimeUnit.MINUTES);
            log.info("user:{}", SystemConstant.REDIS_LOGIN_USER + user.getId());
            //生成token
            Long userId = user.getId();
            log.info("获取的userId为：{}", userId);
            String token = jwtTool.createToken(userId, jwtProperties.getTokenTTL());
            log.info("创建一个新的token:{}", token);
            //封装返回给vo
            UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
            LoginUserVo loginUserVo = new LoginUserVo(token, userInfoVo);
            return Result.okResult(loginUserVo);
    }


    @Override
    public Result register(RegisterUserDto registerUserDto) {
        Integer week = (Integer) redisTemplate.opsForValue().get(SystemConstant.REDIS_WEEK);

        //匹配验证码
        try {
            //匹配验证码（通过email作为key）
            int code = (int) redisTemplate.opsForValue().get(registerUserDto.getEmail());
            if (code != registerUserDto.getCode()) {
                return Result.errorResult(AppHttpCodeEnum.CODE_FALSE);
            }
            //匹配验证码通过，将redis内的验证码删除
            redisTemplate.delete(registerUserDto.getEmail());
        }catch (Exception e) {
            return Result.errorResult(AppHttpCodeEnum.CODE_FALSE);
        }

        //判断手机号


        //判断用户名
        String userName = registerUserDto.getUsername();
        if(StrUtil.isBlank(userName)) {
            return Result.errorResult(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        User one = lambdaQuery().eq(User::getUsername, userName).one();
        if(one != null) {
            return Result.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
        }
        //加密用户密码
        registerUserDto.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));


        //将注册信息注入到对应的user实体类里
        User user = BeanCopyUtils.copyBean(registerUserDto, User.class);

        user.setAvatar(SystemConstant.default_URL);
        user.setSignature(SystemConstant.signature);
        user.setId(snowflakeIdWorkers.nextId());

        //为用户新建一个每日打卡类
        DailyHistory dailyHistory=new DailyHistory(user.getId(),week,0,0,0,0,0,0,0);
        //存入数据库

        dailyHistoryMapper.insert(dailyHistory);
        userMapper.insert(user);
        clockClient.createClock(user.getId(), user.getGrade());
        //        Card card=new Card();
        //        card.setId(user.getId());
        //        cardService.updateById(card);
        return Result.okResult();
    }

    @Override
    public Result sendCode(String email) {
        SimpleMailMessage smm = new SimpleMailMessage();		//创建邮件对象
        if(userMapper.hasEmail(email)!=null){
            log.info("注册邮箱已存在");
            return Result.errorResult(AppHttpCodeEnum.EMAIL_EXIST);
        }
        try {
            smm.setSubject("注册验证码");	//设置邮件主题
            int code= RandomUtil.randomInt(1000, 9999);
            smm.setText("您的验证码为：" + code);		//编辑邮件内容
            smm.setTo(email);	//设置邮件发送地址
            smm.setFrom(from);	//设置邮件发送源址
            redisTemplate.opsForValue().set(email,code);
            mailSender.send(smm); //发送邮件
            log.info("已发送邮件");
            return Result.okResult();
        }catch (Exception e){
            e.printStackTrace();
            return Result.errorResult(AppHttpCodeEnum.CODE_SEND_ERROR);
        }
    }

    @Override
    public Result logout() {
        Long id = UserContext.getUser();
        Boolean flag = redisTemplate.delete(SystemConstant.REDIS_LOGIN_USER + id);
        if(BooleanUtil.isTrue(flag)) {
            log.info("成功从redis移除用户：{}", id);
            return Result.okResult();
        }
        return Result.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public Result getUserInfoById(Long id) {
        User user = userMapper.getById(id);
        log.info("用户id为：{}，查询到的用户信息为：{}", id, user);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return Result.okResult(userInfoVo);
    }

    @Override
    public Result updateUserInfo(UpdateUserDto updateUserDto) {
        //TODO 更改用户信息还得更改其他表
        log.info("需要改成的用户信息为：{}", updateUserDto);
        updateUserDto.setId(UserContext.getUser());
        User user = BeanCopyUtils.copyBean(updateUserDto, User.class);
        boolean flag = updateById(user);
        if(!flag) return Result.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
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
        Long id= UserContext.getUser();
        Object user =  redisTemplate.opsForValue().get(SystemConstant.REDIS_LOGIN_USER + id);
        log.info("isDead查询到的user为：{}", user);
        boolean isDead= ObjectUtil.isNull(user);
        return Result.okResult(!isDead);
    }

    @Override
    public String uploadImage(MultipartFile image) {
        Long id = UserContext.getUser();

        minioUtils.upload(image, image.getOriginalFilename());
        String url = minioUtils.getFileUrl(image.getOriginalFilename());
        log.info("用户上传头像url,{}", url);

        userMapper.updateImage(id, url);
        return url;
    }

    @Override
    public List<User> getUsers(List<Long> ids) {
        log.info("获取未打满用户中ing...");
        List<User> users = userMapper.selectIds(ids);
        return users;
    }
}


