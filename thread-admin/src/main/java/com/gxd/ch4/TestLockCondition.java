package com.gxd.ch4;

import java.util.concurrent.CountDownLatch;

public class TestLockCondition {


    static class KmTask implements Runnable{

        WaitNotifyLockTest waitNotifyLockTest;
        public KmTask(WaitNotifyLockTest waitNotifyLockTest) {
            this.waitNotifyLockTest = waitNotifyLockTest;
        }

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            System.out.println(name + "start。。。");
            waitNotifyLockTest.waitKm();
            System.out.println(name + "end。。。");
        }
    }


    public static void main(String[] args) throws InterruptedException {
        WaitNotifyLockTest waitNotifyLockTest = new WaitNotifyLockTest(0,"");
        System.out.println("start...");
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(new KmTask(waitNotifyLockTest), "thread-" + i);
            thread.start();
        }
        Thread.sleep(1000);
        waitNotifyLockTest.changeKm();
        System.out.println("end...");
    }


}
