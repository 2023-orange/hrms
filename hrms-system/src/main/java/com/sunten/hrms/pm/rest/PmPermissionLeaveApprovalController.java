package com.sunten.hrms.pm.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.PmPermissionLeaveApproval;
import com.sunten.hrms.pm.dto.PmPermissionLeaveApprovalQueryCriteria;
import com.sunten.hrms.pm.service.PmPermissionLeaveApprovalService;
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
 * 离职申请与IT权限关联表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-05-10
 */
@RestController
@Api(tags = "离职申请与IT权限关联表")
@RequestMapping("/api/pm/permissionLeaveApproval")
public class PmPermissionLeaveApprovalController {
    private static final String ENTITY_NAME = "permissionLeaveApproval";
    private final PmPermissionLeaveApprovalService pmPermissionLeaveApprovalService;

    public PmPermissionLeaveApprovalController(PmPermissionLeaveApprovalService pmPermissionLeaveApprovalService) {
        this.pmPermissionLeaveApprovalService = pmPermissionLeaveApprovalService;
    }

    @Log("新增离职申请与IT权限关联表")
    @ApiOperation("新增离职申请与IT权限关联表")
    @PostMapping
    @PreAuthorize("@el.check('leaveApproval:add')")
    public ResponseEntity create(@Validated @RequestBody PmPermissionLeaveApproval permissionLeaveApproval) {
        if (permissionLeaveApproval.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(pmPermissionLeaveApprovalService.insert(permissionLeaveApproval), HttpStatus.CREATED);
    }

    @Log("删除离职申请与IT权限关联表")
    @ApiOperation("删除离职申请与IT权限关联表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('leaveApproval:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmPermissionLeaveApprovalService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该离职申请与IT权限关联表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改离职申请与IT权限关联表")
    @ApiOperation("修改离职申请与IT权限关联表")
    @PutMapping
    @PreAuthorize("@el.check('leaveApproval:edit')")
    public ResponseEntity update(@Validated(PmPermissionLeaveApproval.Update.class) @RequestBody PmPermissionLeaveApproval permissionLeaveApproval) {
        pmPermissionLeaveApprovalService.update(permissionLeaveApproval);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个离职申请与IT权限关联表")
    @ApiOperation("获取单个离职申请与IT权限关联表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('leaveApproval:list')")
    public ResponseEntity getPermissionLeaveApproval(@PathVariable Long id) {
        return new ResponseEntity<>(pmPermissionLeaveApprovalService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询离职申请与IT权限关联表（分页）")
    @ApiOperation("查询离职申请与IT权限关联表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('leaveApproval:list')")
    public ResponseEntity getPermissionLeaveApprovalPage(PmPermissionLeaveApprovalQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmPermissionLeaveApprovalService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询离职申请与IT权限关联表（不分页）")
    @ApiOperation("查询离职申请与IT权限关联表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('leaveApproval:list')")
    public ResponseEntity getPermissionLeaveApprovalNoPaging(PmPermissionLeaveApprovalQueryCriteria criteria) {
    return new ResponseEntity<>(pmPermissionLeaveApprovalService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("以特定对象格式返回permission")
    @ApiOperation("以特定对象格式返回permission")
    @GetMapping(value = "/getPermissionList")
    @PreAuthorize("@el.check('leaveApproval:list')")
    public ResponseEntity getPermissionList(PmPermissionLeaveApprovalQueryCriteria criteria) {
        return new ResponseEntity<>(pmPermissionLeaveApprovalService.getPermissionList(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出离职申请与IT权限关联表数据")
    @ApiOperation("导出离职申请与IT权限关联表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('leaveApproval:list')")
    public void download(HttpServletResponse response, PmPermissionLeaveApprovalQueryCriteria criteria) throws IOException {
        pmPermissionLeaveApprovalService.download(pmPermissionLeaveApprovalService.listAll(criteria), response);
    }
}
