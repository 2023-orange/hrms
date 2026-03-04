package com.sunten.hrms.kpi.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.kpi.dao.KpiAnnualDao;
import com.sunten.hrms.kpi.domain.KpiAnnual;
import com.sunten.hrms.kpi.domain.KpiAssessmentIndicatorsInterface;
import com.sunten.hrms.kpi.dao.KpiAssessmentIndicatorsInterfaceDao;
import com.sunten.hrms.kpi.dto.KpiAnnualQueryCriteria;
import com.sunten.hrms.kpi.service.KpiAssessmentIndicatorsInterfaceService;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsInterfaceDTO;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsInterfaceQueryCriteria;
import com.sunten.hrms.kpi.mapper.KpiAssessmentIndicatorsInterfaceMapper;
import com.sunten.hrms.kpi.service.KpiAssessmentIndicatorsService;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
 *  服务实现类
 * </p>
 *
 * @author zhoujy
 * @since 2023-12-20
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KpiAssessmentIndicatorsInterfaceServiceImpl extends ServiceImpl<KpiAssessmentIndicatorsInterfaceDao, KpiAssessmentIndicatorsInterface> implements KpiAssessmentIndicatorsInterfaceService {
    private final KpiAssessmentIndicatorsInterfaceDao kpiAssessmentIndicatorsInterfaceDao;
    private final KpiAssessmentIndicatorsInterfaceMapper kpiAssessmentIndicatorsInterfaceMapper;
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;
    private final FndUserService fndUserService;
    private final KpiAssessmentIndicatorsService kpiAssessmentIndicatorsService;
    private final KpiAnnualDao kpiAnnualDao;

    public KpiAssessmentIndicatorsInterfaceServiceImpl(KpiAssessmentIndicatorsInterfaceDao kpiAssessmentIndicatorsInterfaceDao, KpiAssessmentIndicatorsInterfaceMapper kpiAssessmentIndicatorsInterfaceMapper, FndInterfaceOperationRecordService fndInterfaceOperationRecordService, FndUserService fndUserService, KpiAssessmentIndicatorsService kpiAssessmentIndicatorsService, KpiAnnualDao kpiAnnualDao) {
        this.kpiAssessmentIndicatorsInterfaceDao = kpiAssessmentIndicatorsInterfaceDao;
        this.kpiAssessmentIndicatorsInterfaceMapper = kpiAssessmentIndicatorsInterfaceMapper;
        this.fndInterfaceOperationRecordService = fndInterfaceOperationRecordService;
        this.fndUserService = fndUserService;
        this.kpiAssessmentIndicatorsService = kpiAssessmentIndicatorsService;
        this.kpiAnnualDao = kpiAnnualDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KpiAssessmentIndicatorsInterfaceDTO insert(KpiAssessmentIndicatorsInterface assessmentIndicatorsInterfaceNew) {
        kpiAssessmentIndicatorsInterfaceDao.insertAllColumn(assessmentIndicatorsInterfaceNew);
        return kpiAssessmentIndicatorsInterfaceMapper.toDto(assessmentIndicatorsInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete() {
        KpiAssessmentIndicatorsInterface assessmentIndicatorsInterface = new KpiAssessmentIndicatorsInterface();
        this.delete(assessmentIndicatorsInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(KpiAssessmentIndicatorsInterface assessmentIndicatorsInterface) {
        // TODO    确认删除前是否需要做检查
        kpiAssessmentIndicatorsInterfaceDao.deleteByEntityKey(assessmentIndicatorsInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(KpiAssessmentIndicatorsInterface assessmentIndicatorsInterfaceNew) {
        KpiAssessmentIndicatorsInterface assessmentIndicatorsInterfaceInDb = Optional.ofNullable(kpiAssessmentIndicatorsInterfaceDao.getByKey()).orElseGet(KpiAssessmentIndicatorsInterface::new);
        kpiAssessmentIndicatorsInterfaceDao.updateAllColumnByKey(assessmentIndicatorsInterfaceNew);
    }

    @Override
    public KpiAssessmentIndicatorsInterfaceDTO getByKey() {
        KpiAssessmentIndicatorsInterface assessmentIndicatorsInterface = Optional.ofNullable(kpiAssessmentIndicatorsInterfaceDao.getByKey()).orElseGet(KpiAssessmentIndicatorsInterface::new);
        return kpiAssessmentIndicatorsInterfaceMapper.toDto(assessmentIndicatorsInterface);
    }

    @Override
    public List<KpiAssessmentIndicatorsInterfaceDTO> listAll(KpiAssessmentIndicatorsInterfaceQueryCriteria criteria) {
        return kpiAssessmentIndicatorsInterfaceMapper.toDto(kpiAssessmentIndicatorsInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(KpiAssessmentIndicatorsInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<KpiAssessmentIndicatorsInterface> page = PageUtil.startPage(pageable);
        List<KpiAssessmentIndicatorsInterface> assessmentIndicatorsInterfaces = kpiAssessmentIndicatorsInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(kpiAssessmentIndicatorsInterfaceMapper.toDto(assessmentIndicatorsInterfaces), page.getTotal());
    }

    @Override
    public void download(List<KpiAssessmentIndicatorsInterfaceDTO> assessmentIndicatorsInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (KpiAssessmentIndicatorsInterfaceDTO assessmentIndicatorsInterfaceDTO : assessmentIndicatorsInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", assessmentIndicatorsInterfaceDTO.getId());
            map.put("assessedDepartmentName", assessmentIndicatorsInterfaceDTO.getAssessedDepartmentName());
            map.put("assessedDepartmentId", assessmentIndicatorsInterfaceDTO.getAssessedDepartmentId());
            map.put("examineType", assessmentIndicatorsInterfaceDTO.getExamineType());
            map.put("targetDimension", assessmentIndicatorsInterfaceDTO.getTargetDimension());
            map.put("keyPerformanceIndicator", assessmentIndicatorsInterfaceDTO.getKeyPerformanceIndicator());
            map.put("define", assessmentIndicatorsInterfaceDTO.getDefine());
            map.put("weight", assessmentIndicatorsInterfaceDTO.getWeight());
            map.put("threshold", assessmentIndicatorsInterfaceDTO.getThreshold());
            map.put("targetValue", assessmentIndicatorsInterfaceDTO.getTargetValue());
            map.put("challengeValue", assessmentIndicatorsInterfaceDTO.getChallengeValue());
            map.put("examineDepartmentId", assessmentIndicatorsInterfaceDTO.getExamineDepartmentId());
            map.put("examineDepartmentName", assessmentIndicatorsInterfaceDTO.getExamineDepartmentName());
            map.put("examineEmployeeName", assessmentIndicatorsInterfaceDTO.getExamineEmployeeName());
            map.put("examineEmployeeWorkCard", assessmentIndicatorsInterfaceDTO.getExamineEmployeeWorkCard());
            map.put("examineEmployeeId", assessmentIndicatorsInterfaceDTO.getExamineEmployeeId());
            map.put("dataAccuracy", assessmentIndicatorsInterfaceDTO.getDataAccuracy());
            map.put("dataType", assessmentIndicatorsInterfaceDTO.getDataType());
            map.put("groupId", assessmentIndicatorsInterfaceDTO.getGroupId());
            map.put("operationCode", assessmentIndicatorsInterfaceDTO.getOperationCode());
            map.put("errorMsg", assessmentIndicatorsInterfaceDTO.getErrorMsg());
            map.put("dataStatus", assessmentIndicatorsInterfaceDTO.getDataStatus());
            map.put("createTime", assessmentIndicatorsInterfaceDTO.getCreateTime());
            map.put("createBy", assessmentIndicatorsInterfaceDTO.getCreateBy());
            map.put("updateTime", assessmentIndicatorsInterfaceDTO.getUpdateTime());
            map.put("updateBy", assessmentIndicatorsInterfaceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<KpiAssessmentIndicatorsInterfaceDTO> importExcel(List<KpiAssessmentIndicatorsInterface> kpiAssessmentIndicatorsInterfaces) {
        FndInterfaceOperationRecord fndInterfaceOperationRecord = new FndInterfaceOperationRecord();
        fndInterfaceOperationRecord.setOperationValue("insertKpiAssessmentIndicators");
        fndInterfaceOperationRecord.setOperationDescription("KPI考核指标导入");
        fndInterfaceOperationRecordService.insert(fndInterfaceOperationRecord);
        Long tempGroupId = fndInterfaceOperationRecord.getId();
        try {
            FndUserDTO user;
            try {
                user = fndUserService.getByName(SecurityUtils.getUsername());
            } catch (Exception ex){
                user = new FndUserDTO();
                user.setId(-1l);
            }
            Long newSerialNumber = null;
            for (KpiAssessmentIndicatorsInterface kpiAssessmentIndicatorsInterface : kpiAssessmentIndicatorsInterfaces
            ) {
                KpiAnnualQueryCriteria criteria = new KpiAnnualQueryCriteria();
                criteria.setYear(kpiAssessmentIndicatorsInterface.getYear());
                List<KpiAnnual> kpiAnnuals = kpiAnnualDao.listAllByCriteria(criteria);
                for (KpiAnnual kpiAnnual : kpiAnnuals)
                {
                    kpiAssessmentIndicatorsInterface.setKpiAnnualId(kpiAnnual.getId());
                }
                kpiAssessmentIndicatorsInterface.setCreateBy(user.getId());
                kpiAssessmentIndicatorsInterface.setUpdateBy(user.getId());
                kpiAssessmentIndicatorsInterface.setGroupId(tempGroupId);
                kpiAssessmentIndicatorsInterface.setDataStatus("I");
                if (kpiAssessmentIndicatorsInterface.getSerialNumber() != null)
                {
                    kpiAssessmentIndicatorsInterface.setOperationCode("U");
                }
                else {
                    kpiAssessmentIndicatorsInterface.setOperationCode("I");
                    if (newSerialNumber == null) {
                        newSerialNumber = kpiAssessmentIndicatorsService.createSerialNumberKpi(kpiAssessmentIndicatorsInterface.getYear());
                    }
                    else {
                        newSerialNumber = newSerialNumber + 1;
                    }
                    kpiAssessmentIndicatorsInterface.setSerialNumber(newSerialNumber);
                }
                kpiAssessmentIndicatorsInterfaceDao.insertAllColumn(kpiAssessmentIndicatorsInterface);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("groupId", tempGroupId);
            map.put("resultStr", "");
            kpiAssessmentIndicatorsInterfaceDao.checkKpiAssessmentIndicatorsInterface(map);
            kpiAssessmentIndicatorsInterfaceDao.insertKpiAssessmentIndicatorsInterfaceToMain(map);
        } catch (Exception e) {
            fndInterfaceOperationRecord.setDataProcessingDescription(ThrowableUtil.getStackTrace(e));
            fndInterfaceOperationRecord.setSuccessFlag(false);
            fndInterfaceOperationRecordService.update(fndInterfaceOperationRecord);
            throw new InfoCheckWarningMessException("导入失败，请联系管理员");
        } finally {
            fndInterfaceOperationRecord.setDataProcessingDescription("导入正常");
            fndInterfaceOperationRecord.setSuccessFlag(true);
            fndInterfaceOperationRecordService.update(fndInterfaceOperationRecord);
        }
        KpiAssessmentIndicatorsInterfaceQueryCriteria kpiAssessmentIndicatorsInterfaceQueryCriteria = new KpiAssessmentIndicatorsInterfaceQueryCriteria();
        kpiAssessmentIndicatorsInterfaceQueryCriteria.setGroupId(tempGroupId);
        kpiAssessmentIndicatorsInterfaceQueryCriteria.setDataStatus("E");
        List<KpiAssessmentIndicatorsInterface> kpiAssessmentIndicatorsInterfaceList = kpiAssessmentIndicatorsInterfaceDao.listAllByCriteria(kpiAssessmentIndicatorsInterfaceQueryCriteria);
        if (kpiAssessmentIndicatorsInterfaceList.size() == 0) {
            kpiAssessmentIndicatorsInterfaceList.add(new KpiAssessmentIndicatorsInterface().setGroupId(fndInterfaceOperationRecord.getId()));
        }
        for (KpiAssessmentIndicatorsInterface kpiAssessmentIndicatorsInterfaceLists:kpiAssessmentIndicatorsInterfaceList)
        {
            if (kpiAssessmentIndicatorsInterfaceLists.getOperationCode().equals("I")) {
                kpiAssessmentIndicatorsInterfaceLists.setSerialNumber(null);
            }
        }
        return kpiAssessmentIndicatorsInterfaceMapper.toDto(kpiAssessmentIndicatorsInterfaceList);
    }

    @Override
    public List<KpiAssessmentIndicatorsInterface> getKpiAssessmentIndicatorsInterfaceSummaryByImportList(Set<String> workCards, Set<Long> groupIds) {
        return kpiAssessmentIndicatorsInterfaceDao.getKpiAssessmentIndicatorsInterfaceSummaryByImportList(workCards, groupIds);
    }
}
