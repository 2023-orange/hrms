package com.sunten.hrms.ac.rest;

import com.sunten.hrms.ac.domain.AcFakeRecordSetting;
import com.sunten.hrms.ac.dto.AcFakeRecordSettingQueryCriteria;
import com.sunten.hrms.ac.service.AcFakeRecordSettingService;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
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
 * @author xukai
 * @since 2021-12-22
 */
@RestController
@Api(tags = "")
@RequestMapping("/api/ac/fakeRecordSetting")
public class AcFakeRecordSettingController {
    private static final String ENTITY_NAME = "fakeRecordSetting";
    private final AcFakeRecordSettingService acFakeRecordSettingService;

    public AcFakeRecordSettingController(AcFakeRecordSettingService acFakeRecordSettingService) {
        this.acFakeRecordSettingService = acFakeRecordSettingService;
    }

    @Log("新增")
    @ApiOperation("新增")
    @PostMapping
    @PreAuthorize("@el.check('fakeRecordSetting:add')")
    public ResponseEntity create(@RequestBody AcFakeRecordSetting fakeRecordSetting) {
        return new ResponseEntity<>(acFakeRecordSettingService.insert(fakeRecordSetting), HttpStatus.CREATED);
    }

    @Log("删除")
    @ApiOperation("删除")
    @DeleteMapping(value = "/{username}")
    @PreAuthorize("@el.check('fakeRecordSetting:del')")
    public ResponseEntity delete(@PathVariable String username) {
        try {
            acFakeRecordSettingService.delete(username);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改")
    @ApiOperation("修改")
    @PutMapping
    @PreAuthorize("@el.check('fakeRecordSetting:edit')")
    public ResponseEntity update(@Validated(AcFakeRecordSetting.Update.class) @RequestBody AcFakeRecordSetting fakeRecordSetting) {
        acFakeRecordSettingService.update(fakeRecordSetting);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个")
    @ApiOperation("获取单个")
    @GetMapping(value = "/{username}")
    @PreAuthorize("@el.check('fakeRecordSetting:list')")
    public ResponseEntity getFakeRecordSetting(@PathVariable String username) {
        return new ResponseEntity<>(acFakeRecordSettingService.getByKey(username), HttpStatus.OK);
    }

    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping
    @PreAuthorize("@el.check('fakeRecordSetting:list')")
    public ResponseEntity getFakeRecordSettingPage(AcFakeRecordSettingQueryCriteria criteria, @PageableDefault(value = 9999, sort = {}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(acFakeRecordSettingService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询（不分页）")
    @ApiOperation("查询（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('fakeRecordSetting:list')")
    public ResponseEntity getFakeRecordSettingNoPaging(AcFakeRecordSettingQueryCriteria criteria) {
    return new ResponseEntity<>(acFakeRecordSettingService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('fakeRecordSetting:list')")
    public void download(HttpServletResponse response, AcFakeRecordSettingQueryCriteria criteria) throws IOException {
        acFakeRecordSettingService.download(acFakeRecordSettingService.listAll(criteria), response);
    }


    @Log("修改开关")
    @ApiOperation("修改开关")
    @PutMapping(value="/updateEnabled")
    @PreAuthorize("@el.check('fakeRecordSetting:edit')")
    public ResponseEntity updateEnabled(@Validated(AcFakeRecordSetting.Update.class) @RequestBody AcFakeRecordSetting fakeRecordSetting) {
        acFakeRecordSettingService.updateEnabled(fakeRecordSetting);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
