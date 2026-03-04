package com.sunten.hrms.td.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.td.domain.TdTrainEmployeeJurisdiction;
import com.sunten.hrms.td.dto.TdTrainEmployeeJurisdictionQueryCriteria;
import com.sunten.hrms.td.service.TdTrainEmployeeJurisdictionService;
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

/**
 * <p>
 * 培训员权限表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-06-23
 */
@RestController
@Api(tags = "培训员权限表")
@RequestMapping("/api/td/trainEmployeeJurisdiction")
public class TdTrainEmployeeJurisdictionController {
    private static final String ENTITY_NAME = "trainEmployeeJurisdiction";
    private final TdTrainEmployeeJurisdictionService tdTrainEmployeeJurisdictionService;

    public TdTrainEmployeeJurisdictionController(TdTrainEmployeeJurisdictionService tdTrainEmployeeJurisdictionService) {
        this.tdTrainEmployeeJurisdictionService = tdTrainEmployeeJurisdictionService;
    }

    @Log("新增培训员权限表")
    @ApiOperation("新增培训员权限表")
    @PostMapping
    @PreAuthorize("@el.check('trainEmployeeJurisdiction:add')")
    public ResponseEntity create(@RequestBody TdTrainEmployeeJurisdiction trainEmployeeJurisdiction) {
        if (trainEmployeeJurisdiction.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdTrainEmployeeJurisdictionService.insert(trainEmployeeJurisdiction), HttpStatus.CREATED);
    }

    @Log("删除培训员权限表")
    @ApiOperation("删除培训员权限表")
    @DeleteMapping(value = "/{employeeId}")
    @PreAuthorize("@el.check('trainEmployeeJurisdiction:del')")
    public ResponseEntity delete(@PathVariable Long employeeId) {
        try {
            tdTrainEmployeeJurisdictionService.deleteByEmployee(employeeId);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该培训员权限表存在 关联，删除失败");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改培训员权限表")
    @ApiOperation("修改培训员权限表")
    @PutMapping
    @PreAuthorize("@el.check('trainEmployeeJurisdiction:edit')")
    public ResponseEntity update(@Validated(TdTrainEmployeeJurisdiction.Update.class) @RequestBody TdTrainEmployeeJurisdiction trainEmployeeJurisdiction) {
        tdTrainEmployeeJurisdictionService.update(trainEmployeeJurisdiction);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个培训员权限表")
    @ApiOperation("获取单个培训员权限表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('trainEmployeeJurisdiction:list')")
    public ResponseEntity getTrainEmployeeJurisdiction(@PathVariable Long id) {
        return new ResponseEntity<>(tdTrainEmployeeJurisdictionService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询培训员权限表（分页）")
    @ApiOperation("查询培训员权限表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('trainEmployeeJurisdiction:list')")
    public ResponseEntity getTrainEmployeeJurisdictionPage(TdTrainEmployeeJurisdictionQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"pe.id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(tdTrainEmployeeJurisdictionService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询培训员权限表（不分页）")
    @ApiOperation("查询培训员权限表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('trainEmployeeJurisdiction:list')")
    public ResponseEntity getTrainEmployeeJurisdictionNoPaging(TdTrainEmployeeJurisdictionQueryCriteria criteria) {
    return new ResponseEntity<>(tdTrainEmployeeJurisdictionService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出培训员权限表数据")
    @ApiOperation("导出培训员权限表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('trainEmployeeJurisdiction:list')")
    public void download(HttpServletResponse response, TdTrainEmployeeJurisdictionQueryCriteria criteria) throws IOException {
        tdTrainEmployeeJurisdictionService.download(tdTrainEmployeeJurisdictionService.listAll(criteria), response);
    }

    @Log("更改授权部门")
    @ApiOperation("更改授权部门")
    @PutMapping("/batchUpdate")
    @PreAuthorize("@el.check('trainEmployeeJurisdiction:edit')")
    public ResponseEntity batchUpdate(@RequestBody TdTrainEmployeeJurisdiction employeeJurisdiction) {
        tdTrainEmployeeJurisdictionService.batchUpdate(employeeJurisdiction);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("获取授权部门")
    @ApiOperation("获取授权部门")
    @GetMapping("/getByEmployeeId/{employeeId}")
    @PreAuthorize("@el.check('trainEmployeeJurisdiction:list')")
    public ResponseEntity getByEmployeeId(@PathVariable Long employeeId) {
        return new ResponseEntity<>(tdTrainEmployeeJurisdictionService.getDeptsByEmployeeeId(employeeId), HttpStatus.OK);
    }
}
