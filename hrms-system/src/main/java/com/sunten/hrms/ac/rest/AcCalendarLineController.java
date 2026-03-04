package com.sunten.hrms.ac.rest;

import com.sunten.hrms.ac.domain.AcCalendarHeaderAndYear;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.AcCalendarLine;
import com.sunten.hrms.ac.dto.AcCalendarLineQueryCriteria;
import com.sunten.hrms.ac.service.AcCalendarLineService;
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
 * 日历详细表 前端控制器
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@RestController
@Api(tags = "日历详细表")
@RequestMapping("/api/ac/calendarLine")
public class AcCalendarLineController {
    private static final String ENTITY_NAME = "calendarLine";
    private final AcCalendarLineService acCalendarLineService;

    public AcCalendarLineController(AcCalendarLineService acCalendarLineService) {
        this.acCalendarLineService = acCalendarLineService;
    }

    @Log("新增日历详细表")
    @ApiOperation("新增日历详细表")
    @PostMapping
    @PreAuthorize("@el.check('calendar:add')")
    public ResponseEntity create(@Validated @RequestBody AcCalendarHeaderAndYear acCalendarHeaderAndYear) {
//        if (calendarLine.getId()  != null) {
//            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
//        }
        return new ResponseEntity<>(acCalendarLineService.insert(acCalendarHeaderAndYear), HttpStatus.CREATED);
    }

    @Log("删除日历详细表")
    @ApiOperation("删除日历详细表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('calendar:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acCalendarLineService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该日历详细表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改日历详细表")
    @ApiOperation("修改日历详细表")
    @PutMapping
    @PreAuthorize("@el.check('calendar:edit')")
    public ResponseEntity update(@Validated(AcCalendarLine.Update.class) @RequestBody AcCalendarLine calendarLine) {
        acCalendarLineService.update(calendarLine);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个日历详细表")
    @ApiOperation("获取单个日历详细表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('calendar:list')")
    public ResponseEntity getCalendarLine(@PathVariable Long id) {
        return new ResponseEntity<>(acCalendarLineService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询日历详细表（分页）")
    @ApiOperation("查询日历详细表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('calendar:list')")
    public ResponseEntity getCalendarLinePage(AcCalendarLineQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(acCalendarLineService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询日历详细表（不分页）")
    @ApiOperation("查询日历详细表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('calendar:list')")
    public ResponseEntity getCalendarLineNoPaging(AcCalendarLineQueryCriteria criteria) {
    return new ResponseEntity<>(acCalendarLineService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出日历详细表数据")
    @ApiOperation("导出日历详细表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('calendar:list')")
    public void download(HttpServletResponse response, AcCalendarLineQueryCriteria criteria) throws IOException {
        acCalendarLineService.download(acCalendarLineService.listAll(criteria), response);
    }
}
