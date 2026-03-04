package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcOvertimeLeaveDao;
import com.sunten.hrms.ac.dao.AcSetUpDao;
import com.sunten.hrms.ac.domain.AcOvertimeLeave;
import com.sunten.hrms.ac.domain.AcSetUp;
import com.sunten.hrms.ac.dto.AcOvertimeLeaveDTO;
import com.sunten.hrms.ac.dto.AcOvertimeLeaveQueryCriteria;
import com.sunten.hrms.ac.mapper.AcOvertimeLeaveMapper;
import com.sunten.hrms.ac.service.AcOvertimeLeaveService;
import com.sunten.hrms.pm.service.impl.PmEmployeeServiceImpl;
import com.sunten.hrms.swm.domain.SwmPostSkillSalary;
import com.sunten.hrms.swm.service.SwmPostSkillSalaryService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * <p>
 * oa加班请假统计 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-26
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcOvertimeLeaveServiceImpl extends ServiceImpl<AcOvertimeLeaveDao, AcOvertimeLeave> implements AcOvertimeLeaveService {
    private final AcOvertimeLeaveDao acOvertimeLeaveDao;
    private final AcOvertimeLeaveMapper acOvertimeLeaveMapper;
    private final PmEmployeeServiceImpl pmEmployeeServiceImpl;
    private final AcSetUpDao acSetUpDao;
    private final SwmPostSkillSalaryService swmPostSkillSalaryService;

    public AcOvertimeLeaveServiceImpl(AcOvertimeLeaveDao acOvertimeLeaveDao, AcOvertimeLeaveMapper acOvertimeLeaveMapper,
                                      PmEmployeeServiceImpl pmEmployeeServiceImpl, AcSetUpDao acSetUpDao, SwmPostSkillSalaryService swmPostSkillSalaryService) {
        this.acOvertimeLeaveDao = acOvertimeLeaveDao;
        this.acOvertimeLeaveMapper = acOvertimeLeaveMapper;
        this.pmEmployeeServiceImpl = pmEmployeeServiceImpl;
        this.acSetUpDao = acSetUpDao;
        this.swmPostSkillSalaryService = swmPostSkillSalaryService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcOvertimeLeaveDTO insert(AcOvertimeLeave overtimeLeaveNew) {
        acOvertimeLeaveDao.insertAllColumn(overtimeLeaveNew);
        return acOvertimeLeaveMapper.toDto(overtimeLeaveNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AcOvertimeLeave overtimeLeave = new AcOvertimeLeave();
        overtimeLeave.setId(id);
        this.delete(overtimeLeave);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcOvertimeLeave overtimeLeave) {
        // TODO    确认删除前是否需要做检查
        acOvertimeLeaveDao.deleteByEntityKey(overtimeLeave);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcOvertimeLeave overtimeLeaveNew) {
            AcOvertimeLeave overtimeLeaveInDb = Optional.ofNullable(acOvertimeLeaveDao.getByKey(overtimeLeaveNew.getId())).orElseGet(AcOvertimeLeave::new);
            ValidationUtil.isNull(overtimeLeaveInDb.getId() ,"OvertimeLeave", "id", overtimeLeaveNew.getId());
            overtimeLeaveNew.setId(overtimeLeaveInDb.getId());
            String period = overtimeLeaveNew.getDate().getYear() + "." + (overtimeLeaveNew.getDate().getMonthValue() < 10 ? "0" + overtimeLeaveNew.getDate().getMonthValue() :
            overtimeLeaveNew.getDate());
            if (swmPostSkillSalaryService.checkFrozenFlagByPeriod(period) > 0) {
                // 检测今天是否有发送过这类邮件
                if (!acOvertimeLeaveDao.checkOvertimeEmailIsSendToday("工时修改通知")) {
                    swmPostSkillSalaryService.sendEmailByOverTimeChange(period);
                }
            }
            acOvertimeLeaveDao.updateAllColumnByKey(overtimeLeaveNew);
    }

    @Override
    public AcOvertimeLeaveDTO getByKey(Long id) {
        AcOvertimeLeave overtimeLeave = Optional.ofNullable(acOvertimeLeaveDao.getByKey(id)).orElseGet(AcOvertimeLeave::new);
        ValidationUtil.isNull(overtimeLeave.getId() ,"OvertimeLeave", "id", id);
        return acOvertimeLeaveMapper.toDto(overtimeLeave);
    }

    @Override
    public List<AcOvertimeLeaveDTO> listAll(AcOvertimeLeaveQueryCriteria criteria) {
        List<AcOvertimeLeave> acOvertimeLeaves = acOvertimeLeaveDao.listAllByCriteria(criteria);
        BigDecimal calculation = acSetUpDao.getByNew().getCalculation();
        for (AcOvertimeLeave ac : acOvertimeLeaves
             ) {
            ac.setWorkingHoursDay(ac.getWorkingHours() != null ? ac.getWorkingHours().divide(calculation, 2, RoundingMode.HALF_UP) : new BigDecimal(0));
            culTotalWorkingHours(calculation, ac);
        }
        return acOvertimeLeaveMapper.toDto(acOvertimeLeaves);
    }

    @Override
    public Map<String, Object> listAll(AcOvertimeLeaveQueryCriteria criteria, Pageable pageable) {
        Page<AcOvertimeLeave> page = PageUtil.startPage(pageable);
        // 获取考勤设置的计算参数
        BigDecimal calculation = acSetUpDao.getByNew().getCalculation();
        List<AcOvertimeLeave> overtimeLeaves = acOvertimeLeaveDao.listAllByCriteriaPage(page, criteria);
        for (AcOvertimeLeave ac : overtimeLeaves
        ) {
            // 工作天数
             ac.setWorkingHoursDay(ac.getWorkingHours() != null ? ac.getWorkingHours().divide(calculation, 2, RoundingMode.HALF_UP) : new BigDecimal(0));
            // 计算实际工时
            culTotalWorkingHours(calculation, ac);
        }
        return PageUtil.toPage(acOvertimeLeaveMapper.toDto(overtimeLeaves), page.getTotal());
    }

    private void culTotalWorkingHours(BigDecimal calculation, AcOvertimeLeave ac) {
        // 实际工时 = 上班工时 + 加班工时- 调休工时- 其它请假
        ac.setTotalWorkingHours(
                new BigDecimal(0).add(null != ac.getWorkingHours() ? ac.getWorkingHours() : new BigDecimal(0))
                        .add(null != ac.getWorkingDayOvertime() ? ac.getWorkingDayOvertime() : new BigDecimal(0))
                        .add(null != ac.getRestDayOvertime() ? ac.getRestDayOvertime() : new BigDecimal(0))
                        .add(null != ac.getHolidayOvertime() ? ac.getHolidayOvertime() : new BigDecimal(0))
                        .subtract(null != ac.getOffHours() ? ac.getOffHours() : new BigDecimal(0))
                        .subtract(null != ac.getAnnualLeave() ? ac.getAnnualLeave().multiply(calculation) : new BigDecimal(0))
                        .subtract(null != ac.getCompassionateLeave() ? ac.getCompassionateLeave().multiply(calculation) : new BigDecimal(0))
                        .subtract(null != ac.getSickLeave() ? ac.getSickLeave().multiply(calculation) : new BigDecimal(0))
                        .subtract(null != ac.getMarriageHoliday() ? ac.getMarriageHoliday().multiply(calculation) : new BigDecimal(0))
                        .subtract(null != ac.getMaternityLeave() ? ac.getMaternityLeave().multiply(calculation) : new BigDecimal(0))
                        .subtract(null != ac.getPaternityLeave() ? ac.getPaternityLeave().multiply(calculation) : new BigDecimal(0))
                        .subtract(null != ac.getBereavementLeave() ? ac.getBereavementLeave().multiply(calculation) : new BigDecimal(0))
                        .subtract(null != ac.getWorkRelatedInjuryLeave() ? ac.getWorkRelatedInjuryLeave().multiply(calculation) : new BigDecimal(0))
                        .subtract(null != ac.getFamilyPlanningLeave() ? ac.getFamilyPlanningLeave().multiply(calculation) : new BigDecimal(0))
                        .subtract(null != ac.getPublicHoliday() ? ac.getPublicHoliday().multiply(calculation) : new BigDecimal(0))

        );
        // 加班工时汇总 = 加班工时 - 调休工时
        ac.setTotalOverTime(
                new BigDecimal(0).add(null != ac.getWorkingDayOvertime() ? ac.getWorkingDayOvertime() : new BigDecimal(0))
                .add(null != ac.getRestDayOvertime() ? ac.getRestDayOvertime() : new BigDecimal(0))
                        .add(null != ac.getHolidayOvertime() ? ac.getHolidayOvertime() : new BigDecimal(0))
                .subtract(null != ac.getOffHours() ? ac.getOffHours() : new BigDecimal(0))
        );
    }



    @Override
    public void download(List<AcOvertimeLeaveDTO> overtimeLeaveDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        BigDecimal calculation = acSetUpDao.getByNew().getCalculation();
        for (AcOvertimeLeaveDTO overtimeLeaveDTO : overtimeLeaveDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            overtimeLeaveDTO.setTotalWorkingHours(new BigDecimal(0).add(null != overtimeLeaveDTO.getWorkingHours() ? overtimeLeaveDTO.getWorkingHours() : new BigDecimal(0))
                    .add(null != overtimeLeaveDTO.getWorkingDayOvertime() ? overtimeLeaveDTO.getWorkingDayOvertime() : new BigDecimal(0))
                    .add(null != overtimeLeaveDTO.getRestDayOvertime() ? overtimeLeaveDTO.getRestDayOvertime() : new BigDecimal(0))
                    .add(null != overtimeLeaveDTO.getHolidayOvertime() ? overtimeLeaveDTO.getHolidayOvertime() : new BigDecimal(0))
                    .subtract(null != overtimeLeaveDTO.getOffHours() ? overtimeLeaveDTO.getOffHours() : new BigDecimal(0))
                    .subtract(null != overtimeLeaveDTO.getAnnualLeave() ? overtimeLeaveDTO.getAnnualLeave().multiply(calculation) : new BigDecimal(0))
                    .subtract(null != overtimeLeaveDTO.getCompassionateLeave() ? overtimeLeaveDTO.getCompassionateLeave().multiply(calculation) : new BigDecimal(0))
                    .subtract(null != overtimeLeaveDTO.getSickLeave() ? overtimeLeaveDTO.getSickLeave().multiply(calculation) : new BigDecimal(0))
                    .subtract(null != overtimeLeaveDTO.getMarriageHoliday() ? overtimeLeaveDTO.getMarriageHoliday().multiply(calculation) : new BigDecimal(0))
                    .subtract(null != overtimeLeaveDTO.getMaternityLeave() ? overtimeLeaveDTO.getMaternityLeave().multiply(calculation) : new BigDecimal(0))
                    .subtract(null != overtimeLeaveDTO.getPaternityLeave() ? overtimeLeaveDTO.getPaternityLeave().multiply(calculation) : new BigDecimal(0))
                    .subtract(null != overtimeLeaveDTO.getBereavementLeave() ? overtimeLeaveDTO.getBereavementLeave().multiply(calculation) : new BigDecimal(0))
                    .subtract(null != overtimeLeaveDTO.getWorkRelatedInjuryLeave() ? overtimeLeaveDTO.getWorkRelatedInjuryLeave().multiply(calculation) : new BigDecimal(0))
                    .subtract(null != overtimeLeaveDTO.getFamilyPlanningLeave() ? overtimeLeaveDTO.getFamilyPlanningLeave().multiply(calculation) : new BigDecimal(0))
                    .subtract(null != overtimeLeaveDTO.getPublicHoliday() ? overtimeLeaveDTO.getPublicHoliday().multiply(calculation) : new BigDecimal(0)));
            map.put("所在区间", overtimeLeaveDTO.getDate().getYear() + "." + (overtimeLeaveDTO.getDate().getMonthValue() < 10 ? "0" + overtimeLeaveDTO.getDate().getMonthValue() : overtimeLeaveDTO.getDate().getMonthValue() ));
            map.put("工牌号", overtimeLeaveDTO.getEmployee().getWorkCard());
            map.put("姓名", overtimeLeaveDTO.getEmployee().getName());
            map.put("部门", overtimeLeaveDTO.getEmployee().getDeptName() != null ? overtimeLeaveDTO.getEmployee().getDeptName() : "");
            map.put("科室", overtimeLeaveDTO.getEmployee().getDepartment() != null ? overtimeLeaveDTO.getEmployee().getDepartment() : "");
            map.put("班组", overtimeLeaveDTO.getEmployee().getTeam() != null ? overtimeLeaveDTO.getEmployee().getTeam() : "");
            map.put("岗位", overtimeLeaveDTO.getEmployee().getJobName() != null ? overtimeLeaveDTO.getEmployee().getJobName() : "");
            map.put("应上班天数", overtimeLeaveDTO.getWorkingHours() != null ? overtimeLeaveDTO.getWorkingHours().divide(calculation, 2, RoundingMode.HALF_UP) : new BigDecimal(0));
            map.put("工作日加班时数", overtimeLeaveDTO.getWorkingDayOvertime() != null ? overtimeLeaveDTO.getWorkingDayOvertime() : "0.00");
            map.put("休息日加班时数", overtimeLeaveDTO.getRestDayOvertime() != null ? overtimeLeaveDTO.getRestDayOvertime() : "0.00");
            map.put("法定节假日加班时数", overtimeLeaveDTO.getHolidayOvertime() != null ? overtimeLeaveDTO.getHolidayOvertime() : "0.00");
            map.put("调休时数", overtimeLeaveDTO.getOffHours() != null ? overtimeLeaveDTO.getOffHours() : "0.00");
            map.put("事假", overtimeLeaveDTO.getCompassionateLeave() != null ? overtimeLeaveDTO.getCompassionateLeave() : "0.00");
            map.put("病假", overtimeLeaveDTO.getSickLeave() != null ? overtimeLeaveDTO.getSickLeave() : "0.00");
            map.put("带薪年假", overtimeLeaveDTO.getAnnualLeave() != null ? overtimeLeaveDTO.getAnnualLeave() : "0.00");
            map.put("婚假", overtimeLeaveDTO.getMarriageHoliday() != null ? overtimeLeaveDTO.getMarriageHoliday() : "0.00");
            map.put("产假", overtimeLeaveDTO.getMaternityLeave() != null ? overtimeLeaveDTO.getMaternityLeave() : "0.00");
            map.put("陪产假", overtimeLeaveDTO.getPaternityLeave() != null ? overtimeLeaveDTO.getPaternityLeave() : "0.00");
            map.put("丧假", overtimeLeaveDTO.getBereavementLeave() != null ? overtimeLeaveDTO.getBereavementLeave() : "0.00");
            map.put("工伤假", overtimeLeaveDTO.getWorkRelatedInjuryLeave() != null ? overtimeLeaveDTO.getWorkRelatedInjuryLeave() : "0.00");
            map.put("计划生育假", overtimeLeaveDTO.getFamilyPlanningLeave() != null ? overtimeLeaveDTO.getFamilyPlanningLeave() : "0.00");
            map.put("公假", overtimeLeaveDTO.getPublicHoliday() != null ? overtimeLeaveDTO.getPublicHoliday() : "0.00");
            map.put("加班时间汇总", overtimeLeaveDTO.getTotalOverTime() != null ? overtimeLeaveDTO.getTotalOverTime() : "0.00");
            map.put("实际上班时数", overtimeLeaveDTO.getTotalWorkingHours() != null ? overtimeLeaveDTO.getTotalWorkingHours() : "0.00");
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void downloadSum(List<AcOvertimeLeaveDTO> overtimeLeaveDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        BigDecimal calculation = acSetUpDao.getByNew().getCalculation();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (AcOvertimeLeaveDTO overtimeLeaveDTO : overtimeLeaveDTOS) {
            AcOvertimeLeave overtimeLeave = acOvertimeLeaveMapper.toEntity(overtimeLeaveDTO);
            overtimeLeave.setWorkingHoursDay(overtimeLeave.getWorkingHours() != null ? overtimeLeave.getWorkingHours().divide(calculation, 2, RoundingMode.HALF_UP) : new BigDecimal(0));
            culTotalWorkingHours(calculation, overtimeLeave);
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("工牌号", overtimeLeave.getEmployee().getWorkCard());
            map.put("姓名", overtimeLeave.getEmployee().getName());
            map.put("部门", overtimeLeave.getEmployee().getDeptName() != null ? overtimeLeave.getEmployee().getDeptName() : "");
            map.put("科室", overtimeLeave.getEmployee().getDepartment() != null ? overtimeLeave.getEmployee().getDepartment() : "");
            map.put("班组", overtimeLeave.getEmployee().getTeam() != null ? overtimeLeave.getEmployee().getTeam() : "");
            map.put("岗位", overtimeLeave.getEmployee().getJobName() != null ? overtimeLeave.getEmployee().getJobName() : "");
            map.put("入职时间", overtimeLeave.getEntryTime() != null ? fmt.format(overtimeLeaveDTO.getEntryTime()) : "无入职时间");
            map.put("离职时间", overtimeLeave.getQuitTime() != null ? fmt.format(overtimeLeave.getQuitTime()) : "无离职时间");
            map.put("应上班天数", overtimeLeave.getWorkingHours() != null ? overtimeLeave.getWorkingHours().divide(calculation, 2, RoundingMode.HALF_UP) : new BigDecimal(0));
            map.put("工作日加班时数", overtimeLeave.getWorkingDayOvertime() != null ? overtimeLeave.getWorkingDayOvertime() : "0.00");
            map.put("休息日加班时数", overtimeLeave.getRestDayOvertime() != null ? overtimeLeave.getRestDayOvertime() : "0.00");
            map.put("法定节假日加班时数", overtimeLeave.getHolidayOvertime() != null ? overtimeLeave.getHolidayOvertime() : "0.00");
            map.put("调休时数", overtimeLeave.getOffHours() != null ? overtimeLeave.getOffHours() : "0.00");
            map.put("事假", overtimeLeave.getCompassionateLeave() != null ? overtimeLeave.getCompassionateLeave() : "0.00");
            map.put("病假", overtimeLeave.getSickLeave() != null ? overtimeLeave.getSickLeave() : "0.00");
            map.put("带薪年假", overtimeLeave.getAnnualLeave() != null ? overtimeLeave.getAnnualLeave() : "0.00");
            map.put("婚假", overtimeLeave.getMarriageHoliday() != null ? overtimeLeave.getMarriageHoliday() : "0.00");
            map.put("产假", overtimeLeave.getMaternityLeave() != null ? overtimeLeave.getMaternityLeave() : "0.00");
            map.put("陪产假", overtimeLeave.getPaternityLeave() != null ? overtimeLeave.getPaternityLeave() : "0.00");
            map.put("丧假", overtimeLeave.getBereavementLeave() != null ? overtimeLeave.getBereavementLeave() : "0.00");
            map.put("工伤假", overtimeLeave.getWorkRelatedInjuryLeave() != null ? overtimeLeave.getWorkRelatedInjuryLeave() : "0.00");
            map.put("计划生育假", overtimeLeave.getFamilyPlanningLeave() != null ? overtimeLeave.getFamilyPlanningLeave() : "0.00");
            map.put("公假", overtimeLeave.getPublicHoliday() != null ? overtimeLeave.getPublicHoliday() : "0.00");
            map.put("加班时间汇总", overtimeLeave.getTotalOverTime() != null ? overtimeLeave.getTotalOverTime() : "0.00");
            map.put("实际上班时数", overtimeLeave.getTotalWorkingHours() != null ? overtimeLeave.getTotalWorkingHours() : "0.00");
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Integer countDataByMonth(LocalDate month) {
        return acOvertimeLeaveDao.countDataByMonth(month);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllByMonth(LocalDate month) {
        acOvertimeLeaveDao.deleteAllByMonth(month);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertByInterface(Long operationId) {
        acOvertimeLeaveDao.insertByInterface(operationId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoCreateLastMonth() { //自动生成
        LocalDate now = LocalDate.now().minusMonths(1);
        AcOvertimeLeaveQueryCriteria acOvertimeLeaveQueryCriteria = new AcOvertimeLeaveQueryCriteria();
        acOvertimeLeaveQueryCriteria.setDate(now);
        List<AcOvertimeLeave> acOvertimeLeaves = acOvertimeLeaveDao.listAllByCriteria(acOvertimeLeaveQueryCriteria);
        if (acOvertimeLeaves.size() <= 0) { //只有未生成才会生成
            acOvertimeLeaveDao.autoCreateLastMonth();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoCreateLastMonthWithoutCheck() {
        acOvertimeLeaveDao.autoCreateLastMonth();
    }

    @Override
    public Map<String, Object> sumAcOvertimeLeavePage(Pageable pageable, AcOvertimeLeaveQueryCriteria acOvertimeLeaveQueryCriteria) {
        Page<AcOvertimeLeave> page = PageUtil.startPage(pageable);
        BigDecimal calculation = acSetUpDao.getByNew().getCalculation();
        List<AcOvertimeLeave> overtimeLeaves = acOvertimeLeaveDao.sumAcOvertimeLeavePage(page, acOvertimeLeaveQueryCriteria);
        for (AcOvertimeLeave ac : overtimeLeaves
        ) {
            ac.setWorkingHoursDay(ac.getWorkingHours() != null ? ac.getWorkingHours().divide(calculation, 2, RoundingMode.HALF_UP) : new BigDecimal(0));
            // 计算实际工时 及 加班时间汇总及实际上班时间
            culTotalWorkingHours(calculation, ac);
        }
        return PageUtil.toPage(acOvertimeLeaveMapper.toDto(overtimeLeaves), page.getTotal());
    }

    @Override
    public List<AcOvertimeLeaveDTO> sumAcOvertimeLeave(AcOvertimeLeaveQueryCriteria acOvertimeLeaveQueryCriteria) {
        BigDecimal calculation = acSetUpDao.getByNew().getCalculation();
        List<AcOvertimeLeave> acOvertimeLeaves = acOvertimeLeaveDao.sumAcOvertimeLeave(acOvertimeLeaveQueryCriteria);
        for (AcOvertimeLeave ac : acOvertimeLeaves
             ) {
            ac.setWorkingHoursDay(ac.getWorkingHours() != null ? ac.getWorkingHours().divide(calculation, 2, RoundingMode.HALF_UP) : new BigDecimal(0));
            culTotalWorkingHours(calculation, ac);
        }
        return acOvertimeLeaveMapper.toDto(acOvertimeLeaves);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLastMonthWorkingHours() {
        acOvertimeLeaveDao.updateLastMonthWorkingHours();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBeforeinterface(LocalDate date) {
        acOvertimeLeaveDao.createBeforeinterface(date);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBeforeinterface(LocalDate date) {
        acOvertimeLeaveDao.updateBeforeinterface(date);
    }
}
