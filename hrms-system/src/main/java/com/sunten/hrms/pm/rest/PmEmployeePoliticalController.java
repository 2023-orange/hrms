package com.sunten.hrms.pm.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.pm.domain.PmEmployeePolitical;
import com.sunten.hrms.pm.dto.PmEmployeePoliticalQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeePoliticalService;
import com.sunten.hrms.utils.ThrowableUtil;
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
 * 政治面貌表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "政治面貌表")
@RequestMapping("/api/pm/employeePolitical")
public class PmEmployeePoliticalController {
    private static final String ENTITY_NAME = "employeePolitical";
    private final PmEmployeePoliticalService pmEmployeePoliticalService;

    public PmEmployeePoliticalController(PmEmployeePoliticalService pmEmployeePoliticalService) {
        this.pmEmployeePoliticalService = pmEmployeePoliticalService;
    }

    @Log("新增政治面貌表")
    @ApiOperation("新增政治面貌表")
    @PostMapping
    @PreAuthorize("@el.check('employeePolitical:add','employee:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeePolitical employeePolitical) {
        if (employeePolitical.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(pmEmployeePoliticalService.insert(employeePolitical), HttpStatus.CREATED);
    }

    @Log("删除政治面貌表")
    @ApiOperation("删除政治面貌表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeePolitical:del','employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeePoliticalService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该政治面貌表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改政治面貌表")
    @ApiOperation("修改政治面貌表")
    @PutMapping
    @PreAuthorize("@el.check('employeePolitical:edit','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployeePolitical.Update.class) @RequestBody PmEmployeePolitical employeePolitical) {
        pmEmployeePoliticalService.update(employeePolitical);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个政治面貌表")
    @ApiOperation("获取单个政治面貌表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeePolitical:list','employee:list')")
    public ResponseEntity getEmployeePolitical(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeePoliticalService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询政治面貌表（分页）")
    @ApiOperation("查询政治面貌表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeePolitical:list','employee:list')")
    public ResponseEntity getEmployeePoliticalPage(PmEmployeePoliticalQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeePoliticalService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询政治面貌表（不分页）")
    @ApiOperation("查询政治面貌表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeePolitical:list','employee:list')")
    public ResponseEntity getEmployeePoliticalNoPaging(PmEmployeePoliticalQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeePoliticalService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出政治面貌表数据")
    @ApiOperation("导出政治面貌表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeePolitical:list','employee:list')")
    public void download(HttpServletResponse response, PmEmployeePoliticalQueryCriteria criteria) throws IOException {
        pmEmployeePoliticalService.download(pmEmployeePoliticalService.listAll(criteria), response);
    }

    @Log("批量编辑政治面貌表")
    @ApiOperation("批量编辑政治面貌表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('employeePolitical:add','employeePolitical:edit','employee:add','employee:edit')")
    public ResponseEntity batchSave(@Validated @RequestBody List<PmEmployeePolitical> employeePoliticals) {
        if (employeePoliticals == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(pmEmployeePoliticalService.batchInsert(employeePoliticals, null), HttpStatus.CREATED);
    }
}
