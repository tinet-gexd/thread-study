package com.gxd.hystrix.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDO {

   private Long id;
   private String name;
   private Integer age;
   private String email;
   private String address;
}
