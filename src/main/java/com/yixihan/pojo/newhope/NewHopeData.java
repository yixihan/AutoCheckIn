package com.yixihan.pojo.newhope;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * description
 *
 * @author yixihan
 * @date 2022/11/11 9:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewHopeData {


    /**
     * newhope 打卡提醒记录接受者 (邮箱接收)
     */
    private String email;

    /**
     * 是否启用自动提醒
     */
    private Boolean autoRemindFlag;

    /**
     * 用户是否打卡成功
     */
    private Boolean remind;

    /**
     * 是否发送邮件
     */
    private Boolean sendEmailFlag;

    /**
     * 发送时间
     */
    private List<Integer> sendTime;

    /**
     * 提醒次数
     */
    private Integer remindCnt;

}
