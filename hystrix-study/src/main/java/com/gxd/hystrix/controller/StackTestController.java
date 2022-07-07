package com.gxd.hystrix.controller;

import com.alibaba.fastjson.JSON;
import com.gxd.hystrix.stack.StackTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/stack")
@Slf4j
public class StackTestController {

    @Autowired
    private StackTest<Integer> stackTest ;

    @ResponseBody
    @GetMapping("/push")
    public String push(@RequestParam Integer[] x){
        for (Integer integer : x) {
            stackTest.push(integer);
        }
        log.info("x:{},stackTest:{}",x,stackTest);
        return JSON.toJSONString(stackTest);
    }

    @ResponseBody
    @GetMapping("/pop")
    public Integer pop(){
        Integer pop = stackTest.pop();
        log.info("pop:{},stackTest:{}",pop,stackTest);
        return pop;
    }

    @ResponseBody
    @GetMapping("/top")
    public Integer top(){
        Integer top = stackTest.top();
        log.info("top:{},stackTest:{}",top,stackTest);
        return top;
    }

    @ResponseBody
    @GetMapping("/isEmpty")
    public boolean isEmpty(){
        boolean isEmpty = stackTest.isEmpty();
        log.info("isEmpty:{},stackTest:{}",isEmpty,stackTest);
        return isEmpty;
    }
}
