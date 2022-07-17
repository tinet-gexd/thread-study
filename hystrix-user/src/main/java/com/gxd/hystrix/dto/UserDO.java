package com.gxd.hystrix.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

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
