package com.yixihan.auto_check;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yixihan.controller.MailSendController;
import com.yixihan.pojo.CookieData;
import com.yixihan.pojo.cordCloud.CordCloud;
import com.yixihan.pojo.cordCloud.CordCloudMsg;
import com.yixihan.util.StringUtils;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

/**
 * @author : yixihan
 * @create : 2022-09-15-13:57
 */
@Configuration
@Log
public class AutoCheck {

    @Resource
    private CookieData cookieData;

    @Resource
    private MailSendController mailSendController;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Scheduled(cron = "0 1 0 * * ?")
    public void autoCheckInCordCloud() {
        Map<Object, Object> map = redisTemplate.opsForHash ().entries (cookieData.getCordCloudName ());

        for (Map.Entry<Object, Object> entry : map.entrySet ()) {
            CordCloud cordCloud = (CordCloud) entry.getValue ();
            log.info ("用户 " + cordCloud.getIsSendEmail () + " 是否已开启自动签到 : " + cordCloud.getIsCheckIn ());
            if (cordCloud.getIsCheckIn ()) {
                log.info ("开始自动签到用户 " + cordCloud.getSendEmail () + " ...");
                checkInCordCloud (cordCloud);
                log.info ("用户 " + cordCloud.getSendEmail () + " 自动签到完成...");
            }
        }
    }

    private void checkInCordCloud(CordCloud cordCloud) {
        // 登录, 设置 cookie
        String cookie = loginCordCloud (cordCloud);
        cordCloud.setCookie (cookie);

        // 签到
        HttpResponse response = HttpRequest.post (cookieData.getCordCloudCheckIn ()).header ("cookie", cordCloud.getCookie ()).execute ();

        JSONObject msg = JSONUtil.parseObj (response.body ());
        if (response.getStatus () == 200) {
            String message = msg.getInt ("ret") == 0 ? msg.getStr ("msg") : buildEmailMessage (msg);
            log.info (message);
            if (cordCloud.getIsSendEmail ()) {
                mailSendController.sendMail (message, cordCloud.getSendEmail ());
                log.info ("邮件发送成功");
            }
        } else {
            log.warning ("签到失败!");
            log.warning (msg.getStr ("msg"));
            mailSendController.sendMail ("cordCloud 自动签到签到失败, 失败原因 : " + msg.getStr ("msg"), cordCloud.getSendEmail ());
        }
    }

    private String buildEmailMessage(JSONObject msg) {
        StringBuilder sb = new StringBuilder ();
        sb.append ("签到成功! ").append (StringUtils.decodeUnicode (msg.getStr ("msg"))).append ("\n");
        sb.append ("当前套餐总流量 : ").append (msg.getStr ("traffic")).append ("\n");
        JSONObject trafficInfo = JSONUtil.parseObj (msg.getStr ("trafficInfo"));
        sb.append ("今日已用流量 : ").append (trafficInfo.getStr ("todayUsedTraffic")).append ("\n");
        sb.append ("过去已用流量 : ").append (trafficInfo.getStr ("lastUsedTraffic")).append ("\n");
        sb.append ("剩余流量 : ").append (trafficInfo.getStr ("unUsedTraffic"));
        return sb.toString ();
    }

    /**
     * 登录 cordCloud, 并返回 cookie
     *
     * @param cordCloud cordCloud
     * @return cookie
     * @throws RuntimeException 响应状态码不为 200 时抛出 | 登录用户信息错误时抛出
     */
    private String loginCordCloud(CordCloud cordCloud) {
        String form = JSONUtil.toJsonStr (JSONUtil.parseObj (cordCloud.getUser (), false, true));

        HttpResponse response = HttpRequest.post (cookieData.getCordCloudLogin ()).body (form).execute ();
        if (response.getStatus () == 200) {
            CordCloudMsg msg = JSONUtil.toBean (JSONUtil.parseObj (response.body ()), CordCloudMsg.class);
            if (msg.getRet () == 1) {
                String cookie = setCookie (response.getCookies ());
                log.info ("登录成功, 正在签到...");
                log.info ("cookie :");
                log.info (cookie);
                return cookie;
            } else {
                log.warning ("登录失败!");
                log.warning (msg.getMsg ());
                mailSendController.sendMail ("cordCloud 自动签到登录失败, 失败原因 : " + msg.getMsg (), cordCloud.getSendEmail ());
                throw new RuntimeException ("cordCloud 自动签到登录失败, 失败原因 : " + msg.getMsg ());
            }
        } else {
            log.warning ("未知错误!");
            log.warning (response.body ());

            throw new RuntimeException ("登录失败, 遇到未知错误, 请联系管理员!");
        }
    }

    /**
     * 设置 cookie
     *
     * @param cookies response 中的 cookies
     * @return cookie
     */
    private String setCookie(List<HttpCookie> cookies) {
        StringBuilder sb = new StringBuilder ();

        for (HttpCookie cookie : cookies) {
            sb.append (cookie).append ("; ");
        }

        return sb.toString ();
    }


}
