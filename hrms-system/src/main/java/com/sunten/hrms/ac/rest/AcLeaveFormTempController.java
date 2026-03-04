package com.sunten.hrms.ac.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.AcLeaveFormTemp;
import com.sunten.hrms.ac.dto.AcLeaveFormTempQueryCriteria;
import com.sunten.hrms.ac.service.AcLeaveFormTempService;
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
 * OA审批通过的请假条临时表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-10-20
 */
@RestController
@Api(tags = "OA审批通过的请假条临时表")
@RequestMapping("/api/ac/leaveFormTemp")
public class AcLeaveFormTempController {
    private static final String ENTITY_NAME = "leaveFormTemp";
    private final AcLeaveFormTempService acLeaveFormTempService;

    public AcLeaveFormTempController(AcLeaveFormTempService acLeaveFormTempService) {
        this.acLeaveFormTempService = acLeaveFormTempService;
    }

    @Log("新增OA审批通过的请假条临时表")
    @ApiOperation("新增OA审批通过的请假条临时表")
    @PostMapping
    @PreAuthorize("@el.check('leaveFormTemp:add')")
    public ResponseEntity create(@Validated @RequestBody AcLeaveFormTemp leaveFormTemp) {
        if (leaveFormTemp.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(acLeaveFormTempService.insert(leaveFormTemp), HttpStatus.CREATED);
    }

    @Log("删除OA审批通过的请假条临时表")
    @ApiOperation("删除OA审批通过的请假条临时表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('leaveFormTemp:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acLeaveFormTempService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该OA审批通过的请假条临时表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改OA审批通过的请假条临时表")
    @ApiOperation("修改OA审批通过的请假条临时表")
    @PutMapping
    @PreAuthorize("@el.check('leaveFormTemp:edit')")
    public ResponseEntity update(@Validated(AcLeaveFormTemp.Update.class) @RequestBody AcLeaveFormTemp leaveFormTemp) {
        acLeaveFormTempService.update(leaveFormTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个OA审批通过的请假条临时表")
    @ApiOperation("获取单个OA审批通过的请假条临时表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('leaveFormTemp:list')")
    public ResponseEntity getLeaveFormTemp(@PathVariable Long id) {
        return new ResponseEntity<>(acLeaveFormTempService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询OA审批通过的请假条临时表（分页）")
    @ApiOperation("查询OA审批通过的请假条临时表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('leaveFormTemp:list')")
    public ResponseEntity getLeaveFormTempPage(AcLeaveFormTempQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(acLeaveFormTempService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询OA审批通过的请假条临时表（不分页）")
    @ApiOperation("查询OA审批通过的请假条临时表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('leaveFormTemp:list')")
    public ResponseEntity getLeaveFormTempNoPaging(AcLeaveFormTempQueryCriteria criteria) {
    return new ResponseEntity<>(acLeaveFormTempService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出OA审批通过的请假条临时表数据")
    @ApiOperation("导出OA审批通过的请假条临时表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('leaveFormTemp:list')")
    public void download(HttpServletResponse response, AcLeaveFormTempQueryCriteria criteria) throws IOException {
        acLeaveFormTempService.download(acLeaveFormTempService.listAll(criteria), response);
    }
}
