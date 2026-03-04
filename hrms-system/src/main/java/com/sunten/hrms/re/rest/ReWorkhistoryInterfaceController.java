package com.sunten.hrms.re.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.re.domain.ReWorkhistoryInterface;
import com.sunten.hrms.re.dto.ReWorkhistoryInterfaceQueryCriteria;
import com.sunten.hrms.re.service.ReWorkhistoryInterfaceService;
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
 * 工作经历临时表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@RestController
@Api(tags = "工作经历临时表")
@RequestMapping("/api/re/workhistoryInterface")
public class ReWorkhistoryInterfaceController {
    private static final String ENTITY_NAME = "workhistoryInterface";
    private final ReWorkhistoryInterfaceService reWorkhistoryInterfaceService;

    public ReWorkhistoryInterfaceController(ReWorkhistoryInterfaceService reWorkhistoryInterfaceService) {
        this.reWorkhistoryInterfaceService = reWorkhistoryInterfaceService;
    }

    @Log("新增工作经历临时表")
    @ApiOperation("新增工作经历临时表")
    @PostMapping
    @PreAuthorize("@el.check('workhistoryInterface:add')")
    public ResponseEntity create(@Validated @RequestBody ReWorkhistoryInterface workhistoryInterface) {
        if (workhistoryInterface.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(reWorkhistoryInterfaceService.insert(workhistoryInterface), HttpStatus.CREATED);
    }

    @Log("删除工作经历临时表")
    @ApiOperation("删除工作经历临时表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('workhistoryInterface:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reWorkhistoryInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该工作经历临时表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改工作经历临时表")
    @ApiOperation("修改工作经历临时表")
    @PutMapping
    @PreAuthorize("@el.check('workhistoryInterface:edit')")
    public ResponseEntity update(@Validated(ReWorkhistoryInterface.Update.class) @RequestBody ReWorkhistoryInterface workhistoryInterface) {
        reWorkhistoryInterfaceService.update(workhistoryInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个工作经历临时表")
    @ApiOperation("获取单个工作经历临时表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('workhistoryInterface:list')")
    public ResponseEntity getWorkhistoryInterface(@PathVariable Long id) {
        return new ResponseEntity<>(reWorkhistoryInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询工作经历临时表（分页）")
    @ApiOperation("查询工作经历临时表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('workhistoryInterface:list')")
    public ResponseEntity getWorkhistoryInterfacePage(ReWorkhistoryInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reWorkhistoryInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询工作经历临时表（不分页）")
    @ApiOperation("查询工作经历临时表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('workhistoryInterface:list')")
    public ResponseEntity getWorkhistoryInterfaceNoPaging(ReWorkhistoryInterfaceQueryCriteria criteria) {
        return new ResponseEntity<>(reWorkhistoryInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出工作经历临时表数据")
    @ApiOperation("导出工作经历临时表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('workhistoryInterface:list')")
    public void download(HttpServletResponse response, ReWorkhistoryInterfaceQueryCriteria criteria) throws IOException {
        reWorkhistoryInterfaceService.download(reWorkhistoryInterfaceService.listAll(criteria), response);
    }
}
