package com.sunten.hrms.ac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.ac.domain.AcOvertime;
import com.sunten.hrms.ac.domain.AcOvertimeApplication;
import com.sunten.hrms.ac.dto.AcOvertimeQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * @atuthor xukai
 * @date 2020/10/16 16:38
 */
public interface AcOvertimeService extends IService<AcOvertime> {

    Map<String, Object> listAll(AcOvertimeQueryCriteria criteria, Pageable pageable);

    void writeOaApprovalResult(AcOvertimeApplication acOvertimeApplication);
}
