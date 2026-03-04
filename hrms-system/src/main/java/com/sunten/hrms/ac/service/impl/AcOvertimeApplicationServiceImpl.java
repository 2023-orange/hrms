package com.sunten.hrms.ac.service.impl;

import com.sunten.hrms.ac.dao.AcLeaveApplicationDao;
import com.sunten.hrms.ac.dao.AcOaLeaveDao;
import com.sunten.hrms.ac.domain.*;
import com.sunten.hrms.ac.dao.AcOvertimeApplicationDao;
import com.sunten.hrms.ac.dto.AcClockRecordRestDTO;
import com.sunten.hrms.ac.mapper.AcOvertimeLeaveCheckListMapper;
import com.sunten.hrms.ac.mapper.AcOvertimeLeaveListMapper;
import com.sunten.hrms.ac.service.AcOvertimeApplicationService;
import com.sunten.hrms.ac.dto.AcOvertimeApplicationDTO;
import com.sunten.hrms.ac.dto.AcOvertimeApplicationQueryCriteria;
import com.sunten.hrms.ac.mapper.AcOvertimeApplicationMapper;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zouyp
 * @since 2023-10-16
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcOvertimeApplicationServiceImpl extends ServiceImpl<AcOvertimeApplicationDao, AcOvertimeApplication> implements AcOvertimeApplicationService {
    private final AcOvertimeApplicationDao acOvertimeApplicationDao;
    private final AcOvertimeApplicationMapper acOvertimeApplicationMapper;
    @Autowired
    private AcOvertimeLeaveCheckListMapper acOvertimeLeaveCheckListMapper;
    @Autowired
    private FndUserService fndUserService;
    @Resource
    private AcOvertimeLeaveListMapper acOvertimeLeaveListMapper;

    public AcOvertimeApplicationServiceImpl(AcOvertimeApplicationDao acOvertimeApplicationDao, AcOvertimeApplicationMapper acOvertimeApplicationMapper) {
        this.acOvertimeApplicationDao = acOvertimeApplicationDao;
        this.acOvertimeApplicationMapper = acOvertimeApplicationMapper;
    }

    @Autowired
    private AcOaLeaveDao acOaLeaveDao;
    @Autowired
    private AcLeaveApplicationDao acLeaveApplicationDao;

    SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
    private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcOvertimeApplicationDTO insert(AcOvertimeApplication overtimeApplicationNew) {
        acOvertimeApplicationDao.insertAllColumn(overtimeApplicationNew);
        return acOvertimeApplicationMapper.toDto(overtimeApplicationNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        AcOvertimeApplication overtimeApplication = new AcOvertimeApplication();
        overtimeApplication.setId(id);
        this.delete(overtimeApplication);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcOvertimeApplication overtimeApplication) {
        // TODO    确认删除前是否需要做检查
        acOvertimeApplicationDao.deleteByEntityKey(overtimeApplication);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcOvertimeApplication overtimeApplicationNew) {
        AcOvertimeApplication overtimeApplicationInDb = Optional.ofNullable(acOvertimeApplicationDao.getByKey(overtimeApplicationNew.getId())).orElseGet(AcOvertimeApplication::new);
        ValidationUtil.isNull(overtimeApplicationInDb.getId(), "OvertimeApplication", "id", overtimeApplicationNew.getId());
        overtimeApplicationNew.setId(overtimeApplicationInDb.getId());
        acOvertimeApplicationDao.updateAllColumnByKey(overtimeApplicationNew);
    }

    @Override
    public AcOvertimeApplicationDTO getByKey(Integer id) {
        AcOvertimeApplication overtimeApplication = Optional.ofNullable(acOvertimeApplicationDao.getByKey(id)).orElseGet(AcOvertimeApplication::new);
        ValidationUtil.isNull(overtimeApplication.getId(), "OvertimeApplication", "id", id);
        return acOvertimeApplicationMapper.toDto(overtimeApplication);
    }

    @Override
    public List<AcOvertimeApplicationDTO> listAll(AcOvertimeApplicationQueryCriteria criteria) {
        return acOvertimeApplicationMapper.toDto(acOvertimeApplicationDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcOvertimeApplicationQueryCriteria criteria, Pageable pageable) {
        Page<AcOvertimeApplication> page = PageUtil.startPage(pageable);
        List<AcOvertimeApplication> overtimeApplications = acOvertimeApplicationDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acOvertimeApplicationMapper.toDto(overtimeApplications), page.getTotal());
    }

    @Override
    public void download(List<AcOvertimeApplicationDTO> overtimeApplicationDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcOvertimeApplicationDTO overtimeApplicationDTO : overtimeApplicationDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", overtimeApplicationDTO.getId());
            map.put("OA单号", overtimeApplicationDTO.getOaOrder());
            map.put("申请人姓名", overtimeApplicationDTO.getNickName());
            map.put("申请人工号", overtimeApplicationDTO.getUserName());
            map.put("加班类别", overtimeApplicationDTO.getOvertimeType());
            map.put("部门", overtimeApplicationDTO.getUserDepartment());
            map.put("科室", overtimeApplicationDTO.getUserSection());
            map.put("班组", overtimeApplicationDTO.getGroups());
            map.put("原因", overtimeApplicationDTO.getReason());
            map.put("加班总人数", overtimeApplicationDTO.getTotalNumber());
            map.put("加班总时数", overtimeApplicationDTO.getTotalTime());
            map.put("当前审批节点", overtimeApplicationDTO.getApprovalNode());
            map.put("审批人", overtimeApplicationDTO.getApprovalEmployee());
            if ("Pass".equals(overtimeApplicationDTO.getApprovalResult()) || "pass".equals(overtimeApplicationDTO.getApprovalResult())) {
                map.put("最终审批结果", "通过");
            } else if ("Cancel".equals(overtimeApplicationDTO.getApprovalResult()) || "cancel".equals(overtimeApplicationDTO.getApprovalResult())) {
                map.put("最终审批结果", "取消");
            } else if ("notPass".equals(overtimeApplicationDTO.getApprovalResult())) {
                map.put("最终审批结果", "不通过");
            } else {
                map.put("最终审批结果", "审批中");
            }
//            map.put("有效标记", overtimeApplicationDTO.getEnabledFlag());
//            map.put("弹性域1", overtimeApplicationDTO.getAttribute1());
//            map.put("弹性域2", overtimeApplicationDTO.getAttribute2());
//            map.put("弹性域3", overtimeApplicationDTO.getAttribute3());
//            map.put("弹性域4", overtimeApplicationDTO.getAttribute4());
//            map.put("弹性域5", overtimeApplicationDTO.getAttribute5());
//            map.put("创建人", overtimeApplicationDTO.getCreateBy());
//            map.put("创建时间", overtimeApplicationDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<HashMap<String, Object>> getMySelfOvertimeListInfo(String workCard, String deptName, String department) {
        // 获取deptId
        Integer deptId = null;
        if (!"".equals(deptName) && !"".equals(department)) {
            deptId = acLeaveApplicationDao.getNewDeptId(deptName, department);
        } else if (!"".equals(department)) {
            deptId = acLeaveApplicationDao.getNewDeptIdByDept(department);
        }
        if (deptId == null) {
//            deptId = acLeaveApplicationDao.getDeptIdBySectionName(deptName);
            deptId = acLeaveApplicationDao.getDeptIdByWorkCard(workCard);
        }
        if (deptId == null) {
            deptId = acLeaveApplicationDao.getDeptIdByDepartment(department);
        }
//        System.out.println("deptId: " + deptId);
        List<HashMap<String, Object>> tempList = acOvertimeApplicationDao.getMySelfOvertimeListInfo(deptId);
        // 这里是处理特殊情况，比如一些人在车间班组里面，但是这个人确实NDVC，所以需要把这些人找出来
        List<HashMap<String, Object>> tempList2 = acOvertimeApplicationDao.getMySelfOvertimeByNDVC(deptId);

        List<HashMap<String, Object>> mergedList = Stream.concat(
                        tempList.stream(),
                        tempList2.stream()
                )
                .collect(Collectors.toList());

        for (HashMap<String, Object> temp : mergedList) {
            String tempCard = "";
            Float monthHours = 0.0f;
            tempCard = (String) temp.get("work_card");
            monthHours = acOvertimeApplicationDao.getOvertimeInfo(tempCard);
            System.out.println("tempCard: " + tempCard + "monthHours=" + monthHours);
            if (monthHours != null && monthHours > 0.0f) {
                temp.put("month_hours", monthHours);
            } else {
                temp.put("month_hours", 0.0f);
            }
        }
        return mergedList;
    }

    @Override
    public HashMap<String, Object> getDeptUsedOvertimeInfo(String deptName, String department, String groups, String workCard) {
        // 获取deptId
//        Integer deptId = acLeaveApplicationDao.getNewDeptId(deptName);
        Integer deptId = null;
        if (!"".equals(deptName) && !"".equals(department)) {
            deptId = acLeaveApplicationDao.getNewDeptId(deptName, department);
        } else if (!"".equals(department)) {
            deptId = acLeaveApplicationDao.getNewDeptIdByDept(department);
        }
        if (deptId == null) {
//            deptId = acLeaveApplicationDao.getDeptIdBySectionName(deptName);
            deptId = acLeaveApplicationDao.getDeptIdByWorkCard(workCard);
        }
        if (deptId == null) {
            deptId = acLeaveApplicationDao.getDeptIdByDepartment(department);
        }
        if (!"".equals(department) && "".equals(deptName) && !"".equals(groups)) {
            deptId = acLeaveApplicationDao.getDeptIdByDepartment(groups);
        }
        System.out.println("deptId: " + deptId);
        HashMap<String, Object> limitHours = acOvertimeApplicationDao.getDeptUsedOvertimeInfo(deptId);
        // 获取科室本月已使用的加班工时
        Float department_used_month_hours = acOvertimeApplicationDao.getUsedMonthHours(deptId);
        if (department_used_month_hours == null) {
            limitHours.put("used_month_hours", 0.0f);
        } else {
            limitHours.put("used_month_hours", department_used_month_hours);
        }
        return limitHours;
    }

    @Override
    public HashMap<String, Object> getCrossMonthInfo(String deptName, String department, String groups, String endTime, String workCard) {
        // 获取deptId
//        Integer deptId = acLeaveApplicationDao.getNewDeptId(deptName);
        Integer deptId = null;
        if (!"".equals(deptName) && !"".equals(department)) {
            deptId = acLeaveApplicationDao.getNewDeptId(deptName, department);
        } else if (!"".equals(department)) {
            deptId = acLeaveApplicationDao.getNewDeptIdByDept(department);
        }
        if (deptId == null) {
//            deptId = acLeaveApplicationDao.getDeptIdBySectionName(deptName);
            deptId = acLeaveApplicationDao.getDeptIdByWorkCard(workCard);
        }
        if (deptId == null) {
            deptId = acLeaveApplicationDao.getDeptIdByDepartment(department);
        }
        if (!"".equals(department) && "".equals(deptName) && !"".equals(groups)) {
            deptId = acLeaveApplicationDao.getDeptIdByDepartment(groups);
        }
        HashMap<String, Object> limitHours = acOvertimeApplicationDao.getCrossMonthInfo(deptId, endTime);
        // 获取科室跨月已使用的加班工时
        Float department_used_month_hours = acOvertimeApplicationDao.getCrossUsedMonthHours(deptId, endTime);
        if (department_used_month_hours == null) {
            limitHours.put("used_month_hours", 0.0f);
        } else {
            limitHours.put("used_month_hours", department_used_month_hours);
        }
        return limitHours;
    }

    @Override
    public Map<String, Object> getOvertimeLeaveStatisticsQueryListNew(AcOvertimeApplicationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {}, direction = Sort.Direction.ASC) Pageable pageable, String checkMonth, Boolean isHr, Integer employeeType, String department, String administrativeOffice, String groups) {
        Page<OvertimeLeaveCheckList> page = PageUtil.startPage(pageable);
        List<OvertimeLeaveCheckList> overtimeApplications = new ArrayList<OvertimeLeaveCheckList>();
        Integer pages = pageable.getPageNumber();
        Integer sizes = pageable.getPageSize();
        if (employeeType == 1) {
            Integer deptId = acOvertimeApplicationDao.getDetpId( "%" + department + "%");
            Integer administrativeOfficeId = acOvertimeApplicationDao.getDetpId("%" +administrativeOffice + "%");
            // 主管级别查询相同科室的人
            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryList2(checkMonth, deptId, administrativeOfficeId, pages, sizes);
            if (overtimeApplications.size() == 0) {
                overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveBySection(checkMonth, deptId, pages, sizes);
            }
        } else if (employeeType == 2) {
            Integer deptId = acOvertimeApplicationDao.getDetpId( "%" + department + "%");
            // 经理级别查询
//            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryList3(checkMonth, deptId, pages, sizes);
        } else if (isHr && isHr != null) {
            // 人力资源部可以查询全部人的信息
            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryList4(checkMonth, pages, sizes);
        }
        overtimeApplications = overtimeApplications.stream()
                .map(overtimeLeaveCheckList -> {
                    if (overtimeLeaveCheckList.getRestDayOvertime() != null && overtimeLeaveCheckList.getTXXS() != null) {
                        overtimeLeaveCheckList.setSyjbgs(overtimeLeaveCheckList.getRestDayOvertime() - overtimeLeaveCheckList.getTXXS());
                        return overtimeLeaveCheckList;
                    } else {
                        return overtimeLeaveCheckList;
                    }
                })
                .collect(Collectors.toList());
        return PageUtil.toPage(acOvertimeLeaveListMapper.toDto(overtimeApplications), page.getTotal());
    }

    @Override
    public List<HashMap<String, Object>> getMySelfOvertimeListInfoByTeam(String workCard, String deptName, String departmentName, String teamName) {
        // 获取deptId
        Integer deptId = null;
        if (!"".equals(deptName) && !"".equals(departmentName)) {
            deptId = acLeaveApplicationDao.getNewDeptId(deptName, departmentName);
        } else if (!"".equals("deptName")) {
            // 科室为空的,只查部门
            deptId = acLeaveApplicationDao.getNewDeptIdByDept(departmentName);
        }
        if (deptId == null) {
//            deptId = acLeaveApplicationDao.getDeptIdBySectionName(deptName);
            deptId = acLeaveApplicationDao.getDeptIdByWorkCard(workCard);
        }
        if (deptId == null) {
            deptId = acLeaveApplicationDao.getDeptIdByDepartment(departmentName);
        }
        List<HashMap<String, Object>> tempList = new ArrayList<HashMap<String, Object>>();
        if ("".equals(teamName)) {
            teamName = "全部";
        }
        if ("全部".equals(teamName)) {
            tempList = acOvertimeApplicationDao.getMySelfOvertimeListInfoByParentId(deptId);
        } else {
            tempList = acOvertimeApplicationDao.getMySelfOvertimeListInfoByParentId2(deptId, teamName);
        }
        for (HashMap<String, Object> temp : tempList) {
            String tempCard = "";
            Float monthHours = 0.0f;
            tempCard = (String) temp.get("work_card");
            monthHours = acOvertimeApplicationDao.getOvertimeInfo(tempCard);
            System.out.println("tempCard: " + tempCard + "monthHours=" + monthHours);
            if (monthHours != null && monthHours > 0.0f) {
                temp.put("month_hours", monthHours);
            } else {
                temp.put("month_hours", 0.0f);
            }
        }
        return tempList;
    }

    @Override
    public List<HashMap<String, Object>> getTeamList(String deptName, String departmentName, String workCard) {
        // 获取deptId
//        Integer deptId = acLeaveApplicationDao.getNewDeptId(deptName);
        Integer deptId = null;
        if (!"".equals(deptName) && !"".equals(departmentName)) {
            deptId = acLeaveApplicationDao.getNewDeptId(deptName, departmentName);
        } else if (!"".equals("deptName")) {
            // 科室为空的,只查部门
            deptId = acLeaveApplicationDao.getNewDeptIdByDept(departmentName);
        }
        if (deptId == null) {
//            deptId = acLeaveApplicationDao.getDeptIdBySectionName(deptName);
            deptId = acLeaveApplicationDao.getDeptIdByWorkCard(workCard);
        }
        if (deptId == null) {
            deptId = acLeaveApplicationDao.getDeptIdByDepartment(departmentName);
        }
        return (List<HashMap<String, Object>>) acOvertimeApplicationDao.getTeamNameList(deptId);
    }

    @Override
    public Integer searchDeptNumber(String deptName) {
//        Integer deptId = acLeaveApplicationDao.getNewDeptId(deptName);
//        if (deptId == null) {
//            return 0;
//        }
        Integer deptId = null;
        if (deptId == null) {
            return 0;
        }
        Integer res = acOvertimeApplicationDao.searchDeptNumber(deptId);
        return res;
    }

    @Override
    public List<HashMap<String, Object>> getDeptInfoListByDeptName(String deptName) {
        List<HashMap<String, Object>> res = acOvertimeApplicationDao.getDeptInfoListByDeptName(deptName);
        res = res.stream()
                        .filter(obj -> obj.get("parentName") != null && !"".equals(obj.get("parentName")))
                                .collect(Collectors.toList());
        System.out.println("res = " + res);
        res.forEach(map -> {
            Long dept_id = (Long) map.get("id");
            Integer childNumber = acOvertimeApplicationDao.getChildNumber(Math.toIntExact(dept_id));
            Integer userNumber = (Integer) map.getOrDefault("userNumber", 0);
            map.put("userNumber", userNumber + childNumber);
        });
        System.out.println(res);
        return res;
    }

    @Override
    public Integer getUserNumberByTreeId(Integer deptId) {
        Integer res = acOvertimeApplicationDao.getUserNumberByTreeId(deptId);
        return res;
    }

    @Override
    public List<HashMap<String, Object>> getMySelfOvertimeListInfoFormChild(String workCard, String deptName, String department) {
        // 获取deptId
        Integer deptId = null;
        if (!"".equals(deptName) && !"".equals(department)) {
            deptId = acLeaveApplicationDao.getNewDeptId(deptName, department);
        } else if (!"".equals(department)) {
            deptId = acLeaveApplicationDao.getNewDeptIdByDept(department);
        }
        if (deptId == null) {
//            deptId = acLeaveApplicationDao.getDeptIdBySectionName(deptName);
            deptId = acLeaveApplicationDao.getDeptIdByWorkCard(workCard);
        }
        if (deptId == null) {
            deptId = acLeaveApplicationDao.getDeptIdByDepartment(department);
        }
        List<HashMap<String, Object>> tempList = acOvertimeApplicationDao.getMySelfOvertimeListInfo(deptId);
        List<HashMap<String, Object>> tempList2 = acOvertimeApplicationDao.getMySelfOvertimeByParentId(deptId);
        // 使用Stream API合并两个列表
        List<HashMap<String, Object>> mergedList = Stream.concat(
                        tempList.stream(),
                        tempList2.stream()
                )
                .collect(Collectors.toList());
        for (HashMap<String, Object> temp : mergedList) {
            String tempCard = "";
            Float monthHours = 0.0f;
            tempCard = (String) temp.get("work_card");
            monthHours = acOvertimeApplicationDao.getOvertimeInfo(tempCard);
            System.out.println("tempCard: " + tempCard + "monthHours=" + monthHours);
            if (monthHours != null && monthHours > 0.0f) {
                temp.put("month_hours", monthHours);
            } else {
                temp.put("month_hours", 0.0f);
            }
        }
        return mergedList;
    }

    @Override
    public String getUserProductionCategory(String workCard) {
        String res = acOvertimeApplicationDao.getUserProductionCategory(workCard);
        return res;
    }

    @Override
    public List<HashMap<String, Object>> checkReviewUser2(String workCard) {
        return acOvertimeApplicationDao.checkReviewUser2(workCard);
    }

    @Override
    public Object getOvertimeApplicationStatisticsPage(AcOvertimeApplicationQueryCriteria criteria, Pageable pageable) {
        Page<AcOvertimeApplication> page = PageUtil.startPage(pageable);
        List<AcOvertimeApplication> overtimeApplications = acOvertimeApplicationDao.getOvertimeApplicationStatisticsPage(page, criteria);
        return PageUtil.toPage(acOvertimeApplicationMapper.toDto(overtimeApplications), page.getTotal());
    }

    @Override
    public List<AcOvertimeApplicationDTO> downloadStatistics(AcOvertimeApplicationQueryCriteria criteria) {
        return acOvertimeApplicationMapper.toDto(acOvertimeApplicationDao.listAllStatisticsByCriteria(criteria));
    }

    @Override
    public HashMap<String, Object> newCheckOvertimeInfo(String startTime, String endTime, String workCard) {
        HashMap<String, Object> result = new HashMap<String, Object>(16);
        // 默认检查通过
        result.put("isCheck", true);
        HashMap isCheck = acOvertimeApplicationDao.newCheckOvertimeInfo(startTime, endTime, workCard);
        if (isCheck != null) {
            result.put("isCheck", false);
            result.put("name", isCheck.get("nick_name"));
            result.put("oaOrder", isCheck.get("oa_order"));
            result.put("position", isCheck.get("position"));
        }
        return result;
    }

    @Override
    public HashMap<String, String> submitData(OvertimeApplicationForm overtimeApplicationForm, String oaOrderWordCard) {
        HashMap<String, String> res = new HashMap<>();
        // 创建申请单号
        String wordCard = overtimeApplicationForm.getOvertimeFormData().getUser_name();
        String num = acOaLeaveDao.createOaReqCode("") + "";
        System.out.println("num: " + num);
        int num1 = 0; // 默认值
        if (!num.equals("") && !"null".equals(num)) {
            num1 = Integer.parseInt(num) + 1;
            System.out.println("---: " + num1);
        } else {
            num1 = +1;
            System.out.println("---2: " + num1);
        }
        String reqCode = sdf.format(new Date()) + new DecimalFormat("000").format(num1) + oaOrderWordCard;
        System.out.println("本次申请单号： " + reqCode);
        res.put("Code", reqCode); // 申请单号需要返回
        // 插入主表数据吃
        overtimeApplicationForm.getOvertimeFormData().setOa_order(reqCode);
        HashMap<String, Object> param1 = new HashMap<>();
        param1.put("oa_order", reqCode);
        param1.put("nick_name", overtimeApplicationForm.getOvertimeFormData().getNick_name());
        param1.put("user_name", overtimeApplicationForm.getOvertimeFormData().getUser_name());
        param1.put("user_department", overtimeApplicationForm.getOvertimeFormData().getDepartment());
        param1.put("user_section", overtimeApplicationForm.getOvertimeFormData().getAdministrative_office());
        param1.put("groups", overtimeApplicationForm.getOvertimeFormData().getGroups());
        param1.put("reason", overtimeApplicationForm.getOvertimeFormData().getReason());
        param1.put("total_number", overtimeApplicationForm.getOvertimeFormData().getTotal_number());
        param1.put("total_time", overtimeApplicationForm.getOvertimeFormData().getTotal_time());
        param1.put("create_by", oaOrderWordCard);
        param1.put("enabled_flag", 1);
        int mainResFlag = acOvertimeApplicationDao.addMainTableInfo(param1);
        String workCard = overtimeApplicationForm.getOvertimeFormData().getUser_name();
        List<SubOvertimeFormData> subDataList2 = null;
        if (mainResFlag != 0) {
            subDataList2 = overtimeApplicationForm.getSubOvertimeFormData().stream()
                    .map(subDataList -> {
                        subDataList.setOa_order(reqCode);
                        subDataList.setWork_card(subDataList.getWork_card());
                        subDataList.setJob_name(subDataList.getJob_name());
                        subDataList.setName(subDataList.getName());
                        subDataList.setDept_name(subDataList.getDept_name());
                        subDataList.setStart_time(subDataList.getStart_time());
                        subDataList.setEnd_time(subDataList.getEnd_time());
                        subDataList.setTotal_rest_time(subDataList.getTotal_rest_time());
                        subDataList.setHours(subDataList.getHours());
                        subDataList.setMonth_hours(subDataList.getMonth_hours());
                        subDataList.setCreate_by(workCard);
                        return subDataList;
                    })
                    .collect(Collectors.toList());
            // 开始插入子表记录
            Integer subRes = acOvertimeApplicationDao.addSubInfo(subDataList2);
            if (subRes != null && subRes != 0) {
                res.put("ok", "success");
                // TODO 发送审批提醒邮件
            }
        }
        return res;
    }

    @Override
    public HashMap<String, String> updateOaReq(String oaOrder, String approvalNode, String approvalEmployee) {
        HashMap<String, String> resMap = new HashMap<>(16);
        int flag = acOvertimeApplicationDao.updateOaReq(oaOrder, approvalNode, approvalEmployee);
        if (flag == 0 || flag == -1) {
            resMap.put("errorMsg", "无法获取审批人信息，请联系系统管理员！");
        }
        return resMap;
    }

    @Override
    public Integer cancelOvertime(String oaOrder) {
        Integer mainRes = acOvertimeApplicationDao.cancelOvertimeMain(oaOrder);
        Integer res = null;
        if (mainRes != null && mainRes > 0) {
            res = acOvertimeApplicationDao.cancelOvertimeSub(oaOrder);
        }
        return res;
    }

    @Override
    public void updateResultToHrOvertime(String oaOrder, String approvalResult) {
        acOvertimeApplicationDao.updateResultToHrOvertime(oaOrder, approvalResult);
    }

    @Override
    public HashMap<String, List<HashMap<String, Object>>> getOvertimeInfo(String checkOaOrder) {
        HashMap<String, List<HashMap<String, Object>>> res = new HashMap<>(16);
        try {
            List<HashMap<String, Object>> acOvertimeInfo = acOvertimeApplicationDao.getAcOvertimeInfo(checkOaOrder);
            List<HashMap<String, Object>> acOvertimeSubInfo = acOvertimeApplicationDao.getAcOvertimeSubInfo(checkOaOrder);
            System.out.println("acOvertimeInfo: " + acOvertimeInfo);
            System.out.println("acOvertimeSubInfo: " + acOvertimeSubInfo);
            res.put("acOvertimeInfo", acOvertimeInfo);
            res.put("acOvertimeSubInfo", acOvertimeSubInfo);
        } catch (Exception e) {
            res.put("Error", null);
        }
        return res;
    }

    @Override
    public List<HashMap<String, Object>> getAllDepartment() {
        return acOvertimeApplicationDao.getAllDepartment();
    }

    @Override
    public List<HashMap<String, Object>> getDepartmentByParentId(String parentId) {
        return acOvertimeApplicationDao.getDepartmentByParentId(parentId);
    }

    @Override
    public void addOvertimeReviewUser(Integer deptId, Integer sectionId, String workCard, String createBy) {
        acOvertimeApplicationDao.addOvertimeReviewUser(deptId, sectionId, workCard, createBy);
    }

    @Override
    public List<HashMap<String, Object>> getOvertimeReviewInfo() {
        List<HashMap<String, Object>> res1 = acOvertimeApplicationDao.getOvertimeReviewInfo();
        List<HashMap<String, Object>> res2 = acOvertimeApplicationDao.getOvertimeReviewInfo2();
        List<HashMap<String, Object>> mergedList = Stream.concat(res1.stream(), res2.stream())
                .collect(Collectors.toList());
        for (HashMap<String, Object> map : mergedList) {
            String sectionName = acOvertimeApplicationDao.getSectionName(map.get("section_id"));
            if (!"".equals(sectionName)) {
                map.put("section_name", sectionName);
            }
        }
        return mergedList;
    }

    @Override
    public HashMap<String, Object> getOvertimeReviewUserInfo(Integer id) {
        HashMap<String, Object> res1 = acOvertimeApplicationDao.getOvertimeReviewUserInfo(id);
//        HashMap<String, Object> res2 = acOvertimeApplicationDao.getOvertimeReviewUserInfo2(id);
//        // 创建一个新的HashMap来存储合并后的结果
//        HashMap<String, Object> mergedResult = new HashMap<>(res1); // 使用res1作为初始内容
//        res2.forEach((key, value) -> mergedResult.put(key, value));
        // return acOvertimeApplicationDao.getOvertimeReviewUserInfo(id);
        String sectionName = acOvertimeApplicationDao.getSectionName(res1.get("section_id"));
        if (!"".equals(sectionName)) {
            res1.put("section_name", sectionName);
        }
        return res1;
    }

    @Override
    public List<HashMap<String, Object>> simpleQuery(String name, String value) {
        List<HashMap<String, Object>> res = null;
        if ("dept_name".equals(name)) {
            res = acOvertimeApplicationDao.simpleQueryByDeptName(value);
        } else if ("section_name".equals(name)) {
            res = acOvertimeApplicationDao.simpleQueryBySectionName(value);
        } else if ("work_card".equals(name)) {
            res = acOvertimeApplicationDao.simpleQueryByWorCard(value);
        } else if ("name".equals(name)) {
            res = acOvertimeApplicationDao.simpleQueryByUserName(value);
        }
        for (HashMap<String, Object> map : res) {
            String sectionName = acOvertimeApplicationDao.getSectionName(map.get("section_id"));
            if (!"".equals(sectionName)) {
                map.put("section_name", sectionName);
            }
        }
        return res;
    }

    @Override
    public void updateOvertimeReview(Integer id, Integer deptId, Integer sectionId, String workCard) {
        acOvertimeApplicationDao.updateOvertimeReview(id, deptId, sectionId, workCard);
    }

    @Override
    public void deleteOvertimeReviewInfo(Integer id) {
        acOvertimeApplicationDao.deleteOvertimeReviewInfo(id);
    }

    @Override
    public Map<String, Object> getOvertimeApplicationReviews(AcOvertimeApplicationQueryCriteria criteria, Pageable pageable, List<HashMap<String, Object>> checkList) {
        Page<AcOvertimeApplication> page = PageUtil.startPage(pageable);
        List<List<AcOvertimeApplication>> res = new ArrayList<>();
        for (HashMap temp : checkList) {
            String deptName = (String) temp.get("dept_name");
            String sectionName = (String) temp.get("section_name");
//            System.out.println("dept_name: " + temp.get("dept_name"));
//            System.out.println("section_name: " + temp.get("section_name"));
            List<AcOvertimeApplication> listMap = new ArrayList<>();
//            listMap = acOvertimeApplicationDao.getOvertimeApplicationReviews(page, criteria, (String) temp.get("dept_name"), (String) temp.get("section_name"));
            if (!"".equals((String) temp.get("dept_name")) && temp.get("dept_name") != null && !"".equals((String) temp.get("section_name")) && temp.get("section_name") != null) {
                if ("待复核申请单".equals(criteria.getReqStatus())) {
                    listMap = acOvertimeApplicationDao.getOvertimeApplicationReviews(page, criteria, deptName, sectionName);
                } else if ("已复核申请单".equals(criteria.getReqStatus())) {
                    listMap = acOvertimeApplicationDao.getReviewedList(page, criteria, deptName, sectionName);
                } else if ("已过期申请单".equals(criteria.getReqStatus())) {
                    listMap = acOvertimeApplicationDao.getExpirationReview(page, criteria, deptName, sectionName);
                }
            } else if (!"".equals((String) temp.get("dept_name")) && temp.get("dept_name") != null) {
                if ("待复核申请单".equals(criteria.getReqStatus())) {
                    listMap = acOvertimeApplicationDao.getOvertimeApplicationReviews2(page, criteria, deptName);
                } else if ("已复核申请单".equals(criteria.getReqStatus())) {
                    listMap = acOvertimeApplicationDao.getReviewedList2(page, criteria, deptName);
                } else if ("已过期申请单".equals(criteria.getReqStatus())) {
                    listMap = acOvertimeApplicationDao.getExpirationReview2(page, criteria, deptName);
                }
            }
//            System.out.println("listMap: " + listMap);
            if (listMap != null && listMap.size() > 0) {
                res.add(listMap);
            }
        }
        List<AcOvertimeApplication> mergedList = null;
        if (res != null && res.size() > 0) {
            mergedList = res.stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }
//        System.out.println("mergedList: " + mergedList);
        return PageUtil.toPage(acOvertimeApplicationMapper.toDto(mergedList), page.getTotal());
    }

    @Override
    public List<HashMap<String, Object>> checkReviewUser(String workCard) {
        return acOvertimeApplicationDao.checkReviewUser(workCard);
    }

    @Override
    public List<HashMap<String, Object>> getRecentlyRecord(String workCard, String endTime) {
        List<HashMap<String, Object>> res = new ArrayList<>();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedDate = dateFormat.parse(endTime);
            res = acOvertimeApplicationDao.getRecentlyRecord(workCard, endTime);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return res;
    }

    @Override
    public void updateReviewFormData(List<ReviewFormData> updatedSubFormData) {
        acOvertimeApplicationDao.updateReviewFormData(updatedSubFormData);
    }

    @Override
    public List<OvertimeLeaveCheckList> getOvertimeLeaveStatisticsQueryList(AcOvertimeApplicationQueryCriteria criteria, Pageable pageable, String checkMonth, Boolean isHr, Integer employeeType, String department, String administrativeOffice, String groups) {
        Page<OvertimeLeaveCheckList> page = PageUtil.startPage(pageable);
        List<OvertimeLeaveCheckList> overtimeApplications = new ArrayList<OvertimeLeaveCheckList>();
        Integer pages = pageable.getPageNumber();
        Integer sizes = pageable.getPageSize();
        // 判断是不是复核人员
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        List<HashMap<String, Object>> hashMaps = acOvertimeApplicationDao.checkReviewUser(workCard);
        if (hashMaps == null || hashMaps.size() == 0) {
            hashMaps = acOvertimeApplicationDao.checkReviewUser2(workCard);
        }
        if (isHr && isHr != null) {
            // 人力资源部可以查询全部人的信息
            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryList4(checkMonth, pages, sizes);
        } else if (hashMaps.size() != 0) {
            Integer deptId = acOvertimeApplicationDao.getDetpId( "%" + department + "%");
            Integer administrativeOfficeId = acOvertimeApplicationDao.getDetpId("%" +administrativeOffice + "%");
            // 主管级别查询相同科室的人
            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryList2(checkMonth, deptId, administrativeOfficeId, pages, sizes);
            System.out.println("overtimeApplications.size() = " + overtimeApplications.size());
//            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryList2(checkMonth, department, administrativeOffice, pages, sizes);
            if (overtimeApplications.size() == 0) {
                overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveBySection(checkMonth, deptId, pages, sizes);
            }
        } else if (employeeType == 1) {
            Integer deptId = acOvertimeApplicationDao.getDetpId( "%" + department + "%");
            Integer administrativeOfficeId = acOvertimeApplicationDao.getDetpId("%" +administrativeOffice + "%");
            // 主管级别查询相同科室的人
            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryList2(checkMonth, deptId, administrativeOfficeId, pages, sizes);
//            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryList2(checkMonth, department, administrativeOffice, pages, sizes);
            if (overtimeApplications.size() == 0) {
                overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveBySection(checkMonth, deptId, pages, sizes);
            }
        } else if (employeeType == 2) {
            Integer deptId = acOvertimeApplicationDao.getDetpId( "%" + department + "%");
            ArrayList<Integer> deptIds = new ArrayList<>();
            deptIds = acOvertimeApplicationDao.getDeptIds(fndUserDTO.getEmployee().getId());
            if (!deptIds.contains(deptId)) {
                deptIds.add(deptId); // 如果不存在，则添加
            }
            // 经理级别查询
//            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryList3(checkMonth, deptId, pages, sizes);
            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryList3(checkMonth, deptIds.toArray(new Integer[0]), pages, sizes);
        } else if (employeeType == 5) {
            // 车间班组长
            Integer deptId = acOvertimeApplicationDao.getDetpId( "%" + department + "%");
            Integer administrativeOfficeId = acOvertimeApplicationDao.getDetpId("%" +administrativeOffice + "%");
            Integer groupsId = acOvertimeApplicationDao.getDetpId("%" + groups + "%");
            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryListTeam(checkMonth, deptId, administrativeOfficeId, groupsId, pages, sizes);
        } else {
            // 普通人员自己看自己的
            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryListMyList(checkMonth, workCard, pages, sizes);
        }
        overtimeApplications = overtimeApplications.stream()
                .map(overtimeLeaveCheckList -> {
                    if (overtimeLeaveCheckList.getRestDayOvertime() != null && overtimeLeaveCheckList.getTXXS() != null) {
                        // 计算差值
                        float diff = overtimeLeaveCheckList.getRestDayOvertime() - overtimeLeaveCheckList.getTXXS();
                        // 如果差值小于0，则设置为0
                        overtimeLeaveCheckList.setSyjbgs(diff >= 0 ? diff : 0);
                        return overtimeLeaveCheckList;
                    } else {
                        // 如果任一值为null，则不修改Syjbgs，直接返回原对象
                        return overtimeLeaveCheckList;
                    }
                })
                .collect(Collectors.toList());
//        overtimeApplications = overtimeApplications.stream()
//                .map(overtimeLeaveCheckList -> {
//                    if (overtimeLeaveCheckList.getRestDayOvertime() != null && overtimeLeaveCheckList.getTXXS() != null) {
//                        overtimeLeaveCheckList.setSyjbgs(overtimeLeaveCheckList.getRestDayOvertime() - overtimeLeaveCheckList.getTXXS());
//                        return overtimeLeaveCheckList;
//                    } else {
//                        return overtimeLeaveCheckList;
//                    }
//                })
//                .collect(Collectors.toList());
        return overtimeApplications;
    }

    @Override
    public HashMap<String, List<HashMap<String, Object>>> getOvertimeLeaveInfoByCheckMonthAndWorkCard(String checkMonth, String workCard) {
        HashMap<String, List<HashMap<String, Object>>> res = new HashMap<String, List<HashMap<String, Object>>>(16);
        List<HashMap<String, Object>> overtimeList = acOvertimeApplicationDao.getOvertimeByCheckMonthAndWorkCard(checkMonth, workCard);
        List<HashMap<String, Object>> leaveList = acOvertimeApplicationDao.getLeaveByCheckMonthAndWorkCard(checkMonth, workCard);
        res.put("overtimeList", overtimeList);
        res.put("leaveList", leaveList);
        return res;
    }

    @Override
    public void downloadOvertimeLeaveList(List<OvertimeLeaveCheckList> overtimeLeaveStatisticsQueryList, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (OvertimeLeaveCheckList overtimeLeaveCheckList : overtimeLeaveStatisticsQueryList) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("日期", overtimeLeaveCheckList.getFormattedMonth());
            map.put("姓名", overtimeLeaveCheckList.getNickName());
            map.put("工牌号", overtimeLeaveCheckList.getUserName());
            map.put("部门", overtimeLeaveCheckList.getDeptName());
            map.put("科室", overtimeLeaveCheckList.getSectionName());
            map.put("班组", overtimeLeaveCheckList.getGroupName());
            map.put("工作日加班", overtimeLeaveCheckList.getWorkDayOvertime());
            map.put("休息日加班", overtimeLeaveCheckList.getRestDayOvertime());
            map.put("调休时数", overtimeLeaveCheckList.getTXXS());
            map.put("调休天数", overtimeLeaveCheckList.getTXTS());
            map.put("年假", overtimeLeaveCheckList.getNJ());
            map.put("事假", overtimeLeaveCheckList.getSJ());
            map.put("病假", overtimeLeaveCheckList.getBJ());
            map.put("婚假", overtimeLeaveCheckList.getHJ());
            map.put("产假", overtimeLeaveCheckList.getCJ());
            map.put("陪产假", overtimeLeaveCheckList.getPCJ());
            map.put("丧假", overtimeLeaveCheckList.getSAJ());
            map.put("工伤假", overtimeLeaveCheckList.getGSJ());
            map.put("计划生育假", overtimeLeaveCheckList.getJHSYJ());
            map.put("公假", overtimeLeaveCheckList.getGJ());
            map.put("育儿假", overtimeLeaveCheckList.getHLJ());
            map.put("护理假", overtimeLeaveCheckList.getYEJ());
            map.put("剩余休息日加班工时", overtimeLeaveCheckList.getSyjbgs());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<OvertimeLeaveCheckList> getOvertimeLeaveStatisticsQueryListDownload(Pageable pageable, String checkMonth, Boolean isHr, Integer employeeType, String department, String administrativeOffice, String groups) {
        Page<OvertimeLeaveCheckList> page = PageUtil.startPage(pageable);
        List<OvertimeLeaveCheckList> overtimeApplications = new ArrayList<OvertimeLeaveCheckList>();
        Integer pages = pageable.getPageNumber();
        Integer sizes = pageable.getPageSize();
        // 判断是不是复核人员
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        List<HashMap<String, Object>> hashMaps = acOvertimeApplicationDao.checkReviewUser(workCard);
        if (hashMaps == null || hashMaps.size() == 0) {
            hashMaps = acOvertimeApplicationDao.checkReviewUser2(workCard);
        }
        if (isHr && isHr != null) {
            // 人力资源部可以查询全部人的信息
            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryList4(checkMonth, pages, sizes);
//            System.out.println("overtimeApplications: " + overtimeApplications.size());
        } else if (hashMaps.size() != 0) {
            Integer deptId = acOvertimeApplicationDao.getDetpId( "%" + department + "%");
            Integer administrativeOfficeId = acOvertimeApplicationDao.getDetpId("%" +administrativeOffice + "%");
            // 主管级别查询相同科室的人
            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryList2(checkMonth, deptId, administrativeOfficeId, pages, sizes);
            System.out.println("overtimeApplications.size() = " + overtimeApplications.size());
//            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryList2(checkMonth, department, administrativeOffice, pages, sizes);
            if (overtimeApplications.size() == 0) {
                overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveBySection(checkMonth, deptId, pages, sizes);
            }
        } else if (employeeType == 1) {
            Integer deptId = acOvertimeApplicationDao.getDetpId( "%" + department + "%");
            Integer administrativeOfficeId = acOvertimeApplicationDao.getDetpId("%" +administrativeOffice + "%");
            // 主管级别查询相同科室的人
            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryList2(checkMonth, deptId, administrativeOfficeId, pages, sizes);
            if (overtimeApplications.size() == 0) {
                overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveBySection(checkMonth, deptId, pages, sizes);
            }
        } else if (employeeType == 2) {
            Integer deptId = acOvertimeApplicationDao.getDetpId( "%" + department + "%");
            ArrayList<Integer> deptIds = new ArrayList<>();
            deptIds = acOvertimeApplicationDao.getDeptIds(fndUserDTO.getEmployee().getId());
            if (!deptIds.contains(deptId)) {
                deptIds.add(deptId); // 如果不存在，则添加
            }
            // 经理级别查询
//            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryList3(checkMonth, deptId, pages, sizes);
            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryList3(checkMonth, deptIds.toArray(new Integer[0]), pages, sizes);
        } else if (employeeType == 5) {
            // 车间班组长
            Integer deptId = acOvertimeApplicationDao.getDetpId( "%" + department + "%");
            Integer administrativeOfficeId = acOvertimeApplicationDao.getDetpId("%" +administrativeOffice + "%");
            Integer groupsId = acOvertimeApplicationDao.getDetpId("%" + groups + "%");
            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryListTeam(checkMonth, deptId, administrativeOfficeId, groupsId, pages, sizes);
        } else {
            // 普通人员自己看自己的
            overtimeApplications = acOvertimeApplicationDao.getOvertimeLeaveStatisticsQueryListMyList(checkMonth, workCard, pages, sizes);
        }
        overtimeApplications = overtimeApplications.stream()
                .map(overtimeLeaveCheckList -> {
                    if (overtimeLeaveCheckList.getRestDayOvertime() != null && overtimeLeaveCheckList.getTXXS() != null) {
                        overtimeLeaveCheckList.setSyjbgs(overtimeLeaveCheckList.getRestDayOvertime() - overtimeLeaveCheckList.getTXXS());
                        return overtimeLeaveCheckList;
                    } else {
                        return overtimeLeaveCheckList;
                    }
                })
                .collect(Collectors.toList());
        return overtimeApplications;
    }


}
