package com.sunten.hrms.cm.rest;

import com.sunten.hrms.ac.dao.AcEmpDeptsDao;
import com.sunten.hrms.cm.domain.CmDetailHistory;
import com.sunten.hrms.cm.domain.RoleFlag;
import com.sunten.hrms.cm.dto.CmDetailDTO;
import com.sunten.hrms.cm.mapper.CmDetailHistoryDTOMapper;
import com.sunten.hrms.cm.service.CmDetailHistoryService;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndDeptQueryCriteria;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeService;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.cm.domain.CmDetail;
import com.sunten.hrms.cm.dto.CmDetailQueryCriteria;
import com.sunten.hrms.cm.service.CmDetailService;
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
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 工衣明细表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2022-03-24
 */
@RestController
@Api(tags = "工衣明细表")
@RequestMapping("/api/cm/detail")
public class CmDetailController {
    private static final String ENTITY_NAME = "detail";
    private final CmDetailService cmDetailService;
    private final CmDetailHistoryService cmDetailHistoryService;
    private final FndUserService fndUserService;
    private final JwtPermissionService jwtPermissionService;
    private final FndDeptService fndDeptService;
    private final AcEmpDeptsDao acEmpDeptsDao;
    private final FndDataScope fndDataScope;
    private final PmEmployeeService pmEmployeeService;
    private final CmDetailHistoryDTOMapper cmDetailHistoryDTOMapper;
    @Value("${role.authDocumenter}")
    private String authDocumenter;
    @Value("${role.clothesAdmin}")
    private String clothesAdmin;
    @Value("${role.admin}")
    private String admin;

    public CmDetailController(CmDetailService cmDetailService, CmDetailHistoryService cmDetailHistoryService, FndUserService fndUserService, JwtPermissionService jwtPermissionService,
                              FndDeptService fndDeptService, AcEmpDeptsDao acEmpDeptsDao, FndDataScope fndDataScope, PmEmployeeService pmEmployeeService, CmDetailHistoryDTOMapper cmDetailHistoryDTOMapper) {
        this.cmDetailHistoryService = cmDetailHistoryService;
        this.fndUserService = fndUserService;
        this.cmDetailService = cmDetailService;
        this.jwtPermissionService = jwtPermissionService;
        this.fndDeptService = fndDeptService;
        this.acEmpDeptsDao = acEmpDeptsDao;
        this.fndDataScope = fndDataScope;
        this.pmEmployeeService = pmEmployeeService;
        this.cmDetailHistoryDTOMapper = cmDetailHistoryDTOMapper;
    }

    @Log("新增工衣明细表")
    @ApiOperation("新增工衣明细表")
    @PostMapping
    @PreAuthorize("@el.check('cmDetail:add')")
    public ResponseEntity create(@Validated @RequestBody CmDetail detail) {
        if (detail.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(cmDetailService.insert(detail), HttpStatus.CREATED);
    }

    @Log("删除工衣明细表")
    @ApiOperation("删除工衣明细表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('cmDetail:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            cmDetailService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该工衣明细表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改工衣明细表")
    @ApiOperation("修改工衣明细表")
    @PutMapping
    @PreAuthorize("@el.check('cmDetail:edit')")
    public ResponseEntity update(@Validated(CmDetail.Update.class) @RequestBody CmDetail detail) {
        Long detailId=detail.getDetailId();
        Long id = detail.getId();
        CmDetailHistory cmDetailHistory=cmDetailHistoryDTOMapper.toEntity(cmDetailService.getByKey(detail.getId()));
        cmDetailHistory.setDetailId(detailId);
        detail.setId(null);
        cmDetailHistoryService.insertIntoHistory(cmDetailHistory.getDetailId());
        detail.setId(id);
        cmDetailService.update(detail);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个工衣明细表")
    @ApiOperation("获取单个工衣明细表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('cmDetail:list')")
    public ResponseEntity getDetail(@PathVariable Long id) {
        return new ResponseEntity<>(cmDetailService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询工衣明细表（分页）")
    @ApiOperation("查询工衣明细表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('cmDetail:list')")
    public ResponseEntity getDetailPage(CmDetailQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        List<Long> deptIds = null;
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        RoleFlag roleFlag = new RoleFlag();
        checkRole(user, roleFlag);
        if (null != criteria.getDeptId()) {
            deptIds = fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptId());
        }
        if (roleFlag.getClothesAdminFlag() || roleFlag.getAdminFlag()) {
            if (null != deptIds && deptIds.size() > 0) {
                criteria.setDeptIds(new HashSet<>(deptIds));
            }
            return new ResponseEntity<>(cmDetailService.listAll(criteria, pageable), HttpStatus.OK);
        }
        Set<Long> roleDeptIds = null;
        // 资料员数据范围
        roleDeptIds = acEmpDeptsDao.getDeptsByRolePermission(user.getEmployee().getId(), authDocumenter);
        // 获取用户的数据范围
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, false, criteria.getDeptIds(), null);
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            // 无权限, 但是资料员
            if (null != roleDeptIds && roleDeptIds.size() > 0) {
                if (null != deptIds && deptIds.size() > 0) {
                    // 有查询部门，取交集
                    criteria.setDeptIds(roleDeptIds);
                    criteria.setDeptIds(roleDeptIds.stream().filter(deptIds::contains).collect(Collectors.toSet()));
                } else {
                    criteria.setDeptIds(roleDeptIds);
                }
                return new ResponseEntity<>(cmDetailService.listAll(criteria, pageable), HttpStatus.OK);
            } else {
                throw new InfoCheckWarningMessException("没有检测到相关权限");
            }
        } else {
            // 有权限
            Set<Long> target;
            // 标记是否为单人
            boolean checkFlag = false;
            if (null != dataScopeVo.getEmployeeId()) {
                if (null != roleDeptIds && roleDeptIds.size() > 0) {
                    if (null != deptIds && deptIds.size() > 0) {
                        criteria.setDeptIds(roleDeptIds.stream().filter(deptIds::contains).collect(Collectors.toSet()));
                    } else {
                        criteria.setDeptIds(roleDeptIds);
                    }
                } else {
                    criteria.setEmployeeId(dataScopeVo.getEmployeeId());
                }
                return new ResponseEntity<>(cmDetailService.listAll(criteria, pageable), HttpStatus.OK);
            } else {
                target = new HashSet<>(dataScopeVo.getDeptIds());
                if (null != roleDeptIds && roleDeptIds.size() > 0) {
                    target.addAll(roleDeptIds);
                }
                if (null != deptIds && deptIds.size() > 0) {
                    criteria.setDeptIds(target.stream().filter(deptIds::contains).collect(Collectors.toSet()));
                } else {
                    criteria.setDeptIds(target);
                }
                return new ResponseEntity<>(cmDetailService.listAll(criteria, pageable), HttpStatus.OK);
            }
        }
    }

    @ErrorLog("查询工衣明细表（不分页）")
    @ApiOperation("查询工衣明细表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('cmDetail:list')")
    public ResponseEntity getDetailNoPaging(CmDetailQueryCriteria criteria) {
    return new ResponseEntity<>(cmDetailService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出工衣明细表数据")
    @ApiOperation("导出工衣明细表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('cmDetail:list')")
    public void download(HttpServletResponse response, CmDetailQueryCriteria criteria) throws IOException {
        cmDetailService.download(cmDetailService.listAll(criteria), response);
    }

    @ErrorLog("导出制衣表")
    @ApiOperation("导出制衣表")
    @GetMapping(value = "/downloadCmReport")
    @PreAuthorize("@el.check('clothes:make')")
    public void downloadCmReport(HttpServletResponse response) throws IOException {
        cmDetailService.downloadForYear(response);
    }

//    @ErrorLog("导出制衣数据")
//    @ApiOperation("导出制衣数据")
//    @GetMapping(value = "/downloadCmSearch")
//    public void downloadCmSearch(HttpServletResponse response, CmDetailQueryCriteria criteria) throws IOException {
//        List<Long> deptIds = null;
//        Set<Long> roleDeptIds = null;
//        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
//        // 资料员数据范围
//        roleDeptIds = acEmpDeptsDao.getDeptsByRolePermission(user.getEmployee().getId(), authDocumenter);
//        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptAllId(), null, false, true, criteria.getDeptIds(), criteria.getEmployeeId());
//        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
//        }
//        if (null != roleDeptIds && roleDeptIds.size() > 0) {
//            // 有查询部门，取交集
//            criteria.setDeptIds(roleDeptIds);
//        }
//         else { criteria.setDeptIds(dataScopeVo.getDeptIds());
//            }
//        cmDetailService.downloadForSearch(cmDetailService.listAllForSearch(criteria), response);
//    }



    @ErrorLog("导出制衣数据")
    @ApiOperation("导出制衣数据")
    @GetMapping(value = "/downloadCmSearch")
    public void downloadCmSearch(HttpServletResponse response, CmDetailQueryCriteria criteria) throws IOException {
        List<Long> deptIds = null;
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        RoleFlag roleFlag = new RoleFlag();
        checkRole(user, roleFlag);
        if (null != criteria.getDeptId()) {
            deptIds = fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptId());
        }
        if (roleFlag.getClothesAdminFlag() || roleFlag.getAdminFlag()) {
            if (null != deptIds && deptIds.size() > 0) {
                criteria.setDeptIds(new HashSet<>(deptIds));
            }
            cmDetailService.downloadForSearch(cmDetailService.listAllForSearch(criteria), response);
            return;
        }
        Set<Long> roleDeptIds = null;
        // 资料员数据范围
        roleDeptIds = acEmpDeptsDao.getDeptsByRolePermission(user.getEmployee().getId(), authDocumenter);
        // 获取用户的数据范围
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, false, criteria.getDeptIds(), null);
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            // 无权限, 但是资料员
            if (null != roleDeptIds && roleDeptIds.size() > 0) {
                if (null != deptIds && deptIds.size() > 0) {
                    // 有查询部门，取交集
                    criteria.setDeptIds(roleDeptIds);
                    criteria.setDeptIds(roleDeptIds.stream().filter(deptIds::contains).collect(Collectors.toSet()));
                } else {
                    criteria.setDeptIds(roleDeptIds);
                }
                cmDetailService.downloadForSearch(cmDetailService.listAllForSearch(criteria), response);
                return;
            } else {
                throw new InfoCheckWarningMessException("没有检测到相关权限");
            }
        } else {
            // 有权限
            Set<Long> target;
            // 标记是否为单人
            boolean checkFlag = false;
            if (null != dataScopeVo.getEmployeeId()) {
                if (null != roleDeptIds && roleDeptIds.size() > 0) {
                    if (null != deptIds && deptIds.size() > 0) {
                        criteria.setDeptIds(roleDeptIds.stream().filter(deptIds::contains).collect(Collectors.toSet()));
                    } else {
                        criteria.setDeptIds(roleDeptIds);
                    }
                } else {
                    criteria.setEmployeeId(dataScopeVo.getEmployeeId());
                }
                cmDetailService.downloadForSearch(cmDetailService.listAllForSearch(criteria), response);
                return;
            } else {
                target = new HashSet<>(dataScopeVo.getDeptIds());
                if (null != roleDeptIds && roleDeptIds.size() > 0) {
                    target.addAll(roleDeptIds);
                }
                if (null != deptIds && deptIds.size() > 0) {
                    criteria.setDeptIds(target.stream().filter(deptIds::contains).collect(Collectors.toSet()));
                } else {
                    criteria.setDeptIds(target);
                }
                cmDetailService.downloadForSearch(cmDetailService.listAllForSearch(criteria), response);
                return;
            }
        }
    }


    @ErrorLog("检测导出制衣前是否有符合的数据")
    @ApiOperation("检测导出制衣前是否有符合的数据")
    @GetMapping(value = "/checkBeforeDownloadCmReport")
    @PreAuthorize("@el.check('clothes:make')")
    public ResponseEntity checkBeforeDownloadCmReport() {
        return new ResponseEntity<>(cmDetailService.checkBeforeDownloadCmReport(), HttpStatus.OK);
    }



    @ErrorLog("根据工衣管理权限查询人事档案")
    @ApiOperation("根据工衣管理权限查询人事档案")
    @GetMapping("/getEmployeesByCmUser")
    @PreAuthorize("@el.check('cmDetail:list')")
    public ResponseEntity getEmployeesByCmUser( PmEmployeeQueryCriteria criteria) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        RoleFlag roleFlag = new RoleFlag();
        checkRole(user, roleFlag);
        Set<Long> roleDeptIds = null;
        roleDeptIds = acEmpDeptsDao.getDeptsByRolePermission(user.getEmployee().getId(), authDocumenter);
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, false, new HashSet<>(), null);
        fndDataScope.isNoDataPermission(dataScopeVo);
        if (null == roleDeptIds) {
            if (null != dataScopeVo.getDeptIds() && dataScopeVo.getDeptIds().size() > 0) {
                roleDeptIds = dataScopeVo.getDeptIds();
            }
        } else {
            if (null != dataScopeVo.getDeptIds() && dataScopeVo.getDeptIds().size() > 0) {
                roleDeptIds.addAll(dataScopeVo.getDeptIds());
            }
        }
        if (null != roleDeptIds) {
            criteria.setDeptIds(roleDeptIds);
            return new ResponseEntity<>(pmEmployeeService.listAll(criteria), HttpStatus.OK);
        } else {
            throw new InfoCheckWarningMessException("工衣权限查询人事档案错误");
        }
    }

    @ErrorLog("返回单个标记，判定是否只能个人增加")
    @ApiOperation("返回单个标记，判定是否只能个人增加")
    @GetMapping("/getSelfFlag")
    @PreAuthorize("@el.check('cmDetail:add')")
    public ResponseEntity getSeflFlag() {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Set<Long> roleDeptIds = acEmpDeptsDao.getDeptsByRolePermission(user.getEmployee().getId(), authDocumenter);
        RoleFlag roleFlag = new RoleFlag();
        checkRole(user, roleFlag);
        if (roleFlag.getAdminFlag() || roleFlag.getClothesAdminFlag() || roleDeptIds.size() > 0) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        } else {
            FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, false, new HashSet<>(), null);
            if (fndDataScope.isNoDataPermission(dataScopeVo)) {
                throw new InfoCheckWarningMessException("用户权限异常");
            } else {
                if (null != dataScopeVo.getDeptIds() && dataScopeVo.getDeptIds().size() > 0) {
                    return new ResponseEntity<>(false, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(true, HttpStatus.OK);
                }
            }
        }

    }

    @ErrorLog("建立工衣管理部门树")
    @ApiOperation("建立工衣管理部门树")
    @GetMapping(value = "/getCmTree")
    @PreAuthorize("@el.check('cmDetail:list')")
    public ResponseEntity getCmTree () {
        FndDeptQueryCriteria fndDeptQueryCriteria = new FndDeptQueryCriteria();
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        RoleFlag roleFlag = new RoleFlag();
        checkRole(user, roleFlag);
        if (roleFlag.getClothesAdminFlag() || roleFlag.getAdminFlag()) {
            return new ResponseEntity<>(fndDeptService.buildAcTree(fndDeptService.listAll(fndDeptQueryCriteria)), HttpStatus.OK);
        } else {
            Set<Long> targetIds = new HashSet<>();
            if (null != fndDataScope.getDeptIds() && fndDataScope.getDeptIds().size() > 0) {
                targetIds.addAll(fndDataScope.getDeptIds());
            }
            Set<Long> roleDeptIds = acEmpDeptsDao.getDeptsByRolePermission(user.getEmployee().getId(), authDocumenter);
            targetIds.addAll(roleDeptIds);
            targetIds.add(user.getDept().getId());
            fndDeptQueryCriteria.setIds(targetIds);
            return new ResponseEntity<>(fndDeptService.buildAcTree(fndDeptService.listAll(fndDeptQueryCriteria)), HttpStatus.OK);
        }
    }

    @ErrorLog("工衣管理返回角色明细")
    @ApiOperation("工衣管理返回角色明细")
    @GetMapping(value = "/getCmRoleDetail")
    @PreAuthorize("@el.check('cmDetail:list')")
    public ResponseEntity getCmRoleDetail() {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        RoleFlag roleFlag = new RoleFlag();
        checkRole(user, roleFlag);
        Set<Long> roleDeptIds = acEmpDeptsDao.getDeptsByRolePermission(user.getEmployee().getId(), authDocumenter);
        roleFlag.setDocFlag(roleDeptIds.size() > 0);
        return new ResponseEntity<>(roleFlag, HttpStatus.OK);
    }

    @ErrorLog("检验是否为领导")
    @ApiOperation("检验是否为领导")
    @GetMapping(value = "/getCmLeaderFlag")
    @PreAuthorize("@el.check('cmDetail:list')")
    public ResponseEntity getCmLeaderFlag() {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        return new ResponseEntity<>(fndDataScope.checkIsLeader(user), HttpStatus.OK);
    }

    /**
     *  @author liangjw
     *  @since 2022/4/8 11:28
     *  由于是资料员判断控制的按钮，没有对应的preAuthorize
     */
    @Log("资料员批量审核工衣")
    @ApiOperation("资料员批量审核工衣")
    @PutMapping(value = "/cmBatchDisposeForDoc")
    @PreAuthorize("@el.check('cmDetail:list')")
    public ResponseEntity cmBatchDisposeForDoc(@RequestBody List<CmDetail> cmDetails) {
        cmDetailService.cmBatchDisposeForDoc(cmDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("获取今年的领导工衣列表")
    @ApiOperation("获取今年的领导工衣列表")
    @GetMapping(value = "/getNowYearLeaderCloth")
    @PreAuthorize("@el.check('cmDetail:list')")
    public ResponseEntity getNowYearLeaderCloth(CmDetailQueryCriteria criteria) {
        criteria.setYear(LocalDate.now().getYear());
        criteria.setEnabledFlag(true);
        criteria.setLeaveFlag(false);
//        criteria.setColName("leader_flag");
//        criteria.setSymbol("=");
//        criteria.setValue("true");
        return new ResponseEntity<>(cmDetailService.listAll(criteria), HttpStatus.OK);
    }



    private void checkRole(FndUserDTO user, RoleFlag roleFlag) {
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority auth : grantedAuthorityCollection
        ) {
            if (auth.getAuthority().equals(clothesAdmin)) {
                roleFlag.setClothesAdminFlag(true);
            }
            if (auth.getAuthority().equals(admin)) {
                roleFlag.setAdminFlag(true);
            }
        }
    }

}
