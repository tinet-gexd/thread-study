package com.gxd.hystrix.stack;

import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 使用队列 先进先出
 * 实现栈 后进先出
 * @author admin
 */
@Component
public class StackTest<T> {

    private Queue<T> list = new LinkedList<>();

    /**
     * 入栈  x => 1,2,3,x     1 ,2   2,1   2 ,1 x  x , 2 , 1
     */
    public void push(T x){
        int size = list.size();
        list.offer(x);
        for (int i = 0; i < size; i++) {
            list.offer(list.poll());
        }

    }

    /**
     * 出栈   1,2,3,x => 1
     */
    public T pop(){
        return list.poll();
    }

    public T top(){
       return list.peek();
    }

    public boolean isEmpty(){
        return list.isEmpty();
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
