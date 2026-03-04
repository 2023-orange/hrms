package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmMedicalLineRelevanceDao;
import com.sunten.hrms.pm.domain.PmMedicalLine;
import com.sunten.hrms.pm.dao.PmMedicalLineDao;
import com.sunten.hrms.pm.dto.PmMedicalLineRelevanceQueryCriteria;
import com.sunten.hrms.pm.dto.PmMedicalRelevanceQueryCriteria;
import com.sunten.hrms.pm.service.PmMedicalLineService;
import com.sunten.hrms.pm.dto.PmMedicalLineDTO;
import com.sunten.hrms.pm.dto.PmMedicalLineQueryCriteria;
import com.sunten.hrms.pm.mapper.PmMedicalLineMapper;
import com.sunten.hrms.pm.vo.PmMedicalAutoVo;
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
 * 体检申请子表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-04-07
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmMedicalLineServiceImpl extends ServiceImpl<PmMedicalLineDao, PmMedicalLine> implements PmMedicalLineService {
    private final PmMedicalLineDao pmMedicalLineDao;
    private final PmMedicalLineMapper pmMedicalLineMapper;
    private final PmMedicalLineRelevanceDao pmMedicalLineRelevanceDao;

    public PmMedicalLineServiceImpl(PmMedicalLineDao pmMedicalLineDao, PmMedicalLineMapper pmMedicalLineMapper, PmMedicalLineRelevanceDao pmMedicalLineRelevanceDao) {
        this.pmMedicalLineDao = pmMedicalLineDao;
        this.pmMedicalLineMapper = pmMedicalLineMapper;
        this.pmMedicalLineRelevanceDao = pmMedicalLineRelevanceDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmMedicalLineDTO insert(PmMedicalLine medicalLineNew) {
        pmMedicalLineDao.insertAllColumn(medicalLineNew);
        return pmMedicalLineMapper.toDto(medicalLineNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmMedicalLine medicalLine = new PmMedicalLine();
        medicalLine.setId(id);
        this.delete(medicalLine);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmMedicalLine medicalLine) {
        // 删除时，将对应体检项子表也删除
        pmMedicalLineDao.deleteByEntityKey(medicalLine);
        pmMedicalLineRelevanceDao.deleteByMedicalLineId(medicalLine.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmMedicalLine medicalLineNew) {
        PmMedicalLine medicalLineInDb = Optional.ofNullable(pmMedicalLineDao.getByKey(medicalLineNew.getId())).orElseGet(PmMedicalLine::new);
        ValidationUtil.isNull(medicalLineInDb.getId() ,"MedicalLine", "id", medicalLineNew.getId());
        medicalLineNew.setId(medicalLineInDb.getId());
        pmMedicalLineDao.updateAllColumnByKey(medicalLineNew);
    }

    @Override
    public PmMedicalLineDTO getByKey(Long id) {
        PmMedicalLine medicalLine = Optional.ofNullable(pmMedicalLineDao.getByKey(id)).orElseGet(PmMedicalLine::new);
        ValidationUtil.isNull(medicalLine.getId() ,"MedicalLine", "id", id);
        PmMedicalLineRelevanceQueryCriteria relevanceQueryCriteria = new PmMedicalLineRelevanceQueryCriteria();
        relevanceQueryCriteria.setMedicalLineId(id);
        medicalLine.setMedicalLineRelevanceList(pmMedicalLineRelevanceDao.listAllByCriteria(relevanceQueryCriteria));
        return pmMedicalLineMapper.toDto(medicalLine);
    }

    @Override
    public List<PmMedicalLineDTO> listAll(PmMedicalLineQueryCriteria criteria) {
        return pmMedicalLineMapper.toDto(pmMedicalLineDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmMedicalLineQueryCriteria criteria, Pageable pageable) {
        Page<PmMedicalLine> page = PageUtil.startPage(pageable);
        List<PmMedicalLine> medicalLines = pmMedicalLineDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmMedicalLineMapper.toDto(medicalLines), page.getTotal());
    }

    @Override
    public void download(List<PmMedicalLineDTO> medicalLineDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmMedicalLineDTO medicalLineDTO : medicalLineDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("作业类别及危害因素", medicalLineDTO.getWorkAndHazard());
            map.put("岗位名称", medicalLineDTO.getJobName());
            map.put("岗位id", medicalLineDTO.getJobId());
            map.put("人员名称", medicalLineDTO.getEmployeeName());
            map.put("数量", medicalLineDTO.getQuantity());
            map.put("体检类别", medicalLineDTO.getMedicalClass());
            map.put("体检项目名称", medicalLineDTO.getMedicalName());
            map.put("体检结果标记", medicalLineDTO.getMedicalResult());
            map.put("体检备注", medicalLineDTO.getRemake());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<PmMedicalLineDTO> listAllByMedicalId(Long medicalId) {
        PmMedicalLineQueryCriteria criteria = new PmMedicalLineQueryCriteria();
        criteria.setMedicalId(medicalId);
        List<PmMedicalLine> pmMedicalLines = pmMedicalLineDao.listAllByCriteria(criteria);
        if (pmMedicalLines.size() > 0) {
            for(PmMedicalLine medicalLine: pmMedicalLines) {
                PmMedicalLineRelevanceQueryCriteria relevanceQueryCriteria = new PmMedicalLineRelevanceQueryCriteria();
                relevanceQueryCriteria.setMedicalLineId(medicalLine.getId());
                medicalLine.setMedicalLineRelevanceList(pmMedicalLineRelevanceDao.listAllByCriteria(relevanceQueryCriteria));
            }
        }
        return pmMedicalLineMapper.toDto(pmMedicalLines);
    }

    @Override
    public void batchWriteMedicalResult(List<PmMedicalLineQueryCriteria> criteria) {
        for (PmMedicalLineQueryCriteria  swmQa : criteria
        ) {
            pmMedicalLineDao.batchWriteMedicalResult(swmQa);
            // 更新对应的sub月份
        }
    }

    @Override
    public void updateFromOa(PmMedicalLine medicalLine) {
        PmMedicalLine newMedicalLine = Optional.ofNullable(pmMedicalLineDao.getByOA(medicalLine.getEmployeeName(),medicalLine.getMedicalClass())).orElseGet(PmMedicalLine::new);
//        ValidationUtil.isNull(newMedicalLine.getId() ,"MedicalLine", "id", medicalLine.getId());
        if(null != newMedicalLine.getId()){
            pmMedicalLineDao.updateAllColumnFromOa(newMedicalLine.getId(),medicalLine.getMedicalResult());
        }else{
            throw new InfoCheckWarningMessException("没找到相关体检申请！");
        }

    }

    @Override
    public List<PmMedicalLineDTO> getMedicalLinesAuto() {
        return pmMedicalLineDao.getMedicalLinesAuto();
    }
}
