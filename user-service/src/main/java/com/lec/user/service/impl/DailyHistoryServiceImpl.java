package com.lec.user.service.impl;

import com.clockcommon.entity.Result;
import com.lec.user.mapper.DailyHistoryMapper;
import com.lec.user.service.DailyHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DailyHistoryServiceImpl implements DailyHistoryService {

    @Resource
    DailyHistoryMapper dailyHistoryMapper;

    @Override
    public Result getDay() {
//        Long id= SecurityUtils.getUserId();
//       return Result.okResult(dailyHistoryMapper.getUserById(id));
        return Result.okResult();
    }
}
