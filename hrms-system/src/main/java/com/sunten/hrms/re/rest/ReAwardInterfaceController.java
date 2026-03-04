package com.sunten.hrms.re.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.re.domain.ReAwardInterface;
import com.sunten.hrms.re.dto.ReAwardInterfaceQueryCriteria;
import com.sunten.hrms.re.service.ReAwardInterfaceService;
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
 * 奖罚情况临时表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@RestController
@Api(tags = "奖罚情况临时表")
@RequestMapping("/api/re/awardInterface")
public class ReAwardInterfaceController {
    private static final String ENTITY_NAME = "awardInterface";
    private final ReAwardInterfaceService reAwardInterfaceService;

    public ReAwardInterfaceController(ReAwardInterfaceService reAwardInterfaceService) {
        this.reAwardInterfaceService = reAwardInterfaceService;
    }

    @Log("新增奖罚情况临时表")
    @ApiOperation("新增奖罚情况临时表")
    @PostMapping
    @PreAuthorize("@el.check('awardInterface:add')")
    public ResponseEntity create(@Validated @RequestBody ReAwardInterface awardInterface) {
        if (awardInterface.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(reAwardInterfaceService.insert(awardInterface), HttpStatus.CREATED);
    }

    @Log("删除奖罚情况临时表")
    @ApiOperation("删除奖罚情况临时表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('awardInterface:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reAwardInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该奖罚情况临时表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改奖罚情况临时表")
    @ApiOperation("修改奖罚情况临时表")
    @PutMapping
    @PreAuthorize("@el.check('awardInterface:edit')")
    public ResponseEntity update(@Validated(ReAwardInterface.Update.class) @RequestBody ReAwardInterface awardInterface) {
        reAwardInterfaceService.update(awardInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个奖罚情况临时表")
    @ApiOperation("获取单个奖罚情况临时表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('awardInterface:list')")
    public ResponseEntity getAwardInterface(@PathVariable Long id) {
        return new ResponseEntity<>(reAwardInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询奖罚情况临时表（分页）")
    @ApiOperation("查询奖罚情况临时表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('awardInterface:list')")
    public ResponseEntity getAwardInterfacePage(ReAwardInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reAwardInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询奖罚情况临时表（不分页）")
    @ApiOperation("查询奖罚情况临时表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('awardInterface:list')")
    public ResponseEntity getAwardInterfaceNoPaging(ReAwardInterfaceQueryCriteria criteria) {
        return new ResponseEntity<>(reAwardInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出奖罚情况临时表数据")
    @ApiOperation("导出奖罚情况临时表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('awardInterface:list')")
    public void download(HttpServletResponse response, ReAwardInterfaceQueryCriteria criteria) throws IOException {
        reAwardInterfaceService.download(reAwardInterfaceService.listAll(criteria), response);
    }
}
