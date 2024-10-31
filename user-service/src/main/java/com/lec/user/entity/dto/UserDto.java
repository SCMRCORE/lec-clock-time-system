package com.lec.user.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    //用户名
    private String username;
    //昵称
    private String nickname;
    //头像
    private String avatar;
    //年级
    private Integer grade;
    private String signature;
}
