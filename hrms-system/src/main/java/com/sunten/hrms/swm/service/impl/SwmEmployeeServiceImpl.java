package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndRoleDao;
import com.sunten.hrms.fnd.dao.FndUserDao;
import com.sunten.hrms.fnd.dao.FndUsersRolesDao;
import com.sunten.hrms.fnd.domain.FndRole;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.fnd.dto.FndRoleQueryCriteria;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.dto.FndUserQueryCriteria;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.fnd.service.RedisService;
import com.sunten.hrms.pm.dao.PmEmployeeDao;
import com.sunten.hrms.pm.dao.PmTransferRequestDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.swm.dao.SwmEmployeeHistoryDao;
import com.sunten.hrms.swm.domain.SwmDept;
import com.sunten.hrms.swm.domain.SwmEmployee;
import com.sunten.hrms.swm.dao.SwmEmployeeDao;
import com.sunten.hrms.swm.domain.SwmEmployeeHistory;
import com.sunten.hrms.swm.service.SwmEmployeeHistoryService;
import com.sunten.hrms.swm.service.SwmEmployeeService;
import com.sunten.hrms.swm.dto.SwmEmployeeDTO;
import com.sunten.hrms.swm.dto.SwmEmployeeQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmEmployeeMapper;
import com.sunten.hrms.swm.vo.EmployeeDistinctTings;
import com.sunten.hrms.swm.vo.EmployeeMsg;
import com.sunten.hrms.swm.vo.JobTransferSalaryVo;
import com.sunten.hrms.swm.vo.SwmDeptVo;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;

/**
 * <p>
 * 薪酬员工信息表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Service
@CacheConfig(cacheNames = "menu")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmEmployeeServiceImpl extends ServiceImpl<SwmEmployeeDao, SwmEmployee> implements SwmEmployeeService {
    private final SwmEmployeeDao swmEmployeeDao;
    private final SwmEmployeeMapper swmEmployeeMapper;
    private final SwmEmployeeHistoryDao swmEmployeeHistoryDao;
    private final PmEmployeeDao pmEmployeeDao;
    private final FndUserService fndUserService;
    private final FndUsersRolesDao fndUsersRolesDao;
    private final FndRoleDao fndRoleDao;
    private final FndUserDao fndUserDao;
    private final RedisService redisService;
    private final PmTransferRequestDao pmTransferRequestDao;

    DecimalFormat decimalFormat = new DecimalFormat("0.00#");

    @Value("${role.monthAssessmentCharge}")
    private String monthAssessmentCharge;

    public SwmEmployeeServiceImpl(SwmEmployeeDao swmEmployeeDao, SwmEmployeeMapper swmEmployeeMapper,SwmEmployeeHistoryDao swmEmployeeHistoryDao,
                                  PmEmployeeDao pmEmployeeDao, FndUserService fndUserService, FndUsersRolesDao fndUsersRolesDao, FndRoleDao fndRoleDao,
                                  FndUserDao fndUserDao, RedisService redisService, PmTransferRequestDao pmTransferRequestDao) {
        this.swmEmployeeDao = swmEmployeeDao;
        this.swmEmployeeMapper = swmEmployeeMapper;
        this.swmEmployeeHistoryDao = swmEmployeeHistoryDao;
        this.pmEmployeeDao = pmEmployeeDao;
        this.fndUserService = fndUserService;
        this.fndUsersRolesDao = fndUsersRolesDao;
        this.fndRoleDao = fndRoleDao;
        this.fndUserDao= fndUserDao;
        this.redisService = redisService;
        this.pmTransferRequestDao = pmTransferRequestDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmEmployeeDTO insert(SwmEmployee employeeNew) {
        swmEmployeeDao.insertAllColumn(employeeNew);
        // 创建历史
        createHistory(employeeNew, new SwmEmployee(), false);
        return swmEmployeeMapper.toDto(employeeNew);
    }

    // 创建历史method
    private void createHistory(SwmEmployee employeeNew,SwmEmployee employeeOld, Boolean updateFlag) {
        SwmEmployeeHistory swmEmployeeHistory = new SwmEmployeeHistory();
        swmEmployeeHistory.setSeId(employeeNew.getId());
        swmEmployeeHistory.setWorkCard(employeeNew.getWorkCard());
        swmEmployeeHistory.setEmployeeName(employeeNew.getName());
        if (null != employeeNew.getPostSkillSalary()) {
            swmEmployeeHistory.setPostSkillSalary(new BigDecimal(decimalFormat.format(employeeNew.getPostSkillSalary())));
        }
        if (null != employeeNew.getTargetPerformancePay()) {
            swmEmployeeHistory.setTargetPerformancePay(new BigDecimal(decimalFormat.format(employeeNew.getTargetPerformancePay())));
        }

        swmEmployeeHistory.setDivisionContractFlag(employeeNew.getDivisionContractFlag());
        if (null != employeeNew.getLumpSumWage()) {
            swmEmployeeHistory.setLumpSumWage(new BigDecimal(decimalFormat.format(employeeNew.getLumpSumWage())));
        }
        if (null != employeeNew.getBasePay()) {
            swmEmployeeHistory.setBasePay(new BigDecimal(decimalFormat.format(employeeNew.getBasePay())));
        }
        if (null != employeeNew.getPostSubsidy()) {
            swmEmployeeHistory.setPostSubsidy(new BigDecimal(decimalFormat.format(employeeNew.getPostSubsidy())));
        }
        if (null != employeeNew.getSeniorityAllowance()) {
            swmEmployeeHistory.setSeniorityAllowance(new BigDecimal(decimalFormat.format(employeeNew.getSeniorityAllowance())));
        }
        if (null != employeeNew.getPersonalDeductibles()) {
            swmEmployeeHistory.setPersonalDeductibles(new BigDecimal(decimalFormat.format(employeeNew.getPersonalDeductibles())));
        }
        if (null != employeeNew.getPersonalDeductAccumulationFund()) {
            swmEmployeeHistory.setPersonalDeductAccumulationFund(new BigDecimal(decimalFormat.format(employeeNew.getPersonalDeductAccumulationFund())));
        }
        if (null != employeeNew.getCompanyDeductibles()) {
            swmEmployeeHistory.setCompanyDeductibles(new BigDecimal(decimalFormat.format(employeeNew.getCompanyDeductibles())));
        }
        if (null != employeeNew.getCompanyDeductAccumulationFund()) {
            swmEmployeeHistory.setCompanyDeductAccumulationFund(new BigDecimal(decimalFormat.format(employeeNew.getCompanyDeductAccumulationFund())));
        }
        if (!updateFlag) { // 新增
            swmEmployeeHistory.setRangeChange(new BigDecimal(decimalFormat.format(
                    new BigDecimal(0)
                            .add(null != swmEmployeeHistory.getPostSkillSalary() ? swmEmployeeHistory.getPostSkillSalary() : new BigDecimal(0) )
                            .add(null != swmEmployeeHistory.getTargetPerformancePay() ? swmEmployeeHistory.getTargetPerformancePay() : new BigDecimal(0))
                            .add(null != swmEmployeeHistory.getLumpSumWage() ? swmEmployeeHistory.getLumpSumWage() : new BigDecimal(0))
                            .add(null != swmEmployeeHistory.getBasePay() ? swmEmployeeHistory.getBasePay() : new BigDecimal(0))
            )));
        } else { // update
            swmEmployeeHistory.setRangeChange(new BigDecimal(decimalFormat.format(new BigDecimal(0)
                    .add(null != swmEmployeeHistory.getPostSkillSalary() ? swmEmployeeHistory.getPostSkillSalary() : new BigDecimal(0) )
                    .add(null != swmEmployeeHistory.getTargetPerformancePay() ? swmEmployeeHistory.getTargetPerformancePay() : new BigDecimal(0))
                    .add(null != swmEmployeeHistory.getLumpSumWage() ? swmEmployeeHistory.getLumpSumWage() : new BigDecimal(0))
                    .add(null != swmEmployeeHistory.getBasePay() ? swmEmployeeHistory.getBasePay() : new BigDecimal(0))
                    .subtract(null != employeeOld.getPostSkillSalary() ? employeeOld.getPostSkillSalary() : new BigDecimal(0))
                    .subtract(null != employeeOld.getTargetPerformancePay() ? employeeOld.getTargetPerformancePay() : new BigDecimal(0))
                    .subtract(null != employeeOld.getLumpSumWage() ? employeeOld.getLumpSumWage() : new BigDecimal(0))
                    .subtract(null != employeeOld.getBasePay() ? employeeOld.getBasePay() : new BigDecimal(0))
            )));
        }
        swmEmployeeHistoryDao.insertAllColumn(swmEmployeeHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmEmployee employee = new SwmEmployee();
        employee.setId(id);
        this.delete(employee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmEmployee employee) {
        // TODO    确认删除前是否需要做检查
        swmEmployeeDao.deleteByEntityKey(employee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmEmployee employeeNew) {
        SwmEmployee employeeInDb = Optional.ofNullable(swmEmployeeDao.getByKey(employeeNew.getId())).orElseGet(SwmEmployee::new);
        if (null == employeeInDb.getPostSkillSalary()) {
            employeeInDb.setPostSkillSalary(new BigDecimal(0));
        }
        ValidationUtil.isNull(employeeInDb.getId() ,"Employee", "id", employeeNew.getId());
        employeeNew.setId(employeeInDb.getId());
        swmEmployeeDao.updateAllColumnByKey(employeeNew);
        pmTransferRequestDao.updateSwmChangeFlag(employeeNew);
        this.checkNullAndSetZero(employeeNew);
        this.checkNullAndSetZero(employeeInDb);
        // 当固定工资或目标绩效工资或包干区分发生改变时，创建一条历史
        if (employeeInDb.getPostSkillSalary().compareTo(employeeNew.getPostSkillSalary()) != 0 ||
            employeeInDb.getTargetPerformancePay().compareTo(employeeNew.getTargetPerformancePay()) != 0 ||
            employeeInDb.getDivisionContractFlag() != employeeNew.getDivisionContractFlag() ||
            employeeInDb.getLumpSumWage().compareTo(employeeNew.getLumpSumWage()) != 0 ||
            employeeInDb.getBasePay().compareTo(employeeNew.getBasePay()) != 0 ||
            employeeInDb.getPostSubsidy().compareTo(employeeNew.getPostSubsidy()) != 0 ||
            employeeInDb.getSeniorityAllowance().compareTo(employeeNew.getSeniorityAllowance()) != 0 ||
            employeeInDb.getPersonalDeductibles().compareTo(employeeNew.getPersonalDeductibles()) != 0 ||
            employeeInDb.getCompanyDeductibles().compareTo(employeeNew.getCompanyDeductibles()) != 0 ||
            employeeInDb.getPersonalDeductAccumulationFund().compareTo(employeeNew.getPersonalDeductAccumulationFund()) != 0 ||
            employeeInDb.getCompanyDeductAccumulationFund().compareTo(employeeNew.getCompanyDeductAccumulationFund()) != 0
        ) {
            // 创建历史
            createHistory(employeeNew, employeeInDb, true);
        }
    }

    @Override
    public SwmEmployeeDTO getByKey(Long id) {
        SwmEmployee employee = Optional.ofNullable(swmEmployeeDao.getByKey(id)).orElseGet(SwmEmployee::new);
        ValidationUtil.isNull(employee.getId() ,"Employee", "id", id);
        return swmEmployeeMapper.toDto(employee);
    }

    @Override
    public List<SwmEmployeeDTO> listAll(SwmEmployeeQueryCriteria criteria) {
        return swmEmployeeMapper.toDto(swmEmployeeDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmEmployeeQueryCriteria criteria, Pageable pageable) {
        Page<SwmEmployee> page = PageUtil.startPage(pageable);
        List<SwmEmployee> employees = swmEmployeeDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmEmployeeMapper.toDto(employees), page.getTotal());
    }

    @Override
    public Map<String, Object> listAllHaveAuth(SwmEmployeeQueryCriteria criteria, Pageable pageable) {
        Page<SwmEmployee> page = PageUtil.startPage(pageable);
        List<SwmEmployee> employees = swmEmployeeDao.listAllHaveAuthByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmEmployeeMapper.toDto(employees), page.getTotal());
    }

    @Override
    public void download(List<SwmEmployeeDTO> employeeDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (SwmEmployeeDTO employeeDTO : employeeDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("工牌号", employeeDTO.getWorkCard());
            map.put("姓名", employeeDTO.getName());
            map.put("员工类别", employeeDTO.getEmployeeCategory());
            map.put("银行账号", null != employeeDTO.getBankAccount() ? employeeDTO.getBankAccount() : "");
            map.put("开户行名称", null != employeeDTO.getBankName() ? employeeDTO.getBankName() : "");
            map.put("所在部门", null != employeeDTO.getDepartment() ? employeeDTO.getDepartment() : "");
            map.put("所在科室", null != employeeDTO.getAdministrativeOffice() ? employeeDTO.getAdministrativeOffice() : "");
            map.put("班组", null != employeeDTO.getTeam() ? employeeDTO.getTeam() : "");
            map.put("岗位", null != employeeDTO.getStation() ? employeeDTO.getStation() : "");
            map.put("职级", employeeDTO.getRank());
            map.put("技术职级", employeeDTO.getTechnicalRank());
            map.put("技能级别", employeeDTO.getSkillLevel());
            if (employeeDTO.getGenerationDifferentiationFlag()) {
                map.put("生产区分", "生产");
            } else {
                map.put("生产区分", "非生产");
            }
            if (employeeDTO.getDivisionContractFlag()) {
                map.put("包干区分", "包干");
            } else {
                map.put("包干区分", "非包干");
            }
            map.put("包干工资", employeeDTO.getLumpSumWage());
            map.put("岗位技能工资", employeeDTO.getPostSkillSalary());
            map.put("目标绩效工资", employeeDTO.getTargetPerformancePay());
            map.put("月基本工资", employeeDTO.getBasePay());
            map.put("班长津贴", employeeDTO.getSquadLeaderAllowance());
            map.put("岗位补贴", employeeDTO.getPostSubsidy());
            map.put("工龄津贴", employeeDTO.getSeniorityAllowance());
            map.put("搬迁交通津贴", employeeDTO.getTransportationAllowance());
            if (null != employeeDTO.getAccessmentForm()) {
                map.put("考核形式（月度考核、季度考核、无考核）",
                        employeeDTO.getAccessmentForm().equals("month") ? "月度考核" :
                        employeeDTO.getAccessmentForm().equals("quarter") ? "季度考核" :
                        employeeDTO.getAccessmentForm().equals("year") ? "年度考核" : "无考核");
            } else {

            }
            map.put("考核形式（月度考核、季度考核、无考核）",
                    employeeDTO.getAccessmentForm());
            map.put("补贴一孩", employeeDTO.getOneChildSubsidy());
            map.put("扣除保险(个人)", employeeDTO.getPersonalDeductibles());
            map.put("扣除保险(公司)", employeeDTO.getCompanyDeductibles());
            map.put("扣除公积金（个人）", employeeDTO.getPersonalDeductAccumulationFund());
            map.put("扣除公积金（公司）", employeeDTO.getCompanyDeductAccumulationFund());
            map.put("安全累积奖", employeeDTO.getSafetyAccumulationAward());
            map.put("高温补贴", employeeDTO.getHighTemperatureSubsidy());
            map.put("职类", employeeDTO.getCategory());
            map.put("职种", employeeDTO.getJob());
            map.put("职位", employeeDTO.getPosition());
            map.put("职称", employeeDTO.getTitle());
            map.put("学历", employeeDTO.getEducation());
            if (null != employeeDTO.getEntryTime()) {
                map.put("入职时间", employeeDTO.getEntryTime());//入职时间
            } else {
                map.put("入职时间", "");
            }
            if (employeeDTO.getDivisionContractFlag()) {
                map.put("包干区分", "包干");
            } else {
                map.put("包干区分", "非包干");
            }
            if (employeeDTO.getGenerationDifferentiationFlag()) {
                map.put("生产区分", "生产");
            } else {
                map.put("生产区分", "非生产");
            }
            map.put("成本中心编号", employeeDTO.getCostCenterNum());
            map.put("成本中心名称", employeeDTO.getCostCenter());
            map.put("服务部门", employeeDTO.getServiceDepartment());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public EmployeeDistinctTings getDistinctTings() {
        EmployeeDistinctTings employeeDistinctTings = new EmployeeDistinctTings();
        employeeDistinctTings.setAdministrativeOffices(swmEmployeeDao.distinctByAdministrativeOffice(null));
        employeeDistinctTings.setCategorys(swmEmployeeDao.distinctByCategory());
        employeeDistinctTings.setDepartments(swmEmployeeDao.distinctByDepartment());
        employeeDistinctTings.setEducations(swmEmployeeDao.distinctByEducation());
        employeeDistinctTings.setEmployeeCategorys(swmEmployeeDao.distinctByEmployeeCategory());
        employeeDistinctTings.setJobs(swmEmployeeDao.distinctByJob());
        employeeDistinctTings.setPositions(swmEmployeeDao.distinctByPosition());
        employeeDistinctTings.setRanks(swmEmployeeDao.distinctByRank());
        employeeDistinctTings.setSkillLevels(swmEmployeeDao.distinctBySkillLevel());
        employeeDistinctTings.setStations(swmEmployeeDao.distinctByStation());
        employeeDistinctTings.setTeams(swmEmployeeDao.distinctByTeam(null));
        employeeDistinctTings.setTechnicalRanks(swmEmployeeDao.distinctByTechnicalRank());
        employeeDistinctTings.setTitles(swmEmployeeDao.distinctByTitle());
        employeeDistinctTings.setServiceDepartments(swmEmployeeDao.distinctByServiceDepartment());
        return employeeDistinctTings;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByWorkFlag(SwmEmployee employee) {
        employee.setWorkFlag(false);
        swmEmployeeDao.updateByWorkFlag(employee);
    }
//
//    @Override
//    @CacheEvict(allEntries = true)
//    @Transactional(rollbackFor = Exception.class)
//    public void updateByManagerFlag(SwmEmployee employee) {
//        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
//        if (employee.getManagerFlag()) { // 建立user role 权限
//            employee.setPermission(monthAssessmentCharge);
//            employee.setUserId(user.getId());
//            swmEmployeeDao.insertUserRoleBySwmEmp(employee);
//            FndUserQueryCriteria fndUserQueryCriteria = new FndUserQueryCriteria();
//            fndUserQueryCriteria.setEmployeeId(employee.getEmployeeId());
//            List<FndUser> fndUsers = fndUserDao.listAllByCriteria(fndUserQueryCriteria);
//            for (FndUser fndUser : fndUsers
//            ) {
//                String key = "role::loadPermissionByUser:" + fndUser.getId();
//                redisService.delete(key);
//                key = "role::findByUserId:" + fndUser.getId();
//                redisService.delete(key);
//            }
//        } else { // 删除user role 权限
//            FndRoleQueryCriteria fndRoleQueryCriteria = new FndRoleQueryCriteria();
//            fndRoleQueryCriteria.setPermission(monthAssessmentCharge);
//            List<FndRole> fndRoles = fndRoleDao.listAllByCriteria(fndRoleQueryCriteria);
//            if (fndRoles.size() != 1) {
//                throw new InfoCheckWarningMessException("检测到基础权限有异常，暂不允许操作，请告知管理员处理");
//            } else {
//                FndUserQueryCriteria fndUserQueryCriteria = new FndUserQueryCriteria();
//                fndUserQueryCriteria.setEmployeeId(employee.getEmployeeId());
//                List<FndUser> fndUsers = fndUserDao.listAllByCriteria(fndUserQueryCriteria);
//                for (FndUser fndUser : fndUsers
//                     ) {
//                    fndUsersRolesDao.deleteByKey(fndUser.getId(), fndRoles.get(0).getId());
//                    String key = "role::loadPermissionByUser:" + fndUser.getId();
//                    redisService.delete(key);
//                    key = "role::findByUserId:" + fndUser.getId();
//                    redisService.delete(key);
//                }
//            }
//        }
//        swmEmployeeDao.updateByManagerFlag(employee);
//    }

    @Override
    public List<EmployeeMsg> listForSwmEmpUse(String nameOrWorkCard) {
        List<PmEmployee> pmEmployees = pmEmployeeDao.listForSwmEmpUse(nameOrWorkCard);
        List<EmployeeMsg> employeeMsgs = new ArrayList<>();
        for (PmEmployee pe : pmEmployees
             ) {
            EmployeeMsg employeeMsg = new EmployeeMsg();
            employeeMsg.setDeptName(pe.getDeptName());
            employeeMsg.setDepartment(pe.getDepartment());
            employeeMsg.setTeam(pe.getTeam());
            employeeMsg.setEducation(null != pe.getBetterEducation() ? pe.getBetterEducation().getEducation() : "");
            employeeMsg.setEmpId(pe.getId());
            employeeMsg.setEntryTime(null != pe.getEmployeeEntry() ? pe.getEmployeeEntry().getEntryTime() : LocalDate.now());
            employeeMsg.setTitle(null != pe.getBetterTitle() ? pe.getBetterTitle().getTitleName() : "");
            employeeMsg.setWorkCard(pe.getWorkCard());
            employeeMsg.setName(pe.getName());
            employeeMsg.setJobName(pe.getJobName());
            employeeMsgs.add(employeeMsg);
        }
        return employeeMsgs;
    }



    @Override
    public List<SwmDeptVo> buildDeptList() { // 生成dept
        List<SwmDept> swmDepts = swmEmployeeDao.distinctByDept();
        List<SwmDeptVo> swmDeptVos = new ArrayList<>();
        String nameTarget = "";
        List<SwmDeptVo> childSet = new ArrayList<>();
        SwmDeptVo swmDeptVo = new SwmDeptVo();
        for (SwmDept sw : swmDepts
        ) {
            if (sw.getDepartment().equals(nameTarget)) {
                SwmDeptVo son = new SwmDeptVo();
                son.setDeptCode((sw.getDepartment() + ".") + (null != sw.getAdministrativeOffice() ? sw.getAdministrativeOffice() : "NULL"));
                son.setDeptName(sw.getAdministrativeOffice());
                childSet.add(son);
            } else {
                swmDeptVo.setChildSet(childSet);
                if (!"".equals(nameTarget)) {
                    swmDeptVos.add(swmDeptVo);
                }
                nameTarget = sw.getDepartment();
                swmDeptVo = new SwmDeptVo();
                swmDeptVo.setDeptName(sw.getDepartment());
                swmDeptVo.setDeptCode(sw.getDepartment());
                childSet = new ArrayList<>();
                if (null != sw.getAdministrativeOffice()) {
                    SwmDeptVo son = new SwmDeptVo();
                    son.setDeptCode(sw.getDepartment() + "." + sw.getAdministrativeOffice());
                    son.setDeptName(sw.getAdministrativeOffice());
                    childSet.add(son);
                }
            }
        }
        swmDeptVo.setChildSet(childSet);
        swmDeptVos.add(swmDeptVo);
        return swmDeptVos;
    }

    @Override
    public SwmEmployeeDTO getSwmEmpByEmpId() {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        SwmEmployee swmEmployee = swmEmployeeDao.getSwmEmpByEmpId(user.getEmployee().getId());
        return swmEmployeeMapper.toDto(swmEmployee);
    }

    @Override
    public List<String> getDistDepartment() {
        return swmEmployeeDao.distinctByDepartment();
    }

    @Override
    public List<String> getDistAdministrativeOffice(String department) {
        return swmEmployeeDao.distinctByAdministrativeOffice(department);
    }

    @Override
    public List<String> getTeamList(String office) {
        return swmEmployeeDao.distinctByTeam(office);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserRoleBySwmEmp(SwmEmployee swmEmployee) {
        swmEmployeeDao.insertUserRoleBySwmEmp(swmEmployee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void interfaceToMain(Long groupId) {
        swmEmployeeDao.interfaceToMain(groupId);
    }

    private void checkNullAndSetZero(SwmEmployee swmEmployee) {
        if (null == swmEmployee.getPostSkillSalary()) {
            swmEmployee.setPostSkillSalary(new BigDecimal(0));
        }
        if (null == swmEmployee.getTargetPerformancePay()) {
            swmEmployee.setTargetPerformancePay(new BigDecimal(0));
        }
        if (null == swmEmployee.getLumpSumWage()) {
            swmEmployee.setLumpSumWage(new BigDecimal(0));
        }
        if (null == swmEmployee.getBasePay()) {
            swmEmployee.setBasePay(new BigDecimal(0));
        }
        if (null == swmEmployee.getPostSubsidy()) {
            swmEmployee.setPostSubsidy(new BigDecimal(0));
        }
        if (null == swmEmployee.getSeniorityAllowance()) {
            swmEmployee.setSeniorityAllowance(new BigDecimal(0));
        }
        if (null == swmEmployee.getPersonalDeductibles()) {
            swmEmployee.setPersonalDeductibles(new BigDecimal(0));
        }
        if (null == swmEmployee.getPersonalDeductAccumulationFund()) {
            swmEmployee.setPersonalDeductAccumulationFund(new BigDecimal(0));
        }
        if (null == swmEmployee.getCompanyDeductibles()) {
            swmEmployee.setCompanyDeductibles(new BigDecimal(0));
        }
        if (null == swmEmployee.getCompanyDeductAccumulationFund()) {
            swmEmployee.setCompanyDeductAccumulationFund(new BigDecimal(0));
        }
    }

    /**
     *  @author liangjw
     *  @since 2022/1/11 9:15
     *  每月一号更新在职员工的工龄补贴
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSeniorityAllowanceMonthly() {
        swmEmployeeDao.updateSeniorityAllowanceMonthly();
    }

    @Override
    public JobTransferSalaryVo getTransforFromSalary(Long employeeId) {
        return swmEmployeeDao.getTransforFromSalary(employeeId);
    }

    @Override
    public int getSalaryStatus() {
        return swmEmployeeDao.getSalaryStatus();
    }

    @Override
    public void autoUpdateEducationTittle() {
        swmEmployeeDao.autoUpdateEducationTittle();
    }

    @Override
    public SwmEmployeeDTO getEmployeeByEmployeeid(Long employeeid) {
        SwmEmployee employee = Optional.ofNullable(swmEmployeeDao.getEmployeeByEmployeeid(employeeid)).orElseGet(SwmEmployee::new);
        ValidationUtil.isNull(employee.getWorkCard() ,"Employee", "id", 0);
        return swmEmployeeMapper.toDto(employee);
    }
}
