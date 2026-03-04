package com.sunten.hrms.pm.rest;

import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.PmMedicalLine;
import com.sunten.hrms.pm.dto.PmMedicalLineQueryCriteria;
import com.sunten.hrms.pm.service.PmMedicalLineService;
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
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 体检申请子表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-04-07
 */
@RestController
@Api(tags = "体检申请子表")
@RequestMapping("/api/pm/medicalLine")
public class PmMedicalLineController {
    private static final String ENTITY_NAME = "medicalLine";
    private final PmMedicalLineService pmMedicalLineService;
    private final FndUserService fndUserService;
    private final FndDataScope fndDataScope;
    private final JwtPermissionService jwtPermissionService;

    public PmMedicalLineController(PmMedicalLineService pmMedicalLineService, FndUserService fndUserService, FndDataScope fndDataScope, JwtPermissionService jwtPermissionService) {
        this.pmMedicalLineService = pmMedicalLineService;
        this.fndUserService = fndUserService;
        this.fndDataScope = fndDataScope;
        this.jwtPermissionService = jwtPermissionService;
    }

    @Log("新增体检申请子表")
    @ApiOperation("新增体检申请子表")
    @PostMapping
    @PreAuthorize("@el.check('medicalLine:add','medical:add')")
    public ResponseEntity create(@Validated @RequestBody PmMedicalLine medicalLine) {
        if (medicalLine.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(pmMedicalLineService.insert(medicalLine), HttpStatus.CREATED);
    }

    @Log("删除体检申请子表")
    @ApiOperation("删除体检申请子表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('medicalLine:del','medical:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmMedicalLineService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该体检申请子表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改体检申请子表")
    @ApiOperation("修改体检申请子表")
    @PutMapping
    @PreAuthorize("@el.check('medicalLine:edit','medical:edit')")
    public ResponseEntity update(@Validated(PmMedicalLine.Update.class) @RequestBody PmMedicalLine medicalLine) {
        pmMedicalLineService.update(medicalLine);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("OA审批时修改体检申请子表")
    @ApiOperation("OA审批时修改体检申请子表")
    @PostMapping(value = "/updateFromOa")
    public ResponseEntity updateFromOa(@RequestBody PmMedicalLine medicalLine) {
        pmMedicalLineService.updateFromOa(medicalLine);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个体检申请子表")
    @ApiOperation("获取单个体检申请子表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('medicalLine:list','medical:list')")
    public ResponseEntity getMedicalLine(@PathVariable Long id) {
        return new ResponseEntity<>(pmMedicalLineService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询体检申请子表（分页）")
    @ApiOperation("查询体检申请子表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('medicalLine:list','medical:list')")
    public ResponseEntity getMedicalLinePage(PmMedicalLineQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptAllId(), null, false, true, criteria.getDeptIds(), null);
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority auth : grantedAuthorityCollection
        ){
            if(auth.getAuthority().equals("medicalNoData:list"))
            {criteria.setEmployeeId(null);}
    }
        return new ResponseEntity<>(pmMedicalLineService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询体检申请子表（不分页）")
    @ApiOperation("查询体检申请子表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('medicalLine:list','medical:list')")
    public ResponseEntity getMedicalLineNoPaging(PmMedicalLineQueryCriteria criteria) {
    return new ResponseEntity<>(pmMedicalLineService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出体检申请子表数据")
    @ApiOperation("导出体检申请子表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('medicalLine:list','medical:list')")
    public void download(HttpServletResponse response, PmMedicalLineQueryCriteria criteria) throws IOException {
        pmMedicalLineService.download(pmMedicalLineService.listAll(criteria), response);
    }

    @ErrorLog("根据体检申请ID获取体检申请子表内容")
    @ApiOperation("根据体检申请ID获取体检申请子表内容")
    @GetMapping(value = "/medicalDetail/{medicalId}")
    @PreAuthorize("@el.check('medicalLine:list','medical:list')")
    public ResponseEntity getMedicalLineNoPaging(@PathVariable Long medicalId) {
        return new ResponseEntity<>(pmMedicalLineService.listAllByMedicalId(medicalId), HttpStatus.OK);
    }

    @Log("批量反馈体检结果")
    @ApiOperation("批量反馈体检结果")
    @PutMapping(value = "/batchMedicalResult")
    @PreAuthorize("@el.check('medicalLine:edit','medical:edit')")
    public ResponseEntity batchMedicalResult(@RequestBody List<PmMedicalLineQueryCriteria> criteria) {
//        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
//        criteria.setUpdateBy(user.getId());
        pmMedicalLineService.batchWriteMedicalResult(criteria);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("自动生成普通体检员工名单")
    @ApiOperation("自动生成普通体检员工名单")
    @GetMapping(value = "/getMedicalLinesAuto")
    @PreAuthorize("@el.check('medicalNoData:list')")
    public ResponseEntity getMedicalLinesAuto() {
        return new ResponseEntity<>(pmMedicalLineService.getMedicalLinesAuto(), HttpStatus.OK);
    }
}
