package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.pm.domain.EmployeeDZB;
import com.sunten.hrms.pm.dao.EmployeeDZBDao;
import com.sunten.hrms.pm.service.EmployeeDZBService;
import com.sunten.hrms.pm.dto.EmployeeDZBDTO;
import com.sunten.hrms.pm.dto.EmployeeDZBQueryCriteria;
import com.sunten.hrms.pm.mapper.EmployeeDZBMapper;
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
 *  服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-09-09
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EmployeeDZBServiceImpl extends ServiceImpl<EmployeeDZBDao, EmployeeDZB> implements EmployeeDZBService {
    private final EmployeeDZBDao employeeDZBDao;
    private final EmployeeDZBMapper employeeDZBMapper;

    public EmployeeDZBServiceImpl(EmployeeDZBDao employeeDZBDao, EmployeeDZBMapper employeeDZBMapper) {
        this.employeeDZBDao = employeeDZBDao;
        this.employeeDZBMapper = employeeDZBMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmployeeDZBDTO insert(EmployeeDZB employeeDZBNew) {
        employeeDZBDao.insertAllColumn(employeeDZBNew);
        return employeeDZBMapper.toDto(employeeDZBNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        EmployeeDZB employeeDZB = new EmployeeDZB();
        employeeDZB.setId(id);
        this.delete(employeeDZB);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(EmployeeDZB employeeDZB) {
        // TODO    确认删除前是否需要做检查
        employeeDZBDao.deleteByEntityKey(employeeDZB);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EmployeeDZB employeeDZBNew) {
        EmployeeDZB employeeDZBInDb = Optional.ofNullable(employeeDZBDao.getByKey(employeeDZBNew.getId())).orElseGet(EmployeeDZB::new);
        ValidationUtil.isNull(employeeDZBInDb.getId() ,"employeeDZB", "id", employeeDZBNew.getId());
        employeeDZBNew.setId(employeeDZBInDb.getId());
        employeeDZBDao.updateAllColumnByKey(employeeDZBNew);
    }

    @Override
    public EmployeeDZBDTO getByKey(Integer id) {
        EmployeeDZB employeeDZB = Optional.ofNullable(employeeDZBDao.getByKey(id)).orElseGet(EmployeeDZB::new);
        ValidationUtil.isNull(employeeDZB.getId() ,"employeeDZB", "id", id);
        return employeeDZBMapper.toDto(employeeDZB);
    }

    @Override
    public List<EmployeeDZBDTO> listAll(EmployeeDZBQueryCriteria criteria) {
        return employeeDZBMapper.toDto(employeeDZBDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(EmployeeDZBQueryCriteria criteria, Pageable pageable) {
        Page<EmployeeDZB> page = PageUtil.startPage(pageable);
        List<EmployeeDZB> employeeDZBs = employeeDZBDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(employeeDZBMapper.toDto(employeeDZBs), page.getTotal());
    }

    @Override
    public void download(List<EmployeeDZBDTO> employeeDZBDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (EmployeeDZBDTO employeeDZBDTO : employeeDZBDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("kh", employeeDZBDTO.getKh());
            map.put("gh", employeeDZBDTO.getGh());
            map.put("xjbs", employeeDZBDTO.getXjbs());
            map.put("clbs", employeeDZBDTO.getClbs());
            map.put("xm", employeeDZBDTO.getXm());
            map.put("id", employeeDZBDTO.getId());
            map.put("bh", employeeDZBDTO.getBh());
            map.put("sj", employeeDZBDTO.getSj());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
