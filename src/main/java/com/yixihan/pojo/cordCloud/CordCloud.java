package com.yixihan.pojo.cordCloud;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : yixihan
 * @create : 2022-09-16-8:06
 */
@Data
@AllArgsConstructor
public class CordCloud {

    /**
     * cordCloud 用户
     */
    private User user;

    /**
     * cordCloud Cookie
     */
    private volatile String cookie;

    /**
     * cordCloud 签到记录接受者 (邮箱接收)
     */
    private String sendEmail;

    /**
     * 是否启用自动签到
     */
    private Boolean isCheckIn = true;

    /**
     * 是否每日发送邮件
     */
    private Boolean isSendEmail = true;

    public CordCloud() {
        this.isCheckIn = true;
        this.isSendEmail = true;
    }

    public CordCloud(User user) {
        this();
        this.user = user;
    }

    public CordCloud(User user, String sendEmail) {
        this(user);
        this.sendEmail = sendEmail;
    }
}
