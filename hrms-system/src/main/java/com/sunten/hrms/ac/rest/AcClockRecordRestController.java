package com.sunten.hrms.ac.rest;

import com.sunten.hrms.ac.dto.AcClockRecordRestQueryCriteria;
import com.sunten.hrms.ac.service.AcClockRecordRestService;
import com.sunten.hrms.fnd.aop.ErrorLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zouyp
 * 	考勤管理中增加‘打卡记录统计页面’，页面显示参考‘考勤记录查询’
 */
@RestController
@Api(tags = "")
@RequestMapping("/api/ac/acClockRecordRest")
public class AcClockRecordRestController {

    @Autowired
    private AcClockRecordRestService acClockRecordRestService;

    private void setCriteria(AcClockRecordRestQueryCriteria criteria) {

    }

    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeClockRecord:list')")
    public ResponseEntity getHrLeavePage(AcClockRecordRestQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
//        System.out.println(criteria);
        setCriteria(criteria);
//        System.out.println("criteria: " + criteria);
//        System.out.println("pageable: " + pageable);
//        System.out.println("pageable: " + pageable.getSort());
//        System.out.println("getSortOr: " + pageable.getSortOr(pageable.getSort()));
        String[] parts = pageable.getSort().toString().split(": ");
        String sorts = parts[0] + " " + parts[1];
        System.out.println(sorts);
        criteria.setSorts(sorts);
        System.out.println("acClockRecordRestService.listAll(criteria, pageable):" + acClockRecordRestService.listAll(criteria, pageable) );
        return new ResponseEntity<>(acClockRecordRestService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("导出打卡记录表数据")
    @ApiOperation("导出打卡记录表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeClockRecord:list')")
    public void download(HttpServletResponse response, AcClockRecordRestQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) throws IOException {
        acClockRecordRestService.download(acClockRecordRestService.downloadListAll(criteria, pageable), response);
    }


}
