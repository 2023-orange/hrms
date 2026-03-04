package com.sunten.hrms.pm.rest;

import cn.hutool.json.JSONArray;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.pm.domain.PmEmployeeContract;
import com.sunten.hrms.pm.dto.PmEmployeeContractQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeContractService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ThrowableUtil;
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
import java.util.List;

/**
 * <p>
 * 合同情况表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "合同情况表")
@RequestMapping("/api/pm/employeeContract")
public class PmEmployeeContractController {
    private static final String ENTITY_NAME = "employeeContract";
    private final PmEmployeeContractService pmEmployeeContractService;
    private final FndDataScope fndDataScope;

    public PmEmployeeContractController(PmEmployeeContractService pmEmployeeContractService, FndDataScope fndDataScope) {
        this.pmEmployeeContractService = pmEmployeeContractService;
        this.fndDataScope = fndDataScope;
    }

    @Log("新增合同情况表")
    @ApiOperation("新增合同情况表")
    @PostMapping
    @PreAuthorize("@el.check('employeeContract:add','employee:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeeContract employeeContract) {
        if (employeeContract.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeContractService.insert(employeeContract), HttpStatus.CREATED);
    }

    @Log("删除合同情况表")
    @ApiOperation("删除合同情况表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeContract:del','employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeContractService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该合同情况表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改合同情况表")
    @ApiOperation("修改合同情况表")
    @PutMapping
    @PreAuthorize("@el.check('employeeContract:edit','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployeeContract.Update.class) @RequestBody PmEmployeeContract employeeContract) {
        pmEmployeeContractService.update(employeeContract);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个合同情况表")
    @ApiOperation("获取单个合同情况表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeContract:list','employee:list')")
    public ResponseEntity getEmployeeContract(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeContractService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("获取人员所有合同情况表")
    @ApiOperation("获取人员所有合同情况表")
    @GetMapping(value = "all/{employeeId}")
    @PreAuthorize("@el.check('employeeContract:list','employee:list')")
    public ResponseEntity getEmployeeAllContract(@PathVariable Long employeeId) {
        return new ResponseEntity<>(pmEmployeeContractService.getAllByemployeeId(employeeId), HttpStatus.OK);
    }

    @ErrorLog("查询合同情况表（分页）")
    @ApiOperation("查询合同情况表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeContract:list','employee:list')")
    public ResponseEntity getEmployeeContractPage(PmEmployeeContractQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeContractService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询合同情况表（不分页）")
    @ApiOperation("查询合同情况表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeContract:list','employee:list')")
    public ResponseEntity getEmployeeContractNoPaging(PmEmployeeContractQueryCriteria criteria) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeContractService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出合同情况表数据")
    @ApiOperation("导出合同情况表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeContract:list','employee:list')")
    public void download(HttpServletResponse response, PmEmployeeContractQueryCriteria criteria) throws IOException {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            FileUtil.downloadExcel(new ArrayList<>(), response);
        } else {
            criteria.setDeptIds(dataScopeVo.getDeptIds());
            criteria.setEmployeeId(dataScopeVo.getEmployeeId());
            pmEmployeeContractService.download(criteria, response);
        }
    }

    @Log("批量编辑合同情况表")
    @ApiOperation("批量编辑合同情况表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('employeeContract:add','employeeContract:edit','employee:add','employee:edit')")
    public ResponseEntity batchSave(@Validated @RequestBody List<PmEmployeeContract> employeeContracts) {
        if (employeeContracts == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(pmEmployeeContractService.batchInsert(employeeContracts, null), HttpStatus.CREATED);
    }


    @ErrorLog("查询合同情况表（分页）")
    @ApiOperation("查询合同情况表（分页）")
    @GetMapping(value = "/getAllAndSignNumByPage")
    @PreAuthorize("@el.check('employeeContract:list','employee:list')")
    public ResponseEntity getAllAndSignNumByPage(PmEmployeeContractQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeContractService.listAllAndSignNumByPage(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询合同情况表（不分页）")
    @ApiOperation("查询合同情况表（不分页）")
    @GetMapping(value = "/getAllAndSignNum")
    @PreAuthorize("@el.check('employeeContract:list','employee:list')")
    public ResponseEntity getAllAndSignNum(PmEmployeeContractQueryCriteria criteria) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeContractService.listAllAndSignNum(criteria), HttpStatus.OK);
    }

    @ErrorLog("根据员工id获取最新合同")
    @ApiOperation("根据员工id获取最新合同")
    @GetMapping(value = "/getNewestContract/{employeeId}")
    @PreAuthorize("@el.check('leaveApproval:list')")
    public ResponseEntity getNewestContract(@PathVariable Long employeeId) {
        return new ResponseEntity<>(pmEmployeeContractService.getNewestContractByEmployeeId(employeeId), HttpStatus.OK);
    }


}
