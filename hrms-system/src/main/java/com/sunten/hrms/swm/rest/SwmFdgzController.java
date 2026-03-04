package com.sunten.hrms.swm.rest;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.dto.SwmFdgzDTO;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmFdgz;
import com.sunten.hrms.swm.dto.SwmFdgzQueryCriteria;
import com.sunten.hrms.swm.service.SwmFdgzService;
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
 * 旧的浮动工资表 前端控制器
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-10
 */
@RestController
@Api(tags = "旧的浮动工资表")
@RequestMapping("/api/swm/fdgz")
public class SwmFdgzController {
    private static final String ENTITY_NAME = "fdgz";
    private final SwmFdgzService swmFdgzService;
    private final FndUserService fndUserService;

    public SwmFdgzController(SwmFdgzService swmFdgzService, FndUserService fndUserService) {
        this.swmFdgzService = swmFdgzService;
        this.fndUserService = fndUserService;
    }

    @Log("新增旧的浮动工资表")
    @ApiOperation("新增旧的浮动工资表")
    @PostMapping
    @PreAuthorize("@el.check('fdgz:add')")
    public ResponseEntity create(@Validated @RequestBody SwmFdgz fdgz) {
        return new ResponseEntity<>(swmFdgzService.insert(fdgz), HttpStatus.CREATED);
    }

//    @Log("删除旧的浮动工资表")
//    @ApiOperation("删除旧的浮动工资表")
//    @DeleteMapping(value = "")
//    @PreAuthorize("@el.check('fdgz:del')")
//    public ResponseEntity delete() {
//        try {
//            swmFdgzService.delete();
//        } catch (Throwable e) {
//            ThrowableUtil.throwForeignKeyException(e, "该旧的浮动工资表存在 关联，请取消关联后再试");
//        }
//        return new ResponseEntity(HttpStatus.OK);
//    }

    @Log("修改旧的浮动工资表")
    @ApiOperation("修改旧的浮动工资表")
    @PutMapping
    @PreAuthorize("@el.check('fdgz:edit')")
    public ResponseEntity update(@Validated(SwmFdgz.Update.class) @RequestBody SwmFdgz fdgz) {
        swmFdgzService.update(fdgz);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

//    @ErrorLog("获取单个旧的浮动工资表")
//    @ApiOperation("获取单个旧的浮动工资表")
//    @GetMapping(value = "")
//    @PreAuthorize("@el.check('fdgz:list')")
//    public ResponseEntity getFdgz() {
//        return new ResponseEntity<>(swmFdgzService.getByKey(), HttpStatus.OK);
//    }

    @ErrorLog("查询旧的浮动工资表（分页）")
    @ApiOperation("查询旧的浮动工资表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('fdgz:list')")
    public ResponseEntity getFdgzPage(SwmFdgzQueryCriteria criteria, @PageableDefault(value = 9999, sort = {}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmFdgzService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询旧的浮动工资表（不分页）")
    @ApiOperation("查询旧的浮动工资表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('fdgz:list')")
    public ResponseEntity getFdgzNoPaging(SwmFdgzQueryCriteria criteria) {
    return new ResponseEntity<>(swmFdgzService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出旧的浮动工资表数据")
    @ApiOperation("导出旧的浮动工资表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('fdgz:list')")
    public void download(HttpServletResponse response, SwmFdgzQueryCriteria criteria) throws IOException {
        swmFdgzService.download(swmFdgzService.listAll(criteria), response);
    }

    @ErrorLog("返回个人旧的浮动工资")
    @ApiOperation("返回个人旧的浮动工资")
    @GetMapping(value = "/getOldFloatSalary")
    @PreAuthorize("@el.check('fdgz:list')")
    public ResponseEntity getOldFloatSalary(String year){
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        if (null == user.getEmployee()) {
            throw new InfoCheckWarningMessException("当前用户尚未绑定用户， 请连续管理员进行绑定");
        }
        List<SwmFdgzDTO> swmFdgzs = swmFdgzService.getOldFloatSalary(user.getEmployee().getWorkCard(), year);
        return new ResponseEntity<>(swmFdgzs, HttpStatus.OK);
    }
}
