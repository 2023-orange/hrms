package com.sunten.hrms.ac.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.AcOvertimeApplicationLine;
import com.sunten.hrms.ac.dto.AcOvertimeApplicationLineQueryCriteria;
import com.sunten.hrms.ac.service.AcOvertimeApplicationLineService;
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
 * @author zouyp
 * @since 2023-10-16
 */
@RestController
@Api(tags = "")
@RequestMapping("/api/ac/overtimeApplicationLine")
public class AcOvertimeApplicationLineController {
    private static final String ENTITY_NAME = "overtimeApplicationLine";
    private final AcOvertimeApplicationLineService acOvertimeApplicationLineService;

    public AcOvertimeApplicationLineController(AcOvertimeApplicationLineService acOvertimeApplicationLineService) {
        this.acOvertimeApplicationLineService = acOvertimeApplicationLineService;
    }

    @Log("新增")
    @ApiOperation("新增")
    @PostMapping
    @PreAuthorize("@el.check('overtimeApplicationLine:add')")
    public ResponseEntity create(@Validated @RequestBody AcOvertimeApplicationLine overtimeApplicationLine) {
        if (overtimeApplicationLine.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(acOvertimeApplicationLineService.insert(overtimeApplicationLine), HttpStatus.CREATED);
    }

    @Log("删除")
    @ApiOperation("删除")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('overtimeApplicationLine:del')")
    public ResponseEntity delete(@PathVariable Integer id) {
        try {
            acOvertimeApplicationLineService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改")
    @ApiOperation("修改")
    @PutMapping
    @PreAuthorize("@el.check('overtimeApplicationLine:edit')")
    public ResponseEntity update(@Validated(AcOvertimeApplicationLine.Update.class) @RequestBody AcOvertimeApplicationLine overtimeApplicationLine) {
        acOvertimeApplicationLineService.update(overtimeApplicationLine);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个")
    @ApiOperation("获取单个")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('overtimeApplicationLine:list')")
    public ResponseEntity getOvertimeApplicationLine(@PathVariable Integer id) {
        return new ResponseEntity<>(acOvertimeApplicationLineService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping
    @PreAuthorize("@el.check('overtimeApplicationLine:list')")
    public ResponseEntity getOvertimeApplicationLinePage(AcOvertimeApplicationLineQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(acOvertimeApplicationLineService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询（不分页）")
    @ApiOperation("查询（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('overtimeApplicationLine:list')")
    public ResponseEntity getOvertimeApplicationLineNoPaging(AcOvertimeApplicationLineQueryCriteria criteria) {
    return new ResponseEntity<>(acOvertimeApplicationLineService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('overtimeApplicationLine:list')")
    public void download(HttpServletResponse response, AcOvertimeApplicationLineQueryCriteria criteria) throws IOException {
        acOvertimeApplicationLineService.download(acOvertimeApplicationLineService.listAll(criteria), response);
    }
}
