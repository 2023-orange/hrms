package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.re.dao.ReEducationDao;
import com.sunten.hrms.re.domain.ReEducation;
import com.sunten.hrms.re.domain.ReRecruitment;
import com.sunten.hrms.re.dto.ReEducationDTO;
import com.sunten.hrms.re.dto.ReEducationQueryCriteria;
import com.sunten.hrms.re.mapper.ReEducationMapper;
import com.sunten.hrms.re.service.ReEducationService;
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
 * 受教育经历表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReEducationServiceImpl extends ServiceImpl<ReEducationDao, ReEducation> implements ReEducationService {
    private final ReEducationDao reEducationDao;
    private final ReEducationMapper reEducationMapper;

    public ReEducationServiceImpl(ReEducationDao reEducationDao, ReEducationMapper reEducationMapper) {
        this.reEducationDao = reEducationDao;
        this.reEducationMapper = reEducationMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReEducationDTO insert(ReEducation educationNew) {
        reEducationDao.insertAllColumn(educationNew);
        return reEducationMapper.toDto(educationNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReEducation education = new ReEducation();
        education.setId(id);
        education.setEnabledFlag(false);
        this.delete(education);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReEducation education) {
        //   确认删除前是否需要做检查，只失效，不删除
        reEducationDao.updateEnableFlag(education);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReEducation educationNew) {
        ReEducation educationInDb = Optional.ofNullable(reEducationDao.getByKey(educationNew.getId())).orElseGet(ReEducation::new);
        ValidationUtil.isNull(educationInDb.getId(), "Education", "id", educationNew.getId());
        educationNew.setId(educationInDb.getId());
        reEducationDao.updateAllColumnByKey(educationNew);
    }

    @Override
    public ReEducationDTO getByKey(Long id) {
        ReEducation education = Optional.ofNullable(reEducationDao.getByKey(id)).orElseGet(ReEducation::new);
        ValidationUtil.isNull(education.getId(), "Education", "id", id);
        return reEducationMapper.toDto(education);
    }

    @Override
    public List<ReEducationDTO> listAll(ReEducationQueryCriteria criteria) {
        return reEducationMapper.toDto(reEducationDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReEducationQueryCriteria criteria, Pageable pageable) {
        Page<ReEducation> page = PageUtil.startPage(pageable);
        List<ReEducation> educations = reEducationDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reEducationMapper.toDto(educations), page.getTotal());
    }

    @Override
    public void download(List<ReEducationDTO> educationDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReEducationDTO educationDTO : educationDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("招骋ID", educationDTO.getRecruitment().getId());
            map.put("毕业学校", educationDTO.getSchool());
            map.put("学历", educationDTO.getEducation());
            map.put("入学时间", educationDTO.getEnrollmentTime());
            map.put("毕业时间", educationDTO.getGraduationTime());
            map.put("是否最高学历", educationDTO.getNewEducationFlag());
            map.put("专业", educationDTO.getSpecializedSubject());
            map.put("入学性质", educationDTO.getEnrollment());
            map.put("弹性域1", educationDTO.getAttribute1());
            map.put("弹性域2", educationDTO.getAttribute2());
            map.put("弹性域3", educationDTO.getAttribute3());
            map.put("有效标记默认值", educationDTO.getEnabledFlag());
            map.put("id", educationDTO.getId());
            map.put("updateBy", educationDTO.getUpdateBy());
            map.put("createTime", educationDTO.getCreateTime());
            map.put("updateTime", educationDTO.getUpdateTime());
            map.put("createBy", educationDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<ReEducationDTO> batchInsert(List<ReEducation> reEducations, Long reId) {
        for (ReEducation re : reEducations) {
            if (reId != null) {
                if (re.getRecruitment() == null) {
                    re.setRecruitment(new ReRecruitment());
                }
                re.getRecruitment().setId(reId);
            }
            if (re.getRecruitment() == null || re.getRecruitment().getId() == null) {
                throw new InfoCheckWarningMessException("招聘id不能为空");
            }
            if (re.getId() != null) {
                if (re.getId().equals(-1L)) {
                    reEducationDao.insertAllColumn(re);
                } else {
                    reEducationDao.updateAllColumnByKey(re);
                }
            } else {
                throw new InfoCheckWarningMessException("招聘模块批量新增学历时id不能为空");
            }
        }
        return reEducationMapper.toDto(reEducations);
    }
}
