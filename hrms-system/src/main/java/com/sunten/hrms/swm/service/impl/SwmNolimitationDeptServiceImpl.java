package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.swm.domain.SwmNolimitationDept;
import com.sunten.hrms.swm.dao.SwmNolimitationDeptDao;
import com.sunten.hrms.swm.service.SwmNolimitationDeptService;
import com.sunten.hrms.swm.dto.SwmNolimitationDeptDTO;
import com.sunten.hrms.swm.dto.SwmNolimitationDeptQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmNolimitationDeptMapper;
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
 * @author zhoujy
 * @since 2023-11-21
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmNolimitationDeptServiceImpl extends ServiceImpl<SwmNolimitationDeptDao, SwmNolimitationDept> implements SwmNolimitationDeptService {
    private final SwmNolimitationDeptDao swmNolimitationDeptDao;
    private final SwmNolimitationDeptMapper swmNolimitationDeptMapper;

    public SwmNolimitationDeptServiceImpl(SwmNolimitationDeptDao swmNolimitationDeptDao, SwmNolimitationDeptMapper swmNolimitationDeptMapper) {
        this.swmNolimitationDeptDao = swmNolimitationDeptDao;
        this.swmNolimitationDeptMapper = swmNolimitationDeptMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmNolimitationDeptDTO insert(SwmNolimitationDept nolimitationDeptNew) {
        System.out.println(nolimitationDeptNew);
        swmNolimitationDeptDao.insertAllColumn(nolimitationDeptNew);
        return swmNolimitationDeptMapper.toDto(nolimitationDeptNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete() {
        SwmNolimitationDept nolimitationDept = new SwmNolimitationDept();
        this.delete(nolimitationDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmNolimitationDept nolimitationDept) {
        // TODO    确认删除前是否需要做检查
        swmNolimitationDeptDao.deleteByEntityKey(nolimitationDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmNolimitationDept nolimitationDeptNew) {
//        SwmNolimitationDept nolimitationDeptInDb = Optional.ofNullable(swmNolimitationDeptDao.getByKey()).orElseGet(SwmNolimitationDept::new);
        swmNolimitationDeptDao.updateAllColumnByKey(nolimitationDeptNew);
    }

    @Override
    public SwmNolimitationDeptDTO getByKey() {
        SwmNolimitationDept nolimitationDept = Optional.ofNullable(swmNolimitationDeptDao.getByKey()).orElseGet(SwmNolimitationDept::new);
        return swmNolimitationDeptMapper.toDto(nolimitationDept);
    }

    @Override
    public List<SwmNolimitationDeptDTO> listAll(SwmNolimitationDeptQueryCriteria criteria) {
        return swmNolimitationDeptMapper.toDto(swmNolimitationDeptDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmNolimitationDeptQueryCriteria criteria, Pageable pageable) {
        Page<SwmNolimitationDept> page = PageUtil.startPage(pageable);
        List<SwmNolimitationDept> nolimitationDepts = swmNolimitationDeptDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmNolimitationDeptMapper.toDto(nolimitationDepts), page.getTotal());
    }

    @Override
    public void download(List<SwmNolimitationDeptDTO> nolimitationDeptDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmNolimitationDeptDTO nolimitationDeptDTO : nolimitationDeptDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", nolimitationDeptDTO.getId());
            map.put("不限制评A部门的id", nolimitationDeptDTO.getDeptId());
            map.put("enabledFlag", nolimitationDeptDTO.getEnabledFlag());
            map.put("createTime", nolimitationDeptDTO.getCreateTime());
            map.put("createBy", nolimitationDeptDTO.getCreateBy());
            map.put("updateTime", nolimitationDeptDTO.getUpdateTime());
            map.put("updateBy", nolimitationDeptDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Long countDept(String deptName) {
        return swmNolimitationDeptDao.countDept(deptName);
    }

    @Override
    public List<SwmNolimitationDept> getSwmDept() {
        return swmNolimitationDeptDao.getSwmDept();
    }
}
