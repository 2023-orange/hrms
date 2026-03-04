package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.dao.SwmFloatingWageDao;
import com.sunten.hrms.swm.dao.SwmMonthlyQuarterlyAssessmentDao;
import com.sunten.hrms.swm.domain.SwmQuarterlyAssessment;
import com.sunten.hrms.swm.dao.SwmQuarterlyAssessmentDao;
import com.sunten.hrms.swm.service.SwmMonthlyQuarterlyAssessmentService;
import com.sunten.hrms.swm.service.SwmQuarterlyAssessmentService;
import com.sunten.hrms.swm.dto.SwmQuarterlyAssessmentDTO;
import com.sunten.hrms.swm.dto.SwmQuarterlyAssessmentQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmQuarterlyAssessmentMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;

/**
 * <p>
 * 季度考核表（一个季度生成一条，主要用作季度考核查询） 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmQuarterlyAssessmentServiceImpl extends ServiceImpl<SwmQuarterlyAssessmentDao, SwmQuarterlyAssessment> implements SwmQuarterlyAssessmentService {
    private final SwmQuarterlyAssessmentDao swmQuarterlyAssessmentDao;
    private final SwmQuarterlyAssessmentMapper swmQuarterlyAssessmentMapper;
    private final SwmMonthlyQuarterlyAssessmentService swmMonthlyQuarterlyAssessmentService;
    private final FndUserService fndUserService;
    private final SwmFloatingWageServiceImpl swmFloatingWageServiceImpl;
    private final SwmFloatingWageDao swmFloatingWageDao;

    public SwmQuarterlyAssessmentServiceImpl(SwmQuarterlyAssessmentDao swmQuarterlyAssessmentDao, SwmQuarterlyAssessmentMapper swmQuarterlyAssessmentMapper,
                                             FndUserService fndUserService, SwmMonthlyQuarterlyAssessmentService swmMonthlyQuarterlyAssessmentService,
                                             SwmFloatingWageServiceImpl swmFloatingWageServiceImpl,SwmFloatingWageDao swmFloatingWageDao) {
        this.swmQuarterlyAssessmentDao = swmQuarterlyAssessmentDao;
        this.swmQuarterlyAssessmentMapper = swmQuarterlyAssessmentMapper;
        this.fndUserService = fndUserService;
        this.swmMonthlyQuarterlyAssessmentService = swmMonthlyQuarterlyAssessmentService;
        this.swmFloatingWageServiceImpl = swmFloatingWageServiceImpl;
        this.swmFloatingWageDao = swmFloatingWageDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmQuarterlyAssessmentDTO insert(SwmQuarterlyAssessment quarterlyAssessmentNew) {
        swmQuarterlyAssessmentDao.insertAllColumn(quarterlyAssessmentNew);
        return swmQuarterlyAssessmentMapper.toDto(quarterlyAssessmentNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmQuarterlyAssessment quarterlyAssessment = new SwmQuarterlyAssessment();
        quarterlyAssessment.setId(id);
        this.delete(quarterlyAssessment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmQuarterlyAssessment quarterlyAssessment) {
        // TODO    确认删除前是否需要做检查
        swmQuarterlyAssessmentDao.deleteByEntityKey(quarterlyAssessment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmQuarterlyAssessment quarterlyAssessmentNew) {
        //TODO 检查浮动工资是否已经生成
        SwmQuarterlyAssessment quarterlyAssessmentInDb = Optional.ofNullable(swmQuarterlyAssessmentDao.getByKey(quarterlyAssessmentNew.getId())).orElseGet(SwmQuarterlyAssessment::new);
        ValidationUtil.isNull(quarterlyAssessmentInDb.getId() ,"QuarterlyAssessment", "id", quarterlyAssessmentNew.getId());
        quarterlyAssessmentNew.setId(quarterlyAssessmentInDb.getId());
        String target = quarterlyAssessmentInDb.getAssessmentQuarter();
        List<String> targetList = this.getPeriodListForFloat(target);
        if (swmFloatingWageDao.checkIsFlozenWithPeriodList(targetList) > 0) {
            throw new InfoCheckWarningMessException("此人该季度中已存在某月浮动工资被冻结，不允许修改季度考核等级");
        } else {
            swmQuarterlyAssessmentDao.updateAllColumnByKey(quarterlyAssessmentNew);
        }
    }

    @Override
    public SwmQuarterlyAssessmentDTO getByKey(Long id) {
        SwmQuarterlyAssessment quarterlyAssessment = Optional.ofNullable(swmQuarterlyAssessmentDao.getByKey(id)).orElseGet(SwmQuarterlyAssessment::new);
        ValidationUtil.isNull(quarterlyAssessment.getId() ,"QuarterlyAssessment", "id", id);
        return swmQuarterlyAssessmentMapper.toDto(quarterlyAssessment);
    }

    @Override
    public List<SwmQuarterlyAssessmentDTO> listAll(SwmQuarterlyAssessmentQueryCriteria criteria) {
        return swmQuarterlyAssessmentMapper.toDto(swmQuarterlyAssessmentDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmQuarterlyAssessmentQueryCriteria criteria, Pageable pageable) {
        Page<SwmQuarterlyAssessment> page = PageUtil.startPage(pageable);
        List<SwmQuarterlyAssessment> quarterlyAssessments = swmQuarterlyAssessmentDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmQuarterlyAssessmentMapper.toDto(quarterlyAssessments), page.getTotal());
    }

    @Override
    public void download(List<SwmQuarterlyAssessmentDTO> quarterlyAssessmentDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmQuarterlyAssessmentDTO quarterlyAssessmentDTO : quarterlyAssessmentDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("考核季度", quarterlyAssessmentDTO.getAssessmentQuarter());
            map.put("工牌号", quarterlyAssessmentDTO.getWorkCard());
            map.put("姓名", quarterlyAssessmentDTO.getName());
            map.put("部门", quarterlyAssessmentDTO.getDepartment());
            map.put("科室", quarterlyAssessmentDTO.getAdministrativeOffice());
            map.put("考核系数", quarterlyAssessmentDTO.getAssessmentNum());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<String> getQuarterPeriodList(String topPeriod) {
        List<String> periodList = new ArrayList<>();
        if (null == topPeriod) {
            topPeriod = swmQuarterlyAssessmentDao.getTopQuarterPeriod();
        }
        LocalDate now = LocalDate.now();
        String addTopPeriod;
        String subtractPeriod;
        if (null == topPeriod) {
            Integer quarter = now.getMonthValue() >= 10 ? 3 : now.getMonthValue() >= 7 ? 2 :
                    now.getMonthValue() >= 4 ? 1 : 4 ;
            topPeriod = quarter == 4 ? now.getYear() - 1 + ".04" : now.getYear() + ".0" + quarter;
            if (quarter == 4) {
                addTopPeriod = now.getYear() + ".01";
                subtractPeriod = now.getYear() - 1 + ".03";
            } else if (quarter == 1) {
                addTopPeriod = now.getYear() + ".02";
                subtractPeriod = now.getYear() - 1 + ".04";
            }
            else {
                addTopPeriod = now.getYear() + ".0" + (quarter + 1);
                subtractPeriod = now.getYear() + ".0" + (quarter - 1);
            }
        } else {
            String[] s = topPeriod.split("\\.");

            subtractPeriod = s[1].equals("01") ? Integer.parseInt(s[0]) - 1  + ".04" : Integer.parseInt(s[0])  + ".0" + (Integer.parseInt(s[1]) - 1 );
            addTopPeriod = s[1].equals("04") ? Integer.parseInt(s[0])  + 1 + ".01" : Integer.parseInt(s[0])  + ".0" + (Integer.parseInt(s[1]) + 1);

        }
        System.out.println("subtractPeriod=============="+subtractPeriod);
        System.out.println("topPeriod=============="+topPeriod);
        System.out.println("addTopPeriod=============="+addTopPeriod);
        periodList.add(subtractPeriod);
        periodList.add(topPeriod);
        periodList.add(addTopPeriod);
        return periodList;
    }

    private List<String> getPeriodListForFloat(String period) {
        String addTopPeriod;
        String subtractPeriod;
        String topPeriod;
        List<String> periodList = new ArrayList<>();
        String[] s = period.split("\\.");
        if (s[1].equals("01")) {
            subtractPeriod = s[0] + ".04";
            topPeriod = s[0] + ".05";
            addTopPeriod = s[0] + ".06";
        } else if (s[1].equals("04")) {
            subtractPeriod = Integer.parseInt(s[0])  + 1 + ".01";
            topPeriod = Integer.parseInt(s[0])  + 1 + ".02";
            addTopPeriod = Integer.parseInt(s[0])  + 1 + ".03";
        } else if (s[1].equals("02")) {
            subtractPeriod = s[0] + ".07";
            topPeriod = s[0] + ".08";
            addTopPeriod = s[0] + ".09";
        } else {
            subtractPeriod = s[0] + ".10";
            topPeriod = s[0] + ".11";
            addTopPeriod = s[0] + ".12";
        }
        periodList.add(addTopPeriod);
        periodList.add(subtractPeriod);
        periodList.add(topPeriod);
        return periodList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SwmQuarterlyAssessment> createQuarterlyAssessment(String period) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Map<String, Object> map = new HashMap<>();
        map.put("period", period);
        map.put("userId", user.getId());
        map.put("resultStr", "");
        swmQuarterlyAssessmentDao.createQuarterlyAssessment(map);
        String str = map.get("resultStr").toString();
        if (str.equals("SUCCESS")) {
            SwmQuarterlyAssessmentQueryCriteria swmQuarterlyAssessmentQueryCriteria = new SwmQuarterlyAssessmentQueryCriteria();
            swmQuarterlyAssessmentQueryCriteria.setPeriod(period);
            return swmQuarterlyAssessmentDao.listAllByCriteria(swmQuarterlyAssessmentQueryCriteria);
        } else {
            throw new InfoCheckWarningMessException(str);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeQuarterlyByPeriod(String period) {
        //TODO 检查浮动工资是否已经生成
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        if (swmQuarterlyAssessmentDao.countByQuarter(period) > 0) {
            // 删除子
            List<String> periods = this.getPeriodByQuarter(period);

            for (String temps : periods
                 ) {
                if (!swmFloatingWageServiceImpl.checkBeforeDelete(temps)) {
                    throw new InfoCheckWarningMessException("该所得期间的浮动工资已冻结， 不允许删除季度考核");
                }
            }
            // 删除父
            SwmQuarterlyAssessment swmQuarterlyAssessment = new SwmQuarterlyAssessment();
            swmQuarterlyAssessment.setAssessmentQuarter(period);
            swmQuarterlyAssessment.setUpdateBy(user.getId());
            swmQuarterlyAssessmentDao.deleteQuarterByPeriod(swmQuarterlyAssessment);
            for (String s : periods
            ) {
                // 删除子
                swmMonthlyQuarterlyAssessmentService.removeMonthlyByPeriodWithNoCheck(s);
            }
        } else {
            throw new InfoCheckWarningMessException("该所得期间尚未生成季度考核");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateQuarterAssessmentLevel(List<SwmQuarterlyAssessment> swmQuarterlyAssessments) {
        // 需要先检查该考核季度下分的月度是否已有某月生成了工资

        String target = swmQuarterlyAssessments.get(0).getAssessmentQuarter();
        List<String> targetList = this.getPeriodByQuarter(target);
//        if (swmFloatingWageDao.checkIsFlozenWithPeriodList(targetList) > 0) {
//            throw new InfoCheckWarningMessException("该季度中已存在某月浮动工资被冻结，不允许修改季度考核等级");
//        } else {
        LocalDateTime now = LocalDateTime.now();
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        for (SwmQuarterlyAssessment  swmQa : swmQuarterlyAssessments
             ) {
            swmQa.setSubmitter(user.getEmployee().getName());
            swmQa.setSubmitTime(now);
            swmQuarterlyAssessmentDao.updateQuarterAssessmentLevel(swmQa);
            // 更新对应的sub月份
        }
        swmMonthlyQuarterlyAssessmentService.updateQuarterSubMonthAssessmentLevel(targetList, target);
//        }
    }

    public List<String> getPeriodByQuarter(String target) {
        String[] strs = target.split("\\.");
        String year = strs[0];
        String quarter = strs[1];
        String period1 = "";
        String period2 = "";
        String period3 = "";
        List<String> targets = new ArrayList<>();
        if (quarter.equals("04")) {
            period1 = Integer.parseInt(year) + 1 + ".01";
            period2 = Integer.parseInt(year) + 1 + ".02";
            period3 = Integer.parseInt(year) + 1 + ".03";
        } else {
            period1 = Integer.parseInt(quarter)*3 + 1 < 10 ? year + ".0" + (Integer.parseInt(quarter)*3 + 1 ):  year + "." + (Integer.parseInt(quarter)*3 + 1);
            period2 = Integer.parseInt(quarter)*3 + 2 < 10 ? year + ".0" + (Integer.parseInt(quarter)*3 + 2 ):  year + "." + (Integer.parseInt(quarter)*3 + 2);
            period3 = Integer.parseInt(quarter)*3 + 3 < 10 ? year + ".0" + (Integer.parseInt(quarter)*3 + 3 ):  year + "." + (Integer.parseInt(quarter)*3 + 3);
        }
        targets.add(period1);
        targets.add(period2);
        targets.add(period3);
        return targets;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void interfaceToMain(Long groupId) {
        swmQuarterlyAssessmentDao.interfaceToMain(groupId);
    }
}
