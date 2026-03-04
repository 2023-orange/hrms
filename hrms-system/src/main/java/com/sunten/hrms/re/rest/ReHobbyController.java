package com.sunten.hrms.re.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.re.domain.ReHobby;
import com.sunten.hrms.re.dto.ReHobbyQueryCriteria;
import com.sunten.hrms.re.service.ReHobbyService;
import com.sunten.hrms.utils.ThrowableUtil;
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
 * 技术爱好表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@RestController
@Api(tags = "技术爱好表")
@RequestMapping("/api/re/hobby")
public class ReHobbyController {
    private static final String ENTITY_NAME = "hobby";
    private final ReHobbyService reHobbyService;

    public ReHobbyController(ReHobbyService reHobbyService) {
        this.reHobbyService = reHobbyService;
    }

    @Log("新增技术爱好表")
    @ApiOperation("新增技术爱好表")
    @PostMapping
    @PreAuthorize("@el.check('hobby:add')")
    public ResponseEntity create(@Validated @RequestBody ReHobby hobby) {
        if (hobby.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(reHobbyService.insert(hobby), HttpStatus.CREATED);
    }

    @Log("删除技术爱好表")
    @ApiOperation("删除技术爱好表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('hobby:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reHobbyService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该技术爱好表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改技术爱好表")
    @ApiOperation("修改技术爱好表")
    @PutMapping
    @PreAuthorize("@el.check('hobby:edit')")
    public ResponseEntity update(@Validated(ReHobby.Update.class) @RequestBody ReHobby hobby) {
        reHobbyService.update(hobby);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个技术爱好表")
    @ApiOperation("获取单个技术爱好表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('hobby:list')")
    public ResponseEntity getHobby(@PathVariable Long id) {
        return new ResponseEntity<>(reHobbyService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询技术爱好表（分页）")
    @ApiOperation("查询技术爱好表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('hobby:list','recruitment:list')")
    public ResponseEntity getHobbyPage(ReHobbyQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reHobbyService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询技术爱好表（不分页）")
    @ApiOperation("查询技术爱好表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('hobby:list','recruitment:list')")
    public ResponseEntity getHobbyNoPaging(ReHobbyQueryCriteria criteria) {
        return new ResponseEntity<>(reHobbyService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出技术爱好表数据")
    @ApiOperation("导出技术爱好表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('hobby:list')")
    public void download(HttpServletResponse response, ReHobbyQueryCriteria criteria) throws IOException {
        reHobbyService.download(reHobbyService.listAll(criteria), response);
    }

    @Log("批量编辑技术爱好表")
    @ApiOperation("批量编辑技术爱好表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('hobby:add','hobby:edit','recruitment:edit','recruitment:add')")
    public ResponseEntity batchSave(@Validated @RequestBody List<ReHobby> hobbys) {
        if (hobbys == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(reHobbyService.batchInsert(hobbys, null), HttpStatus.CREATED);
    }

//    @ErrorLog("查询拼接好的技术爱好")
//    @ApiOperation("查询拼接好的技术爱好")
//    @GetMapping(value = "/getReadyHobby")
//    @PreAuthorize("@el.check('hobby:list','recruitment:list')")
//    public ResponseEntity getReadyHobby(ReHobbyQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
//        return new ResponseEntity<>(reHobbyService.listAll(criteria, pageable), HttpStatus.OK);
//    }

}
