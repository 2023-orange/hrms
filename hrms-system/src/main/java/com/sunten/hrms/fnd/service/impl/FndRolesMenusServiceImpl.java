package com.sunten.hrms.fnd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.fnd.dao.FndRolesMenusDao;
import com.sunten.hrms.fnd.domain.FndRolesMenus;
import com.sunten.hrms.fnd.dto.FndRolesMenusDTO;
import com.sunten.hrms.fnd.dto.FndRolesMenusQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndRolesMenusMapper;
import com.sunten.hrms.fnd.service.FndRolesMenusService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
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

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@Service
@CacheConfig(cacheNames = "rolesMenus")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndRolesMenusServiceImpl extends ServiceImpl<FndRolesMenusDao, FndRolesMenus> implements FndRolesMenusService {
    private final FndRolesMenusDao fndRolesMenusDao;
    private final FndRolesMenusMapper fndRolesMenusMapper;

    public FndRolesMenusServiceImpl(FndRolesMenusDao fndRolesMenusDao, FndRolesMenusMapper fndRolesMenusMapper) {
        this.fndRolesMenusDao = fndRolesMenusDao;
        this.fndRolesMenusMapper = fndRolesMenusMapper;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FndRolesMenusDTO insert(FndRolesMenus rolesMenusNew) {
        fndRolesMenusDao.insertAllColumn(rolesMenusNew);
        return fndRolesMenusMapper.toDto(rolesMenusNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long menuId, Long roleId) {
        fndRolesMenusDao.deleteByKey(menuId, roleId);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(FndRolesMenus rolesMenusNew) {
        FndRolesMenus rolesMenusTemp = Optional.ofNullable(fndRolesMenusDao.getByKey(rolesMenusNew.getMenuId(), rolesMenusNew.getRoleId())).orElseGet(FndRolesMenus::new);
        ValidationUtil.isNull(rolesMenusTemp.getMenuId(), "RolesMenus", "menuId", rolesMenusNew.getMenuId());
        rolesMenusNew.setMenuId(rolesMenusTemp.getMenuId());
        ValidationUtil.isNull(rolesMenusTemp.getRoleId(), "RolesMenus", "roleId", rolesMenusNew.getRoleId());
        rolesMenusNew.setRoleId(rolesMenusTemp.getRoleId());
        fndRolesMenusDao.updateAllColumnByKey(rolesMenusNew);
    }

    @Override
    @Cacheable(key = "#p0")
    public FndRolesMenusDTO getByKey(Long menuId, Long roleId) {
        FndRolesMenus rolesMenus = Optional.ofNullable(fndRolesMenusDao.getByKey(menuId, roleId)).orElseGet(FndRolesMenus::new);
        ValidationUtil.isNull(rolesMenus.getMenuId(), "RolesMenus", "menuId", menuId);
        ValidationUtil.isNull(rolesMenus.getRoleId(), "RolesMenus", "roleId", roleId);
        return fndRolesMenusMapper.toDto(rolesMenus);
    }

    @Override
    @Cacheable
    public List<FndRolesMenusDTO> listAll(FndRolesMenusQueryCriteria criteria) {
        return fndRolesMenusMapper.toDto(fndRolesMenusDao.listAllByCriteria(criteria));
    }

    @Override
    @Cacheable
    public Map<String, Object> listAll(FndRolesMenusQueryCriteria criteria, Pageable pageable) {
        Page<FndRolesMenus> page = PageUtil.startPage(pageable);
        List<FndRolesMenus> rolesMenuss = fndRolesMenusDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndRolesMenusMapper.toDto(rolesMenuss), page.getTotal());
    }

    @Override
    public void download(List<FndRolesMenusDTO> rolesMenusDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndRolesMenusDTO rolesMenusDTO : rolesMenusDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("菜单ID", rolesMenusDTO.getMenuId());
            map.put("角色ID", rolesMenusDTO.getRoleId());
            map.put("createTime", rolesMenusDTO.getCreateTime());
            map.put("createBy", rolesMenusDTO.getCreateBy());
            map.put("updateTime", rolesMenusDTO.getUpdateTime());
            map.put("updateBy", rolesMenusDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
