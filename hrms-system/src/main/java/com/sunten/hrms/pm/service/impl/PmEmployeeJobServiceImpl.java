package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.fnd.dao.FndUserDao;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.mapper.FndUserMapper;
import com.sunten.hrms.pm.dao.PmEmployeeJobDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeeJob;
import com.sunten.hrms.pm.dto.PmEmployeeJobDTO;
import com.sunten.hrms.pm.dto.PmEmployeeJobQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeJobMapper;
import com.sunten.hrms.pm.service.PmEmployeeJobService;
import com.sunten.hrms.pm.vo.PmJobEmployeeVo;
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
import java.util.stream.Collectors;

/**
 * <p>
 * 部门科室岗位关系表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeJobServiceImpl extends ServiceImpl<PmEmployeeJobDao, PmEmployeeJob> implements PmEmployeeJobService {
    private final String[] scopeType = {"全部", "本级", "本级及以下", "本人", "自定义"};
    private final PmEmployeeJobDao pmEmployeeJobDao;
    private final PmEmployeeJobMapper pmEmployeeJobMapper;
    private final FndUserDao fndUserDao;
    private final FndUserMapper fndUserMapper;

    public PmEmployeeJobServiceImpl(PmEmployeeJobDao pmEmployeeJobDao, PmEmployeeJobMapper pmEmployeeJobMapper, FndUserDao fndUserDao,
                                    FndUserMapper fndUserMapper) {
        this.pmEmployeeJobDao = pmEmployeeJobDao;
        this.pmEmployeeJobMapper = pmEmployeeJobMapper;
        this.fndUserDao = fndUserDao;
        this.fndUserMapper = fndUserMapper;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeJobDTO insert(PmEmployeeJob employeeJobNew) {
        if (employeeJobNew.getJobMainFlag() != null && employeeJobNew.getJobMainFlag()) {
            //验证是否存在主岗位
            PmEmployeeJob pmEmployeeJob = pmEmployeeJobDao.getMainJObByKey(employeeJobNew.getEmployee().getId());
            if (pmEmployeeJob != null) {
                pmEmployeeJob.setJobMainFlag(false);
                pmEmployeeJobDao.updateMainFlag(pmEmployeeJob);
            }
        }
        pmEmployeeJobDao.insertAllColumn(employeeJobNew);
        return pmEmployeeJobMapper.toDto(employeeJobNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeJob employeeJob = new PmEmployeeJob();
        employeeJob.setId(id);
        employeeJob.setEnabledFlag(false);
        this.delete(employeeJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeJob employeeJob) {
        // 确认删除前是否需要做检查,只失效，不删除
        //pmEmployeeJobDao.deleteByEntityKey(employeeJob);
        pmEmployeeJobDao.updateEnableFlagByKey(employeeJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeJob employeeJobNew) {
        PmEmployeeJob employeeJobInDb = Optional.ofNullable(pmEmployeeJobDao.getByKey(employeeJobNew.getId())).orElseGet(PmEmployeeJob::new);
        ValidationUtil.isNull(employeeJobInDb.getId(), "EmployeeJob", "id", employeeJobNew.getId());
        employeeJobNew.setId(employeeJobInDb.getId());
        pmEmployeeJobDao.updateAllColumnByKey(employeeJobNew);
    }

    @Override
    public PmEmployeeJobDTO getByKey(Long id) {
        PmEmployeeJob employeeJob = Optional.ofNullable(pmEmployeeJobDao.getByKey(id)).orElseGet(PmEmployeeJob::new);
        ValidationUtil.isNull(employeeJob.getId(), "EmployeeJob", "id", id);
        return pmEmployeeJobMapper.toDto(employeeJob);
    }

    @Override
    public List<PmEmployeeJobDTO> listAll(PmEmployeeJobQueryCriteria criteria) {
        return pmEmployeeJobMapper.toDto(pmEmployeeJobDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeJobQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeJob> page = PageUtil.startPage(pageable);
        List<PmEmployeeJob> employeeJobs = pmEmployeeJobDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeJobMapper.toDto(employeeJobs), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeJobDTO> employeeJobDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeJobDTO employeeJobDTO : employeeJobDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("group任职ID", employeeJobDTO.getGroupId());
            map.put("员工ID", employeeJobDTO.getEmployee().getId());
            map.put("岗位ID", employeeJobDTO.getJob().getId());
            map.put("岗位名称", employeeJobDTO.getJob().getJobName());
            map.put("部门科室ID", employeeJobDTO.getDept().getId());
            map.put("部门科室名称", employeeJobDTO.getDept().getDeptName());
            map.put("备注", employeeJobDTO.getRemarks());
            map.put("弹性域1", employeeJobDTO.getAttribute1());
            map.put("弹性域2", employeeJobDTO.getAttribute2());
            map.put("弹性域3", employeeJobDTO.getAttribute3());
            map.put("弹性域4", employeeJobDTO.getAttribute4());
            map.put("弹性域5", employeeJobDTO.getAttribute5());
            map.put("有效标记默认值", employeeJobDTO.getEnabledFlag());
            map.put("是否主岗位(0否，1是)", employeeJobDTO.getJobMainFlag());
            map.put("id", employeeJobDTO.getId());
            map.put("创建时间", employeeJobDTO.getCreateTime());
            map.put("创建人ID", employeeJobDTO.getCreateBy());
            map.put("修改时间", employeeJobDTO.getUpdateTime());
            map.put("修改人ID", employeeJobDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void downloadByJob(List<PmJobEmployeeVo> pmJobEmployeeVos, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmJobEmployeeVo pmJobEmployeeVo : pmJobEmployeeVos) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("人员名称", pmJobEmployeeVo.getName());
            map.put("工卡", pmJobEmployeeVo.getWorkCard());
            map.put("性别", pmJobEmployeeVo.getGender());
            map.put("是否兼任", pmJobEmployeeVo.getPartTimeFlag() ? "是" : "否");
            map.put("是否离职", pmJobEmployeeVo.getLeaveFlag() ? "离职" : "在职");
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Boolean checkManager(Long userId) {
        FndUserDTO fndUserDTO = fndUserMapper.toDto(fndUserDao.getByKey(userId));
        return fndUserDao.checkManagerByUserId(fndUserDTO.getId());
//        if (fndUserDTO.getEmployee() != null) {
//            // 查询用户岗位
//            List<PmEmployeeJobDTO> jobs = this.listByEmpIdAndEnabledFlagWithExtend(fndUserDTO.getEmployee().getId(), true);
//            for (PmEmployeeJobDTO job : jobs) {
//                if (!scopeType[3].equals(job.getJob().getDataScope())) {
//                    return true;
//                }
//            }
//        }
//        return false;
    }

    @Override
    public PmEmployeeJob getManagerOrSupervisor(Long userId) {
        return pmEmployeeJobDao.getManagerOrSupervisor(userId);
    }

    @Override
    public Long getMaxGroupId() {
        Long maxGroupId = pmEmployeeJobDao.getMaxGroupId();
        if (maxGroupId == null) {
            maxGroupId = 1L;
        } else {
            maxGroupId += 1;
        }
        return maxGroupId;
    }

    @Override
    public List<PmEmployeeJobDTO> listByEmpIdAndEnabledFlagWithExtend(Long employeeId, boolean enabledFlag) {
        return pmEmployeeJobMapper.toDto(pmEmployeeJobDao.listByEmpIdAndEnabledFlagWithExtend(employeeId, enabledFlag));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setMianJob(Long id) {
        PmEmployeeJob newMainJob = pmEmployeeJobDao.getByKey(id);

        //先根据人员标记所有岗位为非主岗，然后标记新岗位为负岗
        pmEmployeeJobDao.updateAllMainFlag(newMainJob);

        newMainJob.setJobMainFlag(true);
        pmEmployeeJobDao.updateMainFlag(newMainJob);
    }

    @Override
    public List<PmEmployeeJobDTO> listAllEntityDeptIds(PmEmployeeJobQueryCriteria criteria) {
        return pmEmployeeJobMapper.toDto(pmEmployeeJobDao.listAllEntityDeptIds(criteria));
    }

    @Override
    public List<PmJobEmployeeVo> listJobEmployee(Long jobId, boolean enabledFlag) {
        List<PmEmployeeJob> employeeJobs = pmEmployeeJobDao.listJobEmployee(jobId, enabledFlag);
        List<PmJobEmployeeVo> employeeVos = new ArrayList<>();
        for (PmEmployeeJob employeeJob : employeeJobs) {
            PmEmployee employee = employeeJob.getEmployee();
            PmJobEmployeeVo vo = new PmJobEmployeeVo(employee.getName(), employee.getWorkCard(), !employeeJob.getJobMainFlag(), employee.getLeaveFlag(), employee.getGender());
            employeeVos.add(vo);
        }
        employeeVos = employeeVos.stream().distinct().collect(Collectors.toList());
        return employeeVos;
    }


//    @Override
//    public List<PmJobEmployeeVo> listJobEmployeeByName(String jobName) {
//        List<PmEmployeeJob> employeeJobs = pmEmployeeJobDao.listJobEmployeeByName(jobName);
//        List<PmJobEmployeeVo> employeeVos = new ArrayList<>();
//        for (PmEmployeeJob employeeJob : employeeJobs) {
//            PmEmployee employee = employeeJob.getEmployee();
//            PmJobEmployeeVo vo = new PmJobEmployeeVo(employee.getName(), employee.getWorkCard(), !employeeJob.getJobMainFlag(), employee.getLeaveFlag(), employee.getGender());
//            employeeVos.add(vo);
//        }
//        employeeVos = employeeVos.stream().distinct().collect(Collectors.toList());
//        return employeeVos;
//    }
}
