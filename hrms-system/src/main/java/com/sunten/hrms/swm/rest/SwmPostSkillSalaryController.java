package com.sunten.hrms.swm.rest;

import cn.hutool.json.JSONArray;
import com.sunten.hrms.ac.domain.AcOvertimeLeaveInterface;
import com.sunten.hrms.ac.service.AcOvertimeLeaveInterfaceService;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.swm.dao.SwmFloatingWageDao;
import com.sunten.hrms.swm.dao.SwmPostSkillSalaryDao;
import com.sunten.hrms.swm.domain.SwmWageSummaryFile;
import com.sunten.hrms.swm.service.SwmFloatingWageService;
import com.sunten.hrms.swm.service.SwmWageSummaryFileService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmPostSkillSalary;
import com.sunten.hrms.swm.dto.SwmPostSkillSalaryQueryCriteria;
import com.sunten.hrms.swm.service.SwmPostSkillSalaryService;
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
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 岗位技能工资表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@RestController
@Api(tags = "岗位技能工资表")
@RequestMapping("/api/swm/postSkillSalary")
public class SwmPostSkillSalaryController {
    private static final String ENTITY_NAME = "postSkillSalary";
    private final SwmPostSkillSalaryService swmPostSkillSalaryService;
    private final SwmWageSummaryFileService swmWageSummaryFileService;
    private final SwmFloatingWageService swmFloatingWageService;
    private final AcOvertimeLeaveInterfaceService acOvertimeLeaveInterfaceService;

    public SwmPostSkillSalaryController(SwmPostSkillSalaryService swmPostSkillSalaryService, SwmWageSummaryFileService swmWageSummaryFileService,
                                        SwmFloatingWageService swmFloatingWageService, AcOvertimeLeaveInterfaceService acOvertimeLeaveInterfaceService) {
        this.swmPostSkillSalaryService = swmPostSkillSalaryService;
        this.swmWageSummaryFileService =swmWageSummaryFileService;
        this.swmFloatingWageService = swmFloatingWageService;
        this.acOvertimeLeaveInterfaceService = acOvertimeLeaveInterfaceService;
    }

    @Log("新增岗位技能工资表")
    @ApiOperation("新增岗位技能工资表")
    @PostMapping
    @PreAuthorize("@el.check('postSkillSalary:add')")
    public ResponseEntity create(@Validated @RequestBody SwmPostSkillSalary postSkillSalary) {
        if (postSkillSalary.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmPostSkillSalaryService.insert(postSkillSalary), HttpStatus.CREATED);
    }

    @Log("岗位技能工资生成")
    @ApiOperation("岗位技能工资生成")
    @PostMapping(value = "/generatePostSkillSalary")
    @PreAuthorize("@el.check('postSkillSalary:add','postSkillSalary:list')")
    public ResponseEntity generatePostSkillSalary( @RequestBody String period) {
        return new ResponseEntity<>(swmPostSkillSalaryService.generatePostSkillSalaryByMsp(period), HttpStatus.CREATED);
    }

    @Log("删除岗位技能工资表")
    @ApiOperation("删除岗位技能工资表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('postSkillSalary:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmPostSkillSalaryService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该岗位技能工资表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("按所得期间删除岗位技能工资")
    @ApiOperation("按所得期间删除岗位技能工资")
    @DeleteMapping(value = "/deleteByPeriod")
    @PreAuthorize("@el.check('postSkillSalary:del')")
    public ResponseEntity deleteByPeriod( @RequestBody String period) {
        swmPostSkillSalaryService.deleteByPeriod(period);
        return new ResponseEntity(HttpStatus.OK);
    }


    @Log("修改岗位技能工资表")
    @ApiOperation("修改岗位技能工资表")
    @PutMapping
    @PreAuthorize("@el.check('postSkillSalary:edit')")
    public ResponseEntity update(@Validated(SwmPostSkillSalary.Update.class) @RequestBody SwmPostSkillSalary postSkillSalary) {
        swmPostSkillSalaryService.update(postSkillSalary);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个岗位技能工资表")
    @ApiOperation("获取单个岗位技能工资表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('postSkillSalary:list')")
    public ResponseEntity getPostSkillSalary(@PathVariable Long id) {
        return new ResponseEntity<>(swmPostSkillSalaryService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询岗位技能工资表（分页）")
    @ApiOperation("查询岗位技能工资表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('postSkillSalary:list')")
    public ResponseEntity getPostSkillSalaryPage(SwmPostSkillSalaryQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        setCriteria(criteria);
        return new ResponseEntity<>(swmPostSkillSalaryService.listAll(criteria, pageable), HttpStatus.OK);
    }

    public void setCriteria(SwmPostSkillSalaryQueryCriteria criteria) {

        }

    @ErrorLog("查询岗位技能工资表（不分页）")
    @ApiOperation("查询岗位技能工资表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('postSkillSalary:list')")
    public ResponseEntity getPostSkillSalaryNoPaging(SwmPostSkillSalaryQueryCriteria criteria) {
        setCriteria(criteria);
        return new ResponseEntity<>(swmPostSkillSalaryService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出岗位技能工资表数据")
    @ApiOperation("导出岗位技能工资表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('postSkillSalary:list')")
    public void download(HttpServletResponse response, SwmPostSkillSalaryQueryCriteria criteria) throws IOException {
        swmPostSkillSalaryService.download(swmPostSkillSalaryService.listAll(criteria), response);
    }

    @ErrorLog("岗位技能工资所得期间获取")
    @ApiOperation("岗位技能工资所得期间获取")
    @GetMapping("/getPeriodList")
    @PreAuthorize("@el.check('postSkillSalary:list')")
    public ResponseEntity getPeriodList() {
        return new ResponseEntity<>(swmPostSkillSalaryService.generatePeriodList(), HttpStatus.OK);
    }

    @Log("岗位技能工资发放")
    @ApiOperation("岗位技能工资发放")
    @PutMapping(value = "/grantPostSkillSalary")
    @PreAuthorize("@el.check('postSkillSalary:edit')")
    public ResponseEntity grantPostSkillSalary(@RequestBody String period) {
        Long limit = getLimit(period);
        swmWageSummaryFileService.grantSalary("post", limit, period);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @Log("岗位技能工资冻结")
    @ApiOperation("岗位技能工资冻结")
    @PutMapping(value = "/flozenPostSkillSalary")
    @PreAuthorize("@el.check('postSkillSalary:edit')")
    public ResponseEntity flozenPostSkillSalary(@RequestBody String period) {
        Long limit = getLimit(period);
        swmWageSummaryFileService.frozenSalary("post", limit, period);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("批量修改固定工资")
    @ApiOperation("批量修改固定工资")
    @PutMapping("/batchUpdatePostSkillSalary")
    @PreAuthorize("@el.check('postSkillSalary:edit')")
    public ResponseEntity updateAttendanceSetBatch(@RequestBody List<SwmPostSkillSalary> swmPostSkillSalaries) {
        swmPostSkillSalaryService.batchUpdatePostSkillSalary(swmPostSkillSalaries);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("自动更新数据预检验")
    @ApiOperation("自动更新数据预检验")
    @GetMapping("/autoUpdateByOverTime")
    @PreAuthorize("@el.check('postSkillSalary:edit')")
    public ResponseEntity autoUpdateByOverTime(String period) {
        // 检验固定浮动是否已经生成且未冻结、工时是否已经导入、
        if (!swmPostSkillSalaryService.checkPostSalaryBeforAutoUpdate(period)) {
            throw new InfoCheckWarningMessException("该所的期间的固定工资未生成或已冻结，不允许按工时更新薪资条目。");
        }
        if (!swmFloatingWageService.checkFloatingWageBeforAutoUpdate(period)) {
            throw new InfoCheckWarningMessException("该所的期间的浮动工资未生成或已冻结，不允许按工时更新薪资条目。");
        }
        if (!acOvertimeLeaveInterfaceService.checkOvertimeLeaveBeforAutoUpdate(period)) {
            throw new InfoCheckWarningMessException("该所得期间的工时尚未导入，不允许按工时更新薪资条目。");
        }

        // 自动扣除
        swmPostSkillSalaryService.autoDeductByMsp(period);
        return new ResponseEntity(HttpStatus.OK);

    }


    private Long getLimit(@RequestBody String period) {
        Long limit;
        if (period == null) {
            LocalDate now = LocalDate.now();
            limit = Long.parseLong(now.getYear() + "" + (now.getMonthValue() - 1 < 10 ? "0" + (now.getMonthValue() - 1) : (now.getMonthValue() - 1) + ""));
        } else {
            String[] s = period.split("\\.");
            limit = Long.parseLong(s[0] + s[1]);
        }
        return limit;
    }

    @ErrorLog("检测该月固定工资是否已经冻结")
    @ApiOperation("检测该月固定工资是否已经冻结")
    @PostMapping("/checkSkillFrozenFlagByPeriod")
    @PreAuthorize("@el.check('postSkillSalary:list')")
    public ResponseEntity checkSkillFrozenFlagByPeriod(@RequestParam String period) {
        return new ResponseEntity<>(swmPostSkillSalaryService.checkFrozenFlagByPeriod(period), HttpStatus.OK);
    }


}
