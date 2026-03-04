package com.sunten.hrms.td.service.impl;

import com.sunten.hrms.td.domain.TdPlanCheckMethod;
import com.sunten.hrms.td.dao.TdPlanCheckMethodDao;
import com.sunten.hrms.td.service.TdPlanCheckMethodService;
import com.sunten.hrms.td.dto.TdPlanCheckMethodDTO;
import com.sunten.hrms.td.dto.TdPlanCheckMethodQueryCriteria;
import com.sunten.hrms.td.mapper.TdPlanCheckMethodMapper;
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
 * 培训效果评价方式表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2022-03-08
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdPlanCheckMethodServiceImpl extends ServiceImpl<TdPlanCheckMethodDao, TdPlanCheckMethod> implements TdPlanCheckMethodService {
    private final TdPlanCheckMethodDao tdPlanCheckMethodDao;
    private final TdPlanCheckMethodMapper tdPlanCheckMethodMapper;

    public TdPlanCheckMethodServiceImpl(TdPlanCheckMethodDao tdPlanCheckMethodDao, TdPlanCheckMethodMapper tdPlanCheckMethodMapper) {
        this.tdPlanCheckMethodDao = tdPlanCheckMethodDao;
        this.tdPlanCheckMethodMapper = tdPlanCheckMethodMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdPlanCheckMethodDTO insert(TdPlanCheckMethod planCheckMethodNew) {
        tdPlanCheckMethodDao.insertAllColumn(planCheckMethodNew);
        return tdPlanCheckMethodMapper.toDto(planCheckMethodNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdPlanCheckMethod planCheckMethod = new TdPlanCheckMethod();
        planCheckMethod.setId(id);
        this.delete(planCheckMethod);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdPlanCheckMethod planCheckMethod) {
        // TODO    确认删除前是否需要做检查
        tdPlanCheckMethodDao.deleteByEntityKey(planCheckMethod);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdPlanCheckMethod planCheckMethodNew) {
        TdPlanCheckMethod planCheckMethodInDb = Optional.ofNullable(tdPlanCheckMethodDao.getByKey(planCheckMethodNew.getId())).orElseGet(TdPlanCheckMethod::new);
        ValidationUtil.isNull(planCheckMethodInDb.getId() ,"PlanCheckMethod", "id", planCheckMethodNew.getId());
        planCheckMethodNew.setId(planCheckMethodInDb.getId());
        tdPlanCheckMethodDao.updateAllColumnByKey(planCheckMethodNew);
    }

    @Override
    public TdPlanCheckMethodDTO getByKey(Long id) {
        TdPlanCheckMethod planCheckMethod = Optional.ofNullable(tdPlanCheckMethodDao.getByKey(id)).orElseGet(TdPlanCheckMethod::new);
        ValidationUtil.isNull(planCheckMethod.getId() ,"PlanCheckMethod", "id", id);
        return tdPlanCheckMethodMapper.toDto(planCheckMethod);
    }

    @Override
    public List<TdPlanCheckMethodDTO> listAll(TdPlanCheckMethodQueryCriteria criteria) {
        return tdPlanCheckMethodMapper.toDto(tdPlanCheckMethodDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdPlanCheckMethodQueryCriteria criteria, Pageable pageable) {
        Page<TdPlanCheckMethod> page = PageUtil.startPage(pageable);
        List<TdPlanCheckMethod> planCheckMethods = tdPlanCheckMethodDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdPlanCheckMethodMapper.toDto(planCheckMethods), page.getTotal());
    }

    @Override
    public void download(List<TdPlanCheckMethodDTO> planCheckMethodDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdPlanCheckMethodDTO planCheckMethodDTO : planCheckMethodDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("td_plan_implement的id", planCheckMethodDTO.getPlanImplementId());
            map.put("考核方式", planCheckMethodDTO.getCheckMethod());
            map.put("其它手填的考核方式", planCheckMethodDTO.getEvaluationResults());
            map.put("enabledFlag", planCheckMethodDTO.getEnabledFlag());
            map.put("id", planCheckMethodDTO.getId());
            map.put("createBy", planCheckMethodDTO.getCreateBy());
            map.put("updateTime", planCheckMethodDTO.getUpdateTime());
            map.put("createTime", planCheckMethodDTO.getCreateTime());
            map.put("updateBy", planCheckMethodDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByMethodAndEnabledFlag(String checkMethod, Long planImplementId, Long updateBy) {
        tdPlanCheckMethodDao.deleteByMethodAndEnabledFlag(checkMethod, planImplementId, updateBy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEvaluationResultsByMethodAndPlanImplementId(TdPlanCheckMethod tdPlanCheckMethod) {
        tdPlanCheckMethodDao.updateEvaluationResultsByMethodAndPlanImplementId(tdPlanCheckMethod);
    }
}
