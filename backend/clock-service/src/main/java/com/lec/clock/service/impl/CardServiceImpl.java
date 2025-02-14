package com.lec.clock.service.impl;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.C;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clockcommon.entity.PageVo;
import com.clockcommon.entity.Result;
import com.clockcommon.enums.AppHttpCodeEnum;
import com.clockcommon.enums.SystemConstant;
import com.clockcommon.utils.BeanCopyUtils;
import com.clockcommon.utils.UserContext;
import com.example.lecapi.clients.UserClient;
import com.lec.clock.entity.dto.CardDto;
import com.lec.clock.entity.dto.CardSkillDto;
import com.lec.clock.entity.pojo.Card;
import com.lec.clock.entity.pojo.Clock;
import com.lec.clock.entity.pojo.ClockHistory;
import com.lec.clock.entity.pojo.Other;
import com.lec.clock.entity.vo.*;
import com.lec.clock.mapper.*;
import com.lec.clock.service.CardService;
import com.lec.clock.service.ClockService;
import com.lec.clock.service.Ipv4LogService;
import com.lec.clock.utils.GetWeekUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * (Clock)表服务实现类
 *
 * @author makejava
 * @since 2023-03-14 16:02:03
 */
@Slf4j
@Service
public class CardServiceImpl implements CardService {

    @Resource
    private CardMapper cardMapper;

    /**
     * 新增减时卡
     * @param cardDto
     */
    @Override
    public Result addCard(CardDto cardDto) {
        if(cardDto==null) return Result.errorResult(AppHttpCodeEnum.CARD_PARAM_INVALID);
        Card card = BeanCopyUtils.copyBean(cardDto, Card.class);
        card.setCreateTime(LocalDateTime.now());
        card.setUpdateTime(LocalDateTime.now());
        cardMapper.addCard(card);
        log.info("新增减时卡成功，卡为{}",card.getTitle());
        CardVo cardVo = BeanCopyUtils.copyBean(card, CardVo.class);
        return Result.okResult(cardVo);
    }

    @Override
    public Result addSkillCard(CardSkillDto cardSkillDto) {
        if(cardSkillDto==null) return Result.errorResult(AppHttpCodeEnum.CARD_PARAM_INVALID);
        Card card = BeanCopyUtils.copyBean(cardSkillDto, Card.class);
        card.setCreateTime(LocalDateTime.now());
        card.setUpdateTime(LocalDateTime.now());
        cardMapper.addSkillCard(card);
        log.info("新增限时减时卡成功，卡为{}",card.getTitle());
        CardSkillVo cardSkillVo = BeanCopyUtils.copyBean(card, CardSkillVo.class);
        return Result.okResult(cardSkillVo);
    }

    @Override
    public Result removeCard(Long cardType, Long id) {
        if(cardType==null||id==null) return Result.errorResult(AppHttpCodeEnum.CARD_PARAM_INVALID);
        //0是普通卡，1是秒杀卡
        if(cardType==0){
            Card card = cardMapper.selectById(id);
            if(card==null) {
                return Result.errorResult(AppHttpCodeEnum.CARD_NOT_EXIST);
            }
            cardMapper.removeCard(id);
            log.info("移除减时卡成功，卡号为{}",id);
            return Result.okResult(card);
        }else{
            Card card = cardMapper.selectSkillById(id);
            if(card==null) {
                return Result.errorResult(AppHttpCodeEnum.CARD_NOT_EXIST);
            }
            cardMapper.removeSkillCard(id);
            log.info("移除秒杀减时卡成功，卡号为{}",id);
            return Result.okResult(card);
        }
    }

    @Override
    public Result selectCards() {
        List<Card> cards = cardMapper.selectCards();
        List<Card> skillCards = cardMapper.selectSkillCards();
        cards.addAll(skillCards);
        if(cards==null) return Result.errorResult(AppHttpCodeEnum.CARD_NOT_EXIST);
        log.info("查询所有卡:{}", cards);
        return Result.okResult(cards);
    }


}
