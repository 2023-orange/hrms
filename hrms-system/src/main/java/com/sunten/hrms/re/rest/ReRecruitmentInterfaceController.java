package com.sunten.hrms.re.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.re.domain.ReRecruitmentInterface;
import com.sunten.hrms.re.dto.ReRecruitmentInterfaceQueryCriteria;
import com.sunten.hrms.re.service.ReRecruitmentInterfaceService;
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
 * 招骋数据临时表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@RestController
@Api(tags = "招骋数据临时表")
@RequestMapping("/api/re/recruitmentInterface")
public class ReRecruitmentInterfaceController {
    private static final String ENTITY_NAME = "recruitmentInterface";
    private final ReRecruitmentInterfaceService reRecruitmentInterfaceService;

    public ReRecruitmentInterfaceController(ReRecruitmentInterfaceService reRecruitmentInterfaceService) {
        this.reRecruitmentInterfaceService = reRecruitmentInterfaceService;
    }

    @Log("新增招骋数据临时表")
    @ApiOperation("新增招骋数据临时表")
    @PostMapping
    @PreAuthorize("@el.check('recruitment:add')")
    public ResponseEntity create(@Validated @RequestBody ReRecruitmentInterface recruitmentInterface) {
        if (recruitmentInterface.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(reRecruitmentInterfaceService.insert(recruitmentInterface), HttpStatus.CREATED);
    }

    @Log("删除招骋数据临时表")
    @ApiOperation("删除招骋数据临时表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('recruitment:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reRecruitmentInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该招骋数据临时表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改招骋数据临时表")
    @ApiOperation("修改招骋数据临时表")
    @PutMapping
    @PreAuthorize("@el.check('recruitment:edit')")
    public ResponseEntity update(@Validated(ReRecruitmentInterface.Update.class) @RequestBody ReRecruitmentInterface recruitmentInterface) {
        reRecruitmentInterfaceService.update(recruitmentInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个招骋数据临时表")
    @ApiOperation("获取单个招骋数据临时表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('recruitment:list')")
    public ResponseEntity getRecruitmentInterface(@PathVariable Long id) {
        return new ResponseEntity<>(reRecruitmentInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询招骋数据临时表（分页）")
    @ApiOperation("查询招骋数据临时表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('recruitment:list')")
    public ResponseEntity getRecruitmentInterfacePage(ReRecruitmentInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reRecruitmentInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询招骋数据临时表（不分页）")
    @ApiOperation("查询招骋数据临时表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('recruitment:list')")
    public ResponseEntity getRecruitmentInterfaceNoPaging(ReRecruitmentInterfaceQueryCriteria criteria) {
        return new ResponseEntity<>(reRecruitmentInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出招骋数据临时表数据")
    @ApiOperation("导出招骋数据临时表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('recruitment:list')")
    public void download(HttpServletResponse response, ReRecruitmentInterfaceQueryCriteria criteria) throws IOException {
        reRecruitmentInterfaceService.download(reRecruitmentInterfaceService.listAll(criteria), response);
    }
}
