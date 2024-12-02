package com.lec.user.entity.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(description = "登录用户传输对象")
public class LoginUserDto {
    private String username;

    private String password;
}
