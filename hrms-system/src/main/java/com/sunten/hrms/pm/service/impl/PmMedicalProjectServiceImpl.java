package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmMedicalRelevanceDao;
import com.sunten.hrms.pm.domain.PmMedicalProject;
import com.sunten.hrms.pm.dao.PmMedicalProjectDao;
import com.sunten.hrms.pm.service.PmMedicalProjectService;
import com.sunten.hrms.pm.dto.PmMedicalProjectDTO;
import com.sunten.hrms.pm.dto.PmMedicalProjectQueryCriteria;
import com.sunten.hrms.pm.mapper.PmMedicalProjectMapper;
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
 * 体检项目表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-04-19
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmMedicalProjectServiceImpl extends ServiceImpl<PmMedicalProjectDao, PmMedicalProject> implements PmMedicalProjectService {
    private final PmMedicalProjectDao pmMedicalProjectDao;
    private final PmMedicalRelevanceDao pmMedicalRelevanceDao;
    private final PmMedicalProjectMapper pmMedicalProjectMapper;

    public PmMedicalProjectServiceImpl(PmMedicalProjectDao pmMedicalProjectDao, PmMedicalRelevanceDao pmMedicalRelevanceDao, PmMedicalProjectMapper pmMedicalProjectMapper) {
        this.pmMedicalProjectDao = pmMedicalProjectDao;
        this.pmMedicalRelevanceDao = pmMedicalRelevanceDao;
        this.pmMedicalProjectMapper = pmMedicalProjectMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmMedicalProjectDTO insert(PmMedicalProject medicalProjectNew) {
        PmMedicalProjectQueryCriteria criteria = new PmMedicalProjectQueryCriteria();
        criteria.setProjectName(medicalProjectNew.getProjectName());
        List<PmMedicalProject> oldProject = pmMedicalProjectDao.listAllByCriteria(criteria);
        if (oldProject.size() > 0) {
            throw new InfoCheckWarningMessException("该名称的体检项目已存在！");
        } else {
            pmMedicalProjectDao.insertAllColumn(medicalProjectNew);
        }
        return pmMedicalProjectMapper.toDto(medicalProjectNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmMedicalProject medicalProject = new PmMedicalProject();
        medicalProject.setId(id);
        this.delete(medicalProject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmMedicalProject medicalProject) {
        // 删除体检项目时，同时删除岗位与体检项目关联信息
        pmMedicalRelevanceDao.deleteRelevanceByProjectId(medicalProject.getId());
        pmMedicalProjectDao.deleteByEntityKey(medicalProject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmMedicalProject medicalProjectNew) {
        PmMedicalProject medicalProjectInDb = Optional.ofNullable(pmMedicalProjectDao.getByKey(medicalProjectNew.getId())).orElseGet(PmMedicalProject::new);
        ValidationUtil.isNull(medicalProjectInDb.getId() ,"MedicalProject", "id", medicalProjectNew.getId());
        medicalProjectNew.setId(medicalProjectInDb.getId());
        PmMedicalProjectQueryCriteria criteria = new PmMedicalProjectQueryCriteria();
        criteria.setProjectName(medicalProjectNew.getProjectName());
        List<PmMedicalProject> oldProject = pmMedicalProjectDao.listAllByCriteria(criteria);
        if (oldProject.size() > 0) {
            throw new InfoCheckWarningMessException("该名称的体检项目已存在！");
        } else {
            pmMedicalProjectDao.updateAllColumnByKey(medicalProjectNew);
        }
    }

    @Override
    public PmMedicalProjectDTO getByKey(Long id) {
        PmMedicalProject medicalProject = Optional.ofNullable(pmMedicalProjectDao.getByKey(id)).orElseGet(PmMedicalProject::new);
        ValidationUtil.isNull(medicalProject.getId() ,"MedicalProject", "id", id);
        return pmMedicalProjectMapper.toDto(medicalProject);
    }

    @Override
    public List<PmMedicalProjectDTO> listAll(PmMedicalProjectQueryCriteria criteria) {
        return pmMedicalProjectMapper.toDto(pmMedicalProjectDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmMedicalProjectQueryCriteria criteria, Pageable pageable) {
        Page<PmMedicalProject> page = PageUtil.startPage(pageable);
        List<PmMedicalProject> medicalProjects = pmMedicalProjectDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmMedicalProjectMapper.toDto(medicalProjects), page.getTotal());
    }

    @Override
    public void download(List<PmMedicalProjectDTO> medicalProjectDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmMedicalProjectDTO medicalProjectDTO : medicalProjectDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("体检项目名称", medicalProjectDTO.getProjectName());
            map.put("id", medicalProjectDTO.getId());
            map.put("创建时间", medicalProjectDTO.getCreateTime());
            map.put("创建人", medicalProjectDTO.getCreateBy());
            map.put("最后修改时间", medicalProjectDTO.getUpdateTime());
            map.put("最后修改人", medicalProjectDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
