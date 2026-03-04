package com.sunten.hrms.ac.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcAttendanceRecordHistoryDao;
import com.sunten.hrms.ac.dao.AcEmpDeptsDao;
import com.sunten.hrms.ac.dao.AcLeaveApplicationDao;
import com.sunten.hrms.ac.dao.AcVacateDao;
import com.sunten.hrms.ac.domain.*;
import com.sunten.hrms.ac.dto.AcAbnormalClockRecordQueryCriteria;
import com.sunten.hrms.ac.dto.AcAttendanceRecordHistoryDTO;
import com.sunten.hrms.ac.dto.AcAttendanceRecordHistoryQueryCriteria;
import com.sunten.hrms.ac.dto.AcVacateQueryCriteria;
import com.sunten.hrms.ac.mapper.AcAttendanceRecordHistoryMapper;
import com.sunten.hrms.ac.service.AcAbnormalClockRecordService;
import com.sunten.hrms.ac.service.AcAttendanceRecordHistoryService;
import com.sunten.hrms.ac.vo.AcExceptionEmailVo;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndAuthorizationDao;
import com.sunten.hrms.fnd.dao.FndDeptDao;
import com.sunten.hrms.fnd.domain.FndAuthorization;
import com.sunten.hrms.fnd.domain.FndAuthorizationDts;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.dto.FndAuthorizationQueryCriteria;
import com.sunten.hrms.fnd.dto.FndDeptQueryCriteria;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.fnd.service.impl.FndDeptServiceImpl;
import com.sunten.hrms.fnd.service.impl.FndDeptSnapshotServiceImpl;
import com.sunten.hrms.fnd.vo.DeptEmp;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.service.PmEmployeeService;
import com.sunten.hrms.pm.service.impl.PmEmployeeServiceImpl;
import com.sunten.hrms.tool.dao.ToolEmailInterfaceDao;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.domain.ToolEmailServer;
import com.sunten.hrms.tool.dto.ToolEmailServerQueryCriteria;
import com.sunten.hrms.tool.service.ToolEmailInterfaceService;
import com.sunten.hrms.utils.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 考勤处理记录历史表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcAttendanceRecordHistoryServiceImpl extends ServiceImpl<AcAttendanceRecordHistoryDao, AcAttendanceRecordHistory> implements AcAttendanceRecordHistoryService {
    private final AcAttendanceRecordHistoryDao acAttendanceRecordHistoryDao;
    private final AcAttendanceRecordHistoryMapper acAttendanceRecordHistoryMapper;
    private final AcVacateDao acVacateDao;
    private final FndUserService fndUserService;
    private final FndDeptServiceImpl fndDeptServiceImpl;
    private final AcEmployeeAttendanceServiceImpl acEmployeeAttendanceServiceImpl;
    private final ToolEmailInterfaceService toolEmailInterfaceService;
    private final AcAbnormalClockRecordServiceImpl acAbnormalClockRecordServiceImpl;
    private final FndAuthorizationDao fndAuthorizationDao;
    private final FndDeptDao fndDeptDao;
    @Value("${role.authDocumenter}")
    private String authDocumenter;
    @Value("${sunten.system-name}")
    private String systemName;
    @Value("${sunten.hrmsVueIp}")
    private String hrmsVueIp;
    @Value("${sunten.hrmsVuePort}")
    private String hrmsVuePort;
    @Autowired
    private AcLeaveApplicationDao acLeaveApplicationDao;
    private static final Logger logger = LoggerFactory.getLogger(AcAttendanceRecordHistoryServiceImpl.class);


    public AcAttendanceRecordHistoryServiceImpl(AcAttendanceRecordHistoryDao acAttendanceRecordHistoryDao,
                                                AcAttendanceRecordHistoryMapper acAttendanceRecordHistoryMapper,
                                                AcVacateDao acVacateDao, FndUserService fndUserService
                                                , FndDeptServiceImpl fndDeptServiceImpl,
                                                AcEmployeeAttendanceServiceImpl acEmployeeAttendanceServiceImpl, ToolEmailInterfaceService toolEmailInterfaceService,
                                                AcAbnormalClockRecordServiceImpl acAbnormalClockRecordServiceImpl,
                                                FndAuthorizationDao fndAuthorizationDao,
                                                FndDeptDao fndDeptDao
                                                ) {
        this.acAttendanceRecordHistoryDao = acAttendanceRecordHistoryDao;
        this.acAttendanceRecordHistoryMapper = acAttendanceRecordHistoryMapper;
        this.acVacateDao = acVacateDao;
        this.fndUserService = fndUserService;
        this.fndDeptServiceImpl = fndDeptServiceImpl;
        this.acEmployeeAttendanceServiceImpl = acEmployeeAttendanceServiceImpl;
        this.toolEmailInterfaceService = toolEmailInterfaceService;
        this.acAbnormalClockRecordServiceImpl = acAbnormalClockRecordServiceImpl;
        this.fndAuthorizationDao = fndAuthorizationDao;
        this.fndDeptDao = fndDeptDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcAttendanceRecordHistoryDTO insert(AcAttendanceRecordHistory attendanceRecordHistoryNew) {
        acAttendanceRecordHistoryDao.insertAllColumn(attendanceRecordHistoryNew);
        return acAttendanceRecordHistoryMapper.toDto(attendanceRecordHistoryNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AcAttendanceRecordHistory attendanceRecordHistory = new AcAttendanceRecordHistory();
        attendanceRecordHistory.setId(id);
        this.delete(attendanceRecordHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcAttendanceRecordHistory attendanceRecordHistory) {
        // TODO    确认删除前是否需要做检查
        acAttendanceRecordHistoryDao.deleteByEntityKey(attendanceRecordHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcAttendanceRecordHistory attendanceRecordHistoryNew) {
        AcAttendanceRecordHistory attendanceRecordHistoryInDb = Optional.ofNullable(acAttendanceRecordHistoryDao.getByKey(attendanceRecordHistoryNew.getId())).orElseGet(AcAttendanceRecordHistory::new);
        ValidationUtil.isNull(attendanceRecordHistoryInDb.getId(), "AttendanceRecordHistory", "id", attendanceRecordHistoryNew.getId());
        attendanceRecordHistoryNew.setId(attendanceRecordHistoryInDb.getId());
        acAttendanceRecordHistoryDao.updateAllColumnByKey(attendanceRecordHistoryNew);
    }

    @Override
    public AcAttendanceRecordHistoryDTO getByKey(Long id) {
        AcAttendanceRecordHistory attendanceRecordHistory = Optional.ofNullable(acAttendanceRecordHistoryDao.getByKey(id)).orElseGet(AcAttendanceRecordHistory::new);
        ValidationUtil.isNull(attendanceRecordHistory.getId(), "AttendanceRecordHistory", "id", id);
        return acAttendanceRecordHistoryMapper.toDto(attendanceRecordHistory);
    }

    @Override
    // 考勤异常处理不分页
    public List<AcAttendanceRecordHistoryDTO> listAll(AcAttendanceRecordHistoryQueryCriteria criteria) {
        return acAttendanceRecordHistoryMapper.toDto(acAttendanceRecordHistoryDao.listAllByCriteria(criteria));
    }

    @Override
    // 考勤异常处理分页
    public Map<String, Object> listAll(AcAttendanceRecordHistoryQueryCriteria criteria, Pageable pageable) {
        Page<AcAttendanceRecordHistory> page = PageUtil.startPage(pageable);
        List<AcAttendanceRecordHistory> attendanceRecordHistorys = acAttendanceRecordHistoryDao.listAllByCriteriaPage(page, criteria);
        List<AcAttendanceRecordHistoryDTO> acAttendanceRecordHistoryDTOS = acAttendanceRecordHistoryMapper.toDto(attendanceRecordHistorys);
        return PageUtil.toPage(acAttendanceRecordHistoryDTOS, page.getTotal());
    }


    @Override
    public void download(List<AcAttendanceRecordHistoryDTO> attendanceRecordHistoryDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcAttendanceRecordHistoryDTO attendanceRecordHistoryDTO : attendanceRecordHistoryDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("异常考勤执行记录id", attendanceRecordHistoryDTO.getAbnromalAttendanceRecordId());
            map.put("员工id", attendanceRecordHistoryDTO.getEmployeeId());
            map.put("日期", attendanceRecordHistoryDTO.getDate());
            map.put("上一段的结束时间（空意味上一天是休息日，非空意味上一天非休息日，时间为上一天的最后一段的结束时间）", attendanceRecordHistoryDTO.getLastParagraphEndTime());
            map.put("一班时间开始", attendanceRecordHistoryDTO.getFirstTimeFrom());
            map.put("一班时间结束", attendanceRecordHistoryDTO.getFirstTimeTo());
            map.put("一班是否跨日", attendanceRecordHistoryDTO.getExtend1TimeFlag());
            map.put("二班时间开始", attendanceRecordHistoryDTO.getSecondTimeFrom());
            map.put("二班时间结束", attendanceRecordHistoryDTO.getSecondTimeTo());
            map.put("二班是否跨日", attendanceRecordHistoryDTO.getExtend2TimeFlag());
            map.put("三班时间开始", attendanceRecordHistoryDTO.getThirdTimeFrom());
            map.put("三班时间结束", attendanceRecordHistoryDTO.getThirdTimeTo());
            map.put("三班是否跨日", attendanceRecordHistoryDTO.getExtend3TimeFlag());
            map.put("下一段的开始时间（空意味着下一天没排班或者位休息日，非空下一天为工作日，时间为下一天的第一段的开始时间）", attendanceRecordHistoryDTO.getNextPeriodStartTime());
            map.put("是否休息", attendanceRecordHistoryDTO.getRestFlag());
            map.put("是否无排班（异常标记）", attendanceRecordHistoryDTO.getNoScheduling());
            map.put("是否休息日打卡（异常标记）", attendanceRecordHistoryDTO.getRestDayClock());
            map.put("是否全天未打卡（异常标记）", attendanceRecordHistoryDTO.getAllDayNoClock());
            map.put("是否上班时间打卡异常（异常标记）", attendanceRecordHistoryDTO.getOfficeTimeClock());
            map.put("是否上班未打卡（异常标记）", attendanceRecordHistoryDTO.getNoClockIn());
            map.put("是否上下班打卡次数异常（异常标记）", attendanceRecordHistoryDTO.getAbnormalClockInTime());
            map.put("是否下班未打卡异常（异常标记）", attendanceRecordHistoryDTO.getNoClockAfterWork());
            map.put("一班时间开始修正", attendanceRecordHistoryDTO.getAltFirstTimeFrom());
            map.put("一班时间结束修正", attendanceRecordHistoryDTO.getAltFirstTimeTo());
            map.put("一班是否跨日修正", attendanceRecordHistoryDTO.getAltExtend1TimeFlag());
            map.put("二班时间开始修正", attendanceRecordHistoryDTO.getAltSecondTimeFrom());
            map.put("二班时间结束修正", attendanceRecordHistoryDTO.getAltSecondTimeTo());
            map.put("二班是否跨日修正", attendanceRecordHistoryDTO.getAltExtend2TimeFlag());
            map.put("三班时间开始修正", attendanceRecordHistoryDTO.getAltThirdTimeFrom());
            map.put("三班时间结束修正", attendanceRecordHistoryDTO.getAltThirdTimeTo());
            map.put("三班是否跨日修正", attendanceRecordHistoryDTO.getAltExtend3TimeFlag());
            map.put("是否休息修正", attendanceRecordHistoryDTO.getAltRestDayFlag());
            map.put("弹性域1", attendanceRecordHistoryDTO.getAttribute1());
            map.put("弹性域2", attendanceRecordHistoryDTO.getAttribute2());
            map.put("弹性域3", attendanceRecordHistoryDTO.getAttribute3());
            map.put("弹性域4", attendanceRecordHistoryDTO.getAttribute4());
            map.put("弹性域5", attendanceRecordHistoryDTO.getAttribute5());
            map.put("id", attendanceRecordHistoryDTO.getId());
            map.put("updateBy", attendanceRecordHistoryDTO.getUpdateBy());
            map.put("createBy", attendanceRecordHistoryDTO.getCreateBy());
            map.put("updateTime", attendanceRecordHistoryDTO.getUpdateTime());
            map.put("createTime", attendanceRecordHistoryDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     *  @author：liangjw
     *  @Date: 2021/7/22 10:07
     *  @Description: 管理员导出考勤
     */
    @Override
    public void downloadAcAdmin(List<AcAttendanceRecordHistoryDTO> attendanceRecordHistoryDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter fmt1 = DateTimeFormatter.ofPattern("HH:mm:ss");
        for (AcAttendanceRecordHistoryDTO attendanceRecordHistoryDTO : attendanceRecordHistoryDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            if (attendanceRecordHistoryDTO.getCodeStatus() != null) {
                switch (attendanceRecordHistoryDTO.getCodeStatus()) {
                    case "person": map.put("处理状态", "未处理");
                    case "documentor": map.put("处理状态", "复核中");
                    case "leader":
                        if (attendanceRecordHistoryDTO.getCommitFlag()) {
                            map.put("处理状态", "领导审批中");
                        } else {
                            map.put("处理状态", "待提交");
                        }
                    case "acAdmin": map.put("处理状态", "专员审核中");
                    case "leaderRepulseToPerson": map.put("处理状态", "领导打回");
                    case "leaderRepulse": map.put("处理状态", "领导打回");
                    case "acAdminRepulse": map.put("处理状态", "专员打回");
                    case "close": map.put("处理状态", "已结束");
                    break;
                }
            } else {
                map.put("处理状态", "未处理");
            }
            map.put("工牌号", attendanceRecordHistoryDTO.getEmployee().getWorkCard());
            map.put("姓名", attendanceRecordHistoryDTO.getEmployee().getName());
            StringBuilder time = new StringBuilder("");
            if (attendanceRecordHistoryDTO.getAltFirstTimeFrom() != null) {
                time.append(attendanceRecordHistoryDTO.getAltFirstTimeFrom().format(fmt1), 0, 5)
                .append("-")
                .append(attendanceRecordHistoryDTO.getAltFirstTimeTo().format(fmt1), 0, 5);
            }
            if (attendanceRecordHistoryDTO.getAltSecondTimeFrom() != null) {
                time.append(",").append(attendanceRecordHistoryDTO.getAltSecondTimeFrom().format(fmt1), 0, 5)
                .append("-")
                .append(attendanceRecordHistoryDTO.getAltSecondTimeTo().format(fmt1), 0, 5);
            }
            if (attendanceRecordHistoryDTO.getAltThirdTimeFrom() != null) {
                time.append(",").append(attendanceRecordHistoryDTO.getAltThirdTimeFrom().format(fmt1), 0, 5)
                        .append("-")
                        .append(attendanceRecordHistoryDTO.getAltThirdTimeTo().format(fmt1), 0, 5);
            }
            map.put("排班时间", time.toString());
            List<String> strList = new ArrayList<>();
            if (attendanceRecordHistoryDTO.getNoScheduling()) {
                strList.add("无排班");
            }
            if (attendanceRecordHistoryDTO.getAllDayNoClock()) {
                strList.add("全天未打卡");
            }
            if (attendanceRecordHistoryDTO.getOfficeTimeClock()) {
                strList.add("上班期间打卡");
            }
            if (attendanceRecordHistoryDTO.getNoClockIn()) {
                strList.add("上班未打卡");
            }
            if (attendanceRecordHistoryDTO.getNoClockAfterWork()) {
                strList.add("下班未打卡");
            }
            if (attendanceRecordHistoryDTO.getAbnormalClockInTime()) {
                strList.add("上下班打卡次数异常");
            }
            String str = StringUtils.join(strList, ",");
            map.put("异常类型", str);
            map.put("异常所属日期", DateUtil.localDateToStr(attendanceRecordHistoryDTO.getDate()));
            map.put("打卡时间", null != attendanceRecordHistoryDTO.getDayClockTime() ? attendanceRecordHistoryDTO.getDayClockTime() : "");
            map.put("处理意见",  null != attendanceRecordHistoryDTO.getTargetDescribes() ? attendanceRecordHistoryDTO.getTargetDescribes() : "");
            map.put("处理意见原因", null != attendanceRecordHistoryDTO.getTargetResult() ? attendanceRecordHistoryDTO.getTargetResult() : "");
            map.put("OA单号", null != attendanceRecordHistoryDTO.getReqCode() ? attendanceRecordHistoryDTO.getReqCode() : "");
            map.put("OA单状态", null != attendanceRecordHistoryDTO.getOaRemark() ? attendanceRecordHistoryDTO.getOaRemark() : "");
//            map.put("异常日期", fmt.format(attendanceRecordHistoryDTO.getDate()));
//            map.put("部门", attendanceRecordHistoryDTO.getEmployee().getDeptName());
//            map.put("科室", attendanceRecordHistoryDTO.getEmployee().getDepartment());
//            map.put("班组", attendanceRecordHistoryDTO.getEmployee().getTeam());
//            map.put("员工", attendanceRecordHistoryDTO.getEmployee().getName());
//            map.put("打卡时间", null != attendanceRecordHistoryDTO.getDayClockTime() ? attendanceRecordHistoryDTO.getDayClockTime() : "");
//            map.put("OA单号", null != attendanceRecordHistoryDTO.getAbClockRecord().getReqCode() ? attendanceRecordHistoryDTO.getAbClockRecord().getReqCode() : "");
//            String stateStr = "";
//            if (null != attendanceRecordHistoryDTO.getAbClockRecord().getState()) {
//                if (attendanceRecordHistoryDTO.getAbClockRecord().getState().equals("pass")) {
//                    stateStr = "审批通过";
//                }
//                if (attendanceRecordHistoryDTO.getAbClockRecord().getState().equals("wait")) {
//                    stateStr = "审批中";
//                }
//                if (attendanceRecordHistoryDTO.getAbClockRecord().getState().equals("notpass")) {
//                    stateStr = "未通过";
//                }
//            }
//            map.put("OA单状态", stateStr);
//
//            String lastCol = "";
//            if (null != attendanceRecordHistoryDTO.getAbClockRecord().getCheckFlag()) {
//                if ( attendanceRecordHistoryDTO.getAbClockRecord().getCheckFlag()) {
//                    lastCol = "已处理";
//                } else if (attendanceRecordHistoryDTO.getAbClockRecord().getDisposeFlag()) {
//                    lastCol = "处理中";
//                } else {
//                    lastCol = "未处理";
//                }
//            }
//            map.put("异常状态",lastCol );
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     *  @author：liangjw
     *  @Date: 2021/7/22 10:07
     *  @Description: 批量处理异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bartchDispose(List<AcAttendanceRecordHistory> acAttendanceRecordHistories) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        for (AcAttendanceRecordHistory acAttendanceRecordHistory : acAttendanceRecordHistories
             ) {
            acAttendanceRecordHistory.setDisposeBy(user.getEmployee().getId());
            acAttendanceRecordHistory.setDisposeTime(LocalDateTime.now());
            if (null != acAttendanceRecordHistory.getReqCode()) {
                // 2024-06-03 从OA读取变成从HR读取
//                AcVacate acVacate = acVacateDao.getAcVacateByRequisitionCode(acAttendanceRecordHistory.getReqCode());
                AcVacate acVacate = acLeaveApplicationDao.getAcVacateByRequisitionCode(acAttendanceRecordHistory.getReqCode());
                if (null != acVacate && acVacate.getState().equals("Pass")) {
                    acAttendanceRecordHistory.setState(acVacate.getState());
                    acAttendanceRecordHistory.setDisposeFlag("pass");
                } else {
                    acAttendanceRecordHistory.setDisposeFlag("wait");
                }
            }
            acAttendanceRecordHistory.setUpdateBy(user.getId());
            acAttendanceRecordHistory.setUpdateTime(LocalDateTime.now());
            acAttendanceRecordHistoryDao.inUsedUpdateByBatchDisposed(acAttendanceRecordHistory);

//            for (AcAbnormalClockRecord abnormalClockRecord : acAttendanceRecordHistory.getClockRecords()
//                 ) {
//                abnormalClockRecord.setDisposeBy(user.getEmployee().getId());
//                abnormalClockRecord.setDisposeTime(LocalDateTime.now());
//                if (null != abnormalClockRecord.getReqCode()) {
//                    AcVacate acVacate = acVacateDao.getAcVacateByRequisitionCode(abnormalClockRecord.getReqCode());
//                    if (null != acVacate && acVacate.getState().equals("Pass")) {
//                        abnormalClockRecord.setState(acVacate.getState());
//                        abnormalClockRecord.setCheckFlag(true);
//                    } else {
//                        abnormalClockRecord.setCheckFlag(false);
//                        flag = false;
//                    }
//                } else {
//                    abnormalClockRecord.setCheckFlag(true);
//                }
//                acAbnormalClockRecordService.update(abnormalClockRecord);
//            }
//            if(flag){
//                acAttendanceRecordHistory.setDisposeFlag("pass");
//            }else{
//                acAttendanceRecordHistory.setDisposeFlag("wait");
//            }
//            acAttendanceRecordHistoryDao.updateDisposeFlagByKey(acAttendanceRecordHistory);
        }
    }

    @Override
    // 考勤异常查询不分页(旧的)
    public List<AcAttendanceRecordHistoryDTO> listForBatch(AcAttendanceRecordHistoryQueryCriteria criteria) {
        return acAttendanceRecordHistoryMapper.toDto(acAttendanceRecordHistoryDao.listForBatch(criteria));
    }

    @Override
    // 考勤异常查询分页（旧的）
    @Transactional(propagation = Propagation.NEVER, readOnly = true, rollbackFor = Exception.class)
    public Map<String, Object> listForBatchByPage(Pageable pageable, AcAttendanceRecordHistoryQueryCriteria criteria) {
        Page<AcAttendanceRecordHistory> page = PageUtil.startPage(pageable);
        List<AcAttendanceRecordHistory> attendanceRecordHistorys = acAttendanceRecordHistoryDao.listForBatchByPage(page, criteria);
        List<AcAttendanceRecordHistoryDTO> acAttendanceRecordHistoryDTOS = acAttendanceRecordHistoryMapper.toDto(attendanceRecordHistorys);
        return PageUtil.toPage(acAttendanceRecordHistoryDTOS, page.getTotal());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCommit(AcAttendanceRecordHistoryQueryCriteria criteria, List<AcEmpDepts> acEmpDepts, FndUserDTO user) {
        LocalDate lastMonth = LocalDate.now().minusMonths(1L);
        int month = lastMonth.getMonthValue();
        int year = lastMonth.getYear();
        if (acAttendanceRecordHistoryDao.inUsedListForBatch(criteria).size() > 0) {
            acAttendanceRecordHistoryDao.inUsedUpdateBatchCommit(criteria);
            ToolEmailInterface toolEmailInterface = new ToolEmailInterface();
            // 存储提交部门的审批人以及提交部门
            List<DeptEmp> deptEmpList = new ArrayList<>();
            // 调用邮件发送, 按照邮箱进行分组
            for (AcEmpDepts ac : acEmpDepts
            )
            {
                // 根据deptId获取对需要发送的邮件对象
                // 改了限制部门
                DeptEmp deptEmp = JSONObject.parseObject(JSONObject.toJSONString(fndDeptServiceImpl.getPmByAdminJobId(ac.getDeptId())), DeptEmp.class);
                deptEmp.setDeptName(ac.getDeptName());
                deptEmp.setDeptId(ac.getDeptId());
                deptEmp.setEmail(deptEmp.getFndUser().getEmail());
                deptEmp.setEmployeeId(deptEmp.getEmployee().getId());
                deptEmpList.add(deptEmp);
            }
            if (deptEmpList.size() > 0) {
                // 非授权邮件发送
                Map<String, List<DeptEmp>> emailMap = new HashMap<>();
                deptEmpList.stream().collect(Collectors.groupingBy(DeptEmp::getEmail, Collectors.toList())).forEach(emailMap::put);

                // 授权分组
                Map<Long, List<DeptEmp>> authorizationsMap = new HashMap<>();
                deptEmpList.stream().collect(Collectors.groupingBy(DeptEmp::getEmployeeId, Collectors.toList())).forEach(authorizationsMap::put);

                acEmployeeAttendanceServiceImpl.getServer(toolEmailInterface);
                TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
                Template template = engine.getTemplate("email/batchCommit.ftl");
                emailMap.forEach((x, y) -> {
                    logger.debug("非授权邮件发送：" + y);
                    setBatchCommitEmailParam(lastMonth, month, year, toolEmailInterface, template, x, y.stream().map(DeptEmp::getDeptName).collect(Collectors.toList()));
                });

                FndAuthorizationQueryCriteria fndAuthorizationQueryCriteria = new FndAuthorizationQueryCriteria();
                fndAuthorizationQueryCriteria.setEnableFlag(true);
                fndAuthorizationQueryCriteria.setAuthorizationType("ac");
                authorizationsMap.forEach((x, y) -> {
                    fndAuthorizationQueryCriteria.setByEmployeeId(x);
                    List<FndAuthorization> fndAuthorizations = fndAuthorizationDao.listAllByCriteriaWithChild(fndAuthorizationQueryCriteria);
                    logger.debug("人员分组权限===================" + fndAuthorizations);
                    logger.debug("授权人员id===============" + x);
                    for (FndAuthorization fndAuthorization : fndAuthorizations
                         ) {
                        // 判定存在交集的部门
                        FndDeptQueryCriteria fndDeptQueryCriteria = new FndDeptQueryCriteria();
                        fndDeptQueryCriteria.setDeleted(false);
                        fndDeptQueryCriteria.setEnabled(true);
                        fndDeptQueryCriteria.setIds(y.stream().map(DeptEmp::getDeptId).collect(Collectors.toSet()));
                        List<FndDept> fndDepts = fndDeptDao.listAllByCriteria(fndDeptQueryCriteria);
                        logger.debug("提交的部门id集合===========" + fndDepts);
                        List<FndDept> authDepts = fndAuthorization.getFndAuthorizationDts().stream().map(FndAuthorizationDts::getFndDept).collect(Collectors.toList());
                        logger.debug("查询出来的授权范围集合 ========" + authDepts);
                        // 取交集
                        List<FndDept> intersection = fndDepts.stream().filter(item -> authDepts.stream().map(FndDept::getId)
                                .collect(Collectors.toList()).contains(item.getId()))
                                .collect(Collectors.toList());
                        logger.debug("交集内容=============" + intersection.stream().map(FndDept::getDeptName).collect(Collectors.toList()));
                        if (intersection.size() > 0) {
                            // 存在交集, 则需要发送了,根据id拿邮箱, 并发送交集作为内容
                            setBatchCommitEmailParam(lastMonth, month, year, toolEmailInterface, template, fndUserService.getEmailByEmployeeId(fndAuthorization.getAuthorizationTo()), intersection.stream().map(FndDept::getDeptName).collect(Collectors.toList()));
                        }
                    }
                });
            }
        } else {
            throw new InfoCheckWarningMessException("上月异常已全部提交，无需再提交");
        }
    }

    private void setBatchCommitEmailParam(LocalDate lastMonth, int month, int year, ToolEmailInterface toolEmailInterface, Template template, String email, List<String> intersection) {
        String hrmsVueUrl = "http://" + hrmsVueIp + ":" + hrmsVuePort;
        toolEmailInterface.setSendTo(email);
        toolEmailInterface.setMailSubject( year + "年" + month + "月考勤异常审批通知 - " + systemName );
        toolEmailInterface.setPlannedDate( LocalDateTime.now());
        toolEmailInterface.setStatus("PLAN");
        List<List<String>> row = new ArrayList<>();
        List<String> col = new ArrayList<>();
        for (int i = 0; i < intersection.size(); i++) {
            if (i % 4 == 0 && i != 0) {
                row.add(col);
                col = new ArrayList<>();
            }
            col.add(intersection.get(i));
        }
        if (col.size() > 0) {
            row.add(col);
        }
        Dict dict = Dict.create();
        dict.set("deptNameList", row);
        dict.set("url", "系统主页链接：<a href='" + hrmsVueUrl + "' target='_blank'>" + hrmsVueUrl + "</a>");
        toolEmailInterface.setMailContent(template.render(dict));
        toolEmailInterfaceService.insert(toolEmailInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendEmailAfterGenerateAbnormalAttendance() {
        String hrmsVueUrl = "http://" + hrmsVueIp + ":" + hrmsVuePort;
        List<AcExceptionEmailVo> emailList = acAttendanceRecordHistoryDao.listEmailTargetByLastMonthException();
        if (emailList.size() > 0) {
            ToolEmailInterface toolEmailInterface = new ToolEmailInterface();
            acEmployeeAttendanceServiceImpl.getServer(toolEmailInterface);
            toolEmailInterface.setPlannedDate( LocalDateTime.now());
            toolEmailInterface.setStatus("PLAN");
            toolEmailInterface.setMailSubject( emailList.get(0).getYearAndMonth() + "个人考勤异常处理通知 - " + systemName );
            Dict dict = Dict.create();
            dict.set("month", emailList.get(0).getYearAndMonth());
            dict.set("url", "系统主页链接：<a href='" + hrmsVueUrl + "' target='_blank'>" + hrmsVueUrl + "</a>");
            TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
            Template template = engine.getTemplate("email/personExceptionEmail.ftl");
            for ( AcExceptionEmailVo acExceptionEmailVo : emailList
            ) {
                toolEmailInterface.setSendTo(acExceptionEmailVo.getEmail());
                toolEmailInterface.setMailContent(template.render(dict));
                toolEmailInterfaceService.insert(toolEmailInterface);
            }
        }
    }

    @Override
    public List<AcAttendanceRecordHistoryDTO> inUsedListForBatch(AcAttendanceRecordHistoryQueryCriteria criteria) {
        return acAttendanceRecordHistoryMapper.toDto(acAttendanceRecordHistoryDao.inUsedListForBatch(criteria));
    }

    @Override
    public Map<String, Object> inUsedListForBatchByPage(Pageable pageable, AcAttendanceRecordHistoryQueryCriteria criteria) {
        Page<AcAttendanceRecordHistory> page = PageUtil.startPage(pageable);
        List<AcAttendanceRecordHistory> attendanceRecordHistorys = acAttendanceRecordHistoryDao.inUsedListForBatchByPage(page, criteria);
        List<AcAttendanceRecordHistoryDTO> acAttendanceRecordHistoryDTOS = acAttendanceRecordHistoryMapper.toDto(attendanceRecordHistorys);
        return PageUtil.toPage(acAttendanceRecordHistoryDTOS, page.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disposeRecordHistory(List<AcAttendanceRecordHistory> acAttendanceRecordHistories) {
        if(acAttendanceRecordHistories.size() > 0){
            FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
//            List<Long> docReturnAccrIds = new ArrayList<>();
//            List<Long> personReturnAccrIds = new ArrayList<>();
            List<Long> docReturnAarhIds = new ArrayList<>();
            List<Long> personReturnAarhIds = new ArrayList<>();
            for(AcAttendanceRecordHistory acAttendanceRecordHistory : acAttendanceRecordHistories){
                if (acAttendanceRecordHistory.getCodeStatus().equals("documentor")) {
                    // 个人处理或班组长处理
                    acAttendanceRecordHistory.setSelfDisposeTime(LocalDateTime.now());
                } else if (acAttendanceRecordHistory.getCodeStatus().equals("leader")) {
                    // 资料员处理
                    if (null != user.getEmployee()) { // 有员工id用员工id，没有则用userid
                        acAttendanceRecordHistory.setDisposeBy(user.getEmployee().getId());
                    } else {
                        acAttendanceRecordHistory.setDisposeBy(user.getId());
                    }
                    acAttendanceRecordHistory.setDisposeTime(LocalDateTime.now());
                } else if (acAttendanceRecordHistory.getCodeStatus().equals("leaderRepulse") || acAttendanceRecordHistory.getCodeStatus().equals("acAdmin")) {
                    // 领导打回或领导通过
                    if (null != user.getEmployee()) {
                        acAttendanceRecordHistory.setLeaderDisposeBy(user.getEmployee().getId());
                    } else {
                        acAttendanceRecordHistory.setLeaderDisposeBy(user.getId());
                    }
                    acAttendanceRecordHistory.setLeaderDisposeTime(LocalDateTime.now());
                } else if (acAttendanceRecordHistory.getCodeStatus().equals("close") || acAttendanceRecordHistory.getCodeStatus().equals("acAdminRepulse")) {
                    // 考勤管理员打回或考勤管理员关闭
                    acAttendanceRecordHistory.setAcAdminDisposeTime(LocalDateTime.now());
                }

                if ((acAttendanceRecordHistory.getCodeStatus().equals("acAdminRepulse") || acAttendanceRecordHistory.getCodeStatus().equals("leaderRepulse")) && !acAttendanceRecordHistory.getRecordIsChargeFlag()) {
                    //  发给资料员的邮件集合
                    docReturnAarhIds.add(acAttendanceRecordHistory.getId());
                }
                if (acAttendanceRecordHistory.getCodeStatus().equals("leaderRepulse")  && acAttendanceRecordHistory.getRecordIsChargeFlag()) {
                    // 发给个人的邮件集合
                    personReturnAarhIds.add(acAttendanceRecordHistory.getId());
                }
                if (null != user.getEmployee()) { // 有员工id用员工id，没有则用userId
                    acAttendanceRecordHistory.setUpdateBy(user.getEmployee().getId());
                } else {
                    acAttendanceRecordHistory.setUpdateBy(user.getId());
                }
                acAttendanceRecordHistory.setUpdateTime(LocalDateTime.now());
                acAttendanceRecordHistoryDao.inUsedUpdateDisposeByColumn(acAttendanceRecordHistory);
            }
            if (docReturnAarhIds.size() > 0) {
                // 获取邮件服务
                acAbnormalClockRecordServiceImpl.sendAcRepulseEmail(docReturnAarhIds, "DOC");
            }
            if (personReturnAarhIds.size() > 0) {
                acAbnormalClockRecordServiceImpl.sendAcRepulseEmail(personReturnAarhIds, "PERSON");
            }
        }else{
            throw new InfoCheckWarningMessException("打卡时间为空！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoUpdateOaCode() {
        /**
         * 1、获取所有状态为审批中的异常记录
         * 2、根据OA单号获取OA审批信息
         * 3、判断OA审批单是否审批完毕，
         * 4、修改异常记录OA单状态
         */
        List<AcAttendanceRecordHistory> acAttendanceRecordHistories = acAttendanceRecordHistoryDao.listByWaitState();
        if (acAttendanceRecordHistories.size() > 0) {
            LocalDateTime nowDate = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String nowDateStr = formatter.format(nowDate);

            AcVacateQueryCriteria criteria = new AcVacateQueryCriteria();
            List<AcAttendanceRecordHistory> realList = new ArrayList<>();
            for(AcAttendanceRecordHistory recordHistory:acAttendanceRecordHistories) {
                criteria.setReqCode(recordHistory.getReqCode());
                // 2024-06-03 修改从OA获取假单变成从HR获取假单
//                List<AcVacate> acVacateList = acVacateDao.getList(criteria);
                List<AcVacate> acVacateList = null;
                if (criteria.getReqCode() != null && !"".equals(criteria.getReqCode())) {
                    acVacateList = acLeaveApplicationDao.getList(criteria.getReqCode());
                } else {
                    acVacateList = acLeaveApplicationDao.getList2();
                }
                // 2021-11-10 新增定时任务执行信息
                recordHistory.setAttribute4("执行日期：" + nowDateStr + "，执行前状态：" + recordHistory.getState());
                if (acVacateList.size() > 0) {
                    AcVacate first = acVacateList.get(0);
                    if("Pass".equals(first.getState())) {
                        recordHistory.setState("pass");
                        realList.add(recordHistory);
                    }
                } else {
                    recordHistory.setState("notpass");
                    realList.add(recordHistory);
                }
            }
            // 存在需要更新状态的记录
            if (realList.size() > 0) {
                for(AcAttendanceRecordHistory acAttendanceRecord:realList) {
                    acAttendanceRecordHistoryDao.updateStateByReqCode(acAttendanceRecord);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoUpdateAttendanceMachineTime() {
        /**
         * 1、获取所有人脸机地址
         * 2、根据人脸机地址获159服务器时间同步到人脸机上
         */
        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        List<AcAttendanceMachine> acAttendanceMachines = acAttendanceRecordHistoryDao.getMachineListAll();
        if (acAttendanceMachines.size() > 0) {
            for(AcAttendanceMachine machineIpAddress:acAttendanceMachines) {
                //定义接收数据
                JSONObject result = new JSONObject();
                //创建json对象
                Map<String, Object> map = new HashMap<String, Object>();
                Map<String, Object> mapinfo = new HashMap<String, Object>();
                map.put("operator", "SetSysTime");
                Calendar calendar= Calendar.getInstance();
                //获取当前年
                int year=calendar.get(Calendar.YEAR);
                //获取当前月
                int month=calendar.get(Calendar.MONTH)+1;
                //获取当前日
                int day=calendar.get(Calendar.DATE);
                //获取当前时
                int hour=calendar.get(Calendar.HOUR_OF_DAY);
                //获取当前分
                int minute=calendar.get(Calendar.MINUTE);
                //获取当前秒
                int second=calendar.get(Calendar.SECOND);
                mapinfo.put("Year",year);
                mapinfo.put("Month",month);
                mapinfo.put("Day",day);
                mapinfo.put("Hour",hour);
                mapinfo.put("Minute",minute);
                mapinfo.put("Second",second);
                map.put("info",mapinfo);
                String json=JSONObject.toJSON(map).toString();
                String machineUrl=machineIpAddress.getMachineIpAddress();
                //创建post方式请求对象
                HttpPost httpPost = new HttpPost("http://"+machineUrl+"/action/SetSysTime");
                //装填参数
                StringEntity s = new StringEntity(json, "utf-8");
//                System.out.println(json.toString());
                s.setContentEncoding("UTF-8");
                s.setContentType("application/json");
                //设置参数到请求对象中
                httpPost.setEntity(s);
//                System.out.println("请求地址："+machineUrl+"/action/SetSysTime");

                //设置header信息
                //指定报文头【Content-type】、【User-Agent】
//              httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                httpPost.setHeader("Authorization", "Basic YWRtaW46U3VudGVuODg=");
                //执行请求操作，并拿到结果（同步阻塞）
                try{
                    HttpResponse response = client.execute(httpPost);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    result.put("error", "连接错误！");
                } catch (IOException e) {
                    e.printStackTrace();
                    result.put("error", "连接错误！");
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoSynchronousAttendanceData(String hh) {
        System.out.println("hh = " + hh);
        int num=Integer.parseInt(hh);
        System.out.println("num = " + num);
//        num=-num;
//        System.out.println("---num = " + num);
        /**
         * 1、获取所有人脸机地址
         * 2、根据人脸机地址获取人脸机当前一天的考勤数据
         * 3、循环写入数据库，KQ_CRJSJ_TEMP
         */
        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        List<AcAttendanceMachine> acAttendanceMachines = acAttendanceRecordHistoryDao.getMachineListAll();
        if (acAttendanceMachines.size() > 0) {
            for(AcAttendanceMachine machineIpAddress:acAttendanceMachines) {
                //定义接收数据
                JSONObject result = new JSONObject();
                //创建json对象
                Map<String, Object> map = new HashMap<String, Object>();
                Map<String, Object> mapinfo = new HashMap<String, Object>();
                map.put("operator", "SearchControl");
//                Calendar calendar= Calendar.getInstance();
                DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");

                Calendar calendar=Calendar.getInstance();
                //取昨天的日期
                calendar.set(Calendar.HOUR_OF_DAY,num);
                String yesterdayDate=dateFormat.format(calendar.getTime());
                mapinfo.put("DeviceID",machineIpAddress.getMachineId());
//                mapinfo.put("BeginTime","2023-02-03T00:00:00");
                mapinfo.put("BeginTime",yesterdayDate+"T00:00:00");
                mapinfo.put("EndTime",yesterdayDate+"T23:59:59");
//                mapinfo.put("EndTime","2023-02-08T23:59:59");
                mapinfo.put("Picture",0);
                mapinfo.put("BeginNO",0);
                mapinfo.put("RequestCount",2000);
                map.put("info",mapinfo);
                String json=JSONObject.toJSON(map).toString();
                String machineUrl=machineIpAddress.getMachineIpAddress();
                //创建post方式请求对象
                HttpPost httpPost = new HttpPost("http://"+machineUrl+"/action/SearchControl");
                //装填参数
                StringEntity s = new StringEntity(json, "utf-8");
                System.out.println(json.toString());
                s.setContentEncoding("UTF-8");
                s.setContentType("application/json");
                //设置参数到请求对象中
                httpPost.setEntity(s);
                System.out.println("请求地址："+machineUrl+"/action/SearchControl");
                //设置header信息
                //指定报文头【Content-type】、【User-Agent】
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                httpPost.setHeader("Authorization", "Basic YWRtaW46U3VudGVuODg=");
                //执行请求操作，并拿到结果（同步阻塞）
                try{
                    HttpResponse response = client.execute(httpPost);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                        JSONObject rt2 = new JSONObject();
                        rt2=JSON.parseObject(result.get("info").toString());
//                        System.out.println("反回数据2："+JSONArray.parseArray(rt2.get("SearchInfo").toString()));
                        JSONArray rt3= JSONArray.parseArray(rt2.get("SearchInfo").toString());
                        int i;
                        for (i = 0; i < rt3.size(); i++) {
                            JSONObject rt4 = (JSONObject) rt3.get(i);// 得到json对象
                            String name=rt4.get("Name").toString();
                            String date8=StringUtils.substringBefore(rt4.get("Time").toString(), "T"); //结果是：he    这里是以第一个“l”为标准
                            String time8=date8+" "+StringUtils.substringAfter(rt4.get("Time").toString(), "T"); //结果为：hello wor   这里以最后一个“l”为标准
                            String crjqk=machineIpAddress.getCrjqk();
                            String machineName=machineIpAddress.getMachineName();
//                            System.out.println("反回数据Name："+name);
//                            System.out.println("反回数据日期："+date8);
//                            System.out.println("反回数据时间："+time8);
//                            System.out.println("反回数据机器号："+crjqk);
//                            System.out.println("反回数据机器名："+machineName);
                            acAttendanceRecordHistoryDao.insertAttendanceData(name,date8,time8,crjqk,machineName);
//                            i=rt3.size();
                        }
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    result.put("error", "连接错误！");
                } catch (IOException e) {
                    e.printStackTrace();
                    result.put("error", "连接错误！");
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoSynchronousAttendanceNowData() {
        /**
         * 1、获取所有人脸机地址
         * 2、根据人脸机地址获取人脸机当前一天的考勤数据
         * 3、循环写入数据库，KQ_CRJSJ_TEMP
         */
        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        List<AcAttendanceMachine> acAttendanceMachines = acAttendanceRecordHistoryDao.getMachineListAll();
        if (acAttendanceMachines.size() > 0) {
            for(AcAttendanceMachine machineIpAddress:acAttendanceMachines) {
                //定义接收数据
                JSONObject result = new JSONObject();
                //创建json对象
                Map<String, Object> map = new HashMap<String, Object>();
                Map<String, Object> mapinfo = new HashMap<String, Object>();
                map.put("operator", "SearchControl");
//                Calendar calendar= Calendar.getInstance();
                DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar=Calendar.getInstance();
                //取当天的日期
                calendar.set(Calendar.HOUR_OF_DAY,-24);
                String yesterdayDate=dateFormat.format(calendar.getTime());
                mapinfo.put("DeviceID",machineIpAddress.getMachineId());
//                mapinfo.put("BeginTime","2023-02-03T00:00:00");
                mapinfo.put("BeginTime",yesterdayDate+"T00:00:00");
                mapinfo.put("EndTime",yesterdayDate+"T23:59:59");
//                mapinfo.put("EndTime","2023-02-08T23:59:59");
                mapinfo.put("Picture",0);
                mapinfo.put("BeginNO",0);
                mapinfo.put("RequestCount",2000);
                map.put("info",mapinfo);
                String json=JSONObject.toJSON(map).toString();
                String machineUrl=machineIpAddress.getMachineIpAddress();
                //创建post方式请求对象
                HttpPost httpPost = new HttpPost("http://"+machineUrl+"/action/SearchControl");
                //装填参数
                StringEntity s = new StringEntity(json, "utf-8");
                System.out.println(json.toString());
                s.setContentEncoding("UTF-8");
                s.setContentType("application/json");
                //设置参数到请求对象中
                httpPost.setEntity(s);
                System.out.println("请求地址："+machineUrl+"/action/SearchControl");
                //设置header信息
                //指定报文头【Content-type】、【User-Agent】
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                httpPost.setHeader("Authorization", "Basic YWRtaW46U3VudGVuODg=");
                //执行请求操作，并拿到结果（同步阻塞）
                try{
                    HttpResponse response = client.execute(httpPost);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                        JSONObject rt2 = new JSONObject();
                        rt2=JSON.parseObject(result.get("info").toString());
//                        System.out.println("反回数据2："+JSONArray.parseArray(rt2.get("SearchInfo").toString()));
                        JSONArray rt3= JSONArray.parseArray(rt2.get("SearchInfo").toString());
                        int i;
                        for (i = 0; i < rt3.size(); i++) {
                            JSONObject rt4 = (JSONObject) rt3.get(i);// 得到json对象
                            String name=rt4.get("Name").toString();
                            String date8=StringUtils.substringBefore(rt4.get("Time").toString(), "T"); //结果是：he    这里是以第一个“l”为标准
                            String time8=date8+" "+StringUtils.substringAfter(rt4.get("Time").toString(), "T"); //结果为：hello wor   这里以最后一个“l”为标准
                            String crjqk=machineIpAddress.getCrjqk();
                            String machineName=machineIpAddress.getMachineName();
//                            System.out.println("反回数据Name："+name);
//                            System.out.println("反回数据日期："+date8);
//                            System.out.println("反回数据时间："+time8);
//                            System.out.println("反回数据机器号："+crjqk);
//                            System.out.println("反回数据机器名："+machineName);
                            acAttendanceRecordHistoryDao.insertAttendanceData(name,date8,time8,crjqk,machineName);
//                            i=rt3.size();
                        }
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    result.put("error", "连接错误！");
                } catch (IOException e) {
                    e.printStackTrace();
                    result.put("error", "连接错误！");
                }
            }
        }
    }
}
