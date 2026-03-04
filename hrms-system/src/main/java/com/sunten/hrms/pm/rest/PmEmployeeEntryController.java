package com.sunten.hrms.pm.rest;

import cn.hutool.json.JSONArray;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.pm.domain.PmEmployeeEntry;
import com.sunten.hrms.pm.dto.PmEmployeeEntryQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeEntryService;
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
 * 入职情况表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "入职情况表")
@RequestMapping("/api/pm/employeeEntry")
public class PmEmployeeEntryController {
    private static final String ENTITY_NAME = "employeeEntry";
    private final PmEmployeeEntryService pmEmployeeEntryService;
    private final FndDataScope fndDataScope;

    public PmEmployeeEntryController(PmEmployeeEntryService pmEmployeeEntryService, FndDataScope fndDataScope) {
        this.pmEmployeeEntryService = pmEmployeeEntryService;
        this.fndDataScope = fndDataScope;
    }

    @Log("新增入职情况表")
    @ApiOperation("新增入职情况表")
    @PostMapping
    @PreAuthorize("@el.check('employeeEntry:add','employee:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeeEntry employeeEntry) {
        if (employeeEntry.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeEntryService.insert(employeeEntry), HttpStatus.CREATED);
    }

    @Log("删除入职情况表")
    @ApiOperation("删除入职情况表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeEntry:del','employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeEntryService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该入职情况表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改入职情况表")
    @ApiOperation("修改入职情况表")
    @PutMapping
    @PreAuthorize("@el.check('employeeEntry:edit','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployeeEntry.Update.class) @RequestBody PmEmployeeEntry employeeEntry) {
        pmEmployeeEntryService.update(employeeEntry);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个入职情况表")
    @ApiOperation("获取单个入职情况表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeEntry:list','employee:list')")
    public ResponseEntity getEmployeeEntry(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeEntryService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询入职情况表（分页）")
    @ApiOperation("查询入职情况表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeEntry:list','employee:list')")
    public ResponseEntity getEmployeeEntryPage(PmEmployeeEntryQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeEntryService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询试用期情况表（分页）")
    @ApiOperation("查询试用期情况表（分页）")
    @GetMapping(value = "/probation")
    @PreAuthorize("@el.check('employeeEntry:list','employee:list')")
    public ResponseEntity getEmployeeEntryProbationPage(PmEmployeeEntryQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeEntryService.listAllByProbationCriteriaPage(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询入职情况表（不分页）")
    @ApiOperation("查询入职情况表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeEntry:list','employee:list')")
    public ResponseEntity getEmployeeEntryNoPaging(PmEmployeeEntryQueryCriteria criteria) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeEntryService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("查询入职情况表（分页）")
    @ApiOperation("查询入职情况表（分页）")
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('employeeEntry:list','employee:list')")
    public ResponseEntity getEmployeeEntryPageAll(PmEmployeeEntryQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeEntryService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("导出入职情况表数据")
    @ApiOperation("导出入职情况表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeEntry:list','employee:list')")
    public void download(HttpServletResponse response, PmEmployeeEntryQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) throws IOException {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            FileUtil.downloadExcel(new ArrayList<>(), response);
        } else {
            criteria.setDeptIds(dataScopeVo.getDeptIds());
            criteria.setEmployeeId(dataScopeVo.getEmployeeId());
            pmEmployeeEntryService.download(criteria, pageable, response);
        }
    }

    @Log("修改入职情况表试用审核标记")
    @ApiOperation("修改入职情况表试用审核标记")
    @PutMapping(value ="/updateAssessFlag")
    public ResponseEntity updateAssessFlag(@RequestBody PmEmployeeEntry employeeEntry) {
        pmEmployeeEntryService.updateAssessFlag(employeeEntry);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
