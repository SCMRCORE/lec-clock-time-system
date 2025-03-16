package com.lec.user.service;
import com.clockcommon.entity.Result;

public interface DailyHistoryService{
    Result getDay();

    void setDaiyTime(int addTodayTime, String day);

    Result weekOff();
}
