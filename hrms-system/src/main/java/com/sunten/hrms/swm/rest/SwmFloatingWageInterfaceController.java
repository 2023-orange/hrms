package com.sunten.hrms.swm.rest;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.swm.dto.SwmFloatingWageQueryCriteria;
import com.sunten.hrms.swm.vo.AllocatePerformancePayVo;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmFloatingWageInterface;
import com.sunten.hrms.swm.dto.SwmFloatingWageInterfaceQueryCriteria;
import com.sunten.hrms.swm.service.SwmFloatingWageInterfaceService;
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
import java.util.stream.Collectors;

/**
 * <p>
 * 浮动工资接口表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@RestController
@Api(tags = "浮动工资接口表")
@RequestMapping("/api/swm/floatingWageInterface")
public class SwmFloatingWageInterfaceController {
    private static final String ENTITY_NAME = "floatingWageInterface";
    private final SwmFloatingWageInterfaceService swmFloatingWageInterfaceService;

    public SwmFloatingWageInterfaceController(SwmFloatingWageInterfaceService swmFloatingWageInterfaceService) {
        this.swmFloatingWageInterfaceService = swmFloatingWageInterfaceService;
    }

    @Log("新增浮动工资接口表")
    @ApiOperation("新增浮动工资接口表")
    @PostMapping
    @PreAuthorize("@el.check('floatingWageInterface:add')")
    public ResponseEntity create(@Validated @RequestBody SwmFloatingWageInterface floatingWageInterface) {
        if (floatingWageInterface.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmFloatingWageInterfaceService.insert(floatingWageInterface), HttpStatus.CREATED);
    }

    @Log("删除浮动工资接口表")
    @ApiOperation("删除浮动工资接口表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('floatingWageInterface:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmFloatingWageInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该浮动工资接口表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改浮动工资接口表")
    @ApiOperation("修改浮动工资接口表")
    @PutMapping
    @PreAuthorize("@el.check('floatingWageInterface:edit')")
    public ResponseEntity update(@Validated(SwmFloatingWageInterface.Update.class) @RequestBody SwmFloatingWageInterface floatingWageInterface) {
        swmFloatingWageInterfaceService.update(floatingWageInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个浮动工资接口表")
    @ApiOperation("获取单个浮动工资接口表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('floatingWageInterface:list')")
    public ResponseEntity getFloatingWageInterface(@PathVariable Long id) {
        return new ResponseEntity<>(swmFloatingWageInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询浮动工资接口表（分页）")
    @ApiOperation("查询浮动工资接口表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('floatingWageInterface:list')")
    public ResponseEntity getFloatingWageInterfacePage(SwmFloatingWageInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmFloatingWageInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询浮动工资接口表（不分页）")
    @ApiOperation("查询浮动工资接口表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('floatingWageInterface:list')")
    public ResponseEntity getFloatingWageInterfaceNoPaging(SwmFloatingWageInterfaceQueryCriteria criteria) {
    return new ResponseEntity<>(swmFloatingWageInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出浮动工资接口表数据")
    @ApiOperation("导出浮动工资接口表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('floatingWageInterface:list')")
    public void download(HttpServletResponse response, SwmFloatingWageInterfaceQueryCriteria criteria) throws IOException {
        swmFloatingWageInterfaceService.download(swmFloatingWageInterfaceService.listAll(criteria), response);
    }


    @Log("浮动工资导入")
    @ApiOperation("浮动工资导入")
    @PutMapping(value = "/insertAllocatePerformancePay")
    @PreAuthorize("@el.check('sfwAllocatePerformance:import')")
    public ResponseEntity insertAllocatePerformancePay(@RequestBody AllocatePerformancePayVo allocatePerformancePayVo) {
        return new ResponseEntity<>(swmFloatingWageInterfaceService.insertExcel(allocatePerformancePayVo.getSwmFloatingWageInterfaces(), allocatePerformancePayVo.getReImportFlag()), HttpStatus.OK);
    }

    @Log("浮动工资根据导入结果获取汇总")
    @ApiOperation("浮动工资根据导入结果获取汇总")
    @PutMapping("/getFloatSummaryByImportList")
    @PreAuthorize("@el.check('floatingWageInterface:list')")
    public ResponseEntity getFloatSummaryByImportList(@RequestBody SwmFloatingWageInterfaceQueryCriteria swmFloatingWageInterfaceQueryCriteria) {
        if (null == swmFloatingWageInterfaceQueryCriteria.getSwmFloatingWageInterfaceList() || swmFloatingWageInterfaceQueryCriteria.getSwmFloatingWageInterfaceList().size() == 0) {
            throw new InfoCheckWarningMessException("浮动工资根据导入结果获取汇总失败");
        }
        return new ResponseEntity<>(swmFloatingWageInterfaceService.getSummaryByImportList(swmFloatingWageInterfaceQueryCriteria.getSwmFloatingWageInterfaceList().get(0).getIncomePeriod(),
                swmFloatingWageInterfaceQueryCriteria.getSwmFloatingWageInterfaceList().stream().map(SwmFloatingWageInterface::getWorkCard).collect(Collectors.toSet()), swmFloatingWageInterfaceQueryCriteria.getGroupIds()), HttpStatus.OK);
    }

}
