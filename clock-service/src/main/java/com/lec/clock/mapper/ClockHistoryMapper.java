package com.lec.clock.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lec.clock.entity.pojo.ClockHistory;
import com.lec.clock.entity.vo.ClockHistoryListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    @Select("select user.id, nickname, avatar, week, duration, is_standard\n" +
            "from user left join clock_history on user.id = clock_history.id\n" +
            "where grade = #{grade} and week = #{week}\n" +
            "order by duration DESC\n" +
            "limit #{pageNum}, #{pageSize}")
    List<ClockHistoryListVo> selectClockHistoryList(@Param("week") Integer week, @Param("grade") Integer grade,
                                                    @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);
}

