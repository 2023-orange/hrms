package com.sunten.hrms.pm.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.pm.domain.PmMedicalTuboo;
import com.sunten.hrms.pm.dto.PmMedicalTubooQueryCriteria;
import com.sunten.hrms.pm.service.PmMedicalTubooService;
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
 *  前端控制器
 * </p>
 *
 * @author zhoujy
 * @since 2022-11-30
 */
@RestController
@Api(tags = "")
@RequestMapping("/api/pm/medicalTuboo")
public class PmMedicalTubooController {
    private static final String ENTITY_NAME = "medicalTuboo";
    private final PmMedicalTubooService pmMedicalTubooService;

    public PmMedicalTubooController(PmMedicalTubooService pmMedicalTubooService) {
        this.pmMedicalTubooService = pmMedicalTubooService;
    }

    @Log("新增职业禁忌人员")
    @ApiOperation("新增职业禁忌人员")
    @PostMapping
    @PreAuthorize("@el.check('medicalTuboo:add')")
    public ResponseEntity create(@Validated @RequestBody PmMedicalTuboo pmMedicalTuboo) {
        if (pmMedicalTuboo.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(pmMedicalTubooService.insert(pmMedicalTuboo), HttpStatus.CREATED);
    }

    @Log("删除职业禁忌人员")
    @ApiOperation("删除职业禁忌人员")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('medicalTuboo:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmMedicalTubooService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改职业禁忌人员")
    @ApiOperation("修改职业禁忌人员")
    @PutMapping
    @PreAuthorize("@el.check('medicalTuboo:edit')")
    public ResponseEntity update(@Validated(PmMedicalTuboo.Update.class) @RequestBody PmMedicalTuboo pmMedicalTuboo) {
        pmMedicalTubooService.update(pmMedicalTuboo);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个职业禁忌人员")
    @ApiOperation("获取单个职业禁忌人员")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('medicalTuboo:list')")
    public ResponseEntity getpmMedicalTuboo(@PathVariable Long id) {
        return new ResponseEntity<>(pmMedicalTubooService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询职业禁忌人员（分页）")
    @ApiOperation("查询职业禁忌人员（分页）")
    @GetMapping
    @PreAuthorize("@el.check('medicalTuboo:list')")
    public ResponseEntity getpmMedicalTubooPage(PmMedicalTubooQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmMedicalTubooService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询职业禁忌人员（不分页）")
    @ApiOperation("查询职业禁忌人员（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('medicalTuboo:list')")
    public ResponseEntity getpmMedicalTubooNoPaging(PmMedicalTubooQueryCriteria criteria) {
    return new ResponseEntity<>(pmMedicalTubooService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("查询职业禁忌人员（个人）")
    @ApiOperation("查询职业禁忌人员（个人）")
    @GetMapping(value = "/getPmMedicalTubooSub")
    public ResponseEntity getPmMedicalTubooSub(String workCard) {
        return new ResponseEntity<>(pmMedicalTubooService.getPmMedicalTubooSub(workCard), HttpStatus.OK);
    }

    @ErrorLog("导出职业禁忌人员数据")
    @ApiOperation("导出职业禁忌人员数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('medicalTuboo:list')")
    public void download(HttpServletResponse response, PmMedicalTubooQueryCriteria criteria) throws IOException {
        pmMedicalTubooService.download(pmMedicalTubooService.listAll(criteria), response);
    }
}
