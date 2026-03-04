package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.re.dao.ReVocationalDao;
import com.sunten.hrms.re.domain.ReRecruitment;
import com.sunten.hrms.re.domain.ReVocational;
import com.sunten.hrms.re.dto.ReVocationalDTO;
import com.sunten.hrms.re.dto.ReVocationalQueryCriteria;
import com.sunten.hrms.re.mapper.ReVocationalMapper;
import com.sunten.hrms.re.service.ReVocationalService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 招聘职业资格表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2020-08-28
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReVocationalServiceImpl extends ServiceImpl<ReVocationalDao, ReVocational> implements ReVocationalService {
    private final ReVocationalDao reVocationalDao;
    private final ReVocationalMapper reVocationalMapper;

    public ReVocationalServiceImpl(ReVocationalDao reVocationalDao, ReVocationalMapper reVocationalMapper) {
        this.reVocationalDao = reVocationalDao;
        this.reVocationalMapper = reVocationalMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReVocationalDTO insert(ReVocational vocationalNew) {
        reVocationalDao.insertAllColumn(vocationalNew);
        return reVocationalMapper.toDto(vocationalNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReVocational vocational = new ReVocational();
        vocational.setId(id);
        vocational.setEnabledFlag(false);
        this.delete(vocational);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReVocational vocational) {
        //  确认删除前是否需要做检查,只失效，不删除
        reVocationalDao.updateEnableFalg(vocational);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReVocational vocationalNew) {
        ReVocational vocationalInDb = Optional.ofNullable(reVocationalDao.getByKey(vocationalNew.getId())).orElseGet(ReVocational::new);
        ValidationUtil.isNull(vocationalInDb.getId(), "Vocational", "id", vocationalNew.getId());
        vocationalNew.setId(vocationalInDb.getId());
        reVocationalDao.updateAllColumnByKey(vocationalNew);
    }

    @Override
    public ReVocationalDTO getByKey(Long id) {
        ReVocational vocational = Optional.ofNullable(reVocationalDao.getByKey(id)).orElseGet(ReVocational::new);
        ValidationUtil.isNull(vocational.getId(), "Vocational", "id", id);
        return reVocationalMapper.toDto(vocational);
    }

    @Override
    public List<ReVocationalDTO> listAll(ReVocationalQueryCriteria criteria) {
        return reVocationalMapper.toDto(reVocationalDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReVocationalQueryCriteria criteria, Pageable pageable) {
        Page<ReVocational> page = PageUtil.startPage(pageable);
        List<ReVocational> vocationals = reVocationalDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reVocationalMapper.toDto(vocationals), page.getTotal());
    }

    @Override
    public void download(List<ReVocationalDTO> vocationalDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReVocationalDTO vocationalDTO : vocationalDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("招聘id", vocationalDTO.getRecruitment().getId());
            map.put("资格名称", vocationalDTO.getVocationalName());
            map.put("级别", vocationalDTO.getVocationalLevel());
            map.put("评定时间", vocationalDTO.getEvaluationTime());
            map.put("发证机构", vocationalDTO.getEvaluationMechanism());
            map.put("有效期", vocationalDTO.getVocationalValidity());
            map.put("备注", vocationalDTO.getRemarks());
            map.put("是否当前最高", vocationalDTO.getNewVocationalFlag());
            map.put("弹性域1", vocationalDTO.getAttribute1());
            map.put("弹性域2", vocationalDTO.getAttribute2());
            map.put("弹性域3", vocationalDTO.getAttribute3());
            map.put("有效标记默认值", vocationalDTO.getEnabledFlag());
            map.put("id", vocationalDTO.getId());
            map.put("创建时间", vocationalDTO.getCreateTime());
            map.put("创建人ID", vocationalDTO.getCreateBy());
            map.put("修改时间", vocationalDTO.getUpdateTime());
            map.put("修改人ID", vocationalDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<ReVocationalDTO> batchInsert(List<ReVocational> reVocationals, Long reId) {
        for (ReVocational rv : reVocationals) {
            if (reId != null) {
                if (rv.getRecruitment() == null) {
                    rv.setRecruitment(new ReRecruitment());
                }
                rv.getRecruitment().setId(reId);
            }
            if (rv.getRecruitment() == null || rv.getRecruitment().getId() == null) {
                throw new InfoCheckWarningMessException("招聘id不能为空");
            }
            if (rv.getId() != null) {
                if (rv.getId().equals(-1L)) {
                    reVocationalDao.insertAllColumn(rv);
                } else {
                    reVocationalDao.updateAllColumnByKey(rv);
                }
            } else {
                throw new InfoCheckWarningMessException("招聘模块批量新增职业资格时id不能为空");
            }
        }
        return reVocationalMapper.toDto(reVocationals);
    }
}
