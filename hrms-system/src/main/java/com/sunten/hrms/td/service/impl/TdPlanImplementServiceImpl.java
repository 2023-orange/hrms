package com.sunten.hrms.td.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.sunten.hrms.ac.dao.AcEmpDeptsDao;
import com.sunten.hrms.ac.service.impl.AcEmployeeAttendanceServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndUserDao;
import com.sunten.hrms.fnd.vo.DeptChargeEmail;
import com.sunten.hrms.pm.dao.PmEmployeeDao;
import com.sunten.hrms.td.dao.TdPlanEmployeeDao;
import com.sunten.hrms.td.domain.TdPlanEmployee;
import com.sunten.hrms.td.domain.TdPlanImplement;
import com.sunten.hrms.td.dao.TdPlanImplementDao;
import com.sunten.hrms.td.dto.TdPlanEmployeeQueryCriteria;
import com.sunten.hrms.td.service.TdPlanImplementService;
import com.sunten.hrms.td.dto.TdPlanImplementDTO;
import com.sunten.hrms.td.dto.TdPlanImplementQueryCriteria;
import com.sunten.hrms.td.mapper.TdPlanImplementMapper;
import com.sunten.hrms.td.service.TdPlanService;
import com.sunten.hrms.tool.dao.ToolEmailInterfaceDao;
import com.sunten.hrms.tool.dao.ToolEmailServerDao;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.domain.ToolEmailServer;
import com.sunten.hrms.tool.dto.ToolEmailServerQueryCriteria;
import com.sunten.hrms.tool.service.ToolEmailInterfaceService;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
 * 计划实施申请 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-05-25
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdPlanImplementServiceImpl extends ServiceImpl<TdPlanImplementDao, TdPlanImplement> implements TdPlanImplementService {
    private final TdPlanImplementDao tdPlanImplementDao;
    private final TdPlanImplementMapper tdPlanImplementMapper;
    private final TdPlanEmployeeDao tdPlanEmployeeDao;
    private final PmEmployeeDao pmEmployeeDao;
    private final AcEmpDeptsDao acEmpDeptsDao;
    private final ToolEmailInterfaceService toolEmailInterfaceService;
    private final AcEmployeeAttendanceServiceImpl acEmployeeAttendanceServiceImpl;
    private final FndUserDao fndUserDao;
    @Value("${role.authTrainCharge}")
    private String authTrainCharge;
    @Value("${sunten.system-name}")
    private String systemName;

    public TdPlanImplementServiceImpl(TdPlanImplementDao tdPlanImplementDao, TdPlanImplementMapper tdPlanImplementMapper, ToolEmailServerDao toolEmailServerDao,
                                      TdPlanEmployeeDao tdPlanEmployeeDao, ToolEmailInterfaceDao toolEmailInterfaceDao,ToolEmailInterfaceService toolEmailInterfaceService,
                                      PmEmployeeDao pmEmployeeDao, AcEmpDeptsDao acEmpDeptsDao, AcEmployeeAttendanceServiceImpl acEmployeeAttendanceServiceImpl,
                                      FndUserDao fndUserDao) {
        this.tdPlanImplementDao = tdPlanImplementDao;
        this.tdPlanImplementMapper = tdPlanImplementMapper;
        this.tdPlanEmployeeDao = tdPlanEmployeeDao;
        this.pmEmployeeDao = pmEmployeeDao;
        this.acEmpDeptsDao = acEmpDeptsDao;
        this.toolEmailInterfaceService = toolEmailInterfaceService;
        this.acEmployeeAttendanceServiceImpl = acEmployeeAttendanceServiceImpl;
        this.fndUserDao = fndUserDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdPlanImplementDTO insert(TdPlanImplement planImplementNew) {
        tdPlanImplementDao.insertAllColumn(planImplementNew);
        return tdPlanImplementMapper.toDto(planImplementNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdPlanImplement planImplement = new TdPlanImplement();
        planImplement.setId(id);
        this.delete(planImplement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdPlanImplement planImplement) {
        // TODO    确认删除前是否需要做检查
        tdPlanImplementDao.deleteByEntityKey(planImplement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdPlanImplement planImplementNew) {
        TdPlanImplement planImplementInDb = Optional.ofNullable(tdPlanImplementDao.getByKey(planImplementNew.getId())).orElseGet(TdPlanImplement::new);
        ValidationUtil.isNull(planImplementInDb.getId() ,"PlanImplement", "id", planImplementNew.getId());
        planImplementNew.setId(planImplementInDb.getId());
        tdPlanImplementDao.updateAllColumnByKey(planImplementNew);
    }

    @Override
    public TdPlanImplementDTO getByKey(Long id) {
        TdPlanImplement planImplement = Optional.ofNullable(tdPlanImplementDao.getByKey(id)).orElseGet(TdPlanImplement::new);
        ValidationUtil.isNull(planImplement.getId() ,"PlanImplement", "id", id);
        return tdPlanImplementMapper.toDto(planImplement);
    }

    @Override
    public List<TdPlanImplementDTO> listAll(TdPlanImplementQueryCriteria criteria) {
        return tdPlanImplementMapper.toDto(tdPlanImplementDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdPlanImplementQueryCriteria criteria, Pageable pageable) {
        Page<TdPlanImplement> page = PageUtil.startPage(pageable);
        List<TdPlanImplement> planImplements = tdPlanImplementDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdPlanImplementMapper.toDto(planImplements), page.getTotal());
    }

    @Override
    public void download(List<TdPlanImplementDTO> planImplementDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdPlanImplementDTO planImplementDTO : planImplementDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("培训计划id", planImplementDTO.getPlanId());
            map.put("起始培训时间", planImplementDTO.getBeginDate());
            map.put("培训结束时间", planImplementDTO.getEndDate());
            map.put("培训时长", planImplementDTO.getTrainingTimeQuantity());
            map.put("培训地址", planImplementDTO.getTrainingAddress());
            map.put("考核方式", planImplementDTO.getCheckMethod());
            map.put("培训费用", planImplementDTO.getTrainingMoney());
            map.put("申请人的empId", planImplementDTO.getRequestBy());
            map.put("申请日期", planImplementDTO.getRequestDate());
            map.put("当前审批节点", planImplementDTO.getCurrentNode());
            map.put("当前审批人", planImplementDTO.getCurrentPerson());
            map.put("OA单号", planImplementDTO.getOaOrder());
            map.put("有效标记", planImplementDTO.getEnabledFlag());
            map.put("申请状态", planImplementDTO.getApprovalStatus());
            map.put("外部讲师", planImplementDTO.getOutTeacher());
            map.put("外部参训人员", planImplementDTO.getOutEmp());
            map.put("id", planImplementDTO.getId());
            map.put("创建时间", planImplementDTO.getCreateTime());
            map.put("创建人ID", planImplementDTO.getCreateBy());
            map.put("修改时间", planImplementDTO.getUpdateTime());
            map.put("修改人ID", planImplementDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendEmailWithImplementId(Long implementId) {
        // 获取根据实施id, 获取实施内容
        TdPlanImplement tdPlanImplement = tdPlanImplementDao.getByKey(implementId);
        ToolEmailInterface toolEmailInterface = new ToolEmailInterface();
        acEmployeeAttendanceServiceImpl.getServer(toolEmailInterface);
        // 获取人员名单
        TdPlanEmployeeQueryCriteria tdPlanEmployeeQueryCriteria = new TdPlanEmployeeQueryCriteria();
        tdPlanEmployeeQueryCriteria.setPlanImplementId(implementId);
        List<TdPlanEmployee> tdPlanEmployees = tdPlanEmployeeDao.listAllByCriteria(tdPlanEmployeeQueryCriteria);
        // 先区分讲师及有邮箱人员及无邮箱的人员
        List<TdPlanEmployee> employeeHaveEmailList = tdPlanEmployees.stream().filter(TdPlanEmployee -> TdPlanEmployee.getType().equals("employee") && null != TdPlanEmployee.getEmail()).collect(Collectors.toList());
        List<TdPlanEmployee> notHaveEmailList = tdPlanEmployees.stream().filter(TdPlanEmployee -> null == TdPlanEmployee.getEmail() && !TdPlanEmployee.getType().equals("teacher")).collect(Collectors.toList());
        List<TdPlanEmployee> teacherEmailList = tdPlanEmployees.stream().filter(TdPlanEmployee -> TdPlanEmployee.getType().equals("teacher")).collect(Collectors.toList());
        StringBuilder teacherStr = new StringBuilder("");
        boolean flag = false;
        for (TdPlanEmployee teacher : teacherEmailList
             ) {
            if (!flag) {
                teacherStr.append(teacher.getEmployeeName());
                flag = true;
            } else {
                teacherStr.append(",").append(teacher.getEmployeeName());
            }
        }
        if (null != tdPlanImplement.getOutTeacher() && !tdPlanImplement.getOutTeacher().equals("")) {
            if (!teacherStr.toString().equals("")) {
                teacherStr.append(",").append(tdPlanImplement.getOutTeacher());
            } else {
                teacherStr.append(tdPlanImplement.getOutTeacher());
            }
        }
        // 先处理个人有邮箱
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/td/planImplementEmail.ftl");
        toolEmailInterface.setStatus("PLAN");
        toolEmailInterface.setPlannedDate(LocalDateTime.now());
        Dict dict = new Dict();
        dict.set("teacher", teacherStr.toString());
        dict.set("trainingName", tdPlanImplement.getPlan().getTrainingName());
        dict.set("trainingDate", DateUtil.localDateToStr(tdPlanImplement.getBeginDate()) + "至" + DateUtil.localDateToStr(tdPlanImplement.getEndDate()));
        dict.set("trainingAddress", tdPlanImplement.getTrainingAddress());
        dict.set("trainingContent", tdPlanImplement.getPlan().getTrainingContent());
        toolEmailInterface.setMailSubject("培训通知 - " + systemName);
        for (TdPlanEmployee tdPlanEmployeeHaveEmail : employeeHaveEmailList
             ) {
            if (tdPlanEmployeeHaveEmail.getType().equals("employee")){
                // 员工的邮件内容
                dict.set("nameList", new ArrayList<>());
                toolEmailInterface.setMailContent(template.render(dict));
                toolEmailInterface.setSendTo(tdPlanEmployeeHaveEmail.getEmail());
                toolEmailInterfaceService.insert(toolEmailInterface);
            }
        }

        // 个人的通知邮件发送完毕, 开始处理个人无邮箱的
        List<String> tdPlanNameList = new ArrayList<>();
        Map<String, List<String>> finalSendTarget = new HashMap<>();
        // 处理人员的邮件发送， 根据deptId对employeeList进行分组
        Map<Long, List<TdPlanEmployee>> employeeDeptEmailMap = new HashMap<>();
        notHaveEmailList.stream().collect(Collectors.groupingBy(TdPlanEmployee::getDeptId, Collectors.toList())).forEach(employeeDeptEmailMap::put);
        employeeDeptEmailMap.forEach((x, y) -> {
            // 根据deptId， 获取班长、主管、经理
            Boolean teamHaveMailFlag = true;
            List<String> temp = new ArrayList<>();
            for (TdPlanEmployee superiorManagerEmp : y
            ) {
                temp.add(superiorManagerEmp.getEmployeeName());
            }
            if (y.get(0).getWorkshopAttendanceFlag()){
                String teamEmail = acEmpDeptsDao.getTeamEmailByDeptId(x);
                if (null == teamEmail || "".equals(teamEmail) || "@in-sunten.com".equals(teamEmail)) {
                    teamHaveMailFlag = false;
                } else {
                    sendEmail(toolEmailInterface, dict, template, temp, teamEmail);
                }
            }
            if (!teamHaveMailFlag) {
                // 没组长
                List<DeptChargeEmail> deptChargeEmails = fndUserDao.selectDeptChargeEmailList(x);
                List<DeptChargeEmail> superiorList = deptChargeEmails.stream().filter(DeptChargeEmail -> DeptChargeEmail.getJobName().equals("主管")).collect(Collectors.toList());
                if (superiorList.size() > 0) {
                    // 找到主管
                    if (!finalSendTarget.containsKey(superiorList.get(0).getEmail())) {
                        finalSendTarget.put(superiorList.get(0).getEmail(), temp);
                    } else {
                        finalSendTarget.get(superiorList.get(0).getEmail()).addAll(temp);
                    }
                } else {
                    List<DeptChargeEmail> managerList = deptChargeEmails.stream().filter(DeptChargeEmail -> DeptChargeEmail.getJobName().equals("经理")).collect(Collectors.toList());
                    if (managerList.size() > 0) {
                        // 找到经理
                        if (!finalSendTarget.containsKey(managerList.get(0).getEmail())) {
                            finalSendTarget.put(managerList.get(0).getEmail(), temp);
                        } else {
                            finalSendTarget.get(managerList.get(0).getEmail()).addAll(temp);
                        }
                    } else {
                        // 发给培训人员
                        tdPlanNameList.addAll(temp);
                    }
                }
            }
        });
        // 处理培训人员
        List<String> trainManagerEmail = fndUserDao.selectEmailListByRole(authTrainCharge);
        for (String email : trainManagerEmail
             ) {
            sendEmail(toolEmailInterface, dict, template, tdPlanNameList ,email);
        }
        for (String email : finalSendTarget.keySet()) {
            // 处理稳到上级的邮件发送
            sendEmail(toolEmailInterface, dict, template, finalSendTarget.get(email), email);
        }
        // 发送完毕
    }

    private void sendEmail(ToolEmailInterface toolEmailInterface, Dict dict,Template template, List<String> y, String superiorManagerEmail) {
        List<List<String>> nameList = new ArrayList<>();
        List<String> row = new ArrayList<>();
        for (String name : y
        ) {
            row.add(name);
            if (row.size() == 6) {
                nameList.add(row);
                row = new ArrayList<>();
            }
        }
        if (row.size() > 0) {
            nameList.add(row);
        }
        if (nameList.size() > 0) {
            dict.set("nameList", nameList);
        } else {
            dict.set("nameList", new ArrayList<>());
        }
        toolEmailInterface.setSendTo(superiorManagerEmail);
        toolEmailInterface.setMailContent(template.render(dict));
        toolEmailInterfaceService.insert(toolEmailInterface);
    }

    @Override
    public TdPlanImplementDTO getByPlanIdForTemplate(Long id) {
        return tdPlanImplementMapper.toDto(tdPlanImplementDao.getByPlanIdForTemplate(id));
    }
}
