package com.gxd.hystrix.controller;

import com.gxd.hystrix.bean.UserDO;
import com.gxd.hystrix.service.InnerUserService;
import com.gxd.hystrix.service.InnerUserServiceImpl;
import com.gxd.hystrix.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
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

    @Autowired
    InnerUserService innerUserService;

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
                            value = "10000")
            }
    )
    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public UserDO getUser(@RequestParam String name) throws Exception {
        return userService.getUser(name);
    }

    @RequestMapping(value = "/getUserByService",method = RequestMethod.GET)
    public UserDO getUserByService(@RequestParam String name) throws Exception {
        log.info(Thread.currentThread().getName() + "======getUserByService======");
        return innerUserService.getUser(name);
    }

}
