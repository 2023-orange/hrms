package com.sunten.hrms.fnd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.fnd.dao.FndDynamicQueryOperatorGroupDao;
import com.sunten.hrms.fnd.domain.FndDynamicQueryOperatorGroup;
import com.sunten.hrms.fnd.dto.FndDynamicQueryOperatorGroupDTO;
import com.sunten.hrms.fnd.dto.FndDynamicQueryOperatorGroupDetailDTO;
import com.sunten.hrms.fnd.dto.FndDynamicQueryOperatorGroupDetailQueryCriteria;
import com.sunten.hrms.fnd.dto.FndDynamicQueryOperatorGroupQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndDynamicQueryOperatorGroupMapper;
import com.sunten.hrms.fnd.service.FndDynamicQueryOperatorGroupDetailService;
import com.sunten.hrms.fnd.service.FndDynamicQueryOperatorGroupService;
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
 *  服务实现类
 * </p>
 *
 * @author batan
 * @since 2022-07-26
 */
@Service
@CacheConfig(cacheNames = "dynamicQueryOperatorGroup")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndDynamicQueryOperatorGroupServiceImpl extends ServiceImpl<FndDynamicQueryOperatorGroupDao, FndDynamicQueryOperatorGroup> implements FndDynamicQueryOperatorGroupService {
    private final FndDynamicQueryOperatorGroupDao fndDynamicQueryOperatorGroupDao;
    private final FndDynamicQueryOperatorGroupMapper fndDynamicQueryOperatorGroupMapper;
    private final FndDynamicQueryOperatorGroupDetailService fndDynamicQueryOperatorGroupDetailService;

    public FndDynamicQueryOperatorGroupServiceImpl(FndDynamicQueryOperatorGroupDao fndDynamicQueryOperatorGroupDao, FndDynamicQueryOperatorGroupMapper fndDynamicQueryOperatorGroupMapper, FndDynamicQueryOperatorGroupDetailService fndDynamicQueryOperatorGroupDetailService) {
        this.fndDynamicQueryOperatorGroupDao = fndDynamicQueryOperatorGroupDao;
        this.fndDynamicQueryOperatorGroupMapper = fndDynamicQueryOperatorGroupMapper;
        this.fndDynamicQueryOperatorGroupDetailService = fndDynamicQueryOperatorGroupDetailService;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FndDynamicQueryOperatorGroupDTO insert(FndDynamicQueryOperatorGroup dynamicQueryOperatorGroupNew) {
        fndDynamicQueryOperatorGroupDao.insertAllColumn(dynamicQueryOperatorGroupNew);
        return fndDynamicQueryOperatorGroupMapper.toDto(dynamicQueryOperatorGroupNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        FndDynamicQueryOperatorGroup dynamicQueryOperatorGroup = new FndDynamicQueryOperatorGroup();
        dynamicQueryOperatorGroup.setId(id);
        this.delete(dynamicQueryOperatorGroup);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(FndDynamicQueryOperatorGroup dynamicQueryOperatorGroup) {
        // TODO    确认删除前是否需要做检查
        fndDynamicQueryOperatorGroupDao.deleteByEntityKey(dynamicQueryOperatorGroup);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(FndDynamicQueryOperatorGroup dynamicQueryOperatorGroupNew) {
        FndDynamicQueryOperatorGroup dynamicQueryOperatorGroupInDb = Optional.ofNullable(fndDynamicQueryOperatorGroupDao.getByKey(dynamicQueryOperatorGroupNew.getId())).orElseGet(FndDynamicQueryOperatorGroup::new);
        ValidationUtil.isNull(dynamicQueryOperatorGroupInDb.getId() ,"DynamicQueryOperatorGroup", "id", dynamicQueryOperatorGroupNew.getId());
        dynamicQueryOperatorGroupNew.setId(dynamicQueryOperatorGroupInDb.getId());
        fndDynamicQueryOperatorGroupDao.updateAllColumnByKey(dynamicQueryOperatorGroupNew);
    }

    @Override
    @Cacheable(key = "#p0")
    public FndDynamicQueryOperatorGroupDTO getByKey(Long id) {
        FndDynamicQueryOperatorGroup dynamicQueryOperatorGroup = Optional.ofNullable(fndDynamicQueryOperatorGroupDao.getByKey(id)).orElseGet(FndDynamicQueryOperatorGroup::new);
        ValidationUtil.isNull(dynamicQueryOperatorGroup.getId() ,"DynamicQueryOperatorGroup", "id", id);
        return fndDynamicQueryOperatorGroupMapper.toDto(dynamicQueryOperatorGroup);
    }

    @Override
    @Cacheable
    public List<FndDynamicQueryOperatorGroupDTO> listAll(FndDynamicQueryOperatorGroupQueryCriteria criteria) {
        List<FndDynamicQueryOperatorGroup> dynamicQueryOperatorGroups = fndDynamicQueryOperatorGroupDao.listAllByCriteria(criteria);
        List<FndDynamicQueryOperatorGroupDTO> dynamicQueryOperatorGroupDTOs = fndDynamicQueryOperatorGroupMapper.toDto(dynamicQueryOperatorGroups);
        expandDetail(criteria, dynamicQueryOperatorGroupDTOs);
        return dynamicQueryOperatorGroupDTOs;
    }

    @Override
    @Cacheable
    public Map<String, Object> listAll(FndDynamicQueryOperatorGroupQueryCriteria criteria, Pageable pageable) {
        Page<FndDynamicQueryOperatorGroup> page = PageUtil.startPage(pageable);
        List<FndDynamicQueryOperatorGroup> dynamicQueryOperatorGroups = fndDynamicQueryOperatorGroupDao.listAllByCriteriaPage(page, criteria);
        List<FndDynamicQueryOperatorGroupDTO> dynamicQueryOperatorGroupDTOs = fndDynamicQueryOperatorGroupMapper.toDto(dynamicQueryOperatorGroups);
        expandDetail(criteria, dynamicQueryOperatorGroupDTOs);
        return PageUtil.toPage(dynamicQueryOperatorGroupDTOs, page.getTotal());
    }

    private void expandDetail(FndDynamicQueryOperatorGroupQueryCriteria criteria, List<FndDynamicQueryOperatorGroupDTO> dynamicQueryOperatorGroupDTOs) {
        if(null != criteria.getExpandDetail() && criteria.getExpandDetail()){
            for(FndDynamicQueryOperatorGroupDTO dynamicQueryOperatorGroupDTO:dynamicQueryOperatorGroupDTOs){
                FndDynamicQueryOperatorGroupDetailQueryCriteria queryCriteria = new FndDynamicQueryOperatorGroupDetailQueryCriteria();
                queryCriteria.setEnabled(true);
                queryCriteria.setGroupId(dynamicQueryOperatorGroupDTO.getId());
                List<FndDynamicQueryOperatorGroupDetailDTO> dynamicQueryOperatorGroupDetailDTOS = fndDynamicQueryOperatorGroupDetailService.listAll(queryCriteria);
                dynamicQueryOperatorGroupDTO.setDynamicQueryOperatorGroupDetails(dynamicQueryOperatorGroupDetailDTOS);
            }
        }
    }

    @Override
    public void download(List<FndDynamicQueryOperatorGroupDTO> dynamicQueryOperatorGroupDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndDynamicQueryOperatorGroupDTO dynamicQueryOperatorGroupDTO : dynamicQueryOperatorGroupDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", dynamicQueryOperatorGroupDTO.getId());
            map.put("name", dynamicQueryOperatorGroupDTO.getName());
            map.put("remark", dynamicQueryOperatorGroupDTO.getRemark());
            map.put("sort", dynamicQueryOperatorGroupDTO.getSort());
            map.put("enabledFlag", dynamicQueryOperatorGroupDTO.getEnabledFlag());
            map.put("createTime", dynamicQueryOperatorGroupDTO.getCreateTime());
            map.put("createBy", dynamicQueryOperatorGroupDTO.getCreateBy());
            map.put("updateTime", dynamicQueryOperatorGroupDTO.getUpdateTime());
            map.put("updateBy", dynamicQueryOperatorGroupDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
