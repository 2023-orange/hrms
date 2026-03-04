package com.sunten.hrms.ac.rest;

import com.sunten.hrms.ac.domain.*;
import com.sunten.hrms.ac.dto.AcClockRecordRestQueryCriteria;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeService;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.dto.AcOvertimeApplicationQueryCriteria;
import com.sunten.hrms.ac.service.AcOvertimeApplicationService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zouyp
 * @since 2023-10-16
 */
@RestController
@Api(tags = "")
@RequestMapping("/api/ac/overtimeApplication")
public class AcOvertimeApplicationController {
    @Autowired
    private FndUserService fndUserService;
    private static final String ENTITY_NAME = "overtimeApplication";
    private final AcOvertimeApplicationService acOvertimeApplicationService;

    public AcOvertimeApplicationController(AcOvertimeApplicationService acOvertimeApplicationService) {
        this.acOvertimeApplicationService = acOvertimeApplicationService;
    }
    @Autowired
    private PmEmployeeService pmEmployeeService;


    @GetMapping("/getUserProductionCategory")
    @ErrorLog("/部门数查询每个部门人数有多少个")
    @ApiOperation("/部门数查询每个部门人数有多少个")
    public ResponseEntity getUserProductionCategory(@RequestParam("work_card") String workCard) {
        String res = acOvertimeApplicationService.getUserProductionCategory(workCard);
        if (res != null && !"".equals("workCard")){

        } else {
            res = "NDVC";
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/getUserNumberByTreeId")
    @ErrorLog("/部门数查询每个部门人数有多少个")
    @ApiOperation("/部门数查询每个部门人数有多少个")
    public ResponseEntity getUserNumberByTreeId(@RequestParam("deptId") Integer deptId) {
        Integer res = acOvertimeApplicationService.getUserNumberByTreeId(deptId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/getOvertimeLeaveInfoByCheckMonthAndWorkCard")
    @ErrorLog("/根据查询月份和工牌号获取对应的加班请假信息")
    @ApiOperation("/根据查询月份和工牌号获取对应的加班请假信息")
    public ResponseEntity getOvertimeLeaveInfoByCheckMonthAndWorkCard(@RequestParam("checkMonth") String checkMonth, @RequestParam("workCard") String workCard) {
        HashMap<String, List<HashMap<String, Object>>> res = acOvertimeApplicationService.getOvertimeLeaveInfoByCheckMonthAndWorkCard(checkMonth, workCard);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/updateReviewFormData")
    @ErrorLog("/复核加班申请信息")
    @ApiOperation("/复核加班申请信息")
    public ResponseEntity updateReviewFormData(@RequestBody UpdateReviewFormData updateReviewFormData) {
        System.out.println(updateReviewFormData.toString());
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        String name = fndUserDTO.getEmployee().getName();
        List<ReviewFormData> subFormData = updateReviewFormData.getUpdateReviewFormData();

        List<ReviewFormData> updatedSubFormData = subFormData.stream()
                .map(reviewFormData -> {
                    reviewFormData.setReviewer_nick_name(name);
                    reviewFormData.setReviewer_user_name(workCard);
                    if (reviewFormData.getHours() < reviewFormData.getReview_hours()) {
                        reviewFormData.setMonth_hours(reviewFormData.getMonth_hours() + (reviewFormData.getReview_hours() - reviewFormData.getHours()));
                    }
                    if (reviewFormData.getHours() > reviewFormData.getReview_hours()) {
                        reviewFormData.setMonth_hours(reviewFormData.getMonth_hours() - (reviewFormData.getReview_hours() - reviewFormData.getHours()));
                    }
                    return reviewFormData;
                })
                .collect(Collectors.toList());
        HashMap<String, String> res = new HashMap<>(16);
        try {
            acOvertimeApplicationService.updateReviewFormData(updatedSubFormData);
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("no", HttpStatus.OK);
        }
    }

    @GetMapping("/getRecentlyRecord")
    @ErrorLog("获取近三天打开记录")
    @ApiOperation("获取近三天打开记录")
    public ResponseEntity getRecentlyRecord(@RequestParam("work_card") String workCard, @RequestParam("end_time") String endTime) {
        List<HashMap<String, Object>> res = acOvertimeApplicationService.getRecentlyRecord(workCard, endTime);
        return  new ResponseEntity<>(res, HttpStatus.OK);
    }


    @GetMapping("/checkISReviewUser")
    @ErrorLog("查看是否是复核人员")
    @ApiOperation("查看是否是复核人员")
    public ResponseEntity checkISReviewUser() {
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        List<HashMap<String, Object>> hashMaps = acOvertimeApplicationService.checkReviewUser(workCard);
        if (hashMaps == null || hashMaps.size() == 0) {
            hashMaps = acOvertimeApplicationService.checkReviewUser2(workCard);
        }
        if (hashMaps.size() != 0) {
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
        return new ResponseEntity<>("NotOk", HttpStatus.OK);
    }

    @DeleteMapping("/deleteOvertimeReviewInfo")
    @ErrorLog("删除复核人员信息")
    @ApiOperation("删除复核人员信息")
    public ResponseEntity deleteOvertimeReviewInfo(@RequestParam("id") Integer id) {
        acOvertimeApplicationService.deleteOvertimeReviewInfo(id);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PutMapping("/updateOvertimeReview")
    @GetMapping("/simpleQuery")
    @ApiOperation("简单查询")
    @ErrorLog("简单查询")
    public ResponseEntity updateOvertimeReview(@RequestParam("id") Integer id,
                                        @RequestParam("dept_id") Integer dept_id,
                                      @RequestParam("section_id") Integer section_id,
                                               @RequestParam("work_card") String work_card) {
        acOvertimeApplicationService.updateOvertimeReview(id, dept_id, section_id, work_card);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


    @GetMapping("/simpleQuery")
    @ApiOperation("简单查询")
    @ErrorLog("简单查询")
    public ResponseEntity simpleQuery(@RequestParam("name") String name,
                                      @RequestParam("value") String value) {
        List<HashMap<String, Object>> res = acOvertimeApplicationService.simpleQuery(name, value);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/getOvertimeReviewUserInfo")
    @ApiOperation("获取所有加班复核人员信息")
    @ErrorLog("获取所有加班复核人员信息")
    public ResponseEntity getOvertimeReviewUserInfo(@RequestParam("id") Integer id) {
        HashMap<String, Object> res = acOvertimeApplicationService.getOvertimeReviewUserInfo(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @GetMapping("/getOvertimeReviewInfo")
    @ApiOperation("获取所有加班复核人员信息")
    @ErrorLog("获取所有加班复核人员信息")
    public ResponseEntity getOvertimeReviewInfo() {
        List<HashMap<String, Object>> res = acOvertimeApplicationService.getOvertimeReviewInfo();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/addOvertimeReviewUser")
    @ApiOperation("新增加班复核人员信息")
    @ErrorLog("新增加班复核人员信息")
    public ResponseEntity addOvertimeReviewUser(@RequestParam("dept_id") Integer dept_id,
                                                @RequestParam(value = "section_id", required = false) Integer section_id,
                                                @RequestParam(value = "work_card") String work_card) {
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String createBy = fndUserDTO.getEmployee().getWorkCard();
        acOvertimeApplicationService.addOvertimeReviewUser(dept_id, section_id,work_card, createBy);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }



    @GetMapping("/getEmployeeBaseInfoList")
    @Log("根据工号或姓名获取人员基本信息列表")
    @ApiOperation("根据工号或姓名获取人员基本信息列表")
    public ResponseEntity getEmployeeBaseInfoList(PmEmployeeQueryCriteria criteria) {
        return new ResponseEntity(pmEmployeeService.listEmployeesBaseInfoList(criteria),HttpStatus.OK);
    }

    @GetMapping("/getAllDepartment")
    @ErrorLog("获取所有部级部门")
    @ApiOperation("获取所有部级部门")
    public ResponseEntity getAllDepartment() {
        List<HashMap<String, Object>> res = acOvertimeApplicationService.getAllDepartment();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/getDepartmentByParentId")
    @ErrorLog("根据parentid获取科室")
    @ApiOperation("根据parentid获取科室")
    public ResponseEntity getDepartmentByParentId(@RequestParam("parentId") String parentId) {
        List<HashMap<String, Object>> res = acOvertimeApplicationService.getDepartmentByParentId(parentId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/getOvertimeInfo")
    @ErrorLog("根据oa_order")
    @ApiOperation("根据oa_order来查询对应的加班详情信息")
    public ResponseEntity getOvertimeInfo(@RequestParam("checkOaOrder") String checkOaOrder) {
        HashMap<String, List<HashMap<String, Object>>> res = acOvertimeApplicationService.getOvertimeInfo(checkOaOrder);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/updateResultToHrLeave")
    @ErrorLog("返写撤销撤销单结果")
    @ApiOperation(value = "返写撤销申请单结果")
    public ResponseEntity updateResultToHrLeave(@RequestParam("oaOrder") String oaOrder, @RequestParam("approvalResult") String approvalResult) {
        acOvertimeApplicationService.updateResultToHrOvertime(oaOrder, approvalResult);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PutMapping("/cancelOvertime")
    @ErrorLog("撤销加班申请")
    @ApiOperation("cancelOvertime")
    public ResponseEntity cancelOvertime(@RequestParam("oaOrder") String oaOrder) {
        Integer res = acOvertimeApplicationService.cancelOvertime(oaOrder);
        HashMap<String, Integer> resMap = new HashMap<String, Integer>(16);
        resMap.put("days", res);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    @PostMapping("/updateHrStatus")
    @ErrorLog("反写当前加班审批流程节点信息")
    @ApiOperation("反写当前加班审批流程节点信息")
    public ResponseEntity updateHrStatus(@RequestBody OvertimeUpdateForm overtimeUpdateForm) {
        HashMap<String, String> resMap = new HashMap<>(16);
        resMap = acOvertimeApplicationService.updateOaReq(overtimeUpdateForm.getOaOrder(), overtimeUpdateForm.getApprovalNode(), overtimeUpdateForm.getApprovalEmployee());
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    @PutMapping("/putmyokk")
    @ErrorLog("反写当前加班审批流程节点信息")
    @ApiOperation("反写当前加班审批流程节点信息")
    public ResponseEntity updateOaReq(@RequestParam( value = "okk", required = false) String okk, @RequestParam(value = "oaOrder", required = false) String okk2, @RequestParam(value = "okk3", required = false) String okk3) {
        HashMap<String, String> resMap = new HashMap<>(16);
        resMap = acOvertimeApplicationService.updateOaReq(okk2, okk, okk3);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }


    @GetMapping("/checkOvertimeInfo")
    @ErrorLog("检查本次加班申请是否重复")
    @ApiOperation("检查本次加班申请是否重复")
    public ResponseEntity checkOvertimeInfo(@RequestParam("start_time") String start_time,
                                            @RequestParam("end_time") String end_time,
                                            @RequestParam("work_card") String work_card) {
        HashMap<String, Object> resMap = acOvertimeApplicationService.newCheckOvertimeInfo(start_time, end_time, work_card);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    @PostMapping("/submitForm")
    @ErrorLog("提交请假表单信息并且做校验")
    @ApiOperation("提交请假表单信息并且做校验")
    public ResponseEntity submitForm(@RequestBody OvertimeApplicationForm overtimeApplicationForm) {
        System.out.println(overtimeApplicationForm.toString());
        System.out.println(overtimeApplicationForm.getSubOvertimeFormData().toString());
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String oaOrderWordCard = fndUserDTO.getEmployee().getWorkCard();
        // 开始校验提交的数据是否符合
        HashMap<String, String> resMap = acOvertimeApplicationService.submitData(overtimeApplicationForm, oaOrderWordCard);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    @GetMapping("/getDeptUsedOvertimeInfo")
    @ErrorLog("从加班时数备份表获取当月加班时数")
    @ApiOperation("从加班时数备份表获取当月加班时数")
    public ResponseEntity getDeptUsedOvertimeInfo(@RequestParam( value = "department", required = false) String department,
                                                  @RequestParam( value = "sectionName", required = false) String sectionName,
                                                  @RequestParam( value = "groups", required = false) String groups,
                                                  @RequestParam( value = "workCard", required = false) String workCard) {
        HashMap<String, Object> resMap = new HashMap<>(16);
//        System.out.println("sectionName: " + sectionName);
//        System.out.println("department: " + department);
        resMap = acOvertimeApplicationService.getDeptUsedOvertimeInfo(sectionName, department,groups, workCard);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }


    @GetMapping("/getCrossMonthInfo")
    @ErrorLog("从加班时数备份表获取当月加班时数")
    @ApiOperation("从加班时数备份表获取当月加班时数")
    public ResponseEntity getCrossMonthInfo(@RequestParam(value = "deptName", required = false) String deptName, @RequestParam(value = "department", required = false) String department,
                                            @RequestParam(value = "groups", required = false) String groups,
                                            @RequestParam("endTime") String endTime,
                                            @RequestParam(value = "workCard", required = false) String workCard) {
        HashMap<String, Object> resMap = new HashMap<>(16);
        resMap = acOvertimeApplicationService.getCrossMonthInfo(deptName, department, groups, endTime, workCard);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }
    @ErrorLog("加班时数管理获取部门人数")
    @ApiOperation("加班时数管理获取部门人数")
    @GetMapping("/searchDeptNumber")
    public ResponseEntity searchDeptNumber(@RequestParam(value = "deptName", required = false) String deptName) {
        System.out.println("部门名称：" + deptName);
        Integer res = acOvertimeApplicationService.searchDeptNumber(deptName);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @ErrorLog("加班时数管理获取部门人数")
    @ApiOperation("加班时数管理获取部门人数")
    @GetMapping("/getDeptInfoListByDeptName")
    public ResponseEntity getDeptInfoListByDeptName(@RequestParam(value = "deptName", required = false) String deptName) {
        System.out.println("部门名称：" + deptName);
        List<HashMap<String, Object>> res = acOvertimeApplicationService.getDeptInfoListByDeptName(deptName);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @GetMapping("/getMySelfOvertimeListInfo")
    @ErrorLog("获取加班人员信息列表")
    @ApiOperation("获取加班人员信息列表")
    public ResponseEntity getMySelfOvertimeListInfo(@RequestParam("workCard") String workCard, @RequestParam(value = "deptName", required = false) String deptName,@RequestParam(value = "departmentName", required = false) String departmentName
    , @RequestParam("roleId") Integer roleId, @RequestParam(value = "teamName", required = false) String teamName,
                                                        @RequestParam(value = "groups", required = false) String groups) {
        System.out.println("科室名称：" + deptName);
        System.out.println("部门名称：" + departmentName);
        System.out.println("班：" + groups);
        List<HashMap<String, Object>> res = new ArrayList<HashMap<String, Object>>();
        if (roleId == 1 && deptName.indexOf("车间") != -1) {
            res = acOvertimeApplicationService.getMySelfOvertimeListInfoByTeam(workCard, deptName, departmentName,teamName);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        if (!"".equals(deptName) && !"".equals(departmentName)) {
            res = acOvertimeApplicationService.getMySelfOvertimeListInfo(workCard, deptName, departmentName);
        } else if (!"".equals(departmentName)) {
            res = acOvertimeApplicationService.getMySelfOvertimeListInfo(workCard, deptName, departmentName);
        }
        if (deptName.indexOf("后勤") != -1) {
            res = acOvertimeApplicationService.getMySelfOvertimeListInfoFormChild(workCard, deptName, departmentName);
        }
//        else {
//            res = acOvertimeApplicationService.getMySelfOvertimeListInfo(workCard, deptName, departmentName);
//        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/getTeamList")
    @ErrorLog("主管获取底下班组人员信息")
    @ApiOperation("主管获取底下班组人员信息")
    public ResponseEntity getTeamList(@RequestParam(value = "deptName", required = false) String deptName, @RequestParam(value = "department", required = false) String department,
                                      @RequestParam(value = "workCard", required = false) String workCard) {
        System.out.println("部门名称：" + deptName);
        List<HashMap<String, Object>> res = acOvertimeApplicationService.getTeamList(deptName, department, workCard);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @Log("新增")
    @ApiOperation("新增")
    @PostMapping
    @PreAuthorize("@el.check('overtimeApplication:add')")
    public ResponseEntity create(@Validated @RequestBody AcOvertimeApplication overtimeApplication) {
        if (overtimeApplication.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(acOvertimeApplicationService.insert(overtimeApplication), HttpStatus.CREATED);
    }

    @Log("删除")
    @ApiOperation("删除")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('overtimeApplication:del')")
    public ResponseEntity delete(@PathVariable Integer id) {
        try {
            acOvertimeApplicationService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改")
    @ApiOperation("修改")
    @PutMapping
    @PreAuthorize("@el.check('overtimeApplication:edit')")
    public ResponseEntity update(@Validated(AcOvertimeApplication.Update.class) @RequestBody AcOvertimeApplication overtimeApplication) {
        acOvertimeApplicationService.update(overtimeApplication);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个")
    @ApiOperation("获取单个")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('overtimeApplication:list')")
    public ResponseEntity getOvertimeApplication(@PathVariable Integer id) {
        return new ResponseEntity<>(acOvertimeApplicationService.getByKey(id), HttpStatus.OK);
    }
    private void setCriteria(AcOvertimeApplicationQueryCriteria criteria) {

    }
    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping
    @PreAuthorize("@el.check('overtimeApplication:list')")
    public ResponseEntity getOvertimeApplicationPage(AcOvertimeApplicationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable, @RequestParam("reqStatusValue") String reqStatus) {
//        System.out.println(criteria);
        setCriteria(criteria);
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        criteria.setUser_name(workCard);
        if ("全部申请单".equals(reqStatus)) {
            reqStatus = "全部申请单";
        } else if ("已结束申请单".equals(reqStatus)) {
            reqStatus = "已结束申请单";
        } else if ("审批中申请单".equals(reqStatus)) {
            reqStatus = "审批中申请单";
        }
        criteria.setReqStatus(reqStatus);
//        System.out.println("criteria: " + criteria);
        return new ResponseEntity<>(acOvertimeApplicationService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("人资全部加班查询（分页）")
    @ApiOperation("人资全部加班查询（分页）")
    @GetMapping("/overtimeStatistics")
    public ResponseEntity getOvertimeApplicationStatisticsPage(AcOvertimeApplicationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable, @RequestParam("reqStatusValue") String reqStatus) {
//        System.out.println(criteria);
        setCriteria(criteria);
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        criteria.setUser_name(workCard);
        if ("全部申请单".equals(reqStatus)) {
            reqStatus = "全部申请单";
        } else if ("已结束申请单".equals(reqStatus)) {
            reqStatus = "已结束申请单";
        } else if ("审批中申请单".equals(reqStatus)) {
            reqStatus = "审批中申请单";
        }
        criteria.setReqStatus(reqStatus);
//        System.out.println("criteria: " + criteria);
        return new ResponseEntity<>(acOvertimeApplicationService.getOvertimeApplicationStatisticsPage(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping("/getOvertimeApplicationReviews")
    public ResponseEntity getOvertimeApplicationReviews(AcOvertimeApplicationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,
                              @RequestParam("reqStatusValue") String reqStatus) {
        System.out.println(criteria);
        setCriteria(criteria);
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        criteria.setUser_name(workCard);
        // TODO: 检查用户是不是复核人员
        List<HashMap<String, Object>> res;
        List<HashMap<String, Object>> res2;
        res = acOvertimeApplicationService.checkReviewUser(workCard);
        System.out.println("res: " + res);
        res2 = acOvertimeApplicationService.checkReviewUser2(workCard);
        System.out.println("res: " + res2);
        if (res2 != null) {
            res = Stream.concat(res.stream(), res2.stream())
                    .collect(Collectors.toList());
        }
//        if (res == null || res.size() == 0) {
//            res2 = acOvertimeApplicationService.checkReviewUser2(workCard);
//        }
        System.out.println("result: " + res.toString());
        if (res != null && res.size() > 0) {
            if ("待复核申请单".equals(reqStatus)) {
                reqStatus = "待复核申请单";
            } else if ("已复核申请单".equals(reqStatus)) {
                reqStatus = "已复核申请单";
            } else if ("已过期申请单".equals(reqStatus)) {
                reqStatus = "已过期申请单";
            }
            criteria.setReqStatus(reqStatus);
            System.out.println("criteria: " + criteria);
            return new ResponseEntity<>(acOvertimeApplicationService.getOvertimeApplicationReviews(criteria, pageable, res), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FALSE", HttpStatus.OK);
        }
    }

    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping("/getOvertimeLeaveStatisticsQueryList")
    public ResponseEntity getOvertimeLeaveStatisticsQueryList(AcOvertimeApplicationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,
                                                              @RequestParam(value = "checkMonth", required = false ) String checkMonth,
                                                              @RequestParam(value = "isHr", required = false) Boolean isHr,
                                                              @RequestParam(value = "employee_type", required = false) Integer employee_type,
                                                              @RequestParam(value = "department", required = false) String department,
                                                              @RequestParam(value = "administrative_office", required = false) String administrative_office,
                                                              @RequestParam(value = "groups", required = false) String groups) {
        System.out.println(criteria);
        setCriteria(criteria);
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        List<HashMap<String, Object>> hashMaps = acOvertimeApplicationService.checkReviewUser(workCard);
        System.out.println("isHr: " + isHr);
        System.out.println("employee_type: " + isHr);
        System.out.println("hashMaps: " + hashMaps);
        if (isHr == null && employee_type != null && employee_type != 1 && employee_type != 2 && hashMaps.size() > 0) {
            // 满足这几个条件，那这个人就是复核人员，那这个人可以查看本部门的所有信息
            employee_type = 2;
        }
        criteria.setUser_name(workCard);
//        System.out.println("checkMonth: " + checkMonth);
//        System.out.println("isHr: " + isHr);
//        System.out.println("employee_type: " + employee_type);
//        System.out.println("department: " + department);
//        System.out.println("administrative_office: " + administrative_office);
        if (isHr == null) {
            isHr = false;
        }
        if (employee_type == null) {
            employee_type = 0;
        }
        if (administrative_office == null) {
            administrative_office = String.valueOf(' ');
        }
        return new ResponseEntity<>(acOvertimeApplicationService.getOvertimeLeaveStatisticsQueryList(criteria, pageable, checkMonth, isHr, employee_type, department, administrative_office, groups), HttpStatus.OK);
    }
//    @ErrorLog("查询（分页）")
//    @ApiOperation("查询（分页）")
//    @GetMapping("/getOvertimeLeaveStatisticsQueryList")
//    public ResponseEntity getOvertimeLeaveStatisticsQueryListNew(AcOvertimeApplicationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,
//                                                              @RequestParam(value = "checkMonth", required = false ) String checkMonth,
//                                                              @RequestParam(value = "isHr", required = false) Boolean isHr,
//                                                              @RequestParam(value = "employee_type", required = false) Integer employee_type,
//                                                              @RequestParam(value = "department", required = false) String department,
//                                                              @RequestParam(value = "administrative_office", required = false) String administrative_office) {
//        System.out.println(criteria);
//        setCriteria(criteria);
//        // 获取令牌中的人员信息
//        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
//        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
//            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
//        }
//        String workCard = fndUserDTO.getEmployee().getWorkCard();
//        List<HashMap<String, Object>> hashMaps = acOvertimeApplicationService.checkReviewUser(workCard);
//        if (isHr == null && employee_type != 1 && employee_type != 2 && hashMaps.size() > 0) {
//            // 满足这几个条件，那这个人就是复核人员，那这个人可以查看本部门的所有信息
//            employee_type = 2;
//        }
//        criteria.setUser_name(workCard);
//        System.out.println("checkMonth: " + checkMonth);
//        System.out.println("isHr: " + isHr);
//        System.out.println("employee_type: " + employee_type);
//        System.out.println("department: " + department);
//        System.out.println("administrative_office: " + administrative_office);
//        if (isHr == null) {
//            isHr = false;
//        }
//        if (employee_type == null) {
//            employee_type = 0;
//        }
//        if (administrative_office == null) {
//            administrative_office = String.valueOf(' ');
//        }
//        return new ResponseEntity<>(acOvertimeApplicationService.getOvertimeLeaveStatisticsQueryListNew(criteria, pageable, checkMonth, isHr, employee_type, department, administrative_office), HttpStatus.OK);
//    }
    @ErrorLog("查询（不分页）")
    @ApiOperation("查询（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('overtimeApplication:list')")
    public ResponseEntity getOvertimeApplicationNoPaging(AcOvertimeApplicationQueryCriteria criteria) {
    return new ResponseEntity<>(acOvertimeApplicationService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('overtimeApplication:list')")
    public void download(HttpServletResponse response, AcOvertimeApplicationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable, @RequestParam("reqStatusValue") String reqStatus) throws IOException {
        setCriteria(criteria);
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        criteria.setUser_name(workCard);
        if ("全部申请单".equals(reqStatus)) {
            reqStatus = "全部申请单";
        } else if ("已结束申请单".equals(reqStatus)) {
            reqStatus = "已结束申请单";
        } else if ("审批中申请单".equals(reqStatus)) {
            reqStatus = "审批中申请单";
        }
        criteria.setReqStatus(reqStatus);
        acOvertimeApplicationService.download(acOvertimeApplicationService.listAll(criteria), response);
    }
    @ErrorLog("人资加班超级查询模块导出数据")
    @ApiOperation("人资加班超级查询模块导出数据")
    @GetMapping(value = "/downloadStatistics")
    @PreAuthorize("@el.check('overtimeApplication:list')")
    public void downloadStatistics(HttpServletResponse response, AcOvertimeApplicationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable, @RequestParam("reqStatusValue") String reqStatus) throws IOException {
        setCriteria(criteria);
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        criteria.setUser_name(workCard);
        if ("全部申请单".equals(reqStatus)) {
            reqStatus = "全部申请单";
        } else if ("已结束申请单".equals(reqStatus)) {
            reqStatus = "已结束申请单";
        } else if ("审批中申请单".equals(reqStatus)) {
            reqStatus = "审批中申请单";
        }
        criteria.setReqStatus(reqStatus);
        acOvertimeApplicationService.download(acOvertimeApplicationService.downloadStatistics(criteria), response);
    }

    @GetMapping(value = "/downloadOvertimeLeaveList")
    @ErrorLog("导出加班请假记录信息")
    @ApiOperation("导出加班请假记录信息")
    public void downloadOvertimeLeaveList(HttpServletResponse response, AcClockRecordRestQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,
                         @RequestParam(value = "checkMonth", required = false ) String checkMonth,
                         @RequestParam(value = "isHr", required = false) Boolean isHr,
                         @RequestParam(value = "employee_type", required = false) Integer employee_type,
                         @RequestParam(value = "department", required = false) String department,
                         @RequestParam(value = "administrative_office", required = false) String administrative_office,
                                          @RequestParam(value = "groups", required = false) String groups) throws IOException {
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        List<HashMap<String, Object>> hashMaps = acOvertimeApplicationService.checkReviewUser(workCard);
        if (isHr == null && employee_type != 1 && employee_type != 2 && hashMaps.size() > 0) {
            // 满足这几个条件，那这个人就是复核人员，那这个人可以查看本部门的所有信息
            employee_type = 2;
        }
        System.out.println("checkMonth: " + checkMonth);
        System.out.println("isHr: " + isHr);
        System.out.println("employee_type: " + employee_type);
        System.out.println("department: " + department);
        System.out.println("administrative_office: " + administrative_office);
        if (isHr == null) {
            isHr = false;
        }
        if (employee_type == null) {
            employee_type = 0;
        }
        if (administrative_office == null) {
            administrative_office = String.valueOf(' ');
        }
        acOvertimeApplicationService.downloadOvertimeLeaveList(acOvertimeApplicationService.getOvertimeLeaveStatisticsQueryListDownload(pageable, checkMonth, isHr, employee_type, department, administrative_office, groups), response);
    }

}
