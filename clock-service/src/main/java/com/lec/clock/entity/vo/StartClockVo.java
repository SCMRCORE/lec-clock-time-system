package com.lec.clock.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StartClockVo {
    private Long id;

    private LocalDateTime beginTime;

    private Integer status;
}
