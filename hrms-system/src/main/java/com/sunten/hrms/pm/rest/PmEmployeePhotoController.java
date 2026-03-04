package com.sunten.hrms.pm.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.pm.domain.PmEmployeePhoto;
import com.sunten.hrms.pm.dto.PmEmployeePhotoQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeePhotoService;
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
 * 人员图像表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2020-09-09
 */
@RestController
@Api(tags = "人员图像表")
@RequestMapping("/api/pm/employeePhoto")
public class PmEmployeePhotoController {
    private static final String ENTITY_NAME = "employeePhoto";
    private final PmEmployeePhotoService pmEmployeePhotoService;

    public PmEmployeePhotoController(PmEmployeePhotoService pmEmployeePhotoService) {
        this.pmEmployeePhotoService = pmEmployeePhotoService;
    }

    @Log("新增人员图像表")
    @ApiOperation("新增人员图像表")
    @PostMapping
    @PreAuthorize("@el.check('employeePhoto:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeePhoto employeePhoto) {
        if (employeePhoto.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(pmEmployeePhotoService.insert(employeePhoto), HttpStatus.CREATED);
    }

    @Log("删除人员图像表")
    @ApiOperation("删除人员图像表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeePhoto:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeePhotoService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该人员图像表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改人员图像表")
    @ApiOperation("修改人员图像表")
    @PutMapping
    @PreAuthorize("@el.check('employeePhoto:edit')")
    public ResponseEntity update(@Validated(PmEmployeePhoto.Update.class) @RequestBody PmEmployeePhoto employeePhoto) {
        pmEmployeePhotoService.update(employeePhoto);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个人员图像表")
    @ApiOperation("获取单个人员图像表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeePhoto:list')")
    public ResponseEntity getEmployeePhoto(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeePhotoService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询人员图像表（分页）")
    @ApiOperation("查询人员图像表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeePhoto:list')")
    public ResponseEntity getEmployeePhotoPage(PmEmployeePhotoQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeePhotoService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询人员图像表（不分页）")
    @ApiOperation("查询人员图像表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeePhoto:list')")
    public ResponseEntity getEmployeePhotoNoPaging(PmEmployeePhotoQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeePhotoService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出人员图像表数据")
    @ApiOperation("导出人员图像表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeePhoto:list')")
    public void download(HttpServletResponse response, PmEmployeePhotoQueryCriteria criteria) throws IOException {
        pmEmployeePhotoService.download(pmEmployeePhotoService.listAll(criteria), response);
    }
}
