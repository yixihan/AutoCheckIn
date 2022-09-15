package com.yixihan.controller;

import com.yixihan.pojo.CookieData;
import lombok.extern.java.Log;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author : yixihan
 * @create : 2022-09-15-14:19
 */
@Component
@Log
public class MailSendController {

    @Resource
    private CookieData cookieData;

    @Resource
    private JavaMailSenderImpl mailSender;

    public void sendMail (String message) {
        try {
            // 创建一个复杂的文件
            MimeMessage mailMessage = mailSender.createMimeMessage ();

            // 组装邮件
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage,true,"utf-8");

            helper.setSubject("你好");
            helper.setText(message,true);

            // 收件人
            helper.setTo(cookieData.getEmail ());
            // 发件人
            helper.setFrom("3113788997@qq.com");

            // 发送
            mailSender.send(mailMessage);
        } catch (MessagingException e) {
            log.warning ("邮件发送失败!");
        }

    }
}
