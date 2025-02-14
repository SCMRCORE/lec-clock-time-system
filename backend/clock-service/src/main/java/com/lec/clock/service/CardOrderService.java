package com.lec.clock.service;


import com.clockcommon.entity.Result;
import com.lec.clock.entity.dto.CardDto;
import com.lec.clock.entity.dto.CardSkillDto;

/**
 * (Clock)表服务接口
 *
 * @author makejava
 * @since 2023-03-14 16:02:03
 */
public interface CardOrderService {

    Result addOrder(Long cardType, Long id);
}

