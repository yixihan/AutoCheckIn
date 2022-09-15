package com.yixihan.auto_check;

import com.yixihan.controller.MailSendController;
import com.yixihan.pojo.CookieData;
import com.yixihan.util.StringUtils;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author : yixihan
 * @create : 2022-09-15-13:57
 */
@Configuration
@EnableScheduling
@Log
public class AutoCheck {

    @Resource
    private CookieData cookieData;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private MailSendController mailSendController;


    @Scheduled(cron = "0 0 0 * * ?")
    private void autoCheckCordCloud() {
        if (!cookieData.getCordCloudFlag ()) {
            return;
        }
        log.info ("开始自动签到 cordCloud ...");
        HttpHeaders header = new HttpHeaders ();
        header.set ("Cookie", cookieData.getCordCloudCookie ());
        HttpEntity<Map<String, String>> entity = new HttpEntity<> (header);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity (cookieData.getCordCloudUrl (), entity, String.class);

        if (responseEntity.getStatusCode ().is2xxSuccessful ()) {
            String body = responseEntity.getBody ();
            body = StringUtils.decodeUnicode (body);
            log.info (body);

            mailSendController.sendMail (responseEntity.getBody ());
        } else {
            log.info ("签到失败, 请重新输入 cookie");
            mailSendController.sendMail ("签到失败, 请重新输入 cookie");
        }

    }

}
