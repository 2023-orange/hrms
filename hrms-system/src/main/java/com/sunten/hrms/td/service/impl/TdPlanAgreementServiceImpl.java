package com.sunten.hrms.td.service.impl;

import com.sunten.hrms.fnd.dao.FndAttachedDocumentDao;
import com.sunten.hrms.fnd.domain.FndAttachedDocument;
import com.sunten.hrms.fnd.dto.FndAttachedDocumentQueryCriteria;
import com.sunten.hrms.fnd.service.FndAttachedDocumentService;
import com.sunten.hrms.td.domain.TdPlanAgreement;
import com.sunten.hrms.td.dao.TdPlanAgreementDao;
import com.sunten.hrms.td.service.TdPlanAgreementService;
import com.sunten.hrms.td.dto.TdPlanAgreementDTO;
import com.sunten.hrms.td.dto.TdPlanAgreementQueryCriteria;
import com.sunten.hrms.td.mapper.TdPlanAgreementMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;

/**
 * <p>
 * 培训协议书记录表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-06-18
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdPlanAgreementServiceImpl extends ServiceImpl<TdPlanAgreementDao, TdPlanAgreement> implements TdPlanAgreementService {
    private final TdPlanAgreementDao tdPlanAgreementDao;
    private final TdPlanAgreementMapper tdPlanAgreementMapper;
    private final FndAttachedDocumentService fndAttachedDocumentService;
    private final FndAttachedDocumentDao fndAttachedDocumentDao;

    public TdPlanAgreementServiceImpl(TdPlanAgreementDao tdPlanAgreementDao, TdPlanAgreementMapper tdPlanAgreementMapper,
                                      FndAttachedDocumentService fndAttachedDocumentService, FndAttachedDocumentDao fndAttachedDocumentDao) {
        this.tdPlanAgreementDao = tdPlanAgreementDao;
        this.tdPlanAgreementMapper = tdPlanAgreementMapper;
        this.fndAttachedDocumentService = fndAttachedDocumentService;
        this.fndAttachedDocumentDao = fndAttachedDocumentDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdPlanAgreementDTO insert(TdPlanAgreement planAgreementNew) {
        if (null != planAgreementNew.getFiles() && planAgreementNew.getFiles().length > 0) {
            tdPlanAgreementDao.insertAllColumn(planAgreementNew);
//            for(int i = 0; i < planAgreementNew.getFiles().length; i ++) {
//                // 写入附件
//                fndAttachedDocumentService.uploadAttachedDoc(planAgreementNew.getFiles()[i], planAgreementNew.getSource(), planAgreementNew.getId(), planAgreementNew.getType());
//            }
        } else {
            tdPlanAgreementDao.insertAllColumn(planAgreementNew);
        }
        return tdPlanAgreementMapper.toDto(planAgreementNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdPlanAgreement planAgreement = new TdPlanAgreement();
        planAgreement.setId(id);
        this.delete(planAgreement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdPlanAgreement planAgreement) {
        // TODO    确认删除前是否需要做检查
        tdPlanAgreementDao.deleteByEntityKey(planAgreement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdPlanAgreement planAgreementNew) {
        TdPlanAgreement planAgreementInDb = Optional.ofNullable(tdPlanAgreementDao.getByKey(planAgreementNew.getId())).orElseGet(TdPlanAgreement::new);
        ValidationUtil.isNull(planAgreementInDb.getId() ,"PlanAgreement", "id", planAgreementNew.getId());
        planAgreementNew.setId(planAgreementInDb.getId());
        tdPlanAgreementDao.updateAllColumnByKey(planAgreementNew);
    }

    @Override
    public TdPlanAgreementDTO getByKey(Long id) {
        TdPlanAgreement planAgreement = Optional.ofNullable(tdPlanAgreementDao.getByKey(id)).orElseGet(TdPlanAgreement::new);
        ValidationUtil.isNull(planAgreement.getId() ,"PlanAgreement", "id", id);
        return tdPlanAgreementMapper.toDto(planAgreement);
    }

    @Override
    public List<TdPlanAgreementDTO> listAll(TdPlanAgreementQueryCriteria criteria) {
        return tdPlanAgreementMapper.toDto(tdPlanAgreementDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdPlanAgreementQueryCriteria criteria, Pageable pageable) {
        Page<TdPlanAgreement> page = PageUtil.startPage(pageable);
        List<TdPlanAgreement> planAgreements = tdPlanAgreementDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdPlanAgreementMapper.toDto(planAgreements), page.getTotal());
    }

    @Override
    public void download(List<TdPlanAgreementDTO> planAgreementDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdPlanAgreementDTO planAgreementDTO : planAgreementDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("员工id", planAgreementDTO.getEmployeeId());
            map.put("培训计划id", planAgreementDTO.getPlanId());
            map.put("服务年限", planAgreementDTO.getServiceYear());
            map.put("服务开始时间", planAgreementDTO.getBeginDate());
            map.put("服务结束时间", planAgreementDTO.getEndDate());
            map.put("生效标记", planAgreementDTO.getEnabledFlag());
            map.put("id", planAgreementDTO.getId());
            map.put("创建时间", planAgreementDTO.getCreateTime());
            map.put("创建人ID", planAgreementDTO.getCreateBy());
            map.put("修改时间", planAgreementDTO.getUpdateTime());
            map.put("修改人ID", planAgreementDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public TdPlanAgreementDTO getByPlanIdAndEmployeeId(Long employeeId, Long planId, String checkMethod) {
        return tdPlanAgreementMapper.toDto(tdPlanAgreementDao.getByPlanIdAndEmployeeId(employeeId, planId, checkMethod));
    }

    @Override
    public void disabledByCheckMethodAndPlanID(Long planId, String checkMethod) {
        tdPlanAgreementDao.disabledByCheckMethodAndPlanID(planId, checkMethod);
    }
}
