package com.lec.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.clockcommon.entity.Result;
import com.lec.user.entity.pojo.Menu;

/**
 * (Menu)表服务接口
 *
 * @author makejava
 * @since 2023-03-14 18:48:47
 */
public interface MenuService extends IService<Menu> {

    Result getAllPermsById(Long id);
}

