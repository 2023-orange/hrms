package com.sunten.hrms.swm.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmEmployeeHistory;
import com.sunten.hrms.swm.dto.SwmEmployeeHistoryQueryCriteria;
import com.sunten.hrms.swm.service.SwmEmployeeHistoryService;
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
 * 员工信息历史表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@RestController
@Api(tags = "员工信息历史表")
@RequestMapping("/api/swm/employeeHistory")
public class SwmEmployeeHistoryController {
    private static final String ENTITY_NAME = "employeeHistory";
    private final SwmEmployeeHistoryService swmEmployeeHistoryService;

    public SwmEmployeeHistoryController(SwmEmployeeHistoryService swmEmployeeHistoryService) {
        this.swmEmployeeHistoryService = swmEmployeeHistoryService;
    }

    @Log("新增员工信息历史表")
    @ApiOperation("新增员工信息历史表")
    @PostMapping
    @PreAuthorize("@el.check('employeeHistory:add')")
    public ResponseEntity create(@Validated @RequestBody SwmEmployeeHistory employeeHistory) {
        if (employeeHistory.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmEmployeeHistoryService.insert(employeeHistory), HttpStatus.CREATED);
    }

    @Log("删除员工信息历史表")
    @ApiOperation("删除员工信息历史表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeHistory:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmEmployeeHistoryService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该员工信息历史表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改员工信息历史表")
    @ApiOperation("修改员工信息历史表")
    @PutMapping
    @PreAuthorize("@el.check('employeeHistory:edit')")
    public ResponseEntity update(@Validated(SwmEmployeeHistory.Update.class) @RequestBody SwmEmployeeHistory employeeHistory) {
        swmEmployeeHistoryService.update(employeeHistory);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个员工信息历史表")
    @ApiOperation("获取单个员工信息历史表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeHistory:list')")
    public ResponseEntity getEmployeeHistory(@PathVariable Long id) {
        return new ResponseEntity<>(swmEmployeeHistoryService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询员工信息历史表（分页）")
    @ApiOperation("查询员工信息历史表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeHistory:list')")
    public ResponseEntity getEmployeeHistoryPage(SwmEmployeeHistoryQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmEmployeeHistoryService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询员工信息历史表（不分页）")
    @ApiOperation("查询员工信息历史表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeHistory:list')")
    public ResponseEntity getEmployeeHistoryNoPaging(SwmEmployeeHistoryQueryCriteria criteria) {
    return new ResponseEntity<>(swmEmployeeHistoryService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("查询员工信息历史表（工牌号）")
    @ApiOperation("查询员工信息历史表（工牌号）")
    @GetMapping(value = "/HistoryByWorkCard")
    @PreAuthorize("@el.check('employeeHistory:list','employee:list')")
    public ResponseEntity getSwmEmployeeHistoryByWorkCard(String workCard) {
        return new ResponseEntity<>(swmEmployeeHistoryService.getSwmEmployeeHistoryByWorkCard(workCard), HttpStatus.OK);
    }

    @ErrorLog("导出员工信息历史表数据")
    @ApiOperation("导出员工信息历史表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeHistory:list')")
    public void download(HttpServletResponse response, SwmEmployeeHistoryQueryCriteria criteria) throws IOException {
        swmEmployeeHistoryService.download(swmEmployeeHistoryService.listAll(criteria), response);
    }
}
