package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.swm.domain.SwmConsolationMoneyInterface;
import com.sunten.hrms.swm.dao.SwmConsolationMoneyInterfaceDao;
import com.sunten.hrms.swm.service.SwmConsolationMoneyInterfaceService;
import com.sunten.hrms.swm.dto.SwmConsolationMoneyInterfaceDTO;
import com.sunten.hrms.swm.dto.SwmConsolationMoneyInterfaceQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmConsolationMoneyInterfaceMapper;
import com.sunten.hrms.swm.service.SwmConsolationMoneyService;
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
 * 慰问金接口表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-08-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmConsolationMoneyInterfaceServiceImpl extends ServiceImpl<SwmConsolationMoneyInterfaceDao, SwmConsolationMoneyInterface> implements SwmConsolationMoneyInterfaceService {
    private final SwmConsolationMoneyInterfaceDao swmConsolationMoneyInterfaceDao;
    private final SwmConsolationMoneyInterfaceMapper swmConsolationMoneyInterfaceMapper;
    private final SwmConsolationMoneyService swmConsolationMoneyService;
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;
    @Autowired
    private SwmConsolationMoneyInterfaceService instance;

    public SwmConsolationMoneyInterfaceServiceImpl(SwmConsolationMoneyInterfaceDao swmConsolationMoneyInterfaceDao,
                                                   SwmConsolationMoneyInterfaceMapper swmConsolationMoneyInterfaceMapper,
                                                   SwmConsolationMoneyService swmConsolationMoneyService, FndInterfaceOperationRecordService fndInterfaceOperationRecordService) {
        this.swmConsolationMoneyInterfaceDao = swmConsolationMoneyInterfaceDao;
        this.swmConsolationMoneyInterfaceMapper = swmConsolationMoneyInterfaceMapper;
        this.swmConsolationMoneyService = swmConsolationMoneyService;
        this.fndInterfaceOperationRecordService = fndInterfaceOperationRecordService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmConsolationMoneyInterfaceDTO insert(SwmConsolationMoneyInterface consolationMoneyInterfaceNew) {
        swmConsolationMoneyInterfaceDao.insertAllColumn(consolationMoneyInterfaceNew);
        return swmConsolationMoneyInterfaceMapper.toDto(consolationMoneyInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmConsolationMoneyInterface consolationMoneyInterface = new SwmConsolationMoneyInterface();
        consolationMoneyInterface.setId(id);
        this.delete(consolationMoneyInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmConsolationMoneyInterface consolationMoneyInterface) {
        // TODO    确认删除前是否需要做检查
        swmConsolationMoneyInterfaceDao.deleteByEntityKey(consolationMoneyInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmConsolationMoneyInterface consolationMoneyInterfaceNew) {
        SwmConsolationMoneyInterface consolationMoneyInterfaceInDb = Optional.ofNullable(swmConsolationMoneyInterfaceDao.getByKey(consolationMoneyInterfaceNew.getId())).orElseGet(SwmConsolationMoneyInterface::new);
        ValidationUtil.isNull(consolationMoneyInterfaceInDb.getId() ,"ConsolationMoneyInterface", "id", consolationMoneyInterfaceNew.getId());
        consolationMoneyInterfaceNew.setId(consolationMoneyInterfaceInDb.getId());
        swmConsolationMoneyInterfaceDao.updateAllColumnByKey(consolationMoneyInterfaceNew);
    }

    @Override
    public SwmConsolationMoneyInterfaceDTO getByKey(Long id) {
        SwmConsolationMoneyInterface consolationMoneyInterface = Optional.ofNullable(swmConsolationMoneyInterfaceDao.getByKey(id)).orElseGet(SwmConsolationMoneyInterface::new);
        ValidationUtil.isNull(consolationMoneyInterface.getId() ,"ConsolationMoneyInterface", "id", id);
        return swmConsolationMoneyInterfaceMapper.toDto(consolationMoneyInterface);
    }

    @Override
    public List<SwmConsolationMoneyInterfaceDTO> listAll(SwmConsolationMoneyInterfaceQueryCriteria criteria) {
        return swmConsolationMoneyInterfaceMapper.toDto(swmConsolationMoneyInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmConsolationMoneyInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<SwmConsolationMoneyInterface> page = PageUtil.startPage(pageable);
        List<SwmConsolationMoneyInterface> consolationMoneyInterfaces = swmConsolationMoneyInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmConsolationMoneyInterfaceMapper.toDto(consolationMoneyInterfaces), page.getTotal());
    }

    @Override
    public void download(List<SwmConsolationMoneyInterfaceDTO> consolationMoneyInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmConsolationMoneyInterfaceDTO consolationMoneyInterfaceDTO : consolationMoneyInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("数据分组id", consolationMoneyInterfaceDTO.getGroupId());
            map.put("员工id", consolationMoneyInterfaceDTO.getEmployeeId());
            map.put("工牌号", consolationMoneyInterfaceDTO.getWorkCard());
            map.put("员工姓名", consolationMoneyInterfaceDTO.getEmployeeName());
            map.put("操作码", consolationMoneyInterfaceDTO.getOperationCode());
            map.put("错误信息", consolationMoneyInterfaceDTO.getErrorMsg());
            map.put("数据状态", consolationMoneyInterfaceDTO.getDataStatus());
            map.put("慰问金类别", consolationMoneyInterfaceDTO.getConsolationMoneyType());
            map.put("申请日期", consolationMoneyInterfaceDTO.getApplicationDate());
            map.put("申请金额", consolationMoneyInterfaceDTO.getApplicationMoney());
            map.put("逝世亲属及称谓(类别为丧事时填写)", consolationMoneyInterfaceDTO.getRelativesDied());
            map.put("实际发放金额", consolationMoneyInterfaceDTO.getReleasedMoney());
            map.put("发放标记", consolationMoneyInterfaceDTO.getReleasedFlag());
            map.put("发放日期", consolationMoneyInterfaceDTO.getReleasedTime());
            map.put("有效标记", consolationMoneyInterfaceDTO.getEnabledFlag());
            map.put("id", consolationMoneyInterfaceDTO.getId());
            map.put("创建时间", consolationMoneyInterfaceDTO.getCreateTime());
            map.put("创建人ID", consolationMoneyInterfaceDTO.getCreateBy());
            map.put("修改时间", consolationMoneyInterfaceDTO.getUpdateTime());
            map.put("修改人ID", consolationMoneyInterfaceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void insertMainAndSon(List<SwmConsolationMoneyInterface> swmConsolationMoneyInterfaces, Long groupId, String importType) {
        for (SwmConsolationMoneyInterface swm : swmConsolationMoneyInterfaces
             ) {
            swm.setGroupId(groupId);
            if (importType.equals("oldest")) {
                swmConsolationMoneyInterfaceDao.importOldestConsolationMoney(swm);
            } else {
                swmConsolationMoneyInterfaceDao.importReleasedConsolationMoney(swm);
            }
        }
        if (importType.equals("oldest")) {
            swmConsolationMoneyService.importInterfaceToMain(groupId);
        } else {
            swmConsolationMoneyService.releasedInterfaceToMain(groupId);
        }
    }

    @Override
    public List<SwmConsolationMoneyInterfaceDTO> importExcel(List<SwmConsolationMoneyInterface> swmConsolationMoneyInterfaces, String importType) {
        FndInterfaceOperationRecord fndInterfaceOperationRecord = new FndInterfaceOperationRecord();
        if (importType.equals("oldest")) {
            fndInterfaceOperationRecord.setOperationValue("insertOldestConsolationMoney");
            fndInterfaceOperationRecord.setOperationDescription("导入未上系统前的慰问金");
        } else {
            fndInterfaceOperationRecord.setOperationValue("insertReleasedConsolationMoney");
            fndInterfaceOperationRecord.setOperationDescription("导入审批完的慰问金");
        }
        fndInterfaceOperationRecordService.insert(fndInterfaceOperationRecord);
        try {
            instance.insertMainAndSon(swmConsolationMoneyInterfaces, fndInterfaceOperationRecord.getId(), importType);
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
        SwmConsolationMoneyInterfaceQueryCriteria swmConsolationMoneyInterfaceQueryCriteria = new SwmConsolationMoneyInterfaceQueryCriteria();
        swmConsolationMoneyInterfaceQueryCriteria.setGroupId(fndInterfaceOperationRecord.getId());
        return swmConsolationMoneyInterfaceMapper.toDto(swmConsolationMoneyInterfaceDao.listAllByCriteria(swmConsolationMoneyInterfaceQueryCriteria));
    }
}
