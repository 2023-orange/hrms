package com.sunten.hrms.fnd.service.impl;

import com.sunten.hrms.fnd.domain.FndSuperQueryTableColumn;
import com.sunten.hrms.fnd.dao.FndSuperQueryTableColumnDao;
import com.sunten.hrms.fnd.service.FndSuperQueryTableColumnService;
import com.sunten.hrms.fnd.dto.FndSuperQueryTableColumnDTO;
import com.sunten.hrms.fnd.dto.FndSuperQueryTableColumnQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndSuperQueryTableColumnMapper;
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
@CacheConfig(cacheNames = "superQueryTableColumn")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndSuperQueryTableColumnServiceImpl extends ServiceImpl<FndSuperQueryTableColumnDao, FndSuperQueryTableColumn> implements FndSuperQueryTableColumnService {
    private final FndSuperQueryTableColumnDao fndSuperQueryTableColumnDao;
    private final FndSuperQueryTableColumnMapper fndSuperQueryTableColumnMapper;

    public FndSuperQueryTableColumnServiceImpl(FndSuperQueryTableColumnDao fndSuperQueryTableColumnDao, FndSuperQueryTableColumnMapper fndSuperQueryTableColumnMapper) {
        this.fndSuperQueryTableColumnDao = fndSuperQueryTableColumnDao;
        this.fndSuperQueryTableColumnMapper = fndSuperQueryTableColumnMapper;
    }

    @Override
    @Caching(evict = {@CacheEvict(allEntries = true),
            @CacheEvict(value = "superQueryGroup", allEntries = true),
            @CacheEvict(value = "superQueryTable", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public FndSuperQueryTableColumnDTO insert(FndSuperQueryTableColumn superQueryTableColumnNew) {
        fndSuperQueryTableColumnDao.insertAllColumn(superQueryTableColumnNew);
        return fndSuperQueryTableColumnMapper.toDto(superQueryTableColumnNew);
    }

    @Override
    @Caching(evict = {@CacheEvict(allEntries = true),
            @CacheEvict(value = "superQueryGroup", allEntries = true),
            @CacheEvict(value = "superQueryTable", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        FndSuperQueryTableColumn superQueryTableColumn = new FndSuperQueryTableColumn();
        superQueryTableColumn.setId(id);
        this.delete(superQueryTableColumn);
    }

    @Override
    @Caching(evict = {@CacheEvict(allEntries = true),
            @CacheEvict(value = "superQueryGroup", allEntries = true),
            @CacheEvict(value = "superQueryTable", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void delete(FndSuperQueryTableColumn superQueryTableColumn) {
        // TODO    确认删除前是否需要做检查
        fndSuperQueryTableColumnDao.deleteByEntityKey(superQueryTableColumn);
    }

    @Override
    @Caching(evict = {@CacheEvict(allEntries = true),
            @CacheEvict(value = "superQueryGroup", allEntries = true),
            @CacheEvict(value = "superQueryTable", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void update(FndSuperQueryTableColumn superQueryTableColumnNew) {
        FndSuperQueryTableColumn superQueryTableColumnInDb = Optional.ofNullable(fndSuperQueryTableColumnDao.getByKey(superQueryTableColumnNew.getId())).orElseGet(FndSuperQueryTableColumn::new);
        ValidationUtil.isNull(superQueryTableColumnInDb.getId() ,"SuperQueryTableColumn", "id", superQueryTableColumnNew.getId());
        superQueryTableColumnNew.setId(superQueryTableColumnInDb.getId());
        fndSuperQueryTableColumnDao.updateAllColumnByKey(superQueryTableColumnNew);
    }

    @Override
    @Cacheable(key = "#p0")
    public FndSuperQueryTableColumnDTO getByKey(Long id) {
        FndSuperQueryTableColumn superQueryTableColumn = Optional.ofNullable(fndSuperQueryTableColumnDao.getByKey(id)).orElseGet(FndSuperQueryTableColumn::new);
        ValidationUtil.isNull(superQueryTableColumn.getId() ,"SuperQueryTableColumn", "id", id);
        return fndSuperQueryTableColumnMapper.toDto(superQueryTableColumn);
    }

    @Override
    public List<FndSuperQueryTableColumnDTO> listAll(FndSuperQueryTableColumnQueryCriteria criteria) {
        return fndSuperQueryTableColumnMapper.toDto(fndSuperQueryTableColumnDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(FndSuperQueryTableColumnQueryCriteria criteria, Pageable pageable) {
        Page<FndSuperQueryTableColumn> page = PageUtil.startPage(pageable);
        List<FndSuperQueryTableColumn> superQueryTableColumns = fndSuperQueryTableColumnDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndSuperQueryTableColumnMapper.toDto(superQueryTableColumns), page.getTotal());
    }

    @Override
    public void download(List<FndSuperQueryTableColumnDTO> superQueryTableColumnDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndSuperQueryTableColumnDTO superQueryTableColumnDTO : superQueryTableColumnDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", superQueryTableColumnDTO.getId());
            map.put("groupId", superQueryTableColumnDTO.getGroup().getId());
            map.put("tableId", superQueryTableColumnDTO.getTable().getId());
            map.put("tableAbbreviation", superQueryTableColumnDTO.getTableAbbreviation());
            map.put("columnName", superQueryTableColumnDTO.getColumnName());
            map.put("columnDescription", superQueryTableColumnDTO.getColumnDescription());
            map.put("convertStyle", superQueryTableColumnDTO.getConvertStyle());
            map.put("alias", superQueryTableColumnDTO.getAlias());
            map.put("sort", superQueryTableColumnDTO.getSort());
            map.put("enabledFlag", superQueryTableColumnDTO.getEnabledFlag());
            map.put("createTime", superQueryTableColumnDTO.getCreateTime());
            map.put("createBy", superQueryTableColumnDTO.getCreateBy());
            map.put("updateTime", superQueryTableColumnDTO.getUpdateTime());
            map.put("updateBy", superQueryTableColumnDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
