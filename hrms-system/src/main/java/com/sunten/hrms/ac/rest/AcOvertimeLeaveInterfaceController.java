package com.sunten.hrms.ac.rest;

import com.sunten.hrms.ac.domain.AcOvertimeLeave;
import com.sunten.hrms.ac.domain.AcOvertimeLeaveInterface;
import com.sunten.hrms.ac.dto.AcOvertimeLeaveInterfaceDTO;
import com.sunten.hrms.ac.dto.AcOvertimeLeaveInterfaceQueryCriteria;
import com.sunten.hrms.ac.service.AcOvertimeLeaveInterfaceService;
import com.sunten.hrms.ac.vo.AcOvertimeImportVo;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * oa加班请假统计接口表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-10-26
 */
@RestController
@Api(tags = "oa加班请假统计接口表")
@RequestMapping("/api/ac/overtimeLeaveInterfe")
public class AcOvertimeLeaveInterfaceController {
    private static final String ENTITY_NAME = "overtimeLeaveInterfe";
    private final AcOvertimeLeaveInterfaceService acOvertimeLeaveInterfaceService;

    public AcOvertimeLeaveInterfaceController(AcOvertimeLeaveInterfaceService acOvertimeLeaveInterfaceService) {
        this.acOvertimeLeaveInterfaceService = acOvertimeLeaveInterfaceService;
    }

    @Log("新增oa加班请假统计接口表")
    @ApiOperation("新增oa加班请假统计接口表")
    @PostMapping
    @PreAuthorize("@el.check('overtimeLeaveInterfe:add')")
    public ResponseEntity create(@Validated @RequestBody AcOvertimeLeaveInterface overtimeLeaveInterfe) {
        if (overtimeLeaveInterfe.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(acOvertimeLeaveInterfaceService.insert(overtimeLeaveInterfe), HttpStatus.CREATED);
    }

    @Log("删除oa加班请假统计接口表")
    @ApiOperation("删除oa加班请假统计接口表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('overtimeLeaveInterfe:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acOvertimeLeaveInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该oa加班请假统计接口表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改oa加班请假统计接口表")
    @ApiOperation("修改oa加班请假统计接口表")
    @PutMapping
    @PreAuthorize("@el.check('overtimeLeaveInterfe:edit')")
    public ResponseEntity update(@Validated(AcOvertimeLeaveInterface.Update.class) @RequestBody AcOvertimeLeaveInterface overtimeLeaveInterfe) {
        acOvertimeLeaveInterfaceService.update(overtimeLeaveInterfe);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个oa加班请假统计接口表")
    @ApiOperation("获取单个oa加班请假统计接口表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('overtimeLeaveInterfe:list')")
    public ResponseEntity getOvertimeLeaveInterfe(@PathVariable Long id) {
        return new ResponseEntity<>(acOvertimeLeaveInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询oa加班请假统计接口表（分页）")
    @ApiOperation("查询oa加班请假统计接口表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('overtimeLeaveInterfe:list')")
    public ResponseEntity getOvertimeLeaveInterfePage(AcOvertimeLeaveInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(acOvertimeLeaveInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询oa加班请假统计接口表（不分页）")
    @ApiOperation("查询oa加班请假统计接口表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('overtimeLeaveInterfe:list')")
    public ResponseEntity getOvertimeLeaveInterfeNoPaging(AcOvertimeLeaveInterfaceQueryCriteria criteria) {
    return new ResponseEntity<>(acOvertimeLeaveInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出oa加班请假统计接口表数据")
    @ApiOperation("导出oa加班请假统计接口表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('overtimeLeaveInterfe:list')")
    public void download(HttpServletResponse response, AcOvertimeLeaveInterfaceQueryCriteria criteria) throws IOException {
        acOvertimeLeaveInterfaceService.download(acOvertimeLeaveInterfaceService.listAll(criteria), response);
    }

    @ErrorLog("导入oa加班请假统计接口表数据")
    @ApiOperation("导入oa加班请假统计接口表数据")
    @PutMapping(value = "/importExcel")
    @PreAuthorize("@el.check('overtimeLeaveInterfe:add')")
    public ResponseEntity importExcel(@RequestBody AcOvertimeImportVo acOvertimeImportVo) {
        return new ResponseEntity<>(acOvertimeLeaveInterfaceService.ImportAcOvertimeLeaveInterfaces(acOvertimeImportVo.getAcOvertimeLeaveInterfaces(), acOvertimeImportVo.getReImportFlag()), HttpStatus.OK);
    }

    @ErrorLog("加班请假预先检验")
    @ApiOperation("加班请假预先检验")
    @PutMapping(value = "/preCheckExcel")
    @PreAuthorize("@el.check('overtimeLeaveInterfe:add')")
    public ResponseEntity preCheckExcel(@RequestBody List<AcOvertimeLeaveInterface> acOvertimeLeaveInterfaces) {
        List<AcOvertimeLeaveInterfaceDTO> overTimeList = acOvertimeLeaveInterfaceService.preCheckOverTime(acOvertimeLeaveInterfaces);
//        overTimeList.sort(Comparator.comparing(AcOvertimeLeaveInterfaceDTO::getPreCheckFlag));
//        overTimeList.forEach(System.out::println);
        return new ResponseEntity<>(overTimeList, HttpStatus.OK);
    }

}
