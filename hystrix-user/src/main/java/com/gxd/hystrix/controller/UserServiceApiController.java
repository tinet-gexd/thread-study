package com.gxd.hystrix.controller;

import com.gxd.hystrix.bean.UserDO;
import com.gxd.hystrix.mapper.UserMapper;
import com.gxd.hystrix.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/feign")
@Slf4j
public class UserServiceApiController implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @GetMapping("/queryAll")
    public List<UserDO> queryAll() {
        log.info("===========UserServiceApiController."+Thread.currentThread().getName()+".listAll================");
        return userMapper.listAll();
    }


    @HystrixCommand(
            fallbackMethod = "getUserFallback1",
            groupKey = "UserServiceGroup",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize",value = "11")
            },
            commandKey = "getUser",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY,value = "THREAD"),
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS,
                            value = "3000")
            }
    )
    @Override
    @GetMapping("/getUser")
    public UserDO getUser(@RequestParam String name) throws Exception {
        log.info("===========getUser:"+Thread.currentThread().getName()+"================");
//        Random random = new Random(4);
//        if (random.nextInt() > 2 ){
//            throw new Exception("1111");
//        }
        return userMapper.getUser(name);
    }
    /**
     * 降级给还是可以降级
     */
    @HystrixCommand(
            groupKey = "UserServiceGroup",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize",value = "10")
            },
            commandKey = "getUser",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY,value = "THREAD"),
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS,
                            value = "3000")
            }
    )
    public UserDO getUserFallback1(String name) {
        log.info("===========降级方法getUserFallback1"+Thread.currentThread().getName()+"================");
//        return userMapper.getUser(name);
        UserDO userDO = new UserDO();
        userDO.setName("这是降级方法");
        return userDO;
    }


//    /**
//     * 降级给还是可以降级
//     */
//    public UserDO getUserFallback2(String name) {
//        log.info("===========再降级方法getUserFallback2"+Thread.currentThread().getName()+"================");
//        UserDO userDO = new UserDO();
//        userDO.setName("这是降级再降级方法");
//        return userDO;
//    }


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
