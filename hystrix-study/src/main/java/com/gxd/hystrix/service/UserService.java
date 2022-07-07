package com.gxd.hystrix.service;

import com.gxd.hystrix.dto.UserDO;
import com.gxd.hystrix.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Cacheable(cacheNames = "queryAll" )
    public List<UserDO> queryAll() {
        log.info("user database");
        return userMapper.listAll();
    }

    @CacheEvict(cacheNames = "queryAll")
    public void queryAllCacheClear() {
        log.info("clear cache");
    }

    public UserDO getUser(String name) {
        return userMapper.getUser(name);
    }
}
