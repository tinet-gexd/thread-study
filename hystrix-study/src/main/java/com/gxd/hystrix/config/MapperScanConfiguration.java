package com.gxd.hystrix.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.gxd.hystrix.mapper")
public class MapperScanConfiguration {


}
