package com.sunten.hrms.fnd.rest;

import com.sunten.hrms.ac.dao.AcEmpDeptsDao;
import com.sunten.hrms.ac.domain.AcEmpDepts;
import com.sunten.hrms.ac.dto.AcEmpDeptsDTO;
import com.sunten.hrms.ac.dto.AcEmpDeptsQueryCriteria;
import com.sunten.hrms.ac.service.AcEmpDeptsService;
import com.sunten.hrms.annotation.AnonymousAccess;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.domain.FndJob;
import com.sunten.hrms.fnd.dto.*;
import com.sunten.hrms.fnd.mapper.FndDeptMapper;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dto.PmEmployeeJobDTO;
import com.sunten.hrms.pm.service.PmEmployeeJobService;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.service.FndDeptService;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@RestController
@Api(tags = "系统：部门管理")
@RequestMapping("/api/fnd/dept")
public class FndDeptController {
    private final String[] scopeType = {"全部", "本级", "本级及以下", "本人", "自定义"};
    private static final String ENTITY_NAME = "dept";
    private final FndDeptService fndDeptService;
    private final FndDataScope fndDataScope;
    private final AcEmpDeptsService acEmpDeptsService;
    private final FndUserService fndUserService;
    private final JwtPermissionService jwtPermissionService;
    private final PmEmployeeJobService pmEmployeeJobService;
    private final AcEmpDeptsDao acEmpDeptsDao;
    @Value("${role.authRegime}")
    private String authRegime;
    @Value("${role.authTeam}")
    private String authTeam;
    private FndDeptMapper fndDeptMapper;


    public FndDeptController(FndDeptService fndDeptService, FndDataScope fndDataScope, AcEmpDeptsService acEmpDeptsService,
                             JwtPermissionService jwtPermissionService, PmEmployeeJobService pmEmployeeJobService,
                             FndUserService fndUserService, AcEmpDeptsDao acEmpDeptsDao,
                             FndDeptMapper fndDeptMapper) {
        this.fndDeptService = fndDeptService;
        this.fndDataScope = fndDataScope;
        this.acEmpDeptsService = acEmpDeptsService;
        this.fndUserService = fndUserService;
        this.jwtPermissionService = jwtPermissionService;
        this.pmEmployeeJobService = pmEmployeeJobService;
        this.acEmpDeptsDao = acEmpDeptsDao;
        this.fndDeptMapper = fndDeptMapper;
    }

    @Log("新增部门")
    @ApiOperation("新增部门")
    @PostMapping
    @PreAuthorize("@el.check('dept:add')")
    public ResponseEntity create(@Validated @RequestBody FndDept dept) {
        if (dept.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(fndDeptService.insert(dept), HttpStatus.CREATED);
    }

    @Log("删除部门")
    @ApiOperation("删除部门")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('dept:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            fndDeptService.delete(id);
        } catch (Throwable e) {
            e.printStackTrace();
            ThrowableUtil.throwForeignKeyException(e, "该部门存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改部门")
    @ApiOperation("修改部门")
    @PutMapping
    @PreAuthorize("@el.check('dept:edit')")
    public ResponseEntity update(@Validated(FndDept.Update.class) @RequestBody FndDept dept) {
        fndDeptService.update(dept);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个部门")
    @ApiOperation("获取单个部门")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('user:list','dept:list')")
    public ResponseEntity getDept(@PathVariable Long id){
        return new ResponseEntity<>(fndDeptService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询部门（分页）")
    @ApiOperation("查询部门（分页）")
    @GetMapping(value = "/page")
    @PreAuthorize("@el.check('user:list','dept:list')")
    @ResponseBody
    public ResponseEntity getDeptPage(FndDeptQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable){
        criteria.setIds(fndDataScope.getDeptIds());
        return new ResponseEntity<>(fndDeptService.listAll(criteria,pageable), HttpStatus.OK);
    }

    @ErrorLog("查询部门（树结构）")
    @ApiOperation("查询部门（树结构）")
    @GetMapping
    @PreAuthorize("@el.check('user:list','dept:list','employee:list','train:list', 'consolationMoney:list')")
    public ResponseEntity getDepts(FndDeptQueryCriteria criteria){
        criteria.setIds(fndDataScope.getDeptIds(true, true));
        return setCriteriaIds(criteria);
    }

    @ErrorLog("查询所有部门（树结构）")
    @ApiOperation("查询所有部门（树结构）")
    @GetMapping(value = "/getAllDepts")
    @AnonymousAccess
    public ResponseEntity getAllDepts(FndDeptQueryCriteria criteria){
        return setCriteriaIds(criteria);
    }

    @ErrorLog("查询所有部门（树结构,特殊树,培训计划专用）")
    @ApiOperation("查询所有部门（树结构,特殊树,培训计划专用）")
    @GetMapping(value = "/getAllDeptsForTdSpecial")
    @PreAuthorize("@el.check('plan:list')")
    public ResponseEntity getAllDeptsForTdSpecial(FndDeptQueryCriteria criteria) {
        List<FndDeptDTO> deptDTOS = fndDeptService.listAll(criteria);
        return new ResponseEntity<>(fndDeptService.buildTree(deptDTOS, true), HttpStatus.OK);
    }

    @ErrorLog("生成车间排班的部门树")
    @ApiOperation("生成车间排班的部门树")
    @GetMapping(value = "/getDeptsForEmployeeAttendance")
    @PreAuthorize("@el.check('employeeAttendance:list')")
    public ResponseEntity getDeptsForEmployeeAttendance(FndDeptQueryCriteria criteria) {
        // 先参考人事的
        Set<Long> pmDeptIds = fndDataScope.getDeptIds(true, true);
        // 获取班组长的管辖范围
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        if (user.getEmployee() != null && null != user.getEmployee().getId()) {
            AcEmpDeptsQueryCriteria acEmpDeptsQueryCriteria = new AcEmpDeptsQueryCriteria();
            acEmpDeptsQueryCriteria.setRoleId(17L);
            acEmpDeptsQueryCriteria.setEnabledFlag(true);
            acEmpDeptsQueryCriteria.setDataType(authTeam);
            acEmpDeptsQueryCriteria.setEmployeeId(user.getEmployee().getId());
            Set<Long> acDeptIds = acEmpDeptsDao.listAllByCriteria(acEmpDeptsQueryCriteria).stream().map(AcEmpDepts::getDeptId)
                    .collect(Collectors.toSet());
            pmDeptIds.addAll(acDeptIds);
        }
        criteria.setIds(pmDeptIds);
        return setCriteriaIds(criteria);
    }

    private ResponseEntity setCriteriaIds(FndDeptQueryCriteria criteria) {
        List<FndDeptDTO> deptDTOS = fndDeptService.listAll(criteria);
        if (null != criteria.getName()) {
            Map<String, Object> map = new HashMap<>();
            map.put("totalElements", deptDTOS.size());
            map.put("content", deptDTOS);

            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(fndDeptService.buildTree(deptDTOS, false), HttpStatus.OK);// new ResponseEntity<>(fndDeptService.buildTree(deptDTOS), HttpStatus.OK);
        }
    }

//    @ErrorLog("查询部门（树结构）以考勤为主")
//    @ApiOperation("查询部门（树结构）以考勤为主")
//    @GetMapping("/getDeptTreeByAc")
//    @PreAuthorize("@el.check('user:list','dept:list','employee:list','train:list')")
//    public ResponseEntity getDeptTreeByAc(FndDeptQueryCriteria criteria) {
//        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
//        // 检测是否考勤管理员
//        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
//        for (GrantedAuthority auth : grantedAuthorityCollection
//        ) {
//            if (auth.getAuthority().equals(authRegime)) {
//                List<FndDeptDTO> deptDTOS = fndDeptService.listAll(criteria);
//                return new ResponseEntity<>(fndDeptService.buildTree(deptDTOS), HttpStatus.OK);
//            }
//        }
//        // 检测是否有考勤关系
//        List<AcEmpDeptsDTO> acEmpDeptsDTOS;
//        if (null == user.getEmployee()) {
//            acEmpDeptsDTOS = new ArrayList<>();
//        } else {
//            AcEmpDeptsQueryCriteria acEmpDeptsQueryCriteria = new AcEmpDeptsQueryCriteria();
//            acEmpDeptsQueryCriteria.setEnabledFlag(true);
//            acEmpDeptsQueryCriteria.setEmployeeId(user.getEmployee().getId());
//            acEmpDeptsQueryCriteria.setRoleId(17L);
//            acEmpDeptsDTOS = acEmpDeptsService.listAll(acEmpDeptsQueryCriteria);
//        }
//        if (acEmpDeptsDTOS.size() > 0) {
//            List<FndDeptDTO> deptDTOS = new ArrayList<>();
//            for (AcEmpDeptsDTO ac: acEmpDeptsDTOS
//                 ) {
//                FndDeptDTO fndDeptDTO = new FndDeptDTO();
//                fndDeptDTO.setId(ac.getDeptId());
//                fndDeptDTO.setDeptName(ac.getDeptName());
//                fndDeptDTO.setParentId(ac.getParentId());
//                fndDeptDTO.setUsed(false);
//                deptDTOS.add(fndDeptDTO);
//            }
//            return new ResponseEntity<>(fndDeptService.buildAcTree(deptDTOS), HttpStatus.OK);
//        } else {
//            // 检擦角色范围
//            if (null != user.getEmployee()) {
//                List<PmEmployeeJobDTO> jobs = pmEmployeeJobService.listByEmpIdAndEnabledFlagWithExtend(user.getEmployee().getId(), true);
//                List <FndJobDTO> fndJobs = jobs.stream().map(PmEmployeeJobDTO::getJob).collect(Collectors.toList());
//                Set<String> authStrings = fndJobs.stream().map(FndJobDTO::getDataScope).collect(Collectors.toSet());
//                if (!authStrings.contains(scopeType[0]) && !authStrings.contains(scopeType[1]) && !authStrings.contains(scopeType[2]) && !authStrings.contains(scopeType[4])) {
//                    // 没必要返回部门树
//                    return new ResponseEntity<>(null, HttpStatus.OK);
//                }
//            }
//            // 返回岗位的角色部门范围
//            criteria.setIds(fndDataScope.getDeptIds(true, true));
//            List<FndDeptDTO> deptDTOS = fndDeptService.listAll(criteria);
//            return new ResponseEntity<>(fndDeptService.buildTree(deptDTOS), HttpStatus.OK);
//        }
//    }


    @ErrorLog("导出部门数据")
    @ApiOperation("导出部门数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dept:list')")
    public void download(HttpServletResponse response, FndDeptQueryCriteria criteria) throws IOException {
        criteria.setIds(fndDataScope.getDeptIds(true, true));
        fndDeptService.download(fndDeptService.listAll(criteria), response);
    }

    @ErrorLog("导出部门数据：树结构")
    @ApiOperation("导出部门数据：树结构")
    @GetMapping(value = "/downloadDeptTree")
    @PreAuthorize("@el.check('dept:list')")
    public void downloadDeptTree(HttpServletResponse response, FndDeptQueryCriteria criteria) throws IOException {
        fndDeptService.downloadDeptTree(fndDeptService.listAll(criteria), response);
    }

    @Log("批量修改序号")
    @ApiOperation("批量修改序号")
    @PutMapping(value="/batchSort")
    @PreAuthorize("@el.check('dept:edit')")
    public ResponseEntity batchSort(@RequestBody List<FndDept> depts){
        fndDeptService.updateBatchSort(depts);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("人员统计")
    @ApiOperation("人员统计")
    @GetMapping(value="/getPmPieData")
    @PreAuthorize("@el.check('dept:list')")
    public ResponseEntity getPmPieData(PieCriteria fndPieQuery){
        return new ResponseEntity<>(fndDeptService.getPieVo(fndPieQuery), HttpStatus.OK);

//        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("获取当前部门的所属上级领导")
    @ApiOperation("获取当前部门的所属上级领导")
    @GetMapping("/getSuperiorByDeptId/{deptId}")
    public ResponseEntity getSuperiorByDeptId(@PathVariable Long deptId) {
        Map<String,Object> map = fndDeptService.getSuperiorByDeptId(deptId);
        return new ResponseEntity(map,HttpStatus.OK);
    }

    @Log("根据部门id集合获取部门信息")
    @ApiOperation("根据部门id集合获取部门信息")
    @GetMapping(value="/getDeptListByIds")
    public ResponseEntity getDeptListByIds(FndDeptQueryCriteria criteria) {
        if (null == criteria.getIds() || criteria.getIds().size() == 0){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(fndDeptService.getDeptListByIds(new ArrayList<>(criteria.getIds())), HttpStatus.OK);
        }
    }

    @Log("根据部门id获取下一级部门")
    @ApiOperation("根据部门id获取下一级部门")
    @GetMapping("/getSubDeptAndDeptByDeptId")
    @PreAuthorize("@el.check('employee:list')")
    public ResponseEntity getSubDeptAndDeptByDeptId(FndDeptQueryCriteria criteria) {
        List<FndDept> fndDepts = fndDeptService.listByPid(criteria.getPid());
        fndDepts.add(fndDeptMapper.toEntity(fndDeptService.getByKey(criteria.getDeptId())));
        return new ResponseEntity<>(fndDepts.stream().map(FndDept::getId).collect(Collectors.toSet()), HttpStatus.OK);
    }

}
