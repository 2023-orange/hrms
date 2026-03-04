package com.sunten.hrms.re.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.re.domain.ReHobbyInterface;
import com.sunten.hrms.re.dto.ReHobbyInterfaceQueryCriteria;
import com.sunten.hrms.re.service.ReHobbyInterfaceService;
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

/**
 * <p>
 * 技术爱好临时表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@RestController
@Api(tags = "技术爱好临时表")
@RequestMapping("/api/re/hobbyInterface")
public class ReHobbyInterfaceController {
    private static final String ENTITY_NAME = "hobbyInterface";
    private final ReHobbyInterfaceService reHobbyInterfaceService;

    public ReHobbyInterfaceController(ReHobbyInterfaceService reHobbyInterfaceService) {
        this.reHobbyInterfaceService = reHobbyInterfaceService;
    }

    @Log("新增技术爱好临时表")
    @ApiOperation("新增技术爱好临时表")
    @PostMapping
    @PreAuthorize("@el.check('hobbyInterface:add')")
    public ResponseEntity create(@Validated @RequestBody ReHobbyInterface hobbyInterface) {
        if (hobbyInterface.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(reHobbyInterfaceService.insert(hobbyInterface), HttpStatus.CREATED);
    }

    @Log("删除技术爱好临时表")
    @ApiOperation("删除技术爱好临时表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('hobbyInterface:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reHobbyInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该技术爱好临时表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改技术爱好临时表")
    @ApiOperation("修改技术爱好临时表")
    @PutMapping
    @PreAuthorize("@el.check('hobbyInterface:edit')")
    public ResponseEntity update(@Validated(ReHobbyInterface.Update.class) @RequestBody ReHobbyInterface hobbyInterface) {
        reHobbyInterfaceService.update(hobbyInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个技术爱好临时表")
    @ApiOperation("获取单个技术爱好临时表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('hobbyInterface:list')")
    public ResponseEntity getHobbyInterface(@PathVariable Long id) {
        return new ResponseEntity<>(reHobbyInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询技术爱好临时表（分页）")
    @ApiOperation("查询技术爱好临时表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('hobbyInterface:list')")
    public ResponseEntity getHobbyInterfacePage(ReHobbyInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reHobbyInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询技术爱好临时表（不分页）")
    @ApiOperation("查询技术爱好临时表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('hobbyInterface:list')")
    public ResponseEntity getHobbyInterfaceNoPaging(ReHobbyInterfaceQueryCriteria criteria) {
        return new ResponseEntity<>(reHobbyInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出技术爱好临时表数据")
    @ApiOperation("导出技术爱好临时表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('hobbyInterface:list')")
    public void download(HttpServletResponse response, ReHobbyInterfaceQueryCriteria criteria) throws IOException {
        reHobbyInterfaceService.download(reHobbyInterfaceService.listAll(criteria), response);
    }
}
