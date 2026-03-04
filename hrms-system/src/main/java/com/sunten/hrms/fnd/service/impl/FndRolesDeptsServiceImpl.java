package com.sunten.hrms.fnd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.fnd.dao.FndRolesDeptsDao;
import com.sunten.hrms.fnd.domain.FndRolesDepts;
import com.sunten.hrms.fnd.dto.FndRolesDeptsDTO;
import com.sunten.hrms.fnd.dto.FndRolesDeptsQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndRolesDeptsMapper;
import com.sunten.hrms.fnd.service.FndRolesDeptsService;
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
@CacheConfig(cacheNames = "rolesDepts")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndRolesDeptsServiceImpl extends ServiceImpl<FndRolesDeptsDao, FndRolesDepts> implements FndRolesDeptsService {
    private final FndRolesDeptsDao fndRolesDeptsDao;
    private final FndRolesDeptsMapper fndRolesDeptsMapper;

    public FndRolesDeptsServiceImpl(FndRolesDeptsDao fndRolesDeptsDao, FndRolesDeptsMapper fndRolesDeptsMapper) {
        this.fndRolesDeptsDao = fndRolesDeptsDao;
        this.fndRolesDeptsMapper = fndRolesDeptsMapper;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FndRolesDeptsDTO insert(FndRolesDepts rolesDeptsNew) {
        fndRolesDeptsDao.insertAllColumn(rolesDeptsNew);
        return fndRolesDeptsMapper.toDto(rolesDeptsNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long roleId, Long deptId) {
        fndRolesDeptsDao.deleteByKey(roleId, deptId);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(FndRolesDepts rolesDeptsNew) {
        FndRolesDepts rolesDeptsTemp = Optional.ofNullable(fndRolesDeptsDao.getByKey(rolesDeptsNew.getRoleId(), rolesDeptsNew.getDeptId())).orElseGet(FndRolesDepts::new);
        ValidationUtil.isNull(rolesDeptsTemp.getRoleId(), "RolesDepts", "roleId", rolesDeptsNew.getRoleId());
        rolesDeptsNew.setRoleId(rolesDeptsTemp.getRoleId());
        ValidationUtil.isNull(rolesDeptsTemp.getDeptId(), "RolesDepts", "deptId", rolesDeptsNew.getDeptId());
        rolesDeptsNew.setDeptId(rolesDeptsTemp.getDeptId());
        fndRolesDeptsDao.updateAllColumnByKey(rolesDeptsNew);
    }

    @Override
    @Cacheable(key = "#p0")
    public FndRolesDeptsDTO getByKey(Long roleId, Long deptId) {
        FndRolesDepts rolesDepts = Optional.ofNullable(fndRolesDeptsDao.getByKey(roleId, deptId)).orElseGet(FndRolesDepts::new);
        ValidationUtil.isNull(rolesDepts.getRoleId(), "RolesDepts", "roleId", roleId);
        ValidationUtil.isNull(rolesDepts.getDeptId(), "RolesDepts", "deptId", deptId);
        return fndRolesDeptsMapper.toDto(rolesDepts);
    }

    @Override
    @Cacheable
    public List<FndRolesDeptsDTO> listAll(FndRolesDeptsQueryCriteria criteria) {
        return fndRolesDeptsMapper.toDto(fndRolesDeptsDao.listAllByCriteria(criteria));
    }

    @Override
    @Cacheable
    public Map<String, Object> listAll(FndRolesDeptsQueryCriteria criteria, Pageable pageable) {
        Page<FndRolesDepts> page = PageUtil.startPage(pageable);
        List<FndRolesDepts> rolesDeptss = fndRolesDeptsDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndRolesDeptsMapper.toDto(rolesDeptss), page.getTotal());
    }

    @Override
    public void download(List<FndRolesDeptsDTO> rolesDeptsDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndRolesDeptsDTO rolesDeptsDTO : rolesDeptsDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("roleId", rolesDeptsDTO.getRoleId());
            map.put("deptId", rolesDeptsDTO.getDeptId());
            map.put("createTime", rolesDeptsDTO.getCreateTime());
            map.put("createBy", rolesDeptsDTO.getCreateBy());
            map.put("updateTime", rolesDeptsDTO.getUpdateTime());
            map.put("updateBy", rolesDeptsDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
