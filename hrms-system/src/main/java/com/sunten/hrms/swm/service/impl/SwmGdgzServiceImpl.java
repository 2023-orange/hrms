package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.swm.domain.SwmGdgz;
import com.sunten.hrms.swm.dao.SwmGdgzDao;
import com.sunten.hrms.swm.service.SwmGdgzService;
import com.sunten.hrms.swm.dto.SwmGdgzDTO;
import com.sunten.hrms.swm.dto.SwmGdgzQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmGdgzMapper;
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
 * 旧的固定工资表 服务实现类
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-07
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmGdgzServiceImpl extends ServiceImpl<SwmGdgzDao, SwmGdgz> implements SwmGdgzService {
    private final SwmGdgzDao swmGdgzDao;
    private final SwmGdgzMapper swmGdgzMapper;

    public SwmGdgzServiceImpl(SwmGdgzDao swmGdgzDao, SwmGdgzMapper swmGdgzMapper) {
        this.swmGdgzDao = swmGdgzDao;
        this.swmGdgzMapper = swmGdgzMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmGdgzDTO insert(SwmGdgz gdgzNew) {
        swmGdgzDao.insertAllColumn(gdgzNew);
        return swmGdgzMapper.toDto(gdgzNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete() {
        SwmGdgz gdgz = new SwmGdgz();
        this.delete(gdgz);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmGdgz gdgz) {
        // TODO    确认删除前是否需要做检查
        swmGdgzDao.deleteByEntityKey(gdgz);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmGdgz gdgzNew) {
        SwmGdgz gdgzInDb = Optional.ofNullable(swmGdgzDao.getByKey()).orElseGet(SwmGdgz::new);
        swmGdgzDao.updateAllColumnByKey(gdgzNew);
    }

    @Override
    public SwmGdgzDTO getByKey() {
        SwmGdgz gdgz = Optional.ofNullable(swmGdgzDao.getByKey()).orElseGet(SwmGdgz::new);
        return swmGdgzMapper.toDto(gdgz);
    }

    @Override
    public List<SwmGdgzDTO> listAll(SwmGdgzQueryCriteria criteria) {
        return swmGdgzMapper.toDto(swmGdgzDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmGdgzQueryCriteria criteria, Pageable pageable) {
        Page<SwmGdgz> page = PageUtil.startPage(pageable);
        List<SwmGdgz> gdgzs = swmGdgzDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmGdgzMapper.toDto(gdgzs), page.getTotal());
    }

    @Override
    public void download(List<SwmGdgzDTO> gdgzDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmGdgzDTO gdgzDTO : gdgzDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", gdgzDTO.getId());
            map.put("sdqj", gdgzDTO.getSdqj());
            map.put("gph", gdgzDTO.getGph());
            map.put("xm", gdgzDTO.getXm());
            map.put("bgqf", gdgzDTO.getBgqf());
            map.put("szbm", gdgzDTO.getSzbm());
            map.put("szks", gdgzDTO.getSzks());
            map.put("yhzh", gdgzDTO.getYhzh());
            map.put("btYh", gdgzDTO.getBtYh());
            map.put("btkcSq", gdgzDTO.getBtkcSq());
            map.put("kcYyf", gdgzDTO.getKcYyf());
            map.put("kcSdf", gdgzDTO.getKcSdf());
            map.put("kcBx", gdgzDTO.getKcBx());
            map.put("kcSds", gdgzDTO.getKcSds());
            map.put("kcQt", gdgzDTO.getKcQt());
            map.put("kcLf", gdgzDTO.getKcLf());
            map.put("jbgz", gdgzDTO.getJbgz());
            map.put("yfgz", gdgzDTO.getYfgz());
            map.put("sfgz", gdgzDTO.getSfgz());
            map.put("bzmc", gdgzDTO.getBzmc());
            map.put("jbgzInput", gdgzDTO.getJbgzInput());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<SwmGdgzDTO> getOldFixedSalary(String workCard, String year) {
        return swmGdgzMapper.toDto(swmGdgzDao.getOldFixedSalary(workCard, year));
    }
}
