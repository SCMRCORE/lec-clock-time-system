package com.lec.clock.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lec.clock.entity.pojo.Card;
import com.lec.clock.entity.pojo.CardOrder;
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
public interface CardMapper {

    void addCard(Card card);

    void addSkillCard(Card card);

    Card selectById(Long id);

    void removeCard(Long id);

    List<Card> selectCards();

    Card selectSkillById(Long id);

    void removeSkillCard(Long id);

    List<Card> selectSkillCards();

    @Update("update `lec-clock-in`.card_skill set status=2 where id = #{id}")
    void updateById(Long id);
}
