package com.sunten.hrms.pm.rest;

import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.pm.domain.PmEmployeeLeaveoffice;
import com.sunten.hrms.pm.dto.PmEmployeeLeaveofficeQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeLeaveofficeService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
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
import java.util.ArrayList;

/**
 * <p>
 * 离职记录表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "离职记录表")
@RequestMapping("/api/pm/employeeLeaveoffice")
public class PmEmployeeLeaveofficeController {
    private static final String ENTITY_NAME = "employeeLeaveoffice";
    private final PmEmployeeLeaveofficeService pmEmployeeLeaveofficeService;
    private final FndDataScope fndDataScope;

    public PmEmployeeLeaveofficeController(PmEmployeeLeaveofficeService pmEmployeeLeaveofficeService, FndDataScope fndDataScope) {
        this.pmEmployeeLeaveofficeService = pmEmployeeLeaveofficeService;
        this.fndDataScope = fndDataScope;
    }

    @Log("新增离职记录表")
    @ApiOperation("新增离职记录表")
    @PostMapping
    @PreAuthorize("@el.check('employeeLeaveoffice:add','employee:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeeLeaveoffice employeeLeaveoffice) {
        if (employeeLeaveoffice.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeLeaveofficeService.insert(employeeLeaveoffice), HttpStatus.CREATED);
    }

    @Log("删除离职记录表")
    @ApiOperation("删除离职记录表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeLeaveoffice:del','employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeLeaveofficeService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该离职记录表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改离职记录表")
    @ApiOperation("修改离职记录表")
    @PutMapping
    @PreAuthorize("@el.check('employeeLeaveoffice:edit','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployeeLeaveoffice.Update.class) @RequestBody PmEmployeeLeaveoffice employeeLeaveoffice) {
        pmEmployeeLeaveofficeService.update(employeeLeaveoffice);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个离职记录表")
    @ApiOperation("获取单个离职记录表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeLeaveoffice:list','employee:list')")
    public ResponseEntity getEmployeeLeaveoffice(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeLeaveofficeService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询离职记录表（分页）")
    @ApiOperation("查询离职记录表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeLeaveoffice:list','employee:list')")
    public ResponseEntity getEmployeeLeaveofficePage(PmEmployeeLeaveofficeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeLeaveofficeService.listAll(criteria, pageable), HttpStatus.OK);
    }


    @ErrorLog("查询离职记录表（不分页）")
    @ApiOperation("查询离职记录表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeLeaveoffice:list','employee:list')")
    public ResponseEntity getEmployeeLeaveofficeNoPaging(PmEmployeeLeaveofficeQueryCriteria criteria) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeLeaveofficeService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出离职记录表数据")
    @ApiOperation("导出离职记录表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeLeaveoffice:list','employee:list')")
    public void download(HttpServletResponse response, PmEmployeeLeaveofficeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) throws IOException {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            FileUtil.downloadExcel(new ArrayList<>(), response);
        } else {
            criteria.setDeptIds(dataScopeVo.getDeptIds());
            criteria.setEmployeeId(dataScopeVo.getEmployeeId());
            pmEmployeeLeaveofficeService.download(criteria, pageable, response);
        }
    }
}
