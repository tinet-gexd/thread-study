package com.gxd.hystrix.service.impl;

import com.gxd.hystrix.bean.UserDO;
import com.gxd.hystrix.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl {

    @Autowired
    UserMapper userMapper;

    public List<UserDO> queryAll() {
        log.info("user database");
        return userMapper.listAll();
    }

    public UserDO getUser(String name) {
        return userMapper.getUser(name);
    }
}
