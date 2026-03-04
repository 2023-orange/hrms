package com.sunten.hrms.tool.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.dto.ToolEmailInterfaceQueryCriteria;
import com.sunten.hrms.tool.service.ToolEmailInterfaceService;
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
 * @author batan
 * @since 2020-10-30
 */
@RestController
@Api(tags = "")
@RequestMapping("/api/tool/emailInterface")
public class ToolEmailInterfaceController {
    private static final String ENTITY_NAME = "emailInterface";
    private final ToolEmailInterfaceService toolEmailInterfaceService;

    public ToolEmailInterfaceController(ToolEmailInterfaceService toolEmailInterfaceService) {
        this.toolEmailInterfaceService = toolEmailInterfaceService;
    }

    @Log("新增EmailInterface")
    @ApiOperation("新增EmailInterface")
    @PostMapping
    @PreAuthorize("@el.check('emailInterface:add')")
    public ResponseEntity create(@Validated @RequestBody ToolEmailInterface emailInterface) {
        if (emailInterface.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(toolEmailInterfaceService.insert(emailInterface), HttpStatus.CREATED);
    }

    @Log("新增EmailInterface并发送")
    @ApiOperation("新增EmailInterface并发送")
    @PostMapping(value = "/send")
    @PreAuthorize("@el.check('emailInterface:add')")
    public ResponseEntity createAndSend(@Validated @RequestBody ToolEmailInterface emailInterface) {
        if (emailInterface.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(toolEmailInterfaceService.sendAndSaveWithThrow(emailInterface, true), HttpStatus.CREATED);
    }

    @Log("删除EmailInterface")
    @ApiOperation("删除EmailInterface")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('emailInterface:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            toolEmailInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该EmailInterface存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改EmailInterface")
    @ApiOperation("修改EmailInterface")
    @PutMapping
    @PreAuthorize("@el.check('emailInterface:edit')")
    public ResponseEntity update(@Validated(ToolEmailInterface.Update.class) @RequestBody ToolEmailInterface emailInterface) {
        toolEmailInterfaceService.update(emailInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("修改EmailInterface并发送")
    @ApiOperation("修改EmailInterface并发送")
    @PutMapping(value = "/send")
    @PreAuthorize("@el.check('emailInterface:edit')")
    public ResponseEntity updateAndSend(@Validated(ToolEmailInterface.Update.class) @RequestBody ToolEmailInterface emailInterface) {
        toolEmailInterfaceService.sendAndSaveWithThrow(emailInterface, false);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个EmailInterface")
    @ApiOperation("获取单个EmailInterface")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('emailInterface:list')")
    public ResponseEntity getEmailInterface(@PathVariable Long id) {
        return new ResponseEntity<>(toolEmailInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询EmailInterface（分页）")
    @ApiOperation("查询EmailInterface（分页）")
    @GetMapping
    @PreAuthorize("@el.check('emailInterface:list')")
    public ResponseEntity getEmailInterfacePage(ToolEmailInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(toolEmailInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询EmailInterface（不分页）")
    @ApiOperation("查询EmailInterface（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('emailInterface:list')")
    public ResponseEntity getEmailInterfaceNoPaging(ToolEmailInterfaceQueryCriteria criteria) {
    return new ResponseEntity<>(toolEmailInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出EmailInterface数据")
    @ApiOperation("导出EmailInterface数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('emailInterface:list')")
    public void download(HttpServletResponse response, ToolEmailInterfaceQueryCriteria criteria) throws IOException {
        toolEmailInterfaceService.download(toolEmailInterfaceService.listAll(criteria), response);
    }
}
