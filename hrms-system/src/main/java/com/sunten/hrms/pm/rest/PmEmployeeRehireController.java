package com.sunten.hrms.pm.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.pm.domain.PmEmployeeRehire;
import com.sunten.hrms.pm.dto.PmEmployeeRehireQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeRehireService;
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

/**
 * <p>
 * 返聘协议表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "返聘协议表")
@RequestMapping("/api/pm/employeeRehire")
public class PmEmployeeRehireController {
    private static final String ENTITY_NAME = "employeeRehire";
    private final PmEmployeeRehireService pmEmployeeRehireService;

    public PmEmployeeRehireController(PmEmployeeRehireService pmEmployeeRehireService) {
        this.pmEmployeeRehireService = pmEmployeeRehireService;
    }

    @Log("新增返聘协议表")
    @ApiOperation("新增返聘协议表")
    @PostMapping
    @PreAuthorize("@el.check('employeeRehire:add','employee:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeeRehire employeeRehire) {
        if (employeeRehire.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeRehireService.insert(employeeRehire), HttpStatus.CREATED);
    }

    @Log("删除返聘协议表")
    @ApiOperation("删除返聘协议表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeRehire:del','employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeRehireService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该返聘协议表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改返聘协议表")
    @ApiOperation("修改返聘协议表")
    @PutMapping
    @PreAuthorize("@el.check('employeeRehire:edit','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployeeRehire.Update.class) @RequestBody PmEmployeeRehire employeeRehire) {
        pmEmployeeRehireService.update(employeeRehire);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("获取单个返聘协议表")
    @ApiOperation("获取单个返聘协议表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeRehire:list','employee:list')")
    public ResponseEntity getEmployeeRehire(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeRehireService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询返聘协议表（分页）")
    @ApiOperation("查询返聘协议表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeRehire:list','employee:list')")
    public ResponseEntity getEmployeeRehirePage(PmEmployeeRehireQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeRehireService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询返聘协议表（不分页）")
    @ApiOperation("查询返聘协议表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeRehire:list','employee:list')")
    public ResponseEntity getEmployeeRehireNoPaging(PmEmployeeRehireQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeRehireService.listAll(criteria), HttpStatus.OK);
    }


    @ErrorLog("导出返聘协议表数据")
    @ApiOperation("导出返聘协议表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeRehire:list','employee:list')")
    public void download(HttpServletResponse response, PmEmployeeRehireQueryCriteria criteria) throws IOException {
        pmEmployeeRehireService.download(pmEmployeeRehireService.listAll(criteria), response);
    }
}
