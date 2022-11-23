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
    @Value("${cordCloud.login}")
    private String cordCloudLogin;

    /**
     * cordCloud 签到接口
     */
    @Value("${cordCloud.checkIn}")
    private String cordCloudCheckIn;

    /**
     * cordCloud 用户信息存入 redis 时使用的 key
     */
    @Value("${cordCloud.name}")
    private String cordCloudName = "cordCloud";

    @Value("${cordCloud.avg-cnt-name}")
    private String cordCloudAvgCntName;

    @Value("${cordCloud.avg-sum-name}")
    private String cordCloudAvgSumName;
}
