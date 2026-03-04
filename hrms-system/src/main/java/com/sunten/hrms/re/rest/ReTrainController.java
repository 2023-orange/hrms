package com.sunten.hrms.re.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.re.domain.ReTrain;
import com.sunten.hrms.re.dto.ReTrainQueryCriteria;
import com.sunten.hrms.re.service.ReTrainService;
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
import java.util.List;

/**
 * <p>
 * 培训记录表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@RestController
@Api(tags = "培训记录表")
@RequestMapping("/api/re/train")
public class ReTrainController {
    private static final String ENTITY_NAME = "train";
    private final ReTrainService reTrainService;

    public ReTrainController(ReTrainService reTrainService) {
        this.reTrainService = reTrainService;
    }

    @Log("新增培训记录表")
    @ApiOperation("新增培训记录表")
    @PostMapping
    @PreAuthorize("@el.check('train:add')")
    public ResponseEntity create(@Validated @RequestBody ReTrain train) {
        if (train.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(reTrainService.insert(train), HttpStatus.CREATED);
    }

    @Log("删除培训记录表")
    @ApiOperation("删除培训记录表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('train:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reTrainService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该培训记录表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改培训记录表")
    @ApiOperation("修改培训记录表")
    @PutMapping
    @PreAuthorize("@el.check('train:edit')")
    public ResponseEntity update(@Validated(ReTrain.Update.class) @RequestBody ReTrain train) {
        reTrainService.update(train);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个培训记录表")
    @ApiOperation("获取单个培训记录表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('train:list')")
    public ResponseEntity getTrain(@PathVariable Long id) {
        return new ResponseEntity<>(reTrainService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询培训记录表（分页）")
    @ApiOperation("查询培训记录表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('train:list','recruitment:list')")
    public ResponseEntity getTrainPage(ReTrainQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reTrainService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询培训记录表（不分页）")
    @ApiOperation("查询培训记录表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('train:list','recruitment:list')")
    public ResponseEntity getTrainNoPaging(ReTrainQueryCriteria criteria) {
        return new ResponseEntity<>(reTrainService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出培训记录表数据")
    @ApiOperation("导出培训记录表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('train:list')")
    public void download(HttpServletResponse response, ReTrainQueryCriteria criteria) throws IOException {
        reTrainService.download(reTrainService.listAll(criteria), response);
    }

    @Log("批量编辑培训记录表")
    @ApiOperation("批量编辑培训记录表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('train:add','train:edit','recruitment:edit','recruitment:add')")
    public ResponseEntity batchSave(@Validated @RequestBody List<ReTrain> trains) {
        if (trains == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(reTrainService.batchInsert(trains, null), HttpStatus.CREATED);
    }
}
