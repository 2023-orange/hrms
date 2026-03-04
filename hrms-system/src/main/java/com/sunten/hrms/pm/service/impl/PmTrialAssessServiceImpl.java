package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndUserDao;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.pm.dao.PmEmployeeDao;
import com.sunten.hrms.pm.dao.PmEmployeeEntryDao;
import com.sunten.hrms.pm.dao.PmTrialAssessDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeeEntry;
import com.sunten.hrms.pm.domain.PmTrialAssess;
import com.sunten.hrms.pm.dto.PmTrialAssessDTO;
import com.sunten.hrms.pm.dto.PmTrialAssessQueryCriteria;
import com.sunten.hrms.pm.mapper.PmTrialAssessMapper;
import com.sunten.hrms.pm.service.PmTrialAssessService;
import com.sunten.hrms.swm.dao.SwmEmployeeDao;
import com.sunten.hrms.swm.domain.SwmEmployee;
import com.sunten.hrms.tool.dao.ToolEmailServerDao;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.domain.ToolEmailServer;
import com.sunten.hrms.tool.service.ToolEmailInterfaceService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 新晋员工试用期考核表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-05-07
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmTrialAssessServiceImpl extends ServiceImpl<PmTrialAssessDao, PmTrialAssess> implements PmTrialAssessService {
    private final PmTrialAssessDao pmTrialAssessDao;
    private final PmTrialAssessMapper pmTrialAssessMapper;
    private final ToolEmailInterfaceService toolEmailInterfaceService;
    private final ToolEmailServerDao toolEmailServerDao;
    private final PmEmployeeEntryDao pmEmployeeEntryDao;
    private final PmEmployeeDao pmEmployeeDao;
    private final SwmEmployeeDao swmEmployeeDao;
    private final FndUserDao fndUserDao;

    public PmTrialAssessServiceImpl(PmTrialAssessDao pmTrialAssessDao, PmTrialAssessMapper pmTrialAssessMapper, ToolEmailInterfaceService toolEmailInterfaceService, ToolEmailServerDao toolEmailServerDao, PmEmployeeEntryDao pmEmployeeEntryDao, PmEmployeeDao pmEmployeeDao, SwmEmployeeDao swmEmployeeDao, FndUserDao fndUserDao) {
        this.pmTrialAssessDao = pmTrialAssessDao;
        this.pmTrialAssessMapper = pmTrialAssessMapper;
        this.toolEmailInterfaceService = toolEmailInterfaceService;
        this.toolEmailServerDao = toolEmailServerDao;
        this.pmEmployeeEntryDao = pmEmployeeEntryDao;
        this.pmEmployeeDao = pmEmployeeDao;
        this.swmEmployeeDao = swmEmployeeDao;
        this.fndUserDao = fndUserDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmTrialAssessDTO insert(PmTrialAssess trialAssessNew) {
        // TODO 验证是否存在申请
        List<PmTrialAssess> exitList = pmTrialAssessDao.listByEmployeeId(trialAssessNew.getPmEmployee().getId());
        if (exitList.size() >0) {
            throw new InfoCheckWarningMessException("该员工已存在或正在进行试用期考核！");
        }
        pmTrialAssessDao.insertAllColumn(trialAssessNew);
        // 如果是从试用期管理列表中新增，则修改试用标记
        if (trialAssessNew.getEntryId() != null) {
            PmEmployeeEntry pmEmployeeEntry = new PmEmployeeEntry();
            pmEmployeeEntry.setId(trialAssessNew.getEntryId());
            pmEmployeeEntry.setAssessFlag("Y");
            pmEmployeeEntryDao.updateAssessFlag(pmEmployeeEntry);
        }
        // TODO 发送邮件通知本人提交工作总结
        StringBuffer content = new StringBuffer();
        FndUser sendEmployee = fndUserDao.getByKeyEmployeeId(trialAssessNew.getPmEmployee().getId());
        // 如果员工本人没有邮箱，则找直属上级，如果再找不到，
        System.out.println(sendEmployee);
        String sendToEmaild = sendEmployee.getEmail();
        if (sendToEmaild == null || "".equals(sendToEmaild) || sendToEmaild == "@in-sunten.com") {
            PmEmployee leaderEmployee = pmEmployeeDao.getByKey(trialAssessNew.getLeaderBy(),null);
            if (leaderEmployee != null) {
                content.append(leaderEmployee.getName());
                content.append("，您好：\n");
                content.append(sendEmployee.getDescription());
                content.append("有一份试用期考核申请正在进行，需要该员工提交工作总结，请您提醒员工本人登录HR系统进行操作");
                sendToEmaild = leaderEmployee.getEmailInside();
            }
        } else {
            content.append(sendEmployee.getDescription());
            content.append("，你好：\n");
            content.append("你有一份试用期考核申请正在进行，需要提交工作总结，请登录HR系统进行操作");
        }
        if (sendToEmaild != null && !"".equals(sendToEmaild)) {
            ToolEmailServer toolEmailServer = toolEmailServerDao.getByKey(7L); // 内部HR发送邮箱
            ToolEmailInterface sendEmail = new ToolEmailInterface();
            sendEmail.setSendTo(sendToEmaild);
            sendEmail.setMailSubject("员工试用期考核提示");
            sendEmail.setEmailServer(toolEmailServer);
            sendEmail.setStatus("PLAN");
            sendEmail.setPlannedDate(LocalDateTime.now());
            sendEmail.setMailContent(content.toString());
//            toolEmailInterfaceService.sendAndSave(sendEmail,true);
            toolEmailInterfaceService.insert(sendEmail);
            System.out.println("发送试用期考核邮件成功");
        } else {
            System.out.println("发送试用期考核邮件失败，找不到员工本人以及其直属上级邮箱！");
        }

        return pmTrialAssessMapper.toDto(trialAssessNew);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmTrialAssess trialAssess = new PmTrialAssess();
        trialAssess.setId(id);
        this.delete(trialAssess);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmTrialAssess trialAssess) {
        // TODO    确认删除前是否需要做检查
        pmTrialAssessDao.deleteByEntityKey(trialAssess);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmTrialAssess trialAssessNew) {
        PmTrialAssess trialAssessInDb = Optional.ofNullable(pmTrialAssessDao.getByKey(trialAssessNew.getId())).orElseGet(PmTrialAssess::new);
        ValidationUtil.isNull(trialAssessInDb.getId() ,"TrialAssess", "id", trialAssessNew.getId());
        trialAssessNew.setId(trialAssessInDb.getId());
        pmTrialAssessDao.updateAllColumnByKey(trialAssessNew);
    }

    @Override
    public void submitWorkSummary(PmTrialAssess trialAssessNew) {
        PmTrialAssess trialAssessInDb = Optional.ofNullable(pmTrialAssessDao.getByKey(trialAssessNew.getId())).orElseGet(PmTrialAssess::new);
        ValidationUtil.isNull(trialAssessInDb.getId() ,"TrialAssess", "id", trialAssessNew.getId());
        trialAssessNew.setId(trialAssessInDb.getId());
        if (trialAssessInDb.getFileDegree() != null) {
            trialAssessNew.setFileDegree(trialAssessInDb.getFileDegree() + 1);
        }
        pmTrialAssessDao.updateBySubmitWorkSummary(trialAssessNew);
        // 邮件通知直属上级领导进行考核
        PmEmployee leaderEmployee = pmEmployeeDao.getByKey(trialAssessNew.getLeaderBy(),null);
        if (leaderEmployee != null) {
            PmEmployee sendEmployee = pmEmployeeDao.getByKey(trialAssessNew.getPmEmployee().getId(),null);
            StringBuffer content = new StringBuffer();
            content.append(leaderEmployee.getName());
            content.append("，您好：\n");
            content.append(sendEmployee.getName());
            content.append("该员工已提交工作总结，请您登录HR系统进行考核操作");
            String sendToEmaild = leaderEmployee.getEmailInside();
            if (sendToEmaild != null && !"".equals(sendToEmaild)) {
                ToolEmailServer toolEmailServer = toolEmailServerDao.getByKey(7L); // 内部HR发送邮箱
                ToolEmailInterface sendEmail = new ToolEmailInterface();
                sendEmail.setSendTo(sendToEmaild);
                sendEmail.setMailSubject("员工试用期考核提示");
                sendEmail.setEmailServer(toolEmailServer);
                sendEmail.setStatus("PLAN");
                sendEmail.setPlannedDate(LocalDateTime.now());
                sendEmail.setMailContent(content.toString());
//                toolEmailInterfaceService.sendAndSave(sendEmail,true);
                toolEmailInterfaceService.insert(sendEmail);
                System.out.println("发送试用期考核邮件成功");
            } else {
                System.out.println("发送试用期考核邮件失败，找不到该考核申请直属上级的邮箱！");
            }
        } else {
            System.out.println("发送试用期考核邮件失败，找不到该考核申请的直属上级！");
        }
    }

    @Override
    public void updateByRebackWorkSummary(PmTrialAssess trialAssessNew) {
        PmTrialAssess trialAssessInDb = Optional.ofNullable(pmTrialAssessDao.getByKey(trialAssessNew.getId())).orElseGet(PmTrialAssess::new);
        ValidationUtil.isNull(trialAssessInDb.getId() ,"TrialAssess", "id", trialAssessNew.getId());
        trialAssessNew.setId(trialAssessInDb.getId());
        pmTrialAssessDao.updateByRebackWorkSummary(trialAssessNew);

        // 邮件通知本人重新提交工作总结
        StringBuffer content = new StringBuffer();
        PmEmployee sendEmployee = pmEmployeeDao.getByKey(trialAssessNew.getPmEmployee().getId(),null);
        // 如果员工本人没有邮箱，则找直属上级，如果再找不到，
        String sendToEmaild = sendEmployee.getEmailInside();
        if (sendToEmaild == null || "".equals(sendToEmaild)) {
            PmEmployee leaderEmployee = pmEmployeeDao.getByKey(trialAssessNew.getLeaderBy(),null);
            if (leaderEmployee != null) {
                content.append(leaderEmployee.getName());
                content.append("，您好：\n");
                content.append(sendEmployee.getName());
                content.append("有一份试用期考核申请正在进行，需要该员工重新提交工作总结，请您提醒员工本人登录HR系统进行操作");
                sendToEmaild = leaderEmployee.getEmailInside();
            }
        } else {
            content.append(sendEmployee.getName());
            content.append("，你好：\n");
            content.append("你在试用期考核中提交的工作总结已被领导打回，请修改后重新提交，登录HR系统进行操作");
        }
        if (sendToEmaild != null && !"".equals(sendToEmaild)) {
            ToolEmailServer toolEmailServer = toolEmailServerDao.getByKey(7L); // 内部HR发送邮箱
            ToolEmailInterface sendEmail = new ToolEmailInterface();
            sendEmail.setSendTo(sendToEmaild);
            sendEmail.setMailSubject("员工试用期考核提示");
            sendEmail.setEmailServer(toolEmailServer);
            sendEmail.setStatus("PLAN");
            sendEmail.setPlannedDate(LocalDateTime.now());
            sendEmail.setMailContent(content.toString());
//            toolEmailInterfaceService.sendAndSave(sendEmail,true);
            toolEmailInterfaceService.insert(sendEmail);
            System.out.println("发送试用期考核邮件成功");
        } else {
            System.out.println("发送试用期考核邮件失败，找不到员工本人以及其直属上级邮箱！");
        }

    }

    @Override
    public PmTrialAssessDTO getByKey(Long id) {
        PmTrialAssess trialAssess = Optional.ofNullable(pmTrialAssessDao.getByKey(id)).orElseGet(PmTrialAssess::new);
        ValidationUtil.isNull(trialAssess.getId() ,"TrialAssess", "id", id);
        return pmTrialAssessMapper.toDto(trialAssess);
    }

    @Override
    public List<PmTrialAssessDTO> listAll(PmTrialAssessQueryCriteria criteria) {
        return pmTrialAssessMapper.toDto(pmTrialAssessDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmTrialAssessQueryCriteria criteria, Pageable pageable) {
        Page<PmTrialAssess> page = PageUtil.startPage(pageable);
        List<PmTrialAssess> trialAssesss = pmTrialAssessDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmTrialAssessMapper.toDto(trialAssesss), page.getTotal());
    }

    @Override
    public void download(List<PmTrialAssessDTO> trialAssessDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmTrialAssessDTO trialAssessDTO : trialAssessDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("姓名", trialAssessDTO.getPmEmployee().getName());
            map.put("工牌", trialAssessDTO.getPmEmployee().getWorkCard());
            map.put("部门", trialAssessDTO.getPmEmployee().getDeptName());
            map.put("科室", trialAssessDTO.getPmEmployee().getDepartment());
            map.put("考核起始日期", trialAssessDTO.getBeginDate());
            map.put("考核结束日期", trialAssessDTO.getEndDate());
            map.put("出勤情况评分", trialAssessDTO.getAttendanceGrade());
            map.put("工作态度评分", trialAssessDTO.getWorkAttitudeGrade());
            map.put("工作效率评分", trialAssessDTO.getWorkIfficiencyGrade());
            map.put("工作能力及项目技能评分", trialAssessDTO.getCapacityAndSkill());
            map.put("服从性及合作性评分", trialAssessDTO.getComplianceAndCooperation());
            map.put("总评分", trialAssessDTO.getTotalGrade());
            map.put("总评语", trialAssessDTO.getLastEvaluate());
            map.put("试用意见（录用、不录用）", trialAssessDTO.getTrailResult());
            map.put("拟定岗位", trialAssessDTO.getJobName());
            map.put("职类", trialAssessDTO.getCategory());
            map.put("职种", trialAssessDTO.getJobClass());
            map.put("建议包干工资", trialAssessDTO.getLumpSumWage());
            map.put("建议岗位技能工资", trialAssessDTO.getBaseSalary());
            map.put("建议目标绩效工资", trialAssessDTO.getPerformanceSalary());
            map.put("OA审批单号", trialAssessDTO.getOaOrder());
            map.put("审批结束日期", trialAssessDTO.getApprovalDate());
            map.put("有效标识", trialAssessDTO.getEnabledFlag());
            map.put("提交审批标识", trialAssessDTO.getSubmitFlag());
            map.put("当前审批节点", trialAssessDTO.getApprovalNode());
            map.put("当前审批人", trialAssessDTO.getApprovalEmployee());
            map.put("id", trialAssessDTO.getId());
            map.put("创建时间", trialAssessDTO.getCreateTime());
            map.put("创建人", trialAssessDTO.getCreateBy());
            map.put("最后修改时间", trialAssessDTO.getUpdateTime());
            map.put("最后修改人", trialAssessDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public PmTrialAssessDTO getByReqCode(String reqCode) {
        PmTrialAssess pmTrialAssess = pmTrialAssessDao.getByReqCode(reqCode);
        SwmEmployee swmEmployee = swmEmployeeDao.getSwmEmpByEmpId(pmTrialAssess.getPmEmployee().getId());
        pmTrialAssess.setSwmEmployee(swmEmployee);
        return pmTrialAssessMapper.toDto(pmTrialAssess);
    }

    @Override
    public void writeOaApprovalResult(PmTrialAssess pmTrialAssess) {
        PmTrialAssess oldAssess =  Optional.ofNullable(pmTrialAssessDao.getByKey(pmTrialAssess.getId())).orElseGet(PmTrialAssess::new);
        ValidationUtil.isNull(oldAssess.getId() ,"TrialAssess", "id", pmTrialAssess.getId());
        if (null != pmTrialAssess && null != pmTrialAssess.getApprovalResult() && !"".equals(pmTrialAssess.getApprovalResult())) {
            // 审批结束
            oldAssess.setApprovalResult(pmTrialAssess.getApprovalResult());
            pmTrialAssessDao.updateByApprovalEnd(oldAssess);
            // 如果是不通过，并且是从入职情况列表中生成的申请，则把试用标记设置为N
            if (null != pmTrialAssess.getEntryId() && pmTrialAssess.getApprovalResult().equals("notPass")) {
                PmEmployeeEntry employeeEntry = new PmEmployeeEntry();
                employeeEntry.setId(pmTrialAssess.getEntryId());
                employeeEntry.setAssessFlag("N");
                pmEmployeeEntryDao.updateAssessFlag(employeeEntry);
            }
        } else { // 审批中
            if (null == pmTrialAssess.getApprovalNode() || "".equals(pmTrialAssess.getApprovalNode())) {
                throw new InfoCheckWarningMessException("OA当前审批节点不能为空！");
            }
            oldAssess.setApprovalEmployee(pmTrialAssess.getApprovalEmployee());
            oldAssess.setApprovalNode(pmTrialAssess.getApprovalNode());
            oldAssess.setApprovalBaseSalary(pmTrialAssess.getApprovalBaseSalary());
            oldAssess.setApprovalPerformanceSalary(pmTrialAssess.getApprovalPerformanceSalary());
            oldAssess.setApprovalLumpSumWage(pmTrialAssess.getApprovalLumpSumWage());
            pmTrialAssessDao.updateByApprovalUnderwar(oldAssess);
        }
    }
}
