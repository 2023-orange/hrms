package com.sunten.hrms.fnd.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.fnd.domain.FndJobDept;
import com.sunten.hrms.fnd.dto.FndJobDeptQueryCriteria;
import com.sunten.hrms.fnd.service.FndJobDeptService;
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
 * @since 2020-12-03
 */
@RestController
@Api(tags = "")
@RequestMapping("/api/fnd/jobDept")
public class FndJobDeptController {
    private static final String ENTITY_NAME = "jobDept";
    private final FndJobDeptService fndJobDeptService;

    public FndJobDeptController(FndJobDeptService fndJobDeptService) {
        this.fndJobDeptService = fndJobDeptService;
    }

    @Log("新增")
    @ApiOperation("新增")
    @PostMapping
    @PreAuthorize("@el.check('jobDept:add')")
    public ResponseEntity create(@Validated @RequestBody FndJobDept jobDept) {
        return new ResponseEntity<>(fndJobDeptService.insert(jobDept), HttpStatus.CREATED);
    }

    @Log("删除")
    @ApiOperation("删除")
    @DeleteMapping(value = "/{deptId}/{jobId}")
    @PreAuthorize("@el.check('jobDept:del')")
    public ResponseEntity delete(@PathVariable Long deptId, @PathVariable Long jobId) {
        try {
            fndJobDeptService.delete(deptId, jobId);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改")
    @ApiOperation("修改")
    @PutMapping
    @PreAuthorize("@el.check('jobDept:edit')")
    public ResponseEntity update(@Validated(FndJobDept.Update.class) @RequestBody FndJobDept jobDept) {
        fndJobDeptService.update(jobDept);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个")
    @ApiOperation("获取单个")
    @GetMapping(value = "/{deptId}/{jobId}")
    @PreAuthorize("@el.check('jobDept:list')")
    public ResponseEntity getJobDept(@PathVariable Long deptId, @PathVariable Long jobId) {
        return new ResponseEntity<>(fndJobDeptService.getByKey(deptId, jobId), HttpStatus.OK);
    }

    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping
    @PreAuthorize("@el.check('jobDept:list')")
    public ResponseEntity getJobDeptPage(FndJobDeptQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"dept_id", "job_id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(fndJobDeptService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询（不分页）")
    @ApiOperation("查询（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('jobDept:list')")
    public ResponseEntity getJobDeptNoPaging(FndJobDeptQueryCriteria criteria) {
    return new ResponseEntity<>(fndJobDeptService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('jobDept:list')")
    public void download(HttpServletResponse response, FndJobDeptQueryCriteria criteria) throws IOException {
        fndJobDeptService.download(fndJobDeptService.listAll(criteria), response);
    }
}
