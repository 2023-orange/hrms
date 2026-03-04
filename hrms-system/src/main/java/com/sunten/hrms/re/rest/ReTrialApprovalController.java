package com.sunten.hrms.re.rest;

import com.sunten.hrms.annotation.AnonymousAccess;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.utils.PageUtil;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.re.domain.ReTrialApproval;
import com.sunten.hrms.re.dto.ReTrialApprovalQueryCriteria;
import com.sunten.hrms.re.service.ReTrialApprovalService;
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

/**
 * <p>
 * 试用审批表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-04-25
 */
@RestController
@Api(tags = "试用审批表")
@RequestMapping("/api/re/trialApproval")
public class ReTrialApprovalController {
    private static final String ENTITY_NAME = "trialApproval";
    private final ReTrialApprovalService reTrialApprovalService;
    private final FndDataScope fndDataScope;

    public ReTrialApprovalController(ReTrialApprovalService reTrialApprovalService, FndDataScope fndDataScope) {
        this.reTrialApprovalService = reTrialApprovalService;
        this.fndDataScope = fndDataScope;
    }

    @Log("新增试用审批表")
    @ApiOperation("新增试用审批表")
    @PostMapping
    @PreAuthorize("@el.check('trialApproval:add')")
    public ResponseEntity create(@Validated @RequestBody ReTrialApproval trialApproval) {
        if (trialApproval.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(reTrialApprovalService.insert(trialApproval), HttpStatus.CREATED);
    }

    @Log("删除试用审批表")
    @ApiOperation("删除试用审批表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('trialApproval:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reTrialApprovalService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该试用审批表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改试用审批表")
    @ApiOperation("修改试用审批表")
    @PutMapping
    @PreAuthorize("@el.check('trialApproval:edit')")
    public ResponseEntity update(@Validated(ReTrialApproval.Update.class) @RequestBody ReTrialApproval trialApproval) {
        reTrialApprovalService.update(trialApproval);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("保存流转记录")
    @ApiOperation("保存流转记录")
    @PutMapping("/saveTrialAfterPass")
    @PreAuthorize("@el.check('trialApproval:edit')")
    public ResponseEntity saveTrialAfterPass(@RequestBody ReTrialApproval trialApproval) {
        reTrialApprovalService.updateTrialAfterPass(trialApproval);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个试用审批表")
    @ApiOperation("获取单个试用审批表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('trialApproval:list')")
    public ResponseEntity getTrialApproval(@PathVariable Long id) {
        return new ResponseEntity<>(reTrialApprovalService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询试用审批表（分页）")
    @ApiOperation("查询试用审批表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('trialApproval:list')")
    public ResponseEntity getTrialApprovalPage(ReTrialApprovalQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptAllId(), null, false, true, criteria.getDeptIds(), null);
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        return new ResponseEntity<>(reTrialApprovalService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询试用审批表（不分页）")
    @ApiOperation("查询试用审批表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('trialApproval:list')")
    public ResponseEntity getTrialApprovalNoPaging(ReTrialApprovalQueryCriteria criteria) {
    return new ResponseEntity<>(reTrialApprovalService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出试用审批表数据")
    @ApiOperation("导出试用审批表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('trialApproval:list')")
    public void download(HttpServletResponse response, ReTrialApprovalQueryCriteria criteria) throws IOException {
        reTrialApprovalService.download(reTrialApprovalService.listAll(criteria), response);
    }

    @Log("撤销试用审批")
    @ApiOperation("撤销试用审批")
    @PutMapping(value = "/repealTrialApproval")
    @PreAuthorize("@el.check('trialApproval:edit')")
    public ResponseEntity repealTrialApproval(@Validated(ReTrialApproval.Update.class) @RequestBody ReTrialApproval trialApproval) {
        reTrialApprovalService.repealTrialApproval(trialApproval);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @ErrorLog("根据OA申请单号获取试用审批表")
    @ApiOperation("根据OA申请单号获取试用审批表")
    @GetMapping(value = "/getTrialApprovalByReqCode")
    @AnonymousAccess
    public ResponseEntity getTrialApprovalByReqCode(String reqCode) {
        return new ResponseEntity<>(reTrialApprovalService.getByReqCode(reqCode), HttpStatus.OK);
    }

    @Log("OA审批时修改试用审批表")
    @ApiOperation("OA审批时修改试用审批表")
    @PostMapping(value = "/updateFromOA")
    public ResponseEntity updateFromOA(@RequestBody ReTrialApproval trialApproval) {
        reTrialApprovalService.writeOaApprovalResult(trialApproval);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
