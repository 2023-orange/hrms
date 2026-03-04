package com.sunten.hrms.tool.rest;

import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.tool.domain.ToolLocalStorage;
import com.sunten.hrms.tool.dto.ToolLocalStorageQueryCriteria;
import com.sunten.hrms.tool.service.ToolLocalStorageService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author batan
 * @since 2019-12-25
 */
@RestController
@Api(tags = "工具：本地存储管理")
@RequestMapping("/api/tool/localStorage")
public class ToolLocalStorageController {
    private static final String ENTITY_NAME = "localStorage";
    private final ToolLocalStorageService toolLocalStorageService;

    public ToolLocalStorageController(ToolLocalStorageService toolLocalStorageService) {
        this.toolLocalStorageService = toolLocalStorageService;
    }

    @ApiOperation("查询文件")
    @GetMapping
    @PreAuthorize("@el.check('storage:list')")
    public ResponseEntity getLocalStorages(ToolLocalStorageQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"tls.id"}, direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(toolLocalStorageService.listAll(criteria,pageable),HttpStatus.OK);
    }

    @ErrorLog("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('storage:list')")
    public void download(HttpServletResponse response, ToolLocalStorageQueryCriteria criteria) throws IOException {
        toolLocalStorageService.download(toolLocalStorageService.listAll(criteria), response);
    }

    @ApiOperation("上传文件")
    @PostMapping
    @PreAuthorize("@el.check('storage:add')")
    public ResponseEntity create(@RequestParam String name, @RequestParam("file") MultipartFile file){
        return new ResponseEntity<>(toolLocalStorageService.insert(name, file),HttpStatus.CREATED);
    }

    @ApiOperation("修改文件")
    @PutMapping
    @PreAuthorize("@el.check('storage:edit')")
    public ResponseEntity update(@Validated @RequestBody ToolLocalStorage resources){
        toolLocalStorageService.updateName(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除文件")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('storage:del')")
    public ResponseEntity delete(@PathVariable Long id){
        toolLocalStorageService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("多选删除")
    @DeleteMapping
    @ApiOperation("多选删除")
    public ResponseEntity deleteAll(@RequestBody Long[] ids) {
        toolLocalStorageService.deleteAll(ids);
        return new ResponseEntity(HttpStatus.OK);
    }
}
