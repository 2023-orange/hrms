package com.sunten.hrms.pm.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.EmployeeDZB;
import com.sunten.hrms.pm.dto.EmployeeDZBQueryCriteria;
import com.sunten.hrms.pm.service.EmployeeDZBService;
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
 * @since 2021-09-09
 */
@RestController
@Api(tags = "")
@RequestMapping("/api/pm/employeeDZB")
public class EmployeeDZBController {
    private static final String ENTITY_NAME = "employeeDZB";
    private final EmployeeDZBService employeeDZBService;

    public EmployeeDZBController(EmployeeDZBService employeeDZBService) {
        this.employeeDZBService = employeeDZBService;
    }

    @Log("新增")
    @ApiOperation("新增")
    @PostMapping
    @PreAuthorize("@el.check('employeeDZB:add')")
    public ResponseEntity create(@Validated @RequestBody EmployeeDZB employeeDZB) {
        if (employeeDZB.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(employeeDZBService.insert(employeeDZB), HttpStatus.CREATED);
    }

    @Log("删除")
    @ApiOperation("删除")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeDZB:del')")
    public ResponseEntity delete(@PathVariable Integer id) {
        try {
            employeeDZBService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改")
    @ApiOperation("修改")
    @PutMapping
    @PreAuthorize("@el.check('employeeDZB:edit')")
    public ResponseEntity update(@Validated(EmployeeDZB.Update.class) @RequestBody EmployeeDZB employeeDZB) {
        employeeDZBService.update(employeeDZB);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个")
    @ApiOperation("获取单个")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeDZB:list')")
    public ResponseEntity getemployeeDZB(@PathVariable Integer id) {
        return new ResponseEntity<>(employeeDZBService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeDZB:list')")
    public ResponseEntity getemployeeDZBPage(EmployeeDZBQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(employeeDZBService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询（不分页）")
    @ApiOperation("查询（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeDZB:list')")
    public ResponseEntity getemployeeDZBNoPaging(EmployeeDZBQueryCriteria criteria) {
    return new ResponseEntity<>(employeeDZBService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeDZB:list')")
    public void download(HttpServletResponse response, EmployeeDZBQueryCriteria criteria) throws IOException {
        employeeDZBService.download(employeeDZBService.listAll(criteria), response);
    }
}
