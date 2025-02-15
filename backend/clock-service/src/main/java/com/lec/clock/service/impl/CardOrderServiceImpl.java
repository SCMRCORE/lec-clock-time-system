package com.lec.clock.service.impl;


import com.clockcommon.entity.Result;
import com.clockcommon.enums.AppHttpCodeEnum;
import com.clockcommon.utils.BeanCopyUtils;
import com.clockcommon.utils.UserContext;
import com.lec.clock.entity.dto.CardDto;
import com.lec.clock.entity.dto.CardSkillDto;
import com.lec.clock.entity.pojo.Card;
import com.lec.clock.entity.pojo.CardCount;
import com.lec.clock.entity.pojo.CardOrder;
import com.lec.clock.entity.pojo.UserCurrency;
import com.lec.clock.entity.vo.CardSkillVo;
import com.lec.clock.entity.vo.CardVo;
import com.lec.clock.mapper.CardMapper;
import com.lec.clock.mapper.CardOrderMapper;
import com.lec.clock.service.CardOrderService;
import com.lec.clock.service.CardService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
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
public class CardOrderServiceImpl implements CardOrderService {

    @Resource
    RedissonClient redissonClient;

    @Resource
    private CardMapper cardMapper;

    @Resource
    private CardOrderMapper cardOrderMapper;

    @Override
    public Result addOrder(Long cardType, Long id) {
        if(cardType == null || id == null){return Result.errorResult(AppHttpCodeEnum.CARD_NOT_EXIST);}
        if(cardType==0){//普通卡，无时间限制，无库存限制
            //获取卡信息
            Card card = cardMapper.selectById(id);
            if(card == null){return Result.errorResult(AppHttpCodeEnum.CARD_NOT_EXIST);}
            //获取用户账户信息
            Long userId = UserContext.getUser();
            UserCurrency userCurrency = cardOrderMapper.selectCurrencyByUserId(userId);
            if(userCurrency==null) {return Result.errorResult(AppHttpCodeEnum.USER_CURRENCY_NOT_FIND);}
            //创建订单
            //使用代理对象，让事务生效
            CardOrderService proxy = (CardOrderService) AopContext.currentProxy();
            return proxy.createOrder(card, id, userId, userCurrency, 0L);
        }else{//秒杀卡
            //获取卡信息
            Card card = cardMapper.selectSkillById(id);
            if(card == null){return Result.errorResult(AppHttpCodeEnum.CARD_NOT_EXIST);}
            //获取用户账户信息
            Long userId = UserContext.getUser();
            UserCurrency userCurrency = cardOrderMapper.selectCurrencyByUserId(userId);
            if(userCurrency==null) {return Result.errorResult(AppHttpCodeEnum.USER_CURRENCY_NOT_FIND);}
            //判断秒杀是否开始
            if(card.getBeginTime().isAfter(LocalDateTime.now())){
                return Result.errorResult(AppHttpCodeEnum.SKILL_NOT_START);
            }
            //判断秒杀是否结束
            if(card.getEndTime().isBefore(LocalDateTime.now())){
                //秒杀结束设置status过期
                cardMapper.updateById(id);
                return Result.errorResult(AppHttpCodeEnum.SKILL_END);
            }
            //判断库存是否充足
            if(card.getStock() < 1){
                return Result.errorResult(AppHttpCodeEnum.STOCK_NOT_ENOUGH);
            }
            //分布式锁
            RLock lock = redissonClient.getLock("clock:card:skill:" + userId);
            boolean isLock = lock.tryLock();
            if(!isLock){
                return Result.errorResult(AppHttpCodeEnum.NO_REPEAT_ORDER);
            }
            try{
                CardOrderService proxy = (CardOrderService) AopContext.currentProxy();
                return proxy.createOrder(card, id, userId, userCurrency, 1L);
            }finally {
                lock.unlock();
            }
        }
    }

    @Transactional
    public Result createOrder(Card card, Long cardId, Long userId, UserCurrency userCurrency, Long cardType) {
        //先看是否重复下单,扣减库存
        if(cardType==1){
            //获取订单信息，查看是否已经购买过
            List<CardOrder> cardOrders = cardOrderMapper.selectByUserIdAndCardId(userId, cardId, cardType);
            if(cardOrders!=null && !cardOrders.isEmpty()){return Result.errorResult(AppHttpCodeEnum.NO_REPEAT_ORDER);}
            //扣减库存
            Boolean stockSuccess = cardOrderMapper.updateSkillStock(cardId);
            if(stockSuccess==null||!stockSuccess){
                return Result.errorResult(AppHttpCodeEnum.STOCK_NOT_ENOUGH);
            }
        }

        //扣除余额
        if(userCurrency.getCurrency() < card.getPayValue()){
            log.info("{}余额不足，购买失败:{}", userId, cardId);
            return Result.errorResult(AppHttpCodeEnum.CURRENCY_NOT_ENOUGH);
        }
        Long newCurrency = userCurrency.getCurrency() - card.getPayValue();
        Boolean currencySuccess = cardOrderMapper.saveCurrency(newCurrency, userId);
        if(currencySuccess==null||!currencySuccess){
            throw new RuntimeException("扣除余额失败");
        }

        //创建订单
        CardOrder cardOrder = new CardOrder();
        cardOrder.setCardId(cardId);
        cardOrder.setUserId(userId);
        cardOrder.setCardType(cardType);
        cardOrder.setCreateTime(LocalDateTime.now());
        Boolean orderSuccess = cardOrderMapper.saveOrder(cardOrder);
        if(orderSuccess==null||!orderSuccess){
            throw new RuntimeException("订单创建失败");
        }
        //添加库存
        Boolean isCardCount = cardOrderMapper.selectCountByCardId(userId, cardId, cardType);
        Boolean CardCountSuccess;
        if(isCardCount==null ||!isCardCount){
            CardCountSuccess =  cardOrderMapper.addNewCardCount(cardId, userId, cardType);
        }else{
            CardCountSuccess = cardOrderMapper.updateNewCardCout(cardId, userId, cardType);
        }
        if(CardCountSuccess==null || !CardCountSuccess){throw new RuntimeException("添加库存失败");}
        log.info("用户{}购买成功:订单ID{},种类{}", userId, cardId, cardType);
        return Result.okResult(cardOrder);
    }

}
