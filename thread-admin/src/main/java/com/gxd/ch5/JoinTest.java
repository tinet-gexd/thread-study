package com.gxd.ch5;

public class JoinTest {

    static String[] start = {"A","B","C"};
    static Thread[] threads = new Thread[3];
    static class PrintTask implements Runnable{

        String print ;
        int il;
        public PrintTask(String print, int i) {
            this.print = print;
            this.il = il;
        }

        public PrintTask() {
        }

        @Override
        public void run() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(print);

            for (int i = 0; i <3; i++) {
                try {
                    if (il !=i){

                        threads[i % 3].join();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {



        for (int i = 0; i <3; i++) {
            threads[i]= new Thread(new PrintTask( start[i] ,i));
            threads[i].start();
        }

    }

}
