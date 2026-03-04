package com.sunten.hrms.fnd.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.fnd.dao.FndDictDao;
import com.sunten.hrms.fnd.domain.FndDict;
import com.sunten.hrms.fnd.dto.FndDictDTO;
import com.sunten.hrms.fnd.dto.FndDictDetailDTO;
import com.sunten.hrms.fnd.dto.FndDictQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndDictMapper;
import com.sunten.hrms.fnd.service.FndDictService;
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
@CacheConfig(cacheNames = "dict")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndDictServiceImpl extends ServiceImpl<FndDictDao, FndDict> implements FndDictService {
    private final FndDictDao fndDictDao;
    private final FndDictMapper fndDictMapper;

    public FndDictServiceImpl(FndDictDao fndDictDao, FndDictMapper fndDictMapper) {
        this.fndDictDao = fndDictDao;
        this.fndDictMapper = fndDictMapper;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FndDictDTO insert(FndDict dictNew) {
        fndDictDao.insertAllColumn(dictNew);
        return fndDictMapper.toDto(dictNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        fndDictDao.deleteByKey(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(FndDict dictNew) {
        FndDict dictTemp = Optional.ofNullable(fndDictDao.getByKey(dictNew.getId())).orElseGet(FndDict::new);
        ValidationUtil.isNull(dictTemp.getId(), "Dict", "id", dictNew.getId());
        dictNew.setId(dictTemp.getId());
        fndDictDao.updateAllColumnByKey(dictNew);
    }

    @Override
    @Cacheable(key = "#p0")
    public FndDictDTO getByKey(Long id) {
        FndDict dict = Optional.ofNullable(fndDictDao.getByKey(id)).orElseGet(FndDict::new);
        ValidationUtil.isNull(dict.getId(), "Dict", "id", id);
        return fndDictMapper.toDto(dict);
    }

    @Override
    @Cacheable
    public List<FndDictDTO> listAll(FndDictQueryCriteria criteria) {
        return fndDictMapper.toDto(fndDictDao.listAllByCriteria(criteria));
    }

    @Override
    @Cacheable
    public Map<String, Object> listAll(FndDictQueryCriteria criteria, Pageable pageable) {
        Page<FndDict> page = PageUtil.startPage(pageable);
        List<FndDict> dicts = fndDictDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndDictMapper.toDto(dicts), page.getTotal());
    }

    @Override
    public void download(List<FndDictDTO> dictDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndDictDTO dictDTO : dictDTOS) {
            if (CollectionUtil.isNotEmpty(dictDTO.getDictDetails())) {
                for (FndDictDetailDTO dictDetail : dictDTO.getDictDetails()) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("字典名称", dictDTO.getName());
                    map.put("字典描述", dictDTO.getRemark());
                    map.put("字典标签", dictDetail.getLabel());
                    map.put("字典值", dictDetail.getValue());
                    map.put("创建日期", dictDetail.getCreateTime());
                    list.add(map);
                }
            } else {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("字典名称", dictDTO.getName());
                map.put("字典描述", dictDTO.getRemark());
                map.put("字典标签", null);
                map.put("字典值", null);
                map.put("创建日期", dictDTO.getCreateTime());
                list.add(map);
            }
        }
        FileUtil.downloadExcel(list, response);
    }
}
