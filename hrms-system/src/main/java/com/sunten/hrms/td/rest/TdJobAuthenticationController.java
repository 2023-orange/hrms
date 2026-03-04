package com.sunten.hrms.td.rest;

import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeService;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.td.dto.TdJobAuthenticationDTO;
import com.sunten.hrms.td.dto.TdJobGradingDTO;
import com.sunten.hrms.td.dto.TdJobGradingQueryCriteria;
import com.sunten.hrms.td.service.TdTrainEmployeeJurisdictionService;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.td.domain.TdJobAuthentication;
import com.sunten.hrms.td.dto.TdJobAuthenticationQueryCriteria;
import com.sunten.hrms.td.service.TdJobAuthenticationService;
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
import java.util.*;

/**
 * <p>
 * 上岗认证表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-06-22
 */
@RestController
@Api(tags = "上岗认证表")
@RequestMapping("/api/td/jobAuthentication")
public class TdJobAuthenticationController {

    @Value("${role.authTrainCharge}")
    private String authTrainCharge;

    private static final String ENTITY_NAME = "jobAuthentication";
    private final TdJobAuthenticationService tdJobAuthenticationService;
    private final FndUserService fndUserService;
    private final JwtPermissionService jwtPermissionService;
    private final TdTrainEmployeeJurisdictionService tdTrainEmployeeJurisdictionService;
    private final PmEmployeeService pmEmployeeService;
    private final FndDataScope fndDataScope;

    public TdJobAuthenticationController(TdJobAuthenticationService tdJobAuthenticationService, FndUserService fndUserService, JwtPermissionService jwtPermissionService, TdTrainEmployeeJurisdictionService tdTrainEmployeeJurisdictionService, PmEmployeeService pmEmployeeService,FndDataScope fndDataScope) {
        this.tdJobAuthenticationService = tdJobAuthenticationService;
        this.fndUserService = fndUserService;
        this.jwtPermissionService = jwtPermissionService;
        this.tdTrainEmployeeJurisdictionService = tdTrainEmployeeJurisdictionService;
        this.pmEmployeeService = pmEmployeeService;
        this.fndDataScope = fndDataScope;
    }

    @Log("新增上岗认证表")
    @ApiOperation("新增上岗认证表")
    @PostMapping
    @PreAuthorize("@el.check('jobAuthentication:add')")
    public ResponseEntity create(@Validated @RequestBody TdJobAuthentication jobAuthentication) {
        if (jobAuthentication.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdJobAuthenticationService.insert(jobAuthentication), HttpStatus.CREATED);
    }

    @Log("删除上岗认证表")
    @ApiOperation("删除上岗认证表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('jobAuthentication:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdJobAuthenticationService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该上岗认证表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改上岗认证表")
    @ApiOperation("修改上岗认证表")
    @PutMapping
    @PreAuthorize("@el.check('jobAuthentication:edit')")
    public ResponseEntity update(@Validated(TdJobAuthentication.Update.class) @RequestBody TdJobAuthentication jobAuthentication) {
        tdJobAuthenticationService.update(jobAuthentication);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个上岗认证表")
    @ApiOperation("获取单个上岗认证表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('jobAuthentication:list')")
    public ResponseEntity getJobAuthentication(@PathVariable Long id) {
        return new ResponseEntity<>(tdJobAuthenticationService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询上岗认证表（分页）")
    @ApiOperation("查询上岗认证表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('jobAuthentication:list')")
    public ResponseEntity getJobAuthenticationPage(TdJobAuthenticationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        //  一：如果是培训专员，那么返回全部记录。
        // 培训管理员标识
        Boolean adminFlag = false;
        // 培训员标识
        Boolean trainFlag = false;
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority auth : grantedAuthorityCollection
        ) {
            // 1、判断是否培训专员
            if (auth.getAuthority().equals(authTrainCharge)) {
                // 返回全部节点
                adminFlag = true;
                return new ResponseEntity<>(tdJobAuthenticationService.listAll(criteria, pageable), HttpStatus.OK);
            }
        }

        // isNoDataPermission这个函数的作用是    获取当前登录人的数据权限，例如是“本部门”，“本部门及其下”，或本人
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptAllId(), criteria.getDeptId(), false, true, criteria.getDeptIds(), criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }

        //  二：如果不是培训专员，再判断是否培训员。如果是培训员，那么返回它的管辖部门。
        if (!adminFlag   ) {
            // TODO获取培训员管辖部门
            List<Long> depts = tdTrainEmployeeJurisdictionService.getDeptsByEmployeeeId(user.getEmployee().getId());
            //System.out.println(depts.size());
            //depts.size()>0  是培训员
            if (depts.size()>0){
            criteria.setDeptIds(new HashSet<>(depts));
            return new ResponseEntity<>(tdJobAuthenticationService.listAll(criteria, pageable), HttpStatus.OK);
            }
        }

        //  三：判断是否公司管理层。如果是，那么返回其能查看的全部部门ID集合。getDeptIds()
        if (dataScopeVo.getDeptIds().size()   > 0  ) {
            criteria.setDeptIds(dataScopeVo.getDeptIds());
            return new ResponseEntity<>(tdJobAuthenticationService.listAll(criteria, pageable), HttpStatus.OK);
        }

        //  四：经过上述筛选，当前登录人 是 普通用户，只能查看自己的本人的一行记录。
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(tdJobAuthenticationService.listAll(criteria, pageable), HttpStatus.OK);
    }


    @ErrorLog("查询人事档案表--培训员范围")
    @ApiOperation("查询人事档案表--培训员范围")
    @GetMapping(value = "/getByTrain")
    @PreAuthorize("@el.check('jobAuthentication:list')")
    public ResponseEntity getByTrain(PmEmployeeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        // 获取登陆人的培训员管辖部门权限
        Boolean adminFlag = false; // 培训管理员标识
        Boolean trainFlag = false; // 培训员标识
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
        if (!adminFlag) { // 如果不是培训管理员,则说明是培训员
            // TODO获取培训员管辖部门
            List<Long> depts = tdTrainEmployeeJurisdictionService.getDeptsByEmployeeeId(user.getEmployee().getId());
            criteria.setDeptIds(new HashSet<>(depts));
        }
        return new ResponseEntity<>(pmEmployeeService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询上岗认证表（不分页）")
    @ApiOperation("查询上岗认证表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('jobAuthentication:list')")
    public ResponseEntity getJobAuthenticationNoPaging(TdJobAuthenticationQueryCriteria criteria) {
        return new ResponseEntity<>(tdJobAuthenticationService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出上岗认证表数据")
    @ApiOperation("导出上岗认证表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('jobAuthentication:list')")
    public void download(HttpServletResponse response, TdJobAuthenticationQueryCriteria criteria) throws IOException {
        tdJobAuthenticationService.download(tdJobAuthenticationService.listAll(criteria), response);
    }

    @ErrorLog("导出上岗认证表数据2")
    @ApiOperation("导出上岗认证表数据2")
    @GetMapping(value = "/downloadTwo")
    @PreAuthorize("@el.check('jobAuthentication:list')")
    public void downloadTwo(HttpServletResponse response, TdJobAuthenticationQueryCriteria criteria) throws IOException {
        //  一：如果是培训专员，那么返回全部记录。
        // 培训管理员标识
        Boolean adminFlag = false;
        // 培训员标识
        Boolean trainFlag = false;
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority auth : grantedAuthorityCollection
        ) {
            // 1、判断是否培训专员
            if (auth.getAuthority().equals(authTrainCharge)) {
                // 返回全部节点
                adminFlag = true;
                tdJobAuthenticationService.download(tdJobAuthenticationService.listAll(criteria), response);
                return;
            }
        }

        // isNoDataPermission这个函数的作用是    获取当前登录人的数据权限，例如是“本部门”，“本部门及其下”，或本人
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptAllId(), criteria.getDeptId(), false, true, criteria.getDeptIds(), criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
             //return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
            throw new InfoCheckWarningMessException("当前登录人的数据权限没有 !");
        }

        //  二：如果不是培训专员，再判断是否培训员。如果是培训员，那么返回它的管辖部门。
        if (!adminFlag   ) {
            // TODO获取培训员管辖部门
            List<Long> depts = tdTrainEmployeeJurisdictionService.getDeptsByEmployeeeId(user.getEmployee().getId());
            //System.out.println(depts.size());
            //depts.size()>0  是培训员
            if (depts.size()>0){
                criteria.setDeptIds(new HashSet<>(depts));
                tdJobAuthenticationService.download(tdJobAuthenticationService.listAll(criteria), response);
                return;
            }
        }

        //  三：判断是否公司管理层。如果是，那么返回其能查看的全部部门ID集合。getDeptIds()
        if (dataScopeVo.getDeptIds().size()   > 0  ) {
            criteria.setDeptIds(dataScopeVo.getDeptIds());
            tdJobAuthenticationService.download(tdJobAuthenticationService.listAll(criteria), response);
            return;
        }

        //  四：经过上述筛选，当前登录人 是 普通用户，只能查看自己的本人的一行记录。
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        tdJobAuthenticationService.download(tdJobAuthenticationService.listAll(criteria), response);
    }

    @ErrorLog("验证申请人员是否持有相关的认证岗位")
    @ApiOperation("验证申请人员是否持有相关的认证岗位")
    @GetMapping(value = "/getAuthentication")
    /**
     * 使用《工号，姓名，人事岗位ID，调入所需的认证岗位名称》作为入参，验证申请人员是否持有相关的认证岗位
     */
    public ResponseEntity getAuthentication(TdJobGradingQueryCriteria criteria) {
        Map<String,Object> map = new HashMap<>();
        List<TdJobAuthenticationDTO> authenticationList = null;
        authenticationList = tdJobAuthenticationService.getAuthentication(criteria);
        map.put("authenticationList",authenticationList);
        map.put("total",authenticationList.size());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
