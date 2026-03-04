package com.sunten.hrms.tool.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.tool.domain.ToolEmailServer;
import com.sunten.hrms.tool.dto.ToolEmailServerQueryCriteria;
import com.sunten.hrms.tool.service.ToolEmailServerService;
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
 * 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-11-02
 */
@RestController
@Api(tags = "")
@RequestMapping("/api/tool/emailServer")
public class ToolEmailServerController {
    private static final String ENTITY_NAME = "emailServer";
    private final ToolEmailServerService toolEmailServerService;

    public ToolEmailServerController(ToolEmailServerService toolEmailServerService) {
        this.toolEmailServerService = toolEmailServerService;
    }

    @Log("新增EmailServer")
    @ApiOperation("新增EmailServer")
    @PostMapping
    @PreAuthorize("@el.check('emailServer:add')")
    public ResponseEntity create(@Validated @RequestBody ToolEmailServer emailServer) {
        if (emailServer.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(toolEmailServerService.insert(emailServer), HttpStatus.CREATED);
    }

    @Log("删除EmailServer")
    @ApiOperation("删除EmailServer")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('emailServer:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            toolEmailServerService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该EmailServer存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改EmailServer")
    @ApiOperation("修改EmailServer")
    @PutMapping
    @PreAuthorize("@el.check('emailServer:edit')")
    public ResponseEntity update(@Validated(ToolEmailServer.Update.class) @RequestBody ToolEmailServer emailServer) {
        toolEmailServerService.update(emailServer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个EmailServer")
    @ApiOperation("获取单个EmailServer")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('emailServer:list')")
    public ResponseEntity getEmailServer(@PathVariable Long id) {
        return new ResponseEntity<>(toolEmailServerService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询EmailServer（分页）")
    @ApiOperation("查询EmailServer（分页）")
    @GetMapping
    @PreAuthorize("@el.check('emailServer:list')")
    public ResponseEntity getEmailServerPage(ToolEmailServerQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(toolEmailServerService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询EmailServer（不分页）")
    @ApiOperation("查询EmailServer（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('emailServer:list')")
    public ResponseEntity getEmailServerNoPaging(ToolEmailServerQueryCriteria criteria) {
        return new ResponseEntity<>(toolEmailServerService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出EmailServer数据")
    @ApiOperation("导出EmailServer数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('emailServer:list')")
    public void download(HttpServletResponse response, ToolEmailServerQueryCriteria criteria) throws IOException {
        toolEmailServerService.download(toolEmailServerService.listAll(criteria), response);
    }
}
