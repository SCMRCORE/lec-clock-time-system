package com.lec.clock.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserClockInfoVo {
    private Integer status;

    private Integer totalDuration;

    private Integer targetDuration;
}
