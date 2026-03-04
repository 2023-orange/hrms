package com.sunten.hrms.td.rest;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.td.domain.TdJobGrading;
import com.sunten.hrms.td.dto.TdJobGradingDTO;
import com.sunten.hrms.td.dto.TdJobGradingQueryCriteria;
import com.sunten.hrms.td.service.TdJobGradingService;
import com.sunten.hrms.td.service.TdTrainEmployeeJurisdictionService;
import com.sunten.hrms.utils.SecurityUtils;
import com.sunten.hrms.utils.ThrowableUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 岗位分级表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2022-03-22
 */
@RestController
@Api(tags = "岗位分级表")
@RequestMapping("/api/td/jobGrading")
public class TdJobGradingController {
    private static final String ENTITY_NAME = "jobGrading";
    private final TdJobGradingService tdJobGradingService;
    private final TdTrainEmployeeJurisdictionService tdTrainEmployeeJurisdictionService;
    private final FndUserService fndUserService;
    private final JwtPermissionService jwtPermissionService;
    @Value("${role.authTrainCharge}")
    private String authTrainCharge;

    public TdJobGradingController(TdJobGradingService tdJobGradingService, TdTrainEmployeeJurisdictionService tdTrainEmployeeJurisdictionService, FndUserService fndUserService, JwtPermissionService jwtPermissionService) {
        this.tdJobGradingService = tdJobGradingService;
        this.tdTrainEmployeeJurisdictionService = tdTrainEmployeeJurisdictionService;
        this.fndUserService = fndUserService;
        this.jwtPermissionService = jwtPermissionService;
    }

    @Log("新增岗位分级表")
    @ApiOperation("新增岗位分级表")
    @PostMapping
    @PreAuthorize("@el.check('jobGrading:add')")
    public ResponseEntity create(@Validated @RequestBody TdJobGrading jobGrading) {
        return new ResponseEntity<>(tdJobGradingService.insert(jobGrading), HttpStatus.CREATED);
    }

    @Log("追加岗位分级表")
    @ApiOperation("追加岗位分级表")
    @PostMapping("/supplementJobGrading")
    @PreAuthorize("@el.check('jobGrading:add')")
    public ResponseEntity supplementJobGrading(@Validated @RequestBody TdJobGrading jobGrading) {
        return new ResponseEntity<>(tdJobGradingService.supplementJobGrading(jobGrading), HttpStatus.CREATED);
    }

    @Log("删除岗位分级表")
    @ApiOperation("删除岗位分级表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('jobGrading:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdJobGradingService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该岗位分级表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改岗位分级表")
    @ApiOperation("修改岗位分级表")
    @PutMapping
    @PreAuthorize("@el.check('jobGrading:edit')")
    public ResponseEntity update(@Validated(TdJobGrading.Update.class) @RequestBody TdJobGrading jobGrading) {
        tdJobGradingService.update(jobGrading);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个岗位分级表")
    @ApiOperation("获取单个岗位分级表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('jobGrading:list')")
    public ResponseEntity getJobGrading(@PathVariable Long id) {
        return new ResponseEntity<>(tdJobGradingService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询岗位分级表（分页）")
    @ApiOperation("查询岗位分级表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('jobGrading:list')")
    public ResponseEntity getJobGradingPage(TdJobGradingQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        // 按权限范围查询
        Boolean isAdmin = false; // 是否培训管理员
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority authority : grantedAuthorityCollection
        ) {
            if (authority.getAuthority().equals(authTrainCharge)) {
                isAdmin = true;
            }
        }
        if (isAdmin) {
            return new ResponseEntity<>(tdJobGradingService.listAll(criteria, pageable), HttpStatus.OK);
        } else {
            if (user== null || user.getEmployee() == null || user.getEmployee().getId() == null)  {
                throw new InfoCheckWarningMessException("登陆用户无效");
            }
            List<Long> deptIds = tdTrainEmployeeJurisdictionService.getDeptsByEmployeeeId(user.getEmployee().getId());
            if (deptIds.size()>0) {
                criteria.setDeptIds(deptIds);
                return new ResponseEntity<>(tdJobGradingService.listAll(criteria, pageable), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
        }
    }

    @ErrorLog("查询岗位分级表（不分页）")
    @ApiOperation("查询岗位分级表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('jobGrading:list')")
    public ResponseEntity getJobGradingNoPaging(TdJobGradingQueryCriteria criteria) {
        return new ResponseEntity<>(tdJobGradingService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出岗位分级表数据")
    @ApiOperation("导出岗位分级表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('jobGrading:list')")
    public void download(HttpServletResponse response, TdJobGradingQueryCriteria criteria) throws IOException {
        // 按权限范围查询
        Boolean isAdmin = false; // 是否培训管理员
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority authority : grantedAuthorityCollection
        ) {
            if (authority.getAuthority().equals(authTrainCharge)) {
                isAdmin = true;
                tdJobGradingService.download(tdJobGradingService.listAll(criteria), response);
                return;
            }
        }
        if (isAdmin) {
            tdJobGradingService.download(tdJobGradingService.listAll(criteria), response);
            return;
        } else {
            if (user== null || user.getEmployee() == null || user.getEmployee().getId() == null)  {
                throw new InfoCheckWarningMessException("登陆用户无效");
            }
            List<Long> deptIds = tdTrainEmployeeJurisdictionService.getDeptsByEmployeeeId(user.getEmployee().getId());
            if (deptIds.size()>0) {
                criteria.setDeptIds(deptIds);
                tdJobGradingService.download(tdJobGradingService.listAll(criteria), response);
                return;
            } else {
                return;
            }
        }
    }

    @ErrorLog("查询工序及认证岗位集合")
    @ApiOperation("查询工序及认证岗位集合")
    @GetMapping(value = "/getProcessAndJobList")
    public ResponseEntity getProcessList() {
        Boolean isAdmin = false; // 是否培训管理员
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority authority : grantedAuthorityCollection
        ) {
            if (authority.getAuthority().equals(authTrainCharge)) {
                isAdmin = true;
            }
        }
        TdJobGradingQueryCriteria criteria = new TdJobGradingQueryCriteria();
        List<String> processList = null;
        List<TdJobGradingDTO> jobList = null;
        Map<String,Object> map = new HashMap<>();
        if (!isAdmin) {
            List<Long> deptIds = tdTrainEmployeeJurisdictionService.getDeptsByEmployeeeId(user.getEmployee().getId());
            if (deptIds.size()>0) {
                criteria.setDeptIds(deptIds);
                processList = tdJobGradingService.listForProcess(criteria);
                jobList = tdJobGradingService.listForJob(criteria);
                map.put("processList",processList);
                map.put("jobList",jobList);
            } else {
                map.put("processList",new ArrayList<>());
                map.put("jobList",new ArrayList<>());
            }
        } else {
            processList = tdJobGradingService.listForProcess(criteria);
            jobList = tdJobGradingService.listForJob(criteria);
            map.put("processList",processList);
            map.put("jobList",jobList);
        }

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @ErrorLog("根据工序查询认证岗位")
    @ApiOperation("根据工序查询认证岗位")
    @GetMapping(value = "/getCertificationJobByProcess")
    public ResponseEntity getCertificationJobByProcess(TdJobGradingQueryCriteria criteria) {
        Map<String,Object> map = new HashMap<>();
        List<TdJobGradingDTO> jobList = null;
        jobList = tdJobGradingService.getCertificationJobByProcess(criteria);
        map.put("jobList",jobList);
        map.put("total",jobList.size());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


}
