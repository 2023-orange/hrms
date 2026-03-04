package com.sunten.hrms.swm.rest;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndInterfaceOperationRecordQueryCriteria;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.swm.domain.SwmPostSkillSalary;
import com.sunten.hrms.swm.domain.SwmPostSkillSalaryExcel;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmPostSkillSalaryInterface;
import com.sunten.hrms.swm.dto.SwmPostSkillSalaryInterfaceQueryCriteria;
import com.sunten.hrms.swm.service.SwmPostSkillSalaryInterfaceService;
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
import java.util.stream.Collectors;

/**
 * <p>
 * 岗位技能工资接口表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@RestController
@Api(tags = "岗位技能工资接口表")
@RequestMapping("/api/swm/postSkillSalaryInterface")
public class SwmPostSkillSalaryInterfaceController {
    private static final String ENTITY_NAME = "postSkillSalaryInterface";
    private final SwmPostSkillSalaryInterfaceService swmPostSkillSalaryInterfaceService;
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;

    public SwmPostSkillSalaryInterfaceController(SwmPostSkillSalaryInterfaceService swmPostSkillSalaryInterfaceService, FndInterfaceOperationRecordService fndInterfaceOperationRecordService) {
        this.swmPostSkillSalaryInterfaceService = swmPostSkillSalaryInterfaceService;
        this.fndInterfaceOperationRecordService = fndInterfaceOperationRecordService;
    }

    @Log("新增岗位技能工资接口表")
    @ApiOperation("新增岗位技能工资接口表")
    @PostMapping
    @PreAuthorize("@el.check('postSkillSalaryInterface:add')")
    public ResponseEntity create(@Validated @RequestBody SwmPostSkillSalaryInterface postSkillSalaryInterface) {
        if (postSkillSalaryInterface.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmPostSkillSalaryInterfaceService.insert(postSkillSalaryInterface), HttpStatus.CREATED);
    }

    @Log("删除岗位技能工资接口表")
    @ApiOperation("删除岗位技能工资接口表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('postSkillSalaryInterface:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmPostSkillSalaryInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该岗位技能工资接口表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改岗位技能工资接口表")
    @ApiOperation("修改岗位技能工资接口表")
    @PutMapping
    @PreAuthorize("@el.check('postSkillSalaryInterface:edit')")
    public ResponseEntity update(@Validated(SwmPostSkillSalaryInterface.Update.class) @RequestBody SwmPostSkillSalaryInterface postSkillSalaryInterface) {
        swmPostSkillSalaryInterfaceService.update(postSkillSalaryInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个岗位技能工资接口表")
    @ApiOperation("获取单个岗位技能工资接口表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('postSkillSalaryInterface:list')")
    public ResponseEntity getPostSkillSalaryInterface(@PathVariable Long id) {
        return new ResponseEntity<>(swmPostSkillSalaryInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询岗位技能工资接口表（分页）")
    @ApiOperation("查询岗位技能工资接口表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('postSkillSalaryInterface:list')")
    public ResponseEntity getPostSkillSalaryInterfacePage(SwmPostSkillSalaryInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmPostSkillSalaryInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询岗位技能工资接口表（不分页）")
    @ApiOperation("查询岗位技能工资接口表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('postSkillSalaryInterface:list')")
    public ResponseEntity getPostSkillSalaryInterfaceNoPaging(SwmPostSkillSalaryInterfaceQueryCriteria criteria) {
    return new ResponseEntity<>(swmPostSkillSalaryInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出岗位技能工资接口表数据")
    @ApiOperation("导出岗位技能工资接口表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('postSkillSalaryInterface:list')")
    public void download(HttpServletResponse response, SwmPostSkillSalaryInterfaceQueryCriteria criteria) throws IOException {
        swmPostSkillSalaryInterfaceService.download(swmPostSkillSalaryInterfaceService.listAll(criteria), response);
    }

//    @Log("工作日加班工资导入")
//    @ApiOperation("工作日加班工资导入")
//    @PutMapping(value = "/insertOvertimePay")
//    @PreAuthorize("@el.check('postSkillSalary:add','postSkillSalary:edit')")
//    public ResponseEntity insertOvertimePay(@RequestBody List<SwmPostSkillSalaryInterface> swmPostSkillSalaryInterfaces) {
//        return new ResponseEntity<>(swmPostSkillSalaryInterfaceService.insertExcel(swmPostSkillSalaryInterfaces, "工作日加班工资导入"), HttpStatus.OK);
//    }
//
//    @Log("法定节假日加班工资导入")
//    @ApiOperation("法定节假日加班工资导入")
//    @PutMapping(value = "/insertHolidayOvertimePay")
//    @PreAuthorize("@el.check('postSkillSalary:add','postSkillSalary:edit')")
//    public ResponseEntity insertHolidayOvertimePay(@RequestBody List<SwmPostSkillSalaryInterface> swmPostSkillSalaryInterfaces) {
//        return new ResponseEntity<>(swmPostSkillSalaryInterfaceService.insertExcel(swmPostSkillSalaryInterfaces, "法定节假日加班工资导入"), HttpStatus.OK);
//    }
//
//    @Log("休息日加班工资导入")
//    @ApiOperation("休息日加班工资导入")
//    @PutMapping(value = "/insertRestOvertimePay")
//    @PreAuthorize("@el.check('postSkillSalary:add','postSkillSalary:edit')")
//    public ResponseEntity insertRestOvertimePay(@RequestBody List<SwmPostSkillSalaryInterface> swmPostSkillSalaryInterfaces) {
//        return new ResponseEntity<>(swmPostSkillSalaryInterfaceService.insertExcel(swmPostSkillSalaryInterfaces, "休息日加班工资导入"), HttpStatus.OK);
//    }
//
//    @Log("扣除所得税导入")
//    @ApiOperation("扣除所得税导入")
//    @PutMapping(value = "/insertDeductIncomeTax")
//    @PreAuthorize("@el.check('postSkillSalary:add','postSkillSalary:edit')")
//    public ResponseEntity insertDeductIncomeTax(@RequestBody List<SwmPostSkillSalaryInterface> swmPostSkillSalaryInterfaces) {
//        return new ResponseEntity<>(swmPostSkillSalaryInterfaceService.insertExcel(swmPostSkillSalaryInterfaces, "扣除所得税导入"), HttpStatus.OK);
//    }

    @Log("导入固定工资相关栏目")
    @ApiOperation("导入固定工资相关栏目")
    @PutMapping(value = "/insertPostSkillSalary")
    @PreAuthorize("@el.check('postSkillSalary:add','postSkillSalary:edit')")
    public ResponseEntity insertPostSkillSalary(@RequestBody SwmPostSkillSalaryExcel swmPostSkillSalaryExcel) {
        return new ResponseEntity<>(swmPostSkillSalaryInterfaceService.insertExcel(swmPostSkillSalaryExcel.getSwmPostSkillSalaryInterfaces(), swmPostSkillSalaryExcel.getReImportFlag(),null), HttpStatus.OK);
    }

    @Log("电网车间专用的固定工资导入")
    @ApiOperation("电网车间专用的固定工资导入")
    @PutMapping(value = "/insertPostSkillSalarySpecial")
    @PreAuthorize("@el.check('postSkillSalary:add','postSkillSalary:edit')")
    public ResponseEntity insertPostSkillSalarySpecial(@RequestBody SwmPostSkillSalaryExcel swmPostSkillSalaryExcel) {
        return new ResponseEntity<>(swmPostSkillSalaryInterfaceService.insertExcel(swmPostSkillSalaryExcel.getSwmPostSkillSalaryInterfaces(), swmPostSkillSalaryExcel.getReImportFlag(), "电网车间加班工资导入"), HttpStatus.OK);
    }

    @Log("获取固定工资的导入记录")
    @ApiOperation("获取固定工资的导入记录")
    @GetMapping(value = "/getPostSkillSalaryImportHistory")
    @PreAuthorize("@el.check('postSkillSalary:add','postSkillSalary:edit')")
    public ResponseEntity getPostSkillSalaryImportHistory() {
        return new ResponseEntity<>(fndInterfaceOperationRecordService.getListByOperationValue("insertPostSkillSalary"), HttpStatus.OK);
    }


    @ErrorLog("导出固定工资的具体导入记录")
    @ApiOperation("导出固定工资的具体导入记录")
    @GetMapping(value = "/downloadImportHistory")
    @PreAuthorize("@el.check('postSkillSalary:add','postSkillSalary:edit')")
    public void downloadImportHistory(HttpServletResponse response,  Long groupId) throws IOException {
        SwmPostSkillSalaryInterfaceQueryCriteria swmPostSkillSalaryInterfaceQueryCriteria = new SwmPostSkillSalaryInterfaceQueryCriteria();
        swmPostSkillSalaryInterfaceQueryCriteria.setGroupId(groupId);
        swmPostSkillSalaryInterfaceService.download(swmPostSkillSalaryInterfaceService.listAll(swmPostSkillSalaryInterfaceQueryCriteria), response);
    }

    @ErrorLog("获取导入后的汇总信息")
    @ApiOperation("获取导入后的汇总信息")
    @PutMapping(value = "/getFixedSummaryByImportList")
    @PreAuthorize("@el.check('postSkillSalary:add', 'postSkillSalary:edit')")
    public ResponseEntity getFixedSummaryByImportList(@RequestBody SwmPostSkillSalaryInterfaceQueryCriteria swmPostSkillSalaryInterfaceQueryCriteria) {
        if (null == swmPostSkillSalaryInterfaceQueryCriteria.getSwmPostSkillSalaryInterfaces() ||
        swmPostSkillSalaryInterfaceQueryCriteria.getSwmPostSkillSalaryInterfaces().size() == 0) {
            throw new InfoCheckWarningMessException("固定工资根据导入数据获取汇总结果失败");
        } else {
            return new ResponseEntity<>(swmPostSkillSalaryInterfaceService.getFixedSummaryByImportList(
                    swmPostSkillSalaryInterfaceQueryCriteria.getSwmPostSkillSalaryInterfaces().get(0).getIncomePeriod(),
                    swmPostSkillSalaryInterfaceQueryCriteria.getSwmPostSkillSalaryInterfaces().stream().map(SwmPostSkillSalaryInterface::getWorkCard).collect(Collectors.toSet()),
                    swmPostSkillSalaryInterfaceQueryCriteria.getGroupIds()
            ), HttpStatus.OK);
        }
    }


}
