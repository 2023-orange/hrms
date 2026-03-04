package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.swm.dao.SwmEmployeeMonthlyBakDao;
import com.sunten.hrms.swm.dao.SwmFloatingWageDao;
import com.sunten.hrms.swm.dao.SwmPostSkillSalaryDao;
import com.sunten.hrms.swm.domain.SwmWageSummaryFile;
import com.sunten.hrms.swm.dao.SwmWageSummaryFileDao;
import com.sunten.hrms.swm.service.SwmWageSummaryFileService;
import com.sunten.hrms.swm.dto.SwmWageSummaryFileDTO;
import com.sunten.hrms.swm.dto.SwmWageSummaryFileQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmWageSummaryFileMapper;
import com.sunten.hrms.swm.vo.CostCenterForSummaryVo;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.DecimalFormat;
import java.time.LocalDate;
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
 * 工资汇总归档表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-12-25
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmWageSummaryFileServiceImpl extends ServiceImpl<SwmWageSummaryFileDao, SwmWageSummaryFile> implements SwmWageSummaryFileService {
    private final SwmWageSummaryFileDao swmWageSummaryFileDao;
    private final SwmWageSummaryFileMapper swmWageSummaryFileMapper;
    private final SwmPostSkillSalaryDao swmPostSkillSalaryDao;
    private final SwmFloatingWageDao swmFloatingWageDao;
    private final SwmEmployeeMonthlyBakDao swmEmployeeMonthlyBakDao;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00#");

    public SwmWageSummaryFileServiceImpl(SwmWageSummaryFileDao swmWageSummaryFileDao, SwmWageSummaryFileMapper swmWageSummaryFileMapper,
                                         SwmPostSkillSalaryDao swmPostSkillSalaryDao, SwmFloatingWageDao swmFloatingWageDao,
                                         SwmEmployeeMonthlyBakDao swmEmployeeMonthlyBakDao) {
        this.swmWageSummaryFileDao = swmWageSummaryFileDao;
        this.swmWageSummaryFileMapper = swmWageSummaryFileMapper;
        this.swmPostSkillSalaryDao = swmPostSkillSalaryDao;
        this.swmFloatingWageDao = swmFloatingWageDao;
        this.swmEmployeeMonthlyBakDao = swmEmployeeMonthlyBakDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmWageSummaryFileDTO insert(SwmWageSummaryFile wageSummaryFileNew) {
        swmWageSummaryFileDao.insertAllColumn(wageSummaryFileNew);
        return swmWageSummaryFileMapper.toDto(wageSummaryFileNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmWageSummaryFile wageSummaryFile = new SwmWageSummaryFile();
        wageSummaryFile.setId(id);
        this.delete(wageSummaryFile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmWageSummaryFile wageSummaryFile) {
        // TODO    确认删除前是否需要做检查
        swmWageSummaryFileDao.deleteByEntityKey(wageSummaryFile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmWageSummaryFile wageSummaryFileNew) {
        SwmWageSummaryFile wageSummaryFileInDb = Optional.ofNullable(swmWageSummaryFileDao.getByKey(wageSummaryFileNew.getId())).orElseGet(SwmWageSummaryFile::new);
        ValidationUtil.isNull(wageSummaryFileInDb.getId() ,"WageSummaryFile", "id", wageSummaryFileNew.getId());
        wageSummaryFileNew.setId(wageSummaryFileInDb.getId());
        swmWageSummaryFileDao.updateAllColumnByKey(wageSummaryFileNew);
    }

    @Override
    public SwmWageSummaryFileDTO getByKey(Long id) {
        SwmWageSummaryFile wageSummaryFile = Optional.ofNullable(swmWageSummaryFileDao.getByKey(id)).orElseGet(SwmWageSummaryFile::new);
        ValidationUtil.isNull(wageSummaryFile.getId() ,"WageSummaryFile", "id", id);
        return swmWageSummaryFileMapper.toDto(wageSummaryFile);
    }

    @Override
    public List<SwmWageSummaryFileDTO> listAll(SwmWageSummaryFileQueryCriteria criteria) {
        return swmWageSummaryFileMapper.toDto(swmWageSummaryFileDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmWageSummaryFileQueryCriteria criteria, Pageable pageable) {
        Page<SwmWageSummaryFile> page = PageUtil.startPage(pageable);
        List<SwmWageSummaryFile> wageSummaryFiles = swmWageSummaryFileDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmWageSummaryFileMapper.toDto(wageSummaryFiles), page.getTotal());
    }

    @Override
    public void download(List<SwmWageSummaryFileDTO> wageSummaryFileDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmWageSummaryFileDTO wageSummaryFileDTO : wageSummaryFileDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("所得期间", wageSummaryFileDTO.getIncomePeriod());
            map.put("工牌号", wageSummaryFileDTO.getWorkCard());
            map.put("姓名", wageSummaryFileDTO.getName());
            map.put("包干区分", null != wageSummaryFileDTO.getDivisionContractFlag() ? (wageSummaryFileDTO.getDivisionContractFlag() ? "包干" : "非包干") : "");
            map.put("部门", null != wageSummaryFileDTO.getDepartment() ? wageSummaryFileDTO.getDepartment() : "");
            map.put("科室",  null != wageSummaryFileDTO.getAdministrativeOffice() ? wageSummaryFileDTO.getAdministrativeOffice() : "");
            map.put("班组", null != wageSummaryFileDTO.getTeam() ? wageSummaryFileDTO.getTeam() : "");
            map.put("岗位", null != wageSummaryFileDTO.getStation() ? wageSummaryFileDTO.getStation() : "");
            map.put("银行账号", wageSummaryFileDTO.getBankAccount());
            map.put("开户行名称", null != wageSummaryFileDTO.getBankName() ? wageSummaryFileDTO.getBankName() : "");
//            map.put("补贴_一孩", null != wageSummaryFileDTO.getOneChildSubsidy() ? decimalFormat.format(wageSummaryFileDTO.getOneChildSubsidy()) : "0");
//            map.put("安全累积奖", null != wageSummaryFileDTO.getSafetyAccumulationAward() ? decimalFormat.format(wageSummaryFileDTO.getSafetyAccumulationAward()) : "0");
//            map.put("工龄津贴", null != wageSummaryFileDTO.getSeniorityAllowance() ? decimalFormat.format(wageSummaryFileDTO.getSeniorityAllowance()) : "0");
//            map.put("高温津贴", null != wageSummaryFileDTO.getHighTemperatureSubsidy() ? decimalFormat.format(wageSummaryFileDTO.getHighTemperatureSubsidy()) : "0");
//            map.put("搬迁交通津贴", null != wageSummaryFileDTO.getTransportationAllowance() ? decimalFormat.format(wageSummaryFileDTO.getTransportationAllowance()) : "0");
//            map.put("岗位补贴", null != wageSummaryFileDTO.getPostSubsidy() ? decimalFormat.format(wageSummaryFileDTO.getPostSubsidy()) : "0");
//            map.put("加班工资", null != wageSummaryFileDTO.getOvertimePay() ? decimalFormat.format(wageSummaryFileDTO.getOvertimePay()) : "0");
//            map.put("补贴扣除", null != wageSummaryFileDTO.getAllowanceDeduction() ? decimalFormat.format(wageSummaryFileDTO.getAllowanceDeduction()) : "0");
//            map.put("应发工资_固定", null != wageSummaryFileDTO.getWagesPayablePost() ? decimalFormat.format(wageSummaryFileDTO.getWagesPayablePost()) : "0");
//            map.put("应发工资_浮动", null != wageSummaryFileDTO.getWagesPayableFloat() ? decimalFormat.format(wageSummaryFileDTO.getWagesPayableFloat()) : "0");
            map.put("补贴_一孩", null != wageSummaryFileDTO.getOneChildSubsidy() ? wageSummaryFileDTO.getOneChildSubsidy() : 0);
            map.put("安全累积奖", null != wageSummaryFileDTO.getSafetyAccumulationAward() ? wageSummaryFileDTO.getSafetyAccumulationAward() : 0);
            map.put("工龄津贴", null != wageSummaryFileDTO.getSeniorityAllowance() ? wageSummaryFileDTO.getSeniorityAllowance() : 0);
            map.put("高温津贴", null != wageSummaryFileDTO.getHighTemperatureSubsidy() ? wageSummaryFileDTO.getHighTemperatureSubsidy() : 0);
            map.put("搬迁交通津贴", null != wageSummaryFileDTO.getTransportationAllowance() ? wageSummaryFileDTO.getTransportationAllowance() : 0);
            map.put("岗位补贴", null != wageSummaryFileDTO.getPostSubsidy() ? wageSummaryFileDTO.getPostSubsidy() : 0);
            map.put("加班工资", null != wageSummaryFileDTO.getOvertimePay() ? wageSummaryFileDTO.getOvertimePay() : 0);
            map.put("补贴扣除", null != wageSummaryFileDTO.getAllowanceDeduction() ? wageSummaryFileDTO.getAllowanceDeduction() : 0);
            map.put("应发工资_固定", null != wageSummaryFileDTO.getWagesPayablePost() ? wageSummaryFileDTO.getWagesPayablePost() : 0);
            map.put("应发工资_浮动", null != wageSummaryFileDTO.getWagesPayableFloat() ? wageSummaryFileDTO.getWagesPayableFloat() : 0);

            if (null != wageSummaryFileDTO.getWagesPayableFloat() && null != wageSummaryFileDTO.getWagesPayablePost()) {
//                map.put("应发工资_合计", decimalFormat.format(wageSummaryFileDTO.getNetPaymentFloat().add(wageSummaryFileDTO.getWagesPayablePost())));
                map.put("应发工资_合计", wageSummaryFileDTO.getWagesPayableFloat().add(wageSummaryFileDTO.getWagesPayablePost()));
            } else {
                if (null != wageSummaryFileDTO.getWagesPayableFloat()) {
//                    map.put("应发工资_合计", decimalFormat.format(wageSummaryFileDTO.getWagesPayableFloat()));
                    map.put("应发工资_合计", wageSummaryFileDTO.getWagesPayableFloat());
                } else {
                    if (null == wageSummaryFileDTO.getWagesPayablePost()) {
                        map.put("应发工资_合计", 0);
                    } else {
//                        map.put("应发工资_合计", decimalFormat.format(wageSummaryFileDTO.getWagesPayablePost()));
                        map.put("应发工资_合计", wageSummaryFileDTO.getWagesPayablePost());
                    }
                }
            }
//            map.put("扣除_医药费", null != wageSummaryFileDTO.getDeductMedicalCosts() ? decimalFormat.format(wageSummaryFileDTO.getDeductMedicalCosts()) : "0");
//            map.put("扣除_水电房", null != wageSummaryFileDTO.getDeductHydropowerHouse() ? decimalFormat.format(wageSummaryFileDTO.getDeductHydropowerHouse()) : "0");
//            map.put("扣除所得税", null != wageSummaryFileDTO.getDeductIncomeTax() ? decimalFormat.format(wageSummaryFileDTO.getDeductIncomeTax()) : "0");
//            map.put("扣除保险_个人", null != wageSummaryFileDTO.getPersonalDeductibles() ? decimalFormat.format(wageSummaryFileDTO.getPersonalDeductibles()) : "0");
//            map.put("扣除保险_公司", null != wageSummaryFileDTO.getCompanyDeductibles() ? decimalFormat.format(wageSummaryFileDTO.getCompanyDeductibles()) : "0");
//            map.put("扣除公积金_个人", null != wageSummaryFileDTO.getPersonalDeductAccumulationFund() ? decimalFormat.format(wageSummaryFileDTO.getPersonalDeductAccumulationFund()) : "0");
//            map.put("扣除公积金_公司", null != wageSummaryFileDTO.getCompanyDeductAccumulationFund() ? decimalFormat.format(wageSummaryFileDTO.getCompanyDeductAccumulationFund()) : "0");
//            map.put("扣除_其它", null != wageSummaryFileDTO.getDeductOther() ? decimalFormat.format(wageSummaryFileDTO.getDeductOther()) : "0");
//            map.put("扣除金额", null != wageSummaryFileDTO.getDeductionAmount() ? decimalFormat.format(wageSummaryFileDTO.getDeductionAmount()) : "0");
//            map.put("实发工资_固定", null != wageSummaryFileDTO.getNetPaymentPost() ? decimalFormat.format(wageSummaryFileDTO.getNetPaymentPost()) : "0");
//            map.put("实发工资_浮动", null != wageSummaryFileDTO.getNetPaymentFloat() ? decimalFormat.format(wageSummaryFileDTO.getNetPaymentFloat()) : "0");
            map.put("扣除_医药费", null != wageSummaryFileDTO.getDeductMedicalCosts() ? wageSummaryFileDTO.getDeductMedicalCosts() : 0);
            map.put("扣除_水电房", null != wageSummaryFileDTO.getDeductHydropowerHouse() ? wageSummaryFileDTO.getDeductHydropowerHouse() : 0);
            map.put("扣除所得税", null != wageSummaryFileDTO.getDeductIncomeTax() ? wageSummaryFileDTO.getDeductIncomeTax() : 0);
            map.put("扣除保险_个人", null != wageSummaryFileDTO.getPersonalDeductibles() ? wageSummaryFileDTO.getPersonalDeductibles() : 0);
            map.put("扣除保险_公司", null != wageSummaryFileDTO.getCompanyDeductibles() ? wageSummaryFileDTO.getCompanyDeductibles() : 0);
            map.put("扣除公积金_个人", null != wageSummaryFileDTO.getPersonalDeductAccumulationFund() ? wageSummaryFileDTO.getPersonalDeductAccumulationFund() : 0);
            map.put("扣除公积金_公司", null != wageSummaryFileDTO.getCompanyDeductAccumulationFund() ? wageSummaryFileDTO.getCompanyDeductAccumulationFund() : 0);
            map.put("扣除_其它", null != wageSummaryFileDTO.getDeductOther() ? wageSummaryFileDTO.getDeductOther() : 0);
            map.put("扣除金额", null != wageSummaryFileDTO.getDeductionAmount() ? wageSummaryFileDTO.getDeductionAmount() : 0);
            map.put("实发工资_固定", null != wageSummaryFileDTO.getNetPaymentPost() ? wageSummaryFileDTO.getNetPaymentPost() : 0);
            map.put("实发工资_浮动", null != wageSummaryFileDTO.getNetPaymentFloat() ? wageSummaryFileDTO.getNetPaymentFloat() : 0);
            if (null != wageSummaryFileDTO.getNetPaymentPost() && null != wageSummaryFileDTO.getNetPaymentFloat()) {
//                map.put("实发工资_合计", decimalFormat.format(wageSummaryFileDTO.getNetPaymentFloat().add(wageSummaryFileDTO.getNetPaymentPost())));
                map.put("实发工资_合计", wageSummaryFileDTO.getNetPaymentFloat().add(wageSummaryFileDTO.getNetPaymentPost()));
            } else {
                if (null != wageSummaryFileDTO.getNetPaymentPost()) {
//                    map.put("实发工资_合计", decimalFormat.format(wageSummaryFileDTO.getNetPaymentPost()));
                    map.put("实发工资_合计", wageSummaryFileDTO.getNetPaymentPost());
                } else {
                    if (null == wageSummaryFileDTO.getNetPaymentFloat()) {
                        map.put("实发工资_合计", 0);
                    } else {
                        map.put("实发工资_合计", wageSummaryFileDTO.getNetPaymentFloat());
                    }
                }
            }
            map.put("成本中心编号", null != wageSummaryFileDTO.getCostCenterNum() ? wageSummaryFileDTO.getCostCenterNum() : "");
            map.put("成本中心名称", null != wageSummaryFileDTO.getCostCenter() ? wageSummaryFileDTO.getCostCenter() : "");
            map.put("服务部门", null != wageSummaryFileDTO.getServiceDepartment() ? wageSummaryFileDTO.getServiceDepartment() : "");
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoInsertFile() { // 自动冻结并归档
        LocalDate now = LocalDate.now();
        String period;
        if (now.getMonthValue() == 1) {
            period = (now.getYear() - 1) + ".12";
        } else {
            period = now.getYear() + "." + ((now.getMonthValue() - 1) < 10 ?"0" + (now.getMonthValue() - 1) : (now.getMonthValue() - 1) + "");
        }
        swmPostSkillSalaryDao.updateFlozenFlag(period);
        swmFloatingWageDao.updateFlozenFlag(period);
        swmWageSummaryFileDao.wageSummaryToFile(period);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    // 冻结
    public void frozenSalary(String type, Long limit, String period) {
        if (type.equals("post")) {
            // 冻结固定， 检测浮动是否冻结，是则归档
            swmPostSkillSalaryDao.updateFlozenFlagByLimit(limit);
            if (swmFloatingWageDao.checkFrozenFlagByPeriod(period) > 0) {
                swmEmployeeMonthlyBakDao.autoInsertBySwmEmployeeMonthly(period);
                swmWageSummaryFileDao.wageSummaryToFile(period);
            }
        } else {
            // 冻结浮动， 检测固定是否冻结，是则归档
            swmFloatingWageDao.updateFlozenFlagByLimit(limit);
            if (swmPostSkillSalaryDao.checkFrozenFlagByPeriod(period) > 0) {
                swmEmployeeMonthlyBakDao.autoInsertBySwmEmployeeMonthly(period);
                swmWageSummaryFileDao.wageSummaryToFile(period);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    // 发放
    public void grantSalary(String type, Long limit, String period) {
        if (type.equals("post")) {
            // 发放固定， 检测浮动是否冻结，是则归档
            swmPostSkillSalaryDao.updateGrantFlagByLimit(limit);
            if (swmFloatingWageDao.checkFrozenFlagByPeriod(period) > 0) {
                swmWageSummaryFileDao.wageSummaryToFile(period);
            }
        } else {
            // 发放浮动， 检测固定是否冻结，是则归档
            swmFloatingWageDao.updateGrantFlagByLimit(limit);
            if (swmPostSkillSalaryDao.checkFrozenFlagByPeriod(period) > 0) {
                swmWageSummaryFileDao.wageSummaryToFile(period);
            }
        }
    }

    @Override
    // 检测该月工资是否已经冻结
    public Boolean checkFrozenFlagByPeriod(String period) {
        if (swmFloatingWageDao.checkFrozenFlagByPeriod(period) > 0)
        {
            if (swmPostSkillSalaryDao.checkFrozenFlagByPeriod(period) > 0)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void exportForTaxCalculation(List<SwmWageSummaryFileDTO> wageSummaryFileDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmWageSummaryFileDTO wageSummaryFileDTO : wageSummaryFileDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("工牌号", wageSummaryFileDTO.getWorkCard());
            map.put("姓名", wageSummaryFileDTO.getName());
            map.put("身份证号", wageSummaryFileDTO.getIdNumber());
            map.put("应发工资_合计", wageSummaryFileDTO.getWagesPayable());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void exportForCostCenter(HttpServletResponse response, SwmWageSummaryFileQueryCriteria criteria) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        List<CostCenterForSummaryVo> costCenterForSummaryVos = swmWageSummaryFileDao.listByCostCenterForSummary(criteria);
        for (CostCenterForSummaryVo costCenterForSummaryVo : costCenterForSummaryVos
             ) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("所得期间", costCenterForSummaryVo.getIncomePeriod());
            map.put("成本中心编号", costCenterForSummaryVo.getCostCenterNum().equals("orderForSum") ? "" : costCenterForSummaryVo.getCostCenterNum());
            map.put("成本中心名称", costCenterForSummaryVo.getCostCenter());
            map.put("服务部门", costCenterForSummaryVo.getServiceDepartment());
            map.put("计数项:姓名", costCenterForSummaryVo.getCountName());
//            map.put("求和项:补贴_一孩", decimalFormat.format(null != costCenterForSummaryVo.getCountOneChildSubsidy() ? costCenterForSummaryVo.getCountOneChildSubsidy() : 0));
            map.put("求和项:补贴_一孩", null != costCenterForSummaryVo.getCountOneChildSubsidy() ? costCenterForSummaryVo.getCountOneChildSubsidy() : 0);
//            map.put("求和项:安全累积奖", decimalFormat.format(null != costCenterForSummaryVo.getCountSafetyAccumulationAward() ? costCenterForSummaryVo.getCountSafetyAccumulationAward() : 0));
            map.put("求和项:安全累积奖", null != costCenterForSummaryVo.getCountSafetyAccumulationAward() ? costCenterForSummaryVo.getCountSafetyAccumulationAward() : 0);
//            map.put("求和项:工龄津贴", decimalFormat.format(null != costCenterForSummaryVo.getCountSeniorityAllowance() ? costCenterForSummaryVo.getCountSeniorityAllowance() : 0));
            map.put("求和项:工龄津贴", null != costCenterForSummaryVo.getCountSeniorityAllowance() ? costCenterForSummaryVo.getCountSeniorityAllowance() : 0);
//            map.put("求和项:高温津贴", decimalFormat.format(null != costCenterForSummaryVo.getCountHighTemperatureSubsidy() ? costCenterForSummaryVo.getCountHighTemperatureSubsidy() : 0));
            map.put("求和项:高温津贴", null != costCenterForSummaryVo.getCountHighTemperatureSubsidy() ? costCenterForSummaryVo.getCountHighTemperatureSubsidy() : 0);
//            map.put("求和项:搬迁交通津贴", decimalFormat.format(null != costCenterForSummaryVo.getCountTransportationAllowance() ? costCenterForSummaryVo.getCountTransportationAllowance() : 0));
            map.put("求和项:搬迁交通津贴", null != costCenterForSummaryVo.getCountTransportationAllowance() ? costCenterForSummaryVo.getCountTransportationAllowance() : 0);
//            map.put("求和项:岗位补贴", decimalFormat.format(null != costCenterForSummaryVo.getCountPostSubsidy() ? costCenterForSummaryVo.getCountPostSubsidy() : 0));
            map.put("求和项:岗位补贴", null != costCenterForSummaryVo.getCountPostSubsidy() ? costCenterForSummaryVo.getCountPostSubsidy() : 0);
//            map.put("求和项:加班工资", decimalFormat.format(null != costCenterForSummaryVo.getCountOvertimePay() ? costCenterForSummaryVo.getCountOvertimePay() : 0));
            map.put("求和项:加班工资", null != costCenterForSummaryVo.getCountOvertimePay() ? costCenterForSummaryVo.getCountOvertimePay() : 0);
//            map.put("求和项:补贴扣除", decimalFormat.format(null != costCenterForSummaryVo.getCountAllowanceDeduction() ? costCenterForSummaryVo.getCountAllowanceDeduction() : 0));
            map.put("求和项:补贴扣除", null != costCenterForSummaryVo.getCountAllowanceDeduction() ? costCenterForSummaryVo.getCountAllowanceDeduction() : 0);
//            map.put("求和项:应发工资_合计", decimalFormat.format(null != costCenterForSummaryVo.getCountWagesPayable() ? costCenterForSummaryVo.getCountWagesPayable() : 0));
            map.put("求和项:应发工资_合计", null != costCenterForSummaryVo.getCountWagesPayable() ? costCenterForSummaryVo.getCountWagesPayable() : 0);
//            map.put("求和项:扣除_医药费", decimalFormat.format(null != costCenterForSummaryVo.getCountDeductMedicalCosts() ? costCenterForSummaryVo.getCountDeductMedicalCosts() : 0));
            map.put("求和项:扣除_医药费", null != costCenterForSummaryVo.getCountDeductMedicalCosts() ? costCenterForSummaryVo.getCountDeductMedicalCosts() : 0);
//            map.put("求和项:扣除_水电房", decimalFormat.format(null != costCenterForSummaryVo.getCountDeductHydropowerHouse() ? costCenterForSummaryVo.getCountDeductHydropowerHouse() : 0));
            map.put("求和项:扣除_水电房", null != costCenterForSummaryVo.getCountDeductHydropowerHouse() ? costCenterForSummaryVo.getCountDeductHydropowerHouse() : 0);
//            map.put("求和项:扣除_所得税_固定", decimalFormat.format(null != costCenterForSummaryVo.getCountDeductIncomeTax() ? costCenterForSummaryVo.getCountDeductIncomeTax() : 0));
            map.put("求和项:扣除_所得税_固定", null != costCenterForSummaryVo.getCountDeductIncomeTax() ? costCenterForSummaryVo.getCountDeductIncomeTax() : 0);
//            map.put("求和项:扣除_保险个人", decimalFormat.format(null != costCenterForSummaryVo.getCountPersonalDeductibles() ? costCenterForSummaryVo.getCountPersonalDeductibles() : 0));
            map.put("求和项:扣除_保险个人", null != costCenterForSummaryVo.getCountPersonalDeductibles() ? costCenterForSummaryVo.getCountPersonalDeductibles() : 0);
//            map.put("求和项:扣除公积金_个人", decimalFormat.format(null != costCenterForSummaryVo.getCountPersonalDeductAccumulationFund() ? costCenterForSummaryVo.getCountPersonalDeductAccumulationFund() : 0));
            map.put("求和项:扣除公积金_个人", null != costCenterForSummaryVo.getCountPersonalDeductAccumulationFund() ? costCenterForSummaryVo.getCountPersonalDeductAccumulationFund() : 0);
//            map.put("求和项:扣除_其它", decimalFormat.format(null != costCenterForSummaryVo.getCountDeductOther() ? costCenterForSummaryVo.getCountDeductOther() : 0));
            map.put("求和项:扣除_其它", null != costCenterForSummaryVo.getCountDeductOther() ? costCenterForSummaryVo.getCountDeductOther() : 0);
//            map.put("求和项:实发工资_汇总", decimalFormat.format(null != costCenterForSummaryVo.getCountNetPayment() ? costCenterForSummaryVo.getCountNetPayment() : 0));
            map.put("求和项:实发工资_汇总",null != costCenterForSummaryVo.getCountNetPayment() ? costCenterForSummaryVo.getCountNetPayment() : 0);
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<SwmWageSummaryFileDTO> getGrantDetail(Long employeeId, String year) {
        return swmWageSummaryFileMapper.toDto(swmWageSummaryFileDao.getGrantDetail(employeeId, year));
    }

    @Override
    public List<SwmWageSummaryFileDTO> listStatistics(String workCard) {
        return swmWageSummaryFileMapper.toDto(swmWageSummaryFileDao.listStatistics(workCard));
    }




}
