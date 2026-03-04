package com.sunten.hrms.swm.rest;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.swm.dao.SwmEmpDeptDao;
import com.sunten.hrms.swm.dao.SwmEmployeeDao;
import com.sunten.hrms.swm.dao.SwmMonthlyQuarterlyAssessmentDao;
import com.sunten.hrms.swm.dao.SwmPostSkillSalaryDao;
import com.sunten.hrms.swm.domain.SwmEmpDept;
import com.sunten.hrms.swm.domain.SwmEmployee;
import com.sunten.hrms.swm.domain.SwmMonthlyQuarterlyAssessmentNum;
import com.sunten.hrms.swm.dto.SwmEmpDeptQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmMonthlyQuarterlyAssessmentMapper;
import com.sunten.hrms.swm.service.SwmFloatingWageService;
import com.sunten.hrms.swm.service.SwmNolimitationDeptService;
import com.sunten.hrms.swm.vo.DeptAndAdministrativeOfficeVo;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmMonthlyQuarterlyAssessment;
import com.sunten.hrms.swm.dto.SwmMonthlyQuarterlyAssessmentQueryCriteria;
import com.sunten.hrms.swm.service.SwmMonthlyQuarterlyAssessmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 月度考核表(一个季度生成三条月度) 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@RestController
@Api(tags = "月度考核表(一个季度生成三条月度)")
@RequestMapping("/api/swm/monthlyQuarterlyAssessment")
public class SwmMonthlyQuarterlyAssessmentController {
    private static final String ENTITY_NAME = "monthlyQuarterlyAssessment";
    private final SwmMonthlyQuarterlyAssessmentService swmMonthlyQuarterlyAssessmentService;
    private final FndUserService fndUserService;
    private final SwmEmployeeDao swmEmployeeDao;
    private final JwtPermissionService jwtPermissionService;
    private final SwmEmpDeptDao swmEmpDeptDao;
    private final SwmMonthlyQuarterlyAssessmentMapper swmMonthlyQuarterlyAssessmentMapper;
    private final SwmPostSkillSalaryDao swmPostSkillSalaryDao;
    private final SwmFloatingWageService swmFloatingWageService;
    private final SwmMonthlyQuarterlyAssessmentDao swmMonthlyQuarterlyAssessmentDao;
    private final SwmNolimitationDeptService swmNolimitationDeptService;

    @Value("${role.authSwmCharge}")
    private String swmCharge;
    @Value("${swmAuthType.monthlyAssessment}")
    private String monthlyAssessment;

    public SwmMonthlyQuarterlyAssessmentController(SwmMonthlyQuarterlyAssessmentService swmMonthlyQuarterlyAssessmentService,
                                                   FndUserService fndUserService, SwmEmployeeDao swmEmployeeDao, JwtPermissionService jwtPermissionService,
                                                   SwmEmpDeptDao swmEmpDeptDao, SwmMonthlyQuarterlyAssessmentMapper swmMonthlyQuarterlyAssessmentMapper,
                                                   SwmPostSkillSalaryDao swmPostSkillSalaryDao, SwmFloatingWageService swmFloatingWageService, SwmMonthlyQuarterlyAssessmentDao swmMonthlyQuarterlyAssessmentDao, SwmNolimitationDeptService swmNolimitationDeptService) {
        this.swmMonthlyQuarterlyAssessmentService = swmMonthlyQuarterlyAssessmentService;
        this.fndUserService = fndUserService;
        this.swmEmployeeDao = swmEmployeeDao;
        this.jwtPermissionService = jwtPermissionService;
        this.swmEmpDeptDao = swmEmpDeptDao;
        this.swmMonthlyQuarterlyAssessmentMapper = swmMonthlyQuarterlyAssessmentMapper;
        this.swmPostSkillSalaryDao = swmPostSkillSalaryDao;
        this.swmFloatingWageService = swmFloatingWageService;
        this.swmMonthlyQuarterlyAssessmentDao = swmMonthlyQuarterlyAssessmentDao;
        this.swmNolimitationDeptService = swmNolimitationDeptService;
    }

    @Log("新增月度考核表(一个季度生成三条月度)")
    @ApiOperation("新增月度考核表(一个季度生成三条月度)")
    @PostMapping
    @PreAuthorize("@el.check('monthlyQuarterlyAssessment:add')")
    public ResponseEntity create(@Validated @RequestBody SwmMonthlyQuarterlyAssessment monthlyQuarterlyAssessment) {
        if (monthlyQuarterlyAssessment.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmMonthlyQuarterlyAssessmentService.insert(monthlyQuarterlyAssessment), HttpStatus.CREATED);
    }

    @Log("生成月度考核month")
    @ApiOperation("生成月度考核month")
    @PostMapping(value = "/generateMonthlyAssessment")
    @PreAuthorize("@el.check('monthlyQuarterlyAssessment:add')")
    public ResponseEntity generateMonthlyAssessment(@RequestBody String period) {
        // 返回的数据不分页
        return new ResponseEntity<>(swmMonthlyQuarterlyAssessmentService.createMonthlyAssessment(period), HttpStatus.CREATED);
    }

    @Log("根据期间删除月度考核month")
    @ApiOperation("根据期间删除月度考核month")
    @DeleteMapping(value = "/removeMonthlyByPeriod")
    @PreAuthorize("@el.check('monthlyQuarterlyAssessment:del')")
    public ResponseEntity removeMonthlyByPeriod(@RequestBody String period) {
        swmMonthlyQuarterlyAssessmentService.removeMonthlyByPeriod(period);
        return new ResponseEntity(HttpStatus.OK);
    }


    @Log("删除月度考核表(一个季度生成三条月度)")
    @ApiOperation("删除月度考核表(一个季度生成三条月度)")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('monthlyQuarterlyAssessment:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmMonthlyQuarterlyAssessmentService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该月度考核表(一个季度生成三条月度)存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改月度考核表(一个季度生成三条月度)")
    @ApiOperation("修改月度考核表(一个季度生成三条月度)")
    @PutMapping
    @PreAuthorize("@el.check('monthlyQuarterlyAssessment:edit')")
    public ResponseEntity update(@Validated(SwmMonthlyQuarterlyAssessment.Update.class) @RequestBody SwmMonthlyQuarterlyAssessment monthlyQuarterlyAssessment) {
        SwmMonthlyQuarterlyAssessmentNum swmNum = new SwmMonthlyQuarterlyAssessmentNum();
        if (monthlyQuarterlyAssessment.getAssessmentLevel().equals("A")) // 等于A时进入判断
        {
            if (swmNolimitationDeptService.countDept(monthlyQuarterlyAssessment.getDepartment()) > 0) { // 科室评A总数相加部门（特殊化处理）
                swmNum = swmMonthlyQuarterlyAssessmentDao.noLimitDept(monthlyQuarterlyAssessment);
                if (swmNum.getActualNum() > swmNum.getCurrentNum())
                {
                    swmMonthlyQuarterlyAssessmentService.update(monthlyQuarterlyAssessment);
                    return new ResponseEntity(HttpStatus.NO_CONTENT);
                }
                else {
                    return new ResponseEntity<>("评A数量已达到上限", HttpStatus.NOT_FOUND);
                }
            }
            else { // 评A限制部门
                swmNum = swmMonthlyQuarterlyAssessmentDao.limitDept(monthlyQuarterlyAssessment);
                if (swmNum.getActualNum() > swmNum.getCurrentNum())
                {
                    swmMonthlyQuarterlyAssessmentService.update(monthlyQuarterlyAssessment);
                    return new ResponseEntity(HttpStatus.NO_CONTENT);
                }
                else {
                    return new ResponseEntity<>("评A数量已达到上限", HttpStatus.NOT_FOUND);
                }
            }
        }
        else {
            swmMonthlyQuarterlyAssessmentService.update(monthlyQuarterlyAssessment);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个月度考核表(一个季度生成三条月度)")
    @ApiOperation("获取单个月度考核表(一个季度生成三条月度)")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('monthlyQuarterlyAssessment:list')")
    public ResponseEntity getMonthlyQuarterlyAssessment(@PathVariable Long id) {
        return new ResponseEntity<>(swmMonthlyQuarterlyAssessmentService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询月度考核表(一个季度生成三条月度)（分页）")
    @ApiOperation("查询月度考核表(一个季度生成三条月度)（分页）")
    @GetMapping
    @PreAuthorize("@el.check('monthlyQuarterlyAssessment:list')")
    public ResponseEntity getMonthlyQuarterlyAssessmentPage(SwmMonthlyQuarterlyAssessmentQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return getMonthlyQuarterAssessment(criteria, pageable);
    }

    private ResponseEntity getMonthlyQuarterAssessment(SwmMonthlyQuarterlyAssessmentQueryCriteria criteria, Pageable pageable) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        List<String> authList = grantedAuthorityCollection.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        if (authList.contains(swmCharge)) {
            if (null == pageable) {
                return new ResponseEntity<>(swmMonthlyQuarterlyAssessmentService.listAll(criteria), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(swmMonthlyQuarterlyAssessmentService.listAll(criteria, pageable), HttpStatus.OK);
            }
        } else {
            // 获取人员
            SwmEmployee swmEmployee = swmEmployeeDao.getByEmpId(user.getEmployee().getId());
            if (null == swmEmployee){
                // 无数据权限
                if (null == pageable) {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
                }
            } else {
                setDepartmentAndAdministratorOffice(criteria, swmEmployee, true);
                if (null == pageable){
                    return new ResponseEntity<>(swmMonthlyQuarterlyAssessmentService.listAll(criteria), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(swmMonthlyQuarterlyAssessmentService.listAll(criteria, pageable), HttpStatus.OK);
                }
            }
        }
    }

    @ErrorLog("查询月度考核表(一个季度生成三条月度)（不分页）")
    @ApiOperation("查询月度考核表(一个季度生成三条月度)（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('monthlyQuarterlyAssessment:list')")
    public ResponseEntity getMonthlyQuarterlyAssessmentNoPaging(SwmMonthlyQuarterlyAssessmentQueryCriteria criteria) {
        return getMonthlyQuarterAssessment(criteria, null);
    }

    @ErrorLog("查询个人月度考核表")
    @ApiOperation("查询个人月度考核表")
    @GetMapping(value = "/assessmentList")
    @PreAuthorize("@el.check('monthlyQuarterlyAssessment:list','employee:list')")
    public ResponseEntity getAssessmentList(String workCard) {
        return new ResponseEntity<>(swmMonthlyQuarterlyAssessmentService.getAssessmentList(workCard), HttpStatus.OK);
    }

    @ErrorLog("根据区间判断是否还可以提交")
    @ApiOperation("根据区间判断是否还可以提交")
    @GetMapping(value = "/checkFloatingWageIsExist")
    @PreAuthorize("@el.check('monthlyQuarterlyAssessment:list')")
    public ResponseEntity checkFloatingWageIsExist(SwmMonthlyQuarterlyAssessmentQueryCriteria criteria) {
        return new ResponseEntity<>(swmFloatingWageService.checkFloatingWageIsExist(criteria.getPeriod()), HttpStatus.OK);
    }

    @ErrorLog("获取提交列表")
    @ApiOperation("获取提交列表")
    @GetMapping(value = "/getCommitList")
    @PreAuthorize("@el.check('monthlyQuarterlyAssessment:list')")
    public ResponseEntity getCommitList(SwmMonthlyQuarterlyAssessmentQueryCriteria criteria) {
        criteria.setAssessmentType("month");
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        if (null == user.getEmployee()) {
            throw new InfoCheckWarningMessException("当前用户没有绑定员工信息");
        } else {
            SwmEmployee swmEmployee = swmEmployeeDao.getByEmpId(user.getEmployee().getId());
            setDepartmentAndAdministratorOffice(criteria, swmEmployee, true);
            criteria.setFrozenFlag(false);
            return new ResponseEntity<>(swmMonthlyQuarterlyAssessmentService.listAll(criteria), HttpStatus.OK);
        }
    }

    @ErrorLog("导出月度考核表(一个季度生成三条月度)数据")
    @ApiOperation("导出月度考核表(一个季度生成三条月度)数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('monthlyQuarterlyAssessment:list')")
    public void download(HttpServletResponse response, SwmMonthlyQuarterlyAssessmentQueryCriteria criteria) throws IOException {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        List<String> authList = grantedAuthorityCollection.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        if (authList.contains(swmCharge)) {
            swmMonthlyQuarterlyAssessmentService.download(swmMonthlyQuarterlyAssessmentService.listAll(criteria), response);
        } else {
            // 获取人员
            SwmEmployee swmEmployee = swmEmployeeDao.getByEmpId(user.getEmployee().getId());
            if (null == swmEmployee){
                throw new InfoCheckWarningMessException("当前人员无数据权限");
            } else {
                setDepartmentAndAdministratorOffice(criteria, swmEmployee, true);
                swmMonthlyQuarterlyAssessmentService.download(swmMonthlyQuarterlyAssessmentService.listAll(criteria), response);
            }
        }
    }

    private void setDepartmentAndAdministratorOffice(SwmMonthlyQuarterlyAssessmentQueryCriteria criteria, SwmEmployee swmEmployee, Boolean exportFlag) {
        SwmEmpDeptQueryCriteria swmEmpDeptQueryCriteria = new SwmEmpDeptQueryCriteria();
        swmEmpDeptQueryCriteria.setType(monthlyAssessment);
        swmEmpDeptQueryCriteria.setEnabledFlag(true);
        swmEmpDeptQueryCriteria.setSeId(swmEmployee.getId());
        List<SwmEmpDept> swmEmpDepts = swmEmpDeptDao.listAllByCriteria(swmEmpDeptQueryCriteria);
        List<DeptAndAdministrativeOfficeVo> deptAndAdministrativeOfficeVos = new ArrayList<>();
        for (SwmEmpDept swmEmpDept : swmEmpDepts
        ) {
            DeptAndAdministrativeOfficeVo deptAndAdministrativeOfficeVo = new DeptAndAdministrativeOfficeVo();
            deptAndAdministrativeOfficeVo.setDepartment(swmEmpDept.getDepartment());
            deptAndAdministrativeOfficeVo.setAdministrativeOffice(null == swmEmpDept.getAdministrativeOffice() ? "-1" : swmEmpDept.getAdministrativeOffice());
            deptAndAdministrativeOfficeVos.add(deptAndAdministrativeOfficeVo);
        }
        criteria.setDeptAndAdministrativeOfficeVos(deptAndAdministrativeOfficeVos);
        // 导出的时候不排除自己，查询的时候需要排除自己
        criteria.setManagerLookSelfFlag(exportFlag);
        criteria.setSelfPmEmpId(swmEmployee.getEmployeeId());
    }


    @ErrorLog("月度考核所得期间获取")
    @ApiOperation("月度考核所得期间获取")
    @GetMapping("/getMonthPeriodList")
    @PreAuthorize("@el.check('monthlyQuarterlyAssessment:list')")
    public ResponseEntity getMonthPeriodList() {
        return new ResponseEntity<>(swmMonthlyQuarterlyAssessmentService.getMonthPeriodList(), HttpStatus.OK);
    }

    @Log("月度考核等级批量设置")
    @ApiOperation("月度考核等级批量设置")
    @PutMapping("/submitEmployeeMonthly")
    @PreAuthorize("@el.check('monthlyQuarterlyAssessment:edit')")
    public ResponseEntity submitEmployeeMonthly(@RequestBody List<SwmMonthlyQuarterlyAssessment> swmMonthlyQuarterlyAssessments){
        if (swmMonthlyQuarterlyAssessments.size() <= 0) {
            throw new InfoCheckWarningMessException("该所得期间未开放月度考核设置，请通知薪酬管理员开放");
        }

        if (swmPostSkillSalaryDao.checkFrozenFlagByPeriod(swmMonthlyQuarterlyAssessments.get(0).getAssessmentMonth()) > 0) {
            throw new InfoCheckWarningMessException("该所得期间的浮动工资已经冻结，不允许再修改考核等级");
        }
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        LocalDateTime now = LocalDateTime.now();
        for (SwmMonthlyQuarterlyAssessment swm : swmMonthlyQuarterlyAssessments
        ) {
            swm.setSubmitter(user.getEmployee().getName());
            swm.setSubmitTime(now);
        }
        swmMonthlyQuarterlyAssessmentService.submitEmployeeMonthly(swmMonthlyQuarterlyAssessments);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("月度考核冻结全部人")
    @ApiOperation("月度考核冻结全部人")
    @PutMapping(value = "/flozenMonthAppraisal")
    @PreAuthorize("@el.check('monthlyQuarterlyAssessment:add')")
    public ResponseEntity flozenMonthAppraisalSalary(@RequestBody String period) {
        Long limit = getLong(period);
        swmMonthlyQuarterlyAssessmentService.flozenMonthAppraisalSalary(limit, period);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    private Long getLong(@RequestBody String period) {
        LocalDate now = LocalDate.now().minusMonths(1);
        Long limit;
        if (period == null) {
            limit = Long.parseLong(now.getYear() + "" + (now.getMonthValue() < 10 ? "0" + now.getMonthValue() : now.getMonthValue() + ""));
        } else {
            String[] s = period.split("\\.");
            limit = Long.parseLong(s[0] + s[1]);
        }
        return limit;
    }

    @Log("月度考核冻结(解冻)")
    @ApiOperation("月度考核冻结(解冻)")
    @PutMapping(value = "/cancelFrozen")
    @PreAuthorize("@el.check('monthlyQuarterlyAssessment:add')")
    public ResponseEntity cancelFrozen(@RequestBody SwmMonthlyQuarterlyAssessment swmMonthlyQuarterlyAssessment) {
        swmMonthlyQuarterlyAssessmentService.cancelFrozen(swmMonthlyQuarterlyAssessment.getWorkCard(),swmMonthlyQuarterlyAssessment.getAssessmentMonth(),swmMonthlyQuarterlyAssessment.getFrozenFlag());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取部门月度A数量")
    @ApiOperation("获取部门月度A数量")
    @PutMapping(value = "/countNumByWorkCard")
    @PreAuthorize("@el.check('monthlyQuarterlyAssessment:list')")
    public ResponseEntity countNumByWorkCard(@RequestBody SwmMonthlyQuarterlyAssessment monthlyQuarterlyAssessment) {
        if (swmNolimitationDeptService.countDept(monthlyQuarterlyAssessment.getDepartment()) > 0) { // 科室评A总数相加部门（特殊化处理）
            return new ResponseEntity<>(swmMonthlyQuarterlyAssessmentDao.noLimitDept(monthlyQuarterlyAssessment), HttpStatus.OK);
        }
        else { // 评A限制部门
            return new ResponseEntity<>(swmMonthlyQuarterlyAssessmentDao.limitDept(monthlyQuarterlyAssessment), HttpStatus.OK);
        }
    }

}
