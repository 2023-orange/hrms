package com.sunten.hrms.re.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.re.domain.ReRecruitment;
import com.sunten.hrms.re.dto.ReRecruitmentQueryCriteria;
import com.sunten.hrms.re.service.ReRecruitmentService;
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
 * 招骋数据表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@RestController
@Api(tags = "招骋数据表")
@RequestMapping("/api/re/recruitment")
public class ReRecruitmentController {
    private static final String ENTITY_NAME = "recruitment";
    private final ReRecruitmentService reRecruitmentService;

    public ReRecruitmentController(ReRecruitmentService reRecruitmentService) {
        this.reRecruitmentService = reRecruitmentService;
    }

    @Log("编辑招骋数据表")
    @ApiOperation("编辑招骋数据表")
    @PostMapping
    @PreAuthorize("@el.check('recruitment:add')")
    public ResponseEntity create(@Validated @RequestBody ReRecruitment recruitment) {
        if (recruitment.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(reRecruitmentService.insert(recruitment), HttpStatus.CREATED);
    }

    @Log("删除招骋数据表")
    @ApiOperation("删除招骋数据表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('recruitment:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reRecruitmentService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该招骋数据表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改招骋数据表")
    @ApiOperation("修改招骋数据表")
    @PutMapping
    @PreAuthorize("@el.check('recruitment:edit')")
    public ResponseEntity update(@Validated(ReRecruitment.Update.class) @RequestBody ReRecruitment recruitment) {
        reRecruitmentService.update(recruitment);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个招骋数据表")
    @ApiOperation("获取单个招骋数据表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('recruitment:list','trialApproval:list')")
    public ResponseEntity getRecruitment(@PathVariable Long id) {
        return new ResponseEntity<>(reRecruitmentService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询招骋数据表（分页）")
    @ApiOperation("查询招骋数据表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('recruitment:list','trialApproval:list')")
    public ResponseEntity getRecruitmentPage(ReRecruitmentQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reRecruitmentService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询招骋数据表（不分页）")
    @ApiOperation("查询招骋数据表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('recruitment:list','trialApproval:list')")
    public ResponseEntity getRecruitmentNoPaging(ReRecruitmentQueryCriteria criteria) {
        return new ResponseEntity<>(reRecruitmentService.listAll(criteria), HttpStatus.OK);
    }


    @ErrorLog("导出招骋数据表数据")
    @ApiOperation("导出招骋数据表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('recruitment:list')")
    public void download(HttpServletResponse response, ReRecruitmentQueryCriteria criteria) throws IOException {
        reRecruitmentService.download(reRecruitmentService.listAll(criteria), response);
    }
}
