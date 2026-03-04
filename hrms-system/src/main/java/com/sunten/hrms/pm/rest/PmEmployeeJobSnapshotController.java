package com.sunten.hrms.pm.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.PmEmployeeJobSnapshot;
import com.sunten.hrms.pm.dto.PmEmployeeJobSnapshotQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeJobSnapshotService;
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
 * 部门科室岗位关系快照 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@RestController
@Api(tags = "部门科室岗位关系快照")
@RequestMapping("/api/pm/employeeJobSnapshot")
public class PmEmployeeJobSnapshotController {
    private static final String ENTITY_NAME = "employeeJobSnapshot";
    private final PmEmployeeJobSnapshotService pmEmployeeJobSnapshotService;

    public PmEmployeeJobSnapshotController(PmEmployeeJobSnapshotService pmEmployeeJobSnapshotService) {
        this.pmEmployeeJobSnapshotService = pmEmployeeJobSnapshotService;
    }

    @Log("新增部门科室岗位关系快照")
    @ApiOperation("新增部门科室岗位关系快照")
    @PostMapping
    @PreAuthorize("@el.check('employeeJobSnapshot:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeeJobSnapshot employeeJobSnapshot) {
        if (employeeJobSnapshot.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeJobSnapshotService.insert(employeeJobSnapshot), HttpStatus.CREATED);
    }

    @Log("删除部门科室岗位关系快照")
    @ApiOperation("删除部门科室岗位关系快照")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeJobSnapshot:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeJobSnapshotService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该部门科室岗位关系快照存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改部门科室岗位关系快照")
    @ApiOperation("修改部门科室岗位关系快照")
    @PutMapping
    @PreAuthorize("@el.check('employeeJobSnapshot:edit')")
    public ResponseEntity update(@Validated(PmEmployeeJobSnapshot.Update.class) @RequestBody PmEmployeeJobSnapshot employeeJobSnapshot) {
        pmEmployeeJobSnapshotService.update(employeeJobSnapshot);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个部门科室岗位关系快照")
    @ApiOperation("获取单个部门科室岗位关系快照")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeJobSnapshot:list')")
    public ResponseEntity getEmployeeJobSnapshot(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeJobSnapshotService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询部门科室岗位关系快照（分页）")
    @ApiOperation("查询部门科室岗位关系快照（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeJobSnapshot:list')")
    public ResponseEntity getEmployeeJobSnapshotPage(PmEmployeeJobSnapshotQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeJobSnapshotService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询部门科室岗位关系快照（不分页）")
    @ApiOperation("查询部门科室岗位关系快照（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeJobSnapshot:list')")
    public ResponseEntity getEmployeeJobSnapshotNoPaging(PmEmployeeJobSnapshotQueryCriteria criteria) {
    return new ResponseEntity<>(pmEmployeeJobSnapshotService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出部门科室岗位关系快照数据")
    @ApiOperation("导出部门科室岗位关系快照数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeJobSnapshot:list')")
    public void download(HttpServletResponse response, PmEmployeeJobSnapshotQueryCriteria criteria) throws IOException {
        pmEmployeeJobSnapshotService.download(pmEmployeeJobSnapshotService.listAll(criteria), response);
    }
}
