package com.sunten.hrms.ac.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.KqCrjsjLs;
import com.sunten.hrms.ac.dto.KqCrjsjLsQueryCriteria;
import com.sunten.hrms.ac.service.KqCrjsjLsService;
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
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-10-19
 */
@RestController
@Api(tags = "")
@RequestMapping("/api/ac/kqCrjsjLs")
public class KqCrjsjLsController {
    private static final String ENTITY_NAME = "kqCrjsjLs";
    private final KqCrjsjLsService kqCrjsjLsService;

    public KqCrjsjLsController(KqCrjsjLsService kqCrjsjLsService) {
        this.kqCrjsjLsService = kqCrjsjLsService;
    }

    @Log("新增kqCrjsjLs")
    @ApiOperation("新增kqCrjsjLs")
    @PostMapping
    @PreAuthorize("@el.check('kqCrjsjLs:add')")
    public ResponseEntity create(@Validated @RequestBody KqCrjsjLs kqCrjsjLs) {
        return new ResponseEntity<>(kqCrjsjLsService.insert(kqCrjsjLs), HttpStatus.CREATED);
    }

    @Log("删除kqCrjsjLs")
    @ApiOperation("删除kqCrjsjLs")
    @DeleteMapping(value = "/{date8}/{kh}/{mjkzqbh}/{time8}")
    @PreAuthorize("@el.check('kqCrjsjLs:del')")
    public ResponseEntity delete(@PathVariable LocalDateTime date8, @PathVariable String kh, @PathVariable String mjkzqbh, @PathVariable LocalDateTime time8) {
        try {
            kqCrjsjLsService.delete(date8, kh, mjkzqbh, time8);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该kqCrjsjLs存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改kqCrjsjLs")
    @ApiOperation("修改kqCrjsjLs")
    @PutMapping
    @PreAuthorize("@el.check('kqCrjsjLs:edit')")
    public ResponseEntity update(@Validated(KqCrjsjLs.Update.class) @RequestBody KqCrjsjLs kqCrjsjLs) {
        kqCrjsjLsService.update(kqCrjsjLs);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个kqCrjsjLs")
    @ApiOperation("获取单个kqCrjsjLs")
    @GetMapping(value = "/{date8}/{kh}/{mjkzqbh}/{time8}")
    @PreAuthorize("@el.check('kqCrjsjLs:list')")
    public ResponseEntity getkqCrjsjLs(@PathVariable LocalDateTime date8, @PathVariable String kh, @PathVariable String mjkzqbh, @PathVariable LocalDateTime time8) {
        return new ResponseEntity<>(kqCrjsjLsService.getByKey(date8, kh, mjkzqbh, time8), HttpStatus.OK);
    }

    @ErrorLog("查询kqCrjsjLs（分页）")
    @ApiOperation("查询kqCrjsjLs（分页）")
    @GetMapping
    @PreAuthorize("@el.check('kqCrjsjLs:list')")
    public ResponseEntity getkqCrjsjLsPage(KqCrjsjLsQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"date8", "kh", "mjkzqbh", "time8"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(kqCrjsjLsService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询kqCrjsjLs（不分页）")
    @ApiOperation("查询kqCrjsjLs（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('kqCrjsjLs:list')")
    public ResponseEntity getkqCrjsjLsNoPaging(KqCrjsjLsQueryCriteria criteria) {
    return new ResponseEntity<>(kqCrjsjLsService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出kqCrjsjLs数据")
    @ApiOperation("导出kqCrjsjLs数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('kqCrjsjLs:list')")
    public void download(HttpServletResponse response, KqCrjsjLsQueryCriteria criteria) throws IOException {
        kqCrjsjLsService.download(kqCrjsjLsService.listAll(criteria), response);
    }
}
