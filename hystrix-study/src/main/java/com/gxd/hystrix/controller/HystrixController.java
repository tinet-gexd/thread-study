package com.gxd.hystrix.controller;

import com.gxd.hystrix.bean.UserDO;
import com.gxd.hystrix.service.InnerUserService;
import com.gxd.hystrix.service.InnerUserServiceImpl;
import com.gxd.hystrix.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import com.netflix.hystrix.exception.HystrixBadRequestException;
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
    public void queryAllError(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info(Thread.currentThread().getName() + "======queryAllError======");
        throw new Exception("queryAllError");
    }
}
