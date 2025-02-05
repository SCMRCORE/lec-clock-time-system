package com.lec.clock.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Other {
//这个类专门用来当补丁，用于获取一些当时没设计好的变量
    //用户id
    private Long id;
    //年级
    private Integer grade;
}
