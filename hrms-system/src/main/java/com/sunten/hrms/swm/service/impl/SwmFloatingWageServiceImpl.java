package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.domain.SwmFloatingWage;
import com.sunten.hrms.swm.dao.SwmFloatingWageDao;
import com.sunten.hrms.swm.dto.SwmFloatingWageSpecialDTO;
import com.sunten.hrms.swm.mapper.SwmFloatingWageSpecialMapper;
import com.sunten.hrms.swm.service.SwmFloatingWageService;
import com.sunten.hrms.swm.dto.SwmFloatingWageDTO;
import com.sunten.hrms.swm.dto.SwmFloatingWageQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmFloatingWageMapper;
import com.sunten.hrms.swm.service.SwmMonthlyQuarterlyAssessmentService;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.format.DateTimeFormatter;
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
 * 浮动工资表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmFloatingWageServiceImpl extends ServiceImpl<SwmFloatingWageDao, SwmFloatingWage> implements SwmFloatingWageService {
    private final SwmFloatingWageDao swmFloatingWageDao;
    private final SwmFloatingWageMapper swmFloatingWageMapper;
    private final SwmFloatingWageSpecialMapper swmFloatingWageSpecialMapper;
    private final FndUserService fndUserService;
    private final SwmPostSkillSalaryServiceImpl swmPostSkillSalaryServiceImpl;
    private final SwmMonthlyQuarterlyAssessmentService swmMonthlyQuarterlyAssessmentService;

    public SwmFloatingWageServiceImpl(SwmFloatingWageDao swmFloatingWageDao, SwmFloatingWageMapper swmFloatingWageMapper, SwmFloatingWageSpecialMapper swmFloatingWageSpecialMapper,
                                      FndUserService fndUserService, SwmPostSkillSalaryServiceImpl swmPostSkillSalaryServiceImpl, SwmMonthlyQuarterlyAssessmentService swmMonthlyQuarterlyAssessmentService) {
        this.swmFloatingWageDao = swmFloatingWageDao;
        this.swmFloatingWageMapper = swmFloatingWageMapper;
        this.fndUserService = fndUserService;
        this.swmPostSkillSalaryServiceImpl = swmPostSkillSalaryServiceImpl;
        this.swmFloatingWageSpecialMapper = swmFloatingWageSpecialMapper;
        this.swmMonthlyQuarterlyAssessmentService = swmMonthlyQuarterlyAssessmentService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmFloatingWageDTO insert(SwmFloatingWage floatingWageNew) {
        swmFloatingWageDao.insertAllColumn(floatingWageNew);
        return swmFloatingWageMapper.toDto(floatingWageNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmFloatingWage floatingWage = new SwmFloatingWage();
        floatingWage.setId(id);
        this.delete(floatingWage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmFloatingWage floatingWage) {
        // TODO    确认删除前是否需要做检查
        swmFloatingWageDao.deleteByEntityKey(floatingWage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmFloatingWage floatingWageNew) {
        SwmFloatingWage floatingWageInDb = Optional.ofNullable(swmFloatingWageDao.getByKey(floatingWageNew.getId())).orElseGet(SwmFloatingWage::new);
        ValidationUtil.isNull(floatingWageInDb.getId() ,"FloatingWage", "id", floatingWageNew.getId());
        floatingWageNew.setId(floatingWageInDb.getId());
        // 更新月绩效工资、 应发工资、 实发工资
        setFloatingWage(floatingWageNew);
        swmFloatingWageDao.updateAllColumnByKey(floatingWageNew);
//        SwmFloatingWageQueryCriteria swmFloatingWageQueryCriteria = new SwmFloatingWageQueryCriteria();
//        swmFloatingWageQueryCriteria.setPeriod(floatingWageNew.getIncomePeriod());
//        swmFloatingWageQueryCriteria.setId(floatingWageNew.getId());
//        swmFloatingWageDao.updateFloatWageAfterEdit(swmFloatingWageQueryCriteria);
    }

    @Override
    public SwmFloatingWageDTO getByKey(Long id) {
        SwmFloatingWage floatingWage = Optional.ofNullable(swmFloatingWageDao.getByKey(id)).orElseGet(SwmFloatingWage::new);
        ValidationUtil.isNull(floatingWage.getId() ,"FloatingWage", "id", id);
        return swmFloatingWageMapper.toDto(floatingWage);
    }

    @Override
    public List<SwmFloatingWageDTO> listAll(SwmFloatingWageQueryCriteria criteria) {
        return swmFloatingWageMapper.toDto(swmFloatingWageDao.listAllByCriteria(criteria));
    }

    @Override
    public List<SwmFloatingWageSpecialDTO> listSpecialAll(SwmFloatingWageQueryCriteria criteria) {
        return swmFloatingWageSpecialMapper.toDto(swmFloatingWageDao.listForSecondListByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmFloatingWageQueryCriteria criteria, Pageable pageable) {
        Page<SwmFloatingWage> page = PageUtil.startPage(pageable);
        List<SwmFloatingWage> floatingWages = swmFloatingWageDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmFloatingWageMapper.toDto(floatingWages), page.getTotal());
    }

    @Override
    public void download(List<SwmFloatingWageDTO> floatingWageDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (SwmFloatingWageDTO floatingWageDTO : floatingWageDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("所得期间", floatingWageDTO.getIncomePeriod());
            map.put("工牌号", floatingWageDTO.getWorkCard());
            map.put("姓名", floatingWageDTO.getEmployeeName());
            map.put("部门", floatingWageDTO.getDepartment());
            map.put("科室", floatingWageDTO.getAdministrativeOffice());
            map.put("员工类别", floatingWageDTO.getEmployeeCategory());
            map.put("包干区分", floatingWageDTO.getDivisionContractFlag() ? "包干" : "非包干");
            map.put("生产区分", floatingWageDTO.getGenerationDifferentiationFlag() ? "生产" : "非生产");
            map.put("分配方式", floatingWageDTO.getDistributionMethod());
            map.put("考核形式", ("quarter").equals(floatingWageDTO.getAccessmentForm()) ? "季度考核" : (("month").equals(floatingWageDTO.getAccessmentForm()) ? "月度考核":"无考核"));
            map.put("银行账号", floatingWageDTO.getBankAccount());
            map.put("开户行名称", floatingWageDTO.getBankName());
            map.put("提交标识 ", floatingWageDTO.getCommitFlag() ? "已提交" : "未提交");
            map.put("目标绩效工资", floatingWageDTO.getTargetPerformancePay());
            map.put("生产系数", floatingWageDTO.getProductionFactor());
            map.put("质量系数", floatingWageDTO.getQualityFactor());
            map.put("考核系数", floatingWageDTO.getAssessmentCoefficient());
            map.put("月绩效工资", floatingWageDTO.getMonthlyPerformanceSalary());
            map.put("调配绩效工资", floatingWageDTO.getAllocatePerformancePay());
            map.put("税前奖励扣发", floatingWageDTO.getPreTaxWithheld());
            map.put("应发工资", floatingWageDTO.getWagesPayable());
            map.put("税后奖励扣发", floatingWageDTO.getAfterTaxWithheld());
            map.put("实发工资", floatingWageDTO.getNetPayment());
            map.put("最后修改人", floatingWageDTO.getUpdateByStr());
            map.put("修改时间", floatingWageDTO.getUpdateTime().format(fmt));
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void downloadSecondList(List<SwmFloatingWageSpecialDTO> floatingWageDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (SwmFloatingWageSpecialDTO floatingWageDTO : floatingWageDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("所得期间", floatingWageDTO.getIncomePeriod());
            map.put("工牌号", floatingWageDTO.getWorkCard());
            map.put("姓名", floatingWageDTO.getEmployeeName());
            map.put("部门", floatingWageDTO.getDepartment());
            map.put("科室", null != floatingWageDTO.getAdministrativeOffice() ? floatingWageDTO.getAdministrativeOffice() : "");
            map.put("目标绩效工资", floatingWageDTO.getTargetPerformancePay());
            map.put("生产系数", floatingWageDTO.getProductionFactor());
            map.put("质量系数", floatingWageDTO.getQualityFactor());
            map.put("考核系数", floatingWageDTO.getAssessmentCoefficient());
            map.put("月绩效工资", floatingWageDTO.getMonthlyPerformanceSalary());
            map.put("调配绩效工资", floatingWageDTO.getAllocatePerformancePay());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SwmFloatingWageDTO> generateFloatingWageByMsp(String period) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Map<String, Object> map = new HashMap<>();
        map.put("period", period);
        map.put("userId", user.getId());
        map.put("resultStr", "");
        swmFloatingWageDao.generateFloatingWageByMsp(map);
        String result = map.get("resultStr").toString();
        if (!result.equals("SUCCESS")) {
            throw new InfoCheckWarningMessException(result);
        } else {
            SwmFloatingWageQueryCriteria swmFloatingWageQueryCriteria = new SwmFloatingWageQueryCriteria();
            swmFloatingWageQueryCriteria.setPeriod(period);
            return swmFloatingWageMapper.toDto(swmFloatingWageDao.listAllByCriteria(swmFloatingWageQueryCriteria));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByPeriod(String period) {
        SwmFloatingWageQueryCriteria swmFloatingWageQueryCriteria = new SwmFloatingWageQueryCriteria();
        swmFloatingWageQueryCriteria.setPeriod(period);
        if (swmFloatingWageDao.countByQuery(swmFloatingWageQueryCriteria) > 0) {
            swmFloatingWageQueryCriteria.setFrozenFlag(true);
            if (swmFloatingWageDao.countByQuery(swmFloatingWageQueryCriteria) > 0) {
                throw new InfoCheckWarningMessException("该所得期间的浮动工资已被冻结,不允许删除");
            } else {
                SwmFloatingWage swmFloatingWage = new SwmFloatingWage();
                swmFloatingWage.setIncomePeriod(period);
                swmFloatingWageDao.removeByPeriod(swmFloatingWage);
                // 删除浮动后还需要删除没有被冻结且在浮动生成的存储过程中，根据季度生成的月度
                swmMonthlyQuarterlyAssessmentService.removeQuarterlyByPeriod(period);
            }
        } else {
            throw new InfoCheckWarningMessException("该所得期间尚未生成浮动工资");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> generatePeriodList() {
        String topPeriod = swmFloatingWageDao.getTopPeriod();
        return swmPostSkillSalaryServiceImpl.getPeriodList(topPeriod);
    }

    @Override
    public String getFloatNewestPeriod() {
        return swmFloatingWageDao.getFloatNewestPeriod();
    }



    public Boolean checkBeforeDelete(String incomePeriod){
        SwmFloatingWageQueryCriteria swmFloatingWageQueryCriteria = new SwmFloatingWageQueryCriteria();
        swmFloatingWageQueryCriteria.setPeriod(incomePeriod);
        swmFloatingWageQueryCriteria.setFrozenFlag(true);
        Integer count = swmFloatingWageDao.countByQuery(swmFloatingWageQueryCriteria);
        return count <= 0;
    }

    private void setFloatingWage(SwmFloatingWage swmFloatingWage) {
        // 月绩效工资(实际绩效工资) = 目标绩效工资 * 生产系数 * 质量系数 * 考核系数
        // 应发工资 =  调配绩效工资 + 税前奖励扣发
        // 实发工资 =  调配绩效工资 + 税前奖励扣发 + 税后奖励扣发
        swmFloatingWage.setMonthlyPerformanceSalary(
                swmFloatingWage.getTargetPerformancePay().multiply( swmFloatingWage.getProductionFactor())
                .multiply(swmFloatingWage.getQualityFactor()).multiply(swmFloatingWage.getAssessmentCoefficient())
        );
        this.setPayAndNet(swmFloatingWage);
    }

    private void setPayAndNet(SwmFloatingWage swmFloatingWage) {
        // 应发工资 =  调配绩效工资 + 税前奖励扣发
        // 实发工资 =  调配绩效工资 + 税前奖励扣发 + 税后奖励扣发
        swmFloatingWage.setWagesPayable(
                swmFloatingWage.getAllocatePerformancePay().add(swmFloatingWage.getPreTaxWithheld())
        );
        swmFloatingWage.setNetPayment(
                swmFloatingWage.getAllocatePerformancePay().add(swmFloatingWage.getPreTaxWithheld())
                        .add(swmFloatingWage.getAfterTaxWithheld())
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateFloatingWage(List<SwmFloatingWage> swmFloatingWages) {
        for (SwmFloatingWage sfw : swmFloatingWages
             ) {
            setFloatingWage(sfw);
            swmFloatingWageDao.updateAllColumnByKey(sfw);
        }
    }

    @Override
    public Boolean checkFloatingWageBeforAutoUpdate(String incomePeriod) {
        return swmFloatingWageDao.checkFloatingWageBeforAutoUpdate(incomePeriod);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateAllocatePerformancePay(List<SwmFloatingWage> swmFloatingWages) {
        for (SwmFloatingWage sfw : swmFloatingWages
             ) {
            swmFloatingWageDao.updateAllocatePerformancePay(sfw);
        }
    }

    @Override
    public Boolean checkFloatingWageIsExist(String incomePeriod) {
        return swmFloatingWageDao.checkFloatingWageIsExist(incomePeriod);
    }

    @Override
    public int checkFrozenFlagByPeriod(String period) {
        return swmFloatingWageDao.checkFrozenFlagByPeriod(period);
    }
}
