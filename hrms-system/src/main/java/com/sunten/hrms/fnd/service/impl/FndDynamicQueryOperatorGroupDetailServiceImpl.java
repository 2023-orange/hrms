package com.sunten.hrms.fnd.service.impl;

import com.sunten.hrms.fnd.domain.FndDynamicQueryOperatorGroupDetail;
import com.sunten.hrms.fnd.dao.FndDynamicQueryOperatorGroupDetailDao;
import com.sunten.hrms.fnd.service.FndDynamicQueryOperatorGroupDetailService;
import com.sunten.hrms.fnd.dto.FndDynamicQueryOperatorGroupDetailDTO;
import com.sunten.hrms.fnd.dto.FndDynamicQueryOperatorGroupDetailQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndDynamicQueryOperatorGroupDetailMapper;
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
 * @since 2022-07-26
 */
@Service
@CacheConfig(cacheNames = "dynamicQueryOperatorGroupDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndDynamicQueryOperatorGroupDetailServiceImpl extends ServiceImpl<FndDynamicQueryOperatorGroupDetailDao, FndDynamicQueryOperatorGroupDetail> implements FndDynamicQueryOperatorGroupDetailService {
    private final FndDynamicQueryOperatorGroupDetailDao fndDynamicQueryOperatorGroupDetailDao;
    private final FndDynamicQueryOperatorGroupDetailMapper fndDynamicQueryOperatorGroupDetailMapper;

    public FndDynamicQueryOperatorGroupDetailServiceImpl(FndDynamicQueryOperatorGroupDetailDao fndDynamicQueryOperatorGroupDetailDao, FndDynamicQueryOperatorGroupDetailMapper fndDynamicQueryOperatorGroupDetailMapper) {
        this.fndDynamicQueryOperatorGroupDetailDao = fndDynamicQueryOperatorGroupDetailDao;
        this.fndDynamicQueryOperatorGroupDetailMapper = fndDynamicQueryOperatorGroupDetailMapper;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FndDynamicQueryOperatorGroupDetailDTO insert(FndDynamicQueryOperatorGroupDetail dynamicQueryOperatorGroupDetailNew) {
        fndDynamicQueryOperatorGroupDetailDao.insertAllColumn(dynamicQueryOperatorGroupDetailNew);
        return fndDynamicQueryOperatorGroupDetailMapper.toDto(dynamicQueryOperatorGroupDetailNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        FndDynamicQueryOperatorGroupDetail dynamicQueryOperatorGroupDetail = new FndDynamicQueryOperatorGroupDetail();
        dynamicQueryOperatorGroupDetail.setId(id);
        this.delete(dynamicQueryOperatorGroupDetail);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(FndDynamicQueryOperatorGroupDetail dynamicQueryOperatorGroupDetail) {
        // TODO    确认删除前是否需要做检查
        fndDynamicQueryOperatorGroupDetailDao.deleteByEntityKey(dynamicQueryOperatorGroupDetail);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(FndDynamicQueryOperatorGroupDetail dynamicQueryOperatorGroupDetailNew) {
        FndDynamicQueryOperatorGroupDetail dynamicQueryOperatorGroupDetailInDb = Optional.ofNullable(fndDynamicQueryOperatorGroupDetailDao.getByKey(dynamicQueryOperatorGroupDetailNew.getId())).orElseGet(FndDynamicQueryOperatorGroupDetail::new);
        ValidationUtil.isNull(dynamicQueryOperatorGroupDetailInDb.getId() ,"DynamicQueryOperatorGroupDetail", "id", dynamicQueryOperatorGroupDetailNew.getId());
        dynamicQueryOperatorGroupDetailNew.setId(dynamicQueryOperatorGroupDetailInDb.getId());
        fndDynamicQueryOperatorGroupDetailDao.updateAllColumnByKey(dynamicQueryOperatorGroupDetailNew);
    }

    @Override
    @Cacheable(key = "#p0")
    public FndDynamicQueryOperatorGroupDetailDTO getByKey(Long id) {
        FndDynamicQueryOperatorGroupDetail dynamicQueryOperatorGroupDetail = Optional.ofNullable(fndDynamicQueryOperatorGroupDetailDao.getByKey(id)).orElseGet(FndDynamicQueryOperatorGroupDetail::new);
        ValidationUtil.isNull(dynamicQueryOperatorGroupDetail.getId() ,"DynamicQueryOperatorGroupDetail", "id", id);
        return fndDynamicQueryOperatorGroupDetailMapper.toDto(dynamicQueryOperatorGroupDetail);
    }

    @Override
    @Cacheable
    public List<FndDynamicQueryOperatorGroupDetailDTO> listAll(FndDynamicQueryOperatorGroupDetailQueryCriteria criteria) {
        return fndDynamicQueryOperatorGroupDetailMapper.toDto(fndDynamicQueryOperatorGroupDetailDao.listAllByCriteria(criteria));
    }

    @Override
    @Cacheable
    public Map<String, Object> listAll(FndDynamicQueryOperatorGroupDetailQueryCriteria criteria, Pageable pageable) {
        Page<FndDynamicQueryOperatorGroupDetail> page = PageUtil.startPage(pageable);
        List<FndDynamicQueryOperatorGroupDetail> dynamicQueryOperatorGroupDetails = fndDynamicQueryOperatorGroupDetailDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndDynamicQueryOperatorGroupDetailMapper.toDto(dynamicQueryOperatorGroupDetails), page.getTotal());
    }

    @Override
    public void download(List<FndDynamicQueryOperatorGroupDetailDTO> dynamicQueryOperatorGroupDetailDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndDynamicQueryOperatorGroupDetailDTO dynamicQueryOperatorGroupDetailDTO : dynamicQueryOperatorGroupDetailDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", dynamicQueryOperatorGroupDetailDTO.getId());
//            map.put("operatorGroupId", dynamicQueryOperatorGroupDetailDTO.getOperatorGroupId());
            map.put("label", dynamicQueryOperatorGroupDetailDTO.getLabel());
            map.put("value", dynamicQueryOperatorGroupDetailDTO.getValue());
            map.put("sort", dynamicQueryOperatorGroupDetailDTO.getSort());
            map.put("enabledFlag", dynamicQueryOperatorGroupDetailDTO.getEnabledFlag());
            map.put("createTime", dynamicQueryOperatorGroupDetailDTO.getCreateTime());
            map.put("createBy", dynamicQueryOperatorGroupDetailDTO.getCreateBy());
            map.put("updateTime", dynamicQueryOperatorGroupDetailDTO.getUpdateTime());
            map.put("updateBy", dynamicQueryOperatorGroupDetailDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
