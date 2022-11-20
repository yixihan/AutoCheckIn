package com.yixihan.controller;

import com.yixihan.auto_check.AutoCheck;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yixihan
 * @date 2022-10-28-15:35
 */
@RestController
@RequestMapping("/test/")
public class TestController {

    @Resource
    private AutoCheck autoCheck;

    @PostMapping("/runnow")
    public void runNow () {
        autoCheck.autoCheckInCordCloud ();
    }
}
