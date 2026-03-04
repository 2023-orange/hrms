package com.sunten.hrms.td.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.td.domain.TdTrainSub;
import com.sunten.hrms.td.dto.TdTrainSubQueryCriteria;
import com.sunten.hrms.td.service.TdTrainSubService;
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
import java.util.List;

/**
 * <p>
 * 参加培训人员情况 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "参加培训人员情况")
@RequestMapping("/api/td/trainSub")
public class TdTrainSubController {
    private static final String ENTITY_NAME = "trainSub";
    private final TdTrainSubService tdTrainSubService;

    public TdTrainSubController(TdTrainSubService tdTrainSubService) {
        this.tdTrainSubService = tdTrainSubService;
    }

    @Log("新增参加培训人员情况")
    @ApiOperation("新增参加培训人员情况")
    @PostMapping
    @PreAuthorize("@el.check('train:add')")
    public ResponseEntity create(@Validated @RequestBody TdTrainSub trainSub) {
        if (trainSub.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdTrainSubService.insert(trainSub), HttpStatus.CREATED);
    }

    @Log("删除参加培训人员情况")
    @ApiOperation("删除参加培训人员情况")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('train:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdTrainSubService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该参加培训人员情况存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改参加培训人员情况")
    @ApiOperation("修改参加培训人员情况")
    @PutMapping
    @PreAuthorize("@el.check('train:edit')")
    public ResponseEntity update(@Validated(TdTrainSub.Update.class) @RequestBody TdTrainSub trainSub) {
        tdTrainSubService.update(trainSub);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个参加培训人员情况")
    @ApiOperation("获取单个参加培训人员情况")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('train:list','employee:list')")
    public ResponseEntity getTrainSub(@PathVariable Long id) {
        return new ResponseEntity<>(tdTrainSubService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询参加培训人员情况（分页）")
    @ApiOperation("查询参加培训人员情况（分页）")
    @GetMapping
    @PreAuthorize("@el.check('train:list','employee:list')")
    public ResponseEntity getTrainSubPage(TdTrainSubQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(tdTrainSubService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询参加培训人员情况（不分页）")
    @ApiOperation("查询参加培训人员情况（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('train:list','employee:list')")
    public ResponseEntity getTrainSubNoPaging(TdTrainSubQueryCriteria criteria) {
        return new ResponseEntity<>(tdTrainSubService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出参加培训人员情况数据")
    @ApiOperation("导出参加培训人员情况数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('train:list','employee:list')")
    public void download(HttpServletResponse response, TdTrainSubQueryCriteria criteria) throws IOException {
        tdTrainSubService.download(tdTrainSubService.listAll(criteria), response);
    }

    @Log("批量编辑参加培训人员情况")
    @ApiOperation("批量编辑参加培训人员情况")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('train:add','train:edit')")
    public ResponseEntity batchSave(@Validated @RequestBody List<TdTrainSub> trainSubs) {
        if (trainSubs == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(tdTrainSubService.batchInsert(trainSubs), HttpStatus.CREATED);
    }
}
