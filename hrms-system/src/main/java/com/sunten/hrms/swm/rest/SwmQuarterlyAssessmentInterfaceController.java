package com.sunten.hrms.swm.rest;

import com.sunten.hrms.swm.domain.SwmQuarterlyAssessment;
import com.sunten.hrms.swm.vo.SwmQuarterAssessmentVo;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmQuarterlyAssessmentInterface;
import com.sunten.hrms.swm.dto.SwmQuarterlyAssessmentInterfaceQueryCriteria;
import com.sunten.hrms.swm.service.SwmQuarterlyAssessmentInterfaceService;
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
 *  前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2022-05-13
 */
@RestController
@Api(tags = "")
@RequestMapping("/api/swm/quarterlyAssessmentInterface")
public class SwmQuarterlyAssessmentInterfaceController {
    private static final String ENTITY_NAME = "quarterlyAssessmentInterface";
    private final SwmQuarterlyAssessmentInterfaceService swmQuarterlyAssessmentInterfaceService;

    public SwmQuarterlyAssessmentInterfaceController(SwmQuarterlyAssessmentInterfaceService swmQuarterlyAssessmentInterfaceService) {
        this.swmQuarterlyAssessmentInterfaceService = swmQuarterlyAssessmentInterfaceService;
    }

    @Log("新增")
    @ApiOperation("新增")
    @PostMapping
    @PreAuthorize("@el.check('quarterlyAssessment:add')")
    public ResponseEntity create(@Validated @RequestBody SwmQuarterlyAssessmentInterface quarterlyAssessmentInterface) {
        if (quarterlyAssessmentInterface.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmQuarterlyAssessmentInterfaceService.insert(quarterlyAssessmentInterface), HttpStatus.CREATED);
    }

    @Log("删除")
    @ApiOperation("删除")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('quarterlyAssessment:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmQuarterlyAssessmentInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改")
    @ApiOperation("修改")
    @PutMapping
    @PreAuthorize("@el.check('quarterlyAssessment:edit')")
    public ResponseEntity update(@Validated(SwmQuarterlyAssessmentInterface.Update.class) @RequestBody SwmQuarterlyAssessmentInterface quarterlyAssessmentInterface) {
        swmQuarterlyAssessmentInterfaceService.update(quarterlyAssessmentInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个")
    @ApiOperation("获取单个")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('quarterlyAssessment:list')")
    public ResponseEntity getQuarterlyAssessmentInterface(@PathVariable Long id) {
        return new ResponseEntity<>(swmQuarterlyAssessmentInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping
    @PreAuthorize("@el.check('quarterlyAssessment:list')")
    public ResponseEntity getQuarterlyAssessmentInterfacePage(SwmQuarterlyAssessmentInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmQuarterlyAssessmentInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询（不分页）")
    @ApiOperation("查询（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('quarterlyAssessment:list')")
    public ResponseEntity getQuarterlyAssessmentInterfaceNoPaging(SwmQuarterlyAssessmentInterfaceQueryCriteria criteria) {
    return new ResponseEntity<>(swmQuarterlyAssessmentInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('quarterlyAssessment:list')")
    public void download(HttpServletResponse response, SwmQuarterlyAssessmentInterfaceQueryCriteria criteria) throws IOException {
        swmQuarterlyAssessmentInterfaceService.download(swmQuarterlyAssessmentInterfaceService.listAll(criteria), response);
    }

    @Log("季度考核数据导入")
    @ApiOperation("季度考核数据导入")
    @PutMapping(value = "/insertQuarterlyAssessment")
    @PreAuthorize("@el.check('quarterlyAssessment:edit')")
    public ResponseEntity insertQuarterlyAssessment(@RequestBody SwmQuarterAssessmentVo swmQuarterAssessmentVo) {
        return new ResponseEntity<>(swmQuarterlyAssessmentInterfaceService.insertExcel(swmQuarterAssessmentVo.getSwmQuarterlyAssessmentInterfaces(), swmQuarterAssessmentVo.getReImportFlag()), HttpStatus.OK);
    }
}
