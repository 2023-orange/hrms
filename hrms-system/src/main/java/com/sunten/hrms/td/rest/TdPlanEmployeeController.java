package com.sunten.hrms.td.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.td.domain.TdPlanEmployee;
import com.sunten.hrms.td.dto.TdPlanEmployeeQueryCriteria;
import com.sunten.hrms.td.service.TdPlanEmployeeService;
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
 * 参训人员表（包括讲师） 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-05-25
 */
@RestController
@Api(tags = "参训人员表（包括讲师）")
@RequestMapping("/api/td/planEmployee")
public class TdPlanEmployeeController {
    private static final String ENTITY_NAME = "planEmployee";
    private final TdPlanEmployeeService tdPlanEmployeeService;

    public TdPlanEmployeeController(TdPlanEmployeeService tdPlanEmployeeService) {
        this.tdPlanEmployeeService = tdPlanEmployeeService;
    }

    @Log("新增参训人员表（包括讲师）")
    @ApiOperation("新增参训人员表（包括讲师）")
    @PostMapping
    @PreAuthorize("@el.check('planEmployee:add')")
    public ResponseEntity create(@Validated @RequestBody TdPlanEmployee planEmployee) {
        if (planEmployee.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdPlanEmployeeService.insert(planEmployee), HttpStatus.CREATED);
    }

    @Log("删除参训人员表（包括讲师）")
    @ApiOperation("删除参训人员表（包括讲师）")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('planEmployee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdPlanEmployeeService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该参训人员表（包括讲师）存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改参训人员表（包括讲师）")
    @ApiOperation("修改参训人员表（包括讲师）")
    @PutMapping
    @PreAuthorize("@el.check('planEmployee:edit')")
    public ResponseEntity update(@Validated(TdPlanEmployee.Update.class) @RequestBody TdPlanEmployee planEmployee) {
        tdPlanEmployeeService.update(planEmployee);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个参训人员表（包括讲师）")
    @ApiOperation("获取单个参训人员表（包括讲师）")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('planEmployee:list')")
    public ResponseEntity getPlanEmployee(@PathVariable Long id) {
        return new ResponseEntity<>(tdPlanEmployeeService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询参训人员表（包括讲师）（分页）")
    @ApiOperation("查询参训人员表（包括讲师）（分页）")
    @GetMapping
    @PreAuthorize("@el.check('planEmployee:list')")
    public ResponseEntity getPlanEmployeePage(TdPlanEmployeeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(tdPlanEmployeeService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询参训人员表（包括讲师）（不分页）")
    @ApiOperation("查询参训人员表（包括讲师）（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('planEmployee:list')")
    public ResponseEntity getPlanEmployeeNoPaging(TdPlanEmployeeQueryCriteria criteria) {
    return new ResponseEntity<>(tdPlanEmployeeService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出参训人员表（包括讲师）数据")
    @ApiOperation("导出参训人员表（包括讲师）数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('planEmployee:list')")
    public void download(HttpServletResponse response, TdPlanEmployeeQueryCriteria criteria) throws IOException {
        tdPlanEmployeeService.download(tdPlanEmployeeService.listAll(criteria), response);
    }
}
