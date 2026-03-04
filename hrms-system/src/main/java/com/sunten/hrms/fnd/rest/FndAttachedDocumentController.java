package com.sunten.hrms.fnd.rest;

import com.sunten.hrms.annotation.AnonymousAccess;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.domain.FndAttachedDocument;
import com.sunten.hrms.fnd.dto.FndAttachedDocumentDTO;
import com.sunten.hrms.fnd.dto.FndAttachedDocumentQueryCriteria;
import com.sunten.hrms.fnd.service.FndAttachedDocumentService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.ThrowableUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 附件表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-09-25
 */
@RestController
@Api(tags = "附件表")
@RequestMapping("/api/fnd/attachedDocument")
public class FndAttachedDocumentController {
    private static final String ENTITY_NAME = "attachedDocument";
    private final FndAttachedDocumentService fndAttachedDocumentService;

    public FndAttachedDocumentController(FndAttachedDocumentService fndAttachedDocumentService) {
        this.fndAttachedDocumentService = fndAttachedDocumentService;
    }

    @Log("新增附件表")
    @ApiOperation("新增附件表")
    @PostMapping
    @PreAuthorize("@el.check('attachedDocument:add')")
    public ResponseEntity create(@Validated @RequestBody FndAttachedDocument attachedDocument) {
        if (attachedDocument.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(fndAttachedDocumentService.insert(attachedDocument), HttpStatus.CREATED);
    }

    @Log("删除附件表")
    @ApiOperation("删除附件表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('attachedDocument:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            fndAttachedDocumentService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该附件表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改附件表")
    @ApiOperation("修改附件表")
    @PutMapping
    @PreAuthorize("@el.check('attachedDocument:edit')")
    public ResponseEntity update(@Validated(FndAttachedDocument.Update.class) @RequestBody FndAttachedDocument attachedDocument) {
        fndAttachedDocumentService.update(attachedDocument);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个附件表")
    @ApiOperation("获取单个附件表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('attachedDocument:list')")
    public ResponseEntity getAttachedDocument(@PathVariable Long id) {
        return new ResponseEntity<>(fndAttachedDocumentService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询附件表（分页）")
    @ApiOperation("查询附件表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('attachedDocument:list')")
    public ResponseEntity getAttachedDocumentPage(FndAttachedDocumentQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(fndAttachedDocumentService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询附件表（不分页）")
    @ApiOperation("查询附件表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('attachedDocument:list')")
    public ResponseEntity getAttachedDocumentNoPaging(FndAttachedDocumentQueryCriteria criteria) {
        return new ResponseEntity<>(fndAttachedDocumentService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("获取附件")
    @ApiOperation("获取附件")
    @GetMapping(value = "/doc")
    @PreAuthorize("@el.check('attachedDocument:list')")
    public ResponseEntity getAttachedDocument(FndAttachedDocumentQueryCriteria criteria) {
        return new ResponseEntity<>(fndAttachedDocumentService.listAttachedDoc(criteria), HttpStatus.OK);
    }

    //以下这个接口，用于给OA端获取附件的
    @ErrorLog("OA端获取附件")
    @ApiOperation("OA端获取附件")
//    @GetMapping(value = "/oaDoc")
    @PostMapping(value = "/oaDoc")
    public ResponseEntity getAttachedDocumentOa(FndAttachedDocumentQueryCriteria criteria) {
        return new ResponseEntity<>(fndAttachedDocumentService.listAttachedDoc(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出附件表数据")
    @ApiOperation("导出附件表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('attachedDocument:list')")
    public void download(HttpServletResponse response, FndAttachedDocumentQueryCriteria criteria) throws IOException {
        fndAttachedDocumentService.download(fndAttachedDocumentService.listAll(criteria), response);
    }

    @Log("上传附件")
    @ApiOperation("上传附件")
    @PostMapping(value = "/uploadAttachedDoc/{source}/{sourceId}/{type}")
    @PreAuthorize("@el.check('attachedDocument:add')")
    public ResponseEntity updatePhoto(@PathVariable String source, @PathVariable Long sourceId, @PathVariable String type, @RequestParam MultipartFile file) {
        fndAttachedDocumentService.uploadAttachedDoc(file, source, sourceId, type);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("多附件传输")
    @ApiOperation("多附件传输")
    @PostMapping(value = "/uploadmutiAttachedDoc/{source}/{sourceId}/{type}")
    @PreAuthorize("@el.check('attachedDocument:add')")
    public ResponseEntity uploadmutiAttachedDoc(@PathVariable String source, @PathVariable Long sourceId, @PathVariable String type, @RequestParam MultipartFile[] file) {
        System.out.println(Arrays.toString(file));
        if (file.length > 0) {
            for (MultipartFile muti : file
                 ) {
                fndAttachedDocumentService.uploadAttachedDoc(muti, source, sourceId, type);
            }
            return new ResponseEntity(HttpStatus.OK);
        } else {
            throw new InfoCheckWarningMessException("请上传至少一个文件");
        }
    }

    @Log("上传多附件")
    @ApiOperation("上传多附件")
    @PostMapping(value = "/uploadFiles")
    @PreAuthorize("@el.check('attachedDocument:add')")
    public ResponseEntity uploadFiles(@Param("source") String source, @Param("sourceId") Long sourceId, @Param("type") String type, @RequestParam MultipartFile[] files) {
        if (files.length > 0) {
//            FndAttachedDocumentQueryCriteria criteria = new FndAttachedDocumentQueryCriteria();
//            criteria.setSource(source);
//            criteria.setSourceId(sourceId);
//            criteria.setType(type);
//            List<FndAttachedDocumentDTO> oldFiles = fndAttachedDocumentService.listAll(criteria);
//            // 判断是否有旧附件，如果有，先删除
//            if(oldFiles.size() > 0) {
//                for (FndAttachedDocumentDTO oldFile : oldFiles) {
//                    fndAttachedDocumentService.delete(oldFile.getId());
//                }
//            }
            for (MultipartFile file : files) {
                fndAttachedDocumentService.uploadAttachedDoc(file, source, sourceId, type);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
