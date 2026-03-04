package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.KqCrjsjHisDao;
import com.sunten.hrms.ac.domain.KqCrjsjHis;
import com.sunten.hrms.ac.dto.KqCrjsjHisDTO;
import com.sunten.hrms.ac.dto.KqCrjsjHisQueryCriteria;
import com.sunten.hrms.ac.mapper.KqCrjsjHisMapper;
import com.sunten.hrms.ac.service.KqCrjsjHisService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-19
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KqCrjsjHisServiceImpl extends ServiceImpl<KqCrjsjHisDao, KqCrjsjHis> implements KqCrjsjHisService {
    private final KqCrjsjHisDao kqCrjsjHisDao;
    private final KqCrjsjHisMapper kqCrjsjHisMapper;

    public KqCrjsjHisServiceImpl(KqCrjsjHisDao kqCrjsjHisDao, KqCrjsjHisMapper kqCrjsjHisMapper) {
        this.kqCrjsjHisDao = kqCrjsjHisDao;
        this.kqCrjsjHisMapper = kqCrjsjHisMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KqCrjsjHisDTO insert(KqCrjsjHis kqCrjsjHisNew) {
        kqCrjsjHisDao.insertAllColumn(kqCrjsjHisNew);
        return kqCrjsjHisMapper.toDto(kqCrjsjHisNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete() {
        KqCrjsjHis kqCrjsjHis = new KqCrjsjHis();
        this.delete(kqCrjsjHis);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(KqCrjsjHis kqCrjsjHis) {
        // TODO    确认删除前是否需要做检查
        kqCrjsjHisDao.deleteByEntityKey(kqCrjsjHis);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(KqCrjsjHis kqCrjsjHisNew) {
        KqCrjsjHis kqCrjsjHisInDb = Optional.ofNullable(kqCrjsjHisDao.getByKey()).orElseGet(KqCrjsjHis::new);
        kqCrjsjHisDao.updateAllColumnByKey(kqCrjsjHisNew);
    }

    @Override
    public KqCrjsjHisDTO getByKey() {
        KqCrjsjHis kqCrjsjHis = Optional.ofNullable(kqCrjsjHisDao.getByKey()).orElseGet(KqCrjsjHis::new);
        return kqCrjsjHisMapper.toDto(kqCrjsjHis);
    }

    @Override
    public List<KqCrjsjHisDTO> listAll(KqCrjsjHisQueryCriteria criteria) {
        return kqCrjsjHisMapper.toDto(kqCrjsjHisDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(KqCrjsjHisQueryCriteria criteria, Pageable pageable) {
        Page<KqCrjsjHis> page = PageUtil.startPage(pageable);
        List<KqCrjsjHis> kqCrjsjHiss = kqCrjsjHisDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(kqCrjsjHisMapper.toDto(kqCrjsjHiss), page.getTotal());
    }

    @Override
    public void download(List<KqCrjsjHisDTO> kqCrjsjHisDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (KqCrjsjHisDTO kqCrjsjHisDTO : kqCrjsjHisDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("mjkzqbh", kqCrjsjHisDTO.getMjkzqbh());
            map.put("time8", kqCrjsjHisDTO.getTime8());
            map.put("acstosql", kqCrjsjHisDTO.getAcstosql());
            map.put("crjqk", kqCrjsjHisDTO.getCrjqk());
            map.put("mjjbh", kqCrjsjHisDTO.getMjjbh());
            map.put("kh", kqCrjsjHisDTO.getKh());
            map.put("id", kqCrjsjHisDTO.getId());
            map.put("date8", kqCrjsjHisDTO.getDate8());
            map.put("syqk", kqCrjsjHisDTO.getSyqk());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
