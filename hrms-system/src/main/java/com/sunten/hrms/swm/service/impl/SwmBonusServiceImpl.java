package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.swm.domain.SwmBonus;
import com.sunten.hrms.swm.dao.SwmBonusDao;
import com.sunten.hrms.swm.service.SwmBonusService;
import com.sunten.hrms.swm.dto.SwmBonusDTO;
import com.sunten.hrms.swm.dto.SwmBonusQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmBonusMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.format.DateTimeFormatter;
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
 * 奖金表	    服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmBonusServiceImpl extends ServiceImpl<SwmBonusDao, SwmBonus> implements SwmBonusService {
    private final SwmBonusDao swmBonusDao;
    private final SwmBonusMapper swmBonusMapper;

    public SwmBonusServiceImpl(SwmBonusDao swmBonusDao, SwmBonusMapper swmBonusMapper) {
        this.swmBonusDao = swmBonusDao;
        this.swmBonusMapper = swmBonusMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmBonusDTO insert(SwmBonus bonusNew) {
        swmBonusDao.insertAllColumn(bonusNew);
        return swmBonusMapper.toDto(bonusNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmBonus bonus = new SwmBonus();
        bonus.setId(id);
        swmBonusDao.deleteByEnabled(bonus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmBonus bonus) {
        // TODO    确认删除前是否需要做检查
        swmBonusDao.deleteByEntityKey(bonus);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmBonus bonusNew) {
        SwmBonus bonusInDb = Optional.ofNullable(swmBonusDao.getByKey(bonusNew.getId())).orElseGet(SwmBonus::new);
        ValidationUtil.isNull(bonusInDb.getId() ,"Bonus", "id", bonusNew.getId());
        bonusNew.setId(bonusInDb.getId());
        swmBonusDao.updateAllColumnByKey(bonusNew);
    }

    @Override
    public SwmBonusDTO getByKey(Long id) {
        SwmBonus bonus = Optional.ofNullable(swmBonusDao.getByKey(id)).orElseGet(SwmBonus::new);
        ValidationUtil.isNull(bonus.getId() ,"Bonus", "id", id);
        return swmBonusMapper.toDto(bonus);
    }

    @Override
    public List<SwmBonusDTO> listAll(SwmBonusQueryCriteria criteria) {
        return swmBonusMapper.toDto(swmBonusDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmBonusQueryCriteria criteria, Pageable pageable) {
        Page<SwmBonus> page = PageUtil.startPage(pageable);
        List<SwmBonus> bonuss = swmBonusDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmBonusMapper.toDto(bonuss), page.getTotal());
    }

    @Override
    public void download(List<SwmBonusDTO> bonusDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (SwmBonusDTO bonusDTO : bonusDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("奖金名称", bonusDTO.getBonusName());
            map.put("所属月份", bonusDTO.getMonth());
            map.put("发放日期", bonusDTO.getReleaseTime());
            map.put("金额", bonusDTO.getMoney());
            map.put("备注", bonusDTO.getComment());
            map.put("最后修改人", bonusDTO.getLastUpdateBy());
            map.put("最后修改时间", bonusDTO.getUpdateTime().format(fmt));
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
