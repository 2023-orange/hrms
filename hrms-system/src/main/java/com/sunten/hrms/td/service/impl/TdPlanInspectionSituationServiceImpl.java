package com.sunten.hrms.td.service.impl;

import com.sunten.hrms.td.domain.TdPlanInspectionSituation;
import com.sunten.hrms.td.dao.TdPlanInspectionSituationDao;
import com.sunten.hrms.td.service.TdPlanInspectionSituationService;
import com.sunten.hrms.td.dto.TdPlanInspectionSituationDTO;
import com.sunten.hrms.td.dto.TdPlanInspectionSituationQueryCriteria;
import com.sunten.hrms.td.mapper.TdPlanInspectionSituationMapper;
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
 * 培训考核情况 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2022-03-11
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdPlanInspectionSituationServiceImpl extends ServiceImpl<TdPlanInspectionSituationDao, TdPlanInspectionSituation> implements TdPlanInspectionSituationService {
    private final TdPlanInspectionSituationDao tdPlanInspectionSituationDao;
    private final TdPlanInspectionSituationMapper tdPlanInspectionSituationMapper;

    public TdPlanInspectionSituationServiceImpl(TdPlanInspectionSituationDao tdPlanInspectionSituationDao, TdPlanInspectionSituationMapper tdPlanInspectionSituationMapper) {
        this.tdPlanInspectionSituationDao = tdPlanInspectionSituationDao;
        this.tdPlanInspectionSituationMapper = tdPlanInspectionSituationMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdPlanInspectionSituationDTO insert(TdPlanInspectionSituation planInspectionSituationNew) {
        tdPlanInspectionSituationDao.insertAllColumn(planInspectionSituationNew);
        return tdPlanInspectionSituationMapper.toDto(planInspectionSituationNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdPlanInspectionSituation planInspectionSituation = new TdPlanInspectionSituation();
        planInspectionSituation.setId(id);
        this.delete(planInspectionSituation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdPlanInspectionSituation planInspectionSituation) {
        // TODO    确认删除前是否需要做检查
        tdPlanInspectionSituationDao.deleteByEntityKey(planInspectionSituation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdPlanInspectionSituation planInspectionSituationNew) {
        TdPlanInspectionSituation planInspectionSituationInDb = Optional.ofNullable(tdPlanInspectionSituationDao.getByKey(planInspectionSituationNew.getId())).orElseGet(TdPlanInspectionSituation::new);
        ValidationUtil.isNull(planInspectionSituationInDb.getId() ,"PlanInspectionSituation", "id", planInspectionSituationNew.getId());
        planInspectionSituationNew.setId(planInspectionSituationInDb.getId());
        tdPlanInspectionSituationDao.updateAllColumnByKey(planInspectionSituationNew);
    }

    @Override
    public TdPlanInspectionSituationDTO getByKey(Long id) {
        TdPlanInspectionSituation planInspectionSituation = Optional.ofNullable(tdPlanInspectionSituationDao.getByKey(id)).orElseGet(TdPlanInspectionSituation::new);
        ValidationUtil.isNull(planInspectionSituation.getId() ,"PlanInspectionSituation", "id", id);
        return tdPlanInspectionSituationMapper.toDto(planInspectionSituation);
    }

    @Override
    public List<TdPlanInspectionSituationDTO> listAll(TdPlanInspectionSituationQueryCriteria criteria) {
        return tdPlanInspectionSituationMapper.toDto(tdPlanInspectionSituationDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdPlanInspectionSituationQueryCriteria criteria, Pageable pageable) {
        Page<TdPlanInspectionSituation> page = PageUtil.startPage(pageable);
        List<TdPlanInspectionSituation> planInspectionSituations = tdPlanInspectionSituationDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdPlanInspectionSituationMapper.toDto(planInspectionSituations), page.getTotal());
    }

    @Override
    public void download(List<TdPlanInspectionSituationDTO> planInspectionSituationDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdPlanInspectionSituationDTO planInspectionSituationDTO : planInspectionSituationDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("实施id", planInspectionSituationDTO.getPlanImplementId());
            map.put("考核的总人数", planInspectionSituationDTO.getPeopleAssessedTotal());
            map.put("一次合格人数", planInspectionSituationDTO.getOneTimePassTotal());
            map.put("一次合格率", planInspectionSituationDTO.getPrimaryPassRate());
            map.put("补考人数", planInspectionSituationDTO.getMakeExaminationNumber());
            map.put("补考合格率", planInspectionSituationDTO.getMakePassRate());
            map.put("id", planInspectionSituationDTO.getId());
            map.put("enabledFlag", planInspectionSituationDTO.getEnabledFlag());
            map.put("checkMethod", planInspectionSituationDTO.getCheckMethod());
            map.put("updateTime", planInspectionSituationDTO.getUpdateTime());
            map.put("createBy", planInspectionSituationDTO.getCreateBy());
            map.put("createTime", planInspectionSituationDTO.getCreateTime());
            map.put("updateBy", planInspectionSituationDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
