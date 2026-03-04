package com.sunten.hrms.kpi.service.impl;

import com.sunten.hrms.kpi.dao.KpiAnnualDao;
import com.sunten.hrms.kpi.domain.KpiAssessmentIndicators;
import com.sunten.hrms.kpi.dao.KpiAssessmentIndicatorsDao;
import com.sunten.hrms.kpi.service.KpiAssessmentIndicatorsService;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsDTO;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsQueryCriteria;
import com.sunten.hrms.kpi.mapper.KpiAssessmentIndicatorsMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;

/**
 * <p>
 * KPI考核指标表 服务实现类
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-28
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KpiAssessmentIndicatorsServiceImpl extends ServiceImpl<KpiAssessmentIndicatorsDao, KpiAssessmentIndicators> implements KpiAssessmentIndicatorsService {
    private final KpiAssessmentIndicatorsDao kpiAssessmentIndicatorsDao;
    private final KpiAssessmentIndicatorsMapper kpiAssessmentIndicatorsMapper;
    private final KpiAnnualDao kpiAnnualDao;

    public KpiAssessmentIndicatorsServiceImpl(KpiAssessmentIndicatorsDao kpiAssessmentIndicatorsDao, KpiAssessmentIndicatorsMapper kpiAssessmentIndicatorsMapper, KpiAnnualDao kpiAnnualDao) {
        this.kpiAssessmentIndicatorsDao = kpiAssessmentIndicatorsDao;
        this.kpiAssessmentIndicatorsMapper = kpiAssessmentIndicatorsMapper;
        this.kpiAnnualDao = kpiAnnualDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KpiAssessmentIndicatorsDTO insert(KpiAssessmentIndicators assessmentIndicatorsNew) {
        Long newSerialNumber = this.createSerialNumberKpi(assessmentIndicatorsNew.getYear());
        assessmentIndicatorsNew.setSerialNumber(newSerialNumber);
        kpiAssessmentIndicatorsDao.insertAllColumn(assessmentIndicatorsNew);
        return kpiAssessmentIndicatorsMapper.toDto(assessmentIndicatorsNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        KpiAssessmentIndicators assessmentIndicators = new KpiAssessmentIndicators();
        assessmentIndicators.setId(id);
        this.delete(assessmentIndicators);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(KpiAssessmentIndicators assessmentIndicators) {
        // TODO    确认删除前是否需要做检查
        kpiAssessmentIndicatorsDao.deleteByEntityKey(assessmentIndicators);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(KpiAssessmentIndicators assessmentIndicatorsNew) {
        kpiAssessmentIndicatorsDao.getByKey(assessmentIndicatorsNew.getId());
        KpiAssessmentIndicators assessmentIndicatorsInDb = Optional.ofNullable(kpiAssessmentIndicatorsDao.getByKey(assessmentIndicatorsNew.getId())).orElseGet(KpiAssessmentIndicators::new);
        ValidationUtil.isNull(assessmentIndicatorsInDb.getId() ,"AssessmentIndicators", "id", assessmentIndicatorsNew.getId());
        assessmentIndicatorsNew.setId(assessmentIndicatorsInDb.getId());
        kpiAssessmentIndicatorsDao.updateAllColumnByKey(assessmentIndicatorsNew);
    }

    @Override
    public KpiAssessmentIndicatorsDTO getByKey(Long id) {
        KpiAssessmentIndicators assessmentIndicators = Optional.ofNullable(kpiAssessmentIndicatorsDao.getByKey(id)).orElseGet(KpiAssessmentIndicators::new);
        ValidationUtil.isNull(assessmentIndicators.getId() ,"AssessmentIndicators", "id", id);
        return kpiAssessmentIndicatorsMapper.toDto(assessmentIndicators);
    }

    @Override
    public List<KpiAssessmentIndicatorsDTO> listAll(KpiAssessmentIndicatorsQueryCriteria criteria) {
        return kpiAssessmentIndicatorsMapper.toDto(kpiAssessmentIndicatorsDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(KpiAssessmentIndicatorsQueryCriteria criteria, Pageable pageable) {
        Page<KpiAssessmentIndicators> page = PageUtil.startPage(pageable);
        List<KpiAssessmentIndicators> assessmentIndicatorss = kpiAssessmentIndicatorsDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(kpiAssessmentIndicatorsMapper.toDto(assessmentIndicatorss), page.getTotal());
    }

    @Override
    public List<KpiAssessmentIndicatorsDTO> listAssessedIndicators(KpiAssessmentIndicatorsQueryCriteria criteria) {
        return kpiAssessmentIndicatorsMapper.toDto(kpiAssessmentIndicatorsDao.listAssessedIndicators(criteria));
    }

    @Override
    public Map<String, Object> listAssessedIndicators(KpiAssessmentIndicatorsQueryCriteria criteria, Pageable pageable) {
        Page<KpiAssessmentIndicators> page = PageUtil.startPage(pageable);
        List<KpiAssessmentIndicators> assessmentIndicatorss = kpiAssessmentIndicatorsDao.listAssessedIndicatorsPage(page, criteria);
        return PageUtil.toPage(kpiAssessmentIndicatorsMapper.toDto(assessmentIndicatorss), page.getTotal());
    }


    @Override
    public void download(List<KpiAssessmentIndicatorsDTO> assessmentIndicatorsDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (KpiAssessmentIndicatorsDTO assessmentIndicatorsDTO : assessmentIndicatorsDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("流水号", assessmentIndicatorsDTO.getSerialNumber());
            map.put("年份", assessmentIndicatorsDTO.getYear());
            map.put("被考核组织上级部门", assessmentIndicatorsDTO.getParentDepartment());
            map.put("被考核组织", assessmentIndicatorsDTO.getAssessedDepartmentName());
            map.put("考核类别", assessmentIndicatorsDTO.getExamineType());
            map.put("目标维度", assessmentIndicatorsDTO.getTargetDimension());
            map.put("关键绩效指标", assessmentIndicatorsDTO.getKeyPerformanceIndicator());
            map.put("定义及计算方法", assessmentIndicatorsDTO.getDefine());
            map.put("权重", assessmentIndicatorsDTO.getWeight());
            if (assessmentIndicatorsDTO.getDataType().equals("百分数"))
            {
                assessmentIndicatorsDTO.setThreshold(assessmentIndicatorsDTO.getThreshold()+"%");
                assessmentIndicatorsDTO.setTargetValue(assessmentIndicatorsDTO.getTargetValue()+"%");
                assessmentIndicatorsDTO.setChallengeValue(assessmentIndicatorsDTO.getChallengeValue()+"%");
            }
            map.put("门槛值", assessmentIndicatorsDTO.getThreshold());
            map.put("目标值", assessmentIndicatorsDTO.getTargetValue());
            map.put("挑战值", assessmentIndicatorsDTO.getChallengeValue());
            map.put("考核部门", assessmentIndicatorsDTO.getExamineDepartmentName());
            map.put("每月考核数据填写人名称", assessmentIndicatorsDTO.getExamineEmployeeName());
            map.put("每月考核数据填写人工牌号", assessmentIndicatorsDTO.getExamineEmployeeWorkCard());
            map.put("数据类型", assessmentIndicatorsDTO.getDataType());
            map.put("列表显示精确度", assessmentIndicatorsDTO.getDataAccuracy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<Integer> getYearList() {
        List<Integer> periodList = new ArrayList<>();
        if (kpiAssessmentIndicatorsDao.getMaxYear() == null)
        {
            Calendar calendar = Calendar.getInstance();
            periodList.add( calendar.get(Calendar.YEAR) - 1);
            periodList.add(calendar.get(Calendar.YEAR));
            periodList.add(calendar.get(Calendar.YEAR) + 1);
        }
        else {
            periodList.add(kpiAssessmentIndicatorsDao.getMaxYear() - 1);
            periodList.add(kpiAssessmentIndicatorsDao.getMaxYear());
            periodList.add(kpiAssessmentIndicatorsDao.getMaxYear() + 1);
        }
        return periodList;
    }

    @Override
    public Long createSerialNumberKpi(String year) {
//        Integer maxYear = kpiAnnualDao.getMaxYear();
        // 获取最新年份，然后通过年份获取最大流水号
        Integer currnetSerialNumber = kpiAssessmentIndicatorsDao.getMaxSerialNumberByYear(Integer.parseInt(year));
        Map<String, Object> map = new HashMap<>();
        map.put("digit", 4);
        map.put("currnetSerialNumber", currnetSerialNumber);
        map.put("year", year);
        map.put("currnetSerialNumberOutput",' ');
        Long newSerialNumber = kpiAssessmentIndicatorsDao.createSerialNumber(map);
        return newSerialNumber;
    }

    @Override
    public List<HashMap<String, Object>> getAssessmentIndicatorsInfoByManger(String year) {
        List<HashMap<String, Object>> treeList = new ArrayList<>();
        List<HashMap<String,Object>> tempList = kpiAssessmentIndicatorsDao.getTreeListInfoByManger(year);
        Iterator<HashMap<String,Object>> iterator = tempList.iterator();
        while (iterator.hasNext()) {
            HashMap<String, Object> temp = iterator.next();
            List<HashMap<String, Object>> detailArrayHash = kpiAssessmentIndicatorsDao.getKpiAssessmentIndicators((Long)temp.get("id"), (String)temp.get("year"), "KPI");
            List<HashMap<String, Object>> superDetailArrayHash = kpiAssessmentIndicatorsDao.getSuperAssessmentIndicators((Long)temp.get("id"), (String)temp.get("year"), "重点工作");
            if (detailArrayHash.isEmpty() && superDetailArrayHash.isEmpty()) {
                iterator.remove(); // 从tempList中删除对应的temp项
            } else {
                HashMap<String, Object> tempMap = new HashMap<>(16);
                tempMap.put("baseInfo", temp);
                tempMap.put("detailArrayHash", detailArrayHash);
                tempMap.put("superDetailArrayHash", superDetailArrayHash);
                treeList.add(tempMap);
            }
        }
        return treeList;
    }

    @Override
    public List<HashMap<String, Object>> getAssessmentIndicatorsInfoByDepartmentHead(String year) {
        List<HashMap<String, Object>> treeList = new ArrayList<>();
        List<HashMap<String,Object>> tempList = kpiAssessmentIndicatorsDao.getTreeListInfo(year);
        Iterator<HashMap<String,Object>> iterator = tempList.iterator();
        while (iterator.hasNext()) {
            HashMap<String, Object> temp = iterator.next();
            List<HashMap<String, Object>> detailArrayHash = kpiAssessmentIndicatorsDao.getKpiAssessmentIndicators((Long)temp.get("id"), (String)temp.get("year"), "KPI");
            List<HashMap<String, Object>> superDetailArrayHash = kpiAssessmentIndicatorsDao.getSuperAssessmentIndicators((Long)temp.get("id"), (String)temp.get("year"), "重点工作");
            if (detailArrayHash.isEmpty() && superDetailArrayHash.isEmpty()) {
                iterator.remove(); // 从tempList中删除对应的temp项
            } else {
                HashMap<String, Object> tempMap = new HashMap<>(16);
                tempMap.put("baseInfo", temp);
                tempMap.put("detailArrayHash", detailArrayHash);
                tempMap.put("superDetailArrayHash", superDetailArrayHash);
                treeList.add(tempMap);
            }
        }
        return treeList;
    }

    @Override
    public Double getResidueWeight(Long id) {
        System.out.println(kpiAssessmentIndicatorsDao.getResidueWeight(id));
        return kpiAssessmentIndicatorsDao.getResidueWeight(id);
    }
}
