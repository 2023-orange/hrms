package com.sunten.hrms.pm.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.PmMedicalRelevance;
import com.sunten.hrms.pm.dto.PmMedicalRelevanceQueryCriteria;
import com.sunten.hrms.pm.service.PmMedicalRelevanceService;
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
 * 体检项目关联表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-04-19
 */
@RestController
@Api(tags = "体检项目关联表")
@RequestMapping("/api/pm/medicalRelevance")
public class PmMedicalRelevanceController {
    private static final String ENTITY_NAME = "medicalRelevance";
    private final PmMedicalRelevanceService pmMedicalRelevanceService;

    public PmMedicalRelevanceController(PmMedicalRelevanceService pmMedicalRelevanceService) {
        this.pmMedicalRelevanceService = pmMedicalRelevanceService;
    }

    @Log("新增体检项目关联表")
    @ApiOperation("新增体检项目关联表")
    @PostMapping
    @PreAuthorize("@el.check('medicalRelevance:add','medicalJob:add')")
    public ResponseEntity create(@Validated @RequestBody PmMedicalRelevance medicalRelevance) {
        return new ResponseEntity<>(pmMedicalRelevanceService.insert(medicalRelevance), HttpStatus.CREATED);
    }

    @Log("删除体检项目关联表")
    @ApiOperation("删除体检项目关联表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('medicalRelevance:del','medicalJob:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmMedicalRelevanceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该体检项目关联表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改体检项目关联表")
    @ApiOperation("修改体检项目关联表")
    @PutMapping
    @PreAuthorize("@el.check('medicalRelevance:edit','medicalJob:edit')")
    public ResponseEntity update(@Validated(PmMedicalRelevance.Update.class) @RequestBody PmMedicalRelevance medicalRelevance) {
        pmMedicalRelevanceService.update(medicalRelevance);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个体检项目关联表")
    @ApiOperation("获取单个体检项目关联表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('medicalRelevance:list','medicalJob:list')")
    public ResponseEntity getMedicalRelevance(@PathVariable Long id) {
        return new ResponseEntity<>(pmMedicalRelevanceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询体检项目关联表（分页）")
    @ApiOperation("查询体检项目关联表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('medicalRelevance:list','medicalJob:list')")
    public ResponseEntity getMedicalRelevancePage(PmMedicalRelevanceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmMedicalRelevanceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询体检项目关联表（不分页）")
    @ApiOperation("查询体检项目关联表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('medicalRelevance:list','medicalJob:list')")
    public ResponseEntity getMedicalRelevanceNoPaging(PmMedicalRelevanceQueryCriteria criteria) {
    return new ResponseEntity<>(pmMedicalRelevanceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出体检项目关联表数据")
    @ApiOperation("导出体检项目关联表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('medicalRelevance:list','medicalJob:list')")
    public void download(HttpServletResponse response, PmMedicalRelevanceQueryCriteria criteria) throws IOException {
        pmMedicalRelevanceService.download(pmMedicalRelevanceService.listAll(criteria), response);
    }
}
