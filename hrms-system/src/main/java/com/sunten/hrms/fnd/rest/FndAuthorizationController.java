package com.sunten.hrms.fnd.rest;

import com.sunten.hrms.fnd.dto.FndDeptDTO;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.fnd.domain.FndAuthorization;
import com.sunten.hrms.fnd.dto.FndAuthorizationQueryCriteria;
import com.sunten.hrms.fnd.service.FndAuthorizationService;
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
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-01-29
 */
@RestController
@Api(tags = "授权主表")
@RequestMapping("/api/fnd/authorization")
public class FndAuthorizationController {
    private static final String ENTITY_NAME = "authorization";
    private final FndAuthorizationService fndAuthorizationService;
    private final FndDeptService fndDeptService;
    private final PmEmployeeService pmEmployeeService;

    public FndAuthorizationController(FndAuthorizationService fndAuthorizationService, FndDeptService fndDeptService, PmEmployeeService pmEmployeeService) {
        this.fndAuthorizationService = fndAuthorizationService;
        this.fndDeptService = fndDeptService;
        this.pmEmployeeService = pmEmployeeService;
    }

    @Log("新增授权信息")
    @ApiOperation("新增授权信息")
    @PostMapping
    @PreAuthorize("@el.check('authorization:add')")
    public ResponseEntity create(@RequestBody FndAuthorization authorization) {
        return new ResponseEntity<>(fndAuthorizationService.insert(authorization), HttpStatus.CREATED);
    }

    @Log("删除授权信息")
    @ApiOperation("删除授权信息")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('authorization:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            fndAuthorizationService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改授权信息")
    @ApiOperation("修改授权信息")
    @PutMapping
    @PreAuthorize("@el.check('authorization:edit')")
    public ResponseEntity update(@Validated(FndAuthorization.Update.class) @RequestBody FndAuthorization authorization) {
        fndAuthorizationService.update(authorization);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个授权信息")
    @ApiOperation("获取单个授权信息")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('authorization:list')")
    public ResponseEntity getAuthorization(@PathVariable Long id) {
        return new ResponseEntity<>(fndAuthorizationService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询授权信息（分页）")
    @ApiOperation("查询授权信息（分页）")
    @GetMapping
    @PreAuthorize("@el.check('authorization:list')")
    public ResponseEntity getAuthorizationPage(FndAuthorizationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(fndAuthorizationService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询授权信息（不分页）")
    @ApiOperation("查询授权信息（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('authorization:list')")
    public ResponseEntity getAuthorizationNoPaging(FndAuthorizationQueryCriteria criteria) {
        return new ResponseEntity<>(fndAuthorizationService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出授权信息数据")
    @ApiOperation("导出授权信息数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('authorization:list')")
    public void download(HttpServletResponse response, FndAuthorizationQueryCriteria criteria) throws IOException {
        fndAuthorizationService.download(fndAuthorizationService.listAll(criteria), response);
    }

    @ErrorLog("获取授权人权限范围")
    @ApiOperation("获取授权人权限范围")
    @GetMapping(value = "/getByUserDept/{byId}")
    public ResponseEntity getByUserDept(@PathVariable Long byId) {
        // TODO 根据不同类型获取
        List<FndDeptDTO> depts = fndDeptService.listByAuthorization(byId);
        return new ResponseEntity<>(fndDeptService.buildAcTree(depts), HttpStatus.OK);
    }


    @Log("获取管理人员列表")
    @ApiOperation("获取管理人员列表")
    @GetMapping("/getLeaderList")
    @PreAuthorize("@el.check('authorization:list','LeaveAuthorization:list')")
    public ResponseEntity getLeaderList() {
        return new ResponseEntity(pmEmployeeService.listLeaderEmployees(),HttpStatus.OK);
    }

    @Log("根据工号或姓名获取人员基本信息列表")
    @ApiOperation("根据工号或姓名获取人员基本信息列表")
    @GetMapping("/getEmployeeBaseInfoList")
    @PreAuthorize("@el.check('authorization:list', 'LeaveAuthorization:list')")
    public ResponseEntity getEmployeeBaseInfoList(PmEmployeeQueryCriteria criteria) {
        return new ResponseEntity(pmEmployeeService.listEmployeesBaseInfoList(criteria),HttpStatus.OK);
    }
}
