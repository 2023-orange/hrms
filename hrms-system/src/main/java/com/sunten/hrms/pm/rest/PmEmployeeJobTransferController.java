package com.sunten.hrms.pm.rest;

import cn.hutool.json.JSONArray;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.pm.domain.PmEmployeeJobTransfer;
import com.sunten.hrms.pm.dto.PmEmployeeJobTransferQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeJobTransferService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
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
import java.util.ArrayList;

/**
 * <p>
 * 岗位调动表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "岗位调动表")
@RequestMapping("/api/pm/employeeJobTransfer")
public class PmEmployeeJobTransferController {
    private static final String ENTITY_NAME = "employeeJobTransfer";
    private final PmEmployeeJobTransferService pmEmployeeJobTransferService;
    private final FndDataScope fndDataScope;

    public PmEmployeeJobTransferController(PmEmployeeJobTransferService pmEmployeeJobTransferService, FndDataScope fndDataScope) {
        this.pmEmployeeJobTransferService = pmEmployeeJobTransferService;
        this.fndDataScope = fndDataScope;
    }

    @Log("新增岗位调动表")
    @ApiOperation("新增岗位调动表")
    @PostMapping
    @PreAuthorize("@el.check('employeeJobTransfer:add','employee:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeeJobTransfer employeeJobTransfer) {
        if (employeeJobTransfer.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeJobTransferService.insert(employeeJobTransfer), HttpStatus.CREATED);
    }

    @Log("删除岗位调动表")
    @ApiOperation("删除岗位调动表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeJobTransfer:del','employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        pmEmployeeJobTransferService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改岗位调动表")
    @ApiOperation("修改岗位调动表")
    @PutMapping
    @PreAuthorize("@el.check('employeeJobTransfer:edit','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployeeJobTransfer.Update.class) @RequestBody PmEmployeeJobTransfer employeeJobTransfer) {
        pmEmployeeJobTransferService.update(employeeJobTransfer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个岗位调动表")
    @ApiOperation("获取单个岗位调动表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeJobTransfer:list','employee:list')")
    public ResponseEntity getEmployeeJobTransfer(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeJobTransferService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询岗位调动表（分页）")
    @ApiOperation("查询岗位调动表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeJobTransfer:list','employee:list')")
    public ResponseEntity getEmployeeJobTransferPage(PmEmployeeJobTransferQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeJobTransferService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询岗位调动表（不分页）")
    @ApiOperation("查询岗位调动表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeJobTransfer:list','employee:list')")
    public ResponseEntity getEmployeeJobTransferNoPaging(PmEmployeeJobTransferQueryCriteria criteria) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeJobTransferService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("查询岗位调动表（不分页）")
    @ApiOperation("查询岗位调动表（不分页）")
    @GetMapping(value = "/noPaging/all")
    @PreAuthorize("@el.check('employeeJobTransfer:list','employee:list')")
    public ResponseEntity getEmployeeJobTransferNoPagingAll(PmEmployeeJobTransferQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeJobTransferService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出岗位调动表数据")
    @ApiOperation("导出岗位调动表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeJobTransfer:list','employee:list')")
    public void download(HttpServletResponse response, PmEmployeeJobTransferQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) throws IOException {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            FileUtil.downloadExcel(new ArrayList<>(), response);
        } else {
            criteria.setDeptIds(dataScopeVo.getDeptIds());
            criteria.setEmployeeId(dataScopeVo.getEmployeeId());
            pmEmployeeJobTransferService.download(criteria, pageable, response);
        }
    }

    @ErrorLog("检查员工是否存在未生效的岗位调动或正在执行的借调记录")
    @ApiOperation("检查员工是否存在未生效的岗位调动或正在执行的借调记录")
    @GetMapping(value = "/isPlanOrTransferring")
    @PreAuthorize("@el.check('employeeJobTransfer:list','employeeJobTransfer:edit','employee:list','employee:edit')")
    public ResponseEntity isPlanOrTransferring(Long employeeId, Long groupId) {
        return new ResponseEntity<>(pmEmployeeJobTransferService.isPlanOrTransferring(employeeId, groupId), HttpStatus.OK);
    }

    @Log("修改岗位调动结束时间")
    @ApiOperation("修改岗位调动结束时间")
    @PutMapping(value = "/endTime")
    @PreAuthorize("@el.check('employeeJobTransfer:edit','employee:edit')")
    public ResponseEntity updateEndTime(@Validated(PmEmployeeJobTransfer.Update.class) @RequestBody PmEmployeeJobTransfer employeeJobTransfer) {
        pmEmployeeJobTransferService.updateEndTime(employeeJobTransfer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
