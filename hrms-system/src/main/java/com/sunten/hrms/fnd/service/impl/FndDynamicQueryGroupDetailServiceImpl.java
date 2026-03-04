package com.sunten.hrms.fnd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.fnd.dao.FndDynamicQueryGroupDetailDao;
import com.sunten.hrms.fnd.domain.FndDynamicQueryGroupDetail;
import com.sunten.hrms.fnd.dto.FndDynamicQueryGroupDetailDTO;
import com.sunten.hrms.fnd.dto.FndDynamicQueryGroupDetailQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndDynamicQueryGroupDetailMapper;
import com.sunten.hrms.fnd.service.FndDynamicQueryGroupDetailService;
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
 * @since 2022-07-29
 */
@Service
@CacheConfig(cacheNames = "dynamicQueryGroupDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndDynamicQueryGroupDetailServiceImpl extends ServiceImpl<FndDynamicQueryGroupDetailDao, FndDynamicQueryGroupDetail> implements FndDynamicQueryGroupDetailService {
    private final FndDynamicQueryGroupDetailDao fndDynamicQueryGroupDetailDao;
    private final FndDynamicQueryGroupDetailMapper fndDynamicQueryGroupDetailMapper;

    public FndDynamicQueryGroupDetailServiceImpl(FndDynamicQueryGroupDetailDao fndDynamicQueryGroupDetailDao, FndDynamicQueryGroupDetailMapper fndDynamicQueryGroupDetailMapper) {
        this.fndDynamicQueryGroupDetailDao = fndDynamicQueryGroupDetailDao;
        this.fndDynamicQueryGroupDetailMapper = fndDynamicQueryGroupDetailMapper;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FndDynamicQueryGroupDetailDTO insert(FndDynamicQueryGroupDetail dynamicQueryGroupDetailNew) {
        fndDynamicQueryGroupDetailDao.insertAllColumn(dynamicQueryGroupDetailNew);
        return fndDynamicQueryGroupDetailMapper.toDto(dynamicQueryGroupDetailNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        FndDynamicQueryGroupDetail dynamicQueryGroupDetail = new FndDynamicQueryGroupDetail();
        dynamicQueryGroupDetail.setId(id);
        this.delete(dynamicQueryGroupDetail);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteByGroupId(Long groupId) {
        fndDynamicQueryGroupDetailDao.deleteByGroupId(groupId);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(FndDynamicQueryGroupDetail dynamicQueryGroupDetail) {
        // TODO    确认删除前是否需要做检查
        fndDynamicQueryGroupDetailDao.deleteByEntityKey(dynamicQueryGroupDetail);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(FndDynamicQueryGroupDetail dynamicQueryGroupDetailNew) {
        FndDynamicQueryGroupDetail dynamicQueryGroupDetailInDb = Optional.ofNullable(fndDynamicQueryGroupDetailDao.getByKey(dynamicQueryGroupDetailNew.getId())).orElseGet(FndDynamicQueryGroupDetail::new);
        ValidationUtil.isNull(dynamicQueryGroupDetailInDb.getId(), "DynamicQueryGroupDetail", "id", dynamicQueryGroupDetailNew.getId());
        dynamicQueryGroupDetailNew.setId(dynamicQueryGroupDetailInDb.getId());
        fndDynamicQueryGroupDetailDao.updateAllColumnByKey(dynamicQueryGroupDetailNew);
    }

    @Override
    @Cacheable(key = "#p0")
    public FndDynamicQueryGroupDetailDTO getByKey(Long id) {
        FndDynamicQueryGroupDetail dynamicQueryGroupDetail = Optional.ofNullable(fndDynamicQueryGroupDetailDao.getByKey(id)).orElseGet(FndDynamicQueryGroupDetail::new);
        ValidationUtil.isNull(dynamicQueryGroupDetail.getId(), "DynamicQueryGroupDetail", "id", id);
        return fndDynamicQueryGroupDetailMapper.toDto(dynamicQueryGroupDetail);
    }

    @Override
    @Cacheable
    public List<FndDynamicQueryGroupDetailDTO> listAll(FndDynamicQueryGroupDetailQueryCriteria criteria) {
        return fndDynamicQueryGroupDetailMapper.toDto(fndDynamicQueryGroupDetailDao.listAllByCriteria(criteria));
    }

    @Override
    @Cacheable
    public Map<String, Object> listAll(FndDynamicQueryGroupDetailQueryCriteria criteria, Pageable pageable) {
        Page<FndDynamicQueryGroupDetail> page = PageUtil.startPage(pageable);
        List<FndDynamicQueryGroupDetail> dynamicQueryGroupDetails = fndDynamicQueryGroupDetailDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndDynamicQueryGroupDetailMapper.toDto(dynamicQueryGroupDetails), page.getTotal());
    }

    @Override
    public void download(List<FndDynamicQueryGroupDetailDTO> dynamicQueryGroupDetailDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndDynamicQueryGroupDetailDTO dynamicQueryGroupDetailDTO : dynamicQueryGroupDetailDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", dynamicQueryGroupDetailDTO.getId());
            map.put("groupId", dynamicQueryGroupDetailDTO.getDynamicQueryGroup().getId());
            map.put("label", dynamicQueryGroupDetailDTO.getLabel());
            map.put("queryTable", dynamicQueryGroupDetailDTO.getQueryTable());
            map.put("field", dynamicQueryGroupDetailDTO.getField());
            map.put("fieldType", dynamicQueryGroupDetailDTO.getFieldType());
            map.put("fieldTypeDesc", dynamicQueryGroupDetailDTO.getFieldTypeDesc());
            map.put("jdbcType", dynamicQueryGroupDetailDTO.getJdbcType());
            map.put("operatorGroupId", dynamicQueryGroupDetailDTO.getDynamicQueryOperatorGroup().getId());
            map.put("component", dynamicQueryGroupDetailDTO.getComponent());
            map.put("componentAttributes", dynamicQueryGroupDetailDTO.getComponentAttributes());
            map.put("listType", dynamicQueryGroupDetailDTO.getListType());
            map.put("listAttributes", dynamicQueryGroupDetailDTO.getListAttributes());
            map.put("regularExpression", dynamicQueryGroupDetailDTO.getRegularExpression());
            map.put("specialSql", dynamicQueryGroupDetailDTO.getSpecialSql());
            map.put("sort", dynamicQueryGroupDetailDTO.getSort());
            map.put("enabledFlag", dynamicQueryGroupDetailDTO.getEnabledFlag());
            map.put("createTime", dynamicQueryGroupDetailDTO.getCreateTime());
            map.put("createBy", dynamicQueryGroupDetailDTO.getCreateBy());
            map.put("updateTime", dynamicQueryGroupDetailDTO.getUpdateTime());
            map.put("updateBy", dynamicQueryGroupDetailDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
