package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.swm.dao.SwmFloatingWageDao;
import com.sunten.hrms.swm.domain.SwmFloatingWage;
import com.sunten.hrms.swm.domain.SwmFloatingWageInterface;
import com.sunten.hrms.swm.dao.SwmFloatingWageInterfaceDao;
import com.sunten.hrms.swm.dto.SwmFloatingWageQueryCriteria;
import com.sunten.hrms.swm.service.SwmFloatingWageInterfaceService;
import com.sunten.hrms.swm.dto.SwmFloatingWageInterfaceDTO;
import com.sunten.hrms.swm.dto.SwmFloatingWageInterfaceQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmFloatingWageInterfaceMapper;
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

/**
 * <p>
 * 浮动工资接口表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmFloatingWageInterfaceServiceImpl extends ServiceImpl<SwmFloatingWageInterfaceDao, SwmFloatingWageInterface> implements SwmFloatingWageInterfaceService {
    private final SwmFloatingWageInterfaceDao swmFloatingWageInterfaceDao;
    private final SwmFloatingWageInterfaceMapper swmFloatingWageInterfaceMapper;
    private final SwmFloatingWageDao swmFloatingWageDao;
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;
    @Autowired
    private SwmFloatingWageInterfaceService instance;

    public SwmFloatingWageInterfaceServiceImpl(SwmFloatingWageInterfaceDao swmFloatingWageInterfaceDao, SwmFloatingWageInterfaceMapper swmFloatingWageInterfaceMapper,
                                               SwmFloatingWageDao swmFloatingWageDao,FndInterfaceOperationRecordService fndInterfaceOperationRecordService) {
        this.swmFloatingWageDao = swmFloatingWageDao;
        this.swmFloatingWageInterfaceDao = swmFloatingWageInterfaceDao;
        this.swmFloatingWageInterfaceMapper = swmFloatingWageInterfaceMapper;
        this.fndInterfaceOperationRecordService =fndInterfaceOperationRecordService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmFloatingWageInterfaceDTO insert(SwmFloatingWageInterface floatingWageInterfaceNew) {
        swmFloatingWageInterfaceDao.insertAllColumn(floatingWageInterfaceNew);
        return swmFloatingWageInterfaceMapper.toDto(floatingWageInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmFloatingWageInterface floatingWageInterface = new SwmFloatingWageInterface();
        floatingWageInterface.setId(id);
        this.delete(floatingWageInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmFloatingWageInterface floatingWageInterface) {
        // TODO    确认删除前是否需要做检查
        swmFloatingWageInterfaceDao.deleteByEntityKey(floatingWageInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmFloatingWageInterface floatingWageInterfaceNew) {
        SwmFloatingWageInterface floatingWageInterfaceInDb = Optional.ofNullable(swmFloatingWageInterfaceDao.getByKey(floatingWageInterfaceNew.getId())).orElseGet(SwmFloatingWageInterface::new);
        ValidationUtil.isNull(floatingWageInterfaceInDb.getId() ,"FloatingWageInterface", "id", floatingWageInterfaceNew.getId());
        floatingWageInterfaceNew.setId(floatingWageInterfaceInDb.getId());
        swmFloatingWageInterfaceDao.updateAllColumnByKey(floatingWageInterfaceNew);
    }

    @Override
    public SwmFloatingWageInterfaceDTO getByKey(Long id) {
        SwmFloatingWageInterface floatingWageInterface = Optional.ofNullable(swmFloatingWageInterfaceDao.getByKey(id)).orElseGet(SwmFloatingWageInterface::new);
        ValidationUtil.isNull(floatingWageInterface.getId() ,"FloatingWageInterface", "id", id);
        return swmFloatingWageInterfaceMapper.toDto(floatingWageInterface);
    }

    @Override
    public List<SwmFloatingWageInterfaceDTO> listAll(SwmFloatingWageInterfaceQueryCriteria criteria) {
        return swmFloatingWageInterfaceMapper.toDto(swmFloatingWageInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmFloatingWageInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<SwmFloatingWageInterface> page = PageUtil.startPage(pageable);
        List<SwmFloatingWageInterface> floatingWageInterfaces = swmFloatingWageInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmFloatingWageInterfaceMapper.toDto(floatingWageInterfaces), page.getTotal());
    }

    @Override
    public void download(List<SwmFloatingWageInterfaceDTO> floatingWageInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmFloatingWageInterfaceDTO floatingWageInterfaceDTO : floatingWageInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id主键", floatingWageInterfaceDTO.getId());
            map.put("数据分组id", floatingWageInterfaceDTO.getGroupId());
            map.put("员工id", floatingWageInterfaceDTO.getEmployeeId());
            map.put("所得期间（格式：年.月）", floatingWageInterfaceDTO.getIncomePeriod());
            map.put("工牌号", floatingWageInterfaceDTO.getWorkCard());
            map.put("员工姓名", floatingWageInterfaceDTO.getEmployeeName());
            map.put("调配绩效工资", floatingWageInterfaceDTO.getAllocatePerformancePay());
            map.put("操作码", floatingWageInterfaceDTO.getOperationCode());
            map.put("错误信息", floatingWageInterfaceDTO.getErrorMsg());
            map.put("数据状态", floatingWageInterfaceDTO.getDataStatus());
            map.put("弹性域", floatingWageInterfaceDTO.getAttribute1());
            map.put("弹性域", floatingWageInterfaceDTO.getAttribute2());
            map.put("弹性域", floatingWageInterfaceDTO.getAttribute3());
            map.put("创建时间", floatingWageInterfaceDTO.getCreateTime());
            map.put("创建人id", floatingWageInterfaceDTO.getCreateBy());
            map.put("修改时间", floatingWageInterfaceDTO.getUpdateTime());
            map.put("修改人id", floatingWageInterfaceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<SwmFloatingWageInterface> insertExcel(List<SwmFloatingWageInterface> swmFloatingWageInterfaces, Boolean reImportFlag) {
        SwmFloatingWageQueryCriteria swmFloatingWageQueryCriteria = new SwmFloatingWageQueryCriteria();
        swmFloatingWageQueryCriteria.setPeriod(swmFloatingWageInterfaces.get(0).getIncomePeriod());
        List<SwmFloatingWage> tempList = swmFloatingWageDao.listAllByCriteria(swmFloatingWageQueryCriteria);
        if (tempList.size() <= 0) {
            throw new InfoCheckWarningMessException("该所得期间尚未生成浮动工资，不允许导入");
        } else {
            if (tempList.get(0).getFrozenFlag()) {
                throw new InfoCheckWarningMessException("该所得期间的浮动工资已被冻结，不允许导入");
            }
        }
        FndInterfaceOperationRecord fndInterfaceOperationRecord = new FndInterfaceOperationRecord();
        fndInterfaceOperationRecord.setOperationValue("insertAllocatePerformancePay");
        fndInterfaceOperationRecord.setOperationDescription("调配绩效工资导入");
        fndInterfaceOperationRecordService.insert(fndInterfaceOperationRecord);
        try {
            instance.insertMainAndSon(swmFloatingWageInterfaces, fndInterfaceOperationRecord.getId(), reImportFlag);
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
        SwmFloatingWageInterfaceQueryCriteria swmFloatingWageInterfaceQueryCriteria = new SwmFloatingWageInterfaceQueryCriteria();
        swmFloatingWageInterfaceQueryCriteria.setGroupId(fndInterfaceOperationRecord.getId());
        swmFloatingWageInterfaceQueryCriteria.setDataStatus("F");
        List<SwmFloatingWageInterface> swmFloatingWageInterfaceList = swmFloatingWageInterfaceDao.listAllByCriteria(swmFloatingWageInterfaceQueryCriteria);
        if (swmFloatingWageInterfaceList.size() == 0) {
            swmFloatingWageInterfaceList.add(new SwmFloatingWageInterface().setGroupId(fndInterfaceOperationRecord.getId()));
        }
        return swmFloatingWageInterfaceList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void insertMainAndSon(List<SwmFloatingWageInterface> swmFloatingWageInterfaces, Long groupId, Boolean reImportFlag) {
        for (SwmFloatingWageInterface swm : swmFloatingWageInterfaces
             ) {
            if (reImportFlag) {
                swm.setDataStatus("F");
                swm.setErrorMsg("");
            }
            swm.setGroupId(groupId);
            swmFloatingWageInterfaceDao.insertAllocatePerformancePay(swm);
        }
        // 更新调配绩效工资的同时更新应发工资以及实发工资
        swmFloatingWageDao.interfaceToMain(groupId);
        swmFloatingWageDao.updateMonthlyPerformanceSalary(groupId);
        swmFloatingWageDao.updateWageAndNetAfterImportExcel(groupId);
    }

    @Override
    public List<SwmFloatingWageInterface> getSummaryByImportList(String incomePeriod, Set<String> workCards, Set<Long> groupIds) {
        return swmFloatingWageInterfaceDao.getSummaryByImportList(incomePeriod, workCards, groupIds);
    }
}
