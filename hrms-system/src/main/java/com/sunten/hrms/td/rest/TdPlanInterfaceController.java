package com.sunten.hrms.td.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.td.domain.TdPlanInterface;
import com.sunten.hrms.td.dto.TdPlanInterfaceQueryCriteria;
import com.sunten.hrms.td.service.TdPlanInterfaceService;
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
 * 培训计划接口表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-05-23
 */
@RestController
@Api(tags = "培训计划接口表")
@RequestMapping("/api/td/planInterface")
public class TdPlanInterfaceController {
    private static final String ENTITY_NAME = "planInterface";
    private final TdPlanInterfaceService tdPlanInterfaceService;

    public TdPlanInterfaceController(TdPlanInterfaceService tdPlanInterfaceService) {
        this.tdPlanInterfaceService = tdPlanInterfaceService;
    }

    @Log("新增培训计划接口表")
    @ApiOperation("新增培训计划接口表")
    @PostMapping
    @PreAuthorize("@el.check('planInterface:add')")
    public ResponseEntity create(@Validated @RequestBody TdPlanInterface planInterface) {
        if (planInterface.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdPlanInterfaceService.insert(planInterface), HttpStatus.CREATED);
    }

    @Log("删除培训计划接口表")
    @ApiOperation("删除培训计划接口表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('planInterface:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdPlanInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该培训计划接口表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改培训计划接口表")
    @ApiOperation("修改培训计划接口表")
    @PutMapping
    @PreAuthorize("@el.check('planInterface:edit')")
    public ResponseEntity update(@Validated(TdPlanInterface.Update.class) @RequestBody TdPlanInterface planInterface) {
        tdPlanInterfaceService.update(planInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个培训计划接口表")
    @ApiOperation("获取单个培训计划接口表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('planInterface:list')")
    public ResponseEntity getPlanInterface(@PathVariable Long id) {
        return new ResponseEntity<>(tdPlanInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询培训计划接口表（分页）")
    @ApiOperation("查询培训计划接口表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('plan:list')")
    public ResponseEntity getPlanInterfacePage(TdPlanInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(tdPlanInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询培训计划接口表（不分页）")
    @ApiOperation("查询培训计划接口表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('plan:list')")
    public ResponseEntity getPlanInterfaceNoPaging(TdPlanInterfaceQueryCriteria criteria) {
    return new ResponseEntity<>(tdPlanInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出培训计划接口表数据")
    @ApiOperation("导出培训计划接口表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('planInterface:list')")
    public void download(HttpServletResponse response, TdPlanInterfaceQueryCriteria criteria) throws IOException {
        tdPlanInterfaceService.download(tdPlanInterfaceService.listAll(criteria), response);
    }

    @ErrorLog("导入年度计划")
    @ApiOperation("导入年度计划数据")
    @PutMapping(value = "/insertExcel")
    @PreAuthorize("@el.check('plan:add')")
    public ResponseEntity insertExcel(@RequestBody List<TdPlanInterface> tdPlanInterfaces) {
        return new ResponseEntity<>(tdPlanInterfaceService.insertExcel(tdPlanInterfaces), HttpStatus.OK);
    }
}
