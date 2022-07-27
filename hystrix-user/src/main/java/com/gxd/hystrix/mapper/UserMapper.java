package com.gxd.hystrix.mapper;


import com.gxd.hystrix.bean.UserDO;

import java.util.List;

public interface UserMapper {


    List<UserDO>  listAll();

    UserDO getUser(String name);

    String saveUser(UserDO userDO);

    String deleteUser(UserDO userDO);
}
