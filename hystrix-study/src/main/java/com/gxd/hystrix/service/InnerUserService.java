package com.gxd.hystrix.service;

import com.gxd.hystrix.bean.UserDO;

import java.util.List;

public interface InnerUserService {

    UserDO getUser(String name) throws Exception;

    List<UserDO> queryAllBySemaphore();
}
