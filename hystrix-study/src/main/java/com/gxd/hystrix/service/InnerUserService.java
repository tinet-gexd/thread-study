package com.gxd.hystrix.service;

import com.gxd.hystrix.bean.UserDO;

public interface InnerUserService {

    UserDO getUser(String name) throws Exception;
}
