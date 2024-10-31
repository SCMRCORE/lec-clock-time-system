package com.lec.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lec.user.entity.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


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
}

