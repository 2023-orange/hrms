package com.sunten.hrms.td.service.impl;

import com.sunten.hrms.fnd.dao.FndUserDao;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.td.domain.TdPlanChangeHistory;
import com.sunten.hrms.td.dao.TdPlanChangeHistoryDao;
import com.sunten.hrms.td.service.TdPlanChangeHistoryService;
import com.sunten.hrms.td.dto.TdPlanChangeHistoryDTO;
import com.sunten.hrms.td.dto.TdPlanChangeHistoryQueryCriteria;
import com.sunten.hrms.td.mapper.TdPlanChangeHistoryMapper;
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
 * 培训计划变更历史 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-06-16
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdPlanChangeHistoryServiceImpl extends ServiceImpl<TdPlanChangeHistoryDao, TdPlanChangeHistory> implements TdPlanChangeHistoryService {
    private final TdPlanChangeHistoryDao tdPlanChangeHistoryDao;
    private final TdPlanChangeHistoryMapper tdPlanChangeHistoryMapper;
    private final FndUserDao fndUserDao;

    public TdPlanChangeHistoryServiceImpl(TdPlanChangeHistoryDao tdPlanChangeHistoryDao, TdPlanChangeHistoryMapper tdPlanChangeHistoryMapper,
                                          FndUserDao fndUserDao) {
        this.tdPlanChangeHistoryDao = tdPlanChangeHistoryDao;
        this.tdPlanChangeHistoryMapper = tdPlanChangeHistoryMapper;
        this.fndUserDao = fndUserDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdPlanChangeHistoryDTO insert(TdPlanChangeHistory planChangeHistoryNew) {
        tdPlanChangeHistoryDao.insertAllColumn(planChangeHistoryNew);
        return tdPlanChangeHistoryMapper.toDto(planChangeHistoryNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdPlanChangeHistory planChangeHistory = new TdPlanChangeHistory();
        planChangeHistory.setId(id);
        this.delete(planChangeHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdPlanChangeHistory planChangeHistory) {
        // TODO    确认删除前是否需要做检查
        tdPlanChangeHistoryDao.deleteByEntityKey(planChangeHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdPlanChangeHistory planChangeHistoryNew) {
        TdPlanChangeHistory planChangeHistoryInDb = Optional.ofNullable(tdPlanChangeHistoryDao.getByKey(planChangeHistoryNew.getId())).orElseGet(TdPlanChangeHistory::new);
        ValidationUtil.isNull(planChangeHistoryInDb.getId() ,"PlanChangeHistory", "id", planChangeHistoryNew.getId());
        planChangeHistoryNew.setId(planChangeHistoryInDb.getId());
        tdPlanChangeHistoryDao.updateAllColumnByKey(planChangeHistoryNew);
    }

    @Override
    public TdPlanChangeHistoryDTO getByKey(Long id) {
        TdPlanChangeHistory planChangeHistory = Optional.ofNullable(tdPlanChangeHistoryDao.getByKey(id)).orElseGet(TdPlanChangeHistory::new);
        ValidationUtil.isNull(planChangeHistory.getId() ,"PlanChangeHistory", "id", id);
        return tdPlanChangeHistoryMapper.toDto(planChangeHistory);
    }

    @Override
    public List<TdPlanChangeHistoryDTO> listAll(TdPlanChangeHistoryQueryCriteria criteria) {
        return tdPlanChangeHistoryMapper.toDto(tdPlanChangeHistoryDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdPlanChangeHistoryQueryCriteria criteria, Pageable pageable) {
        Page<TdPlanChangeHistory> page = PageUtil.startPage(pageable);
        List<TdPlanChangeHistory> planChangeHistorys = tdPlanChangeHistoryDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdPlanChangeHistoryMapper.toDto(planChangeHistorys), page.getTotal());
    }

    @Override
    public void download(List<TdPlanChangeHistoryDTO> planChangeHistoryDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdPlanChangeHistoryDTO planChangeHistoryDTO : planChangeHistoryDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("父id", planChangeHistoryDTO.getParentId());
            map.put("变更类别(change、cancel、implement)", planChangeHistoryDTO.getChangeType());
            map.put("拟更改时间段", planChangeHistoryDTO.getChangePlanDate());
            map.put("变更原因", planChangeHistoryDTO.getChangeDescribe());
            map.put("申请单号", planChangeHistoryDTO.getOaOrder());
            map.put("id", planChangeHistoryDTO.getId());
            map.put("创建时间", planChangeHistoryDTO.getCreateTime());
            map.put("创建人ID", planChangeHistoryDTO.getCreateBy());
            map.put("修改时间", planChangeHistoryDTO.getUpdateTime());
            map.put("修改人ID", planChangeHistoryDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassOrNotPass(TdPlanChangeHistory tdPlanChangeHistory) {
        // 根据工号获取userId
//        List<FndUser> fndUsers = fndUserDao.getByUsername("%" + tdPlanChangeHistory.getUpdateStr() + "%");
        tdPlanChangeHistoryDao.updatePassOrNotPass(tdPlanChangeHistory);
    }
}
