package com.sunten.hrms.ac.vo;

import lombok.Data;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;

@Data
public class RepulseEmailMes {
    // 收件人姓名
    private String toName;
    // 收件人邮箱
    private String email;
    // 被打回的异常的所属日期
    private LocalDate date;
    // 发件人姓名
    private String fromName;
    // 异常所属日期str
    private String dateStr;

}
