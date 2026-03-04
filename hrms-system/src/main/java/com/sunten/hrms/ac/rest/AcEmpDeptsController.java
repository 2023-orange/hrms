package com.sunten.hrms.ac.rest;

import com.sunten.hrms.ac.domain.AcEmpDepts;
import com.sunten.hrms.ac.dto.AcEmpDeptsQueryCriteria;
import com.sunten.hrms.ac.service.AcEmpDeptsService;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.dao.FndDeptDao;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import com.sunten.hrms.utils.SecurityUtils;
import com.sunten.hrms.utils.ThrowableUtil;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 考勤模块人员数据权限范围表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-12-09
 */
@RestController
@Api(tags = "考勤模块人员数据权限范围表")
@RequestMapping("/api/ac/empDepts")
public class AcEmpDeptsController {
    private static final String ENTITY_NAME = "empDepts";
    private final AcEmpDeptsService acEmpDeptsService;
    private final FndDeptService fndDeptService;
    private final FndUserService fndUserService;
    private final FndDeptDao fndDeptDao;

    public AcEmpDeptsController(AcEmpDeptsService acEmpDeptsService, FndDeptService fndDeptService, FndUserService fndUserService, FndDeptDao fndDeptDao) {
        this.acEmpDeptsService = acEmpDeptsService;
        this.fndDeptService = fndDeptService;
        this.fndUserService = fndUserService;
        this.fndDeptDao = fndDeptDao;
    }

    @Log("新增考勤模块人员数据权限范围表")
    @ApiOperation("新增考勤模块人员数据权限范围表")
    @PostMapping
//    @PreAuthorize("@el.check('empDepts:add')")
    public ResponseEntity create(@Validated @RequestBody AcEmpDepts empDepts) {
        if (empDepts.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(acEmpDeptsService.insert(empDepts), HttpStatus.CREATED);
    }

    @Log("删除考勤模块人员数据权限范围表")
    @ApiOperation("删除考勤模块人员数据权限范围表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('empDepts:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acEmpDeptsService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该考勤模块人员数据权限范围表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改考勤模块人员数据权限范围表")
    @ApiOperation("修改考勤模块人员数据权限范围表")
    @PutMapping
    @PreAuthorize("@el.check('empDepts:edit')")
    public ResponseEntity update(@Validated(AcEmpDepts.Update.class) @RequestBody AcEmpDepts empDepts) {
        acEmpDeptsService.update(empDepts);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个考勤模块人员数据权限范围表")
    @ApiOperation("获取单个考勤模块人员数据权限范围表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('empDepts:list')")
    public ResponseEntity getEmpDepts(@PathVariable Long id) {
        return new ResponseEntity<>(acEmpDeptsService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询考勤模块人员数据权限范围表（分页）")
    @ApiOperation("查询考勤模块人员数据权限范围表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('empDepts:list')")
    public ResponseEntity getEmpDeptsPage(AcEmpDeptsQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(acEmpDeptsService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询考勤模块人员数据权限范围表（不分页）")
    @ApiOperation("查询考勤模块人员数据权限范围表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('empDepts:list')")
    public ResponseEntity getEmpDeptsNoPaging(AcEmpDeptsQueryCriteria criteria) {
    return new ResponseEntity<>(acEmpDeptsService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出考勤模块人员数据权限范围表数据")
    @ApiOperation("导出考勤模块人员数据权限范围表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('empDepts:list')")
    public void download(HttpServletResponse response, AcEmpDeptsQueryCriteria criteria) throws IOException {
        acEmpDeptsService.download(acEmpDeptsService.listAll(criteria), response);
    }


    @ErrorLog("根据人员ID获取考勤数据范围")
    @ApiOperation("根据人员ID获取考勤数据范围")
    @GetMapping(value = "/getEmpDeptsByEmpId")
    @PreAuthorize("@el.check('empDepts:list')")
    public ResponseEntity getEmpDeptsByEmpId(AcEmpDeptsQueryCriteria criteria) {
//        Set<Long> ids =  acEmpDeptsService.listAll(criteria).stream().map(AcEmpDeptsDTO::getDeptId).collect(Collectors.toSet());
        return new ResponseEntity<>(acEmpDeptsService.listAll(criteria), HttpStatus.OK);
    }

    @Log("修改权限范围")
    @ApiOperation("修改权限范围")
    @PutMapping(value = "/editEmpDeptsIds")
    @PreAuthorize("@el.check('empDepts:edit')")
    public ResponseEntity editEmpDeptsIds(@RequestBody AcEmpDepts empDepts) {
        acEmpDeptsService.updateEmpDept(empDepts);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除权限")
    @ApiOperation("删除权限")
    @PutMapping(value = "/removeRelationByid")
    @PreAuthorize("@el.check('empDepts:edit')")
    public ResponseEntity removeRelationByid(@RequestBody AcEmpDepts empDepts) {
        acEmpDeptsService.removeRelationByid(empDepts.getEmployeeId(), empDepts.getDataType());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取ac人员权限列表")
    @ApiOperation("获取ac人员权限列表")
    @GetMapping(value = "/getRoleEmpList")
    @PreAuthorize("@el.check('empDepts:list')")
    public ResponseEntity getRoleEmpList(PmEmployeeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"work_card"}) Pageable pageable) {
        criteria.setEnabledFlag(true);
        criteria.setLeaveFlag(false);
        if (criteria.getDeptAllId() != null) {
            Set<Long> deptIds = new HashSet<>(fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptAllId()));
            criteria.setDeptIds(deptIds);
        }
        return new ResponseEntity<>(acEmpDeptsService.getRoleEmpList(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("检查是否存在权限")
    @ApiOperation("检查是否存在权限")
    @GetMapping(value = "/checkRoleEmp")
    @PreAuthorize("@el.check('empDepts:list')")
    public ResponseEntity checkRoleEmp(PmEmployeeQueryCriteria criteria) {
        criteria.setEnabledFlag(true);
        criteria.setLeaveFlag(false);
        return new ResponseEntity<>(acEmpDeptsService.countRoleEmp(criteria), HttpStatus.OK);
    }

    @ErrorLog("获取资料员权限")
    @ApiOperation("获取资料员权限")
    @GetMapping(value = "/getRoleEmp")
    public ResponseEntity getRoleEmp(AcEmpDeptsQueryCriteria criteria) {
        criteria.setEnabledFlag(true);
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        criteria.setEmployeeId(user.getEmployee().getId());
        return new ResponseEntity<>(acEmpDeptsService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("检验是否为资料员")
    @ApiOperation("检验是否为资料员")
    @GetMapping(value = "/checkDocPermission")
    public ResponseEntity checkDocRole() {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        return new ResponseEntity<>(acEmpDeptsService.checkDocPermission(user.getEmployee().getId()), HttpStatus.OK);
    }

    @ErrorLog("检验是否为资料员或管理层")
    @ApiOperation("检验是否为资料员或管理层")
    @GetMapping(value = "/checkDocOrLeader")
    public ResponseEntity checkDocOrLeader() {
        Boolean res = false;
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        // 资料员
        Boolean docFlag = acEmpDeptsService.checkDocPermission(user.getEmployee().getId());
        // 管理层
        Boolean leaFlag = false;
        List<FndDept> fndDepCharge = fndDeptDao.getDeptByEmpAndAdminJob(user.getEmployee().getId());

        if (fndDepCharge.size() > 0) {
            leaFlag = true;
        }
        if (docFlag || leaFlag) {
            res = true;
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
