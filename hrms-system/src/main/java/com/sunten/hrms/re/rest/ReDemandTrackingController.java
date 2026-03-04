package com.sunten.hrms.re.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.re.domain.ReDemandTracking;
import com.sunten.hrms.re.dto.ReDemandTrackingQueryCriteria;
import com.sunten.hrms.re.service.ReDemandTrackingService;
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
 * 用人需求招聘过程记录 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2022-01-18
 */
@RestController
@Api(tags = "用人需求招聘过程记录")
@RequestMapping("/api/re/demandTracking")
public class ReDemandTrackingController {
    private static final String ENTITY_NAME = "demandTracking";
    private final ReDemandTrackingService reDemandTrackingService;

    public ReDemandTrackingController(ReDemandTrackingService reDemandTrackingService) {
        this.reDemandTrackingService = reDemandTrackingService;
    }

    @Log("新增用人需求招聘过程记录")
    @ApiOperation("新增用人需求招聘过程记录")
    @PostMapping
    @PreAuthorize("@el.check('demandTracking:add')")
    public ResponseEntity create(@Validated @RequestBody ReDemandTracking demandTracking) {
        if (demandTracking.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(reDemandTrackingService.insert(demandTracking), HttpStatus.OK);
    }

    @Log("删除用人需求招聘过程记录")
    @ApiOperation("删除用人需求招聘过程记录")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('demandTracking:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reDemandTrackingService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该用人需求招聘过程记录存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改用人需求招聘过程记录")
    @ApiOperation("修改用人需求招聘过程记录")
    @PutMapping
    @PreAuthorize("@el.check('demandTracking:edit')")
    public ResponseEntity update(@Validated(ReDemandTracking.Update.class) @RequestBody ReDemandTracking demandTracking) {
        return new ResponseEntity<>(reDemandTrackingService.update(demandTracking), HttpStatus.OK);
    }

    @ErrorLog("获取单个用人需求招聘过程记录")
    @ApiOperation("获取单个用人需求招聘过程记录")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('demandTracking:list')")
    public ResponseEntity getDemandTracking(@PathVariable Long id) {
        return new ResponseEntity<>(reDemandTrackingService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询用人需求招聘过程记录（分页）")
    @ApiOperation("查询用人需求招聘过程记录（分页）")
    @GetMapping
    @PreAuthorize("@el.check('demandTracking:list')")
    public ResponseEntity getDemandTrackingPage(ReDemandTrackingQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reDemandTrackingService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询用人需求招聘过程记录（不分页）")
    @ApiOperation("查询用人需求招聘过程记录（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('demandTracking:list')")
    public ResponseEntity getDemandTrackingNoPaging(ReDemandTrackingQueryCriteria criteria) {
    return new ResponseEntity<>(reDemandTrackingService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出用人需求招聘过程记录数据")
    @ApiOperation("导出用人需求招聘过程记录数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('demandTracking:list')")
    public void download(HttpServletResponse response, ReDemandTrackingQueryCriteria criteria) throws IOException {
        reDemandTrackingService.download(reDemandTrackingService.listAll(criteria), response);
    }
}
