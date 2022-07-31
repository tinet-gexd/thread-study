package com.gxd.hystrix.controller;

import com.gxd.hystrix.bean.UserDO;
import com.gxd.hystrix.service.InnerUserService;
import com.gxd.hystrix.service.InnerUserServiceImpl;
import com.gxd.hystrix.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@RestController
@Slf4j
public class HystrixController  {

    @Autowired
    UserService userService;

    @Autowired
    InnerUserService innerUserService;


    /**
     * @HystrixCommand 开启短路器功能
     *
     *
     */
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
        log.info(Thread.currentThread().getName() + "======getUser======");
        return userService.getUser(name);
    }

    @RequestMapping(value = "/getUserByService",method = RequestMethod.GET)
    public UserDO getUserByService(@RequestParam String name) throws Exception {
        log.info(Thread.currentThread().getName() + "======getUserByService======");
        return innerUserService.getUser(name);
    }

    @HystrixCommand(
            groupKey = "queryAllGroup",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize",value = "10")
            },
            commandKey = "queryAll",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY,value = "THREAD"),
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS,
                            value = "10000")
            }
    )
    @RequestMapping(value = "/queryAll",method = RequestMethod.GET)
    public List<UserDO> queryAll() {
        log.info(Thread.currentThread().getName() + "======queryAll======");
        return userService.queryAll();
    }


    @HystrixCommand(
            groupKey = "queryAllBySemaphoreGroup",
            commandKey = "queryAllBySemaphore",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy",value = "SEMAPHORE"),
                    @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests",value = "10")
            }
    )
    @RequestMapping(value = "/queryAllBySemaphore",method = RequestMethod.GET)
    public List<UserDO> queryAllBySemaphore() {
        log.info(Thread.currentThread().getName() + "======queryAllBySemaphore======");
        return userService.queryAll();
    }

    @RequestMapping(value = "/testSemaphore",method = RequestMethod.GET)
    public String testSemaphore() {
        int count = 11;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            new Thread(){
                @SneakyThrows
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("========"+currentThread().getName()+".queryAllBySemaphore()");
                    queryAllBySemaphore();
                }
            }.start();
            countDownLatch.countDown();
        }
        return "testSemaphore";
    }



    @HystrixCommand(
            groupKey = "queryAllGroup",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize",value = "10")
            },
            commandKey = "queryAll",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY,value = "THREAD"),
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS,
                            value = "10000")
            }
    )
    @RequestMapping(value = "/queryAllThrowError",method = RequestMethod.GET)
    public void queryAllThrowError(HttpServletRequest request, HttpServletResponse response) {
        log.info(Thread.currentThread().getName() + "======queryAllThrowError======");
        throw new HystrixBadRequestException("queryAllThrowError bad request test");
    }


    @HystrixCommand(
            groupKey = "queryAllGroup",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize",value = "10")
            },
            commandKey = "queryAll",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY,value = "THREAD"),
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS,
                            value = "500")
            }
    )
    @RequestMapping(value = "/queryAllTimeOut",method = RequestMethod.GET)
    public void queryAllTimeOut(HttpServletRequest request, HttpServletResponse response) {
        log.info(Thread.currentThread().getName() + "======queryAllTimeOut======");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @HystrixCommand(
            fallbackMethod = "queryAllErrorHandler",
            groupKey = "queryAllGroup",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize",value = "10")
            },
            commandKey = "queryAll",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY,value = "THREAD"),
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS,
                            value = "500")
            }
    )
    @RequestMapping(value = "/queryAllError",method = RequestMethod.GET)
    public String queryAllError(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info(Thread.currentThread().getName() + "======queryAllError======");
        throw new Exception("queryAllError");
    }

    public String queryAllErrorHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info(Thread.currentThread().getName() + "======queryAllErrorHandler======");
        return "queryAllErrorHandler";
    }


}
