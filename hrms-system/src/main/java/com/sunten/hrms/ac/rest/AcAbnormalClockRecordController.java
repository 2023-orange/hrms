package com.sunten.hrms.ac.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.AcAbnormalClockRecord;
import com.sunten.hrms.ac.dto.AcAbnormalClockRecordQueryCriteria;
import com.sunten.hrms.ac.service.AcAbnormalClockRecordService;
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
 * 异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录） 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@RestController
@Api(tags = "异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录）")
@RequestMapping("/api/ac/abnormalClockRecord")
public class AcAbnormalClockRecordController {
    private static final String ENTITY_NAME = "abnormalClockRecord";
    private final AcAbnormalClockRecordService acAbnormalClockRecordService;

    public AcAbnormalClockRecordController(AcAbnormalClockRecordService acAbnormalClockRecordService) {
        this.acAbnormalClockRecordService = acAbnormalClockRecordService;
    }

    @Log("新增异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录）")
    @ApiOperation("新增异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录）")
    @PostMapping
    @PreAuthorize("@el.check('exceptionDispose:add')")
    public ResponseEntity create(@Validated @RequestBody AcAbnormalClockRecord abnormalClockRecord) {
        if (abnormalClockRecord.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(acAbnormalClockRecordService.insert(abnormalClockRecord), HttpStatus.CREATED);
    }

    @Log("删除异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录）")
    @ApiOperation("删除异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录）")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('exceptionDispose:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acAbnormalClockRecordService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录）存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录）")
    @ApiOperation("修改异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录）")
    @PutMapping
    @PreAuthorize("@el.check('exceptionDispose:edit')")
    public ResponseEntity update(@Validated(AcAbnormalClockRecord.Update.class) @RequestBody AcAbnormalClockRecord abnormalClockRecord) {
        acAbnormalClockRecordService.update(abnormalClockRecord);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录）")
    @ApiOperation("获取单个异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录）")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('exceptionDispose:list')")
    public ResponseEntity getAbnormalClockRecord(@PathVariable Long id) {
        return new ResponseEntity<>(acAbnormalClockRecordService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录）（分页）")
    @ApiOperation("查询异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录）（分页）")
    @GetMapping
    @PreAuthorize("@el.check('exceptionDispose:list')")
    public ResponseEntity getAbnormalClockRecordPage(AcAbnormalClockRecordQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(acAbnormalClockRecordService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录）（不分页）")
    @ApiOperation("查询异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录）（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('exceptionDispose:list')")
    public ResponseEntity getAbnormalClockRecordNoPaging(AcAbnormalClockRecordQueryCriteria criteria) {
    return new ResponseEntity<>(acAbnormalClockRecordService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录）数据")
    @ApiOperation("导出异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录）数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('exceptionDispose:list')")
    public void download(HttpServletResponse response, AcAbnormalClockRecordQueryCriteria criteria) throws IOException {
        acAbnormalClockRecordService.download(acAbnormalClockRecordService.listAll(criteria), response);
    }

//    @Log("处理异常打卡记录")
//    @ApiOperation("处理异常打卡记录")
//    @PutMapping(value = "/disposeClock")
//    @PreAuthorize("@el.check('exceptionDispose:edit')")
//    public ResponseEntity disposeClock(@RequestBody List<AcAbnormalClockRecord> abnormalClockRecords) {
//        acAbnormalClockRecordService.disposeClock(abnormalClockRecords);
//        return new ResponseEntity(HttpStatus.NO_CONTENT);
//    }

    @Log("处理OATest")
    @ApiOperation("处理OATest")
    @PostMapping(value = "/disposeClockTestByOA")
    @PreAuthorize("@el.check('exceptionDispose:edit')")
    public ResponseEntity disposeClockTestByOA(String projectId, String testOA) {
        return new ResponseEntity<>("hahahahahahhaahah", HttpStatus.OK);
    }
}
