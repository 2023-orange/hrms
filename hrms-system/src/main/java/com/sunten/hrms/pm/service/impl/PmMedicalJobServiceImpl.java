package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmMedicalRelevanceDao;
import com.sunten.hrms.pm.domain.PmMedicalJob;
import com.sunten.hrms.pm.dao.PmMedicalJobDao;
import com.sunten.hrms.pm.domain.PmMedicalRelevance;
import com.sunten.hrms.pm.dto.PmMedicalRelevanceQueryCriteria;
import com.sunten.hrms.pm.service.PmMedicalJobService;
import com.sunten.hrms.pm.dto.PmMedicalJobDTO;
import com.sunten.hrms.pm.dto.PmMedicalJobQueryCriteria;
import com.sunten.hrms.pm.mapper.PmMedicalJobMapper;
import com.sunten.hrms.pm.service.PmMedicalRelevanceService;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
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
 * 岗位体检表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-04-19
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmMedicalJobServiceImpl extends ServiceImpl<PmMedicalJobDao, PmMedicalJob> implements PmMedicalJobService {
    private final PmMedicalJobDao pmMedicalJobDao;
    private final PmMedicalRelevanceDao pmMedicalRelevanceDao;
    private final PmMedicalRelevanceService pmMedicalRelevanceService;
    private final PmMedicalJobMapper pmMedicalJobMapper;

    public PmMedicalJobServiceImpl(PmMedicalJobDao pmMedicalJobDao, PmMedicalRelevanceDao pmMedicalRelevanceDao, PmMedicalRelevanceService pmMedicalRelevanceService, PmMedicalJobMapper pmMedicalJobMapper) {
        this.pmMedicalJobDao = pmMedicalJobDao;
        this.pmMedicalRelevanceDao = pmMedicalRelevanceDao;
        this.pmMedicalRelevanceService = pmMedicalRelevanceService;
        this.pmMedicalJobMapper = pmMedicalJobMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmMedicalJobDTO insert(PmMedicalJob medicalJobNew) {
        PmMedicalJobQueryCriteria criteria = new PmMedicalJobQueryCriteria();
        criteria.setJobId(medicalJobNew.getJobId());
        List<PmMedicalJob> pmMedicalJobs = pmMedicalJobDao.listAllByCriteria(criteria);
        if (pmMedicalJobs.size() > 0) {
            throw new InfoCheckWarningMessException("该岗位体检关系已存在！");
        }
        pmMedicalJobDao.insertAllColumn(medicalJobNew);
        List<PmMedicalRelevance> pmMedicalRelevances = new ArrayList<>();
        if(medicalJobNew.getGoMedicalRelevanceList() != null && medicalJobNew.getGoMedicalRelevanceList().size() >0) {
            pmMedicalRelevances.addAll(medicalJobNew.getGoMedicalRelevanceList());
        }
        if(medicalJobNew.getInMedicalRelevanceList() != null && medicalJobNew.getInMedicalRelevanceList().size() >0) {
            pmMedicalRelevances.addAll(medicalJobNew.getInMedicalRelevanceList());
        }
        if(medicalJobNew.getOutMedicalRelevanceList() != null && medicalJobNew.getOutMedicalRelevanceList().size() >0) {
            pmMedicalRelevances.addAll(medicalJobNew.getOutMedicalRelevanceList());
        }
        if(pmMedicalRelevances.size() >0) {
            // 批量插入岗位体检项关系表，设置id，创建人，创建日期等参数，批量插入无法自动给日期、创建人赋值
            for(PmMedicalRelevance relevance: pmMedicalRelevances) {
                relevance.setMedicalJobId(medicalJobNew.getId());
                relevance.setCreateBy(medicalJobNew.getCreateBy());
                relevance.setUpdateBy(medicalJobNew.getCreateBy());
                relevance.setCreateTime(LocalDateTime.now());
                relevance.setUpdateTime(LocalDateTime.now());
            }
            pmMedicalRelevanceDao.batchInsertAllColumn(pmMedicalRelevances);
        }

        return pmMedicalJobMapper.toDto(medicalJobNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmMedicalJob medicalJob = new PmMedicalJob();
        medicalJob.setId(id);
        this.delete(medicalJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmMedicalJob medicalJob) {
        // 删除时，将关联的体检项一起删除
        pmMedicalJobDao.deleteByEntityKey(medicalJob);
        pmMedicalRelevanceDao.deleteRelevanceByJobId(medicalJob.getJobId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmMedicalJob medicalJobNew) {
        PmMedicalJob medicalJobInDb = Optional.ofNullable(pmMedicalJobDao.getByKey(medicalJobNew.getId())).orElseGet(PmMedicalJob::new);
        ValidationUtil.isNull(medicalJobInDb.getId() ,"MedicalJob", "id", medicalJobNew.getId());
        medicalJobNew.setId(medicalJobInDb.getId());
        pmMedicalJobDao.updateAllColumnByKey(medicalJobNew);
        // 修改时，判断对应体检项有没有改动
        List<PmMedicalRelevance> allList = new ArrayList<>();
        allList.addAll(medicalJobNew.getGoMedicalRelevanceList());
        allList.addAll(medicalJobNew.getInMedicalRelevanceList());
        allList.addAll(medicalJobNew.getOutMedicalRelevanceList());
        pmMedicalRelevanceService.batchEdit(allList,medicalJobInDb.getId(),medicalJobInDb.getUpdateBy());
    }

    @Override
    public PmMedicalJobDTO getByKey(Long id) {
        PmMedicalJob medicalJob = Optional.ofNullable(pmMedicalJobDao.getByKey(id)).orElseGet(PmMedicalJob::new);
        ValidationUtil.isNull(medicalJob.getId() ,"MedicalJob", "id", id);
        return pmMedicalJobMapper.toDto(medicalJob);
    }

    @Override
    public List<PmMedicalJobDTO> listAll(PmMedicalJobQueryCriteria criteria) {
        return pmMedicalJobMapper.toDto(pmMedicalJobDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmMedicalJobQueryCriteria criteria, Pageable pageable) {
        Page<PmMedicalJob> page = PageUtil.startPage(pageable);
        List<PmMedicalJob> medicalJobs = pmMedicalJobDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmMedicalJobMapper.toDto(medicalJobs), page.getTotal());
    }

    @Override
    public void download(List<PmMedicalJobDTO> medicalJobDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmMedicalJobDTO medicalJobDTO : medicalJobDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("部门名称", medicalJobDTO.getDeptName());
            map.put("科室名称", medicalJobDTO.getOfficeName());
            map.put("班组名称", medicalJobDTO.getTeamName());
            map.put("对应部门科室班组id", medicalJobDTO.getDeptId());
            map.put("岗位名称", medicalJobDTO.getJobName());
            map.put("岗位id", medicalJobDTO.getJobId());
            map.put("岗位类别", medicalJobDTO.getJobClass());
            map.put("职业禁忌症", medicalJobDTO.getJobTuboo());
            map.put("id", medicalJobDTO.getId());
            map.put("创建时间", medicalJobDTO.getCreateTime());
            map.put("创建人", medicalJobDTO.getCreateBy());
            map.put("最后修改时间", medicalJobDTO.getUpdateTime());
            map.put("最后修改人", medicalJobDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public PmMedicalJobDTO getProjectListByKey(Long id) {
        PmMedicalJob medicalJob = Optional.ofNullable(pmMedicalJobDao.getByKey(id)).orElseGet(PmMedicalJob::new);
        ValidationUtil.isNull(medicalJob.getId() ,"MedicalJob", "id", id);
        PmMedicalRelevanceQueryCriteria criteria = new PmMedicalRelevanceQueryCriteria();
        // 上岗前体检项目
        criteria.setProjectType("上岗前体检项目");
        criteria.setMedicalJobId(medicalJob.getId());
        List<PmMedicalRelevance> goMedicalRelevanceList = pmMedicalRelevanceDao.listAllByCriteria(criteria);
        medicalJob.setGoMedicalRelevanceList(goMedicalRelevanceList);
        // 在岗期间体检项目
        criteria.setProjectType("在岗期间体检项目");
        List<PmMedicalRelevance> inMedicalRelevanceList = pmMedicalRelevanceDao.listAllByCriteria(criteria);
        medicalJob.setInMedicalRelevanceList(inMedicalRelevanceList);
        // 离岗体检项目
        criteria.setProjectType("离岗体检项目");
        List<PmMedicalRelevance> outMedicalRelevanceList = pmMedicalRelevanceDao.listAllByCriteria(criteria);
        medicalJob.setOutMedicalRelevanceList(outMedicalRelevanceList);

        return pmMedicalJobMapper.toDto(medicalJob);
    }

    @Override
    public PmMedicalJobDTO getByJobId(Long jobId) {
        PmMedicalJob medicalJob = pmMedicalJobDao.getByJobId(jobId);
        if (medicalJob != null) {
            PmMedicalRelevanceQueryCriteria criteria = new PmMedicalRelevanceQueryCriteria();
            // 上岗前体检项目
            criteria.setProjectType("上岗前体检项目");
            criteria.setMedicalJobId(medicalJob.getId());
            List<PmMedicalRelevance> goMedicalRelevanceList = pmMedicalRelevanceDao.listAllByCriteria(criteria);
            medicalJob.setGoMedicalRelevanceList(goMedicalRelevanceList);
            // 在岗期间体检项目
            criteria.setProjectType("在岗期间体检项目");
            List<PmMedicalRelevance> inMedicalRelevanceList = pmMedicalRelevanceDao.listAllByCriteria(criteria);
            medicalJob.setInMedicalRelevanceList(inMedicalRelevanceList);
            // 离岗体检项目
            criteria.setProjectType("离岗体检项目");
            List<PmMedicalRelevance> outMedicalRelevanceList = pmMedicalRelevanceDao.listAllByCriteria(criteria);
            medicalJob.setOutMedicalRelevanceList(outMedicalRelevanceList);
        }
        return pmMedicalJobMapper.toDto(medicalJob);
    }
}
