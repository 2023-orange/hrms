package com.sunten.hrms.swm.rest;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.dto.SwmSalataxDTO;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.swm.domain.SwmSalatax;
import com.sunten.hrms.swm.dto.SwmSalataxQueryCriteria;
import com.sunten.hrms.swm.service.SwmSalataxService;
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
 * 旧的所得税表 前端控制器
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-10
 */
@RestController
@Api(tags = "旧的所得税表")
@RequestMapping("/api/swm/salatax")
public class SwmSalataxController {
    private static final String ENTITY_NAME = "salatax";
    private final SwmSalataxService swmSalataxService;
    private final FndUserService fndUserService;

    public SwmSalataxController(SwmSalataxService swmSalataxService, FndUserService fndUserService) {
        this.swmSalataxService = swmSalataxService;
        this.fndUserService = fndUserService;
    }

    @Log("新增旧的所得税表")
    @ApiOperation("新增旧的所得税表")
    @PostMapping
    @PreAuthorize("@el.check('salatax:add')")
    public ResponseEntity create(@Validated @RequestBody SwmSalatax salatax) {
        return new ResponseEntity<>(swmSalataxService.insert(salatax), HttpStatus.CREATED);
    }

//    @Log("删除旧的所得税表")
//    @ApiOperation("删除旧的所得税表")
//    @DeleteMapping(value = "")
//    @PreAuthorize("@el.check('salatax:del')")
//    public ResponseEntity delete() {
//        try {
//            swmSalataxService.delete();
//        } catch (Throwable e) {
//            ThrowableUtil.throwForeignKeyException(e, "该旧的所得税表存在 关联，请取消关联后再试");
//        }
//        return new ResponseEntity(HttpStatus.OK);
//    }

    @Log("修改旧的所得税表")
    @ApiOperation("修改旧的所得税表")
    @PutMapping
    @PreAuthorize("@el.check('salatax:edit')")
    public ResponseEntity update(@Validated(SwmSalatax.Update.class) @RequestBody SwmSalatax salatax) {
        swmSalataxService.update(salatax);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

//    @ErrorLog("获取单个旧的所得税表")
//    @ApiOperation("获取单个旧的所得税表")
//    @GetMapping(value = "")
//    @PreAuthorize("@el.check('salatax:list')")
//    public ResponseEntity getSalatax() {
//        return new ResponseEntity<>(swmSalataxService.getByKey(), HttpStatus.OK);
//    }

    @ErrorLog("查询旧的所得税表（分页）")
    @ApiOperation("查询旧的所得税表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('salatax:list')")
    public ResponseEntity getSalataxPage(SwmSalataxQueryCriteria criteria, @PageableDefault(value = 9999, sort = {}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmSalataxService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询旧的所得税表（不分页）")
    @ApiOperation("查询旧的所得税表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('salatax:list')")
    public ResponseEntity getSalataxNoPaging(SwmSalataxQueryCriteria criteria) {
    return new ResponseEntity<>(swmSalataxService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出旧的所得税表数据")
    @ApiOperation("导出旧的所得税表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('salatax:list')")
    public void download(HttpServletResponse response, SwmSalataxQueryCriteria criteria) throws IOException {
        swmSalataxService.download(swmSalataxService.listAll(criteria), response);
    }

    @ErrorLog("返回个人旧的浮动工资")
    @ApiOperation("返回个人旧的浮动工资")
    @GetMapping(value = "/getOldSalatax")
    @PreAuthorize("@el.check('salatax:list')")
    public ResponseEntity getOldSalatax(String year){
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        if (null == user.getEmployee()) {
            throw new InfoCheckWarningMessException("当前用户尚未绑定用户， 请连续管理员进行绑定");
        }
        List<SwmSalataxDTO> swmSalataxs = swmSalataxService.getOldSalatax(user.getEmployee().getWorkCard(), year);
        return new ResponseEntity<>(swmSalataxs, HttpStatus.OK);
    }
}
