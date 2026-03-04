package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcOvertimeApplicationDao;
import com.sunten.hrms.ac.dao.AcOvertimeDao;
import com.sunten.hrms.ac.dao.AcSetUpDao;
import com.sunten.hrms.ac.domain.AcOvertime;
import com.sunten.hrms.ac.domain.AcOvertimeApplication;
import com.sunten.hrms.ac.domain.AcSetUp;
import com.sunten.hrms.ac.dto.AcOvertimeQueryCriteria;
import com.sunten.hrms.ac.mapper.AcOvertimeMapper;
import com.sunten.hrms.ac.service.AcOvertimeService;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @atuthor xukai
 * @date 2020/10/16 16:40
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcOvertimeServiceImpl extends ServiceImpl<AcOvertimeDao, AcOvertime> implements AcOvertimeService {

    private final AcOvertimeDao acOvertimeDao;
    private final AcOvertimeMapper acOvertimeMapper;
    private final AcSetUpDao acSetUpDao;
    private final AcOvertimeApplicationDao acOvertimeApplicationDao;

    public AcOvertimeServiceImpl(AcOvertimeDao acOvertimeDao, AcOvertimeMapper acOvertimeMapper, AcSetUpDao acSetUpDao, AcOvertimeApplicationDao acOvertimeApplicationDao) {
        this.acOvertimeDao = acOvertimeDao;
        this.acOvertimeMapper = acOvertimeMapper;
        this.acSetUpDao = acSetUpDao;
        this.acOvertimeApplicationDao = acOvertimeApplicationDao;
    }

    @Override
    public Map<String, Object> listAll(AcOvertimeQueryCriteria criteria, Pageable pageable) {
        Page<AcOvertime> page = PageUtil.startPage(pageable);
        AcSetUp acSetUp = acSetUpDao.getByNew();
        if (null == acSetUp || null == acSetUp.getCalculation() || new BigDecimal("0").equals(acSetUp.getCalculation())) {
            throw new InfoCheckWarningMessException("查询失败，ac_set_up配置表里没有获取到天数转小时数的参数， 请联系管理员解决");
        } else {
            List<AcOvertime> overtimes = acOvertimeDao.getOvertimeList(criteria, page);
            for (AcOvertime acOvertime : overtimes
                 ) {
                if (acOvertime.getQuantity() != 0) {
                    acOvertime.setQuantity(new BigDecimal(acOvertime.getQuantity()).multiply(acSetUp.getCalculation()).doubleValue());
                }
            }
            return PageUtil.toPage(acOvertimeMapper.toDto(overtimes), page.getTotal());
        }
    }

    @Override
    public void writeOaApprovalResult(AcOvertimeApplication acOvertimeApplicationNew) {
        AcOvertimeApplication acOvertimeApplication = Optional.ofNullable(acOvertimeApplicationDao.getByKey(acOvertimeApplicationNew.getId())).orElseGet(AcOvertimeApplication::new);
        ValidationUtil.isNull(acOvertimeApplication.getId(),"AcOvertimeApplication","id",acOvertimeApplicationNew.getId());
        // 如果最终审批有数据，说明该审批流程已结束
        if (acOvertimeApplicationNew.getApprovalResult() != null && !"".equals(acOvertimeApplicationNew.getApprovalResult())) {
            acOvertimeApplicationDao.updateByApprovalEnd(acOvertimeApplicationNew);
        } else {
            // 该审批流程还没有结束
            if (null == acOvertimeApplicationNew.getApprovalNode() || "".equals(acOvertimeApplicationNew.getApprovalNode())) {
                throw new InfoCheckWarningMessException("OA当前审批节点不能为空！");
            }
            acOvertimeApplicationDao.updateByApprovalUnderwar(acOvertimeApplicationNew);
        }
    }
}
