package com.sunten.hrms.ac.rest;

import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.AcExceptionDispose;
import com.sunten.hrms.ac.dto.AcExceptionDisposeQueryCriteria;
import com.sunten.hrms.ac.service.AcExceptionDisposeService;
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
import java.util.Iterator;
import java.util.Set;

/**
 * <p>
 * 考勤异常及处理表 前端控制器
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@RestController
@Api(tags = "考勤异常及处理表")
@RequestMapping("/api/ac/exceptionDispose")
public class AcExceptionDisposeController {
    private static final String ENTITY_NAME = "exceptionDispose";
    private final AcExceptionDisposeService acExceptionDisposeService;
    private final FndDataScope fndDataScope;

    public AcExceptionDisposeController(AcExceptionDisposeService acExceptionDisposeService, FndDataScope fndDataScope) {
        this.acExceptionDisposeService = acExceptionDisposeService;
        this.fndDataScope = fndDataScope;
    }

    @Log("新增考勤异常及处理表")
    @ApiOperation("新增考勤异常及处理表")
    @PostMapping
    @PreAuthorize("@el.check('exceptionDispose:add')")
    public ResponseEntity create(@Validated @RequestBody AcExceptionDispose exceptionDispose) {
        if (exceptionDispose.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(acExceptionDisposeService.insert(exceptionDispose), HttpStatus.CREATED);
    }

    @Log("删除考勤异常及处理表")
    @ApiOperation("删除考勤异常及处理表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('exceptionDispose:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acExceptionDisposeService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该考勤异常及处理表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改考勤异常及处理表")
    @ApiOperation("修改考勤异常及处理表")
    @PutMapping
    @PreAuthorize("@el.check('exceptionDispose:edit')")
    public ResponseEntity update(@RequestBody AcExceptionDispose exceptionDispose) {
        acExceptionDisposeService.update(exceptionDispose);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个考勤异常及处理表")
    @ApiOperation("获取单个考勤异常及处理表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('exceptionDispose:list')")
    public ResponseEntity getExceptionDispose(@PathVariable Long id) {
        return new ResponseEntity<>(acExceptionDisposeService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询考勤异常及处理表（分页）")
    @ApiOperation("查询考勤异常及处理表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('exceptionDispose:list')")
    public ResponseEntity getExceptionDisposePage(AcExceptionDisposeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Set deptSet = fndDataScope.getDeptIds();
        if(deptSet != null && !deptSet.isEmpty()){
            if(criteria == null){
                criteria = new AcExceptionDisposeQueryCriteria();
            }
            Iterator<Long> iterable = deptSet.iterator();
            while (iterable.hasNext()) {
                Long thisId = iterable.next();
                if(thisId.longValue() < 0) {
                    criteria.setEmployeeId(-thisId);
                }
            }
            if(criteria.getEmployeeId() == null) {
                criteria.setDeptIds(deptSet);
            }
        }

        return new ResponseEntity<>(acExceptionDisposeService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询考勤异常及处理表（不分页）")
    @ApiOperation("查询考勤异常及处理表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('exceptionDispose:list')")
    public ResponseEntity getExceptionDisposeNoPaging(AcExceptionDisposeQueryCriteria criteria) {
    return new ResponseEntity<>(acExceptionDisposeService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出考勤异常及处理表数据")
    @ApiOperation("导出考勤异常及处理表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('exceptionDispose:list')")
    public void download(HttpServletResponse response, AcExceptionDisposeQueryCriteria criteria) throws IOException {
        acExceptionDisposeService.download(acExceptionDisposeService.listAll(criteria), response);
    }
}
