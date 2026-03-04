package com.sunten.hrms.fnd.service.impl;

import com.sunten.hrms.fnd.domain.FndJobDept;
import com.sunten.hrms.fnd.dao.FndJobDeptDao;
import com.sunten.hrms.fnd.service.FndJobDeptService;
import com.sunten.hrms.fnd.dto.FndJobDeptDTO;
import com.sunten.hrms.fnd.dto.FndJobDeptQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndJobDeptMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
 * @author batan
 * @since 2020-12-03
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndJobDeptServiceImpl extends ServiceImpl<FndJobDeptDao, FndJobDept> implements FndJobDeptService {
    private final FndJobDeptDao fndJobDeptDao;
    private final FndJobDeptMapper fndJobDeptMapper;

    public FndJobDeptServiceImpl(FndJobDeptDao fndJobDeptDao, FndJobDeptMapper fndJobDeptMapper) {
        this.fndJobDeptDao = fndJobDeptDao;
        this.fndJobDeptMapper = fndJobDeptMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FndJobDeptDTO insert(FndJobDept jobDeptNew) {
        fndJobDeptDao.insertAllColumn(jobDeptNew);
        return fndJobDeptMapper.toDto(jobDeptNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long deptId, Long jobId) {
        FndJobDept jobDept = new FndJobDept();
        jobDept.setDeptId(deptId);
        jobDept.setJobId(jobId);
        this.delete(jobDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(FndJobDept jobDept) {
        // TODO    确认删除前是否需要做检查
        fndJobDeptDao.deleteByEntityKey(jobDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FndJobDept jobDeptNew) {
        FndJobDept jobDeptInDb = Optional.ofNullable(fndJobDeptDao.getByKey(jobDeptNew.getDeptId(), jobDeptNew.getJobId())).orElseGet(FndJobDept::new);
        ValidationUtil.isNull(jobDeptInDb.getDeptId() ,"JobDept", "deptId", jobDeptNew.getDeptId());
        jobDeptNew.setDeptId(jobDeptInDb.getDeptId());
        ValidationUtil.isNull(jobDeptInDb.getJobId() ,"JobDept", "jobId", jobDeptNew.getJobId());
        jobDeptNew.setJobId(jobDeptInDb.getJobId());
        fndJobDeptDao.updateAllColumnByKey(jobDeptNew);
    }

    @Override
    public FndJobDeptDTO getByKey(Long deptId, Long jobId) {
        FndJobDept jobDept = Optional.ofNullable(fndJobDeptDao.getByKey(deptId, jobId)).orElseGet(FndJobDept::new);
        ValidationUtil.isNull(jobDept.getDeptId() ,"JobDept", "deptId", deptId);
        ValidationUtil.isNull(jobDept.getJobId() ,"JobDept", "jobId", jobId);
        return fndJobDeptMapper.toDto(jobDept);
    }

    @Override
    public List<FndJobDeptDTO> listAll(FndJobDeptQueryCriteria criteria) {
        return fndJobDeptMapper.toDto(fndJobDeptDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(FndJobDeptQueryCriteria criteria, Pageable pageable) {
        Page<FndJobDept> page = PageUtil.startPage(pageable);
        List<FndJobDept> jobDepts = fndJobDeptDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndJobDeptMapper.toDto(jobDepts), page.getTotal());
    }

    @Override
    public void download(List<FndJobDeptDTO> jobDeptDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndJobDeptDTO jobDeptDTO : jobDeptDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("deptId", jobDeptDTO.getDeptId());
            map.put("jobId", jobDeptDTO.getJobId());
            map.put("updateTime", jobDeptDTO.getUpdateTime());
            map.put("createTime", jobDeptDTO.getCreateTime());
            map.put("updateBy", jobDeptDTO.getUpdateBy());
            map.put("createBy", jobDeptDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
