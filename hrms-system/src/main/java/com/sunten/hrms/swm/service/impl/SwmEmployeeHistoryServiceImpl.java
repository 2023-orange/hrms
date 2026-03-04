package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.swm.domain.SwmEmployeeHistory;
import com.sunten.hrms.swm.dao.SwmEmployeeHistoryDao;
import com.sunten.hrms.swm.service.SwmEmployeeHistoryService;
import com.sunten.hrms.swm.dto.SwmEmployeeHistoryDTO;
import com.sunten.hrms.swm.dto.SwmEmployeeHistoryQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmEmployeeHistoryMapper;
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
 * 员工信息历史表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmEmployeeHistoryServiceImpl extends ServiceImpl<SwmEmployeeHistoryDao, SwmEmployeeHistory> implements SwmEmployeeHistoryService {
    private final SwmEmployeeHistoryDao swmEmployeeHistoryDao;
    private final SwmEmployeeHistoryMapper swmEmployeeHistoryMapper;

    public SwmEmployeeHistoryServiceImpl(SwmEmployeeHistoryDao swmEmployeeHistoryDao, SwmEmployeeHistoryMapper swmEmployeeHistoryMapper) {
        this.swmEmployeeHistoryDao = swmEmployeeHistoryDao;
        this.swmEmployeeHistoryMapper = swmEmployeeHistoryMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmEmployeeHistoryDTO insert(SwmEmployeeHistory employeeHistoryNew) {
        swmEmployeeHistoryDao.insertAllColumn(employeeHistoryNew);
        return swmEmployeeHistoryMapper.toDto(employeeHistoryNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmEmployeeHistory employeeHistory = new SwmEmployeeHistory();
        employeeHistory.setId(id);
        this.delete(employeeHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmEmployeeHistory employeeHistory) {
        // TODO    确认删除前是否需要做检查
        swmEmployeeHistoryDao.deleteByEntityKey(employeeHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmEmployeeHistory employeeHistoryNew) {
        SwmEmployeeHistory employeeHistoryInDb = Optional.ofNullable(swmEmployeeHistoryDao.getByKey(employeeHistoryNew.getId())).orElseGet(SwmEmployeeHistory::new);
        ValidationUtil.isNull(employeeHistoryInDb.getId() ,"EmployeeHistory", "id", employeeHistoryNew.getId());
        employeeHistoryNew.setId(employeeHistoryInDb.getId());
        swmEmployeeHistoryDao.updateAllColumnByKey(employeeHistoryNew);
    }

    @Override
    public SwmEmployeeHistoryDTO getByKey(Long id) {
        SwmEmployeeHistory employeeHistory = Optional.ofNullable(swmEmployeeHistoryDao.getByKey(id)).orElseGet(SwmEmployeeHistory::new);
        ValidationUtil.isNull(employeeHistory.getId() ,"EmployeeHistory", "id", id);
        return swmEmployeeHistoryMapper.toDto(employeeHistory);
    }

    @Override
    public List<SwmEmployeeHistoryDTO> listAll(SwmEmployeeHistoryQueryCriteria criteria) {
        return swmEmployeeHistoryMapper.toDto(swmEmployeeHistoryDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmEmployeeHistoryQueryCriteria criteria, Pageable pageable) {
        Page<SwmEmployeeHistory> page = PageUtil.startPage(pageable);
        List<SwmEmployeeHistory> employeeHistorys = swmEmployeeHistoryDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmEmployeeHistoryMapper.toDto(employeeHistorys), page.getTotal());
    }

    @Override
    public void download(List<SwmEmployeeHistoryDTO> employeeHistoryDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmEmployeeHistoryDTO employeeHistoryDTO : employeeHistoryDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id主键", employeeHistoryDTO.getId());
            map.put("工牌号", employeeHistoryDTO.getWorkCard());
            map.put("员工姓名", employeeHistoryDTO.getEmployeeName());
            map.put("岗位技能工资", employeeHistoryDTO.getPostSkillSalary());
            map.put("目标绩效工资", employeeHistoryDTO.getTargetPerformancePay());
            map.put("包干区分（1包干，0非包干）", employeeHistoryDTO.getDivisionContractFlag());
            map.put("变化幅度", employeeHistoryDTO.getRangeChange());
            map.put("弹性域", employeeHistoryDTO.getAttribute1());
            map.put("弹性域", employeeHistoryDTO.getAttribute2());
            map.put("弹性域", employeeHistoryDTO.getAttribute3());
            map.put("弹性域", employeeHistoryDTO.getAttribute4());
            map.put("弹性域", employeeHistoryDTO.getAttribute5());
            map.put("创建时间", employeeHistoryDTO.getCreateTime());
            map.put("创建人id", employeeHistoryDTO.getCreateBy());
            map.put("修改时间", employeeHistoryDTO.getUpdateTime());
            map.put("修改人id", employeeHistoryDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<SwmEmployeeHistoryDTO> getSwmEmployeeHistoryByWorkCard(String workCard) {
        return swmEmployeeHistoryMapper.toDto(swmEmployeeHistoryDao.getSwmEmployeeHistoryByWorkCard(workCard));
    }
}
