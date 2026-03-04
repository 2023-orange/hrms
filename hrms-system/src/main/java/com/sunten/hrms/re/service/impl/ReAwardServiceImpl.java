package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.re.dao.ReAwardDao;
import com.sunten.hrms.re.domain.ReAward;
import com.sunten.hrms.re.domain.ReRecruitment;
import com.sunten.hrms.re.dto.ReAwardDTO;
import com.sunten.hrms.re.dto.ReAwardQueryCriteria;
import com.sunten.hrms.re.mapper.ReAwardMapper;
import com.sunten.hrms.re.service.ReAwardService;
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
 * 奖罚情况表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReAwardServiceImpl extends ServiceImpl<ReAwardDao, ReAward> implements ReAwardService {
    private final ReAwardDao reAwardDao;
    private final ReAwardMapper reAwardMapper;

    public ReAwardServiceImpl(ReAwardDao reAwardDao, ReAwardMapper reAwardMapper) {
        this.reAwardDao = reAwardDao;
        this.reAwardMapper = reAwardMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReAwardDTO insert(ReAward awardNew) {
        reAwardDao.insertAllColumn(awardNew);
        return reAwardMapper.toDto(awardNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReAward award = new ReAward();
        award.setId(id);
        award.setEnabledFlag(false);
        this.delete(award);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReAward award) {
        //   确认删除前是否需要做检查，只失效，不删除
        reAwardDao.updateEnableFlag(award);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReAward awardNew) {
        ReAward awardInDb = Optional.ofNullable(reAwardDao.getByKey(awardNew.getId())).orElseGet(ReAward::new);
        ValidationUtil.isNull(awardInDb.getId(), "Award", "id", awardNew.getId());
        awardNew.setId(awardInDb.getId());
        reAwardDao.updateAllColumnByKey(awardNew);
    }

    @Override
    public ReAwardDTO getByKey(Long id) {
        ReAward award = Optional.ofNullable(reAwardDao.getByKey(id)).orElseGet(ReAward::new);
        ValidationUtil.isNull(award.getId(), "Award", "id", id);
        return reAwardMapper.toDto(award);
    }

    @Override
    public List<ReAwardDTO> listAll(ReAwardQueryCriteria criteria) {
        return reAwardMapper.toDto(reAwardDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReAwardQueryCriteria criteria, Pageable pageable) {
        Page<ReAward> page = PageUtil.startPage(pageable);
        List<ReAward> awards = reAwardDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reAwardMapper.toDto(awards), page.getTotal());
    }

    @Override
    public void download(List<ReAwardDTO> awardDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReAwardDTO awardDTO : awardDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("招骋ID", awardDTO.getRecruitment().getId());
            map.put("奖罚类别", awardDTO.getType());
            map.put("奖罚名称", awardDTO.getAwardName());
            map.put("奖罚处理开始时间", awardDTO.getAwardStarTime());
            map.put("奖罚处理结束时间", awardDTO.getAwardEndTime());
            map.put("奖罚单位", awardDTO.getAwardCompany());
            map.put("奖罚内容", awardDTO.getAwardContent());
            map.put("奖罚结果", awardDTO.getAwardResult());
            map.put("奖罚金额", awardDTO.getAwardMoney());
            map.put("是否有备查资料", awardDTO.getReferenceBackupFlag());
            map.put("备注", awardDTO.getRemarks());
            map.put("弹性域1", awardDTO.getAttribute1());
            map.put("弹性域2", awardDTO.getAttribute2());
            map.put("弹性域3", awardDTO.getAttribute3());
            map.put("有效标记默认值", awardDTO.getEnabledFlag());
            map.put("id", awardDTO.getId());
            map.put("updateTime", awardDTO.getUpdateTime());
            map.put("createTime", awardDTO.getCreateTime());
            map.put("updateBy", awardDTO.getUpdateBy());
            map.put("createBy", awardDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<ReAwardDTO> batchInsert(List<ReAward> reAwards, Long reId) {
        for (ReAward ra : reAwards) {
            if (reId != null) {
                if (ra.getRecruitment() == null) {
                    ra.setRecruitment(new ReRecruitment());
                }
                ra.getRecruitment().setId(reId);
            }
            if (ra.getRecruitment() == null || ra.getRecruitment().getId() == null) {
                throw new InfoCheckWarningMessException("招聘id不能为空");
            }
            if (ra.getId() != null) {
                if (ra.getId().equals(-1L)) {
                    reAwardDao.insertAllColumn(ra);
                } else {
                    reAwardDao.updateAllColumnByKey(ra);
                }
            } else {
                throw new InfoCheckWarningMessException("招聘模块批量新增奖罚情况时id不能为空");
            }
        }
        return reAwardMapper.toDto(reAwards);
    }
}
