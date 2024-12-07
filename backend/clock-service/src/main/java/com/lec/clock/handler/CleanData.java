//package com.lec.clock.handler;
//
//import com.clockcommon.enums.SystemConstant;
//import com.lec.clock.entity.pojo.Clock;
//import com.lec.clock.mapper.ClockHistoryMapper;
//import com.lec.clock.mapper.ClockMapper;
//import com.lec.clock.service.ClockService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//@Component
//@Slf4j
//public class CleanData {
//
//    @Resource
//    ClockMapper clockMapper;
//
//    @Resource
//    ClockHistoryMapper clockHistoryMapper;
//
//    @Resource
//    RedisTemplate redisTemplate;
//
//
//    //定时任务：每周清理打卡
//    @Scheduled(cron = "0 0 23 ? * SUN")
//    public void clean() {
//        //获取所有打卡记录id
//        log.info(clockMapper.toString());
//
//        List<Clock> records = clockMapper.getAllRecords();
//
////        //保存打卡历史
////        log.info("保存打卡历史");
////        for(Clock record : records) {
////            clockHistoryMapper.saveAll(record);
////        }
//
//        //test功能，给所有人下卡
//        log.info("给所有人下卡");
//        clockMapper.clockOff(records);
//
//
////        //清空时长
////        log.info("清空时长");
////        clockMapper.cleanAllTime(records);
//    }
//}
