package com.sunten.hrms.fnd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.fnd.dao.FndUsersRolesDao;
import com.sunten.hrms.fnd.domain.FndUsersRoles;
import com.sunten.hrms.fnd.dto.FndUsersRolesDTO;
import com.sunten.hrms.fnd.dto.FndUsersRolesQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndUsersRolesMapper;
import com.sunten.hrms.fnd.service.FndUsersRolesService;
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
@CacheConfig(cacheNames = "usersRoles")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndUsersRolesServiceImpl extends ServiceImpl<FndUsersRolesDao, FndUsersRoles> implements FndUsersRolesService {
    private final FndUsersRolesDao fndUsersRolesDao;
    private final FndUsersRolesMapper fndUsersRolesMapper;

    public FndUsersRolesServiceImpl(FndUsersRolesDao fndUsersRolesDao, FndUsersRolesMapper fndUsersRolesMapper) {
        this.fndUsersRolesDao = fndUsersRolesDao;
        this.fndUsersRolesMapper = fndUsersRolesMapper;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FndUsersRolesDTO insert(FndUsersRoles usersRolesNew) {
        fndUsersRolesDao.insertAllColumn(usersRolesNew);
        return fndUsersRolesMapper.toDto(usersRolesNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long userId, Long roleId) {
        fndUsersRolesDao.deleteByKey(userId, roleId);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(FndUsersRoles usersRolesNew) {
        FndUsersRoles usersRolesTemp = Optional.ofNullable(fndUsersRolesDao.getByKey(usersRolesNew.getUserId(), usersRolesNew.getRoleId())).orElseGet(FndUsersRoles::new);
        ValidationUtil.isNull(usersRolesTemp.getUserId(), "UsersRoles", "userId", usersRolesNew.getUserId());
        usersRolesNew.setUserId(usersRolesTemp.getUserId());
        ValidationUtil.isNull(usersRolesTemp.getRoleId(), "UsersRoles", "roleId", usersRolesNew.getRoleId());
        usersRolesNew.setRoleId(usersRolesTemp.getRoleId());
        fndUsersRolesDao.updateAllColumnByKey(usersRolesNew);
    }

    @Override
    @Cacheable(key = "#p0")
    public FndUsersRolesDTO getByKey(Long userId, Long roleId) {
        FndUsersRoles usersRoles = Optional.ofNullable(fndUsersRolesDao.getByKey(userId, roleId)).orElseGet(FndUsersRoles::new);
        ValidationUtil.isNull(usersRoles.getUserId(), "UsersRoles", "userId", userId);
        ValidationUtil.isNull(usersRoles.getRoleId(), "UsersRoles", "roleId", roleId);
        return fndUsersRolesMapper.toDto(usersRoles);
    }

    @Override
    @Cacheable
    public List<FndUsersRolesDTO> listAll(FndUsersRolesQueryCriteria criteria) {
        return fndUsersRolesMapper.toDto(fndUsersRolesDao.listAllByCriteria(criteria));
    }

    @Override
    @Cacheable
    public Map<String, Object> listAll(FndUsersRolesQueryCriteria criteria, Pageable pageable) {
        Page<FndUsersRoles> page = PageUtil.startPage(pageable);
        List<FndUsersRoles> usersRoless = fndUsersRolesDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndUsersRolesMapper.toDto(usersRoless), page.getTotal());
    }

    @Override
    public void download(List<FndUsersRolesDTO> usersRolesDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndUsersRolesDTO usersRolesDTO : usersRolesDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("用户ID", usersRolesDTO.getUserId());
            map.put("角色ID", usersRolesDTO.getRoleId());
            map.put("createTime", usersRolesDTO.getCreateTime());
            map.put("createBy", usersRolesDTO.getCreateBy());
            map.put("updateTime", usersRolesDTO.getUpdateTime());
            map.put("updateBy", usersRolesDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Boolean checkHaveRoleBySeIdAndPermission(Long seId, String permission) {
        return fndUsersRolesDao.checkHaveRoleBySeIdAndPermission(seId, permission);
    }

    @Override
    public Boolean checkHaveRoleByUserIdAndPermission(Long userId, String permission) {
        return fndUsersRolesDao.checkHaveRoleByUserIdAndPermission(userId, permission);
    }
}
