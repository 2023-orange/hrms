package com.sunten.hrms.td.rest;

import cn.hutool.json.JSONArray;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.td.domain.*;
import com.sunten.hrms.td.dto.*;
import com.sunten.hrms.td.mapper.TdPlanMapper;
import com.sunten.hrms.td.service.*;
import com.sunten.hrms.utils.ListUtils;
import com.sunten.hrms.utils.SecurityUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * <p>
 * 培训计划表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-05-19
 */
@RestController
@Api(tags = "培训计划表")
@RequestMapping("/api/td/plan")
public class TdPlanController {
    private static final String ENTITY_NAME = "plan";
    private final TdPlanService tdPlanService;
    private final TdPlanImplementService tdPlanImplementService;
    private final TdPlanEmployeeService tdPlanEmployeeService;
    private final TdPlanMapper tdPlanMapper;
    private final TdPlanChangeHistoryService tdPlanChangeHistoryService;
    private final FndUserService fndUserService;
    private final TdPlanImplementDeptService tdPlanImplementDeptService;
    private final JwtPermissionService jwtPermissionService;
    private final FndDeptService fndDeptService;
    private final FndDataScope fndDataScope;
    @Value("${role.authTrainCharge}")
    private String authTrainCharge;

    public TdPlanController(TdPlanService tdPlanService, TdPlanImplementService tdPlanImplementService, TdPlanEmployeeService tdPlanEmployeeService,
                            TdPlanMapper tdPlanMapper, TdPlanChangeHistoryService tdPlanChangeHistoryService, FndUserService fndUserService,
                            TdPlanImplementDeptService tdPlanImplementDeptService, JwtPermissionService jwtPermissionService,
                            FndDeptService fndDeptService, FndDataScope fndDataScope) {
        this.tdPlanService = tdPlanService;
        this.tdPlanImplementService = tdPlanImplementService;
        this.tdPlanEmployeeService = tdPlanEmployeeService;
        this.tdPlanMapper = tdPlanMapper;
        this.tdPlanChangeHistoryService = tdPlanChangeHistoryService;
        this.fndUserService = fndUserService;
        this.tdPlanImplementDeptService = tdPlanImplementDeptService;
        this.jwtPermissionService = jwtPermissionService;
        this.fndDeptService = fndDeptService;
        this.fndDataScope = fndDataScope;
    }

    @Log("新增培训计划表")
    @ApiOperation("新增培训计划表")
    @PostMapping
    @PreAuthorize("@el.check('plan:add')")
    public ResponseEntity create(@Validated @RequestBody TdPlan plan) {
        if (plan.getId()  != -1) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdPlanService.insert(plan), HttpStatus.CREATED);
    }

    @Log("删除培训计划表")
    @ApiOperation("删除培训计划表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('plan:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdPlanService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该培训计划表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改培训计划表")
    @ApiOperation("修改培训计划表")
    @PutMapping
    @PreAuthorize("@el.check('plan:edit')")
    public ResponseEntity update(@Validated(TdPlan.Update.class) @RequestBody TdPlan plan) {
        tdPlanService.update(plan);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个培训计划表")
    @ApiOperation("获取单个培训计划表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('plan:list')")
    public ResponseEntity getPlan(@PathVariable Long id) {
        return new ResponseEntity<>(tdPlanService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询培训计划表（分页）")
    @ApiOperation("查询培训计划表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('plan:list')")
    public ResponseEntity getPlanPage(TdPlanQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {

        // 按权限范围查询
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority authority : grantedAuthorityCollection
        ) {
            if (authority.getAuthority().equals(authTrainCharge)) {
                return new ResponseEntity<>(tdPlanService.listAll(criteria, pageable), HttpStatus.OK);
            }
        }
        if (null == user.getEmployee()) {
            throw new InfoCheckWarningMessException("该用户没有绑定员工");
        } else {
            // 取交集
            criteria.setDeptChargeId(user.getEmployee().getId());
            criteria.setPlanChargeId(user.getEmployee().getId());
            return new ResponseEntity<>(tdPlanService.listAll(criteria, pageable), HttpStatus.OK);
        }
    }

    @ErrorLog("查询培训计划表（不分页）")
    @ApiOperation("查询培训计划表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('plan:list')")
    public ResponseEntity getPlanNoPaging(TdPlanQueryCriteria criteria) {
    return new ResponseEntity<>(tdPlanService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("查询安全培训列表（分页）")
    @ApiOperation("查询安全培训列表（分页）")
    @GetMapping("/getSafetyTrainingByPage")
    @PreAuthorize("@el.check('safetyTraining:list')")
    public ResponseEntity getSafetyTrainingByPage(TdPlanQueryCriteria criteria, @PageableDefault(value= 9999, sort = {"id"}, direction = Sort.Direction.ASC)Pageable pageable) {
        return new ResponseEntity<>(tdPlanService.listSafetyTrainingByCriteriaPage(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询安全培训列表（不分页）")
    @ApiOperation("查询安全培训列表（不分页）")
    @GetMapping("/getSafetyTrainingNoPage")
    @PreAuthorize("@el.check('safetyTraining:list')")
    public ResponseEntity getSafetyTrainingNoPage(TdPlanQueryCriteria criteria) {
        return new ResponseEntity<>(tdPlanService.listSafetyTrainingByCriteria(criteria),HttpStatus.OK);
    }

    @Log("新增安全培训")
    @ApiOperation("新增安全培训")
    @PostMapping("/addSafetyTraining")
    @PreAuthorize("@el.check('safetyTraining:add')")
    public void addSafetyTraining(TdSafetyTraining tdSafetyTraining) {
        tdPlanService.insertSafetyTraining(tdSafetyTraining);
    }

    @Log("更新安全培训")
    @ApiOperation("更新安全培训")
    @PutMapping("/updateSafetyTraining")
    @PreAuthorize("@el.check('safetyTraining:update')")
    public void updateSafetyTraining(TdSafetyTraining tdSafetyTraining) {
        tdPlanService.updateSafetyTraining(tdSafetyTraining);
    }

    @ErrorLog("安全培训部门架构")
    @ApiOperation("安全培训部门架构")
    @GetMapping("/getSafetTrainingDept")
    @PreAuthorize("@el.check('safetyTraining:list')")
    public ResponseEntity getSafetTrainingDept() {
        return new ResponseEntity<>(tdPlanService.getSafetyTrainingDeptVo(), HttpStatus.OK);
    }


    @ErrorLog("导出培训计划表数据")
    @ApiOperation("导出培训计划表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('plan:list')")
    public void download(HttpServletResponse response, TdPlanQueryCriteria criteria) throws IOException {
        tdPlanService.download(tdPlanService.listAll(criteria), response);
    }

    @ErrorLog("根据培训id获取Plan及planImplement")
    @ApiOperation("据培训id获取Plan及planImplement")
    @GetMapping(value = "/getPlanAndPlanImplement/{id}")
    @PreAuthorize("@el.check('plan:list')")
    public ResponseEntity getPlanAndPlanImplement(@PathVariable Long id) {
        return new ResponseEntity<>(tdPlanService.getPlanAndImpementByPlanId(id), HttpStatus.OK);
    }

    @ErrorLog("根据实施的OA单号获取完整的培训、培训实施、参训人员")
    @ApiOperation("根据实施的OA单号获取完整的培训、培训实施、参训人员")
    @GetMapping(value = "/getPlanAndImpementByOaOrder")
    public ResponseEntity getPlanAndImpementByOaOrder(String oaOrder) {
        return new ResponseEntity<>(tdPlanService.getPlanAndImpementByOaOrder(oaOrder), HttpStatus.OK);
    }



    @Log("插入实施及对应的参训人员以及参训讲师")
    @ApiOperation("插入实施及对应的参训人员以及参训讲师")
    @PostMapping(value = "/insertPlanAndImplementAndOther")
    @PreAuthorize("@el.check('planImplement:add')")
    public ResponseEntity insertPlanAndImplementAndOther(@RequestBody TdPlan plan) {
        return new ResponseEntity<>(tdPlanService.insertPlanAndImplementAndOther(plan), HttpStatus.OK);
    }

    @ErrorLog("根据变更OA单号获取培训信息")
    @ApiOperation("根据变更OA单号获取培训信息")
    @GetMapping(value = "/getPlanByChangeOaOrder")
    public ResponseEntity getPlanByChangeOaOrder(String changeOaOrder) {
        return new ResponseEntity<>(tdPlanService.getPlanByChangeOaOrder(changeOaOrder), HttpStatus.OK);
    }

//    @ErrorLog("实施申请通过后, 将plan的showFlag设置true")
//    @ApiOperation("实施申请通过后, 将plan的showFlag设置true")
//    @PutMapping(value = "/updateShowFlagAfterImplementPass")
//    public ResponseEntity updateShowFlagAfterImplementPass(@RequestBody TdPlan plan) {
//        if (null == plan || null == plan.getId() || null == plan.getShowFlag()) {
//            throw new InfoCheckWarningMessException("培训数据传输有误，请联系管理员");
//        } else {
//            if (!plan.getShowFlag()) {
//                tdPlanService.updateShowFlagAfterImplementPass(plan.getId());
//            }
//            return new ResponseEntity<>("success", HttpStatus.OK);
//        }
//    }

    /**
     *  @author：liangjw
     *  @Date: 2021/6/8 15:15
     *  @Description:
     *  主要更新changeCurrentNode、changeCurrentPerson，
     *  培训取消审批通过时，将planStatus更新为已取消；培训取消审批不通过时，将planStatus更新为等待实施
     *  培训变更审批通过或不通过时，将planStatus更新为等待实施
     */
    @Log("OA更新使用TdPlan使用的接口")
    @ApiOperation("OA更新使用TdPlan使用的接口")
    @PutMapping(value = "/updateWithNoAuth")
    public ResponseEntity updateWithNoAuth(@RequestBody TdPlan tdPlan) {
        if (null == tdPlan || null == tdPlan.getId()) {
            throw new InfoCheckWarningMessException("id不能为空");
        } else {
            tdPlanService.update(tdPlan);
            return new ResponseEntity<>("success", HttpStatus.OK);
        }
    }

}
