package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcAbnormalAttendanceRecordDao;
import com.sunten.hrms.ac.domain.AcAbnormalAttendanceRecord;
import com.sunten.hrms.ac.dto.AcAbnormalAttendanceRecordDTO;
import com.sunten.hrms.ac.dto.AcAbnormalAttendanceRecordQueryCriteria;
import com.sunten.hrms.ac.mapper.AcAbnormalAttendanceRecordMapper;
import com.sunten.hrms.ac.service.AcAbnormalAttendanceRecordService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 异常考勤执行记录 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcAbnormalAttendanceRecordServiceImpl extends ServiceImpl<AcAbnormalAttendanceRecordDao, AcAbnormalAttendanceRecord> implements AcAbnormalAttendanceRecordService {
    private final AcAbnormalAttendanceRecordDao acAbnormalAttendanceRecordDao;
    private final AcAbnormalAttendanceRecordMapper acAbnormalAttendanceRecordMapper;

    public AcAbnormalAttendanceRecordServiceImpl(AcAbnormalAttendanceRecordDao acAbnormalAttendanceRecordDao, AcAbnormalAttendanceRecordMapper acAbnormalAttendanceRecordMapper) {
        this.acAbnormalAttendanceRecordDao = acAbnormalAttendanceRecordDao;
        this.acAbnormalAttendanceRecordMapper = acAbnormalAttendanceRecordMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcAbnormalAttendanceRecordDTO insert(AcAbnormalAttendanceRecord abnormalAttendanceRecordNew) {
        acAbnormalAttendanceRecordDao.insertAllColumn(abnormalAttendanceRecordNew);
        return acAbnormalAttendanceRecordMapper.toDto(abnormalAttendanceRecordNew);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true, rollbackFor = Exception.class)
    public AcAbnormalAttendanceRecordDTO insertByNewTransation(AcAbnormalAttendanceRecord acAbnormalAttendanceRecord) {
        acAbnormalAttendanceRecordDao.insertAllColumn(acAbnormalAttendanceRecord);
        return acAbnormalAttendanceRecordMapper.toDto(acAbnormalAttendanceRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AcAbnormalAttendanceRecord abnormalAttendanceRecord = new AcAbnormalAttendanceRecord();
        abnormalAttendanceRecord.setId(id);
        this.delete(abnormalAttendanceRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcAbnormalAttendanceRecord abnormalAttendanceRecord) {
        // TODO    确认删除前是否需要做检查
        acAbnormalAttendanceRecordDao.deleteByEntityKey(abnormalAttendanceRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcAbnormalAttendanceRecord abnormalAttendanceRecordNew) {
        AcAbnormalAttendanceRecord abnormalAttendanceRecordInDb = Optional.ofNullable(acAbnormalAttendanceRecordDao.getByKey(abnormalAttendanceRecordNew.getId())).orElseGet(AcAbnormalAttendanceRecord::new);
        ValidationUtil.isNull(abnormalAttendanceRecordInDb.getId(), "AbnormalAttendanceRecord", "id", abnormalAttendanceRecordNew.getId());
        abnormalAttendanceRecordNew.setId(abnormalAttendanceRecordInDb.getId());
        acAbnormalAttendanceRecordDao.updateAllColumnByKey(abnormalAttendanceRecordNew);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true, rollbackFor = Exception.class)
    public void updateByNewTransation(AcAbnormalAttendanceRecord acAbnormalAttendanceRecord) {
        AcAbnormalAttendanceRecord abnormalAttendanceRecordInDb = Optional.ofNullable(acAbnormalAttendanceRecordDao.getByKey(acAbnormalAttendanceRecord.getId())).orElseGet(AcAbnormalAttendanceRecord::new);
        ValidationUtil.isNull(abnormalAttendanceRecordInDb.getId(), "AbnormalAttendanceRecord", "id", acAbnormalAttendanceRecord.getId());
        acAbnormalAttendanceRecord.setId(abnormalAttendanceRecordInDb.getId());
        acAbnormalAttendanceRecordDao.updateAllColumnByKey(acAbnormalAttendanceRecord);
    }

    @Override
    public AcAbnormalAttendanceRecordDTO getByKey(Long id) {
        AcAbnormalAttendanceRecord abnormalAttendanceRecord = Optional.ofNullable(acAbnormalAttendanceRecordDao.getByKey(id)).orElseGet(AcAbnormalAttendanceRecord::new);
        ValidationUtil.isNull(abnormalAttendanceRecord.getId(), "AbnormalAttendanceRecord", "id", id);
        return acAbnormalAttendanceRecordMapper.toDto(abnormalAttendanceRecord);
    }

    @Override
    public List<AcAbnormalAttendanceRecordDTO> listAll(AcAbnormalAttendanceRecordQueryCriteria criteria) {
        return acAbnormalAttendanceRecordMapper.toDto(acAbnormalAttendanceRecordDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcAbnormalAttendanceRecordQueryCriteria criteria, Pageable pageable) {
        Page<AcAbnormalAttendanceRecord> page = PageUtil.startPage(pageable);
        List<AcAbnormalAttendanceRecord> abnormalAttendanceRecords = acAbnormalAttendanceRecordDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acAbnormalAttendanceRecordMapper.toDto(abnormalAttendanceRecords), page.getTotal());
    }

    @Override
    public void download(List<AcAbnormalAttendanceRecordDTO> abnormalAttendanceRecordDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcAbnormalAttendanceRecordDTO abnormalAttendanceRecordDTO : abnormalAttendanceRecordDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("开始日期", abnormalAttendanceRecordDTO.getStartTime());
            map.put("结束日期", abnormalAttendanceRecordDTO.getEndTime());
            map.put("执行状态", abnormalAttendanceRecordDTO.getExecutionStatus());
            map.put("执行时间", abnormalAttendanceRecordDTO.getExecutionTime());
            map.put("弹性域1", abnormalAttendanceRecordDTO.getAttribute1());
            map.put("弹性域2", abnormalAttendanceRecordDTO.getAttribute2());
            map.put("弹性域3", abnormalAttendanceRecordDTO.getAttribute3());
            map.put("弹性域4", abnormalAttendanceRecordDTO.getAttribute4());
            map.put("弹性域5", abnormalAttendanceRecordDTO.getAttribute5());
            map.put("id", abnormalAttendanceRecordDTO.getId());
            map.put("createBy", abnormalAttendanceRecordDTO.getCreateBy());
            map.put("updateTime", abnormalAttendanceRecordDTO.getUpdateTime());
            map.put("updateBy", abnormalAttendanceRecordDTO.getUpdateBy());
            map.put("createTime", abnormalAttendanceRecordDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
