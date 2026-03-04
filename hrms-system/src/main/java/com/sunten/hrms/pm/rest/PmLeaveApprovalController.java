package com.sunten.hrms.pm.rest;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import com.sunten.hrms.pm.dto.PmLeaveApprovalDTO;
import com.sunten.hrms.pm.mapper.PmLeaveApprovalMapper;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.utils.ListUtils;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.PmLeaveApproval;
import com.sunten.hrms.pm.dto.PmLeaveApprovalQueryCriteria;
import com.sunten.hrms.pm.service.PmLeaveApprovalService;
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
 * 离职审批表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-05-07
 */
@RestController
@Api(tags = "离职审批表")
@RequestMapping("/api/pm/leaveApproval")
public class PmLeaveApprovalController {
    private static final String ENTITY_NAME = "leaveApproval";
    private final PmLeaveApprovalService pmLeaveApprovalService;
    private final FndUserService fndUserService;
    private final JwtPermissionService jwtPermissionService;
    private final FndDeptService fndDeptService;
    private final FndDataScope fndDataScope;
    private final PmLeaveApprovalMapper pmLeaveApprovalMapper;
    @Value("${role.authPmCharge}")
    private String pmCharge;

    public PmLeaveApprovalController(PmLeaveApprovalService pmLeaveApprovalService, FndUserService fndUserService, JwtPermissionService jwtPermissionService,
                                     FndDeptService fndDeptService, FndDataScope fndDataScope, PmLeaveApprovalMapper pmLeaveApprovalMapper) {
        this.pmLeaveApprovalService = pmLeaveApprovalService;
        this.fndUserService = fndUserService;
        this.jwtPermissionService = jwtPermissionService;
        this.fndDeptService = fndDeptService;
        this.fndDataScope = fndDataScope;
        this.pmLeaveApprovalMapper = pmLeaveApprovalMapper;
    }

    @Log("新增离职审批表")
    @ApiOperation("新增离职审批表")
    @PostMapping
    @PreAuthorize("@el.check('leaveApproval:add')")
    public ResponseEntity create(@Validated @RequestBody PmLeaveApproval leaveApproval) {
        if (leaveApproval.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(pmLeaveApprovalService.insert(leaveApproval), HttpStatus.CREATED);
    }

    @Log("删除离职审批表")
    @ApiOperation("删除离职审批表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('leaveApproval:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmLeaveApprovalService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该离职审批表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("失效离职审批")
    @ApiOperation("删除离职审批表")
    @PutMapping(value = "disabledById/{id}")
    @PreAuthorize("@el.check('leaveApproval:cancel')")
    public ResponseEntity disabledById(@PathVariable Long id){
        pmLeaveApprovalService.disabledById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改离职审批表")
    @ApiOperation("修改离职审批表")
    @PutMapping
    @PreAuthorize("@el.check('leaveApproval:edit')")
    public ResponseEntity update(@Validated(PmLeaveApproval.Update.class) @RequestBody PmLeaveApproval leaveApproval) {
        pmLeaveApprovalService.update(leaveApproval);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个离职审批表")
    @ApiOperation("获取单个离职审批表")
    @GetMapping(value = "/{id}")
//    @PreAuthorize("@el.check('leaveApproval:list')")
    public ResponseEntity getLeaveApproval(@PathVariable Long id) {
        return new ResponseEntity<>(pmLeaveApprovalService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("根据oaOrder获取离职审批")
    @ApiOperation("根据oaOrder获取离职审批")
    @GetMapping(value = "/getByOaOrder/{oaOrder}")
    @PreAuthorize("@el.check('leaveApproval:list')")
    public ResponseEntity getLeaveApprovalByOaOrder(@PathVariable String oaOrder) {
        return new ResponseEntity<>(pmLeaveApprovalService.getByOaOrder(oaOrder), HttpStatus.OK);
    }

    @ErrorLog("查询离职审批表（分页）")
    @ApiOperation("查询离职审批表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('leaveApproval:list')")
    public ResponseEntity getLeaveApprovalPage(PmLeaveApprovalQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(getListByAuthAndPageable(criteria, pageable), HttpStatus.OK);
    }

    private Object getListByAuthAndPageable(PmLeaveApprovalQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        if (null != criteria.getDeptId()) {
            Set<Long> deptIds = new HashSet<>(fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptId()));
            criteria.setDeptId(null);
            criteria.setDeptIds(deptIds);
        }
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        if (user.getJob().getJobName().equals("市场管理专员")) {
            criteria.setCreateBy(user.getId());
        }
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority auth: grantedAuthorityCollection
             ) {
            if (auth.getAuthority().equals(pmCharge)) {
                // 直接返回
                if (null == pageable) {
                    return pmLeaveApprovalService.listAll(criteria);
                } else {
                    return pmLeaveApprovalService.listAll(criteria, pageable);
                }
            }
        }
        if (null == user.getEmployee()){ // 已忽略admin
            throw new InfoCheckWarningMessException("检测到该用户未绑定员工，请联系管理员进行员工绑定");
        } else {
            FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptId(), null, false, false, criteria.getDeptIds(), null);
            if (fndDataScope.isNoDataPermission(dataScopeVo)) { // 无权限, 不会进到这里面
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
            } else {
                if (null != dataScopeVo.getEmployeeId()) { // 只查看个人
                    criteria.setEmployeeId(user.getEmployee().getId());
                    if (null == pageable){
                        return pmLeaveApprovalService.listAll(criteria);
                    } else {
                        return pmLeaveApprovalService.listAll(criteria, pageable);
                    }
                } else { // 按权限查看
                    criteria.setDeptId(null);
                    criteria.setDeptIds(dataScopeVo.getDeptIds());
                    if (null == pageable){
                        return pmLeaveApprovalService.listAll(criteria);
                    } else {
                        return pmLeaveApprovalService.listAll(criteria, pageable);
                    }
                }
            }
        }
    }

    @ErrorLog("查询离职审批表（不分页）")
    @ApiOperation("查询离职审批表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('leaveApproval:list')")
    public ResponseEntity getLeaveApprovalNoPaging(PmLeaveApprovalQueryCriteria criteria) {
        return new ResponseEntity<>(getListByAuthAndPageable(criteria, null), HttpStatus.OK);
    }

    @ErrorLog("导出离职审批表数据")
    @ApiOperation("导出离职审批表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('leaveApproval:list')")
    public void download(HttpServletResponse response, PmLeaveApprovalQueryCriteria criteria) throws IOException {
        try {
            pmLeaveApprovalService.download((List<PmLeaveApprovalDTO>)getListByAuthAndPageable(criteria, null), response);
        } catch (Exception e){
            e.printStackTrace();
            throw new InfoCheckWarningMessException("数据转换有问题");
        }
    }

    @ErrorLog("获取销售区域审批人员等")
    @ApiOperation("获取销售区域审批人员等")
    @GetMapping(value = "/getSalesAreaEtc/{deptId}")
    @PreAuthorize("@el.check('leaveApproval:list')")
    public ResponseEntity getSalesAreaEtc(@PathVariable Long deptId) {
        return new ResponseEntity<>(pmLeaveApprovalService.getSalesAreaEtcByDeptId(deptId), HttpStatus.OK);
    }

    @ErrorLog("根据销售关系获取人员信息")
    @ApiOperation("根据销售关系获取人员信息")
    @GetMapping(value = "/getPmsBySalesRelations")
    @PreAuthorize("@el.check('leaveApproval:list')")
    public ResponseEntity getPmsBySalesRelations(PmEmployeeQueryCriteria pmEmployeeQueryCriteria) {
        return new ResponseEntity<>(pmLeaveApprovalService.getPmsBySalesRelations(pmEmployeeQueryCriteria), HttpStatus.OK);
    }

    @ErrorLog("检测同一个人是否存在已在走的离职流程")
    @ApiOperation("检测同一个人是否存在已在走的离职流程")
    @GetMapping(value = "/checkIsExistLeaveInApproval/{employeeId}")
    @PreAuthorize("@el.check('leaveApproval:add')")
    public ResponseEntity checkIsExistLeaveInApproval(@PathVariable Long employeeId) {
        return new ResponseEntity<>(pmLeaveApprovalService.checkIsExistLeaveInApproval(employeeId), HttpStatus.OK);
    }

}
