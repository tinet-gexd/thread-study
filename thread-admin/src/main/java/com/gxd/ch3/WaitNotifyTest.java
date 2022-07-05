package com.gxd.ch3;

import com.sun.deploy.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class WaitNotifyTest {

    private static final int GOODS_NUM = 20;

    private static final int STUDENT_NUM = 100;

    private static List<String> arr = new ArrayList<>(GOODS_NUM);

    private final static AtomicInteger GET_FOOD_TIMES = new AtomicInteger(0);
    private final static AtomicInteger NO_FOOD_TIMES = new AtomicInteger(0);

    private final static CountDownLatch LATCH = new CountDownLatch(STUDENT_NUM);

    static class EatTask implements Runnable{

        private int count = 10;

        @Override
        public void run() {
            try {

                while (count > 0) {
//                System.out.println(Thread.currentThread().getName()+":开始拿物");
                    String goods = WaitNotifyTest.getGoods(20);
                    if (goods != null ) {
                        Thread.sleep(20);
                        System.out.println(Thread.currentThread().getName() + ":拿物"+goods);
                        WaitNotifyTest.putGoods(goods);
                        GET_FOOD_TIMES.incrementAndGet();
                    } else {
                        System.out.println(Thread.currentThread().getName() + ":没拿到物");
                        NO_FOOD_TIMES.incrementAndGet();
                    }
                    count --;
                }
//                System.out.println(Thread.currentThread().getName()+":结束");
            }catch (Exception e){
                System.out.println(e);
            }finally {
                LATCH.countDown();
            }
        }
    }



    public static String getGoods(int millis){
        synchronized (arr){
            if (millis < 0){
                while (arr.isEmpty()){
                    try {
                        arr.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return arr.remove(0);
            }else {
                String name = Thread.currentThread().getName();
                Long waitTime = System.currentTimeMillis() + millis;
                while (arr.isEmpty() && waitTime - System.currentTimeMillis() > 0 ){
                    try {
                        arr.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!arr.isEmpty()){
                    return arr.remove(0);
                }
                return null;
            }
        }
    }

    public static void putGoods(String goodsName) throws InterruptedException {
        synchronized (arr){
            arr.add(goodsName);
            arr.notifyAll();
        }
    }



    public static void main(String[] args) {

        for (int i = 0; i < GOODS_NUM /2; i++) {
            arr.add(i+"");
        }

        for (int i = 0; i <STUDENT_NUM ; i++) {
            new Thread(new EatTask(),"学生"+i).start();
        }
        try {
            LATCH.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("拿到物线程个数："+GET_FOOD_TIMES.get());
        System.out.println("未拿到物线程个数："+NO_FOOD_TIMES.get());

    }


}
