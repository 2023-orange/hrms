package com.sunten.hrms.swm.rest;

import cn.hutool.json.JSONArray;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.swm.dao.SwmEmployeeDao;
import com.sunten.hrms.swm.domain.SwmEmpDept;
import com.sunten.hrms.swm.domain.SwmEmployee;
import com.sunten.hrms.swm.domain.SwmWageSummaryFile;
import com.sunten.hrms.swm.dto.SwmEmpDeptDTO;
import com.sunten.hrms.swm.dto.SwmEmpDeptQueryCriteria;
import com.sunten.hrms.swm.service.SwmEmpDeptService;
import com.sunten.hrms.swm.service.SwmEmployeeService;
import com.sunten.hrms.swm.service.SwmWageSummaryFileService;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmFloatingWage;
import com.sunten.hrms.swm.dto.SwmFloatingWageQueryCriteria;
import com.sunten.hrms.swm.service.SwmFloatingWageService;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 浮动工资表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@RestController
@Api(tags = "浮动工资表")
@RequestMapping("/api/swm/floatingWage")
public class SwmFloatingWageController {
    private static final String ENTITY_NAME = "floatingWage";
    private final SwmFloatingWageService swmFloatingWageService;
    private final FndUserService fndUserService;
    private final SwmEmployeeDao swmEmployeeDao;
    private final SwmWageSummaryFileService swmWageSummaryFileService;
    private final JwtPermissionService jwtPermissionService;
    private final SwmEmpDeptService swmEmpDeptService;
    @Value("${role.authSwmCharge}")
    private String authSwmCharge;

    public SwmFloatingWageController(SwmFloatingWageService swmFloatingWageService, FndUserService fndUserService, SwmEmployeeDao swmEmployeeDao,
                                     SwmWageSummaryFileService swmWageSummaryFileService, JwtPermissionService jwtPermissionService, SwmEmpDeptService swmEmpDeptService) {
        this.swmFloatingWageService = swmFloatingWageService;
        this.fndUserService = fndUserService;
        this.swmEmployeeDao = swmEmployeeDao;
        this.swmWageSummaryFileService = swmWageSummaryFileService;
        this.jwtPermissionService = jwtPermissionService;
        this.swmEmpDeptService = swmEmpDeptService;
    }

    @Log("新增浮动工资表")
    @ApiOperation("新增浮动工资表")
    @PostMapping
    @PreAuthorize("@el.check('floatingWage:add')")
    public ResponseEntity create(@Validated @RequestBody SwmFloatingWage floatingWage) {
        if (floatingWage.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmFloatingWageService.insert(floatingWage), HttpStatus.CREATED);
    }

    @Log("删除浮动工资表")
    @ApiOperation("删除浮动工资表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('floatingWage:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmFloatingWageService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该浮动工资表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改浮动工资表")
    @ApiOperation("修改浮动工资表")
    @PutMapping
    @PreAuthorize("@el.check('floatingWage:edit')")
    public ResponseEntity update(@Validated(SwmFloatingWage.Update.class) @RequestBody SwmFloatingWage floatingWage) {
        swmFloatingWageService.update(floatingWage);
        // 更新
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个浮动工资表")
    @ApiOperation("获取单个浮动工资表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('floatingWage:list')")
    public ResponseEntity getFloatingWage(@PathVariable Long id) {
        return new ResponseEntity<>(swmFloatingWageService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询浮动工资表（分页）")
    @ApiOperation("查询浮动工资表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('floatingWage:list')")
    public ResponseEntity getFloatingWagePage(SwmFloatingWageQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        setCriteria(criteria);
        return new ResponseEntity<>(swmFloatingWageService.listAll(criteria, pageable), HttpStatus.OK);
    }

    private void setCriteria(SwmFloatingWageQueryCriteria criteria) {
    }

    @ErrorLog("查询浮动工资表（不分页）")
    @ApiOperation("查询浮动工资表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('floatingWage:list')")
    public ResponseEntity getFloatingWageNoPaging(SwmFloatingWageQueryCriteria criteria) {
        setCriteria(criteria);
        return new ResponseEntity<>(swmFloatingWageService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出浮动工资表数据")
    @ApiOperation("导出浮动工资表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('floatingWage:list')")
    public void download(HttpServletResponse response, SwmFloatingWageQueryCriteria criteria) throws IOException {
        setCriteria(criteria);
        swmFloatingWageService.download(swmFloatingWageService.listAll(criteria), response);
    }

    @Log("浮动工资生成")
    @ApiOperation("浮动工资生成")
    @PostMapping(value = "/generateFloatingWage")
    @PreAuthorize("@el.check('floatingWage:add','floatingWage:list')")
    public ResponseEntity generateFloatingWage( @RequestBody String period) {
        return new ResponseEntity<>(swmFloatingWageService.generateFloatingWageByMsp(period), HttpStatus.CREATED);
    }

    @Log("按所得期间删除浮动工资")
    @ApiOperation("按所得期间删除浮动工资")
    @DeleteMapping(value = "/deleteByPeriod")
    @PreAuthorize("@el.check('floatingWage:del')")
    public ResponseEntity deleteByPeriod( @RequestBody String period) {
        swmFloatingWageService.deleteByPeriod(period);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ErrorLog("浮动工资所得期间集合获取")
    @ApiOperation("浮动工资所得期间集合获取")
    @GetMapping("/getFloatingWagePeriodList")
    @PreAuthorize("@el.check('floatingWage:list')")
    public ResponseEntity getFloatingWagePeriodList() {
        return new ResponseEntity<>(swmFloatingWageService.generatePeriodList(), HttpStatus.OK);
    }

    @Log("浮动工资发放")
    @ApiOperation("浮动工资发放")
    @PutMapping(value = "/grantFloatingWage")
    @PreAuthorize("@el.check('floatingWage:edit')")
    public ResponseEntity grantPostSkillSalary(@RequestBody String period) {
        Long limit = getLong(period);
        swmWageSummaryFileService.grantSalary("float", limit, period);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @Log("浮动工资冻结")
    @ApiOperation("浮动工资冻结")
    @PutMapping(value = "/flozenFloatingWage")
    @PreAuthorize("@el.check('floatingWage:edit')")
    public ResponseEntity flozenPostSkillSalary(@RequestBody String period) {
        Long limit = getLong(period);
        swmWageSummaryFileService.frozenSalary("float", limit, period);
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

    @ErrorLog("获取浮动工资最新所得期间")
    @ApiOperation("获取浮动工资最新所得期间")
    @GetMapping(value = "/getFloatNewestPeriod")
    @PreAuthorize("@el.check('sfwAllocatePerformance:list')")
    public String getFloatNewestPeriod() {
        return swmFloatingWageService.getFloatNewestPeriod();
    }

    @ErrorLog("导出车间二次分配数据")
    @ApiOperation("导出车间二次分配数据")
    @GetMapping(value = "/downloadSecondList")
    @PreAuthorize("@el.check('sfwAllocatePerformance:list')")
    public void downloadSecondList(HttpServletResponse response, SwmFloatingWageQueryCriteria criteria) throws IOException {
        if (setCriteriaForSpecialList(criteria)) {
            swmFloatingWageService.downloadSecondList(swmFloatingWageService.listSpecialAll(criteria), response);
        } else {
            swmFloatingWageService.downloadSecondList(swmFloatingWageService.listSpecialAll(criteria), response);
        }

    }

    @ErrorLog("调配绩效导入时专用列表获取")
    @ApiOperation("调配绩效导入时专用列表获取")
    @GetMapping(value = "/getFloatingWageSpecialList")
    @PreAuthorize("@el.check('sfwAllocatePerformance:list')")
    public ResponseEntity getFloatingWageSpecialList(SwmFloatingWageQueryCriteria criteria) {
        if (setCriteriaForSpecialList(criteria))
            return new ResponseEntity<>(swmFloatingWageService.listSpecialAll(criteria), HttpStatus.OK);
        return new ResponseEntity<>(swmFloatingWageService.listSpecialAll(criteria), HttpStatus.OK);
    }

    private boolean setCriteriaForSpecialList(SwmFloatingWageQueryCriteria criteria) {
        // 获取当前用户的数据范围
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        boolean swmChargeFlag = false;
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority auth : grantedAuthorityCollection
        ) {
            if (auth.getAuthority().equals(authSwmCharge)) {
                // 返回全部节点
                swmChargeFlag = true;
            }
        }
        if (swmChargeFlag) {
            criteria.setEmployeeCategory("02");
            return true;
        }
        SwmEmployee swmEmployee;
        if (null == user.getEmployee()) {
            throw new InfoCheckWarningMessException("该用户没有绑定员工");
        } else {
            swmEmployee = swmEmployeeDao.getByEmpId(user.getEmployee().getId());
        }
        criteria.setEmployeeCategory("02");
        SwmEmpDeptQueryCriteria swmEmpDeptQueryCriteria = new SwmEmpDeptQueryCriteria();
        swmEmpDeptQueryCriteria.setEnabledFlag(true);
        swmEmpDeptQueryCriteria.setSeId(swmEmployee.getId());
        swmEmpDeptQueryCriteria.setType("monthlyAssessment");
        List<SwmEmpDeptDTO> swmEmpDepts = swmEmpDeptService.listAll(swmEmpDeptQueryCriteria);
        if (swmEmpDepts.size() > 0) {
            criteria.setSwmEmpDepts(swmEmpDepts);
        } else {
            if (null != swmEmployee.getAdministrativeOffice()) {
                criteria.setAdministrativeOffice(swmEmployee.getAdministrativeOffice());
            } else {
                criteria.setDepartment(swmEmployee.getDepartment());
            }
        }
        return false;
    }

    @Log("批量修改浮动工资")
    @ApiOperation("批量修改浮动工资")
    @PutMapping("/batchUpdateFloatingWageSalary")
    @PreAuthorize("@el.check('floatingWage:edit')")
    public ResponseEntity updateAttendanceSetBatch(@RequestBody List<SwmFloatingWage> swmFloatingWageList) {
        swmFloatingWageService.batchUpdateFloatingWage(swmFloatingWageList);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ErrorLog("检测该所的期间的浮动是否冻结或为生成")
    @ApiOperation("检测该所的期间的浮动是否冻结或为生成")
    @PutMapping("/checkFloatingWageByPeriod")
    @PreAuthorize("@el.check('sfwAllocatePerformance:list')")
    public ResponseEntity checkFloatingWageByPeriod(@RequestBody String period) {
        return new ResponseEntity<>(swmFloatingWageService.checkFloatingWageBeforAutoUpdate(period), HttpStatus.OK);
    }

    @Log("批量保存车间调配绩效工资")
    @ApiOperation("批量保存车间调配绩效工资")
    @PutMapping("/batchUpdateAllocatePerformancePay")
    @PreAuthorize("@el.check('sfwAllocatePerformance:import')")
    public ResponseEntity batchUpdateAllocatePerformancePay(@RequestBody List<SwmFloatingWage> swmFloatingWages) {
        swmFloatingWageService.batchUpdateAllocatePerformancePay(swmFloatingWages);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ErrorLog("检测该月浮动工资是否已经冻结")
    @ApiOperation("检测该月浮动工资是否已经冻结")
    @PostMapping("/checkFloatFrozenFlagByPeriod")
    @PreAuthorize("@el.check('floatingWage:list')")
    public ResponseEntity checkFloatFrozenFlagByPeriod(@RequestParam String period) {
        return new ResponseEntity<>(swmFloatingWageService.checkFrozenFlagByPeriod(period), HttpStatus.OK);
    }
}
