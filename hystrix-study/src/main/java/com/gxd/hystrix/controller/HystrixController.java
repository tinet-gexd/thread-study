package com.gxd.hystrix.controller;

import com.gxd.hystrix.bean.UserDO;
import com.gxd.hystrix.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.HystrixCommandFactory;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class HystrixController  {

    @Autowired
    UserService userService;

    public List<UserDO> queryAll() {
        return userService.queryAll();
    }

    @HystrixCommand(
            groupKey = "getUserGroup",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize",value = "10")
            },
            commandKey = "getUser",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY,value = "THREAD"),
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS,
                            value = "1000")
            }
    )
    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public UserDO getUser(@RequestParam String name){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userService.getUser(name);
    }


}
