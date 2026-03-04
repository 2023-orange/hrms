package com.sunten.hrms.re.rest;

import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.re.domain.ReDemandAgreeNum;
import com.sunten.hrms.re.dto.ReDemandAgreeNumQueryCriteria;
import com.sunten.hrms.re.service.ReDemandAgreeNumService;
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


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhoujy
 * @since 2022-11-22
 */
@RestController
@Api(tags = "同意人数子表")
@RequestMapping("/api/re/reDemandAgreeNum")
public class ReDemandAgreeNumController {
    private static final String ENTITY_NAME = "reDemandAgreeNum";
    private final ReDemandAgreeNumService reDemandAgreeNumService;

    public ReDemandAgreeNumController(ReDemandAgreeNumService reDemandAgreeNumService) {
        this.reDemandAgreeNumService = reDemandAgreeNumService;
    }

    @ErrorLog("记录同意人数变更")
    @ApiOperation("记录同意人数变更")
    @PostMapping(value = "/insert")
    public ResponseEntity insert(@RequestBody ReDemandAgreeNum reDemandAgreeNum) {
        reDemandAgreeNumService.insert(reDemandAgreeNum);
        return new ResponseEntity<>("200", HttpStatus.OK);
    }

    @ErrorLog("获取单个")
    @ApiOperation("获取单个")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('reDemandAgreeNum:list')")
    public ResponseEntity getreDemandAgreeNum(@PathVariable Long id) {
        return new ResponseEntity<>(reDemandAgreeNumService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping
    @PreAuthorize("@el.check('reDemandAgreeNum:list')")
    public ResponseEntity getreDemandAgreeNumPage(ReDemandAgreeNumQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reDemandAgreeNumService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询（不分页）")
    @ApiOperation("查询（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('reDemandAgreeNum:list')")
    public ResponseEntity getreDemandAgreeNumNoPaging(ReDemandAgreeNumQueryCriteria criteria) {
    return new ResponseEntity<>(reDemandAgreeNumService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('reDemandAgreeNum:list')")
    public void download(HttpServletResponse response, ReDemandAgreeNumQueryCriteria criteria) throws IOException {
        reDemandAgreeNumService.download(reDemandAgreeNumService.listAll(criteria), response);
    }
}
