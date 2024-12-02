package com.lec.clock.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clockcommon.entity.PageVo;
import com.clockcommon.entity.Result;
import com.clockcommon.enums.AppHttpCodeEnum;
import com.clockcommon.enums.SystemConstant;
import com.clockcommon.utils.BeanCopyUtils;
import com.clockcommon.utils.UserContext;
import com.example.lecapi.clients.UserClient;
import com.lec.clock.entity.pojo.Clock;
import com.lec.clock.entity.vo.*;
import com.lec.clock.mapper.ClockIpMapper;

import com.lec.clock.mapper.ClockMapper;
import com.lec.clock.mapper.Ipv4LogMapper;

import com.lec.clock.service.ClockService;
import com.lec.clock.service.Ipv4LogService;
import com.lec.clock.utils.GetWeekUtil;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (Clock)表服务实现类
 *
 * @author makejava
 * @since 2023-03-14 16:02:03
 */
@Slf4j
@Service("clockService")
public class ClockServiceImpl extends ServiceImpl<ClockMapper, Clock> implements ClockService {

    @Resource
    UserClient userClient;

    @Autowired
    ClockMapper clockMapper;

    @Autowired
    Ipv4LogService ipv4LogService;
    @Autowired
    Ipv4LogMapper ipv4LogMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ClockIpMapper clockIpMapper;


    @Override
    public Result listAllClock(Integer grade, Integer pageNum, Integer pageSize) {
        if(pageNum==null){
            pageNum=1;
        }
        if(pageSize==null){
            pageSize=10;
        }
        ClockMapper mapper = getBaseMapper();
        //从数据库获取打卡记录
        List<ClockInfoVo> clockInfoVos = mapper.selectAllClock(grade, (pageNum - 1) * pageSize, pageSize);
        List<ClockListInfoVo> clockListInfoVo = clockInfoVos.stream()
                .map(i -> {
                    //处于打卡中
                    if (i.getStatus() == 1) {
                        //现在时间-开始时间+已经打卡总时长
                        i.setTotalDuration((int) ChronoUnit.MINUTES.between(i.getBeginTime(), LocalDateTime.now()) + i.getTotalDuration());
                    }
                    return BeanCopyUtils.copyBean(i, ClockListInfoVo.class);
                }).collect(Collectors.toList());
        PageVo pageVo = new PageVo(clockListInfoVo, clockInfoVos.size());
        return Result.okResult(pageVo);
    }

    @Override
    public Result clock(Long id) throws UnknownHostException {
//        Integer week = (Integer) redisTemplate.opsForValue().get(SystemConstant.REDIS_WEEK);
        Clock clock=clockMapper.getById(id);
        int status = clock.getStatus();

        //从redis数据库里获取对应的ipv4信息
        String ipv4= (String) redisTemplate.opsForValue().get(SystemConstant.REDIS_CLOCK_IPV4+id);
        redisTemplate.delete(SystemConstant.REDIS_CLOCK_IPV4+id);
        // 将IP地址和子网掩码解析为InetAddress对象
        InetAddress ip = InetAddress.getByName(ipv4);
        InetAddress mask = InetAddress.getByName("255.255.255.0");

        // 获取IP地址和子网掩码的字节数组
        byte[] ipBytes = ip.getAddress();
        byte[] maskBytes = mask.getAddress();

        // 进行AND运算
        byte[] networkBytes = new byte[ipBytes.length];
        for (int i = 0; i < ipBytes.length; i++) {
            networkBytes[i] = (byte) (ipBytes[i] & maskBytes[i]);
        }
        if(ipv4LogMapper.select(ipv4)==null&&ipv4!=null){
            ipv4LogMapper.insert(ipv4);
        }
        String ipAndMask= Base64.getEncoder().encodeToString(networkBytes);

        log.info("子网信息:{}",ipAndMask);
        List<String> list=clockIpMapper.getClockIpList();
        boolean isClockIp=false;
        log.info("list:{}",list);
        for(String i:list){
            log.info("比对子网信息:{},:{}",ipAndMask, i);
            if(ipAndMask.equals(i)) {
                isClockIp=true;
                break;
            }
        }

        //开始打卡
        if(status == SystemConstant.CLOCKED_STATUS) {
            if(!isClockIp){
                log.info("不在打卡ip内");
                return Result.okResult("打卡失败！！！，请在团队内打卡");
            }
            log.info("开始打卡");
            clock.setStatus(SystemConstant.CLOCKING_STATUS);
            clock.setBeginTime(LocalDateTime.now());
            updateById(clock);
            StartClockVo startClockVo = BeanCopyUtils.copyBean(clock, StartClockVo.class);
            return Result.okResult(startClockVo);
        }
        //结束打卡
        log.info("结束打卡");
        clock.setStatus(SystemConstant.CLOCKED_STATUS);
        long duration = ChronoUnit.MINUTES.between(clock.getBeginTime(), LocalDateTime.now());

        int time=clock.getTotalDuration();
        Date date=new Date();
        String day= GetWeekUtil.GetWeekUtil(date).toString();
        if(duration >= 60 * 5){
            updateById(clock);
            userClient.dailyclock(0, day);
            return Result.errorResult(AppHttpCodeEnum.CLOCK_TIMEOUT);
        }
        else {
            clock.setTotalDuration((int) duration + clock.getTotalDuration());//加上每日打卡时长
            userClient.dailyclock(clock.getTotalDuration() - time, day);
            updateById(clock);
            StopClockVo stopClockVo = BeanCopyUtils.copyBean(clock, StopClockVo.class);
            return Result.okResult(stopClockVo);
        }
    }

    @Override
    public Result getClockById(Long id) {
        Clock clock = getById(id);
        if(clock.getStatus() == 1){
            clock.setTotalDuration((int) ChronoUnit.MINUTES.between(clock.getBeginTime(), LocalDateTime.now()) + clock.getTotalDuration());
        }
        UserClockInfoVo userClockInfoVo = BeanCopyUtils.copyBean(clock, UserClockInfoVo.class);
        return Result.okResult(userClockInfoVo);
    }

    @Override
    public Result  updateDuration(Long id, Integer duration) {
        boolean flag = lambdaUpdate().setSql("total_duration = total_duration+" + duration).eq(Clock::getId, id).update();
        if(!flag) {
            return Result.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return Result.okResult();
    }

    @Override
    public Result addIpv4() throws UnknownHostException {
        Long id=UserContext.getUser();

        String ipv4= (String) redisTemplate.opsForValue().get(SystemConstant.REDIS_CLOCK_IPV4+id);
        log.info("获取的ip为：{}， id为：{}",ipv4,id);
        redisTemplate.delete(SystemConstant.REDIS_CLOCK_IPV4+id);
        // 将IP地址和子网掩码解析为InetAddress对象
        InetAddress ip = InetAddress.getByName(ipv4);
        InetAddress mask = InetAddress.getByName("255.255.255.0");

        // 获取IP地址和子网掩码的字节数组
        byte[] ipBytes = ip.getAddress();
        byte[] maskBytes = mask.getAddress();

        // 进行AND运算
        byte[] networkBytes = new byte[ipBytes.length];
        for (int i = 0; i < ipBytes.length; i++) {
            networkBytes[i] = (byte) (ipBytes[i] & maskBytes[i]);
        }
        //插入
        if(ipv4LogMapper.select(ipv4)==null){
            ipv4LogMapper.insert(ipv4);
        }
        String ipAndMask=Base64.getEncoder().encodeToString(networkBytes);
        log.info("执行更新打卡ip功能，转义后的ip:{}",ipAndMask);
        String clockIp=clockIpMapper.getClockIp(ipAndMask);
        if(!(clockIp ==null)){
            log.info("已经存在的打卡ip，ip:{},转义后的ip:{}",ipv4,ipAndMask);
            return Result.okResult("该网络可以打卡，请重新连接再次刷新,如果不成功请联系管理员");
        }
        log.info("成功添加可以打卡的公网ip,ip:{},转义后的ip:{}",ipv4,ipAndMask);
        clockIpMapper.insertClockIp(ipAndMask);
        return Result.okResult("操作成功，请再次尝试打卡，如若不成功请联系管理员");
    }



    //以下为rpc部分
    /**
     * 根据用户id创建clock
     * @param userId
     * @param grade
     */
    @Override
    public void createClockByUserId(Long userId, Integer grade) {
        log.info("创建用户clock对象，用户id:{},年级:{}",userId,grade);
        Clock clock = new Clock(LocalDateTime.now(), SystemConstant.CLOCKED_STATUS, 0, SystemConstant.FIRST_GRADE_CLOCK_TARGET);
        if(grade==2){
            clock.setTargetDuration(SystemConstant.SECOND_GRADE_CLOCK_TARGET);
        }
        clock.setId(userId);
        clockMapper.addNewClock(clock);
    }

}