package com.gxd.hystrix.service;

import com.gxd.hystrix.bean.UserDO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "HYSTRIX-USER",path = "/feign",url = "http://localhost:8089")
public interface UserService {

    @GetMapping("/queryAll")
    List<UserDO> queryAll();

    @GetMapping("/getUser")
    UserDO getUser(@RequestParam("name") String name);

    @PostMapping("/saveUser")
    String saveUser(@RequestBody UserDO userDO);

    @PutMapping("/deleteUser")
    String deleteUser(@RequestBody UserDO userDO);


}
