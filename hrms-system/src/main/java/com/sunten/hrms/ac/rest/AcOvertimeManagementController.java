package com.sunten.hrms.ac.rest;

import com.sunten.hrms.ac.dto.AcOvertimeApplicationQueryCriteria;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.AcOvertimeMangement;
import com.sunten.hrms.ac.dto.AcOvertimeManagementQueryCriteria;
import com.sunten.hrms.ac.service.AcOvertimeMangementService;
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
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zouyp
 * @since 2023-10-16
 */
@RestController
@Api(tags = "")
@RequestMapping("/api/ac/overtimeManagement")
public class AcOvertimeManagementController {
    private static final String ENTITY_NAME = "overtimeManagement";
    private final AcOvertimeMangementService acOvertimeMangementService;
    @Autowired
    private FndUserService fndUserService;
    public AcOvertimeManagementController(AcOvertimeMangementService acOvertimeMangementService) {
        this.acOvertimeMangementService = acOvertimeMangementService;
    }

    @GetMapping("/getBackUpAndCheckList")
    @ErrorLog("主管及经理类查询每月加班工时备份查询页")
    @ApiOperation("主管及经理类查询每月加班工时备份查询页")
    public ResponseEntity getBackUpAndCheckList(@RequestParam(value = "deptId", required = false) Integer deptId,
                                                        @RequestParam(value = "beginDate", required = false) String beginDate,
                                                        @RequestParam(value = "endDate", required = false) String endDate) {
        List<HashMap<String, Object>> resList = null;
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
//        if ("".equals(deptName) || deptName == null) {
//            deptName = "%%";
//        }
        if (beginDate == null || "".equals(beginDate)) {
            resList = acOvertimeMangementService.getBackUpAndCheckListByDeptName(deptId, workCard);
        } else {
            resList = acOvertimeMangementService.getBackUpAndCheckListByDate(deptId, beginDate,endDate,workCard);
        }
        return new ResponseEntity<>(resList, HttpStatus.OK);
    }

    @GetMapping("/getBackOvertimeManagementList")
    @ErrorLog("每月加班工时备份查询页")
    @ApiOperation("每月加班工时备份查询页")
    public ResponseEntity getBackOvertimeManagementList(@RequestParam(value = "deptId", required = false) Integer deptId,
                                                        @RequestParam(value = "beginDate", required = false) String beginDate,
                                                        @RequestParam(value = "endDate", required = false) String endDate) {
        List<HashMap<String, Object>> resList = null;
//        if ("".equals(deptName) || deptName == null) {
//            deptName = "%%";
//        }
        if (beginDate == null || "".equals(beginDate)) {
            resList = acOvertimeMangementService.getBackOvertimeManagementListByDeptName(deptId);
        } else {
            resList = acOvertimeMangementService.acOvertimeManagementServiceByDate(deptId, beginDate,endDate);
        }
        return new ResponseEntity<>(resList, HttpStatus.OK);
    }
    private void setCriteria(AcOvertimeManagementQueryCriteria criteria) {

    }
//    @GetMapping("/getBackOvertimeManagementList")
//    @ErrorLog("每月加班工时备份查询页")
//    @ApiOperation("每月加班工时备份查询页")
//    public ResponseEntity getBackOvertimeManagementList(AcOvertimeManagementQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
//        setCriteria(criteria);
//        return new ResponseEntity<>(acOvertimeMangementService.getBackOvertimeManagementListNew(criteria,pageable), HttpStatus.OK);
//    }

    @GetMapping("/getAndCheckList")
    @ErrorLog("主管经理级别获取自己管理的科室列表")
    @ApiOperation("主管经理级别获取自己管理的科室列表")
    public ResponseEntity getAndCheckList() {
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        List<HashMap<String, Object>> resList = acOvertimeMangementService.getAndCheckList(workCard);
        return new ResponseEntity<>(resList, HttpStatus.OK);
    }

    @GetMapping("/checkIsRole")
    @ErrorLog("检查当前登录人员是那种类型")
    @ApiOperation("检查当前登录人员是那种类型")
    public ResponseEntity checkIsRole() {
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        HashMap<String, Object> res = acOvertimeMangementService.checkIsRole(workCard);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @GetMapping("/checkList")
    @ErrorLog("获取加班时数管理列表")
    @ApiOperation("获取加班时数管理列表")
    public ResponseEntity checkList(@RequestParam("deptId") Integer deptId) {
        List<HashMap<String, Object>> resList = acOvertimeMangementService.checkList(deptId);
        return new ResponseEntity<>(resList, HttpStatus.OK);
    }

    @PostMapping("/addOvertimeManagementDetail")
    @ErrorLog("添加请假审批部门审批人员是否生效")
    @ApiOperation("添加请假审批部门审批人员是否生效")
    public ResponseEntity addOvertimeManagementDetail(@RequestParam("dept_id") Integer dept_id,
                                                      @RequestParam("dept_name") String dept_name,
                                                      @RequestParam("total_number") Integer total_number,
                                                      @RequestParam("average_overtime_hour") Float average_overtime_hour,
                                                      @RequestParam("system_limit_hour") Float system_limit_hour) {
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String createBy = fndUserDTO.getEmployee().getWorkCard();
        Integer res = acOvertimeMangementService.addOvertimeManagementDetail(dept_id, dept_name, total_number, average_overtime_hour, system_limit_hour, createBy);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @PutMapping("/updateOvertimeManagementDetail")
    @ErrorLog("更新请假审批部门审批人员是否生效")
    @ApiOperation("更新请假审批部门审批人员是否生效")
    public ResponseEntity updateOvertimeManagementDetail(@RequestParam("id") Integer id,
                                                         @RequestParam("dept_id") Integer deptId,
                                                         @RequestParam("total_number") Integer total_number,
                                                         @RequestParam("average_overtime_hour") Float average_overtime_hour,
                                                         @RequestParam("system_limit_hour") Float system_limit_hour) {
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String updateBy = fndUserDTO.getEmployee().getWorkCard();
        System.out.println("id: " + id);
        System.out.println("deptId: " + deptId);
        System.out.println("total_number: " + total_number);
        System.out.println("average_overtime_hour: " + average_overtime_hour);
        System.out.println("system_limit_hour: " + system_limit_hour);
        acOvertimeMangementService.updateOvertimeManagementDetail(id, deptId, total_number, average_overtime_hour, system_limit_hour,updateBy);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PutMapping("/changeAcOvertimeManagementStatus")
    @ErrorLog("更新请假审批部门审批人员是否生效")
    @ApiOperation("更新请假审批部门审批人员是否生效")
    public ResponseEntity changeAcOvertimeManagementStatus(@RequestParam("id") Integer id,
                                                           @RequestParam("deptId") Integer deptId,
                                                           @RequestParam("enabled_flag") Boolean enabled_flag) {
        if (enabled_flag) {
            acOvertimeMangementService.changeAcApprovalDepartmentStatus(id, deptId, 1);
        } else {
            acOvertimeMangementService.changeAcApprovalDepartmentStatus(id, deptId, 0);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/getOvertimeManagementDetail")
    @ErrorLog("获取加班时数管理具体详情信息")
    @ApiOperation("获取加班时数管理具体详情信息")
    public ResponseEntity getOvertimeManagementDetail(@RequestParam("id") Integer id, @RequestParam("dept_id") Integer dept_id) {
        List<HashMap<String, Object>> resList = acOvertimeMangementService.getOvertimeManagementDetail(id, dept_id);
        return new ResponseEntity<>(resList, HttpStatus.OK);
    }

    @GetMapping("/getOvertimeManagementList")
    @ErrorLog("获取加班时数管理列表")
    @ApiOperation("获取加班时数管理列表")
    public ResponseEntity getOvertimeManagementList() {
        List<HashMap<String, Object>> resList = acOvertimeMangementService.getOvertimeManagementList();
        return new ResponseEntity<>(resList, HttpStatus.OK);
    }

    @Log("新增")
    @ApiOperation("新增")
    @PostMapping
    @PreAuthorize("@el.check('overtimeMangement:add')")
    public ResponseEntity create(@Validated @RequestBody AcOvertimeMangement overtimeManagement) {
        return new ResponseEntity<>(acOvertimeMangementService.insert(overtimeManagement), HttpStatus.CREATED);
    }

    @Log("删除")
    @ApiOperation("删除")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('overtimeMangement:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acOvertimeMangementService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改")
    @ApiOperation("修改")
    @PutMapping
    @PreAuthorize("@el.check('overtimeMangement:edit')")
    public ResponseEntity update(@Validated(AcOvertimeMangement.Update.class) @RequestBody AcOvertimeMangement overtimeMangement) {
        acOvertimeMangementService.update(overtimeMangement);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个")
    @ApiOperation("获取单个")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('overtimeMangement:list')")
    public ResponseEntity getOvertimeMangement(@PathVariable Long id) {
        return new ResponseEntity<>(acOvertimeMangementService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping
    @PreAuthorize("@el.check('overtimeMangement:list')")
    public ResponseEntity getOvertimeMangementPage(AcOvertimeManagementQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(acOvertimeMangementService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询（不分页）")
    @ApiOperation("查询（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('overtimeMangement:list')")
    public ResponseEntity getOvertimeMangementNoPaging(AcOvertimeManagementQueryCriteria criteria) {
        return new ResponseEntity<>(acOvertimeMangementService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('overtimeMangement:list')")
    public void download(HttpServletResponse response, AcOvertimeManagementQueryCriteria criteria) throws IOException {
        acOvertimeMangementService.download(acOvertimeMangementService.listAll(criteria), response);
    }
}
