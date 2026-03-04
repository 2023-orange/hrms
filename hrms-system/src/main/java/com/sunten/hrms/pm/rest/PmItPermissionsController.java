package com.sunten.hrms.pm.rest;

import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.PmItPermissions;
import com.sunten.hrms.pm.dto.PmItPermissionsQueryCriteria;
import com.sunten.hrms.pm.service.PmItPermissionsService;
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
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * it权限清单 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-05-10
 */
@RestController
@Api(tags = "it权限清单")
@RequestMapping("/api/pm/itPermissions")
public class PmItPermissionsController {
    private static final String ENTITY_NAME = "itPermissions";
    private final PmItPermissionsService pmItPermissionsService;

    public PmItPermissionsController(PmItPermissionsService pmItPermissionsService) {
        this.pmItPermissionsService = pmItPermissionsService;
    }

    @Log("新增it权限清单")
    @ApiOperation("新增it权限清单")
    @PostMapping
    @PreAuthorize("@el.check('itPermissions:add')")
    public ResponseEntity create(@Validated @RequestBody PmItPermissions itPermissions) {
        if (itPermissions.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(pmItPermissionsService.insert(itPermissions), HttpStatus.CREATED);
    }

    @Log("删除it权限清单")
    @ApiOperation("删除it权限清单")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('itPermissions:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmItPermissionsService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该it权限清单存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改it权限清单")
    @ApiOperation("修改it权限清单")
    @PutMapping
    @PreAuthorize("@el.check('itPermissions:edit')")
    public ResponseEntity update(@Validated(PmItPermissions.Update.class) @RequestBody PmItPermissions itPermissions) {
        pmItPermissionsService.update(itPermissions);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个it权限清单")
    @ApiOperation("获取单个it权限清单")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('itPermissions:list')")
    public ResponseEntity getItPermissions(@PathVariable Long id) {
        return new ResponseEntity<>(pmItPermissionsService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询it权限清单（分页）")
    @ApiOperation("查询it权限清单（分页）")
    @GetMapping
    @PreAuthorize("@el.check('itPermissions:list')")
    public ResponseEntity getItPermissionsPage(PmItPermissionsQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmItPermissionsService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询it权限清单（不分页）")
    @ApiOperation("查询it权限清单（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('itPermissions:list')")
    public ResponseEntity getItPermissionsNoPaging(PmItPermissionsQueryCriteria criteria) {
    return new ResponseEntity<>(pmItPermissionsService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出it权限清单数据")
    @ApiOperation("导出it权限清单数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('itPermissions:list')")
    public void download(HttpServletResponse response, PmItPermissionsQueryCriteria criteria) throws IOException {
        pmItPermissionsService.download(pmItPermissionsService.listAll(criteria), response);
    }

    @ErrorLog("返回所有permission,并以belong分类")
    @ApiOperation("返回所有permission,并以belong分类")
    @GetMapping(value = "/getPermissionListByBelong")
    @PreAuthorize("@el.check('leaveApproval:list')")
    public ResponseEntity getPermissionListByBelong() {
        return new ResponseEntity<>(pmItPermissionsService.getPermissionListByBelong(), HttpStatus.OK);
    }


}
