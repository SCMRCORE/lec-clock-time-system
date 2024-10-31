package com.lec.user.controller;


import com.clockcommon.entity.Result;
import com.lec.user.service.MenuService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu")
@Api(tags = "权限相关接口")
public class MenuController {

    @Autowired
    MenuService menuService;
    @GetMapping("/{id}")
    public Result getAllPermsById(@PathVariable("id") Long id) {
        return menuService.getAllPermsById(id);
    }
}
