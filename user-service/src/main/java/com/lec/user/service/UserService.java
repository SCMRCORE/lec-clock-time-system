package com.lec.user.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.clockcommon.entity.Result;
import com.lec.user.entity.dto.LoginUserDto;
import com.lec.user.entity.dto.RegisterUserDto;
import com.lec.user.entity.dto.UpdateUserDto;
import com.lec.user.entity.dto.UserDto;
import com.lec.user.entity.pojo.User;
import org.springframework.web.multipart.MultipartFile;


/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2023-03-14 15:56:54
 */
public interface UserService extends IService<User> {

    Result login(LoginUserDto loginUserDto);

    Result register(RegisterUserDto registerUserDto);

    Result sendCode(String email);

    Result logout();

    Result getUserInfoById(Long id);

    Result updateUserInfo(UpdateUserDto updateUserDto);

    Result getUserByGrade(UserDto userDto);

    Result isDead();

    String uploadImage(MultipartFile image);
}

