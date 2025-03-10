package com.lec.clock.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lec.clock.entity.pojo.Clock;
import com.lec.clock.entity.pojo.Other;
import com.lec.clock.entity.vo.ClockInfoVo;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * (Clock)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-14 16:02:03
 */
@Mapper
public interface ClockMapper extends BaseMapper<Clock> {
    @Select("select nickname, avatar, status, begin_time, total_duration, (target_duration-temporary) as adjust_target_duration\n" +
            "from user left join clock on user.id = clock.id\n" +
            "where grade = #{grade} and clock.del_flag = 0\n" +
            "order by total_duration DESC\n" +
            "limit #{pageNum}, #{pageSize}")
    List<ClockInfoVo> selectAllClock(@Param("grade") Integer grade, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    @Select("select * from clock where id = #{id}")
    Clock getById(Long id);

    @Insert("insert into clock (id, begin_time, status, total_duration, target_duration, create_time, update_time, temporary) values (#{id}, #{beginTime}, #{status}, #{totalDuration}, #{targetDuration}, #{createTime}, #{updateTime}, 0)")
    void addNewClock(Clock clock);

    @Select("select * from `lec-clock-in`.clock")
    List<Clock> getAllRecords();


    void cleanAllTime(List<Clock> records);

    void clockOff(List<Clock> records);

    Other getGradeById(Long id);

    @Update("update clock set temporary = temporary + #{targetReduce} where id = #{userId} and target_duration-#{targetReduce} >= 1440")
    Boolean reduceTime(Long userId, Integer targetReduce);

    @Update("update clock set temporary = 0")
    void clearTempTime();

    @Update("update user_currency set currency = currency + #{currency} where id = #{id}")
    void updateCurrency(Long id, int currency);
}
