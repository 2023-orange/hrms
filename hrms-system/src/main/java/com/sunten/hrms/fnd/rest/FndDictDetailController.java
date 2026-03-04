package com.sunten.hrms.fnd.rest;

import com.sunten.hrms.fnd.aop.ErrorLog;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.domain.FndDictDetail;
import com.sunten.hrms.fnd.dto.FndDictDetailQueryCriteria;
import com.sunten.hrms.fnd.service.FndDictDetailService;
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
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@RestController
@Api(tags = "系统：字典详情管理")
@RequestMapping("/api/fnd/dictDetail")
public class FndDictDetailController {
    private static final String ENTITY_NAME = "dictDetail";
    private final FndDictDetailService fndDictDetailService;

    public FndDictDetailController(FndDictDetailService fndDictDetailService) {
        this.fndDictDetailService = fndDictDetailService;
    }

    @Log("新增字典详情")
    @ApiOperation("新增字典详情")
    @PostMapping
    @PreAuthorize("@el.check('dict:add')")
    public ResponseEntity create(@Validated @RequestBody FndDictDetail dictDetail) {
        if (dictDetail.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(fndDictDetailService.insert(dictDetail), HttpStatus.CREATED);
    }

    @Log("删除字典详情")
    @ApiOperation("删除字典详情")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('dict:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        fndDictDetailService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改字典详情")
    @ApiOperation("修改字典详情")
    @PutMapping
    @PreAuthorize("@el.check('dict:edit')")
    public ResponseEntity update(@Validated(FndDictDetail.Update.class) @RequestBody FndDictDetail dictDetail) {
        fndDictDetailService.update(dictDetail);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("获取单个字典详情")
    @ApiOperation("获取单个字典详情")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity getDictDetail(@PathVariable Long id){
        return new ResponseEntity<>(fndDictDetailService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询字典详情（分页）")
    @ApiOperation("查询字典详情（分页）")
    @GetMapping
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity getDictDetailPage(FndDictDetailQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"sort"}, direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(fndDictDetailService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询字典详情（不分页）")
    @ApiOperation("查询字典详情（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity getDictDetailPage(FndDictDetailQueryCriteria criteria){
        return new ResponseEntity<>(fndDictDetailService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("查询多个字典详情（分页）")
    @ApiOperation("查询多个字典详情（分页）")
    @GetMapping(value = "/map")
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity getDictDetailPageMaps(FndDictDetailQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"sort"}, direction = Sort.Direction.ASC) Pageable pageable){
        String[] names = criteria.getDictName().split(",");
        Map<String, Object> map = new HashMap<>(names.length);
        for (String name : names) {
            criteria.setDictName(name);
            map.put(name, fndDictDetailService.listAll(criteria, pageable).get("content"));
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @ErrorLog("导出字典详情数据")
    @ApiOperation("导出字典详情数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dict:list')")
    public void download(HttpServletResponse response, FndDictDetailQueryCriteria criteria) throws IOException {
        fndDictDetailService.download(fndDictDetailService.listAll(criteria), response);
    }
}
