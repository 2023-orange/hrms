package com.sunten.hrms.pm.rest;

import cn.hutool.json.JSONArray;
import com.sunten.hrms.ac.service.AcEmpDeptsService;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.dto.PmEmployeeLikeQueryCriteria;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.SecurityUtils;
import com.sunten.hrms.utils.ThrowableUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
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
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

/**
 * <p>
 * 人事档案表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "人事档案表")
@RequestMapping("/api/pm/employee")
public class PmEmployeeController {
    private static final String ENTITY_NAME = "employee";
    private final PmEmployeeService pmEmployeeService;
    private final FndDataScope fndDataScope;
    private final FndUserService fndUserService;
    private final FndDeptService fndDeptService;
    private final AcEmpDeptsService acEmpDeptsService;

    public PmEmployeeController(PmEmployeeService pmEmployeeService, FndDataScope fndDataScope, FndUserService fndUserService, FndDeptService fndDeptService, AcEmpDeptsService acEmpDeptsService) {
        this.pmEmployeeService = pmEmployeeService;
        this.fndDataScope = fndDataScope;
        this.fndUserService = fndUserService;
        this.fndDeptService = fndDeptService;
        this.acEmpDeptsService = acEmpDeptsService;
    }

    @Log("编辑人事档案表")
    @ApiOperation("编辑人事档案表")
    @PostMapping
    @PreAuthorize("@el.check('employee:add','employee:edit')")
    public ResponseEntity create(@Validated @RequestBody PmEmployee employee) {
        return new ResponseEntity<>(pmEmployeeService.insert(employee), HttpStatus.CREATED);
    }

    @Log("删除人事档案表")
    @ApiOperation("删除人事档案表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该人事档案表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改人事档案表")
    @ApiOperation("修改人事档案表")
    @PutMapping
    @PreAuthorize("@el.check('employee:add','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployee.Update.class) @RequestBody PmEmployee employee) {
        pmEmployeeService.update(employee);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个人事档案表")
    @ApiOperation("获取单个人事档案表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employee:list','medical:list', 'transferRequest:list', 'consolationMoney:list')")
    public ResponseEntity getEmployee(@PathVariable Long id) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, null, id);
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(pmEmployeeService.getByKey(dataScopeVo.getEmployeeId(), dataScopeVo.getDeptIds()), HttpStatus.OK);
    }

    @ErrorLog("查询人事档案表（分页）")
    @ApiOperation("查询人事档案表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employee:list','train:list','empDepts:list','medical:list','teacher:edit','employee:query','assessmentIndicators:list')")
    public ResponseEntity getEmployeePage(PmEmployeeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptAllId(), criteria.getDeptId(), false, true, criteria.getDeptIds(), criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询人事档案表（分页）")
    @ApiOperation("查询人事档案表（分页）")
    @GetMapping("/getPmNoAuth")
    @PreAuthorize("@el.check('plan:list')")
    public ResponseEntity getEmployeeNoPagingByNoAuth(PmEmployeeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Set<Long> deptIds = new HashSet<>(fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptAllId()));
        criteria.setDeptIds(deptIds);
        return new ResponseEntity<>(pmEmployeeService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询电话修改页（分页）")
    @ApiOperation("查询电话修改页（分页）")
    @GetMapping("/getPmTelList")
    @PreAuthorize("@el.check('employeeTele:list')")
    public ResponseEntity getPmTelList(PmEmployeeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(pmEmployeeService.getPmTelList(pageable, criteria), HttpStatus.OK);
    }

    @ErrorLog("查询邮箱修改页（分页）")
    @ApiOperation("查询邮箱修改页（分页）")
    @GetMapping("/getPmEmailList")
    @PreAuthorize("@el.check('employee:emailList')")
    public ResponseEntity getPmEmailList(PmEmployeeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(pmEmployeeService.getPmEmailList(pageable, criteria), HttpStatus.OK);
    }

    @Log("邮箱修改")
    @ApiOperation("邮箱修改")
    @PutMapping("/savePmEmail")
    @PreAuthorize("@el.check('employee:emailEdit')")
    public ResponseEntity savePmEmail(@RequestBody PmEmployee pmEmployee) {
        pmEmployeeService.savePmEmail(pmEmployee);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ErrorLog("查询人事档案表（不分页）")
    @ApiOperation("查询人事档案表（不分页）")
    @GetMapping("/noPaging")
    @PreAuthorize("@el.check('employee:list','train:list','empDepts:list')")
    public ResponseEntity getEmployeeNoPaging(PmEmployeeQueryCriteria criteria) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptAllId(), criteria.getDeptId(), false, true, criteria.getDeptIds(), criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("根据考勤角色设置查询人事档案表")
    @ApiOperation("根据考勤角色设置查询人事档案表")
    @GetMapping("/getEmployeesByAcUser")
    public ResponseEntity getEmployeesByAcUser(PmEmployeeQueryCriteria criteria) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        criteria.setDeptIds(acEmpDeptsService.getJurisdictionDeptId(user.getEmployee().getId()));

        return new ResponseEntity<>(pmEmployeeService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("查询人事档案表（不分权限）")
    @ApiOperation("查询人事档案表（不分权限）")
    @GetMapping("/getPublicEmployees")
    public ResponseEntity getPublicEmployees(PmEmployeeQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出人事档案表数据")
    @ApiOperation("导出人事档案表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employee:list')")
    public void download(HttpServletResponse response, PmEmployeeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) throws IOException {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptAllId(), criteria.getDeptId(), false, true, criteria.getDeptIds(), criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            FileUtil.downloadExcel(new ArrayList<>(), response);
        } else {
            criteria.setDeptIds(dataScopeVo.getDeptIds());
            criteria.setEmployeeId(dataScopeVo.getEmployeeId());
            pmEmployeeService.download(criteria, pageable, response);
        }
    }

    @ErrorLog("根据名称、工牌、身份证查询员工数据")
    @ApiOperation("根据名称、工牌、身份证查询员工数据")
    @GetMapping(value = "/like")
    @PreAuthorize("@el.check('employee:list')")
    public ResponseEntity like(PmEmployeeLikeQueryCriteria criteria) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, criteria.getDeptIds(), criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeService.getListByNameOrCard(criteria), HttpStatus.OK);
    }

    @ErrorLog("获取待校核人员列表")
    @ApiOperation("获取待校核人员列表")
    @GetMapping(value = "/getTempList")
    @PreAuthorize("@el.check('employeeTemp:list')")
    public ResponseEntity getTempList(PmEmployeeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {

        return new ResponseEntity<>(pmEmployeeService.listAllByCheck(criteria, pageable), HttpStatus.OK);
    }

    @Log("修改照片")
    @ApiOperation("修改照片")
    @PostMapping(value = "/updatePhoto/{id}")
    @PreAuthorize("@el.check('employee:edit')")
    public ResponseEntity updatePhoto(@PathVariable Long id, @RequestParam MultipartFile file) throws InvalidKeySpecException, NoSuchAlgorithmException {
        pmEmployeeService.updatePhoto(file, id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ErrorLog("获取岗位下的人事档案表")
    @ApiOperation("获取岗位下的人事档案表")
    @GetMapping(value = "/jobEmployee/{jobId}")
    @PreAuthorize("@el.check('employee:list')")
    public ResponseEntity getEmployeeByJob(@PathVariable Long jobId) {
        return new ResponseEntity<>(pmEmployeeService.listAllByJobId(jobId), HttpStatus.OK);
    }

    @ErrorLog("验证工牌或身份证")
    @ApiOperation("验证工牌或身份证）")
    @GetMapping("/checkCardOrNumber")
    @PreAuthorize("@el.check('employee:add','employee:edit')")
    public ResponseEntity checkCardOrNumber(PmEmployeeQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeService.listForIdNumberOrWorkCardExist(criteria), HttpStatus.OK);
    }

    @ErrorLog("查询人员考勤排班设置情况（分页）")
    @ApiOperation("查询人员考勤排班设置情况（分页）")
    @GetMapping("/attendanceSetting")
    @PreAuthorize("@el.check('employeeAttendance:list')")
    public ResponseEntity getAttendanceSetting(PmEmployeeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
//        if (criteria != null && criteria.getDeptAllId() != null) {
//            FndDeptDTO deptDTO = fndDeptService.getByKey(criteria.getDeptAllId());
//            FndDept fndDept = new FndDept();
//            fndDept.setId(deptDTO.getId());
//            fndDept.setEnabledFlag(deptDTO.getEnabledFlag());
//            List<FndDept> depts1 = new ArrayList<>();
//            depts1.add(fndDept);
//            criteria.setDeptIds(new HashSet<>(fndDataScope.getDeptChildren(depts1)));
//        }
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptAllId(), criteria.getDeptId(), false, true, criteria.getDeptIds(), criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeService.listForAttendanceSet(criteria, pageable), HttpStatus.OK);
    }

    @Log("修改人员考勤标记")
    @ApiOperation("修改人员考勤标记")
    @PutMapping("/editAttendanceSet")
    @PreAuthorize("@el.check('attendanceSetting:edit')")
    public ResponseEntity updateAttendanceSet(@RequestBody PmEmployee employee) {
        pmEmployeeService.updateAttendanceFlag(employee);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("批量修改人员考勤标记")
    @ApiOperation("批量修改人员考勤标记")
    @PutMapping("/batchAttendanceSet")
    @PreAuthorize("@el.check('attendanceSetting:edit')")
    public ResponseEntity updateAttendanceSetBatch(@RequestBody List<PmEmployee> employees) {
        pmEmployeeService.updateAttendanceFlagBatch(employees);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("获取登录人所属部门领导")
    @ApiOperation("获取登录人所属部门领导")
    @GetMapping("/getSuperiorByUser")
    public ResponseEntity getSuperiorByUser() {
        // 1、获取登录用户
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        // 2、根据登录用户获取部门信息
        // 3、根据登录用户部门获取其父级部门及其管理岗信息
        // 4、循环父级部门获取对应领导的工牌号
        Map<String,Object> map = fndDeptService.getSuperiorByDept(user.getEmployee().getId());
        map.put("User",user.getEmployee().getWorkCard()); // 当前登录用户

        return new ResponseEntity(map,HttpStatus.OK);
    }

    //    @Log("获取人员所属部门领导")
    //    @ApiOperation("获取人员所属部门领导")
    //    @GetMapping("/getSuperiorByEmployee")
    //    public ResponseEntity getSuperiorByEmployee(@Param("username") String username) {
    //        // 1、根据工号获取用户
    //        FndUserDTO user = fndUserService.getByName(username);
    //        // 2、根据用户获取部门信息
    //        // 3、根据用户部门获取其父级部门及其管理岗信息
    //        // 4、循环父级部门获取对应领导的工牌号
    //        Map<String,Object> map = fndDeptService.getSuperiorByDept(user.getEmployee().getId());
    //        map.put("User",user.getEmployee().getWorkCard()); // 当前用户
    //
    //        return new ResponseEntity(map,HttpStatus.OK);
    //    }
    @Log("根据部门id获取主管及经理")
    @ApiOperation("根据部门id获取主管及经理")
    @GetMapping("/getManagerAndSuperiorByDeptId/{deptId}")
    @PreAuthorize("@el.check('common')")
    public ResponseEntity getManagerAndSuperiorByDeptId(@PathVariable Long deptId) {
        return new ResponseEntity<>(pmEmployeeService.getManagerAndSuperiorByDeptId(deptId), HttpStatus.OK);
    }

    /**
     *  @author：liangjw
     *  @Date: 2021/6/8 9:01
     *  @Description: 可查询经理列表或查询当前登录人的经理
     *  @params: listFlag 为true 时查所有经理，false 时查当前登录人的经理
     *  workCardFlag false时不带入登录人workCard，true时带入登录人workCard
     */
    @Log("获取经理列表")
    @ApiOperation("获取经理列表")
    @GetMapping("/getManagerByDeptName")
    public ResponseEntity getManagerByDeptName(Boolean workCardFlag, Boolean listFlag) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Map<String, Object> map = new HashMap<>();
        if (!listFlag) {
            map.put("deptName", user.getDept().getExtDeptName());
            map.put("workCard", workCardFlag ? user.getUsername() : null);
        }
        return new ResponseEntity<>(pmEmployeeService.getManagerByDeptName(map), HttpStatus.OK);
    }

    /**
     *  @author：liangjw
     *  @Date: 2021/6/8 9:01
     *  @Description: 可查询主管列表或查询当前登录人的经理
     *  @params: listFlag 为true 时查所有主管，false 时查当前登录人的主管
     *  workCardFlag false时不带入登录人workCard，true时带入登录人workCard
     */
    @Log("获取主管列表")
    @ApiOperation("获取主管列表")
    @GetMapping("/getSuperiorByDeptNameAndDepartmentName")
    public ResponseEntity getSuperiorByDeptNameAndDepartmentName(Boolean workCardFlag, Boolean listFlag) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Map<String, Object> map = new HashMap<>();
        if (!listFlag) {
            map.put("deptName", user.getDept().getExtDeptName());
            map.put("departmentName", user.getDept().getExtDepartmentName());
            map.put("workCard", workCardFlag ? user.getUsername() : null);
        }
        return new ResponseEntity<>(pmEmployeeService.getSuperiorByDeptNameAndDepartmentName(map), HttpStatus.OK);
    }

    /**
     *  @author：liangjw
     *  @Date: 2021/6/8 9:01
     *  @Description: 可查询班组长列表或当前登录的班组长
     *  @params: listFlag 为true 时查所有班长 ，false 时查当前登录人的班长
     *  workCardFlag false时不带入登录人workCard，true时带入登录人workCard
     */
    @Log("获取班长列表")
    @ApiOperation("获取班长列表")
    @GetMapping("/getTeamByDeptNameAndDepartmentNameAndTeamName")
    public ResponseEntity getTeamByDeptNameAndDepartmentNameAndTeamName(Boolean workCardFlag, Boolean listFlag) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Map<String, Object> map = new HashMap<>();
        if (!listFlag) {
            map.put("deptName", user.getDept().getExtDeptName());
            map.put("departmentName", user.getDept().getExtDepartmentName());
            map.put("teamName", user.getDept().getExtTeamName());
            map.put("workCard", workCardFlag ? user.getUsername() : null);
        }
        return new ResponseEntity<>(pmEmployeeService.getTeamByDeptNameAndDepartmentNameAndTeamName(map), HttpStatus.OK);
    }

    /**
     *  @author：liangjw
     *  @Date: 2021/6/8 9:01
     *  @Description: 可查询高级经理或领导
     */
    @Log("获取高级经理或领导列表")
    @ApiOperation("获取高级经理或领导列表")
    @GetMapping("/getLeaderList")
    public ResponseEntity getLeaderList() {
        return new ResponseEntity<>(pmEmployeeService.getLeaderList(), HttpStatus.OK);
    }


    /**
     *  @author：liangjw
     *  @Date: 2021/6/8 9:01
     *  @Description: 可查询高级经理或领导
     */
    @Log("根据部门id获取部门负责人")
    @ApiOperation("根据部门id获取部门负责人")
    @GetMapping("/getLeaderByDeptId/{deptId}")
    public ResponseEntity getLeaderByDeptId(@PathVariable Long deptId) {
        return new ResponseEntity<>(pmEmployeeService.getLeaderByDeptId(deptId), HttpStatus.OK);
    }

    @Log("根据部门获取人员")
    @ApiOperation("根据部门获取人员")
    @GetMapping("/getPmsByDeptIds")
    public ResponseEntity getPmsByDeptIds(PmEmployeeQueryCriteria criteria) {
        criteria.setEnabledFlag(true);
        criteria.setLeaveFlag(false);
        return new ResponseEntity<>(pmEmployeeService.listAll(criteria), HttpStatus.OK);
    }


    /**
     *  @author：zhoujy
     *  @Date: 2023/1/9 16:18
     *  @Description: 可查询高级经理或领导
     */
    @ErrorLog("查询人事档案表(无数据范围限制)")
    @ApiOperation("查询人事档案表(无数据范围限制)")
    @GetMapping("/getEmployeePageNoDataScope")
    @PreAuthorize("@el.check('medicalNoData:list')")
    public ResponseEntity getEmployeePageNoDataScope(PmEmployeeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptAllId(), criteria.getDeptId(), false, true, criteria.getDeptIds(), criteria.getEmployeeId());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("OA上根据工牌查询员工数据")
    @ApiOperation("OA上根据工牌查询员工数据")
    @GetMapping(value = "/workCard")
    public ResponseEntity workCard(PmEmployeeLikeQueryCriteria criteria) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, true, criteria.getDeptIds(), criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(pmEmployeeService.getListByNameOrCard(criteria), HttpStatus.OK);
    }

}
