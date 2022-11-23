package com.yixihan;

import cn.hutool.core.util.NumberUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * description
 *
 * @author yixihan
 * @date 2022/11/23 20:16
 */
public class CustomTest {


    @Test
    public void testAvg () {
        double sum = 156;
        double cnt = 1;
        double avg = sum / cnt;
        double thisSum = Integer.parseInt ("165");
        BigDecimal percentage = NumberUtil.round (NumberUtil.mul (NumberUtil.div (thisSum, avg), 100), 2);
        System.out.println (percentage);
    }
}
