package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.swm.domain.SwmCostCenter;
import com.sunten.hrms.swm.dao.SwmCostCenterDao;
import com.sunten.hrms.swm.service.SwmCostCenterService;
import com.sunten.hrms.swm.dto.SwmCostCenterDTO;
import com.sunten.hrms.swm.dto.SwmCostCenterQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmCostCenterMapper;
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
 * 成本中心表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmCostCenterServiceImpl extends ServiceImpl<SwmCostCenterDao, SwmCostCenter> implements SwmCostCenterService {
    private final SwmCostCenterDao swmCostCenterDao;
    private final SwmCostCenterMapper swmCostCenterMapper;

    public SwmCostCenterServiceImpl(SwmCostCenterDao swmCostCenterDao, SwmCostCenterMapper swmCostCenterMapper) {
        this.swmCostCenterDao = swmCostCenterDao;
        this.swmCostCenterMapper = swmCostCenterMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmCostCenterDTO insert(SwmCostCenter costCenterNew) {
        swmCostCenterDao.insertAllColumn(costCenterNew);
        return swmCostCenterMapper.toDto(costCenterNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmCostCenter costCenter = new SwmCostCenter();
        costCenter.setId(id);
        this.delete(costCenter);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmCostCenter costCenter) {
        // TODO    确认删除前是否需要做检查
        swmCostCenterDao.deleteByEntityKey(costCenter);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmCostCenter costCenterNew) {
        SwmCostCenter costCenterInDb = Optional.ofNullable(swmCostCenterDao.getByKey(costCenterNew.getId())).orElseGet(SwmCostCenter::new);
        ValidationUtil.isNull(costCenterInDb.getId() ,"CostCenter", "id", costCenterNew.getId());
        costCenterNew.setId(costCenterInDb.getId());
        swmCostCenterDao.updateAllColumnByKey(costCenterNew);
    }

    @Override
    public SwmCostCenterDTO getByKey(Long id) {
        SwmCostCenter costCenter = Optional.ofNullable(swmCostCenterDao.getByKey(id)).orElseGet(SwmCostCenter::new);
        ValidationUtil.isNull(costCenter.getId() ,"CostCenter", "id", id);
        return swmCostCenterMapper.toDto(costCenter);
    }

    @Override
    public List<SwmCostCenterDTO> listAll(SwmCostCenterQueryCriteria criteria) {
        return swmCostCenterMapper.toDto(swmCostCenterDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmCostCenterQueryCriteria criteria, Pageable pageable) {
        Page<SwmCostCenter> page = PageUtil.startPage(pageable);
        List<SwmCostCenter> costCenters = swmCostCenterDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmCostCenterMapper.toDto(costCenters), page.getTotal());
    }

    @Override
    public void download(List<SwmCostCenterDTO> costCenterDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmCostCenterDTO costCenterDTO : costCenterDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id主键", costCenterDTO.getId());
            map.put("序号", costCenterDTO.getOrderNum());
            map.put("成本中心编号", costCenterDTO.getCostCenterNum());
            map.put("成本中心名称", costCenterDTO.getCostCenterName());
            map.put("创建时间", costCenterDTO.getCreateTime());
            map.put("创建人id", costCenterDTO.getCreateBy());
            map.put("修改时间", costCenterDTO.getUpdateTime());
            map.put("修改人id", costCenterDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
