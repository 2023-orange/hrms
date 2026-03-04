package com.sunten.hrms.swm.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.swm.domain.SwmQuarterlyAssessmentInterface;
import com.sunten.hrms.swm.dao.SwmQuarterlyAssessmentInterfaceDao;
import com.sunten.hrms.swm.dto.SwmQuarterlyAssessmentQueryCriteria;
import com.sunten.hrms.swm.service.SwmQuarterlyAssessmentInterfaceService;
import com.sunten.hrms.swm.dto.SwmQuarterlyAssessmentInterfaceDTO;
import com.sunten.hrms.swm.dto.SwmQuarterlyAssessmentInterfaceQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmQuarterlyAssessmentInterfaceMapper;
import com.sunten.hrms.swm.service.SwmQuarterlyAssessmentService;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import java.util.function.Function;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2022-05-13
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmQuarterlyAssessmentInterfaceServiceImpl extends ServiceImpl<SwmQuarterlyAssessmentInterfaceDao, SwmQuarterlyAssessmentInterface> implements SwmQuarterlyAssessmentInterfaceService {
    private final SwmQuarterlyAssessmentInterfaceDao swmQuarterlyAssessmentInterfaceDao;
    private final SwmQuarterlyAssessmentInterfaceMapper swmQuarterlyAssessmentInterfaceMapper;
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;
    private final SwmQuarterlyAssessmentService swmQuarterlyAssessmentService;
    @Autowired
    private SwmQuarterlyAssessmentInterfaceService instance;
    public SwmQuarterlyAssessmentInterfaceServiceImpl(SwmQuarterlyAssessmentInterfaceDao swmQuarterlyAssessmentInterfaceDao,
                                                      SwmQuarterlyAssessmentInterfaceMapper swmQuarterlyAssessmentInterfaceMapper,
                                                      FndInterfaceOperationRecordService fndInterfaceOperationRecordService,
                                                      SwmQuarterlyAssessmentService swmQuarterlyAssessmentService) {
        this.swmQuarterlyAssessmentInterfaceDao = swmQuarterlyAssessmentInterfaceDao;
        this.swmQuarterlyAssessmentInterfaceMapper = swmQuarterlyAssessmentInterfaceMapper;
        this.fndInterfaceOperationRecordService = fndInterfaceOperationRecordService;
        this.swmQuarterlyAssessmentService = swmQuarterlyAssessmentService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmQuarterlyAssessmentInterfaceDTO insert(SwmQuarterlyAssessmentInterface quarterlyAssessmentInterfaceNew) {
        swmQuarterlyAssessmentInterfaceDao.insertAllColumn(quarterlyAssessmentInterfaceNew);
        return swmQuarterlyAssessmentInterfaceMapper.toDto(quarterlyAssessmentInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmQuarterlyAssessmentInterface quarterlyAssessmentInterface = new SwmQuarterlyAssessmentInterface();
        quarterlyAssessmentInterface.setId(id);
        this.delete(quarterlyAssessmentInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmQuarterlyAssessmentInterface quarterlyAssessmentInterface) {
        // TODO    确认删除前是否需要做检查
        swmQuarterlyAssessmentInterfaceDao.deleteByEntityKey(quarterlyAssessmentInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmQuarterlyAssessmentInterface quarterlyAssessmentInterfaceNew) {
        SwmQuarterlyAssessmentInterface quarterlyAssessmentInterfaceInDb = Optional.ofNullable(swmQuarterlyAssessmentInterfaceDao.getByKey(quarterlyAssessmentInterfaceNew.getId())).orElseGet(SwmQuarterlyAssessmentInterface::new);
        ValidationUtil.isNull(quarterlyAssessmentInterfaceInDb.getId() ,"QuarterlyAssessmentInterface", "id", quarterlyAssessmentInterfaceNew.getId());
        quarterlyAssessmentInterfaceNew.setId(quarterlyAssessmentInterfaceInDb.getId());
        swmQuarterlyAssessmentInterfaceDao.updateAllColumnByKey(quarterlyAssessmentInterfaceNew);
    }

    @Override
    public SwmQuarterlyAssessmentInterfaceDTO getByKey(Long id) {
        SwmQuarterlyAssessmentInterface quarterlyAssessmentInterface = Optional.ofNullable(swmQuarterlyAssessmentInterfaceDao.getByKey(id)).orElseGet(SwmQuarterlyAssessmentInterface::new);
        ValidationUtil.isNull(quarterlyAssessmentInterface.getId() ,"QuarterlyAssessmentInterface", "id", id);
        return swmQuarterlyAssessmentInterfaceMapper.toDto(quarterlyAssessmentInterface);
    }

    @Override
    public List<SwmQuarterlyAssessmentInterfaceDTO> listAll(SwmQuarterlyAssessmentInterfaceQueryCriteria criteria) {
        return swmQuarterlyAssessmentInterfaceMapper.toDto(swmQuarterlyAssessmentInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmQuarterlyAssessmentInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<SwmQuarterlyAssessmentInterface> page = PageUtil.startPage(pageable);
        List<SwmQuarterlyAssessmentInterface> quarterlyAssessmentInterfaces = swmQuarterlyAssessmentInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmQuarterlyAssessmentInterfaceMapper.toDto(quarterlyAssessmentInterfaces), page.getTotal());
    }

    @Override
    public void download(List<SwmQuarterlyAssessmentInterfaceDTO> quarterlyAssessmentInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmQuarterlyAssessmentInterfaceDTO quarterlyAssessmentInterfaceDTO : quarterlyAssessmentInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id主键", quarterlyAssessmentInterfaceDTO.getId());
            map.put("数据分组id", quarterlyAssessmentInterfaceDTO.getGroupId());
            map.put("工牌号", quarterlyAssessmentInterfaceDTO.getWorkCard());
            map.put("员工姓名", quarterlyAssessmentInterfaceDTO.getEmployeeName());
            map.put("操作码", quarterlyAssessmentInterfaceDTO.getOperationCode());
            map.put("错误信息", quarterlyAssessmentInterfaceDTO.getErrorMsg());
            map.put("数据状态", quarterlyAssessmentInterfaceDTO.getDataStatus());
            map.put("考核季度", quarterlyAssessmentInterfaceDTO.getAssessmentQuarter());
            map.put("考核系数（负责新数据的写入）", quarterlyAssessmentInterfaceDTO.getAssessmentNum());
            map.put("薪酬人员id", quarterlyAssessmentInterfaceDTO.getSeId());
            map.put("创建时间", quarterlyAssessmentInterfaceDTO.getCreateTime());
            map.put("创建人id", quarterlyAssessmentInterfaceDTO.getCreateBy());
            map.put("修改时间", quarterlyAssessmentInterfaceDTO.getUpdateTime());
            map.put("修改人id", quarterlyAssessmentInterfaceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void insertMainAndSon(List<SwmQuarterlyAssessmentInterface> swmQuarterlyAssessmentInterfaces, Long groupId, Boolean reImportFlag) {
        for (SwmQuarterlyAssessmentInterface swm : swmQuarterlyAssessmentInterfaces
             ) {
            if (reImportFlag) {
                swm.setDataStatus("F");
                swm.setErrorMsg("");
            }
            swm.setGroupId(groupId);
            swm.setCreateBy(SecurityUtils.getUserId());
            swm.setUpdateBy(SecurityUtils.getUserId());
            swmQuarterlyAssessmentInterfaceDao.insertByInterface(swm);
        }
        swmQuarterlyAssessmentService.interfaceToMain(groupId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SwmQuarterlyAssessmentInterface> insertExcel(List<SwmQuarterlyAssessmentInterface> swmQuarterlyAssessmentInterfaces, Boolean reImportFlag) {
        FndInterfaceOperationRecord fndInterfaceOperationRecord = new FndInterfaceOperationRecord();
        fndInterfaceOperationRecord.setOperationValue("insertQuarterAssessment");
        fndInterfaceOperationRecord.setOperationDescription("季度考核导入");
        fndInterfaceOperationRecordService.insert(fndInterfaceOperationRecord);
        Long tempGroupId = fndInterfaceOperationRecord.getId();
        try {
            instance.insertMainAndSon(swmQuarterlyAssessmentInterfaces, tempGroupId , reImportFlag);
        } catch (Exception e) {
            fndInterfaceOperationRecord.setDataProcessingDescription(ThrowableUtil.getStackTrace(e));
            fndInterfaceOperationRecord.setSuccessFlag(false);
            fndInterfaceOperationRecordService.update(fndInterfaceOperationRecord);
            throw new InfoCheckWarningMessException("Excel导入失败，请联系管理员。");
        } finally {
            fndInterfaceOperationRecord.setSuccessFlag(true);
            fndInterfaceOperationRecord.setDataProcessingDescription("导入正常");
            fndInterfaceOperationRecordService.update(fndInterfaceOperationRecord);
        }
        SwmQuarterlyAssessmentInterfaceQueryCriteria criteria = new SwmQuarterlyAssessmentInterfaceQueryCriteria();
        criteria.setGroupId(tempGroupId);
        criteria.setDataStatus("F");
        return swmQuarterlyAssessmentInterfaceDao.listAllByCriteria(criteria);
    }
}
