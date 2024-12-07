package com.lec.clock.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.clockcommon.entity.Result;
import com.lec.clock.entity.pojo.Clock;

import java.net.UnknownHostException;
import java.util.List;

/**
 * (Clock)表服务接口
 *
 * @author makejava
 * @since 2023-03-14 16:02:03
 */
public interface ClockService extends IService<Clock> {

    Result listAllClock(Integer grade, Integer pageNum, Integer pageSize);

    Result clock(Long id) throws UnknownHostException;

    Result getClockById(Long id);

    Result updateDuration(Long id, Integer duration);

    Result addIpv4() throws UnknownHostException;

    void createClockByUserId(Long userId, Integer grade);

    Result clockOff();
}

