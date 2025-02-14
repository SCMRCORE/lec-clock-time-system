package com.lec.clock.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardSkillDto {
    private String title;
    private String rule;
    private int payValue;
    private int actualValue;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private int stock;
}
