package com.sunten.hrms.fnd.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.fnd.domain.FndDeptSnapshot;
import com.sunten.hrms.fnd.dto.FndDeptSnapshotQueryCriteria;
import com.sunten.hrms.fnd.service.FndDeptSnapshotService;
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
 * 组织架构快照 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@RestController
@Api(tags = "组织架构快照")
@RequestMapping("/api/fnd/deptSnapshot")
public class FndDeptSnapshotController {
    private static final String ENTITY_NAME = "deptSnapshot";
    private final FndDeptSnapshotService fndDeptSnapshotService;

    public FndDeptSnapshotController(FndDeptSnapshotService fndDeptSnapshotService) {
        this.fndDeptSnapshotService = fndDeptSnapshotService;
    }

    @Log("新增组织架构快照")
    @ApiOperation("新增组织架构快照")
    @PostMapping
    @PreAuthorize("@el.check('deptSnapshot:add')")
    public ResponseEntity create(@Validated @RequestBody FndDeptSnapshot deptSnapshot) {
        if (deptSnapshot.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(fndDeptSnapshotService.insert(deptSnapshot), HttpStatus.CREATED);
    }

    @Log("删除组织架构快照")
    @ApiOperation("删除组织架构快照")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('deptSnapshot:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            fndDeptSnapshotService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该组织架构快照存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改组织架构快照")
    @ApiOperation("修改组织架构快照")
    @PutMapping
    @PreAuthorize("@el.check('deptSnapshot:edit')")
    public ResponseEntity update(@Validated(FndDeptSnapshot.Update.class) @RequestBody FndDeptSnapshot deptSnapshot) {
        fndDeptSnapshotService.update(deptSnapshot);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个组织架构快照")
    @ApiOperation("获取单个组织架构快照")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('deptSnapshot:list')")
    public ResponseEntity getDeptSnapshot(@PathVariable Long id) {
        return new ResponseEntity<>(fndDeptSnapshotService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询组织架构快照（分页）")
    @ApiOperation("查询组织架构快照（分页）")
    @GetMapping
    @PreAuthorize("@el.check('deptSnapshot:list')")
    public ResponseEntity getDeptSnapshotPage(FndDeptSnapshotQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(fndDeptSnapshotService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询组织架构快照（不分页）")
    @ApiOperation("查询组织架构快照（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('deptSnapshot:list')")
    public ResponseEntity getDeptSnapshotNoPaging(FndDeptSnapshotQueryCriteria criteria) {
    return new ResponseEntity<>(fndDeptSnapshotService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出组织架构快照数据")
    @ApiOperation("导出组织架构快照数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('deptSnapshot:list')")
    public void download(HttpServletResponse response, FndDeptSnapshotQueryCriteria criteria) throws IOException {
        fndDeptSnapshotService.download(fndDeptSnapshotService.listAll(criteria), response);
    }
}
