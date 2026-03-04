package com.sunten.hrms.ac.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.AcAttendanceRecordTemp;
import com.sunten.hrms.ac.dto.AcAttendanceRecordTempQueryCriteria;
import com.sunten.hrms.ac.service.AcAttendanceRecordTempService;
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
 * 考勤处理记录临时表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@RestController
@Api(tags = "考勤处理记录临时表")
@RequestMapping("/api/ac/attendanceRecordTemp")
public class AcAttendanceRecordTempController {
    private static final String ENTITY_NAME = "attendanceRecordTemp";
    private final AcAttendanceRecordTempService acAttendanceRecordTempService;

    public AcAttendanceRecordTempController(AcAttendanceRecordTempService acAttendanceRecordTempService) {
        this.acAttendanceRecordTempService = acAttendanceRecordTempService;
    }

    @Log("新增考勤处理记录临时表")
    @ApiOperation("新增考勤处理记录临时表")
    @PostMapping
    @PreAuthorize("@el.check('attendanceRecordTemp:add')")
    public ResponseEntity create(@Validated @RequestBody AcAttendanceRecordTemp attendanceRecordTemp) {
        if (attendanceRecordTemp.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(acAttendanceRecordTempService.insert(attendanceRecordTemp), HttpStatus.CREATED);
    }

    @Log("删除考勤处理记录临时表")
    @ApiOperation("删除考勤处理记录临时表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('attendanceRecordTemp:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acAttendanceRecordTempService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该考勤处理记录临时表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改考勤处理记录临时表")
    @ApiOperation("修改考勤处理记录临时表")
    @PutMapping
    @PreAuthorize("@el.check('attendanceRecordTemp:edit')")
    public ResponseEntity update(@Validated(AcAttendanceRecordTemp.Update.class) @RequestBody AcAttendanceRecordTemp attendanceRecordTemp) {
        acAttendanceRecordTempService.update(attendanceRecordTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个考勤处理记录临时表")
    @ApiOperation("获取单个考勤处理记录临时表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('attendanceRecordTemp:list')")
    public ResponseEntity getAttendanceRecordTemp(@PathVariable Long id) {
        return new ResponseEntity<>(acAttendanceRecordTempService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询考勤处理记录临时表（分页）")
    @ApiOperation("查询考勤处理记录临时表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('attendanceRecordTemp:list')")
    public ResponseEntity getAttendanceRecordTempPage(AcAttendanceRecordTempQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(acAttendanceRecordTempService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询考勤处理记录临时表（不分页）")
    @ApiOperation("查询考勤处理记录临时表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('attendanceRecordTemp:list')")
    public ResponseEntity getAttendanceRecordTempNoPaging(AcAttendanceRecordTempQueryCriteria criteria) {
    return new ResponseEntity<>(acAttendanceRecordTempService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出考勤处理记录临时表数据")
    @ApiOperation("导出考勤处理记录临时表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('attendanceRecordTemp:list')")
    public void download(HttpServletResponse response, AcAttendanceRecordTempQueryCriteria criteria) throws IOException {
        acAttendanceRecordTempService.download(acAttendanceRecordTempService.listAll(criteria), response);
    }
}
