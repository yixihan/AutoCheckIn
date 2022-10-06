package com.yixihan;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yixihan.controller.MailSendController;
import com.yixihan.pojo.CookieData;
import com.yixihan.pojo.cordCloud.CordCloudMsg;
import com.yixihan.pojo.cordCloud.User;
import com.yixihan.util.StringUtils;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.net.HttpCookie;
import java.util.List;

/**
 * @author : yixihan
 * @create : 2022-09-15-21:29
 */
@SpringBootTest
@Log
public class LoginTest {

    @Resource
    private CookieData cookieData;

    @Resource
    private MailSendController mailSendController;


    @Test
    public void login() {
        User user = new User ("3113788997@qq.com", "qqzst12345678");
        String form = JSONUtil.toJsonStr (JSONUtil.parseObj (user, false, true));

        HttpResponse response = HttpRequest.post (cookieData.getCordCloudLogin ()).body (form).execute ();
        if (response.getStatus () == 200) {
            CordCloudMsg msg = JSONUtil.toBean (JSONUtil.parseObj (response.body ()), CordCloudMsg.class);
            if (msg.getRet () == 1) {
                log.info ("登录成功, 正在签到...");
                log.info ("cookie :");
                String cookie = setCookie (response.getCookies ());
                log.info (cookie);
            } else {
                log.warning ("登录失败!");
                log.warning (msg.getMsg ());
            }
        } else {
            log.warning ("未知错误!");
            log.warning (response.body ());

            throw new RuntimeException ("登录失败, 遇到未知错误, 请联系管理员!");
        }
    }

    private String setCookie(List<HttpCookie> cookies) {
        StringBuilder sb = new StringBuilder ();

        for (HttpCookie cookie : cookies) {
            sb.append (cookie).append ("; ");
        }

        return sb.toString ();
    }

//    @Test
//    void buildEmailMessage() {
//        String body = "{\"msg\":\"\\u83b7\\u5f97\\u4e86 438MB \\u6d41\\u91cf.\",\"unflowtraffic\":385235288064,\"traffic\":\"358.78GB\",\"trafficInfo\":{\"todayUsedTraffic\":\"8.99MB\",\"lastUsedTraffic\":\"251.77GB\",\"unUsedTraffic\":\"107GB\"},\"ret\":1}";
//        JSONObject msg = JSONUtil.parseObj (body);
//        JSONObject trafficInfo = JSONUtil.parseObj (msg.getStr ("trafficInfo"));
//        System.out.println (msg);
//        StringBuilder sb = new StringBuilder ();
//        sb.append ("签到成功! ").append (StringUtils.decodeUnicode (msg.getStr ("msg"))).append ("\n");
//        sb.append ("当前套餐总流量 : ").append (msg.getStr ("traffic")).append ("\n");
//        sb.append ("今日已用流量 : ").append (trafficInfo.getStr ("todayUsedTraffic")).append ("\n");
//        sb.append ("过去已用流量 : ").append (trafficInfo.getStr ("lastUsedTraffic")).append ("\n");
//        sb.append ("剩余流量 : ").append (trafficInfo.getStr ("unUsedTraffic"));
//        System.out.println (sb);
//    }

    @Test
    void testSendMessage() {
        String form = "{\n" + "\t\"user_id\": \"3113788997\",\n" + "\t\"message\": \"你好~\"\n" + "}";
        HttpResponse response = HttpRequest.post ("http://175.24.229.41:5700/send_private_msg").body (form).execute ();
        if (response.getStatus () == 200) {
            System.out.println (response.body ());
        } else {
            System.out.println (response.getStatus ());
        }
    }

    @Test
    void testSenMail() {
        JSONObject msg = JSONUtil.parseObj ("{\"msg\":\"\\u83b7\\u5f97\\u4e86 282MB \\u6d41\\u91cf.\",\"unflowtraffic\":385530986496,\"traffic\":\"359.05GB\",\"trafficInfo\":{\"todayUsedTraffic\":\"0B\",\"lastUsedTraffic\":\"252.25GB\",\"unUsedTraffic\":\"106.81GB\"},\"ret\":1}");
        String message = msg.getInt ("ret") == 0 ? msg.getStr ("msg") : buildEmailMessage (msg);
        log.info (message);

        mailSendController.sendMail (message, "3113788997@qq.com", "test");
        log.info ("邮件发送成功");

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
}
