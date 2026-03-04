package com.sunten.hrms.fnd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.domain.DomainEqualsResult;
import com.sunten.hrms.fnd.dao.FndUpdateHistoryDao;
import com.sunten.hrms.fnd.domain.FndUpdateHistory;
import com.sunten.hrms.fnd.dto.FndUpdateHistoryDTO;
import com.sunten.hrms.fnd.dto.FndUpdateHistoryQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndUpdateHistoryMapper;
import com.sunten.hrms.fnd.service.FndUpdateHistoryService;
import com.sunten.hrms.utils.DomainEqualsUtil;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 历史修改表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-07-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndUpdateHistoryServiceImpl extends ServiceImpl<FndUpdateHistoryDao, FndUpdateHistory> implements FndUpdateHistoryService {
    private final FndUpdateHistoryDao fndUpdateHistoryDao;
    private final FndUpdateHistoryMapper fndUpdateHistoryMapper;

    public FndUpdateHistoryServiceImpl(FndUpdateHistoryDao fndUpdateHistoryDao, FndUpdateHistoryMapper fndUpdateHistoryMapper) {
        this.fndUpdateHistoryDao = fndUpdateHistoryDao;
        this.fndUpdateHistoryMapper = fndUpdateHistoryMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FndUpdateHistoryDTO insert(FndUpdateHistory updateHistoryNew) {
        fndUpdateHistoryDao.insertAllColumn(updateHistoryNew);
        return fndUpdateHistoryMapper.toDto(updateHistoryNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        FndUpdateHistory updateHistory = new FndUpdateHistory();
        updateHistory.setId(id);
        this.delete(updateHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(FndUpdateHistory updateHistory) {
        // TODO    确认删除前是否需要做检查
        fndUpdateHistoryDao.deleteByEntityKey(updateHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FndUpdateHistory updateHistoryNew) {
        FndUpdateHistory updateHistoryInDb = Optional.ofNullable(fndUpdateHistoryDao.getByKey(updateHistoryNew.getId())).orElseGet(FndUpdateHistory::new);
        ValidationUtil.isNull(updateHistoryInDb.getId(), "UpdateHistory", "id", updateHistoryNew.getId());
        updateHistoryNew.setId(updateHistoryInDb.getId());
        fndUpdateHistoryDao.updateAllColumnByKey(updateHistoryNew);
    }

    @Override
    public FndUpdateHistoryDTO getByKey(Long id) {
        FndUpdateHistory updateHistory = Optional.ofNullable(fndUpdateHistoryDao.getByKey(id)).orElseGet(FndUpdateHistory::new);
        ValidationUtil.isNull(updateHistory.getId(), "UpdateHistory", "id", id);
        return fndUpdateHistoryMapper.toDto(updateHistory);
    }

    @Override
    public List<FndUpdateHistoryDTO> listAll(FndUpdateHistoryQueryCriteria criteria) {
        return fndUpdateHistoryMapper.toDto(fndUpdateHistoryDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(FndUpdateHistoryQueryCriteria criteria, Pageable pageable) {
        Page<FndUpdateHistory> page = PageUtil.startPage(pageable);
        List<FndUpdateHistory> updateHistorys = fndUpdateHistoryDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndUpdateHistoryMapper.toDto(updateHistorys), page.getTotal());
    }

    @Override
    public void download(List<FndUpdateHistoryDTO> updateHistoryDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndUpdateHistoryDTO updateHistoryDTO : updateHistoryDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", updateHistoryDTO.getId());
            map.put("表名", updateHistoryDTO.getTableName());
            map.put("列名", updateHistoryDTO.getColumnName());
            map.put("表id", updateHistoryDTO.getTableId());
            map.put("原值", updateHistoryDTO.getOldValue());
            map.put("修改值", updateHistoryDTO.getNewValue());
            map.put("创建人id", updateHistoryDTO.getCreateBy());
            map.put("创建时间", updateHistoryDTO.getCreateTime());
            map.put("修改人id", updateHistoryDTO.getUpdateBy());
            map.put("修改时间", updateHistoryDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertDomainEqualsResultList(String tableName, String columnS, Long id, Object domainNew, Object domainOld) {
        List<DomainEqualsResult> domainEqualsResultList = DomainEqualsUtil.domainEquals(columnS, domainNew, domainOld);
        for (DomainEqualsResult domainEqualsResult : domainEqualsResultList) {
            FndUpdateHistory updateHistory = new FndUpdateHistory();
            updateHistory.setTableName(tableName);
            updateHistory.setColumnName(domainEqualsResult.getCloumnName());
            updateHistory.setTableId(id);
            updateHistory.setNewValue(domainEqualsResult.getValueOne());
            updateHistory.setOldValue(domainEqualsResult.getValueTwo());
            this.insert(updateHistory);
        }
    }
}
