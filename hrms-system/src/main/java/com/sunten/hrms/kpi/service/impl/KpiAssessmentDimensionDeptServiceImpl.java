package com.sunten.hrms.kpi.service.impl;

import com.sunten.hrms.kpi.domain.KpiAssessmentDimensionDept;
import com.sunten.hrms.kpi.dao.KpiAssessmentDimensionDeptDao;
import com.sunten.hrms.kpi.service.KpiAssessmentDimensionDeptService;
import com.sunten.hrms.kpi.dto.KpiAssessmentDimensionDeptDTO;
import com.sunten.hrms.kpi.dto.KpiAssessmentDimensionDeptQueryCriteria;
import com.sunten.hrms.kpi.mapper.KpiAssessmentDimensionDeptMapper;
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
 * KPI考核维度与部门关系表 服务实现类
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-28
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KpiAssessmentDimensionDeptServiceImpl extends ServiceImpl<KpiAssessmentDimensionDeptDao, KpiAssessmentDimensionDept> implements KpiAssessmentDimensionDeptService {
    private final KpiAssessmentDimensionDeptDao kpiAssessmentDimensionDeptDao;
    private final KpiAssessmentDimensionDeptMapper kpiAssessmentDimensionDeptMapper;

    public KpiAssessmentDimensionDeptServiceImpl(KpiAssessmentDimensionDeptDao kpiAssessmentDimensionDeptDao, KpiAssessmentDimensionDeptMapper kpiAssessmentDimensionDeptMapper) {
        this.kpiAssessmentDimensionDeptDao = kpiAssessmentDimensionDeptDao;
        this.kpiAssessmentDimensionDeptMapper = kpiAssessmentDimensionDeptMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KpiAssessmentDimensionDeptDTO insert(KpiAssessmentDimensionDept assessmentDimensionDeptNew) {
        kpiAssessmentDimensionDeptDao.insertAllColumn(assessmentDimensionDeptNew);
        return kpiAssessmentDimensionDeptMapper.toDto(assessmentDimensionDeptNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        KpiAssessmentDimensionDept assessmentDimensionDept = new KpiAssessmentDimensionDept();
        assessmentDimensionDept.setId(id);
        this.delete(assessmentDimensionDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(KpiAssessmentDimensionDept assessmentDimensionDept) {
        // TODO    确认删除前是否需要做检查
        kpiAssessmentDimensionDeptDao.deleteByEntityKey(assessmentDimensionDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(KpiAssessmentDimensionDept assessmentDimensionDeptNew) {
        KpiAssessmentDimensionDept assessmentDimensionDeptInDb = Optional.ofNullable(kpiAssessmentDimensionDeptDao.getByKey(assessmentDimensionDeptNew.getId())).orElseGet(KpiAssessmentDimensionDept::new);
        ValidationUtil.isNull(assessmentDimensionDeptInDb.getId() ,"AssessmentDimensionDept", "id", assessmentDimensionDeptNew.getId());
        assessmentDimensionDeptNew.setId(assessmentDimensionDeptInDb.getId());
        kpiAssessmentDimensionDeptDao.updateAllColumnByKey(assessmentDimensionDeptNew);
    }

    @Override
    public KpiAssessmentDimensionDeptDTO getByKey(Long id) {
        KpiAssessmentDimensionDept assessmentDimensionDept = Optional.ofNullable(kpiAssessmentDimensionDeptDao.getByKey(id)).orElseGet(KpiAssessmentDimensionDept::new);
        ValidationUtil.isNull(assessmentDimensionDept.getId() ,"AssessmentDimensionDept", "id", id);
        return kpiAssessmentDimensionDeptMapper.toDto(assessmentDimensionDept);
    }

    @Override
    public List<KpiAssessmentDimensionDeptDTO> listAll(KpiAssessmentDimensionDeptQueryCriteria criteria) {
        return kpiAssessmentDimensionDeptMapper.toDto(kpiAssessmentDimensionDeptDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(KpiAssessmentDimensionDeptQueryCriteria criteria, Pageable pageable) {
        Page<KpiAssessmentDimensionDept> page = PageUtil.startPage(pageable);
        List<KpiAssessmentDimensionDept> assessmentDimensionDepts = kpiAssessmentDimensionDeptDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(kpiAssessmentDimensionDeptMapper.toDto(assessmentDimensionDepts), page.getTotal());
    }

    @Override
    public void download(List<KpiAssessmentDimensionDeptDTO> assessmentDimensionDeptDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (KpiAssessmentDimensionDeptDTO assessmentDimensionDeptDTO : assessmentDimensionDeptDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("主键", assessmentDimensionDeptDTO.getId());
            map.put("KPI考核维度id", assessmentDimensionDeptDTO.getAssessmentDimensionId());
            map.put("KPI部门树id", assessmentDimensionDeptDTO.getDepartmentTreeId());
            map.put("弹性域1", assessmentDimensionDeptDTO.getAttribute1());
            map.put("弹性域2", assessmentDimensionDeptDTO.getAttribute2());
            map.put("弹性域3", assessmentDimensionDeptDTO.getAttribute3());
            map.put("弹性域4", assessmentDimensionDeptDTO.getAttribute4());
            map.put("弹性域5", assessmentDimensionDeptDTO.getAttribute5());
            map.put("创建时间", assessmentDimensionDeptDTO.getCreateTime());
            map.put("创建人", assessmentDimensionDeptDTO.getCreateBy());
            map.put("更新时间", assessmentDimensionDeptDTO.getUpdateTime());
            map.put("更新人", assessmentDimensionDeptDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<KpiAssessmentDimensionDeptDTO> listMultipleChoice(Long id) {
        return kpiAssessmentDimensionDeptMapper.toDto(kpiAssessmentDimensionDeptDao.listMultipleChoice(id));
    }
}
