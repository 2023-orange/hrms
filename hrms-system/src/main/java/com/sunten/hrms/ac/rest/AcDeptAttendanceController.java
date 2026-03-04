package com.sunten.hrms.ac.rest;

import com.sunten.hrms.ac.dto.AcDeptAttendanceDTO;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.AcDeptAttendance;
import com.sunten.hrms.ac.dto.AcDeptAttendanceQueryCriteria;
import com.sunten.hrms.ac.service.AcDeptAttendanceService;
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
import java.util.Map;

/**
 * <p>
 * 部门考勤制度关系表 前端控制器
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@RestController
@Api(tags = "部门考勤制度关系表")
@RequestMapping("/api/ac/deptAttendance")
public class AcDeptAttendanceController {
    private static final String ENTITY_NAME = "deptAttendance";
    private final AcDeptAttendanceService acDeptAttendanceService;

    public AcDeptAttendanceController(AcDeptAttendanceService acDeptAttendanceService) {
        this.acDeptAttendanceService = acDeptAttendanceService;
    }

    @Log("新增部门考勤制度关系表")
    @ApiOperation("新增部门考勤制度关系表")
    @PostMapping
    @PreAuthorize("@el.check('deptAttendance:edit')")
    public ResponseEntity create(@Validated @RequestBody AcDeptAttendance deptAttendance) {
        if (deptAttendance.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        deptAttendance.setDeptId(deptAttendance.getFndDept().getId());
        deptAttendance.setRegimeId(deptAttendance.getAcRegime().getId());
        deptAttendance.setCalendarHeaderId(deptAttendance.getAcCalendarHeader().getId());
        return new ResponseEntity<>(acDeptAttendanceService.insert(deptAttendance), HttpStatus.CREATED);
    }

    @Log("删除部门考勤制度关系表")
    @ApiOperation("删除部门考勤制度关系表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('deptAttendance:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acDeptAttendanceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该部门考勤制度关系表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("删除未生效考勤制度关系并重置生效关系的失效时间")
    @ApiOperation("删除未生效考勤制度关系并重置生效关系的失效时间")
    @PutMapping(value = "/removeByIdAndResetOld")
    @PreAuthorize("@el.check('deptAttendance:del')")
    public ResponseEntity removeByIdAndResetOld(@Validated(AcDeptAttendance.Update.class) @RequestBody AcDeptAttendance deptAttendance) {
        acDeptAttendanceService.removeByIdAndResetOld(deptAttendance);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改部门考勤制度关系表")
    @ApiOperation("修改部门考勤制度关系表")
    @PutMapping
    @PreAuthorize("@el.check('deptAttendance:edit')")
    public ResponseEntity update(@Validated(AcDeptAttendance.Update.class) @RequestBody AcDeptAttendance deptAttendance) {
        acDeptAttendanceService.update(deptAttendance);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个部门考勤制度关系表")
    @ApiOperation("获取单个部门考勤制度关系表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('deptAttendance:list')")
    public ResponseEntity getDeptAttendance(@PathVariable Long id) {
        return new ResponseEntity<>(acDeptAttendanceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询部门考勤制度关系表（分页）")
    @ApiOperation("查询部门考勤制度关系表（分页）")
    @GetMapping(value = "/page")
    @PreAuthorize("@el.check('deptAttendance:list')")
    public ResponseEntity getDeptAttendancePage(AcDeptAttendanceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        criteria.setHistory(true);
        return new ResponseEntity<>(acDeptAttendanceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询部门考勤制度关系表（树结构）")
    @ApiOperation("查询部门考勤制度关系表（树结构）")
    @GetMapping
    @PreAuthorize("@el.check('deptAttendance:list')")
    public ResponseEntity getDeptAttendanceNoPaging(AcDeptAttendanceQueryCriteria criteria) {
        criteria.setHistory(false);
        List<AcDeptAttendanceDTO> acDeptAttendanceDTOS = acDeptAttendanceService.listAll(criteria);
        if (null != criteria.getDeptName()) {
            Map<String, Object> map = new HashMap<>();
            map.put("totalElements", acDeptAttendanceDTOS.size());
            map.put("content", acDeptAttendanceDTOS);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(acDeptAttendanceService.buildTree(acDeptAttendanceDTOS), HttpStatus.OK);
        }
    }

    @ErrorLog("查询部门考勤制度历史")
    @ApiOperation("查询部门考勤制度历史")
    @GetMapping(value = "/getHistoryById")
    @PreAuthorize("@el.check('deptAttendance:list')")
    public ResponseEntity getDeptAttendanceByHistory(AcDeptAttendanceQueryCriteria criteria) {
        return new ResponseEntity<>(acDeptAttendanceService.listForDeptHistory(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出部门考勤制度关系表数据")
    @ApiOperation("导出部门考勤制度关系表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('deptAttendance:list')")
    public void download(HttpServletResponse response, AcDeptAttendanceQueryCriteria criteria) throws IOException {
        acDeptAttendanceService.download(acDeptAttendanceService.listAll(criteria), response);
    }
}
