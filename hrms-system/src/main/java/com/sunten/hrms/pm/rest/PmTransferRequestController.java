package com.sunten.hrms.pm.rest;

import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmTransferRequestDao;
import com.sunten.hrms.pm.dto.PmTransferRequestDTO;
import com.sunten.hrms.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.PmTransferRequest;
import com.sunten.hrms.pm.dto.PmTransferRequestQueryCriteria;
import com.sunten.hrms.pm.service.PmTransferRequestService;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 岗位调动申请表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-05-24
 */
@RestController
@Api(tags = "岗位调动申请表")
@RequestMapping("/api/pm/transferRequest")
public class PmTransferRequestController {
    private static final String ENTITY_NAME = "transferRequest";
    private final PmTransferRequestService pmTransferRequestService;
    private final FndDataScope fndDataScope;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private PmTransferRequestDao pmTransferRequestDao;

    public PmTransferRequestController(PmTransferRequestService pmTransferRequestService, FndDataScope fndDataScope,PmTransferRequestDao pmTransferRequestDao) {
        this.pmTransferRequestService = pmTransferRequestService;
        this.fndDataScope = fndDataScope;
        this.pmTransferRequestDao = pmTransferRequestDao;
    }

    @Log("新增岗位调动申请表")
    @ApiOperation("新增岗位调动申请表")
    @PostMapping
    @PreAuthorize("@el.check('transferRequest:add')")
    public ResponseEntity create(@Validated @RequestBody PmTransferRequest transferRequest) {
        if (transferRequest.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }

        return new ResponseEntity<>(pmTransferRequestService.insert(transferRequest), HttpStatus.CREATED);
    }

    @Log("删除岗位调动申请表")
    @ApiOperation("删除岗位调动申请表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('transferRequest:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmTransferRequestService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该岗位调动申请表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }


    @Log("修改岗位调动申请表")
    @ApiOperation("修改岗位调动申请表")
    @PutMapping
    @PreAuthorize("@el.check('transferRequest:edit')")
    public ResponseEntity update(@Validated(PmTransferRequest.Update.class) @RequestBody PmTransferRequest transferRequest) {
        pmTransferRequestService.update(transferRequest);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("岗位调动个人签署意见")
    @ApiOperation("岗位调动个人签署意见")
    @PutMapping(value = "/selfVerify")
    @PreAuthorize("@el.check('transferRequest:list')")
    public ResponseEntity updateBySelfVerify(@Validated(PmTransferRequest.Update.class) @RequestBody PmTransferRequest transferRequest) {
        pmTransferRequestService.updateSelfVerify(transferRequest);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个岗位调动申请表")
    @ApiOperation("获取单个岗位调动申请表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('transferRequest:list')")
    public ResponseEntity getTransferRequest(@PathVariable Long id) {
        return new ResponseEntity<>(pmTransferRequestService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询岗位调动申请表（分页）")
    @ApiOperation("查询岗位调动申请表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('transferRequest:list')")
    public ResponseEntity getTransferRequestPage(PmTransferRequestQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptAllId(), null, false, true, null, null);
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            throw new InfoCheckWarningMessException("当前用户没有权限访问，请联系管理员");
        } else {
            logger.debug("==========================" + dataScopeVo.toString());
            if (null != dataScopeVo.getEmployeeId()) {
                criteria.setEmployeeId(dataScopeVo.getEmployeeId());
            } else {
                criteria.setDeptIds(dataScopeVo.getDeptIds());
            }
        }
        return new ResponseEntity<>(pmTransferRequestService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询岗位调动申请表（不分页）")
    @ApiOperation("查询岗位调动申请表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('transferRequest:list')")
    public ResponseEntity getTransferRequestNoPaging(PmTransferRequestQueryCriteria criteria) {
    return new ResponseEntity<>(pmTransferRequestService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出岗位调动申请表数据")
    @ApiOperation("导出岗位调动申请表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('transferRequest:list')")
    public void download(HttpServletResponse response, PmTransferRequestQueryCriteria criteria) throws IOException {
        pmTransferRequestService.download(pmTransferRequestService.listAll(criteria), response);
    }

    @ErrorLog("根据OA申请单号获取调动申请表")
    @ApiOperation("根据OA申请单号获取调动申请表")
    @GetMapping(value = "/getTransferRequestByReqCode")
    public ResponseEntity getTransferRequestByReqCode(String reqCode) {
        return new ResponseEntity<>(pmTransferRequestService.getByReqCode(reqCode), HttpStatus.OK);
    }

    @Log("OA审批时修改试用审批表")
    @ApiOperation("OA审批时修改试用审批表")
    @PostMapping(value = "/updateFromOA")
    public ResponseEntity updateFromOA(@RequestBody PmTransferRequest transferRequest) {
        pmTransferRequestService.writeOaApprovalResult(transferRequest);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("获取随机生成的调动编号")
    @ApiOperation("获取随机生成的调动编号")
    @GetMapping(value = "/getTransferCode/{transferType}")
    public ResponseEntity getTransferCode(@PathVariable String transferType) {
        String transferCode = pmTransferRequestService.getNowTransferCode(transferType);
        return new ResponseEntity<>(transferCode,HttpStatus.OK);
    }

    @Log("修改岗位调动申请表")
    @ApiOperation("修改岗位调动申请表")
    @PutMapping(value = "/updateTeacherContractFlag")
    public ResponseEntity updateTeacherContractFlag(@Validated(PmTransferRequest.Update.class) @RequestBody PmTransferRequest transferRequest) {
        pmTransferRequestDao.updateTeacherContractFlag(transferRequest);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("修改调动申请表提交状态")
    @ApiOperation("修改调动申请表提交状态")
    @PutMapping(value = "/updateSubmitFlag")
    public ResponseEntity updateSubmitFlag(@Validated(PmTransferRequest.Update.class) @RequestBody Long id) {
        pmTransferRequestDao.updateSubmitFlag(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("根据用户ID获取列表调动信息")
    @ApiOperation("根据用户ID获取列表调动信息")
    @GetMapping(value = "/getByPmEmployeeId/{pmEmployeeId}")
    public ResponseEntity getByPmEmployeeId(@PathVariable String pmEmployeeId) {
        PmTransferRequest pmTransferRequest = pmTransferRequestDao.getByPmEmployeeId(pmEmployeeId);
        return new ResponseEntity<>(pmTransferRequest,HttpStatus.OK);
    }
}
