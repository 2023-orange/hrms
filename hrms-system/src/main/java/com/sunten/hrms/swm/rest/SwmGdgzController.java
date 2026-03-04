package com.sunten.hrms.swm.rest;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.dto.SwmGdgzDTO;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.swm.domain.SwmGdgz;
import com.sunten.hrms.swm.dto.SwmGdgzQueryCriteria;
import com.sunten.hrms.swm.service.SwmGdgzService;
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
 * 旧的固定工资表 前端控制器
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-07
 */
@RestController
@Api(tags = "旧的固定工资表")
@RequestMapping("/api/swm/gdgz")
public class SwmGdgzController {
    private static final String ENTITY_NAME = "gdgz";
    private final SwmGdgzService swmGdgzService;
    private final FndUserService fndUserService;

    public SwmGdgzController(SwmGdgzService swmGdgzService, FndUserService fndUserService) {
        this.swmGdgzService = swmGdgzService;
        this.fndUserService = fndUserService;
    }

    @Log("新增旧的固定工资表")
    @ApiOperation("新增旧的固定工资表")
    @PostMapping
    @PreAuthorize("@el.check('gdgz:add')")
    public ResponseEntity create(@Validated @RequestBody SwmGdgz gdgz) {
        return new ResponseEntity<>(swmGdgzService.insert(gdgz), HttpStatus.CREATED);
    }

//    @Log("删除旧的固定工资表")
//    @ApiOperation("删除旧的固定工资表")
//    @DeleteMapping(value = "")
//    @PreAuthorize("@el.check('gdgz:del')")
//    public ResponseEntity delete() {
//        try {
//            swmGdgzService.delete();
//        } catch (Throwable e) {
//            ThrowableUtil.throwForeignKeyException(e, "该旧的固定工资表存在 关联，请取消关联后再试");
//        }
//        return new ResponseEntity(HttpStatus.OK);
//    }

    @Log("修改旧的固定工资表")
    @ApiOperation("修改旧的固定工资表")
    @PutMapping
    @PreAuthorize("@el.check('gdgz:edit')")
    public ResponseEntity update(@Validated(SwmGdgz.Update.class) @RequestBody SwmGdgz gdgz) {
        swmGdgzService.update(gdgz);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

//    @ErrorLog("获取单个旧的固定工资表")
//    @ApiOperation("获取单个旧的固定工资表")
//    @GetMapping(value = "")
//    @PreAuthorize("@el.check('gdgz:list')")
//    public ResponseEntity getGdgz() {
//        return new ResponseEntity<>(swmGdgzService.getByKey(), HttpStatus.OK);
//    }

    @ErrorLog("查询旧的固定工资表（分页）")
    @ApiOperation("查询旧的固定工资表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('gdgz:list')")
    public ResponseEntity getGdgzPage(SwmGdgzQueryCriteria criteria, @PageableDefault(value = 9999, sort = {}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmGdgzService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询旧的固定工资表（不分页）")
    @ApiOperation("查询旧的固定工资表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('gdgz:list')")
    public ResponseEntity getGdgzNoPaging(SwmGdgzQueryCriteria criteria) {
    return new ResponseEntity<>(swmGdgzService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出旧的固定工资表数据")
    @ApiOperation("导出旧的固定工资表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('gdgz:list')")
    public void download(HttpServletResponse response, SwmGdgzQueryCriteria criteria) throws IOException {
        swmGdgzService.download(swmGdgzService.listAll(criteria), response);
    }

    @ErrorLog("返回个人旧的固定工资")
    @ApiOperation("返回个人旧的固定工资")
    @GetMapping(value = "/getOldFixedSalary")
    @PreAuthorize("@el.check('gdgz:list')")
    public ResponseEntity getOldFixedSalary(String year){
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        if (null == user.getEmployee()) {
            throw new InfoCheckWarningMessException("当前用户尚未绑定用户， 请连续管理员进行绑定");
        }
        List<SwmGdgzDTO> swmGdgzs = swmGdgzService.getOldFixedSalary(user.getEmployee().getWorkCard(), year);
        return new ResponseEntity<>(swmGdgzs, HttpStatus.OK);
    }
}
