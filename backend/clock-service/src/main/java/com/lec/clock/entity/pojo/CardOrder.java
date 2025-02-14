package com.lec.clock.entity.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardOrder {
    private Long id;
    private Long cardType;
    private Long userId;
    private Long cardId;
    private Date createTime;
}
