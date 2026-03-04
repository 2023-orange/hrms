package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.KqCrjsjDao;
import com.sunten.hrms.ac.domain.KqCrjsj;
import com.sunten.hrms.ac.dto.KqCrjsjDTO;
import com.sunten.hrms.ac.dto.KqCrjsjQueryCriteria;
import com.sunten.hrms.ac.mapper.KqCrjsjMapper;
import com.sunten.hrms.ac.service.KqCrjsjService;
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
public class KqCrjsjServiceImpl extends ServiceImpl<KqCrjsjDao, KqCrjsj> implements KqCrjsjService {
    private final KqCrjsjDao kqCrjsjDao;
    private final KqCrjsjMapper kqCrjsjMapper;

    public KqCrjsjServiceImpl(KqCrjsjDao kqCrjsjDao, KqCrjsjMapper kqCrjsjMapper) {
        this.kqCrjsjDao = kqCrjsjDao;
        this.kqCrjsjMapper = kqCrjsjMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KqCrjsjDTO insert(KqCrjsj kqCrjsjNew) {
        if (kqCrjsjNew.getTime8().getHour() < 9) {
            kqCrjsjNew.setMjkzqbh("南门闸机1入");
        }
        if (kqCrjsjNew.getTime8().getHour() > 17) {
            kqCrjsjNew.setMjkzqbh("南门闸机2出");
        }
        if (null == kqCrjsjNew.getMjkzqbh()) {
            kqCrjsjNew.setMjkzqbh("南门闸机1入");
        }
        kqCrjsjDao.insertAllColumn(kqCrjsjNew);
        return kqCrjsjMapper.toDto(kqCrjsjNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete() {
        KqCrjsj kqCrjsj = new KqCrjsj();
        this.delete(kqCrjsj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(KqCrjsj kqCrjsj) {
        // TODO    确认删除前是否需要做检查
        kqCrjsjDao.deleteByEntityKey(kqCrjsj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(KqCrjsj kqCrjsjNew) {
        KqCrjsj kqCrjsjInDb = Optional.ofNullable(kqCrjsjDao.getByKey()).orElseGet(KqCrjsj::new);
        kqCrjsjDao.updateAllColumnByKey(kqCrjsjNew);
    }

    @Override
    public KqCrjsjDTO getByKey() {
        KqCrjsj kqCrjsj = Optional.ofNullable(kqCrjsjDao.getByKey()).orElseGet(KqCrjsj::new);
        return kqCrjsjMapper.toDto(kqCrjsj);
    }

    @Override
    public List<KqCrjsjDTO> listAll(KqCrjsjQueryCriteria criteria) {
        return kqCrjsjMapper.toDto(kqCrjsjDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(KqCrjsjQueryCriteria criteria, Pageable pageable) {
        Page<KqCrjsj> page = PageUtil.startPage(pageable);
        List<KqCrjsj> kqCrjsjs = kqCrjsjDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(kqCrjsjMapper.toDto(kqCrjsjs), page.getTotal());
    }

    @Override
    public void download(List<KqCrjsjDTO> kqCrjsjDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (KqCrjsjDTO kqCrjsjDTO : kqCrjsjDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("mjjbh", kqCrjsjDTO.getMjjbh());
            map.put("kh", kqCrjsjDTO.getKh());
            map.put("crjqk", kqCrjsjDTO.getCrjqk());
            map.put("time8", kqCrjsjDTO.getTime8());
            map.put("acstosql", kqCrjsjDTO.getAcstosql());
            map.put("mjkzqbh", kqCrjsjDTO.getMjkzqbh());
            map.put("syqk", kqCrjsjDTO.getSyqk());
            map.put("id", kqCrjsjDTO.getID());
            map.put("date8", kqCrjsjDTO.getDate8());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
