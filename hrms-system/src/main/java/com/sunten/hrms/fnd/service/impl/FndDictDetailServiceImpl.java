package com.sunten.hrms.fnd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.fnd.dao.FndDictDetailDao;
import com.sunten.hrms.fnd.domain.FndDictDetail;
import com.sunten.hrms.fnd.dto.FndDictDetailDTO;
import com.sunten.hrms.fnd.dto.FndDictDetailQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndDictDetailMapper;
import com.sunten.hrms.fnd.service.FndDictDetailService;
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
@CacheConfig(cacheNames = "dictDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndDictDetailServiceImpl extends ServiceImpl<FndDictDetailDao, FndDictDetail> implements FndDictDetailService {
    private final FndDictDetailDao fndDictDetailDao;
    private final FndDictDetailMapper fndDictDetailMapper;

    public FndDictDetailServiceImpl(FndDictDetailDao fndDictDetailDao, FndDictDetailMapper fndDictDetailMapper) {
        this.fndDictDetailDao = fndDictDetailDao;
        this.fndDictDetailMapper = fndDictDetailMapper;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FndDictDetailDTO insert(FndDictDetail dictDetailNew) {
        fndDictDetailDao.insertAllColumn(dictDetailNew);
        return fndDictDetailMapper.toDto(dictDetailNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        fndDictDetailDao.deleteByKey(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(FndDictDetail dictDetailNew) {
        FndDictDetail dictDetailTemp = Optional.ofNullable(fndDictDetailDao.getByKey(dictDetailNew.getId())).orElseGet(FndDictDetail::new);
        ValidationUtil.isNull(dictDetailTemp.getId(), "DictDetail", "id", dictDetailNew.getId());
        dictDetailNew.setId(dictDetailTemp.getId());
        fndDictDetailDao.updateAllColumnByKey(dictDetailNew);
    }

    @Override
    @Cacheable(key = "#p0")
    public FndDictDetailDTO getByKey(Long id) {
        FndDictDetail dictDetail = Optional.ofNullable(fndDictDetailDao.getByKey(id)).orElseGet(FndDictDetail::new);
        ValidationUtil.isNull(dictDetail.getId(), "DictDetail", "id", id);
        return fndDictDetailMapper.toDto(dictDetail);
    }

    @Override
    @Cacheable
    public List<FndDictDetailDTO> listAll(FndDictDetailQueryCriteria criteria) {
        return fndDictDetailMapper.toDto(fndDictDetailDao.listAllByCriteria(criteria));
    }

    @Override
    @Cacheable
    public Map<String, Object> listAll(FndDictDetailQueryCriteria criteria, Pageable pageable) {
        Page<FndDictDetail> page = PageUtil.startPage(pageable);
        List<FndDictDetail> dictDetails = fndDictDetailDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndDictDetailMapper.toDto(dictDetails), page.getTotal());
    }

    @Override
    public void download(List<FndDictDetailDTO> dictDetailDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndDictDetailDTO dictDetailDTO : dictDetailDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", dictDetailDTO.getId());
            map.put("字典标签", dictDetailDTO.getLabel());
            map.put("字典值", dictDetailDTO.getValue());
            map.put("排序", dictDetailDTO.getSort());
//            map.put("字典id", dictDetailDTO.getDictId());
            map.put("createTime", dictDetailDTO.getCreateTime());
            map.put("createBy", dictDetailDTO.getCreateBy());
            map.put("updateTime", dictDetailDTO.getUpdateTime());
            map.put("updateBy", dictDetailDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
