package com.sunten.hrms.swm.rest;

import cn.hutool.json.JSONArray;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmQuarterlyAssessment;
import com.sunten.hrms.swm.dto.SwmQuarterlyAssessmentQueryCriteria;
import com.sunten.hrms.swm.service.SwmQuarterlyAssessmentService;
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
 * 季度考核表（一个季度生成一条，主要用作季度考核查询） 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@RestController
@Api(tags = "季度考核表（一个季度生成一条，主要用作季度考核查询）")
@RequestMapping("/api/swm/quarterlyAssessment")
public class SwmQuarterlyAssessmentController {
    private static final String ENTITY_NAME = "quarterlyAssessment";
    private final SwmQuarterlyAssessmentService swmQuarterlyAssessmentService;


    public SwmQuarterlyAssessmentController(SwmQuarterlyAssessmentService swmQuarterlyAssessmentService) {
        this.swmQuarterlyAssessmentService = swmQuarterlyAssessmentService;

    }

    @Log("新增季度考核表（一个季度生成一条，主要用作季度考核查询）")
    @ApiOperation("新增季度考核表（一个季度生成一条，主要用作季度考核查询）")
    @PostMapping
    @PreAuthorize("@el.check('quarterlyAssessment:add')")
    public ResponseEntity create(@Validated @RequestBody SwmQuarterlyAssessment quarterlyAssessment) {
        if (quarterlyAssessment.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmQuarterlyAssessmentService.insert(quarterlyAssessment), HttpStatus.CREATED);
    }

//    @Log("删除季度考核表（一个季度生成一条，主要用作季度考核查询）")
//    @ApiOperation("删除季度考核表（一个季度生成一条，主要用作季度考核查询）")
//    @DeleteMapping(value = "/{id}")
//    @PreAuthorize("@el.check('quarterlyAssessment:del')")
//    public ResponseEntity delete(@PathVariable Long id) {
//        try {
//            swmQuarterlyAssessmentService.delete(id);
//        } catch (Throwable e) {
//            ThrowableUtil.throwForeignKeyException(e, "该季度考核表（一个季度生成一条，主要用作季度考核查询）存在 关联，请取消关联后再试");
//        }
//        return new ResponseEntity(HttpStatus.OK);
//    }

    @Log("修改季度考核表（一个季度生成一条，主要用作季度考核查询）")
    @ApiOperation("修改季度考核表（一个季度生成一条，主要用作季度考核查询）")
    @PutMapping
    @PreAuthorize("@el.check('quarterlyAssessment:edit')")
    public ResponseEntity update(@Validated(SwmQuarterlyAssessment.Update.class) @RequestBody SwmQuarterlyAssessment quarterlyAssessment) {
        swmQuarterlyAssessmentService.update(quarterlyAssessment);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个季度考核表（一个季度生成一条，主要用作季度考核查询）")
    @ApiOperation("获取单个季度考核表（一个季度生成一条，主要用作季度考核查询）")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('quarterlyAssessment:list')")
    public ResponseEntity getQuarterlyAssessment(@PathVariable Long id) {
        return new ResponseEntity<>(swmQuarterlyAssessmentService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询季度考核表（一个季度生成一条，主要用作季度考核查询）（分页）")
    @ApiOperation("查询季度考核表（一个季度生成一条，主要用作季度考核查询）（分页）")
    @GetMapping
    @PreAuthorize("@el.check('quarterlyAssessment:list')")
    public ResponseEntity getQuarterlyAssessmentPage(SwmQuarterlyAssessmentQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        setCriteria(criteria);
        return new ResponseEntity<>(swmQuarterlyAssessmentService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询季度考核表（一个季度生成一条，主要用作季度考核查询）（不分页）")
    @ApiOperation("查询季度考核表（一个季度生成一条，主要用作季度考核查询）（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('quarterlyAssessment:list')")
    public ResponseEntity getQuarterlyAssessmentNoPaging(SwmQuarterlyAssessmentQueryCriteria criteria) {
        setCriteria(criteria);
        return new ResponseEntity<>(swmQuarterlyAssessmentService.listAll(criteria), HttpStatus.OK);
    }

    public void setCriteria(SwmQuarterlyAssessmentQueryCriteria criteria) {

    }

    @ErrorLog("导出季度考核表（一个季度生成一条，主要用作季度考核查询）数据")
    @ApiOperation("导出季度考核表（一个季度生成一条，主要用作季度考核查询）数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('quarterlyAssessment:list')")
    public void download(HttpServletResponse response, SwmQuarterlyAssessmentQueryCriteria criteria) throws IOException {
        setCriteria(criteria);
        swmQuarterlyAssessmentService.download(swmQuarterlyAssessmentService.listAll(criteria), response);
    }

    @ErrorLog("季度考核所得期间获取")
    @ApiOperation("季度考核所得期间获取")
    @GetMapping("/getQuarterPeriodList")
    @PreAuthorize("@el.check('quarterlyAssessment:list')")
    public ResponseEntity getQuarterPeriodList() {
        return new ResponseEntity<>(swmQuarterlyAssessmentService.getQuarterPeriodList(null), HttpStatus.OK);
    }

    @Log("生成季度考核quarter")
    @ApiOperation("生成季度考核quarter")
    @PostMapping(value = "/generateQuarterlyAssessment")
    @PreAuthorize("@el.check('quarterlyAssessment:add')")
    public ResponseEntity generateQuarterlyAssessment(@RequestBody String period) {
        // 返回的数据不分页
        return new ResponseEntity<>(swmQuarterlyAssessmentService.createQuarterlyAssessment(period), HttpStatus.CREATED);
    }

    @Log("根据期间删除季度考核")
    @ApiOperation("根据期间删除季度考核")
    @DeleteMapping(value = "/removeQuarterlyByPeriod")
    @PreAuthorize("@el.check('quarterlyAssessment:del')")
    public ResponseEntity removeQuarterlyByPeriod(@RequestBody String period) {
        swmQuarterlyAssessmentService.removeQuarterlyByPeriod(period);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("季度考核等级批量设置")
    @ApiOperation("季度考核等级批量设置")
    @PutMapping("/submitEmployeeQuarterly")
    @PreAuthorize("@el.check('quarterlyAssessment:edit')")
    public ResponseEntity submitEmployeeQuarterly(@RequestBody List<SwmQuarterlyAssessment> swmQuarterlyAssessments){

        swmQuarterlyAssessmentService.batchUpdateQuarterAssessmentLevel(swmQuarterlyAssessments);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }




}
