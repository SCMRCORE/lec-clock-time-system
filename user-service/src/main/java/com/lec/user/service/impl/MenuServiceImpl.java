package com.lec.user.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clockcommon.entity.Result;
import com.clockcommon.utils.BeanCopyUtils;
import com.lec.user.entity.pojo.Menu;
import com.lec.user.entity.vo.MenuInfoVo;
import com.lec.user.mapper.MenuMapper;
import com.lec.user.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-03-14 18:48:47
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public Result getAllPermsById(Long id) {
        MenuMapper mapper = getBaseMapper();
        List<Menu> menus = null;
        if(id == 1L){
            menus = list();
        }else{
            menus = mapper.selectMenusByUserId(id);
        }
        List<MenuInfoVo> menuInfoVos = BeanCopyUtils.copyBeanList(menus, MenuInfoVo.class);
        return Result.okResult(menuInfoVos);
    }
}

