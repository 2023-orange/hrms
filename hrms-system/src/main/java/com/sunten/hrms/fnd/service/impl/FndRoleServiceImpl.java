package com.sunten.hrms.fnd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.EntityExistException;
import com.sunten.hrms.fnd.dao.*;
import com.sunten.hrms.fnd.domain.*;
import com.sunten.hrms.fnd.dto.FndRoleDTO;
import com.sunten.hrms.fnd.dto.FndRoleQueryCriteria;
import com.sunten.hrms.fnd.dto.FndRoleSmallDTO;
import com.sunten.hrms.fnd.mapper.FndRoleMapper;
import com.sunten.hrms.fnd.mapper.FndRoleSmallMapper;
import com.sunten.hrms.fnd.service.FndRoleService;
import com.sunten.hrms.fnd.service.RedisService;
import com.sunten.hrms.pm.service.PmEmployeeJobService;
import com.sunten.hrms.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@Service
@CacheConfig(cacheNames = "role")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndRoleServiceImpl extends ServiceImpl<FndRoleDao, FndRole> implements FndRoleService {
    @Value("${role.suntenManager}")
    private String SUNTEN_MANAGER;
    private final FndRoleDao fndRoleDao;
    private final FndRoleMapper fndRoleMapper;
    private final FndRoleSmallMapper fndRoleSmallMapper;
    private final FndRolesMenusDao fndRolesMenusDao;
    private final FndRolesDeptsDao fndRolesDeptsDao;
    private final FndMenuDao fndMenuDao;
    private final FndDeptDao fndDeptDao;
    private final PmEmployeeJobService pmEmployeeJobService;
    private final RedisService redisService;

    public FndRoleServiceImpl(FndRoleDao fndRoleDao, FndRoleMapper fndRoleMapper, FndRoleSmallMapper fndRoleSmallMapper, FndRolesMenusDao fndRolesMenusDao, FndRolesDeptsDao fndRolesDeptsDao,
                              FndMenuDao fndMenuDao, FndDeptDao fndDeptDao, PmEmployeeJobService pmEmployeeJobService, RedisService redisService) {
        this.fndRoleDao = fndRoleDao;
        this.fndRoleMapper = fndRoleMapper;
        this.fndRoleSmallMapper = fndRoleSmallMapper;
        this.fndRolesMenusDao = fndRolesMenusDao;
        this.fndRolesDeptsDao = fndRolesDeptsDao;
        this.fndMenuDao = fndMenuDao;
        this.fndDeptDao = fndDeptDao;
        this.pmEmployeeJobService = pmEmployeeJobService;
        this.redisService = redisService;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FndRoleDTO insert(FndRole roleNew) {
        if (fndRoleDao.getByName(roleNew.getName()) != null) {
            throw new EntityExistException(FndRole.class, "username", roleNew.getName());
        }
        fndRoleDao.insertAllColumn(roleNew);
        //处理depts关联
        List<FndDept> deptNew = new ArrayList<>(roleNew.getDepts());
        List<Long> deptIdsNew = deptNew.stream().map(FndDept::getId).sorted().collect(Collectors.toList());
        FndRolesDepts rolesDepts = new FndRolesDepts();
        rolesDepts.setRoleId(roleNew.getId());
        deptIdsNew.stream().forEach((id) -> {
            rolesDepts.setDeptId(id);
            fndRolesDeptsDao.insertAllColumn(rolesDepts);
        });
        return fndRoleMapper.toDto(roleNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        fndRoleDao.deleteByKey(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(FndRole roleNew) {
        FndRole role = Optional.ofNullable(fndRoleDao.getByKey(roleNew.getId())).orElseGet(FndRole::new);
        ValidationUtil.isNull(role.getId(), "Role", "id", roleNew.getId());
        FndRole role1 = fndRoleDao.getByName(roleNew.getName());
        if (role1 != null && !role1.getId().equals(role.getId())) {
            throw new EntityExistException(FndRole.class, "username", roleNew.getName());
        }
        role.setName(roleNew.getName());
        role.setRemark(roleNew.getRemark());
        role.setDataScope(roleNew.getDataScope());
        role.setLevel(roleNew.getLevel());
        role.setPermission(roleNew.getPermission());
        fndRoleDao.updateAllColumnByKey(role);
        //处理depts关联
        List<FndDept> deptOld = fndDeptDao.listByRoleId(role.getId());
        List<FndDept> deptNew = new ArrayList<>(roleNew.getDepts());
        List<Long> deptIdsOld = deptOld.stream().map(FndDept::getId).sorted().collect(Collectors.toList());
        List<Long> deptIdsNew = deptNew.stream().map(FndDept::getId).sorted().collect(Collectors.toList());
        ListUtils.listComp(deptIdsOld, deptIdsNew);
        FndRolesDepts rolesDepts = new FndRolesDepts();
        rolesDepts.setRoleId(role.getId());
        deptIdsOld.stream().forEach((id) -> {
            rolesDepts.setDeptId(id);
            fndRolesDeptsDao.deleteByEntityKey(rolesDepts);
        });
        deptIdsNew.stream().forEach((id) -> {
            rolesDepts.setDeptId(id);
            fndRolesDeptsDao.insertAllColumn(rolesDepts);
        });
    }

    @Override
    @Cacheable(key = "#p0")
    public FndRoleDTO getByKey(Long id) {
        FndRole role = Optional.ofNullable(fndRoleDao.getByKey(id)).orElseGet(FndRole::new);
        ValidationUtil.isNull(role.getId(), "Role", "id", id);
        getRefMenusAndDepts(role);
        return fndRoleMapper.toDto(role);
    }

    @Override
    @Cacheable
    public List<FndRoleDTO> listAll(FndRoleQueryCriteria criteria) {
        List<FndRole> roles = fndRoleDao.listAllByCriteria(criteria);
        getRefMenusAndDepts(roles);
        return fndRoleMapper.toDto(roles);
    }

    @Override
    @Cacheable
    public Map<String, Object> listAll(FndRoleQueryCriteria criteria, Pageable pageable) {
        Page<FndRole> page = PageUtil.startPage(pageable);
        List<FndRole> roles = fndRoleDao.listAllByCriteriaPage(page, criteria);
        getRefMenusAndDepts(roles);
        return PageUtil.toPage(fndRoleMapper.toDto(roles), page.getTotal());
    }

    @Override
    public void download(List<FndRoleDTO> roleDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndRoleDTO roleDTO : roleDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("角色名称", roleDTO.getName());
            map.put("默认权限", roleDTO.getPermission());
            map.put("角色级别", roleDTO.getLevel());
            map.put("描述", roleDTO.getRemark());
            map.put("创建日期", roleDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Cacheable(key = "'findByUserId:' + #p0")
    public List<FndRoleSmallDTO> listByUserId(Long id) {
        List<FndRole> roles = fndRoleDao.listByUserId(id);
        FndRole role = fndRoleDao.getByKey(2L);
        roles.add(role);
        Boolean managerFlag = pmEmployeeJobService.checkManager(id);
        if (managerFlag) {
            FndRole managerRole = fndRoleDao.getByName(SUNTEN_MANAGER);
            roles.add(managerRole);
        }
        return fndRoleSmallMapper.toDto(roles);
    }

    @Override
    public List<FndRole> listByUserIdComp(Long id) {
        List<FndRole> roles = fndRoleDao.listByUserId(id);
        FndRole role = fndRoleDao.getByKey(2L);
        roles.add(role);
        Boolean managerFlag = pmEmployeeJobService.checkManager(id);
        if (managerFlag) {
            FndRole managerRole = fndRoleDao.getByName(SUNTEN_MANAGER);
            roles.add(managerRole);
        }
        getRefMenusAndDepts(roles);
        return roles;
    }

    @Override
    @Cacheable
    public Integer getByRoles(Set<FndRole> roles) {
        Set<FndRoleDTO> roleDTOS = new HashSet<>();
        for (FndRole role : roles) {
            roleDTOS.add(getByKey(role.getId()));
        }
        return Collections.min(roleDTOS.stream().map(FndRoleDTO::getLevel).collect(Collectors.toList()));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(FndRole resources) {
        List<FndMenu> menusOld = fndMenuDao.listByRolesIdExType(resources.getId(), null);
        List<FndMenu> menusNew = new ArrayList<>(resources.getMenus());
        List<Long> menuIdsOld = menusOld.stream().map(FndMenu::getId).sorted().collect(Collectors.toList());
        List<Long> menuIdsNew = menusNew.stream().map(FndMenu::getId).sorted().collect(Collectors.toList());
        ListUtils.listComp(menuIdsOld, menuIdsNew);
        FndRolesMenus rolesMenus = new FndRolesMenus();
        rolesMenus.setRoleId(resources.getId());
        menuIdsOld.stream().forEach((id) -> {
            rolesMenus.setMenuId(id);
            fndRolesMenusDao.deleteByEntityKey(rolesMenus);
        });
        menuIdsNew.stream().forEach((id) -> {
            rolesMenus.setMenuId(id);
            fndRolesMenusDao.insertAllColumn(rolesMenus);
        });
        if (menuIdsOld.size() > 0 || menuIdsNew.size() > 0) {
            String key = "role::loadPermissionByUser:";
            redisService.deleteFuzzy(key);
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void untiedMenu(Long id) {
        fndRolesMenusDao.untiedMenu(id);
    }

    @Override
    public Object listAll(Pageable pageable) {
        Page<FndRole> page = PageUtil.startPage(pageable);
        List<FndRole> roles = fndRoleDao.listAllByPage(page);
        getRefMenusAndDepts(roles);
        return fndRoleMapper.toDto(roles);
    }


    private void getRefMenusAndDepts(List<FndRole> roles) {
        roles.stream().forEach((role) -> {
            getRefMenusAndDepts(role);
        });
    }

    private void getRefMenusAndDepts(FndRole role) {
        Set<FndMenu> menus = new HashSet<>(fndMenuDao.listByRolesIdExType(role.getId(), null));
        role.setMenus(menus);
        Set<FndDept> deptSet = new HashSet<>(fndDeptDao.listByRoleId(role.getId()));
        role.setDepts(deptSet);
    }


//    /**
//     * key的名称如有修改，请同步修改 UserServiceImpl 中的 update 方法
//     *
//     * @param user 用户信息
//     * @return Collection
//     */
//    @Override
//    @Cacheable(key = "'loadPermissionByUser:' + #p0.username")
//    public Collection<GrantedAuthority> mapToGrantedAuthorities(FndUserDTO user) {
//        System.out.println("--------------------loadPermissionByUser:" + user.getUsername() + "---------------------");
//        Set<FndRole> roles = new HashSet<>(listByUserIdComp(user.getId()));
//        Set<String> permissions = roles.stream().filter(role -> StringUtils.isNotBlank(role.getPermission())).map(FndRole::getPermission).collect(Collectors.toSet());
//        permissions.addAll(
//                roles.stream().flatMap(role -> role.getMenus().stream())
//                        .filter(menu -> StringUtils.isNotBlank(menu.getPermission()))
//                        .map(FndMenu::getPermission).collect(Collectors.toSet())
//        );
//        return permissions.stream().map(permission -> new SimpleGrantedAuthority(permission))
//                .collect(Collectors.toList());
//    }
}
