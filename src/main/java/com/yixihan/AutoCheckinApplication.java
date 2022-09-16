package com.yixihan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author yixihan
 */
@SpringBootApplication
@EnableScheduling
public class AutoCheckinApplication {


    public static void main(String[] args) {
        SpringApplication.run (AutoCheckinApplication.class, args);
    }


}