package com.sunten.hrms.pm.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.PmMedicalLineRelevance;
import com.sunten.hrms.pm.dto.PmMedicalLineRelevanceQueryCriteria;
import com.sunten.hrms.pm.service.PmMedicalLineRelevanceService;
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
 *  前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-04-20
 */
@RestController
@Api(tags = "体检申请对应体检项")
@RequestMapping("/api/pm/medicalLineRelevance")
public class PmMedicalLineRelevanceController {
    private static final String ENTITY_NAME = "medicalLineRelevance";
    private final PmMedicalLineRelevanceService pmMedicalLineRelevanceService;

    public PmMedicalLineRelevanceController(PmMedicalLineRelevanceService pmMedicalLineRelevanceService) {
        this.pmMedicalLineRelevanceService = pmMedicalLineRelevanceService;
    }

    @Log("新增")
    @ApiOperation("新增")
    @PostMapping
    @PreAuthorize("@el.check('medicalLineRelevance:add')")
    public ResponseEntity create(@Validated @RequestBody PmMedicalLineRelevance medicalLineRelevance) {
        return new ResponseEntity<>(pmMedicalLineRelevanceService.insert(medicalLineRelevance), HttpStatus.CREATED);
    }

    @Log("删除")
    @ApiOperation("删除")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('medicalLineRelevance:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmMedicalLineRelevanceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改")
    @ApiOperation("修改")
    @PutMapping
    @PreAuthorize("@el.check('medicalLineRelevance:edit')")
    public ResponseEntity update(@Validated(PmMedicalLineRelevance.Update.class) @RequestBody PmMedicalLineRelevance medicalLineRelevance) {
        pmMedicalLineRelevanceService.update(medicalLineRelevance);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个")
    @ApiOperation("获取单个")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('medicalLineRelevance:list')")
    public ResponseEntity getMedicalLineRelevance(@PathVariable Long id) {
        return new ResponseEntity<>(pmMedicalLineRelevanceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping
    @PreAuthorize("@el.check('medicalLineRelevance:list')")
    public ResponseEntity getMedicalLineRelevancePage(PmMedicalLineRelevanceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmMedicalLineRelevanceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询（不分页）")
    @ApiOperation("查询（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('medicalLineRelevance:list')")
    public ResponseEntity getMedicalLineRelevanceNoPaging(PmMedicalLineRelevanceQueryCriteria criteria) {
    return new ResponseEntity<>(pmMedicalLineRelevanceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('medicalLineRelevance:list')")
    public void download(HttpServletResponse response, PmMedicalLineRelevanceQueryCriteria criteria) throws IOException {
        pmMedicalLineRelevanceService.download(pmMedicalLineRelevanceService.listAll(criteria), response);
    }
}
