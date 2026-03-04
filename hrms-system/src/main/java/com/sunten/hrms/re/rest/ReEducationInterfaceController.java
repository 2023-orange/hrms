package com.sunten.hrms.re.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.re.domain.ReEducationInterface;
import com.sunten.hrms.re.dto.ReEducationInterfaceQueryCriteria;
import com.sunten.hrms.re.service.ReEducationInterfaceService;
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
 * 受教育经历临时表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@RestController
@Api(tags = "受教育经历临时表")
@RequestMapping("/api/re/educationInterface")
public class ReEducationInterfaceController {
    private static final String ENTITY_NAME = "educationInterface";
    private final ReEducationInterfaceService reEducationInterfaceService;

    public ReEducationInterfaceController(ReEducationInterfaceService reEducationInterfaceService) {
        this.reEducationInterfaceService = reEducationInterfaceService;
    }

    @Log("新增受教育经历临时表")
    @ApiOperation("新增受教育经历临时表")
    @PostMapping
    @PreAuthorize("@el.check('educationInterface:add')")
    public ResponseEntity create(@Validated @RequestBody ReEducationInterface educationInterface) {
        if (educationInterface.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(reEducationInterfaceService.insert(educationInterface), HttpStatus.CREATED);
    }

    @Log("删除受教育经历临时表")
    @ApiOperation("删除受教育经历临时表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('educationInterface:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reEducationInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该受教育经历临时表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改受教育经历临时表")
    @ApiOperation("修改受教育经历临时表")
    @PutMapping
    @PreAuthorize("@el.check('educationInterface:edit')")
    public ResponseEntity update(@Validated(ReEducationInterface.Update.class) @RequestBody ReEducationInterface educationInterface) {
        reEducationInterfaceService.update(educationInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个受教育经历临时表")
    @ApiOperation("获取单个受教育经历临时表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('educationInterface:list')")
    public ResponseEntity getEducationInterface(@PathVariable Long id) {
        return new ResponseEntity<>(reEducationInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询受教育经历临时表（分页）")
    @ApiOperation("查询受教育经历临时表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('educationInterface:list')")
    public ResponseEntity getEducationInterfacePage(ReEducationInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reEducationInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询受教育经历临时表（不分页）")
    @ApiOperation("查询受教育经历临时表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('educationInterface:list')")
    public ResponseEntity getEducationInterfaceNoPaging(ReEducationInterfaceQueryCriteria criteria) {
        return new ResponseEntity<>(reEducationInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出受教育经历临时表数据")
    @ApiOperation("导出受教育经历临时表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('educationInterface:list')")
    public void download(HttpServletResponse response, ReEducationInterfaceQueryCriteria criteria) throws IOException {
        reEducationInterfaceService.download(reEducationInterfaceService.listAll(criteria), response);
    }
}
