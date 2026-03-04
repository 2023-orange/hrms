package com.sunten.hrms.ac.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcDeptAttendanceDao;
import com.sunten.hrms.ac.dao.AcRegimeDao;
import com.sunten.hrms.ac.dao.AcRegimeRelationDao;
import com.sunten.hrms.ac.domain.AcDeptAttendance;
import com.sunten.hrms.ac.domain.AcRegime;
import com.sunten.hrms.ac.domain.AcRegimeRelation;
import com.sunten.hrms.ac.domain.AcRegimeTime;
import com.sunten.hrms.ac.dto.AcDeptAttendanceQueryCriteria;
import com.sunten.hrms.ac.dto.AcRegimeDTO;
import com.sunten.hrms.ac.dto.AcRegimeQueryCriteria;
import com.sunten.hrms.ac.dto.AcRegimeRelationQueryCriteria;
import com.sunten.hrms.ac.mapper.AcRegimeMapper;
import com.sunten.hrms.ac.service.AcRegimeRelationService;
import com.sunten.hrms.ac.service.AcRegimeService;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.ListUtils;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 考勤制度表 服务实现类
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcRegimeServiceImpl extends ServiceImpl<AcRegimeDao, AcRegime> implements AcRegimeService {
    private final AcRegimeDao acRegimeDao;
    private final AcRegimeMapper acRegimeMapper;
    private final AcRegimeRelationDao acRegimeRelationDao;
    private final AcRegimeRelationService acRegimeRelationService;
    private final AcDeptAttendanceDao acDeptAttendanceDao;
    @Autowired
    private AcRegimeServiceImpl instance;

    public AcRegimeServiceImpl(AcRegimeDao acRegimeDao, AcRegimeMapper acRegimeMapper, AcRegimeRelationDao acRegimeRelationDao,
                               AcDeptAttendanceDao acDeptAttendanceDao, AcRegimeRelationService acRegimeRelationService
                               ) {
        this.acRegimeDao = acRegimeDao;
        this.acRegimeMapper = acRegimeMapper;
        this.acRegimeRelationDao = acRegimeRelationDao;
        this.acDeptAttendanceDao = acDeptAttendanceDao;
        this.acRegimeRelationService = acRegimeRelationService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcRegimeDTO insert(AcRegime regimeNew) {
        List<AcRegimeRelation> acRegimeRelations = regimeNew.getAcRegimeRelations();

        List<AcRegimeTime> acRegimeTimes =acRegimeRelations.stream().map(AcRegimeRelation::getAcRegimeTime).collect(Collectors.toList());
        if (!this.checkTimeCross(acRegimeTimes)) {
            throw new InfoCheckWarningMessException("时间段不允许重叠");
        }

        acRegimeDao.insertAllColumn(regimeNew);
        // 自动生成regimeCode
        regimeNew.setRegimeCode("code" + regimeNew.getId());
        AcRegimeDTO acRegimeDTO = acRegimeMapper.toDto(regimeNew);
        if (regimeNew.getAcRegimeRelations() != null && regimeNew.getAcRegimeRelations().size() > 0) {
            for (AcRegimeRelation acRegimeRelation : regimeNew.getAcRegimeRelations()
            ) {
                acRegimeRelation.setRegimeaId(acRegimeDTO.getId());
            }
            acRegimeRelationDao.insertCollection(regimeNew.getAcRegimeRelations());
        }
        return acRegimeMapper.toDto(regimeNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) { // 已弃用
        AcDeptAttendanceQueryCriteria acDeptAttendanceQueryCriteria = new AcDeptAttendanceQueryCriteria();
        acDeptAttendanceQueryCriteria.setRegimeId(id);
        acDeptAttendanceQueryCriteria.setEnabled(true);
        List<AcDeptAttendance> acDeptAttendances = acDeptAttendanceDao.listAllByCriteria(acDeptAttendanceQueryCriteria);
        List<AcDeptAttendance> checkList = acDeptAttendances.stream().filter(AcDeptAttendance -> null != AcDeptAttendance.getRegimeId()).collect(Collectors.toList());
        if (checkList.size() > 0) {
            throw new InfoCheckWarningMessException("该考勤制度正被" + checkList.get(0).getFndDept().getDeptName() + "使用且正在生效，不允许失效");
        }
        // 失效主表
        AcRegime regime = new AcRegime();
        regime.setId(id);
        acRegimeDao.deleteByEnabled(regime);
        // 失效副表
        AcRegimeQueryCriteria acRegimeQueryCriteria = new AcRegimeQueryCriteria();
        acRegimeQueryCriteria.setEnabled(true);
        acRegimeQueryCriteria.setArId(id);
        List<AcRegime> acRegimes = acRegimeDao.listAllByCriteria(acRegimeQueryCriteria);
        if (acRegimes.size() > 0) {
            for (int i = 0; i < acRegimes.get(0).getAcRegimeRelations().size(); i++) {
                acRegimeRelationDao.deleteByEnabled(acRegimes.get(0).getAcRegimeRelations().get(i));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcRegime regime) {
        // TODO    确认删除前是否需要做检查
        acRegimeDao.deleteByEntityKey(regime);
    }


    private void listCompForAcRegimeRelation(List<AcRegimeRelation> acRegimeRelationsOld, List<AcRegimeRelation> acRegimeRelationsNew) {
        List<AcRegimeRelation> intersection = new ArrayList<>();
        intersection.addAll(acRegimeRelationsNew);
        intersection.retainAll(acRegimeRelationsOld);
        //原集-交集=删除集
        acRegimeRelationsOld.removeAll(intersection);
        //新集-交集=新增集
        acRegimeRelationsNew.removeAll(intersection);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcRegime regimeNew) {
        AcRegime regimeInDb = Optional.ofNullable(acRegimeDao.getByKey(regimeNew.getId())).orElseGet(AcRegime::new);
        ValidationUtil.isNull(regimeInDb.getId(), "Regime", "id", regimeNew.getId());
        regimeNew.setId(regimeInDb.getId());

        // 判断是否有部门制度在使用， 有不允许修改
        AcDeptAttendanceQueryCriteria acDeptAttendanceQueryCriteria = new AcDeptAttendanceQueryCriteria();
        acDeptAttendanceQueryCriteria.setRegimeId(regimeNew.getId());
        acDeptAttendanceQueryCriteria.setEnabled(true);
        acDeptAttendanceQueryCriteria.setNowAndAfter(true);
        List<AcDeptAttendance> acDeptAttendances = acDeptAttendanceDao.listAllByCriteria(acDeptAttendanceQueryCriteria);
        List<AcDeptAttendance> checkList = acDeptAttendances.stream().filter(AcDeptAttendance -> null != AcDeptAttendance.getRegimeId()).collect(Collectors.toList());
        if (checkList.size() > 0) {
            if (!regimeNew.getEnabledFlag()) { // 失效
                throw new InfoCheckWarningMessException("该考勤制度正被" + checkList.get(0).getFndDept().getDeptName() + "使用且正在生效或将来会生效，不允许失效");
            } else {
                throw new InfoCheckWarningMessException("该考勤制度正被" + checkList.get(0).getFndDept().getDeptName() + "使用且正在生效或将来会生效，不允许修改");
            }
        }
        acRegimeDao.updateAllColumnByKey(regimeNew);
        AcRegimeQueryCriteria acRegimeQueryCriteria = new AcRegimeQueryCriteria();
        acRegimeQueryCriteria.setArId(regimeNew.getId());
        acRegimeQueryCriteria.setEnabled(true);
        List<AcRegime> acRegimes = acRegimeDao.listRelationByCriteria(acRegimeQueryCriteria);
        List<AcRegimeRelation> acRegimeRelationsOld = new ArrayList<>();
        if (acRegimes.size() > 0) {
            acRegimeRelationsOld = acRegimes.get(0).getAcRegimeRelations();
        }
        if (!this.checkTimeCross(regimeNew.getAcRegimeRelations()
                .stream().filter(AcRegimeRelation::getEnabledFlag).collect(Collectors.toList())
                .stream().map(AcRegimeRelation::getAcRegimeTime).collect(Collectors.toList())
        )) {
            throw new InfoCheckWarningMessException("时间段不允许重叠");
        }
        List<Long> acRegimeRelationsOldIds = acRegimeRelationsOld.stream().map(AcRegimeRelation::getRegimeTimeId).sorted().collect(Collectors.toList());
        List<Long> acRegimeRelationsNewIds = regimeNew.getAcRegimeRelations().stream().map(AcRegimeRelation::getRegimeTimeId).sorted().collect(Collectors.toList());
        ListUtils.listComp(acRegimeRelationsOldIds, acRegimeRelationsNewIds);
        AcRegimeRelation acRegimeRelation = new AcRegimeRelation();
        acRegimeRelation.setRegimeaId(regimeNew.getId());
        acRegimeRelationsOldIds.forEach((id) -> {
            acRegimeRelation.setRegimeTimeId(id);
            acRegimeRelation.setEnabledFlag(false);
            acRegimeRelationService.deleteByRegimeIdAndRegimeTimeId(acRegimeRelation);
        });

        acRegimeRelationsNewIds.forEach((id) -> {
            acRegimeRelation.setRegimeTimeId(id);
            acRegimeRelation.setEnabledFlag(true);
            acRegimeRelationService.insert(acRegimeRelation);
        });
    }

    @Override
    public AcRegimeDTO getByKey(Long id) {
        AcRegime regime = Optional.ofNullable(acRegimeDao.getByKey(id)).orElseGet(AcRegime::new);
        ValidationUtil.isNull(regime.getId(), "Regime", "id", id);
        return acRegimeMapper.toDto(regime);
    }

    @Override
    public List<AcRegimeDTO> listAll(AcRegimeQueryCriteria criteria) {
        return acRegimeMapper.toDto(acRegimeDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcRegimeQueryCriteria criteria, Pageable pageable) {
        Page<AcRegime> page = PageUtil.startPage(pageable);
        List<AcRegime> regimes = acRegimeDao.listAllByCriteriaPage(page, criteria);
        // 循环regimes获取里面的relation集合
        AcRegimeRelationQueryCriteria acRegimeRelationQueryCriteria = new AcRegimeRelationQueryCriteria();
        acRegimeRelationQueryCriteria.setEnabled(true);
        for (AcRegime acRegime : regimes
        ) {
            acRegimeRelationQueryCriteria.setRegimeId(acRegime.getId());
            acRegimeRelationQueryCriteria.setEnabled(true);
            List<AcRegimeRelation> regimeRelations = acRegimeRelationDao.listAllByCriteria(acRegimeRelationQueryCriteria);
            acRegime.setAcRegimeRelations(regimeRelations);
        }
        return PageUtil.toPage(acRegimeMapper.toDto(regimes), page.getTotal());
    }

    @Override
    public void download(List<AcRegimeDTO> regimeDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcRegimeDTO regimeDTO : regimeDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", regimeDTO.getId());
            map.put("考勤制度名称", regimeDTO.getRegimeName());
            map.put("考勤制度代号", regimeDTO.getRegimeCode());
            map.put("备注", regimeDTO.getRemake());
            map.put("弹性域1", regimeDTO.getAttribute1());
            map.put("弹性域2", regimeDTO.getAttribute2());
            map.put("弹性域3", regimeDTO.getAttribute3());
            map.put("有效标记", regimeDTO.getEnabledFlag());
            map.put("创建人id", regimeDTO.getCreateBy());
            map.put("创建时间", regimeDTO.getCreateTime());
            map.put("修改人id", regimeDTO.getUpdateBy());
            map.put("修改时间", regimeDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * @author：liangjw
     * @Date: 2020/10/12 14:23
     * @Description: 校验时间是否重叠
     * @params:
     */
    public Boolean checkTimeCross(List<AcRegimeTime> acRegimeTimes) {
        for (AcRegimeTime act : acRegimeTimes
        ) {
            for (AcRegimeTime tempArr : acRegimeTimes
            ) {
                if (act.getId().longValue() != tempArr.getId().longValue()) {
                    if (act.getTimeFrom().isAfter(tempArr.getTimeFrom())
                            && act.getTimeFrom().isBefore(tempArr.getTimeTo())
                    ) {
                        return false;
                    }
                    if (act.getTimeTo().isAfter(tempArr.getTimeFrom())
                            && act.getTimeTo().isBefore(tempArr.getTimeTo())
                            && !act.getExtendTimeFlag()
                    ) { //非跨日 且在区间内
                        return false;
                    }
                    if (act.getTimeFrom().equals(tempArr.getTimeFrom()) && act.getTimeTo().equals(tempArr.getTimeTo())) { // 时间段一致
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
