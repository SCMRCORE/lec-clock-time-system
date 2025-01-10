package com.lec.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lec.user.entity.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * (User)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-14 15:56:53
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where id = #{id}")
    User getById(Long id);

    @Select("select * from user where username = #{username} and password = #{password}")
    User checkUserExit(String username, String password);

    @Select("select * from user where username = #{username}")
    User getUserByUsername(String username);

    @Update("update user set `lec-clock-in`.user.avatar = #{url} where id = #{id}")
    void updateImage(Long id, String url);

    List<User> selectIds(List<Long> ids);

    @Select("select user.email from `lec-clock-in`.user where email = #{email}")
    String hasEmail(String email);
}

