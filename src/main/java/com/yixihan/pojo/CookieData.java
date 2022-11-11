package com.yixihan.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author : yixihan
 * @create : 2022-09-15-13:55
 */
@Component
@Data
public class CookieData {

    /**
     * cordCloud 登录接口
     */
    @Value("${cookie.cordCloud.login}")
    private String cordCloudLogin;

    /**
     * cordCloud 签到接口
     */
    @Value("${cookie.cordCloud.checkIn}")
    private String cordCloudCheckIn;

    /**
     * cordCloud 用户信息存入 redis 时使用的 key
     */
    @Value("${cookie.cordCloud.name}")
    private String cordCloudName = "cordCloud";

    /**
     * cordCloud 用户信息存入 redis 时使用的 key
     */
    @Value("${cookie.newhope.name}")
    private String newhopeName = "newhope";

}
