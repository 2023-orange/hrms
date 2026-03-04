package com.sunten.hrms.fnd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.fnd.dao.FndDynamicQueryGroupDao;
import com.sunten.hrms.fnd.domain.FndDynamicQueryGroup;
import com.sunten.hrms.fnd.dto.FndDynamicQueryGroupDTO;
import com.sunten.hrms.fnd.dto.FndDynamicQueryGroupQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndDynamicQueryGroupMapper;
//import com.sunten.hrms.fnd.service.FndDynamicQueryGroupDetailService;
import com.sunten.hrms.fnd.service.FndDynamicQueryGroupDetailService;
import com.sunten.hrms.fnd.service.FndDynamicQueryGroupService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
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

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author batan
 * @since 2022-07-26
 */
@Service
@CacheConfig(cacheNames = "dynamicQueryGroup")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndDynamicQueryGroupServiceImpl extends ServiceImpl<FndDynamicQueryGroupDao, FndDynamicQueryGroup> implements FndDynamicQueryGroupService {
    private final FndDynamicQueryGroupDao fndDynamicQueryGroupDao;
    private final FndDynamicQueryGroupMapper fndDynamicQueryGroupMapper;
    private final FndDynamicQueryGroupDetailService fndDynamicQueryGroupDetailService;

    public FndDynamicQueryGroupServiceImpl(FndDynamicQueryGroupDao fndDynamicQueryGroupDao, FndDynamicQueryGroupMapper fndDynamicQueryGroupMapper, FndDynamicQueryGroupDetailService fndDynamicQueryGroupDetailService) {
        this.fndDynamicQueryGroupDao = fndDynamicQueryGroupDao;
        this.fndDynamicQueryGroupMapper = fndDynamicQueryGroupMapper;
        this.fndDynamicQueryGroupDetailService = fndDynamicQueryGroupDetailService;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FndDynamicQueryGroupDTO insert(FndDynamicQueryGroup dynamicQueryGroupNew) {
        fndDynamicQueryGroupDao.insertAllColumn(dynamicQueryGroupNew);
        return fndDynamicQueryGroupMapper.toDto(dynamicQueryGroupNew);
    }

    @Override
    @Caching(evict = {@CacheEvict(allEntries = true),
            @CacheEvict(value = "dynamicQueryGroupDetail", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        FndDynamicQueryGroup dynamicQueryGroup = new FndDynamicQueryGroup();
        dynamicQueryGroup.setId(id);
        this.delete(dynamicQueryGroup);
    }

    @Override
    @Caching(evict = {@CacheEvict(allEntries = true),
            @CacheEvict(value = "dynamicQueryGroupDetail", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void delete(FndDynamicQueryGroup dynamicQueryGroup) {
        fndDynamicQueryGroupDetailService.deleteByGroupId(dynamicQueryGroup.getId());
        fndDynamicQueryGroupDao.deleteByEntityKey(dynamicQueryGroup);
    }

    @Override
    @Caching(evict = {@CacheEvict(allEntries = true),
            @CacheEvict(value = "dynamicQueryGroupDetail", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void update(FndDynamicQueryGroup dynamicQueryGroupNew) {
        FndDynamicQueryGroup dynamicQueryGroupInDb = Optional.ofNullable(fndDynamicQueryGroupDao.getByKey(dynamicQueryGroupNew.getId())).orElseGet(FndDynamicQueryGroup::new);
        ValidationUtil.isNull(dynamicQueryGroupInDb.getId(), "DynamicQueryGroup", "id", dynamicQueryGroupNew.getId());
        dynamicQueryGroupNew.setId(dynamicQueryGroupInDb.getId());
        fndDynamicQueryGroupDao.updateAllColumnByKey(dynamicQueryGroupNew);
    }

    @Override
    @Cacheable(key = "#p0")
    public FndDynamicQueryGroupDTO getByKey(Long id) {
        FndDynamicQueryGroup dynamicQueryGroup = Optional.ofNullable(fndDynamicQueryGroupDao.getByKey(id)).orElseGet(FndDynamicQueryGroup::new);
        ValidationUtil.isNull(dynamicQueryGroup.getId(), "DynamicQueryGroup", "id", id);
        return fndDynamicQueryGroupMapper.toDto(dynamicQueryGroup);
    }

    @Override
    @Cacheable
    public List<FndDynamicQueryGroupDTO> listAll(FndDynamicQueryGroupQueryCriteria criteria) {
        return fndDynamicQueryGroupMapper.toDto(fndDynamicQueryGroupDao.listAllByCriteria(criteria));
    }

    @Override
    @Cacheable
    public Map<String, Object> listAll(FndDynamicQueryGroupQueryCriteria criteria, Pageable pageable) {
        Page<FndDynamicQueryGroup> page = PageUtil.startPage(pageable);
        List<FndDynamicQueryGroup> dynamicQueryGroups = fndDynamicQueryGroupDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndDynamicQueryGroupMapper.toDto(dynamicQueryGroups), page.getTotal());
    }

    @Override
    public void download(List<FndDynamicQueryGroupDTO> dynamicQueryGroupDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndDynamicQueryGroupDTO dynamicQueryGroupDTO : dynamicQueryGroupDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", dynamicQueryGroupDTO.getId());
            map.put("name", dynamicQueryGroupDTO.getName());
            map.put("remark", dynamicQueryGroupDTO.getRemark());
            map.put("enabledFlag", dynamicQueryGroupDTO.getEnabledFlag());
            map.put("createTime", dynamicQueryGroupDTO.getCreateTime());
            map.put("createBy", dynamicQueryGroupDTO.getCreateBy());
            map.put("updateTime", dynamicQueryGroupDTO.getUpdateTime());
            map.put("updateBy", dynamicQueryGroupDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
