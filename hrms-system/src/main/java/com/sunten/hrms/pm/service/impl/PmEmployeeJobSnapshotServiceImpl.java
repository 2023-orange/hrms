package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.pm.dao.PmEmployeeJobDao;
import com.sunten.hrms.pm.dao.PmEmployeeJobSnapshotDao;
import com.sunten.hrms.pm.domain.PmEmployeeJobSnapshot;
import com.sunten.hrms.pm.dto.PmEmployeeJobSnapshotDTO;
import com.sunten.hrms.pm.dto.PmEmployeeJobSnapshotQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeJobSnapshotMapper;
import com.sunten.hrms.pm.service.PmEmployeeJobSnapshotService;
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
 * 部门科室岗位关系快照 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeJobSnapshotServiceImpl extends ServiceImpl<PmEmployeeJobSnapshotDao, PmEmployeeJobSnapshot> implements PmEmployeeJobSnapshotService {
    private final PmEmployeeJobSnapshotDao pmEmployeeJobSnapshotDao;
    private final PmEmployeeJobSnapshotMapper pmEmployeeJobSnapshotMapper;
    private final PmEmployeeJobDao pmEmployeeJobDao;

    public PmEmployeeJobSnapshotServiceImpl(PmEmployeeJobSnapshotDao pmEmployeeJobSnapshotDao, PmEmployeeJobSnapshotMapper pmEmployeeJobSnapshotMapper,
                                            PmEmployeeJobDao pmEmployeeJobDao) {
        this.pmEmployeeJobSnapshotDao = pmEmployeeJobSnapshotDao;
        this.pmEmployeeJobSnapshotMapper = pmEmployeeJobSnapshotMapper;
        this.pmEmployeeJobDao = pmEmployeeJobDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeJobSnapshotDTO insert(PmEmployeeJobSnapshot employeeJobSnapshotNew) {
        pmEmployeeJobSnapshotDao.insertAllColumn(employeeJobSnapshotNew);
        return pmEmployeeJobSnapshotMapper.toDto(employeeJobSnapshotNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeJobSnapshot employeeJobSnapshot = new PmEmployeeJobSnapshot();
        employeeJobSnapshot.setId(id);
        this.delete(employeeJobSnapshot);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeJobSnapshot employeeJobSnapshot) {
        // TODO    确认删除前是否需要做检查
        pmEmployeeJobSnapshotDao.deleteByEntityKey(employeeJobSnapshot);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeJobSnapshot employeeJobSnapshotNew) {
        PmEmployeeJobSnapshot employeeJobSnapshotInDb = Optional.ofNullable(pmEmployeeJobSnapshotDao.getByKey(employeeJobSnapshotNew.getId())).orElseGet(PmEmployeeJobSnapshot::new);
        ValidationUtil.isNull(employeeJobSnapshotInDb.getId(), "EmployeeJobSnapshot", "id", employeeJobSnapshotNew.getId());
        employeeJobSnapshotNew.setId(employeeJobSnapshotInDb.getId());
        pmEmployeeJobSnapshotDao.updateAllColumnByKey(employeeJobSnapshotNew);
    }

    @Override
    public PmEmployeeJobSnapshotDTO getByKey(Long id) {
        PmEmployeeJobSnapshot employeeJobSnapshot = Optional.ofNullable(pmEmployeeJobSnapshotDao.getByKey(id)).orElseGet(PmEmployeeJobSnapshot::new);
        ValidationUtil.isNull(employeeJobSnapshot.getId(), "EmployeeJobSnapshot", "id", id);
        return pmEmployeeJobSnapshotMapper.toDto(employeeJobSnapshot);
    }

    @Override
    public List<PmEmployeeJobSnapshotDTO> listAll(PmEmployeeJobSnapshotQueryCriteria criteria) {
        return pmEmployeeJobSnapshotMapper.toDto(pmEmployeeJobSnapshotDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeJobSnapshotQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeJobSnapshot> page = PageUtil.startPage(pageable);
        List<PmEmployeeJobSnapshot> employeeJobSnapshots = pmEmployeeJobSnapshotDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeJobSnapshotMapper.toDto(employeeJobSnapshots), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeJobSnapshotDTO> employeeJobSnapshotDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeJobSnapshotDTO employeeJobSnapshotDTO : employeeJobSnapshotDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("日期", employeeJobSnapshotDTO.getDate());
            map.put("员工ID", employeeJobSnapshotDTO.getEmployeeId());
            map.put("岗位ID", employeeJobSnapshotDTO.getJobId());
            map.put("部门科室ID", employeeJobSnapshotDTO.getDeptId());
            map.put("弹性域1", employeeJobSnapshotDTO.getAttribute1());
            map.put("弹性域2", employeeJobSnapshotDTO.getAttribute2());
            map.put("弹性域3", employeeJobSnapshotDTO.getAttribute3());
            map.put("弹性域4", employeeJobSnapshotDTO.getAttribute4());
            map.put("弹性域5", employeeJobSnapshotDTO.getAttribute5());
            map.put("id", employeeJobSnapshotDTO.getId());
            map.put("创建时间", employeeJobSnapshotDTO.getCreateTime());
            map.put("创建人ID", employeeJobSnapshotDTO.getCreateBy());
            map.put("修改时间", employeeJobSnapshotDTO.getUpdateTime());
            map.put("修改人ID", employeeJobSnapshotDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateSnapShot() {
        Integer count = pmEmployeeJobSnapshotDao.countByDate();
        if (count == 0) {
            PmEmployeeJobSnapshot pmEmployeeJobSnapshot = new PmEmployeeJobSnapshot();
            pmEmployeeJobSnapshot.setDate(LocalDate.now());
            pmEmployeeJobSnapshot.setUpdateTime(LocalDateTime.now());
            pmEmployeeJobSnapshot.setCreateTime(LocalDateTime.now());
            pmEmployeeJobSnapshot.setCreateBy(-1L);
            pmEmployeeJobSnapshot.setUpdateBy(-1L);
            pmEmployeeJobSnapshotDao.insertSnapShot(pmEmployeeJobSnapshot);
        }
    }
}
