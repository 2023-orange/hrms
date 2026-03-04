package com.sunten.hrms.td.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.td.domain.TdPlanEmployeeInterface;
import com.sunten.hrms.td.dto.TdPlanEmployeeInterfaceQueryCriteria;
import com.sunten.hrms.td.service.TdPlanEmployeeInterfaceService;
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
 * 培训参训人员接口表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-09-24
 */
@RestController
@Api(tags = "培训参训人员接口表")
@RequestMapping("/api/td/planEmployeeInterface")
public class TdPlanEmployeeInterfaceController {
    private static final String ENTITY_NAME = "planEmployeeInterface";
    private final TdPlanEmployeeInterfaceService tdPlanEmployeeInterfaceService;

    public TdPlanEmployeeInterfaceController(TdPlanEmployeeInterfaceService tdPlanEmployeeInterfaceService) {
        this.tdPlanEmployeeInterfaceService = tdPlanEmployeeInterfaceService;
    }

    @Log("新增培训参训人员接口表")
    @ApiOperation("新增培训参训人员接口表")
    @PostMapping
    @PreAuthorize("@el.check('planEmployeeInterface:add')")
    public ResponseEntity create(@Validated @RequestBody TdPlanEmployeeInterface planEmployeeInterface) {
        if (planEmployeeInterface.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdPlanEmployeeInterfaceService.insert(planEmployeeInterface), HttpStatus.CREATED);
    }

    @Log("删除培训参训人员接口表")
    @ApiOperation("删除培训参训人员接口表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('planEmployeeInterface:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdPlanEmployeeInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该培训参训人员接口表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改培训参训人员接口表")
    @ApiOperation("修改培训参训人员接口表")
    @PutMapping
    @PreAuthorize("@el.check('planEmployeeInterface:edit')")
    public ResponseEntity update(@Validated(TdPlanEmployeeInterface.Update.class) @RequestBody TdPlanEmployeeInterface planEmployeeInterface) {
        tdPlanEmployeeInterfaceService.update(planEmployeeInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个培训参训人员接口表")
    @ApiOperation("获取单个培训参训人员接口表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('planEmployeeInterface:list')")
    public ResponseEntity getPlanEmployeeInterface(@PathVariable Long id) {
        return new ResponseEntity<>(tdPlanEmployeeInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询培训参训人员接口表（分页）")
    @ApiOperation("查询培训参训人员接口表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('planEmployeeInterface:list')")
    public ResponseEntity getPlanEmployeeInterfacePage(TdPlanEmployeeInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(tdPlanEmployeeInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询培训参训人员接口表（不分页）")
    @ApiOperation("查询培训参训人员接口表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('planEmployeeInterface:list')")
    public ResponseEntity getPlanEmployeeInterfaceNoPaging(TdPlanEmployeeInterfaceQueryCriteria criteria) {
    return new ResponseEntity<>(tdPlanEmployeeInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出培训参训人员接口表数据")
    @ApiOperation("导出培训参训人员接口表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('planEmployeeInterface:list')")
    public void download(HttpServletResponse response, TdPlanEmployeeInterfaceQueryCriteria criteria) throws IOException {
        tdPlanEmployeeInterfaceService.download(tdPlanEmployeeInterfaceService.listAll(criteria), response);
    }

    @Log("参训人员名单导入")
    @ApiOperation("参训人员名单导入")
    @PutMapping(value = "/importPlanEmployeeExcel")
    @PreAuthorize("@el.check('plan:add')")
    public ResponseEntity importPlanEmployeeExcel(@RequestBody List<TdPlanEmployeeInterface> tdPlanEmployeeInterfaces) {
        return new ResponseEntity<>(tdPlanEmployeeInterfaceService.insertExcel(tdPlanEmployeeInterfaces), HttpStatus.OK);
    }
}
