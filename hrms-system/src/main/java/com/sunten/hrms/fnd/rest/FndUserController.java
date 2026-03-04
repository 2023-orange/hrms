package com.sunten.hrms.fnd.rest;

import com.sunten.hrms.annotation.AnonymousAccess;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.fnd.dto.FndRoleSmallDTO;
import com.sunten.hrms.fnd.dto.FndUserQueryCriteria;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndRoleService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.fnd.vo.FndUserPassVo;
import com.sunten.hrms.fnd.vo.RsaKeyVo;
import com.sunten.hrms.pm.dao.PmEmployeeDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.service.PmEmployeeService;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.SecurityUtils;
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
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@RestController
@Api(tags = "系统：用户管理")
@RequestMapping("/api/fnd/user")
public class FndUserController {
    private static final String ENTITY_NAME = "user";
    private final FndUserService fndUserService;
    private final FndDataScope fndDataScope;
    private final FndDeptService fndDeptService;
    private final FndRoleService fndRoleService;
    private final PmEmployeeDao pmEmployeeDao;

    public FndUserController(FndUserService fndUserService, FndDataScope fndDataScope, FndDeptService fndDeptService, FndRoleService fndRoleService, PmEmployeeDao pmEmployeeDao) {
        this.fndUserService = fndUserService;
        this.fndDataScope = fndDataScope;
        this.fndDeptService = fndDeptService;
        this.fndRoleService = fndRoleService;
        this.pmEmployeeDao = pmEmployeeDao;
    }

    @Log("新增用户")
    @ApiOperation("新增用户")
    @PostMapping
    @PreAuthorize("@el.check('user:add')")
    public ResponseEntity create(@Validated @RequestBody FndUser user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        checkLevel(user);
        return new ResponseEntity<>(fndUserService.insert(user), HttpStatus.CREATED);
    }

    @Log("删除用户")
    @ApiOperation("删除用户")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('user:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        Integer currentLevel = Collections.min(fndRoleService.listByUserId(SecurityUtils.getUserId()).stream().map(FndRoleSmallDTO::getLevel).collect(Collectors.toList()));
        Integer optLevel = Collections.min(fndRoleService.listByUserId(id).stream().map(FndRoleSmallDTO::getLevel).collect(Collectors.toList()));
        if (currentLevel > optLevel) {
            throw new InfoCheckWarningMessException("角色权限不足");
        }
        fndUserService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改用户")
    @ApiOperation("修改用户")
    @PutMapping
    @PreAuthorize("@el.check('user:edit')")
    public ResponseEntity update(@Validated(FndUser.Update.class) @RequestBody FndUser user) {
        checkLevel(user);
        fndUserService.update(user);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("重置用户密码")
    @ApiOperation("重置用户密码")
    @PutMapping(value = "/resetPass/{username}")
    @PreAuthorize("@el.check('user:edit')")
    public ResponseEntity resetPassword(@PathVariable String username, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        fndUserService.resetPass(username, password);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个用户")
    @ApiOperation("获取单个用户")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity getUser(@PathVariable Long id) {
        return new ResponseEntity<>(fndUserService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询用户（分页）")
    @ApiOperation("查询用户（分页）")
    @GetMapping
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity getUserPage(FndUserQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptId(), null, false, true, criteria.getDeptIds(), null);
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }

        criteria.setDeptIds(dataScopeVo.getDeptIds());
//        criteria.setEmployeeId(dataScopeVo.getEmployeeId());
        return new ResponseEntity<>(fndUserService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("导出用户数据")
    @ApiOperation("导出用户数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('user:list')")
    public void download(HttpServletResponse response, FndUserQueryCriteria criteria) throws IOException {
        fndUserService.download(fndUserService.listAll(criteria), response);
    }

    @ApiOperation("修改密码")
    @PostMapping(value = "/updatePass")
    public ResponseEntity updatePass(@RequestBody FndUserPassVo user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        fndUserService.updatePass(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("修改头像")
    @PostMapping(value = "/updateAvatar")
    public ResponseEntity updateAvatar(@RequestParam MultipartFile file) {
        fndUserService.updateAvatar(file);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改邮箱")
    @ApiOperation("修改邮箱")
    @PostMapping(value = "/updateEmail/{code}")
    public ResponseEntity updateEmail(@PathVariable String code, @RequestBody FndUser user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        fndUserService.updateEmail(code, user);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     *
     * @param resources /
     */
    private void checkLevel(FndUser resources) {
        Integer currentLevel = Collections.min(fndRoleService.listByUserId(SecurityUtils.getUserId()).stream().map(FndRoleSmallDTO::getLevel).collect(Collectors.toList()));
        Integer optLevel = fndRoleService.getByRoles(resources.getRoles());
        if (currentLevel > optLevel) {
            throw new InfoCheckWarningMessException("角色权限不足");
        }
    }

    @Log("获取用户密码")
    @ApiOperation("获取用户密码")
    @PostMapping(value = "/getPass/{id}")
    @PreAuthorize("@el.check('user:edit')")
    public ResponseEntity getDecryptPassword(@PathVariable Long id, @RequestBody RsaKeyVo rsaKey) {
        return new ResponseEntity<>(fndUserService.getDecryptPassword(id, rsaKey.getPrivateKey()), HttpStatus.OK);
    }

    @Log("获取用户信息集合（经理、主管、招聘人员等等）/(人事专员岗位,招聘专员岗位)，用于前台判定或流程发起")
    @ApiOperation("获取用户信息集合（经理、主管、招聘人员等等）/(人事专员岗位,招聘专员岗位)，用于前台判定或流程发起")
    @GetMapping(value = "/getUserMesForSendRequest")
    @PreAuthorize("@el.check('common')")
    public ResponseEntity getUserMesForSendRequest() {
        Map<String, Object> map = fndDataScope.getUserMesForSendRequest(null, true, true, true, true,true,true,true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Log("根据工号获取用户信息集合（经理、主管、招聘人员等等）/(人事专员岗位,招聘专员岗位)，用于前台判定或流程发起")
    @ApiOperation("根据工号获取用户信息集合（经理、主管、招聘人员等等）/(人事专员岗位,招聘专员岗位)，用于前台判定或流程发起")
    @GetMapping(value = "/getUserMesForSendRequestByWorkCard/{workCard}")
    @PreAuthorize("@el.check('common')")
    public ResponseEntity getUserMesForSendRequestByWorkCard(@PathVariable String workCard) {
        Map<String, Object> map = fndDataScope.getUserMesForSendRequest(workCard , true, true, true, true,true,true,true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Log("获取人事、培训、招聘专员集合")
    @ApiOperation("获取人事、培训、招聘专员集合")
    @GetMapping(value = "/getPmTdReCharge")
    @PreAuthorize("@el.check('common')")
    public ResponseEntity getPmTdReCharge() {
        Map<String, Object> map = fndDataScope.getUserMesForSendRequest(null, false, true, true, true, false,true,true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Log("根据工号单独获取主管及经理")
    @ApiOperation("根据工号单独获取主管及经理")
    @GetMapping(value = "/getManagerAndSuperiorByWorkCard/{workCard}")
    @PreAuthorize("@el.check('common')")
    public ResponseEntity getManagerAndSuperiorByWorkCard(@PathVariable String workCard) {
        return new ResponseEntity<>(pmEmployeeDao.getCurrentManagerAndSuperior(fndUserService.getByName(workCard).getId()), HttpStatus.OK);
    }

    @GetMapping("/getDepartmentEmailByWorkCard")
    @ErrorLog("根据工号获取部门领导邮件")
    @ApiOperation("根据工号获取对应的部门领导邮件")
    public ResponseEntity getDepartmentEmailByWorkCard(@RequestParam("workCard") String workCard) {
        String DepartmentEmail = fndUserService.getDepartmentEmailByWorkCard(workCard);
        return new ResponseEntity<>(DepartmentEmail, HttpStatus.OK);
    }

    @ErrorLog("查询用户名及attribute")
    @ApiOperation("查询用户名及attribute")
    @GetMapping(value = "/getEncryptPsd")
    public ResponseEntity getEncryptPsd() {
        return new ResponseEntity<>(fndUserService.getuserNameAndAttribute(), HttpStatus.OK);
    }

    @Log("获取用户信息集合（根据工号或者姓名）")
    @ApiOperation("获取用户信息集合（根据工号或者姓名）")
    @GetMapping(value = "/getUserByEmployee/{str}")
    public ResponseEntity getUserMesForSendRequest(@PathVariable String str) {
        return new ResponseEntity<>(fndUserService.getuserByNameOrWorkCard(str), HttpStatus.OK);
    }

    @Log("系统转接验证请求")
    @ApiOperation("系统转接验证请求")
    @PostMapping(value = "/systemRemote")
    public String systemRemote() {
        return "200";
    }

    @Log("获取人事专员集合")
    @ApiOperation("获取人事专员集合")
    @GetMapping(value = "/getPmCharge")
    @PreAuthorize("@el.check('common')")
    public ResponseEntity getCharge() {
        return new ResponseEntity<>(pmEmployeeDao.getWorkListByRolePermission("PmCharge"), HttpStatus.OK);
    }
}

