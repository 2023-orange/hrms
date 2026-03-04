package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.swm.domain.SwmSalatax;
import com.sunten.hrms.swm.dao.SwmSalataxDao;
import com.sunten.hrms.swm.service.SwmSalataxService;
import com.sunten.hrms.swm.dto.SwmSalataxDTO;
import com.sunten.hrms.swm.dto.SwmSalataxQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmSalataxMapper;
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
 * 旧的所得税表 服务实现类
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-10
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmSalataxServiceImpl extends ServiceImpl<SwmSalataxDao, SwmSalatax> implements SwmSalataxService {
    private final SwmSalataxDao swmSalataxDao;
    private final SwmSalataxMapper swmSalataxMapper;

    public SwmSalataxServiceImpl(SwmSalataxDao swmSalataxDao, SwmSalataxMapper swmSalataxMapper) {
        this.swmSalataxDao = swmSalataxDao;
        this.swmSalataxMapper = swmSalataxMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmSalataxDTO insert(SwmSalatax salataxNew) {
        swmSalataxDao.insertAllColumn(salataxNew);
        return swmSalataxMapper.toDto(salataxNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete() {
        SwmSalatax salatax = new SwmSalatax();
        this.delete(salatax);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmSalatax salatax) {
        // TODO    确认删除前是否需要做检查
        swmSalataxDao.deleteByEntityKey(salatax);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmSalatax salataxNew) {
        SwmSalatax salataxInDb = Optional.ofNullable(swmSalataxDao.getByKey()).orElseGet(SwmSalatax::new);
        swmSalataxDao.updateAllColumnByKey(salataxNew);
    }

    @Override
    public SwmSalataxDTO getByKey() {
        SwmSalatax salatax = Optional.ofNullable(swmSalataxDao.getByKey()).orElseGet(SwmSalatax::new);
        return swmSalataxMapper.toDto(salatax);
    }

    @Override
    public List<SwmSalataxDTO> listAll(SwmSalataxQueryCriteria criteria) {
        return swmSalataxMapper.toDto(swmSalataxDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmSalataxQueryCriteria criteria, Pageable pageable) {
        Page<SwmSalatax> page = PageUtil.startPage(pageable);
        List<SwmSalatax> salataxs = swmSalataxDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmSalataxMapper.toDto(salataxs), page.getTotal());
    }

    @Override
    public void download(List<SwmSalataxDTO> salataxDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmSalataxDTO salataxDTO : salataxDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", salataxDTO.getId());
            map.put("fdyfgz", salataxDTO.getFdyfgz());
            map.put("gdyfgz", salataxDTO.getGdyfgz());
            map.put("btYh", salataxDTO.getBtYh());
            map.put("kcBx", salataxDTO.getKcBx());
            map.put("jjje", salataxDTO.getJjje());
            map.put("sjsdqj", salataxDTO.getSjsdqj());
            map.put("sjsdqj1", salataxDTO.getSjsdqj1());
            map.put("gph", salataxDTO.getGph());
            map.put("xm", salataxDTO.getXm());
            map.put("sdqj", salataxDTO.getSdqj());
            map.put("rmb", salataxDTO.getRmb());
            map.put("jfye", salataxDTO.getJfye());
            map.put("ynssde", salataxDTO.getYnssde());
            map.put("sl", salataxDTO.getSl());
            map.put("sskcs", salataxDTO.getSskcs());
            map.put("dksds", salataxDTO.getDksds());
            map.put("dksds0701", salataxDTO.getDksds0701());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<SwmSalataxDTO> getOldSalatax(String workCard, String year) {
        return swmSalataxMapper.toDto(swmSalataxDao.getOldSalatax(workCard, year));
    }
}
