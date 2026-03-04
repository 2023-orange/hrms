package com.sunten.hrms.ac.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcAbnormalAttendanceRecordDao;
import com.sunten.hrms.ac.dao.AcEmployeeAttendanceDao;
import com.sunten.hrms.ac.domain.AcAbnormalAttendanceRecord;
import com.sunten.hrms.ac.domain.AcEmployeeAttendance;
import com.sunten.hrms.ac.domain.AcEmployeeAttendanceCollect;
import com.sunten.hrms.ac.dto.AcEmployeeAttendanceDTO;
import com.sunten.hrms.ac.dto.AcEmployeeAttendanceQueryCriteria;
import com.sunten.hrms.ac.mapper.AcEmployeeAttendanceMapper;
import com.sunten.hrms.ac.service.AcEmployeeAttendanceService;
import com.sunten.hrms.ac.vo.AcEmployeeAttendanceVo;
import com.sunten.hrms.ac.vo.AcEmployeeVo;
import com.sunten.hrms.ac.vo.NoAttendanceDetailVo;
import com.sunten.hrms.ac.vo.NoAttendanceEmialHeader;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndDeptDao;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dao.PmEmployeeDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.dto.PmEmployeeJobDTO;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeJobService;
import com.sunten.hrms.pm.service.impl.PmEmployeeServiceImpl;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.tool.dao.ToolEmailInterfaceDao;
import com.sunten.hrms.tool.dao.ToolEmailServerDao;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.domain.ToolEmailServer;
import com.sunten.hrms.tool.dto.ToolEmailServerQueryCriteria;
import com.sunten.hrms.tool.service.ToolEmailInterfaceService;
import com.sunten.hrms.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 排班员工考勤制度关系表 服务实现类
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcEmployeeAttendanceServiceImpl extends ServiceImpl<AcEmployeeAttendanceDao, AcEmployeeAttendance> implements AcEmployeeAttendanceService {
    private final AcEmployeeAttendanceDao acEmployeeAttendanceDao;
    private final AcEmployeeAttendanceMapper acEmployeeAttendanceMapper;
    private final PmEmployeeDao pmEmployeeDao;
    private final FndUserService fndUserService;
    private final PmEmployeeJobService pmEmployeeJobService;
    private final FndDataScope fndDataScope;
    private final FndDeptService fndDeptService;
    private final AcAbnormalAttendanceRecordDao acAbnormalAttendanceRecordDao;
    private final ToolEmailServerDao toolEmailServerDao;
    private final FndDeptDao fndDeptDao;
    private final ToolEmailInterfaceService toolEmailInterfaceService;
    @Value("${sunten.system-name}")
    private String systemName;

    @Value("${sunten.role-permission.data-processor}")
    private String attendanceAdminAuthorityCode;

    public AcEmployeeAttendanceServiceImpl(AcEmployeeAttendanceDao acEmployeeAttendanceDao, AcEmployeeAttendanceMapper acEmployeeAttendanceMapper,
                                            PmEmployeeDao pmEmployeeDao, FndUserService fndUserService, PmEmployeeJobService pmEmployeeJobService,
                                            FndDataScope fndDataScope, FndDeptService fndDeptService, AcAbnormalAttendanceRecordDao acAbnormalAttendanceRecordDao,
                                            ToolEmailServerDao toolEmailServerDao, FndDeptDao fndDeptDao, ToolEmailInterfaceService toolEmailInterfaceService
    ) {
        this.acEmployeeAttendanceDao = acEmployeeAttendanceDao;
        this.acEmployeeAttendanceMapper = acEmployeeAttendanceMapper;
        this.pmEmployeeDao = pmEmployeeDao;
        this.fndUserService = fndUserService;
        this.pmEmployeeJobService = pmEmployeeJobService;
        this.fndDataScope = fndDataScope;
        this.fndDeptService = fndDeptService;
        this.acAbnormalAttendanceRecordDao = acAbnormalAttendanceRecordDao;
        this.toolEmailServerDao = toolEmailServerDao;
        this.fndDeptDao = fndDeptDao;
        this.toolEmailInterfaceService = toolEmailInterfaceService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcEmployeeAttendanceDTO insert(AcEmployeeAttendance employeeAttendanceNew) {
        if (employeeAttendanceNew.getRestDayFlag()) {
            this.setAcEmployeeAttendanceNull(employeeAttendanceNew);
        } else {
            if (this.validTime(employeeAttendanceNew)) {
                throw new InfoCheckWarningMessException("时间段不允许重叠");
            }
            if (!this.validAfterToday(employeeAttendanceNew)) {
                throw new InfoCheckWarningMessException("不允许新增今天之前的排班");
            }
        }
        acEmployeeAttendanceDao.insertAllColumn(employeeAttendanceNew);
        return acEmployeeAttendanceMapper.toDto(employeeAttendanceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AcEmployeeAttendance employeeAttendance = new AcEmployeeAttendance();
        employeeAttendance.setId(id);
        this.delete(employeeAttendance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcEmployeeAttendance employeeAttendance) {
        // TODO    确认删除前是否需要做检查
        acEmployeeAttendanceDao.deleteByEntityKey(employeeAttendance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcEmployeeAttendance employeeAttendanceNew) {
        AcEmployeeAttendance employeeAttendanceInDb = Optional.ofNullable(acEmployeeAttendanceDao.getByKey(employeeAttendanceNew.getId())).orElseGet(AcEmployeeAttendance::new);
        ValidationUtil.isNull(employeeAttendanceInDb.getId(), "EmployeeAttendance", "id", employeeAttendanceNew.getId());
        employeeAttendanceNew.setId(employeeAttendanceInDb.getId());
        if (employeeAttendanceNew.getRestDayFlag()) {
            this.setAcEmployeeAttendanceNull(employeeAttendanceNew);
        }
        if (this.validTime(employeeAttendanceNew)) {
            throw new InfoCheckWarningMessException("时间段不允许重叠");
        }
        if (!this.validAfterToday(employeeAttendanceNew)) {
            throw new InfoCheckWarningMessException("不允许更新今天之前的排班");
        }
        acEmployeeAttendanceDao.updateAllColumnByKey(employeeAttendanceNew);
    }

    @Override
    public AcEmployeeAttendanceDTO getByKey(Long id) {
        AcEmployeeAttendance employeeAttendance = Optional.ofNullable(acEmployeeAttendanceDao.getByKey(id)).orElseGet(AcEmployeeAttendance::new);
        ValidationUtil.isNull(employeeAttendance.getId(), "EmployeeAttendance", "id", id);
        return acEmployeeAttendanceMapper.toDto(employeeAttendance);
    }

    @Override
    public List<AcEmployeeAttendanceDTO> listAll(AcEmployeeAttendanceQueryCriteria criteria) {
        return acEmployeeAttendanceMapper.toDto(acEmployeeAttendanceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcEmployeeAttendanceQueryCriteria criteria, Pageable pageable) {
        Page<AcEmployeeAttendance> page = PageUtil.startPage(pageable);
        List<AcEmployeeAttendance> employeeAttendances = acEmployeeAttendanceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acEmployeeAttendanceMapper.toDto(employeeAttendances), page.getTotal());
    }

    @Override
    public void download(List<AcEmployeeAttendanceDTO> employeeAttendanceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcEmployeeAttendanceDTO employeeAttendanceDTO : employeeAttendanceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", employeeAttendanceDTO.getId());
            map.put("员工人事档案id", employeeAttendanceDTO.getEmployeeId());
            map.put("排班日期", employeeAttendanceDTO.getRegimeaDate());
            map.put("一班时间开始", employeeAttendanceDTO.getFirstTimeFrom());
            map.put("一班时间结束", employeeAttendanceDTO.getFirstTimeTo());
            map.put("一班是否跨日", employeeAttendanceDTO.getExtend1TimeFlag());
            map.put("二班时间开始", employeeAttendanceDTO.getSecondTimeFrom());
            map.put("二班时间结束", employeeAttendanceDTO.getSecondTimeTo());
            map.put("二班是否跨日", employeeAttendanceDTO.getExtend2TimeFlag());
            map.put("三班时间开始", employeeAttendanceDTO.getThirdTimeFrom());
            map.put("三班时间结束", employeeAttendanceDTO.getThirdTimeTo());
            map.put("三班是否跨日", employeeAttendanceDTO.getExtend3TimeFlag());
            map.put("是否休息日", employeeAttendanceDTO.getRestDayFlag());
            map.put("弹性域1", employeeAttendanceDTO.getAttribute1());
            map.put("弹性域2", employeeAttendanceDTO.getAttribute2());
            map.put("弹性域3", employeeAttendanceDTO.getAttribute3());
            map.put("有效标记", employeeAttendanceDTO.getEnabledFlag());
            map.put("创建人id", employeeAttendanceDTO.getCreateBy());
            map.put("创建时间", employeeAttendanceDTO.getCreateTime());
            map.put("修改人id", employeeAttendanceDTO.getUpdateBy());
            map.put("修改时间", employeeAttendanceDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<AcEmployeeAttendanceDTO> batchSave(AcEmployeeAttendanceCollect acEmployeeAttendanceCollect) {
        List<LocalDate> dates = acEmployeeAttendanceCollect.getDates();
        // 判断日期是否空
        if (null == dates || dates.size() < 1) {
            throw new InfoCheckWarningMessException("请选择日期");
        }
        List<Long> employeeIds = acEmployeeAttendanceCollect.getEmpIds();
        // 判断人员是否空
        if (null == employeeIds || employeeIds.size() < 1) {
            throw new InfoCheckWarningMessException("请选择人员");
        }
        // 对前台传来的日期集合进行排序
        dates.sort(Comparator.comparingLong((LocalDate date) -> DateUtil.asLong(date.atStartOfDay())));
        // 判断日期集合是否会大于已运行打卡异常的最后日期
        AcAbnormalAttendanceRecord lastOne = acAbnormalAttendanceRecordDao.getLastOne();
        if (null != lastOne) {
            if (dates.get(0).isBefore(lastOne.getEndTime()) || dates.get(0).equals(lastOne.getEndTime())) {
                throw new InfoCheckWarningMessException("不允许修改已运行打卡异常的日期的排班");
            }
        }
        // 失效相应的旧排班
        acEmployeeAttendanceDao.disableAttendanceInDatesAndEmpIds(dates, employeeIds, SecurityUtils.getUserId(), LocalDateTime.now());
        // 去重
        dates = dates.stream().distinct().collect(Collectors.toList());
        employeeIds = employeeIds.stream().distinct().collect(Collectors.toList());
        // 新增
        AcEmployeeAttendance attendance = acEmployeeAttendanceCollect.getAcEmployeeAttendance();
        // 组建对象
        List<AcEmployeeAttendance> targetList = new ArrayList<>();
        for (LocalDate date : dates) {
            for (Long empId : employeeIds) {
                AcEmployeeAttendance acEmployeeAttendance = new AcEmployeeAttendance();
                acEmployeeAttendance.setFirstTimeFrom(attendance.getFirstTimeFrom());
                acEmployeeAttendance.setFirstTimeTo(attendance.getFirstTimeTo());
                acEmployeeAttendance.setSecondTimeFrom(attendance.getSecondTimeFrom());
                acEmployeeAttendance.setSecondTimeTo(attendance.getSecondTimeTo());
                acEmployeeAttendance.setThirdTimeFrom(attendance.getThirdTimeFrom());
                acEmployeeAttendance.setThirdTimeTo(attendance.getThirdTimeTo());
                acEmployeeAttendance.setExtend1TimeFlag(attendance.getExtend1TimeFlag());
                acEmployeeAttendance.setExtend2TimeFlag(attendance.getExtend2TimeFlag());
                acEmployeeAttendance.setExtend3TimeFlag(attendance.getExtend3TimeFlag());
                acEmployeeAttendance.setRestDayFlag(attendance.getRestDayFlag());
                acEmployeeAttendance.setEnabledFlag(attendance.getEnabledFlag());
                acEmployeeAttendance.setAttribute1(attendance.getAttribute1());
                acEmployeeAttendance.setAttribute2(attendance.getAttribute2());
                acEmployeeAttendance.setAttribute3(attendance.getAttribute3());
                acEmployeeAttendance.setRegimeaDate(date);
                acEmployeeAttendance.setEmployeeId(empId);
                targetList.add(acEmployeeAttendance);
            }
        }
        // 开始插入
        for (AcEmployeeAttendance ac : targetList) {
            acEmployeeAttendanceDao.insertAllColumn(ac);
        }
        return acEmployeeAttendanceMapper.toDto(targetList);

    }

    @Override
    public List<AcEmployeeVo> listAllByCalendarLine(AcEmployeeAttendanceQueryCriteria acEmployeeAttendanceQueryCriteria) {
        Set<Long> deptIds = new HashSet<>(); // 部门id
        // 获取当前用户的岗位，根据岗位获取部门及子部门人员
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        boolean adminFlag = fndDataScope.checkPermissions(this.attendanceAdminAuthorityCode);
        if (user.getEmployee() == null && !adminFlag) {
            throw new InfoCheckWarningMessException("用户无关联员工，无权限，请联系管理员！");
        }
        if (null == acEmployeeAttendanceQueryCriteria.getDeptId()) {
            if (!adminFlag) {
                List<PmEmployeeJobDTO> jobs = pmEmployeeJobService.listByEmpIdAndEnabledFlagWithExtend(user.getEmployee().getId(), true);
                for (PmEmployeeJobDTO job : jobs) {
                    deptIds.addAll(fndDeptService.listAllEnableChildrenIdByPid(job.getDept().getId()));
                }
                // 非管理员无部门设置则查-1部门
                if (deptIds.size() == 0) {
                    deptIds.add(-1L);
                }
            }
        } else {
            deptIds.addAll(fndDeptService.listAllEnableChildrenIdByPid(acEmployeeAttendanceQueryCriteria.getDeptId()));
        }
        // 获取所有的deptIDs结束
        acEmployeeAttendanceQueryCriteria.setDeptIds(deptIds);
        // 根据条件展开排班
        List<AcEmployeeAttendance> attendances = acEmployeeAttendanceDao.listEmployeeAttendanceByCriteria(acEmployeeAttendanceQueryCriteria);

        // 开始组建AcEmployeeVo
        List<AcEmployeeVo> acEmployeeVos = new ArrayList<AcEmployeeVo>();
        AcEmployeeVo acEmployeeVo = new AcEmployeeVo();
        List<AcEmployeeAttendanceVo> acEmployeeAttendanceVoNeed = new ArrayList<>();
        acEmployeeVo.setEmployeeId(-1L);
        for (AcEmployeeAttendance attendance : attendances) {
            PmEmployee pmEmployee = attendance.getEmployee();
            if (!acEmployeeVo.getEmployeeId().equals(pmEmployee.getId())) {
                acEmployeeVo = new AcEmployeeVo();
                acEmployeeVo.setDept(pmEmployee.getDeptName());
                acEmployeeVo.setDepartment(pmEmployee.getDepartment());
                acEmployeeVo.setTeam(pmEmployee.getTeam());
                acEmployeeVo.setEmployeeId(pmEmployee.getId());
                acEmployeeVo.setEmployeeName(pmEmployee.getName());
                acEmployeeVo.setWorkCard(pmEmployee.getWorkCard());
                acEmployeeAttendanceVoNeed = new ArrayList<>();
                acEmployeeVo.setAcEmployeeAttendances(acEmployeeAttendanceVoNeed);
                acEmployeeVo.setCheckFlag(false);
                acEmployeeVos.add(acEmployeeVo);
            }
            AcEmployeeAttendanceVo acEmployeeAttendanceTemp = new AcEmployeeAttendanceVo();
            acEmployeeAttendanceTemp.setId(attendance.getId());
            acEmployeeAttendanceTemp.setRegimeaDate(attendance.getRegimeaDate()); // 日期acEmployeeAttendanceTemp.setId(ac.getId());
            acEmployeeAttendanceTemp.setFirstTimeFrom(attendance.getFirstTimeFrom());
            acEmployeeAttendanceTemp.setFirstTimeTo(attendance.getFirstTimeTo());
            acEmployeeAttendanceTemp.setSecondTimeFrom(attendance.getSecondTimeFrom());
            acEmployeeAttendanceTemp.setSecondTimeTo(attendance.getSecondTimeTo());
            acEmployeeAttendanceTemp.setThirdTimeFrom(attendance.getThirdTimeFrom());
            acEmployeeAttendanceTemp.setThirdTimeTo(attendance.getThirdTimeTo());
            acEmployeeAttendanceTemp.setExtend1TimeFlag(attendance.getExtend1TimeFlag());
            acEmployeeAttendanceTemp.setExtend2TimeFlag(attendance.getExtend2TimeFlag());
            acEmployeeAttendanceTemp.setExtend3TimeFlag(attendance.getExtend3TimeFlag());
            acEmployeeAttendanceTemp.setRestDayFlag(attendance.getRestDayFlag());
            acEmployeeAttendanceTemp.setAttribute1(attendance.getAttribute1());
            acEmployeeAttendanceTemp.setAttribute2(attendance.getAttribute2());
            acEmployeeAttendanceTemp.setAttribute3(attendance.getAttribute3());
            acEmployeeAttendanceTemp.setEnabledFlag(attendance.getEnabledFlag());
            acEmployeeAttendanceVoNeed.add(acEmployeeAttendanceTemp);
        }
        return acEmployeeVos;
    }

    @Override
    public Map<String, Object> listAllByCalendarLineByPage(AcEmployeeAttendanceQueryCriteria acEmployeeAttendanceQueryCriteria, Pageable pageable) {
        Set<Long> deptIds = new HashSet<>(); // 部门id
        // 获取当前用户的岗位，根据岗位获取部门及子部门人员
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Boolean adminFlag = fndDataScope.checkPermissions(this.attendanceAdminAuthorityCode);
        if (user.getEmployee() == null && !adminFlag) {
            throw new InfoCheckWarningMessException("用户无关联员工，无权限，请联系管理员！");
        }
        if (null == acEmployeeAttendanceQueryCriteria.getDeptId()) {
            if (!adminFlag) {
                List<PmEmployeeJobDTO> jobs = pmEmployeeJobService.listByEmpIdAndEnabledFlagWithExtend(user.getEmployee().getId(), true);
                for (PmEmployeeJobDTO job : jobs) {
                    deptIds.addAll(fndDeptService.listAllEnableChildrenIdByPid(job.getDept().getId()));
                }
                // 非管理员无部门设置则查-1部门
                if (deptIds.size() == 0) {
                    deptIds.add(-1L);
                }
            }
        } else {
            deptIds.addAll(fndDeptService.listAllEnableChildrenIdByPid(acEmployeeAttendanceQueryCriteria.getDeptId()));
        }
        // 获取所有的deptIDs结束
        acEmployeeAttendanceQueryCriteria.setDeptIds(deptIds);

        // 获取分页员工
        List<Long> empIds = new ArrayList<>();
        PmEmployeeQueryCriteria pmEmployeeQueryCriteria = new PmEmployeeQueryCriteria();
        pmEmployeeQueryCriteria.setEnabledFlag(true);
        pmEmployeeQueryCriteria.setLeaveFlag(false);
        pmEmployeeQueryCriteria.setWorkshopAttendanceFlag(true);
        pmEmployeeQueryCriteria.setWorkAttendanceFlag(true);
        pmEmployeeQueryCriteria.setDeptIds(deptIds);
        pmEmployeeQueryCriteria.setName(acEmployeeAttendanceQueryCriteria.getName());
        pmEmployeeQueryCriteria.setWorkCard(acEmployeeAttendanceQueryCriteria.getWorkCard());
        Page<PmEmployee> page = PageUtil.startPage(pageable);
        List<PmEmployee> pmEmployees = pmEmployeeDao.listAllByCriteriaPage(page,pmEmployeeQueryCriteria);
        if(pmEmployees.size()>0) {
            pmEmployees.forEach(employee -> {
                empIds.add(employee.getId());
            });
        } else {
            empIds.add(-99L);
        }
        acEmployeeAttendanceQueryCriteria.setEmpIds(empIds);
        // 根据条件展开排班
        List<AcEmployeeAttendance> attendances = acEmployeeAttendanceDao.listEmployeeAttendanceByCriteria(acEmployeeAttendanceQueryCriteria);

        // 开始组建AcEmployeeVo
        List<AcEmployeeVo> acEmployeeVos = new ArrayList<AcEmployeeVo>();
        AcEmployeeVo acEmployeeVo = new AcEmployeeVo();
        List<AcEmployeeAttendanceVo> acEmployeeAttendanceVoNeed = new ArrayList<>();
        acEmployeeVo.setEmployeeId(-1L);
        for (AcEmployeeAttendance attendance : attendances) {
            PmEmployee pmEmployee = attendance.getEmployee();
            if (!acEmployeeVo.getEmployeeId().equals(pmEmployee.getId())) {
                acEmployeeVo = new AcEmployeeVo();
                acEmployeeVo.setDept(pmEmployee.getDeptName());
                acEmployeeVo.setDepartment(pmEmployee.getDepartment());
                acEmployeeVo.setTeam(pmEmployee.getTeam());
                acEmployeeVo.setEmployeeId(pmEmployee.getId());
                acEmployeeVo.setEmployeeName(pmEmployee.getName());
                acEmployeeVo.setWorkCard(pmEmployee.getWorkCard());
                acEmployeeAttendanceVoNeed = new ArrayList<>();
                acEmployeeVo.setAcEmployeeAttendances(acEmployeeAttendanceVoNeed);
                acEmployeeVo.setCheckFlag(false);
                acEmployeeVos.add(acEmployeeVo);
            }
            AcEmployeeAttendanceVo acEmployeeAttendanceTemp = new AcEmployeeAttendanceVo();
            acEmployeeAttendanceTemp.setId(attendance.getId());
            acEmployeeAttendanceTemp.setRegimeaDate(attendance.getRegimeaDate()); // 日期acEmployeeAttendanceTemp.setId(ac.getId());
            acEmployeeAttendanceTemp.setFirstTimeFrom(attendance.getFirstTimeFrom());
            acEmployeeAttendanceTemp.setFirstTimeTo(attendance.getFirstTimeTo());
            acEmployeeAttendanceTemp.setSecondTimeFrom(attendance.getSecondTimeFrom());
            acEmployeeAttendanceTemp.setSecondTimeTo(attendance.getSecondTimeTo());
            acEmployeeAttendanceTemp.setThirdTimeFrom(attendance.getThirdTimeFrom());
            acEmployeeAttendanceTemp.setThirdTimeTo(attendance.getThirdTimeTo());
            acEmployeeAttendanceTemp.setExtend1TimeFlag(attendance.getExtend1TimeFlag());
            acEmployeeAttendanceTemp.setExtend2TimeFlag(attendance.getExtend2TimeFlag());
            acEmployeeAttendanceTemp.setExtend3TimeFlag(attendance.getExtend3TimeFlag());
            acEmployeeAttendanceTemp.setRestDayFlag(attendance.getRestDayFlag());
            acEmployeeAttendanceTemp.setAttribute1(attendance.getAttribute1());
            acEmployeeAttendanceTemp.setAttribute2(attendance.getAttribute2());
            acEmployeeAttendanceTemp.setAttribute3(attendance.getAttribute3());
            acEmployeeAttendanceTemp.setEnabledFlag(attendance.getEnabledFlag());
            acEmployeeAttendanceVoNeed.add(acEmployeeAttendanceTemp);
        }
        return PageUtil.toPage(acEmployeeVos, page.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoSendEmailForNoEmployeeAttendace() {
        ToolEmailInterface toolEmailInterface;
        List<NoAttendanceEmialHeader> noAttendanceEmialHeaderList = acEmployeeAttendanceDao.getNoAttendanceEmailHeader(null);
        // 拿server
        // 组建Email
        // 拿取列表
        toolEmailInterface = new ToolEmailInterface();
        // server 暂时不弄
        getServer(toolEmailInterface);
        // 设置标题
        toolEmailInterface.setMailSubject("员工未排班提醒 - " + systemName);
        toolEmailInterface.setStatus("PLAN");
        // 计划发送时间
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0));
        toolEmailInterface.setPlannedDate(dateTime);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/acEmployeeAttendance.ftl");
        for (NoAttendanceEmialHeader noAttendanceEmialHeader : noAttendanceEmialHeaderList
             ) {
            List<NoAttendanceDetailVo> noAttendanceDetailVos = acEmployeeAttendanceDao.getNoAttendanceDetail(
                    fndDeptDao.listAllChildrenByPid(noAttendanceEmialHeader.getDeptId())
                            .stream().map(FndDept::getId).collect(Collectors.toList())
            );
            if (noAttendanceDetailVos.size() > 0 && !noAttendanceEmialHeader.getEmail().equals("@in-sunten.com")) {
                toolEmailInterface.setSendTo(noAttendanceEmialHeader.getEmail());
                Dict dict = Dict.create();
                dict.set("acEmployeeAttendanceList", noAttendanceDetailVos);
                toolEmailInterface.setMailContent(template.render(dict));
                toolEmailInterfaceService.insert(toolEmailInterface);
            }
        }
    }

    public void getServer(ToolEmailInterface toolEmailInterface) {
        ToolEmailServerQueryCriteria toolEmailServerQueryCriteria = new ToolEmailServerQueryCriteria();
        toolEmailServerQueryCriteria.setFromUser("HR_system@in-sunten.com");
        List<ToolEmailServer> toolEmailServers = toolEmailServerDao.listAllByCriteria(toolEmailServerQueryCriteria);
        if (toolEmailServers.size() <= 0) {
            throw new InfoCheckWarningMessException("没找到相应的server");
        }
        toolEmailInterface.setEmailServer(toolEmailServers.get(0));
    }

    private void setAcEmployeeAttendanceNull(AcEmployeeAttendance acEmployeeAttendance) {
        acEmployeeAttendance.setExtend2TimeFlag(null);
        acEmployeeAttendance.setSecondTimeTo(null);
        acEmployeeAttendance.setSecondTimeFrom(null);
        acEmployeeAttendance.setFirstTimeTo(null);
        acEmployeeAttendance.setFirstTimeFrom(null);
        acEmployeeAttendance.setExtend1TimeFlag(null);
        acEmployeeAttendance.setExtend3TimeFlag(null);
        acEmployeeAttendance.setThirdTimeFrom(null);
        acEmployeeAttendance.setThirdTimeTo(null);
    }

    private Boolean validTime(AcEmployeeAttendance acEmployeeAttendance) {
        boolean falseFlag = false;
        if (null != acEmployeeAttendance.getExtend1TimeFlag() && null != acEmployeeAttendance.getExtend2TimeFlag()) {
            if (acEmployeeAttendance.getFirstTimeTo().isAfter(acEmployeeAttendance.getSecondTimeFrom())) {
                falseFlag = true;
            }
        }
        if (null != acEmployeeAttendance.getExtend2TimeFlag() && null != acEmployeeAttendance.getExtend3TimeFlag()) {
            if (acEmployeeAttendance.getSecondTimeTo().isAfter(acEmployeeAttendance.getThirdTimeFrom())) {
                falseFlag = true;
            }
        }
        return falseFlag;
    }

    private Boolean validAfterToday(AcEmployeeAttendance acEmployeeAttendance) {
        boolean validAfterTodayFlag = true;
        LocalDate now = LocalDate.now();
        if (acEmployeeAttendance.getRegimeaDate().isBefore(now)) {
            validAfterTodayFlag = false;
        }
        return validAfterTodayFlag;
    }

    @Override
    public void downloadAttendance(List<AcEmployeeVo> employeeAttendanceDTOS, Boolean showType, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        // 日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M.d");
        for (AcEmployeeVo employeeAttendanceDTO : employeeAttendanceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("姓名", employeeAttendanceDTO.getEmployeeName());
            map.put("工牌", employeeAttendanceDTO.getWorkCard());
            map.put("部门", employeeAttendanceDTO.getDept());
            map.put("科室", employeeAttendanceDTO.getDepartment());
            map.put("班组", employeeAttendanceDTO.getTeam());
            for(AcEmployeeAttendanceVo acEmployeeAttendanceVo : employeeAttendanceDTO.getAcEmployeeAttendances()) {
                String keyStr = formatter.format(acEmployeeAttendanceVo.getRegimeaDate());
                if (showType) {
                    map.put(keyStr, dateToStr(acEmployeeAttendanceVo));
                } else {
                    map.put(keyStr, countWorkTime(acEmployeeAttendanceVo));
                }
            }
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
    // 拼接日期
    private String dateToStr(AcEmployeeAttendanceVo acEmployeeAttendanceVo){
        StringBuilder res = new StringBuilder();
        if (!acEmployeeAttendanceVo.getId().equals(-1L)) {
            if (acEmployeeAttendanceVo.getRestDayFlag()) {
                res.append("休息日");
                return res.toString();
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            if (acEmployeeAttendanceVo.getFirstTimeFrom() != null) {
                res.append(formatter.format(acEmployeeAttendanceVo.getFirstTimeFrom()));
                res.append("-");
                res.append(formatter.format(acEmployeeAttendanceVo.getFirstTimeTo()));
            }
            if (acEmployeeAttendanceVo.getSecondTimeFrom() != null) {
                res.append(",");
                res.append(formatter.format(acEmployeeAttendanceVo.getSecondTimeFrom()));
                res.append("-");
                res.append(formatter.format(acEmployeeAttendanceVo.getSecondTimeTo()));
            }
            if (acEmployeeAttendanceVo.getThirdTimeFrom() != null) {
                res.append(",");
                res.append(formatter.format(acEmployeeAttendanceVo.getThirdTimeFrom()));
                res.append("-");
                res.append(formatter.format(acEmployeeAttendanceVo.getThirdTimeTo()));
            }
        }
        return res.toString();
    }
    // 计算总工时
    private Object countWorkTime(AcEmployeeAttendanceVo acEmployeeAttendanceVo) {
        if (!acEmployeeAttendanceVo.getId().equals(-1L)) {
            if (acEmployeeAttendanceVo.getRestDayFlag()) {
                return "休息日";
            }
            Long minutes = 0L;
            if (acEmployeeAttendanceVo.getFirstTimeFrom() != null) {
                Long first = acEmployeeAttendanceVo.getFirstTimeFrom().until(acEmployeeAttendanceVo.getFirstTimeTo(), ChronoUnit.MINUTES);
                minutes += first;
            }
            if (acEmployeeAttendanceVo.getSecondTimeFrom() != null) {
                Long second = acEmployeeAttendanceVo.getSecondTimeFrom().until(acEmployeeAttendanceVo.getSecondTimeTo(), ChronoUnit.MINUTES);
                minutes += second;
            }
            if (acEmployeeAttendanceVo.getThirdTimeFrom() != null) {
                Long third = acEmployeeAttendanceVo.getThirdTimeFrom().until(acEmployeeAttendanceVo.getThirdTimeTo(), ChronoUnit.MINUTES);
                minutes += third;
            }
            minutes = (minutes < 0 ? minutes + 1440L : minutes);
            BigDecimal minutesDecimal = new BigDecimal(minutes);
            BigDecimal hourDecimal = new BigDecimal("60");
            return minutesDecimal.divide(hourDecimal,2,BigDecimal.ROUND_HALF_UP);
        } else {
            return 0;
        }
    }
}
