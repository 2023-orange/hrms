package com.sunten.hrms.re.rest;

import com.sunten.hrms.re.dto.QueryUsedByTrialApprovalCriteria;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.re.domain.ReDemandJob;
import com.sunten.hrms.re.dto.ReDemandJobQueryCriteria;
import com.sunten.hrms.re.service.ReDemandJobService;
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
import java.util.List;

/**
 * <p>
 * 用人需求岗位子表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-04-23
 */
@RestController
@Api(tags = "用人需求岗位子表")
@RequestMapping("/api/re/demandJob")
public class ReDemandJobController {
    private static final String ENTITY_NAME = "demandJob";
    private final ReDemandJobService reDemandJobService;

    public ReDemandJobController(ReDemandJobService reDemandJobService) {
        this.reDemandJobService = reDemandJobService;
    }

    @Log("新增用人需求岗位子表")
    @ApiOperation("新增用人需求岗位子表")
    @PostMapping
    @PreAuthorize("@el.check('demandJob:add')")
    public ResponseEntity create(@Validated @RequestBody ReDemandJob demandJob) {
        if (demandJob.getId()  != -1L) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an unExcept ID");
        }
        return new ResponseEntity<>(reDemandJobService.insert(demandJob), HttpStatus.CREATED);
    }

    @Log("删除用人需求岗位子表")
    @ApiOperation("删除用人需求岗位子表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('demandJob:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reDemandJobService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该用人需求岗位子表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }


    @Log("失效用人需求")
    @ApiOperation("失效用人需求")
    @PutMapping(value = "/reBack/{id}")
    @PreAuthorize("@el.check('demand:edit')")
    public ResponseEntity reBack(@PathVariable Long id) {
        reDemandJobService.disabledByEnabled(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改用人需求岗位子表")
    @ApiOperation("修改用人需求岗位子表")
    @PutMapping
    @PreAuthorize("@el.check('demandJob:edit')")
    public ResponseEntity update(@Validated(ReDemandJob.Update.class) @RequestBody ReDemandJob demandJob) {
        reDemandJobService.update(demandJob);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个用人需求岗位子表")
    @ApiOperation("获取单个用人需求岗位子表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('demandJob:list')")
    public ResponseEntity getDemandJob(@PathVariable Long id) {
        return new ResponseEntity<>(reDemandJobService.getByKey(id), HttpStatus.OK);
    }


    @ErrorLog("查询用人需求岗位子表（分页）")
    @ApiOperation("查询用人需求岗位子表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('demandJob:list')")
    public ResponseEntity getDemandJobPage(ReDemandJobQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reDemandJobService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询用人需求岗位子表（不分页）")
    @ApiOperation("查询用人需求岗位子表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('demand:list')")
    public ResponseEntity getDemandJobNoPaging(ReDemandJobQueryCriteria criteria) {
    return new ResponseEntity<>(reDemandJobService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出用人需求岗位子表数据")
    @ApiOperation("导出用人需求岗位子表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('demandJob:list')")
    public void download(HttpServletResponse response, ReDemandJobQueryCriteria criteria) throws IOException {
        reDemandJobService.download(reDemandJobService.listAll(criteria), response);
    }

    @ErrorLog("根据列名进行更新")
    @ApiOperation("根据列名进行更新")
    @PutMapping(value = "/updateColumnByValue")
    public ResponseEntity updateColumnByValue(@RequestBody ReDemandJob demandJob) {
        if (null == demandJob.getId()) {
            throw new BadRequestException("该岗位需求没有ID，无效的请求");
        } else {
            reDemandJobService.updateColumnByValue(demandJob);
            return new ResponseEntity<>("200", HttpStatus.OK);
        }
    }

    @ErrorLog("试用审批专用的需求查询")
    @ApiOperation("试用审批专用的需求查询")
    @GetMapping(value = "/queryUsedByTrialApproval")
    public ResponseEntity queryUsedByTrialApproval(QueryUsedByTrialApprovalCriteria queryUsedByTrialApprovalCriteria) {
        return new ResponseEntity<>(reDemandJobService.queryUsedByTrialApproval(queryUsedByTrialApprovalCriteria), HttpStatus.OK);
    }

    @Log("更新用人岗位需求的在用数量，申请提交时调用")
    @ApiOperation("其它关联模块申请单生成时调用，申请提交时调用")
    @PutMapping(value = "/updateInUsedQuantityAfterUsed/{id}")
    public void updateInUsedQuantityAfterUsed(@PathVariable Long id) {
        reDemandJobService.updateInUsedQuantityAfterUsed(id);
    }

    @Log("更新用人岗位需求的通过数量")
    @ApiOperation("更新用人岗位需求的通过数量")
    @PutMapping(value = "/updatePassQuantityAfterUsed/{id}")
    public void updatePassQuantityAfterUsed(@PathVariable Long id) {
        reDemandJobService.updatePassQuantityAfterUsed(id);
    }

    @Log("更新用人岗位需求的在用数量，申请不通过时调用")
    @ApiOperation("更新用人岗位需求的在用数量，申请不通过时调用")
    @PutMapping(value = "/updateInUsedQuantityAfterCancel/{id}")
    public void updateInUsedQuantityAfterCancel(@PathVariable Long id) {
        reDemandJobService.updateInUsedQuantityAfterCancel(id);
    }

    @Log("开放用人需求")
    @ApiOperation("开放用人需求")
    @PutMapping(value = "/updatePassQuantityByCharge")
    @PreAuthorize("@el.check('reCharge')")
    public void updatePassQuantityByCharge(@RequestBody List<ReDemandJob> reDemandJobs) {
        reDemandJobService.updatePassQuantityByCharge(reDemandJobs);
    }

    @ErrorLog("试用审批提交前检测剩余岗位人数需求数量")
    @ApiOperation("试用审批提交前检测剩余岗位人数需求数量")
    @GetMapping(value = "/checkResidueQuantityBeforeTrialCommit/{demandJobId}")
    @PreAuthorize("@el.check('trialApproval:add')")
    public ResponseEntity checkResidueQuantityBeforeTrialCommit(@PathVariable Long demandJobId) {
        return new ResponseEntity<>(reDemandJobService.checkResidueQuantityBeforeTrialCommit(demandJobId), HttpStatus.OK);
    }

}
