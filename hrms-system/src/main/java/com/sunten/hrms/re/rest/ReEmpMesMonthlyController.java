package com.sunten.hrms.re.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.re.domain.ReEmpMesMonthly;
import com.sunten.hrms.re.dto.ReEmpMesMonthlyQueryCriteria;
import com.sunten.hrms.re.service.ReEmpMesMonthlyService;
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
 * 每月人员情况存档 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2022-01-07
 */
@RestController
@Api(tags = "每月人员情况存档")
@RequestMapping("/api/re/empMesMonthly")
public class ReEmpMesMonthlyController {
    private static final String ENTITY_NAME = "empMesMonthly";
    private final ReEmpMesMonthlyService reEmpMesMonthlyService;

    public ReEmpMesMonthlyController(ReEmpMesMonthlyService reEmpMesMonthlyService) {
        this.reEmpMesMonthlyService = reEmpMesMonthlyService;
    }

    @Log("新增每月人员情况存档")
    @ApiOperation("新增每月人员情况存档")
    @PostMapping
    @PreAuthorize("@el.check('empMesMonthly:add')")
    public ResponseEntity create(@Validated @RequestBody ReEmpMesMonthly empMesMonthly) {
        if (empMesMonthly.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(reEmpMesMonthlyService.insert(empMesMonthly), HttpStatus.CREATED);
    }

    @Log("删除每月人员情况存档")
    @ApiOperation("删除每月人员情况存档")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('empMesMonthly:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reEmpMesMonthlyService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该每月人员情况存档存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改每月人员情况存档")
    @ApiOperation("修改每月人员情况存档")
    @PutMapping
    @PreAuthorize("@el.check('empMesMonthly:edit')")
    public ResponseEntity update(@Validated(ReEmpMesMonthly.Update.class) @RequestBody ReEmpMesMonthly empMesMonthly) {
        reEmpMesMonthlyService.update(empMesMonthly);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个每月人员情况存档")
    @ApiOperation("获取单个每月人员情况存档")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('empMesMonthly:list')")
    public ResponseEntity getEmpMesMonthly(@PathVariable Long id) {
        return new ResponseEntity<>(reEmpMesMonthlyService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询每月人员情况存档（分页）")
    @ApiOperation("查询每月人员情况存档（分页）")
    @GetMapping
    @PreAuthorize("@el.check('empMesMonthly:list')")
    public ResponseEntity getEmpMesMonthlyPage(ReEmpMesMonthlyQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reEmpMesMonthlyService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询每月人员情况存档（不分页）")
    @ApiOperation("查询每月人员情况存档（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('empMesMonthly:list')")
    public ResponseEntity getEmpMesMonthlyNoPaging(ReEmpMesMonthlyQueryCriteria criteria) {
    return new ResponseEntity<>(reEmpMesMonthlyService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出每月人员情况存档数据")
    @ApiOperation("导出每月人员情况存档数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('empMesMonthly:list')")
    public void download(HttpServletResponse response, ReEmpMesMonthlyQueryCriteria criteria) throws IOException {
        reEmpMesMonthlyService.download(reEmpMesMonthlyService.listAll(criteria), response);
    }
}
