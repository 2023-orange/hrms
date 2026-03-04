package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.dao.SwmPostSkillSalaryDao;
import com.sunten.hrms.swm.domain.SwmPostSkillSalaryInterface;
import com.sunten.hrms.swm.dao.SwmPostSkillSalaryInterfaceDao;
import com.sunten.hrms.swm.dto.SwmPostSkillSalaryQueryCriteria;
import com.sunten.hrms.swm.service.SwmPostSkillSalaryInterfaceService;
import com.sunten.hrms.swm.dto.SwmPostSkillSalaryInterfaceDTO;
import com.sunten.hrms.swm.dto.SwmPostSkillSalaryInterfaceQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmPostSkillSalaryInterfaceMapper;
import com.sunten.hrms.swm.service.SwmPostSkillSalaryService;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;

/**
 * <p>
 * 岗位技能工资接口表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmPostSkillSalaryInterfaceServiceImpl extends ServiceImpl<SwmPostSkillSalaryInterfaceDao, SwmPostSkillSalaryInterface> implements SwmPostSkillSalaryInterfaceService {
    private final SwmPostSkillSalaryInterfaceDao swmPostSkillSalaryInterfaceDao;
    private final SwmPostSkillSalaryInterfaceMapper swmPostSkillSalaryInterfaceMapper;
    private final FndUserService fndUserService;
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;
    private final SwmPostSkillSalaryDao swmPostSkillSalaryDao;
    private final SwmPostSkillSalaryService swmPostSkillSalaryService;

    @Autowired
    private SwmPostSkillSalaryInterfaceService instance;

    public SwmPostSkillSalaryInterfaceServiceImpl(SwmPostSkillSalaryInterfaceDao swmPostSkillSalaryInterfaceDao, SwmPostSkillSalaryInterfaceMapper swmPostSkillSalaryInterfaceMapper,FndUserService fndUserService
    ,FndInterfaceOperationRecordService fndInterfaceOperationRecordService, SwmPostSkillSalaryDao swmPostSkillSalaryDao,SwmPostSkillSalaryService swmPostSkillSalaryService) {
        this.swmPostSkillSalaryInterfaceDao = swmPostSkillSalaryInterfaceDao;
        this.swmPostSkillSalaryInterfaceMapper = swmPostSkillSalaryInterfaceMapper;
        this.fndUserService = fndUserService;
        this.fndInterfaceOperationRecordService = fndInterfaceOperationRecordService;
        this.swmPostSkillSalaryDao = swmPostSkillSalaryDao;
        this.swmPostSkillSalaryService = swmPostSkillSalaryService;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmPostSkillSalaryInterfaceDTO insert(SwmPostSkillSalaryInterface postSkillSalaryInterfaceNew) {
        swmPostSkillSalaryInterfaceDao.insertAllColumn(postSkillSalaryInterfaceNew);
        return swmPostSkillSalaryInterfaceMapper.toDto(postSkillSalaryInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmPostSkillSalaryInterface postSkillSalaryInterface = new SwmPostSkillSalaryInterface();
        postSkillSalaryInterface.setId(id);
        this.delete(postSkillSalaryInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmPostSkillSalaryInterface postSkillSalaryInterface) {
        // TODO    确认删除前是否需要做检查
        swmPostSkillSalaryInterfaceDao.deleteByEntityKey(postSkillSalaryInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmPostSkillSalaryInterface postSkillSalaryInterfaceNew) {
        SwmPostSkillSalaryInterface postSkillSalaryInterfaceInDb = Optional.ofNullable(swmPostSkillSalaryInterfaceDao.getByKey(postSkillSalaryInterfaceNew.getId())).orElseGet(SwmPostSkillSalaryInterface::new);
        ValidationUtil.isNull(postSkillSalaryInterfaceInDb.getId() ,"PostSkillSalaryInterface", "id", postSkillSalaryInterfaceNew.getId());
        postSkillSalaryInterfaceNew.setId(postSkillSalaryInterfaceInDb.getId());
        swmPostSkillSalaryInterfaceDao.updateAllColumnByKey(postSkillSalaryInterfaceNew);
    }

    @Override
    public SwmPostSkillSalaryInterfaceDTO getByKey(Long id) {
        SwmPostSkillSalaryInterface postSkillSalaryInterface = Optional.ofNullable(swmPostSkillSalaryInterfaceDao.getByKey(id)).orElseGet(SwmPostSkillSalaryInterface::new);
        ValidationUtil.isNull(postSkillSalaryInterface.getId() ,"PostSkillSalaryInterface", "id", id);
        return swmPostSkillSalaryInterfaceMapper.toDto(postSkillSalaryInterface);
    }

    @Override
    public List<SwmPostSkillSalaryInterfaceDTO> listAll(SwmPostSkillSalaryInterfaceQueryCriteria criteria) {
        return swmPostSkillSalaryInterfaceMapper.toDto(swmPostSkillSalaryInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmPostSkillSalaryInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<SwmPostSkillSalaryInterface> page = PageUtil.startPage(pageable);
        List<SwmPostSkillSalaryInterface> postSkillSalaryInterfaces = swmPostSkillSalaryInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmPostSkillSalaryInterfaceMapper.toDto(postSkillSalaryInterfaces), page.getTotal());
    }

    @Override
    public void download(List<SwmPostSkillSalaryInterfaceDTO> postSkillSalaryInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmPostSkillSalaryInterfaceDTO postSkillSalaryInterfaceDTO : postSkillSalaryInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("所得期间（格式：年.月）", postSkillSalaryInterfaceDTO.getIncomePeriod());
            map.put("工牌号", postSkillSalaryInterfaceDTO.getWorkCard());
            map.put("员工姓名", postSkillSalaryInterfaceDTO.getEmployeeName());
            map.put("工作日加班工时", null != postSkillSalaryInterfaceDTO.getOvertimePayTime() ? postSkillSalaryInterfaceDTO.getOvertimePayTime() : "");
            map.put("休息日加班工时", null != postSkillSalaryInterfaceDTO.getRestOvertimePayTime() ? postSkillSalaryInterfaceDTO.getRestOvertimePayTime() : "");
            map.put("法定节假日加班工时", null != postSkillSalaryInterfaceDTO.getHolidayOvertimePayTime() ? postSkillSalaryInterfaceDTO.getHolidayOvertimePayTime() : "");
            map.put("工作日加班工资", null != postSkillSalaryInterfaceDTO.getOvertimePay() ? postSkillSalaryInterfaceDTO.getOvertimePay() : "");
            map.put("休息日加班工资", null != postSkillSalaryInterfaceDTO.getRestOvertimePay() ? postSkillSalaryInterfaceDTO.getRestOvertimePay() : "");
            map.put("法定节假日加班工资", null != postSkillSalaryInterfaceDTO.getHolidayOvertimePay() ? postSkillSalaryInterfaceDTO.getHolidayOvertimePay() : "");
            map.put("安全累积奖", null != postSkillSalaryInterfaceDTO.getSafetyAccumulationAward() ? postSkillSalaryInterfaceDTO.getSafetyAccumulationAward() : "");
            map.put("高温补贴",  null != postSkillSalaryInterfaceDTO.getHighTemperatureSubsidy() ? postSkillSalaryInterfaceDTO.getHighTemperatureSubsidy() : "");
            map.put("扣除补贴", null != postSkillSalaryInterfaceDTO.getAllowanceDeduction() ? postSkillSalaryInterfaceDTO.getAllowanceDeduction() : "");
            map.put("操作属性", postSkillSalaryInterfaceDTO.getOperationCode().equals("U") ? "更新" : postSkillSalaryInterfaceDTO.getOperationCode().equals("C") ? "新增" : "删除");
            map.put("错误信息", postSkillSalaryInterfaceDTO.getErrorMsg());
            map.put("数据状态", postSkillSalaryInterfaceDTO.getDataStatus().equals("T") ? "导入成功" : "导入失败");
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SwmPostSkillSalaryInterface> insertExcel(List<SwmPostSkillSalaryInterface> swmPostSkillSalaryInterfaces,Boolean reImportFlag, String type) {
        SwmPostSkillSalaryQueryCriteria swmPostSkillSalaryQueryCriteria = new SwmPostSkillSalaryQueryCriteria();
        swmPostSkillSalaryQueryCriteria.setPeriod(swmPostSkillSalaryInterfaces.get(0).getIncomePeriod());
        if (swmPostSkillSalaryDao.listAllByCriteria(swmPostSkillSalaryQueryCriteria).size() <= 0){
            throw new InfoCheckWarningMessException("该所得期间尚未生成固定工资，不允许导入");
        }
        FndInterfaceOperationRecord fndInterfaceOperationRecord = new FndInterfaceOperationRecord();
//        if (type.equals("工作日加班工资导入")) {
//            fndInterfaceOperationRecord.setOperationValue("insertOvertimePay");
//            fndInterfaceOperationRecord.setOperationDescription("工作日加班工资导入");
//        } else if (type.equals("法定节假日加班工资导入")) {
//            fndInterfaceOperationRecord.setOperationValue("insertHolidayOvertimePay");
//            fndInterfaceOperationRecord.setOperationDescription("法定节假日加班工资导入");
//        } else if (type.equals("休息日加班工资导入")) {
//            fndInterfaceOperationRecord.setOperationValue("insertRestOvertimePay");
//            fndInterfaceOperationRecord.setOperationDescription("休息日加班工资导入");
//        } else {
//            fndInterfaceOperationRecord.setOperationValue("insertDeductIncomeTax");
//            fndInterfaceOperationRecord.setOperationDescription("扣除所得税导入");
//        }

        boolean specialFlag = false;
        fndInterfaceOperationRecord.setOperationValue("insertPostSkillSalary");
        if (null != type) { // 描述不空
            if (type.equals("电网车间加班工资导入")) {
                specialFlag = true;
            }
            fndInterfaceOperationRecord.setOperationDescription(type);
        } else {
            fndInterfaceOperationRecord.setOperationDescription("固定工资导入");
        }
        fndInterfaceOperationRecordService.insert(fndInterfaceOperationRecord);
        Long tempGroupId = fndInterfaceOperationRecord.getId();
        try {
            FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
            instance.insertMainAndSon(swmPostSkillSalaryInterfaces, fndInterfaceOperationRecord.getId(), type, user.getId(), specialFlag, reImportFlag);
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
        SwmPostSkillSalaryInterfaceQueryCriteria swmPostSkillSalaryInterfaceQueryCriteria = new SwmPostSkillSalaryInterfaceQueryCriteria();
        swmPostSkillSalaryInterfaceQueryCriteria.setGroupId(tempGroupId);
        swmPostSkillSalaryInterfaceQueryCriteria.setDataStatus("F");
        List<SwmPostSkillSalaryInterface> result = swmPostSkillSalaryInterfaceDao.listAllByCriteria(swmPostSkillSalaryInterfaceQueryCriteria);
        if (result.size() == 0) {
            result.add(new SwmPostSkillSalaryInterface().setGroupId(fndInterfaceOperationRecord.getId()));
        }
        return result;
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void insertOvertimePay(List<SwmPostSkillSalaryInterface> swmPostSkillSalaryInterfaces) {
//        Map<String, Object> map = new HashMap<>();
//        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
//        map.put("uId", user.getEmployee().getId());
//        map.put("swmPostSkillSalaryInterface", swmPostSkillSalaryInterfaces);
////        swmPostSkillSalaryInterfaceDao.insertOvertimePay(map);
//    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void insertMainAndSon(List<SwmPostSkillSalaryInterface> swmPostSkillSalaryInterfaces, Long groupId, String importType, Long userId, Boolean sepcialFlag, Boolean reImportFlag) {
//        if (importType.equals("工作日加班工资导入")) {
//            type = "insertOvertimePay";
//        } else if (importType.equals("法定节假日加班工资导入")) {
//            type = "insertHolidayOvertimePay";
//        } else if (importType.equals("休息日加班工资导入")) {
//            type = "insertRestOvertimePay";
//        } else {
//            type = "insertDeductIncomeTax";
//        }
        for (SwmPostSkillSalaryInterface swm : swmPostSkillSalaryInterfaces
        ) {
            swm.setGroupId(groupId);
            swm.setUpdateBy(userId);
            swm.setCreateBy(userId);
            if (reImportFlag) {
                swm.setDataStatus("F");
                swm.setErrorMsg("");
            }
            if (sepcialFlag) {
                swmPostSkillSalaryInterfaceDao.insertPayBySpecial(swm);
            } else {
                swmPostSkillSalaryInterfaceDao.insertPay(swm);
            }
        }
        swmPostSkillSalaryService.interfaceToMain(groupId);

        swmPostSkillSalaryService.interfaceToMainUpdateWageAndNet(groupId);

    }

    @Override
    public List<SwmPostSkillSalaryInterface> getFixedSummaryByImportList(String incomePeriod, Set<String> workCards, Set<Long> groupIds) {
        return swmPostSkillSalaryInterfaceDao.getFixedSummaryByImportList(incomePeriod, workCards, groupIds);
    }
}
