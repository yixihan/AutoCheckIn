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
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
        SpringApplication.run (AutoCheckinApplication.class, args);
    }


}
