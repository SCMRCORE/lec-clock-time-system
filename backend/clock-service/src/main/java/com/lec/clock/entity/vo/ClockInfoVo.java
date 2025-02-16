package com.lec.clock.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClockInfoVo {

    private String nickname;

    private String avatar;

    private Integer status;

    private LocalDateTime beginTime;

    private Integer totalDuration;


    private Integer adjustTargetDuration;

}
