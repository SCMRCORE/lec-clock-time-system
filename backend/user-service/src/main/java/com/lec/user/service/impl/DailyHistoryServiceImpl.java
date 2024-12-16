package com.lec.user.service.impl;

import com.clockcommon.entity.Result;
import com.clockcommon.utils.UserContext;
import com.lec.user.entity.pojo.DailyHistory;
import com.lec.user.enums.SystemConstant;
import com.lec.user.mapper.DailyHistoryMapper;
import com.lec.user.service.DailyHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class DailyHistoryServiceImpl implements DailyHistoryService {

    @Resource
    DailyHistoryMapper dailyHistoryMapper;

    @Resource
    RedisTemplate redisTemplate;

    @Override
    public Result getDay() {
        Long id= UserContext.getUser();
        return Result.okResult(dailyHistoryMapper.getUserById(id));
    }

    @Override
    public void setDaiyTime(int addTodayTime, String day) {
        Long userId = UserContext.getUser();
        Integer week = (Integer) redisTemplate.opsForValue().get(SystemConstant.REDIS_WEEK);
        DailyHistory dailyHistory = dailyHistoryMapper.selectById(userId);
        dailyHistory.setWeek(week);
        dailyHistory.setTime(addTodayTime, day, dailyHistory);
        dailyHistoryMapper.updateById(dailyHistory);
    }

    @Override
    @Scheduled(cron = "0 0 23 ? * SUN")
    public Result weekOff() {
        List<DailyHistory> dailyHistoryList = dailyHistoryMapper.getAll();

        log.info("清空每周时长");
        for(DailyHistory dailyHistory : dailyHistoryList){
            dailyHistoryMapper.clearWeekRecord(dailyHistory);
        }
        return Result.okResult();
    }
}
