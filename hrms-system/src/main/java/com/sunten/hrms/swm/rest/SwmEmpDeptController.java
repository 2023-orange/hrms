package com.sunten.hrms.swm.rest;

import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.domain.SwmEmployee;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmEmpDept;
import com.sunten.hrms.swm.dto.SwmEmpDeptQueryCriteria;
import com.sunten.hrms.swm.service.SwmEmpDeptService;
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
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 薪酬人员管理范围 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-02-24
 */
@RestController
@Api(tags = "薪酬人员管理范围")
@RequestMapping("/api/swm/empDept")
public class SwmEmpDeptController {
    private static final String ENTITY_NAME = "empDept";
    private final SwmEmpDeptService swmEmpDeptService;
    private final FndUserService fndUserService;
    @Value("${swmAuthType.monthlyAssessment}")
    private String monthlyAssessment;

    public SwmEmpDeptController(SwmEmpDeptService swmEmpDeptService, FndUserService fndUserService) {
        this.swmEmpDeptService = swmEmpDeptService;
        this.fndUserService = fndUserService;
    }

    @Log("新增薪酬人员管理范围")
    @ApiOperation("新增薪酬人员管理范围")
    @PostMapping
    @PreAuthorize("@el.check('empDept:add')")
    public ResponseEntity create(@Validated @RequestBody SwmEmpDept empDept) {
        if (empDept.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmEmpDeptService.insert(empDept), HttpStatus.CREATED);
    }

    @Log("删除薪酬人员管理范围")
    @ApiOperation("删除薪酬人员管理范围")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('empDept:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmEmpDeptService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该薪酬人员管理范围存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }
//
//    @Log("修改薪酬人员管理范围")
//    @ApiOperation("修改薪酬人员管理范围")
//    @PutMapping
//    @PreAuthorize("@el.check('empDept:edit')")
//    public ResponseEntity update(@Validated(SwmEmpDept.Update.class) @RequestBody SwmEmpDept empDept) {
//        swmEmpDeptService.update(empDept);
//        return new ResponseEntity(HttpStatus.NO_CONTENT);
//    }


    @ErrorLog("获取单个薪酬人员管理范围")
    @ApiOperation("获取单个薪酬人员管理范围")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('swmEmployee:list')")
    public ResponseEntity getEmpDept(@PathVariable Long id) {
        return new ResponseEntity<>(swmEmpDeptService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询薪酬人员管理范围（分页）")
    @ApiOperation("查询薪酬人员管理范围（分页）")
    @GetMapping
    @PreAuthorize("@el.check('swmEmployee:list')")
    public ResponseEntity getEmpDeptPage(SwmEmpDeptQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmEmpDeptService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询薪酬人员管理范围（不分页）")
    @ApiOperation("查询薪酬人员管理范围（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('swmEmployee:list')")
    public ResponseEntity getEmpDeptNoPaging(SwmEmpDeptQueryCriteria criteria) {
    return new ResponseEntity<>(swmEmpDeptService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出薪酬人员管理范围数据")
    @ApiOperation("导出薪酬人员管理范围数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('empDept:list')")
    public void download(HttpServletResponse response, SwmEmpDeptQueryCriteria criteria) throws IOException {
        swmEmpDeptService.download(swmEmpDeptService.listAll(criteria), response);
    }

    @ErrorLog("根据工号查管辖范围, 返回部门code集合")
    @ApiOperation("根据工号查管辖范围, 返回部门code集合")
    @GetMapping("/getDeptCodeBySeIdAndType")
    @PreAuthorize("@el.check('swmEmployee:list')")
    public ResponseEntity getDeptCodeBySeIdAndType(SwmEmpDeptQueryCriteria criteria) {
        return new ResponseEntity<>(swmEmpDeptService.getDeptCodeList(criteria), HttpStatus.OK);

    }

    @Log("修改薪酬人员范围")
    @ApiOperation("修改薪酬人员范围")
    @PutMapping(value = "/editSwmEmpDeptCode")
    @PreAuthorize("@el.check('swmEmployee:edit')")
    public ResponseEntity editEmpDepts(@RequestBody SwmEmpDept swmEmpDept) {
        swmEmpDeptService.updateEmpDepts(swmEmpDept);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("移除薪酬人员所有管辖范围")
    @ApiOperation("移除薪酬人员所有管辖范围")
    @PutMapping(value = "/removeAllAuth")
    @PreAuthorize("@el.check('swmEmployee:edit')")
    public ResponseEntity removeAllAuth(@RequestBody SwmEmployee swmEmployee) {
        SwmEmpDept swmEmpDept = new SwmEmpDept();
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        swmEmpDept.setUpdateBy(user.getId());
        swmEmpDept.setUpdateTime(LocalDateTime.now());
        swmEmpDept.setType(monthlyAssessment);
        swmEmpDept.setEnabledFlag(true);
        swmEmpDept.setSeId(swmEmployee.getId());
        swmEmpDeptService.updateEnabledBySwmEmployee(swmEmpDept);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
