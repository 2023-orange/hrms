package com.sunten.hrms.fnd.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.EntityExistException;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndMenuDao;
import com.sunten.hrms.fnd.domain.FndMenu;
import com.sunten.hrms.fnd.dto.FndMenuDTO;
import com.sunten.hrms.fnd.dto.FndMenuQueryCriteria;
import com.sunten.hrms.fnd.dto.FndRoleSmallDTO;
import com.sunten.hrms.fnd.mapper.FndMenuMapper;
import com.sunten.hrms.fnd.service.FndMenuService;
import com.sunten.hrms.fnd.service.FndRoleService;
import com.sunten.hrms.fnd.vo.FndMenuMetaVo;
import com.sunten.hrms.fnd.vo.FndMenuVo;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.StringUtils;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
@CacheConfig(cacheNames = "menu")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndMenuServiceImpl extends ServiceImpl<FndMenuDao, FndMenu> implements FndMenuService {
    private final FndMenuDao fndMenuDao;
    private final FndMenuMapper fndMenuMapper;
    private final FndRoleService fndRoleService;

    public FndMenuServiceImpl(FndMenuDao fndMenuDao, FndMenuMapper fndMenuMapper, FndRoleService fndRoleService) {
        this.fndMenuDao = fndMenuDao;
        this.fndMenuMapper = fndMenuMapper;
        this.fndRoleService = fndRoleService;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FndMenuDTO insert(FndMenu menuNew) {
        if (fndMenuDao.getByName(menuNew.getName()) != null) {
            throw new EntityExistException(FndMenu.class, "name", menuNew.getName());
        }
        if (StringUtils.isNotBlank(menuNew.getComponentName())) {
            if (fndMenuDao.getByComponentName(menuNew.getComponentName()) != null) {
                throw new EntityExistException(FndMenu.class, "componentName", menuNew.getComponentName());
            }
        }
        if (menuNew.getIFrameFlag()) {
            if (!(menuNew.getPath().toLowerCase().startsWith("http://") || menuNew.getPath().toLowerCase().startsWith("https://"))) {
                throw new InfoCheckWarningMessException("外链必须以http://或者https://开头");
            }
        }
        fndMenuDao.insertAllColumn(menuNew);
        return fndMenuMapper.toDto(menuNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        fndMenuDao.deleteByKey(id);
    }

    @Override
    @Caching(evict = {@CacheEvict(allEntries = true),
            @CacheEvict(value = "role", key = "'loadPermissionByUser:*'", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void update(FndMenu menuNew) {
//        boolean permissionChanged = false;
        if (menuNew.getId().equals(menuNew.getPid())) {
            throw new InfoCheckWarningMessException("上级不能为自己");
        }
        FndMenu menuInDB = Optional.ofNullable(fndMenuDao.getByKey(menuNew.getId())).orElseGet(FndMenu::new);
        ValidationUtil.isNull(menuInDB.getId(), "Permission", "id", menuNew.getId());
//        permissionChanged = !menuInDB.getPermission().equals(menuNew.getPermission());
        if (menuNew.getIFrameFlag()) {
            if (!(menuNew.getPath().toLowerCase().startsWith("http://") || menuNew.getPath().toLowerCase().startsWith("https://"))) {
                throw new InfoCheckWarningMessException("外链必须以http://或者https://开头");
            }
        }
        FndMenu menu1 = fndMenuDao.getByName(menuNew.getName());

        if (menu1 != null && !menu1.getId().equals(menuInDB.getId())) {
            throw new EntityExistException(FndMenu.class, "name", menuNew.getName());
        }

        if (StringUtils.isNotBlank(menuNew.getComponentName())) {
            menu1 = fndMenuDao.getByComponentName(menuNew.getComponentName());
            if (menu1 != null && !menu1.getId().equals(menuInDB.getId())) {
                throw new EntityExistException(FndMenu.class, "componentName", menuNew.getComponentName());
            }
        }
        menuInDB.setName(menuNew.getName());
        menuInDB.setComponent(menuNew.getComponent());
        menuInDB.setPath(menuNew.getPath());
        menuInDB.setIcon(menuNew.getIcon());
        menuInDB.setIFrameFlag(menuNew.getIFrameFlag());
        menuInDB.setPid(menuNew.getPid());
        menuInDB.setSort(menuNew.getSort());
        menuInDB.setCacheFlag(menuNew.getCacheFlag());
        menuInDB.setHiddenFlag(menuNew.getHiddenFlag());
        menuInDB.setBlankFlag(menuNew.getBlankFlag());
        menuInDB.setSendTokenFlag(menuNew.getSendTokenFlag());
        menuInDB.setComponentName(menuNew.getComponentName());
        menuInDB.setPermission(menuNew.getPermission());
        menuInDB.setType(menuNew.getType());
        fndMenuDao.updateAllColumnByKey(menuInDB);
    }

    @Override
    @Cacheable(key = "#p0")
    public FndMenuDTO getByKey(Long id) {
        FndMenu menu = Optional.ofNullable(fndMenuDao.getByKey(id)).orElseGet(FndMenu::new);
        ValidationUtil.isNull(menu.getId(), "Menu", "id", id);
        return fndMenuMapper.toDto(menu);
    }

    @Override
    @Cacheable
    public List<FndMenuDTO> listAll(FndMenuQueryCriteria criteria) {
        return fndMenuMapper.toDto(fndMenuDao.listAllByCriteria(criteria));
    }

    @Override
    @Cacheable
    public Map<String, Object> listAll(FndMenuQueryCriteria criteria, Pageable pageable) {
        Page<FndMenu> page = PageUtil.startPage(pageable);
        List<FndMenu> menus = fndMenuDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndMenuMapper.toDto(menus), page.getTotal());
    }

    @Override
    public void download(List<FndMenuDTO> menuDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndMenuDTO menuDTO : menuDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("菜单名称", menuDTO.getName());
            map.put("菜单类型", menuDTO.getType() == 0 ? "目录" : menuDTO.getType() == 1 ? "菜单" : "按钮");
            map.put("权限标识", menuDTO.getPermission());
            map.put("外链菜单", menuDTO.getIFrameFlag() ? "是" : "否");
            map.put("菜单可见", menuDTO.getHiddenFlag() ? "否" : "是");
            map.put("是否缓存", menuDTO.getCacheFlag() ? "是" : "否");
            map.put("弹出页面", menuDTO.getBlankFlag() ? "是" : "否");
            map.put("创建日期", menuDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<FndMenu> menus) {
        for (FndMenu menu : menus) {
            fndRoleService.untiedMenu(menu.getId());
            fndMenuDao.deleteById(menu.getId());
        }
    }

    @Override
    public Set<FndMenu> listMenus(List<FndMenu> menuList, Set<FndMenu> menuSet) {
        // 递归找出待删除的菜单
        for (FndMenu fndMenu : menuList) {
            menuSet.add(fndMenu);
            List<FndMenu> fndMenus = fndMenuDao.listByPid(fndMenu.getId());
            if (fndMenus != null && fndMenus.size() != 0) {
                listMenus(fndMenus, menuSet);
            }
        }
        return menuSet;
    }

    @Override
    @Cacheable(key = "'tree'")
    public Object getMenuTree(List<FndMenu> menus) {
        List<Map<String, Object>> list = new LinkedList<>();
        menus.forEach(menu -> {
                    if (menu != null) {
                        List<FndMenu> childrenFndMenuList = fndMenuDao.listByPid(menu.getId());
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", menu.getId());
                        map.put("label", menu.getName());
                        if (childrenFndMenuList != null && childrenFndMenuList.size() != 0) {
                            map.put("children", getMenuTree(childrenFndMenuList));
                        }
                        list.add(map);
                    }
                }
        );
        return list;
    }

    @Override
    @Cacheable(key = "'pid:'+#p0")
    public List<FndMenu> listByPid(long pid) {
        return fndMenuDao.listByPid(pid);
    }

    @Override
    public Map<String, Object> buildTree(List<FndMenuDTO> menuDTOS) {
        List<FndMenuDTO> menuTrees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (FndMenuDTO menuDTO : menuDTOS) {
            if (menuDTO.getPid() == 0) {
                menuTrees.add(menuDTO);
            }
            for (FndMenuDTO it : menuDTOS) {
                if (it.getPid().equals(menuDTO.getId())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(new ArrayList<>());
                    }
                    menuDTO.getChildren().add(it);
                    ids.add(it.getId());
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        if (menuTrees.size() == 0) {
            menuTrees = menuDTOS.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        map.put("content", menuTrees);
        map.put("totalElements", menuDTOS.size());
        return map;
    }

    @Override
    public List<FndMenuDTO> listByRoles(List<FndRoleSmallDTO> roles) {
        Set<FndMenu> menus = new LinkedHashSet<>();
        List<Long> roleIds = new ArrayList<>();
        for (FndRoleSmallDTO role : roles) {
            roleIds.add(role.getId());
        }
        List<FndMenu> fndMenuList = fndMenuDao.listByRolesIdsAndType(roleIds,2);
        menus.addAll(fndMenuList);
        return menus.stream().map(fndMenuMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<FndMenuVo> buildMenus(List<FndMenuDTO> menuDTOS) {
        List<FndMenuVo> list = new LinkedList<>();
        menuDTOS.forEach(fndMenuDTO -> {
            if (fndMenuDTO != null) {
                List<FndMenuDTO> fndMenuDTOList = fndMenuDTO.getChildren();
                FndMenuVo menuVo = new FndMenuVo();
                menuVo.setName(ObjectUtil.isNotEmpty(fndMenuDTO.getComponentName()) ? fndMenuDTO.getComponentName() : fndMenuDTO.getName());
                // 一级目录需要加斜杠，不然会报警告
                menuVo.setPath(fndMenuDTO.getPid() == 0 ? "/" + fndMenuDTO.getPath() : fndMenuDTO.getPath());
                menuVo.setHidden(fndMenuDTO.getHiddenFlag());
                // 如果不是外链
                if (!fndMenuDTO.getIFrameFlag()) {
                    if (fndMenuDTO.getPid() == 0) {
                        menuVo.setComponent(StrUtil.isEmpty(fndMenuDTO.getComponent()) ? "Layout" : fndMenuDTO.getComponent());
                    } else if (!StrUtil.isEmpty(fndMenuDTO.getComponent())) {
                        menuVo.setComponent(fndMenuDTO.getComponent());
                    }
                }
                menuVo.setMeta(new FndMenuMetaVo(fndMenuDTO.getName(), fndMenuDTO.getIcon(), fndMenuDTO.getIFrameFlag(), !fndMenuDTO.getCacheFlag(), fndMenuDTO.getBlankFlag(), fndMenuDTO.getSendTokenFlag()));
                if (fndMenuDTOList != null && fndMenuDTOList.size() != 0) {
                    menuVo.setAlwaysShow(true);
                    menuVo.setRedirect("noredirect");
                    menuVo.setChildren(buildMenus(fndMenuDTOList));
                    // 处理是一级菜单并且没有子菜单的情况
                } else if (fndMenuDTO.getPid() == 0) {
                    FndMenuVo menuVo1 = new FndMenuVo();
                    menuVo1.setMeta(menuVo.getMeta());
                    // 非外链
                    if (!fndMenuDTO.getIFrameFlag()) {
                        menuVo1.setPath("index");
                        menuVo1.setName(menuVo.getName());
                        menuVo1.setComponent(menuVo.getComponent());
                    } else {
                        menuVo1.setPath(fndMenuDTO.getPath());
                    }
                    menuVo.setName(null);
                    menuVo.setMeta(null);
                    menuVo.setComponent("Layout");
                    List<FndMenuVo> list1 = new ArrayList<>();
                    list1.add(menuVo1);
                    menuVo.setChildren(list1);
                }
                list.add(menuVo);
            }
        });
        return list;
    }

    @Override
    public FndMenu getOne(Long id) {
        FndMenu menu = Optional.ofNullable(fndMenuDao.getByKey(id)).orElseGet(FndMenu::new);
        ValidationUtil.isNull(menu.getId(), "Menu", "id", id);
        return menu;
    }
}
