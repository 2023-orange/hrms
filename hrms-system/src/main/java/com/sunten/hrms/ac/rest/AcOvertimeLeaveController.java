package com.sunten.hrms.ac.rest;

import com.sunten.hrms.ac.dao.AcSetUpDao;
import com.sunten.hrms.ac.domain.AcOvertimeLeave;
import com.sunten.hrms.ac.dto.AcOvertimeLeaveQueryCriteria;
import com.sunten.hrms.ac.service.AcOvertimeLeaveService;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ThrowableUtil;
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
import java.math.BigDecimal;

/**
 * <p>
 * oa加班请假统计 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-10-26
 */
@RestController
@Api(tags = "oa加班请假统计")
@RequestMapping("/api/ac/overtimeLeave")
public class AcOvertimeLeaveController {
    private static final String ENTITY_NAME = "overtimeLeave";
    private final AcOvertimeLeaveService acOvertimeLeaveService;
    private final FndDataScope fndDataScope;
    private final AcSetUpDao acSetUpDao;

    public AcOvertimeLeaveController(AcOvertimeLeaveService acOvertimeLeaveService, FndDataScope fndDataScope, AcSetUpDao acSetUpDao) {
        this.acOvertimeLeaveService = acOvertimeLeaveService;
        this.fndDataScope = fndDataScope;
        this.acSetUpDao = acSetUpDao;
    }

    @Log("新增oa加班请假统计")
    @ApiOperation("新增oa加班请假统计")
    @PostMapping
    @PreAuthorize("@el.check('overtimeLeave:add')")
    public ResponseEntity create(@Validated @RequestBody AcOvertimeLeave overtimeLeave) {
        if (overtimeLeave.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(acOvertimeLeaveService.insert(overtimeLeave), HttpStatus.CREATED);
    }

    @Log("删除oa加班请假统计")
    @ApiOperation("删除oa加班请假统计")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('overtimeLeave:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acOvertimeLeaveService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该oa加班请假统计存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改oa加班请假统计")
    @ApiOperation("修改oa加班请假统计")
    @PutMapping
    @PreAuthorize("@el.check('overtimeLeave:edit')")
    public ResponseEntity update(@Validated(AcOvertimeLeave.Update.class) @RequestBody AcOvertimeLeave overtimeLeave) {
        acOvertimeLeaveService.update(overtimeLeave);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个oa加班请假统计")
    @ApiOperation("获取单个oa加班请假统计")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('overtimeLeave:list')")
    public ResponseEntity getOvertimeLeave(@PathVariable Long id) {
        return new ResponseEntity<>(acOvertimeLeaveService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询oa加班请假统计（分页）")
    @ApiOperation("查询oa加班请假统计（分页）")
    @GetMapping
    @PreAuthorize("@el.check('overtimeLeave:list')")
    public ResponseEntity getOvertimeLeavePage(AcOvertimeLeaveQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"ot.pe_work_card"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptId(), null, false, true, criteria.getDeptIds(), criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
//        //这个System.out.print是卢添加的
//        System.out.print("这是工时管理查询！！！！！！！！！！！！！！！！！！！！这是工时管理查询！！！！！！！！！！！！！！！！");
//        System.out.print(criteria.getDeptId()+"      ");
//        System.out.print(criteria.getDeptIds()  +"      ");
//        System.out.print(criteria.getDate()+"      ");
//        System.out.print(criteria.getColName()+"      ");
//        System.out.print(criteria.getSymbol()+"      ");
//        System.out.print(criteria.getValue()+"      ");

        return new ResponseEntity<>(acOvertimeLeaveService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询工时汇总（分页）")
    @ApiOperation("查询工时汇总（分页）")
    @GetMapping(value = "/getSumAcOvertimeLeave")
    @PreAuthorize("@el.check('overtimeLeave:list')")
    public ResponseEntity getSumAcOvertimeLeave(AcOvertimeLeaveQueryCriteria criteria,  @PageableDefault(value = 9999, sort = {"pe_work_card"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptId(), null, false, true, criteria.getDeptIds(), criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());

        BigDecimal calculation = acSetUpDao.getByNew().getCalculation();
        criteria.setCulTime(calculation);

        //这个System.out.print是卢添加的
        System.out.print("这是工时汇总！！！！！！！！！！！！！！！！！！！！这是工时汇总！！！！！！！！！！！！！！！！");
        System.out.print(criteria.getDeptId()+"      ");
        System.out.print(criteria.getDeptIds()  +"      ");
        System.out.print(criteria.getDateFrom()+"      ");
        System.out.print(criteria.getDateTo()+"      ");
        System.out.print(criteria.getColName()+"      ");
        System.out.print(criteria.getSymbol()+"      ");
        System.out.print(criteria.getValue()+"      ");
        return new ResponseEntity<>(acOvertimeLeaveService.sumAcOvertimeLeavePage(pageable, criteria), HttpStatus.OK);
    }


    @ErrorLog("查询oa加班请假统计（不分页）")
    @ApiOperation("查询oa加班请假统计（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('overtimeLeave:list')")
    public ResponseEntity getOvertimeLeaveNoPaging(AcOvertimeLeaveQueryCriteria criteria) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptId(), null, false, true, criteria.getDeptIds(), criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(acOvertimeLeaveService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出oa加班请假统计数据")
    @ApiOperation("导出oa加班请假统计数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('overtimeLeave:list')")
    public void download(HttpServletResponse response, AcOvertimeLeaveQueryCriteria criteria) throws IOException {
        acOvertimeLeaveService.download(acOvertimeLeaveService.listAll(criteria), response);
    }

    @ErrorLog("导出工时管理汇总数据")
    @ApiOperation("导出工时管理汇总数据")
    @GetMapping(value = "/downloadSum")
    @PreAuthorize("@el.check('overtimeLeave:list')")
    public void downloadSum(HttpServletResponse response, AcOvertimeLeaveQueryCriteria criteria) throws IOException {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptId(), null, false, true, criteria.getDeptIds(), criteria.getEmployeeId());
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        System.out.print("这是工时汇总导出！！！！！！！！！！！！！！！！！！！！这是工时汇总导出！！！！！！！！！！！！！！！！");
        System.out.print(criteria.getDeptId()+"       ");
        System.out.print(criteria.getDeptIds()  +"       ");
        System.out.print(criteria.getDateFrom()+"       ");
        System.out.print(criteria.getDateTo()+"       ");
        System.out.print(criteria.getColName()+"       ");
        System.out.print(criteria.getSymbol()+"       ");
        System.out.print(criteria.getValue()+"       ");
        acOvertimeLeaveService.downloadSum(acOvertimeLeaveService.sumAcOvertimeLeave(criteria), response);
    }


    @ErrorLog("获取某月数据量")
    @ApiOperation("获取某月数据量")
    @GetMapping(value = "/countByMonth")
    @PreAuthorize("@el.check('overtimeLeave:list','overtimeLeave:add')")
    public ResponseEntity countByMonth(AcOvertimeLeaveQueryCriteria criteria) {
        return new ResponseEntity<>(acOvertimeLeaveService.countDataByMonth(criteria.getMonth()), HttpStatus.OK);
    }

    @Log("工时管理统计按钮（生成上月人员的应上班时数）")
    @ApiOperation("工时管理统计按钮（生成上月人员的应上班时数）")
    @PostMapping(value = "/generateLastMonthWorkingHours")
    @PreAuthorize("@el.check('overtimeLeaveInterfe:add')")
    public ResponseEntity generateLastMonthWorkingHours() {
        acOvertimeLeaveService.autoCreateLastMonthWithoutCheck();
        acOvertimeLeaveService.updateLastMonthWorkingHours();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
