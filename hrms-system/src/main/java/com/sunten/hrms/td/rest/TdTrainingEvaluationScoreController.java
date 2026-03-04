package com.sunten.hrms.td.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.td.domain.TdTrainingEvaluationScore;
import com.sunten.hrms.td.dto.TdTrainingEvaluationScoreQueryCriteria;
import com.sunten.hrms.td.service.TdTrainingEvaluationScoreService;
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
 * 培训评价分数表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2022-03-10
 */
@RestController
@Api(tags = "培训评价分数表")
@RequestMapping("/api/td/trainingEvaluationScore")
public class TdTrainingEvaluationScoreController {
    private static final String ENTITY_NAME = "trainingEvaluationScore";
    private final TdTrainingEvaluationScoreService tdTrainingEvaluationScoreService;

    public TdTrainingEvaluationScoreController(TdTrainingEvaluationScoreService tdTrainingEvaluationScoreService) {
        this.tdTrainingEvaluationScoreService = tdTrainingEvaluationScoreService;
    }

    @Log("新增培训评价分数表")
    @ApiOperation("新增培训评价分数表")
    @PostMapping
    @PreAuthorize("@el.check('trainingEvaluationScore:add')")
    public ResponseEntity create(@Validated @RequestBody TdTrainingEvaluationScore trainingEvaluationScore) {
        if (trainingEvaluationScore.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdTrainingEvaluationScoreService.insert(trainingEvaluationScore), HttpStatus.CREATED);
    }

    @Log("删除培训评价分数表")
    @ApiOperation("删除培训评价分数表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('trainingEvaluationScore:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdTrainingEvaluationScoreService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该培训评价分数表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改培训评价分数表")
    @ApiOperation("修改培训评价分数表")
    @PutMapping
    @PreAuthorize("@el.check('trainingEvaluationScore:edit')")
    public ResponseEntity update(@Validated(TdTrainingEvaluationScore.Update.class) @RequestBody TdTrainingEvaluationScore trainingEvaluationScore) {
        tdTrainingEvaluationScoreService.update(trainingEvaluationScore);
        return new ResponseEntity<>(tdTrainingEvaluationScoreService.getByKey(trainingEvaluationScore.getId()), HttpStatus.OK);
    }

    @ErrorLog("获取单个培训评价分数表")
    @ApiOperation("获取单个培训评价分数表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('trainingEvaluationScore:list')")
    public ResponseEntity getTrainingEvaluationScore(@PathVariable Long id) {
        return new ResponseEntity<>(tdTrainingEvaluationScoreService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询培训评价分数表（分页）")
    @ApiOperation("查询培训评价分数表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('trainingEvaluationScore:list')")
    public ResponseEntity getTrainingEvaluationScorePage(TdTrainingEvaluationScoreQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(tdTrainingEvaluationScoreService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询培训评价分数表（不分页）")
    @ApiOperation("查询培训评价分数表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('trainingEvaluationScore:list')")
    public ResponseEntity getTrainingEvaluationScoreNoPaging(TdTrainingEvaluationScoreQueryCriteria criteria) {
    return new ResponseEntity<>(tdTrainingEvaluationScoreService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出培训评价分数表数据")
    @ApiOperation("导出培训评价分数表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('trainingEvaluationScore:list')")
    public void download(HttpServletResponse response, TdTrainingEvaluationScoreQueryCriteria criteria) throws IOException {
        tdTrainingEvaluationScoreService.download(tdTrainingEvaluationScoreService.listAll(criteria), response);
    }
}
