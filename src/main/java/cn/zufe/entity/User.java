package cn.zufe.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class User {

    //id
    private Integer id;

    //用户名
    private String username;

    //密码
    @JSONField(serialize = false)
    private String password;

    //邮箱
    private String email;

    //电话
    private String phone;
}
