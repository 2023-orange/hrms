package com.sunten.hrms.fnd.rest;

import com.sunten.hrms.fnd.aop.ErrorLog;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.domain.FndDict;
import com.sunten.hrms.fnd.dto.FndDictQueryCriteria;
import com.sunten.hrms.fnd.service.FndDictService;
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

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@RestController
@Api(tags = "系统：字典管理")
@RequestMapping("/api/fnd/dict")
public class FndDictController {
    private static final String ENTITY_NAME = "dict";
    private final FndDictService fndDictService;

    public FndDictController(FndDictService fndDictService) {
        this.fndDictService = fndDictService;
    }

    @Log("新增字典")
    @ApiOperation("新增字典")
    @PostMapping
    @PreAuthorize("@el.check('dict:add')")
    public ResponseEntity create(@Validated @RequestBody FndDict dict) {
        if (dict.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(fndDictService.insert(dict), HttpStatus.CREATED);
    }

    @Log("删除字典")
    @ApiOperation("删除字典")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('dict:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        fndDictService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改字典")
    @ApiOperation("修改字典")
    @PutMapping
    @PreAuthorize("@el.check('dict:edit')")
    public ResponseEntity update(@Validated(FndDict.Update.class) @RequestBody FndDict dict) {
        fndDictService.update(dict);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个字典")
    @ApiOperation("获取单个字典")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity getDict(@PathVariable Long id){
        return new ResponseEntity<>(fndDictService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询字典（分页）")
    @ApiOperation("查询字典（分页）")
    @GetMapping
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity getDictPage(FndDictQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(fndDictService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("导出字典数据")
    @ApiOperation("导出字典数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dict:list')")
    public void download(HttpServletResponse response, FndDictQueryCriteria criteria) throws IOException {
        fndDictService.download(fndDictService.listAll(criteria), response);
    }
}
