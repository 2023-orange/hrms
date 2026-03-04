package com.sunten.hrms.td.rest;

import com.alibaba.fastjson.JSONObject;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.td.domain.TdCourseware;
import com.sunten.hrms.td.dto.TdCoursewareQueryCriteria;
import com.sunten.hrms.td.service.TdCoursewareService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * <p>
 * 课件资料表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-06-18
 */
@RestController
@Api(tags = "课件资料表")
@RequestMapping("/api/td/courseware")
public class TdCoursewareController {
    private static final String ENTITY_NAME = "courseware";
    @Value("${role.authTrainCharge}")
    private String authTrainCharge;
    private final TdCoursewareService tdCoursewareService;
    private final FndUserService fndUserService;
    private final JwtPermissionService jwtPermissionService;

    public TdCoursewareController(TdCoursewareService tdCoursewareService, FndUserService fndUserService, JwtPermissionService jwtPermissionService) {
        this.tdCoursewareService = tdCoursewareService;
        this.fndUserService = fndUserService;
        this.jwtPermissionService = jwtPermissionService;
    }

    @Log("新增课件资料表")
    @ApiOperation("新增课件资料表")
    @PostMapping
    @PreAuthorize("@el.check('courseware:add')")
    public ResponseEntity create(@Validated @RequestBody TdCourseware courseware) {
        if (courseware.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdCoursewareService.insert(courseware), HttpStatus.CREATED);
    }

    @Log("新增、修改课件资料-附件")
    @ApiOperation("新增、修改课件资料-附件")
    @PostMapping(value = "/attestationCourseware")
    @PreAuthorize("@el.check('courseware:add','courseware:edit')")
    public ResponseEntity addCourseware( @RequestParam("courseware") String courseware,@RequestParam("file") MultipartFile file) {
        JSONObject object = JSONObject.parseObject(courseware);
        String jsonStr = object.toJSONString();
        TdCourseware tdCourseware = JSONObject.parseObject(jsonStr,TdCourseware.class);
        tdCoursewareService.attestationCourseware(tdCourseware,file);
        return new ResponseEntity<>(HttpStatus.OK, HttpStatus.CREATED);
//        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @Log("删除课件资料表")
    @ApiOperation("删除课件资料表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('courseware:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdCoursewareService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该课件资料表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改课件资料表")
    @ApiOperation("修改课件资料表")
    @PutMapping
    @PreAuthorize("@el.check('courseware:edit')")
    public ResponseEntity update(@Validated(TdCourseware.Update.class) @RequestBody TdCourseware courseware) {
        tdCoursewareService.update(courseware);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个课件资料表")
    @ApiOperation("获取单个课件资料表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('courseware:list')")
    public ResponseEntity getCourseware(@PathVariable Long id) {
        return new ResponseEntity<>(tdCoursewareService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询课件资料表（分页）")
    @ApiOperation("查询课件资料表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('courseware:list')")
    public ResponseEntity getCoursewarePage(TdCoursewareQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        // 权限设定：培训管理员查全部、其他人查询公开、课程内的、本人创建
        // 获取登陆人的培训员管辖部门权限
        Boolean adminFlag = false; // 培训管理员标识

        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        criteria.setUserId(user.getId());
        // 判断是否培训专员
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority auth : grantedAuthorityCollection
        ) {
            if (auth.getAuthority().equals(authTrainCharge)) {
                adminFlag = true;
            }
        }
        if (!adminFlag) {
            criteria.setAdminFlag(adminFlag);
            criteria.setEmployeeId(user.getEmployee().getId());
        }
        return new ResponseEntity<>(tdCoursewareService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询课件资料表（不分页）")
    @ApiOperation("查询课件资料表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('courseware:list')")
    public ResponseEntity getCoursewareNoPaging(TdCoursewareQueryCriteria criteria) {
    return new ResponseEntity<>(tdCoursewareService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出课件资料表数据")
    @ApiOperation("导出课件资料表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('courseware:list')")
    public void download(HttpServletResponse response, TdCoursewareQueryCriteria criteria) throws IOException {
        tdCoursewareService.download(tdCoursewareService.listAll(criteria), response);
    }

    @Log("根据OA申请单号获取课件资料审批内容")
    @ApiOperation("根据申请单号获取课件资料审批内容")
    @GetMapping(value = "/getCoursewareDetailByReqCode")
    public ResponseEntity getCoursewareDetailByReqCode(String reqCode) {
        if (null == reqCode || "".equals(reqCode))  {
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(tdCoursewareService.getByOaOrder(reqCode), HttpStatus.CREATED);
    }

    @Log("从OA回填课件资料表审批内容")
    @ApiOperation("从OA回填课件资料表审批内容")
    @PostMapping(value = "/updateHrCourseware")
    public ResponseEntity updateHrCourseware(String reqCode, String approvalStatus, String lastVerdict) {

        if (reqCode == null || "".equals(reqCode)) {
            throw new InfoCheckWarningMessException("OA单号为空");
        }
        if (approvalStatus == null || "".equals(approvalStatus)) {
            throw new InfoCheckWarningMessException("审批意见为空");
        }
        TdCourseware tdCourseware = new TdCourseware();
        tdCourseware.setOaOrder(reqCode);
        tdCourseware.setApprovalStatus(approvalStatus);
        tdCourseware.setLastVerdict(lastVerdict);
        if ("pass".equals(approvalStatus)) {
            tdCourseware.setApprovalEndTime(LocalDateTime.now());
        }
        tdCoursewareService.approval(tdCourseware);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
