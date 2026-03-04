package com.sunten.hrms.re.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.re.domain.ReDemandJobDescribes;
import com.sunten.hrms.re.dto.ReDemandJobDescribesQueryCriteria;
import com.sunten.hrms.re.service.ReDemandJobDescribesService;
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
 * 岗位说明书表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-04-23
 */
@RestController
@Api(tags = "岗位说明书表")
@RequestMapping("/api/re/demandJobDescribes")
public class ReDemandJobDescribesController {
    private static final String ENTITY_NAME = "demandJobDescribes";
    private final ReDemandJobDescribesService reDemandJobDescribesService;

    public ReDemandJobDescribesController(ReDemandJobDescribesService reDemandJobDescribesService) {
        this.reDemandJobDescribesService = reDemandJobDescribesService;
    }

    @Log("新增岗位说明书表")
    @ApiOperation("新增岗位说明书表")
    @PostMapping
    @PreAuthorize("@el.check('demand:add')")
    public ResponseEntity create(@Validated @RequestBody ReDemandJobDescribes demandJobDescribes) {
        if (demandJobDescribes.getId()  != -1) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(reDemandJobDescribesService.insert(demandJobDescribes), HttpStatus.CREATED);
    }

    @Log("删除岗位说明书表")
    @ApiOperation("删除岗位说明书表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('demand:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reDemandJobDescribesService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该岗位说明书表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改岗位说明书表")
    @ApiOperation("修改岗位说明书表")
    @PutMapping
    @PreAuthorize("@el.check('demand:edit')")
    public ResponseEntity update(@Validated(ReDemandJobDescribes.Update.class) @RequestBody ReDemandJobDescribes demandJobDescribes) {
        reDemandJobDescribesService.update(demandJobDescribes);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个岗位说明书表")
    @ApiOperation("获取单个岗位说明书表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('demand:list')")
    public ResponseEntity getDemandJobDescribes(@PathVariable Long id) {
        return new ResponseEntity<>(reDemandJobDescribesService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询岗位说明书表（分页）")
    @ApiOperation("查询岗位说明书表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('demand:list')")
    public ResponseEntity getDemandJobDescribesPage(ReDemandJobDescribesQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reDemandJobDescribesService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询岗位说明书表（不分页）")
    @ApiOperation("查询岗位说明书表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('demand:list')")
    public ResponseEntity getDemandJobDescribesNoPaging(ReDemandJobDescribesQueryCriteria criteria) {
    return new ResponseEntity<>(reDemandJobDescribesService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出岗位说明书表数据")
    @ApiOperation("导出岗位说明书表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('demand:list')")
    public void download(HttpServletResponse response, ReDemandJobDescribesQueryCriteria criteria) throws IOException {
        reDemandJobDescribesService.download(reDemandJobDescribesService.listAll(criteria), response);
    }

    @Log("根据列名进行更新")
    @ApiOperation("根据列名进行更新")
    @PutMapping(value = "/updateColumnByValue")
    public ResponseEntity updateColumnByValue(@RequestBody ReDemandJobDescribes reDemandJobDescribes) {
        if (null == reDemandJobDescribes.getId()) {
            throw new BadRequestException("该岗位需求说明没有ID，无效的请求");
        } else {
            reDemandJobDescribesService.updateColumnByValue(reDemandJobDescribes);
            return new ResponseEntity<>("200", HttpStatus.OK);
        }
    }
}
