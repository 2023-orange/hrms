package com.sunten.hrms.td.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.td.domain.TdPlanInspectionSituation;
import com.sunten.hrms.td.dto.TdPlanInspectionSituationQueryCriteria;
import com.sunten.hrms.td.service.TdPlanInspectionSituationService;
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
 * 培训考核情况 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2022-03-11
 */
@RestController
@Api(tags = "培训考核情况")
@RequestMapping("/api/td/planInspectionSituation")
public class TdPlanInspectionSituationController {
    private static final String ENTITY_NAME = "planInspectionSituation";
    private final TdPlanInspectionSituationService tdPlanInspectionSituationService;

    public TdPlanInspectionSituationController(TdPlanInspectionSituationService tdPlanInspectionSituationService) {
        this.tdPlanInspectionSituationService = tdPlanInspectionSituationService;
    }

    @Log("新增培训考核情况")
    @ApiOperation("新增培训考核情况")
    @PostMapping
    @PreAuthorize("@el.check('planInspectionSituation:add')")
    public ResponseEntity create(@Validated @RequestBody TdPlanInspectionSituation planInspectionSituation) {
        if (planInspectionSituation.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdPlanInspectionSituationService.insert(planInspectionSituation), HttpStatus.CREATED);
    }

    @Log("删除培训考核情况")
    @ApiOperation("删除培训考核情况")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('planInspectionSituation:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdPlanInspectionSituationService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该培训考核情况存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改培训考核情况")
    @ApiOperation("修改培训考核情况")
    @PutMapping
    @PreAuthorize("@el.check('planInspectionSituation:edit')")
    public ResponseEntity update(@Validated(TdPlanInspectionSituation.Update.class) @RequestBody TdPlanInspectionSituation planInspectionSituation) {
        tdPlanInspectionSituationService.update(planInspectionSituation);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个培训考核情况")
    @ApiOperation("获取单个培训考核情况")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('planInspectionSituation:list')")
    public ResponseEntity getPlanInspectionSituation(@PathVariable Long id) {
        return new ResponseEntity<>(tdPlanInspectionSituationService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询培训考核情况（分页）")
    @ApiOperation("查询培训考核情况（分页）")
    @GetMapping
    @PreAuthorize("@el.check('planInspectionSituation:list')")
    public ResponseEntity getPlanInspectionSituationPage(TdPlanInspectionSituationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(tdPlanInspectionSituationService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询培训考核情况（不分页）")
    @ApiOperation("查询培训考核情况（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('planInspectionSituation:list')")
    public ResponseEntity getPlanInspectionSituationNoPaging(TdPlanInspectionSituationQueryCriteria criteria) {
    return new ResponseEntity<>(tdPlanInspectionSituationService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出培训考核情况数据")
    @ApiOperation("导出培训考核情况数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('planInspectionSituation:list')")
    public void download(HttpServletResponse response, TdPlanInspectionSituationQueryCriteria criteria) throws IOException {
        tdPlanInspectionSituationService.download(tdPlanInspectionSituationService.listAll(criteria), response);
    }
}
