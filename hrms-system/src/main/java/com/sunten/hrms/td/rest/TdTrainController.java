package com.sunten.hrms.td.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.td.domain.TdTrain;
import com.sunten.hrms.td.dto.TdTrainQueryCriteria;
import com.sunten.hrms.td.service.TdTrainService;
import com.sunten.hrms.utils.ThrowableUtil;
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
 * 培训情况表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "培训情况表")
@RequestMapping("/api/td/train")
public class TdTrainController {
    private static final String ENTITY_NAME = "train";
    private final TdTrainService tdTrainService;

    public TdTrainController(TdTrainService tdTrainService) {
        this.tdTrainService = tdTrainService;
    }

    @Log("新增培训情况表")
    @ApiOperation("新增培训情况表")
    @PostMapping
    @PreAuthorize("@el.check('train:add')")
    public ResponseEntity create(@Validated @RequestBody TdTrain train) {
        if (train.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdTrainService.insert(train), HttpStatus.CREATED);
    }

    @Log("删除培训情况表")
    @ApiOperation("删除培训情况表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('train:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdTrainService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该培训情况表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改培训情况表")
    @ApiOperation("修改培训情况表")
    @PutMapping
    @PreAuthorize("@el.check('train:edit')")
    public ResponseEntity update(@Validated(TdTrain.Update.class) @RequestBody TdTrain train) {
        tdTrainService.update(train);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个培训情况表")
    @ApiOperation("获取单个培训情况表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('train:list')")
    public ResponseEntity getTrain(@PathVariable Long id) {
        return new ResponseEntity<>(tdTrainService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询培训情况表（分页）")
    @ApiOperation("查询培训情况表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('train:list')")
    public ResponseEntity getTrainPage(TdTrainQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(tdTrainService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询培训情况表（不分页）")
    @ApiOperation("查询培训情况表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('train:list')")
    public ResponseEntity getTrainNoPaging(TdTrainQueryCriteria criteria) {
        return new ResponseEntity<>(tdTrainService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出培训情况表数据")
    @ApiOperation("导出培训情况表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('train:list')")
    public void download(HttpServletResponse response, TdTrainQueryCriteria criteria) throws IOException {
        tdTrainService.download(tdTrainService.listAll(criteria), response);
    }
}
