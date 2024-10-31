package com.lec.user.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterUserDto {
    private String username;
    //昵称
    private String nickname;
    //密码
    private String password;
    //年级
    private Integer grade;

    private Integer code;

    private String email;
}
