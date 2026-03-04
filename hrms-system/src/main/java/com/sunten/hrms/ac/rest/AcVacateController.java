package com.sunten.hrms.ac.rest;

import com.sunten.hrms.ac.dto.AcVacateQueryCriteria;
import com.sunten.hrms.ac.service.AcVacateService;
import com.sunten.hrms.annotation.AnonymousAccess;
import com.sunten.hrms.fnd.aop.ErrorLog;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *  用于获取OA系统的相关数据
 * @atuthor xukai
 * @date 2020/10/15 14:05
 */
@RestController
@RequestMapping("/api/ac/vacate")
public class AcVacateController {
    private static final String ENTITY_NAME = "vacateController";
    private final AcVacateService acVacateService;

    public AcVacateController(AcVacateService acVacateService){
        this.acVacateService = acVacateService;
    }

    @ErrorLog("获取单个OA请假单")
    @GetMapping(value = "/{reqCode}")
    @AnonymousAccess
    public ResponseEntity getRegimeRelation(@PathVariable String reqCode) {
        return new ResponseEntity<>(acVacateService.getVacateByRequisitionCode(reqCode), HttpStatus.OK);
    }

    @ErrorLog("根据条件获取OA请假单")
    @GetMapping(value = "/getVacateByReqCodeAndDate")
    @AnonymousAccess
    public ResponseEntity getVacateByReqCodeAndDate(AcVacateQueryCriteria criteria) {
        return new ResponseEntity<>(acVacateService.getVacateByReqCodeAndDate(criteria), HttpStatus.OK);
    }

    @ErrorLog("根据条件获取OA请假单")
    @GetMapping(value = "/getVacateByReqCode")
    @AnonymousAccess
    public ResponseEntity getVacateByReqCode(AcVacateQueryCriteria criteria) {
        return new ResponseEntity<>(acVacateService.getVacateByReqCodeAndWorkcard(criteria), HttpStatus.OK);
    }
}
