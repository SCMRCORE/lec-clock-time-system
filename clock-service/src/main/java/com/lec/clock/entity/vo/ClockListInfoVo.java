package com.lec.clock.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClockListInfoVo {
    private String nickname;

    private String avatar;

    private Integer status;

    private Integer totalDuration;

    private Integer targetDuration;
}
