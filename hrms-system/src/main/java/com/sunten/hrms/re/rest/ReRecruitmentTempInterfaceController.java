package com.sunten.hrms.re.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.re.domain.ReRecruitmentTempInterface;
import com.sunten.hrms.re.dto.ReRecruitmentTempInterfaceQueryCriteria;
import com.sunten.hrms.re.service.ReRecruitmentTempInterfaceService;
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
 * 应聘全部数据导入的临时表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-09-08
 */
@RestController
@Api(tags = "应聘全部数据导入的临时表")
@RequestMapping("/api/re/recruitmentTempInterface")
public class ReRecruitmentTempInterfaceController {
    private static final String ENTITY_NAME = "recruitmentTempInterface";
    private final ReRecruitmentTempInterfaceService reRecruitmentTempInterfaceService;

    public ReRecruitmentTempInterfaceController(ReRecruitmentTempInterfaceService reRecruitmentTempInterfaceService) {
        this.reRecruitmentTempInterfaceService = reRecruitmentTempInterfaceService;
    }

    @Log("新增应聘全部数据导入的临时表")
    @ApiOperation("新增应聘全部数据导入的临时表")
    @PostMapping
    @PreAuthorize("@el.check('recruitment:add')")
    public ResponseEntity create(@Validated @RequestBody ReRecruitmentTempInterface recruitmentTempInterface) {
        if (recruitmentTempInterface.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(reRecruitmentTempInterfaceService.insert(recruitmentTempInterface), HttpStatus.CREATED);
    }

    @Log("删除应聘全部数据导入的临时表")
    @ApiOperation("删除应聘全部数据导入的临时表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('recruitment:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reRecruitmentTempInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该应聘全部数据导入的临时表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改应聘全部数据导入的临时表")
    @ApiOperation("修改应聘全部数据导入的临时表")
    @PutMapping
    @PreAuthorize("@el.check('recruitment:edit')")
    public ResponseEntity update(@Validated(ReRecruitmentTempInterface.Update.class) @RequestBody ReRecruitmentTempInterface recruitmentTempInterface) {
        reRecruitmentTempInterfaceService.update(recruitmentTempInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个应聘全部数据导入的临时表")
    @ApiOperation("获取单个应聘全部数据导入的临时表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('recruitment:list')")
    public ResponseEntity getRecruitmentTempInterface(@PathVariable Long id) {
        return new ResponseEntity<>(reRecruitmentTempInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询应聘全部数据导入的临时表（分页）")
    @ApiOperation("查询应聘全部数据导入的临时表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('recruitment:list')")
    public ResponseEntity getRecruitmentTempInterfacePage(ReRecruitmentTempInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reRecruitmentTempInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询应聘全部数据导入的临时表（不分页）")
    @ApiOperation("查询应聘全部数据导入的临时表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('recruitment:list')")
    public ResponseEntity getRecruitmentTempInterfaceNoPaging(ReRecruitmentTempInterfaceQueryCriteria criteria) {
    return new ResponseEntity<>(reRecruitmentTempInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出应聘全部数据导入的临时表数据")
    @ApiOperation("导出应聘全部数据导入的临时表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('recruitment:list')")
    public void download(HttpServletResponse response, ReRecruitmentTempInterfaceQueryCriteria criteria) throws IOException {
        reRecruitmentTempInterfaceService.download(reRecruitmentTempInterfaceService.listAll(criteria), response);
    }

    @ErrorLog("导入应聘数据")
    @ApiOperation("导入应聘数据")
    @PutMapping(value = "/importExcelData")
    @PreAuthorize("@el.check('recruitment:add')")
    public ResponseEntity importExcelData(@RequestBody List<ReRecruitmentTempInterface> reRecruitmentTempInterfaces) {
        return new ResponseEntity<>(reRecruitmentTempInterfaceService.importExcelData(reRecruitmentTempInterfaces), HttpStatus.OK);
    }
}
