package com.sunten.hrms.td.rest;

import cn.hutool.json.JSONArray;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.td.domain.TdPlan;
import com.sunten.hrms.td.domain.TdPlanChangeHistory;
import com.sunten.hrms.td.domain.TdPlanImplement;
import com.sunten.hrms.td.dto.TdPlanImplementQueryCriteria;
import com.sunten.hrms.td.service.TdPlanChangeHistoryService;
import com.sunten.hrms.td.service.TdPlanImplementService;
import com.sunten.hrms.td.service.TdPlanService;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * <p>
 * 计划实施申请 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-05-25
 */
@RestController
@Api(tags = "计划实施申请")
@RequestMapping("/api/td/planImplement")
public class TdPlanImplementController {
    private static final String ENTITY_NAME = "planImplement";
    private final TdPlanImplementService tdPlanImplementService;
    private final TdPlanService tdPlanService;
    private final FndUserService fndUserService;
    private final JwtPermissionService jwtPermissionService;
    private final FndDataScope fndDataScope;
    private final TdPlanChangeHistoryService tdPlanChangeHistoryService;
    private final FndDeptService fndDeptService;
    @Value("${role.authTrainCharge}")
    private String authTrainCharge;

    public TdPlanImplementController(TdPlanImplementService tdPlanImplementService, TdPlanService tdPlanService, FndUserService fndUserService,
                                     JwtPermissionService jwtPermissionService, FndDataScope fndDataScope,TdPlanChangeHistoryService tdPlanChangeHistoryService,
                                     FndDeptService fndDeptService) {
        this.tdPlanImplementService = tdPlanImplementService;
        this.tdPlanService = tdPlanService;
        this.fndUserService = fndUserService;
        this.jwtPermissionService = jwtPermissionService;
        this.fndDataScope = fndDataScope;
        this.tdPlanChangeHistoryService = tdPlanChangeHistoryService;
        this.fndDeptService = fndDeptService;
    }

    @Log("新增计划实施申请")
    @ApiOperation("新增计划实施申请")
    @PostMapping
    @PreAuthorize("@el.check('planImplement:add')")
    public ResponseEntity create(@Validated @RequestBody TdPlanImplement planImplement) {
        if (planImplement.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdPlanImplementService.insert(planImplement), HttpStatus.CREATED);
    }

    @Log("删除计划实施申请")
    @ApiOperation("删除计划实施申请")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('planImplement:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdPlanImplementService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该计划实施申请存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改计划实施申请")
    @ApiOperation("修改计划实施申请")
    @PutMapping
    @PreAuthorize("@el.check('planImplement:edit')")
    public ResponseEntity update(@Validated(TdPlanImplement.Update.class) @RequestBody TdPlanImplement planImplement) {
        tdPlanImplementService.update(planImplement);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    /**
     *  @author：liangjw
     *  @Date: 2021/6/8 15:40
     *  @Description:  OA更新计划实施申请专用， 规则
     *  审批时主要更新currentPerson 和currentPerson
     *  审批通过时：
     *  将approvalStatus更新为已完成
     *  审批不通过时：
     *  将approvalStatus更新为不通过
     *
     *  由于实施页获取的对象时plan 包含planImplement 所以实施更新时，只需要更新该对象里对应的属性值，然后上传修改后的对象进行更新。如果自己组建对象要注意某些必要的值不能缺失
     *  @params:
     *
     */
    @Log("OA使用修改计划实施申请")
    @ApiOperation("OA使用修改计划实施申请")
    @PutMapping("/updateWithNoAuth")
    public ResponseEntity updateWithNoAuth(@RequestBody TdPlan plan) {
        if (null != plan && null != plan.getPlanImplement() && null != plan.getId()) {
            if (null != plan.getPlanImplement().getId()) {
                // 更新实施
                tdPlanImplementService.update(plan.getPlanImplement());
                // 如果为已完成，需要将plan的showFlag设为true
                // 整个查询对象传过来的,所以不用担心空指针
                if (plan.getPlanImplement().getApprovalStatus().equals("已完成")) {
                    // 更新Plan的showFlag及planStatus
                    tdPlanService.updateShowFlagAfterImplementPass(plan.getId());
                    // 发送邮件
                    tdPlanImplementService.sendEmailWithImplementId(plan.getPlanImplement().getId());
                    // 更新机flag
                    TdPlanChangeHistory tdPlanChangeHistory = new TdPlanChangeHistory();
                    tdPlanChangeHistory.setParentId(plan.getId());
                    tdPlanChangeHistory.setPassFlag(true);
                    tdPlanChangeHistoryService.updatePassOrNotPass(tdPlanChangeHistory);
                } else {
                    if (plan.getPlanImplement().getApprovalStatus().equals("不通过")) {
                        // 更新plan的planStatus
                        TdPlan tdPlan = new TdPlan();
                        tdPlan.setId(plan.getId());
                        tdPlan.setPlanStatus("实施审批不通过");
                        tdPlanService.update(tdPlan);
                        // 是否需要发邮件?
                        TdPlanChangeHistory tdPlanChangeHistory = new TdPlanChangeHistory();
                        tdPlanChangeHistory.setParentId(plan.getId());
                        tdPlanChangeHistory.setPassFlag(false);
                        tdPlanChangeHistoryService.updatePassOrNotPass(tdPlanChangeHistory);
                    }
                }
                return new ResponseEntity<>("success", HttpStatus.OK);
            } else {
                throw new InfoCheckWarningMessException("实施计划id缺失");
            }
        } else {
            throw new InfoCheckWarningMessException("更新对象不完整");
        }
    }

    @ErrorLog("获取单个计划实施申请")
    @ApiOperation("获取单个计划实施申请")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('planImplement:list')")
    public ResponseEntity getPlanImplement(@PathVariable Long id) {
        return new ResponseEntity<>(tdPlanImplementService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("给模板获取培训的相关id")
    @ApiOperation("给模板获取培训的相关id")
    @GetMapping(value = "/getByPlanIdForTemplate/{id}")
    @PreAuthorize("@el.check('plan:list')")
    public ResponseEntity getByPlanIdForTemplate(@PathVariable Long id) {
        return new ResponseEntity<>(tdPlanImplementService.getByPlanIdForTemplate(id), HttpStatus.OK);
    }

    @ErrorLog("查询计划实施申请（分页）")
    @ApiOperation("查询计划实施申请（分页）")
    @GetMapping
    @PreAuthorize("@el.check('planImplement:list','courseware:list')")
    public ResponseEntity getPlanImplementPage(TdPlanImplementQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());

        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority auth : grantedAuthorityCollection
        ) {
            if (auth.getAuthority().equals(authTrainCharge)) {
                // 返回全部
                if (null != criteria.getDeptId()) {
                    criteria.setDeptIds(new HashSet<>(fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptId())));
                    criteria.setDeptId(null);
                }
                return new ResponseEntity<>(tdPlanImplementService.listAll(criteria, pageable), HttpStatus.OK);
            }
        }
        if (null == user.getEmployee()) {
            throw new InfoCheckWarningMessException("该用户没有绑定员工，请联系管理员进行绑定");
        }
        // 获取管辖范围
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptId(), null, false, false, null, null);

        criteria.setPlanChargeId(user.getEmployee().getId());
        criteria.setDeptChargeId(user.getEmployee().getId());
        if (null != criteria.getDeptId()) {
            criteria.setDeptIds(dataScopeVo.getDeptIds().stream().filter(fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptId())::contains).collect(Collectors.toSet()));
            criteria.setDeptId(null);
        }
        return new ResponseEntity<>(tdPlanImplementService.listAll(criteria, pageable), HttpStatus.OK);
//        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
//            criteria.setRequestBy(user.getEmployee().getId());
//            criteria.setDeptChargeId(user.getEmployee().getId());
//            // 判定是不是计划负责人
//            if (null != criteria.getDeptId()) {
//                return new ResponseEntity<>(null, HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>( tdPlanImplementService.listAll(criteria, pageable), HttpStatus.OK);
//            }
//        } else {
//            // 取交集
//            criteria.setRequestBy(user.getEmployee().getId());
//            criteria.setDeptChargeId(user.getEmployee().getId());
//            if (null != criteria.getDeptId()) {
//                criteria.setDeptIds(dataScopeVo.getDeptIds().stream().filter(fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptId())::contains).collect(Collectors.toSet()));
//                criteria.setDeptId(null);
//            }
//            return new ResponseEntity<>(tdPlanImplementService.listAll(criteria, pageable), HttpStatus.OK);
//        }
    }

    @ErrorLog("查询计划实施申请（不分页）")
    @ApiOperation("查询计划实施申请（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('planImplement:list','courseware:list')")
    public ResponseEntity getPlanImplementNoPaging(TdPlanImplementQueryCriteria criteria) {
    return new ResponseEntity<>(tdPlanImplementService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出计划实施申请数据")
    @ApiOperation("导出计划实施申请数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('planImplement:list')")
    public void download(HttpServletResponse response, TdPlanImplementQueryCriteria criteria) throws IOException {
        tdPlanImplementService.download(tdPlanImplementService.listAll(criteria), response);
    }

    @Log("更新整体出勤情况")
    @ApiOperation("更新整体出勤情况")
    @PutMapping(value="/updateOverallAttendanceDescription")
    @PreAuthorize("@el.check('plan:edit')")
    public ResponseEntity updateOverallAttendanceDescription(@RequestBody TdPlanImplement tdPlanImplement) {
        tdPlanImplementService.update(tdPlanImplement);
        return new ResponseEntity(HttpStatus.OK);
    }


//    /**
//     *  @author：liangjw
//     *  @Date: 2021/5/30 16:20
//     *  @Description: 审批结束，更新实施表的同时发送邮件
//     */
//    @ErrorLog("实施审批结束时发送邮件通知")
//    @ApiOperation("实施审批结束时发送邮件通知 ")
//    @PostMapping(value = "/updateImplementAndSendEmail")
//    public ResponseEntity updateImplementAndSendEmail(@RequestBody TdPlanImplement planImplement) {
//        if (null == planImplement.getPlanId() || null == planImplement.getId()) {
//            throw new InfoCheckWarningMessException("参数缺失，培训计划id或计划实施id缺失");
//        } else {
//            // 更新培训计划
//            TdPlan tdPlan = new TdPlan();
//            tdPlan.setId(planImplement.getPlanId());
//            tdPlan.setShowFlag(true);
//            tdPlan.setPlanStatus("实施审批通过");
//            tdPlanService.update(tdPlan);
//            // 更新培训计划实施
//            TdPlanImplement tdPlanImplement = new TdPlanImplement();
//            tdPlanImplement.setId(planImplement.getId());
//            tdPlanImplement.setApprovalStatus("审批通过");
//            tdPlanImplementService.update(tdPlanImplement);
//            // 准备发送邮件
//            tdPlanImplementService.sendEmailWithImplementId(tdPlanImplement.getId());
//            return new ResponseEntity<>("success", HttpStatus.OK);
//        }
//    }
}
