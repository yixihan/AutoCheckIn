package com.yixihan.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;

/**
 * @author : yixihan
 * @create : 2022-09-15-13:55
 */
@Component
@Data
public class CookieData {

    /**
     * cordCloud Cookie
     */
    @Value ("${cookie.cordCloud.cookie}")
    private String cordCloudCookie;

    /**
     * 是否开启 cordCloud 自动签到
     */
    @Value ("${cookie.cordCloud.isChecked}")
    private Boolean cordCloudFlag;

    /**
     * cordCloud 自动签到 url
     */
    @Value ("${cookie.cordCloud.url}")
    private String cordCloudUrl;

    @Email
    @Value ("${cookie.email}")
    private String email;
}
