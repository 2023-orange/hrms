package com.sunten.hrms.swm.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmBonus;
import com.sunten.hrms.swm.dto.SwmBonusQueryCriteria;
import com.sunten.hrms.swm.service.SwmBonusService;
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
 * 奖金表	    前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@RestController
@Api(tags = "奖金表	   ")
@RequestMapping("/api/swm/bonus")
public class SwmBonusController {
    private static final String ENTITY_NAME = "bonus";
    private final SwmBonusService swmBonusService;

    public SwmBonusController(SwmBonusService swmBonusService) {
        this.swmBonusService = swmBonusService;
    }

    @Log("新增奖金表	   ")
    @ApiOperation("新增奖金表	   ")
    @PostMapping
    @PreAuthorize("@el.check('bonus:add')")
    public ResponseEntity create(@Validated @RequestBody SwmBonus bonus) {
        if (bonus.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmBonusService.insert(bonus), HttpStatus.CREATED);
    }

    @Log("删除奖金表	   ")
    @ApiOperation("删除奖金表	   ")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('bonus:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        swmBonusService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }


    @Log("修改奖金表	   ")
    @ApiOperation("修改奖金表	   ")
    @PutMapping
    @PreAuthorize("@el.check('bonus:edit')")
    public ResponseEntity update(@Validated(SwmBonus.Update.class) @RequestBody SwmBonus bonus) {
        swmBonusService.update(bonus);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个奖金表	   ")
    @ApiOperation("获取单个奖金表	   ")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('bonus:list')")
    public ResponseEntity getBonus(@PathVariable Long id) {
        return new ResponseEntity<>(swmBonusService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询奖金表	   （分页）")
    @ApiOperation("查询奖金表	   （分页）")
    @GetMapping
    @PreAuthorize("@el.check('bonus:list')")
    public ResponseEntity getBonusPage(SwmBonusQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(swmBonusService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询奖金表	   （不分页）")
    @ApiOperation("查询奖金表	   （不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('bonus:list')")
    public ResponseEntity getBonusNoPaging(SwmBonusQueryCriteria criteria) {
    return new ResponseEntity<>(swmBonusService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出奖金表	   数据")
    @ApiOperation("导出奖金表	   数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('bonus:list')")
    public void download(HttpServletResponse response, SwmBonusQueryCriteria criteria) throws IOException {
        swmBonusService.download(swmBonusService.listAll(criteria), response);
    }
}
