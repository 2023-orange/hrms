package com.sunten.hrms.ac.service.impl;

import com.sunten.hrms.ac.dao.AcLeaveApplicationDao;
//import com.sunten.hrms.ac.domain.AcOvertimeBackUp;
import com.sunten.hrms.ac.dao.AcOvertimeApplicationDao;
import com.sunten.hrms.ac.domain.AcOvertimeMangement;
import com.sunten.hrms.ac.dao.AcOvertimeMangementDao;
//import com.sunten.hrms.ac.dto.AcOvertimeBackUpDTO;
//import com.sunten.hrms.ac.mapper.AcOvertimeBackUpMapper;
import com.sunten.hrms.ac.service.AcOvertimeMangementService;
import com.sunten.hrms.ac.dto.AcOvertimeMangementDTO;
import com.sunten.hrms.ac.dto.AcOvertimeManagementQueryCriteria;
import com.sunten.hrms.ac.mapper.AcOvertimeMangementMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
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
 *  服务实现类
 * </p>
 *
 * @author zouyp
 * @since 2023-10-16
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcOvertimeMangementServiceImpl extends ServiceImpl<AcOvertimeMangementDao, AcOvertimeMangement> implements AcOvertimeMangementService {
    private final AcOvertimeMangementDao acOvertimeMangementDao;
    private final AcOvertimeMangementMapper acOvertimeMangementMapper;
//    @Resource
//    private AcOvertimeBackUpMapper acOvertimeBackUpMapper;
    @Autowired
    private AcOvertimeApplicationDao acOvertimeApplicationDao;

    @Autowired
    private AcLeaveApplicationDao acLeaveApplicationDao;
    public AcOvertimeMangementServiceImpl(AcOvertimeMangementDao acOvertimeMangementDao, AcOvertimeMangementMapper acOvertimeMangementMapper) {
        this.acOvertimeMangementDao = acOvertimeMangementDao;
        this.acOvertimeMangementMapper = acOvertimeMangementMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcOvertimeMangementDTO insert(AcOvertimeMangement overtimeMangementNew) {
        acOvertimeMangementDao.insertAllColumn(overtimeMangementNew);
        return acOvertimeMangementMapper.toDto(overtimeMangementNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AcOvertimeMangement overtimeMangement = new AcOvertimeMangement();
        overtimeMangement.setId(id);
        this.delete(overtimeMangement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcOvertimeMangement overtimeMangement) {
        // TODO    确认删除前是否需要做检查
        acOvertimeMangementDao.deleteByEntityKey(overtimeMangement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcOvertimeMangement overtimeMangementNew) {
        AcOvertimeMangement overtimeMangementInDb = Optional.ofNullable(acOvertimeMangementDao.getByKey(overtimeMangementNew.getId())).orElseGet(AcOvertimeMangement::new);
        ValidationUtil.isNull(overtimeMangementInDb.getId() ,"OvertimeMangement", "id", overtimeMangementNew.getId());
        overtimeMangementNew.setId(overtimeMangementInDb.getId());
        acOvertimeMangementDao.updateAllColumnByKey(overtimeMangementNew);
    }

    @Override
    public AcOvertimeMangementDTO getByKey(Long id) {
        AcOvertimeMangement overtimeMangement = Optional.ofNullable(acOvertimeMangementDao.getByKey(id)).orElseGet(AcOvertimeMangement::new);
        ValidationUtil.isNull(overtimeMangement.getId() ,"OvertimeMangement", "id", id);
        return acOvertimeMangementMapper.toDto(overtimeMangement);
    }

    @Override
    public List<AcOvertimeMangementDTO> listAll(AcOvertimeManagementQueryCriteria criteria) {
        return acOvertimeMangementMapper.toDto(acOvertimeMangementDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcOvertimeManagementQueryCriteria criteria, Pageable pageable) {
        Page<AcOvertimeMangement> page = PageUtil.startPage(pageable);
        List<AcOvertimeMangement> overtimeMangements = acOvertimeMangementDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acOvertimeMangementMapper.toDto(overtimeMangements), page.getTotal());
    }

    @Override
    public void download(List<AcOvertimeMangementDTO> overtimeMangementDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcOvertimeMangementDTO overtimeMangementDTO : overtimeMangementDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("主键ID", overtimeMangementDTO.getId());
            map.put("部门id", overtimeMangementDTO.getDeptId());
            map.put("总人数", overtimeMangementDTO.getTotalNumber());
            map.put("部门人均加班工时", overtimeMangementDTO.getAverageOvertimeHour());
            map.put("系统限制时数", overtimeMangementDTO.getSystemLimitHour());
            map.put("attribute1", overtimeMangementDTO.getAttribute1());
            map.put("attribute2", overtimeMangementDTO.getAttribute2());
            map.put("attribute3", overtimeMangementDTO.getAttribute3());
            map.put("attribute4", overtimeMangementDTO.getAttribute4());
            map.put("状态（0失效，1生效）", overtimeMangementDTO.getEnabledFlag());
            map.put("创建人", overtimeMangementDTO.getCreateBy());
            map.put("创建时间", overtimeMangementDTO.getCreateTime());
            map.put("更新者", overtimeMangementDTO.getUpdateBy());
            map.put("更新时间", overtimeMangementDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<HashMap<String, Object>> getOvertimeManagementList() {
        // 获取列表前每次实时更新加班时数管理页面的每个科室的总人数
        acOvertimeMangementDao.updateTotalNumber();
        List<HashMap<String, Object>> overtimeManagementList = acOvertimeMangementDao.getOvertimeManagementList();
        System.out.println("overtimeManagementList: ");
        System.out.println(overtimeManagementList);
        overtimeManagementList.forEach(map -> {
            Integer dept_id = (Integer) map.get("dept_id");
            Integer childNumber = acOvertimeApplicationDao.getChildNumber(dept_id);
            Integer userNumber = (Integer) map.getOrDefault("total_number", 0);
            acOvertimeMangementDao.updateTotalNumberById((Integer)map.get("id"),(userNumber + childNumber));
            map.put("total_number", userNumber + childNumber);
        });
        System.out.println("overtimeManagementList2: ");
        System.out.println(overtimeManagementList);
        return overtimeManagementList;
//        return acOvertimeMangementDao.getOvertimeManagementList();
    }

    @Override
    public List<HashMap<String, Object>> getOvertimeManagementDetail(Integer id, Integer deptId) {
        List<HashMap<String, Object>> overtimeManagementList = acOvertimeMangementDao.getOvertimeManagementDetail(id, deptId);
        return overtimeManagementList;
    }

    @Override
    public void changeAcApprovalDepartmentStatus(Integer id, Integer deptId, int enabledFlag) {
        acOvertimeMangementDao.changeAcApprovalDepartmentStatus(id,deptId,enabledFlag);
    }

    @Override
    public void updateOvertimeManagementDetail(Integer id, Integer deptId, Integer totalNumber, Float averageOvertimeHour, Float systemLimitHour, String updateBy) {
        acOvertimeMangementDao.updateOvertimeManagementDetail(id,deptId,totalNumber,averageOvertimeHour, systemLimitHour, updateBy);
    }

    @Override
    public Integer addOvertimeManagementDetail(Integer dept_id, String deptName, Integer totalNumber, Float averageOvertimeHour, Float systemLimitHour, String createBy) {
        // 查询部门dept_id
        Integer res = null;
        try {
//            Integer deptId = acLeaveApplicationDao.getNewDeptId(deptName);
            res = acOvertimeMangementDao.addOvertimeManagementDetail(dept_id, totalNumber, averageOvertimeHour, systemLimitHour, createBy);
        } catch (Exception e) {
            return 999;
        }
        return res;
    }

    @Override
    public List<HashMap<String, Object>> checkList(Integer deptId) {
//        System.out.println("deptId=" + deptId);
        List<HashMap<String, Object>> overtimeManagementList = acOvertimeMangementDao.checkList3(deptId);
        return overtimeManagementList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void backupOvertimeManagementList() {
        // 获取列表前每次实时更新加班时数管理页面的每个科室的总人数
        // 先注释这些 因为认识老是获取不正确
//        acOvertimeMangementDao.updateTotalNumber();
//        List<HashMap<String, Object>> overtimeManagementList = acOvertimeMangementDao.getOvertimeManagementBackupList();
//        System.out.println("overtimeManagementList: ");
//        System.out.println(overtimeManagementList);
//        overtimeManagementList.forEach(map -> {
//            Integer dept_id = (Integer) map.get("dept_id");
//            Integer childNumber = acOvertimeApplicationDao.getChildNumber(dept_id);
//            Integer userNumber = (Integer) map.getOrDefault("total_number", 0);
//            acOvertimeMangementDao.updateTotalNumberByIdToBackup((Integer)map.get("id"),(userNumber + childNumber));
//            map.put("total_number", userNumber + childNumber);
//        });
        // 每月1号凌晨备份一次当前加班管理
        acOvertimeMangementDao.backupOvertimeManagementList();
    }

    @Override
    public HashMap<String, Object> checkIsRole(String workCard) {
        return acOvertimeMangementDao.checkIsRole(workCard);
    }

    @Override
    public List<HashMap<String, Object>> getAndCheckList(String workCard) {
        acOvertimeMangementDao.updateTotalNumber();
        List<HashMap<String, Object>> overtimeManagementList = null;
        // 先判断是不是主管经理类
        HashMap<String, Object> teamRes = acOvertimeMangementDao.checkUserRole(workCard);
        if (teamRes != null && "主管".equals(teamRes.get("job_name"))) {
            // 判断是主管
            overtimeManagementList = acOvertimeMangementDao.getDepartmentOvertimeList((teamRes.get("dept_id")));
        } else if (teamRes != null && "经理".equals(teamRes.get("job_name"))) {
            // 判断是经理
            overtimeManagementList = acOvertimeMangementDao.getMangerOvertimeList((teamRes.get("dept_id")));
        }
        return overtimeManagementList;
    }

    @Override
    public List<HashMap<String, Object>> getBackOvertimeManagementListByDeptName(Integer deptId) {
        List<HashMap<String, Object>> backupOvertimeManagementList = null;
        if (deptId == null) {
            backupOvertimeManagementList = acOvertimeMangementDao.getBackupOvertimeManagementListAll();
        } else {
             backupOvertimeManagementList = acOvertimeMangementDao.getBackupOvertimeManagementList(deptId);
        }
        return backupOvertimeManagementList;
    }

    @Override
    public List<HashMap<String, Object>> acOvertimeManagementServiceByDate(Integer deptId, String beginDate, String endDate) {
        List<HashMap<String, Object>> backupOvertimeManagementListByDate = null;
        if (deptId != null) {
            backupOvertimeManagementListByDate = acOvertimeMangementDao.getBackupOvertimeManagementListByDate(deptId,  beginDate, endDate);
        } else {
            backupOvertimeManagementListByDate = acOvertimeMangementDao.getBackupOvertimeManagementListByDateAll(beginDate, endDate);
        }
        return backupOvertimeManagementListByDate;
    }

    @Override
    public List<HashMap<String, Object>> getBackUpAndCheckListByDeptName(Integer deptId, String workCard) {
        List<HashMap<String, Object>> overtimeManagementList = null;
        // 先判断是不是主管经理类
        HashMap<String, Object> teamRes = acOvertimeMangementDao.checkUserRole(workCard);
        if (teamRes != null && "主管".equals(teamRes.get("job_name"))) {
            // 判断是主管
            overtimeManagementList = acOvertimeMangementDao.getBackUpDepartmentOvertimeList((teamRes.get("dept_id")), deptId);
        } else if (teamRes != null && "经理".equals(teamRes.get("job_name"))) {
            // 判断是经理
            overtimeManagementList = acOvertimeMangementDao.getBackUpMangerOvertimeList((teamRes.get("dept_id")), deptId);
        }
        return overtimeManagementList;
    }

    @Override
    public List<HashMap<String, Object>> getBackUpAndCheckListByDate(Integer deptId, String beginDate, String endDate, String workCard) {
        List<HashMap<String, Object>> overtimeManagementList = null;
        // 先判断是不是主管经理类
        HashMap<String, Object> teamRes = acOvertimeMangementDao.checkUserRole(workCard);
        if (teamRes != null && "主管".equals(teamRes.get("job_name"))) {
            // 判断是主管
            overtimeManagementList = acOvertimeMangementDao.getDepartmentOvertimeListByDate((teamRes.get("dept_id")), deptId,beginDate,endDate);
        } else if (teamRes != null && "经理".equals(teamRes.get("job_name"))) {
            // 判断是经理
            overtimeManagementList = acOvertimeMangementDao.getMangerOvertimeListByDate((teamRes.get("dept_id")), deptId,beginDate,endDate);
        }
        return overtimeManagementList;
    }
//    @Override
//    public Map<String, Object> getBackOvertimeManagementListNew(AcOvertimeManagementQueryCriteria criteria, Pageable pageable) {
//        Page<AcOvertimeBackUpDTO> page = PageUtil.startPage(pageable);
//        List<AcOvertimeBackUp> backupList = acOvertimeMangementDao.getBackOvertimeManagementListNew(page, criteria);
//        System.out.println("backupList: " + backupList);
//        return PageUtil.toPage(acOvertimeBackUpMapper.toDto(backupList), page.getTotal());
//    }
}
