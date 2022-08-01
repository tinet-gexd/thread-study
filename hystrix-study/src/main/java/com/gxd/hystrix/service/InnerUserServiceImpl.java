package com.gxd.hystrix.service;

import com.gxd.hystrix.bean.UserDO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class InnerUserServiceImpl implements InnerUserService{

    @Autowired
    UserService userService;

    @HystrixCommand(
            groupKey = "queryAllBySemaphoreGroup",
            commandKey = "queryAllBySemaphore",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy",value = "SEMAPHORE"),
                    @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests",value = "10")
            }
    )
    @Override
    public List<UserDO> queryAllBySemaphore() {
        log.info(Thread.currentThread().getName() + "======getUserByService======");
        return userService.queryAll();
    }

    @HystrixCommand(
            groupKey = "getUserGroup",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize",value = "10")
            },
            commandKey = "getUserByService",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY,value = "THREAD"),
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS,
                            value = "10000")
            }
    )
    @Override
    public UserDO getUser(String name) throws Exception {
        log.info(Thread.currentThread().getName() + "======getUserByService======");
        return userService.getUser(name);
    }
}
