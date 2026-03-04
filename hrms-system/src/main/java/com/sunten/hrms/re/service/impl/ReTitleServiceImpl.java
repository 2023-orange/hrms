package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.re.dao.ReTitleDao;
import com.sunten.hrms.re.domain.ReRecruitment;
import com.sunten.hrms.re.domain.ReTitle;
import com.sunten.hrms.re.dto.ReTitleDTO;
import com.sunten.hrms.re.dto.ReTitleQueryCriteria;
import com.sunten.hrms.re.mapper.ReTitleMapper;
import com.sunten.hrms.re.service.ReTitleService;
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
 * 职称情况表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReTitleServiceImpl extends ServiceImpl<ReTitleDao, ReTitle> implements ReTitleService {
    private final ReTitleDao reTitleDao;
    private final ReTitleMapper reTitleMapper;

    public ReTitleServiceImpl(ReTitleDao reTitleDao, ReTitleMapper reTitleMapper) {
        this.reTitleDao = reTitleDao;
        this.reTitleMapper = reTitleMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReTitleDTO insert(ReTitle titleNew) {
        reTitleDao.insertAllColumn(titleNew);
        return reTitleMapper.toDto(titleNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReTitle title = new ReTitle();
        title.setId(id);
        title.setEnabledFlag(false);
        this.delete(title);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReTitle title) {
        //  确认删除前是否需要做检查，只失效，不删除
        reTitleDao.updateEnableFlag(title);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReTitle titleNew) {
        ReTitle titleInDb = Optional.ofNullable(reTitleDao.getByKey(titleNew.getId())).orElseGet(ReTitle::new);
        ValidationUtil.isNull(titleInDb.getId(), "Title", "id", titleNew.getId());
        titleNew.setId(titleInDb.getId());
        reTitleDao.updateAllColumnByKey(titleNew);
    }

    @Override
    public ReTitleDTO getByKey(Long id) {
        ReTitle title = Optional.ofNullable(reTitleDao.getByKey(id)).orElseGet(ReTitle::new);
        ValidationUtil.isNull(title.getId(), "Title", "id", id);
        return reTitleMapper.toDto(title);
    }

    @Override
    public List<ReTitleDTO> listAll(ReTitleQueryCriteria criteria) {
        return reTitleMapper.toDto(reTitleDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReTitleQueryCriteria criteria, Pageable pageable) {
        Page<ReTitle> page = PageUtil.startPage(pageable);
        List<ReTitle> titles = reTitleDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reTitleMapper.toDto(titles), page.getTotal());
    }

    @Override
    public void download(List<ReTitleDTO> titleDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReTitleDTO titleDTO : titleDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("招骋ID", titleDTO.getRecruitment().getId());
            map.put("职称级别", titleDTO.getTitleLevel());
            map.put("职称名称", titleDTO.getTitleName());
            map.put("评定时间", titleDTO.getEvaluationTime());
            map.put("是否最高职称", titleDTO.getNewTitleFlag());
            map.put("弹性域1", titleDTO.getAttribute1());
            map.put("弹性域2", titleDTO.getAttribute2());
            map.put("弹性域3", titleDTO.getAttribute3());
            map.put("有效标记默认值", titleDTO.getEnabledFlag());
            map.put("id", titleDTO.getId());
            map.put("updateBy", titleDTO.getUpdateBy());
            map.put("createTime", titleDTO.getCreateTime());
            map.put("createBy", titleDTO.getCreateBy());
            map.put("updateTime", titleDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<ReTitleDTO> batchInsert(List<ReTitle> reTitles, Long reId) {
        for (ReTitle rt : reTitles) {
            if (reId != null) {
                if (rt.getRecruitment() == null) {
                    rt.setRecruitment(new ReRecruitment());
                }
                rt.getRecruitment().setId(reId);
            }
            if (rt.getRecruitment() == null || rt.getRecruitment().getId() == null) {
                throw new InfoCheckWarningMessException("招聘id不能为空");
            }
            if (rt.getId() != null) {
                if (rt.getId().equals(-1L)) {
                    reTitleDao.insertAllColumn(rt);
                } else {
                    reTitleDao.updateAllColumnByKey(rt);
                }
            } else {
                throw new InfoCheckWarningMessException("招聘模块批量新增职称时id不能为空");
            }
        }
        return reTitleMapper.toDto(reTitles);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertByTempInterface(Long groupId) {
        reTitleDao.insertByTempInterface(groupId);
    }
}
