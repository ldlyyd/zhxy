package com.ldl.zhxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_admin")
public class Admin {
   @TableId(value = "id", type = IdType.AUTO)
   private int id;
   private String name;
   private String gender;
   private String password;
   private String email;
   private String telephone;
   private String address;
   private String portraitPath;
}
