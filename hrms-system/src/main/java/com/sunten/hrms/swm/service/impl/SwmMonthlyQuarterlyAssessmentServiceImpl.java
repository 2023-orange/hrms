package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.dao.SwmFloatingWageDao;
import com.sunten.hrms.swm.domain.SwmMonthlyQuarterlyAssessment;
import com.sunten.hrms.swm.dao.SwmMonthlyQuarterlyAssessmentDao;
import com.sunten.hrms.swm.domain.SwmMonthlyQuarterlyAssessmentNum;
import com.sunten.hrms.swm.dto.SwmFloatingWageQueryCriteria;
import com.sunten.hrms.swm.service.SwmMonthlyQuarterlyAssessmentService;
import com.sunten.hrms.swm.dto.SwmMonthlyQuarterlyAssessmentDTO;
import com.sunten.hrms.swm.dto.SwmMonthlyQuarterlyAssessmentQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmMonthlyQuarterlyAssessmentMapper;
import com.sunten.hrms.swm.service.SwmNolimitationDeptService;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
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
 * 月度考核表(一个季度生成三条月度) 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmMonthlyQuarterlyAssessmentServiceImpl extends ServiceImpl<SwmMonthlyQuarterlyAssessmentDao, SwmMonthlyQuarterlyAssessment> implements SwmMonthlyQuarterlyAssessmentService {
    private final SwmMonthlyQuarterlyAssessmentDao swmMonthlyQuarterlyAssessmentDao;
    private final SwmMonthlyQuarterlyAssessmentMapper swmMonthlyQuarterlyAssessmentMapper;
    private final FndUserService fndUserService;
    private final SwmPostSkillSalaryServiceImpl swmPostSkillSalaryServiceImpl;
    private final SwmFloatingWageDao swmFloatingWageDao;
    private final SwmNolimitationDeptService swmNolimitationDeptService;

    public SwmMonthlyQuarterlyAssessmentServiceImpl(SwmMonthlyQuarterlyAssessmentDao swmMonthlyQuarterlyAssessmentDao, SwmMonthlyQuarterlyAssessmentMapper swmMonthlyQuarterlyAssessmentMapper
            , FndUserService fndUserService, SwmPostSkillSalaryServiceImpl swmPostSkillSalaryServiceImpl, SwmFloatingWageDao swmFloatingWageDao, SwmNolimitationDeptService swmNolimitationDeptService) {
        this.swmMonthlyQuarterlyAssessmentDao = swmMonthlyQuarterlyAssessmentDao;
        this.swmMonthlyQuarterlyAssessmentMapper = swmMonthlyQuarterlyAssessmentMapper;
        this.fndUserService = fndUserService;
        this.swmPostSkillSalaryServiceImpl = swmPostSkillSalaryServiceImpl;
        this.swmFloatingWageDao = swmFloatingWageDao;
        this.swmNolimitationDeptService = swmNolimitationDeptService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmMonthlyQuarterlyAssessmentDTO insert(SwmMonthlyQuarterlyAssessment monthlyQuarterlyAssessmentNew) {
        swmMonthlyQuarterlyAssessmentDao.insertAllColumn(monthlyQuarterlyAssessmentNew);
        return swmMonthlyQuarterlyAssessmentMapper.toDto(monthlyQuarterlyAssessmentNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmMonthlyQuarterlyAssessment monthlyQuarterlyAssessment = new SwmMonthlyQuarterlyAssessment();
        monthlyQuarterlyAssessment.setId(id);
        this.delete(monthlyQuarterlyAssessment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmMonthlyQuarterlyAssessment monthlyQuarterlyAssessment) {
        // TODO    确认删除前是否需要做检查
        swmMonthlyQuarterlyAssessmentDao.deleteByEntityKey(monthlyQuarterlyAssessment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmMonthlyQuarterlyAssessment monthlyQuarterlyAssessmentNew) {
        //TODO 检查浮动工资是否已经生成
        SwmMonthlyQuarterlyAssessment monthlyQuarterlyAssessmentInDb = Optional.ofNullable(swmMonthlyQuarterlyAssessmentDao.getByKey(monthlyQuarterlyAssessmentNew.getId())).orElseGet(SwmMonthlyQuarterlyAssessment::new);
        ValidationUtil.isNull(monthlyQuarterlyAssessmentInDb.getId() ,"MonthlyQuarterlyAssessment", "id", monthlyQuarterlyAssessmentNew.getId());
        monthlyQuarterlyAssessmentNew.setId(monthlyQuarterlyAssessmentInDb.getId());
        if (null != monthlyQuarterlyAssessmentNew.getSubmitTime()) { // 自动填入提交人
            FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
            monthlyQuarterlyAssessmentNew.setSubmitter(user.getEmployee().getName());
        }
        swmMonthlyQuarterlyAssessmentDao.updateAllColumnByKey(monthlyQuarterlyAssessmentNew);
    }

    @Override
    public SwmMonthlyQuarterlyAssessmentDTO getByKey(Long id) {
        SwmMonthlyQuarterlyAssessment monthlyQuarterlyAssessment = Optional.ofNullable(swmMonthlyQuarterlyAssessmentDao.getByKey(id)).orElseGet(SwmMonthlyQuarterlyAssessment::new);
        ValidationUtil.isNull(monthlyQuarterlyAssessment.getId() ,"MonthlyQuarterlyAssessment", "id", id);
        return swmMonthlyQuarterlyAssessmentMapper.toDto(monthlyQuarterlyAssessment);
    }

    @Override
    public List<SwmMonthlyQuarterlyAssessmentDTO> listAll(SwmMonthlyQuarterlyAssessmentQueryCriteria criteria) {
        return swmMonthlyQuarterlyAssessmentMapper.toDto(swmMonthlyQuarterlyAssessmentDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmMonthlyQuarterlyAssessmentQueryCriteria criteria, Pageable pageable) {
        Page<SwmMonthlyQuarterlyAssessment> page = PageUtil.startPage(pageable);
        List<SwmMonthlyQuarterlyAssessment> monthlyQuarterlyAssessments = swmMonthlyQuarterlyAssessmentDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmMonthlyQuarterlyAssessmentMapper.toDto(monthlyQuarterlyAssessments), page.getTotal());
    }

    @Override
    public void download(List<SwmMonthlyQuarterlyAssessmentDTO> monthlyQuarterlyAssessmentDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmMonthlyQuarterlyAssessmentDTO monthlyQuarterlyAssessmentDTO : monthlyQuarterlyAssessmentDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("考核月度", monthlyQuarterlyAssessmentDTO.getAssessmentMonth());
            map.put("工牌号", monthlyQuarterlyAssessmentDTO.getWorkCard());
            map.put("姓名", monthlyQuarterlyAssessmentDTO.getName());
            map.put("部门", monthlyQuarterlyAssessmentDTO.getDepartment());
            map.put("科室", monthlyQuarterlyAssessmentDTO.getAdministrativeOffice());
            map.put("考核等级", monthlyQuarterlyAssessmentDTO.getAssessmentLevel());
            map.put("是否提交", monthlyQuarterlyAssessmentDTO.getSubmissionIdentificationFlag() ?  "已提交" : "未提交");
            map.put("提交人", null != monthlyQuarterlyAssessmentDTO.getSubmitter() ? monthlyQuarterlyAssessmentDTO.getSubmitter() : "");
            map.put("提交时间", null != monthlyQuarterlyAssessmentDTO.getSubmitTime() ? DateUtil.localDateTimeToStr(monthlyQuarterlyAssessmentDTO.getSubmitTime()) : "");
//            map.put("区分月度季度", "month".equals(monthlyQuarterlyAssessmentDTO.getAssessmentType()) ? "月度考核" : "季度考核");
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SwmMonthlyQuarterlyAssessment> createMonthlyAssessment(String period) {
        Map<String, Object> map = new HashMap<>();
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        map.put("period", period);
        map.put("userId", user.getId());
        map.put("resultStr", "");
        swmMonthlyQuarterlyAssessmentDao.createMonthlyAssessment(map);
        String str = map.get("resultStr").toString();
        if (str.equals("SUCCESS")) {
            SwmMonthlyQuarterlyAssessmentQueryCriteria swmMonthlyQuarterlyAssessmentQueryCriteria = new SwmMonthlyQuarterlyAssessmentQueryCriteria();
            swmMonthlyQuarterlyAssessmentQueryCriteria.setAssessmentType("month");
            swmMonthlyQuarterlyAssessmentQueryCriteria.setPeriod(period);
            return swmMonthlyQuarterlyAssessmentDao.listAllByCriteria(swmMonthlyQuarterlyAssessmentQueryCriteria);
        } else {
            throw new InfoCheckWarningMessException(str);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMonthlyByPeriod(String period) {
        if (swmMonthlyQuarterlyAssessmentDao.countByMonth(period) > 0) {
            // 判定浮动工资是否已经发放
            SwmFloatingWageQueryCriteria swmFloatingWageQueryCriteria = new SwmFloatingWageQueryCriteria();
            swmFloatingWageQueryCriteria.setPeriod(period);
            swmFloatingWageQueryCriteria.setFrozenFlag(true);
            Integer count = swmFloatingWageDao.countByQuery(swmFloatingWageQueryCriteria);
            Integer countFrozenFlag = swmMonthlyQuarterlyAssessmentDao.countByFrozenFlag(period);
            System.out.println("----------------------------- " + countFrozenFlag);
            if (!(count <= 0)) { // 不能删
                throw new InfoCheckWarningMessException("该所得期间的浮动工资已经冻结，不允许删除月度考核");
            }  else if( countFrozenFlag >0 ){
                throw new InfoCheckWarningMessException("该所得期间的月度考核存在冻结数据，不允许删除月度考核");
            }
            else {
                FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
                SwmMonthlyQuarterlyAssessment swmMonthlyQuarterlyAssessment = new SwmMonthlyQuarterlyAssessment();
                swmMonthlyQuarterlyAssessment.setAssessmentMonth(period);
                swmMonthlyQuarterlyAssessment.setUpdateTime(LocalDateTime.now());
                swmMonthlyQuarterlyAssessment.setUpdateBy(user.getId());
                swmMonthlyQuarterlyAssessment.setAssessmentType("month");
                swmMonthlyQuarterlyAssessmentDao.deleteMonthlyByPeriod(swmMonthlyQuarterlyAssessment);
            }
        } else {
            throw new InfoCheckWarningMessException("该所得期间暂未生成月度考核");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMonthlyByPeriodWithNoCheck(String period) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        SwmMonthlyQuarterlyAssessment swmMonthlyQuarterlyAssessment = new SwmMonthlyQuarterlyAssessment();
        swmMonthlyQuarterlyAssessment.setAssessmentMonth(period);
        swmMonthlyQuarterlyAssessment.setUpdateTime(LocalDateTime.now());
        swmMonthlyQuarterlyAssessment.setUpdateBy(user.getId());
        swmMonthlyQuarterlyAssessment.setAssessmentType("quarter");
        swmMonthlyQuarterlyAssessmentDao.deleteMonthlyByPeriod(swmMonthlyQuarterlyAssessment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeQuarterlyByPeriod(String period) {

        swmMonthlyQuarterlyAssessmentDao.deleteQuarterByPeriod(period);
    }

    @Override
    public List<String> getMonthPeriodList() {
        String topPeriod = swmMonthlyQuarterlyAssessmentDao.getTopMonthPeriod();
        return swmPostSkillSalaryServiceImpl.getPeriodList(topPeriod);
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void submitEmployeeMonthly(List<SwmMonthlyQuarterlyAssessment> swmMonthlyQuarterlyAssessments) {
//        for (SwmMonthlyQuarterlyAssessment swmMonthlyQuarterlyAssessment : swmMonthlyQuarterlyAssessments
//             ) {
//            System.out.println(swmMonthlyQuarterlyAssessment);
//            SwmMonthlyQuarterlyAssessmentNum swmMonthlyQuarterlyAssessmentNum = swmMonthlyQuarterlyAssessmentDao.countNumByWorkCard(swmMonthlyQuarterlyAssessment.getWorkCard());
//            if (swmMonthlyQuarterlyAssessment.getAssessmentLevel() == "A")
//            {
//                int num = swmMonthlyQuarterlyAssessmentNum.getActualNum();
//            }
//            if()
//            {
//                throw new InfoCheckWarningMessException("该所得期间的浮动工资已经冻结，不允许再修改考核等级");
//            }
//            swmMonthlyQuarterlyAssessmentDao.updateAssessmentLevel(swmMonthlyQuarterlyAssessment);
//        }
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitEmployeeMonthly(List<SwmMonthlyQuarterlyAssessment> swmMonthlyQuarterlyAssessments) {
        for (SwmMonthlyQuarterlyAssessment swmMonthlyQuarterlyAssessment : swmMonthlyQuarterlyAssessments
        ) {
            swmMonthlyQuarterlyAssessmentDao.updateAssessmentLevel(swmMonthlyQuarterlyAssessment);
        }
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String submitEmployeeMonthly(List<SwmMonthlyQuarterlyAssessment> swmMonthlyQuarterlyAssessments) {
//        for (SwmMonthlyQuarterlyAssessment swmMonthlyQuarterlyAssessment : swmMonthlyQuarterlyAssessments
//        ) {
//            SwmMonthlyQuarterlyAssessmentNum swmNum = new SwmMonthlyQuarterlyAssessmentNum();
//            if (swmMonthlyQuarterlyAssessment.getAssessmentLevel().equals("A")) // 等于A时进入判断
//            {
//                if (swmNolimitationDeptService.countDept(swmMonthlyQuarterlyAssessment.getDepartment()) > 0) { // 科室评A总数相加部门（特殊化处理）
//                    swmNum = swmMonthlyQuarterlyAssessmentDao.noLimitDept(swmMonthlyQuarterlyAssessment);
//                    if (swmNum.getActualNum() > swmNum.getCurrentNum())
//                    {
//                        swmMonthlyQuarterlyAssessmentDao.updateAssessmentLevel(swmMonthlyQuarterlyAssessment);
//                        return "OK";
//                    }
//                    else {
//                        return "评A数量已达到上限";
//                    }
//                }
//                else { // 评A限制部门
//                    swmNum = swmMonthlyQuarterlyAssessmentDao.limitDept(swmMonthlyQuarterlyAssessment);
//                    if (swmNum.getActualNum() > swmNum.getCurrentNum())
//                    {
//                        swmMonthlyQuarterlyAssessmentDao.updateAssessmentLevel(swmMonthlyQuarterlyAssessment);
//                        return "OK";
//                    }
//                    else {
//                        return "评A数量已达到上限";
//                    }
//                }
//            }
//            else {
//                swmMonthlyQuarterlyAssessmentDao.updateAssessmentLevel(swmMonthlyQuarterlyAssessment);
//            }
//        }
//        return "OK";
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateQuarterSubMonthAssessmentLevel(List<String> periodList, String quarter) {
        swmMonthlyQuarterlyAssessmentDao.updateQuarterSubMonthAssessmentLevel(quarter, periodList);
    }

    @Override
    public List<SwmMonthlyQuarterlyAssessmentDTO> getAssessmentList(String workCard) {
        return swmMonthlyQuarterlyAssessmentMapper.toDto(swmMonthlyQuarterlyAssessmentDao.getAssessmentList(workCard));
    }

    @Override
    public void flozenMonthAppraisalSalary(Long limit, String period) {
        swmMonthlyQuarterlyAssessmentDao.updateFlozenFlagByLimit(limit);
    }

    @Override
    public void cancelFrozen(String workCard, String assessmentMonth, Boolean frozenFlag) {
        swmMonthlyQuarterlyAssessmentDao.cancelFrozen(workCard,assessmentMonth,frozenFlag);
    }

}
