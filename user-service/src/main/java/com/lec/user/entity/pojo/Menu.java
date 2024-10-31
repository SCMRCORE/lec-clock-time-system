package com.lec.user.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (Menu)表实体类
 *
 * @author makejava
 * @since 2023-03-14 18:48:47
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    
    private Long id;

    //权限
    private String perms;

    private String des;

    private Date createTime;
    //修改时间
    private Date updateTime;
}

