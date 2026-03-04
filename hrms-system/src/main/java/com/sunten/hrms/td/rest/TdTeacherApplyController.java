package com.sunten.hrms.td.rest;

import com.sunten.hrms.annotation.AnonymousAccess;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.td.domain.TdTeacherApply;
import com.sunten.hrms.td.dto.TdTeacherApplyQueryCriteria;
import com.sunten.hrms.td.service.TdTeacherApplyService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * 讲师身份（申请）表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-06-15
 */
@RestController
@Api(tags = "讲师身份（申请）表")
@RequestMapping("/api/td/teacherApply")
public class TdTeacherApplyController {
    private static final String ENTITY_NAME = "teacherApply";

    @Value("${role.authTrainCharge}")
    private String authTrainCharge;
    private final TdTeacherApplyService tdTeacherApplyService;
    private final FndDataScope fndDataScope;
    private final FndUserService fndUserService;
    private final JwtPermissionService jwtPermissionService;

    public TdTeacherApplyController(TdTeacherApplyService tdTeacherApplyService, FndDataScope fndDataScope, FndUserService fndUserService, JwtPermissionService jwtPermissionService) {
        this.tdTeacherApplyService = tdTeacherApplyService;
        this.fndDataScope = fndDataScope;
        this.fndUserService = fndUserService;
        this.jwtPermissionService = jwtPermissionService;
    }

    @Log("新增讲师身份（申请）表")
    @ApiOperation("新增讲师身份（申请）表")
    @PostMapping
    @PreAuthorize("@el.check('teacherApply:add')")
    public ResponseEntity create(@Validated @RequestBody TdTeacherApply teacherApply) {
        if (teacherApply.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdTeacherApplyService.insert(teacherApply), HttpStatus.CREATED);
    }

    @Log("删除讲师身份（申请）表")
    @ApiOperation("删除讲师身份（申请）表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('teacherApply:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdTeacherApplyService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该讲师身份（申请）表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改讲师身份（申请）表")
    @ApiOperation("修改讲师身份（申请）表")
    @PutMapping
    @PreAuthorize("@el.check('teacherApply:edit')")
    public ResponseEntity update(@Validated(TdTeacherApply.Update.class) @RequestBody TdTeacherApply teacherApply) {
        tdTeacherApplyService.update(teacherApply);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个讲师身份（申请）表")
    @ApiOperation("获取单个讲师身份（申请）表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('teacherApply:list')")
    public ResponseEntity getTeacherApply(@PathVariable Long id) {
        return new ResponseEntity<>(tdTeacherApplyService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询讲师身份（申请）表（分页）")
    @ApiOperation("查询讲师身份（申请）表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('teacherApply:list')")
    public ResponseEntity getTeacherApplyPage(TdTeacherApplyQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Boolean adminFlag = false; // 培训管理员标识
        // 1、判断是否培训专员
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        // 考勤管理员
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority auth : grantedAuthorityCollection
        ) {
            if (auth.getAuthority().equals(authTrainCharge)) {
                // 返回全部节点
                adminFlag = true;
            }
        }
        if (!adminFlag) {
            FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, criteria.getDeptIds(), criteria.getEmployeeId());
            if (fndDataScope.isNoDataPermission(dataScopeVo)) {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            criteria.setDeptIds(dataScopeVo.getDeptIds());
            criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        }

        return new ResponseEntity<>(tdTeacherApplyService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询讲师身份（申请）表（不分页）")
    @ApiOperation("查询讲师身份（申请）表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('teacherApply:list')")
    public ResponseEntity getTeacherApplyNoPaging(TdTeacherApplyQueryCriteria criteria) {
    return new ResponseEntity<>(tdTeacherApplyService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出讲师身份（申请）表数据")
    @ApiOperation("导出讲师身份（申请）表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('teacherApply:list')")
    public void download(HttpServletResponse response, TdTeacherApplyQueryCriteria criteria) throws IOException {
        tdTeacherApplyService.download(tdTeacherApplyService.listAll(criteria), response);
    }
    @Log("认证讲师并上传文件")
    @ApiOperation("认证讲师并上传文件")
    @PostMapping(value = "/attestationTeacher")
    @PreAuthorize("@el.check('teacherApply:admin')")
    public ResponseEntity attestationTeacher(@RequestParam("id") Long id, @RequestParam("level") String level,
                                             @RequestParam("score") BigDecimal score,
                                             @RequestParam("attribute3") Boolean attribute3,
                                             @RequestParam("file") MultipartFile file){
        TdTeacherApply tdTeacherApply = new TdTeacherApply();
        tdTeacherApply.setId(id);
        tdTeacherApply.setLevel(level);
        tdTeacherApply.setScore(score);
        tdTeacherApply.setAttribute3(attribute3);
        return new ResponseEntity<>(tdTeacherApplyService.attestationTeacher(tdTeacherApply,file),HttpStatus.CREATED);
    }

    @ErrorLog("根据OA申请单号获取试用审批表")
    @ApiOperation("根据OA申请单号获取试用审批表")
    @GetMapping(value = "/getTeacherApplyByReqCode")
    @AnonymousAccess
    public ResponseEntity getTeacherApplyByReqCode(String reqCode) {
        return new ResponseEntity<>(tdTeacherApplyService.getByReqCode(reqCode), HttpStatus.OK);
    }

    @Log("OA审批时修改试用审批表")
    @ApiOperation("OA审批时修改试用审批表")
    @PostMapping(value = "/updateFromOA")
    public ResponseEntity updateFromOA(@RequestBody TdTeacherApply tdTeacherApply) {
        tdTeacherApplyService.writeOaApprovalResult(tdTeacherApply);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
