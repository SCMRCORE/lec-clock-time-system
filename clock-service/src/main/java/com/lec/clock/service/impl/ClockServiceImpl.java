package com.lec.clock.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clockcommon.entity.PageVo;
import com.clockcommon.entity.Result;
import com.clockcommon.enums.AppHttpCodeEnum;
import com.clockcommon.enums.SystemConstant;
import com.clockcommon.utils.BeanCopyUtils;
import com.clockcommon.utils.UserContext;
import com.lec.clock.entity.pojo.Clock;
import com.lec.clock.entity.vo.*;
import com.lec.clock.mapper.ClockIpMapper;

import com.lec.clock.mapper.ClockMapper;
import com.lec.clock.mapper.Ipv4LogMapper;

import com.lec.clock.service.ClockService;
import com.lec.clock.service.Ipv4LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

//    @Autowired
//    DailyHistoryMapper dailyHistoryMapper;
//
//    @Autowired
//    DailyHistoryService dailyHistoryService;

    @Override
    public Result listAllClock(Integer grade, Integer pageNum, Integer pageSize) {
        if(pageNum==null){
            pageNum=1;
        }
        if(pageSize==null){
            pageSize=10;
        }
        ClockMapper mapper = getBaseMapper();
        List<ClockInfoVo> clockInfoVos = mapper.selectAllClock(grade, (pageNum - 1) * pageSize, pageSize);
        List<ClockListInfoVo> clockListInfoVo = clockInfoVos.stream()
                .map(i -> {
                    if (i.getStatus() == 1) {
                        i.setTotalDuration((int) ChronoUnit.MINUTES.between(i.getBeginTime(), LocalDateTime.now()) + i.getTotalDuration());
                    }
                    return BeanCopyUtils.copyBean(i, ClockListInfoVo.class);
                }).collect(Collectors.toList());
        PageVo pageVo = new PageVo(clockListInfoVo, clockInfoVos.size());
        return Result.okResult(pageVo);
    }

    @Override
    public Result clock() throws UnknownHostException {
        Integer week = (Integer) redisTemplate.opsForValue().get(SystemConstant.REDIS_WEEK);
        //TODO 获取ID1
//        Long id=SecurityUtils.getUserId();
        Long id = UserContext.getUser();
        log.info("获取的id为:{}", id);
//        Clock clock=clockMapper.getById(id);
//        int status = clock.getStatus();
//
//
//        //从redis数据库里获取对应的ipv4信息
//        String ipv4= (String) redisTemplate.opsForValue().get(SystemConstant.REDIS_CLOCK_IPV4+id);
//        redisTemplate.delete(SystemConstant.REDIS_CLOCK_IPV4+id);
//        // 将IP地址和子网掩码解析为InetAddress对象
//        InetAddress ip = InetAddress.getByName(ipv4);
//        InetAddress mask = InetAddress.getByName("255.255.255.0");
//
//        // 获取IP地址和子网掩码的字节数组
//        byte[] ipBytes = ip.getAddress();
//        byte[] maskBytes = mask.getAddress();
//
//        // 进行AND运算
//        byte[] networkBytes = new byte[ipBytes.length];
//        for (int i = 0; i < ipBytes.length; i++) {
//            networkBytes[i] = (byte) (ipBytes[i] & maskBytes[i]);
//        }
//        if(ipv4LogMapper.select(ipv4)==null&&ipv4!=null){
//            ipv4LogMapper.insert(ipv4);
//        }
//        String ipAndMask=Base64.getEncoder().encodeToString(networkBytes);
//
//        log.info("子网信息:{}",ipAndMask);
//        List<String> list=clockIpMapper.getClockIpList();
//        boolean isClockIp=false;
//        log.info("list:{}",list);
//        for(String i:list){
//            log.info("比对子网信息:{},:{}",ipAndMask, i);
//            if(ipAndMask.equals(i)) {
//                isClockIp=true;
//                break;
//            }
//        }
//
//        if(status == SystemConstant.CLOCKED_STATUS) {
//            if(!isClockIp){
//                return Result.okResult("打卡失败！！！，请在团队内打卡");
//            }
//            clock.setStatus(SystemConstant.CLOCKING_STATUS);
//            clock.setBeginTime(LocalDateTime.now());
//            updateById(clock);
//            StartClockVo startClockVo = BeanCopyUtils.copyBean(clock, StartClockVo.class);
//            return Result.okResult(startClockVo);
//        }
//        clock.setStatus(SystemConstant.CLOCKED_STATUS);
//        long duration = ChronoUnit.MINUTES.between(clock.getBeginTime(), LocalDateTime.now());
//
//        //TODO 计算每日打卡
//        DailyHistory dailyHistory=dailyHistoryMapper.selectById(id);
//        dailyHistory.setWeek(week);
//        int time=clock.getTotalDuration();
//        Date date=new Date();
//        String day= GetWeekUtil.GetWeekUtil(date).toString();
//        if(duration >= 60 * 5){
//            updateById(clock);
//            dailyHistory.setTime(0,day,dailyHistory);
//            dailyHistoryService.updateById(dailyHistory);
//            return Result.errorResult(AppHttpCodeEnum.CLOCK_TIMEOUT);
//        }
//        clock.setTotalDuration((int) duration+ clock.getTotalDuration());
//        dailyHistory.setTime(clock.getTotalDuration()-time,day,dailyHistory);
//        dailyHistoryService.updateById(dailyHistory);
//        updateById(clock);
//        StopClockVo stopClockVo = BeanCopyUtils.copyBean(clock, StopClockVo.class);
//        return Result.okResult(stopClockVo);
        return Result.okResult();
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
//        TODO 获取ID2
//        Long id=SecurityUtils.getUserId();
//
//        String ipv4= (String) redisTemplate.opsForValue().get(SystemConstant.REDIS_CLOCK_IPV4+id);
//        redisTemplate.delete(SystemConstant.REDIS_CLOCK_IPV4+id);
//        // 将IP地址和子网掩码解析为InetAddress对象
//        InetAddress ip = InetAddress.getByName(ipv4);
//        InetAddress mask = InetAddress.getByName("255.255.255.0");
//
//        // 获取IP地址和子网掩码的字节数组
//        byte[] ipBytes = ip.getAddress();
//        byte[] maskBytes = mask.getAddress();
//
//        // 进行AND运算
//        byte[] networkBytes = new byte[ipBytes.length];
//        for (int i = 0; i < ipBytes.length; i++) {
//            networkBytes[i] = (byte) (ipBytes[i] & maskBytes[i]);
//        }
//        if(ipv4LogMapper.select(ipv4)==null){
//            ipv4LogMapper.insert(ipv4);
//        }
//        String ipAndMask=Base64.getEncoder().encodeToString(networkBytes);
//        log.info("执行更新打卡ip功能，转义后的ip:{}",ipAndMask);
//        String isInClub=ipAndMask.substring(0,4);
////        if(isInClub.equals("q1hg")){
////            return Result.okResult("请求失败，请在团队里操作");
////        }
////
////        isInClub= ipAndMask.substring(0,2);
////        if(!isInClub.equals("q1")&&!isInClub.equals("31")){
////            log.info("该ip不是团队WIFI:{}",ipv4);
////            return Result.okResult("请求失败，请在团队里操作");
////        }
//        String clockIp=clockIpMapper.getClockIp(ipAndMask);
//        if(!(clockIp ==null)){
//            log.info("已经存在的打卡ip，ip:{},转义后的ip:{}",ipv4,ipAndMask);
//            return Result.okResult("该网络可以打卡，请重新连接再次刷新,如果不成功请联系管理员");
//        }
//        log.info("成功添加可以打卡的公网ip,ip:{},转义后的ip:{}",ipv4,ipAndMask);
//        clockIpMapper.insertClockIp(ipAndMask);
//        return Result.okResult("操作成功，请再次尝试打卡，如若不成功请联系管理员");
        return Result.okResult();
    }
}
