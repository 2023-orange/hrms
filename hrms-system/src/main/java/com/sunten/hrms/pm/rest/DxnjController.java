package com.sunten.hrms.pm.rest;

import cn.hutool.json.JSONArray;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.pm.dto.DxnjDTO;
import com.sunten.hrms.utils.PageUtil;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.Dxnj;
import com.sunten.hrms.pm.dto.DxnjQueryCriteria;
import com.sunten.hrms.pm.service.DxnjService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-10-08
 * 带薪年假
 */
@RestController
@Api(tags = "带薪年假")
@RequestMapping("/api/pm/dxnj")
public class DxnjController {
    private static final String ENTITY_NAME = "dxnj";
    private final DxnjService dxnjService;
    private final FndDeptService fndDeptService;
    private final FndDataScope fndDataScope;

    public DxnjController(DxnjService dxnjService, FndDeptService fndDeptService, FndDataScope fndDataScope) {
        this.dxnjService = dxnjService;
        this.fndDeptService = fndDeptService;
        this.fndDataScope = fndDataScope;
    }

    @Log("新增")
    @ApiOperation("新增")
    @PostMapping
    @PreAuthorize("@el.check('dxnj:add')")
    public ResponseEntity create(@Validated @RequestBody Dxnj dxnj) {
        if (dxnj.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(dxnjService.insert(dxnj), HttpStatus.CREATED);
    }

    @Log("删除")
    @ApiOperation("删除")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('dxnj:del')")
    public ResponseEntity delete(@PathVariable Integer id) {
        try {
            dxnjService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改")
    @ApiOperation("修改")
    @PutMapping
    @PreAuthorize("@el.check('dxnj:edit')")
    public ResponseEntity update(@Validated(Dxnj.Update.class) @RequestBody Dxnj dxnj) {
        dxnjService.update(dxnj);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个")
    @ApiOperation("获取单个")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('dxnj:list')")
    public ResponseEntity getdxnj(@PathVariable Integer id) {
        return new ResponseEntity<>(dxnjService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping
    @PreAuthorize("@el.check('dxnj:list')")
    public ResponseEntity getdxnjPage(DxnjQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"D.id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return returnDxnjQueryForAll(criteria, pageable);
    }

    @ErrorLog("查询（不分页）")
    @ApiOperation("查询（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('dxnj:list')")
    public ResponseEntity getdxnjNoPaging(DxnjQueryCriteria criteria) {
        return returnDxnjQueryForAll(criteria, null);
    }

    @Log("批量修改带薪年假")
    @ApiOperation("批量修改带薪年假")
    @PutMapping(value = "/batchEditAnnualLeave")
    @PreAuthorize("@el.check('dxnj:edit')")
    public ResponseEntity batchEditAnnualLeave(@RequestBody List<Dxnj> dxnjs) {
        dxnjService.batchUpdate(dxnjs);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dxnj:list')")
    public void download(HttpServletResponse response, DxnjQueryCriteria criteria) throws IOException {
        dxnjService.download((List<DxnjDTO>)returnDxnjQueryForAll(criteria, null).getBody(), response);
    }

    private ResponseEntity returnDxnjQueryForAll(DxnjQueryCriteria criteria, Pageable pageable) {
//        fndDataScope.setQueryCriteria(criteria);
        if (null != criteria.getDeptId()) {
            Set<Long> deptIds = new HashSet<>(fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptId()));
            criteria.setDeptIds(deptIds);
            criteria.setDeptId(null);
        }
        if (null != pageable){
            return new ResponseEntity<>(dxnjService.listAll(criteria, pageable), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(dxnjService.listAll(criteria), HttpStatus.OK);
        }
    }
}
