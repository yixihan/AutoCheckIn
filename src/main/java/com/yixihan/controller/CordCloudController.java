package com.yixihan.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.yixihan.pojo.CookieData;
import com.yixihan.pojo.Result;
import com.yixihan.pojo.cordCloud.CordCloud;
import com.yixihan.pojo.cordCloud.CordCloudMsg;
import com.yixihan.pojo.cordCloud.User;
import lombok.extern.java.Log;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author : yixihan
 * @create : 2022-09-16-13:58
 */
@RestController
@Log
@RequestMapping("/cordCloud")
public class CordCloudController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private CookieData cookieData;

    /**
     * 添加自动签到 cordCloud 用户
     * sendEmail 用户邮箱 (也是签到信息接收者)
     * username cordCloud 登录账户
     * passwd cordCloud 登录密码
     */
    @PostMapping("/addUser")
    public Result addUser(@RequestBody Map<String, Object> params) {
        String sendEmail = String.valueOf (params.get ("sendEmail"));
        String email = String.valueOf (params.get ("email"));
        String passwd = String.valueOf (params.get ("passwd"));

        User user = new User (email, passwd);
        CordCloud cordCloud = new CordCloud (user, sendEmail);

        // 严重账户密码正确性
        CordCloudMsg msg = loginCordCloud (user);
        log.info (msg.toString ());
        if (msg.getRet () == 0) {
            return Result.fail (msg.getMsg ());
        }

        redisTemplate.opsForHash ().put (cookieData.getCordCloudName (), sendEmail, cordCloud);

        return Result.success ("添加成功");
    }

    /**
     * 更改 cordCloud 自动签到设置
     * <p>
     * sendEmail   用户邮箱 (也是签到信息接收者)
     * isAutoCheck true : 开启自动签到 | false : 关闭自动签到
     */
    @PostMapping("/changeAutoCheck")
    public Result changeAutoCheck(@RequestBody Map<String, Object> params) {
        String sendEmail = String.valueOf (params.get ("sendEmail"));
        Boolean isAutoCheck = Boolean.valueOf (String.valueOf (params.get ("isAutoCheck")));
        CordCloud cordCloud = (CordCloud) redisTemplate.opsForHash ().get (cookieData.getCordCloudName (), sendEmail);
        if (cordCloud == null) {
            return Result.fail ("请先添加用户!");
        }

        cordCloud.setIsCheckIn (isAutoCheck);
        redisTemplate.opsForHash ().put (cookieData.getCordCloudName (), sendEmail, cordCloud);
        return Result.success ("修改状态成功!");
    }


    /**
     * 更改 cordCloud 自动发送邮件设置
     * <p>
     * sendEmail       用户邮箱 (也是签到信息接收者)
     * isAutoSendEmail true : 开启自动发送邮件 | false : 关闭自动发送邮件
     */
    @PostMapping("/changeAutoSendEmail")
    public Result changeAutoSendEmail(@RequestBody Map<String, Object> params) {
        String sendEmail = String.valueOf (params.get ("sendEmail"));
        Boolean isAutoSendEmail = Boolean.valueOf (String.valueOf (params.get ("isAutoCheck")));
        CordCloud cordCloud = (CordCloud) redisTemplate.opsForHash ().get (cookieData.getCordCloudName (), sendEmail);
        if (cordCloud == null) {
            return Result.fail ("请先添加用户!");
        }

        cordCloud.setIsSendEmail (isAutoSendEmail);
        redisTemplate.opsForHash ().put (cookieData.getCordCloudName (), sendEmail, cordCloud);
        return Result.success ("修改状态成功!");
    }

    /**
     * 删除 cordCloud 用户信息
     * <p>
     * sendEmail 用户邮箱 (也是签到信息接收者)
     */
    @PostMapping("/delUser")
    public Result delUser(@RequestBody Map<String, Object> params) {
        String sendEmail = String.valueOf (params.get ("sendEmail"));
        redisTemplate.opsForHash ().delete (cookieData.getCordCloudName (), sendEmail);
        return Result.success ("删除用户信息成功!");
    }

    /**
     * 登录 cordCloud
     *
     * @param user cordCloud 登录用户信息
     * @return cordCloud 通用返回信息
     * @throws RuntimeException 响应状态码不为 200 时抛出
     */
    private CordCloudMsg loginCordCloud(User user) {
        String form = JSONUtil.toJsonStr (JSONUtil.parseObj (user, false, true));
        log.info (form);
        HttpResponse response = HttpRequest.post (cookieData.getCordCloudLogin ()).body (form).execute ();
        if (response.getStatus () == 200) {
            return JSONUtil.toBean (JSONUtil.parseObj (response.body ()), CordCloudMsg.class);
        } else {
            log.warning ("未知错误!");
            log.warning (response.body ());
            throw new RuntimeException ("登录失败, 遇到未知错误, 请联系管理员!");
        }
    }

}
