package com.sunten.hrms.pm.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.PmMedicalProject;
import com.sunten.hrms.pm.dto.PmMedicalProjectQueryCriteria;
import com.sunten.hrms.pm.service.PmMedicalProjectService;
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
 * 体检项目表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-04-19
 */
@RestController
@Api(tags = "体检项目表")
@RequestMapping("/api/pm/medicalProject")
public class PmMedicalProjectController {
    private static final String ENTITY_NAME = "medicalProject";
    private final PmMedicalProjectService pmMedicalProjectService;

    public PmMedicalProjectController(PmMedicalProjectService pmMedicalProjectService) {
        this.pmMedicalProjectService = pmMedicalProjectService;
    }

    @Log("新增体检项目表")
    @ApiOperation("新增体检项目表")
    @PostMapping
    @PreAuthorize("@el.check('medicalProject:add','medicalJob:add')")
    public ResponseEntity create(@Validated @RequestBody PmMedicalProject medicalProject) {
        if (medicalProject.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(pmMedicalProjectService.insert(medicalProject), HttpStatus.CREATED);
    }

    @Log("删除体检项目表")
    @ApiOperation("删除体检项目表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('medicalProject:del','medicalJob:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmMedicalProjectService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该体检项目表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改体检项目表")
    @ApiOperation("修改体检项目表")
    @PutMapping
    @PreAuthorize("@el.check('medicalProject:edit','medicalJob:edit')")
    public ResponseEntity update(@Validated(PmMedicalProject.Update.class) @RequestBody PmMedicalProject medicalProject) {
        pmMedicalProjectService.update(medicalProject);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个体检项目表")
    @ApiOperation("获取单个体检项目表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('medicalProject:list','medicalJob:list')")
    public ResponseEntity getMedicalProject(@PathVariable Long id) {
        return new ResponseEntity<>(pmMedicalProjectService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询体检项目表（分页）")
    @ApiOperation("查询体检项目表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('medicalProject:list','medicalJob:list')")
    public ResponseEntity getMedicalProjectPage(PmMedicalProjectQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmMedicalProjectService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询体检项目表（不分页）")
    @ApiOperation("查询体检项目表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('medicalProject:list','medicalJob:list')")
    public ResponseEntity getMedicalProjectNoPaging(PmMedicalProjectQueryCriteria criteria) {
    return new ResponseEntity<>(pmMedicalProjectService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出体检项目表数据")
    @ApiOperation("导出体检项目表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('medicalProject:list','medicalJob:list')")
    public void download(HttpServletResponse response, PmMedicalProjectQueryCriteria criteria) throws IOException {
        pmMedicalProjectService.download(pmMedicalProjectService.listAll(criteria), response);
    }
}
