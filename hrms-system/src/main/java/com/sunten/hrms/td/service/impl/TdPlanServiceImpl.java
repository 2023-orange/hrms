package com.sunten.hrms.td.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.sunten.hrms.ac.service.impl.AcEmployeeAttendanceServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.dto.FndUserQueryCriteria;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.td.dao.*;
import com.sunten.hrms.td.domain.*;
import com.sunten.hrms.td.dto.*;
import com.sunten.hrms.td.mapper.TdPlanImplementMapper;
import com.sunten.hrms.td.service.*;
import com.sunten.hrms.td.mapper.TdPlanMapper;
import com.sunten.hrms.td.vo.SafetyTrainingDeptVo;
import com.sunten.hrms.tool.dao.ToolEmailServerDao;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.domain.ToolEmailServer;
import com.sunten.hrms.tool.dto.ToolEmailServerQueryCriteria;
import com.sunten.hrms.tool.service.ToolEmailInterfaceService;
import com.sunten.hrms.tool.service.ToolEmailServerService;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <p>
 * 培训计划表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-05-19
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdPlanServiceImpl extends ServiceImpl<TdPlanDao, TdPlan> implements TdPlanService {
    private final TdPlanDao tdPlanDao;
    private final TdPlanMapper tdPlanMapper;
    private final TdPlanImplementMapper tdPlanImplementMapper;
    private final ToolEmailServerDao toolEmailServerDao;
    private final ToolEmailInterfaceService toolEmailInterfaceService;
    private final TdPlanEmployeeDao tdPlanEmployeeDao;
    private final TdPlanImplementDeptDao tdPlanImplementDeptDao;
    private final AcEmployeeAttendanceServiceImpl acEmployeeAttendanceServiceImpl;
    private final FndUserService fndUserService;
    private final TdPlanChangeHistoryService tdPlanChangeHistoryService;
    private final TdPlanImplementService tdPlanImplementService;
    private final TdPlanEmployeeService tdPlanEmployeeService;
    private final TdPlanImplementDeptService tdPlanImplementDeptService;
    private final TdPlanImplementDao tdPlanImplementDao;
    private final TdPlanCheckMethodDao tdPlanCheckMethodDao;
    @Value("${sunten.system-name}")
    private String systemName;

    public TdPlanServiceImpl(TdPlanDao tdPlanDao, TdPlanMapper tdPlanMapper, ToolEmailServerDao toolEmailServerDao,
                             ToolEmailInterfaceService toolEmailInterfaceService, TdPlanEmployeeDao tdPlanEmployeeDao,
                             TdPlanImplementDeptDao tdPlanImplementDeptDao,AcEmployeeAttendanceServiceImpl acEmployeeAttendanceServiceImpl
                             ,FndUserService fndUserService, TdPlanChangeHistoryService tdPlanChangeHistoryService, TdPlanImplementService tdPlanImplementService,
                             TdPlanEmployeeService tdPlanEmployeeService, TdPlanImplementDeptService tdPlanImplementDeptService,TdPlanImplementDao tdPlanImplementDao,
                             TdPlanImplementMapper tdPlanImplementMapper, TdPlanCheckMethodDao tdPlanCheckMethodDao) {
        this.tdPlanDao = tdPlanDao;
        this.tdPlanMapper = tdPlanMapper;
        this.tdPlanImplementMapper = tdPlanImplementMapper;
        this.toolEmailServerDao = toolEmailServerDao;
        this.toolEmailInterfaceService= toolEmailInterfaceService;
        this.tdPlanEmployeeDao = tdPlanEmployeeDao;
        this.tdPlanImplementDeptDao = tdPlanImplementDeptDao;
        this.acEmployeeAttendanceServiceImpl = acEmployeeAttendanceServiceImpl;
        this.fndUserService = fndUserService;
        this.tdPlanChangeHistoryService = tdPlanChangeHistoryService;
        this.tdPlanImplementService = tdPlanImplementService;
        this.tdPlanEmployeeService = tdPlanEmployeeService;
        this.tdPlanImplementDeptService = tdPlanImplementDeptService;
        this.tdPlanImplementDao = tdPlanImplementDao;
        this.tdPlanCheckMethodDao = tdPlanCheckMethodDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdPlanDTO insert(TdPlan planNew) {
        tdPlanDao.insertAllColumn(planNew);
        return tdPlanMapper.toDto(planNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdPlan plan = new TdPlan();
        plan.setId(id);
        this.delete(plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdPlan plan) {
        // TODO    确认删除前是否需要做检查
        tdPlanDao.deleteByEntityKey(plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdPlan planNew) {
        TdPlan planInDb = Optional.ofNullable(tdPlanDao.getByKey(planNew.getId())).orElseGet(TdPlan::new);
        ValidationUtil.isNull(planInDb.getId() ,"Plan", "id", planNew.getId());
        planNew.setId(planInDb.getId());
        tdPlanDao.updateAllColumnByKey(planNew);
        ToolEmailInterface toolEmailInterface = new ToolEmailInterface();
        acEmployeeAttendanceServiceImpl.getServer(toolEmailInterface);
        toolEmailInterface.setMailSubject("培训项目通知 - " + systemName);
        toolEmailInterface.setStatus("PLAN");
        toolEmailInterface.setPlannedDate( LocalDateTime.now());
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/td/planCharge.ftl");
        Dict dict = Dict.create();
        dict.set("planName", planNew.getTrainingName());
        if (null != planNew.getDeptChargeId()) {
            // 获取邮箱
            FndUserQueryCriteria fndUserQueryCriteria = new FndUserQueryCriteria();
            fndUserQueryCriteria.setEnabled(true);
            fndUserQueryCriteria.setEmployeeId(planNew.getDeptChargeId());
            List<FndUserDTO> fndUsers = fndUserService.listAll(fndUserQueryCriteria);
            if (fndUsers.size() > 0 && null != fndUsers.get(0).getEmail() && !fndUsers.get(0).getEmail().equals("")) {
                toolEmailInterface.setSendTo(fndUsers.get(0).getEmail());
                dict.set("mailType", "dept");
                toolEmailInterface.setMailContent(template.render(dict));
                toolEmailInterfaceService.insert(toolEmailInterface);
            }
        }
        if (null != planNew.getPlanChargeId()) {
            FndUserQueryCriteria fndUserQueryCriteria = new FndUserQueryCriteria();
            fndUserQueryCriteria.setEnabled(true);
            fndUserQueryCriteria.setEmployeeId(planNew.getPlanChargeId());
            List<FndUserDTO> fndUsers = fndUserService.listAll(fndUserQueryCriteria);
            if (fndUsers.size() > 0 && null != fndUsers.get(0).getEmail() && !fndUsers.get(0).getEmail().equals("")) {
                // 发送邮件通知计划负责人
                toolEmailInterface.setSendTo(fndUsers.get(0).getEmail());
                dict.set("mailType", "plan");
                toolEmailInterface.setMailContent(template.render(dict));
                toolEmailInterfaceService.insert(toolEmailInterface);
            }
        }
        if (null != planNew.getPassFlag()) {
            TdPlanChangeHistory tdPlanChangeHistory = new TdPlanChangeHistory();
            tdPlanChangeHistory.setParentId(planNew.getId());
            tdPlanChangeHistory.setPassFlag(planNew.getPassFlag());
            tdPlanChangeHistoryService.updatePassOrNotPass(tdPlanChangeHistory);
        }
    }

    @Override
    public TdPlanDTO getByKey(Long id) {
        TdPlan plan = Optional.ofNullable(tdPlanDao.getByKey(id)).orElseGet(TdPlan::new);
        ValidationUtil.isNull(plan.getId() ,"Plan", "id", id);
        return tdPlanMapper.toDto(plan);
    }

    @Override
    public List<TdPlanDTO> listAll(TdPlanQueryCriteria criteria) {
        return tdPlanMapper.toDto(tdPlanDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdPlanQueryCriteria criteria, Pageable pageable) {
        Page<TdPlan> page = PageUtil.startPage(pageable);
        List<TdPlan> plans = tdPlanDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdPlanMapper.toDto(plans), page.getTotal());
    }

    @Override
    public void download(List<TdPlanDTO> planDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdPlanDTO planDTO : planDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("培训名称", planDTO.getTrainingName());
            map.put("培训方式", planDTO.getTrainingMethod());
            map.put("培训内容", planDTO.getTrainingContent());
            map.put("培训目的", planDTO.getTrainingPurpose());
            map.put("培训级别", planDTO.getTrainingLevel());
            map.put("专业分类", planDTO.getProfessionClassify());
            map.put("所属部门", planDTO.getDependenceDept());
            map.put("主办部门", planDTO.getHostDept());
            map.put("讲师", planDTO.getTeacher());
            map.put("参加人员", planDTO.getEmployeeDescribes());
            map.put("时间", planDTO.getPlanDate());
            map.put("参训人数", planDTO.getEmployeeQuantity());
            map.put("预算(元)", planDTO.getPlanMoney());
            map.put("是否线上审批", planDTO.getOnlineFlag());
            map.put("备注", planDTO.getRemark());
            map.put("有效标记", planDTO.getEnabledFlag());
            map.put("计划进度(实施、作废、取消)", planDTO.getPlanStatus());
            map.put("id", planDTO.getId());
            map.put("创建时间", planDTO.getCreateTime());
            map.put("创建人ID", planDTO.getCreateBy());
            map.put("修改时间", planDTO.getUpdateTime());
            map.put("修改人ID", planDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void interfaceToMain(Long groupId) {
        tdPlanDao.interfaceToMain(groupId);
    }


    @Override
    public void interfaceToMainByObj(TdPlanInterface tdPlanInterface) {
        tdPlanDao.interfaceToMainByObj(tdPlanInterface);
    }

    @Override
    public TdPlanDTO getPlanAndImpementByPlanId(Long planId) {
        // 获取参训人员
        TdPlan tdPlan = tdPlanDao.getPlanAndImpementByPlanId(planId);
        if (null != tdPlan && null !=  tdPlan.getId() && tdPlan.getPlanImplement() != null && tdPlan.getPlanImplement().getId() != null) {
            setPlanPerson(tdPlan);
            setPlanDept(tdPlan);
        }
        if (null != tdPlan) {
            // 设置对象，防止前台报错
            if (null == tdPlan.getInEmpList()) {
                tdPlan.setInEmpList(new ArrayList<>());
                tdPlan.setEmployeesShow(new ArrayList<>());
            }
            if (null == tdPlan.getInTeacherList()) {
                tdPlan.setInTeacherList(new ArrayList<>());
                tdPlan.setTeacherShow(new ArrayList<>());
            }
            if (null == tdPlan.getInDeptList()) {
                tdPlan.setInDeptList(new ArrayList<>());
                tdPlan.setDeptShow(new ArrayList<>());
            }
        }


        return tdPlanMapper.toDto(tdPlan);
    }

    @Override
    public TdPlanDTO getPlanAndImpementByOaOrder(String oaOrder) {
        TdPlan tdPlan = tdPlanDao.getPlanAndImpementByOaOrder(oaOrder);
        if (null != tdPlan && null !=  tdPlan.getId() && tdPlan.getPlanImplement() != null && tdPlan.getPlanImplement().getId() != null) {
            setPlanPerson(tdPlan);
            setPlanDept(tdPlan);
        }
        return tdPlanMapper.toDto(tdPlan);
    }


    private void setPlanDept(TdPlan tdPlan) {
        TdPlanImplementDeptQueryCriteria tdPlanImplementDeptQueryCriteria = new TdPlanImplementDeptQueryCriteria();
        tdPlanImplementDeptQueryCriteria.setEnabledFlag(true);
        tdPlanImplementDeptQueryCriteria.setPlanImplementId(tdPlan.getPlanImplement().getId());
        List<TdPlanImplementDept> tdPlanImplementDepts = tdPlanImplementDeptDao.listAllByCriteria(tdPlanImplementDeptQueryCriteria);
        List<TdPlanImplementDept> inDeptList = new ArrayList<>();
        if (null != tdPlanImplementDepts && tdPlanImplementDepts.size() > 0) {
            List<List<TdPlanImplementDept>> deptShow = new ArrayList<>();
            List<TdPlanImplementDept> row = new ArrayList<>();
            for (int i = 0; i < tdPlanImplementDepts.size(); i++) {
                if (i % 5 == 0 && i != 0) {
                    deptShow.add(row);
                    row = new ArrayList<>();
                }
                row.add(tdPlanImplementDepts.get(i));
                inDeptList.add(tdPlanImplementDepts.get(i));
            }
            if (row.size() > 0) {
                deptShow.add(row);
            }
            tdPlan.setInDeptList(inDeptList);
            tdPlan.setDeptShow(deptShow);
        }
    }

    private void setPlanPerson(TdPlan tdPlan) {
        TdPlanEmployeeQueryCriteria tdPlanEmployeeQueryCriteria = new TdPlanEmployeeQueryCriteria();
        tdPlanEmployeeQueryCriteria.setType("employee");
        tdPlanEmployeeQueryCriteria.setPlanId(tdPlan.getId());
        tdPlanEmployeeQueryCriteria.setPlanImplementId(tdPlan.getPlanImplement().getId());
        List<TdPlanEmployee> inEmployeeList = new ArrayList<>();
        List<TdPlanEmployee> inTeacherList = new ArrayList<>();
        List<TdPlanEmployee> employees = tdPlanEmployeeDao.listAllByCriteria(tdPlanEmployeeQueryCriteria);
        if (null != employees && employees.size() > 0) {
            List<List<TdPlanEmployee>> employeeShow = setEmpListByPlan(inEmployeeList, employees);
            tdPlan.setInEmpList(inEmployeeList);
            tdPlan.setEmployeesShow(employeeShow);
        }
        tdPlanEmployeeQueryCriteria.setType("teacher");
        List<TdPlanEmployee> teachers = tdPlanEmployeeDao.listAllByCriteria(tdPlanEmployeeQueryCriteria);
        if (null != teachers && teachers.size() > 0) {
            List<List<TdPlanEmployee>> teacherShow = setEmpListByPlan(inTeacherList, teachers);
            tdPlan.setInTeacherList(inTeacherList);
            tdPlan.setTeacherShow(teacherShow);
        }
    }

    private List<List<TdPlanEmployee>> setEmpListByPlan(List<TdPlanEmployee> inEmployeeList, List<TdPlanEmployee> employees) {
        List<List<TdPlanEmployee>> employeeShow = new ArrayList<>();
        List<TdPlanEmployee> row = new ArrayList<>();
        for (int i = 0; i < employees.size(); i++) {
            if (i % 5 == 0 && i != 0) {
                employeeShow.add(row);
                row = new ArrayList<>();
            }
            row.add(employees.get(i));
            inEmployeeList.add(employees.get(i));
        }
        if (row.size() > 0) {
            employeeShow.add(row);
        }
        return employeeShow;
    }

    @Override
    public TdPlanDTO getPlanByChangeOaOrder(String changeOaOrder) {
        return tdPlanMapper.toDto(tdPlanDao.getPlanByChangeOaOrder(changeOaOrder));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShowFlagAfterImplementPass(Long id) {
        tdPlanDao.setShowFlagAfterImplementPass(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdPlanDTO insertPlanAndImplementAndOther(TdPlan plan) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        TdPlan currentTdPlan = null;
        TdPlanImplementDTO tdPlanImplementDTO = null;
        if (plan.getCommitType().equals("commit") || plan.getCommitType().equals("reCommit")) {
            if (plan.getOnlineFlag()) {
                plan.setPlanStatus("计划实施审批中");
            } else {
                //当培训费用为0元时，不需要走OA审批流程。
                //所以把td_plan数据表的字段PlanStatus设置为"实施审批通过"
                //所以把td_plan_implement数据表的字段ApprovalStatus设置为"已完成"
                plan.setPlanStatus("实施审批通过");
                plan.getPlanImplement().setApprovalStatus("已完成");
            }
        } else {
            plan.setPlanStatus("存在实施未提交");
        }
        if (plan.getId() == -1){ // 新增加计划及实施
            currentTdPlan = tdPlanMapper.toEntity(insert(plan));
            plan.getPlanImplement().setPlanId(currentTdPlan.getId());
            tdPlanImplementDTO = tdPlanImplementService.insert(plan.getPlanImplement());
            if (null != plan.getCheckMethodList()) {
                this.insertOrUpdateTdPlanCheckMethod(plan.getCheckMethodList(), tdPlanImplementDTO.getId(), true);
            }
        } else { // 更新计划
            tdPlanDao.updateAllColumnByKey(plan);
            plan.getPlanImplement().setPlanId(plan.getId());
            if (plan.getPlanImplement().getId() == -1) { // 新增实施
                tdPlanImplementDTO = tdPlanImplementService.insert(plan.getPlanImplement());
                if (null != plan.getCheckMethodList()) {
                    this.insertOrUpdateTdPlanCheckMethod(plan.getCheckMethodList(), tdPlanImplementDTO.getId(), true);
                }
            } else { // 更新实施
                tdPlanImplementService.update(plan.getPlanImplement());
                tdPlanImplementDTO = tdPlanImplementMapper.toDto(tdPlanImplementDao.selectByPlanId(plan.getId()).get(0));
                if (null != plan.getCheckMethodList()) {
                    this.insertOrUpdateTdPlanCheckMethod(plan.getCheckMethodList(), tdPlanImplementDTO.getId(), false);
                }
            }
            currentTdPlan = tdPlanDao.getByKey(plan.getId());
        }
        if ((plan.getCommitType().equals("commit") || plan.getCommitType().equals("reCommit")) && plan.getOnlineFlag()) { // 写入一条历史
            TdPlanChangeHistory tdPlanChangeHistory = new TdPlanChangeHistory();
            tdPlanChangeHistory.setChangeType("implement");
            tdPlanChangeHistory.setOaOrder(plan.getChangeOaOrder());
            tdPlanChangeHistory.setParentId(plan.getId());
            tdPlanChangeHistoryService.insert(tdPlanChangeHistory);
        }

        // 插入培训人员
        TdPlanEmployeeQueryCriteria tdPlanEmployeeQueryCriteria = new TdPlanEmployeeQueryCriteria();
        tdPlanEmployeeQueryCriteria.setType("employee");
        tdPlanEmployeeQueryCriteria.setPlanImplementId(tdPlanImplementDTO.getId());
        List<Long> oldEmployeeList = tdPlanEmployeeDao.listAllByCriteria(tdPlanEmployeeQueryCriteria).stream().map(TdPlanEmployee::getEmployeeId).collect(Collectors.toList());

        List<Long> getEmployeeList = plan.getInEmpList().stream().map(TdPlanEmployee::getEmployeeId).collect(Collectors.toList());
        ListUtils.listComp(oldEmployeeList, getEmployeeList);
        TdPlanEmployee tdPlanEmployee = new TdPlanEmployee();
        tdPlanEmployee.setPlanImplementId(tdPlanImplementDTO.getId());
        tdPlanEmployee.setType("employee");
        oldEmployeeList.forEach((id) -> {
            tdPlanEmployee.setEmployeeId(id);
            tdPlanEmployee.setUpdateBy(user.getId());
            tdPlanEmployee.setUpdateTime(LocalDateTime.now());
            tdPlanEmployeeService.deleteByEnabled(tdPlanEmployee);
        });
        getEmployeeList.forEach((id) -> {
            tdPlanEmployee.setEmployeeId(id);
            tdPlanEmployee.setUpdateBy(user.getId());
            tdPlanEmployee.setUpdateTime(LocalDateTime.now());
            tdPlanEmployee.setEnabledFlag(true);
            tdPlanEmployeeService.insert(tdPlanEmployee);
        });
        currentTdPlan.setInEmpList(tdPlanEmployeeDao.listAllByCriteria(tdPlanEmployeeQueryCriteria));

        // 插入讲师
        tdPlanEmployeeQueryCriteria.setType("teacher");
        List<Long> oldTeacherList = tdPlanEmployeeService.listAll(tdPlanEmployeeQueryCriteria).stream().map(TdPlanEmployeeDTO::getEmployeeId).collect(Collectors.toList());
        List<Long> getTeacherList = plan.getInTeacherList().stream().map(TdPlanEmployee::getEmployeeId).collect(Collectors.toList());
        ListUtils.listComp(oldTeacherList, getTeacherList);
        TdPlanEmployee tdPlanTeacher = new TdPlanEmployee();
        tdPlanTeacher.setPlanImplementId(tdPlanImplementDTO.getId());
        tdPlanTeacher.setType("teacher");
        oldTeacherList.forEach((id) -> {
            tdPlanTeacher.setEmployeeId(id);
            tdPlanTeacher.setUpdateBy(user.getId());
            tdPlanTeacher.setUpdateTime(LocalDateTime.now());
            tdPlanEmployeeService.deleteByEnabled(tdPlanTeacher);
        });
        getTeacherList.forEach((id) -> {
            tdPlanTeacher.setEmployeeId(id);
            tdPlanTeacher.setUpdateBy(user.getId());
            tdPlanTeacher.setUpdateTime(LocalDateTime.now());
            tdPlanTeacher.setEnabledFlag(true);
            tdPlanEmployeeService.insert(tdPlanTeacher);
        });
        currentTdPlan.setInTeacherList(tdPlanEmployeeDao.listAllByCriteria(tdPlanEmployeeQueryCriteria));

        // 插入部门
        TdPlanImplementDeptQueryCriteria tdPlanImplementDeptQueryCriteria = new TdPlanImplementDeptQueryCriteria();
        tdPlanImplementDeptQueryCriteria.setEnabledFlag(true);
        tdPlanImplementDeptQueryCriteria.setPlanImplementId(tdPlanImplementDTO.getId());
        List<Long> oldDeptList = tdPlanImplementDeptService.listAll(tdPlanImplementDeptQueryCriteria).stream().map(TdPlanImplementDeptDTO::getDeptId).collect(Collectors.toList());
        List<Long> getDeptList = plan.getInDeptList().stream().map(TdPlanImplementDept::getDeptId).collect(Collectors.toList());
        ListUtils.listComp(oldDeptList, getDeptList);
        TdPlanImplementDept tdPlanImplementDept = new TdPlanImplementDept();
        tdPlanImplementDept.setPlanImplementId(tdPlanImplementDTO.getId());
        tdPlanImplementDept.setEnabledFlag(true);
        oldDeptList.forEach((id) -> {
            tdPlanImplementDept.setDeptId(id);
            tdPlanImplementDept.setUpdateBy(user.getId());
            tdPlanImplementDept.setUpdateTime(LocalDateTime.now());
            tdPlanImplementDeptService.deleteByEnabled(tdPlanImplementDept);
        });
        getDeptList.forEach((id) -> {
            tdPlanImplementDept.setDeptId(id);
            tdPlanImplementDept.setUpdateBy(user.getId());
            tdPlanImplementDept.setCreateBy(user.getId());
            tdPlanImplementDeptService.insertByImplement(tdPlanImplementDept);
        });
        currentTdPlan.setInDeptList(tdPlanImplementDeptDao.listAllByCriteria(tdPlanImplementDeptQueryCriteria));

        // 设置考核方式
        TdPlanCheckMethodQueryCriteria tdPlanCheckMethodQueryCriteria = new TdPlanCheckMethodQueryCriteria();
        tdPlanCheckMethodQueryCriteria.setPlanImplementId(tdPlanImplementDTO.getId());
        tdPlanCheckMethodQueryCriteria.setEnabledFlag(true);

        List<TdPlanCheckMethod> tdPlanCheckMethods = tdPlanCheckMethodDao.listAllByCriteria(tdPlanCheckMethodQueryCriteria);
        currentTdPlan.setCheckMethodList(tdPlanCheckMethods);
        // 设置 method1，method2的行样式
        currentTdPlan.getPlanImplement().setCheckMethod(
                tdPlanCheckMethods.stream().map(TdPlanCheckMethod::getCheckMethod).collect(Collectors.joining(",")));
        return tdPlanMapper.toDto(currentTdPlan);
    }

    /**
     *  @author liangjw
     *  @since 2022/3/8 14:57
     *  插入或更新考核方式
     */
    private void insertOrUpdateTdPlanCheckMethod(List<TdPlanCheckMethod> tdPlanCheckMethods, Long planImplementId,Boolean insertFlag) {
        if (insertFlag) {
            for (TdPlanCheckMethod tdPlanCheckMethod : tdPlanCheckMethods
                 ) {
                tdPlanCheckMethod.setPlanImplementId(planImplementId);
                tdPlanCheckMethodDao.insertAllColumn(tdPlanCheckMethod);
            }
        } else {
            TdPlanCheckMethodQueryCriteria tdPlanCheckMethodQueryCriteria = new TdPlanCheckMethodQueryCriteria();
            tdPlanCheckMethodQueryCriteria.setEnabledFlag(true);
            tdPlanCheckMethodQueryCriteria.setPlanImplementId(planImplementId);
            List<TdPlanCheckMethod> checkMethodList = tdPlanCheckMethodDao.listAllByCriteria(tdPlanCheckMethodQueryCriteria);
            List<String> oldList = checkMethodList.stream().map(TdPlanCheckMethod::getCheckMethod).collect(Collectors.toList());
            List<String> newList = tdPlanCheckMethods.stream().map(TdPlanCheckMethod::getCheckMethod).collect(Collectors.toList());
            ListUtils.listCompForStr(oldList, newList);
            // old删除， new 增加
            TdPlanCheckMethod tdPlanCheckMethod = new TdPlanCheckMethod();
            oldList.forEach((method) -> {
                tdPlanCheckMethodDao.deleteByMethodAndEnabledFlag(method, planImplementId, SecurityUtils.getUserId());
            });
            tdPlanCheckMethod.setPlanImplementId(planImplementId);
            tdPlanCheckMethod.setEnabledFlag(true);
            newList.forEach((method) -> {
                tdPlanCheckMethod.setCheckMethod(method);
                tdPlanCheckMethodDao.insertAllColumn(tdPlanCheckMethod);
            });
        }
    }

    @Override
    public List<TdSafetyTraining> listSafetyTrainingByCriteria(TdPlanQueryCriteria criteria) {
        return tdPlanDao.getSafetyTrainingByCriteria(criteria);
    }

    @Override
    public Map<String, Object> listSafetyTrainingByCriteriaPage(TdPlanQueryCriteria criteria, Pageable pageable) {
        Page<TdSafetyTraining> page = PageUtil.startPage(pageable);
        List<TdSafetyTraining> tdSafetyTrainings = tdPlanDao.getSafetyTrainingByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdSafetyTrainings, page.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSafetyTraining(TdSafetyTraining tdSafetyTraining) {
        tdSafetyTraining.setCreateBy(SecurityUtils.getUserId());
        tdSafetyTraining.setUpdateTime(LocalDateTime.now());
        tdSafetyTraining.setUpdateBy(SecurityUtils.getUserId());
        tdSafetyTraining.setCreateTime(LocalDateTime.now());
        tdPlanDao.insertSafetyTraining(tdSafetyTraining);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSafetyTraining(TdSafetyTraining tdSafetyTraining) {
        tdSafetyTraining.setUpdateTime(LocalDateTime.now());
        tdSafetyTraining.setUpdateBy(SecurityUtils.getUserId());
        tdPlanDao.updateSafetyTraining(tdSafetyTraining);
    }

    @Override
    public Set<SafetyTrainingDeptVo> getSafetyTrainingDeptVo() {
        List<TdSafetyTrainingDept> tdSafetyTrainingDepts = tdPlanDao.getSafetyTrainingDept();
        Set<SafetyTrainingDeptVo> safetyTrainingDeptVos = new HashSet<>();
        Set<SafetyTrainingDeptVo> childSet = new HashSet<>();
        String nameTarget = "";
        SafetyTrainingDeptVo safetyTrainingDeptVo = new SafetyTrainingDeptVo();
        for (TdSafetyTrainingDept tdSafetyTrainingDept: tdSafetyTrainingDepts
             ) {
            if (tdSafetyTrainingDept.getDeptName().equals(nameTarget)) {
                SafetyTrainingDeptVo son = new SafetyTrainingDeptVo();
                son.setDeptCode((tdSafetyTrainingDept.getDeptName() + ".") + (null != tdSafetyTrainingDept.getDepartment() ? tdSafetyTrainingDept.getDepartment() : "NULL"));
                son.setDeptName(tdSafetyTrainingDept.getDeptName());
                childSet.add(son);
            } else {
                safetyTrainingDeptVo.setSafetyTrainingDeptVoSet(childSet);
                if (!"".equals(nameTarget)) {
                    safetyTrainingDeptVos.add(safetyTrainingDeptVo);
                }
                nameTarget = tdSafetyTrainingDept.getDeptName();
                safetyTrainingDeptVo = new SafetyTrainingDeptVo();
                safetyTrainingDeptVo.setDeptName(tdSafetyTrainingDept.getDeptName());
                safetyTrainingDeptVo.setDeptCode(tdSafetyTrainingDept.getDeptName());
                childSet = new HashSet<>();
                if (null != tdSafetyTrainingDept.getDepartment()) {
                    SafetyTrainingDeptVo son = new SafetyTrainingDeptVo();
                    son.setDeptCode((tdSafetyTrainingDept.getDeptName() + ".") + (null != tdSafetyTrainingDept.getDepartment() ? tdSafetyTrainingDept.getDepartment() : "NULL"));
                    son.setDeptName(tdSafetyTrainingDept.getDeptName());
                    childSet.add(son);
                }
            }
        }
        safetyTrainingDeptVo.setSafetyTrainingDeptVoSet(childSet);
        safetyTrainingDeptVos.add(safetyTrainingDeptVo);
        return safetyTrainingDeptVos;
    }
}
