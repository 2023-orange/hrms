package com.sunten.hrms.td.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.td.domain.TdPlanImplementDept;
import com.sunten.hrms.td.dto.TdPlanImplementDeptQueryCriteria;
import com.sunten.hrms.td.service.TdPlanImplementDeptService;
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
 * 培训实施参与部门扩展表（用于后期统计使用） 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-06-21
 */
@RestController
@Api(tags = "培训实施参与部门扩展表（用于后期统计使用）")
@RequestMapping("/api/td/planImplementDept")
public class TdPlanImplementDeptController {
    private static final String ENTITY_NAME = "planImplementDept";
    private final TdPlanImplementDeptService tdPlanImplementDeptService;

    public TdPlanImplementDeptController(TdPlanImplementDeptService tdPlanImplementDeptService) {
        this.tdPlanImplementDeptService = tdPlanImplementDeptService;
    }

    @Log("新增培训实施参与部门扩展表（用于后期统计使用）")
    @ApiOperation("新增培训实施参与部门扩展表（用于后期统计使用）")
    @PostMapping
    @PreAuthorize("@el.check('planImplementDept:add')")
    public ResponseEntity create(@Validated @RequestBody TdPlanImplementDept planImplementDept) {
        if (planImplementDept.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdPlanImplementDeptService.insert(planImplementDept), HttpStatus.CREATED);
    }

    @Log("删除培训实施参与部门扩展表（用于后期统计使用）")
    @ApiOperation("删除培训实施参与部门扩展表（用于后期统计使用）")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('planImplementDept:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdPlanImplementDeptService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该培训实施参与部门扩展表（用于后期统计使用）存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改培训实施参与部门扩展表（用于后期统计使用）")
    @ApiOperation("修改培训实施参与部门扩展表（用于后期统计使用）")
    @PutMapping
    @PreAuthorize("@el.check('planImplementDept:edit')")
    public ResponseEntity update(@Validated(TdPlanImplementDept.Update.class) @RequestBody TdPlanImplementDept planImplementDept) {
        tdPlanImplementDeptService.update(planImplementDept);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个培训实施参与部门扩展表（用于后期统计使用）")
    @ApiOperation("获取单个培训实施参与部门扩展表（用于后期统计使用）")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('planImplementDept:list')")
    public ResponseEntity getPlanImplementDept(@PathVariable Long id) {
        return new ResponseEntity<>(tdPlanImplementDeptService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询培训实施参与部门扩展表（用于后期统计使用）（分页）")
    @ApiOperation("查询培训实施参与部门扩展表（用于后期统计使用）（分页）")
    @GetMapping
    @PreAuthorize("@el.check('planImplementDept:list')")
    public ResponseEntity getPlanImplementDeptPage(TdPlanImplementDeptQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(tdPlanImplementDeptService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询培训实施参与部门扩展表（用于后期统计使用）（不分页）")
    @ApiOperation("查询培训实施参与部门扩展表（用于后期统计使用）（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('planImplementDept:list')")
    public ResponseEntity getPlanImplementDeptNoPaging(TdPlanImplementDeptQueryCriteria criteria) {
    return new ResponseEntity<>(tdPlanImplementDeptService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出培训实施参与部门扩展表（用于后期统计使用）数据")
    @ApiOperation("导出培训实施参与部门扩展表（用于后期统计使用）数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('planImplementDept:list')")
    public void download(HttpServletResponse response, TdPlanImplementDeptQueryCriteria criteria) throws IOException {
        tdPlanImplementDeptService.download(tdPlanImplementDeptService.listAll(criteria), response);
    }
}
