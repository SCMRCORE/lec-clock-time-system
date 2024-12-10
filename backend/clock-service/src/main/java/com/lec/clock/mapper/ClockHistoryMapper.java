package com.lec.clock.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.lec.clock.entity.pojo.Clock;
import com.lec.clock.entity.pojo.ClockHistory;
import com.lec.clock.entity.vo.ClockHistoryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * (ClockHistory)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-14 16:03:25
 */
@Mapper
public interface ClockHistoryMapper extends BaseMapper<ClockHistory> {

    void saveAll(Clock clock);

    Page<ClockHistoryVo> getAllClock();
}

