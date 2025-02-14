package com.lec.clock.entity.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardVo {
    private String title;
    private String rule;
    private int payValue;
    private int actualValue;
    private int status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

