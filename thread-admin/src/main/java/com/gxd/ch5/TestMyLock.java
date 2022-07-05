package com.gxd.ch5;

public class TestMyLock {

    private final static SelfLock lock = new SelfLock();


    static class Task1 implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "ready get lock");
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + "get lock and doing");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }finally {
                System.out.println(Thread.currentThread().getName() + "release lock");
                lock.unlock();
            }

        }
    }


    public static void main(String[] args) {

        for (int i = 0; i < 3; i++) {
            new Thread(new Task1()).start();
        }

//        try {
//            Thread.currentThread().join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


    }

}
