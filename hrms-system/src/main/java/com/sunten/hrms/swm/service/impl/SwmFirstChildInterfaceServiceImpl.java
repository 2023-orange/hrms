package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.swm.domain.SwmFirstChildInterface;
import com.sunten.hrms.swm.dao.SwmFirstChildInterfaceDao;
import com.sunten.hrms.swm.dto.SwmConsolationMoneyInterfaceQueryCriteria;
import com.sunten.hrms.swm.service.SwmFirstChildInterfaceService;
import com.sunten.hrms.swm.dto.SwmFirstChildInterfaceDTO;
import com.sunten.hrms.swm.dto.SwmFirstChildInterfaceQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmFirstChildInterfaceMapper;
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
 * 第一胎子女信息登记表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-08-10
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmFirstChildInterfaceServiceImpl extends ServiceImpl<SwmFirstChildInterfaceDao, SwmFirstChildInterface> implements SwmFirstChildInterfaceService {
    private final SwmFirstChildInterfaceDao swmFirstChildInterfaceDao;
    private final SwmFirstChildInterfaceMapper swmFirstChildInterfaceMapper;
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;
    @Autowired
    private SwmFirstChildInterfaceService instance;

    public SwmFirstChildInterfaceServiceImpl(SwmFirstChildInterfaceDao swmFirstChildInterfaceDao, SwmFirstChildInterfaceMapper swmFirstChildInterfaceMapper,
                                             FndInterfaceOperationRecordService fndInterfaceOperationRecordService) {
        this.swmFirstChildInterfaceDao = swmFirstChildInterfaceDao;
        this.swmFirstChildInterfaceMapper = swmFirstChildInterfaceMapper;
        this.fndInterfaceOperationRecordService = fndInterfaceOperationRecordService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmFirstChildInterfaceDTO insert(SwmFirstChildInterface firstChildInterfaceNew) {
        swmFirstChildInterfaceDao.insertAllColumn(firstChildInterfaceNew);
        return swmFirstChildInterfaceMapper.toDto(firstChildInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmFirstChildInterface firstChildInterface = new SwmFirstChildInterface();
        firstChildInterface.setId(id);
        this.delete(firstChildInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmFirstChildInterface firstChildInterface) {
        // TODO    确认删除前是否需要做检查
        swmFirstChildInterfaceDao.deleteByEntityKey(firstChildInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmFirstChildInterface firstChildInterfaceNew) {
        SwmFirstChildInterface firstChildInterfaceInDb = Optional.ofNullable(swmFirstChildInterfaceDao.getByKey(firstChildInterfaceNew.getId())).orElseGet(SwmFirstChildInterface::new);
        ValidationUtil.isNull(firstChildInterfaceInDb.getId() ,"FirstChildInterface", "id", firstChildInterfaceNew.getId());
        firstChildInterfaceNew.setId(firstChildInterfaceInDb.getId());
        swmFirstChildInterfaceDao.updateAllColumnByKey(firstChildInterfaceNew);
    }

    @Override
    public SwmFirstChildInterfaceDTO getByKey(Long id) {
        SwmFirstChildInterface firstChildInterface = Optional.ofNullable(swmFirstChildInterfaceDao.getByKey(id)).orElseGet(SwmFirstChildInterface::new);
        ValidationUtil.isNull(firstChildInterface.getId() ,"FirstChildInterface", "id", id);
        return swmFirstChildInterfaceMapper.toDto(firstChildInterface);
    }

    @Override
    public List<SwmFirstChildInterfaceDTO> listAll(SwmFirstChildInterfaceQueryCriteria criteria) {
        return swmFirstChildInterfaceMapper.toDto(swmFirstChildInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmFirstChildInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<SwmFirstChildInterface> page = PageUtil.startPage(pageable);
        List<SwmFirstChildInterface> firstChildInterfaces = swmFirstChildInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmFirstChildInterfaceMapper.toDto(firstChildInterfaces), page.getTotal());
    }

    @Override
    public void download(List<SwmFirstChildInterfaceDTO> firstChildInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmFirstChildInterfaceDTO firstChildInterfaceDTO : firstChildInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("工牌号", firstChildInterfaceDTO.getWorkCard());
            map.put("数据分组id", firstChildInterfaceDTO.getGroupId());
            map.put("员工id", firstChildInterfaceDTO.getEmployeeId());
            map.put("操作码", firstChildInterfaceDTO.getOperationCode());
            map.put("错误信息", firstChildInterfaceDTO.getErrorMsg());
            map.put("数据状态", firstChildInterfaceDTO.getDataStatus());
            map.put("弹性域", firstChildInterfaceDTO.getAttribute1());
            map.put("弹性域", firstChildInterfaceDTO.getAttribute2());
            map.put("弹性域", firstChildInterfaceDTO.getAttribute3());
            map.put("出生日期", firstChildInterfaceDTO.getDate());
            map.put("子女名称", firstChildInterfaceDTO.getChildName());
            map.put("员工姓名", firstChildInterfaceDTO.getName());
            map.put("id", firstChildInterfaceDTO.getId());
            map.put("创建时间", firstChildInterfaceDTO.getCreateTime());
            map.put("创建人ID", firstChildInterfaceDTO.getCreateBy());
            map.put("修改时间", firstChildInterfaceDTO.getUpdateTime());
            map.put("修改人ID", firstChildInterfaceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<SwmFirstChildInterfaceDTO> importFirstChildExcel(List<SwmFirstChildInterface> swmFirstChildInterfaces) {
        FndInterfaceOperationRecord fndInterfaceOperationRecord = new FndInterfaceOperationRecord();
        fndInterfaceOperationRecord.setOperationValue("insertFirstChild");
        fndInterfaceOperationRecord.setOperationDescription("导入第一胎子女信息");
        fndInterfaceOperationRecordService.insert(fndInterfaceOperationRecord);
        try {
            instance.insertMainAndSon(swmFirstChildInterfaces, fndInterfaceOperationRecord.getId());
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
        SwmFirstChildInterfaceQueryCriteria swmFirstChildInterfaceQueryCriteria = new SwmFirstChildInterfaceQueryCriteria();
        swmFirstChildInterfaceQueryCriteria.setGroupId(fndInterfaceOperationRecord.getId());
        return swmFirstChildInterfaceMapper.toDto(swmFirstChildInterfaceDao.listAllByCriteria(swmFirstChildInterfaceQueryCriteria));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void insertMainAndSon(List<SwmFirstChildInterface> swmFirstChildInterfaces, Long groupId) {
        for (SwmFirstChildInterface swm : swmFirstChildInterfaces
             ) {
            swm.setGroupId(groupId);
            swmFirstChildInterfaceDao.insertFirstChildByExcel(swm);
        }
    }
}
