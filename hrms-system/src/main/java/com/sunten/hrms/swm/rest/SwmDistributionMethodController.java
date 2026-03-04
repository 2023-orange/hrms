package com.sunten.hrms.swm.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmDistributionMethod;
import com.sunten.hrms.swm.dto.SwmDistributionMethodQueryCriteria;
import com.sunten.hrms.swm.service.SwmDistributionMethodService;
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
 * 分配方式 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@RestController
@Api(tags = "分配方式")
@RequestMapping("/api/swm/distributionMethod")
public class SwmDistributionMethodController {
    private static final String ENTITY_NAME = "distributionMethod";
    private final SwmDistributionMethodService swmDistributionMethodService;

    public SwmDistributionMethodController(SwmDistributionMethodService swmDistributionMethodService) {
        this.swmDistributionMethodService = swmDistributionMethodService;
    }

    @Log("新增分配方式")
    @ApiOperation("新增分配方式")
    @PostMapping
    @PreAuthorize("@el.check('distributionMethod:add')")
    public ResponseEntity create(@Validated @RequestBody SwmDistributionMethod distributionMethod) {
        if (distributionMethod.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmDistributionMethodService.insertSwmDistributionMethod(distributionMethod), HttpStatus.CREATED);
    }

//
//    @Log("删除分配方式")
//    @ApiOperation("删除分配方式")
//    @DeleteMapping(value = "/{id}")
//    @PreAuthorize("@el.check('distributionMethod:del')")
//    public ResponseEntity delete(@PathVariable Long id) {
//        try {
//            swmDistributionMethodService.delete(id);
//        } catch (Throwable e) {
//            ThrowableUtil.throwForeignKeyException(e, "该分配方式存在 关联，请取消关联后再试");
//        }
//        return new ResponseEntity(HttpStatus.OK);
//    }

    @Log("失效分配方式")
    @ApiOperation("失效分配方式")
    @DeleteMapping(value = "/disabled")
    @PreAuthorize("@el.check('distributionMethod:del')")
    public ResponseEntity disabled(@RequestBody SwmDistributionMethod distributionMethod) {
        swmDistributionMethodService.disabled(distributionMethod);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("修改分配方式")
    @ApiOperation("修改分配方式")
    @PutMapping
    @PreAuthorize("@el.check('distributionMethod:edit')")
    public ResponseEntity update(@Validated(SwmDistributionMethod.Update.class) @RequestBody SwmDistributionMethod distributionMethod) {
        swmDistributionMethodService.update(distributionMethod);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个分配方式")
    @ApiOperation("获取单个分配方式")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('distributionMethod:list')")
    public ResponseEntity getDistributionMethod(@PathVariable Long id) {
        return new ResponseEntity<>(swmDistributionMethodService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询分配方式（分页）")
    @ApiOperation("查询分配方式（分页）")
    @GetMapping
    @PreAuthorize("@el.check('distributionMethod:list')")
    public ResponseEntity getDistributionMethodPage(SwmDistributionMethodQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmDistributionMethodService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询分配方式（不分页）")
    @ApiOperation("查询分配方式（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('distributionMethod:list')")
    public ResponseEntity getDistributionMethodNoPaging(SwmDistributionMethodQueryCriteria criteria) {
    return new ResponseEntity<>(swmDistributionMethodService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出分配方式数据")
    @ApiOperation("导出分配方式数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('distributionMethod:list')")
    public void download(HttpServletResponse response, SwmDistributionMethodQueryCriteria criteria) throws IOException {
        swmDistributionMethodService.download(swmDistributionMethodService.listAll(criteria), response);
    }
}
