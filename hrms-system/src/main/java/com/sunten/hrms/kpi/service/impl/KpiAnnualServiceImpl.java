package com.sunten.hrms.kpi.service.impl;

import com.sunten.hrms.kpi.dao.KpiDepartmentTreeDao;
import com.sunten.hrms.kpi.domain.KpiAnnual;
import com.sunten.hrms.kpi.dao.KpiAnnualDao;
import com.sunten.hrms.kpi.domain.KpiDepartmentTree;
import com.sunten.hrms.kpi.service.KpiAnnualService;
import com.sunten.hrms.kpi.dto.KpiAnnualDTO;
import com.sunten.hrms.kpi.dto.KpiAnnualQueryCriteria;
import com.sunten.hrms.kpi.mapper.KpiAnnualMapper;
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
 * KPI考核年度概况 服务实现类
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-27
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KpiAnnualServiceImpl extends ServiceImpl<KpiAnnualDao, KpiAnnual> implements KpiAnnualService {
    private final KpiAnnualDao kpiAnnualDao;
    private final KpiAnnualMapper kpiAnnualMapper;
    private final KpiDepartmentTreeDao kpiDepartmentTreeDao;

    public KpiAnnualServiceImpl(KpiAnnualDao kpiAnnualDao, KpiAnnualMapper kpiAnnualMapper, KpiDepartmentTreeDao kpiDepartmentTreeDao) {
        this.kpiAnnualDao = kpiAnnualDao;
        this.kpiAnnualMapper = kpiAnnualMapper;
        this.kpiDepartmentTreeDao = kpiDepartmentTreeDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KpiAnnualDTO insert(KpiAnnual annualNew) {
        Integer year = kpiAnnualDao.getMaxYear();
        if (year == null)
        {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
        }
        else {
            year = year + 1;
        }
        annualNew.setYear(year.toString());
        annualNew.setExamineStatus("数据初始化");
        kpiAnnualDao.insertAllColumn(annualNew);
        KpiAnnualQueryCriteria annualNewQuery = new KpiAnnualQueryCriteria();
        annualNewQuery.setYear(year.toString());
        List<KpiAnnualDTO> annualDTOS = this.listAll(annualNewQuery);
        Long annualId = 0L;
        for (KpiAnnualDTO annualDTOES: annualDTOS)
        {
            annualId = annualDTOES.getId();
        }
        KpiDepartmentTree departmentTree = new KpiDepartmentTree();
        departmentTree.setYear(year.toString());
        departmentTree.setKpiAnnualId(annualId);
        kpiDepartmentTreeDao.insertTreeFromDept(departmentTree);
        return kpiAnnualMapper.toDto(annualNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        KpiAnnual annual = new KpiAnnual();
        annual.setId(id);
        this.delete(annual);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(KpiAnnual annual) {
        // TODO    确认删除前是否需要做检查
        kpiAnnualDao.deleteByEntityKey(annual);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(KpiAnnual annualNew) {
        KpiAnnual annualInDb = Optional.ofNullable(kpiAnnualDao.getByKey(annualNew.getId())).orElseGet(KpiAnnual::new);
        ValidationUtil.isNull(annualInDb.getId() ,"Annual", "id", annualNew.getId());
        annualNew.setId(annualInDb.getId());
        kpiAnnualDao.updateAllColumnByKey(annualNew);
    }

    @Override
    public KpiAnnualDTO getByKey(Long id) {
        KpiAnnual annual = Optional.ofNullable(kpiAnnualDao.getByKey(id)).orElseGet(KpiAnnual::new);
        ValidationUtil.isNull(annual.getId() ,"Annual", "id", id);
        return kpiAnnualMapper.toDto(annual);
    }

    @Override
    public List<KpiAnnualDTO> listAll(KpiAnnualQueryCriteria criteria) {
        System.out.println(criteria);
        return kpiAnnualMapper.toDto(kpiAnnualDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(KpiAnnualQueryCriteria criteria, Pageable pageable) {
        Page<KpiAnnual> page = PageUtil.startPage(pageable);
        List<KpiAnnual> annuals = kpiAnnualDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(kpiAnnualMapper.toDto(annuals), page.getTotal());
    }

    @Override
    public void download(List<KpiAnnualDTO> annualDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (KpiAnnualDTO annualDTO : annualDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("主键", annualDTO.getId());
            map.put("年份", annualDTO.getYear());
            map.put("考核状态", annualDTO.getExamineStatus());
            map.put("弹性域1", annualDTO.getAttribute1());
            map.put("弹性域2", annualDTO.getAttribute2());
            map.put("弹性域3", annualDTO.getAttribute3());
            map.put("弹性域4", annualDTO.getAttribute4());
            map.put("弹性域5", annualDTO.getAttribute5());
            map.put("创建时间", annualDTO.getCreateTime());
            map.put("创建人", annualDTO.getCreateBy());
            map.put("更新时间", annualDTO.getUpdateTime());
            map.put("更新人", annualDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<Integer> getYearList() {
        List<Integer> periodList = new ArrayList<>();
        if (kpiAnnualDao.getMaxYear() == null)
        {
            Calendar calendar = Calendar.getInstance();
            periodList.add( calendar.get(Calendar.YEAR) - 1);
            periodList.add(calendar.get(Calendar.YEAR));
            periodList.add(calendar.get(Calendar.YEAR) + 1);
        }
        else {
            periodList.add(kpiAnnualDao.getMaxYear() - 1);
            periodList.add(kpiAnnualDao.getMaxYear());
            periodList.add(kpiAnnualDao.getMaxYear() + 1);
        }
        return periodList;
    }


}
