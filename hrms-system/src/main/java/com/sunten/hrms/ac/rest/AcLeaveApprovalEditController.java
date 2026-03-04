package com.sunten.hrms.ac.rest;

import com.sunten.hrms.ac.domain.HrUpdateLeaveInfoForm;
import com.sunten.hrms.ac.dto.AcLeaveApplicationQueryCriteria;
import com.sunten.hrms.ac.service.AcLeaveApplicationService;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author Zouyp
 */
@RestController
@Api(tags = "请假申请")
@RequestMapping("/api/ac/hrLeaveEdit")
public class AcLeaveApprovalEditController {
    @Autowired
    private AcLeaveApplicationService acLeaveApplicationService;

    @Autowired
    private FndUserService fndUserService;

    private void setCriteria(AcLeaveApplicationQueryCriteria criteria) {

    }
    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping
    @PreAuthorize("@el.check('leaveApprovalEdit:list')")
    public ResponseEntity getHrLeavePage(AcLeaveApplicationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable, @RequestParam("reqStatusValue") String reqStatus) {
        System.out.println(criteria);
        setCriteria(criteria);
        criteria.setReqStatus(reqStatus);
        criteria.setApprovalResult("Pass");
        System.out.println("criteria: " + criteria);
        return new ResponseEntity<>(acLeaveApplicationService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("人资修改请假条信息")
    @ApiOperation("人资修改请假条信息")
    @PostMapping("/hrUpdateLeaveInfo")
    public ResponseEntity hrUpdateLeaveInfo(@RequestBody HrUpdateLeaveInfoForm hrUpdateLeaveInfo) {
        Integer res = acLeaveApplicationService.updateHrChangeLeaveInfo(hrUpdateLeaveInfo);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @ErrorLog("获取人资修改后的请假信息")
    @ApiOperation("获取人资修改后的请假信息")
    @GetMapping("/getHrChangeLeaveInfo")
    public ResponseEntity getHrChangeLeaveInfo(@RequestParam("checkOaOrder") String oaOrder) {
        HashMap<String, Object> resMap = new HashMap<String, Object>(16);
        resMap = acLeaveApplicationService.getHrChangeLeaveInfo(oaOrder);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    @ErrorLog("逻辑删除人资修改后的子表信息")
    @ApiOperation("逻辑删除人资修改后的子表信息")
    @DeleteMapping("/deleteHrLeaveEditInfo")
    public ResponseEntity deleteHrLeaveEditInfo(@RequestParam("oaOrder") String oaOrder, @RequestParam("version") Integer version) {
        return new ResponseEntity<>(acLeaveApplicationService.updateHrLeaveEditInfo(oaOrder, version), HttpStatus.OK);
    }
}

