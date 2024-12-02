package com.lec.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lec.user.entity.pojo.DailyHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DailyHistoryMapper extends BaseMapper<DailyHistory> {

    @Select("select * from daily_history where id = #{id}")
    DailyHistory getUserById(Long id);
}
