package com.sunten.hrms.ac.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.AcCalendarHeader;
import com.sunten.hrms.ac.dto.AcCalendarHeaderQueryCriteria;
import com.sunten.hrms.ac.service.AcCalendarHeaderService;
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
 * 日历主表 前端控制器
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@RestController
@Api(tags = "日历主表")
@RequestMapping("/api/ac/calendarHeader")
public class AcCalendarHeaderController {
    private static final String ENTITY_NAME = "calendarHeader";
    private final AcCalendarHeaderService acCalendarHeaderService;

    public AcCalendarHeaderController(AcCalendarHeaderService acCalendarHeaderService) {
        this.acCalendarHeaderService = acCalendarHeaderService;
    }

    @Log("新增日历主表")
    @ApiOperation("新增日历主表")
    @PostMapping
    @PreAuthorize("@el.check('calendar:add')")
    public ResponseEntity create(@Validated @RequestBody AcCalendarHeader calendarHeader) {
        if (calendarHeader.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(acCalendarHeaderService.insert(calendarHeader), HttpStatus.CREATED);
    }

    @Log("删除日历主表")
    @ApiOperation("删除日历主表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('calendar:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acCalendarHeaderService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该日历主表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改日历主表")
    @ApiOperation("修改日历主表")
    @PutMapping
    @PreAuthorize("@el.check('calendar:edit')")
    public ResponseEntity update(@Validated(AcCalendarHeader.Update.class) @RequestBody AcCalendarHeader calendarHeader) {
        acCalendarHeaderService.update(calendarHeader);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个日历主表")
    @ApiOperation("获取单个日历主表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('calendar:list')")
    public ResponseEntity getCalendarHeader(@PathVariable Long id) {
        return new ResponseEntity<>(acCalendarHeaderService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询日历主表（分页）")
    @ApiOperation("查询日历主表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('calendar:list')")
    public ResponseEntity getCalendarHeaderPage(AcCalendarHeaderQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(acCalendarHeaderService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询日历主表（不分页）")
    @ApiOperation("查询日历主表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('calendar:list')")
    public ResponseEntity getCalendarHeaderNoPaging(AcCalendarHeaderQueryCriteria criteria) {
    return new ResponseEntity<>(acCalendarHeaderService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出日历主表数据")
    @ApiOperation("导出日历主表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('calendar:list')")
    public void download(HttpServletResponse response, AcCalendarHeaderQueryCriteria criteria) throws IOException {
        acCalendarHeaderService.download(acCalendarHeaderService.listAll(criteria), response);
    }
}
