package com.sunten.hrms.ac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.ac.domain.AcVacate;
import com.sunten.hrms.ac.dto.AcVacateDTO;
import com.sunten.hrms.ac.dto.AcVacateQueryCriteria;

/**
 * @atuthor xukai
 * @date 2020/10/15 11:55
 */
public interface AcVacateService extends IService<AcVacate> {
    AcVacateDTO getVacateByRequisitionCode(String reqCode);

    AcVacateDTO getVacateByReqCodeAndDate(AcVacateQueryCriteria criteria);

    AcVacateDTO getVacateByReqCodeAndWorkcard(AcVacateQueryCriteria criteria);
}
