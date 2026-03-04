package com.sunten.hrms.fnd.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.fnd.domain.FndSuperQueryGroup;
import com.sunten.hrms.fnd.dto.FndSuperQueryGroupQueryCriteria;
import com.sunten.hrms.fnd.service.FndSuperQueryGroupService;
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
 * @author batan
 * @since 2022-08-12
 */
@RestController
@Api(tags = "")
@RequestMapping("/api/fnd/superQueryGroup")
public class FndSuperQueryGroupController {
    private static final String ENTITY_NAME = "superQueryGroup";
    private final FndSuperQueryGroupService fndSuperQueryGroupService;

    public FndSuperQueryGroupController(FndSuperQueryGroupService fndSuperQueryGroupService) {
        this.fndSuperQueryGroupService = fndSuperQueryGroupService;
    }

    @Log("新增")
    @ApiOperation("新增")
    @PostMapping
    @PreAuthorize("@el.check('superQueryGroup:add')")
    public ResponseEntity create(@Validated @RequestBody FndSuperQueryGroup superQueryGroup) {
        if (superQueryGroup.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(fndSuperQueryGroupService.insert(superQueryGroup), HttpStatus.CREATED);
    }

    @Log("删除")
    @ApiOperation("删除")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('superQueryGroup:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            fndSuperQueryGroupService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改")
    @ApiOperation("修改")
    @PutMapping
    @PreAuthorize("@el.check('superQueryGroup:edit')")
    public ResponseEntity update(@Validated(FndSuperQueryGroup.Update.class) @RequestBody FndSuperQueryGroup superQueryGroup) {
        fndSuperQueryGroupService.update(superQueryGroup);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个")
    @ApiOperation("获取单个")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('superQueryGroup:list')")
    public ResponseEntity getSuperQueryGroup(@PathVariable Long id) {
        return new ResponseEntity<>(fndSuperQueryGroupService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping
    @PreAuthorize("@el.check('superQueryGroup:list')")
    public ResponseEntity getSuperQueryGroupPage(FndSuperQueryGroupQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(fndSuperQueryGroupService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询（不分页）")
    @ApiOperation("查询（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('superQueryGroup:list')")
    public ResponseEntity getSuperQueryGroupNoPaging(FndSuperQueryGroupQueryCriteria criteria) {
    return new ResponseEntity<>(fndSuperQueryGroupService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('superQueryGroup:list')")
    public void download(HttpServletResponse response, FndSuperQueryGroupQueryCriteria criteria) throws IOException {
        fndSuperQueryGroupService.download(fndSuperQueryGroupService.listAll(criteria), response);
    }
}
