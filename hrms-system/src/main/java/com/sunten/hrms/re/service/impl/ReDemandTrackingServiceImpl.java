package com.sunten.hrms.re.service.impl;

import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.re.domain.ReDemandTracking;
import com.sunten.hrms.re.dao.ReDemandTrackingDao;
import com.sunten.hrms.re.service.ReDemandTrackingService;
import com.sunten.hrms.re.dto.ReDemandTrackingDTO;
import com.sunten.hrms.re.dto.ReDemandTrackingQueryCriteria;
import com.sunten.hrms.re.mapper.ReDemandTrackingMapper;
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
 * 用人需求招聘过程记录 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2022-01-18
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReDemandTrackingServiceImpl extends ServiceImpl<ReDemandTrackingDao, ReDemandTracking> implements ReDemandTrackingService {
    private final ReDemandTrackingDao reDemandTrackingDao;
    private final ReDemandTrackingMapper reDemandTrackingMapper;
    private final FndUserService fndUserService;

    public ReDemandTrackingServiceImpl(ReDemandTrackingDao reDemandTrackingDao, ReDemandTrackingMapper reDemandTrackingMapper, FndUserService fndUserService) {
        this.reDemandTrackingDao = reDemandTrackingDao;
        this.reDemandTrackingMapper = reDemandTrackingMapper;
        this.fndUserService = fndUserService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ReDemandTrackingDTO>  insert(ReDemandTracking demandTrackingNew) {
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        demandTrackingNew.setEditBy(null != fndUserDTO.getEmployee() ? fndUserDTO.getEmployee().getName() : fndUserDTO.getUsername());
        reDemandTrackingDao.insertAllColumn(demandTrackingNew);
        ReDemandTrackingQueryCriteria reDemandTrackingQueryCriteria = new ReDemandTrackingQueryCriteria();
        reDemandTrackingQueryCriteria.setEnabledFlag(true);
        reDemandTrackingQueryCriteria.setDemandId(demandTrackingNew.getDemandId());
        return reDemandTrackingMapper.toDto(getSimpleList(demandTrackingNew.getDemandId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        reDemandTrackingDao.deleteByEnabled(id, fndUserDTO.getId());
//        ReDemandTracking demandTracking = new ReDemandTracking();
//        demandTracking.setId(id);
//        this.delete(demandTracking);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReDemandTracking demandTracking) {
        // TODO    确认删除前是否需要做检查
        reDemandTrackingDao.deleteByEntityKey(demandTracking);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ReDemandTrackingDTO>  update(ReDemandTracking demandTrackingNew) {
        ReDemandTracking demandTrackingInDb = Optional.ofNullable(reDemandTrackingDao.getByKey(demandTrackingNew.getId())).orElseGet(ReDemandTracking::new);
        ValidationUtil.isNull(demandTrackingInDb.getId() ,"DemandTracking", "id", demandTrackingNew.getId());
        demandTrackingNew.setId(demandTrackingInDb.getId());
        reDemandTrackingDao.updateAllColumnByKey(demandTrackingNew);
        return reDemandTrackingMapper.toDto(getSimpleList(demandTrackingNew.getDemandId()));
    }

    @Override
    public ReDemandTrackingDTO getByKey(Long id) {
        ReDemandTracking demandTracking = Optional.ofNullable(reDemandTrackingDao.getByKey(id)).orElseGet(ReDemandTracking::new);
        ValidationUtil.isNull(demandTracking.getId() ,"DemandTracking", "id", id);
        return reDemandTrackingMapper.toDto(demandTracking);
    }

    @Override
    public List<ReDemandTrackingDTO> listAll(ReDemandTrackingQueryCriteria criteria) {
        return reDemandTrackingMapper.toDto(reDemandTrackingDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReDemandTrackingQueryCriteria criteria, Pageable pageable) {
        Page<ReDemandTracking> page = PageUtil.startPage(pageable);
        List<ReDemandTracking> demandTrackings = reDemandTrackingDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reDemandTrackingMapper.toDto(demandTrackings), page.getTotal());
    }

    @Override
    public void download(List<ReDemandTrackingDTO> demandTrackingDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReDemandTrackingDTO demandTrackingDTO : demandTrackingDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("需求id", demandTrackingDTO.getDemandId());
            map.put("过程记录", demandTrackingDTO.getReContent());
            map.put("id", demandTrackingDTO.getId());
            map.put("createTime", demandTrackingDTO.getCreateTime());
            map.put("updateBy", demandTrackingDTO.getUpdateBy());
            map.put("updateTime", demandTrackingDTO.getUpdateTime());
            map.put("createBy", demandTrackingDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    private List<ReDemandTracking> getSimpleList(Long demandId) {
        ReDemandTrackingQueryCriteria reDemandTrackingQueryCriteria = new ReDemandTrackingQueryCriteria();
        reDemandTrackingQueryCriteria.setDemandId(demandId);
        reDemandTrackingQueryCriteria.setEnabledFlag(true);
        return reDemandTrackingDao.listAllByCriteria(reDemandTrackingQueryCriteria);
    }
}
