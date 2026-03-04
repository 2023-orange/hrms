package com.sunten.hrms.td.service.impl;

import com.sunten.hrms.td.domain.TdPlanResult;
import com.sunten.hrms.td.dao.TdPlanResultDao;
import com.sunten.hrms.td.service.TdPlanResultService;
import com.sunten.hrms.td.dto.TdPlanResultDTO;
import com.sunten.hrms.td.dto.TdPlanResultQueryCriteria;
import com.sunten.hrms.td.mapper.TdPlanResultMapper;
import com.sunten.hrms.td.vo.*;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDate;
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
 * 培训结果记录 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-06-16
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdPlanResultServiceImpl extends ServiceImpl<TdPlanResultDao, TdPlanResult> implements TdPlanResultService {
    private final TdPlanResultDao tdPlanResultDao;
    private final TdPlanResultMapper tdPlanResultMapper;

    public TdPlanResultServiceImpl(TdPlanResultDao tdPlanResultDao, TdPlanResultMapper tdPlanResultMapper) {
        this.tdPlanResultDao = tdPlanResultDao;
        this.tdPlanResultMapper = tdPlanResultMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdPlanResultDTO insert(TdPlanResult planResultNew) {
        tdPlanResultDao.insertAllColumn(planResultNew);
        return tdPlanResultMapper.toDto(planResultNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdPlanResult planResult = new TdPlanResult();
        planResult.setId(id);
        this.delete(planResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdPlanResult planResult) {
        // TODO    确认删除前是否需要做检查
        tdPlanResultDao.deleteByEntityKey(planResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdPlanResult planResultNew) {
        TdPlanResult planResultInDb = Optional.ofNullable(tdPlanResultDao.getByKey(planResultNew.getId())).orElseGet(TdPlanResult::new);
        ValidationUtil.isNull(planResultInDb.getId() ,"PlanResult", "id", planResultNew.getId());
        planResultNew.setId(planResultInDb.getId());
        tdPlanResultDao.updateAllColumnByKey(planResultNew);
    }

    @Override
    public TdPlanResultDTO getByKey(Long id) {
        TdPlanResult planResult = Optional.ofNullable(tdPlanResultDao.getByKey(id)).orElseGet(TdPlanResult::new);
        ValidationUtil.isNull(planResult.getId() ,"PlanResult", "id", id);
        return tdPlanResultMapper.toDto(planResult);
    }

    @Override
    public List<TdPlanResultDTO> listAll(TdPlanResultQueryCriteria criteria) {
        return tdPlanResultMapper.toDto(tdPlanResultDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdPlanResultQueryCriteria criteria, Pageable pageable) {
        Page<TdPlanResult> page = PageUtil.startPage(pageable);
        List<TdPlanResult> planResults = tdPlanResultDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdPlanResultMapper.toDto(planResults), page.getTotal());
    }

    @Override
    public void download(List<TdPlanResultDTO> planResultDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdPlanResultDTO planResultDTO : planResultDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("员工id", planResultDTO.getEmployeeId());
            map.put("培训计划id", planResultDTO.getPlanId());
            map.put("员工出勤情况", planResultDTO.getAttendance());
            map.put("培训时长", planResultDTO.getDuration());
            map.put("成绩", planResultDTO.getGrade());
            map.put("满意度", planResultDTO.getEvaluate());
            map.put("是否签订培训协议书", planResultDTO.getNeedFlag());
            map.put("生效标记", planResultDTO.getEnabledFlag());
            map.put("id", planResultDTO.getId());
            map.put("创建时间", planResultDTO.getCreateTime());
            map.put("创建人ID", planResultDTO.getCreateBy());
            map.put("修改时间", planResultDTO.getUpdateTime());
            map.put("修改人ID", planResultDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void interfaceToMain(Long groupId) {
        tdPlanResultDao.interfaceToMain(groupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByPlanId(TdPlanResult planResult) {
        tdPlanResultDao.disabledByPlanId(planResult);
    }

    @Override
    public void downloadDeptPlanExcel(List<DeptPlanExcelVo> deptPlanExcelVos, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DeptPlanExcelVo deptPlanExcelVo : deptPlanExcelVos
             ) {
            Map<String, Object> map = new LinkedHashMap<>();
            if (null == deptPlanExcelVo.getBeginDate() || null == deptPlanExcelVo.getEndDate()) {
                map.put("日期", "全部");
            } else {
                map.put("日期", DateUtil.localDateToStr(deptPlanExcelVo.getBeginDate()) + " 至 " + DateUtil.localDateToStr(deptPlanExcelVo.getEndDate()));
            }
            map.put("部门", null == deptPlanExcelVo.getDeptName() ? "" : deptPlanExcelVo.getDeptName());
            map.put("科室", null == deptPlanExcelVo.getDepartment() ? "" : deptPlanExcelVo.getDepartment());
            map.put("班组", null == deptPlanExcelVo.getTeam() ? "" : deptPlanExcelVo.getTeam());
            map.put("参训人数", deptPlanExcelVo.getPlanMenber().toString());
            map.put("部门人数", deptPlanExcelVo.getDeptMenberCount().toString());
            map.put("培训覆盖率", deptPlanExcelVo.getDeptMenberCount() != 0 ? String.format("%.2f", (deptPlanExcelVo.getPlanMenber().doubleValue()/deptPlanExcelVo.getDeptMenberCount().doubleValue() * 100)) : "0");
            map.put("总培训时长（小时）", deptPlanExcelVo.getDuration().toString());
            map.put("人均培训时长（小时）", deptPlanExcelVo.getDeptMenberCount() != 0 ? String.format("%.2f", deptPlanExcelVo.getDuration()/deptPlanExcelVo.getDeptMenberCount()) : "0");
            map.put("培训评价平均满意度",  deptPlanExcelVo.getDeptMenberCount() != 0 ? String.format("%.2f", deptPlanExcelVo.getEvaluate()/deptPlanExcelVo.getDeptMenberCount()) : "0");
            map.put("发起培训项目数", deptPlanExcelVo.getMainPlanCount().toString());
            map.put("已实施项目数", deptPlanExcelVo.getMainPlanPassCount().toString());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void downloadPmPlanExcel(List<PmPlanExcelVo> pmPlanExcelVos, String inTeacher, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmPlanExcelVo pmPlanExcelVo : pmPlanExcelVos
             ) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("培训项目", pmPlanExcelVo.getTrainingName());
            map.put("项目类型", pmPlanExcelVo.getTrainingMethod());
            map.put("培训内容", pmPlanExcelVo.getTrainingContent());
            map.put("考核方式", pmPlanExcelVo.getCheckMethod());
            map.put("主讲人", pmPlanExcelVo.getTeacher());
            map.put("培训地点", pmPlanExcelVo.getTrainingAddress());
            if (null == pmPlanExcelVo.getBeginDate()) {
                map.put("培训日期", "");
            } else {
                map.put("培训日期",
                        null == pmPlanExcelVo.getEndDate() ? DateUtil.localDateToStr(pmPlanExcelVo.getBeginDate()) :
                                DateUtil.localDateToStr(pmPlanExcelVo.getBeginDate()) + " 至 " + DateUtil.localDateToStr(pmPlanExcelVo.getEndDate()));
            }
            map.put("培训预设学时（/h）", null != pmPlanExcelVo.getTrainingTimeQuantity() ? pmPlanExcelVo.getTrainingTimeQuantity() : "");
            map.put("学习计划总人数", null != pmPlanExcelVo.getEmployeeQuantity() ? pmPlanExcelVo.getEmployeeQuantity() : "");
            map.put("培训通过率（%）", null == pmPlanExcelVo.getScoreLine() ? "不考试" : String.format("%.2f", pmPlanExcelVo.getPassNumber().doubleValue() / pmPlanExcelVo.getEmployeeQuantity().doubleValue() * 100));
            map.put("员工姓名", pmPlanExcelVo.getName());
            map.put("部门", pmPlanExcelVo.getDeptName());
            map.put("科室", pmPlanExcelVo.getDepartment());
            map.put("班组", pmPlanExcelVo.getTeam());
            map.put("实际出勤时间", null != pmPlanExcelVo.getDuration() ? pmPlanExcelVo.getDuration() : "");
            map.put("培训满意度", null != pmPlanExcelVo.getEvaluate() ? pmPlanExcelVo.getEvaluate() : "");
            map.put("考核成绩", null != pmPlanExcelVo.getGrade() ? pmPlanExcelVo.getGrade() : "");
            map.put("是否通过", null == pmPlanExcelVo.getScoreLine() ? "不考试" : pmPlanExcelVo.getScoreLine() <= pmPlanExcelVo.getGrade() ? "通过" : "不通过");
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);

    }

    @Override
    public void downloadPmPlanHistoryExcel(List<PmPlanHistoryVo> pmPlanHistoryVos, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmPlanHistoryVo pmPlanHistoryVo : pmPlanHistoryVos
             ) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("员工姓名", pmPlanHistoryVo.getName());
            map.put("员工账号", pmPlanHistoryVo.getWorkCard());
            map.put("部门", pmPlanHistoryVo.getDeptName());
            map.put("科室", pmPlanHistoryVo.getDepartment());
            map.put("班组", pmPlanHistoryVo.getTeam());
            map.put("总学习时长（小时）", null != pmPlanHistoryVo.getSumDuration() ? String.format("%.2f", pmPlanHistoryVo.getSumDuration()) : "");
            map.put("参加培训数（场次）", null != pmPlanHistoryVo.getParticipateNum() ? pmPlanHistoryVo.getParticipateNum() : "");
            map.put("培训名称", pmPlanHistoryVo.getTrainingName());
            map.put("培训项目类别", pmPlanHistoryVo.getTrainingMethod());
            map.put("培训内容", pmPlanHistoryVo.getTrainingContent());
            map.put("主讲人", pmPlanHistoryVo.getTeacher());
            if (null == pmPlanHistoryVo.getBeginDate()) {
                map.put("培训日期", "");
            } else {
                map.put("培训日期",
                        null == pmPlanHistoryVo.getEndDate() ? DateUtil.localDateToStr(pmPlanHistoryVo.getBeginDate()) :
                        DateUtil.localDateToStr(pmPlanHistoryVo.getBeginDate()) + " 至 " + DateUtil.localDateToStr(pmPlanHistoryVo.getEndDate()));
            }
            map.put("培训地点", pmPlanHistoryVo.getTrainingAddress());
            map.put("培训预设总学时（小时）", null != pmPlanHistoryVo.getTrainingTimeQuantity() ? String.format("%.2f", pmPlanHistoryVo.getTrainingTimeQuantity()) : "");
            map.put("实际出勤学时（小时）", null != pmPlanHistoryVo.getDuration() ? String.format("%.2f", pmPlanHistoryVo.getDuration()) : "");
            map.put("考试成绩", null != pmPlanHistoryVo.getGrade() ? String.format("%.2f", pmPlanHistoryVo.getGrade()) : "");
            map.put("培训满意度评价", null != pmPlanHistoryVo.getEvaluate() ? String.format("%.1f", pmPlanHistoryVo.getEvaluate()) : "");
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void downloadAgreementExcel(List<AgreementExcelVo> agreementExcelVos, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AgreementExcelVo agreementExcelVo : agreementExcelVos
             ) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("培训名称", agreementExcelVo.getTrainingName());
            map.put("培训地址", agreementExcelVo.getTrainingAddress());
            map.put("培训时长(/h)", null != agreementExcelVo.getTrainingTimeQuantity() ? String.format("%.2f", agreementExcelVo.getTrainingTimeQuantity()) : "");
            map.put("培训开始日期", null != agreementExcelVo.getBeginDate() ? DateUtil.localDateToStr(agreementExcelVo.getBeginDate()) : "");
            map.put("培训结束日期", null != agreementExcelVo.getEndDate() ? DateUtil.localDateToStr(agreementExcelVo.getEndDate()) : "");
            map.put("培训费用预算(/元)", null != agreementExcelVo.getPlanMoney() ? String.format("%.2f", agreementExcelVo.getPlanMoney()) : "");
            map.put("实际费用(/元)", null != agreementExcelVo.getTrainingMoney() ? String.format("%.2f", agreementExcelVo.getTrainingMoney()) : "");
            map.put("姓名", agreementExcelVo.getName());
            map.put("工牌号", agreementExcelVo.getWorkCard());
            map.put("身份证号", agreementExcelVo.getIdNumber());
            map.put("服务期年限(/年)", null != agreementExcelVo.getServiceYear() ? String.format("%.2f", agreementExcelVo.getServiceYear()) : "");
            map.put("服务期开始时间", null != agreementExcelVo.getServiceBeginDate() ? DateUtil.localDateToStr(agreementExcelVo.getServiceBeginDate()) : "");
            map.put("服务期结束时间", null != agreementExcelVo.getServiceEndDate() ? DateUtil.localDateToStr(agreementExcelVo.getServiceEndDate()) : "");
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<AgreementExcelVo> listAgreementForExcelVo(AgreementExcelVo agreementExcelVo) {
        return tdPlanResultDao.listAgreementForExcel(agreementExcelVo);
    }

    @Override
    public List<DeptPlanExcelVo> getDeptPlanExcelVoList(DeptPlanExcelVo deptPlanExcelVo) {
        return tdPlanResultDao.listDeptPlanExcelVo(deptPlanExcelVo);
    }

    @Override
    public List<PmPlanExcelVo> listEmpByPlanExcelVo(PmPlanExcelVo pmPlanExcelVo) {
        return tdPlanResultDao.listEmpByPlanExcelVo(pmPlanExcelVo);
    }

    @Override
    public List<PmPlanHistoryVo> listPmPlanHistoryExcelVo(PmPlanHistoryVo pmPlanHistoryVo) {
        return tdPlanResultDao.listEmpPlanHistory(pmPlanHistoryVo);
    }

    @Override
    public List<PlanChildResultVo> listForChild(Long employeeId) {
        return tdPlanResultDao.listForChild(employeeId);
    }
}
