package com.lec.user.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginUserVo {
    private String token;

    private UserInfoVo userInfoVo;
}