package com.sunten.hrms.fnd.rest;

import cn.hutool.core.lang.Dict;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.dto.FndRoleSmallDTO;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.fnd.domain.FndRole;
import com.sunten.hrms.fnd.dto.FndRoleQueryCriteria;
import com.sunten.hrms.fnd.service.FndRoleService;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@RestController
@Api(tags = "系统：角色管理")
@RequestMapping("/api/fnd/role")
public class FndRoleController {
    private static final String ENTITY_NAME = "role";
    private final FndRoleService fndRoleService;

    public FndRoleController(FndRoleService fndRoleService) {
        this.fndRoleService = fndRoleService;
    }

    @Log("新增角色")
    @ApiOperation("新增角色")
    @PostMapping
    @PreAuthorize("@el.check('role:add')")
    public ResponseEntity create(@Validated @RequestBody FndRole role) {
        if (role.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(fndRoleService.insert(role), HttpStatus.CREATED);
    }

    @Log("删除角色")
    @ApiOperation("删除角色")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('role:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            fndRoleService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该角色存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改角色")
    @ApiOperation("修改角色")
    @PutMapping
    @PreAuthorize("@el.check('role:edit')")
    public ResponseEntity update(@Validated(FndRole.Update.class) @RequestBody FndRole role) {
        fndRoleService.update(role);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个角色")
    @ApiOperation("获取单个角色")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('role:list')")
    public ResponseEntity getRole(@PathVariable Long id){
        return new ResponseEntity<>(fndRoleService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询角色（分页）")
    @ApiOperation("查询角色（分页）")
    @GetMapping
    @PreAuthorize("@el.check('role:list')")
    public ResponseEntity getRolePage(FndRoleQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"level"}, direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(fndRoleService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("导出角色数据")
    @ApiOperation("导出角色数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('role:list')")
    public void download(HttpServletResponse response, FndRoleQueryCriteria criteria) throws IOException {
        fndRoleService.download(fndRoleService.listAll(criteria), response);
    }

    @ErrorLog("返回全部的角色")
    @ApiOperation("返回全部的角色")
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('role:list','user:add','user:edit')")
    public ResponseEntity getAll(@PageableDefault(value = 2000, sort = {"level"}, direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(fndRoleService.listAll(pageable), HttpStatus.OK);
    }


    @ErrorLog("获取用户级别")
    @ApiOperation("获取用户级别")
    @GetMapping(value = "/level")
    public ResponseEntity getLevel(){
        List<Integer> levels = fndRoleService.listByUserId(SecurityUtils.getUserId()).stream().map(FndRoleSmallDTO::getLevel).collect(Collectors.toList());
        return new ResponseEntity<>(Dict.create().set("level", Collections.min(levels)), HttpStatus.OK);
    }


    @Log("修改角色菜单")
    @ApiOperation("修改角色菜单")
    @PutMapping(value = "/menu")
    @PreAuthorize("@el.check('role:edit')")
    public ResponseEntity updateMenu(@RequestBody FndRole resources){
        fndRoleService.updateMenu(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
