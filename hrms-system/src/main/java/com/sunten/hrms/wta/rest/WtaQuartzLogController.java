package com.sunten.hrms.wta.rest;

import com.sunten.hrms.fnd.aop.ErrorLog;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.wta.domain.WtaQuartzLog;
import com.sunten.hrms.wta.dto.WtaQuartzLogQueryCriteria;
import com.sunten.hrms.wta.service.WtaQuartzLogService;
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
@RequestMapping("/api/wta/quartzLog")
public class WtaQuartzLogController {
    private static final String ENTITY_NAME = "quartzLog";
    private final WtaQuartzLogService wtaQuartzLogService;

    public WtaQuartzLogController(WtaQuartzLogService wtaQuartzLogService) {
        this.wtaQuartzLogService = wtaQuartzLogService;
    }

    @Log("新增定时任务日志")
    @ApiOperation("新增定时任务日志")
    @PostMapping
    @PreAuthorize("@el.check('quartzLog:add')")
    public ResponseEntity create(@Validated @RequestBody WtaQuartzLog quartzLog) {
        if (quartzLog.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(wtaQuartzLogService.insert(quartzLog), HttpStatus.CREATED);
    }

    @Log("删除定时任务日志")
    @ApiOperation("删除定时任务日志")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('quartzLog:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            wtaQuartzLogService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该定时任务日志存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改定时任务日志")
    @ApiOperation("修改定时任务日志")
    @PutMapping
    @PreAuthorize("@el.check('quartzLog:edit')")
    public ResponseEntity update(@Validated(WtaQuartzLog.Update.class) @RequestBody WtaQuartzLog quartzLog) {
        wtaQuartzLogService.update(quartzLog);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个定时任务日志")
    @ApiOperation("获取单个定时任务日志")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('quartzJob:list')")
    public ResponseEntity getQuartzLog(@PathVariable Long id){
        return new ResponseEntity<>(wtaQuartzLogService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询定时任务日志（分页）")
    @ApiOperation("查询定时任务日志（分页）")
    @GetMapping
    @PreAuthorize("@el.check('quartzJob:list')")
    public ResponseEntity getQuartzLogPage(WtaQuartzLogQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(wtaQuartzLogService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("导出定时任务日志数据")
    @ApiOperation("导出定时任务日志数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('quartzJob:list')")
    public void download(HttpServletResponse response, WtaQuartzLogQueryCriteria criteria) throws IOException {
        wtaQuartzLogService.download(wtaQuartzLogService.listAll(criteria), response);
    }
}
