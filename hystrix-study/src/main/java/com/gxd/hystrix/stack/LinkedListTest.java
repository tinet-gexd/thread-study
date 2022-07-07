package com.gxd.hystrix.stack;


import java.util.LinkedList;

public class LinkedListTest {
    public static void main(String[] args) {

        LinkedList<String> objects = new LinkedList<>();
        objects.add("1");
        objects.add("2");
        objects.add("3");
        objects.add("4");
        objects.add("5");
        objects.add("6");
        objects.add("7");



        System.out.println(objects.subList(0,4));
         System.out.println(objects.subList(8,Math.min(objects.size(),8)));

    }
}
