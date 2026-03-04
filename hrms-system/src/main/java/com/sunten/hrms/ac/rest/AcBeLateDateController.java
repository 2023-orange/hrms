package com.sunten.hrms.ac.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.AcBeLateDate;
import com.sunten.hrms.ac.dto.AcBeLateDateQueryCriteria;
import com.sunten.hrms.ac.service.AcBeLateDateService;
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
 * 厂车迟到时间记录表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-07-08
 */
@RestController
@Api(tags = "厂车迟到时间记录表")
@RequestMapping("/api/ac/beLateDate")
public class AcBeLateDateController {
    private static final String ENTITY_NAME = "beLateDate";
    private final AcBeLateDateService acBeLateDateService;

    public AcBeLateDateController(AcBeLateDateService acBeLateDateService) {
        this.acBeLateDateService = acBeLateDateService;
    }

    @Log("新增厂车迟到时间记录表")
    @ApiOperation("新增厂车迟到时间记录表")
    @PostMapping
    @PreAuthorize("@el.check('beLateDate:add')")
    public ResponseEntity create(@Validated @RequestBody AcBeLateDate beLateDate) {
        if (beLateDate.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(acBeLateDateService.insert(beLateDate), HttpStatus.CREATED);
    }

    @Log("删除厂车迟到时间记录表")
    @ApiOperation("删除厂车迟到时间记录表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('beLateDate:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acBeLateDateService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该厂车迟到时间记录表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改厂车迟到时间记录表")
    @ApiOperation("修改厂车迟到时间记录表")
    @PutMapping
    @PreAuthorize("@el.check('beLateDate:edit')")
    public ResponseEntity update(@Validated(AcBeLateDate.Update.class) @RequestBody AcBeLateDate beLateDate) {
        acBeLateDateService.update(beLateDate);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个厂车迟到时间记录表")
    @ApiOperation("获取单个厂车迟到时间记录表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('beLateDate:list')")
    public ResponseEntity getBeLateDate(@PathVariable Long id) {
        return new ResponseEntity<>(acBeLateDateService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询厂车迟到时间记录表（分页）")
    @ApiOperation("查询厂车迟到时间记录表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('beLateDate:list')")
    public ResponseEntity getBeLateDatePage(AcBeLateDateQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        criteria.setLateFlag(true);
        return new ResponseEntity<>(acBeLateDateService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询厂车迟到时间记录表（不分页）")
    @ApiOperation("查询厂车迟到时间记录表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('beLateDate:list')")
    public ResponseEntity getBeLateDateNoPaging(AcBeLateDateQueryCriteria criteria) {
        criteria.setLateFlag(true);
        return new ResponseEntity<>(acBeLateDateService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出厂车迟到时间记录表数据")
    @ApiOperation("导出厂车迟到时间记录表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('beLateDate:list')")
    public void download(HttpServletResponse response, AcBeLateDateQueryCriteria criteria) throws IOException {
        acBeLateDateService.download(acBeLateDateService.listAll(criteria), response);
    }
}
