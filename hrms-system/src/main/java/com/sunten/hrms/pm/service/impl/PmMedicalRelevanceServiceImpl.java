package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.pm.domain.PmMedicalRelevance;
import com.sunten.hrms.pm.dao.PmMedicalRelevanceDao;
import com.sunten.hrms.pm.service.PmMedicalRelevanceService;
import com.sunten.hrms.pm.dto.PmMedicalRelevanceDTO;
import com.sunten.hrms.pm.dto.PmMedicalRelevanceQueryCriteria;
import com.sunten.hrms.pm.mapper.PmMedicalRelevanceMapper;
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
 * 体检项目关联表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-04-19
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmMedicalRelevanceServiceImpl extends ServiceImpl<PmMedicalRelevanceDao, PmMedicalRelevance> implements PmMedicalRelevanceService {
    private final PmMedicalRelevanceDao pmMedicalRelevanceDao;
    private final PmMedicalRelevanceMapper pmMedicalRelevanceMapper;

    public PmMedicalRelevanceServiceImpl(PmMedicalRelevanceDao pmMedicalRelevanceDao, PmMedicalRelevanceMapper pmMedicalRelevanceMapper) {
        this.pmMedicalRelevanceDao = pmMedicalRelevanceDao;
        this.pmMedicalRelevanceMapper = pmMedicalRelevanceMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmMedicalRelevanceDTO insert(PmMedicalRelevance medicalRelevanceNew) {
        pmMedicalRelevanceDao.insertAllColumn(medicalRelevanceNew);
        return pmMedicalRelevanceMapper.toDto(medicalRelevanceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmMedicalRelevance medicalRelevance = new PmMedicalRelevance();
        medicalRelevance.setId(id);
        this.delete(medicalRelevance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmMedicalRelevance medicalRelevance) {
        // TODO    确认删除前是否需要做检查
        pmMedicalRelevanceDao.deleteByEntityKey(medicalRelevance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmMedicalRelevance medicalRelevanceNew) {
        PmMedicalRelevance medicalRelevanceInDb = Optional.ofNullable(pmMedicalRelevanceDao.getByKey(medicalRelevanceNew.getId())).orElseGet(PmMedicalRelevance::new);
        pmMedicalRelevanceDao.updateAllColumnByKey(medicalRelevanceNew);
    }

    @Override
    public PmMedicalRelevanceDTO getByKey(Long id) {
        PmMedicalRelevance medicalRelevance = Optional.ofNullable(pmMedicalRelevanceDao.getByKey(id)).orElseGet(PmMedicalRelevance::new);
        return pmMedicalRelevanceMapper.toDto(medicalRelevance);
    }

    @Override
    public List<PmMedicalRelevanceDTO> listAll(PmMedicalRelevanceQueryCriteria criteria) {
        return pmMedicalRelevanceMapper.toDto(pmMedicalRelevanceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmMedicalRelevanceQueryCriteria criteria, Pageable pageable) {
        Page<PmMedicalRelevance> page = PageUtil.startPage(pageable);
        List<PmMedicalRelevance> medicalRelevances = pmMedicalRelevanceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmMedicalRelevanceMapper.toDto(medicalRelevances), page.getTotal());
    }

    @Override
    public void download(List<PmMedicalRelevanceDTO> medicalRelevanceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmMedicalRelevanceDTO medicalRelevanceDTO : medicalRelevanceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("岗位体检表id", medicalRelevanceDTO.getMedicalJobId());
            map.put("体检项", medicalRelevanceDTO.getMedicalProject().getProjectName());
            map.put("体检项目类别（上岗体检项目、在岗体检项目、离岗体检项目）", medicalRelevanceDTO.getProjectType());
            map.put("id", medicalRelevanceDTO.getId());
            map.put("创建时间", medicalRelevanceDTO.getCreateTime());
            map.put("创建人", medicalRelevanceDTO.getCreateBy());
            map.put("最后修改时间", medicalRelevanceDTO.getUpdateTime());
            map.put("最后修改人", medicalRelevanceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void batchEdit(List<PmMedicalRelevance> pmMedicalRelevances, Long medicalJobId, Long updateBy) {
        List<PmMedicalRelevance> addList = new ArrayList<>();
        List<PmMedicalRelevance> delList = new ArrayList<>();
        PmMedicalRelevanceQueryCriteria criteria = new PmMedicalRelevanceQueryCriteria();
        criteria.setMedicalJobId(medicalJobId);
        List<PmMedicalRelevance> oldList = pmMedicalRelevanceDao.listAllByCriteria(criteria);
        if (pmMedicalRelevances == null || pmMedicalRelevances.size() == 0) {
            if(oldList.size() >0) delList.addAll(oldList);
        } else {
            // 循环获取新增的行,id为空或-l即为新增
            for (PmMedicalRelevance first: pmMedicalRelevances) {
                if (first.getId() == null || first.getId().equals(-1L)) {
                    first.setUpdateTime(LocalDateTime.now());
                    first.setCreateTime(LocalDateTime.now());
                    first.setCreateBy(updateBy);
                    first.setUpdateBy(updateBy);
                    addList.add(first);
                }
            }
            // 获取应删除的行，在新的集合中找不到旧集合的行数据id，即为删除
            for (PmMedicalRelevance oldSecond: oldList) {
                boolean delFlag = true;
                for (PmMedicalRelevance nowSecond: pmMedicalRelevances) {
                    if (oldSecond.getId().equals(nowSecond.getId())){
                        delFlag = false;
                    }
                }
                if (delFlag) {
                    delList.add(oldSecond);
                }
            }
        }
        if (addList.size()>0) {
            pmMedicalRelevanceDao.batchInsertAllColumn(addList);
        }
        if (delList.size() > 0) {
            pmMedicalRelevanceDao.bathcdeleteRelevances(delList);
        }

    }
}
