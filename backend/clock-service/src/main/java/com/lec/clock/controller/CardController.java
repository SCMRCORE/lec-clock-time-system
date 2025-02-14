package com.lec.clock.controller;

import com.clockcommon.entity.Result;
import com.lec.clock.entity.dto.CardDto;
import com.lec.clock.entity.dto.CardSkillDto;
import com.lec.clock.service.CardService;
import com.lec.clock.service.impl.CardServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/api/card")
public class CardController {

    @Resource
    CardService cardService;

    /**
     * 新增卡
     * @return
     */
    @PostMapping("/add")
    public Result addCard(@RequestBody CardDto cardDto){
        return cardService.addCard(cardDto);
    }

    /**
     * 新增秒杀卡
     * @return
     */
    @PostMapping("/addSkill")
    public Result addSkillCard(@RequestBody CardSkillDto cardSkillDto){
        return cardService.addSkillCard(cardSkillDto);
    }

    /**
     * 删除卡,软删除
     * @return
     */
    @PostMapping("/remove/{cardType}/{id}")
    public Result removeCard(@PathVariable Long cardType, @PathVariable Long id){
        //TODO 秒杀优惠券过期时间可以通过业务来判断，使用时检测是否过期，过期则设置status=3
        //软删除就行
        return cardService.removeCard(cardType, id);
    }

    /**
     * 查看卡列表
     * @return
     */
    @GetMapping("/list")
    public Result listCard(){
        //查询卡目前有哪些卡
        return cardService.selectCards();
    }

}
