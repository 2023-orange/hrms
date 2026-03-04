package com.sunten.hrms.re.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.re.domain.ReFamilyInterface;
import com.sunten.hrms.re.dto.ReFamilyInterfaceQueryCriteria;
import com.sunten.hrms.re.service.ReFamilyInterfaceService;
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
 * 家庭情况临时表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@RestController
@Api(tags = "家庭情况临时表")
@RequestMapping("/api/re/familyInterface")
public class ReFamilyInterfaceController {
    private static final String ENTITY_NAME = "familyInterface";
    private final ReFamilyInterfaceService reFamilyInterfaceService;

    public ReFamilyInterfaceController(ReFamilyInterfaceService reFamilyInterfaceService) {
        this.reFamilyInterfaceService = reFamilyInterfaceService;
    }

    @Log("新增家庭情况临时表")
    @ApiOperation("新增家庭情况临时表")
    @PostMapping
    @PreAuthorize("@el.check('familyInterface:add')")
    public ResponseEntity create(@Validated @RequestBody ReFamilyInterface familyInterface) {
        if (familyInterface.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(reFamilyInterfaceService.insert(familyInterface), HttpStatus.CREATED);
    }

    @Log("删除家庭情况临时表")
    @ApiOperation("删除家庭情况临时表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('familyInterface:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reFamilyInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该家庭情况临时表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改家庭情况临时表")
    @ApiOperation("修改家庭情况临时表")
    @PutMapping
    @PreAuthorize("@el.check('familyInterface:edit')")
    public ResponseEntity update(@Validated(ReFamilyInterface.Update.class) @RequestBody ReFamilyInterface familyInterface) {
        reFamilyInterfaceService.update(familyInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个家庭情况临时表")
    @ApiOperation("获取单个家庭情况临时表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('familyInterface:list')")
    public ResponseEntity getFamilyInterface(@PathVariable Long id) {
        return new ResponseEntity<>(reFamilyInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询家庭情况临时表（分页）")
    @ApiOperation("查询家庭情况临时表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('familyInterface:list')")
    public ResponseEntity getFamilyInterfacePage(ReFamilyInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reFamilyInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询家庭情况临时表（不分页）")
    @ApiOperation("查询家庭情况临时表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('familyInterface:list')")
    public ResponseEntity getFamilyInterfaceNoPaging(ReFamilyInterfaceQueryCriteria criteria) {
        return new ResponseEntity<>(reFamilyInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出家庭情况临时表数据")
    @ApiOperation("导出家庭情况临时表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('familyInterface:list')")
    public void download(HttpServletResponse response, ReFamilyInterfaceQueryCriteria criteria) throws IOException {
        reFamilyInterfaceService.download(reFamilyInterfaceService.listAll(criteria), response);
    }
}
