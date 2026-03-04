package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.swm.domain.SwmFdgz;
import com.sunten.hrms.swm.dao.SwmFdgzDao;
import com.sunten.hrms.swm.service.SwmFdgzService;
import com.sunten.hrms.swm.dto.SwmFdgzDTO;
import com.sunten.hrms.swm.dto.SwmFdgzQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmFdgzMapper;
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
 * 旧的浮动工资表 服务实现类
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-10
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmFdgzServiceImpl extends ServiceImpl<SwmFdgzDao, SwmFdgz> implements SwmFdgzService {
    private final SwmFdgzDao swmFdgzDao;
    private final SwmFdgzMapper swmFdgzMapper;

    public SwmFdgzServiceImpl(SwmFdgzDao swmFdgzDao, SwmFdgzMapper swmFdgzMapper) {
        this.swmFdgzDao = swmFdgzDao;
        this.swmFdgzMapper = swmFdgzMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmFdgzDTO insert(SwmFdgz fdgzNew) {
        swmFdgzDao.insertAllColumn(fdgzNew);
        return swmFdgzMapper.toDto(fdgzNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete() {
        SwmFdgz fdgz = new SwmFdgz();
        this.delete(fdgz);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmFdgz fdgz) {
        // TODO    确认删除前是否需要做检查
        swmFdgzDao.deleteByEntityKey(fdgz);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmFdgz fdgzNew) {
        SwmFdgz fdgzInDb = Optional.ofNullable(swmFdgzDao.getByKey()).orElseGet(SwmFdgz::new);
        swmFdgzDao.updateAllColumnByKey(fdgzNew);
    }

    @Override
    public SwmFdgzDTO getByKey() {
        SwmFdgz fdgz = Optional.ofNullable(swmFdgzDao.getByKey()).orElseGet(SwmFdgz::new);
        return swmFdgzMapper.toDto(fdgz);
    }

    @Override
    public List<SwmFdgzDTO> listAll(SwmFdgzQueryCriteria criteria) {
        return swmFdgzMapper.toDto(swmFdgzDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmFdgzQueryCriteria criteria, Pageable pageable) {
        Page<SwmFdgz> page = PageUtil.startPage(pageable);
        List<SwmFdgz> fdgzs = swmFdgzDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmFdgzMapper.toDto(fdgzs), page.getTotal());
    }

    @Override
    public void download(List<SwmFdgzDTO> fdgzDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmFdgzDTO fdgzDTO : fdgzDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", fdgzDTO.getId());
            map.put("sdqj", fdgzDTO.getSdqj());
            map.put("gph", fdgzDTO.getGph());
            map.put("xm", fdgzDTO.getXm());
            map.put("szbm", fdgzDTO.getSzbm());
            map.put("szks", fdgzDTO.getSzks());
            map.put("yhzh", fdgzDTO.getYhzh());
            map.put("tpgz", fdgzDTO.getTpgz());
            map.put("xddj", fdgzDTO.getXddj());
            map.put("tjbs", fdgzDTO.getTjbs());
            map.put("jlkfSq", fdgzDTO.getJlkfSq());
            map.put("jlkfSh", fdgzDTO.getJlkfSh());
            map.put("kcSds", fdgzDTO.getKcSds());
            map.put("sfgz", fdgzDTO.getSfgz());
            map.put("yfgz", fdgzDTO.getYfgz());
            map.put("tpjz", fdgzDTO.getTpjz());
            map.put("bzmc", fdgzDTO.getBzmc());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<SwmFdgzDTO> getOldFloatSalary(String workCard, String year) {
        return swmFdgzMapper.toDto(swmFdgzDao.getOldFloatSalary(workCard, year));
    }
}
