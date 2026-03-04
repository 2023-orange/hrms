package com.sunten.hrms.re.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.re.domain.ReFamily;
import com.sunten.hrms.re.dto.ReFamilyQueryCriteria;
import com.sunten.hrms.re.service.ReFamilyService;
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
import java.util.List;

/**
 * <p>
 * 家庭情况表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@RestController
@Api(tags = "家庭情况表")
@RequestMapping("/api/re/family")
public class ReFamilyController {
    private static final String ENTITY_NAME = "family";
    private final ReFamilyService reFamilyService;

    public ReFamilyController(ReFamilyService reFamilyService) {
        this.reFamilyService = reFamilyService;
    }

    @Log("新增家庭情况表")
    @ApiOperation("新增家庭情况表")
    @PostMapping
    @PreAuthorize("@el.check('family:add')")
    public ResponseEntity create(@Validated @RequestBody ReFamily family) {
        if (family.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(reFamilyService.insert(family), HttpStatus.CREATED);
    }

    @Log("删除家庭情况表")
    @ApiOperation("删除家庭情况表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('family:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reFamilyService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该家庭情况表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改家庭情况表")
    @ApiOperation("修改家庭情况表")
    @PutMapping
    @PreAuthorize("@el.check('family:edit')")
    public ResponseEntity update(@Validated(ReFamily.Update.class) @RequestBody ReFamily family) {
        reFamilyService.update(family);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个家庭情况表")
    @ApiOperation("获取单个家庭情况表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('family:list')")
    public ResponseEntity getFamily(@PathVariable Long id) {
        return new ResponseEntity<>(reFamilyService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询家庭情况表（分页）")
    @ApiOperation("查询家庭情况表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('family:list','recruitment:list')")
    public ResponseEntity getFamilyPage(ReFamilyQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reFamilyService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询家庭情况表（不分页）")
    @ApiOperation("查询家庭情况表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('family:list','recruitment:list')")
    public ResponseEntity getFamilyNoPaging(ReFamilyQueryCriteria criteria) {
        return new ResponseEntity<>(reFamilyService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出家庭情况表数据")
    @ApiOperation("导出家庭情况表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('family:list')")
    public void download(HttpServletResponse response, ReFamilyQueryCriteria criteria) throws IOException {
        reFamilyService.download(reFamilyService.listAll(criteria), response);
    }

    @Log("批量编辑家庭情况表")
    @ApiOperation("批量编辑家庭情况表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('family:add','family:edit','recruitment:edit','recruitment:add')")
    public ResponseEntity batchSave(@Validated @RequestBody List<ReFamily> familys) {
        if (familys == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(reFamilyService.batchInsert(familys, null), HttpStatus.CREATED);
    }
}
