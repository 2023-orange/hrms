package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcDeptAttendanceDao;
import com.sunten.hrms.ac.dao.AcRegimeRelationDao;
import com.sunten.hrms.ac.domain.AcRegimeRelation;
import com.sunten.hrms.ac.dto.AcRegimeRelationDTO;
import com.sunten.hrms.ac.dto.AcRegimeRelationQueryCriteria;
import com.sunten.hrms.ac.mapper.AcRegimeRelationMapper;
import com.sunten.hrms.ac.service.AcRegimeRelationService;
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
 * 考勤制度排班时间关系表 服务实现类
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcRegimeRelationServiceImpl extends ServiceImpl<AcRegimeRelationDao, AcRegimeRelation> implements AcRegimeRelationService {
    private final AcRegimeRelationDao acRegimeRelationDao;
    private final AcRegimeRelationMapper acRegimeRelationMapper;
    private final AcDeptAttendanceDao acDeptAttendanceDao;
    private final AcDeptAttendanceServiceImpl acDeptAttendanceServiceImpl;

    public AcRegimeRelationServiceImpl(AcRegimeRelationDao acRegimeRelationDao, AcRegimeRelationMapper acRegimeRelationMapper,
                                       AcDeptAttendanceDao acDeptAttendanceDao, AcDeptAttendanceServiceImpl acDeptAttendanceServiceImpl) {
        this.acRegimeRelationDao = acRegimeRelationDao;
        this.acRegimeRelationMapper = acRegimeRelationMapper;
        this.acDeptAttendanceDao = acDeptAttendanceDao;
        this.acDeptAttendanceServiceImpl = acDeptAttendanceServiceImpl;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcRegimeRelationDTO insert(AcRegimeRelation regimeRelationNew) {
        acRegimeRelationDao.insertAllColumn(regimeRelationNew);
        return acRegimeRelationMapper.toDto(regimeRelationNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AcRegimeRelation regimeRelation = new AcRegimeRelation();
        regimeRelation.setId(id);
        // 失效时需要判断当前是否有生效的部门制度关系在使用该制度
        acRegimeRelationDao.deleteByEnabled(regimeRelation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcRegimeRelation regimeRelation) {
        // TODO    确认删除前是否需要做检查
        acRegimeRelationDao.deleteByEntityKey(regimeRelation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcRegimeRelation regimeRelationNew) {
        AcRegimeRelation regimeRelationInDb = Optional.ofNullable(acRegimeRelationDao.getByKey(regimeRelationNew.getId())).orElseGet(AcRegimeRelation::new);
        ValidationUtil.isNull(regimeRelationInDb.getId(), "RegimeRelation", "id", regimeRelationNew.getId());
        regimeRelationNew.setId(regimeRelationInDb.getId());
        acRegimeRelationDao.updateAllColumnByKey(regimeRelationNew);
    }

    @Override
    public AcRegimeRelationDTO getByKey(Long id) {
        AcRegimeRelation regimeRelation = Optional.ofNullable(acRegimeRelationDao.getByKey(id)).orElseGet(AcRegimeRelation::new);
        ValidationUtil.isNull(regimeRelation.getId(), "RegimeRelation", "id", id);
        return acRegimeRelationMapper.toDto(regimeRelation);
    }

    @Override
    public List<AcRegimeRelationDTO> listAll(AcRegimeRelationQueryCriteria criteria) {
        return acRegimeRelationMapper.toDto(acRegimeRelationDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcRegimeRelationQueryCriteria criteria, Pageable pageable) {
        Page<AcRegimeRelation> page = PageUtil.startPage(pageable);
        List<AcRegimeRelation> regimeRelations = acRegimeRelationDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acRegimeRelationMapper.toDto(regimeRelations), page.getTotal());
    }

    @Override
    public void download(List<AcRegimeRelationDTO> regimeRelationDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcRegimeRelationDTO regimeRelationDTO : regimeRelationDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", regimeRelationDTO.getId());
            map.put("制度id", regimeRelationDTO.getRegimeaId());
            map.put("排班 id", regimeRelationDTO.getRegimeTimeId());
            map.put("弹性域1", regimeRelationDTO.getAttribute1());
            map.put("弹性域2", regimeRelationDTO.getAttribute2());
            map.put("弹性域3", regimeRelationDTO.getAttribute3());
            map.put("有效标记", regimeRelationDTO.getEnabledFlag());
            map.put("创建人id", regimeRelationDTO.getCreateBy());
            map.put("创建时间", regimeRelationDTO.getCreateTime());
            map.put("修改人id", regimeRelationDTO.getUpdateBy());
            map.put("修改时间", regimeRelationDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByRegimeIdAndRegimeTimeId(AcRegimeRelation regimeRelation) {
        acRegimeRelationDao.deleteByRegimeIdAndRegimeTimeId(regimeRelation);
    }
}
