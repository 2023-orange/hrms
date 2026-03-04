package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndDictDetailDao;
import com.sunten.hrms.fnd.domain.FndDictDetail;
import com.sunten.hrms.fnd.dto.FndDictDetailQueryCriteria;
import com.sunten.hrms.swm.dao.SwmPostSkillSalaryDao;
import com.sunten.hrms.swm.domain.SwmEntryBatchSettingHistory;
import com.sunten.hrms.swm.dao.SwmEntryBatchSettingHistoryDao;
import com.sunten.hrms.swm.domain.SwmPostSkillSalary;
import com.sunten.hrms.swm.dto.SwmPostSkillSalaryQueryCriteria;
import com.sunten.hrms.swm.service.SwmEntryBatchSettingHistoryService;
import com.sunten.hrms.swm.dto.SwmEntryBatchSettingHistoryDTO;
import com.sunten.hrms.swm.dto.SwmEntryBatchSettingHistoryQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmEntryBatchSettingHistoryMapper;
import com.sunten.hrms.swm.vo.BatchSettingVo;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.format.DateTimeFormatter;
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
 * 人员薪资条目批量设置历史表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmEntryBatchSettingHistoryServiceImpl extends ServiceImpl<SwmEntryBatchSettingHistoryDao, SwmEntryBatchSettingHistory> implements SwmEntryBatchSettingHistoryService {
    private final SwmEntryBatchSettingHistoryDao swmEntryBatchSettingHistoryDao;
    private final SwmEntryBatchSettingHistoryMapper swmEntryBatchSettingHistoryMapper;
    private final SwmPostSkillSalaryDao swmPostSkillSalaryDao;
    private final FndDictDetailDao fndDictDetailDao;

    public SwmEntryBatchSettingHistoryServiceImpl(SwmEntryBatchSettingHistoryDao swmEntryBatchSettingHistoryDao, SwmEntryBatchSettingHistoryMapper swmEntryBatchSettingHistoryMapper,
                                                  SwmPostSkillSalaryDao swmPostSkillSalaryDao, FndDictDetailDao fndDictDetailDao) {
        this.swmEntryBatchSettingHistoryDao = swmEntryBatchSettingHistoryDao;
        this.swmEntryBatchSettingHistoryMapper = swmEntryBatchSettingHistoryMapper;
        this.swmPostSkillSalaryDao = swmPostSkillSalaryDao;
        this.fndDictDetailDao = fndDictDetailDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmEntryBatchSettingHistoryDTO insert(SwmEntryBatchSettingHistory entryBatchSettingHistoryNew) {
        // 判定该所得期间的岗位技能工资是否已经生成且未关闭
        SwmPostSkillSalaryQueryCriteria swmPostSkillSalaryQueryCriteria = new SwmPostSkillSalaryQueryCriteria();
        swmPostSkillSalaryQueryCriteria.setFrozenFlag(false);
        swmPostSkillSalaryQueryCriteria.setPeriod(entryBatchSettingHistoryNew.getIncomePeriod());
        Integer count = swmPostSkillSalaryDao.countByCriteria(swmPostSkillSalaryQueryCriteria);
        if (count == 0) {
            throw new InfoCheckWarningMessException("该所得期间不符合存在且未冻结的条件, 不允许做批量操作");
        } else {
            // 插入一条批量操作记录
            SwmEntryBatchSettingHistory swmEntryBatchSettingHistory = new SwmEntryBatchSettingHistory();
            FndDictDetailQueryCriteria fndDictDetailQueryCriteria = new FndDictDetailQueryCriteria();
            fndDictDetailQueryCriteria.setValue(entryBatchSettingHistoryNew.getEntryName() + "%");
            List<FndDictDetail> fndDictDetail = fndDictDetailDao.listAllByCriteria(fndDictDetailQueryCriteria);
            swmEntryBatchSettingHistory.setEntryName(fndDictDetail.size() > 0 ? fndDictDetail.get(0).getLabel() : "无对应列");
            swmEntryBatchSettingHistory.setPrice(entryBatchSettingHistoryNew.getPrice());
            swmEntryBatchSettingHistory.setIncomePeriod(entryBatchSettingHistoryNew.getIncomePeriod());
            swmEntryBatchSettingHistoryDao.insertAllColumn(swmEntryBatchSettingHistory);
//            // 批量更新某所得期间的固定工资
//            swmPostSkillSalaryQueryCriteria.setColName(entryBatchSettingHistoryNew.getEntryName());
            swmPostSkillSalaryQueryCriteria.setPrice(entryBatchSettingHistoryNew.getPrice());
            swmPostSkillSalaryDao.updateByBatchSave(swmPostSkillSalaryQueryCriteria);
        }
        return swmEntryBatchSettingHistoryMapper.toDto(entryBatchSettingHistoryNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmEntryBatchSettingHistory entryBatchSettingHistory = new SwmEntryBatchSettingHistory();
        entryBatchSettingHistory.setId(id);
        this.delete(entryBatchSettingHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmEntryBatchSettingHistory entryBatchSettingHistory) {
        // TODO    确认删除前是否需要做检查
        swmEntryBatchSettingHistoryDao.deleteByEntityKey(entryBatchSettingHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmEntryBatchSettingHistory entryBatchSettingHistoryNew) {
        SwmEntryBatchSettingHistory entryBatchSettingHistoryInDb = Optional.ofNullable(swmEntryBatchSettingHistoryDao.getByKey(entryBatchSettingHistoryNew.getId())).orElseGet(SwmEntryBatchSettingHistory::new);
        ValidationUtil.isNull(entryBatchSettingHistoryInDb.getId() ,"EntryBatchSettingHistory", "id", entryBatchSettingHistoryNew.getId());
        entryBatchSettingHistoryNew.setId(entryBatchSettingHistoryInDb.getId());
        swmEntryBatchSettingHistoryDao.updateAllColumnByKey(entryBatchSettingHistoryNew);
    }

    @Override
    public SwmEntryBatchSettingHistoryDTO getByKey(Long id) {
        SwmEntryBatchSettingHistory entryBatchSettingHistory = Optional.ofNullable(swmEntryBatchSettingHistoryDao.getByKey(id)).orElseGet(SwmEntryBatchSettingHistory::new);
        ValidationUtil.isNull(entryBatchSettingHistory.getId() ,"EntryBatchSettingHistory", "id", id);
        return swmEntryBatchSettingHistoryMapper.toDto(entryBatchSettingHistory);
    }

    @Override
    public List<BatchSettingVo> listAll(SwmEntryBatchSettingHistoryQueryCriteria criteria) {
        List<SwmEntryBatchSettingHistory> swmEntryBatchSettingHistories = swmEntryBatchSettingHistoryDao.listAllByCriteria(criteria);
        List<BatchSettingVo> batchSettingVos = new ArrayList<>();
        for (SwmEntryBatchSettingHistory swmEntry : swmEntryBatchSettingHistories
             ) {
            BatchSettingVo batchSettingVo = new BatchSettingVo();
            batchSettingVo.setDatetime(swmEntry.getCreateTime());
            batchSettingVo.setEntryName(swmEntry.getEntryName());
            batchSettingVo.setName(null != swmEntry.getEmpName() ? swmEntry.getEmpName() : "无绑定员工");
            batchSettingVo.setPeriod(swmEntry.getIncomePeriod());
            batchSettingVo.setPrice(swmEntry.getPrice());
            batchSettingVos.add(batchSettingVo);
        }
        return batchSettingVos;
    }

    @Override
    public Map<String, Object> listAll(SwmEntryBatchSettingHistoryQueryCriteria criteria, Pageable pageable) {
        Page<SwmEntryBatchSettingHistory> page = PageUtil.startPage(pageable);
        List<SwmEntryBatchSettingHistory> entryBatchSettingHistorys = swmEntryBatchSettingHistoryDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmEntryBatchSettingHistoryMapper.toDto(entryBatchSettingHistorys), page.getTotal());
    }

    @Override
    public void download(List<BatchSettingVo> entryBatchSettingHistoryDTOS, HttpServletResponse response) throws IOException {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> list = new ArrayList<>();
        for (BatchSettingVo entryBatchSettingHistoryDTO : entryBatchSettingHistoryDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("所得期间", entryBatchSettingHistoryDTO.getPeriod());
            map.put("条目", entryBatchSettingHistoryDTO.getEntryName());
            map.put("金额", entryBatchSettingHistoryDTO.getPrice());
            map.put("创建时间", fmt.format(entryBatchSettingHistoryDTO.getDatetime()));
            map.put("创建人id", null != entryBatchSettingHistoryDTO.getEntryName() ? entryBatchSettingHistoryDTO.getEntryName() : "无绑定员工");
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
