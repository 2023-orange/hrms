package com.sunten.hrms.pm.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.PmTransferComment;
import com.sunten.hrms.pm.dto.PmTransferCommentQueryCriteria;
import com.sunten.hrms.pm.service.PmTransferCommentService;
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
 * 调动人员流转记录表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-05-24
 */
@RestController
@Api(tags = "调动人员流转记录表")
@RequestMapping("/api/pm/transferComment")
public class PmTransferCommentController {
    private static final String ENTITY_NAME = "transferComment";
    private final PmTransferCommentService pmTransferCommentService;

    public PmTransferCommentController(PmTransferCommentService pmTransferCommentService) {
        this.pmTransferCommentService = pmTransferCommentService;
    }

    @Log("新增调动人员流转记录表")
    @ApiOperation("新增调动人员流转记录表")
    @PostMapping
    @PreAuthorize("@el.check('transferComment:add','transferRequest:admin')")
    public ResponseEntity create(@Validated @RequestBody PmTransferComment transferComment) {
        if (transferComment.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(pmTransferCommentService.insert(transferComment), HttpStatus.CREATED);
    }

    @Log("删除调动人员流转记录表")
    @ApiOperation("删除调动人员流转记录表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('transferComment:del','transferRequest:admin')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmTransferCommentService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该调动人员流转记录表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改调动人员流转记录表")
    @ApiOperation("修改调动人员流转记录表")
    @PutMapping
    @PreAuthorize("@el.check('transferComment:edit','transferRequest:admin')")
    public ResponseEntity update(@Validated(PmTransferComment.Update.class) @RequestBody PmTransferComment transferComment) {
        pmTransferCommentService.update(transferComment);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个调动人员流转记录表")
    @ApiOperation("获取单个调动人员流转记录表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('transferComment:list','transferRequest:admin')")
    public ResponseEntity getTransferComment(@PathVariable Long id) {
        return new ResponseEntity<>(pmTransferCommentService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询调动人员流转记录表（分页）")
    @ApiOperation("查询调动人员流转记录表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('transferComment:list','transferRequest:admin')")
    public ResponseEntity getTransferCommentPage(PmTransferCommentQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmTransferCommentService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询调动人员流转记录表（不分页）")
    @ApiOperation("查询调动人员流转记录表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('transferComment:list','transferRequest:admin')")
    public ResponseEntity getTransferCommentNoPaging(PmTransferCommentQueryCriteria criteria) {
    return new ResponseEntity<>(pmTransferCommentService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出调动人员流转记录表数据")
    @ApiOperation("导出调动人员流转记录表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('transferComment:list','transferRequest:admin')")
    public void download(HttpServletResponse response, PmTransferCommentQueryCriteria criteria) throws IOException {
        pmTransferCommentService.download(pmTransferCommentService.listAll(criteria), response);
    }
}
