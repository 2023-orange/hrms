package com.sunten.hrms.pm.rest;

import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.pm.domain.PmEmployeeAward;
import com.sunten.hrms.pm.dto.PmEmployeeAwardQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeAwardService;
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
 * 奖罚情况表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "奖罚情况表")
@RequestMapping("/api/pm/employeeAward")
public class PmEmployeeAwardController {
    private static final String ENTITY_NAME = "employeeAward";
    private final PmEmployeeAwardService pmEmployeeAwardService;
    private final FndDataScope fndDataScope;

    public PmEmployeeAwardController(PmEmployeeAwardService pmEmployeeAwardService, FndDataScope fndDataScope) {
        this.pmEmployeeAwardService = pmEmployeeAwardService;
        this.fndDataScope = fndDataScope;
    }

    @Log("新增奖罚情况表")
    @ApiOperation("新增奖罚情况表")
    @PostMapping
    @PreAuthorize("@el.check('employeeAward:add','employee:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeeAward employeeAward) {
        if (employeeAward.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeAwardService.insert(employeeAward), HttpStatus.CREATED);
    }

    @Log("删除奖罚情况表")
    @ApiOperation("删除奖罚情况表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeAward:del','employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeAwardService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该奖罚情况表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改奖罚情况表")
    @ApiOperation("修改奖罚情况表")
    @PutMapping
    @PreAuthorize("@el.check('employeeAward:edit','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployeeAward.Update.class) @RequestBody PmEmployeeAward employeeAward) {
        pmEmployeeAwardService.update(employeeAward);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个奖罚情况表")
    @ApiOperation("获取单个奖罚情况表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeAward:list','employee:list')")
    public ResponseEntity getEmployeeAward(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeAwardService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询奖罚情况表（分页）")
    @ApiOperation("查询奖罚情况表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeAward:list','employee:list')")
    public ResponseEntity getEmployeeAwardPage(PmEmployeeAwardQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeAwardService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询奖罚情况表（不分页）")
    @ApiOperation("查询奖罚情况表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeAward:list','employee:list')")
    public ResponseEntity getEmployeeAwardNoPaging(PmEmployeeAwardQueryCriteria criteria) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeAwardService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("查询奖罚情况表（分页）")
    @ApiOperation("查询奖罚情况表（分页）")
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('employeeAward:list','employee:list')")
    public ResponseEntity getEmployeeAwardPageAll(PmEmployeeAwardQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeAwardService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("导出奖罚情况表数据")
    @ApiOperation("导出奖罚情况表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeAward:list','employee:list')")
    public void download(HttpServletResponse response, PmEmployeeAwardQueryCriteria criteria) throws IOException {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            FileUtil.downloadExcel(new ArrayList<>(), response);
        } else {
            criteria.setDeptIds(dataScopeVo.getDeptIds());
            criteria.setEmployeeId(dataScopeVo.getEmployeeId());
            pmEmployeeAwardService.download(pmEmployeeAwardService.listAll(criteria), response);
        }
    }

    @Log("批量编辑奖罚情况表")
    @ApiOperation("批量编辑奖罚情况表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('employeeAward:add','employeeAward:edit','employee:add','employee:edit')")
    public ResponseEntity batchSave(@Validated @RequestBody List<PmEmployeeAward> employeeAwards) {
        if (employeeAwards == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " can't be null");
        }
        return new ResponseEntity<>(pmEmployeeAwardService.batchInsert(employeeAwards, null), HttpStatus.CREATED);
    }
}
