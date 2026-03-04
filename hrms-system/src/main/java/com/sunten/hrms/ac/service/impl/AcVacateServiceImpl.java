package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcLeaveApplicationDao;
import com.sunten.hrms.ac.dao.AcVacateDao;
import com.sunten.hrms.ac.domain.AcVacate;
import com.sunten.hrms.ac.dto.AcVacateDTO;
import com.sunten.hrms.ac.dto.AcVacateQueryCriteria;
import com.sunten.hrms.ac.mapper.AcVacateMapper;
import com.sunten.hrms.ac.service.AcVacateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @atuthor xukai
 * @date 2020/10/15 11:58
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcVacateServiceImpl extends ServiceImpl<AcVacateDao, AcVacate> implements AcVacateService {

    private final AcVacateDao acVacateDao;
    private final AcVacateMapper acVacateMapper;
    @Autowired
    private AcLeaveApplicationDao acLeaveApplicationDao;

    public AcVacateServiceImpl(AcVacateDao acVacateDao, AcVacateMapper acVacateMapper) {
        this.acVacateDao = acVacateDao;
        this.acVacateMapper = acVacateMapper;
    }

    @Override
    public AcVacateDTO getVacateByRequisitionCode(String reqCode) {
//        return acVacateMapper.toDto(acVacateDao.getAcVacateByRequisitionCode(reqCode));
        // 2024-06-03 进行修改从OA读取变成从HR读取
        return acVacateMapper.toDto(acLeaveApplicationDao.getAcVacateByRequisitionCode(reqCode));
    }

    @Override
    public AcVacateDTO getVacateByReqCodeAndDate(AcVacateQueryCriteria criteria) {
        // 2024-06-03 进行修改，原因OA请假模块迁移到HR系统了，所以获取请假单要修改
//        AcVacate acVacate = acVacateDao.getAcVacateByRequisitionCode(criteria.getReqCode());
        AcVacate acVacate = acLeaveApplicationDao.getAcVacateByRequisitionCode(criteria.getReqCode());
        if (acVacate != null) { // OA单号存在
            LocalDate beginDate = acVacate.getStartTime().toLocalDate();
            LocalDate endDate = acVacate.getEndTime().toLocalDate();
            if (!criteria.getWorkCard().equals(acVacate.getUserName())) { // 工号不匹配
                acVacate.setCheckResult("noSelf");
            } else if (criteria.getExceptionDate().isBefore(beginDate) || criteria.getExceptionDate().isAfter(endDate)){ // 请假时间不匹配
                acVacate.setCheckResult("dateError");
            } else if ("Pass".equals(acVacate.getState())) { // 申请单已审批通过
                acVacate.setCheckResult("pass");
            } else { // 申请单审批中
                acVacate.setCheckResult("wait");
            }
        }
        return acVacateMapper.toDto(acVacate);
    }


    @Override
    public AcVacateDTO getVacateByReqCodeAndWorkcard(AcVacateQueryCriteria criteria) {
        // 2024-06-30 接到用户反馈请假条和考勤异常匹配不上，然后发现是OA请假迁移到HR了 所以现在改成从HR获取假条
//        List<AcVacate> acVacateList = acVacateDao.getList(criteria);
        // 2024-06-30 将Pass的假条从HR获取出来
//        System.out.println("criteria：" + criteria);
        List<AcVacate> acVacateList = null;
        if (criteria.getReqCode() != null && !"".equals(criteria.getReqCode())) {
            acVacateList = acLeaveApplicationDao.getList(criteria.getReqCode());
        } else {
            acVacateList = acLeaveApplicationDao.getList2();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        AcVacate acVacate = null;
        if (acVacateList .size() > 0) { // OA单号存在
            acVacate = new AcVacate();
            if (!criteria.getWorkCard().equals(acVacateList.get(0).getUserName())) { // 工号不匹配
                acVacate.setCheckResult("noSelf");
            } else {
                boolean dateFlag = false;
                StringBuilder dateStr = new StringBuilder();
                for(AcVacate acVacate1 :  acVacateList) {
                    LocalDate beginDate = acVacate1.getStartTime().toLocalDate();
                    LocalDate endDate = acVacate1.getEndTime().toLocalDate();
                    if ((criteria.getExceptionDate().isAfter(beginDate) || criteria.getExceptionDate().isEqual(beginDate)) &&
                       (criteria.getExceptionDate().isBefore(endDate) || criteria.getExceptionDate().isEqual(endDate))){ // 请假时间匹配
                        dateFlag = true;
                    }
                    if (dateStr.length() >0) {
                        dateStr.append("," + formatter.format(acVacate1.getStartTime()) + "到" + formatter.format(acVacate1.getEndTime()));
                    } else {
                        dateStr.append(formatter.format(acVacate1.getStartTime()) + "到" + formatter.format(acVacate1.getEndTime()));
                    }
                }
                acVacate.setDateStr(dateStr.toString());
                if (!dateFlag) {
                    acVacate.setCheckResult("dateError");
                } else if ("Pass".equals(acVacateList.get(0).getState())) { // 申请单已审批通过
                    acVacate.setCheckResult("pass");
                } else { // 申请单审批中
                    acVacate.setCheckResult("wait");
                }

            }


        }
        return acVacateMapper.toDto(acVacate);
    }
}
