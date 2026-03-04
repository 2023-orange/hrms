package com.sunten.hrms.td.service.impl;

import com.sunten.hrms.td.domain.TdPlanImplementDept;
import com.sunten.hrms.td.dao.TdPlanImplementDeptDao;
import com.sunten.hrms.td.service.TdPlanImplementDeptService;
import com.sunten.hrms.td.dto.TdPlanImplementDeptDTO;
import com.sunten.hrms.td.dto.TdPlanImplementDeptQueryCriteria;
import com.sunten.hrms.td.mapper.TdPlanImplementDeptMapper;
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
 * 培训实施参与部门扩展表（用于后期统计使用） 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-06-21
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdPlanImplementDeptServiceImpl extends ServiceImpl<TdPlanImplementDeptDao, TdPlanImplementDept> implements TdPlanImplementDeptService {
    private final TdPlanImplementDeptDao tdPlanImplementDeptDao;
    private final TdPlanImplementDeptMapper tdPlanImplementDeptMapper;

    public TdPlanImplementDeptServiceImpl(TdPlanImplementDeptDao tdPlanImplementDeptDao, TdPlanImplementDeptMapper tdPlanImplementDeptMapper) {
        this.tdPlanImplementDeptDao = tdPlanImplementDeptDao;
        this.tdPlanImplementDeptMapper = tdPlanImplementDeptMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdPlanImplementDeptDTO insert(TdPlanImplementDept planImplementDeptNew) {
        tdPlanImplementDeptDao.insertAllColumn(planImplementDeptNew);
        return tdPlanImplementDeptMapper.toDto(planImplementDeptNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdPlanImplementDept planImplementDept = new TdPlanImplementDept();
        planImplementDept.setId(id);
        this.delete(planImplementDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdPlanImplementDept planImplementDept) {
        // TODO    确认删除前是否需要做检查
        tdPlanImplementDeptDao.deleteByEntityKey(planImplementDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdPlanImplementDept planImplementDeptNew) {
        TdPlanImplementDept planImplementDeptInDb = Optional.ofNullable(tdPlanImplementDeptDao.getByKey(planImplementDeptNew.getId())).orElseGet(TdPlanImplementDept::new);
        ValidationUtil.isNull(planImplementDeptInDb.getId() ,"PlanImplementDept", "id", planImplementDeptNew.getId());
        planImplementDeptNew.setId(planImplementDeptInDb.getId());
        tdPlanImplementDeptDao.updateAllColumnByKey(planImplementDeptNew);
    }

    @Override
    public TdPlanImplementDeptDTO getByKey(Long id) {
        TdPlanImplementDept planImplementDept = Optional.ofNullable(tdPlanImplementDeptDao.getByKey(id)).orElseGet(TdPlanImplementDept::new);
        ValidationUtil.isNull(planImplementDept.getId() ,"PlanImplementDept", "id", id);
        return tdPlanImplementDeptMapper.toDto(planImplementDept);
    }

    @Override
    public List<TdPlanImplementDeptDTO> listAll(TdPlanImplementDeptQueryCriteria criteria) {
        return tdPlanImplementDeptMapper.toDto(tdPlanImplementDeptDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdPlanImplementDeptQueryCriteria criteria, Pageable pageable) {
        Page<TdPlanImplementDept> page = PageUtil.startPage(pageable);
        List<TdPlanImplementDept> planImplementDepts = tdPlanImplementDeptDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdPlanImplementDeptMapper.toDto(planImplementDepts), page.getTotal());
    }

    @Override
    public void download(List<TdPlanImplementDeptDTO> planImplementDeptDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdPlanImplementDeptDTO planImplementDeptDTO : planImplementDeptDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("培训计划实施id", planImplementDeptDTO.getPlanImplementId());
            map.put("部门id", planImplementDeptDTO.getDeptId());
            map.put("有效标记", planImplementDeptDTO.getEnabledFlag());
            map.put("部门", planImplementDeptDTO.getDeptName());
            map.put("id", planImplementDeptDTO.getId());
            map.put("创建时间", planImplementDeptDTO.getCreateTime());
            map.put("创建人ID", planImplementDeptDTO.getCreateBy());
            map.put("修改时间", planImplementDeptDTO.getUpdateTime());
            map.put("修改人ID", planImplementDeptDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByEnabled(TdPlanImplementDept planImplementDept) {
        tdPlanImplementDeptDao.deleteByEnabled(planImplementDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertByImplement(TdPlanImplementDept planImplementDept) {
        tdPlanImplementDeptDao.insertByImplement(planImplementDept);
    }
}
