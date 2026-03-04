package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcDeptAttendanceDao;
import com.sunten.hrms.ac.dao.AcRegimeDao;
import com.sunten.hrms.ac.domain.AcDeptAttendance;
import com.sunten.hrms.ac.domain.AcRegime;
import com.sunten.hrms.ac.dto.AcDeptAttendanceDTO;
import com.sunten.hrms.ac.dto.AcDeptAttendanceQueryCriteria;
import com.sunten.hrms.ac.dto.AcRegimeQueryCriteria;
import com.sunten.hrms.ac.mapper.AcDeptAttendanceMapper;
import com.sunten.hrms.ac.service.AcDeptAttendanceService;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndDeptSnapshot;
import com.sunten.hrms.fnd.dto.FndDeptDTO;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门考勤制度关系表 服务实现类
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcDeptAttendanceServiceImpl extends ServiceImpl<AcDeptAttendanceDao, AcDeptAttendance> implements AcDeptAttendanceService {
    private final AcDeptAttendanceDao acDeptAttendanceDao;
    private final AcDeptAttendanceMapper acDeptAttendanceMapper;
    private final AcRegimeDao acRegimeDao;

    public AcDeptAttendanceServiceImpl(AcDeptAttendanceDao acDeptAttendanceDao, AcDeptAttendanceMapper acDeptAttendanceMapper,
                                       AcRegimeDao acRegimeDao) {
        this.acDeptAttendanceDao = acDeptAttendanceDao;
        this.acDeptAttendanceMapper = acDeptAttendanceMapper;
        this.acRegimeDao = acRegimeDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcDeptAttendanceDTO insert(AcDeptAttendance deptAttendanceNew) {
        this.setRegimeDetailTime(deptAttendanceNew);
        acDeptAttendanceDao.insertAllColumn(deptAttendanceNew);
        return acDeptAttendanceMapper.toDto(deptAttendanceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AcDeptAttendance deptAttendance = new AcDeptAttendance();
        deptAttendance.setId(id);
        this.delete(deptAttendance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcDeptAttendance deptAttendance) {
        // TODO    确认删除前是否需要做检查
        acDeptAttendanceDao.deleteByEntityKey(deptAttendance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcDeptAttendance deptAttendanceNew) {
        AcDeptAttendance deptAttendanceInDb = Optional.ofNullable(acDeptAttendanceDao.getByKey(deptAttendanceNew.getId())).orElseGet(AcDeptAttendance::new);
        ValidationUtil.isNull(deptAttendanceInDb.getId(), "DeptAttendance", "id", deptAttendanceNew.getId());
        deptAttendanceNew.setId(deptAttendanceInDb.getId());
        // 可能传过来时数据不齐全，根据deptId查一次，查当前生效的
        AcDeptAttendanceQueryCriteria acDeptAttendanceQueryCriteria = new AcDeptAttendanceQueryCriteria();
        acDeptAttendanceQueryCriteria.setDeptId(deptAttendanceNew.getFndDept().getId());
        acDeptAttendanceQueryCriteria.setEnabled(true);
        acDeptAttendanceQueryCriteria.setHistory(false);
        List<AcDeptAttendance> acDeptAttendances = acDeptAttendanceDao.baseListByCriteria(acDeptAttendanceQueryCriteria);
        // 先判断时间区间是否符合连续性
        for (AcDeptAttendance ac : acDeptAttendances
             ) {
            if (null != ac.getInvalidDate()) {
                throw new InfoCheckWarningMessException("检测到曾经已通过变更，生成新的考勤规则。若要再次变更，请先删除上一次变更所生成的考勤规则");
            }
        }
        // 更新旧关系的失效时间
        AcDeptAttendance acDeptAttendanceOld = acDeptAttendances.get(0);
        acDeptAttendanceOld.setEnabledFlag(true);
        acDeptAttendanceOld.setInvalidDate(deptAttendanceNew.getTakeEffectDate().minusDays(1L));
        System.out.println("acDeptAttendanceOldacDeptAttendanceOld==============" + acDeptAttendanceOld);
        acDeptAttendanceDao.updateAllColumnByKey(acDeptAttendanceOld);
        deptAttendanceNew.setId(null);
        deptAttendanceNew.setEnabledFlag(true);
        deptAttendanceNew.setExtend3TimeFlag(null);
        deptAttendanceNew.setExtend2TimeFlag(null);
        deptAttendanceNew.setExtend1TimeFlag(null);
        deptAttendanceNew.setThirdTimeTo(null);
        deptAttendanceNew.setThirdTimeFrom(null);
        deptAttendanceNew.setSecondTimeFrom(null);
        deptAttendanceNew.setSecondTimeTo(null);
        deptAttendanceNew.setFirstTimeTo(null);
        deptAttendanceNew.setFirstTimeFrom(null);
        deptAttendanceNew.setDeptId(deptAttendanceNew.getFndDept().getId()); // id
        deptAttendanceNew.setRegimeId(deptAttendanceNew.getAcRegime().getId());
        deptAttendanceNew.setCalendarHeaderId(deptAttendanceNew.getAcCalendarHeader().getId());
        deptAttendanceNew.setInvalidDate(null);
        // 时间段
        this.setRegimeDetailTime(deptAttendanceNew);
        acDeptAttendanceDao.insertAllColumn(deptAttendanceNew);
    }

    @Override
    public AcDeptAttendanceDTO getByKey(Long id) {
        AcDeptAttendance deptAttendance = Optional.ofNullable(acDeptAttendanceDao.getByKey(id)).orElseGet(AcDeptAttendance::new);
        ValidationUtil.isNull(deptAttendance.getId(), "DeptAttendance", "id", id);
        return acDeptAttendanceMapper.toDto(deptAttendance);
    }

    @Override
    public List<AcDeptAttendanceDTO> listAll(AcDeptAttendanceQueryCriteria criteria) {
        // 设置criteria.history 为null 查全部，为false查当前
        return acDeptAttendanceMapper.toDto(acDeptAttendanceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcDeptAttendanceQueryCriteria criteria, Pageable pageable) {
        Page<AcDeptAttendance> page = PageUtil.startPage(pageable);
        List<AcDeptAttendance> deptAttendances = acDeptAttendanceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acDeptAttendanceMapper.toDto(deptAttendances), page.getTotal());
    }

    @Override
    public void download(List<AcDeptAttendanceDTO> deptAttendanceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcDeptAttendanceDTO deptAttendanceDTO : deptAttendanceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", deptAttendanceDTO.getId());
            map.put("部门id", deptAttendanceDTO.getDeptId());
            map.put("考勤制度ID", deptAttendanceDTO.getRegimeId());
            map.put("工作日历id", deptAttendanceDTO.getCalendarHeaderId());
            map.put("一班时间开始", deptAttendanceDTO.getFirstTimeFrom());
            map.put("一班时间结束", deptAttendanceDTO.getFirstTimeTo());
            map.put("一班是否跨日", deptAttendanceDTO.getExtend1TimeFlag());
            map.put("二班时间开始", deptAttendanceDTO.getSecondTimeFrom());
            map.put("二班时间结束", deptAttendanceDTO.getSecondTimeTo());
            map.put("二班是否跨日", deptAttendanceDTO.getExtend2TimeFlag());
            map.put("三班时间开始", deptAttendanceDTO.getThirdTimeFrom());
            map.put("三班时间结束", deptAttendanceDTO.getThirdTimeTo());
            map.put("三班是否跨日", deptAttendanceDTO.getExtend3TimeFlag());
            map.put("生效日期(填写加1日)", deptAttendanceDTO.getTakeEffectDate());
            map.put("失效日期(只看日期不看时分秒)", deptAttendanceDTO.getInvalidDate());
            map.put("弹性域1", deptAttendanceDTO.getAttribute1());
            map.put("弹性域2", deptAttendanceDTO.getAttribute2());
            map.put("弹性域3", deptAttendanceDTO.getAttribute3());
            map.put("有效标记", deptAttendanceDTO.getEnabledFlag());
            map.put("创建人id", deptAttendanceDTO.getCreateBy());
            map.put("创建时间", deptAttendanceDTO.getCreateTime());
            map.put("修改人id", deptAttendanceDTO.getUpdateBy());
            map.put("修改时间", deptAttendanceDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Object buildTree(List<AcDeptAttendanceDTO> acDeptAttendanceDTOS) {
        Set<AcDeptAttendanceDTO> trees = new LinkedHashSet<>();
        Set<AcDeptAttendanceDTO> depts = new LinkedHashSet<>();
        List<FndDeptDTO> fndDepts = acDeptAttendanceDTOS.stream().map(AcDeptAttendanceDTO::getFndDept).collect(Collectors.toList());
        List<String> deptNames = fndDepts.stream().map(FndDeptDTO::getDeptName).collect(Collectors.toList());
        boolean isChild;
        for (AcDeptAttendanceDTO acDeptAttendanceDTO : acDeptAttendanceDTOS
        ) {
            isChild = false;
            if ("0".equals(acDeptAttendanceDTO.getFndDept().getParentId().toString())) {
                trees.add(acDeptAttendanceDTO);
            }
            for (AcDeptAttendanceDTO it : acDeptAttendanceDTOS
            ) {
                if (it.getFndDept().getParentId().equals(acDeptAttendanceDTO.getFndDept().getId())) {
                    isChild = true;
                    if (acDeptAttendanceDTO.getChildren() == null) {
                        acDeptAttendanceDTO.setChildren(new ArrayList<>());
                    }
                    acDeptAttendanceDTO.getChildren().add(it);
                }
            }
            if (isChild) {
                depts.add(acDeptAttendanceDTO);
            } else if (!deptNames.contains(acDeptAttendanceDao.getByDeptId(acDeptAttendanceDTO.getFndDept().getParentId()).getFndDept().getDeptName())) {
                depts.add(acDeptAttendanceDTO);
            }
        }
        if (CollectionUtils.isEmpty(trees)) {
            trees = depts;
        }
        Integer totalElements = acDeptAttendanceDTOS.size();
        Map<String, Object> map = new HashMap<>();
        map.put("totalElements", totalElements);
        map.put("content", CollectionUtils.isEmpty(trees) ? acDeptAttendanceDTOS : trees);
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIdAndResetOld(AcDeptAttendance deptAttendance) {
        AcDeptAttendance deptAttendanceInDb = Optional.ofNullable(acDeptAttendanceDao.getByKey(deptAttendance.getId())).orElseGet(AcDeptAttendance::new);
        ValidationUtil.isNull(deptAttendanceInDb.getId(), "DeptAttendance", "id", deptAttendance.getId());
        deptAttendance.setId(deptAttendanceInDb.getId());
        // 当前生效的不允许删除
        AcDeptAttendanceQueryCriteria acDeptAttendanceQueryCriteria = new AcDeptAttendanceQueryCriteria();
        acDeptAttendanceQueryCriteria.setHistory(false); // 不填时查全部，填false查当前生效
        acDeptAttendanceQueryCriteria.setEnabled(true);
        acDeptAttendanceQueryCriteria.setDeptId(deptAttendance.getDeptId());
        List<AcDeptAttendance> acDeptAttendances = acDeptAttendanceDao.baseListByCriteria(acDeptAttendanceQueryCriteria);
        if (acDeptAttendances.size() > 0) {
            if (acDeptAttendances.get(0).getId().equals(deptAttendance.getId())) {
                throw new InfoCheckWarningMessException("不允许删除当前处于生效时间段内的制度关系");
            }
        }
        // 没抛出异常则表示删除其它未生效的,执行删除
        deptAttendance.setEnabledFlag(false);
        acDeptAttendanceDao.updateAllColumnByKey(deptAttendance);
        // 更新当前生效的规则的失效日期未null
        AcDeptAttendance acDeptAttendanceOld = acDeptAttendances.get(0);
        acDeptAttendanceOld.setInvalidDate(null);
        acDeptAttendanceDao.updateAllColumnByKey(acDeptAttendanceOld);
    }

    @Override
    public List<AcDeptAttendanceDTO> listForDeptHistory(AcDeptAttendanceQueryCriteria criteria) {
        return acDeptAttendanceMapper.toDto(acDeptAttendanceDao.listForDeptHistory(criteria));
    }

    private void setRegimeDetailTime(AcDeptAttendance acDeptAttendance) {
        AcRegimeQueryCriteria acRegimeQueryCriteria = new AcRegimeQueryCriteria();
        acRegimeQueryCriteria.setEnabled(true);
        acRegimeQueryCriteria.setArId(acDeptAttendance.getRegimeId());
        List<AcRegime> acRegimes = acRegimeDao.listRelationByCriteria(acRegimeQueryCriteria);
        if (acRegimes.size() > 0) {
            AcRegime acRegimeTarget = acRegimes.get(0);
            int timeCount = 0;
            if (acRegimeTarget.getAcRegimeRelations().size() > 0) {
                int size = acRegimeTarget.getAcRegimeRelations().size();
                if (timeCount < size) {
                    acDeptAttendance.setFirstTimeFrom(acRegimeTarget.getAcRegimeRelations().get(timeCount).getAcRegimeTime().getTimeFrom());
                    acDeptAttendance.setFirstTimeTo(acRegimeTarget.getAcRegimeRelations().get(timeCount).getAcRegimeTime().getTimeTo());
                    acDeptAttendance.setExtend1TimeFlag(acRegimeTarget.getAcRegimeRelations().get(timeCount).getAcRegimeTime().getExtendTimeFlag());
                }
                timeCount++;
                if (timeCount < size) {
                    acDeptAttendance.setSecondTimeFrom(acRegimeTarget.getAcRegimeRelations().get(timeCount).getAcRegimeTime().getTimeFrom());
                    acDeptAttendance.setSecondTimeTo(acRegimeTarget.getAcRegimeRelations().get(timeCount).getAcRegimeTime().getTimeTo());
                    acDeptAttendance.setExtend2TimeFlag(acRegimeTarget.getAcRegimeRelations().get(timeCount).getAcRegimeTime().getExtendTimeFlag());
                }
                timeCount++;
                if (timeCount < size) {
                    acDeptAttendance.setThirdTimeFrom(acRegimeTarget.getAcRegimeRelations().get(timeCount).getAcRegimeTime().getTimeFrom());
                    acDeptAttendance.setThirdTimeTo(acRegimeTarget.getAcRegimeRelations().get(timeCount).getAcRegimeTime().getTimeTo());
                    acDeptAttendance.setExtend3TimeFlag(acRegimeTarget.getAcRegimeRelations().get(timeCount).getAcRegimeTime().getExtendTimeFlag());
                }
            }
        }
    }

    public AcDeptAttendance getParentAttendance(FndDeptSnapshot current, List<FndDeptSnapshot> fndDeptSnapshots) {
        //找parent
        for (FndDeptSnapshot fndDeptSnapshot : fndDeptSnapshots) {

            if (current.getParentId().equals(fndDeptSnapshot.getDeptId())) {

                if (fndDeptSnapshot.getAttendance() != null) {
                    return fndDeptSnapshot.getAttendance();
                } else {
                    if (fndDeptSnapshot.getParentId() != null) {
                        fndDeptSnapshot.setAttendance(getParentAttendance(fndDeptSnapshot, fndDeptSnapshots));
                        return fndDeptSnapshot.getAttendance();
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
