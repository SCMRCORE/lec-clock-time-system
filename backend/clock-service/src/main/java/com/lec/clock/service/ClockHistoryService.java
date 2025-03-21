package com.lec.clock.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lec.clock.entity.pojo.ClockHistory;
import com.lec.clock.utils.PageResult;

/**
 * (ClockHistory)表服务接口
 *
 * @author makejava
 * @since 2023-03-14 16:03:25
 */
public interface ClockHistoryService extends IService<ClockHistory> {

    PageResult list(Integer pageNum, Integer pageSize);

//    Result getClockHistoryById(Long id, Integer pageNum, Integer pageSize);
}

