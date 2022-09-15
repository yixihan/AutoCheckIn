package com.yixihan;

import com.yixihan.util.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AutoCheckinApplicationTests {

    @Test
    void contextLoads() {
        String body = "{\"ret\":0,\"msg\":\"\\u60a8\\u4f3c\\u4e4e\\u5df2\\u7ecf\\u7b7e\\u5230\\u8fc7\\u4e86...\"}";
        body = StringUtils.decodeUnicode (body);
        System.out.println (body);
    }

}
