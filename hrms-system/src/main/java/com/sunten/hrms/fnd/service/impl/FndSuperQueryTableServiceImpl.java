package com.sunten.hrms.fnd.service.impl;

import com.sunten.hrms.fnd.domain.FndSuperQueryTable;
import com.sunten.hrms.fnd.dao.FndSuperQueryTableDao;
import com.sunten.hrms.fnd.service.FndSuperQueryTableService;
import com.sunten.hrms.fnd.dto.FndSuperQueryTableDTO;
import com.sunten.hrms.fnd.dto.FndSuperQueryTableQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndSuperQueryTableMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
@CacheConfig(cacheNames = "superQueryTable")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndSuperQueryTableServiceImpl extends ServiceImpl<FndSuperQueryTableDao, FndSuperQueryTable> implements FndSuperQueryTableService {
    private final FndSuperQueryTableDao fndSuperQueryTableDao;
    private final FndSuperQueryTableMapper fndSuperQueryTableMapper;

    public FndSuperQueryTableServiceImpl(FndSuperQueryTableDao fndSuperQueryTableDao, FndSuperQueryTableMapper fndSuperQueryTableMapper) {
        this.fndSuperQueryTableDao = fndSuperQueryTableDao;
        this.fndSuperQueryTableMapper = fndSuperQueryTableMapper;
    }

    @Override
    @Caching(evict = {@CacheEvict(allEntries = true),
            @CacheEvict(value = "superQueryGroup", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public FndSuperQueryTableDTO insert(FndSuperQueryTable superQueryTableNew) {
        fndSuperQueryTableDao.insertAllColumn(superQueryTableNew);
        return fndSuperQueryTableMapper.toDto(superQueryTableNew);
    }

    @Override
    @Caching(evict = {@CacheEvict(allEntries = true),
            @CacheEvict(value = "superQueryGroup", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        FndSuperQueryTable superQueryTable = new FndSuperQueryTable();
        superQueryTable.setId(id);
        this.delete(superQueryTable);
    }

    @Override
    @Caching(evict = {@CacheEvict(allEntries = true),
            @CacheEvict(value = "superQueryGroup", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void delete(FndSuperQueryTable superQueryTable) {
        // TODO    确认删除前是否需要做检查
        fndSuperQueryTableDao.deleteByEntityKey(superQueryTable);
    }

    @Override
    @Caching(evict = {@CacheEvict(allEntries = true),
            @CacheEvict(value = "superQueryGroup", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void update(FndSuperQueryTable superQueryTableNew) {
        FndSuperQueryTable superQueryTableInDb = Optional.ofNullable(fndSuperQueryTableDao.getByKey(superQueryTableNew.getId())).orElseGet(FndSuperQueryTable::new);
        ValidationUtil.isNull(superQueryTableInDb.getId() ,"SuperQueryTable", "id", superQueryTableNew.getId());
        superQueryTableNew.setId(superQueryTableInDb.getId());
        fndSuperQueryTableDao.updateAllColumnByKey(superQueryTableNew);
    }

    @Override
    @Cacheable(key = "#p0")
    public FndSuperQueryTableDTO getByKey(Long id) {
        FndSuperQueryTable superQueryTable = Optional.ofNullable(fndSuperQueryTableDao.getByKey(id)).orElseGet(FndSuperQueryTable::new);
        ValidationUtil.isNull(superQueryTable.getId() ,"SuperQueryTable", "id", id);
        return fndSuperQueryTableMapper.toDto(superQueryTable);
    }

    @Override
    public List<FndSuperQueryTableDTO> listAll(FndSuperQueryTableQueryCriteria criteria) {
        return fndSuperQueryTableMapper.toDto(fndSuperQueryTableDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(FndSuperQueryTableQueryCriteria criteria, Pageable pageable) {
        Page<FndSuperQueryTable> page = PageUtil.startPage(pageable);
        List<FndSuperQueryTable> superQueryTables = fndSuperQueryTableDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndSuperQueryTableMapper.toDto(superQueryTables), page.getTotal());
    }

    @Override
    public void download(List<FndSuperQueryTableDTO> superQueryTableDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndSuperQueryTableDTO superQueryTableDTO : superQueryTableDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", superQueryTableDTO.getId());
            map.put("groupId", superQueryTableDTO.getGroup().getId());
            map.put("tableName", superQueryTableDTO.getTableName());
            map.put("tableDescription", superQueryTableDTO.getTableDescription());
            map.put("tableAbbreviation", superQueryTableDTO.getTableAbbreviation());
            map.put("tableArea", superQueryTableDTO.getTableArea());
            map.put("whereArea", superQueryTableDTO.getWhereArea());
            map.put("sort", superQueryTableDTO.getSort());
            map.put("enabledFlag", superQueryTableDTO.getEnabledFlag());
            map.put("createTime", superQueryTableDTO.getCreateTime());
            map.put("createBy", superQueryTableDTO.getCreateBy());
            map.put("updateTime", superQueryTableDTO.getUpdateTime());
            map.put("updateBy", superQueryTableDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
