package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.swm.domain.SwmEmployeeMonthlyBak;
import com.sunten.hrms.swm.dao.SwmEmployeeMonthlyBakDao;
import com.sunten.hrms.swm.service.SwmEmployeeMonthlyBakService;
import com.sunten.hrms.swm.dto.SwmEmployeeMonthlyBakDTO;
import com.sunten.hrms.swm.dto.SwmEmployeeMonthlyBakQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmEmployeeMonthlyBakMapper;
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
 * 薪酬员工信息每月备份表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-09-15
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmEmployeeMonthlyBakServiceImpl extends ServiceImpl<SwmEmployeeMonthlyBakDao, SwmEmployeeMonthlyBak> implements SwmEmployeeMonthlyBakService {
    private final SwmEmployeeMonthlyBakDao swmEmployeeMonthlyBakDao;
    private final SwmEmployeeMonthlyBakMapper swmEmployeeMonthlyBakMapper;

    public SwmEmployeeMonthlyBakServiceImpl(SwmEmployeeMonthlyBakDao swmEmployeeMonthlyBakDao, SwmEmployeeMonthlyBakMapper swmEmployeeMonthlyBakMapper) {
        this.swmEmployeeMonthlyBakDao = swmEmployeeMonthlyBakDao;
        this.swmEmployeeMonthlyBakMapper = swmEmployeeMonthlyBakMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmEmployeeMonthlyBakDTO insert(SwmEmployeeMonthlyBak employeeMonthlyBakNew) {
        swmEmployeeMonthlyBakDao.insertAllColumn(employeeMonthlyBakNew);
        return swmEmployeeMonthlyBakMapper.toDto(employeeMonthlyBakNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmEmployeeMonthlyBak employeeMonthlyBak = new SwmEmployeeMonthlyBak();
        employeeMonthlyBak.setId(id);
        this.delete(employeeMonthlyBak);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmEmployeeMonthlyBak employeeMonthlyBak) {
        // TODO    确认删除前是否需要做检查
        swmEmployeeMonthlyBakDao.deleteByEntityKey(employeeMonthlyBak);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmEmployeeMonthlyBak employeeMonthlyBakNew) {
        SwmEmployeeMonthlyBak employeeMonthlyBakInDb = Optional.ofNullable(swmEmployeeMonthlyBakDao.getByKey(employeeMonthlyBakNew.getId())).orElseGet(SwmEmployeeMonthlyBak::new);
        ValidationUtil.isNull(employeeMonthlyBakInDb.getId() ,"EmployeeMonthlyBak", "id", employeeMonthlyBakNew.getId());
        employeeMonthlyBakNew.setId(employeeMonthlyBakInDb.getId());
        swmEmployeeMonthlyBakDao.updateAllColumnByKey(employeeMonthlyBakNew);
    }

    @Override
    public SwmEmployeeMonthlyBakDTO getByKey(Long id) {
        SwmEmployeeMonthlyBak employeeMonthlyBak = Optional.ofNullable(swmEmployeeMonthlyBakDao.getByKey(id)).orElseGet(SwmEmployeeMonthlyBak::new);
        ValidationUtil.isNull(employeeMonthlyBak.getId() ,"EmployeeMonthlyBak", "id", id);
        return swmEmployeeMonthlyBakMapper.toDto(employeeMonthlyBak);
    }

    @Override
    public List<SwmEmployeeMonthlyBakDTO> listAll(SwmEmployeeMonthlyBakQueryCriteria criteria) {
        return swmEmployeeMonthlyBakMapper.toDto(swmEmployeeMonthlyBakDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmEmployeeMonthlyBakQueryCriteria criteria, Pageable pageable) {
        Page<SwmEmployeeMonthlyBak> page = PageUtil.startPage(pageable);
        List<SwmEmployeeMonthlyBak> employeeMonthlyBaks = swmEmployeeMonthlyBakDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmEmployeeMonthlyBakMapper.toDto(employeeMonthlyBaks), page.getTotal());
    }

    @Override
    public void download(List<SwmEmployeeMonthlyBakDTO> employeeMonthlyBakDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmEmployeeMonthlyBakDTO employeeMonthlyBakDTO : employeeMonthlyBakDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("备份日期", DateUtil.localDateToStr(employeeMonthlyBakDTO.getBakDate()));
            map.put("实际备份区间", null == employeeMonthlyBakDTO.getIncomePeriod() ? "" : employeeMonthlyBakDTO.getIncomePeriod());
            map.put("工牌号", employeeMonthlyBakDTO.getWorkCard());
            map.put("姓名", employeeMonthlyBakDTO.getName());
            map.put("银行账号", null == employeeMonthlyBakDTO.getBankAccount() ? "" : employeeMonthlyBakDTO.getBankAccount());
            map.put("开户行名称", null == employeeMonthlyBakDTO.getBankName() ? "" : employeeMonthlyBakDTO.getBankName());
            map.put("部门", null == employeeMonthlyBakDTO.getDepartment() ? "" : employeeMonthlyBakDTO.getDepartment());
            map.put("科室", null == employeeMonthlyBakDTO.getAdministrativeOffice() ? "" : employeeMonthlyBakDTO.getAdministrativeOffice());
            map.put("岗位", null == employeeMonthlyBakDTO.getStation() ? "" : employeeMonthlyBakDTO.getStation());
            map.put("员工类别", null == employeeMonthlyBakDTO.getEmployeeCategory() ? "" : employeeMonthlyBakDTO.getEmployeeCategory());
            map.put("职级", null ==  employeeMonthlyBakDTO.getRank() ? "" : employeeMonthlyBakDTO.getRank());
            map.put("技术职级", null == employeeMonthlyBakDTO.getTechnicalRank() ? "" : employeeMonthlyBakDTO.getTechnicalRank());
            map.put("技能级别", null == employeeMonthlyBakDTO.getSkillLevel() ? "" : employeeMonthlyBakDTO.getSkillLevel());
            map.put("职类", null == employeeMonthlyBakDTO.getCategory() ? "" : employeeMonthlyBakDTO.getCategory());
            map.put("职种", null == employeeMonthlyBakDTO.getJob() ? "" : employeeMonthlyBakDTO.getJob());
            map.put("职位", null == employeeMonthlyBakDTO.getPosition() ? "" : employeeMonthlyBakDTO.getPosition());
            map.put("职称", null == employeeMonthlyBakDTO.getTitle() ? "" : employeeMonthlyBakDTO.getTitle());
            map.put("学历", null == employeeMonthlyBakDTO.getEducation() ? "" : employeeMonthlyBakDTO.getEducation());
            map.put("入职时间", null == employeeMonthlyBakDTO.getEntryTime() ? "" : DateUtil.localDateToStr(employeeMonthlyBakDTO.getEntryTime()));
            map.put("包干区分（1包干，0非包干）", null == employeeMonthlyBakDTO.getDivisionContractFlag() ? "" : employeeMonthlyBakDTO.getDivisionContractFlag() ? "包干" : "非包干");
            map.put("包干工资", null == employeeMonthlyBakDTO.getLumpSumWage() ? "" : employeeMonthlyBakDTO.getLumpSumWage());
            map.put("生产区分（1生产，0非生产）", null == employeeMonthlyBakDTO.getGenerationDifferentiationFlag() ? "" : employeeMonthlyBakDTO.getGenerationDifferentiationFlag() ? "生产" : "非生产");
            map.put("基本工资", null == employeeMonthlyBakDTO.getBasePay() ? "" :employeeMonthlyBakDTO.getBasePay());
            map.put("岗位技能工资", null == employeeMonthlyBakDTO.getPostSkillSalary() ? "" :employeeMonthlyBakDTO.getPostSkillSalary());
            map.put("目标绩效工资", null == employeeMonthlyBakDTO.getTargetPerformancePay() ? "" :employeeMonthlyBakDTO.getTargetPerformancePay());
            map.put("考核形式（月度考核、季度考核、无考核）", null == employeeMonthlyBakDTO.getAccessmentForm() ? "" :
                    employeeMonthlyBakDTO.getAccessmentForm().equals("month") ? "月度考核" :
                    employeeMonthlyBakDTO.getAccessmentForm().equals("quarter") ? "季度考核" : "无考核");
            map.put("成本中心名称", null == employeeMonthlyBakDTO.getCostCenter() ? "" :employeeMonthlyBakDTO.getCostCenter());
            map.put("成本中心号", null == employeeMonthlyBakDTO.getCostCenterNum() ? "" :employeeMonthlyBakDTO.getCostCenterNum());
            map.put("服务部门", null == employeeMonthlyBakDTO.getServiceDepartment() ? "" :employeeMonthlyBakDTO.getServiceDepartment());
            map.put("班长津贴", null == employeeMonthlyBakDTO.getSquadLeaderAllowance() ? "" :employeeMonthlyBakDTO.getSquadLeaderAllowance());
            map.put("岗位补贴", null == employeeMonthlyBakDTO.getPostSubsidy() ? "" :employeeMonthlyBakDTO.getPostSubsidy());
            map.put("工龄津贴", null == employeeMonthlyBakDTO.getSeniorityAllowance() ? "" :employeeMonthlyBakDTO.getSeniorityAllowance());
            map.put("一孩补贴", null == employeeMonthlyBakDTO.getOneChildSubsidy() ? "" :employeeMonthlyBakDTO.getOneChildSubsidy());
            map.put("高温补贴", null == employeeMonthlyBakDTO.getHighTemperatureSubsidy() ? "" :employeeMonthlyBakDTO.getHighTemperatureSubsidy());
            map.put("安全累积奖", null == employeeMonthlyBakDTO.getSafetyAccumulationAward() ? "" :employeeMonthlyBakDTO.getSafetyAccumulationAward());
            map.put("扣除保险(个人)", null == employeeMonthlyBakDTO.getPersonalDeductibles() ? "" :employeeMonthlyBakDTO.getPersonalDeductibles());
            map.put("扣除保险(公司)", null == employeeMonthlyBakDTO.getCompanyDeductibles() ? "" :employeeMonthlyBakDTO.getCompanyDeductibles());
            map.put("扣除公积金（个人）", null == employeeMonthlyBakDTO.getPersonalDeductAccumulationFund() ? "" :employeeMonthlyBakDTO.getPersonalDeductAccumulationFund());
            map.put("扣除公积金（公司）", null == employeeMonthlyBakDTO.getCompanyDeductAccumulationFund() ? "" :employeeMonthlyBakDTO.getCompanyDeductAccumulationFund());
            map.put("交通津贴", null == employeeMonthlyBakDTO.getTransportationAllowance() ? "" :employeeMonthlyBakDTO.getTransportationAllowance());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoInsertBySwmEmployeeMonthly(String period) {
        //  检查是否已经生成
        if (swmEmployeeMonthlyBakDao.checkBeforeMonthlyJob()) {
            swmEmployeeMonthlyBakDao.deleteMonthlyBak(period);
        }
        swmEmployeeMonthlyBakDao.autoInsertBySwmEmployeeMonthly(period);
    }
}
