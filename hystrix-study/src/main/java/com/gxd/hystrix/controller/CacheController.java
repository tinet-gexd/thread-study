package com.gxd.hystrix.controller;


import com.gxd.hystrix.dto.UserDO;
import com.gxd.hystrix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CacheController {

    @Autowired
    private UserService userService;

    @GetMapping("queryAll")
    public List<UserDO> queryAll(){
        List<UserDO> list = userService.queryAll();
        return list;
    }

    @GetMapping("clear")
    public String clear(){
        try {
            userService.queryAllCacheClear();
        }catch (Exception ex){
            return "0";
        }
        return "1";
    }
}

