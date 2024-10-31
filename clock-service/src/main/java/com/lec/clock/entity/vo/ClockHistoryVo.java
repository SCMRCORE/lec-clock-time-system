package com.lec.clock.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClockHistoryVo {
    private Long id;

    private Integer week;

    private Integer duration;

    private Integer isStandard;


}