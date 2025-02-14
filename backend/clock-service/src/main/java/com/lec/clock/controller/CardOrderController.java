package com.lec.clock.controller;

import com.clockcommon.entity.Result;
import com.lec.clock.service.CardOrderService;
import com.lec.clock.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/api/card-order")
public class CardOrderController {

    @Resource
    private CardOrderService cardOrderService;

    /**
     * 创建购卡订单
     * @return
     */
    @PostMapping("/seckill/{cardType}/{id}")
    public Result secCard(@PathVariable Long cardType, @PathVariable Long id){
        return cardOrderService.addOrder(cardType, id);
    }

}
