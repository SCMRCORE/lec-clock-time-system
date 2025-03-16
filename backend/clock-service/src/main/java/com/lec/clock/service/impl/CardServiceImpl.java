package com.lec.clock.service.impl;

import com.clockcommon.entity.Result;
import com.clockcommon.enums.AppHttpCodeEnum;
import com.clockcommon.utils.BeanCopyUtils;
import com.clockcommon.utils.UserContext;
import com.lec.clock.entity.dto.CardDto;
import com.lec.clock.entity.dto.CardSkillDto;
import com.lec.clock.entity.pojo.*;
import com.lec.clock.entity.vo.*;
import com.lec.clock.mapper.*;
import com.lec.clock.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

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
    private RedisTemplate redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private CardMapper cardMapper;

    @Resource
    private ClockMapper clockMapper;

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


    /**
     * 查询卡列表
     * @return
     */
    @Override
    public Result selectCards() {
        List<Card> cards = cardMapper.selectCards();
        List<Card> skillCards = cardMapper.selectSkillCards();
        cards.addAll(skillCards);
        if(cards==null) return Result.errorResult(AppHttpCodeEnum.CARD_NOT_EXIST);
        log.info("查询所有卡:{}", cards);
        return Result.okResult(cards);
    }


    /**
     * 查询用户卡
     * @param userId
     * @return
     */
    @Override
    public Result selectById(Long userId) {
        List<CardCount> cardCounts = cardMapper.selectCardCountById(userId);
        if(cardCounts==null) return Result.errorResult(AppHttpCodeEnum.CARD_NOT_EXIST);
        return Result.okResult(cardCounts);
    }

    @Override
    public Result useCard(Long cardType, Long cardId) {
        Long userId = UserContext.getUser();
        //看库存是否充足
        CardCount cardCount = cardMapper.selectCardCountByCardId(userId, cardType, cardId);
        if(cardCount==null || cardCount.getCount()<1){
            log.info("{}的卡{}{}不存在", userId, cardType, cardId);
            return Result.errorResult(AppHttpCodeEnum.CARD_NOT_EXIST);
        }
        RLock lock = redissonClient.getLock("useCard:" + userId);
        Boolean isLock = lock.tryLock();
        if(!isLock) {
            return Result.errorResult(AppHttpCodeEnum.PLEASE_WAIT_FOR_A_WHILE);
        }
        try{
            CardService cardServiceProxy = (CardService) AopContext.currentProxy();
            return cardServiceProxy.reduceTime(userId, cardType, cardId);
        }finally {
            lock.unlock();
        }
    }


    @Transactional
    public Result reduceTime(Long userId,Long cardType, Long cardId) {
        Card card;
        //查看卡
        if(cardType==0) {
            card = cardMapper.selectById(cardId);
        }else{
            card = cardMapper.selectSkillById(cardId);
        }
        if(card.getStatus()!=1) return Result.errorResult(AppHttpCodeEnum.CARD_NOT_EXIST);

        //扣除库存
        Boolean isSuccess = cardMapper.reduceCardCount(userId, cardType, cardId);
        if(!isSuccess) {
            throw new RuntimeException("扣除库存失败");
        }

        //扣除时间
        Integer targetReduce = card.getActualValue()*60;
        Boolean isReduce = clockMapper.reduceTime(userId, targetReduce);
        if(!isReduce) {
            throw new RuntimeException("扣除时间失败");
        }

        //删除缓存
        Other other = clockMapper.getGradeById(userId);
        Integer grade = other.getGrade();
        redisTemplate.delete("lec:listAllClock:" + grade);

        return Result.okResult("使用成功");
    }


}
