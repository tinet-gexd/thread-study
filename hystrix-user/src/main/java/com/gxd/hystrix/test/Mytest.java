package com.gxd.hystrix.test;

import com.gxd.hystrix.service.UserService;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.concurrent.CountDownLatch;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class Mytest {

    //线程数
    private int count = 11;


    CountDownLatch countDownLatch = new CountDownLatch(count);

    @Autowired
    UserService userService;


    @Test
    public void test(){
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
                    System.out.println("========"+currentThread().getName()+".getUser(),result:"+userService.getUser("gxd"));
                }
            }.start();
            countDownLatch.countDown();
        }
        userService.queryAll();
    }


}
