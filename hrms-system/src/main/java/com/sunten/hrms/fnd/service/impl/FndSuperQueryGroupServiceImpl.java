package com.sunten.hrms.fnd.service.impl;

import com.sunten.hrms.fnd.domain.FndSuperQueryGroup;
import com.sunten.hrms.fnd.dao.FndSuperQueryGroupDao;
import com.sunten.hrms.fnd.service.FndSuperQueryGroupService;
import com.sunten.hrms.fnd.dto.FndSuperQueryGroupDTO;
import com.sunten.hrms.fnd.dto.FndSuperQueryGroupQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndSuperQueryGroupMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author batan
 * @since 2022-08-12
 */
@Service
@CacheConfig(cacheNames = "superQueryGroup")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndSuperQueryGroupServiceImpl extends ServiceImpl<FndSuperQueryGroupDao, FndSuperQueryGroup> implements FndSuperQueryGroupService {
    private final FndSuperQueryGroupDao fndSuperQueryGroupDao;
    private final FndSuperQueryGroupMapper fndSuperQueryGroupMapper;

    public FndSuperQueryGroupServiceImpl(FndSuperQueryGroupDao fndSuperQueryGroupDao, FndSuperQueryGroupMapper fndSuperQueryGroupMapper) {
        this.fndSuperQueryGroupDao = fndSuperQueryGroupDao;
        this.fndSuperQueryGroupMapper = fndSuperQueryGroupMapper;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FndSuperQueryGroupDTO insert(FndSuperQueryGroup superQueryGroupNew) {
        fndSuperQueryGroupDao.insertAllColumn(superQueryGroupNew);
        return fndSuperQueryGroupMapper.toDto(superQueryGroupNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        FndSuperQueryGroup superQueryGroup = new FndSuperQueryGroup();
        superQueryGroup.setId(id);
        this.delete(superQueryGroup);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(FndSuperQueryGroup superQueryGroup) {
        // TODO    确认删除前是否需要做检查
        fndSuperQueryGroupDao.deleteByEntityKey(superQueryGroup);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(FndSuperQueryGroup superQueryGroupNew) {
        FndSuperQueryGroup superQueryGroupInDb = Optional.ofNullable(fndSuperQueryGroupDao.getByKey(superQueryGroupNew.getId())).orElseGet(FndSuperQueryGroup::new);
        ValidationUtil.isNull(superQueryGroupInDb.getId() ,"SuperQueryGroup", "id", superQueryGroupNew.getId());
        superQueryGroupNew.setId(superQueryGroupInDb.getId());
        fndSuperQueryGroupDao.updateAllColumnByKey(superQueryGroupNew);
    }

    @Override
    @Cacheable(key = "#p0")
    public FndSuperQueryGroupDTO getByKey(Long id) {
        FndSuperQueryGroup superQueryGroup = Optional.ofNullable(fndSuperQueryGroupDao.getByKey(id)).orElseGet(FndSuperQueryGroup::new);
        ValidationUtil.isNull(superQueryGroup.getId() ,"SuperQueryGroup", "id", id);
        return fndSuperQueryGroupMapper.toDto(superQueryGroup);
    }

    @Override
    @Cacheable
    public List<FndSuperQueryGroup> listAllExpand(FndSuperQueryGroupQueryCriteria criteria) {
        return fndSuperQueryGroupDao.listAllByCriteriaExpand(criteria);
    }

    @Override
    public List<FndSuperQueryGroupDTO> listAll(FndSuperQueryGroupQueryCriteria criteria) {
        return fndSuperQueryGroupMapper.toDto(fndSuperQueryGroupDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(FndSuperQueryGroupQueryCriteria criteria, Pageable pageable) {
        Page<FndSuperQueryGroup> page = PageUtil.startPage(pageable);
        List<FndSuperQueryGroup> superQueryGroups = fndSuperQueryGroupDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndSuperQueryGroupMapper.toDto(superQueryGroups), page.getTotal());
    }

    @Override
    public void download(List<FndSuperQueryGroupDTO> superQueryGroupDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndSuperQueryGroupDTO superQueryGroupDTO : superQueryGroupDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", superQueryGroupDTO.getId());
            map.put("groupName", superQueryGroupDTO.getGroupName());
            map.put("remark", superQueryGroupDTO.getRemark());
            map.put("enabledFlag", superQueryGroupDTO.getEnabledFlag());
            map.put("createTime", superQueryGroupDTO.getCreateTime());
            map.put("createBy", superQueryGroupDTO.getCreateBy());
            map.put("updateTime", superQueryGroupDTO.getUpdateTime());
            map.put("updateBy", superQueryGroupDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
