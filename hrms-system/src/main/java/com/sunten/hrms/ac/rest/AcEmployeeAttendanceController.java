package com.sunten.hrms.ac.rest;

import com.sunten.hrms.ac.domain.AcEmployeeAttendanceCollect;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.AcEmployeeAttendance;
import com.sunten.hrms.ac.dto.AcEmployeeAttendanceQueryCriteria;
import com.sunten.hrms.ac.service.AcEmployeeAttendanceService;
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
 * 排班员工考勤制度关系表 前端控制器
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@RestController
@Api(tags = "排班员工考勤制度关系表")
@RequestMapping("/api/ac/employeeAttendance")
public class AcEmployeeAttendanceController {
    private static final String ENTITY_NAME = "employeeAttendance";
    private final AcEmployeeAttendanceService acEmployeeAttendanceService;
    private final FndDataScope fndDataScope;

    public AcEmployeeAttendanceController(AcEmployeeAttendanceService acEmployeeAttendanceService, FndDataScope fndDataScope) {
        this.acEmployeeAttendanceService = acEmployeeAttendanceService;
        this.fndDataScope = fndDataScope;
    }

    @Log("新增排班员工考勤制度关系表")
    @ApiOperation("新增排班员工考勤制度关系表")
    @PostMapping
    @PreAuthorize("@el.check('employeeAttendance:add')")
    public ResponseEntity create(@Validated @RequestBody AcEmployeeAttendance employeeAttendance) {
        if (employeeAttendance.getId()  != -1) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(acEmployeeAttendanceService.insert(employeeAttendance), HttpStatus.CREATED);
    }

    @Log("删除排班员工考勤制度关系表")
    @ApiOperation("删除排班员工考勤制度关系表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeAttendance:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acEmployeeAttendanceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该排班员工考勤制度关系表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改排班员工考勤制度关系表")
    @ApiOperation("修改排班员工考勤制度关系表")
    @PutMapping
    @PreAuthorize("@el.check('employeeAttendance:edit')")
    public ResponseEntity update(@Validated(AcEmployeeAttendance.Update.class) @RequestBody AcEmployeeAttendance employeeAttendance) {
        acEmployeeAttendanceService.update(employeeAttendance);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个排班员工考勤制度关系表")
    @ApiOperation("获取单个排班员工考勤制度关系表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeAttendance:list')")
    public ResponseEntity getEmployeeAttendance(@PathVariable Long id) {
        return new ResponseEntity<>(acEmployeeAttendanceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询排班员工考勤制度关系表（分页）")
    @ApiOperation("查询排班员工考勤制度关系表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeAttendance:list')")
    public ResponseEntity getEmployeeAttendancePage(AcEmployeeAttendanceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        if (null != criteria.getDeptId()) { // 部门id不会空，查节点及节点下人员
            PmEmployeeQueryCriteria pmEmployeeQueryCriteria = new PmEmployeeQueryCriteria();
            pmEmployeeQueryCriteria.setDeptAllId(criteria.getDeptId());
            criteria.setEmployeeJObs(pmEmployeeQueryCriteria.getEmployeeJObs());
        }
        return new ResponseEntity<>(acEmployeeAttendanceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询排班员工考勤制度关系表（不分页）")
    @ApiOperation("查询排班员工考勤制度关系表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeAttendance:list')")
    public ResponseEntity getEmployeeAttendanceNoPaging(AcEmployeeAttendanceQueryCriteria criteria) {
    if (null != criteria.getDeptId()) { // 部门id不会空，查节点及节点下人员
        PmEmployeeQueryCriteria pmEmployeeQueryCriteria = new PmEmployeeQueryCriteria();
        pmEmployeeQueryCriteria.setDeptAllId(criteria.getDeptId());
        criteria.setEmployeeJObs(pmEmployeeQueryCriteria.getEmployeeJObs());
    }
    return new ResponseEntity<>(acEmployeeAttendanceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("按默认日历查询排班员工考勤制度关系表")
    @ApiOperation("按默日历认查询排班员工考勤制度关系表")
    @GetMapping(value = "/listByCalendarLine")
    @PreAuthorize("@el.check('employeeAttendance:list')")
    public ResponseEntity getEmployeeAttendanceByCalendarLine(AcEmployeeAttendanceQueryCriteria criteria) {

        return new ResponseEntity<>(acEmployeeAttendanceService.listAllByCalendarLine(criteria), HttpStatus.OK);
    }

    @ErrorLog("按默认日历查询排班员工考勤制度关系表(分页)")
    @ApiOperation("按默日历认查询排班员工考勤制度关系表(分页)")
    @GetMapping(value = "/listByCalendarLineByPage")
    @PreAuthorize("@el.check('employeeAttendance:list')")
    public ResponseEntity getEmployeeAttendanceByCalendarLineByPage(AcEmployeeAttendanceQueryCriteria criteria,@PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {

        return new ResponseEntity<>(acEmployeeAttendanceService.listAllByCalendarLineByPage(criteria,pageable), HttpStatus.OK);
    }

    @ErrorLog("导出排班员工考勤制度关系表数据")
    @ApiOperation("导出排班员工考勤制度关系表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeAttendance:list')")
    public void download(HttpServletResponse response, AcEmployeeAttendanceQueryCriteria criteria) throws IOException {
        acEmployeeAttendanceService.download(acEmployeeAttendanceService.listAll(criteria), response);
    }

    @Log("批量保存修改员工考勤制度关系表数据")
    @ApiOperation("批量保存修改员工考勤制度关系表数据")
    @PutMapping(value = "/batchSave")
//    @PreAuthorize("@el.check('employeeAttendance:edit')")
    public ResponseEntity batchSave(@RequestBody AcEmployeeAttendanceCollect acEmployeeAttendanceCollect){
        acEmployeeAttendanceService.batchSave(acEmployeeAttendanceCollect);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("导出车间排班报表--时间段形式")
    @ApiOperation("导出车间排班报表--时间段形式")
    @GetMapping(value = "/downloadAttendacne")
    @PreAuthorize("@el.check('employeeAttendance:list')")
    public void downloadAttendacne(HttpServletResponse response, AcEmployeeAttendanceQueryCriteria criteria) throws IOException {
        acEmployeeAttendanceService.downloadAttendance(acEmployeeAttendanceService.listAllByCalendarLine(criteria), criteria.getShowType(), response);
    }
}
