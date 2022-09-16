package com.yixihan.pojo.cordCloud;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : yixihan
 * @create : 2022-09-16-13:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    /**
     * cordCloud 登录邮箱
     */
    private String email;

    /**
     * cordCloud 登录密码
     */
    private String passwd;

}
