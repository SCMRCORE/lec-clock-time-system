package com.lec.user.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.clockcommon.entity.Result;
import com.lec.user.entity.pojo.DailyHistory;


public interface DailyHistoryService{
    Result getDay();
}
