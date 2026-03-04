package com.sunten.hrms.pm.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.pm.domain.PmEmployeeWorkhistory;
import com.sunten.hrms.pm.dto.PmEmployeeWorkhistoryQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeWorkhistoryService;
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
 * 工作经历表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "工作经历表")
@RequestMapping("/api/pm/employeeWorkhistory")
public class PmEmployeeWorkhistoryController {
    private static final String ENTITY_NAME = "employeeWorkhistory";
    private final PmEmployeeWorkhistoryService pmEmployeeWorkhistoryService;

    public PmEmployeeWorkhistoryController(PmEmployeeWorkhistoryService pmEmployeeWorkhistoryService) {
        this.pmEmployeeWorkhistoryService = pmEmployeeWorkhistoryService;
    }

    @Log("新增工作经历表")
    @ApiOperation("新增工作经历表")
    @PostMapping
    @PreAuthorize("@el.check('employeeWorkhistory:add','employee:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeeWorkhistory employeeWorkhistory) {
        if (employeeWorkhistory.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeWorkhistoryService.insert(employeeWorkhistory), HttpStatus.CREATED);
    }

    @Log("删除工作经历表")
    @ApiOperation("删除工作经历表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeWorkhistory:del','employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeWorkhistoryService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该工作经历表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改工作经历表")
    @ApiOperation("修改工作经历表")
    @PutMapping
    @PreAuthorize("@el.check('employeeWorkhistory:edit','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployeeWorkhistory.Update.class) @RequestBody PmEmployeeWorkhistory employeeWorkhistory) {
        pmEmployeeWorkhistoryService.update(employeeWorkhistory);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个工作经历表")
    @ApiOperation("获取单个工作经历表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeWorkhistory:list','employee:list')")
    public ResponseEntity getEmployeeWorkhistory(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeWorkhistoryService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询工作经历表（分页）")
    @ApiOperation("查询工作经历表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeWorkhistory:list','employee:list')")
    public ResponseEntity getEmployeeWorkhistoryPage(PmEmployeeWorkhistoryQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeWorkhistoryService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询工作经历表（不分页）")
    @ApiOperation("查询工作经历表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeWorkhistory:list','employee:list')")
    public ResponseEntity getEmployeeWorkhistoryNoPaging(PmEmployeeWorkhistoryQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeWorkhistoryService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出工作经历表数据")
    @ApiOperation("导出工作经历表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeWorkhistory:list','employee:list')")
    public void download(HttpServletResponse response, PmEmployeeWorkhistoryQueryCriteria criteria) throws IOException {
        pmEmployeeWorkhistoryService.download(pmEmployeeWorkhistoryService.listAll(criteria), response);
    }

    @Log("批量编辑工作经历表")
    @ApiOperation("批量编辑工作经历表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('employeeWorkhistory:add','employeeWorkhistory:edit','employee:add','employee:edit')")
    public ResponseEntity batchSave(@Validated @RequestBody List<PmEmployeeWorkhistory> employeeWorkhistorys) {
        if (employeeWorkhistorys == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null ");
        }
        return new ResponseEntity<>(pmEmployeeWorkhistoryService.batchInsert(employeeWorkhistorys, null), HttpStatus.CREATED);
    }
}
