package com.lec.clock.mapper;


import com.lec.clock.entity.pojo.Card;
import com.lec.clock.entity.pojo.CardCount;
import com.lec.clock.entity.pojo.CardOrder;
import com.lec.clock.entity.pojo.UserCurrency;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Insert("insert into card_order (user_id, card_id, create_time, card_type) values (#{userId}, #{cardId}, #{createTime}, #{cardType})")
    Boolean saveOrder(CardOrder cardOrder);

    @Update("update user_currency set currency = #{newCurrency} where id = #{userId}")
    Boolean saveCurrency(Long newCurrency, Long userId);

    @Select("select * from card_count where userId = #{userId} and cardId = #{cardId} and cardType=#{cardType}")
    Boolean selectCountByCardId(Long userId, Long cardId, Long cardType);

    @Update("update card_count set count = count+1 where userId = #{userId} and cardId = #{cardId} and cardType=#{cardType}")
    Boolean updateNewCardCout(Long cardId, Long userId, Long cardType);

    @Insert("insert into card_count (userId, cardId, count, cardType) values (#{userId}, #{cardId}, 1, #{cardType})")
    Boolean addNewCardCount(Long cardId, Long userId, Long cardType);

    @Update("update card_skill set stock =stock-1 where id = #{cardId} and stock > 0")
    Boolean updateSkillStock(Long cardId);

    @Select("select * from card_order where user_id = #{userId} and card_id = #{cardId} and card_type=#{cardType}")
    List<CardOrder> selectByUserIdAndCardId(Long userId, Long cardId, Long cardType);
}
