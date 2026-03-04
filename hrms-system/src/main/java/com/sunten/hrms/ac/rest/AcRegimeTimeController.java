package com.sunten.hrms.ac.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.AcRegimeTime;
import com.sunten.hrms.ac.dto.AcRegimeTimeQueryCriteria;
import com.sunten.hrms.ac.service.AcRegimeTimeService;
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
 * 考勤制度时间段表 前端控制器
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@RestController
@Api(tags = "考勤制度时间段表")
@RequestMapping("/api/ac/regimeTime")
public class AcRegimeTimeController {
    private static final String ENTITY_NAME = "regimeTime";
    private final AcRegimeTimeService acRegimeTimeService;

    public AcRegimeTimeController(AcRegimeTimeService acRegimeTimeService) {
        this.acRegimeTimeService = acRegimeTimeService;
    }

    @Log("新增考勤制度时间段表")
    @ApiOperation("新增考勤制度时间段表")
    @PostMapping
    @PreAuthorize("@el.check('regime:add')")
    public ResponseEntity create(@Validated @RequestBody AcRegimeTime regimeTime) {
        if (regimeTime.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(acRegimeTimeService.insert(regimeTime), HttpStatus.CREATED);
    }

    @Log("删除考勤制度时间段表")
    @ApiOperation("删除考勤制度时间段表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('regime:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acRegimeTimeService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该考勤制度时间段表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改考勤制度时间段表")
    @ApiOperation("修改考勤制度时间段表")
    @PutMapping
    @PreAuthorize("@el.check('regime:edit')")
    public ResponseEntity update(@Validated(AcRegimeTime.Update.class) @RequestBody AcRegimeTime regimeTime) {
        acRegimeTimeService.update(regimeTime);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个考勤制度时间段表")
    @ApiOperation("获取单个考勤制度时间段表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('regime:list')")
    public ResponseEntity getRegimeTime(@PathVariable Long id) {
        return new ResponseEntity<>(acRegimeTimeService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询考勤制度时间段表（分页）")
    @ApiOperation("查询考勤制度时间段表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('regime:list','employeeAttendance:list')")
    public ResponseEntity getRegimeTimePage(AcRegimeTimeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(acRegimeTimeService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询考勤制度时间段表（不分页）")
    @ApiOperation("查询考勤制度时间段表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('regime:list','employeeAttendance:list')")
    public ResponseEntity getRegimeTimeNoPaging(AcRegimeTimeQueryCriteria criteria) {
    return new ResponseEntity<>(acRegimeTimeService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出考勤制度时间段表数据")
    @ApiOperation("导出考勤制度时间段表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('regime:list')")
    public void download(HttpServletResponse response, AcRegimeTimeQueryCriteria criteria) throws IOException {
        acRegimeTimeService.download(acRegimeTimeService.listAll(criteria), response);
    }
}
