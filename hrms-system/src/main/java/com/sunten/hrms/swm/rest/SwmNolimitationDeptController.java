package com.sunten.hrms.swm.rest;

import com.sunten.hrms.swm.dao.SwmNolimitationDeptDao;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmNolimitationDept;
import com.sunten.hrms.swm.dto.SwmNolimitationDeptQueryCriteria;
import com.sunten.hrms.swm.service.SwmNolimitationDeptService;
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
 *  前端控制器
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-21
 */
@RestController
@Api(tags = "")
@RequestMapping("/api/swm/nolimitationDept")
public class SwmNolimitationDeptController {
    private static final String ENTITY_NAME = "nolimitationDept";
    private final SwmNolimitationDeptService swmNolimitationDeptService;
    private SwmNolimitationDeptDao swmNolimitationDeptDao;

    public SwmNolimitationDeptController(SwmNolimitationDeptService swmNolimitationDeptService, SwmNolimitationDeptDao swmNolimitationDeptDao) {
        this.swmNolimitationDeptService = swmNolimitationDeptService;
        this.swmNolimitationDeptDao = swmNolimitationDeptDao;
    }

    @Log("新增")
    @ApiOperation("新增")
    @PostMapping
    public ResponseEntity create(@Validated @RequestBody SwmNolimitationDept nolimitationDept) {
        return new ResponseEntity<>(swmNolimitationDeptService.insert(nolimitationDept), HttpStatus.CREATED);
    }

    @Log("删除")
    @ApiOperation("删除")
    @DeleteMapping(value = "")
    @PreAuthorize("@el.check('nolimitationDept:del')")
    public ResponseEntity delete() {
        try {
            swmNolimitationDeptService.delete();
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改")
    @ApiOperation("修改")
    @PutMapping
//    @PreAuthorize("@el.check('nolimitationDept:edit')")
    public ResponseEntity update(@Validated(SwmNolimitationDept.Update.class) @RequestBody SwmNolimitationDept nolimitationDept) {
        swmNolimitationDeptService.update(nolimitationDept);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个")
    @ApiOperation("获取单个")
    @GetMapping(value = "id")
    @PreAuthorize("@el.check('nolimitationDept:list')")
    public ResponseEntity getNolimitationDept() {
        return new ResponseEntity<>(swmNolimitationDeptService.getByKey(), HttpStatus.OK);
    }

    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping
    @PreAuthorize("@el.check('nolimitationDept:list')")
    public ResponseEntity getNolimitationDeptPage(SwmNolimitationDeptQueryCriteria criteria, @PageableDefault(value = 9999, sort = {}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmNolimitationDeptService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询（不分页）")
    @ApiOperation("查询（不分页）")
    @GetMapping(value = "/noPaging")
//    @PreAuthorize("@el.check('nolimitationDept:list')")
    public ResponseEntity getNolimitationDeptNoPaging(SwmNolimitationDeptQueryCriteria criteria) {
    return new ResponseEntity<>(swmNolimitationDeptService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('nolimitationDept:list')")
    public void download(HttpServletResponse response, SwmNolimitationDeptQueryCriteria criteria) throws IOException {
        swmNolimitationDeptService.download(swmNolimitationDeptService.listAll(criteria), response);
    }

    @ErrorLog("检查是否已经存在部门")
    @ApiOperation("检查是否已经存在部门")
    @PostMapping(value = "/checkDept")
    public ResponseEntity checkDept(@Validated @RequestBody SwmNolimitationDept criteria) {
        return new ResponseEntity<>(swmNolimitationDeptService.countDept(criteria.getDeptName()), HttpStatus.OK);
    }

    @ErrorLog("获取薪酬的所有部门")
    @ApiOperation("获取单个")
    @GetMapping(value = "/getSwmDept")
    public ResponseEntity getSwmDept() {
        return new ResponseEntity<>(swmNolimitationDeptService.getSwmDept(), HttpStatus.OK);
    }
}
