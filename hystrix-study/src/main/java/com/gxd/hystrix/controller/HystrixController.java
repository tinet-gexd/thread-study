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
import javax.xml.ws.http.HTTPException;
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
     * Command属性
     * execution.isolation.strategy  执行的隔离策略
     * THREAD 线程池隔离策略  独立线程接收请求
     * SEMAPHORE 信号量隔离策略 在调用线程上执行
     * <p>
     * execution.isolation.thread.timeoutInMilliseconds  设置HystrixCommand执行的超时时间，单位毫秒
     * execution.timeout.enabled  是否启动超时时间，true，false
     * execution.isolation.semaphore.maxConcurrentRequests  隔离策略为信号量的时候，该属性来配置信号量的大小，最大并发达到信号量时，后续请求被拒绝
     * <p>
     * circuitBreaker.enabled   是否开启断路器功能
     * circuitBreaker.requestVolumeThreshold  该属性设置在滚动时间窗口中，断路器的最小请求数。默认20，如果在窗口时间内请求次数19，即使19个全部失败，断路器也不会打开
     * circuitBreaker.sleepWindowInMilliseconds    改属性用来设置当断路器打开之后的休眠时间，休眠时间结束后断路器为半开状态，断路器能接受请求，如果请求失败又重新回到打开状态，如果请求成功又回到关闭状态
     * circuitBreaker.errorThresholdPercentage  该属性设置断路器打开的错误百分比。在滚动时间内，在请求数量超过circuitBreaker.requestVolumeThreshold,如果错误请求数的百分比超过这个比例，断路器就为打开状态
     * circuitBreaker.forceOpen   true表示强制打开断路器，拒绝所有请求
     * circuitBreaker.forceClosed  true表示强制进入关闭状态，接收所有请求
     * <p>
     * metrics.rollingStats.timeInMilliseconds   设置滚动时间窗的长度，单位毫秒。这个时间窗口就是断路器收集信息的持续时间。断路器在收集指标信息的时会根据这个时间窗口把这个窗口拆分成多个桶，每个桶代表一段时间的指标，默认10000
     * metrics.rollingStats.numBuckets   滚动时间窗统计指标信息划分的桶的数量，但是滚动时间必须能够整除这个桶的个数，要不然抛异常
     * <p>
     * requestCache.enabled   是否开启请求缓存，默认为true
     * requestLog.enabled 是否打印日志到HystrixRequestLog中，默认true
     *
     * @HystrixCollapser 请求合并
     * maxRequestsInBatch  设置一次请求合并批处理中允许的最大请求数
     * timerDelayInMilliseconds  设置批处理过程中每个命令延迟时间
     * requestCache.enabled   批处理过程中是否开启请求缓存，默认true
     * <p>
     * threadPoolProperties
     * threadPoolProperties 属性
     * coreSize   执行命令线程池的最大线程数，也就是命令执行的最大并发数，默认10
     */
    //========================================1隔离策略============================
    @HystrixCommand(
            groupKey = "getUserGroup",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize",value = "10")
            },
            commandKey = "getUser",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy",value = "THREAD"),
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS,
                            value = "3000")
            }
    )
    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public UserDO getUser(@RequestParam String name) throws Exception {
        log.info(Thread.currentThread().getName() + "======getUser======");
        return userService.getUser(name);
    }

    /**
     * todo 1 SEMAPHORE 信号量隔离策略 在调用线程上执行
     */
    @RequestMapping(value = "/queryAllBySemaphore",method = RequestMethod.GET)
    public String queryAllBySemaphore() {
        log.info(Thread.currentThread().getName() + "======queryAllBySemaphore======");
        //线程数
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
                    System.out.println("queryAllBySemaphore======="+currentThread().getName());
                    innerUserService.queryAllBySemaphore();
                }
            }.start();
            countDownLatch.countDown();
        }
        return "innerUserService.queryAllBySemaphore";
    }

    //========================================2降级&UserServiceApiController============================
    /**
     * 降级
     * fallbackMethod
     */
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

    //========================================3服务监控各种情况============================
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
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求BadRequest
     */
    @HystrixCommand(
            groupKey = "queryAllGroup",
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


}
