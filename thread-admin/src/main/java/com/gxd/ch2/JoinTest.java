package com.gxd.ch2;

public class JoinTest {


    static class Task1 implements Runnable{

        /**
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    public static void main(String[] args) throws InterruptedException {

        Integer a = new Integer(128);
        Integer b = 128;
        Integer d = 128;
        int c =128;
        System.out.println("a==b"+(b==a));
        System.out.println(a==c);
        System.out.println(b==c);

        System.out.println(b==d);

    }
}
