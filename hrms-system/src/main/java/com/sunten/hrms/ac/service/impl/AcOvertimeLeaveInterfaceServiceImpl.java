package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcOvertimeLeaveDao;
import com.sunten.hrms.ac.dao.AcOvertimeLeaveInterfaceDao;
import com.sunten.hrms.ac.domain.AcOvertimeLeave;
import com.sunten.hrms.ac.domain.AcOvertimeLeaveInterface;
import com.sunten.hrms.ac.dto.AcOvertimeLeaveInterfaceDTO;
import com.sunten.hrms.ac.dto.AcOvertimeLeaveInterfaceQueryCriteria;
import com.sunten.hrms.ac.dto.AcOvertimeLeaveQueryCriteria;
import com.sunten.hrms.ac.mapper.AcOvertimeLeaveInterfaceMapper;
import com.sunten.hrms.ac.service.AcOvertimeLeaveInterfaceService;
import com.sunten.hrms.ac.service.AcOvertimeLeaveService;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndInterfaceOperationRecordDao;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.dto.FndInterfaceOperationRecordDTO;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.service.SwmPostSkillSalaryService;
import com.sunten.hrms.utils.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <p>
 * oa加班请假统计接口表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-26
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcOvertimeLeaveInterfaceServiceImpl extends ServiceImpl<AcOvertimeLeaveInterfaceDao, AcOvertimeLeaveInterface> implements AcOvertimeLeaveInterfaceService {
    private final AcOvertimeLeaveInterfaceDao acOvertimeLeaveInterfaceDao;
    private final AcOvertimeLeaveInterfaceMapper acOvertimeLeaveInterfaceMapper;
    private final AcOvertimeLeaveDao acOvertimeLeaveDao;
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;
    private final FndUserService fndUserService;
    private final SwmPostSkillSalaryService swmPostSkillSalaryService;

    @Autowired
    private AcOvertimeLeaveInterfaceServiceImpl instance;

    public AcOvertimeLeaveInterfaceServiceImpl(AcOvertimeLeaveInterfaceDao acOvertimeLeaveInterfaceDao, AcOvertimeLeaveInterfaceMapper acOvertimeLeaveInterfaceMapper,
                                               AcOvertimeLeaveDao acOvertimeLeaveDao, FndInterfaceOperationRecordService fndInterfaceOperationRecordService, FndUserService fndUserService,
                                               SwmPostSkillSalaryService swmPostSkillSalaryService
                                               ) {
        this.acOvertimeLeaveInterfaceDao = acOvertimeLeaveInterfaceDao;
        this.acOvertimeLeaveInterfaceMapper = acOvertimeLeaveInterfaceMapper;
        this.acOvertimeLeaveDao = acOvertimeLeaveDao;
        this.fndInterfaceOperationRecordService = fndInterfaceOperationRecordService;
        this.fndUserService = fndUserService;
        this.swmPostSkillSalaryService = swmPostSkillSalaryService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcOvertimeLeaveInterfaceDTO insert(AcOvertimeLeaveInterface overtimeLeaveInterfeNew) {
        acOvertimeLeaveInterfaceDao.insertAllColumn(overtimeLeaveInterfeNew);
        return acOvertimeLeaveInterfaceMapper.toDto(overtimeLeaveInterfeNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AcOvertimeLeaveInterface overtimeLeaveInterfe = new AcOvertimeLeaveInterface();
        overtimeLeaveInterfe.setId(id);
        this.delete(overtimeLeaveInterfe);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcOvertimeLeaveInterface overtimeLeaveInterfe) {
        // TODO    确认删除前是否需要做检查
        acOvertimeLeaveInterfaceDao.deleteByEntityKey(overtimeLeaveInterfe);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcOvertimeLeaveInterface overtimeLeaveInterfeNew) {
        AcOvertimeLeaveInterface overtimeLeaveInterfeInDb = Optional.ofNullable(acOvertimeLeaveInterfaceDao.getByKey(overtimeLeaveInterfeNew.getId())).orElseGet(AcOvertimeLeaveInterface::new);
        ValidationUtil.isNull(overtimeLeaveInterfeInDb.getId() ,"OvertimeLeaveInterfe", "id", overtimeLeaveInterfeNew.getId());
        overtimeLeaveInterfeNew.setId(overtimeLeaveInterfeInDb.getId());
        acOvertimeLeaveInterfaceDao.updateAllColumnByKey(overtimeLeaveInterfeNew);
    }

    @Override
    public AcOvertimeLeaveInterfaceDTO getByKey(Long id) {
        AcOvertimeLeaveInterface overtimeLeaveInterfe = Optional.ofNullable(acOvertimeLeaveInterfaceDao.getByKey(id)).orElseGet(AcOvertimeLeaveInterface::new);
        ValidationUtil.isNull(overtimeLeaveInterfe.getId() ,"OvertimeLeaveInterfe", "id", id);
        return acOvertimeLeaveInterfaceMapper.toDto(overtimeLeaveInterfe);
    }

    @Override
    public List<AcOvertimeLeaveInterfaceDTO> listAll(AcOvertimeLeaveInterfaceQueryCriteria criteria) {
        return acOvertimeLeaveInterfaceMapper.toDto(acOvertimeLeaveInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcOvertimeLeaveInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<AcOvertimeLeaveInterface> page = PageUtil.startPage(pageable);
        List<AcOvertimeLeaveInterface> overtimeLeaveInterfes = acOvertimeLeaveInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acOvertimeLeaveInterfaceMapper.toDto(overtimeLeaveInterfes), page.getTotal());
    }

    @Override
    public void download(List<AcOvertimeLeaveInterfaceDTO> overtimeLeaveInterfeDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcOvertimeLeaveInterfaceDTO overtimeLeaveInterfeDTO : overtimeLeaveInterfeDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("日期", overtimeLeaveInterfeDTO.getDate());
            map.put("工牌号", overtimeLeaveInterfeDTO.getWorkCard());
            map.put("员工id", overtimeLeaveInterfeDTO.getEmployeeId());
            map.put("工作日加班时数（周一到周五加班）", overtimeLeaveInterfeDTO.getWorkingDayOvertime());
            map.put("休息日加班时数（周六日加班）", overtimeLeaveInterfeDTO.getRestDayOvertime());
            map.put("法定节假日加班时数", overtimeLeaveInterfeDTO.getHolidayOvertime());
            map.put("调休", overtimeLeaveInterfeDTO.getOffHours());
            map.put("年假", overtimeLeaveInterfeDTO.getAnnualLeave());
            map.put("事假", overtimeLeaveInterfeDTO.getCompassionateLeave());
            map.put("病假", overtimeLeaveInterfeDTO.getSickLeave());
            map.put("婚假", overtimeLeaveInterfeDTO.getMarriageHoliday());
            map.put("产假", overtimeLeaveInterfeDTO.getMaternityLeave());
            map.put("陪产假", overtimeLeaveInterfeDTO.getPaternityLeave());
            map.put("丧假", overtimeLeaveInterfeDTO.getBereavementLeave());
            map.put("工伤假", overtimeLeaveInterfeDTO.getWorkRelatedInjuryLeave());
            map.put("id", overtimeLeaveInterfeDTO.getId());
            map.put("创建时间", overtimeLeaveInterfeDTO.getCreateTime());
            map.put("创建人ID", overtimeLeaveInterfeDTO.getCreateBy());
            map.put("修改时间", overtimeLeaveInterfeDTO.getUpdateTime());
            map.put("修改人ID", overtimeLeaveInterfeDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<AcOvertimeLeaveInterfaceDTO> ImportAcOvertimeLeaveInterfaces(List<AcOvertimeLeaveInterface> acOvertimeLeaveInterfaces, Boolean reImportFlag) {
//        return new ArrayList<AcOvertimeLeaveInterfaceDTO>();
        if (null == acOvertimeLeaveInterfaces || acOvertimeLeaveInterfaces.size() == 0) {
            throw new InfoCheckWarningMessException("不允许上传空的文件");
        } else if (acOvertimeLeaveInterfaces.stream().map(AcOvertimeLeaveInterface::getDate).distinct().count() > 1) {
            throw new InfoCheckWarningMessException("为保证数据正确，单次导入限定所有数据的日期必须一致");
        } else {
            FndInterfaceOperationRecord fndInterfaceOperationRecord = new FndInterfaceOperationRecord();
            fndInterfaceOperationRecord.setOperationDescription("员工加班请假统计导入");
            fndInterfaceOperationRecord.setOperationValue("AcOvertimeLeaveInterfaces");
            fndInterfaceOperationRecordService.insert(fndInterfaceOperationRecord);
            // 数值校验
            Long tempGroupId = fndInterfaceOperationRecord.getId();
            try {
                instance.insertMainAndSon(acOvertimeLeaveInterfaces, fndInterfaceOperationRecord.getId(), acOvertimeLeaveInterfaces.get(0).getDate(), reImportFlag);
            } catch(Exception e) {
                fndInterfaceOperationRecord.setDataProcessingDescription(ThrowableUtil.getStackTrace(e));
                fndInterfaceOperationRecord.setSuccessFlag(false);
                fndInterfaceOperationRecordService.update(fndInterfaceOperationRecord);
                throw new InfoCheckWarningMessException("Excel导入失败，请联系管理员。");
            } finally {
                fndInterfaceOperationRecord.setSuccessFlag(true);
                fndInterfaceOperationRecord.setDataProcessingDescription("导入正常");
                fndInterfaceOperationRecordService.update(fndInterfaceOperationRecord);
            }
            // 查出刚才插入的interface数据，并返回
            AcOvertimeLeaveInterfaceQueryCriteria acOvertimeLeaveInterfaceQueryCriteria = new AcOvertimeLeaveInterfaceQueryCriteria();
            acOvertimeLeaveInterfaceQueryCriteria.setGroupId(tempGroupId);
            acOvertimeLeaveInterfaceQueryCriteria.setDataStatus("F");
            List<AcOvertimeLeaveInterface> acOvertimeLeaveInterfaceList = acOvertimeLeaveInterfaceDao.listAllByCriteria(acOvertimeLeaveInterfaceQueryCriteria);
            int count = 1;
            for (AcOvertimeLeaveInterface ac : acOvertimeLeaveInterfaceList
                 ) {
                ac.setOrder(count++);
            }
            // 检测该所得期间的浮动固定是否已经冻结，发送邮件通知薪酬管理人员
//            if (null != acOvertimeLeaveInterfaceList.get(0)) {
//                if (swmPostSkillSalaryService.checkFrozenFlagByPeriod(acOvertimeLeaveInterfaceList.get(0).getDateStr()) > 0) {
//                    if (!acOvertimeLeaveDao.checkOvertimeEmailIsSendToday("工时修改通知")) {
//                        swmPostSkillSalaryService.sendEmailByOverTimeChange(acOvertimeLeaveInterfaceList.get(0).getDateStr());
//                    }
//                }
//            }
            return acOvertimeLeaveInterfaceMapper.toDto(acOvertimeLeaveInterfaceList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void insertMainAndSon(List<AcOvertimeLeaveInterface> acOvertimeLeaveInterfaces, Long groupId, LocalDate date, Boolean reImportFlag) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
//        for (AcOvertimeLeaveInterface ac: acOvertimeLeaveInterfaces
//             ) {
//            ac.setGroupId(groupId);
//            acOvertimeLeaveInterfaceDao.checkAndInsertInterFace(ac);
//        }
        if (reImportFlag) {
            for (AcOvertimeLeaveInterface acOver : acOvertimeLeaveInterfaces
                 ) {
                acOver.setDataStatus("F");
                acOver.setErrorMsg("");
            }
        }
        Integer count = acOvertimeLeaveInterfaces.size() / 50;
        for (int i = 1; i <= count; i++) {
            log.debug("第" + i + "次批量插入");
            int finalI = i;
            List<AcOvertimeLeaveInterface> acOvertimeLeaveInterfaceList = acOvertimeLeaveInterfaces.stream().filter(AcOvertimeLeaveInterface -> AcOvertimeLeaveInterface.getOrder() < (finalI * 50) && AcOvertimeLeaveInterface.getOrder() >= (finalI -1) * 50).collect(Collectors.toList());
            acOvertimeLeaveInterfaceDao.insertOverTimeCollection(groupId,acOvertimeLeaveInterfaceList, LocalDateTime.now(), user.getId());
        }
        Integer sCount = acOvertimeLeaveInterfaces.size() % 50;
        if (sCount > 0) {
            log.debug("最后一次批量插入");
            List<AcOvertimeLeaveInterface> sAcOvertimeLeaveInterfaceList = acOvertimeLeaveInterfaces.stream().filter(AcOvertimeLeaveInterface -> AcOvertimeLeaveInterface.getOrder() >= (count * 50)).collect(Collectors.toList());
            acOvertimeLeaveInterfaceDao.insertOverTimeCollection(groupId,sAcOvertimeLeaveInterfaceList, LocalDateTime.now(), user.getId());
        }
        // 调用存储过程更新错误信息
        acOvertimeLeaveInterfaceDao.checkAfterInsertCollection(groupId);
        if (!reImportFlag) {
            // 生成一份可能未生成的员工， 然后更新当前日期
            acOvertimeLeaveDao.createBeforeinterface(date);
            acOvertimeLeaveDao.updateBeforeinterface(date);
        }
        // 将接口的数据更新到主表
        //        AcOvertimeLeaveInterfaceQueryCriteria acOvertimeLeaveInterfaceQueryCriteria = new AcOvertimeLeaveInterfaceQueryCriteria();
        //        acOvertimeLeaveInterfaceQueryCriteria.setGroupId(groupId);
        //        List<AcOvertimeLeaveInterface> targets = acOvertimeLeaveInterfaceDao.listAllByCriteria(acOvertimeLeaveInterfaceQueryCriteria);
        //        for (AcOvertimeLeaveInterface  in: targets
        //             ) {
        //            acOvertimeLeaveDao.updateByInterface(in);
        //        }
        acOvertimeLeaveDao.updateByInterface(groupId);

    }

    @Override
    public List<AcOvertimeLeaveInterfaceDTO> preCheckOverTime(List<AcOvertimeLeaveInterface> acOvertimeLeaveInterfaces) {
        for (AcOvertimeLeaveInterface ac : acOvertimeLeaveInterfaces
             ) {
            ac.setPreCheckFlag(acOvertimeLeaveInterfaceDao.preCheckAcOverTime(ac));
        }
        return acOvertimeLeaveInterfaceMapper.toDto(acOvertimeLeaveInterfaces);
    }

    @Override
    public Boolean checkOvertimeLeaveBeforAutoUpdate(String incomePeriod) {
        return acOvertimeLeaveInterfaceDao.checkOvertimeLeaveBeforAutoUpdate(incomePeriod);
    }
}
