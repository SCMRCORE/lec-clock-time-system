package com.lec.user.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfoVo {
    private Long id;
    //用户名
    private String username;
    //昵称
    private String nickname;
    //头像
    private String avatar;
    //年级
    private Integer grade;
    //签名
    private String signature;
}
