package com.yixihan.controller;

import com.yixihan.pojo.CookieData;
import com.yixihan.pojo.Result;
import com.yixihan.pojo.newhope.NewHopeData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * description
 *
 * @author yixihan
 * @date 2022/11/11 9:48
 */
@Slf4j
@RestController
@RequestMapping("/newhope/")
public class NewHopeController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private CookieData cookieData;

    /**
     * 添加自动签到 cordCloud 用户
     */
    @PostMapping("/addUser")
    public Result addUser(@RequestBody NewHopeData newhope) {

        log.info (newhope.toString ());
        redisTemplate.opsForHash ().put (cookieData.getNewhopeName (), newhope.getEmail (), newhope);

        return Result.success ("添加成功");
    }


    /**
     * 删除 cordCloud 用户信息
     * <p>
     * sendEmail 用户邮箱 (也是签到信息接收者)
     */
    @PostMapping("/delUser")
    public Result delUser(@RequestParam("email") String email) {
        redisTemplate.opsForHash ().delete (cookieData.getNewhopeName (), email);
        return Result.success ("删除用户信息成功!");
    }

    @GetMapping("/remind")
    public Result remind(@RequestParam("flag") Boolean flag, @RequestParam("email") String email) {
        NewHopeData newhope  = (NewHopeData) redisTemplate.opsForHash ().get (cookieData.getNewhopeName (), email);
        if (newhope != null) {
            newhope.setRemind (flag);
            redisTemplate.opsForHash ().put (cookieData.getNewhopeName (), email, newhope);
            return Result.success ("恭喜你打卡成功!");
        } else {
            return Result.fail ("未知账号!");
        }
    }

}
