package com.sunten.hrms.fnd.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.fnd.domain.FndAuthorizationDts;
import com.sunten.hrms.fnd.dto.FndAuthorizationDtsQueryCriteria;
import com.sunten.hrms.fnd.service.FndAuthorizationDtsService;
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
 * @author xukai
 * @since 2021-02-02
 */
@RestController
@Api(tags = "被授权明细")
@RequestMapping("/api/fnd/authorizationDts")
public class FndAuthorizationDtsController {
    private static final String ENTITY_NAME = "authorizationDts";
    private final FndAuthorizationDtsService fndAuthorizationDtsService;

    public FndAuthorizationDtsController(FndAuthorizationDtsService fndAuthorizationDtsService) {
        this.fndAuthorizationDtsService = fndAuthorizationDtsService;
    }

    @Log("新增")
    @ApiOperation("新增")
    @PostMapping
    @PreAuthorize("@el.check('authorizationDts:add')")
    public ResponseEntity create(@Validated @RequestBody FndAuthorizationDts authorizationDts) {
        return new ResponseEntity<>(fndAuthorizationDtsService.insert(authorizationDts), HttpStatus.CREATED);
    }

    @Log("删除")
    @ApiOperation("删除")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('authorizationDts:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            fndAuthorizationDtsService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改")
    @ApiOperation("修改")
    @PutMapping
    @PreAuthorize("@el.check('authorizationDts:edit')")
    public ResponseEntity update(@Validated(FndAuthorizationDts.Update.class) @RequestBody FndAuthorizationDts authorizationDts) {
        fndAuthorizationDtsService.update(authorizationDts);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个")
    @ApiOperation("获取单个")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('authorizationDts:list')")
    public ResponseEntity getAuthorizationDts(@PathVariable Long id) {
        return new ResponseEntity<>(fndAuthorizationDtsService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping
    @PreAuthorize("@el.check('authorizationDts:list')")
    public ResponseEntity getAuthorizationDtsPage(FndAuthorizationDtsQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(fndAuthorizationDtsService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询（不分页）")
    @ApiOperation("查询（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('authorizationDts:list')")
    public ResponseEntity getAuthorizationDtsNoPaging(FndAuthorizationDtsQueryCriteria criteria) {
    return new ResponseEntity<>(fndAuthorizationDtsService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('authorizationDts:list')")
    public void download(HttpServletResponse response, FndAuthorizationDtsQueryCriteria criteria) throws IOException {
        fndAuthorizationDtsService.download(fndAuthorizationDtsService.listAll(criteria), response);
    }


    @ErrorLog("获取被授权人权限范围")
    @ApiOperation("获取被授权人权限范围")
    @GetMapping(value = "/getToUserDept/{toId}")
    public ResponseEntity getToUserDept(@PathVariable Long toId) {
        return new ResponseEntity<>(fndAuthorizationDtsService.getAuthorizationDeptsByToEmployee(toId), HttpStatus.OK);
    }

    @Log("更改授权部门")
    @ApiOperation("更改授权部门")
    @PutMapping("/batchUpdate")
    @PreAuthorize("@el.check('authorizationDts:edit')")
    public ResponseEntity batchUpdate(@RequestBody FndAuthorizationDts authorizationDts) {
        fndAuthorizationDtsService.batchUpdate(authorizationDts);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
