package com.lec.clock.mapper;


import com.lec.clock.entity.pojo.Card;
import com.lec.clock.entity.pojo.UserCurrency;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * (Clock)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-14 16:02:03
 */
@Mapper
public interface CardOrderMapper {

    @Select("select * from user_currency where id = #{userId}")
    UserCurrency selectCurrencyByUserId(Long userId);
}
