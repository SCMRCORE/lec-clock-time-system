package com.lec.clock.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.clockcommon.entity.Result;
import com.lec.clock.entity.dto.CardDto;
import com.lec.clock.entity.dto.CardSkillDto;
import com.lec.clock.entity.pojo.Clock;

import java.net.UnknownHostException;

/**
 * (Clock)表服务接口
 *
 * @author makejava
 * @since 2023-03-14 16:02:03
 */
public interface CardService{


    Result addCard(CardDto cardDto);

    Result addSkillCard(CardSkillDto cardSkillDto);

    Result removeCard(Long cardType ,Long id);

    Result selectCards();

}

