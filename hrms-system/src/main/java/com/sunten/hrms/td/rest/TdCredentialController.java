package com.sunten.hrms.td.rest;

import cn.hutool.json.JSONArray;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.td.domain.TdCredential;
import com.sunten.hrms.td.dto.TdCredentialQueryCriteria;
import com.sunten.hrms.td.service.TdCredentialService;
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
import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * 培训证书表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-06-30
 */
@RestController
@Api(tags = "培训证书表")
@RequestMapping("/api/td/credential")
public class TdCredentialController {
    private static final String ENTITY_NAME = "credential";
    private final TdCredentialService tdCredentialService;
    private final JwtPermissionService jwtPermissionService;
    private final FndDataScope fndDataScope;
    private final FndUserService fndUserService;
    private final FndDeptService fndDeptService;
    @Value("${role.authTrainCharge}")
    private String authTrainCharge;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public TdCredentialController(TdCredentialService tdCredentialService, JwtPermissionService jwtPermissionService, FndDataScope fndDataScope,
                                  FndUserService fndUserService, FndDeptService fndDeptService) {
        this.tdCredentialService = tdCredentialService;
        this.jwtPermissionService = jwtPermissionService;
        this.fndDataScope = fndDataScope;
        this.fndUserService = fndUserService;
        this.fndDeptService = fndDeptService;
    }

    @Log("新增培训证书表")
    @ApiOperation("新增培训证书表")
    @PostMapping
    @PreAuthorize("@el.check('credential:add')")
    public ResponseEntity create(@Validated @RequestBody TdCredential credential) {
        if (credential.getId()  != -1) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdCredentialService.insert(credential), HttpStatus.CREATED);
    }

    @Log("删除培训证书表")
    @ApiOperation("删除培训证书表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('credential:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdCredentialService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该培训证书表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("失效培训证书")
    @ApiOperation("失效培训证书")
    @PutMapping(value = "disabledCredential")
    @PreAuthorize("@el.check('credential:del')")
    public ResponseEntity disabledCredential(@Validated(TdCredential.Update.class) @RequestBody TdCredential credential) {
        tdCredentialService.update(credential);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("审核培训证书")
    @ApiOperation("审核培训证书")
    @PutMapping(value = "checkCredential")
    @PreAuthorize("@el.check('credential:edit')")
    public ResponseEntity checkCredential(@Validated(TdCredential.Update.class) @RequestBody TdCredential credential) {
        tdCredentialService.update(credential);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改培训证书表")
    @ApiOperation("修改培训证书表")
    @PutMapping
    @PreAuthorize("@el.check('credential:edit')")
    public ResponseEntity update(@Validated(TdCredential.Update.class) @RequestBody TdCredential credential) {
        tdCredentialService.update(credential);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个培训证书表")
    @ApiOperation("获取单个培训证书表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('credential:list')")
    public ResponseEntity getCredential(@PathVariable Long id) {
        return new ResponseEntity<>(tdCredentialService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询培训证书表（分页）")
    @ApiOperation("查询培训证书表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('credential:list')")
    public ResponseEntity getCredentialPage(TdCredentialQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        // 高级查询条件
        // 部分范围控制
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority authority : grantedAuthorityCollection
             ) {
            if (authority.getAuthority().equals(authTrainCharge)) {
                if (null != criteria.getDeptId()) {
                    criteria.setDeptIds(new HashSet<>(fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptId())));
                    return new ResponseEntity<>(tdCredentialService.listAll(criteria, pageable), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(tdCredentialService.listAll(criteria, pageable), HttpStatus.OK);
                }
            }
        }
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, criteria.getDeptId(), false, true, null, null);
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            criteria.setDeptIds(dataScopeVo.getDeptIds());
            criteria.setEmployeeId(dataScopeVo.getEmployeeId());
            return new ResponseEntity<>(tdCredentialService.listAll(criteria, pageable), HttpStatus.OK);
        }
    }

    @ErrorLog("查询培训证书表（不分页）")
    @ApiOperation("查询培训证书表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('credential:list')")
    public ResponseEntity getCredentialNoPaging(TdCredentialQueryCriteria criteria) {
    return new ResponseEntity<>(tdCredentialService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出培训证书表数据")
    @ApiOperation("导出培训证书表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('credential:list')")
    public void download(HttpServletResponse response, TdCredentialQueryCriteria criteria) throws IOException {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority authority : grantedAuthorityCollection
        ) {
            if (authority.getAuthority().equals(authTrainCharge)) {
                if (null != criteria.getDeptId()) {
                    criteria.setDeptIds(new HashSet<>(fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptId())));
                    tdCredentialService.download(tdCredentialService.listAll(criteria), response);
                } else {
                    tdCredentialService.download(tdCredentialService.listAll(criteria), response);
                }
            }
        }
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, criteria.getDeptId(), false, true, null, null);
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            tdCredentialService.download(tdCredentialService.listAll(criteria), response);
        } else {
            criteria.setDeptIds(dataScopeVo.getDeptIds());
            criteria.setEmployeeId(dataScopeVo.getEmployeeId());
            tdCredentialService.download(tdCredentialService.listAll(criteria), response);
        }
    }
}
