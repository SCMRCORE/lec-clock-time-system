package com.lec.clock.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.clockcommon.entity.Result;
import com.lec.clock.entity.pojo.ClockHistory;

/**
 * (ClockHistory)表服务接口
 *
 * @author makejava
 * @since 2023-03-14 16:03:25
 */
public interface ClockHistoryService extends IService<ClockHistory> {

    Result list(Integer week, Integer grade, Integer pageNum, Integer pageSize);

    Result getClockHistoryById(Long id, Integer pageNum, Integer pageSize);
}

