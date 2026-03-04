package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.pm.dao.PmMedicalLineDao;
import com.sunten.hrms.pm.domain.PmMedical;
import com.sunten.hrms.pm.dao.PmMedicalDao;
import com.sunten.hrms.pm.domain.PmMedicalLine;
import com.sunten.hrms.pm.dto.PmMedicalLineQueryCriteria;
import com.sunten.hrms.pm.service.PmMedicalLineRelevanceService;
import com.sunten.hrms.pm.service.PmMedicalService;
import com.sunten.hrms.pm.dto.PmMedicalDTO;
import com.sunten.hrms.pm.dto.PmMedicalQueryCriteria;
import com.sunten.hrms.pm.mapper.PmMedicalMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
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
 * 体检申请表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-04-07
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmMedicalServiceImpl extends ServiceImpl<PmMedicalDao, PmMedical> implements PmMedicalService {
    private final PmMedicalDao pmMedicalDao;
    private final PmMedicalMapper pmMedicalMapper;
    private final PmMedicalLineDao pmMedicalLineDao;
    private final PmMedicalLineRelevanceService pmMedicalLineRelevanceService;

    public PmMedicalServiceImpl(PmMedicalDao pmMedicalDao, PmMedicalMapper pmMedicalMapper, PmMedicalLineDao pmMedicalLineDao, PmMedicalLineRelevanceService pmMedicalLineRelevanceService) {
        this.pmMedicalDao = pmMedicalDao;
        this.pmMedicalMapper = pmMedicalMapper;
        this.pmMedicalLineDao = pmMedicalLineDao;
        this.pmMedicalLineRelevanceService = pmMedicalLineRelevanceService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmMedicalDTO insert(PmMedical medicalNew) {
        pmMedicalDao.insertAllColumn(medicalNew);
        for(PmMedicalLine medicalLine : medicalNew.getMedicalLines()) {
            // 循环插入子集
            medicalLine.setMedicalId(medicalNew.getId());
            pmMedicalLineDao.insertAllColumn(medicalLine);
            if (null != medicalLine.getMedicalLineRelevanceList() && medicalLine.getMedicalLineRelevanceList().size() > 0) {
                pmMedicalLineRelevanceService.batchInsert(medicalLine.getMedicalLineRelevanceList(),medicalLine.getCreateBy(),medicalLine.getId());
            }
        }

        return pmMedicalMapper.toDto(medicalNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmMedical medical = new PmMedical();
        medical.setId(id);
        this.delete(medical);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmMedical medical) {
        // 删除体检申请单主表时同时删除其子表信息
        pmMedicalDao.deleteByEntityKey(medical);
        pmMedicalLineDao.deleteByHeaderId(medical.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmMedical medicalNew) {
        PmMedical medicalInDb = Optional.ofNullable(pmMedicalDao.getByKey(medicalNew.getId())).orElseGet(PmMedical::new);
        ValidationUtil.isNull(medicalInDb.getId() ,"Medical", "id", medicalNew.getId());
        medicalNew.setId(medicalInDb.getId());
        pmMedicalDao.updateAllColumnByKey(medicalNew);

        // 循环子集，增加或修改
        if (null != medicalNew.getMedicalLines() && medicalNew.getMedicalLines().size() > 0) {
            for(PmMedicalLine medicalLine: medicalNew.getMedicalLines()) {
                if (null == medicalLine.getId()) { // 新增
                    medicalLine.setMedicalId(medicalNew.getId());
                    pmMedicalLineDao.insertAllColumn(medicalLine);
                } else if(null != medicalLine.getEditFlag() && medicalLine.getEditFlag() == true) { // 修改
                    pmMedicalLineDao.updateAllColumnByKey(medicalLine);
                }
                pmMedicalLineRelevanceService.batchEdit(medicalLine.getMedicalLineRelevanceList(),medicalLine.getUpdateBy(),medicalLine.getId());
            }
        }

    }

    @Override
    public PmMedicalDTO getByKey(Long id) {
        PmMedical medical = Optional.ofNullable(pmMedicalDao.getByKey(id)).orElseGet(PmMedical::new);
        ValidationUtil.isNull(medical.getId() ,"Medical", "id", id);
        return pmMedicalMapper.toDto(medical);
    }

    @Override
    public List<PmMedicalDTO> listAll(PmMedicalQueryCriteria criteria) {
        return pmMedicalMapper.toDto(pmMedicalDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmMedicalQueryCriteria criteria, Pageable pageable) {
        Page<PmMedical> page = PageUtil.startPage(pageable);
        List<PmMedical> medicals = pmMedicalDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmMedicalMapper.toDto(medicals), page.getTotal());
    }

    @Override
    public void download(List<PmMedicalDTO> medicalDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmMedicalDTO medicalDTO : medicalDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("部门", medicalDTO.getDeptName());
            map.put("科室", medicalDTO.getOfficeName());
            map.put("申请人", medicalDTO.getEmployee().getName());
//            map.put("申请日期", medicalDTO.getRequestDate());
            map.put("最终审批结果", medicalDTO.getApprovalResult());
            map.put("OA单号", medicalDTO.getOaOrder());
            map.put("审批结束日期", medicalDTO.getApprovalDate());
            map.put("有效标记", medicalDTO.getEnabledFlag());
            map.put("当前审批节点", medicalDTO.getApprovalNode());
            map.put("审批人", medicalDTO.getApprovalEmployee());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public PmMedicalDTO getByReqCode(String reqCode) {
        PmMedical medical = pmMedicalDao.getByReqCode(reqCode);
        if (medical != null) {
            PmMedicalLineQueryCriteria lineQueryCriteria = new PmMedicalLineQueryCriteria();
            lineQueryCriteria.setMedicalId(medical.getId());
            List<PmMedicalLine> medicalLines = pmMedicalLineDao.listAllByCriteria(lineQueryCriteria);
            medical.setMedicalLines(medicalLines);
        }

        return pmMedicalMapper.toDto(medical);
    }

    @Override
    public void updateApprovalContent(String reqCode, String approvalNode, String approvalEmployee, String approvalResult) {
        PmMedical medical = pmMedicalDao.getByReqCode(reqCode);
        if (null != approvalResult && !"".equals(approvalResult) && !approvalResult.isEmpty()) { // 审批结束
            medical.setApprovalDate(LocalDateTime.now());
            medical.setApprovalResult(approvalResult);
           pmMedicalDao.updateApprovalColumnByEnd(medical);
        } else {
            medical.setApprovalNode(approvalNode);
            medical.setApprovalEmployee(approvalEmployee);
            pmMedicalDao.updateApprovalColumnByUnderwar(medical);
        }
    }

    @Override
    public int getPmMedicalPass() {
        return pmMedicalDao.getPmMedicalPass();
    }
}
