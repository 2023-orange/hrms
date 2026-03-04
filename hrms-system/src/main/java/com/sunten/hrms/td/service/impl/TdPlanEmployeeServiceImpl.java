package com.sunten.hrms.td.service.impl;

import com.sunten.hrms.td.domain.TdPlanEmployee;
import com.sunten.hrms.td.dao.TdPlanEmployeeDao;
import com.sunten.hrms.td.service.TdPlanEmployeeService;
import com.sunten.hrms.td.dto.TdPlanEmployeeDTO;
import com.sunten.hrms.td.dto.TdPlanEmployeeQueryCriteria;
import com.sunten.hrms.td.mapper.TdPlanEmployeeMapper;
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
 * 参训人员表（包括讲师） 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-05-25
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdPlanEmployeeServiceImpl extends ServiceImpl<TdPlanEmployeeDao, TdPlanEmployee> implements TdPlanEmployeeService {
    private final TdPlanEmployeeDao tdPlanEmployeeDao;
    private final TdPlanEmployeeMapper tdPlanEmployeeMapper;

    public TdPlanEmployeeServiceImpl(TdPlanEmployeeDao tdPlanEmployeeDao, TdPlanEmployeeMapper tdPlanEmployeeMapper) {
        this.tdPlanEmployeeDao = tdPlanEmployeeDao;
        this.tdPlanEmployeeMapper = tdPlanEmployeeMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdPlanEmployeeDTO insert(TdPlanEmployee planEmployeeNew) {
        tdPlanEmployeeDao.insertAllColumn(planEmployeeNew);
        return tdPlanEmployeeMapper.toDto(planEmployeeNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdPlanEmployee planEmployee = new TdPlanEmployee();
        planEmployee.setId(id);
        this.delete(planEmployee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdPlanEmployee planEmployee) {
        // TODO    确认删除前是否需要做检查
        tdPlanEmployeeDao.deleteByEntityKey(planEmployee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdPlanEmployee planEmployeeNew) {
        TdPlanEmployee planEmployeeInDb = Optional.ofNullable(tdPlanEmployeeDao.getByKey(planEmployeeNew.getId())).orElseGet(TdPlanEmployee::new);
        ValidationUtil.isNull(planEmployeeInDb.getId() ,"PlanEmployee", "id", planEmployeeNew.getId());
        planEmployeeNew.setId(planEmployeeInDb.getId());
        tdPlanEmployeeDao.updateAllColumnByKey(planEmployeeNew);
    }

    @Override
    public TdPlanEmployeeDTO getByKey(Long id) {
        TdPlanEmployee planEmployee = Optional.ofNullable(tdPlanEmployeeDao.getByKey(id)).orElseGet(TdPlanEmployee::new);
        ValidationUtil.isNull(planEmployee.getId() ,"PlanEmployee", "id", id);
        return tdPlanEmployeeMapper.toDto(planEmployee);
    }

    @Override
    public List<TdPlanEmployeeDTO> listAll(TdPlanEmployeeQueryCriteria criteria) {
        return tdPlanEmployeeMapper.toDto(tdPlanEmployeeDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdPlanEmployeeQueryCriteria criteria, Pageable pageable) {
        Page<TdPlanEmployee> page = PageUtil.startPage(pageable);
        List<TdPlanEmployee> planEmployees = tdPlanEmployeeDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdPlanEmployeeMapper.toDto(planEmployees), page.getTotal());
    }

    @Override
    public void download(List<TdPlanEmployeeDTO> planEmployeeDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdPlanEmployeeDTO planEmployeeDTO : planEmployeeDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("培训计划实施id", planEmployeeDTO.getPlanImplementId());
            map.put("人员id", planEmployeeDTO.getEmployeeId());
            map.put("有效标记", planEmployeeDTO.getEnabledFlag());
            map.put("employee、teacher", planEmployeeDTO.getType());
            map.put("id", planEmployeeDTO.getId());
            map.put("创建时间", planEmployeeDTO.getCreateTime());
            map.put("创建人ID", planEmployeeDTO.getCreateBy());
            map.put("修改时间", planEmployeeDTO.getUpdateTime());
            map.put("修改人ID", planEmployeeDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByEnabled(TdPlanEmployee planEmployee) {
        tdPlanEmployeeDao.deleteByEnabled(planEmployee);
    }
}
