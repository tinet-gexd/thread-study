package com.gxd.hystrix.mapper;


import com.gxd.hystrix.dto.UserDO;

import java.util.List;

public interface UserMapper {


    List<UserDO>  listAll();

    UserDO getUser(String name);
}
