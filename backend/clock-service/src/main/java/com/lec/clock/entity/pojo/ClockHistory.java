package com.lec.clock.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * (ClockHistory)表实体类
 *
 * @author makejava
 * @since 2023-03-14 16:03:25
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClockHistory {
    
    private Long id;
    //用户名
    private String username;
    //昵称
    private String nickname;
    //时长
    private Integer duration;
}

