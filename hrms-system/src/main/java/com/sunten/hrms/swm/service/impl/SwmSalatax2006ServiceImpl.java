package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.swm.domain.SwmSalatax2006;
import com.sunten.hrms.swm.dao.SwmSalatax2006Dao;
import com.sunten.hrms.swm.service.SwmSalatax2006Service;
import com.sunten.hrms.swm.dto.SwmSalatax2006DTO;
import com.sunten.hrms.swm.dto.SwmSalatax2006QueryCriteria;
import com.sunten.hrms.swm.mapper.SwmSalatax2006Mapper;
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
 * 年底奖金所得税 服务实现类
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-11
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmSalatax2006ServiceImpl extends ServiceImpl<SwmSalatax2006Dao, SwmSalatax2006> implements SwmSalatax2006Service {
    private final SwmSalatax2006Dao swmSalatax2006Dao;
    private final SwmSalatax2006Mapper swmSalatax2006Mapper;

    public SwmSalatax2006ServiceImpl(SwmSalatax2006Dao swmSalatax2006Dao, SwmSalatax2006Mapper swmSalatax2006Mapper) {
        this.swmSalatax2006Dao = swmSalatax2006Dao;
        this.swmSalatax2006Mapper = swmSalatax2006Mapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmSalatax2006DTO insert(SwmSalatax2006 salatax2006New) {
        swmSalatax2006Dao.insertAllColumn(salatax2006New);
        return swmSalatax2006Mapper.toDto(salatax2006New);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete() {
        SwmSalatax2006 salatax2006 = new SwmSalatax2006();
        this.delete(salatax2006);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmSalatax2006 salatax2006) {
        // TODO    确认删除前是否需要做检查
        swmSalatax2006Dao.deleteByEntityKey(salatax2006);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmSalatax2006 salatax2006New) {
        SwmSalatax2006 salatax2006InDb = Optional.ofNullable(swmSalatax2006Dao.getByKey()).orElseGet(SwmSalatax2006::new);
        swmSalatax2006Dao.updateAllColumnByKey(salatax2006New);
    }

    @Override
    public SwmSalatax2006DTO getByKey() {
        SwmSalatax2006 salatax2006 = Optional.ofNullable(swmSalatax2006Dao.getByKey()).orElseGet(SwmSalatax2006::new);
        return swmSalatax2006Mapper.toDto(salatax2006);
    }

    @Override
    public List<SwmSalatax2006DTO> listAll(SwmSalatax2006QueryCriteria criteria) {
        return swmSalatax2006Mapper.toDto(swmSalatax2006Dao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmSalatax2006QueryCriteria criteria, Pageable pageable) {
        Page<SwmSalatax2006> page = PageUtil.startPage(pageable);
        List<SwmSalatax2006> salatax2006s = swmSalatax2006Dao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmSalatax2006Mapper.toDto(salatax2006s), page.getTotal());
    }


    @Override
    public List<SwmSalatax2006DTO> getOldSalatax2006(String workCard) {
        return swmSalatax2006Mapper.toDto(swmSalatax2006Dao.getOldSalatax2006(workCard));
    }
}
