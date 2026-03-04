package com.sunten.hrms.td.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndDeptDao;
import com.sunten.hrms.fnd.vo.DeptEmp;
import com.sunten.hrms.pm.dao.PmEmployeeDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.td.dao.TdCredentialDao;
import com.sunten.hrms.td.dao.TdTrainEmployeeJurisdictionDao;
import com.sunten.hrms.td.domain.TdCredential;
import com.sunten.hrms.td.domain.TdJobAuthentication;
import com.sunten.hrms.td.dao.TdJobAuthenticationDao;
import com.sunten.hrms.td.domain.TdTrainEmployeeJurisdiction;
import com.sunten.hrms.td.dto.TdJobGradingQueryCriteria;
import com.sunten.hrms.td.service.TdJobAuthenticationService;
import com.sunten.hrms.td.dto.TdJobAuthenticationDTO;
import com.sunten.hrms.td.dto.TdJobAuthenticationQueryCriteria;
import com.sunten.hrms.td.mapper.TdJobAuthenticationMapper;
import com.sunten.hrms.tool.dao.ToolEmailServerDao;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.domain.ToolEmailServer;
import com.sunten.hrms.tool.service.ToolEmailInterfaceService;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
 * 上岗认证表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-06-22
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdJobAuthenticationServiceImpl extends ServiceImpl<TdJobAuthenticationDao, TdJobAuthentication> implements TdJobAuthenticationService {
    private final TdJobAuthenticationDao tdJobAuthenticationDao;
    private final TdJobAuthenticationMapper tdJobAuthenticationMapper;
    private final FndDeptDao fndDeptDao;
    private final TdTrainEmployeeJurisdictionDao tdTrainEmployeeJurisdictionDao;
    private final PmEmployeeDao pmEmployeeDao;
    private final ToolEmailServerDao toolEmailServerDao;
    private final ToolEmailInterfaceService toolEmailInterfaceService;
    private final TdCredentialDao tdCredentialDao;

    public TdJobAuthenticationServiceImpl(TdJobAuthenticationDao tdJobAuthenticationDao, TdJobAuthenticationMapper tdJobAuthenticationMapper, FndDeptDao fndDeptDao, TdTrainEmployeeJurisdictionDao tdTrainEmployeeJurisdictionDao, PmEmployeeDao pmEmployeeDao, ToolEmailServerDao toolEmailServerDao, ToolEmailInterfaceService toolEmailInterfaceService, TdCredentialDao tdCredentialDao) {
        this.tdJobAuthenticationDao = tdJobAuthenticationDao;
        this.tdJobAuthenticationMapper = tdJobAuthenticationMapper;
        this.fndDeptDao = fndDeptDao;
        this.tdTrainEmployeeJurisdictionDao = tdTrainEmployeeJurisdictionDao;
        this.pmEmployeeDao = pmEmployeeDao;
        this.toolEmailServerDao = toolEmailServerDao;
        this.toolEmailInterfaceService = toolEmailInterfaceService;
        this.tdCredentialDao = tdCredentialDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdJobAuthenticationDTO insert(TdJobAuthentication jobAuthenticationNew) {
        tdJobAuthenticationDao.insertAllColumn(jobAuthenticationNew);
        // todo 新增后新增一条证书记录
        TdCredential credential = new TdCredential();
        credential.setEmployeeId(jobAuthenticationNew.getEmployeeId());
        credential.setCredentialName(jobAuthenticationNew.getJob());
        credential.setCredentialType("上岗认证证书");
        credential.setGrantOrganization("顺特电气");
        credential.setGrantDate(jobAuthenticationNew.getCredentialGrantDate());
        credential.setValidityDate(jobAuthenticationNew.getEnabledTime());
//        StringBuilder addressStr = new StringBuilder();
        credential.setAdminAdress(jobAuthenticationNew.getSurety());
        credential.setStoreAdress(jobAuthenticationNew.getSurety());
        credential.setAppraisalFlag(true);
        credential.setEnabledFlag(true);
        credential.setAdminAdressType("部门科室");
        credential.setStoreAdressType("个人");
        tdCredentialDao.insertAllColumn(credential);
        return tdJobAuthenticationMapper.toDto(jobAuthenticationNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdJobAuthentication jobAuthentication = new TdJobAuthentication();
        jobAuthentication.setId(id);
        jobAuthentication.setEnabledFlag(false);
        this.delete(jobAuthentication);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdJobAuthentication jobAuthentication) {
        // TODO    确认删除前是否需要做检查
        tdJobAuthenticationDao.updateEnableFlag(jobAuthentication);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdJobAuthentication jobAuthenticationNew) {
        TdJobAuthentication jobAuthenticationInDb = Optional.ofNullable(tdJobAuthenticationDao.getByKey(jobAuthenticationNew.getId())).orElseGet(TdJobAuthentication::new);
        ValidationUtil.isNull(jobAuthenticationInDb.getId() ,"JobAuthentication", "id", jobAuthenticationNew.getId());
        jobAuthenticationNew.setId(jobAuthenticationInDb.getId());
        tdJobAuthenticationDao.updateAllColumnByKey(jobAuthenticationNew);
    }

    @Override
    public TdJobAuthenticationDTO getByKey(Long id) {
        TdJobAuthentication jobAuthentication = Optional.ofNullable(tdJobAuthenticationDao.getByKey(id)).orElseGet(TdJobAuthentication::new);
        ValidationUtil.isNull(jobAuthentication.getId() ,"JobAuthentication", "id", id);
        return tdJobAuthenticationMapper.toDto(jobAuthentication);
    }

    @Override
    public List<TdJobAuthenticationDTO> listAll(TdJobAuthenticationQueryCriteria criteria) {
        return tdJobAuthenticationMapper.toDto(tdJobAuthenticationDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdJobAuthenticationQueryCriteria criteria, Pageable pageable) {
        Page<TdJobAuthentication> page = PageUtil.startPage(pageable);
        List<TdJobAuthentication> jobAuthentications = tdJobAuthenticationDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdJobAuthenticationMapper.toDto(jobAuthentications), page.getTotal());
    }

    @Override
    public void download(List<TdJobAuthenticationDTO> jobAuthenticationDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdJobAuthenticationDTO jobAuthenticationDTO : jobAuthenticationDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("部门", jobAuthenticationDTO.getDeptName());
            map.put("科室", jobAuthenticationDTO.getDepartment());
            map.put("班组", jobAuthenticationDTO.getTeam());
            map.put("工号", jobAuthenticationDTO.getWorkCard());
            map.put("姓名", jobAuthenticationDTO.getName());
//            map.put("人事ID", jobAuthenticationDTO.getEmployeeId());
            map.put("工序", jobAuthenticationDTO.getProcess());
            map.put("认证岗位", jobAuthenticationDTO.getJob());
            map.put("认证岗位类别", jobAuthenticationDTO.getAuthenticationLevel());
            map.put("岗位级别", jobAuthenticationDTO.getLevel());
            map.put("培训认证负责人", jobAuthenticationDTO.getSurety());
            map.put("第一次通用考试成绩", jobAuthenticationDTO.getFirstGeneralGrade());
            map.put("第二次通用考试成绩", jobAuthenticationDTO.getSecondGeneralGrade());
//            map.put("第三次通用考试成绩", jobAuthenticationDTO.getThreeGeneralGrade());
            map.put("第一次岗位理论考试成绩", jobAuthenticationDTO.getFirstTheoryGrade());
            map.put("第二次岗位理论考试成绩", jobAuthenticationDTO.getSecondTheoryGrade());
//            map.put("第三次岗位理论考试成绩", jobAuthenticationDTO.getThreeTheoryGrade());
            map.put("第一次岗位实操评估成绩", jobAuthenticationDTO.getFirstOperationGrade());
            map.put("第二次岗位实操评估成绩", jobAuthenticationDTO.getSecondOperationGrade());
//            map.put("第三次岗位实操评估成绩", jobAuthenticationDTO.getThreeOperationGrade());
            map.put("证书发放日期", jobAuthenticationDTO.getCredentialGrantDate().toString());
//            map.put("有效标记", jobAuthenticationDTO.getEnabledFlag());
            map.put("证书截止日期", jobAuthenticationDTO.getEnabledTime().toString());
//            map.put("attribute2", jobAuthenticationDTO.getAttribute2());
//            map.put("attribute3", jobAuthenticationDTO.getAttribute3());
//            map.put("attribute1", jobAuthenticationDTO.getAttribute1());
//            map.put("id", jobAuthenticationDTO.getId());
//            map.put("创建时间", jobAuthenticationDTO.getCreateTime());
//            map.put("创建人", jobAuthenticationDTO.getCreateBy());
//            map.put("最后修改时间", jobAuthenticationDTO.getUpdateTime());
//            map.put("最后修改人", jobAuthenticationDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void JobAuthenticationRemind() {
        // 查询当前日期三个月内到期的上岗认证数据
        ToolEmailServer toolEmailServer = toolEmailServerDao.getByKey(7L); // 内部HR发送邮箱
        if (toolEmailServer == null) throw new InfoCheckWarningMessException("没找到相应的HR服务器邮箱");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<ToolEmailInterface> managerEmailList = null; // 主管邮件集合
        Set<String> managerStrList = null;
        List<ToolEmailInterface> authenticationEmailList = null; // 培训员邮件集合
        Set<String> authenticationStrList = null;
        List<ToolEmailInterface> sendEmailList = new ArrayList<>(); // 发送邮件集合
        List<TdJobAuthentication> jobAuthentications = tdJobAuthenticationDao.getDueToRemindAuthentication(); // 待提醒上岗认证记录

        if (jobAuthentications.size() >0) {
            /**
             * 取每条记录对应的培训员及主管（或直属上级领导）
             */
            managerEmailList = new ArrayList<>();
            authenticationEmailList = new ArrayList<>();
            for(TdJobAuthentication tdJobAuthentication: jobAuthentications) {
                PmEmployee managerEmployee = null; // 主管
                Long currentEmployeeID = tdJobAuthentication.getEmployeeId(); // 当前记录员工Id
                DeptEmp deptEmp = fndDeptDao.getDeptByEmployeeId(currentEmployeeID); // 员工所属部门
                TdTrainEmployeeJurisdiction tdTrainEmployeeJurisdiction = tdTrainEmployeeJurisdictionDao.getByDept(deptEmp.getFndDept().getId()); // 培训员id
                PmEmployee jurisdictionEmployee = pmEmployeeDao.getByKey(tdTrainEmployeeJurisdiction.getEmployeeId(),null); // 培训员人事信息
                if (jurisdictionEmployee != null) { // 培训员
                    managerStrList.add(jurisdictionEmployee.getEmailInside()); // 保存主管邮箱

                    ToolEmailInterface authenticationEmail = new ToolEmailInterface();
                    authenticationEmail.setSendTo(jurisdictionEmployee.getEmailInside()); // 设置培训员收件邮箱

                    StringBuffer content = new StringBuffer();
                    content.append("姓名："+tdJobAuthentication.getName());
                    content.append(",认证岗位："+tdJobAuthentication.getJob());
                    content.append(",认证等级："+tdJobAuthentication.getLevel());
                    content.append(",证书发放日期："+formatter.format(tdJobAuthentication.getCredentialGrantDate()));
                    authenticationEmail.setMailContent(content.toString());
                    authenticationEmailList.add(authenticationEmail);
                }

                if (deptEmp.getFndDept().getExtDepartmentId() != null) {
                    DeptEmp managerDept = fndDeptDao.getDeptAndEmployeeById(deptEmp.getFndDept().getExtDepartmentId()); // 主管
                    if (managerDept.getEmployee() != null ){
                        managerStrList.add(managerDept.getEmployee().getEmailInside()); // 保存主管邮箱

                        ToolEmailInterface managerEmail = new ToolEmailInterface();
                        managerEmail.setSendTo(managerDept.getEmployee().getEmailInside()); // 设置主管收件邮箱

                        StringBuffer content = new StringBuffer();
                        content.append("姓名："+tdJobAuthentication.getName());
                        content.append(",认证岗位："+tdJobAuthentication.getJob());
                        content.append(",认证等级："+tdJobAuthentication.getLevel());
                        content.append(",证书发放日期："+formatter.format(tdJobAuthentication.getCredentialGrantDate()));
                        managerEmail.setMailContent(content.toString());
                        managerEmailList.add(managerEmail);
                    }
                }
            }

            // 1、循环主管邮箱
            if(managerStrList != null && managerStrList.size() > 0) {
                for(String email :managerStrList) {
                    ToolEmailInterface sendEmail = new ToolEmailInterface();
                    sendEmail.setSendTo(email);
                    sendEmail.setMailSubject("以下员工的上岗认证证书即将到期，请登录HR系统进行查看。");
                    sendEmail.setEmailServer(toolEmailServer);
                    sendEmail.setStatus("PLAN");
                    sendEmail.setPlannedDate(LocalDateTime.now());
                    StringBuffer content = new StringBuffer();
                    managerEmailList.forEach(item -> {
                        if (item.getSendTo().equals(email)) {
                            String str = item.getMailContent() + "\n";
                            content.append(str);
                        }
                    });
                    sendEmail.setMailContent(content.toString());
                    sendEmailList.add(sendEmail);
                }
            }
            // 2、循环培训员邮箱
            if(authenticationStrList != null && authenticationStrList.size() > 0) {
                for(String email :authenticationStrList) {
                    ToolEmailInterface sendEmail = new ToolEmailInterface();
                    sendEmail.setSendTo(email);
                    sendEmail.setMailSubject("以下员工的上岗认证证书即将到期，请登录HR系统进行查看。");
                    sendEmail.setEmailServer(toolEmailServer);
                    sendEmail.setStatus("PLAN");
                    sendEmail.setPlannedDate(LocalDateTime.now());
                    StringBuffer content = new StringBuffer();
                    authenticationEmailList.forEach(item -> {
                        if (item.getSendTo().equals(email)) {
                            String str = item.getMailContent() + "\n";
                            content.append(str);
                        }
                    });
                    sendEmail.setMailContent(content.toString());
                    sendEmailList.add(sendEmail);
                }
            }

            if (sendEmailList.size() > 0) { // 插入邮件
                sendEmailList.forEach(sendEmail -> {
                    toolEmailInterfaceService.insert(sendEmail);
                });
            }
        }
    }

    @Override
    public  List<TdJobAuthenticationDTO> getAuthentication(TdJobGradingQueryCriteria criteria){
        return tdJobAuthenticationMapper.toDto(tdJobAuthenticationDao.getAuthentication(criteria));
    }
}
