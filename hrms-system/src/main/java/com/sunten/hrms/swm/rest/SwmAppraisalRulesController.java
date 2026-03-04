package com.sunten.hrms.swm.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmAppraisalRules;
import com.sunten.hrms.swm.dto.SwmAppraisalRulesQueryCriteria;
import com.sunten.hrms.swm.service.SwmAppraisalRulesService;
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
 * 考核规则 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@RestController
@Api(tags = "考核规则")
@RequestMapping("/api/swm/appraisalRules")
public class SwmAppraisalRulesController {
    private static final String ENTITY_NAME = "appraisalRules";
    private final SwmAppraisalRulesService swmAppraisalRulesService;

    public SwmAppraisalRulesController(SwmAppraisalRulesService swmAppraisalRulesService) {
        this.swmAppraisalRulesService = swmAppraisalRulesService;
    }

    @Log("新增考核规则")
    @ApiOperation("新增考核规则")
    @PostMapping
    @PreAuthorize("@el.check('appraisalRules:add')")
    public ResponseEntity create(@Validated @RequestBody SwmAppraisalRules appraisalRules) {


        if (appraisalRules.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmAppraisalRulesService.insert(appraisalRules), HttpStatus.CREATED);
    }

    @Log("删除考核规则")
    @ApiOperation("删除考核规则")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('appraisalRules:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmAppraisalRulesService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该考核规则存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改考核规则")
    @ApiOperation("修改考核规则")
    @PutMapping
    @PreAuthorize("@el.check('appraisalRules:edit')")
    public ResponseEntity update(@Validated(SwmAppraisalRules.Update.class) @RequestBody SwmAppraisalRules appraisalRules) {
        swmAppraisalRulesService.update(appraisalRules);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("失效考核规则")
    @ApiOperation("失效考核规则")
    @PutMapping(value="/updateByWorkFlag")
    @PreAuthorize("@el.check('appraisalRules:edit')")
    public ResponseEntity updateByWorkFlag(@Validated(SwmAppraisalRules.Update.class) @RequestBody SwmAppraisalRules appraisalRules) {
        appraisalRules.setWorkFlag(false);
        swmAppraisalRulesService.update(appraisalRules);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @ErrorLog("获取单个考核规则")
    @ApiOperation("获取单个考核规则")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('appraisalRules:list')")
    public ResponseEntity getAppraisalRules(@PathVariable Long id) {
        return new ResponseEntity<>(swmAppraisalRulesService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询考核规则（分页）")
    @ApiOperation("查询考核规则（分页）")
    @GetMapping
    @PreAuthorize("@el.check('appraisalRules:list')")
    public ResponseEntity getAppraisalRulesPage(SwmAppraisalRulesQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmAppraisalRulesService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询考核规则（不分页）")
    @ApiOperation("查询考核规则（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('appraisalRules:list')")
    public ResponseEntity getAppraisalRulesNoPaging(SwmAppraisalRulesQueryCriteria criteria) {
    return new ResponseEntity<>(swmAppraisalRulesService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出考核规则数据")
    @ApiOperation("导出考核规则数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('appraisalRules:list')")
    public void download(HttpServletResponse response, SwmAppraisalRulesQueryCriteria criteria) throws IOException {
        swmAppraisalRulesService.download(swmAppraisalRulesService.listAll(criteria), response);
    }
}
