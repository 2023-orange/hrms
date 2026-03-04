package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.KqCrjsjLsDao;
import com.sunten.hrms.ac.domain.KqCrjsjLs;
import com.sunten.hrms.ac.dto.KqCrjsjLsDTO;
import com.sunten.hrms.ac.dto.KqCrjsjLsQueryCriteria;
import com.sunten.hrms.ac.mapper.KqCrjsjLsMapper;
import com.sunten.hrms.ac.service.KqCrjsjLsService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
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
public class KqCrjsjLsServiceImpl extends ServiceImpl<KqCrjsjLsDao, KqCrjsjLs> implements KqCrjsjLsService {
    private final KqCrjsjLsDao kqCrjsjLsDao;
    private final KqCrjsjLsMapper kqCrjsjLsMapper;

    public KqCrjsjLsServiceImpl(KqCrjsjLsDao kqCrjsjLsDao, KqCrjsjLsMapper kqCrjsjLsMapper) {
        this.kqCrjsjLsDao = kqCrjsjLsDao;
        this.kqCrjsjLsMapper = kqCrjsjLsMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KqCrjsjLsDTO insert(KqCrjsjLs kqCrjsjLsNew) {
        kqCrjsjLsDao.insertAllColumn(kqCrjsjLsNew);
        return kqCrjsjLsMapper.toDto(kqCrjsjLsNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(LocalDateTime date8, String kh, String mjkzqbh, LocalDateTime time8) {
        KqCrjsjLs kqCrjsjLs = new KqCrjsjLs();
        kqCrjsjLs.setDate8(date8);
        kqCrjsjLs.setKh(kh);
        kqCrjsjLs.setMjkzqbh(mjkzqbh);
        kqCrjsjLs.setTime8(time8);
        this.delete(kqCrjsjLs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(KqCrjsjLs kqCrjsjLs) {
        // TODO    确认删除前是否需要做检查
        kqCrjsjLsDao.deleteByEntityKey(kqCrjsjLs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(KqCrjsjLs kqCrjsjLsNew) {
        KqCrjsjLs kqCrjsjLsInDb = Optional.ofNullable(kqCrjsjLsDao.getByKey(kqCrjsjLsNew.getDate8(), kqCrjsjLsNew.getKh(), kqCrjsjLsNew.getMjkzqbh(), kqCrjsjLsNew.getTime8())).orElseGet(KqCrjsjLs::new);
        ValidationUtil.isNull(kqCrjsjLsInDb.getDate8(), "kqCrjsjLs", "date8", kqCrjsjLsNew.getDate8());
        kqCrjsjLsNew.setDate8(kqCrjsjLsInDb.getDate8());
        ValidationUtil.isNull(kqCrjsjLsInDb.getKh(), "kqCrjsjLs", "kh", kqCrjsjLsNew.getKh());
        kqCrjsjLsNew.setKh(kqCrjsjLsInDb.getKh());
        ValidationUtil.isNull(kqCrjsjLsInDb.getMjkzqbh(), "kqCrjsjLs", "mjkzqbh", kqCrjsjLsNew.getMjkzqbh());
        kqCrjsjLsNew.setMjkzqbh(kqCrjsjLsInDb.getMjkzqbh());
        ValidationUtil.isNull(kqCrjsjLsInDb.getTime8(), "kqCrjsjLs", "time8", kqCrjsjLsNew.getTime8());
        kqCrjsjLsNew.setTime8(kqCrjsjLsInDb.getTime8());
        kqCrjsjLsDao.updateAllColumnByKey(kqCrjsjLsNew);
    }

    @Override
    public KqCrjsjLsDTO getByKey(LocalDateTime date8, String kh, String mjkzqbh, LocalDateTime time8) {
        KqCrjsjLs kqCrjsjLs = Optional.ofNullable(kqCrjsjLsDao.getByKey(date8, kh, mjkzqbh, time8)).orElseGet(KqCrjsjLs::new);
        ValidationUtil.isNull(kqCrjsjLs.getDate8(), "kqCrjsjLs", "date8", date8);
        ValidationUtil.isNull(kqCrjsjLs.getKh(), "kqCrjsjLs", "kh", kh);
        ValidationUtil.isNull(kqCrjsjLs.getMjkzqbh(), "kqCrjsjLs", "mjkzqbh", mjkzqbh);
        ValidationUtil.isNull(kqCrjsjLs.getTime8(), "kqCrjsjLs", "time8", time8);
        return kqCrjsjLsMapper.toDto(kqCrjsjLs);
    }

    @Override
    public List<KqCrjsjLsDTO> listAll(KqCrjsjLsQueryCriteria criteria) {
        return kqCrjsjLsMapper.toDto(kqCrjsjLsDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(KqCrjsjLsQueryCriteria criteria, Pageable pageable) {
        Page<KqCrjsjLs> page = PageUtil.startPage(pageable);
        List<KqCrjsjLs> kqCrjsjLss = kqCrjsjLsDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(kqCrjsjLsMapper.toDto(kqCrjsjLss), page.getTotal());
    }

    @Override
    public void download(List<KqCrjsjLsDTO> kqCrjsjLsDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (KqCrjsjLsDTO kqCrjsjLsDTO : kqCrjsjLsDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("date8", kqCrjsjLsDTO.getDate8());
            map.put("id", kqCrjsjLsDTO.getId());
            map.put("syqk", kqCrjsjLsDTO.getSyqk());
            map.put("kh", kqCrjsjLsDTO.getKh());
            map.put("crjqk", kqCrjsjLsDTO.getCrjqk());
            map.put("mjjbh", kqCrjsjLsDTO.getMjjbh());
            map.put("mjkzqbh", kqCrjsjLsDTO.getMjkzqbh());
            map.put("time8", kqCrjsjLsDTO.getTime8());
            map.put("acstosql", kqCrjsjLsDTO.getAcstosql());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
