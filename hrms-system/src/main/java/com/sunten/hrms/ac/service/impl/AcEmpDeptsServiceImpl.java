package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcEmpDeptsDao;
import com.sunten.hrms.ac.dao.AcOvertimeReviewDao;
import com.sunten.hrms.ac.domain.AcEmpDepts;
import com.sunten.hrms.ac.domain.AcOvertimeReview;
import com.sunten.hrms.ac.dto.AcEmpDeptsDTO;
import com.sunten.hrms.ac.dto.AcEmpDeptsQueryCriteria;
import com.sunten.hrms.ac.mapper.AcEmpDeptsMapper;
import com.sunten.hrms.ac.service.AcEmpDeptsService;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndRoleDao;
import com.sunten.hrms.fnd.dao.FndUserDao;
import com.sunten.hrms.fnd.domain.FndRole;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.fnd.dto.FndDeptDTO;
import com.sunten.hrms.fnd.dto.FndRoleQueryCriteria;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.dto.FndUserQueryCriteria;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.fnd.service.FndUsersRolesService;
import com.sunten.hrms.fnd.service.RedisService;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeMapper;
import com.sunten.hrms.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 考勤模块人员数据权限范围表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-12-09
 */
@Service
@CacheConfig(cacheNames = "acEmpDept")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcEmpDeptsServiceImpl extends ServiceImpl<AcEmpDeptsDao, AcEmpDepts> implements AcEmpDeptsService {
    private final AcEmpDeptsDao acEmpDeptsDao;
    private final AcEmpDeptsMapper acEmpDeptsMapper;
    private final PmEmployeeMapper pmEmployeeMapper;
    private final FndUserService fndUserService;
    private final AcOvertimeReviewDao acOvertimeReviewDao;
    private final FndDeptService fndDeptService;
    private final FndRoleDao fndRoleDao;
    private final FndUserDao fndUserDao;
    private final FndUsersRolesService fndUsersRolesService;
    private final RedisService redisService;
    @Value("${role.authDocumenter}")
    private String authDocumenter;
    @Value("${role.authTeam}")
    private String authTeam;
    @Value("${role.pmAttendanceCharge}")
    private String pmAttendanceCharge;

    public AcEmpDeptsServiceImpl(AcEmpDeptsDao acEmpDeptsDao, AcEmpDeptsMapper acEmpDeptsMapper, PmEmployeeMapper pmEmployeeMapper,
                                 FndUserService fndUserService,AcOvertimeReviewDao acOvertimeReviewDao,
                                 FndDeptService fndDeptService, FndRoleDao fndRoleDao, FndUserDao fndUserDao, FndUsersRolesService fndUsersRolesService,
                                 RedisService redisService) {
        this.acEmpDeptsDao = acEmpDeptsDao;
        this.acEmpDeptsMapper = acEmpDeptsMapper;
        this.pmEmployeeMapper = pmEmployeeMapper;
        this.fndUserService = fndUserService;
        this.acOvertimeReviewDao =acOvertimeReviewDao;
        this.fndDeptService= fndDeptService;
        this.fndRoleDao = fndRoleDao;
        this.fndUserDao = fndUserDao;
        this.fndUsersRolesService = fndUsersRolesService;
        this.redisService = redisService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcEmpDeptsDTO insert(AcEmpDepts empDeptsNew) {
        acEmpDeptsDao.insertAllColumn(empDeptsNew);
        return acEmpDeptsMapper.toDto(empDeptsNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AcEmpDepts empDepts = new AcEmpDepts();
        empDepts.setId(id);
        this.delete(empDepts);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcEmpDepts empDepts) {
        // TODO    确认删除前是否需要做检查
        acEmpDeptsDao.deleteByEntityKey(empDepts);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcEmpDepts empDeptsNew) {
        AcEmpDepts empDeptsInDb = Optional.ofNullable(acEmpDeptsDao.getByKey(empDeptsNew.getId())).orElseGet(AcEmpDepts::new);
        ValidationUtil.isNull(empDeptsInDb.getId() ,"EmpDepts", "id", empDeptsNew.getId());
        empDeptsNew.setId(empDeptsInDb.getId());
        acEmpDeptsDao.updateAllColumnByKey(empDeptsNew);
    }

    @Override
    public AcEmpDeptsDTO getByKey(Long id) {
        AcEmpDepts empDepts = Optional.ofNullable(acEmpDeptsDao.getByKey(id)).orElseGet(AcEmpDepts::new);
        ValidationUtil.isNull(empDepts.getId() ,"EmpDepts", "id", id);
        return acEmpDeptsMapper.toDto(empDepts);
    }

    @Override
    public List<AcEmpDeptsDTO> listAll(AcEmpDeptsQueryCriteria criteria) {
        return acEmpDeptsMapper.toDto(acEmpDeptsDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcEmpDeptsQueryCriteria criteria, Pageable pageable) {
        Page<AcEmpDepts> page = PageUtil.startPage(pageable);
        List<AcEmpDepts> empDeptss = acEmpDeptsDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acEmpDeptsMapper.toDto(empDeptss), page.getTotal());
    }

    @Override
    public void download(List<AcEmpDeptsDTO> empDeptsDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcEmpDeptsDTO empDeptsDTO : empDeptsDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("对应fnd_role 目前只考虑资料员", empDeptsDTO.getRoleId());
            map.put("对应人事主表id", empDeptsDTO.getEmployeeId());
            map.put("部门id", empDeptsDTO.getDeptId());
            map.put("有效标识", empDeptsDTO.getEnabledFlag());
            map.put("id", empDeptsDTO.getId());
            map.put("创建时间", empDeptsDTO.getCreateTime());
            map.put("创建人ID", empDeptsDTO.getCreateBy());
            map.put("修改时间", empDeptsDTO.getUpdateTime());
            map.put("修改人ID", empDeptsDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     *  @author：liangjw
     *  @Date: 2020/12/11 11:30
     *  @Description: 更新权限范围
     */
    @Override
    @Caching(evict = {@CacheEvict(allEntries = true),
    @CacheEvict(value = "role", key = "'loadPermissionByUser:*'", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void updateEmpDept(AcEmpDepts acEmpDepts) {
        boolean haveMenuFlag = false;
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        AcEmpDeptsQueryCriteria acEmpDeptsQueryCriteria = new AcEmpDeptsQueryCriteria();
        acEmpDeptsQueryCriteria.setEnabledFlag(true);
        acEmpDeptsQueryCriteria.setRoleId(17L);
        acEmpDeptsQueryCriteria.setEmployeeId(acEmpDepts.getEmployeeId());
        acEmpDeptsQueryCriteria.setDataType(acEmpDepts.getDataType());
        List<AcEmpDepts> acEmpDeptsListOld = acEmpDeptsDao.listAllByCriteria(acEmpDeptsQueryCriteria);
        if (null != acEmpDeptsListOld && acEmpDeptsListOld.size() > 0) {
            haveMenuFlag = true;
        }
        List<AcEmpDepts> acEmpDeptsListNew = new ArrayList<>();
        for (Long deptId : acEmpDepts.getDeptList()
             ) {
            AcEmpDepts temp = new AcEmpDepts();
            temp.setDeptId(deptId);
//            temp.setEmployeeId(acEmpDepts.getEmployeeId());
//            temp.setEnabledFlag(true);
//            temp.setRoleId(17L);
            acEmpDeptsListNew.add(temp);
        }
        // 2025-2-8,修复原有逻辑BUG
        List<Long> acEmpDeptsListOldIds = (haveMenuFlag ? acEmpDeptsListOld.stream().map(AcEmpDepts::getDeptId).collect(Collectors.toList()) : new ArrayList<>());
        List<Long> acEmpDeptsListNewIds = acEmpDeptsListNew.stream().map(AcEmpDepts::getDeptId).collect(Collectors.toList());

        ListUtils.listComp(acEmpDeptsListOldIds, acEmpDeptsListNewIds);
        // 原来范围逐个对比，当全部相同时移除菜单权限
        if (acEmpDepts.getDataType().equals(authTeam)){
            int sameSize = (int) acEmpDeptsListNewIds.stream()
                    .map(t -> acEmpDeptsListOldIds.stream().filter(s -> Objects.nonNull(t) && Objects.nonNull(s) && Objects.equals(t, s)).findAny().orElse(null))
                    .filter(Objects::nonNull).count();
            if (sameSize == acEmpDeptsListOldIds.size() && acEmpDeptsListOldIds.size() != 0){
                // 给user 移除菜单
                removeMenu(acEmpDepts);
            } else {
                if (!haveMenuFlag) {
                    // 给用户分配班组长排班权限
                    acEmpDepts.setCreateBy(user.getId());
                    FndUserQueryCriteria fndUserQueryCriteria = new FndUserQueryCriteria();
                    fndUserQueryCriteria.setEmployeeId(acEmpDepts.getEmployeeId());
                    List<FndUser> fndUsers = fndUserDao.listAllByCriteria(fndUserQueryCriteria);
                    for (FndUser fndUser : fndUsers
                    ) {
                        // 检测是否已经有排版负责人角色
                        if (!fndUsersRolesService.checkHaveRoleByUserIdAndPermission(fndUser.getId(), pmAttendanceCharge)) {
                            // 第一个是创建人ID，第三个是所属人用户Id
                            acEmpDeptsDao.insertRoleByAcEmpDept(user.getId(), pmAttendanceCharge, fndUser.getId());
//                            String key = "role::loadPermissionByUser:" + fndUser.getId();
//                            redisService.delete(key);
//                            key = "role::findByUserId:" + fndUser.getId();
//                            redisService.delete(key);
                            try {
                                String key = "role::loadPermissionByUser:" + fndUser.getId();
                                redisService.delete(key);
                                key = "role::findByUserId:" + fndUser.getId();
                                redisService.delete(key);
                            } catch (Exception e) {
                                System.out.println("Redis异常：" + e.getMessage());
                                throw e;
                            }
                        }
                    }
                }
            }
        }
        AcEmpDepts target = new AcEmpDepts();
        target.setEmployeeId(acEmpDepts.getEmployeeId());
        target.setDataType(acEmpDepts.getDataType());
        try {
            acEmpDeptsListOldIds.stream().forEach((id) -> {
                target.setDeptId(id);
                target.setUpdateBy(user.getId());
                target.setUpdateTime(LocalDateTime.now());
                acEmpDeptsDao.deleteByEnabled(target);
            });
            acEmpDeptsListNewIds.stream().forEach((id) -> {
                target.setDeptId(id);
                target.setRoleId(17L);
                target.setEnabledFlag(true);
                acEmpDeptsDao.insertAllColumn(target);
            });
        }catch (Exception e) {
            System.out.println("错误信息看这里： " + e.getMessage());
        }
    }


    @Override
    public Map<String, Object> getRoleEmpList(PmEmployeeQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployee> page = PageUtil.startPage(pageable);
        List<PmEmployee> pms = acEmpDeptsDao.getRoleEmpListByPage(criteria, page);
        return PageUtil.toPage(pmEmployeeMapper.toDto(pms), page.getTotal());
    }

    @Override
    public Integer countRoleEmp(PmEmployeeQueryCriteria criteria) {
        return acEmpDeptsDao.countRoleEmp(criteria);
    }

    @Override
    @Caching(evict = {@CacheEvict(allEntries = true),
    @CacheEvict(value = "role", key = "'loadPermissionByUser:*'", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void removeRelationByid(Long employeeId, String dataType) {
        acEmpDeptsDao.removeRelationByid(employeeId, dataType);
        AcEmpDepts acEmpDepts = new AcEmpDepts();
        acEmpDepts.setEmployeeId(employeeId);
        acEmpDepts.setDataType(dataType);
        removeMenu(acEmpDepts);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    // 初始化empdept
    public void initEmpDept() {
        List<AcOvertimeReview> acOvertimeReviews = acOvertimeReviewDao.getAcOvertimeReviewList();
        for (AcOvertimeReview acOvertimeReview: acOvertimeReviews
        ) {
            acEmpDeptsDao.insertByOvertimeReview(acOvertimeReview);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    //初始化empdept并扩展empDept
    public void initEmpDeptChildExtend() {
        AcEmpDeptsQueryCriteria acEmpDeptsQueryCriteria = new AcEmpDeptsQueryCriteria();
        acEmpDeptsQueryCriteria.setEnabledFlag(true);
        List<AcEmpDepts> acEmpDeptsList = acEmpDeptsDao.listTempByCriteria(acEmpDeptsQueryCriteria);
        Set<Long> deptIds = new HashSet<>();
        Long empId = -1L;
        for (AcEmpDepts ac: acEmpDeptsList
        ) {
            if (!empId.equals(ac.getEmployeeId())) {
                System.out.println(deptIds);
                insertAcEmpDeptsByList(deptIds, empId);
                empId = ac.getEmployeeId();
                deptIds = new HashSet<>();
            }
            deptIds.addAll(fndDeptService.listAllEnableChildrenIdByPid(ac.getDeptId()));
            deptIds.remove(ac.getDeptId());
        }
        insertAcEmpDeptsByList(deptIds, empId);
    }

    private void insertAcEmpDeptsByList(Set<Long> deptIds, Long empId) {
        for (Long deptId : deptIds
        ) {
            AcEmpDepts acEmpDepts = new AcEmpDepts();
            acEmpDepts.setEnabledFlag(true);
            acEmpDepts.setRoleId(17L);
            acEmpDepts.setDeptId(deptId);
            acEmpDepts.setEmployeeId(empId);
            acEmpDeptsDao.insertAllColumnTemp(acEmpDepts);
        }
    }

    @Override
    public Boolean checkDocPermission(Long employeeId) {
        return acEmpDeptsDao.checkDocPermission(employeeId);
    }

    @Override
    public Set<Long> getJurisdictionDeptId(Long employeeId) {
        // 获取管理岗的管辖部门
        List<FndDeptDTO> leaderDepts = fndDeptService.listByAuthorization(employeeId);
        // 获取非管理岗的管辖部门（资料员、班组长、站长等职位）
        AcEmpDeptsQueryCriteria acEmpDeptsQueryCriteria = new AcEmpDeptsQueryCriteria();
        acEmpDeptsQueryCriteria.setDataType(authTeam);
        acEmpDeptsQueryCriteria.setEnabledFlag(true);
        acEmpDeptsQueryCriteria.setEmployeeId(employeeId);
        Set<Long> teamDeptIds = acEmpDeptsDao.listAllByCriteria(acEmpDeptsQueryCriteria).stream().map(AcEmpDepts::getDeptId).collect(Collectors.toSet());

        acEmpDeptsQueryCriteria.setDataType(authDocumenter);
        Set<Long> docDeptIds = acEmpDeptsDao.listAllByCriteria(acEmpDeptsQueryCriteria).stream().map(AcEmpDepts::getDeptId).collect(Collectors.toSet());
        Set<Long> leaderDeptIds = leaderDepts.stream().map(FndDeptDTO::getId).collect(Collectors.toSet());

        leaderDeptIds.addAll(teamDeptIds);
        leaderDeptIds.addAll(docDeptIds);

        return leaderDeptIds;
    }

    private void removeMenu(AcEmpDepts acEmpDepts) {
        FndRoleQueryCriteria fndRoleQueryCriteria = new FndRoleQueryCriteria();
        fndRoleQueryCriteria.setPermission(pmAttendanceCharge);
        List<FndRole> teamRole = fndRoleDao.listAllByCriteria(fndRoleQueryCriteria);
        if (teamRole.size() != 1) {
            throw new InfoCheckWarningMessException("检测到基础权限有异常，暂不允许操作，请告知管理员处理");
        } else {
            FndUserQueryCriteria fndUserQueryCriteria = new FndUserQueryCriteria();
            fndUserQueryCriteria.setEmployeeId(acEmpDepts.getEmployeeId());
            fndUserQueryCriteria.setEnabled(true);
            List<FndUser> fndUsers = fndUserDao.listAllByCriteria(fndUserQueryCriteria);
            for (FndUser fndUser : fndUsers
                 ) {
                // 删除对应的权限菜单
                fndUsersRolesService.delete(fndUser.getId(), teamRole.get(0).getId());
                String key = "role::loadPermissionByUser:" + fndUser.getId();
                redisService.delete(key);
                key = "role::findByUserId:" + fndUser.getId();
                redisService.delete(key);
            }
        }
    }
}
