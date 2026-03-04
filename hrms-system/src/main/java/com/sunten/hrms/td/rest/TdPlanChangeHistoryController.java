package com.sunten.hrms.td.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.td.domain.TdPlanChangeHistory;
import com.sunten.hrms.td.dto.TdPlanChangeHistoryQueryCriteria;
import com.sunten.hrms.td.service.TdPlanChangeHistoryService;
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
 * 培训计划变更历史 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-06-16
 */
@RestController
@Api(tags = "培训计划变更历史")
@RequestMapping("/api/td/planChangeHistory")
public class TdPlanChangeHistoryController {
    private static final String ENTITY_NAME = "planChangeHistory";
    private final TdPlanChangeHistoryService tdPlanChangeHistoryService;

    public TdPlanChangeHistoryController(TdPlanChangeHistoryService tdPlanChangeHistoryService) {
        this.tdPlanChangeHistoryService = tdPlanChangeHistoryService;
    }

    @Log("新增培训计划变更历史")
    @ApiOperation("新增培训计划变更历史")
    @PostMapping
    @PreAuthorize("@el.check('plan:add')")
    public ResponseEntity create(@Validated @RequestBody TdPlanChangeHistory planChangeHistory) {
        if (planChangeHistory.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdPlanChangeHistoryService.insert(planChangeHistory), HttpStatus.CREATED);
    }

    @Log("删除培训计划变更历史")
    @ApiOperation("删除培训计划变更历史")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('plan:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdPlanChangeHistoryService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该培训计划变更历史存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改培训计划变更历史")
    @ApiOperation("修改培训计划变更历史")
    @PutMapping
    @PreAuthorize("@el.check('plan:edit')")
    public ResponseEntity update(@Validated(TdPlanChangeHistory.Update.class) @RequestBody TdPlanChangeHistory planChangeHistory) {
        tdPlanChangeHistoryService.update(planChangeHistory);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个培训计划变更历史")
    @ApiOperation("获取单个培训计划变更历史")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('plan:list')")
    public ResponseEntity getPlanChangeHistory(@PathVariable Long id) {
        return new ResponseEntity<>(tdPlanChangeHistoryService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询培训计划变更历史（分页）")
    @ApiOperation("查询培训计划变更历史（分页）")
    @GetMapping
    @PreAuthorize("@el.check('plan:list')")
    public ResponseEntity getPlanChangeHistoryPage(TdPlanChangeHistoryQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(tdPlanChangeHistoryService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询培训计划变更历史（不分页）")
    @ApiOperation("查询培训计划变更历史（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('plan:list')")
    public ResponseEntity getPlanChangeHistoryNoPaging(TdPlanChangeHistoryQueryCriteria criteria) {
    return new ResponseEntity<>(tdPlanChangeHistoryService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出培训计划变更历史数据")
    @ApiOperation("导出培训计划变更历史数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('plan:list')")
    public void download(HttpServletResponse response, TdPlanChangeHistoryQueryCriteria criteria) throws IOException {
        tdPlanChangeHistoryService.download(tdPlanChangeHistoryService.listAll(criteria), response);
    }

    @Log("更新培训实施是否通过字段，OA用")
    @ApiOperation("更新培训实施是否通过字段，OA用")
    @PutMapping(value = "/updatePassAndNotPass")
    public ResponseEntity updatePassAndNotPass(@RequestBody TdPlanChangeHistory planChangeHistory) {
        tdPlanChangeHistoryService.updatePassOrNotPass(planChangeHistory);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
