package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcRegimeRelationDao;
import com.sunten.hrms.ac.dao.AcRegimeTimeDao;
import com.sunten.hrms.ac.domain.AcRegimeRelation;
import com.sunten.hrms.ac.domain.AcRegimeTime;
import com.sunten.hrms.ac.dto.AcRegimeRelationQueryCriteria;
import com.sunten.hrms.ac.dto.AcRegimeTimeDTO;
import com.sunten.hrms.ac.dto.AcRegimeTimeQueryCriteria;
import com.sunten.hrms.ac.mapper.AcRegimeTimeMapper;
import com.sunten.hrms.ac.service.AcRegimeTimeService;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 考勤制度时间段表 服务实现类
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcRegimeTimeServiceImpl extends ServiceImpl<AcRegimeTimeDao, AcRegimeTime> implements AcRegimeTimeService {
    private final AcRegimeTimeDao acRegimeTimeDao;
    private final AcRegimeTimeMapper acRegimeTimeMapper;
    private final AcRegimeRelationDao acRegimeRelationDao;

    public AcRegimeTimeServiceImpl(AcRegimeTimeDao acRegimeTimeDao, AcRegimeTimeMapper acRegimeTimeMapper, AcRegimeRelationDao regimeRelationDao) {
        this.acRegimeTimeDao = acRegimeTimeDao;
        this.acRegimeTimeMapper = acRegimeTimeMapper;
        this.acRegimeRelationDao = regimeRelationDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcRegimeTimeDTO insert(AcRegimeTime regimeTimeNew) {
        checkTime(regimeTimeNew);
        acRegimeTimeDao.insertAllColumn(regimeTimeNew);
        return acRegimeTimeMapper.toDto(regimeTimeNew);
    }

    private void checkTime(AcRegimeTime regimeTimeNew) {
        if (!regimeTimeNew.getExtendTimeFlag()) { // 跨日
            if (regimeTimeNew.getTimeFrom().isAfter(regimeTimeNew.getTimeTo()) ||
                    regimeTimeNew.getTimeFrom() == regimeTimeNew.getTimeTo()) {
                throw new InfoCheckWarningMessException("开始时间必须早于结束时间");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 删除前需要判断该考勤时间段是否已被生效的考勤制度使用
        AcRegimeRelationQueryCriteria regimeRelationQueryCriteria = new AcRegimeRelationQueryCriteria();
        regimeRelationQueryCriteria.setEnabled(true);
        regimeRelationQueryCriteria.setRegimeTimeId(id);
        List<AcRegimeRelation> acRegimeRelationList = acRegimeRelationDao.listAllByCriteria(regimeRelationQueryCriteria);
        if (acRegimeRelationList.size() > 0) {
            throw new InfoCheckWarningMessException("该考勤制度时间段已被考勤制度:" + acRegimeRelationList.get(0).getAcRegime().getRegimeName() + "使用,若要删除请先解除关联");
        } else {
            AcRegimeTime regimeTime = new AcRegimeTime();
            regimeTime.setId(id);
            acRegimeTimeDao.deleteByEnabled(regimeTime);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcRegimeTime regimeTime) {
        // TODO    确认删除前是否需要做检查
        acRegimeTimeDao.deleteByEntityKey(regimeTime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcRegimeTime regimeTimeNew) {
        AcRegimeTime regimeTimeInDb = Optional.ofNullable(acRegimeTimeDao.getByKey(regimeTimeNew.getId())).orElseGet(AcRegimeTime::new);
        ValidationUtil.isNull(regimeTimeInDb.getId(), "RegimeTime", "id", regimeTimeNew.getId());
        regimeTimeNew.setId(regimeTimeInDb.getId());

        checkTime(regimeTimeNew);

        if (!regimeTimeNew.getEnabledFlag()) { // 禁用需要判断是否有regime在使用，即生效的关系
            AcRegimeRelationQueryCriteria regimeRelationQueryCriteria = new AcRegimeRelationQueryCriteria();
            regimeRelationQueryCriteria.setEnabled(true);
            regimeRelationQueryCriteria.setRegimeTimeId(regimeTimeNew.getId());
            List<AcRegimeRelation> acRegimeRelationList = acRegimeRelationDao.listAllByCriteria(regimeRelationQueryCriteria);
            if (acRegimeRelationList.size() > 0) {
                throw new InfoCheckWarningMessException("该考勤制度时间段已被考勤制度:" + acRegimeRelationList.get(0).getAcRegime().getRegimeName() + "使用,若要删除请先解除关联");
            }
        }
        acRegimeTimeDao.updateAllColumnByKey(regimeTimeNew);
    }

    @Override
    public AcRegimeTimeDTO getByKey(Long id) {
        AcRegimeTime regimeTime = Optional.ofNullable(acRegimeTimeDao.getByKey(id)).orElseGet(AcRegimeTime::new);
        ValidationUtil.isNull(regimeTime.getId(), "RegimeTime", "id", id);
        return acRegimeTimeMapper.toDto(regimeTime);
    }

    @Override
    public List<AcRegimeTimeDTO> listAll(AcRegimeTimeQueryCriteria criteria) {
        return acRegimeTimeMapper.toDto(acRegimeTimeDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcRegimeTimeQueryCriteria criteria, Pageable pageable) {
        Page<AcRegimeTime> page = PageUtil.startPage(pageable);
        List<AcRegimeTime> regimeTimes = acRegimeTimeDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acRegimeTimeMapper.toDto(regimeTimes), page.getTotal());
    }

    @Override
    public void download(List<AcRegimeTimeDTO> regimeTimeDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcRegimeTimeDTO regimeTimeDTO : regimeTimeDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", regimeTimeDTO.getId());
            map.put("排班时间开始", regimeTimeDTO.getTimeFrom());
            map.put("排班时间结束", regimeTimeDTO.getTimeTo());
            map.put("是否跨日", regimeTimeDTO.getExtendTimeFlag());
            map.put("弹性域1", regimeTimeDTO.getAttribute1());
            map.put("弹性域2", regimeTimeDTO.getAttribute2());
            map.put("弹性域3", regimeTimeDTO.getAttribute3());
            map.put("有效标记", regimeTimeDTO.getEnabledFlag());
            map.put("创建人id", regimeTimeDTO.getCreateBy());
            map.put("创建时间", regimeTimeDTO.getCreateTime());
            map.put("修改人id", regimeTimeDTO.getUpdateBy());
            map.put("修改时间", regimeTimeDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
