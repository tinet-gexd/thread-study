package com.gxd.hystrix.controller;

import com.gxd.hystrix.bean.UserDO;
import com.gxd.hystrix.mapper.UserMapper;
import com.gxd.hystrix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feign")
public class UserServiceApiController implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @GetMapping("/queryAll")
    public List<UserDO> queryAll() {
        return userMapper.listAll();
    }

    @Override
    @GetMapping("/getUser")
    public UserDO getUser(@RequestParam String name){
        return userMapper.getUser(name);
    }

    @Override
    @PostMapping("/saveUser")
    public String saveUser(@RequestBody UserDO userDO) {
        return userMapper.saveUser(userDO);
    }

    @Override
    @PutMapping("/deleteUser")
    public String deleteUser(@RequestBody UserDO userDO) {
        return userMapper.deleteUser(userDO);
    }
}
