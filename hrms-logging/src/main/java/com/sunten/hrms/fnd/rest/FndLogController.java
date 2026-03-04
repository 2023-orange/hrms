package com.sunten.hrms.fnd.rest;

import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.fnd.domain.FndLog;
import com.sunten.hrms.fnd.dto.FndLogQueryCriteria;
import com.sunten.hrms.fnd.service.FndLogService;
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

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author batan
 * @since 2019-12-23
 */
@RestController
@Api(tags = "")
@RequestMapping("/api/fnd/log")
public class FndLogController {
    private static final String ENTITY_NAME = "log";
    private final FndLogService fndLogService;

    public FndLogController(FndLogService fndLogService) {
        this.fndLogService = fndLogService;
    }

    @Log("新增日志")
    @ApiOperation("新增日志")
    @PostMapping
    @PreAuthorize("@el.check('log:add')")
    public ResponseEntity create(@Validated @RequestBody FndLog log) {
        if (log.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(fndLogService.insert(log), HttpStatus.CREATED);
    }

    @Log("删除日志")
    @ApiOperation("删除日志")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('log:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            fndLogService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该日志存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改日志")
    @ApiOperation("修改日志")
    @PutMapping
    @PreAuthorize("@el.check('log:edit')")
    public ResponseEntity update(@Validated(FndLog.Update.class) @RequestBody FndLog log) {
        fndLogService.update(log);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个日志")
    @ApiOperation("获取单个日志")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('log:list')")
    public ResponseEntity getLog(@PathVariable Long id){
        return new ResponseEntity<>(fndLogService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询日志（分页）")
    @ApiOperation("查询日志（分页）")
    @GetMapping(value="/page")
    @PreAuthorize("@el.check('log:list')")
    public ResponseEntity getLogPage(FndLogQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(fndLogService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("导出日志数据")
    @ApiOperation("导出日志数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check()")
    public void download(HttpServletResponse response, FndLogQueryCriteria criteria) throws IOException {
        fndLogService.download(fndLogService.listAll(criteria), response);
    }


    @GetMapping
    @ApiOperation("日志查询")
    @PreAuthorize("@el.check('log:list')")
    public ResponseEntity getLogs(FndLogQueryCriteria criteria, Pageable pageable){
        criteria.setLogType("INFO");
        return new ResponseEntity<>(fndLogService.listAll(criteria,pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/user")
    @ApiOperation("用户日志查询")
    public ResponseEntity getUserLogs(FndLogQueryCriteria criteria, Pageable pageable){
        criteria.setLogType("INFO");
        criteria.setBlurry(SecurityUtils.getUsername());
        return new ResponseEntity<>(fndLogService.listAllByUser(criteria,pageable), HttpStatus.OK);
    }


    @GetMapping(value = "/error")
    @ApiOperation("错误日志查询")
    @PreAuthorize("@el.check('log:list')")
    public ResponseEntity getErrorLogs(FndLogQueryCriteria criteria, Pageable pageable){
        criteria.setLogType("ERROR");
        return new ResponseEntity<>(fndLogService.listAll(criteria,pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/error/{id}")
    @ApiOperation("日志异常详情查询")
    @PreAuthorize("@el.check('log:list')")
    public ResponseEntity getErrorLogs(@PathVariable Long id){
        return new ResponseEntity<>(fndLogService.listByErrDetail(id), HttpStatus.OK);
    }
}
