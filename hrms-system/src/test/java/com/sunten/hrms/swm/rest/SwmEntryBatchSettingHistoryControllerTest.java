package com.sunten.hrms.swm.rest;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndDictDetailDao;
import com.sunten.hrms.fnd.domain.FndDictDetail;
import com.sunten.hrms.fnd.dto.FndDictDetailQueryCriteria;
import com.sunten.hrms.swm.dao.SwmEntryBatchSettingHistoryDao;
import com.sunten.hrms.swm.dao.SwmPostSkillSalaryDao;
import com.sunten.hrms.swm.domain.SwmEntryBatchSettingHistory;
import com.sunten.hrms.swm.dto.SwmPostSkillSalaryQueryCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SwmEntryBatchSettingHistoryControllerTest {
    @Autowired
    SwmPostSkillSalaryDao swmPostSkillSalaryDao;
    @Autowired
    FndDictDetailDao fndDictDetailDao;
    @Autowired
    SwmEntryBatchSettingHistoryDao swmEntryBatchSettingHistoryDao;
    @Test
    public void create() {
        SwmEntryBatchSettingHistory entryBatchSettingHistoryNew = new SwmEntryBatchSettingHistory();
        entryBatchSettingHistoryNew.setIncomePeriod("2020.11");
        entryBatchSettingHistoryNew.setEntryName("1111");
        entryBatchSettingHistoryNew.setPrice(new BigDecimal("111"));
        // 判定该所得期间的岗位技能工资是否已经生成且未关闭
        SwmPostSkillSalaryQueryCriteria swmPostSkillSalaryQueryCriteria = new SwmPostSkillSalaryQueryCriteria();
        swmPostSkillSalaryQueryCriteria.setFrozenFlag(false);
        swmPostSkillSalaryQueryCriteria.setPeriod(entryBatchSettingHistoryNew.getIncomePeriod());
        Integer count = swmPostSkillSalaryDao.countByCriteria(swmPostSkillSalaryQueryCriteria);
        if (count == 0) {
            throw new InfoCheckWarningMessException("不存在该所得期间且未冻结的岗位技能工资, 不允许做批量操作");
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
    }
}
