package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcLeaveApplicationDao;
import com.sunten.hrms.ac.dao.AcOaLeaveDao;
import com.sunten.hrms.ac.domain.*;
import com.sunten.hrms.ac.dto.AcLeaveApplicationDTO;
import com.sunten.hrms.ac.dto.AcLeaveApplicationQueryCriteria;
import com.sunten.hrms.ac.dto.PmLeaveApplicationDTO;
import com.sunten.hrms.ac.mapper.AcHrLeaveMapper;
import com.sunten.hrms.ac.mapper.PmLeaveApplicationMapper;
import com.sunten.hrms.ac.service.AcLeaveApplicationService;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dao.PmEmployeeDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.dto.PmEmployeeDTO;
import com.sunten.hrms.pm.mapper.PmEmployeeMapper;
import com.sunten.hrms.pm.service.PmEmployeeService;
import com.sunten.hrms.tool.dao.ToolEmailInterfaceDao;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.domain.ToolEmailServer;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zouyp
 * @since 2023-05-29
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcLeaveApplicationServiceImpl extends ServiceImpl<AcLeaveApplicationDao, AcLeaveApplication> implements AcLeaveApplicationService {
    @Autowired
    private AcLeaveApplicationDao acLeaveApplicationDao;
    @Autowired
    private AcHrLeaveMapper acHrLeaveMapper;
    @Autowired
    private AcOaLeaveDao acOaLeaveDao;
    @Autowired
    private PmLeaveApplicationMapper pmLeaveApplicationMapper;
    @Autowired
    private ToolEmailInterfaceDao toolEmailInterfaceDao;

    @Autowired
    private PmEmployeeDao pmEmployeeDao;

    @Autowired
    private PmEmployeeMapper pmEmployeeMapper;
    private static final int MAX_PARENTAL_LEAVE_AGE = 3;
    FndUserService fndUserService;
    private static final int MAX_PARENTAL_LEAVE_COUNT = 2;

    @Value("${sunten.in-sunten-email-server-id}")
    private Long inSuntenEmailServerId;

    SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
    private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcLeaveApplicationDTO insert(AcLeaveApplication hrLeaveNew) {
        acLeaveApplicationDao.insertAllColumn(hrLeaveNew);
        return acHrLeaveMapper.toDto(hrLeaveNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete() {
        AcLeaveApplication hrLeave = new AcLeaveApplication();
        this.delete(hrLeave);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcLeaveApplication hrLeave) {
        // TODO    确认删除前是否需要做检查
        acLeaveApplicationDao.deleteByEntityKey(hrLeave);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcLeaveApplication hrLeaveNew) {
//        AcLeaveApplication hrLeaveInDb = Optional.ofNullable(acLeaveApplicationDao.getByKey()).orElseGet(AcLeaveApplication::new);
//        acLeaveApplicationDao.updateAllColumnByKey(hrLeaveNew);
    }

    @Override
    public AcLeaveApplicationDTO getByKey() {
//        AcLeaveApplication hrLeave = Optional.ofNullable(acLeaveApplicationDao.getByKey()).orElseGet(AcLeaveApplication::new);
//        return acHrLeaveMapper.toDto(hrLeave);
          return  null;
    }

    @Override
    public List<AcLeaveApplicationDTO> listAll(AcLeaveApplicationQueryCriteria criteria) {
        return acHrLeaveMapper.toDto(acLeaveApplicationDao.listAllByCriteria(criteria));
    }

    @Override
    public List<AcLeaveApplicationDTO> listAll2(AcLeaveApplicationQueryCriteria criteria) {
        return acHrLeaveMapper.toDto(acLeaveApplicationDao.listAllByCriteria2(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcLeaveApplicationQueryCriteria criteria, Pageable pageable) {
        Page<AcLeaveApplication> page = PageUtil.startPage(pageable);
        List<AcLeaveApplication> hrLeaves = acLeaveApplicationDao.listAllByCriteriaPage(page, criteria);
        System.out.println("hrLeaves: ");
        System.out.println(hrLeaves);
        return PageUtil.toPage(acHrLeaveMapper.toDto(hrLeaves), page.getTotal());
    }

    @Override
    public void download(List<AcLeaveApplicationDTO> hrLeaveDTOS, HttpServletResponse response) throws IOException {
//        System.out.println(hrLeaveDTOS);
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcLeaveApplicationDTO hrLeaveDTO : hrLeaveDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
//            map.put("主键ID", hrLeaveDTO.getId());
            map.put("OA申请单号", hrLeaveDTO.getOaOrder());
            map.put("员工姓名", hrLeaveDTO.getNickName());
            map.put("工牌号", hrLeaveDTO.getUserName());
            map.put("部门", hrLeaveDTO.getUserDepartment());
            map.put("科室", hrLeaveDTO.getUserSection());
            map.put("班组", hrLeaveDTO.getGroups());
            map.put("岗位", hrLeaveDTO.getPosition());
            map.put("请假类别",hrLeaveDTO.getLeaveType());
            map.put("申请理由", hrLeaveDTO.getReason());
            map.put("开始时间", hrLeaveDTO.getStartTime());
            map.put("结束时间", hrLeaveDTO.getEndTime());
            map.put("总申请天数", hrLeaveDTO.getTotalNumber());
            map.put("人资校验天数", hrLeaveDTO.getHrTotal());
            map.put("校验理由", hrLeaveDTO.getModifyReason());
            map.put("最终审批结果", hrLeaveDTO.getApprovalResult());
//            map.put("版本号", hrLeaveDTO.getVersion());
//            map.put("删除标志", hrLeaveDTO.getEnabledFlag());
//            map.put("是否上传附件", hrLeaveDTO.getTo_be_uploaded_flag());
//            map.put("attribute1", hrLeaveDTO.getAttribute1());
//            map.put("attribute2", hrLeaveDTO.getAttribute2());
//            map.put("attribute3", hrLeaveDTO.getAttribute3());
//            map.put("attribute4", hrLeaveDTO.getAttribute4());
//            map.put("创建时间", hrLeaveDTO.getCreateTime());
//            map.put("创建人id", hrLeaveDTO.getCreateBy());
//            map.put("修改时间", hrLeaveDTO.getUpdateTime());
//            map.put("修改人id", hrLeaveDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Double getAnnualLeave(String workCard) {
        try {
            // 首先获取HRMS中用户带薪年假总数
//            System.out.println("------工牌号---------" + workCard);
            Double annualLeaveCount = acLeaveApplicationDao.getAnnualLeave(workCard);
//            System.out.println("------剩余年假-----------：" + annualLeaveCount);
            // 取出已经使用的年假
            // Double usedAnnualLeaveDays = acOaLeaveDao.getUsedAnnualLeaveDays(workCard);
            Double usedAnnualLeaveDays = acLeaveApplicationDao.getUsedAnnualLeave(workCard);
//            System.out.println("-----已使用年假-----------：" + usedAnnualLeaveDays);
            // 计算剩余年假天数
            annualLeaveCount = (annualLeaveCount != null && annualLeaveCount > 0) ?
                    (usedAnnualLeaveDays != null ? annualLeaveCount - usedAnnualLeaveDays : annualLeaveCount) :
                    0.00;
            return annualLeaveCount;
        } catch (Exception e) {
            // 处理异常
            System.err.println("获取年假出错：" + e.getMessage());
            return 0.00;
        }
    }
    @Override
    public Double getTeamHourAnnualYear(String workCard) {
        Double annualLeaveCount = acLeaveApplicationDao.getAnnualLeave(workCard);
        return annualLeaveCount;
    }

    @Override
    public Double getTeamUsedHourAnnualYear(String workCard) {
        Double usedAnnualLeaveDays = acLeaveApplicationDao.getUsedAnnualLeave(workCard);
        return usedAnnualLeaveDays;
    }

    @Override
    public void leaveApplicationNotifyHr(String oaOrder, String mailUser) {
        // 获取当前申请单号对应的人
        String userName = acLeaveApplicationDao.getUserNameByOaOrder(oaOrder);
        // 先查询这个人的邮箱地址
        HashMap<String, String> approvalUser = acLeaveApplicationDao.checkMail(mailUser);
        ToolEmailServer emailServer = new ToolEmailServer();
        emailServer.setId(inSuntenEmailServerId);
        String mailContent = null;
        mailContent = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"> <html xmlns=\"http://www.w3.org/1999/xhtml\"> <head>     <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>  \n" +
                "   <style>         @page {             margin: 0;         }     </style> </head> <body style=\"margin: 0px;             padding: 0px;    font: 100% SimSun, Microsoft YaHei, Times New Roman, Verdana, Arial, Helvetica, sans-serif;    \n" +
                "            color: #000;\"> <div style=\"height: auto;    width: 820px;    min-width: 820px;    margin: 0 auto;    margin-top: 20px;             border: 1px solid #eee;\">     <div style=\"padding: 10px;padding-bottom: 0px;\">       \n" +
                "\t\t\t  <p style=\"margin-bottom: 10px;padding-bottom: 0px;\">尊敬的用户，您好：</p> <p style=\"text-indent: 2em;\">" + "请假申请人" + userName + "补充了请假证明附件，请您及时审批！申请单号："  + oaOrder + "</p>";
        LocalDateTime date = LocalDateTime.now();
        ToolEmailInterface toolEmailInterface = new ToolEmailInterface();
        toolEmailInterface.setMailSubject("请假审批通知 - SUNTEN人力资源管理系统");
        toolEmailInterface.setStatus("PLAN");
        toolEmailInterface.setPlannedDate(date);
        toolEmailInterface.setEmailServer(emailServer);
        mailContent = mailContent +"<p style=\"text-align: center;    font-family: Times New Roman;    font-size: small;  \n" +
                "\t\t\t    color: #C60024;    padding: 20px 0px;    margin-bottom: 10px;    font-weight: bold;    background: #ebebeb;\">系统主页链接：<br>\n" +
                "\t\t\t\t点击链接进行跳转<a href='http://172.18.1.159:8016' target='_blank'>http://172.18.1.159:8016</a> </p>      \n" +
                "\t\t\t\t   <div class=\"foot-hr hr\" style=\"margin: 0 auto;    z-index: 111;    width: 800px;    margin-top: 30px;    border-top: 1px solid #DA251D;\">      \n" +
                "\t\t\t\t      </div>         <div style=\"text-align: center;    font-size: 12px;    padding: 20px 0px;    font-family: Microsoft YaHei;\">           \n" +
                "\t\t\t\t\t         Copyright &copy;2023 SUNTEN 人力资源管理系统 All Rights Reserved.         </div>      </div> </div> </body> </html> ";
        toolEmailInterface.setMailContent(mailContent);
        toolEmailInterface.setSendTo(approvalUser.get("email_inside"));
        toolEmailInterfaceDao.insertAllColumn(toolEmailInterface);
    }

    @Override
    public HashMap<String, Object> getStartTimeList(String workCard, String startTime) {
        HashMap<String, Object> res = new HashMap<String, Object>();
        // 先查询这一天有没有排班
        HashMap<String ,Object> attendanceMap = acLeaveApplicationDao.getStartTimeList(workCard, startTime);
        List<Object> startTimeList = new ArrayList<>();
        List<Object> endTimeList = new ArrayList<>();
        if (attendanceMap != null && !attendanceMap.isEmpty()) {
            if (attendanceMap.get("first_time_from") != null) {
                startTimeList.add(attendanceMap.get("first_time_from"));
            }
            if (attendanceMap.get("second_time_from") != null) {
                startTimeList.add(attendanceMap.get("second_time_from"));
            }
            if (attendanceMap.get("third_time_from") != null) {
                startTimeList.add(attendanceMap.get("third_time_from"));
            }
            if (attendanceMap.get("first_time_to") != null) {
                endTimeList.add(attendanceMap.get("first_time_to"));
            }
            if (attendanceMap.get("second_time_to") != null) {
                endTimeList.add(attendanceMap.get("second_time_to"));
            }
            if (attendanceMap.get("third_time_to") != null) {
                endTimeList.add(attendanceMap.get("third_time_to"));
            }
        } else {
            // 这里就按正常的上下班时间
            startTimeList.add("08:15:00");
            startTimeList.add("13:15:00");
            endTimeList.add("12:00:00");
            endTimeList.add("17:00:00");
        }
        res.put("startTimeList", startTimeList);
        res.put("endTimeList", endTimeList);
        if (attendanceMap != null) {
            res.put("rest_day_flag", attendanceMap.get("rest_day_flag"));
        }
        return res;
    }

    @Override
    public HashMap<String, Object> getEndTimeList(String workCard, String endTime) {
        HashMap<String, Object> res = new HashMap<String, Object>();
        // 先查询这一天有没有排班
        HashMap<String ,Object> attendanceMap = acLeaveApplicationDao.getEndTimeList(workCard, endTime);
//        System.out.println("attendanceMap11111111111111");
//        System.out.println(attendanceMap);
        List<Object> startTimeList = new ArrayList<>();
        List<Object> endTimeList = new ArrayList<>();
        if (attendanceMap != null && !attendanceMap.isEmpty()) {
            if (attendanceMap.get("first_time_from") != null) {
                startTimeList.add(attendanceMap.get("first_time_from"));
            }
            if (attendanceMap.get("second_time_from") != null) {
                startTimeList.add(attendanceMap.get("second_time_from"));
            }
            if (attendanceMap.get("third_time_from") != null) {
                startTimeList.add(attendanceMap.get("third_time_from"));
            }
            if (attendanceMap.get("first_time_to") != null) {
                endTimeList.add(attendanceMap.get("first_time_to"));
            }
            if (attendanceMap.get("second_time_to") != null) {
                endTimeList.add(attendanceMap.get("second_time_to"));
            }
            if (attendanceMap.get("third_time_to") != null) {
                endTimeList.add(attendanceMap.get("third_time_to"));
            }
        } else {
            // 这里就按正常的上下班时间
            startTimeList.add("08:15:00");
            startTimeList.add("13:15:00");
            endTimeList.add("12:00:00");
            endTimeList.add("17:00:00");
        }
        res.put("startTimeList", startTimeList);
        res.put("endTimeList", endTimeList);
        if (attendanceMap != null) {
            res.put("rest_day_flag", attendanceMap.get("rest_day_flag"));
        }
        return res;
    }

    @Override
    public HashMap<String, Object> checkLeaveRule(String subStartDate, String subEndDate, String workCard) {
        HashMap<String, Object> resMap = new HashMap<>();
        StringBuilder resDate = new StringBuilder();
        resDate.append("【");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(subStartDate, formatter);
        LocalDate endDate = LocalDate.parse(subEndDate, formatter);
        List<LocalDate> dateRange = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dateRange.add(date);
        }
        if (startDate.equals(endDate)) {
//            System.out.println("两个日期相等");
            // 检查这个时间段是否要排班
            HashMap temp = acLeaveApplicationDao.checkLeaveRule(subStartDate, workCard);
            if (temp == null || temp.isEmpty()) {
                resDate.append(subStartDate + "】");
            }
        } else {
//            System.out.println("两个日期不相等");
            // 遍历日期范围，查询每一天的考勤数据
            for (LocalDate date : dateRange) {
                // 格式化日期为字符串，以便与数据库中的日期字段匹配
                String formattedDate = date.format(formatter);
//                System.out.println("日期：" + formattedDate);
                HashMap temp = acLeaveApplicationDao.checkLeaveRule(formattedDate, workCard);
//                System.out.println("kll: " + temp);
                if (temp == null || temp.isEmpty()) {
                    resDate.append(formattedDate);
                    resDate.append("、");
                }
            }
            resDate.append("】");
        }
        Integer restDay = acLeaveApplicationDao.getRestDay(subStartDate, subEndDate, workCard);
        Integer daysBetween = Math.toIntExact(ChronoUnit.DAYS.between(startDate.plusDays(1), endDate.minusDays(1)));
        resMap.put("Flag","PASS");
        if (resDate.length() >= 10 ) {
            // 这个代表请假的日期存在未排班的日期，所以返回未排班的日期和检查未通过的标识NOTPASS
            resMap.put("resDate", resDate);
            resMap.put("Flag","NOTPASS");
        }
        resMap.put("resDay", restDay);
        resMap.put("daysBetween", daysBetween);
        return resMap;
    }

    @Override
    public Integer shiftChecks(String workCard) {
        Integer res = acLeaveApplicationDao.shiftChecks(workCard);
        return res;
    }

    @Override
    public Integer getWorkDaysSecond(String startTime, String endTime) {
        return acLeaveApplicationDao.getWorkDaysSecond(startTime, endTime);
    }

    @Override
    public PmEmployeeDTO getByEmployeeId(Long id) {
        PmEmployee employee = pmEmployeeDao.getByKey(id, null);
        return pmEmployeeMapper.toDto(employee);
    }

    @Override
    public Double getLeaveOfAbsence(String workCard) {
        // 事假默认15天
        Double leaveOfAbsence = 15.00;
        // 获取已休事假天数
        // Double usedLeaveOfAbsenceDays = acOaLeaveDao.getUsedLeaveOfAbsenceDays(workCard);
        Double usedLeaveOfAbsenceDays = acLeaveApplicationDao.getUsedAbsenceLeaveDays(workCard);
//        System.out.println("--已使用事假天数---：" + usedLeaveOfAbsenceDays);
        if (usedLeaveOfAbsenceDays != null && usedLeaveOfAbsenceDays != 0) {
            leaveOfAbsence -= usedLeaveOfAbsenceDays;
        }
        return leaveOfAbsence;
    }


    @Override
    public Map<String, String> getUserViews(String workCard) {
        return acLeaveApplicationDao.getUserViews(workCard);
    }

    @Override
    public List<PmLeaveApplication> getUserForemanList(HashMap<String, String> params) {
        return acLeaveApplicationDao.getUserForemanList(params);
    }

    @Override
    public List<PmLeaveApplication> getDepartmentHeads(HashMap<String, String> params) {
        return acLeaveApplicationDao.getDepartmentHeads(params);
    }

    @Override
    public List<PmLeaveApplication> getMangerList(HashMap<String, String> params) {
        return acLeaveApplicationDao.getMangerList(params);
    }

    @Override
    public Map<String, Object> getDirectorMap(String keyWord) {
        return acOaLeaveDao.getDirectorMap(keyWord);
    }

    //    @Override
//    public HashMap<String, Object> checkLeaveSubDataContinuous(String startTime, String endTime, String workCard) {
//        //请假的开始日期或结束日期是否跟审批中的日期是否有重复
//        Integer res = acLeaveApplicationDao.checkLeaveSubDataContinuous(startTime, endTime,workCard);
//        System.out.println("res--------:  " + res);
//        HashMap<String, Object> resMap = new HashMap<>(16);
//        if (res > 0) {
//            resMap.put("date", true);
//            return resMap;
//        }
//        // 获取当前用户除-取消之外的所有请假条目
//        List<HashMap<String,String>> leaveData = acLeaveApplicationDao.GetUserNotCancel(workCard);
//        System.out.println("-----sieze--------: " + leaveData.size());
//        System.out.println(leaveData);
//        int i1 = 0;
//        try {
//            for (int i = 0; i < leaveData.size(); i++) {
//                HashMap<String, String> leaveMap = leaveData.get(i);
//                String start_time = leaveMap.get("start_time");
//                String end_time = (String) leaveMap.get("end_time");
//                // 连续工作日检查
//                Integer continuousFlag = acLeaveApplicationDao.checkContinuousDate((String) startTime.substring(0,10),(String) endTime.substring(0,10), (String) start_time.substring(0,10),(String) end_time.substring(0,10));
//                System.out.println("continuousFlag--: " + continuousFlag + "shu: " + ++i1);
//                if (continuousFlag == 1) {
//                    if (leaveMap.get("approval_result") == null) {
//                        System.out.println("请假天数： " + leaveMap.get("total_number")); // 请假天数
//                        System.out.println("请假单号：" + leaveMap.get("oa_order")); // 请假单号
//                        resMap.put("LXBSnotPASS", true);
//                        resMap.put("isContinuousReqCode", leaveMap.get("oa_order"));
//                        return resMap;
//                    }else {
//                        System.out.println("请假天数： " + leaveMap.get("total_number")); // 请假天数
//                        System.out.println("请假单号：" + leaveMap.get("oa_order")); // 请假单号
//                        resMap.put("isContinuousFlag", true); // 确定为连续请假
//                        resMap.put("isContinuousReqCode", leaveMap.get("oa_order"));
//                        resMap.put("isContinuousNumber", true);  //
//                        return resMap;
//                    }
//                }
//            }
//        }catch(Exception e){
//            System.out.println(e.getMessage());
//        }
//        return resMap;
//    }
    @Override
    public HashMap<String, Object> checkLeaveSubDataContinuous(String startTime, String endTime, String workCard, Float subTotalNumber) {
//        LocalDateTime start_time1 = LocalDateTime.parse(startTime, inputFormatter);
//        LocalDateTime end_time1 = LocalDateTime.parse(endTime, inputFormatter);
//        Integer res = acLeaveApplicationDao.checkLeaveSubDataContinuous(start_time1.format(outputFormatter), end_time1.format(outputFormatter), workCard);
        LocalDateTime start_time1;
        LocalDateTime end_time1;
        if (startTime.length() == 16) {
            // 如果时间字符串长度为16，表示它是"yyyy-MM-dd HH:mm"格式
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            start_time1 = LocalDateTime.parse(startTime, inputFormatter);
            end_time1 = LocalDateTime.parse(endTime, inputFormatter);
        } else {
            // 否则，假设时间字符串为"yyyy-MM-dd HH:mm:ss"格式
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            start_time1 = LocalDateTime.parse(startTime, inputFormatter);
            end_time1 = LocalDateTime.parse(endTime, inputFormatter);
        }
        Integer res = acLeaveApplicationDao.checkLeaveSubDataContinuous(start_time1.format(outputFormatter), end_time1.format(outputFormatter), workCard);
//        System.out.println("res--------:  " + res);
        HashMap<String, Object> resMap = new HashMap<>(16);
        if (res > 0) {
            resMap.put("date", true);
            return resMap;
        }

        List<HashMap<String, String>> leaveData = acLeaveApplicationDao.GetUserNotCancel(workCard);
//        System.out.println("-----sieze--------: " + leaveData.size());
//        System.out.println(leaveData);
        try {
            Optional<HashMap<String, String>> continuousLeaveData = leaveData.stream()
                    .filter(leaveMap -> {
                        String start_time = leaveMap.get("start_time");
                        String end_time = leaveMap.get("end_time");
                        Integer continuousFlag = acLeaveApplicationDao.checkContinuousDate(
                                startTime.substring(0, 10),
                                endTime.substring(0, 10),
                                start_time.substring(0, 10),
                                end_time.substring(0, 10)
                        );
//                        System.out.println("continuousFlag--: " + continuousFlag);
//                        if (continuousFlag == 1) {
//                            if ()
//                        }
                        return continuousFlag == 1;
                    })
                    .findFirst();
            if (continuousLeaveData.isPresent()) {
                HashMap<String, String> leaveMap = continuousLeaveData.get();
//                System.out.println("---leaveMap---: " + leaveMap);
//                System.out.println("--aprroval_result---: " + leaveMap.get("approval_result"));
                if (leaveMap.get("approval_result") == null) { // 审批中的
                    resMap.put("isContinuousnotPASS", true);
                    resMap.put("isContinuousReqCode", leaveMap.get("oa_order"));
                } else { // 审批通过的
                    resMap.put("isContinuousFlag", true); // 确定为连续请假
                    resMap.put("isContinuousReqCode", leaveMap.get("oa_order"));
                    resMap.put("isContinuousNumber", true);
                }
            }
        } catch (Exception e) {
//            System.out.println(e.getMessage());
            resMap.put("errorMessage", "操作失败，请稍后重试！");
        }
        return resMap;
    }

    @Override
    public HashMap<String, String> checkSubmitData(LeaveReqForm leaveReqForm, String oaOrderWordCard) {
        HashMap<String, String> res = new HashMap<>();
        /**
         * jb 当前请假用户的职务级别
         * ts 请假天数的 天数级别 3天=1， 3-10天=1， 大于10天=2
         * mng 是否选择了经理
         * dh 是否选择了主管
         * hrd 是否需要人资部门去审核
         */
        int jb = 0, ts = 0, mng = 0, dh = 0, hrd = 0;
        float totalNumber = Float.parseFloat(leaveReqForm.getLeaveFormData().getTotal_number()); // 请假总天数
        int employeeType = leaveReqForm.getEmployee_type(); // 获取请假人员的级别 0 ， 1， 2， 3 普通员工，主管，经理，车间人员
        if (employeeType == 1) {
            jb = 1;
            if ("".equals(leaveReqForm.getMangerUser()) && leaveReqForm.isMangerFlags()) {
                res.put("errorMsg", "警告，请选择【部门经理】！");
                return res;
            } else if ("".equals(leaveReqForm.getLeaderUser()) && leaveReqForm.isLeaderFlags() && !leaveReqForm.isMangerFlags()) {
                res.put("errorMsg", "警告，没有【部门经理】请选择【主管总监】！");
                return res;
            }
            if (totalNumber > 3) {
                // 主管请假天数大于3天，又刚刚好没有经理
                if ("".equals(leaveReqForm.getLeaderUser())) {
                    res.put("errorMsg", "警告，请假天数大于三天，请选择【主管总监】");
                    return res;
                } else {
                    // 设置天数级别为 2
                    ts = 2;
                }
            } else if (totalNumber <= 3 && !"".equals(leaveReqForm.getMangerUser())) {
                // 请假天数小于3天，并且选择了经理,设置天数级别为1，并且设置他选择了经理，并且清空他选择的上级领导
                ts = 1;
                mng = 1;
                leaveReqForm.setLeaderUser("");
            }
            // 这里OA那边不知道为什么还要重复判断，但是预防是为了填某些坑，所以暂时还是保留
            if (leaveReqForm.isMangerFlags() && !"".equals(leaveReqForm.getMangerUser())) {
                jb = 1;
                if (totalNumber <= 3) {
                    ts = 1;
                } else {
                    ts = 2;
                }
            }
        } else if (leaveReqForm.getEmployee_type() == 2) {
            // 经理级别
            jb = 2;
            // 经理如果天数小于三天
            if (totalNumber <= 3) {
                ts = 1;
            } else {
                ts = 2;
            }
        } else {
            // 这里是普通员工和车员工】
            if (leaveReqForm.isForeManFlags() && "".equals(leaveReqForm.getForeManUser()) && (!leaveReqForm.getLeaveFormData().getJob_name().contains("班长") &&
                    !leaveReqForm.getLeaveFormData().getJob_name().contains("组长") && !leaveReqForm.getLeaveFormData().getJob_name().contains("站长"))) {
                res.put("errorMsg", "警告，请选择【班组长】！");
                return res;
            }
            if ("".equals(leaveReqForm.getChargeUser()) && leaveReqForm.isChargeFlags()) {
                res.put("errorMsg", "警告，请选择【主管】！");
                return res;
            }
            if (totalNumber > 3) {
                if ("".equals(leaveReqForm.getMangerUser()) && leaveReqForm.isMangerFlags()) {
                    // 如果有经理，但是天数大于3天，必须要选经理
                    res.put("errorMsg", "警告，请假天数大于3天，请选择【部门经理】");
                    return res;
                } else if (!leaveReqForm.isMangerFlags() && leaveReqForm.isLeaderFlags() && "".equals(leaveReqForm.getLeaderUser())) {
                    // 如果没有经理，但是有主管总监，但是他又没选，那不让他通过，必须选主管总监
                    res.put("errorMsg", "警告，没有【部门经理】请选择【主管总监】！");
                    return res;
                } else if (!"".equals(leaveReqForm.getMangerUser())) {
                    mng = 1;
                } else {
                    mng = 0;
                }
            } else {
                // 小于三天，选择主管审批即可，把用户选择的经理和领导全部清空
                if (!"".equals(leaveReqForm.getChargeUser())) {
//                    System.out.println("会执行到这里吗！！！！！");
                    leaveReqForm.setMangerUser("");
                    leaveReqForm.setLeaderUser("");
                } else if (!"".equals(leaveReqForm.getMangerUser())) {
                    leaveReqForm.setLeaderUser("");
                }
            }
            // 在这里进行普通员工和车间人员的识别
            if (!"".equals(leaveReqForm.getForeManUser()) && leaveReqForm.getEmployee_type() == 3) {
                jb = 3;
                if (!"".equals(leaveReqForm.getChargeUser())) {
                    // 车间人员选择了主管
                    dh = 1;
                } else {
                    dh = 0;
                }
                if (totalNumber <= 3) {
                    // 请假天数级 ts = 1
                    ts = 1;
                } else {
                    // 请假天数级别 ts = 2
                    ts = 2;
                }
            } else if (leaveReqForm.getEmployee_type() == 0) {
                if (!"".equals(leaveReqForm.getChargeUser())) {
                    jb = 0; // 普通员工级别
                } else {
                    // 普通员工直接总经理的时候jb=4
//                    if ("".equals(leaveReqForm.getMangerUser()) && !"".equals(leaveReqForm.getMangerUser())) {
                    if (!"".equals(leaveReqForm.getMangerUser())) {
                        jb = 4;
                    } else {
                        jb = 2;
                    }
                    // 天数级别判断
                    if (totalNumber < 10) {
                        ts = 1;
                    } else {
                        ts = 2;
                    }
                }

            }
        }
        // 行政级别判断
        if (jb == 0 || jb == 3 || jb == 4) {
            if (totalNumber <= 3) {
                ts = 1;
                leaveReqForm.setLeaderUser("");
            } else if (totalNumber >= 10) {
                ts = 2;
            } else {
                ts = 1;
            }
            // 是否要HR部门审批,普通员工事假大于等于10天需人力总监审批
            // 统计出事假的工作日天数
            float days = (float) leaveReqForm.getLeaveSubData().stream()
                    .filter(cur -> ("事假").equals(cur.getLeave_type()))
                    .mapToDouble(cur -> Float.parseFloat(cur.getWork_number().replace("天", "")))
                    .sum();
            if (days >= 10) {
                hrd = 1;
            } else {
                hrd = 2;
            }
        } else {
            // 其他主管经理角色
            if (totalNumber <= 3) {
                hrd = 2;
                ts = 0;
            } else {
                hrd = 1;
                ts = 2;
            }
        }
//        System.out.println("ts: " + ts + " hrd: " + hrd + " jb: " + jb + "mng: " + mng + " dh: " + dh);
        res.put("ts", String.valueOf(ts));
        res.put("hrd", String.valueOf(hrd));
        res.put("jb", String.valueOf(jb));
        res.put("mng", String.valueOf(mng));
        res.put("dh", String.valueOf(dh));
        res.put("leaderUser", leaveReqForm.getLeaderUser());
        res.put("mangerUser", leaveReqForm.getMangerUser());
        res.put("chargeUser", leaveReqForm.getChargeUser());
        res.put("foreManUser", leaveReqForm.getForeManUser());
//        System.out.println("leaveReqForm: " + leaveReqForm.getLeaveFormData().toString());
        // 创建申请单号
        String wordCard = leaveReqForm.getLeaveFormData().getWork_card();
        String num = acOaLeaveDao.createOaReqCode("") + "";
//        System.out.println("num: " + num);
        int num1 = 0; // 默认值
        if (!num.equals("") && !"null".equals(num)) {
            num1 = Integer.parseInt(num) + 1;
//            System.out.println("---: " + num1);
        } else {
            num1 = +1;
//            System.out.println("---2: " + num1);
        }
        // 2024-03-25 这里修改成用享受假期的哪一个人来生成oa申请单号
//        String reqCode = sdf.format(new Date()) + new DecimalFormat("000").format(num1) + oaOrderWordCard;
//        System.out.println("leaveReqForm.getLeaveFormData().getWork_card(): " + leaveReqForm.getLeaveFormData().getWork_card());
        String reqCode = sdf.format(new Date()) + new DecimalFormat("000").format(num1) + leaveReqForm.getLeaveFormData().getWork_card();
//        System.out.println("本次申请单号： " + reqCode);
        res.put("Code", reqCode); // 申请单号需要返回
        // TODO 这里还有一个添加什么申请单类型的插入，和设置已知参数
        // 插入主表数据吃
        leaveReqForm.getLeaveFormData().setRequisition_code(reqCode);
        HashMap<String, Object> param1 = new HashMap<>();
        param1.put("oa_order", leaveReqForm.getLeaveFormData().getRequisition_code());
        param1.put("nick_name", leaveReqForm.getLeaveFormData().getEmployee_name());
        param1.put("user_name", leaveReqForm.getLeaveFormData().getWork_card());
        param1.put("user_department", leaveReqForm.getLeaveFormData().getDepartment());
        param1.put("user_section", leaveReqForm.getLeaveFormData().getAdministrative_office());
        param1.put("groups", leaveReqForm.getLeaveFormData().getGroups());
        param1.put("position", leaveReqForm.getLeaveFormData().getJob_name());
        param1.put("reason", leaveReqForm.getLeaveFormData().getReason());
        param1.put("total_number", Float.parseFloat(leaveReqForm.getLeaveFormData().getTotal_number()));
//        param1.put("create_by", leaveReqForm.getLeaveFormData().getWork_card());
        param1.put("create_by", oaOrderWordCard);
        try {
            int mainResFlag = acLeaveApplicationDao.addMainTableInfo(param1);
            String workCard = leaveReqForm.getLeaveFormData().getWork_card();
            System.out.println("leaveReqForm.getLeaveSubData(): " + leaveReqForm.getLeaveSubData());
            List<LeaveSubData> subDataList = null;
            if (mainResFlag != 0) {
                subDataList = leaveReqForm.getLeaveSubData().stream()
                        .map(leaveSubData -> {
                            leaveSubData.setOa_order(reqCode);
                            leaveSubData.setCreate_by(workCard);
                            leaveSubData.setTotal_number(leaveSubData.getTotal_number().replace("天",""));
                            leaveSubData.setWork_number(leaveSubData.getWork_number().replace("天",""));
//                            leaveSubData.setTotal_number(leaveSubData.getTotal_number().substring(0, leaveSubData.getTotal_number().length() - 1));
//                            leaveSubData.setWork_number(leaveSubData.getWork_number().substring(0, leaveSubData.getWork_number().length() - 1));
                            LocalDateTime start_time = LocalDateTime.parse(leaveSubData.getStart_time(), inputFormatter);
                            LocalDateTime end_time = LocalDateTime.parse(leaveSubData.getEnd_time(), inputFormatter);
                            leaveSubData.setStart_time(start_time.format(outputFormatter));
                            leaveSubData.setEnd_time(end_time.format(outputFormatter));
                            return leaveSubData;
                        })
                        .collect(Collectors.toList());
                // 开始插入子表记录
                System.out.println("subDataList: " + subDataList);
                Integer subRes = acLeaveApplicationDao.addSubLeaveInfo(subDataList);
                if (subRes != null && subRes != 0) {
                    res.put("ok", "success");
                    // TODO 发送审批提醒邮件
                    // 获取当前申请单号对应的人
                    String userName = acLeaveApplicationDao.getUserNameByOaOrder(leaveReqForm.getLeaveFormData().getRequisition_code());
                    // 获取审批人员的邮箱
                }
            }
        } catch (Exception e) {
            System.out.println("e.getMessage()");
            System.out.println(e.getMessage());
            res.put("errorMsg", "出错了请稍后再试试或者联系工作人员！");
        }
        return res;
    }

    @Override
    public Integer getWorkDays(String startTime, String endTime) {
        return acLeaveApplicationDao.getWorkDays(startTime, endTime);
    }


    @Override
    public HashMap<String, Object> getLeaveInfoByReqCode(String reqCode) {
        HashMap<String, Object> res = new HashMap<String, Object>();
        try {
            List<HashMap<String, Object>> main = acLeaveApplicationDao.getLeaveInfoByReqCode(reqCode);
            List<HashMap<String, Object>> sub = acLeaveApplicationDao.getLeaveSubInfoByReqCode(reqCode);
            res.put("main", main);
            res.put("sub", sub);
            res.put("success", "查找成功");
        } catch (Exception e) {
            res.put("errorMsg", "查找失败");
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public void  writeOaApprovalResult(AcLeaveApplication acLeaveApplicationNew){
        AcLeaveApplication acLeaveApplication = Optional.ofNullable(acLeaveApplicationDao.getByKey(acLeaveApplicationNew.getId())).orElseGet(AcLeaveApplication::new);
        //acLeaveApplication.getId()如果是NULL，那么抛出异常终止。
        ValidationUtil.isNull(acLeaveApplication.getId() ,"AcLeaveApplication", "id", acLeaveApplicationNew.getId());
        // 如果最终审批有数据，说明该审批流程已结束
        if (acLeaveApplicationNew.getApprovalResult() != null && !"".equals(acLeaveApplicationNew.getApprovalResult())) {
            acLeaveApplicationDao.updateByApprovalEnd(acLeaveApplicationNew);
        } else {
            // 该审批流程还没有结束
            if (null == acLeaveApplicationNew.getApprovalNode() || "".equals(acLeaveApplicationNew.getApprovalNode())) {
                throw new InfoCheckWarningMessException("OA当前审批节点不能为空！");
            }
            acLeaveApplicationDao.updateByApprovalUnderwar(acLeaveApplicationNew);
        }

    }









    @Transactional
    @Override
    public HashMap<String, List<HashMap<String, Object>>> getLeaveInfo(String oaOrder) {
        HashMap<String, List<HashMap<String, Object>>> res = new HashMap<>(16);
        try {
            List<HashMap<String, Object>> PmLeaveInfo = acLeaveApplicationDao.getPmLeaveInfo(oaOrder);
            List<HashMap<String, Object>> PmLeaveSubInfo = acLeaveApplicationDao.getPmLeaveSubInfo(oaOrder);
//            System.out.println("PmLeaveInfo: " + PmLeaveInfo);
//            System.out.println("PmLeaveSubInfo: " + PmLeaveSubInfo);
            res.put("leaveFormData", PmLeaveInfo);
            res.put("leaveSubData", PmLeaveSubInfo);
        } catch (Exception e) {
            res.put("Error", null);
        }
        return res;
    }


    @Transactional
    @Override
    public HashMap<String, List<HashMap<String, Object>>> getHrEditLeaveInfo(String oaOrder) {
        HashMap<String, List<HashMap<String, Object>>> res = new HashMap<>(16);
        try {
            List<HashMap<String, Object>> PmLeaveInfo = acLeaveApplicationDao.getPmLeaveInfo(oaOrder);
            List<HashMap<String, Object>> PmLeaveSubInfo = acLeaveApplicationDao.getHrEditLeaveSubInfo(oaOrder);
//            System.out.println("PmLeaveInfo: " + PmLeaveInfo);
//            System.out.println("PmLeaveSubInfo: " + PmLeaveSubInfo);
            res.put("leaveFormData", PmLeaveInfo);
            res.put("leaveSubData", PmLeaveSubInfo);
        } catch (Exception e) {
            res.put("Error", null);
        }
        return res;
    }



    @Override
    public HashMap<String, Object> logicalProcessing(String childrenDate, String workCard) {
        HashMap<String, Object> res = new HashMap<>(16);
        LocalDate childDate = LocalDate.parse(childrenDate);
        LocalDate currentDate = LocalDate.now();
        // 检查当前日期是否在子女生日之前
        if (currentDate.isBefore(childDate)) {
            // 从子女生日中减去1年
            childDate = childDate.minusYears(1);
        }
        int childAgeInYears = Period.between(childDate, currentDate).getYears(); // 子女周岁年龄
        int parentalLeaveDays = 0;
        if (childAgeInYears <= MAX_PARENTAL_LEAVE_AGE) {
            int maxLeaveDays = 10; // 每年最多可请的育儿假天数
            parentalLeaveDays = maxLeaveDays;
        }
        // 已请育儿假的天数
        int usedParentalLeave = 0;
        // 从数据查询今年已休的育儿假天数
        usedParentalLeave = acLeaveApplicationDao.getUsedParentalLeave(workCard, childrenDate, childrenDate);
        res.put("parentalLeaveDays", parentalLeaveDays - usedParentalLeave);
        // 已请育儿假的次数
        int parentalLeaveCount = 0;
        // 从数据库或存储系统中获取已请假的次数
        parentalLeaveCount = acLeaveApplicationDao.getParentalLeaveCount(workCard, childrenDate, childrenDate);
        res.put("CountFlag", parentalLeaveCount);
//        if (parentalLeaveCount >= MAX_PARENTAL_LEAVE_COUNT) {
//            // 超过育儿假次数限制
//            res.put("CountFlag", parentalLeaveCount);
//        } else {
//            res.put("CountFlag", parentalLeaveCount);
//        }
        return res;
    }

    @Override
    public HashMap<String, String> updateOaReq(String oaOrder, String approvalNode, String approvalEmployee) {
        HashMap<String, String> resMap = new HashMap<>(16);
        int flag = acLeaveApplicationDao.updateOaReq(oaOrder, approvalNode, approvalEmployee);

        if (flag == 0 || flag == -1) {
            resMap.put("errorMsg", "无法获取审批人信息，请联系系统管理员！");
        }
        return resMap;
    }

    @Override
    public List<PmLeaveApplicationDTO> getLeaveFormList(String workCard) {
        return pmLeaveApplicationMapper.toDto(acLeaveApplicationDao.getLeaveFormList(workCard));
    }

    @Override
    public HashMap<String, Object> getNurseDays(String parentDate, String workCard) {
        HashMap<String, Object> resMap = new HashMap<>(32);
        LocalDate parentDates = LocalDate.parse(parentDate);
        LocalDate currentDate = LocalDate.now();
        // 检查当前日期是否在父母生日之前
        if (currentDate.isBefore(parentDates)) {
            // 从父母生日中减去1年
            parentDates = parentDates.minusYears(1);
        }
        int parentAgeInYears = Period.between(parentDates, currentDate).getYears();
        // 父母周岁年龄
        resMap.put("parentAgeInYears", parentAgeInYears);
//        System.out.println("---parentAgeInYears--- " + parentAgeInYears);
//        int NurseDays = 0;
//        if (parentAgeInYears >= 60) {
//            int maxLeaveDays = 5; // 每年最多可请的护理假天数
//            NurseDays = maxLeaveDays;
//        }
        // 已请育儿假的天数
        int usedParentalLeave = 0;
        // 从数据查询今年已休的护理假天数
        usedParentalLeave = acLeaveApplicationDao.getUsedNurseDays(workCard);
        resMap.put("usedParentalLeave", usedParentalLeave);
//        System.out.println("---usedParentalLeave--- " + usedParentalLeave);
        // 已请护理假的次数
        int parentalLeaveCount = 0;
        // 从数据库或存储系统中获取已请假的次数
        parentalLeaveCount = acLeaveApplicationDao.getUsedNurseDaysCount(workCard);
        resMap.put("NurseCountFlag", parentalLeaveCount);
//        System.out.println("---NurseCountFlag--- " + parentalLeaveCount);
        return resMap;
    }

    @Override
    public List<HashMap<String, String>> getDepartmentList() {
        List<HashMap<String, String>> res = acLeaveApplicationDao.getDepartmentList();
        return res;
    }

    @Override
    public List<HashMap<String, String>> getUserByDepartmentID(String departmentID) {
        List<HashMap<String, String>> res = acLeaveApplicationDao.getUserByDepartmentID(departmentID);
        return res;
    }

    @Override
    public Integer cancelLeave(String oaOrder) {
        Integer mainRes = acLeaveApplicationDao.cancelLeaveMain(oaOrder);
        Integer res = null;
        if (mainRes != null && mainRes > 0) {
            res = acLeaveApplicationDao.cancelLeaveSub(oaOrder);
        }
        return res;
    }

    @Override
    public void updateResultToHrLeave(String oaOrder, String approvalResult) {
        acLeaveApplicationDao.updateResultToHrLeave(oaOrder, approvalResult);
    }

    @Override
    public Double getCompensatoryLeaveHoursWithMonth(String workCard) {
        // 获取员工月度加班工时,需要从OA中查询
        Double monthOverTime = acOaLeaveDao.getMonthOverTime(workCard);
//        System.out.println("月度加班工时------------: " + monthOverTime);
        // 获取员工月度已休工时
        Double usedMonthOverTime = acOaLeaveDao.getUsedMonthOverTime(workCard);
//        System.out.println("---月度已休工时------------: " + usedMonthOverTime);
        // 月度可调休数 = 月度休息日加班工时 - 月度已调休小时,
        Double monthlyAdjustableHours = monthOverTime - usedMonthOverTime * 7.5;
        if (monthlyAdjustableHours < 0) {
            monthlyAdjustableHours = 0.00;
        }
        return monthlyAdjustableHours;
    }

    @Override
    public Double getMonthlyRestDayOvertimeHours(String workCard) {
        // 獲取員工月度加班工時，注意是已經複核過阿德加班時數
        Double mothOverTime = acLeaveApplicationDao.getMonthOverTime(workCard);
//        System.out.println("月度加班工时------------: " + mothOverTime);
        // 获取员工月度调休天数
        Double usedMonthOverDays = acLeaveApplicationDao.getUsedMonthOverTime(workCard);
        // 月度可调休数 = 月度休息日加班工时 - 月度已调休小时,
        Double monthlyAdjustableHours = mothOverTime - usedMonthOverDays * 7.5;
        if (monthlyAdjustableHours < 0) {
            monthlyAdjustableHours = 0.00;
        }
        return monthlyAdjustableHours;
    }

    @Override
    public HashMap<String, String> autoMatchLeaderUser(String department, String administrativeOffice) {
        return acLeaveApplicationDao.autoMatchLeaderUser(department, administrativeOffice);
    }

    @Override
    public Integer updateHrChangeLeaveInfo(HrUpdateLeaveInfoForm hrUpdateLeaveInfo) {
        // 先更新主表记录
        acLeaveApplicationDao.updateMainInfo(hrUpdateLeaveInfo.getOaOrder(), hrUpdateLeaveInfo.getHrTotal(), hrUpdateLeaveInfo.getModifyReason(), hrUpdateLeaveInfo.getVersion());
//        System.out.println("hrUpdateLeaveInfo.getHrUpdateLeaveInfoSub(): " + hrUpdateLeaveInfo.getHrUpdateLeaveInfoSub());
//        List<HrUpdateLeaveInfoSub> tempSub = acLeaveApplicationDao.getSubLeaveInfoList(hrUpdateLeaveInfo.getOaOrder(), hrUpdateLeaveInfo.getVersion() - 1);
        List<Long> tempSubIds = hrUpdateLeaveInfo.getHrUpdateLeaveInfoSub().stream()
                .map(HrUpdateLeaveInfoSub::getId) // 将每个HrUpdateLeaveInfoSub对象映射为其subId字段的值
                .collect(Collectors.toList());
//        System.out.println("getHrUpdateLeaveInfoSub: " + hrUpdateLeaveInfo.getHrUpdateLeaveInfoSub());
        List<HashMap<String, Object>> tempSub2 = acLeaveApplicationDao.getSubLeaveInfoList2(hrUpdateLeaveInfo.getOaOrder(), hrUpdateLeaveInfo.getVersion() - 1, tempSubIds);
//        System.out.println("tempSub2:" + tempSub2);
//        System.out.println("hrUpdateLeaveInfo.subDataList: " + hrUpdateLeaveInfo.getHrUpdateLeaveInfoSub());
        // 插入新子表记录
        if (hrUpdateLeaveInfo.getHrUpdateLeaveInfoSub() != null && !hrUpdateLeaveInfo.getHrUpdateLeaveInfoSub().isEmpty()) {
            // 遍历tempSub列表
            for (HashMap<String, Object> tempMap : tempSub2) {
//                boolean isExist = false; // 标记当前temp的leave_type是否已存在于main中
//                // 遍历main列表，检查leave_type是否已存在
//                for (HrUpdateLeaveInfoSub mainItem : hrUpdateLeaveInfo.getHrUpdateLeaveInfoSub()) {
//                    System.out.println("mainItem:" + mainItem);
//                    System.out.println("temp:" + tempMap);
//                    if (tempMap.get("leave_type").equals(mainItem.getLeave_type())) {
//                        isExist = true;
//                        break;
//                    }
//                }
//                // 如果不存在，则添加到main中，并且version加1
//                if (!isExist) {
                    // 假设有合适的构造器或克隆方法
                    HrUpdateLeaveInfoSub newItem = new HrUpdateLeaveInfoSub();
                    // 复制temp的所有属性到新对象，除了version需要特别处理
                    // 假设有get和set方法
                    newItem.setLeave_type((String) tempMap.get("leave_type"));
                    // 复制其他必要字段...
                    // 特别注意version加1
                    newItem.setVersion((Integer)tempMap.get("version") + 1);
                    newItem.setOa_order((String) tempMap.get("oa_order"));
                    newItem.setStart_time((String) tempMap.get("start_time"));
                    newItem.setEnd_time((String) tempMap.get("end_time"));
                    newItem.setHr_nick_name(hrUpdateLeaveInfo.getHrNickName());
                    newItem.setModify_time(hrUpdateLeaveInfo.getHrUpdateLeaveInfoSub().get(0).getModify_time());
                    newItem.setTotal_number(((Double) tempMap.get("total_number")).floatValue());
                    newItem.setWork_number(((Double) tempMap.get("work_number")).floatValue());
                    // 将newItem添加到main列表中
                    hrUpdateLeaveInfo.getHrUpdateLeaveInfoSub().add(newItem);
//                }
                // 注意：这里我们没有修改tempSub中的元素，只是根据它创建了新的对象添加到main中
            }
            List<HrUpdateLeaveInfoSub> subDataList = null;
//            System.out.println("hrUpdateLeaveInfo.getVersion(): " + hrUpdateLeaveInfo.getVersion());
//            System.out.println(hrUpdateLeaveInfo.getHrUpdateLeaveInfoSub());
            subDataList = hrUpdateLeaveInfo.getHrUpdateLeaveInfoSub().stream()
                    .map(leaveSubData -> {
//                        leaveSubData.setOa_order(leaveSubData.getOa_order());
                        leaveSubData.setOa_order(hrUpdateLeaveInfo.getOaOrder());
                        leaveSubData.setVersion(hrUpdateLeaveInfo.getVersion());
                        leaveSubData.setLeave_type(leaveSubData.getLeave_type());
                        leaveSubData.setCreate_by(leaveSubData.getCreate_by());
                        leaveSubData.setTotal_number(leaveSubData.getTotal_number());
                        leaveSubData.setWork_number(leaveSubData.getWork_number());
                        leaveSubData.setModify_time(leaveSubData.getModify_time());
                        leaveSubData.setHr_nick_name(leaveSubData.getHr_nick_name());
//                    LocalDateTime start_time = LocalDateTime.parse(leaveSubData.getStart_time(), inputFormatter);
//                    LocalDateTime end_time = LocalDateTime.parse(leaveSubData.getEnd_time(), inputFormatter);
                        leaveSubData.setStart_time(leaveSubData.getStart_time());
                        leaveSubData.setEnd_time(leaveSubData.getEnd_time());
                        return leaveSubData;
                    })
                    .collect(Collectors.toList());
//            System.out.println("subDataList: " + subDataList);
            return acLeaveApplicationDao.insertHrSubInfo(subDataList);
        } else {
            return 0;
        }
    }

    @Override
    public HashMap<String, Object> getHrChangeLeaveInfo(String oaOrder) {
        HashMap<String, Object> res = new HashMap<String, Object>();
        HashMap mainRes = acLeaveApplicationDao.getMainInfo(oaOrder);
        res.put("mainRes", mainRes);
//        HashMap subRes = acLeaveApplicationDao.getHrChangeNewLeaveSubInfo(oaOrder);
        List<HashMap<String, Object>> subResNew = acLeaveApplicationDao.getAllHrChangeNewLeaveSubInfo(oaOrder);
        // 2024-01-18 原本是获取最新的修改记录的，但是现在变成了获取所有修改记录
        res.put("subRes", subResNew);
        return res;
    }

    @Override
    public Object updateHrLeaveEditInfo(String oaOrder, Integer version) {
        return acLeaveApplicationDao.updateHrLeaveEditInfo(oaOrder, version);
    }

    @Override
    public HashMap<String, Object> getAcLeaveLeaderUser(String deptName) {
        Integer parentID = acLeaveApplicationDao.getDeptId(deptName);
        HashMap<String, Object> res = new HashMap<String, Object>(16);
        HashMap<String, Object> tempRes = new HashMap<>(16);
        tempRes = acLeaveApplicationDao.getParentId(parentID);
        res = tempRes;
        while (tempRes != null) {
            tempRes = acLeaveApplicationDao.getParentId((Integer) tempRes.get("parent_id"));
            if (tempRes != null) {
                res = tempRes;
            } else {
                break;
            }
        }
        return res;
    }

    @Override
    public HashMap<String, Object> getHrManger() {
        return acLeaveApplicationDao.getHrManger();
    }

    @Override
    public List<HashMap<String, Object>> getHrInfo() {
        return acLeaveApplicationDao.getHrInfo();
    }

    @Override
    public void notifyHrManager(String oaOrder) {
        // 先获取人资经理的邮箱
        String hrManagerEmail = acLeaveApplicationDao.getHrManagerEmail();
        ToolEmailServer emailServer = new ToolEmailServer();
        emailServer.setId(inSuntenEmailServerId);
        String mailContent = null;
        mailContent = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"> <html xmlns=\"http://www.w3.org/1999/xhtml\"> <head>     <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>  \n" +
                "   <style>         @page {             margin: 0;         }     </style> </head> <body style=\"margin: 0px;             padding: 0px;    font: 100% SimSun, Microsoft YaHei, Times New Roman, Verdana, Arial, Helvetica, sans-serif;    \n" +
                "            color: #000;\"> <div style=\"height: auto;    width: 820px;    min-width: 820px;    margin: 0 auto;    margin-top: 20px;             border: 1px solid #eee;\">     <div style=\"padding: 10px;padding-bottom: 0px;\">       \n" +
                "\t\t\t  <p style=\"margin-bottom: 10px;padding-bottom: 0px;\">尊敬的用户，您好：</p> <p style=\"text-indent: 2em;\">" + "请假申请人补充了请假证明附件，请您及时审批！申请单号："  + oaOrder + "</p>";
        LocalDateTime date = LocalDateTime.now();
        ToolEmailInterface toolEmailInterface = new ToolEmailInterface();
        toolEmailInterface.setMailSubject("请假审批通知 - SUNTEN人力资源管理系统");
        toolEmailInterface.setStatus("PLAN");
        toolEmailInterface.setPlannedDate(date);
        toolEmailInterface.setEmailServer(emailServer);
        mailContent = mailContent +"<p style=\"text-align: center;    font-family: Times New Roman;    font-size: small;  \n" +
                "\t\t\t    color: #C60024;    padding: 20px 0px;    margin-bottom: 10px;    font-weight: bold;    background: #ebebeb;\">系统主页链接：<br>\n" +
                "\t\t\t\t点击链接进行跳转<a href='http://172.18.1.159:8016' target='_blank'>http://172.18.1.159:8016</a> </p>      \n" +
                "\t\t\t\t   <div class=\"foot-hr hr\" style=\"margin: 0 auto;    z-index: 111;    width: 800px;    margin-top: 30px;    border-top: 1px solid #DA251D;\">      \n" +
                "\t\t\t\t      </div>         <div style=\"text-align: center;    font-size: 12px;    padding: 20px 0px;    font-family: Microsoft YaHei;\">           \n" +
                "\t\t\t\t\t         Copyright &copy;2023 SUNTEN 人力资源管理系统 All Rights Reserved.         </div>      </div> </div> </body> </html> ";
        toolEmailInterface.setMailContent(mailContent);
        toolEmailInterface.setSendTo(hrManagerEmail);
        toolEmailInterfaceDao.insertAllColumn(toolEmailInterface);
        // 修改附件上传状态
        acLeaveApplicationDao.updateToBeUploadedFlag(oaOrder);
    }

    @Override
    public void notifyHr(String oaOrder) {
        // 先获取人事专员的邮箱集合
        List<String> list = acLeaveApplicationDao.getHrEmails();
        for (String email : list) {
            ToolEmailServer emailServer = new ToolEmailServer();
            emailServer.setId(inSuntenEmailServerId);
            String mailContent = null;
            mailContent = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"> <html xmlns=\"http://www.w3.org/1999/xhtml\"> <head>     <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>  \n" +
                    "   <style>         @page {             margin: 0;         }     </style> </head> <body style=\"margin: 0px;             padding: 0px;    font: 100% SimSun, Microsoft YaHei, Times New Roman, Verdana, Arial, Helvetica, sans-serif;    \n" +
                    "            color: #000;\"> <div style=\"height: auto;    width: 820px;    min-width: 820px;    margin: 0 auto;    margin-top: 20px;             border: 1px solid #eee;\">     <div style=\"padding: 10px;padding-bottom: 0px;\">       \n" +
                    "\t\t\t  <p style=\"margin-bottom: 10px;padding-bottom: 0px;\">尊敬的用户，您好：</p> <p style=\"text-indent: 2em;\">" + "请假申请人补充了请假证明附件，请您及时审批！申请单号："  + oaOrder + "</p>";
            LocalDateTime date = LocalDateTime.now();
            ToolEmailInterface toolEmailInterface = new ToolEmailInterface();
            toolEmailInterface.setMailSubject("请假审批通知 - SUNTEN人力资源管理系统");
            toolEmailInterface.setStatus("PLAN");
            toolEmailInterface.setPlannedDate(date);
            toolEmailInterface.setEmailServer(emailServer);
            mailContent = mailContent +"<p style=\"text-align: center;    font-family: Times New Roman;    font-size: small;  \n" +
                    "\t\t\t    color: #C60024;    padding: 20px 0px;    margin-bottom: 10px;    font-weight: bold;    background: #ebebeb;\">系统主页链接：<br>\n" +
                    "\t\t\t\t点击链接进行跳转<a href='http://172.18.1.159:8016' target='_blank'>http://172.18.1.159:8016</a> </p>      \n" +
                    "\t\t\t\t   <div class=\"foot-hr hr\" style=\"margin: 0 auto;    z-index: 111;    width: 800px;    margin-top: 30px;    border-top: 1px solid #DA251D;\">      \n" +
                    "\t\t\t\t      </div>         <div style=\"text-align: center;    font-size: 12px;    padding: 20px 0px;    font-family: Microsoft YaHei;\">           \n" +
                    "\t\t\t\t\t         Copyright &copy;2023 SUNTEN 人力资源管理系统 All Rights Reserved.         </div>      </div> </div> </body> </html> ";
            toolEmailInterface.setMailContent(mailContent);
            toolEmailInterface.setSendTo(email);
            toolEmailInterfaceDao.insertAllColumn(toolEmailInterface);
        }
    }

    @Override
    public List<HashMap<String, Object>> getAcAuthorizationList(Integer deptId2) {
        List<HashMap<String, Object>> authList = new ArrayList<>();
        if (deptId2 == null) {
             authList = acLeaveApplicationDao.getAuthorizationListAll();
        } else {
             authList = acLeaveApplicationDao.getAuthorizationList(deptId2);
        }
        for (HashMap<String, Object> map : authList) {
            // 获取dept_id值
            Integer deptId = (Integer) map.get("dept_id");
            // 班长map
            if (!"".equals(map.get("team_authorize_work_card")) && map.get("team_authorize_work_card") != null) {
                map.put("team_authorize_work_card",map.get("team_authorize_work_card"));
            } else {
                map.put("team_authorize_work_card","");
            }
            if (!"".equals(map.get("team_authorize_work_card"))) {
                map.put("team_authorize_name", acLeaveApplicationDao.getUserNameByWorkCard((String)map.get("team_authorize_work_card")));
            }
            map.put("team_work_card",acLeaveApplicationDao.getWorkCardById((Integer) map.get("team_leader_employee_id")));
            map.put("team_name",acLeaveApplicationDao.getUserNameByWorkCard((String)map.get("team_work_card")));


            // 主管map
            if (!"".equals(map.get("department_authorize_work_card")) && map.get("department_authorize_work_card") != null) {
                map.put("department_authorize_work_card",map.get("department_authorize_work_card"));
            } else {
                map.put("department_authorize_work_card","");
            }
            if (!"".equals(map.get("department_authorize_work_card"))) {
                map.put("department_authorize_name", acLeaveApplicationDao.getUserNameByWorkCard((String)map.get("department_authorize_work_card")));
            }
            map.put("department_work_card",acLeaveApplicationDao.getWorkCardById((Integer) map.get("department_head_employee_id")));
            map.put("department_name",acLeaveApplicationDao.getUserNameByWorkCard((String)map.get("department_work_card")));


            // 经理map
            if (!"".equals(map.get("manger_authorize_work_card")) && map.get("manger_authorize_work_card") != null) {
                map.put("manger_authorize_work_card",map.get("manger_authorize_work_card"));
            } else {
                map.put("manger_authorize_work_card","");
            }
            if (!"".equals(map.get("manger_authorize_work_card"))) {
                map.put("manger_authorize_name", acLeaveApplicationDao.getUserNameByWorkCard((String)map.get("manger_authorize_work_card")));
            }
            map.put("manger_work_card",acLeaveApplicationDao.getWorkCardById((Integer) map.get("manger_employee_id")));
            map.put("manger_name",acLeaveApplicationDao.getUserNameByWorkCard((String)map.get("manger_work_card")));

            // 领导map

            if (!"".equals(map.get("leader_authorize_work_card")) && map.get("leader_authorize_work_card") != null) {
                map.put("leader_authorize_work_card",map.get("leader_authorize_work_card"));
            } else {
                map.put("leader_authorize_work_card","");
            }
            if (!"".equals(map.get("leader_authorize_work_card"))) {
                map.put("leader_authorize_name", acLeaveApplicationDao.getUserNameByWorkCard((String)map.get("leader_authorize_work_card")));
            }
            map.put("leader_work_card",acLeaveApplicationDao.getWorkCardById((Integer) map.get("leader_employee_id")));
            map.put("leader_name",acLeaveApplicationDao.getUserNameByWorkCard((String)map.get("leader_work_card")));

        }

        return authList;
    }

    @Override
    public List<HashMap<String, Object>> getApprovalDetail(Integer id, Integer deptId2) {
        List<HashMap<String, Object>> authList = acLeaveApplicationDao.getApprovalDetail(id, deptId2);
        for (HashMap<String, Object> map : authList) {
            // 获取dept_id值
//            Integer deptId = (Integer) map.get("dept_id");
            // 班长map
            if (!"".equals(map.get("team_authorize_work_card")) && map.get("team_authorize_work_card") != null) {
                map.put("team_authorize_work_card",map.get("team_authorize_work_card"));
            } else {
                map.put("team_authorize_work_card","");
            }
            if (!"".equals(map.get("team_authorize_work_card"))) {
                map.put("team_authorize_name", acLeaveApplicationDao.getUserNameByWorkCard((String)map.get("team_authorize_work_card")));
            }
            map.put("team_work_card",acLeaveApplicationDao.getWorkCardById((Integer) map.get("team_leader_employee_id")));
            map.put("team_name",acLeaveApplicationDao.getUserNameByWorkCard((String)map.get("team_work_card")));


            // 主管map
            if (!"".equals(map.get("department_authorize_work_card")) && map.get("department_authorize_work_card") != null) {
                map.put("department_authorize_work_card",map.get("department_authorize_work_card"));
            } else {
                map.put("department_authorize_work_card","");
            }
            if (!"".equals(map.get("department_authorize_work_card"))) {
                map.put("department_authorize_name", acLeaveApplicationDao.getUserNameByWorkCard((String)map.get("department_authorize_work_card")));
            }
            map.put("department_work_card",acLeaveApplicationDao.getWorkCardById((Integer) map.get("department_head_employee_id")));
            map.put("department_name",acLeaveApplicationDao.getUserNameByWorkCard((String)map.get("department_work_card")));


            // 经理map
            if (!"".equals(map.get("manger_authorize_work_card")) && map.get("manger_authorize_work_card") != null) {
                map.put("manger_authorize_work_card",map.get("manger_authorize_work_card"));
            } else {
                map.put("manger_authorize_work_card","");
            }
            if (!"".equals(map.get("manger_authorize_work_card"))) {
                map.put("manger_authorize_name", acLeaveApplicationDao.getUserNameByWorkCard((String)map.get("manger_authorize_work_card")));
            }
            map.put("manger_work_card",acLeaveApplicationDao.getWorkCardById((Integer) map.get("manger_employee_id")));
            map.put("manger_name",acLeaveApplicationDao.getUserNameByWorkCard((String)map.get("manger_work_card")));

            // 领导map

            if (!"".equals(map.get("leader_authorize_work_card")) && map.get("leader_authorize_work_card") != null) {
                map.put("leader_authorize_work_card",map.get("leader_authorize_work_card"));
            } else {
                map.put("leader_authorize_work_card","");
            }
            if (!"".equals(map.get("leader_authorize_work_card"))) {
                map.put("leader_authorize_name", acLeaveApplicationDao.getUserNameByWorkCard((String)map.get("leader_authorize_work_card")));
            }
            map.put("leader_work_card",acLeaveApplicationDao.getWorkCardById((Integer) map.get("leader_employee_id")));
            map.put("leader_name",acLeaveApplicationDao.getUserNameByWorkCard((String)map.get("leader_work_card")));

        }
        return authList;
    }

    @Override
    public void updateApproval(Integer id, Integer deptId, String teamWorkCard,
            String departmentWorkCard, String mangerWorkCard, String leaderWorkCard,
            String teamAuthorizeWorkCard, String departmentAuthorizeWorkCard, String mangerAuthorizeWorkCard, String leaderAuthorizeWorkCard) {
        Integer team_leader_job_id = acLeaveApplicationDao.getNewJobId(teamWorkCard);
        Integer team_leader_id = acLeaveApplicationDao.getEmployeeIdByWordCard(teamWorkCard);
        Integer department_head_job_id = acLeaveApplicationDao.getNewJobId(departmentWorkCard);
        Integer department_head_id = acLeaveApplicationDao.getEmployeeIdByWordCard(departmentWorkCard);
        Integer manger_job_id = acLeaveApplicationDao.getNewJobId(mangerWorkCard);
        Integer manger_id = acLeaveApplicationDao.getEmployeeIdByWordCard(mangerWorkCard);
        Integer leader_job_id = acLeaveApplicationDao.getNewJobId(leaderWorkCard);
        Integer leader_id = acLeaveApplicationDao.getEmployeeIdByWordCard(leaderWorkCard);
        acLeaveApplicationDao.updateApproval(id, deptId,team_leader_job_id,team_leader_id,department_head_job_id,department_head_id,
                manger_job_id,manger_id,
                leader_job_id, leader_id,
                teamAuthorizeWorkCard,departmentAuthorizeWorkCard, mangerAuthorizeWorkCard, leaderAuthorizeWorkCard);
    }

    @Override
    public Integer addApprovalDetail(Integer dept_id, String teamWorkCard,
                                     String teamAuthorizeWorkCard,
                                     String departmentWorkCard,
                                     String departmentAuthorizeWorkCard,
                                     String mangerWorkCard,
                                     String mangerAuthorizeWorkCard,
                                     String leaderWorkCard,
                                     String leaderAuthorizeWorkCard) {
        HashMap map = new HashMap();
        // 查询部门dept_id
//        Integer deptId = acLeaveApplicationDao.getNewDeptId(deptName);
//        Integer deptId = null;
        Integer team_leader_job_id = acLeaveApplicationDao.getNewJobId(teamWorkCard);
        Integer department_head_job_id = acLeaveApplicationDao.getNewJobId(departmentWorkCard);
        Integer manger_job_id = acLeaveApplicationDao.getNewJobId(mangerWorkCard);
        Integer leader_job_id = acLeaveApplicationDao.getNewJobId(leaderWorkCard);
//        System.out.println("teamAuthorizeWorkCard: " + teamAuthorizeWorkCard);
//        System.out.println("departmentAuthorizeWorkCard: " + departmentAuthorizeWorkCard);
//        System.out.println("mangerAuthorizeWorkCard: " + mangerAuthorizeWorkCard);
//        System.out.println("leaderAuthorizeWorkCard: " + leaderAuthorizeWorkCard);
        Integer res = acLeaveApplicationDao.addApprovalDetail(dept_id,team_leader_job_id,department_head_job_id,manger_job_id,leader_job_id,teamAuthorizeWorkCard,departmentAuthorizeWorkCard,mangerAuthorizeWorkCard,leaderAuthorizeWorkCard, 1);
        return res;
    }

    @Override
    public List<Map<String, Object>> getTeamLeaderList(String workCard) {
        Integer deptId = acLeaveApplicationDao.getNewDeptIdbyWorkCard(workCard);
        List<Map<String, Object>> res = acLeaveApplicationDao.getTeamLeaderList(deptId);
//        for (Map<String, Object> map : res) {
        Iterator<Map<String, Object>> iterator = res.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> map = iterator.next();
            if (map.get("team_leader_employee_id") != null) {
                String username = acLeaveApplicationDao.getWorkCardById((Integer) map.get("team_leader_employee_id"));
                map.put("username", username);
                map.put("nickname", acLeaveApplicationDao.getUserNameByWorkCard(username));
            }
            if (!"".equals(map.get("team_authorize_work_card")) && map.get("team_authorize_work_card") != null) {
                String uName = (String) map.get("team_authorize_work_card");
                map.put("username", uName);
                map.put("nickname", acLeaveApplicationDao.getUserNameByWorkCard(uName));
            }
            // 检查username和nickname是否都为空或null
            if (map.get("username") == null || map.get("nickname") == null ||
                    "".equals(map.get("username")) || "".equals(map.get("nickname"))) {
                // 移除该HashMap
                iterator.remove();
            }
        }
//        System.out.println("deptId: " + deptId);
//        return acLeaveApplicationDao.getTeamLeaderList(deptId);
        return res;
    }

    @Override
    public List<Map<String, Object>> getDepartmentLists(String workCard) {
        Integer deptId = acLeaveApplicationDao.getNewDeptIdbyWorkCard(workCard);
//        System.out.println("deptId: " + deptId);
        List<Map<String, Object>> res = acLeaveApplicationDao.getTeamLeaderList(deptId);
        Iterator<Map<String, Object>> iterator = res.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> map = iterator.next();
            if (map.get("department_head_employee_id") != null) {
                String username = acLeaveApplicationDao.getWorkCardById((Integer) map.get("department_head_employee_id"));
                map.put("username", username);
                String nickname = (String) acLeaveApplicationDao.getUserNameByWorkCard(username);
                map.put("nickname", nickname);
            }
            if (!"".equals(map.getOrDefault("department_authorize_work_card", "")) &&
                    map.get("department_authorize_work_card") != null) {
                String uName = (String) map.get("department_authorize_work_card");
                map.put("username", uName); // 注意这里会覆盖之前设置的username，如果这是有意的则保留
                String nickname = (String) acLeaveApplicationDao.getUserNameByWorkCard(uName);
                map.put("nickname", nickname); // 同样会覆盖之前设置的nickname
            }

            // 检查username和nickname是否都为空或null
            if (map.get("username") == null || map.get("nickname") == null ||
                    "".equals(map.get("username")) || "".equals(map.get("nickname"))) {
                // 移除该HashMap
                iterator.remove();
            }
        }
//        for (Map<String, Object> map : res) {
//            if (map.get("department_head_employee_id") != null) {
//                String username = acLeaveApplicationDao.getWorkCardById((Integer) map.get("department_head_employee_id"));
//                map.put("username", username);
//                map.put("nickname", acLeaveApplicationDao.getUserNameByWorkCard(username));
//            }
//            if (!"".equals(map.get("department_authorize_work_card")) && map.get("department_authorize_work_card") != null) {
//                String uName = (String) map.get("department_authorize_work_card");
//                map.put("username", uName);
//                map.put("nickname", acLeaveApplicationDao.getUserNameByWorkCard(uName));
//            }
//        }
//        List<Map<String, String>> res = acLeaveApplicationDao.getDepartmentLists(deptId).stream()
//                .map(map -> {
//                    // 使用computeIfAbsent方法进行team_authorize_work_card和username的值替换
//                    Map<String, String> updatedMap = new HashMap<>(map);
//                    // 先找代理主管
//                    Map<String, Object> brokerMap = acLeaveApplicationDao.getBrokerDepartmentList(deptId);
//                    // 找完代理主管找真正的主管
//                    Map<String, Object> deptMainMap = acLeaveApplicationDao.getDeptMainMap(deptId);
//                    System.out.println("deptMainMap: " + deptMainMap);
//                    if (brokerMap != null && !brokerMap.isEmpty()) {
//                        // 将代理主管的值替换进去
//                        updatedMap.put("username", (String) brokerMap.get("username"));
//                        updatedMap.put("nickname", (String) brokerMap.get("nickname"));
//                    } else if (deptMainMap != null && !deptMainMap.isEmpty()) {
//                        updatedMap.put("username", (String) deptMainMap.get("username"));
//                        updatedMap.put("nickname", (String) deptMainMap.get("nickname"));
//                    } else {
//                        String department_authorize_work_card = updatedMap.get("department_authorize_work_card");
//                        if (department_authorize_work_card != null && !"".equals(department_authorize_work_card)) {
//                            updatedMap.put("username", department_authorize_work_card);
//                        }
//                    }
//                    return updatedMap;
//                })
//                .collect(Collectors.toList());
//        System.out.println("res: " + res.toString());
        return res;
    }

    @Override
    public List<Map<String, Object>> getHrMangerList(String workCard) {
        Integer deptId = acLeaveApplicationDao.getNewDeptIdbyWorkCard(workCard);
        System.out.println("deptId: " + deptId);
        List<Map<String, Object>> res = acLeaveApplicationDao.getTeamLeaderList(deptId);
//        for (Map<String, Object> map : res) {
        Iterator<Map<String, Object>> iterator = res.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> map = iterator.next();
            if (map.get("manger_employee_id") != null) {
                String username = acLeaveApplicationDao.getWorkCardById((Integer) map.get("manger_employee_id"));
                map.put("username", username);
                map.put("nickname", acLeaveApplicationDao.getUserNameByWorkCard(username));
            }
            if (!"".equals(map.get("manger_authorize_work_card")) && map.get("manger_authorize_work_card") != null) {
                String uName = (String) map.get("manger_authorize_work_card");
                map.put("username", uName);
                map.put("nickname", acLeaveApplicationDao.getUserNameByWorkCard(uName));
            }
            // 检查username和nickname是否都为空或null
            if (map.get("username") == null || map.get("nickname") == null ||
                    "".equals(map.get("username")) || "".equals(map.get("nickname"))) {
                // 移除该HashMap
                iterator.remove();
            }
        }
//        return acLeaveApplicationDao.getHrMangerList(deptId);
        return res;
    }

    @Override
    public List<Map<String, Object>> getHrLeaderList(String workCard) {
        Integer deptId = acLeaveApplicationDao.getNewDeptIdbyWorkCard(workCard);
//        System.out.println("deptId: " + deptId);
//        return acLeaveApplicationDao.getHrLeaderList(deptId);
        List<Map<String, Object>> res = acLeaveApplicationDao.getTeamLeaderList(deptId);
//        for (Map<String, Object> map : res) {
        Iterator<Map<String, Object>> iterator = res.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> map = iterator.next();
            if (map.get("leader_employee_id") != null) {
                String username = acLeaveApplicationDao.getWorkCardById((Integer) map.get("leader_employee_id"));
                map.put("username", username);
                map.put("nickname", acLeaveApplicationDao.getUserNameByWorkCard(username));
            }
            if (!"".equals(map.get("leader_authorize_work_card")) && map.get("leader_authorize_work_card") != null) {
                String uName = (String) map.get("leader_authorize_work_card");
                map.put("username", uName);
                map.put("nickname", acLeaveApplicationDao.getUserNameByWorkCard(uName));
            }
            // 检查username和nickname是否都为空或null
            if (map.get("username") == null || map.get("nickname") == null ||
                    "".equals(map.get("username")) || "".equals(map.get("nickname"))) {
                // 移除该HashMap
                iterator.remove();
            }
        }
        return res;
    }

    @Override
    public HashMap<String, String> autoMatchNewLeaderUser(String workCard) {
        Integer deptId = acLeaveApplicationDao.getNewDeptIdbyWorkCard(workCard);
//        System.out.println("deptId: " + deptId);
        return acLeaveApplicationDao.autoMatchNewLeaderUser(deptId);
    }

    @Override
    public void changeAcApprovalDepartmentStatus(Integer id, Integer deptId, Integer enabledFlag) {
        acLeaveApplicationDao.changeAcApprovalDepartmentStatus(id,deptId,enabledFlag);
    }


//    public HashMap<String, Object> logicalProcessing(String childrenDate, String workCard) {
//        HashMap<String, Object> res = new HashMap<>(16);
//        LocalDate childDate = LocalDate.parse(childrenDate);
//        LocalDate currentDate = LocalDate.now();
//        // 检查当前日期是否在子女生日之前
//        if (currentDate.isBefore(childDate)) {
//            // 从子女生日中减去1年
//            childDate = childDate.minusYears(1);
//        }
//        Period age = Period.between(childDate.plusDays(1), currentDate);
//        int years = age.getYears();
//        int months = age.getMonths();
//        int days = age.getDays();
//
//        int year = currentDate.getYear();
//        YearMonth yearMonthObject = YearMonth.of(year, currentDate.getMonth());
//        int daysInMonth = yearMonthObject.lengthOfMonth();
//        int daysInYear = currentDate.isLeapYear() ? 366 : 365;
//
//        int parentalLeaveDays = 0;
//        if (years < MAX_PARENTAL_LEAVE_AGE) {
//            int currentAgeInDays = age.getDays() + (years * daysInYear) + (months * daysInMonth);
//            int maxLeaveDays = years * 10;
//            parentalLeaveDays = Math.min(maxLeaveDays, currentAgeInDays);
//        }
//        // 已请育儿假的次数
//        int usedParentalLeave = 0 , parentalLeaveCount = 0;
//        // 从数据查询今年已休的育儿假天数和已经请的育儿假次数
//        usedParentalLeave = acLeaveApplicationDao.getUsedParentalLeave(workCard);
//        parentalLeaveCount = acLeaveApplicationDao.getParentalLeaveCount(workCard);
//        res.put("parentalLeaveDays", parentalLeaveDays - usedParentalLeave);
//        // 从数据库或存储系统中获取已请假的次数
//        if (parentalLeaveCount >= MAX_PARENTAL_LEAVE_COUNT) {
//            // 超过育儿假次数限制
//            res.put("CountFlag", "YES");
//        } else {
//            res.put("CountFlag", "NO");
//        }
//        return null;
//    }

    @Override
    public HashMap<String, Object> newCheckLeaveSubDataContinuous(String startTime, String endTime, String workCard, Float subTotalNumber) {
        LocalDateTime start_time1;
        LocalDateTime end_time1;
        if (startTime.length() == 16) {
            // 如果时间字符串长度为16，表示它是"yyyy-MM-dd HH:mm"格式
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            start_time1 = LocalDateTime.parse(startTime, inputFormatter);
            end_time1 = LocalDateTime.parse(endTime, inputFormatter);
        } else {
            // 否则，假设时间字符串为"yyyy-MM-dd HH:mm:ss"格式
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            start_time1 = LocalDateTime.parse(startTime, inputFormatter);
            end_time1 = LocalDateTime.parse(endTime, inputFormatter);
        }
        Integer res = acLeaveApplicationDao.checkLeaveSubDataContinuous(start_time1.format(outputFormatter), end_time1.format(outputFormatter), workCard);
        HashMap<String, Object> resMap = new HashMap<>(16);
        if (res > 0) {
            resMap.put("date", true);
            return resMap;
        }
        if (startTime.length() == 16) {
            // 如果时间字符串长度为16，表示它是"yyyy-MM-dd HH:mm"格式
            startTime = startTime + ":00";
            endTime = endTime + ":00";
        }
        List<HashMap<String, Object>> lLeaveList = acLeaveApplicationDao.GetLeftLeaveNotCancel(startTime, workCard);
        List<HashMap<String, Object>> rLeaveList = acLeaveApplicationDao.GetRigthLeaveNotCancel(endTime, workCard);
        Double lLeaveSUM = 0.0;
        Double rLeaveSUM = 0.0;
        String lStart_time = startTime;
        String lEnd_time = endTime;
        StringBuilder oaOrder = new StringBuilder("");
        String rStart_time = startTime;
        String rEnd_time = endTime;

        if (lLeaveList == null && rLeaveList == null) {

        } else {
            System.out.println("走进来==============");
            // 处理 lLeaveList
            if (lLeaveList != null && !lLeaveList.isEmpty()) {
                for (int i = 0; i < lLeaveList.size(); i++) {
                    String workDate = acLeaveApplicationDao.getFirstMaxWorkDate(lStart_time);
                    String date = (String) lLeaveList.get(i).get("end_time");
                    if (date.substring(0,10).equals(workDate)) {
                        // 走到这里证明日期连续，然后进行时间段是否连续判断
                        if (lTimeMatch((String) lLeaveList.get(i).get("start_time"), (String) lLeaveList.get(i).get("end_time"), lStart_time, lEnd_time)) {
//                            System.out.println("时间段连续了");
                            // 时间段也连续
                            lLeaveSUM += (Double) (lLeaveList.get(i).get("number"));
                            oaOrder.append((String) lLeaveList.get(i).get("oa_order")).append(",");
                        } else {
                            // 走到这里时间段不连续了，直接跳出 结束向左找
                            break;
                        }
                    } else {
                        // 日期都不连续了直接断开跳出循环
                        break;
                    }
                    // 滑动窗口,更新时间
                    lStart_time = (String) lLeaveList.get(i).get("start_time");
                    lEnd_time = (String) lLeaveList.get(i).get("end_time");
                }
            }
            // 处理 rLeaveList
            if (rLeaveList != null && !rLeaveList.isEmpty()) {
                for (int i = 0; i < rLeaveList.size(); i++) {
                    String workDate = acLeaveApplicationDao.getFirsminWorkDate(rEnd_time);
                    String date = (String) rLeaveList.get(i).get("start_time");
                    if (date.substring(0,10).equals(workDate)) {
                        // 走到这里证明日期连续，然后进行时间段是否连续判断
                        if (rTimeMatch((String) rLeaveList.get(i).get("start_time"), (String) rLeaveList.get(i).get("end_time"), rStart_time, rEnd_time)) {
                            // 时间段也连续
                            rLeaveSUM += (Double) (rLeaveList.get(i).get("number"));
                            oaOrder.append((String) rLeaveList.get(i).get("oa_order")).append(",");
                        } else {
                            // 走到这里时间段不连续了，直接跳出 结束向左找
                            break;
                        }
                    } else {
                        // 日期都不连续了直接断开跳出循环
                        break;
                    }
                    // 滑动窗口,更新时间
                    rStart_time = (String) rLeaveList.get(i).get("start_time");
                    rEnd_time = (String) rLeaveList.get(i).get("start_time");
                }
            }
        }
        System.out.println("oaOrder: " + oaOrder);
        System.out.println("lLeaveSUM: " + lLeaveSUM);
        System.out.println("rLeaveSUM: " + rLeaveSUM);
        if ((rLeaveSUM + lLeaveSUM) > 0 && !"".equals(oaOrder)) {
            // 走到这里可以判断是连续请假了
            if ((rLeaveSUM + lLeaveSUM) + subTotalNumber > 3) {
                // 连续请假了
                resMap.put("isContinuousnotPASS", true);
                resMap.put("isContinuousReqCode", oaOrder.deleteCharAt(oaOrder.length() - 1));
            }
        }
        // 在这里加多一个查询本月休息日加班时数的查询
        // 獲取員工月度加班工時，注意是已經複核過阿德加班時數
        Double mothOverTime = acLeaveApplicationDao.getMonthOverTime(workCard);
//        System.out.println("月度加班工时------------: " + mothOverTime);
        // 获取员工月度调休天数
        Double usedMonthOverDays = acLeaveApplicationDao.getUsedMonthOverTime(workCard);
        // 月度可调休数 = 月度休息日加班工时 - 月度已调休小时,
        Double monthlyAdjustableHours = mothOverTime - usedMonthOverDays * 7.5;
        if (monthlyAdjustableHours < 0) {
            monthlyAdjustableHours = 0.00;
        }
        resMap.put("compensatoryHour", monthlyAdjustableHours);
        return resMap;
    }

    @Override
    public Map<String, Object> listAllLeaveStatistics(AcLeaveApplicationQueryCriteria criteria, Pageable pageable) {
        Page<AcLeaveApplication> page = PageUtil.startPage(pageable);
        List<AcLeaveApplication> hrLeaves = acLeaveApplicationDao.listAllLeaveStatistics(page, criteria);
        System.out.println("hrLeaves: ");
        System.out.println(hrLeaves);
        return PageUtil.toPage(acHrLeaveMapper.toDto(hrLeaves), page.getTotal());
    }


    public static Boolean lTimeMatch(String recordStartTime, String recordEndTime, String start_time, String end_time) {
        // 定义时间格式
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//        DateTimeFormatter timeFormatter2 = DateTimeFormatter.ofPattern("HH:mm");
        // 解析时间部分
        LocalTime recordStart = LocalTime.parse(recordStartTime.substring(11), timeFormatter);
        LocalTime recordEnd = LocalTime.parse(recordEndTime.substring(11), timeFormatter);
        LocalTime start = LocalTime.parse(start_time.substring(11), timeFormatter);
        LocalTime end = LocalTime.parse(end_time.substring(11), timeFormatter);
        // 计算时间差
        long duration = ChronoUnit.HOURS.between(recordStart, recordEnd);
        // 根据时间差进行不同的判断
        if (duration > 4) {
            // 时间超过4小时，走C判断
            if (recordStart.equals(start)) {
                // 如果recordStartTime的HH:MM:SS部分与start_time的HH:MM:SS部分相同，判断为连续
                return true;
            }
            // 其他情况都是不连续
            return false;
        } else {
            // 时间小于等于4小时，走D判断
            // 判断上午还是下午
            if (end.isBefore(LocalTime.of(14, 0))) {
                // 结束时间小于2点，属于上午
                // 上午类型的直接判断不连续
                return false;
            } else {
                // 下午类型
                // 注意：这里假设recordEndTime和end_time不可能是同一个时间，因为那样应该直接判断为连续
                if (!recordStart.equals(start) && !recordEnd.equals(end)) {
                    // 如果recordStartTime和start_time的时间不同，并且recordEndTime和end_time的时间也不同，判断为连续
                    return true;
                }
                // 其他情况判断为不连续
                return false;
            }
        }
    }
    public static Boolean rTimeMatch(String recordStartTime, String recordEndTime, String start_time, String end_time) {
        // 定义时间格式
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        // 解析时间部分
        LocalTime recordStart = LocalTime.parse(recordStartTime.substring(11), timeFormatter);
        LocalTime recordEnd = LocalTime.parse(recordEndTime.substring(11), timeFormatter);
        LocalTime end = LocalTime.parse(end_time.substring(11), timeFormatter);
        // 计算时间差
        long duration = ChronoUnit.HOURS.between(recordStart, recordEnd);
        // 根据时间差进行不同的判断
        if (duration > 4) {
            // 时间超过4小时，走C判断
            // C判断依据: 如果recordEndTime不等于end_time，则不连续，否则连续
            return recordEnd.equals(end);
        } else {
            // 时间小于等于4小时，走D判断
            // D判断依据: 如果结束时间小于2点，属于上午
            if (end.isBefore(LocalTime.of(14, 0))) {
                // 上午类型的
                // 如果recordEndTime不等于end_time，则不连续，否则连续
                return recordEnd.equals(end);
            } else {
                // 下午类型的直接判断不连续
                return false;
            }
        }
    }
}
