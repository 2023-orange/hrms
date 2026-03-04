package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.pm.domain.PmSalesApprovalRelations;
import com.sunten.hrms.pm.dao.PmSalesApprovalRelationsDao;
import com.sunten.hrms.pm.service.PmSalesApprovalRelationsService;
import com.sunten.hrms.pm.dto.PmSalesApprovalRelationsDTO;
import com.sunten.hrms.pm.dto.PmSalesApprovalRelationsQueryCriteria;
import com.sunten.hrms.pm.mapper.PmSalesApprovalRelationsMapper;
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
 * 销售审批节点关系表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2022-02-17
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmSalesApprovalRelationsServiceImpl extends ServiceImpl<PmSalesApprovalRelationsDao, PmSalesApprovalRelations> implements PmSalesApprovalRelationsService {
    private final PmSalesApprovalRelationsDao pmSalesApprovalRelationsDao;
    private final PmSalesApprovalRelationsMapper pmSalesApprovalRelationsMapper;

    public PmSalesApprovalRelationsServiceImpl(PmSalesApprovalRelationsDao pmSalesApprovalRelationsDao, PmSalesApprovalRelationsMapper pmSalesApprovalRelationsMapper) {
        this.pmSalesApprovalRelationsDao = pmSalesApprovalRelationsDao;
        this.pmSalesApprovalRelationsMapper = pmSalesApprovalRelationsMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmSalesApprovalRelationsDTO insert(PmSalesApprovalRelations salesApprovalRelationsNew) {
        pmSalesApprovalRelationsDao.insertAllColumn(salesApprovalRelationsNew);
        return pmSalesApprovalRelationsMapper.toDto(salesApprovalRelationsNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmSalesApprovalRelations salesApprovalRelations = new PmSalesApprovalRelations();
        salesApprovalRelations.setId(id);
        this.delete(salesApprovalRelations);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmSalesApprovalRelations salesApprovalRelations) {
        // TODO    确认删除前是否需要做检查
        pmSalesApprovalRelationsDao.deleteByEntityKey(salesApprovalRelations);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmSalesApprovalRelations salesApprovalRelationsNew) {
        PmSalesApprovalRelations salesApprovalRelationsInDb = Optional.ofNullable(pmSalesApprovalRelationsDao.getByKey(salesApprovalRelationsNew.getId())).orElseGet(PmSalesApprovalRelations::new);
        ValidationUtil.isNull(salesApprovalRelationsInDb.getId() ,"SalesApprovalRelations", "id", salesApprovalRelationsNew.getId());
        salesApprovalRelationsNew.setId(salesApprovalRelationsInDb.getId());
        pmSalesApprovalRelationsDao.updateAllColumnByKey(salesApprovalRelationsNew);
    }

    @Override
    public PmSalesApprovalRelationsDTO getByKey(Long id) {
        PmSalesApprovalRelations salesApprovalRelations = Optional.ofNullable(pmSalesApprovalRelationsDao.getByKey(id)).orElseGet(PmSalesApprovalRelations::new);
        ValidationUtil.isNull(salesApprovalRelations.getId() ,"SalesApprovalRelations", "id", id);
        return pmSalesApprovalRelationsMapper.toDto(salesApprovalRelations);
    }

    @Override
    public List<PmSalesApprovalRelationsDTO> listAll(PmSalesApprovalRelationsQueryCriteria criteria) {
        return pmSalesApprovalRelationsMapper.toDto(pmSalesApprovalRelationsDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmSalesApprovalRelationsQueryCriteria criteria, Pageable pageable) {
        Page<PmSalesApprovalRelations> page = PageUtil.startPage(pageable);
        List<PmSalesApprovalRelations> salesApprovalRelationss = pmSalesApprovalRelationsDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmSalesApprovalRelationsMapper.toDto(salesApprovalRelationss), page.getTotal());
    }

    @Override
    public void download(List<PmSalesApprovalRelationsDTO> salesApprovalRelationsDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmSalesApprovalRelationsDTO salesApprovalRelationsDTO : salesApprovalRelationsDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("主键", salesApprovalRelationsDTO.getId());
            map.put("部门id", salesApprovalRelationsDTO.getDeptId());
            map.put("部门名称", salesApprovalRelationsDTO.getDeptName());
            map.put("所属营销部门审批人id", salesApprovalRelationsDTO.getMarketDepartmentEmpId());
            map.put("所属营销区域审批人id", salesApprovalRelationsDTO.getMarketAreaEmpId());
            map.put("enabledFlag", salesApprovalRelationsDTO.getEnabledFlag());
            map.put("createTime", salesApprovalRelationsDTO.getCreateTime());
            map.put("updateBy", salesApprovalRelationsDTO.getUpdateBy());
            map.put("createBy", salesApprovalRelationsDTO.getCreateBy());
            map.put("updateTime", salesApprovalRelationsDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
