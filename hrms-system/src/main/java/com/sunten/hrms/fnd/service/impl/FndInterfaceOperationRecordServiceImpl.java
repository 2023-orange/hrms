package com.sunten.hrms.fnd.service.impl;

import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.dao.FndInterfaceOperationRecordDao;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.fnd.dto.FndInterfaceOperationRecordDTO;
import com.sunten.hrms.fnd.dto.FndInterfaceOperationRecordQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndInterfaceOperationRecordMapper;
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
 * 接口操作记录表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-26
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndInterfaceOperationRecordServiceImpl extends ServiceImpl<FndInterfaceOperationRecordDao, FndInterfaceOperationRecord> implements FndInterfaceOperationRecordService {
    private final FndInterfaceOperationRecordDao fndInterfaceOperationRecordDao;
    private final FndInterfaceOperationRecordMapper fndInterfaceOperationRecordMapper;

    public FndInterfaceOperationRecordServiceImpl(FndInterfaceOperationRecordDao fndInterfaceOperationRecordDao, FndInterfaceOperationRecordMapper fndInterfaceOperationRecordMapper) {
        this.fndInterfaceOperationRecordDao = fndInterfaceOperationRecordDao;
        this.fndInterfaceOperationRecordMapper = fndInterfaceOperationRecordMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FndInterfaceOperationRecordDTO insert(FndInterfaceOperationRecord interfaceOperationRecordNew) {
        fndInterfaceOperationRecordDao.insertAllColumn(interfaceOperationRecordNew);
        return fndInterfaceOperationRecordMapper.toDto(interfaceOperationRecordNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        FndInterfaceOperationRecord interfaceOperationRecord = new FndInterfaceOperationRecord();
        interfaceOperationRecord.setId(id);
        this.delete(interfaceOperationRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(FndInterfaceOperationRecord interfaceOperationRecord) {
        // TODO    确认删除前是否需要做检查
        fndInterfaceOperationRecordDao.deleteByEntityKey(interfaceOperationRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FndInterfaceOperationRecord interfaceOperationRecordNew) {
        FndInterfaceOperationRecord interfaceOperationRecordInDb = Optional.ofNullable(fndInterfaceOperationRecordDao.getByKey(interfaceOperationRecordNew.getId())).orElseGet(FndInterfaceOperationRecord::new);
        ValidationUtil.isNull(interfaceOperationRecordInDb.getId() ,"InterfaceOperationRecord", "id", interfaceOperationRecordNew.getId());
        interfaceOperationRecordNew.setId(interfaceOperationRecordInDb.getId());
        fndInterfaceOperationRecordDao.updateAllColumnByKey(interfaceOperationRecordNew);
    }

    @Override
    public FndInterfaceOperationRecordDTO getByKey(Long id) {
        FndInterfaceOperationRecord interfaceOperationRecord = Optional.ofNullable(fndInterfaceOperationRecordDao.getByKey(id)).orElseGet(FndInterfaceOperationRecord::new);
        ValidationUtil.isNull(interfaceOperationRecord.getId() ,"InterfaceOperationRecord", "id", id);
        return fndInterfaceOperationRecordMapper.toDto(interfaceOperationRecord);
    }

    @Override
    public List<FndInterfaceOperationRecordDTO> listAll(FndInterfaceOperationRecordQueryCriteria criteria) {
        return fndInterfaceOperationRecordMapper.toDto(fndInterfaceOperationRecordDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(FndInterfaceOperationRecordQueryCriteria criteria, Pageable pageable) {
        Page<FndInterfaceOperationRecord> page = PageUtil.startPage(pageable);
        List<FndInterfaceOperationRecord> interfaceOperationRecords = fndInterfaceOperationRecordDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndInterfaceOperationRecordMapper.toDto(interfaceOperationRecords), page.getTotal());
    }

    @Override
    public void download(List<FndInterfaceOperationRecordDTO> interfaceOperationRecordDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndInterfaceOperationRecordDTO interfaceOperationRecordDTO : interfaceOperationRecordDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id主键", interfaceOperationRecordDTO.getId());
            map.put("接口底层名名（真实名称放集值", interfaceOperationRecordDTO.getOperationValue());
            map.put("是否成功（1成功，0失败）", interfaceOperationRecordDTO.getSuccessFlag());
            map.put("数据处理描述", interfaceOperationRecordDTO.getDataProcessingDescription());
            map.put("操作描述", interfaceOperationRecordDTO.getOperationDescription());
            map.put("创建时间", interfaceOperationRecordDTO.getCreateTime());
            map.put("创建人id", interfaceOperationRecordDTO.getCreateBy());
            map.put("修改时间", interfaceOperationRecordDTO.getUpdateTime());
            map.put("修改人id", interfaceOperationRecordDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<FndInterfaceOperationRecordDTO> getListByOperationValue(String operationValue) {
        FndInterfaceOperationRecordQueryCriteria fndInterfaceOperationRecordQueryCriteria = new FndInterfaceOperationRecordQueryCriteria();
        fndInterfaceOperationRecordQueryCriteria.setOperationValue(operationValue);
        return fndInterfaceOperationRecordMapper.toDto(fndInterfaceOperationRecordDao.listAllByCriteria(fndInterfaceOperationRecordQueryCriteria));
    }
}
