package com.sunten.hrms.ac.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.KqCrjsj;
import com.sunten.hrms.ac.dto.KqCrjsjQueryCriteria;
import com.sunten.hrms.ac.service.KqCrjsjService;
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
@RequestMapping("/api/ac/kqCrjsj")
public class KqCrjsjController {
    private static final String ENTITY_NAME = "kqCrjsj";
    private final KqCrjsjService kqCrjsjService;

    public KqCrjsjController(KqCrjsjService kqCrjsjService) {
        this.kqCrjsjService = kqCrjsjService;
    }

    @Log("新增kqCrjsj")
    @ApiOperation("新增kqCrjsj")
    @PostMapping
    @PreAuthorize("@el.check('kqCrjsj:add')")
    public ResponseEntity create(@Validated @RequestBody KqCrjsj kqCrjsj) {
        return new ResponseEntity<>(kqCrjsjService.insert(kqCrjsj), HttpStatus.CREATED);
    }

    @Log("删除kqCrjsj")
    @ApiOperation("删除kqCrjsj")
    @DeleteMapping(value = "")
    @PreAuthorize("@el.check('kqCrjsj:del')")
    public ResponseEntity delete() {
        try {
            kqCrjsjService.delete();
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该kqCrjsj存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改kqCrjsj")
    @ApiOperation("修改kqCrjsj")
    @PutMapping
    @PreAuthorize("@el.check('kqCrjsj:edit')")
    public ResponseEntity update(@Validated(KqCrjsj.Update.class) @RequestBody KqCrjsj kqCrjsj) {
        kqCrjsjService.update(kqCrjsj);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

//    @ErrorLog("获取单个kqCrjsj")
//    @ApiOperation("获取单个kqCrjsj")
//    @GetMapping(value = "")
//    @PreAuthorize("@el.check('kqCrjsj:list')")
//    public ResponseEntity getkqCrjsj() {
//        return new ResponseEntity<>(kqCrjsjService.getByKey(), HttpStatus.OK);
//    }

    @ErrorLog("查询kqCrjsj（分页）")
    @ApiOperation("查询kqCrjsj（分页）")
    @GetMapping
    @PreAuthorize("@el.check('kqCrjsj:list')")
    public ResponseEntity getkqCrjsjPage(KqCrjsjQueryCriteria criteria, @PageableDefault(value = 9999, sort = {}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(kqCrjsjService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询kqCrjsj（不分页）")
    @ApiOperation("查询kqCrjsj（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('kqCrjsj:list')")
    public ResponseEntity getkqCrjsjNoPaging(KqCrjsjQueryCriteria criteria) {
    return new ResponseEntity<>(kqCrjsjService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出kqCrjsj数据")
    @ApiOperation("导出kqCrjsj数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('kqCrjsj:list')")
    public void download(HttpServletResponse response, KqCrjsjQueryCriteria criteria) throws IOException {
        kqCrjsjService.download(kqCrjsjService.listAll(criteria), response);
    }
}
