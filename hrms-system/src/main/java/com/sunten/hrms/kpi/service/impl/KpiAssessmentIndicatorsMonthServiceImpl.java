package com.sunten.hrms.kpi.service.impl;

import com.sunten.hrms.kpi.domain.KpiAssessmentIndicatorsMonth;
import com.sunten.hrms.kpi.dao.KpiAssessmentIndicatorsMonthDao;
import com.sunten.hrms.kpi.service.KpiAssessmentIndicatorsMonthService;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsMonthDTO;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsMonthQueryCriteria;
import com.sunten.hrms.kpi.mapper.KpiAssessmentIndicatorsMonthMapper;
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
 * KPI考核指标子表 服务实现类
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-28
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KpiAssessmentIndicatorsMonthServiceImpl extends ServiceImpl<KpiAssessmentIndicatorsMonthDao, KpiAssessmentIndicatorsMonth> implements KpiAssessmentIndicatorsMonthService {
    private final KpiAssessmentIndicatorsMonthDao kpiAssessmentIndicatorsMonthDao;
    private final KpiAssessmentIndicatorsMonthMapper kpiAssessmentIndicatorsMonthMapper;

    public KpiAssessmentIndicatorsMonthServiceImpl(KpiAssessmentIndicatorsMonthDao kpiAssessmentIndicatorsMonthDao, KpiAssessmentIndicatorsMonthMapper kpiAssessmentIndicatorsMonthMapper) {
        this.kpiAssessmentIndicatorsMonthDao = kpiAssessmentIndicatorsMonthDao;
        this.kpiAssessmentIndicatorsMonthMapper = kpiAssessmentIndicatorsMonthMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KpiAssessmentIndicatorsMonthDTO insert(KpiAssessmentIndicatorsMonth assessmentIndicatorsMonthNew) {
        kpiAssessmentIndicatorsMonthDao.insertAllColumn(assessmentIndicatorsMonthNew);
        return kpiAssessmentIndicatorsMonthMapper.toDto(assessmentIndicatorsMonthNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        KpiAssessmentIndicatorsMonth assessmentIndicatorsMonth = new KpiAssessmentIndicatorsMonth();
        assessmentIndicatorsMonth.setId(id);
        this.delete(assessmentIndicatorsMonth);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(KpiAssessmentIndicatorsMonth assessmentIndicatorsMonth) {
        // TODO    确认删除前是否需要做检查
        kpiAssessmentIndicatorsMonthDao.deleteByEntityKey(assessmentIndicatorsMonth);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(KpiAssessmentIndicatorsMonth assessmentIndicatorsMonthNew) {
        KpiAssessmentIndicatorsMonth assessmentIndicatorsMonthInDb = Optional.ofNullable(kpiAssessmentIndicatorsMonthDao.getByKey(assessmentIndicatorsMonthNew.getId())).orElseGet(KpiAssessmentIndicatorsMonth::new);
        ValidationUtil.isNull(assessmentIndicatorsMonthInDb.getId() ,"AssessmentIndicatorsMonth", "id", assessmentIndicatorsMonthNew.getId());
        assessmentIndicatorsMonthNew.setId(assessmentIndicatorsMonthInDb.getId());
        kpiAssessmentIndicatorsMonthDao.updateAllColumnByKey(assessmentIndicatorsMonthNew);
    }

    @Override
    public KpiAssessmentIndicatorsMonthDTO getByKey(Long id) {
        KpiAssessmentIndicatorsMonth assessmentIndicatorsMonth = Optional.ofNullable(kpiAssessmentIndicatorsMonthDao.getByKey(id)).orElseGet(KpiAssessmentIndicatorsMonth::new);
        ValidationUtil.isNull(assessmentIndicatorsMonth.getId() ,"AssessmentIndicatorsMonth", "id", id);
        return kpiAssessmentIndicatorsMonthMapper.toDto(assessmentIndicatorsMonth);
    }

    @Override
    public List<KpiAssessmentIndicatorsMonthDTO> listAll(KpiAssessmentIndicatorsMonthQueryCriteria criteria) {
        return kpiAssessmentIndicatorsMonthMapper.toDto(kpiAssessmentIndicatorsMonthDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(KpiAssessmentIndicatorsMonthQueryCriteria criteria, Pageable pageable) {
        Page<KpiAssessmentIndicatorsMonth> page = PageUtil.startPage(pageable);
        List<KpiAssessmentIndicatorsMonth> assessmentIndicatorsMonths = kpiAssessmentIndicatorsMonthDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(kpiAssessmentIndicatorsMonthMapper.toDto(assessmentIndicatorsMonths), page.getTotal());
    }

    @Override
    public void download(List<KpiAssessmentIndicatorsMonthDTO> assessmentIndicatorsMonthDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (KpiAssessmentIndicatorsMonthDTO assessmentIndicatorsMonthDTO : assessmentIndicatorsMonthDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("主键", assessmentIndicatorsMonthDTO.getId());
            map.put("弹性域1", assessmentIndicatorsMonthDTO.getAttribute1());
            map.put("弹性域2", assessmentIndicatorsMonthDTO.getAttribute2());
            map.put("弹性域3", assessmentIndicatorsMonthDTO.getAttribute3());
            map.put("弹性域4", assessmentIndicatorsMonthDTO.getAttribute4());
            map.put("弹性域5", assessmentIndicatorsMonthDTO.getAttribute5());
            map.put("Kpi考核指标ID", assessmentIndicatorsMonthDTO.getKpiAssessmentIndicatorsId());
            map.put("一月", assessmentIndicatorsMonthDTO.getJanuary());
            map.put("二月", assessmentIndicatorsMonthDTO.getFebruary());
            map.put("三月", assessmentIndicatorsMonthDTO.getMarch());
            map.put("四月", assessmentIndicatorsMonthDTO.getApril());
            map.put("五月", assessmentIndicatorsMonthDTO.getMay());
            map.put("六月", assessmentIndicatorsMonthDTO.getJune());
            map.put("七月", assessmentIndicatorsMonthDTO.getJuly());
            map.put("八月", assessmentIndicatorsMonthDTO.getAugust());
            map.put("九月", assessmentIndicatorsMonthDTO.getSeptember());
            map.put("十月", assessmentIndicatorsMonthDTO.getOctober());
            map.put("十一月", assessmentIndicatorsMonthDTO.getNovember());
            map.put("十二月", assessmentIndicatorsMonthDTO.getDecember());
            map.put("Q1", assessmentIndicatorsMonthDTO.getSpring());
            map.put("Q1考核结果", assessmentIndicatorsMonthDTO.getSpringAssessmentResults());
            map.put("Q1考核得分", assessmentIndicatorsMonthDTO.getSpringAssessmentScore());
            map.put("Q1修正考核结果", assessmentIndicatorsMonthDTO.getSpringReviseAssessmentResults());
            map.put("Q1修正得分", assessmentIndicatorsMonthDTO.getSpringCorrectionScore());
            map.put("Q2", assessmentIndicatorsMonthDTO.getSummer());
            map.put("Q2考核结果", assessmentIndicatorsMonthDTO.getSummerAssessmentResults());
            map.put("Q2考核得分", assessmentIndicatorsMonthDTO.getSummerAssessmentScore());
            map.put("Q2修正考核结果", assessmentIndicatorsMonthDTO.getSummerReviseAssessmentResults());
            map.put("Q2修正得分", assessmentIndicatorsMonthDTO.getSummerCorrectionScore());
            map.put("Q3", assessmentIndicatorsMonthDTO.getAutumn());
            map.put("Q3考核结果", assessmentIndicatorsMonthDTO.getAutumnAssessmentResults());
            map.put("Q3考核得分", assessmentIndicatorsMonthDTO.getAutumnAssessmentScore());
            map.put("Q3修正考核结果", assessmentIndicatorsMonthDTO.getAutumnReviseAssessmentResults());
            map.put("Q3修正得分", assessmentIndicatorsMonthDTO.getAutumnCorrectionScore());
            map.put("Q4", assessmentIndicatorsMonthDTO.getWinter());
            map.put("Q4考核结果", assessmentIndicatorsMonthDTO.getWinterAssessmentResults());
            map.put("Q4考核得分", assessmentIndicatorsMonthDTO.getWinterAssessmentScore());
            map.put("Q1得分", assessmentIndicatorsMonthDTO.getSpringScore());
            map.put("Q1修正", assessmentIndicatorsMonthDTO.getSpringCorrect());
            map.put("Q2得分", assessmentIndicatorsMonthDTO.getSummerScore());
            map.put("Q2修正", assessmentIndicatorsMonthDTO.getSummerCorrect());
            map.put("Q3得分", assessmentIndicatorsMonthDTO.getAutumnScore());
            map.put("Q3修正", assessmentIndicatorsMonthDTO.getAutumnCorrect());
            map.put("Q4得分", assessmentIndicatorsMonthDTO.getWinterScore());
            map.put("创建时间", assessmentIndicatorsMonthDTO.getCreateTime());
            map.put("创建人", assessmentIndicatorsMonthDTO.getCreateBy());
            map.put("更新时间", assessmentIndicatorsMonthDTO.getUpdateTime());
            map.put("更新人", assessmentIndicatorsMonthDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
