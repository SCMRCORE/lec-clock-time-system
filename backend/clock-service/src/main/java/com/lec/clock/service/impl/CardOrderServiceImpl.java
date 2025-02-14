package com.lec.clock.service.impl;


import com.clockcommon.entity.Result;
import com.clockcommon.enums.AppHttpCodeEnum;
import com.clockcommon.utils.BeanCopyUtils;
import com.clockcommon.utils.UserContext;
import com.lec.clock.entity.dto.CardDto;
import com.lec.clock.entity.dto.CardSkillDto;
import com.lec.clock.entity.pojo.Card;
import com.lec.clock.entity.pojo.UserCurrency;
import com.lec.clock.entity.vo.CardSkillVo;
import com.lec.clock.entity.vo.CardVo;
import com.lec.clock.mapper.CardMapper;
import com.lec.clock.mapper.CardOrderMapper;
import com.lec.clock.service.CardOrderService;
import com.lec.clock.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private CardMapper cardMapper;

    @Resource
    private CardOrderMapper cardOrderMapper;

    @Override
    public Result addOrder(Long cardType, Long id) {
        if(cardType == null || id == null){return Result.errorResult(AppHttpCodeEnum.CARD_NOT_EXIST);}
        //获取所需购入的卡
        Card card;
        if(cardType==0){
            card = cardMapper.selectById(id);
        }else{
            card = cardMapper.selectSkillById(id);
        }
        if(card == null){return Result.errorResult(AppHttpCodeEnum.CARD_NOT_EXIST);}
        //判断用户货币是否足够
        Long userId = UserContext.getUser();
        UserCurrency userCurrency = cardOrderMapper.selectCurrencyByUserId(userId);
        if(userCurrency==null) {return Result.errorResult(AppHttpCodeEnum.USER_CURRENCY_NOT_FIND);}
        //余额是否充足
        //TODO 待完善
        return null;
    }
}
