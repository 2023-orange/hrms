package com.sunten.hrms.kpi.service.impl;

import com.sunten.hrms.kpi.domain.KpiAssessmentDimension;
import com.sunten.hrms.kpi.dao.KpiAssessmentDimensionDao;
import com.sunten.hrms.kpi.service.KpiAssessmentDimensionService;
import com.sunten.hrms.kpi.dto.KpiAssessmentDimensionDTO;
import com.sunten.hrms.kpi.dto.KpiAssessmentDimensionQueryCriteria;
import com.sunten.hrms.kpi.mapper.KpiAssessmentDimensionMapper;
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
 * KPI考核维度表 服务实现类
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-27
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KpiAssessmentDimensionServiceImpl extends ServiceImpl<KpiAssessmentDimensionDao, KpiAssessmentDimension> implements KpiAssessmentDimensionService {
    private final KpiAssessmentDimensionDao kpiAssessmentDimensionDao;
    private final KpiAssessmentDimensionMapper kpiAssessmentDimensionMapper;

    public KpiAssessmentDimensionServiceImpl(KpiAssessmentDimensionDao kpiAssessmentDimensionDao, KpiAssessmentDimensionMapper kpiAssessmentDimensionMapper) {
        this.kpiAssessmentDimensionDao = kpiAssessmentDimensionDao;
        this.kpiAssessmentDimensionMapper = kpiAssessmentDimensionMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KpiAssessmentDimensionDTO insert(KpiAssessmentDimension assessmentDimensionNew) {
        kpiAssessmentDimensionDao.insertAllColumn(assessmentDimensionNew);
        return kpiAssessmentDimensionMapper.toDto(assessmentDimensionNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        KpiAssessmentDimension assessmentDimension = new KpiAssessmentDimension();
        assessmentDimension.setId(id);
        this.delete(assessmentDimension);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(KpiAssessmentDimension assessmentDimension) {
        // TODO    确认删除前是否需要做检查
        kpiAssessmentDimensionDao.deleteByEntityKey(assessmentDimension);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(KpiAssessmentDimension assessmentDimensionNew) {
        KpiAssessmentDimension assessmentDimensionInDb = Optional.ofNullable(kpiAssessmentDimensionDao.getByKey(assessmentDimensionNew.getId())).orElseGet(KpiAssessmentDimension::new);
        ValidationUtil.isNull(assessmentDimensionInDb.getId() ,"AssessmentDimension", "id", assessmentDimensionNew.getId());
        assessmentDimensionNew.setId(assessmentDimensionInDb.getId());
        kpiAssessmentDimensionDao.updateAllColumnByKey(assessmentDimensionNew);
    }

    @Override
    public KpiAssessmentDimensionDTO getByKey(Long id) {
        KpiAssessmentDimension assessmentDimension = Optional.ofNullable(kpiAssessmentDimensionDao.getByKey(id)).orElseGet(KpiAssessmentDimension::new);
        ValidationUtil.isNull(assessmentDimension.getId() ,"AssessmentDimension", "id", id);
        return kpiAssessmentDimensionMapper.toDto(assessmentDimension);
    }

    @Override
    public List<KpiAssessmentDimensionDTO> listAll(KpiAssessmentDimensionQueryCriteria criteria) {
        return kpiAssessmentDimensionMapper.toDto(kpiAssessmentDimensionDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(KpiAssessmentDimensionQueryCriteria criteria, Pageable pageable) {
        Page<KpiAssessmentDimension> page = PageUtil.startPage(pageable);
        List<KpiAssessmentDimension> assessmentDimensions = kpiAssessmentDimensionDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(kpiAssessmentDimensionMapper.toDto(assessmentDimensions), page.getTotal());
    }

    @Override
    public void download(List<KpiAssessmentDimensionDTO> assessmentDimensionDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (KpiAssessmentDimensionDTO assessmentDimensionDTO : assessmentDimensionDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("主键", assessmentDimensionDTO.getId());
            map.put("考核维度", assessmentDimensionDTO.getAssessmentDimension());
            map.put("是否启用", assessmentDimensionDTO.getEnableFlag());
            map.put("弹性域1", assessmentDimensionDTO.getAttribute1());
            map.put("弹性域2", assessmentDimensionDTO.getAttribute2());
            map.put("弹性域3", assessmentDimensionDTO.getAttribute3());
            map.put("弹性域4", assessmentDimensionDTO.getAttribute4());
            map.put("弹性域5", assessmentDimensionDTO.getAttribute5());
            map.put("创建时间", assessmentDimensionDTO.getCreateTime());
            map.put("创建人", assessmentDimensionDTO.getCreateBy());
            map.put("更新时间", assessmentDimensionDTO.getUpdateTime());
            map.put("更新人", assessmentDimensionDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<KpiAssessmentDimension> listAllByEnableFlag() {
        return kpiAssessmentDimensionDao.listAllByEnableFlag();
    }
}
