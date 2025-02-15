package com.lec.clock.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardCount {
    private Long id;
    private Long cardType;
    private Long userId;
    private Long cardId;
    private Long count;
}
