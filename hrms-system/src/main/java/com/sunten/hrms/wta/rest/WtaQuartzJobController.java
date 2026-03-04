package com.sunten.hrms.wta.rest;

import com.sunten.hrms.fnd.aop.ErrorLog;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.wta.domain.WtaQuartzJob;
import com.sunten.hrms.wta.dto.WtaQuartzJobQueryCriteria;
import com.sunten.hrms.wta.service.WtaQuartzJobService;
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
@RequestMapping("/api/wta/quartzJob")
public class WtaQuartzJobController {
    private static final String ENTITY_NAME = "quartzJob";
    private final WtaQuartzJobService wtaQuartzJobService;

    public WtaQuartzJobController(WtaQuartzJobService wtaQuartzJobService) {
        this.wtaQuartzJobService = wtaQuartzJobService;
    }

    @Log("新增定时任务")
    @ApiOperation("新增定时任务")
    @PostMapping
    @PreAuthorize("@el.check('quartzJob:add')")
    public ResponseEntity create(@Validated @RequestBody WtaQuartzJob quartzJob) {
        if (quartzJob.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(wtaQuartzJobService.insert(quartzJob), HttpStatus.CREATED);
    }

    @Log("删除定时任务")
    @ApiOperation("删除定时任务")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('quartzJob:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            wtaQuartzJobService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该定时任务存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改定时任务")
    @ApiOperation("修改定时任务")
    @PutMapping
    @PreAuthorize("@el.check('quartzJob:edit')")
    public ResponseEntity update(@Validated(WtaQuartzJob.Update.class) @RequestBody WtaQuartzJob quartzJob) {
        wtaQuartzJobService.update(quartzJob);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个定时任务")
    @ApiOperation("获取单个定时任务")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('quartzJob:list')")
    public ResponseEntity getQuartzJob(@PathVariable Long id){
        return new ResponseEntity<>(wtaQuartzJobService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询定时任务（分页）")
    @ApiOperation("查询定时任务（分页）")
    @GetMapping
    @PreAuthorize("@el.check('quartzJob:list')")
    public ResponseEntity getQuartzJobPage(WtaQuartzJobQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(wtaQuartzJobService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("导出定时任务数据")
    @ApiOperation("导出定时任务数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('quartzJob:list')")
    public void download(HttpServletResponse response, WtaQuartzJobQueryCriteria criteria) throws IOException {
        wtaQuartzJobService.download(wtaQuartzJobService.listAll(criteria), response);
    }

    @Log("更改定时任务状态")
    @ApiOperation("更改定时任务状态")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@el.check('quartzJob:edit')")
    public ResponseEntity updatePause(@PathVariable Long id){
        wtaQuartzJobService.updatePause(wtaQuartzJobService.getEntityByKey(id));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @Log("执行定时任务")
    @ApiOperation("执行定时任务")
    @PutMapping(value = "/exec/{id}")
    @PreAuthorize("@el.check('quartzJob:edit')")
    public ResponseEntity execution(@PathVariable Long id){
        wtaQuartzJobService.execution(wtaQuartzJobService.getEntityByKey(id));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
