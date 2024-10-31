package com.lec.clock.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClockHistoryListVo {
    private Long id;

    private String nickname;

    private String avatar;

    private Integer week;

    private Integer duration;

    private Integer isStandard;


}
