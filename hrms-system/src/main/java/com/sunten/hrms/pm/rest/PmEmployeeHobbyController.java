package com.sunten.hrms.pm.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.pm.domain.PmEmployeeHobby;
import com.sunten.hrms.pm.dto.PmEmployeeHobbyQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeHobbyService;
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
import java.util.List;

/**
 * <p>
 * 技术爱好表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "技术爱好表")
@RequestMapping("/api/pm/employeeHobby")
public class PmEmployeeHobbyController {
    private static final String ENTITY_NAME = "employeeHobby";
    private final PmEmployeeHobbyService pmEmployeeHobbyService;

    public PmEmployeeHobbyController(PmEmployeeHobbyService pmEmployeeHobbyService) {
        this.pmEmployeeHobbyService = pmEmployeeHobbyService;
    }

    @Log("新增技术爱好表")
    @ApiOperation("新增技术爱好表")
    @PostMapping
    @PreAuthorize("@el.check('employeeHobby:add','employee:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeeHobby employeeHobby) {
        if (employeeHobby.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeHobbyService.insert(employeeHobby), HttpStatus.CREATED);
    }

    @Log("删除技术爱好表")
    @ApiOperation("删除技术爱好表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeHobby:del','employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeHobbyService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该技术爱好表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改技术爱好表")
    @ApiOperation("修改技术爱好表")
    @PutMapping
    @PreAuthorize("@el.check('employeeHobby:edit','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployeeHobby.Update.class) @RequestBody PmEmployeeHobby employeeHobby) {
        pmEmployeeHobbyService.update(employeeHobby);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个技术爱好表")
    @ApiOperation("获取单个技术爱好表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeHobby:list','employee:list')")
    public ResponseEntity getEmployeeHobby(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeHobbyService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询技术爱好表（分页）")
    @ApiOperation("查询技术爱好表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeHobby:list','employee:list')")
    public ResponseEntity getEmployeeHobbyPage(PmEmployeeHobbyQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeHobbyService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询技术爱好表（汇总）")
    @ApiOperation("查询技术爱好表（汇总）")
    @GetMapping(value = "/summary")
    @PreAuthorize("@el.check('employeeHobby:list','employee:list')")
    public ResponseEntity getEmployeeHobbySummary(PmEmployeeHobbyQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"employee_id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeHobbyService.listAllSummary(criteria), HttpStatus.OK);
    }

    @ErrorLog("查询技术爱好表（不分页）")
    @ApiOperation("查询技术爱好表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeHobby:list','employee:list')")
    public ResponseEntity getEmployeeHobbyNoPaging(PmEmployeeHobbyQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeHobbyService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出技术爱好表数据")
    @ApiOperation("导出技术爱好表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeHobby:list','employee:list')")
    public void download(HttpServletResponse response, PmEmployeeHobbyQueryCriteria criteria) throws IOException {
        pmEmployeeHobbyService.download(pmEmployeeHobbyService.listAll(criteria), response);
    }

    @Log("批量编辑技术爱好表")
    @ApiOperation("批量编辑技术爱好表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('employeeHobby:add','employeeHobby:edit','employee:add','employee:edit')")
    public ResponseEntity batchSave(@Validated @RequestBody List<PmEmployeeHobby> employeeHobbys) {
        if (employeeHobbys == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(pmEmployeeHobbyService.batchInsert(employeeHobbys, null), HttpStatus.CREATED);
    }
}
