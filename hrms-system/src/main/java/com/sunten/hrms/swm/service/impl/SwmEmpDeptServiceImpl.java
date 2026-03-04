package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.ac.service.impl.AcEmpDeptsServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndRoleDao;
import com.sunten.hrms.fnd.dao.FndUserDao;
import com.sunten.hrms.fnd.domain.FndRole;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.fnd.dto.FndRoleQueryCriteria;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.dto.FndUserQueryCriteria;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.fnd.service.FndUsersRolesService;
import com.sunten.hrms.fnd.service.RedisService;
import com.sunten.hrms.swm.domain.SwmEmpDept;
import com.sunten.hrms.swm.dao.SwmEmpDeptDao;
import com.sunten.hrms.swm.domain.SwmEmployee;
import com.sunten.hrms.swm.dto.SwmEmployeeQueryCriteria;
import com.sunten.hrms.swm.service.SwmEmpDeptService;
import com.sunten.hrms.swm.dto.SwmEmpDeptDTO;
import com.sunten.hrms.swm.dto.SwmEmpDeptQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmEmpDeptMapper;
import com.sunten.hrms.swm.service.SwmEmployeeService;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 薪酬人员管理范围 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-02-24
 */
@Service
@CacheConfig(cacheNames = "menu")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmEmpDeptServiceImpl extends ServiceImpl<SwmEmpDeptDao, SwmEmpDept> implements SwmEmpDeptService {
    private final SwmEmpDeptDao swmEmpDeptDao;
    private final SwmEmpDeptMapper swmEmpDeptMapper;
    private final FndUserService fndUserService;
    private final SwmEmployeeService swmEmployeeService;
    private final FndUserDao fndUserDao;
    private final RedisService redisService;
    private final FndRoleDao fndRoleDao;
    private final FndUsersRolesService fndUsersRolesService;

    @Value("${role.monthAssessmentCharge}")
    private String monthAssessmentCharge;
    @Value("${role.allocatePerformancePayCharge}")
    private String allocatePerformancePayCharge;
    @Value("${swmAuthType.monthlyAssessment}")
    private String monthlyAssessment;




    public SwmEmpDeptServiceImpl(SwmEmpDeptDao swmEmpDeptDao, SwmEmpDeptMapper swmEmpDeptMapper, FndUserService fndUserService,
                                 SwmEmployeeService swmEmployeeService, FndUserDao fndUserDao,
                                 RedisService redisService, FndRoleDao fndRoleDao, FndUsersRolesService fndUsersRolesService) {
        this.swmEmpDeptDao = swmEmpDeptDao;
        this.swmEmpDeptMapper = swmEmpDeptMapper;
        this.fndUserService = fndUserService;
        this.swmEmployeeService = swmEmployeeService;
        this.fndUserDao = fndUserDao;
        this.redisService = redisService;
        this.fndRoleDao = fndRoleDao;
        this.fndUsersRolesService = fndUsersRolesService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmEmpDeptDTO insert(SwmEmpDept empDeptNew) {
        swmEmpDeptDao.insertAllColumn(empDeptNew);
        return swmEmpDeptMapper.toDto(empDeptNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmEmpDept empDept = new SwmEmpDept();
        empDept.setId(id);
        this.delete(empDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmEmpDept empDept) {
        // TODO    确认删除前是否需要做检查
        swmEmpDeptDao.deleteByEntityKey(empDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmEmpDept empDeptNew) {
        SwmEmpDept empDeptInDb = Optional.ofNullable(swmEmpDeptDao.getByKey(empDeptNew.getId())).orElseGet(SwmEmpDept::new);
        ValidationUtil.isNull(empDeptInDb.getId() ,"EmpDept", "id", empDeptNew.getId());
        empDeptNew.setId(empDeptInDb.getId());
        swmEmpDeptDao.updateAllColumnByKey(empDeptNew);
    }

    @Override
    public SwmEmpDeptDTO getByKey(Long id) {
        SwmEmpDept empDept = Optional.ofNullable(swmEmpDeptDao.getByKey(id)).orElseGet(SwmEmpDept::new);
        ValidationUtil.isNull(empDept.getId() ,"EmpDept", "id", id);
        return swmEmpDeptMapper.toDto(empDept);
    }

    @Override
    public List<SwmEmpDeptDTO> listAll(SwmEmpDeptQueryCriteria criteria) {
        return swmEmpDeptMapper.toDto(swmEmpDeptDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmEmpDeptQueryCriteria criteria, Pageable pageable) {
        Page<SwmEmpDept> page = PageUtil.startPage(pageable);
        List<SwmEmpDept> empDepts = swmEmpDeptDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmEmpDeptMapper.toDto(empDepts), page.getTotal());
    }

    @Override
    public void download(List<SwmEmpDeptDTO> empDeptDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmEmpDeptDTO empDeptDTO : empDeptDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("工牌号", empDeptDTO.getSwmEmployee().getWorkCard());
            map.put("姓名", empDeptDTO.getSwmEmployee().getName());
            map.put("部门", empDeptDTO.getDepartment());
            map.put("科室", empDeptDTO.getAdministrativeOffice());
            map.put("岗位", empDeptDTO.getSwmEmployee().getJob());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<String> getDeptCodeList(SwmEmpDeptQueryCriteria criteria) {
        List<SwmEmpDept> swmEmpDepts = swmEmpDeptDao.listAllByCriteria(criteria);
        List<String> targets = new ArrayList<>();
        for (SwmEmpDept swmEmpDept : swmEmpDepts
             ) {
            if (null != swmEmpDept.getAdministrativeOffice() && !swmEmpDept.getAdministrativeOffice().trim().equals("")){
                String code = swmEmpDept.getDepartment() + "." + swmEmpDept.getAdministrativeOffice();
                targets.add(code);
            } else {
                targets.add(swmEmpDept.getDepartment());
            }
        }
        return targets;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByEnabled(SwmEmpDept swmEmpDept) {
        swmEmpDeptDao.updateByEnabled(swmEmpDept);
    }


    @Override
    @Caching(evict = {@CacheEvict(allEntries = true),
    @CacheEvict(value = "role", key = "'loadPermissionByUser:*'", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void updateEmpDepts(SwmEmpDept swmEmpDept) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        SwmEmpDeptQueryCriteria swmEmpDeptQueryCriteria = new SwmEmpDeptQueryCriteria();
        swmEmpDeptQueryCriteria.setEnabledFlag(true);
        swmEmpDeptQueryCriteria.setSeId(swmEmpDept.getSeId());
        swmEmpDeptQueryCriteria.setType(monthlyAssessment);
        List<String> originalList = this.getDeptCodeList(swmEmpDeptQueryCriteria);
        Boolean haveMenuFlag = false;
        if (originalList.size() > 0) {
            haveMenuFlag = true;
        }
        List<String> newList = new ArrayList<>(swmEmpDept.getDeptCodeList());
        ListUtils.listCompForStr(originalList, newList);

        // 原来范围逐个对比，当全部相同时移除菜单权限
        int sameSize = (int) newList.stream()
                .map(t -> originalList.stream().filter(s -> Objects.nonNull(t) && Objects.nonNull(s) && Objects.equals(t, s)).findAny().orElse(null))
                .filter(Objects::nonNull).count();
        SwmEmployee swmEmployeeAuth = new SwmEmployee();
        if (sameSize == originalList.size() && originalList.size() != 0) { // 全部的范围都被移除掉了
            // 给user移除菜单
            removeMenu(swmEmpDept);
        } else {
            if (!haveMenuFlag) {
                // 判断用户是否有月度考核角色
                if (!fndUsersRolesService.checkHaveRoleBySeIdAndPermission(swmEmpDept.getSeId(), monthAssessmentCharge)) {
                    // 给user分配月度考核权限的相关角色
                    swmEmployeeAuth.setPermission(monthAssessmentCharge);
                    swmEmployeeAuth.setId(swmEmpDept.getSeId());
                    swmEmployeeAuth.setUserId(user.getEmployee().getId());
                    swmEmployeeService.insertUserRoleBySwmEmp(swmEmployeeAuth);
                }
                // 检查newList里有没有2类的员工，有则分配菜单
                SwmEmpDept swmTypeCheck = new SwmEmpDept();
                Boolean checkFlag = false;
                for (String deptAndOffice : newList
                     ) {
                    if (deptAndOffice.contains(".")) {
                        String[] strs = deptAndOffice.split("\\.");
                        swmTypeCheck.setDepartment(strs[0]);
                        swmTypeCheck.setAdministrativeOffice(strs[1]);
                    } else {
                        swmTypeCheck.setDepartment(deptAndOffice);
                        swmTypeCheck.setAdministrativeOffice(null);
                    }
//                    checkFlag = swmEmpDeptDao.checkGenerateByPmType(swmTypeCheck);
//                    if (checkFlag) {
                        // 判断用户是否有二次分配角色
                    if (!fndUsersRolesService.checkHaveRoleBySeIdAndPermission(swmEmpDept.getSeId(), allocatePerformancePayCharge)) {
                        // 分配二次车间二次分配
                        SwmEmployee swmEmployeeAuthx = new SwmEmployee();
                        swmEmployeeAuthx.setId(swmEmpDept.getSeId());
                        swmEmployeeAuthx.setUserId(user.getEmployee().getId());
                        swmEmployeeAuthx.setPermission(allocatePerformancePayCharge);
                        swmEmployeeService.insertUserRoleBySwmEmp(swmEmployeeAuthx);
                    }
                    break;
//                    }
                }

                FndUserQueryCriteria fndUserQueryCriteria = new FndUserQueryCriteria();
                fndUserQueryCriteria.setEmployeeId(swmEmpDept.getSeId());
                List<FndUser> fndUsers = fndUserDao.listAllByCriteria(fndUserQueryCriteria);
                for (FndUser fndUser : fndUsers
                ) {
                    String key = "role::loadPermissionByUser:" + fndUser.getId();
                    redisService.delete(key);
                    key = "role::findByUserId:" + fndUser.getId();
                    redisService.delete(key);
                }
            }
        }
        // 菜单处理完毕

        SwmEmpDept target = new SwmEmpDept();
        target.setSeId(swmEmpDept.getSeId());
        target.setType(monthlyAssessment);
        // 分配菜单
        originalList.forEach((string) -> {
            if (string.contains(".")) {
                String[] strs = string.split("\\.");
                target.setDepartment(strs[0]);
                target.setAdministrativeOffice(strs[1]);

            } else {
                target.setDepartment(string);
                target.setAdministrativeOffice(null);
            }
            target.setUpdateBy(user.getId());
            target.setUpdateTime(LocalDateTime.now());
            swmEmpDeptDao.updateByEnabled(target);
        });

        newList.forEach((string) -> {
            if (string.contains(".")) {
                String[] strs = string.split("\\.");
                target.setDepartment(strs[0]);
                target.setAdministrativeOffice(strs[1]);

            } else {
                target.setDepartment(string);
                target.setAdministrativeOffice(null);
            }
            target.setEnabledFlag(true);
            swmEmpDeptDao.insertAllColumn(target);
        });

    }

    @Override
    @Caching(evict = {@CacheEvict(allEntries = true),
    @CacheEvict(value = "role", key = "'loadPermissionByUser:*'", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void updateEnabledBySwmEmployee(SwmEmpDept swmEmpDept) {
        swmEmpDeptDao.updateEnabledBySeId(swmEmpDept);
        // 移除菜单
        removeMenu(swmEmpDept);
    }

    private void removeMenu(SwmEmpDept swmEmpDept) {
        FndRoleQueryCriteria fndRoleQueryCriteria = new FndRoleQueryCriteria();
        fndRoleQueryCriteria.setPermission(monthAssessmentCharge);
        List<FndRole> fndRoles = fndRoleDao.listAllByCriteria(fndRoleQueryCriteria);
        fndRoleQueryCriteria.setPermission(allocatePerformancePayCharge);
        List<FndRole> allocateRoles = fndRoleDao.listAllByCriteria(fndRoleQueryCriteria);
        if (fndRoles.size() != 1 || allocateRoles.size() != 1) {
            throw new InfoCheckWarningMessException("检测到基础权限有异常，暂不允许操作，请告知管理员处理");
        } else {
            FndUserQueryCriteria fndUserQueryCriteria = new FndUserQueryCriteria();
            fndUserQueryCriteria.setEmployeeId(swmEmployeeService.getByKey(swmEmpDept.getSeId()).getEmployeeId());
            List<FndUser> fndUsers = fndUserDao.listAllByCriteria(fndUserQueryCriteria);
            for (FndUser fndUser : fndUsers
            ) {
                fndUsersRolesService.delete(fndUser.getId(), fndRoles.get(0).getId());
                fndUsersRolesService.delete(fndUser.getId(), allocateRoles.get(0).getId());
                String key = "role::loadPermissionByUser:" + fndUser.getId();
                redisService.delete(key);
                key = "role::findByUserId:" + fndUser.getId();
                redisService.delete(key);

            }
        }
    }
}
