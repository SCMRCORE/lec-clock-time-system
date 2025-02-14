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
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.lec.user.mapper.UserIpMapper;
import com.lec.user.mapper.UserMapper;
import com.lec.user.service.UserService;
import com.lec.user.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.util.Base64;
import java.util.List;
import java.util.Random;
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

    @Resource
    RedisTemplate redisTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${spring.mail.from}")
    private String from;

    @Autowired
    private JavaMailSenderImpl mailSender;

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

    @Resource
    UserIpMapper userIpMapper;

    public boolean checkIP(Long userId){
        try {
            String ipv4= (String) redisTemplate.opsForValue().get(SystemConstant.REDIS_CLOCK_IPV4+userId);
            if (ipv4 == null) {
                log.info("未获取IP，请检查代码");
                //想想该怎么解决
                return true;
            } else {
                redisTemplate.delete(SystemConstant.REDIS_CLOCK_IPV4+userId);
                InetAddress ip = InetAddress.getByName(ipv4);
                InetAddress mask = InetAddress.getByName("255.255.255.0");

                byte[] ipBytes = ip.getAddress();
                byte[] maskBytes = mask.getAddress();

                byte[] networkBytes = new byte[ipBytes.length];
                for(int i = 0; i<ipBytes.length; i++){
                    networkBytes[i] = (byte)(ipBytes[i] & maskBytes[i]);
                }

                String ipAndMask = Base64.getEncoder().encodeToString(networkBytes);

                log.info("子网信息：{}", ipAndMask);
                //执行判断逻辑
                List<String> ipList = userIpMapper.getClockIpList();
                for(String i : ipList){
                    if(ipAndMask.equals(i)){
                        return true;
                    }
                }
                return false;
            }
        }catch (Exception e){
            log.info("IP检查出错，请立刻检查代码");
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public Result login(LoginUserDto loginUserDto) {
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

        Long userId = user.getId();
        String userEmail = user.getEmail();
        log.info("获取的userId为：{}", userId);
        //验证IP
        if(!checkIP(userId)){
            //邮件告警但是仍然登录
            SimpleMailMessage smm  = new SimpleMailMessage();
            try{
                smm.setSubject("账号异地登陆");
                smm.setText("警告:" +username+'\n'+
                            "检测到你的账号正在非团队网络下登录"+'\n'+
                            "若非本人操作，请立刻联系管理员"+'\n'+
                            SystemConstant.SITE_ADDRESS
                );
                smm.setFrom(from);
                smm.setTo(userEmail);
                mailSender.send(smm);
                log.info("异地登陆邮件发送成功");
            }catch (Exception e){
                log.info("异地登陆邮件发送失败");
                return Result.errorResult(AppHttpCodeEnum.EMAIL_ERROR);
            }
        }

        //获取用户实体并且将信息存入redis
        redisTemplate.opsForValue().set(SystemConstant.REDIS_LOGIN_USER + user.getId(), user, 60 * 24 * 3, TimeUnit.MINUTES);
        log.info("user:{}", SystemConstant.REDIS_LOGIN_USER + user.getId());

        //生成token
        String token = jwtTool.createToken(userId, jwtProperties.getTokenTTL());
        log.info("创建一个新的token:{}", token);

        //封装返回给vo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        LoginUserVo loginUserVo = new LoginUserVo(token, userInfoVo);
        return Result.okResult(loginUserVo);
    }


    @Override
    public Result register(RegisterUserDto registerUserDto) {
        //TODO 注册的话，后端有感知，但是前端目前无感知
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

        //给admin发邮件
        SimpleMailMessage smm = new SimpleMailMessage();
        Random random = new Random();
        //生成随机盐-用户注册请求ID
        String salt = String.valueOf(random.nextInt(99999));
        try {
            smm.setSubject("用户注册审核");
            smm.setText("用户注册编号："+ salt +'\n'+
                        "用户名："+registerUserDto.getUsername()+'\n'+
                        "用户昵称："+registerUserDto.getNickname()+'\n'+
                        "用户年级："+registerUserDto.getGrade().toString()+'\n'+
                        "用户邮箱："+registerUserDto.getEmail()+'\n'
            );
            smm.setTo(SystemConstant.adminEmail);
            smm.setFrom(from);
            //将用户信息存到Redis,有效期3天
            ObjectMapper objectMapper = new ObjectMapper();
            String registerUserDtoJson = objectMapper.writeValueAsString(registerUserDto);
            redisTemplate.opsForValue().set("RegisterAudit"+salt, registerUserDtoJson, 60 * 24 * 3, TimeUnit.MINUTES);
            mailSender.send(smm);
            log.info("给admin发邮件成功");
            //TODO 可以通过前端来提示，审核发送成功若3天未收到审核邮件，则联系管理员或者重写发送
            return Result.okResult("审核邮件发送成功");
        }catch (Exception e) {
            log.info("给admin发邮件失败");
            e.printStackTrace();
            return Result.errorResult(AppHttpCodeEnum.EMAIL_ERROR);
        }
    }

    //用于从redis获取用户信息
    public RegisterUserDto getRegisterUserDto(String key) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String registerUserDtoJson = (String) redisTemplate.opsForValue().get(key);
            if (registerUserDtoJson == null) {
                log.info("未从Redis获取到用户信息");
                return null;
            }
            return objectMapper.readValue(registerUserDtoJson, RegisterUserDto.class);
        }catch (Exception e) {
            log.info("Redis获取注册信息失败");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result registerAudit(String choose, String salt) {
        //从Redis获取注册信息
        RegisterUserDto registerUserDto = getRegisterUserDto("RegisterAudit"+salt);
        if(registerUserDto==null){
            return Result.errorResult(AppHttpCodeEnum.USER_NOT_EXIT);
        }

            SimpleMailMessage smm = new SimpleMailMessage();
            if(choose.equals("NO")) {
                try {
                    smm.setSubject("用户注册审核通知");
                    smm.setText("抱歉，"+ registerUserDto.getUsername()+'\n'+
                                "您的注册申请未通过审核"+'\n'+
                                "请联系管理员说明情况后再尝试请重新注册");
                    smm.setFrom(from);
                    smm.setTo(registerUserDto.getEmail());
                    mailSender.send(smm);
                    log.info("拒绝用户注册的通告文件发送成功");
                }catch (Exception e) {
                    log.info("拒绝用户注册的通告文件发送失败,请代码检查错误");
                    e.printStackTrace();
                    return Result.errorResult(AppHttpCodeEnum.EMAIL_ERROR);
                }
                redisTemplate.delete("RegisterAudit"+salt);
                return Result.okResult("审核未通过");
            }

            Integer week = (Integer) redisTemplate.opsForValue().get(SystemConstant.REDIS_WEEK);
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
            //添加到用户和货币表
            userMapper.insert(user);
            userMapper.insertUserCurrency(user.getId(), 0);
            try {
                clockClient.createClock(user.getId(), user.getGrade(), user.getUsername(), user.getNickname());
            }catch (Exception e) {
                log.info("OpenFeign请求失败，创建打卡失败");
                e.printStackTrace();
                //捕获后触发回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.errorResult(AppHttpCodeEnum.OPENFIRGN_ERROR);
            }

            try {
                smm.setSubject("用户注册审核通知");
                smm.setText("恭喜，"+userName +'\n'+
                            "您的注册申请已经通过审核，欢迎加入乐程大家庭"+'\n'+
                            "现在就可以登录系统开始使用啦"+'\n'+
                            SystemConstant.SITE_ADDRESS
                );
                smm.setFrom(from);
                smm.setTo(registerUserDto.getEmail());
                mailSender.send(smm);
                log.info("同意用户注册的通告文件发送成功");
            }catch (Exception e) {
                log.info("同意用户注册的通告文件发送失败,请代码检查错误");
                e.printStackTrace();
                //捕获后触发回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.errorResult(AppHttpCodeEnum.EMAIL_ERROR);
            }
            redisTemplate.delete("RegisterAudit"+salt);
            return Result.okResult("审核通过");
    }

    @Override
    public Result sendCode(String email) {
        SimpleMailMessage smm = new SimpleMailMessage();		//创建邮件对象
        String IsEmail = userMapper.hasEmail(email);
        if(IsEmail!=null){
            log.info("注册邮箱已存在：{}", IsEmail);
            //TODO 发送验证码，后端有感知，但是前端目前无感知
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


