package com.sunten.hrms.pm.rest;

import cn.hutool.json.JSONObject;
import com.sunten.hrms.annotation.AnonymousAccess;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.dao.FndDeptDao;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.pm.domain.PmTrialAssess;
import com.sunten.hrms.pm.dto.PmTrialAssessQueryCriteria;
import com.sunten.hrms.pm.service.PmTrialAssessService;
import com.sunten.hrms.swm.dao.SwmEmployeeDao;
import com.sunten.hrms.swm.domain.SwmEmployee;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ThrowableUtil;
import io.jsonwebtoken.io.IOException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 新晋员工试用期考核表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-05-07
 */
@RestController
@Api(tags = "新晋员工试用期考核表")
@RequestMapping("/api/pm/trialAssess")
public class PmTrialAssessController {
    private static final String ENTITY_NAME = "trialAssess";
    private final PmTrialAssessService pmTrialAssessService;
    private final FndDataScope fndDataScope;
    private final SwmEmployeeDao swmEmployeeDao;
    private final FndDeptDao fndDeptDao;
    private final FndDeptService fndDeptService;

    public PmTrialAssessController(PmTrialAssessService pmTrialAssessService, FndDataScope fndDataScope, SwmEmployeeDao swmEmployeeDao, FndDeptDao fndDeptDao, FndDeptService fndDeptService) {
        this.pmTrialAssessService = pmTrialAssessService;
        this.fndDataScope = fndDataScope;
        this.swmEmployeeDao = swmEmployeeDao;
        this.fndDeptDao = fndDeptDao;
        this.fndDeptService = fndDeptService;
    }

    @Log("新增新晋员工试用期考核表")
    @ApiOperation("新增新晋员工试用期考核表")
    @PostMapping
    @PreAuthorize("@el.check('trialAssess:add')")
    public ResponseEntity create(@Validated @RequestBody PmTrialAssess trialAssess) {
            if (trialAssess.getId() != null) {
                throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
            }
            // 获取管理岗
            List<FndDept> fndDepCharge = fndDeptDao.getDeptByEmpAndAdminJob(trialAssess.getPmEmployee().getId());
            if (fndDepCharge.size() > 0) {
                trialAssess.setAdminFlag(true);
            } else {
                trialAssess.setAdminFlag(false);
            }
            SwmEmployee swmEmployee = swmEmployeeDao.getSwmEmpByEmpId(trialAssess.getPmEmployee().getId());/*!!!!!*/
            if (swmEmployee == null) {
                throw new NullPointerException("该员工暂无工资信息，无法发起试用考核！");
            }
            System.out.println(swmEmployee); /*null*/
            trialAssess.setLumpSumWage(swmEmployee.getLumpSumWage());
            trialAssess.setBaseSalary(swmEmployee.getPostSkillSalary());
            trialAssess.setPerformanceSalary(swmEmployee.getTargetPerformancePay());
            Long leaderBy = fndDeptService.getLeaderPmIdByColleague(trialAssess.getPmEmployee().getId());
            System.out.println("插入时当前直属领导id:" + leaderBy);
            trialAssess.setLeaderBy(leaderBy);
            return new ResponseEntity<>(pmTrialAssessService.insert(trialAssess), HttpStatus.CREATED);
        }


    @Log("删除新晋员工试用期考核表")
    @ApiOperation("删除新晋员工试用期考核表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('trialAssess:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmTrialAssessService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该新晋员工试用期考核表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改新晋员工试用期考核表")
    @ApiOperation("修改新晋员工试用期考核表")
    @PutMapping
    @PreAuthorize("@el.check('trialAssess:edit')")
    public ResponseEntity update(@Validated(PmTrialAssess.Update.class) @RequestBody PmTrialAssess trialAssess) {
        pmTrialAssessService.update(trialAssess);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("员工提交工作总结")
    @ApiOperation("员工提交工作总结")
    @PutMapping("/submitWorkSummary")
    @PreAuthorize("@el.check('trialAssess:edit')")
    public ResponseEntity submitWorkSummary(@Validated(PmTrialAssess.Update.class) @RequestBody PmTrialAssess trialAssess) {
        pmTrialAssessService.submitWorkSummary(trialAssess);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("领导打回工作总结")
    @ApiOperation("领导打回工作总结")
    @PutMapping("/rebackWorkSummary")
    @PreAuthorize("@el.check('trialAssess:leader')")
    public ResponseEntity rebackWorkSummary(@Validated(PmTrialAssess.Update.class) @RequestBody PmTrialAssess trialAssess) {
        pmTrialAssessService.updateByRebackWorkSummary(trialAssess);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个新晋员工试用期考核表")
    @ApiOperation("获取单个新晋员工试用期考核表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('trialAssess:list')")
    public ResponseEntity getTrialAssess(@PathVariable Long id) {
        return new ResponseEntity<>(pmTrialAssessService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询新晋员工试用期考核表（分页）")
    @ApiOperation("查询新晋员工试用期考核表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('trialAssess:list')")
    public ResponseEntity getTrialAssessPage(PmTrialAssessQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptAllId(), null, false, true, criteria.getDeptIds(), null);
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmTrialAssessService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("根据OA申请单号获取试用审批表")
    @ApiOperation("根据OA申请单号获取试用审批表")
    @GetMapping(value = "/getTrialAssessByReqCode")
    @AnonymousAccess
    public ResponseEntity getTrialApprovalByReqCode(String reqCode) {
        return new ResponseEntity<>(pmTrialAssessService.getByReqCode(reqCode), HttpStatus.OK);
    }

    @Log("OA审批时修改试用审批表")
    @ApiOperation("OA审批时修改试用审批表")
    @PostMapping(value = "/updateFromOA")
    public ResponseEntity updateFromOA(@RequestBody PmTrialAssess pmTrialAssess) {
        pmTrialAssessService.writeOaApprovalResult(pmTrialAssess);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
