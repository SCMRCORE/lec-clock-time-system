package com.lec.clock.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClockHistoryVo {
    private Long id;

    private String nickname;

    private Integer duration;
    //头像
    private String avatar;
    //年级
    private Integer grade;

}