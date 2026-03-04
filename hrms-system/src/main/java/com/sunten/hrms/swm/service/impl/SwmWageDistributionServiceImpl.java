package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.domain.SwmWageDistribution;
import com.sunten.hrms.swm.dao.SwmWageDistributionDao;
import com.sunten.hrms.swm.service.SwmWageDistributionService;
import com.sunten.hrms.swm.dto.SwmWageDistributionDTO;
import com.sunten.hrms.swm.dto.SwmWageDistributionQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmWageDistributionMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;

/**
 * <p>
 * 工资分配（工资系数）表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmWageDistributionServiceImpl extends ServiceImpl<SwmWageDistributionDao, SwmWageDistribution> implements SwmWageDistributionService {
    private final SwmWageDistributionDao swmWageDistributionDao;
    private final SwmWageDistributionMapper swmWageDistributionMapper;
    private final FndUserService fndUserService;
    private final SwmFloatingWageServiceImpl swmFloatingWageServiceImpl;
    public SwmWageDistributionServiceImpl(SwmWageDistributionDao swmWageDistributionDao, SwmWageDistributionMapper swmWageDistributionMapper
    ,FndUserService fndUserService, SwmFloatingWageServiceImpl swmFloatingWageServiceImpl) {
        this.swmWageDistributionDao = swmWageDistributionDao;
        this.swmWageDistributionMapper = swmWageDistributionMapper;
        this.fndUserService = fndUserService;
        this.swmFloatingWageServiceImpl = swmFloatingWageServiceImpl;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmWageDistributionDTO insert(SwmWageDistribution wageDistributionNew) {
        swmWageDistributionDao.insertAllColumn(wageDistributionNew);
        return swmWageDistributionMapper.toDto(wageDistributionNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmWageDistribution wageDistribution = new SwmWageDistribution();
        wageDistribution.setId(id);
        this.delete(wageDistribution);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmWageDistribution wageDistribution) {
        // TODO    确认删除前是否需要做检查
        swmWageDistributionDao.deleteByEntityKey(wageDistribution);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmWageDistribution wageDistributionNew) {
        SwmWageDistribution wageDistributionInDb = Optional.ofNullable(swmWageDistributionDao.getByKey(wageDistributionNew.getId())).orElseGet(SwmWageDistribution::new);
        ValidationUtil.isNull(wageDistributionInDb.getId() ,"WageDistribution", "id", wageDistributionNew.getId());
        wageDistributionNew.setId(wageDistributionInDb.getId());
        swmWageDistributionDao.updateAllColumnByKey(wageDistributionNew);
    }

    @Override
    public SwmWageDistributionDTO getByKey(Long id) {
        SwmWageDistribution wageDistribution = Optional.ofNullable(swmWageDistributionDao.getByKey(id)).orElseGet(SwmWageDistribution::new);
        ValidationUtil.isNull(wageDistribution.getId() ,"WageDistribution", "id", id);
        return swmWageDistributionMapper.toDto(wageDistribution);
    }

    @Override
    public List<SwmWageDistributionDTO> listAll(SwmWageDistributionQueryCriteria criteria) {
        return swmWageDistributionMapper.toDto(swmWageDistributionDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmWageDistributionQueryCriteria criteria, Pageable pageable) {
        Page<SwmWageDistribution> page = PageUtil.startPage(pageable);
        List<SwmWageDistribution> wageDistributions = swmWageDistributionDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmWageDistributionMapper.toDto(wageDistributions), page.getTotal());
    }

    @Override
    public void download(List<SwmWageDistributionDTO> wageDistributionDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmWageDistributionDTO wageDistributionDTO : wageDistributionDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("所得期间", wageDistributionDTO.getIncomePeriod());
            map.put("分配方式", wageDistributionDTO.getDistributionMethod());
            map.put("生成区分", wageDistributionDTO.getGenerationDifferentiationFlag() ? "生产" : "非生产");
            map.put("生产系数", wageDistributionDTO.getProductionFactor());
            map.put("质量系数", wageDistributionDTO.getQualityFactor());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SwmWageDistribution> generateWageDistributionByMsp(String period) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Map<String, Object> map = new HashMap<>();
        map.put("period", period);
        map.put("userId", user.getId());
        map.put("resultStr", "");
        swmWageDistributionDao.generateWageDistributionByMsp(map);
        String result = map.get("resultStr").toString();
        if (result.equals("SUCCESS")) {
            SwmWageDistributionQueryCriteria swmWageDistributionQueryCriteria = new SwmWageDistributionQueryCriteria();
            swmWageDistributionQueryCriteria.setPeriod(period);
            return swmWageDistributionDao.listAllByCriteria(swmWageDistributionQueryCriteria);
        } else {
            throw new InfoCheckWarningMessException(result);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateWageDistribution(List<SwmWageDistribution> swmWageDistributions) {
        for (SwmWageDistribution swd : swmWageDistributions
             ) {
            swmWageDistributionDao.updateAllColumnByKey(swd);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByPeriod(String period) {
        if (swmWageDistributionDao.countByPeriod(period) > 0) {
            if (!swmFloatingWageServiceImpl.checkBeforeDelete(period)) {
                throw new InfoCheckWarningMessException("该所得期间的浮动工资已经冻结， 不允许删除分配系数");
            } else {
                swmWageDistributionDao.deleteByPeriod(period);
            }
        } else {
            throw new InfoCheckWarningMessException("该所得期间尚未生成工资分配");
        }
    }
}
