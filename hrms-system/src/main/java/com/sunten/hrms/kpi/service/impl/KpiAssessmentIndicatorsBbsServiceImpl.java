package com.sunten.hrms.kpi.service.impl;

import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.kpi.domain.KpiAssessmentIndicatorsBbs;
import com.sunten.hrms.kpi.dao.KpiAssessmentIndicatorsBbsDao;
import com.sunten.hrms.kpi.service.KpiAssessmentIndicatorsBbsService;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsBbsDTO;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsBbsQueryCriteria;
import com.sunten.hrms.kpi.mapper.KpiAssessmentIndicatorsBbsMapper;
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
 *  服务实现类
 * </p>
 *
 * @author zhoujy
 * @since 2023-12-26
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KpiAssessmentIndicatorsBbsServiceImpl extends ServiceImpl<KpiAssessmentIndicatorsBbsDao, KpiAssessmentIndicatorsBbs> implements KpiAssessmentIndicatorsBbsService {
    private final KpiAssessmentIndicatorsBbsDao kpiAssessmentIndicatorsBbsDao;
    private final KpiAssessmentIndicatorsBbsMapper kpiAssessmentIndicatorsBbsMapper;
    private final FndUserService fndUserService;

    public KpiAssessmentIndicatorsBbsServiceImpl(KpiAssessmentIndicatorsBbsDao kpiAssessmentIndicatorsBbsDao, KpiAssessmentIndicatorsBbsMapper kpiAssessmentIndicatorsBbsMapper, FndUserService fndUserService) {
        this.kpiAssessmentIndicatorsBbsDao = kpiAssessmentIndicatorsBbsDao;
        this.kpiAssessmentIndicatorsBbsMapper = kpiAssessmentIndicatorsBbsMapper;
        this.fndUserService = fndUserService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KpiAssessmentIndicatorsBbsDTO insert(KpiAssessmentIndicatorsBbs assessmentIndicatorsBbsNew) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        assessmentIndicatorsBbsNew.setCreateName(user.getEmployee().getName());
        kpiAssessmentIndicatorsBbsDao.insertAllColumn(assessmentIndicatorsBbsNew);
        return kpiAssessmentIndicatorsBbsMapper.toDto(assessmentIndicatorsBbsNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete() {
        KpiAssessmentIndicatorsBbs assessmentIndicatorsBbs = new KpiAssessmentIndicatorsBbs();
        this.delete(assessmentIndicatorsBbs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(KpiAssessmentIndicatorsBbs assessmentIndicatorsBbs) {
        // TODO    确认删除前是否需要做检查
        kpiAssessmentIndicatorsBbsDao.deleteByEntityKey(assessmentIndicatorsBbs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(KpiAssessmentIndicatorsBbs assessmentIndicatorsBbsNew) {
        KpiAssessmentIndicatorsBbs assessmentIndicatorsBbsInDb = Optional.ofNullable(kpiAssessmentIndicatorsBbsDao.getByKey()).orElseGet(KpiAssessmentIndicatorsBbs::new);
        kpiAssessmentIndicatorsBbsDao.updateAllColumnByKey(assessmentIndicatorsBbsNew);
    }

    @Override
    public KpiAssessmentIndicatorsBbsDTO getByKey() {
        KpiAssessmentIndicatorsBbs assessmentIndicatorsBbs = Optional.ofNullable(kpiAssessmentIndicatorsBbsDao.getByKey()).orElseGet(KpiAssessmentIndicatorsBbs::new);
        return kpiAssessmentIndicatorsBbsMapper.toDto(assessmentIndicatorsBbs);
    }

    @Override
    public List<KpiAssessmentIndicatorsBbsDTO> listAll(KpiAssessmentIndicatorsBbsQueryCriteria criteria) {
        return kpiAssessmentIndicatorsBbsMapper.toDto(kpiAssessmentIndicatorsBbsDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(KpiAssessmentIndicatorsBbsQueryCriteria criteria, Pageable pageable) {
        Page<KpiAssessmentIndicatorsBbs> page = PageUtil.startPage(pageable);
        List<KpiAssessmentIndicatorsBbs> assessmentIndicatorsBbss = kpiAssessmentIndicatorsBbsDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(kpiAssessmentIndicatorsBbsMapper.toDto(assessmentIndicatorsBbss), page.getTotal());
    }

    @Override
    public void download(List<KpiAssessmentIndicatorsBbsDTO> assessmentIndicatorsBbsDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (KpiAssessmentIndicatorsBbsDTO assessmentIndicatorsBbsDTO : assessmentIndicatorsBbsDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("主键", assessmentIndicatorsBbsDTO.getId());
            map.put("考核指标ID", assessmentIndicatorsBbsDTO.getKpiAssessmentIndicatorsId());
            map.put("BBS内容", assessmentIndicatorsBbsDTO.getBbsContent());
            map.put("创建人姓名", assessmentIndicatorsBbsDTO.getCreateName());
            map.put("创建人", assessmentIndicatorsBbsDTO.getCreateBy());
            map.put("创建时间", assessmentIndicatorsBbsDTO.getCreateTime());
            map.put("更新人", assessmentIndicatorsBbsDTO.getUpdateBy());
            map.put("更新时间", assessmentIndicatorsBbsDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
