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
import com.sunten.hrms.pm.domain.PmMedical;
import com.sunten.hrms.pm.dto.PmMedicalQueryCriteria;
import com.sunten.hrms.pm.service.PmMedicalService;
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

/**
 * <p>
 * 体检申请表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-04-07
 */
@RestController
@Api(tags = "体检申请表")
@RequestMapping("/api/pm/medical")
public class PmMedicalController {
    private static final String ENTITY_NAME = "medical";
    private final PmMedicalService pmMedicalService;
    private final FndDataScope fndDataScope;
    private final JwtPermissionService jwtPermissionService;
    private final FndUserService fndUserService;

    public PmMedicalController(PmMedicalService pmMedicalService, FndDataScope fndDataScope, JwtPermissionService jwtPermissionService, FndUserService fndUserService) {
        this.pmMedicalService = pmMedicalService;
        this.fndDataScope = fndDataScope;
        this.jwtPermissionService = jwtPermissionService;
        this.fndUserService = fndUserService;
    }

    @Log("新增体检申请表")
    @ApiOperation("新增体检申请表")
    @PostMapping
    @PreAuthorize("@el.check('medical:add')")
    public ResponseEntity create(@Validated @RequestBody PmMedical medical) {
        if (medical.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(pmMedicalService.insert(medical), HttpStatus.CREATED);
    }

    @Log("删除体检申请表")
    @ApiOperation("删除体检申请表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('medical:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmMedicalService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该体检申请表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改体检申请表")
    @ApiOperation("修改体检申请表")
    @PutMapping
    @PreAuthorize("@el.check('medical:edit')")
    public ResponseEntity update(@Validated(PmMedical.Update.class) @RequestBody PmMedical medical) {
        pmMedicalService.update(medical);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个体检申请表")
    @ApiOperation("获取单个体检申请表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('medical:list')")
    public ResponseEntity getMedical(@PathVariable Long id) {
        return new ResponseEntity<>(pmMedicalService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询体检申请表（分页）")
    @ApiOperation("查询体检申请表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('medical:list')")
    public ResponseEntity getMedicalPage(PmMedicalQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
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
        return new ResponseEntity<>(pmMedicalService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询体检申请表（不分页）")
    @ApiOperation("查询体检申请表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('medical:list')")
    public ResponseEntity getMedicalNoPaging(PmMedicalQueryCriteria criteria) {
    return new ResponseEntity<>(pmMedicalService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出体检申请表数据")
    @ApiOperation("导出体检申请表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('medical:list')")
    public void download(HttpServletResponse response, PmMedicalQueryCriteria criteria) throws IOException {
        pmMedicalService.download(pmMedicalService.listAll(criteria), response);
    }

    @Log("根据OA申请单号获取体检申请内容")
    @ApiOperation("根据申请单号获取体检申请内容")
    @GetMapping(value = "/getMedicalDetailByReqCode")
    public ResponseEntity createFromOA(String reqCode) {
        // 获取所传申请单，获取体检申请所有信息
        return new ResponseEntity<>(pmMedicalService.getByReqCode(reqCode), HttpStatus.CREATED);
    }

    @Log("从OA回填体检申请表审批内容")
    @ApiOperation("从OA回填体检申请表审批内容")
    @PostMapping(value = "/updateMedicalApproval")
    public ResponseEntity updateMedicalApproval(String reqCode, String approvalNode, String approvalEmployee, String approvalResult) {
        // 获取所传体检申请表单信息，看是否需要处理，然后执行修改
        pmMedicalService.updateApprovalContent(reqCode,approvalNode,approvalEmployee,approvalResult);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("今年是否通过特殊工种年度体检和干部层年度体检")
    @ApiOperation("从OA回填体检申请表审批内容")
    @GetMapping(value = "/getPmMedicalPass")
    public int getPmMedicalPass() {
        return pmMedicalService.getPmMedicalPass();
    }
}
