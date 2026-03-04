package com.sunten.hrms.swm.rest;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.dto.SwmSalatax2006DTO;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.swm.domain.SwmSalatax2006;
import com.sunten.hrms.swm.dto.SwmSalatax2006QueryCriteria;
import com.sunten.hrms.swm.service.SwmSalatax2006Service;
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
 * 年底奖金所得税 前端控制器
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-11
 */
@RestController
@Api(tags = "年底奖金所得税")
@RequestMapping("/api/swm/salatax2006")
public class SwmSalatax2006Controller {
    private static final String ENTITY_NAME = "salatax2006";
    private final SwmSalatax2006Service swmSalatax2006Service;
    private final FndUserService fndUserService;

    public SwmSalatax2006Controller(SwmSalatax2006Service swmSalatax2006Service, FndUserService fndUserService) {
        this.swmSalatax2006Service = swmSalatax2006Service;
        this.fndUserService = fndUserService;
    }

    @Log("新增年底奖金所得税")
    @ApiOperation("新增年底奖金所得税")
    @PostMapping
    @PreAuthorize("@el.check('salatax2006:add')")
    public ResponseEntity create(@Validated @RequestBody SwmSalatax2006 salatax2006) {
        return new ResponseEntity<>(swmSalatax2006Service.insert(salatax2006), HttpStatus.CREATED);
    }

//    @Log("删除年底奖金所得税")
//    @ApiOperation("删除年底奖金所得税")
//    @DeleteMapping(value = "")
//    @PreAuthorize("@el.check('salatax2006:del')")
//    public ResponseEntity delete() {
//        try {
//            swmSalatax2006Service.delete();
//        } catch (Throwable e) {
//            ThrowableUtil.throwForeignKeyException(e, "该年底奖金所得税存在 关联，请取消关联后再试");
//        }
//        return new ResponseEntity(HttpStatus.OK);
//    }

    @Log("修改年底奖金所得税")
    @ApiOperation("修改年底奖金所得税")
    @PutMapping
    @PreAuthorize("@el.check('salatax2006:edit')")
    public ResponseEntity update(@Validated(SwmSalatax2006.Update.class) @RequestBody SwmSalatax2006 salatax2006) {
        swmSalatax2006Service.update(salatax2006);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

//    @ErrorLog("获取单个年底奖金所得税")
//    @ApiOperation("获取单个年底奖金所得税")
//    @GetMapping(value = "")
//    @PreAuthorize("@el.check('salatax2006:list')")
//    public ResponseEntity getSalatax2006() {
//        return new ResponseEntity<>(swmSalatax2006Service.getByKey(), HttpStatus.OK);
//    }

    @ErrorLog("查询年底奖金所得税（分页）")
    @ApiOperation("查询年底奖金所得税（分页）")
    @GetMapping
    @PreAuthorize("@el.check('salatax2006:list')")
    public ResponseEntity getSalatax2006Page(SwmSalatax2006QueryCriteria criteria, @PageableDefault(value = 9999, sort = {}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmSalatax2006Service.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询年底奖金所得税（不分页）")
    @ApiOperation("查询年底奖金所得税（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('salatax2006:list')")
    public ResponseEntity getSalatax2006NoPaging(SwmSalatax2006QueryCriteria criteria) {
    return new ResponseEntity<>(swmSalatax2006Service.listAll(criteria), HttpStatus.OK);
    }



    @ErrorLog("返回个人2006奖金个人所得税明细")
    @ApiOperation("返回个人2006奖金个人所得税明细")
    @GetMapping(value = "/getOldSalatax2006")
    @PreAuthorize("@el.check('salatax2006:list')")
    public ResponseEntity getOldSalatax2006(){
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        if (null == user.getEmployee()) {
            throw new InfoCheckWarningMessException("当前用户尚未绑定用户， 请连续管理员进行绑定");
        }
        List<SwmSalatax2006DTO> swmSalataxs2006 = swmSalatax2006Service.getOldSalatax2006(user.getEmployee().getWorkCard());
        return new ResponseEntity<>(swmSalataxs2006, HttpStatus.OK);
    }
}
