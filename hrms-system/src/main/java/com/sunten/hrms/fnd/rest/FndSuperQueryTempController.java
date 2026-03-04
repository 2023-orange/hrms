package com.sunten.hrms.fnd.rest;

import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.fnd.domain.FndSuperQueryTemp;
import com.sunten.hrms.fnd.dto.FndSuperQueryTempQueryCriteria;
import com.sunten.hrms.fnd.service.FndSuperQueryTempService;
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
 * 超级查询数据临时表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-08-19
 */
@RestController
@Api(tags = "超级查询数据临时表")
@RequestMapping("/api/fnd/superQueryTemp")
public class FndSuperQueryTempController {
    private static final String ENTITY_NAME = "superQueryTemp";
    private final FndSuperQueryTempService fndSuperQueryTempService;
    private final FndUserService fndUserService;

    public FndSuperQueryTempController(FndSuperQueryTempService fndSuperQueryTempService, FndUserService fndUserService) {
        this.fndSuperQueryTempService = fndSuperQueryTempService;
        this.fndUserService = fndUserService;
    }

//    @Log("新增超级查询数据临时表")
//    @ApiOperation("新增超级查询数据临时表")
//    @PostMapping
//    @PreAuthorize("@el.check('superQueryTemp:add')")
//    public ResponseEntity create(@Validated @RequestBody FndSuperQueryTemp superQueryTemp) {
//        if (superQueryTemp.getId()  != null) {
//            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
//        }
//        return new ResponseEntity<>(fndSuperQueryTempService.insert(superQueryTemp), HttpStatus.CREATED);
//    }
//
//    @Log("删除超级查询数据临时表")
//    @ApiOperation("删除超级查询数据临时表")
//    @DeleteMapping(value = "/{id}")
//    @PreAuthorize("@el.check('superQueryTemp:del')")
//    public ResponseEntity delete(@PathVariable Long id) {
//        try {
//            fndSuperQueryTempService.delete(id);
//        } catch (Throwable e) {
//            ThrowableUtil.throwForeignKeyException(e, "该超级查询数据临时表存在 关联，请取消关联后再试");
//        }
//        return new ResponseEntity(HttpStatus.OK);
//    }

//    @Log("修改超级查询数据临时表")
//    @ApiOperation("修改超级查询数据临时表")
//    @PutMapping
//    @PreAuthorize("@el.check('superQueryTemp:edit')")
//    public ResponseEntity update(@Validated(FndSuperQueryTemp.Update.class) @RequestBody FndSuperQueryTemp superQueryTemp) {
//        fndSuperQueryTempService.update(superQueryTemp);
//        return new ResponseEntity(HttpStatus.NO_CONTENT);
//    }

//    @ErrorLog("获取单个超级查询数据临时表")
//    @ApiOperation("获取单个超级查询数据临时表")
//    @GetMapping(value = "/{id}")
//    @PreAuthorize("@el.check('superQueryTemp:list')")
//    public ResponseEntity getSuperQueryTemp(@PathVariable Long id) {
//        return new ResponseEntity<>(fndSuperQueryTempService.getByKey(id), HttpStatus.OK);
//    }

    @ErrorLog("查询超级查询数据临时表（分页）")
    @ApiOperation("查询超级查询数据临时表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('superQueryTemp:list')")
    public ResponseEntity getSuperQueryTempPage(FndSuperQueryTempQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"fsqt.type, fsqt.col_name, pe.work_card"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        criteria.setSearchUserId(user.getId());
        return new ResponseEntity<>(fndSuperQueryTempService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询超级查询数据临时表（不分页）")
    @ApiOperation("查询超级查询数据临时表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('superQueryTemp:list')")
    public ResponseEntity getSuperQueryTempNoPaging(FndSuperQueryTempQueryCriteria criteria) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        criteria.setSearchUserId(user.getId());
        return new ResponseEntity<>(fndSuperQueryTempService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出超级查询数据临时表数据")
    @ApiOperation("导出超级查询数据临时表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('superQueryTemp:list')")
    public void download(HttpServletResponse response, FndSuperQueryTempQueryCriteria criteria) throws IOException {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        criteria.setSearchUserId(user.getId());
        criteria.setCreateFlag(false);
        fndSuperQueryTempService.download(fndSuperQueryTempService.listAll(criteria), response);
    }

}
