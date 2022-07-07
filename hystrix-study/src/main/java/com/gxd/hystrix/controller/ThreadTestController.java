package com.gxd.hystrix.controller;

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
        System.out.println(Thread.currentThread().getName()+":begin");
        ThreadTestRunning threadTestRunning = new ThreadTestRunning(0);
        threadTestRunning.setName("ThreadTestRunning");
        threadTestRunning.start();
        try {
            Thread.sleep(10);
            threadTestRunning.setRunning(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+":end");
        return "ok";
    }

    @ResponseBody
    @RequestMapping(value = "/testInterrupted", method = RequestMethod.POST)
    public Object testInterrupted() {
        System.out.println(Thread.currentThread().getName()+":begin");
        ThreadTestInterrupted threadTestInterrupted = new ThreadTestInterrupted(0);
        threadTestInterrupted.setName("ThreadTestInterrupted");
        threadTestInterrupted.start();
        try {
            Thread.sleep(10);
            threadTestInterrupted.interrupt();
            System.out.println(Thread.currentThread().getName()+" getThreadTestInterrupted i :"+threadTestInterrupted.getI());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+":end");
        return "ok";
    }

    @Data
    class ThreadTestRunning extends Thread {

        private boolean running = true;

        private int i;

        private int j;

        public ThreadTestRunning() {
            super();
        }

        public ThreadTestRunning(int i) {
            this.i = i;
        }

        public void setRunning(boolean running) {
            this.running = running;
            this.j = i;
        }

        @Override
        public void run() {
            while (running) {
//                try {
                    i++;
//                    System.out.println(Thread.currentThread().getName()+" running i:" + i);
//                    sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
            System.out.println(Thread.currentThread().getName()+" end i: " + i + "j:"+j);
        }
    }

    @Data
    class ThreadTestInterrupted extends Thread {

        private int i;

        private int j;

        public ThreadTestInterrupted() {
            super();
        }

        public ThreadTestInterrupted(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
//                try {
                    i++;
//                    System.out.println(Thread.currentThread().getName()+" running i:" + i);
//                    sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    System.out.println(Thread.currentThread().getName()+" interrupt 1:" + isInterrupted());
//                    interrupt();
//                    Thread.interrupted();
//                    System.out.println(Thread.currentThread().getName()+" interrupt 2:" + isInterrupted());
//                }
            }
            System.out.println(Thread.currentThread().getName()+" end i: " + i + "j:"+j);
        }
    }


}
