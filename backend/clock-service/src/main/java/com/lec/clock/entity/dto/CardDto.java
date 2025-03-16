package com.lec.clock.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
    private String title;
    private String rule;
    private int payValue;
    private int actualValue;
}
