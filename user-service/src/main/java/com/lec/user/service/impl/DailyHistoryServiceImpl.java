package com.lec.user.service.impl;

import com.clockcommon.entity.Result;
import com.clockcommon.utils.UserContext;
import com.lec.user.entity.pojo.DailyHistory;
import com.lec.user.enums.SystemConstant;
import com.lec.user.mapper.DailyHistoryMapper;
import com.lec.user.service.DailyHistoryService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
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
}
