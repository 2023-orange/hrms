package com.sunten.hrms.wta.task;

import com.sunten.hrms.pm.service.PmEmployeeJobTransferService;
import com.sunten.hrms.utils.DateUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
public class JobTransferTask {
    private final PmEmployeeJobTransferService pmEmployeeJobTransferService;
//    private final ObjectMapper objectMapper;

    public JobTransferTask(PmEmployeeJobTransferService pmEmployeeJobTransferService) {
        this.pmEmployeeJobTransferService = pmEmployeeJobTransferService;
    }

    public void finishTransfer(String transferDateStr) {
        LocalDate transferDate = null;
        if (transferDateStr.equals("now()")) {
            transferDate = LocalDate.now();
        } else {
            Long dateLong = Long.getLong(transferDateStr);
            transferDate = DateUtil.asLocalDate(new Date(dateLong));
        }
        pmEmployeeJobTransferService.finishTransfer(transferDate);
    }

//    public void test(String json) throws JsonProcessingException {
//        System.out.println("json:" + json);
//        TestJson ttt = objectMapper.readValue(json, TestJson.class);
//        System.out.println(ttt);
//    }
//
//    @Data
//    @ToString
//    public class TestJson {
//        private LocalDate beginDate;
//        private String str;
//        private Long testStr;
//    }
}
