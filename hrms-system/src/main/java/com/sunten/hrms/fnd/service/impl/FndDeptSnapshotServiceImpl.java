package com.sunten.hrms.fnd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.service.impl.AcDeptAttendanceServiceImpl;
import com.sunten.hrms.fnd.dao.FndDeptDao;
import com.sunten.hrms.fnd.dao.FndDeptSnapshotDao;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.domain.FndDeptSnapshot;
import com.sunten.hrms.fnd.dto.FndDeptQueryCriteria;
import com.sunten.hrms.fnd.dto.FndDeptSnapshotDTO;
import com.sunten.hrms.fnd.dto.FndDeptSnapshotQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndDeptSnapshotMapper;
import com.sunten.hrms.fnd.service.FndDeptSnapshotService;
import com.sunten.hrms.fnd.vo.DeptEmp;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.utils.DateUtil;
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
 * 组织架构快照 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndDeptSnapshotServiceImpl extends ServiceImpl<FndDeptSnapshotDao, FndDeptSnapshot> implements FndDeptSnapshotService {
    private final FndDeptSnapshotDao fndDeptSnapshotDao;
    private final FndDeptSnapshotMapper fndDeptSnapshotMapper;
    private final AcDeptAttendanceServiceImpl acDeptAttendanceServiceImpl;
    private final FndDeptDao fndDeptDao;

    public FndDeptSnapshotServiceImpl(FndDeptSnapshotDao fndDeptSnapshotDao, FndDeptSnapshotMapper fndDeptSnapshotMapper,
                                      AcDeptAttendanceServiceImpl acDeptAttendanceServiceImpl, FndDeptDao fndDeptDao) {
        this.fndDeptSnapshotDao = fndDeptSnapshotDao;
        this.fndDeptSnapshotMapper = fndDeptSnapshotMapper;
        this.acDeptAttendanceServiceImpl = acDeptAttendanceServiceImpl;
        this.fndDeptDao = fndDeptDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FndDeptSnapshotDTO insert(FndDeptSnapshot deptSnapshotNew) {
        fndDeptSnapshotDao.insertAllColumn(deptSnapshotNew);
        return fndDeptSnapshotMapper.toDto(deptSnapshotNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        FndDeptSnapshot deptSnapshot = new FndDeptSnapshot();
        deptSnapshot.setId(id);
        this.delete(deptSnapshot);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(FndDeptSnapshot deptSnapshot) {
        // TODO    确认删除前是否需要做检查
        fndDeptSnapshotDao.deleteByEntityKey(deptSnapshot);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FndDeptSnapshot deptSnapshotNew) {
        FndDeptSnapshot deptSnapshotInDb = Optional.ofNullable(fndDeptSnapshotDao.getByKey(deptSnapshotNew.getId())).orElseGet(FndDeptSnapshot::new);
        ValidationUtil.isNull(deptSnapshotInDb.getId(), "DeptSnapshot", "id", deptSnapshotNew.getId());
        deptSnapshotNew.setId(deptSnapshotInDb.getId());
        fndDeptSnapshotDao.updateAllColumnByKey(deptSnapshotNew);
    }

    @Override
    public FndDeptSnapshotDTO getByKey(Long id) {
        FndDeptSnapshot deptSnapshot = Optional.ofNullable(fndDeptSnapshotDao.getByKey(id)).orElseGet(FndDeptSnapshot::new);
        ValidationUtil.isNull(deptSnapshot.getId(), "DeptSnapshot", "id", id);
        return fndDeptSnapshotMapper.toDto(deptSnapshot);
    }

    @Override
    public List<FndDeptSnapshotDTO> listAll(FndDeptSnapshotQueryCriteria criteria) {
        return fndDeptSnapshotMapper.toDto(fndDeptSnapshotDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(FndDeptSnapshotQueryCriteria criteria, Pageable pageable) {
        Page<FndDeptSnapshot> page = PageUtil.startPage(pageable);
        List<FndDeptSnapshot> deptSnapshots = fndDeptSnapshotDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndDeptSnapshotMapper.toDto(deptSnapshots), page.getTotal());
    }

    @Override
    public void download(List<FndDeptSnapshotDTO> deptSnapshotDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndDeptSnapshotDTO deptSnapshotDTO : deptSnapshotDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", deptSnapshotDTO.getId());
            map.put("日期", deptSnapshotDTO.getDate());
            map.put("节点id", deptSnapshotDTO.getDeptId());
            map.put("节点名称", deptSnapshotDTO.getDeptName());
            map.put("父级节点id", deptSnapshotDTO.getParentId());
            map.put("节点代码", deptSnapshotDTO.getDeptCode());
            map.put("所属层级", deptSnapshotDTO.getDeptLevel());
            map.put("节点序号", deptSnapshotDTO.getDeptSequence());
            map.put("弹性域1", deptSnapshotDTO.getAttribute1());
            map.put("弹性域2", deptSnapshotDTO.getAttribute2());
            map.put("弹性域3", deptSnapshotDTO.getAttribute3());
            map.put("弹性域4", deptSnapshotDTO.getAttribute4());
            map.put("弹性域5", deptSnapshotDTO.getAttribute5());
            map.put("创建人id", deptSnapshotDTO.getCreateBy());
            map.put("创建时间", deptSnapshotDTO.getCreateTime());
            map.put("修改人id", deptSnapshotDTO.getUpdateBy());
            map.put("修改时间", deptSnapshotDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateSnapShot() {
        Integer count = fndDeptSnapshotDao.countByDate(LocalDate.now());
        if (count == 0) {
            FndDeptSnapshot fndDeptSnapshot = new FndDeptSnapshot();
            fndDeptSnapshot.setDate(LocalDate.now());
            fndDeptSnapshot.setCreateBy(-1L);
            fndDeptSnapshot.setUpdateBy(-1L);
            fndDeptSnapshot.setCreateTime(LocalDateTime.now());
            fndDeptSnapshot.setUpdateTime(LocalDateTime.now());
            fndDeptSnapshotDao.insertSnapShot(fndDeptSnapshot);
            List<FndDeptSnapshot> fndDeptSnapshots = fndDeptSnapshotDao.listAllWithAttendanceByCriteria(LocalDate.now());
            for (FndDeptSnapshot fds : fndDeptSnapshots) {
                if (fds.getAttendance() == null) {
                    fds.setAttendance(acDeptAttendanceServiceImpl.getParentAttendance(fds, fndDeptSnapshots));
                }
            }
            for (FndDeptSnapshot fds : fndDeptSnapshots) {
                fds.setUpdateTime(LocalDateTime.now());
                fds.setUpdateBy(-1L);
                fds.setAttendanceId(fds.getAttendance().getId());
                fndDeptSnapshotDao.updateAttendanceId(fds);
            }
        }
    }

}
