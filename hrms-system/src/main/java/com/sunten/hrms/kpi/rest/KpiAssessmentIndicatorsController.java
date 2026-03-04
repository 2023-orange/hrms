package com.sunten.hrms.kpi.rest;

import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.kpi.dao.KpiAssessmentIndicatorsDao;
import com.sunten.hrms.kpi.domain.KpiAssessmentIndicatorsExcel;
import com.sunten.hrms.kpi.service.KpiAssessmentIndicatorsInterfaceService;
import com.sunten.hrms.pm.dao.PmEmployeeDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.security.service.JwtPermissionService;
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
import com.sunten.hrms.kpi.domain.KpiAssessmentIndicators;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsQueryCriteria;
import com.sunten.hrms.kpi.service.KpiAssessmentIndicatorsService;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * KPI考核指标表 前端控制器
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-28
 */
@RestController
@Api(tags = "KPI考核指标表")
@RequestMapping("/api/kpi/assessmentIndicators")
public class KpiAssessmentIndicatorsController {
    private static final String ENTITY_NAME = "assessmentIndicators";
    private final KpiAssessmentIndicatorsService kpiAssessmentIndicatorsService;
    private final FndDataScope fndDataScope;
    private final FndUserService fndUserService;
    private final JwtPermissionService jwtPermissionService;
    private final KpiAssessmentIndicatorsDao kpiAssessmentIndicatorsDao;
    private final PmEmployeeDao pmEmployeeDao;
    private final KpiAssessmentIndicatorsInterfaceService kpiAssessmentIndicatorsInterfaceService;

    @Value("${role.authRegime}")
    private String authRegime;

    public KpiAssessmentIndicatorsController(KpiAssessmentIndicatorsService kpiAssessmentIndicatorsService, FndDataScope fndDataScope, FndUserService fndUserService, JwtPermissionService jwtPermissionService, KpiAssessmentIndicatorsDao kpiAssessmentIndicatorsDao, PmEmployeeDao pmEmployeeDao, KpiAssessmentIndicatorsInterfaceService kpiAssessmentIndicatorsInterfaceService, KpiAssessmentIndicatorsInterfaceService kpiAssessmentIndicatorsInterfaceService1) {
        this.kpiAssessmentIndicatorsService = kpiAssessmentIndicatorsService;
        this.fndDataScope = fndDataScope;
        this.fndUserService = fndUserService;
        this.jwtPermissionService = jwtPermissionService;
        this.kpiAssessmentIndicatorsDao = kpiAssessmentIndicatorsDao;
        this.pmEmployeeDao = pmEmployeeDao;
        this.kpiAssessmentIndicatorsInterfaceService = kpiAssessmentIndicatorsInterfaceService;
    }

    @Log("新增KPI考核指标表")
    @ApiOperation("新增KPI考核指标表")
    @PostMapping
    @PreAuthorize("@el.check('assessmentIndicators:add')")
    public ResponseEntity create(@Validated @RequestBody KpiAssessmentIndicators assessmentIndicators) {
        if (assessmentIndicators.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(kpiAssessmentIndicatorsService.insert(assessmentIndicators), HttpStatus.CREATED);
    }

    @Log("删除KPI考核指标表")
    @ApiOperation("删除KPI考核指标表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('assessmentIndicators:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            kpiAssessmentIndicatorsService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该KPI考核指标表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改KPI考核指标表")
    @ApiOperation("修改KPI考核指标表")
    @PutMapping
    @PreAuthorize("@el.check('assessmentIndicators:edit')")
    public ResponseEntity update(@Validated(KpiAssessmentIndicators.Update.class) @RequestBody KpiAssessmentIndicators assessmentIndicators) {
        kpiAssessmentIndicatorsService.update(assessmentIndicators);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个KPI考核指标表")
    @ApiOperation("获取单个KPI考核指标表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('assessmentIndicators:list')")
    public ResponseEntity getAssessmentIndicators(@PathVariable Long id) {
        return new ResponseEntity<>(kpiAssessmentIndicatorsService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询KPI考核指标表（分页）")
    @ApiOperation("查询KPI考核指标表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('assessmentIndicators:list')")
    public ResponseEntity getAssessmentIndicatorsPage(KpiAssessmentIndicatorsQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, false, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        Boolean enterpriseManagement = false; // 是否KPI企管负责人
        PmEmployee pmEmployee = pmEmployeeDao.getPmEmployeeByWorkCard(user.getEmployee().getWorkCard());
        Long currentEmployeeId = pmEmployee.getId();
        criteria.setCurrentEmployeeId(currentEmployeeId);
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        for (GrantedAuthority auth: grantedAuthorityCollection
        ) {
            if (auth.getAuthority().equals("enterpriseManagement") || auth.getAuthority().equals("admin1")) {
                criteria.setDeptIds(null);
                criteria.setEmployeeId(null);
            }
        }
        return new ResponseEntity<>(kpiAssessmentIndicatorsService.listAll(criteria, pageable), HttpStatus.OK);
    }


    @ErrorLog("查询KPI被考核指标表（分页）")
    @ApiOperation("查询KPI被考核指标表（分页）")
    @GetMapping(value = "/getAssessedIndicators")
    @PreAuthorize("@el.check('assessedIndicators:list')")
    public ResponseEntity getAssessedIndicatorsPage(KpiAssessmentIndicatorsQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, false, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        Boolean enterpriseManagement = false; // 是否KPI企管负责人
        PmEmployee pmEmployee = pmEmployeeDao.getPmEmployeeByWorkCard(user.getEmployee().getWorkCard());
        Long currentEmployeeId = pmEmployee.getId();
        criteria.setCurrentEmployeeId(currentEmployeeId);
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        for (GrantedAuthority auth: grantedAuthorityCollection
        ) {
            if (auth.getAuthority().equals("enterpriseManagement") || auth.getAuthority().equals("admin1")) {
                criteria.setDeptIds(null);
                criteria.setEmployeeId(null);
            }
        }
        return new ResponseEntity<>(kpiAssessmentIndicatorsService.listAssessedIndicators(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询KPI考核指标表（不分页）")
    @ApiOperation("查询KPI考核指标表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('assessmentIndicators:list')")
    public ResponseEntity getAssessmentIndicatorsNoPaging(KpiAssessmentIndicatorsQueryCriteria criteria) {
    return new ResponseEntity<>(kpiAssessmentIndicatorsService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出KPI考核指标表数据")
    @ApiOperation("导出KPI考核指标表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('assessmentIndicators:list')")
    public void download(HttpServletResponse response, KpiAssessmentIndicatorsQueryCriteria criteria) throws IOException {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, false, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            kpiAssessmentIndicatorsService.download(kpiAssessmentIndicatorsService.listAll(criteria), response);
        }
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        Boolean enterpriseManagement = false; // 是否KPI企管负责人
        PmEmployee pmEmployee = pmEmployeeDao.getPmEmployeeByWorkCard(user.getEmployee().getWorkCard());
        Long currentEmployeeId = pmEmployee.getId();
        criteria.setCurrentEmployeeId(currentEmployeeId);
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        for (GrantedAuthority auth: grantedAuthorityCollection
        ) {
            if (auth.getAuthority().equals("enterpriseManagement") || auth.getAuthority().equals("admin1")) {
                criteria.setDeptIds(null);
                criteria.setEmployeeId(null);
            }
        }
        kpiAssessmentIndicatorsService.download(kpiAssessmentIndicatorsService.listAll(criteria), response);
    }

    @ErrorLog("导出KPI被考核部门指标表数据")
    @ApiOperation("导出KPI被考核部门指标表数据")
    @GetMapping(value = "/downloadAssessedIndicators")
    @PreAuthorize("@el.check('assessmentIndicators:list')")
    public void downloadAssessedIndicators(HttpServletResponse response, KpiAssessmentIndicatorsQueryCriteria criteria) throws IOException {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, false, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            kpiAssessmentIndicatorsService.download(kpiAssessmentIndicatorsService.listAssessedIndicators(criteria), response);
        }
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        Boolean enterpriseManagement = false; // 是否KPI企管负责人
        PmEmployee pmEmployee = pmEmployeeDao.getPmEmployeeByWorkCard(user.getEmployee().getWorkCard());
        Long currentEmployeeId = pmEmployee.getId();
        criteria.setCurrentEmployeeId(currentEmployeeId);
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        for (GrantedAuthority auth: grantedAuthorityCollection
        ) {
            if (auth.getAuthority().equals("enterpriseManagement") || auth.getAuthority().equals("admin1")) {
                criteria.setDeptIds(null);
                criteria.setEmployeeId(null);
            }
        }
        kpiAssessmentIndicatorsService.download(kpiAssessmentIndicatorsService.listAssessedIndicators(criteria), response);
    }

    @ErrorLog("获取KPI考核指标最新年份")
    @ApiOperation("获取KPI考核指标最新年份")
    @GetMapping(value = "/getNowDate")
    @PreAuthorize("@el.check('assessmentIndicators:list')")
    public ResponseEntity getNowDate() {
        return new ResponseEntity<>(kpiAssessmentIndicatorsService.getYearList(), HttpStatus.OK);
    }

    @ErrorLog("判断登录人是否为考核部门领导或者企管负责人或者考核数据填写人")
    @ApiOperation("判断登录人是否为考核部门领导或者企管负责人或者考核数据填写人")
    @GetMapping(value = "/getKpiAddRole")
    @PreAuthorize("@el.check('assessmentIndicators:list')")
    public ResponseEntity getKpiAddFlag() {
        Boolean kpiAddFlag = false;
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, false, null, null);
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        // 一、判断是否为企管
        for (GrantedAuthority auth: grantedAuthorityCollection
        ) {
            if (auth.getAuthority().equals("enterpriseManagement")) {
                kpiAddFlag = true;
                return new ResponseEntity<>(kpiAddFlag, HttpStatus.OK);
            }
        }
        // 二、判断是否为考核部门负责人或是否为KPI树上的数据填写人
        kpiAddFlag = kpiAssessmentIndicatorsDao.getAssessmentIndicatorsAddFlag(user.getEmployee().getWorkCard());
        return new ResponseEntity<>(kpiAddFlag, HttpStatus.OK);
    }

    @Log("导入KPI考核指标")
    @ApiOperation("导入KPI考核指标")
    @PutMapping(value = "/insertExcel")
    @PreAuthorize("@el.check('assessmentIndicators:list')")
    public ResponseEntity insertExcel(@RequestBody KpiAssessmentIndicatorsExcel kpiAssessmentIndicatorsExcel) {
        return new ResponseEntity<>(kpiAssessmentIndicatorsInterfaceService.importExcel(kpiAssessmentIndicatorsExcel.getKpiAssessmentIndicatorsInterfaces()), HttpStatus.OK);
    }


    @Log("获取打印经营责任书信息")
    @ApiOperation("获取打印经营责任书信息")
    @GetMapping(value = "/getAssessmentIndicatorsInfo")
    public ResponseEntity getAssessmentIndicatorsInfo(@RequestParam("year") String year, @RequestParam("openType") String openType) {
        List<HashMap<String, Object>> res = new ArrayList<>();
        if ("Manager".equals(openType)) {
            // 获取经理的
            res = kpiAssessmentIndicatorsService.getAssessmentIndicatorsInfoByManger(year);
        } else {
            // 获取主管的
            res = kpiAssessmentIndicatorsService.getAssessmentIndicatorsInfoByDepartmentHead(year);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @ErrorLog("获取被考核部门剩余权重")
    @ApiOperation("获取被考核部门剩余权重")
    @GetMapping(value = "/getResidueWeight/{id}")
    public ResponseEntity getResidueWeight(@PathVariable Long id) {
        System.out.println(kpiAssessmentIndicatorsService.getResidueWeight(id));
        return new ResponseEntity<>(kpiAssessmentIndicatorsService.getResidueWeight(id), HttpStatus.OK);
    }
}
