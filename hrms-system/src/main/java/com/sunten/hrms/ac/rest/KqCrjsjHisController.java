package com.sunten.hrms.ac.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.KqCrjsjHis;
import com.sunten.hrms.ac.dto.KqCrjsjHisQueryCriteria;
import com.sunten.hrms.ac.service.KqCrjsjHisService;
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
 *  前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-10-19
 */
@RestController
@Api(tags = "")
@RequestMapping("/api/ac/kqCrjsjHis")
public class KqCrjsjHisController {
    private static final String ENTITY_NAME = "kqCrjsjHis";
    private final KqCrjsjHisService kqCrjsjHisService;

    public KqCrjsjHisController(KqCrjsjHisService kqCrjsjHisService) {
        this.kqCrjsjHisService = kqCrjsjHisService;
    }

    @Log("新增kqCrjsjHis")
    @ApiOperation("新增kqCrjsjHis")
    @PostMapping
    @PreAuthorize("@el.check('kqCrjsjHis:add')")
    public ResponseEntity create(@Validated @RequestBody KqCrjsjHis kqCrjsjHis) {
        return new ResponseEntity<>(kqCrjsjHisService.insert(kqCrjsjHis), HttpStatus.CREATED);
    }

    @Log("删除kqCrjsjHis")
    @ApiOperation("删除kqCrjsjHis")
    @DeleteMapping(value = "")
    @PreAuthorize("@el.check('kqCrjsjHis:del')")
    public ResponseEntity delete() {
        try {
            kqCrjsjHisService.delete();
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该kqCrjsjHis存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改kqCrjsjHis")
    @ApiOperation("修改kqCrjsjHis")
    @PutMapping
    @PreAuthorize("@el.check('kqCrjsjHis:edit')")
    public ResponseEntity update(@Validated(KqCrjsjHis.Update.class) @RequestBody KqCrjsjHis kqCrjsjHis) {
        kqCrjsjHisService.update(kqCrjsjHis);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

//    @ErrorLog("获取单个kqCrjsjHis")
//    @ApiOperation("获取单个kqCrjsjHis")
//    @GetMapping(value = "")
//    @PreAuthorize("@el.check('kqCrjsjHis:list')")
//    public ResponseEntity getkqCrjsjHis() {
//        return new ResponseEntity<>(kqCrjsjHisService.getByKey(), HttpStatus.OK);
//    }

    @ErrorLog("查询kqCrjsjHis（分页）")
    @ApiOperation("查询kqCrjsjHis（分页）")
    @GetMapping
    @PreAuthorize("@el.check('kqCrjsjHis:list')")
    public ResponseEntity getkqCrjsjHisPage(KqCrjsjHisQueryCriteria criteria, @PageableDefault(value = 9999, sort = {}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(kqCrjsjHisService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询kqCrjsjHis（不分页）")
    @ApiOperation("查询kqCrjsjHis（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('kqCrjsjHis:list')")
    public ResponseEntity getkqCrjsjHisNoPaging(KqCrjsjHisQueryCriteria criteria) {
    return new ResponseEntity<>(kqCrjsjHisService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出kqCrjsjHis数据")
    @ApiOperation("导出kqCrjsjHis数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('kqCrjsjHis:list')")
    public void download(HttpServletResponse response, KqCrjsjHisQueryCriteria criteria) throws IOException {
        kqCrjsjHisService.download(kqCrjsjHisService.listAll(criteria), response);
    }
}
