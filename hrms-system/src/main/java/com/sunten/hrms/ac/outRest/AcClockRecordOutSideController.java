package com.sunten.hrms.ac.outRest;

import com.sunten.hrms.ac.domain.AcClockRecord;
import com.sunten.hrms.ac.domain.KqCrjsj;
import com.sunten.hrms.ac.dto.AcClockRecordQueryCriteria;
import com.sunten.hrms.ac.rest.AcClockRecordController;
import com.sunten.hrms.ac.service.AcClockRecordService;
import com.sunten.hrms.ac.service.KqCrjsjService;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.utils.SecurityUtils;
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

@RestController
@Api(tags = "外部访问打卡记录表")
@RequestMapping("outSide/api/ac/clockRecord")
public class AcClockRecordOutSideController {
    private final AcClockRecordService acClockRecordService;
    private final KqCrjsjService kqCrjsjService;
    private final FndUserService fndUserService;

    public AcClockRecordOutSideController(AcClockRecordService acClockRecordService,KqCrjsjService kqCrjsjService, FndUserService fndUserService) {
        this.acClockRecordService = acClockRecordService;
        this.kqCrjsjService = kqCrjsjService;
        this.fndUserService = fndUserService;
    }

    @Log("外部新增打卡记录表")
    @ApiOperation("新增打卡记录表")
    @PostMapping
    @PreAuthorize("@el.check('acClockRecord:add')")
    public ResponseEntity create(@RequestBody KqCrjsj kqCrjsj) {
        System.out.println(kqCrjsj.toString());
        return new ResponseEntity<>(kqCrjsjService.insert(kqCrjsj), HttpStatus.CREATED);
    }

    @Log("外部编辑打卡记录")
    @ApiOperation("编辑打卡记录")
    @PostMapping("/updateCard")
    @PreAuthorize("@el.check('acClockRecord:edit')")
    public ResponseEntity update(@RequestBody AcClockRecord clockRecord) {
        acClockRecordService.updateClockForOutRest(clockRecord);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("查询个人打卡记录表（分页）")
    @ApiOperation("查询个人打卡记录表（分页）")
    @GetMapping(value = "/self")
    @PreAuthorize("@el.check('clockRecord:list')")
    public ResponseEntity getSelfClockRecordPage(AcClockRecordQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return AcClockRecordController.setCriteriaForSelfClock(criteria, pageable, fndUserService, acClockRecordService);
    }



}
