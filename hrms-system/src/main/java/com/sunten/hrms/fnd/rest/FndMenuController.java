package com.sunten.hrms.fnd.rest;

import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.dto.FndMenuDTO;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndRoleService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.domain.FndMenu;
import com.sunten.hrms.fnd.dto.FndMenuQueryCriteria;
import com.sunten.hrms.fnd.service.FndMenuService;
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
 *  前端控制器
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@RestController
@Api(tags = "系统：菜单管理")
@RequestMapping("/api/fnd/menu")
public class FndMenuController {
    private static final String ENTITY_NAME = "menu";
    private final FndMenuService fndMenuService;
    private final FndUserService fndUserService;
    private final FndRoleService fndRoleService;

    public FndMenuController(FndMenuService fndMenuService, FndUserService fndUserService, FndRoleService fndRoleService) {
        this.fndMenuService = fndMenuService;
        this.fndUserService = fndUserService;
        this.fndRoleService = fndRoleService;
    }

    @Log("新增菜单")
    @ApiOperation("新增菜单")
    @PostMapping
    @PreAuthorize("@el.check('menu:add')")
    public ResponseEntity create(@Validated @RequestBody FndMenu menu) {
        if (menu.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(fndMenuService.insert(menu), HttpStatus.CREATED);
    }

    @Log("删除菜单")
    @ApiOperation("删除菜单")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('menu:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        List<FndMenu> menuList = fndMenuService.listByPid(id);
        Set<FndMenu> menuSet = new HashSet<>();
        menuSet.add(fndMenuService.getOne(id));
        menuSet = fndMenuService.listMenus(menuList, menuSet);
        fndMenuService.delete(menuSet);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改菜单")
    @ApiOperation("修改菜单")
    @PutMapping
    @PreAuthorize("@el.check('menu:edit')")
    public ResponseEntity update(@Validated(FndMenu.Update.class) @RequestBody FndMenu menu) {
        fndMenuService.update(menu);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个菜单")
    @ApiOperation("获取单个菜单")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity getMenu(@PathVariable Long id){
        return new ResponseEntity<>(fndMenuService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询菜单（分页）")
    @ApiOperation("查询菜单（分页）")
    @GetMapping(value = "/page")
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity getMenuPage(FndMenuQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(fndMenuService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("导出菜单数据")
    @ApiOperation("导出菜单数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('menu:list')")
    public void download(HttpServletResponse response, FndMenuQueryCriteria criteria) throws IOException {
        fndMenuService.download(fndMenuService.listAll(criteria), response);
    }

    @ErrorLog("获取前端所需菜单")
    @ApiOperation("获取前端所需菜单")
    @GetMapping(value = "/build")
    public ResponseEntity buildMenus(){
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        List<FndMenuDTO> menuDTOList = fndMenuService.listByRoles(fndRoleService.listByUserId(user.getId()));
        List<FndMenuDTO> menuDTOS = (List<FndMenuDTO>) fndMenuService.buildTree(menuDTOList).get("content");
        return new ResponseEntity<>(fndMenuService.buildMenus(menuDTOS), HttpStatus.OK);
    }

    @ErrorLog("返回全部的菜单")
    @ApiOperation("返回全部的菜单")
    @GetMapping(value = "/tree")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResponseEntity getMenuTree(){
        return new ResponseEntity<>(fndMenuService.getMenuTree(fndMenuService.listByPid(0L)), HttpStatus.OK);
    }

    @ErrorLog("查询菜单")
    @ApiOperation("查询菜单")
    @GetMapping
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity getMenus(FndMenuQueryCriteria criteria){
        List<FndMenuDTO> fndMenuDTOList = fndMenuService.listAll(criteria);
        return new ResponseEntity<>(fndMenuService.buildTree(fndMenuDTOList), HttpStatus.OK);
    }
}
