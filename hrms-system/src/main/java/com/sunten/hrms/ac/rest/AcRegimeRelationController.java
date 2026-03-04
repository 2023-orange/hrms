package com.sunten.hrms.ac.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.AcRegimeRelation;
import com.sunten.hrms.ac.dto.AcRegimeRelationQueryCriteria;
import com.sunten.hrms.ac.service.AcRegimeRelationService;
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
 * 考勤制度排班时间关系表 前端控制器
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@RestController
@Api(tags = "考勤制度排班时间关系表")
@RequestMapping("/api/ac/regimeRelation")
public class AcRegimeRelationController {
    private static final String ENTITY_NAME = "regimeRelation";
    private final AcRegimeRelationService acRegimeRelationService;

    public AcRegimeRelationController(AcRegimeRelationService acRegimeRelationService) {
        this.acRegimeRelationService = acRegimeRelationService;
    }

    @Log("新增考勤制度排班时间关系表")
    @ApiOperation("新增考勤制度排班时间关系表")
    @PostMapping
    @PreAuthorize("@el.check('regime:add')")
    public ResponseEntity create(@Validated @RequestBody AcRegimeRelation regimeRelation) {
        if (regimeRelation.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(acRegimeRelationService.insert(regimeRelation), HttpStatus.CREATED);
    }

    @Log("删除考勤制度排班时间关系表")
    @ApiOperation("删除考勤制度排班时间关系表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('regime:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acRegimeRelationService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该考勤制度排班时间关系表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改考勤制度排班时间关系表")
    @ApiOperation("修改考勤制度排班时间关系表")
    @PutMapping
    @PreAuthorize("@el.check('regime:edit')")
    public ResponseEntity update(@Validated(AcRegimeRelation.Update.class) @RequestBody AcRegimeRelation regimeRelation) {
        acRegimeRelationService.update(regimeRelation);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个考勤制度排班时间关系表")
    @ApiOperation("获取单个考勤制度排班时间关系表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('regime:list')")
    public ResponseEntity getRegimeRelation(@PathVariable Long id) {
        return new ResponseEntity<>(acRegimeRelationService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询考勤制度排班时间关系表（分页）")
    @ApiOperation("查询考勤制度排班时间关系表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('regime:list')")
    public ResponseEntity getRegimeRelationPage(AcRegimeRelationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(acRegimeRelationService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询考勤制度排班时间关系表（不分页）")
    @ApiOperation("查询考勤制度排班时间关系表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('regime:list')")
    public ResponseEntity getRegimeRelationNoPaging(AcRegimeRelationQueryCriteria criteria) {
    return new ResponseEntity<>(acRegimeRelationService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出考勤制度排班时间关系表数据")
    @ApiOperation("导出考勤制度排班时间关系表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('regime:list')")
    public void download(HttpServletResponse response, AcRegimeRelationQueryCriteria criteria) throws IOException {
        acRegimeRelationService.download(acRegimeRelationService.listAll(criteria), response);
    }
}
