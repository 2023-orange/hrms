package com.sunten.hrms.pm.rest;

import com.sunten.hrms.pm.vo.PmEmployeeAwardVo;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.PmEmployeeAwardInterface;
import com.sunten.hrms.pm.dto.PmEmployeeAwardInterfaceQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeAwardInterfaceService;
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
 * @author xk
 * @since 2021-09-23
 */
@RestController
@Api(tags = "")
@RequestMapping("/api/pm/employeeAwardInterface")
public class PmEmployeeAwardInterfaceController {
    private static final String ENTITY_NAME = "employeeAwardInterface";
    private final PmEmployeeAwardInterfaceService pmEmployeeAwardInterfaceService;

    public PmEmployeeAwardInterfaceController(PmEmployeeAwardInterfaceService pmEmployeeAwardInterfaceService) {
        this.pmEmployeeAwardInterfaceService = pmEmployeeAwardInterfaceService;
    }

    @Log("新增")
    @ApiOperation("新增")
    @PostMapping
    @PreAuthorize("@el.check('employeeAward:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeeAwardInterface employeeAwardInterface) {
        if (employeeAwardInterface.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeAwardInterfaceService.insert(employeeAwardInterface), HttpStatus.CREATED);
    }

    @Log("删除")
    @ApiOperation("删除")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeAward:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeAwardInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改")
    @ApiOperation("修改")
    @PutMapping
    @PreAuthorize("@el.check('employeeAward:edit')")
    public ResponseEntity update(@Validated(PmEmployeeAwardInterface.Update.class) @RequestBody PmEmployeeAwardInterface employeeAwardInterface) {
        pmEmployeeAwardInterfaceService.update(employeeAwardInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个")
    @ApiOperation("获取单个")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeAward:list')")
    public ResponseEntity getEmployeeAwardInterface(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeAwardInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeAward:list')")
    public ResponseEntity getEmployeeAwardInterfacePage(PmEmployeeAwardInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeAwardInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询（不分页）")
    @ApiOperation("查询（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeAward:list')")
    public ResponseEntity getEmployeeAwardInterfaceNoPaging(PmEmployeeAwardInterfaceQueryCriteria criteria) {
    return new ResponseEntity<>(pmEmployeeAwardInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeAward:list')")
    public void download(HttpServletResponse response, PmEmployeeAwardInterfaceQueryCriteria criteria) throws IOException {
        pmEmployeeAwardInterfaceService.download(pmEmployeeAwardInterfaceService.listAll(criteria), response);
    }

//    @Log("奖罚情况导入")
//    @ApiOperation("奖罚情况导入")
//    @PutMapping(value="/importAwardInterface")
//    @PreAuthorize("@el.check('employeeAward:edit')")
//    public ResponseEntity importAward(@RequestBody List<PmEmployeeAwardInterface> employeeAwardInterfaces) {
//        pmEmployeeAwardInterfaceService.importAwardByExcel(employeeAwardInterfaces);
//        return new ResponseEntity(HttpStatus.NO_CONTENT);
//    }

    @Log("奖罚情况导入在用")
    @ApiOperation("奖罚情况导入在用")
    @PutMapping(value = "/importExcel")
    @PreAuthorize("@el.check('employeeAward:edit')")
    public ResponseEntity importExcel(@RequestBody PmEmployeeAwardVo pmEmployeeAwardVo) {
        return new ResponseEntity<>(pmEmployeeAwardInterfaceService.importAwardByExcel(pmEmployeeAwardVo.getPmEmployeeAwardInterfaceList(), pmEmployeeAwardVo.getReImportFlag()), HttpStatus.OK);
    }
}
