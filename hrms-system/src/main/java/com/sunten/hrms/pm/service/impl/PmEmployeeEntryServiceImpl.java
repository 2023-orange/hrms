package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmEmployeeEntryDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeeEntry;
import com.sunten.hrms.pm.dto.PmEmployeeEntryDTO;
import com.sunten.hrms.pm.dto.PmEmployeeEntryQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeEntryMapper;
import com.sunten.hrms.pm.service.PmEmployeeEntryService;
import com.sunten.hrms.pm.service.PmEmployeeService;
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
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 入职情况表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeEntryServiceImpl extends ServiceImpl<PmEmployeeEntryDao, PmEmployeeEntry> implements PmEmployeeEntryService {
    private final PmEmployeeEntryDao pmEmployeeEntryDao;
    private final PmEmployeeEntryMapper pmEmployeeEntryMapper;
    private final PmEmployeeService pmEmployeeService;

    public PmEmployeeEntryServiceImpl(PmEmployeeEntryDao pmEmployeeEntryDao, PmEmployeeEntryMapper pmEmployeeEntryMapper, PmEmployeeService pmEmployeeService) {
        this.pmEmployeeEntryDao = pmEmployeeEntryDao;
        this.pmEmployeeEntryMapper = pmEmployeeEntryMapper;
        this.pmEmployeeService = pmEmployeeService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeEntryDTO insert(PmEmployeeEntry employeeEntryNew) {
        // TODO 是否插入员工信息
        /// employeeEntryNew.setFormalTime(employeeEntryNew.getEntryTime().plusMonths(employeeEntryNew.getProbation()));
        pmEmployeeEntryDao.insertAllColumn(employeeEntryNew);
        if (employeeEntryNew.getEmployee() != null && employeeEntryNew.getEmployee().getId().equals(-1L)) {
            pmEmployeeService.insert(employeeEntryNew.getEmployee());
        }

        return pmEmployeeEntryMapper.toDto(employeeEntryNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeEntry employeeEntry = new PmEmployeeEntry();
        employeeEntry.setId(id);
        employeeEntry.setEnabledFlag(false);
        this.delete(employeeEntry);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeEntry employeeEntry) {
        //     确认删除前是否需要做检查,只失效，不删除
        pmEmployeeEntryDao.updateEnableFlag(employeeEntry);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeEntry employeeEntryNew) {
        PmEmployeeEntry employeeEntryInDb = Optional.ofNullable(pmEmployeeEntryDao.getByKey(employeeEntryNew.getId())).orElseGet(PmEmployeeEntry::new);
        ValidationUtil.isNull(employeeEntryInDb.getId(), "EmployeeEntry", "id", employeeEntryNew.getId());
        employeeEntryNew.setId(employeeEntryInDb.getId());
        pmEmployeeEntryDao.updateAllColumnByKey(employeeEntryNew);
    }

    @Override
    public PmEmployeeEntryDTO getByKey(Long id) {
        PmEmployeeEntry employeeEntry = Optional.ofNullable(pmEmployeeEntryDao.getByKey(id)).orElseGet(PmEmployeeEntry::new);
        ValidationUtil.isNull(employeeEntry.getId(), "EmployeeEntry", "id", id);
        PmEmployeeEntryDTO pmEmployeeEntryDTO = pmEmployeeEntryMapper.toDto(employeeEntry);
//        setContractEmployeeInfo(pmEmployeeEntryDTO);
        return pmEmployeeEntryDTO;
    }

    @Override
    public List<PmEmployeeEntryDTO> listAll(PmEmployeeEntryQueryCriteria criteria) {
        List<PmEmployeeEntryDTO> entryDTOS = pmEmployeeEntryMapper.toDto(pmEmployeeEntryDao.listAllByCriteria(criteria));
//        for (PmEmployeeEntryDTO pee : entryDTOS) {
//            setContractEmployeeInfo(pee);
//        }
        return entryDTOS;
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeEntryQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeEntry> page = PageUtil.startPage(pageable);
        List<PmEmployeeEntry> employeeEntrys = pmEmployeeEntryDao.listAllByCriteriaPage(page, criteria);
        List<PmEmployeeEntryDTO> entryDTOS = pmEmployeeEntryMapper.toDto(employeeEntrys);
//        for (PmEmployeeEntryDTO pee : entryDTOS) {
//            setContractEmployeeInfo(pee);
//        }
        return PageUtil.toPage(entryDTOS, page.getTotal());
    }

    @Override
    public void download(PmEmployeeEntryQueryCriteria criteria, Pageable pageable, HttpServletResponse response) throws IOException {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Map<String, Object>> list = new ArrayList<>();
        Page<PmEmployeeEntry> page = PageUtil.startPage(pageable);
        if (criteria != null && criteria.getSelectTime() != null) {
            LocalDate nowDate = criteria.getSelectTime();
            LocalDate todayDate = LocalDate.now();
            if (nowDate.isEqual(todayDate)) {
                criteria.setSelectTimeType("today");
            } else {
                criteria.setSelectTimeType("dayAfter");
            }
        }
        List<PmEmployeeEntry> employeeEntrys = pmEmployeeEntryDao.listAllByProbationCriteriaPage(page, criteria);
        List<PmEmployeeEntryDTO> entryDTOS = pmEmployeeEntryMapper.toDto(employeeEntrys);
//        for (PmEmployeeEntryDTO pee : entryDTOS) {
//            setContractEmployeeInfo(pee);
//        }
        for (PmEmployeeEntryDTO employeeEntryDTO : entryDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("工牌", employeeEntryDTO.getEmployee() == null ? "" : employeeEntryDTO.getEmployee().getWorkCard());
            map.put("姓名", employeeEntryDTO.getEmployee() == null ? "" : employeeEntryDTO.getEmployee().getName());
            map.put("部门", employeeEntryDTO.getEmployee() == null ? "" : employeeEntryDTO.getEmployee().getDeptName());
            map.put("科室", employeeEntryDTO.getEmployee() == null ? "" : employeeEntryDTO.getEmployee().getDepartment());
            map.put("班组", employeeEntryDTO.getEmployee() == null ? "" : employeeEntryDTO.getEmployee().getTeam());
            map.put("岗位", employeeEntryDTO.getEmployee() == null ? "" : (employeeEntryDTO.getEmployee().getMainJob() == null ? "" : employeeEntryDTO.getEmployee().getMainJob().getJob().getJobName()));
            if (null == employeeEntryDTO.getEntryTime()) {
                map.put("入职时间", "");
            } else {
                map.put("入职时间", employeeEntryDTO.getEntryTime().format(fmt));
            }
            map.put("入职录用方式", employeeEntryDTO.getEntryMode());
            if (employeeEntryDTO.getEntryArchivesFlag()) {
                map.put("是否有人事档案", "是");
            } else {
                map.put("是否有人事档案", "否");
            }
            map.put("档案不详", employeeEntryDTO.getArchivesUnknown());
            if (employeeEntryDTO.getCrimeFlag()) {
                map.put("是否有犯罪记录", "是");
            } else {
                map.put("是否有犯罪记录", "否");
            }
            map.put("试用期（月）", employeeEntryDTO.getProbation());
            if (null == employeeEntryDTO.getFormalTime()) {
                map.put("转正时间", "");
            } else {
                map.put("转正时间", employeeEntryDTO.getFormalTime().format(fmt));//转正时间
            }
            map.put("介绍信工资", employeeEntryDTO.getIntroductionWages());
            map.put("介绍信情况", employeeEntryDTO.getIntroductionSituation());
            map.put("档案所在地", employeeEntryDTO.getArchivesAddress());
            map.put("过往工龄", employeeEntryDTO.getWorkedYears());
            map.put("备注", employeeEntryDTO.getRemarks());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

//    /**
//     * 获取员工的基本属性
//     */
//    public void setContractEmployeeInfo(PmEmployeeEntryDTO peeDTO) {
////        Long employeeId = peeDTO.getEmployee().getId();
////        PmEmployeeDTO pm = pmEmployeeService.getByKey(employeeId);
////        peeDTO.setEmployee(pm);
//    }

    @Override
    public List<PmEmployeeEntryDTO> batchInsert(List<PmEmployeeEntry> pmEmployeeEntries, Long employeeId) {
        for (PmEmployeeEntry pee : pmEmployeeEntries) {
            if (employeeId != null) {
                if (pee.getEmployee() == null) {
                    pee.setEmployee(new PmEmployee());
                }
                pee.getEmployee().setId(employeeId);
            }
            if (pee.getEmployee() == null || pee.getEmployee().getId() == null) {
                throw new InfoCheckWarningMessException("员工id不能为空");
            }
            if (pee.getId() != null) {
                if (pee.getId().equals(-1L)) {
                    pmEmployeeEntryDao.insertAllColumn(pee);
                } else {
                    pmEmployeeEntryDao.updateAllColumnByKey(pee);
                }
            } else {
                throw new InfoCheckWarningMessException("入职情况批量插入时ID不能为空");
            }
        }
        return pmEmployeeEntryMapper.toDto(pmEmployeeEntries);
    }

    @Override
    public Map<String, Object> listAllByProbationCriteriaPage(PmEmployeeEntryQueryCriteria criteria, Pageable pageable) {
        if (criteria != null && criteria.getSelectTime() != null) {
            LocalDate nowDate = criteria.getSelectTime();
            LocalDate todayDate = LocalDate.now();
            if (nowDate.isEqual(todayDate)) {
                criteria.setSelectTimeType("today");
            } else {
                criteria.setSelectTimeType("dayAfter");
            }
        }
        Page<PmEmployeeEntry> page = PageUtil.startPage(pageable);
        List<PmEmployeeEntry> employeeEntrys = pmEmployeeEntryDao.listAllByProbationCriteriaPage(page, criteria);
        List<PmEmployeeEntryDTO> entryDTOS = pmEmployeeEntryMapper.toDto(employeeEntrys);
//        for (PmEmployeeEntryDTO pee : entryDTOS) {
//            setContractEmployeeInfo(pee);
//        }
        return PageUtil.toPage(entryDTOS, page.getTotal());
    }

    @Override
    public void updateAssessFlag(PmEmployeeEntry employeeEntry) {
        pmEmployeeEntryDao.updateEnableFlag(employeeEntry);
    }
}
