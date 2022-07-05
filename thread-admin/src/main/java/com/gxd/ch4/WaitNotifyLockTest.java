package com.gxd.ch4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WaitNotifyLockTest {
    public final static String CITY = "ShangHai";
    private int km;/*快递运输里程数*/
    private String site;/*快递到达地点*/

    private Lock kmLock = new ReentrantLock();
    private Lock siteLock = new ReentrantLock();

    private Condition kmCondition = kmLock.newCondition();
    private Condition siteCondition = siteLock.newCondition();

    public WaitNotifyLockTest() {
    }

    public WaitNotifyLockTest(int km, String site) {
        this.km = km;
        this.site = site;
    }

    /* 变化公里数，然后通知处于wait状态并需要处理公里数的线程进行业务处理*/
    public void changeKm(){
        kmLock.lock();
        try {
            km = 100;
            kmCondition.signalAll();
        }finally {
            kmLock.unlock();
        }
    }

    /* 变化地点，然后通知处于wait状态并需要处理地点的线程进行业务处理*/
    public  void changeSite(){



    }

    /*线程等待公里的变化*/
    public  void waitKm(){
        kmLock.lock();
        try {
            while (km < 100){
                kmCondition.await();
            }
            //do sth
            Thread.sleep(1000);
            System.out.println( Thread.currentThread().getName() + "接收到通知了，km:"+km);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            kmLock.unlock();
        }

    }

    /*线程等待目的地的变化*/
    public  void waitSite(){

    }
}
