package com.yixihan.remind;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.yixihan.controller.MailSendController;
import com.yixihan.pojo.CookieData;
import com.yixihan.pojo.newhope.NewHopeData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * description
 *
 * @author yixihan
 * @date 2022/11/11 9:00
 */
@Configuration
@Slf4j
public class NewHopeRemind {

    @Resource
    private CookieData cookieData;

    @Resource
    private MailSendController mailSendController;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    static ThreadPoolExecutor executor;

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;

    private static final int MAX_REMIND = 7;

    static {
        executor = new ThreadPoolExecutor (CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<> (QUEUE_CAPACITY), new ThreadPoolExecutor.CallerRunsPolicy ());

    }

    public void remindNow() {
        startAutoRemind (-1);
    }


    @Scheduled(cron = "0 30 7 * * ?")
    public void remindAt8() {
        startAutoRemind (8);
    }

    @Scheduled(cron = "0 30 8 * * ?")
    public void remindAt9() {
        startAutoRemind (9);
    }

    @Scheduled(cron = "0 55 16 * * ?")
    public void remindAt17() {
        startAutoRemind (17);
    }

    @Scheduled(cron = "0 55 17 * * ?")
    public void remindAt18() {
        startAutoRemind (18);
    }

    public void resetAtNow() {
        reset ();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetAt0() {
        reset ();
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public void resetAt12() {
        reset ();
    }

    /**
     * 自动提醒
     *
     * @param hour 发送时间
     */
    private void startAutoRemind(int hour) {
        Map<Object, Object> map = redisTemplate.opsForHash ().entries (cookieData.getNewhopeName ());

        for (Map.Entry<Object, Object> entry : map.entrySet ()) {
            executor.execute (() -> {
                NewHopeData newhope = (NewHopeData) entry.getValue ();
                while (newhope.getRemindCnt () < MAX_REMIND) {
                    newhope = (NewHopeData) redisTemplate.opsForHash ().get (cookieData.getNewhopeName (), newhope.getEmail ());
                    log.info ("用户 " + newhope.getEmail () + " 是否已开启自动提醒 : " + newhope.getAutoRemindFlag ());
                    if (newhope.getRemind ()) {
                        log.info ("用户 " + newhope.getEmail () + " 已经收到提醒");
                        return;
                    }
                    if (newhope.getAutoRemindFlag ()
                            && !newhope.getRemind ()
                            && (CollectionUtil.contains (newhope.getSendTime (), hour) || hour == -1)
                            && newhope.getRemindCnt () < MAX_REMIND) {
                        log.info ("第 " + (newhope.getRemindCnt () + 1) + " 提醒用户 " + newhope.getEmail () + " 打卡...");
                        autoRemind (newhope);
                        newhope.setRemindCnt (newhope.getRemindCnt () + 1);
                        log.info ("第 " + newhope.getRemindCnt () + " 提醒用户 " + newhope.getEmail () + " 打卡成功");
                        redisTemplate.opsForHash ().put (cookieData.getNewhopeName (), newhope.getEmail (), newhope);
                        ThreadUtil.sleep (5, TimeUnit.MINUTES);
                    }
                }
            });
        }
    }


    private void reset() {
        Map<Object, Object> map = redisTemplate.opsForHash ().entries (cookieData.getNewhopeName ());

        for (Map.Entry<Object, Object> entry : map.entrySet ()) {
            NewHopeData newhope = (NewHopeData) entry.getValue ();
            newhope.setRemind (false);
            newhope.setRemindCnt (0);
            redisTemplate.opsForHash ().put (cookieData.getNewhopeName (), newhope.getEmail (), newhope);
        }
    }

    /**
     * 发送邮件
     */
    private void autoRemind(NewHopeData newhope) {
        try {
            String address = InetAddress.getLocalHost ().getHostAddress ();
            String message = "您好, 小易提醒你请完成今天的打卡哦\n" + "<br>\n" +
                    "<a href=\"http://" + address + ":9999/newhope/remind?flag=true&email=" + newhope.getEmail () + "\">" +
                    "<button>已打卡</button></a>";
            mailSendController.sendMail (message, newhope.getEmail (), null);
        } catch (UnknownHostException e) {
            throw new RuntimeException (e);
        }


    }
}
