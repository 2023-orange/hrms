package com.sunten.hrms.ac.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.AcAbnormalAttendanceRecord;
import com.sunten.hrms.ac.dto.AcAbnormalAttendanceRecordQueryCriteria;
import com.sunten.hrms.ac.service.AcAbnormalAttendanceRecordService;
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
 * 异常考勤执行记录 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@RestController
@Api(tags = "异常考勤执行记录")
@RequestMapping("/api/ac/abnormalAttendanceRecord")
public class AcAbnormalAttendanceRecordController {
    private static final String ENTITY_NAME = "abnormalAttendanceRecord";
    private final AcAbnormalAttendanceRecordService acAbnormalAttendanceRecordService;

    public AcAbnormalAttendanceRecordController(AcAbnormalAttendanceRecordService acAbnormalAttendanceRecordService) {
        this.acAbnormalAttendanceRecordService = acAbnormalAttendanceRecordService;
    }

    @Log("新增异常考勤执行记录")
    @ApiOperation("新增异常考勤执行记录")
    @PostMapping
    @PreAuthorize("@el.check('abnormalAttendanceRecord:add')")
    public ResponseEntity create(@Validated @RequestBody AcAbnormalAttendanceRecord abnormalAttendanceRecord) {
        if (abnormalAttendanceRecord.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(acAbnormalAttendanceRecordService.insert(abnormalAttendanceRecord), HttpStatus.CREATED);
    }

    @Log("删除异常考勤执行记录")
    @ApiOperation("删除异常考勤执行记录")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('abnormalAttendanceRecord:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acAbnormalAttendanceRecordService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该异常考勤执行记录存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改异常考勤执行记录")
    @ApiOperation("修改异常考勤执行记录")
    @PutMapping
    @PreAuthorize("@el.check('abnormalAttendanceRecord:edit')")
    public ResponseEntity update(@Validated(AcAbnormalAttendanceRecord.Update.class) @RequestBody AcAbnormalAttendanceRecord abnormalAttendanceRecord) {
        acAbnormalAttendanceRecordService.update(abnormalAttendanceRecord);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个异常考勤执行记录")
    @ApiOperation("获取单个异常考勤执行记录")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('abnormalAttendanceRecord:list')")
    public ResponseEntity getAbnormalAttendanceRecord(@PathVariable Long id) {
        return new ResponseEntity<>(acAbnormalAttendanceRecordService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询异常考勤执行记录（分页）")
    @ApiOperation("查询异常考勤执行记录（分页）")
    @GetMapping
    @PreAuthorize("@el.check('abnormalAttendanceRecord:list')")
    public ResponseEntity getAbnormalAttendanceRecordPage(AcAbnormalAttendanceRecordQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(acAbnormalAttendanceRecordService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询异常考勤执行记录（不分页）")
    @ApiOperation("查询异常考勤执行记录（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('abnormalAttendanceRecord:list')")
    public ResponseEntity getAbnormalAttendanceRecordNoPaging(AcAbnormalAttendanceRecordQueryCriteria criteria) {
    return new ResponseEntity<>(acAbnormalAttendanceRecordService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出异常考勤执行记录数据")
    @ApiOperation("导出异常考勤执行记录数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('abnormalAttendanceRecord:list')")
    public void download(HttpServletResponse response, AcAbnormalAttendanceRecordQueryCriteria criteria) throws IOException {
        acAbnormalAttendanceRecordService.download(acAbnormalAttendanceRecordService.listAll(criteria), response);
    }
}
