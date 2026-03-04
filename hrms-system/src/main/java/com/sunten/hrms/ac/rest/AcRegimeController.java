package com.sunten.hrms.ac.rest;

import com.sunten.hrms.ac.dto.AcRegimeDTO;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.AcRegime;
import com.sunten.hrms.ac.dto.AcRegimeQueryCriteria;
import com.sunten.hrms.ac.service.AcRegimeService;
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
 * 考勤制度表 前端控制器
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@RestController
@Api(tags = "考勤制度表")
@RequestMapping("/api/ac/regime")
public class AcRegimeController {
    private static final String ENTITY_NAME = "regime";
    private final AcRegimeService acRegimeService;

    public AcRegimeController(AcRegimeService acRegimeService) {
        this.acRegimeService = acRegimeService;
    }

    @Log("新增考勤制度表")
    @ApiOperation("新增考勤制度表")
    @PostMapping
    @PreAuthorize("@el.check('regime:add')")
    public ResponseEntity create(@Validated @RequestBody AcRegime regime) {
        if (regime.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        regime.setRegimeCode("tempCode");
        AcRegimeDTO acRegimeDTO = acRegimeService.insert(regime);
        regime.setRegimeCode("code" + acRegimeDTO.getId());
        regime.setId(acRegimeDTO.getId());
        acRegimeService.update(regime);
        return new ResponseEntity<>(regime, HttpStatus.CREATED);
    }

    @Log("删除考勤制度表")
    @ApiOperation("删除考勤制度表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('regime:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        acRegimeService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改考勤制度表")
    @ApiOperation("修改考勤制度表")
    @PutMapping
    @PreAuthorize("@el.check('regime:edit')")
    public ResponseEntity update(@Validated(AcRegime.Update.class) @RequestBody AcRegime regime) {
        acRegimeService.update(regime);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个考勤制度表")
    @ApiOperation("获取单个考勤制度表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('regime:list')")
    public ResponseEntity getRegime(@PathVariable Long id) {
        return new ResponseEntity<>(acRegimeService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询考勤制度表（分页）")
    @ApiOperation("查询考勤制度表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('regime:list')")
    public ResponseEntity getRegimePage(AcRegimeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(acRegimeService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询考勤制度表（不分页）")
    @ApiOperation("查询考勤制度表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('regime:list')")
    public ResponseEntity getRegimeNoPaging(AcRegimeQueryCriteria criteria) {
    return new ResponseEntity<>(acRegimeService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出考勤制度表数据")
    @ApiOperation("导出考勤制度表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('regime:list')")
    public void download(HttpServletResponse response, AcRegimeQueryCriteria criteria) throws IOException {
        acRegimeService.download(acRegimeService.listAll(criteria), response);
    }
}
