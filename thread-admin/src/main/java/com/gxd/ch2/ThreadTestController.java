package com.gxd.ch2;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author admin
 */
@Controller
@RequestMapping(value = "thread", method = RequestMethod.GET)
public class ThreadTestController {


    @ResponseBody
    @RequestMapping(value = "/testRunning", method = RequestMethod.POST)
    public Object testRunning() {
        System.out.println(Thread.currentThread().getName() + ":begin");
        ThreadTestRunning threadTestRunning = new ThreadTestRunning(0);
        threadTestRunning.setName("ThreadTestRunning");
        threadTestRunning.start();
        try {
            Thread.sleep(1000);
            threadTestRunning.setRunning(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ":end");
        return "ok";
    }

    @ResponseBody
    @RequestMapping(value = "/testInterrupted", method = RequestMethod.POST)
    public Object testInterrupted() {
        System.out.println(Thread.currentThread().getName() + ":begin");
        ThreadTestInterrupted threadTestInterrupted = new ThreadTestInterrupted(0);
        threadTestInterrupted.setName("ThreadTestInterrupted");
        threadTestInterrupted.start();
        try {
            Thread.sleep(100);
            System.out.println(Thread.currentThread().getName() + " set interrupt true before, i: " + threadTestInterrupted.getI());
            threadTestInterrupted.interrupt();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " set running true after, i: " + threadTestInterrupted.getI());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ":end");
        return "ok";
    }

    @ResponseBody
    @RequestMapping(value = "/testVolatile", method = RequestMethod.POST)
    public Object testVolatile() {
        System.out.println(Thread.currentThread().getName() + ":begin");
        ThreadTestVolatile threadTestVolatile = new ThreadTestVolatile(0);
        threadTestVolatile.setName("threadTestVolatile");
        threadTestVolatile.start();
        try {
            Thread.sleep(100);
            threadTestVolatile.setA("1");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ":end");
        return "ok";
    }


    @Data
    class ThreadTestRunning extends Thread {

        private boolean running = true;

        private int i;

        public ThreadTestRunning() {
            super();
        }

        public ThreadTestRunning(int i) {
            this.i = i;
        }

        public void setRunning(boolean running) {
            System.out.println(Thread.currentThread().getName() + " set running false before, i: " + i);
            this.running = running;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " set running false after, i: " + i);
        }

        @Override
        public void run() {
            while (running) {
//                try {
                i = i + 1;
//                    System.out.println(Thread.currentThread().getName()+" running i:" + i);
//                    sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
            System.out.println(Thread.currentThread().getName() + " end i: " + i);
        }
    }

    @Data
    class ThreadTestInterrupted extends Thread {

        private int i;

        public ThreadTestInterrupted() {
            super();
        }

        public ThreadTestInterrupted(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                i = i + 1;
//                    System.out.println(Thread.currentThread().getName()+" running i:" + i);
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
//                    System.out.println(Thread.currentThread().getName()+" interrupt 1:" + isInterrupted());
                    interrupt();
//                    Thread.interrupted();
//                    System.out.println(Thread.currentThread().getName()+" interrupt 2:" + isInterrupted());
                }
            }
            System.out.println(Thread.currentThread().getName() + " end i: " + i);
        }
    }

    @Data
    class ThreadTestVolatile extends Thread {

        private  int i;

        private volatile String a = "0";

        public ThreadTestVolatile() {
            super();
        }

        public ThreadTestVolatile(int i) {
            this.i = i;
        }

        public void setA(String a){
            System.out.println(Thread.currentThread().getName() + " set Volatile true before, i: " + i);
            this.a = a;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " set Volatile true after, i: " + i);
        }

        @Override
        public void run() {
            while (!"1".equals(a)) {
                try {
                    i = i + 1;
//                    System.out.println(Thread.currentThread().getName()+" running i:" + i);
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
//                    System.out.println(Thread.currentThread().getName()+" interrupt 1:" + isInterrupted());
                    interrupt();
//                    Thread.interrupted();
//                    System.out.println(Thread.currentThread().getName()+" interrupt 2:" + isInterrupted());
                }
            }
            System.out.println(Thread.currentThread().getName() + " end i: " + i);
        }
    }

}
