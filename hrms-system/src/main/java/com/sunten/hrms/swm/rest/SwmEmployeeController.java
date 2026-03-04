package com.sunten.hrms.swm.rest;

import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.fnd.dto.FndRoleSmallDTO;
import com.sunten.hrms.fnd.service.FndRoleService;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.swm.domain.SwmEmployee;
import com.sunten.hrms.swm.dto.SwmEmployeeQueryCriteria;
import com.sunten.hrms.swm.service.SwmEmployeeService;
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
import java.util.Map;

/**
 * <p>
 * 薪酬员工信息表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@RestController
@Api(tags = "薪酬员工信息表")
@RequestMapping("/api/swm/employee")
public class SwmEmployeeController {
    private static final String ENTITY_NAME = "employee";
    private final SwmEmployeeService swmEmployeeService;
    private final FndDataScope fndDataScope;
    private final FndRoleService fndRoleService;

    public SwmEmployeeController(SwmEmployeeService swmEmployeeService, FndDataScope fndDataScope, FndRoleService fndRoleService) {
        this.swmEmployeeService = swmEmployeeService;
        this.fndDataScope = fndDataScope;
        this.fndRoleService = fndRoleService;
    }

    @ErrorLog("新增、修改员工信息的选择条件查询")
    @ApiOperation("新增、修改员工信息的选择条件查询")
    @GetMapping(value = "/distinctBeforeAU")
    @PreAuthorize("@el.check('swmEmployee:add','swmEmployee:edit')")
    public ResponseEntity distinctBeforeAU() {
        return new ResponseEntity<>(swmEmployeeService.getDistinctTings(), HttpStatus.OK);
    }

    @ErrorLog("人事信息指导性信息查询")
    @ApiOperation("人事信息指导性信息查询")
    @GetMapping(value = "/getEmpMsg")
    @PreAuthorize("@el.check('swmEmployee:add','swmEmployee:edit')")
    public ResponseEntity getEmpMsg(SwmEmployeeQueryCriteria criteria) {
        return new ResponseEntity<>(swmEmployeeService.listForSwmEmpUse(criteria.getNameOrWorkCard()), HttpStatus.OK);
    }

    @Log("新增薪酬员工信息表")
    @ApiOperation("新增薪酬员工信息表")
    @PostMapping
    @PreAuthorize("@el.check('swmEmployee:add')")
    public ResponseEntity create(@Validated @RequestBody SwmEmployee employee) {
        if (employee.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmEmployeeService.insert(employee), HttpStatus.CREATED);
    }

//    @Log("删除薪酬员工信息表")
//    @ApiOperation("删除薪酬员工信息表")
//    @DeleteMapping(value = "/removeEmp")
//    @PreAuthorize("@el.check('employee:del')")
//    public ResponseEntity delete(@PathVariable Long id) {
//        try {
//            swmEmployeeService.delete(id);
//        } catch (Throwable e) {
//            ThrowableUtil.throwForeignKeyException(e, "该薪酬员工信息表存在 关联，请取消关联后再试");
//        }
//        return new ResponseEntity(HttpStatus.OK);
//    }

    @Log("修改薪酬员工信息表")
    @ApiOperation("修改薪酬员工信息表")
    @PutMapping
    @PreAuthorize("@el.check('swmEmployee:edit')")
    public ResponseEntity update(@Validated(SwmEmployee.Update.class) @RequestBody SwmEmployee employee) {
        swmEmployeeService.update(employee);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除薪酬员工信息表")
    @ApiOperation("修改薪酬员工信息表")
    @PutMapping(value = "/removeEmp")
    @PreAuthorize("@el.check('swmEmployee:del')")
    public ResponseEntity removeEmp(@RequestBody SwmEmployee employee) {
        swmEmployeeService.updateByWorkFlag(employee);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

//    @Log("修改薪酬员工权限")
//    @ApiOperation("修改薪酬员工权限")
//    @PutMapping(value = "/altSwmAuth")
//    @PreAuthorize("@el.check('swmEmployee:edit')")
//    public ResponseEntity altSwmAuth(@RequestBody SwmEmployee employee) {
//        swmEmployeeService.updateByManagerFlag(employee);
//        return new ResponseEntity(HttpStatus.NO_CONTENT);
//    }

    @ErrorLog("获取单个薪酬员工信息表")
    @ApiOperation("获取单个薪酬员工信息表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('swmEmployee:list')")
    public ResponseEntity getEmployee(@PathVariable Long id) {
        return new ResponseEntity<>(swmEmployeeService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("根据Employee_id获取单个薪酬员工信息表")
    @ApiOperation("根据Employee_id获取单个薪酬员工信息表")
    @GetMapping(value = "/byEmployee/{employeeId}")
    @PreAuthorize("@el.check('swmEmployee:list')")
    public ResponseEntity getEmployeeByEmployeeid(@PathVariable Long employeeId) {
        return new ResponseEntity<>(swmEmployeeService.getEmployeeByEmployeeid(employeeId), HttpStatus.OK);
    }

    @ErrorLog("查询薪酬员工信息表（分页）")
    @ApiOperation("查询薪酬员工信息表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('swmEmployee:list')")
    public ResponseEntity getEmployeePage(SwmEmployeeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        setCriteariaForSwmEmployee(criteria);
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, false,null, null);
        List<FndRoleSmallDTO> fndRoleSmallDTO=fndRoleService.listByUserId(SecurityUtils.getUserId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(swmEmployeeService.listAll(criteria, pageable), HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        for(FndRoleSmallDTO s:fndRoleSmallDTO)
        {
            if(s.getId()==20||s.getId()==1||s.getName().equals("薪酬模块查看") )
            {
                criteria.setEmployeeId(null);
                criteria.setDeptIds(null);
            }
        }
        return new ResponseEntity<>(swmEmployeeService.listAll(criteria, pageable), HttpStatus.OK);
    }


    @ErrorLog("查询拥有权限范围的薪酬人员信息（分页）")
    @ApiOperation("查询拥有权限范围的薪酬人员信息（分页）")
    @GetMapping("/getAllHaveAuth")
    @PreAuthorize("@el.check('swmEmployee:list')")
    public ResponseEntity getAllHaveAuthPage(SwmEmployeeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"workCard"}, direction = Sort.Direction.ASC) Pageable pageable) {
        setCriteariaForSwmEmployee(criteria);
        return new ResponseEntity<>(swmEmployeeService.listAllHaveAuth(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询薪酬员工信息表（不分页）")
    @ApiOperation("查询薪酬员工信息表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('swmEmployee:list')")
    public ResponseEntity getEmployeeNoPaging(SwmEmployeeQueryCriteria criteria) {
    return new ResponseEntity<>(swmEmployeeService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出薪酬员工信息表数据")
    @ApiOperation("导出薪酬员工信息表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('swmEmployee:list')")
    public void download(HttpServletResponse response, SwmEmployeeQueryCriteria criteria) throws IOException {
        setCriteariaForSwmEmployee(criteria);
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, false,null, null);
        List<FndRoleSmallDTO> fndRoleSmallDTO=fndRoleService.listByUserId(SecurityUtils.getUserId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            swmEmployeeService.download(swmEmployeeService.listAll(criteria), response);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        for(FndRoleSmallDTO s:fndRoleSmallDTO)
        {
            if(s.getId()==20||s.getId()==1||s.getName().equals("薪酬模块查看") )
            {
                criteria.setEmployeeId(null);
                criteria.setDeptIds(null);
            }
        }
        swmEmployeeService.download(swmEmployeeService.listAll(criteria), response);
    }

    private void setCriteariaForSwmEmployee(SwmEmployeeQueryCriteria criteria) {
        if (null != criteria.getDeptCode()) {
            if (criteria.getDeptCode().contains(".")) {
                String[] strs = criteria.getDeptCode().split("\\.");
                criteria.setDepartment(strs[0]);
                criteria.setAdministrativeOffice(strs[1]);
            } else {
                criteria.setDepartment(criteria.getDeptCode());
            }

        }
    }


    @ErrorLog("查询薪酬部门（树结构）")
    @ApiOperation("查询薪酬部门（树结构）")
    @GetMapping(value = "/getSwmDeptTree")
    @PreAuthorize("@el.check('swmEmployee:list')")
    public ResponseEntity getSwmDeptTree(){
            return new ResponseEntity<>(swmEmployeeService.buildDeptList(), HttpStatus.OK);
    }

    @ErrorLog("获取当前用户的人员信息（薪资）")
    @ApiOperation("获取当前用户的人员信息（薪资）")
    @GetMapping(value = "/getSwmEmpByEmpId")
    @PreAuthorize("@el.check('mySalary:list')")
    public ResponseEntity getSwmEmpByEmpId() {
        return new ResponseEntity<>(swmEmployeeService.getSwmEmpByEmpId(), HttpStatus.OK);
    }

    @ErrorLog("导出薪酬人员管理范围数据")
    @ApiOperation("导出薪酬人员管理范围数据")
    @GetMapping(value = "/getDistDepartment")
    @PreAuthorize("@el.check('swmEmployee:list')")
    public ResponseEntity getDistDepartment() {
        return new ResponseEntity<>(swmEmployeeService.getDistDepartment(), HttpStatus.OK);
    }

    @ErrorLog("导出薪酬人员管理范围数据")
    @ApiOperation("导出薪酬人员管理范围数据")
    @GetMapping(value = "/getDistAdministrativeOffice")
    @PreAuthorize("@el.check('swmEmployee:list')")
    public ResponseEntity getDistAdministrativeOffice() {
        return new ResponseEntity<>(swmEmployeeService.getDistAdministrativeOffice(null), HttpStatus.OK);
    }

    @ErrorLog("根据部门查询其下的科室")
    @ApiOperation("根据部门查询其下的科室")
    @GetMapping(value = "/getOfficeByDepartment/{department}")
    @PreAuthorize("@el.check('swmEmployee:list')")
    public ResponseEntity getOfficeByDepartment(@PathVariable String department) {
        return new ResponseEntity<>(swmEmployeeService.getDistAdministrativeOffice(department), HttpStatus.OK);
    }

    @ErrorLog("根据科室查询其下的班组")
    @ApiOperation("根据科室查询其下的班组")
    @GetMapping(value = "/getTeamByOffice/{office}")
    @PreAuthorize("@el.check('swmEmployee:list')")
    public ResponseEntity getTeamByOffice(@PathVariable String office) {
        return new ResponseEntity<>(swmEmployeeService.getTeamList(office), HttpStatus.OK);
    }

    @ErrorLog("根据员工id获取调出岗位得薪资")
    @ApiOperation("根据员工id获取调出岗位得薪资")
    @GetMapping(value = "/getTransforFromSalary/{employeeId}")
    @PreAuthorize("@el.check('transferRequest:list')")
    public ResponseEntity getTransforFromSalary(@PathVariable Long employeeId) {
        return new ResponseEntity<>(swmEmployeeService.getTransforFromSalary(employeeId), HttpStatus.OK);
    }

    @ErrorLog("获取当月固定工资或浮动工资是否已生成且为未冻结")
    @ApiOperation("获取当月固定工资或浮动工资是否已生成且为未冻结")
    @GetMapping(value = "/getSalaryStatus")
    @PreAuthorize("@el.check('swmEmployee:edit')")
    public ResponseEntity getSalaryStatus() {
        return new ResponseEntity<>(swmEmployeeService.getSalaryStatus(), HttpStatus.OK);
    }
}
