package com.lec.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lec.user.entity.pojo.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * (Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-14 18:48:47
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    @Select("select perms, des\n" +
            "from user_role ur\n" +
            "left join role_menu rm on ur.role_id = rm.role_id\n" +
            "left join menu m on rm.menu_id = m.id\n" +
            "where user_id = #{id} and m.del_flag = 0")
    List<Menu> selectMenusByUserId(Long id);

}

