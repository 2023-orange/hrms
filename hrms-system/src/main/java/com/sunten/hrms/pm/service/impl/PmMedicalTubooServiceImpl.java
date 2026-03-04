package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.pm.dao.PmMedicalTubooDao;
import com.sunten.hrms.pm.domain.PmMedicalTuboo;
import com.sunten.hrms.pm.dto.PmMedicalTubooDTO;
import com.sunten.hrms.pm.dto.PmMedicalTubooQueryCriteria;
import com.sunten.hrms.pm.mapper.PmMedicalTubooMapper;
import com.sunten.hrms.pm.service.PmMedicalTubooService;
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

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoujy
 * @since 2022-11-30
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmMedicalTubooServiceImpl extends ServiceImpl<PmMedicalTubooDao, PmMedicalTuboo> implements PmMedicalTubooService {
    private final PmMedicalTubooDao pmMedicalTubooDao;
    private final PmMedicalTubooMapper pmMedicalTubooMapper;

    public PmMedicalTubooServiceImpl(PmMedicalTubooDao pmMedicalTubooDao, PmMedicalTubooMapper pmMedicalTubooMapper) {
        this.pmMedicalTubooDao = pmMedicalTubooDao;
        this.pmMedicalTubooMapper = pmMedicalTubooMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmMedicalTubooDTO insert(PmMedicalTuboo pmMedicalTubooNew) {
        pmMedicalTubooDao.insertAllColumn(pmMedicalTubooNew);
        return pmMedicalTubooMapper.toDto(pmMedicalTubooNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmMedicalTuboo pmMedicalTuboo = new PmMedicalTuboo();
        pmMedicalTuboo.setId(id);
        this.delete(pmMedicalTuboo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmMedicalTuboo pmMedicalTuboo) {
        // TODO    确认删除前是否需要做检查
        pmMedicalTubooDao.deleteByEntityKey(pmMedicalTuboo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmMedicalTuboo pmMedicalTubooNew) {
        PmMedicalTuboo pmMedicalTubooInDb = Optional.ofNullable(pmMedicalTubooDao.getByKey(pmMedicalTubooNew.getId())).orElseGet(PmMedicalTuboo::new);
        ValidationUtil.isNull(pmMedicalTubooInDb.getId() ,"pmMedicalTuboo", "id", pmMedicalTubooNew.getId());
        pmMedicalTubooNew.setId(pmMedicalTubooInDb.getId());
        pmMedicalTubooDao.updateAllColumnByKey(pmMedicalTubooNew);
    }

    @Override
    public PmMedicalTubooDTO getByKey(Long id) {
        PmMedicalTuboo pmMedicalTuboo = Optional.ofNullable(pmMedicalTubooDao.getByKey(id)).orElseGet(PmMedicalTuboo::new);
        ValidationUtil.isNull(pmMedicalTuboo.getId() ,"pmMedicalTuboo", "id", id);
        return pmMedicalTubooMapper.toDto(pmMedicalTuboo);
    }

    @Override
    public List<PmMedicalTubooDTO> listAll(PmMedicalTubooQueryCriteria criteria) {
        return pmMedicalTubooMapper.toDto(pmMedicalTubooDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmMedicalTubooQueryCriteria criteria, Pageable pageable) {
        Page<PmMedicalTuboo> page = PageUtil.startPage(pageable);
        List<PmMedicalTuboo> pmMedicalTuboos = pmMedicalTubooDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmMedicalTubooMapper.toDto(pmMedicalTuboos), page.getTotal());
    }

    @Override
    public void download(List<PmMedicalTubooDTO> pmMedicalTubooDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmMedicalTubooDTO pmMedicalTubooDTO : pmMedicalTubooDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", pmMedicalTubooDTO.getId());
            map.put("人事档案ID", pmMedicalTubooDTO.getEmployeeId());
            map.put("职业禁忌", pmMedicalTubooDTO.getJobTuboo());
            map.put("remarks", pmMedicalTubooDTO.getRemarks());
            map.put("创建时间", pmMedicalTubooDTO.getCreateTime());
            map.put("创建人", pmMedicalTubooDTO.getCreateBy());
            map.put("最后修改时间", pmMedicalTubooDTO.getUpdateTime());
            map.put("最后修改人", pmMedicalTubooDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<PmMedicalTubooDTO> getPmMedicalTubooSub(String workCard) {
        return pmMedicalTubooMapper.toDto(pmMedicalTubooDao.getPmMedicalTubooSub(workCard));
    }
}
