package com.lec.clock.controller;

import com.clockcommon.entity.Result;
import com.lec.clock.entity.dto.CardDto;
import com.lec.clock.entity.dto.CardSkillDto;
import com.lec.clock.service.CardService;
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

    /**
     * 根据用户id查看卡列表
     * @param userId
     * @return
     */
    @GetMapping("/list/{userId}")
    public Result listCardById(@PathVariable Long userId){
        return cardService.selectById(userId);
    }

    @PostMapping("/use/{cardType}/{cardId}")
    public Result useCard(@PathVariable Long cardType, @PathVariable Long cardId){
        return cardService.useCard(cardType, cardId);
    }

}
