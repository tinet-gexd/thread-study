package com.gxd.ch4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {

    private  Integer i = 0;
    private static Lock lock = new ReentrantLock();

    static class AddNum implements Runnable{
        private LockTest lockTest ;

        public AddNum(LockTest lockTest) {
            this.lockTest =lockTest;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                for (int j = 0; j < 5000; j++) {
                    lockTest.addI();
                }
                System.out.println(Thread.currentThread().getName()
                        +" i =  "+lockTest.getI());
            }finally {
                lock.unlock();
            }
        }
    }

    private void addI() {
        i++;
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    public static void main(String[] args) throws InterruptedException {
        LockTest lockTest = new LockTest();

        for (int i = 0; i < 5; i++) {
            new Thread(new AddNum(lockTest)).start();
        }


        Thread.sleep(1000);
        System.out.println(lockTest.getI());

    }
}
