package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.pm.domain.PmMedicalLineRelevance;
import com.sunten.hrms.pm.dao.PmMedicalLineRelevanceDao;
import com.sunten.hrms.pm.service.PmMedicalLineRelevanceService;
import com.sunten.hrms.pm.dto.PmMedicalLineRelevanceDTO;
import com.sunten.hrms.pm.dto.PmMedicalLineRelevanceQueryCriteria;
import com.sunten.hrms.pm.mapper.PmMedicalLineRelevanceMapper;
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
 *  服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-04-20
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmMedicalLineRelevanceServiceImpl extends ServiceImpl<PmMedicalLineRelevanceDao, PmMedicalLineRelevance> implements PmMedicalLineRelevanceService {
    private final PmMedicalLineRelevanceDao pmMedicalLineRelevanceDao;
    private final PmMedicalLineRelevanceMapper pmMedicalLineRelevanceMapper;

    public PmMedicalLineRelevanceServiceImpl(PmMedicalLineRelevanceDao pmMedicalLineRelevanceDao, PmMedicalLineRelevanceMapper pmMedicalLineRelevanceMapper) {
        this.pmMedicalLineRelevanceDao = pmMedicalLineRelevanceDao;
        this.pmMedicalLineRelevanceMapper = pmMedicalLineRelevanceMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmMedicalLineRelevanceDTO insert(PmMedicalLineRelevance medicalLineRelevanceNew) {
        pmMedicalLineRelevanceDao.insertAllColumn(medicalLineRelevanceNew);
        return pmMedicalLineRelevanceMapper.toDto(medicalLineRelevanceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmMedicalLineRelevance medicalLineRelevance = new PmMedicalLineRelevance();
        medicalLineRelevance.setId(id);
        this.delete(medicalLineRelevance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmMedicalLineRelevance medicalLineRelevance) {
        // TODO    确认删除前是否需要做检查
        pmMedicalLineRelevanceDao.deleteByEntityKey(medicalLineRelevance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmMedicalLineRelevance medicalLineRelevanceNew) {
        PmMedicalLineRelevance medicalLineRelevanceInDb = Optional.ofNullable(pmMedicalLineRelevanceDao.getByKey(medicalLineRelevanceNew.getId())).orElseGet(PmMedicalLineRelevance::new);
        ValidationUtil.isNull(medicalLineRelevanceInDb.getId() ,"MedicalLineRelevance", "id", medicalLineRelevanceNew.getId());
        medicalLineRelevanceNew.setId(medicalLineRelevanceInDb.getId());
        pmMedicalLineRelevanceDao.updateAllColumnByKey(medicalLineRelevanceNew);
    }

    @Override
    public PmMedicalLineRelevanceDTO getByKey(Long id) {
        PmMedicalLineRelevance medicalLineRelevance = Optional.ofNullable(pmMedicalLineRelevanceDao.getByKey(id)).orElseGet(PmMedicalLineRelevance::new);
        ValidationUtil.isNull(medicalLineRelevance.getId() ,"MedicalLineRelevance", "id", id);
        return pmMedicalLineRelevanceMapper.toDto(medicalLineRelevance);
    }

    @Override
    public List<PmMedicalLineRelevanceDTO> listAll(PmMedicalLineRelevanceQueryCriteria criteria) {
        return pmMedicalLineRelevanceMapper.toDto(pmMedicalLineRelevanceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmMedicalLineRelevanceQueryCriteria criteria, Pageable pageable) {
        Page<PmMedicalLineRelevance> page = PageUtil.startPage(pageable);
        List<PmMedicalLineRelevance> medicalLineRelevances = pmMedicalLineRelevanceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmMedicalLineRelevanceMapper.toDto(medicalLineRelevances), page.getTotal());
    }

    @Override
    public void download(List<PmMedicalLineRelevanceDTO> medicalLineRelevanceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmMedicalLineRelevanceDTO medicalLineRelevanceDTO : medicalLineRelevanceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("体检申请子表id", medicalLineRelevanceDTO.getMedicalLineId());
            map.put("体检项目名称", medicalLineRelevanceDTO.getProjectName());
            map.put("id", medicalLineRelevanceDTO.getId());
            map.put("创建时间", medicalLineRelevanceDTO.getCreateTime());
            map.put("创建人", medicalLineRelevanceDTO.getCreateBy());
            map.put("最后修改时间", medicalLineRelevanceDTO.getUpdateTime());
            map.put("最后修改人", medicalLineRelevanceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<PmMedicalLineRelevance> relevances,Long createBy,Long medicalLineId) {
        if (relevances != null && relevances.size() >0) {
            if (createBy != null || medicalLineId != null) {
                for (PmMedicalLineRelevance lineRelevance: relevances) {
                    if (medicalLineId != null) {
                        lineRelevance.setMedicalLineId(medicalLineId);
                    }
                    if (createBy != null) {
                        lineRelevance.setCreateBy(createBy);
                        lineRelevance.setCreateTime(LocalDateTime.now());
                        lineRelevance.setUpdateBy(createBy);
                        lineRelevance.setUpdateTime(LocalDateTime.now());
                    }
                }
            }
            pmMedicalLineRelevanceDao.batchInsertAllColumn(relevances);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchEdit(List<PmMedicalLineRelevance> relevances,Long updateBy,Long medicalLineId) {
        List<PmMedicalLineRelevance> addList = new ArrayList<>();
        List<PmMedicalLineRelevance> delList = new ArrayList<>();
        LocalDateTime nowDate = LocalDateTime.now();
        PmMedicalLineRelevanceQueryCriteria criteria = new PmMedicalLineRelevanceQueryCriteria();
        criteria.setMedicalLineId(medicalLineId);
        List<PmMedicalLineRelevance> oldRelavances = pmMedicalLineRelevanceDao.listAllByCriteria(criteria);
        if (relevances == null || relevances.size() ==0) {
            if (oldRelavances.size() >0) delList.addAll(oldRelavances);
        } else {
            // 第一次循环取新增的行记录
            for(PmMedicalLineRelevance firstNow: relevances) {
                if (firstNow.getId() == null || firstNow.getId().equals(-1L)) {
                    firstNow.setMedicalLineId(medicalLineId);
                    firstNow.setCreateBy(updateBy);
                    firstNow.setUpdateBy(updateBy);
                    firstNow.setCreateTime(nowDate);
                    firstNow.setUpdateTime(nowDate);
                    addList.add(firstNow);
                }
            }
            // 第二次循环取被删除的行
            for(PmMedicalLineRelevance secondOld: oldRelavances) {
                boolean delFlag = true;
                for(PmMedicalLineRelevance secondNow: relevances) {
                    if(secondOld.getId().equals(secondNow.getId())) {
                        delFlag = false;
                    }
                }
                if (delFlag) {
                    secondOld.setUpdateBy(updateBy);
                    secondOld.setUpdateTime(nowDate);
                    delList.add(secondOld);
                }
            }
        }
        if (addList.size() > 0) {
            pmMedicalLineRelevanceDao.batchInsertAllColumn(addList);
        }
        if (delList.size() > 0) {
            pmMedicalLineRelevanceDao.batchDeleteByList(delList);
        }
    }
}
