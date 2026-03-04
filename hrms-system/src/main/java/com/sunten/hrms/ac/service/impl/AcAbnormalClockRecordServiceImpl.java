package com.sunten.hrms.ac.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcAbnormalClockRecordDao;
import com.sunten.hrms.ac.dao.AcAttendanceRecordHistoryDao;
import com.sunten.hrms.ac.domain.AcAbnormalClockRecord;
import com.sunten.hrms.ac.domain.AcAttendanceRecordHistory;
import com.sunten.hrms.ac.dto.AcAbnormalClockRecordDTO;
import com.sunten.hrms.ac.dto.AcAbnormalClockRecordQueryCriteria;
import com.sunten.hrms.ac.dto.AcAttendanceRecordHistoryQueryCriteria;
import com.sunten.hrms.ac.mapper.AcAbnormalClockRecordMapper;
import com.sunten.hrms.ac.service.AcAbnormalClockRecordService;
import com.sunten.hrms.ac.vo.RepulseEmailMes;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.tool.dao.ToolEmailInterfaceDao;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.service.ToolEmailInterfaceService;
import com.sunten.hrms.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录） 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcAbnormalClockRecordServiceImpl extends ServiceImpl<AcAbnormalClockRecordDao, AcAbnormalClockRecord> implements AcAbnormalClockRecordService {
    private final AcAbnormalClockRecordDao acAbnormalClockRecordDao;
    private final AcAbnormalClockRecordMapper acAbnormalClockRecordMapper;
    private final AcAttendanceRecordHistoryDao acAttendanceRecordHistoryDao;
    private final FndUserService fndUserService;
    private final AcEmployeeAttendanceServiceImpl acEmployeeAttendanceServiceImpl;
    private final ToolEmailInterfaceService toolEmailInterfaceService;

    @Value("${sunten.system-name}")
    private String systemName;
    public AcAbnormalClockRecordServiceImpl(AcAbnormalClockRecordDao acAbnormalClockRecordDao, AcAbnormalClockRecordMapper acAbnormalClockRecordMapper,
                                            AcAttendanceRecordHistoryDao acAttendanceRecordHistoryDao,
                                            FndUserService fndUserService, AcEmployeeAttendanceServiceImpl acEmployeeAttendanceServiceImpl,
                                            ToolEmailInterfaceService toolEmailInterfaceService) {
        this.acAbnormalClockRecordDao = acAbnormalClockRecordDao;
        this.acAbnormalClockRecordMapper = acAbnormalClockRecordMapper;
        this.acAttendanceRecordHistoryDao = acAttendanceRecordHistoryDao;
        this.fndUserService = fndUserService;
        this.acEmployeeAttendanceServiceImpl = acEmployeeAttendanceServiceImpl;
        this.toolEmailInterfaceService = toolEmailInterfaceService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcAbnormalClockRecordDTO insert(AcAbnormalClockRecord abnormalClockRecordNew) {
        acAbnormalClockRecordDao.insertAllColumn(abnormalClockRecordNew);
        return acAbnormalClockRecordMapper.toDto(abnormalClockRecordNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AcAbnormalClockRecord abnormalClockRecord = new AcAbnormalClockRecord();
        abnormalClockRecord.setId(id);
        this.delete(abnormalClockRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcAbnormalClockRecord abnormalClockRecord) {
        // TODO    确认删除前是否需要做检查
        acAbnormalClockRecordDao.deleteByEntityKey(abnormalClockRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcAbnormalClockRecord abnormalClockRecordNew) {
        AcAbnormalClockRecord abnormalClockRecordInDb = Optional.ofNullable(acAbnormalClockRecordDao.getByKey(abnormalClockRecordNew.getId())).orElseGet(AcAbnormalClockRecord::new);
        ValidationUtil.isNull(abnormalClockRecordInDb.getId(), "AbnormalClockRecord", "id", abnormalClockRecordNew.getId());
        abnormalClockRecordNew.setId(abnormalClockRecordInDb.getId());
        acAbnormalClockRecordDao.updateAllColumnByKey(abnormalClockRecordNew);
    }

    @Override
    public AcAbnormalClockRecordDTO getByKey(Long id) {
        AcAbnormalClockRecord abnormalClockRecord = Optional.ofNullable(acAbnormalClockRecordDao.getByKey(id)).orElseGet(AcAbnormalClockRecord::new);
        ValidationUtil.isNull(abnormalClockRecord.getId(), "AbnormalClockRecord", "id", id);
        return acAbnormalClockRecordMapper.toDto(abnormalClockRecord);
    }

    @Override
    public List<AcAbnormalClockRecordDTO> listAll(AcAbnormalClockRecordQueryCriteria criteria) {
        List<AcAbnormalClockRecord> acAbnormalClockRecords = acAbnormalClockRecordDao.listAllByCriteria(criteria);
        return acAbnormalClockRecordMapper.toDto(acAbnormalClockRecords);
    }

    @Override
    public Map<String, Object> listAll(AcAbnormalClockRecordQueryCriteria criteria, Pageable pageable) {
        Page<AcAbnormalClockRecord> page = PageUtil.startPage(pageable);
        List<AcAbnormalClockRecord> abnormalClockRecords = acAbnormalClockRecordDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acAbnormalClockRecordMapper.toDto(abnormalClockRecords), page.getTotal());
    }

    @Override
    public void download(List<AcAbnormalClockRecordDTO> abnormalClockRecordDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcAbnormalClockRecordDTO abnormalClockRecordDTO : abnormalClockRecordDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("日期", abnormalClockRecordDTO.getDate());
            map.put("考勤处理记录id", abnormalClockRecordDTO.getAcAttendanceRecordHistoryId());
            map.put("打卡时间", abnormalClockRecordDTO.getClockTime());
            map.put("门禁控制器编号", abnormalClockRecordDTO.getCardMachineNo());
            map.put("弹性域1", abnormalClockRecordDTO.getAttribute1());
            map.put("弹性域2", abnormalClockRecordDTO.getAttribute2());
            map.put("弹性域3", abnormalClockRecordDTO.getAttribute3());
            map.put("弹性域4", abnormalClockRecordDTO.getAttribute4());
            map.put("弹性域5", abnormalClockRecordDTO.getAttribute5());
            map.put("id", abnormalClockRecordDTO.getId());
            map.put("updateTime", abnormalClockRecordDTO.getUpdateTime());
            map.put("updateBy", abnormalClockRecordDTO.getUpdateBy());
            map.put("createTime", abnormalClockRecordDTO.getCreateTime());
            map.put("createBy", abnormalClockRecordDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    //    @Override
    //    @Transactional(rollbackFor = Exception.class)
    //    public void disposeClock(List<AcAbnormalClockRecord> clockRecords) {
    //        if(clockRecords.size() > 0){
    //            FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
    //            List<Long> docReturnAccrIds = new ArrayList<>();
    //            List<Long> personReturnAccrIds = new ArrayList<>();
    //            for(AcAbnormalClockRecord aac:clockRecords){
    //                setDisposeByAndDisposeTimeByRole(user, aac);
    //                if ((aac.getCodeStatus().equals("acAdminRepulse") || aac.getCodeStatus().equals("leaderRepulse")) && !aac.getRecordIsChargeFlag()) {
    //                    //  发给资料员的邮件集合
    //                    docReturnAccrIds.add(aac.getId());
    //                }
    //                if (aac.getCodeStatus().equals("leaderRepulse")  && aac.getRecordIsChargeFlag()) {
    //                    // 发给个人的邮件集合
    //                    personReturnAccrIds.add(aac.getId());
    //                }
    //                if (null != user.getEmployee()) { // 有员工id用员工id，没有则用userId
    //                    aac.setUpdateBy(user.getEmployee().getId());
    //                } else {
    //                    aac.setUpdateBy(user.getId());
    //                }
    //                aac.setUpdateTime(LocalDateTime.now());
    //                acAbnormalClockRecordDao.updateDisposeColumnByKey(aac);
    //            }
    //            if (docReturnAccrIds.size() > 0) {
    //                // 获取邮件服务
    //                sendAcRepulseEmail(docReturnAccrIds, "DOC");
    //            }
    //            if (personReturnAccrIds.size() > 0) {
    //                sendAcRepulseEmail(personReturnAccrIds, "PERSON");
    //            }
    //            // 新的处理方式不再需要修改主表
    //            //            if(passFlag){
    //            //                history.setDisposeFlag("pass");
    //            //            }else{
    //            //                history.setDisposeFlag("wait");
    //            //            }
    //            //            acAttendanceRecordHistoryDao.updateDisposeFlagByKey(history);
    //        }else{
    //            throw new InfoCheckWarningMessException("打卡时间为空！");
    //        }
    //    }

//    public void setDisposeByAndDisposeTimeByRole(FndUserDTO user, AcAbnormalClockRecord aac) {
//        if (aac.getCodeStatus().equals("documentor")) {
//            // 个人处理或班组长处理
//            aac.setSelfDisposeTime(LocalDateTime.now());
//        } else if (aac.getCodeStatus().equals("leader")) {
//            // 资料员处理
//            if (null != user.getEmployee()) { // 有员工id用员工id，没有则用userid
//                aac.setDisposeBy(user.getEmployee().getId());
//            } else {
//                aac.setDisposeBy(user.getId());
//            }
//            aac.setDisposeTime(LocalDateTime.now());
//        } else if (aac.getCodeStatus().equals("leaderRepulse") || aac.getCodeStatus().equals("acAdmin")) {
//            // 领导打回或领导通过
//            if (null != user.getEmployee()) {
//                aac.setLeaderDisposeBy(user.getEmployee().getId());
//            } else {
//                aac.setLeaderDisposeBy(user.getId());
//            }
//            aac.setLeaderDisposeTime(LocalDateTime.now());
//        } else if (aac.getCodeStatus().equals("close") || aac.getCodeStatus().equals("acAdminRepulse")) {
//            // 考勤管理员打回或考勤管理员关闭
//            aac.setAcAdminDisposeTime(LocalDateTime.now());
//        }
//    }

    public void sendAcRepulseEmail(List<Long> returnAarhIds, String type) {
        ToolEmailInterface toolEmailInterface = new ToolEmailInterface();
        acEmployeeAttendanceServiceImpl.getServer(toolEmailInterface);
        AcAttendanceRecordHistoryQueryCriteria acAttendanceRecordHistoryQueryCriteria = new AcAttendanceRecordHistoryQueryCriteria();
        acAttendanceRecordHistoryQueryCriteria.setAarhIds(returnAarhIds);
//        acAttendanceRecordHistoryQueryCriteria.setAacrIds(returnAccrIds);
        List<RepulseEmailMes> repulseList = new ArrayList<>();
        if (type.equals("DOC")) {
            repulseList = acAttendanceRecordHistoryDao.getDocEmailInfoByAccrId(acAttendanceRecordHistoryQueryCriteria);
        } else {
            repulseList = acAttendanceRecordHistoryDao.getPersonEmailInfoByAccrId(acAttendanceRecordHistoryQueryCriteria);
        }
        // 根据邮箱地址分组
        Map<String, List<RepulseEmailMes>> emailMap = new HashMap<>();
        repulseList.stream().collect(Collectors.groupingBy(RepulseEmailMes::getEmail, Collectors.toList())).forEach(emailMap::put);

        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/acException.ftl");
        Dict dict = Dict.create();
        emailMap.forEach((x, y) -> {
            toolEmailInterface.setSendTo(x); // 正式启用
            toolEmailInterface.setStatus("PLAN");
            toolEmailInterface.setPlannedDate(LocalDateTime.now());
            if (type.equals("DOC")) {
                dict.set("type", "DOC");
                toolEmailInterface.setMailSubject("存在考勤异常被打回 - " + systemName);
                dict.set("acList", y);
                toolEmailInterface.setMailContent(template.render(dict));
            } else {
                dict.set("type", "PERSON");
                toolEmailInterface.setMailSubject("个人考勤异常被打回 - " + systemName);
                dict.set("acList", y);
                toolEmailInterface.setMailContent(template.render(dict));
            }
            toolEmailInterfaceService.insert(toolEmailInterface);
        });
    }


}
