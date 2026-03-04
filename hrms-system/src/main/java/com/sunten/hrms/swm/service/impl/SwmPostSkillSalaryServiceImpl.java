package com.sunten.hrms.swm.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.sunten.hrms.ac.service.impl.AcEmployeeAttendanceServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndDictDetailDao;
import com.sunten.hrms.fnd.dao.FndUserDao;
import com.sunten.hrms.fnd.domain.FndDictDetail;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndDictDetailService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.dao.SwmFloatingWageDao;
import com.sunten.hrms.swm.domain.SwmPostSkillSalary;
import com.sunten.hrms.swm.dao.SwmPostSkillSalaryDao;
import com.sunten.hrms.swm.domain.SwmPostSkillSalaryCheckMes;
import com.sunten.hrms.swm.service.SwmPostSkillSalaryService;
import com.sunten.hrms.swm.dto.SwmPostSkillSalaryDTO;
import com.sunten.hrms.swm.dto.SwmPostSkillSalaryQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmPostSkillSalaryMapper;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.service.ToolEmailInterfaceService;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 岗位技能工资表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmPostSkillSalaryServiceImpl extends ServiceImpl<SwmPostSkillSalaryDao, SwmPostSkillSalary> implements SwmPostSkillSalaryService {
    private final SwmPostSkillSalaryDao swmPostSkillSalaryDao;
    private final SwmPostSkillSalaryMapper swmPostSkillSalaryMapper;
    private final FndUserService fndUserService;
    private final FndDictDetailDao fndDictDetailDao;
    private final SwmFloatingWageDao swmFloatingWageDao;
    private final AcEmployeeAttendanceServiceImpl acEmployeeAttendanceServiceImpl;
    private final ToolEmailInterfaceService toolEmailInterfaceService;
    private final FndUserDao fndUserDao;
    DecimalFormat decimalFormat = new DecimalFormat("0.00#");
    @Value("${sunten.system-name}")
    private String systemName;
    @Value("${role.authSwmCharge}")
    private String authSwmCharge;

    public SwmPostSkillSalaryServiceImpl(SwmPostSkillSalaryDao swmPostSkillSalaryDao, SwmPostSkillSalaryMapper swmPostSkillSalaryMapper, FndUserService fndUserService
    ,FndDictDetailDao fndDictDetailDao, SwmFloatingWageDao swmFloatingWageDao, AcEmployeeAttendanceServiceImpl acEmployeeAttendanceServiceImpl,
                                         ToolEmailInterfaceService toolEmailInterfaceService, FndUserDao fndUserDao) {
        this.swmPostSkillSalaryDao = swmPostSkillSalaryDao;
        this.swmPostSkillSalaryMapper = swmPostSkillSalaryMapper;
        this.fndUserService = fndUserService;
        this.fndDictDetailDao = fndDictDetailDao;
        this.swmFloatingWageDao = swmFloatingWageDao;
        this.acEmployeeAttendanceServiceImpl = acEmployeeAttendanceServiceImpl;
        this.toolEmailInterfaceService = toolEmailInterfaceService;
        this.fndUserDao = fndUserDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmPostSkillSalaryDTO insert(SwmPostSkillSalary postSkillSalaryNew) {
        swmPostSkillSalaryDao.insertAllColumn(postSkillSalaryNew);
        return swmPostSkillSalaryMapper.toDto(postSkillSalaryNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmPostSkillSalary postSkillSalary = new SwmPostSkillSalary();
        postSkillSalary.setId(id);
        this.delete(postSkillSalary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmPostSkillSalary postSkillSalary) {
        // TODO    确认删除前是否需要做检查
        swmPostSkillSalaryDao.deleteByEntityKey(postSkillSalary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByPeriod(String period) {
        // 是否已被冻结
        SwmPostSkillSalaryQueryCriteria swmPostSkillSalaryQueryCriteria = new SwmPostSkillSalaryQueryCriteria();
        swmPostSkillSalaryQueryCriteria.setPeriod(period);
        if (swmPostSkillSalaryDao.countByCriteria(swmPostSkillSalaryQueryCriteria) > 0) {
            swmPostSkillSalaryQueryCriteria.setFrozenFlag(true);
            if (swmPostSkillSalaryDao.countByCriteria(swmPostSkillSalaryQueryCriteria) > 0) {
                throw new InfoCheckWarningMessException("该所得期间的岗位技能工资已被冻结、不允许删除");
            } else {
                SwmPostSkillSalary swmPostSkillSalary = new SwmPostSkillSalary();
                swmPostSkillSalary.setIncomePeriod(period);
                swmPostSkillSalaryDao.removeByPeriod(swmPostSkillSalary);
            }
        } else {
            throw new InfoCheckWarningMessException("该所得期间暂未生成岗位技能工资");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmPostSkillSalary postSkillSalaryNew) {
        SwmPostSkillSalary postSkillSalaryInDb = Optional.ofNullable(swmPostSkillSalaryDao.getByKey(postSkillSalaryNew.getId())).orElseGet(SwmPostSkillSalary::new);
        ValidationUtil.isNull(postSkillSalaryInDb.getId() ,"PostSkillSalary", "id", postSkillSalaryNew.getId());
        postSkillSalaryNew.setId(postSkillSalaryInDb.getId());
        // 设置应发工资、 实发工资
        setWage(postSkillSalaryNew);
        swmPostSkillSalaryDao.updateAllColumnByKey(postSkillSalaryNew);
    }
    @Override
    public void setWage(SwmPostSkillSalary postSkillSalaryNew) {
        // 应发工资= 岗位技能工资 + 工龄津贴 + 补贴_一孩 + 安全累积奖 + 高温补贴 + 搬迁交通补贴 + 岗位补贴 + 加班工资 + 补贴扣除
        postSkillSalaryNew.setWagesPayable(getWagesPayable(postSkillSalaryNew));

        // 实发工资 =岗位技能工资 + 工龄津贴 + 补贴_一孩 + 安全累积奖 + 高温补贴 + 搬迁交通补贴 + 岗位补贴 + 加班工资 + 补贴扣除
        // – 扣除_医药费 – 扣除_水电房 – 扣除_所得税 – 扣除_保险 – 扣除公积金_个人 – 扣除_其它
        postSkillSalaryNew.setNetPayment(new BigDecimal(decimalFormat.format(getWagesPayable(postSkillSalaryNew).subtract(null != postSkillSalaryNew.getDeductMedicalCosts() ? postSkillSalaryNew.getDeductMedicalCosts() : new BigDecimal(0))
                .subtract(null != postSkillSalaryNew.getDeductHydropowerHouse() ? postSkillSalaryNew.getDeductHydropowerHouse() : new BigDecimal(0))
                .subtract(null != postSkillSalaryNew.getDeductIncomeTax() ? postSkillSalaryNew.getDeductIncomeTax() : new BigDecimal(0))
                .subtract(null != postSkillSalaryNew.getPersonalDeductibles() ? postSkillSalaryNew.getPersonalDeductibles() : new BigDecimal(0))
                .subtract(null != postSkillSalaryNew.getPersonalDeductAccumulationFund() ? postSkillSalaryNew.getPersonalDeductAccumulationFund() : new BigDecimal(0))
                .subtract(null != postSkillSalaryNew.getDeductOther() ? postSkillSalaryNew.getDeductOther() : new BigDecimal(0))
        )));
    }

    @Override
    public SwmPostSkillSalaryDTO getByKey(Long id) {
        SwmPostSkillSalary postSkillSalary = Optional.ofNullable(swmPostSkillSalaryDao.getByKey(id)).orElseGet(SwmPostSkillSalary::new);
        ValidationUtil.isNull(postSkillSalary.getId() ,"PostSkillSalary", "id", id);
        return swmPostSkillSalaryMapper.toDto(postSkillSalary);
    }

    @Override
    public List<SwmPostSkillSalaryDTO> listAll(SwmPostSkillSalaryQueryCriteria criteria) {
        return swmPostSkillSalaryMapper.toDto(swmPostSkillSalaryDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmPostSkillSalaryQueryCriteria criteria, Pageable pageable) {
        Page<SwmPostSkillSalary> page = PageUtil.startPage(pageable);
        List<SwmPostSkillSalary> postSkillSalarys = swmPostSkillSalaryDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmPostSkillSalaryMapper.toDto(postSkillSalarys), page.getTotal());
    }

    @Override
    public void download(List<SwmPostSkillSalaryDTO> postSkillSalaryDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmPostSkillSalaryDTO postSkillSalaryDTO : postSkillSalaryDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("所得期间（格式：年.月）", postSkillSalaryDTO.getIncomePeriod());
            map.put("工牌号", postSkillSalaryDTO.getWorkCard());
            map.put("员工姓名", postSkillSalaryDTO.getEmployeeName());
            map.put("员工类别", postSkillSalaryDTO.getEmployeeCategory());
            if (postSkillSalaryDTO.getDivisionContractFlag()) {
                map.put("包干区分", "包干");
            } else {
                map.put("包干区分", "非包干");
            }
            map.put("部门", postSkillSalaryDTO.getDepartment());
            map.put("科室", postSkillSalaryDTO.getAdministrativeOffice());
            map.put("班组", postSkillSalaryDTO.getTeam());
            map.put("银行账号", postSkillSalaryDTO.getBankAccount());
            map.put("开户行名称", postSkillSalaryDTO.getBankName());
//            map.put("岗位技能工资", null != postSkillSalaryDTO.getPostSkillSalary() ? postSkillSalaryDTO.getPostSkillSalary() : new BigDecimal(decimalFormat.format(0)));
//            map.put("工龄津贴",  null != postSkillSalaryDTO.getSeniorityAllowance() ? postSkillSalaryDTO.getSeniorityAllowance() : new BigDecimal(decimalFormat.format(0)));
//            map.put("补贴_一孩", null !=  postSkillSalaryDTO.getOneChildSubsidy() ? postSkillSalaryDTO.getOneChildSubsidy() : new BigDecimal(decimalFormat.format(0)));
//            map.put("安全累积奖", null != postSkillSalaryDTO.getSafetyAccumulationAward() ? postSkillSalaryDTO.getSafetyAccumulationAward() : new BigDecimal(decimalFormat.format(0)));
//            map.put("高温津贴",  null != postSkillSalaryDTO.getHighTemperatureSubsidy() ?postSkillSalaryDTO.getHighTemperatureSubsidy() : new BigDecimal(decimalFormat.format(0)));
//            map.put("搬迁交通津贴",  null != postSkillSalaryDTO.getTransportationAllowance() ? postSkillSalaryDTO.getTransportationAllowance(): new BigDecimal(decimalFormat.format(0)));
//            map.put("岗位补贴",  null != postSkillSalaryDTO.getPostSubsidy() ? postSkillSalaryDTO.getPostSubsidy() : new BigDecimal(decimalFormat.format(0)));
//            map.put("工作日加班工资",  null != postSkillSalaryDTO.getOvertimePay() ? postSkillSalaryDTO.getOvertimePay() : new BigDecimal(decimalFormat.format(0)));
//            map.put("法定节假日加班工资",  null != postSkillSalaryDTO.getHolidayOvertimePay() ?postSkillSalaryDTO.getHolidayOvertimePay() : new BigDecimal(decimalFormat.format(0)));
//            map.put("休息日加班工资", null !=  postSkillSalaryDTO.getRestOvertimePay() ? postSkillSalaryDTO.getRestOvertimePay(): new BigDecimal(decimalFormat.format(0)));
//            map.put("补贴扣除",  null != postSkillSalaryDTO.getAllowanceDeduction() ?postSkillSalaryDTO.getAllowanceDeduction() : new BigDecimal(decimalFormat.format(0)));
//            map.put("应发工资",  null != postSkillSalaryDTO.getWagesPayable() ? postSkillSalaryDTO.getWagesPayable(): new BigDecimal(decimalFormat.format(0)));
//            map.put("扣除_医药费", null !=  postSkillSalaryDTO.getDeductMedicalCosts() ? postSkillSalaryDTO.getDeductMedicalCosts(): new BigDecimal(decimalFormat.format(0)));
//            map.put("扣除_水电房",  null != postSkillSalaryDTO.getDeductHydropowerHouse() ? postSkillSalaryDTO.getDeductHydropowerHouse(): new BigDecimal(decimalFormat.format(0)));
//            map.put("扣除_所得税",  null != postSkillSalaryDTO.getDeductIncomeTax() ?postSkillSalaryDTO.getDeductIncomeTax() : new BigDecimal(decimalFormat.format(0)));
//            map.put("扣除_保险",  null != postSkillSalaryDTO.getPersonalDeductibles() ? postSkillSalaryDTO.getPersonalDeductibles(): new BigDecimal(decimalFormat.format(0)));
//            map.put("扣除公积金_个人",  null != postSkillSalaryDTO.getPersonalDeductAccumulationFund() ? postSkillSalaryDTO.getPersonalDeductAccumulationFund(): new BigDecimal(decimalFormat.format(0)));
//            map.put("扣除_其它",  null != postSkillSalaryDTO.getDeductOther() ? postSkillSalaryDTO.getDeductOther(): new BigDecimal(decimalFormat.format(0)));
//            map.put("实发工资",  null != postSkillSalaryDTO.getNetPayment() ?postSkillSalaryDTO.getNetPayment() : new BigDecimal(decimalFormat.format(0)));
            map.put("岗位技能工资", null != postSkillSalaryDTO.getPostSkillSalary() ? postSkillSalaryDTO.getPostSkillSalary() : 0);
            map.put("工龄津贴",  null != postSkillSalaryDTO.getSeniorityAllowance() ? postSkillSalaryDTO.getSeniorityAllowance() : 0);
            map.put("一孩补贴", null !=  postSkillSalaryDTO.getOneChildSubsidy() ? postSkillSalaryDTO.getOneChildSubsidy() : 0);
            map.put("安全累积奖", null != postSkillSalaryDTO.getSafetyAccumulationAward() ? postSkillSalaryDTO.getSafetyAccumulationAward() : 0);
            map.put("高温津贴",  null != postSkillSalaryDTO.getHighTemperatureSubsidy() ?postSkillSalaryDTO.getHighTemperatureSubsidy() : 0);
            map.put("搬迁交通津贴",  null != postSkillSalaryDTO.getTransportationAllowance() ? postSkillSalaryDTO.getTransportationAllowance(): 0);
            map.put("岗位补贴",  null != postSkillSalaryDTO.getPostSubsidy() ? postSkillSalaryDTO.getPostSubsidy() : 0);
            map.put("工作日加班工资",  null != postSkillSalaryDTO.getOvertimePay() ? postSkillSalaryDTO.getOvertimePay() : 0);
            map.put("休息日加班工资", null !=  postSkillSalaryDTO.getRestOvertimePay() ? postSkillSalaryDTO.getRestOvertimePay(): 0);
            map.put("法定节假日加班工资",  null != postSkillSalaryDTO.getHolidayOvertimePay() ?postSkillSalaryDTO.getHolidayOvertimePay() : 0);
            map.put("补贴扣除",  null != postSkillSalaryDTO.getAllowanceDeduction() ?postSkillSalaryDTO.getAllowanceDeduction() : 0);
            map.put("应发工资",  null != postSkillSalaryDTO.getWagesPayable() ? postSkillSalaryDTO.getWagesPayable(): 0);
            map.put("扣除医药费", null !=  postSkillSalaryDTO.getDeductMedicalCosts() ? postSkillSalaryDTO.getDeductMedicalCosts(): 0);
            map.put("扣除水电房",  null != postSkillSalaryDTO.getDeductHydropowerHouse() ? postSkillSalaryDTO.getDeductHydropowerHouse(): 0);
            map.put("扣除所得税",  null != postSkillSalaryDTO.getDeductIncomeTax() ?postSkillSalaryDTO.getDeductIncomeTax() : 0);
            map.put("扣除保险（个人）",  null != postSkillSalaryDTO.getPersonalDeductibles() ? postSkillSalaryDTO.getPersonalDeductibles(): 0);
            map.put("扣除公积金（个人）",  null != postSkillSalaryDTO.getPersonalDeductAccumulationFund() ? postSkillSalaryDTO.getPersonalDeductAccumulationFund(): 0);
            map.put("扣除其它",  null != postSkillSalaryDTO.getDeductOther() ? postSkillSalaryDTO.getDeductOther(): 0);
            map.put("实发工资",  null != postSkillSalaryDTO.getNetPayment() ?postSkillSalaryDTO.getNetPayment() : 0);
            map.put("成本中心名称", postSkillSalaryDTO.getCostCenter());
            map.put("成本中心号", postSkillSalaryDTO.getCostCenterNum());
            map.put("服务部门", postSkillSalaryDTO.getServiceDepartment());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SwmPostSkillSalary> generatePostSkillSalary(String period) {
        // 检查人员信息
        SwmPostSkillSalaryCheckMes swmPostSkillSalaryCheckMes = swmPostSkillSalaryDao.checkBeforePostSkillSalaryGenerate();
        StringBuilder str = new StringBuilder();
        if (!swmPostSkillSalaryCheckMes.getCheckEmployeeCategory().equals("")) {
            str.append(swmPostSkillSalaryCheckMes.getCheckEmployeeCategory());
        }
        if (!swmPostSkillSalaryCheckMes.getCheckIsDivisionContractFlag().equals("")) {
            str.append(swmPostSkillSalaryCheckMes.getCheckIsDivisionContractFlag());
        }
        if (!swmPostSkillSalaryCheckMes.getCheckNotDivisionContractFlag1().equals("")) {
            str.append(swmPostSkillSalaryCheckMes.getCheckNotDivisionContractFlag1());
        }
        if (!swmPostSkillSalaryCheckMes.getCheckNotDivisionContractFlag2().equals("")) {
            str.append(swmPostSkillSalaryCheckMes.getCheckNotDivisionContractFlag2());
        }
        if (!str.toString().equals("")) {
            throw new InfoCheckWarningMessException(str.toString());
        }
        // 检查岗位技能工资
        SwmPostSkillSalaryQueryCriteria swmPostSkillSalaryQueryCriteria = new SwmPostSkillSalaryQueryCriteria();
        swmPostSkillSalaryQueryCriteria.setPeriod(period);
        List<SwmPostSkillSalary> swmPostSkillSalaries = swmPostSkillSalaryDao.listAllByCriteria(swmPostSkillSalaryQueryCriteria);
        if (swmPostSkillSalaries.size() > 0) {
            throw new InfoCheckWarningMessException("已存在该所得期间的岗位技能工资。若要生成，请先删除该锁定期间的岗位技能工资。");
        }
        // 生成固定工资
        swmPostSkillSalaryDao.generatePostSkillSalary(period);
        // 返回
        List<SwmPostSkillSalary> swmPostSkillSalaryList = swmPostSkillSalaryDao.listAllByCriteria(swmPostSkillSalaryQueryCriteria);
        for (SwmPostSkillSalary sa : swmPostSkillSalaryList
             ) {
            setWage(sa);
        }
        return swmPostSkillSalaryList;
    }

    @Override
    public List<String> generatePeriodList() {
        String topPeriod = swmPostSkillSalaryDao.selectTop1Period();
        return getPeriodList(topPeriod);
    }

    public List<String> getPeriodList(String topPeriod) {
        List<String> periodList = new ArrayList<>();
        LocalDate now = LocalDate.now();
        if (null == topPeriod) {
            System.out.println(now.getYear());
            String middlePeriod = now.getYear() + "." + (now.getMonthValue() < 10 ? "0" + now.getMonthValue() : now.getMonthValue() + "");
            String addTopPeriod = now.getMonthValue() == 12 ?
                    now.getYear() + 1 + ".01" : now.getYear() + "." + (now.getMonthValue() + 1 < 10 ? "0" + (now.getMonthValue() + 1): now.getMonthValue() + 1 + "");
            String subtractPeriod = now.getMonthValue() == 1 ?
                    now.getYear() - 1 + ".12" : now.getYear() + "." + (now.getMonthValue() - 1 < 10 ? "0" + (now.getMonthValue() - 1) : now.getMonthValue() - 1 + "");
            periodList.add(subtractPeriod);
            periodList.add(middlePeriod);
            periodList.add(addTopPeriod);
        } else {
            String[] s = topPeriod.split("\\.");
            String addTopPeriod = s[1].equals("12") ?
                    Integer.parseInt(s[0]) + 1 + ".01" : s[0] + "." + (Integer.parseInt(s[1]) + 1 < 10 ? "0" + (Integer.parseInt(s[1]) + 1) : (Integer.parseInt(s[1]) + 1));
            String subtractPeriod = s[1].equals("01") ?
                    Integer.parseInt(s[0]) - 1 + ".12" : s[0] + "." + (Integer.parseInt(s[1]) - 1 < 10 ? "0" + (Integer.parseInt(s[1]) - 1) : (Integer.parseInt(s[1]) - 1));
            periodList.add(subtractPeriod);
            periodList.add(topPeriod);
            periodList.add(addTopPeriod);
        }
        return periodList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void interfaceToMain(Long groupId) {
        swmPostSkillSalaryDao.interfaceToMain(groupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void interfaceToMainUpdateWageAndNet(Long groupId) {
        swmPostSkillSalaryDao.interfaceToMainUpdateWageAndNet(groupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SwmPostSkillSalary> generatePostSkillSalaryByMsp(String period) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Map<String, Object> map = new HashMap<>();
        map.put("period", period);
        map.put("userId", user.getId());
        map.put("resultStr", "");
        swmPostSkillSalaryDao.generatePostSkillSalaryByMsp(map);
        String result = map.get("resultStr").toString();
        if (!result.equals("SUCCESS")) {
            throw new InfoCheckWarningMessException(result);
        } else {
            SwmPostSkillSalaryQueryCriteria swmPostSkillSalaryQueryCriteria = new SwmPostSkillSalaryQueryCriteria();
            swmPostSkillSalaryQueryCriteria.setPeriod(period);
            return swmPostSkillSalaryDao.listAllByCriteria(swmPostSkillSalaryQueryCriteria);
        }
    }

    private BigDecimal getWagesPayable(SwmPostSkillSalary sa) {
        // 应发工资= 岗位技能工资 + 工龄津贴 + 补贴_一孩 + 安全累积奖 + 高温补贴 + 搬迁交通补贴 + 岗位补贴 + 加班工资 + 补贴扣除
        return new BigDecimal(decimalFormat.format(new BigDecimal(0).add(null != sa.getPostSkillSalary() ? sa.getPostSkillSalary() : new BigDecimal(0))
                .add(null != sa.getSeniorityAllowance() ? sa.getSeniorityAllowance() : new BigDecimal(0))
                .add(null != sa.getOneChildSubsidy() ? sa.getOneChildSubsidy() : new BigDecimal(0))
                .add(null != sa.getSafetyAccumulationAward() ? sa.getSafetyAccumulationAward() : new BigDecimal(0))
                .add(null != sa.getHighTemperatureSubsidy() ? sa.getHighTemperatureSubsidy() : new BigDecimal(0))
                .add(null != sa.getTransportationAllowance() ? sa.getTransportationAllowance() : new BigDecimal(0))
                .add(null != sa.getPostSubsidy() ? sa.getPostSubsidy() : new BigDecimal(0))
                .add(null != sa.getOvertimePay() ? sa.getOvertimePay() : new BigDecimal(0))
                .add(null != sa.getHolidayOvertimePay() ? sa.getHolidayOvertimePay() : new BigDecimal(0))
                .add(null != sa.getRestOvertimePay() ? sa.getRestOvertimePay() : new BigDecimal(0))
                .add(null != sa.getAllowanceDeduction() ? sa.getAllowanceDeduction() : new BigDecimal(0))
        ));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdatePostSkillSalary(List<SwmPostSkillSalary> swmPostSkillSalaryList) {
        try {
            List<Long> ids = swmPostSkillSalaryList.stream().map(SwmPostSkillSalary::getId).collect(Collectors.toList());
            System.out.println(ids);
            for (SwmPostSkillSalary sps : swmPostSkillSalaryList
            ) {
                swmPostSkillSalaryDao.updateAllColumnByKey(sps);
            }
            swmPostSkillSalaryDao.updateWageAndNetAfterUpdate(ids);
        } catch (Exception e) {
            throw new InfoCheckWarningMessException("批量更新数据错误，请联系管理人员");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDeductIncomeTaxAfterTaxImport(String incomePeriod) {
        swmPostSkillSalaryDao.updateDeductIncomeTaxAfterTaxImport(incomePeriod);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWageAndNetAfterUpdateCol(String incomePeriod) {
        swmPostSkillSalaryDao.updateWageAndNetAfterUpdateCol(incomePeriod);
    }

    @Override
    public Boolean checkPostSalaryBeforAutoUpdate(String incomePeriod) {
        return swmPostSkillSalaryDao.checkPostSalaryBeforAutoUpdate(incomePeriod);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoDeductByMsp(String period) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Map<String, Object> map = new HashMap<>();
        map.put("period", period);
        map.put("userId", user.getId());
        map.put("resultStr", "");
        swmPostSkillSalaryDao.autoDeductByMsp(map);
        if (!map.get("resultStr").equals("SUCCESS")) {
            throw new InfoCheckWarningMessException((String)map.get("resultStr"));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendEmailByOverTimeChange(String period) {
        ToolEmailInterface toolEmailInterface = new ToolEmailInterface();
        acEmployeeAttendanceServiceImpl.getServer(toolEmailInterface);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/swm/overTimeChangeTemplate.ftl");
        // 获取薪酬人员名单
        List<String> emails = fndUserDao.selectEmailListByRole(authSwmCharge);
        toolEmailInterface.setMailSubject(period + "工时修改通知 - " + systemName );
        toolEmailInterface.setPlannedDate( LocalDateTime.now());
        toolEmailInterface.setStatus("PLAN");
        Dict dict = Dict.create();
        dict.set("period", period);
        for (String email : emails
             ) {
            toolEmailInterface.setSendTo(email);
            toolEmailInterface.setMailContent(template.render(dict));
            toolEmailInterfaceService.insert(toolEmailInterface);
        }
    }

    @Override
    public int checkFrozenFlagByPeriod(String period) {
        return swmPostSkillSalaryDao.checkFrozenFlagByPeriod(period);
    }
}
