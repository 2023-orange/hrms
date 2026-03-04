package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.re.dao.ReFamilyDao;
import com.sunten.hrms.re.domain.ReFamily;
import com.sunten.hrms.re.domain.ReRecruitment;
import com.sunten.hrms.re.dto.ReFamilyDTO;
import com.sunten.hrms.re.dto.ReFamilyQueryCriteria;
import com.sunten.hrms.re.mapper.ReFamilyMapper;
import com.sunten.hrms.re.service.ReFamilyService;
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
 * 家庭情况表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReFamilyServiceImpl extends ServiceImpl<ReFamilyDao, ReFamily> implements ReFamilyService {
    private final ReFamilyDao reFamilyDao;
    private final ReFamilyMapper reFamilyMapper;

    public ReFamilyServiceImpl(ReFamilyDao reFamilyDao, ReFamilyMapper reFamilyMapper) {
        this.reFamilyDao = reFamilyDao;
        this.reFamilyMapper = reFamilyMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReFamilyDTO insert(ReFamily familyNew) {
        reFamilyDao.insertAllColumn(familyNew);
        return reFamilyMapper.toDto(familyNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReFamily family = new ReFamily();
        family.setId(id);
        family.setEnabledFlag(false);
        this.delete(family);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReFamily family) {
        //    确认删除前是否需要做检查，只失效。不删除
        reFamilyDao.updateEnableFlag(family);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReFamily familyNew) {
        ReFamily familyInDb = Optional.ofNullable(reFamilyDao.getByKey(familyNew.getId())).orElseGet(ReFamily::new);
        ValidationUtil.isNull(familyInDb.getId(), "Family", "id", familyNew.getId());
        familyNew.setId(familyInDb.getId());
        reFamilyDao.updateAllColumnByKey(familyNew);
    }

    @Override
    public ReFamilyDTO getByKey(Long id) {
        ReFamily family = Optional.ofNullable(reFamilyDao.getByKey(id)).orElseGet(ReFamily::new);
        ValidationUtil.isNull(family.getId(), "Family", "id", id);
        return reFamilyMapper.toDto(family);
    }

    @Override
    public List<ReFamilyDTO> listAll(ReFamilyQueryCriteria criteria) {
        return reFamilyMapper.toDto(reFamilyDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReFamilyQueryCriteria criteria, Pageable pageable) {
        Page<ReFamily> page = PageUtil.startPage(pageable);
        List<ReFamily> familys = reFamilyDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reFamilyMapper.toDto(familys), page.getTotal());
    }

    @Override
    public void download(List<ReFamilyDTO> familyDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReFamilyDTO familyDTO : familyDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("招骋ID", familyDTO.getRecruitment().getId());
            map.put("姓名", familyDTO.getName());
            map.put("关系", familyDTO.getRelationship());
            map.put("单位", familyDTO.getCompany());
            map.put("职务", familyDTO.getPost());
            map.put("电话", familyDTO.getTele());
            map.put("性别", familyDTO.getGender());
            map.put("出生日期", familyDTO.getBirthday());
            map.put("手机", familyDTO.getMobilePhone());
            map.put("弹性域1", familyDTO.getAttribute1());
            map.put("弹性域2", familyDTO.getAttribute2());
            map.put("弹性域3", familyDTO.getAttribute3());
            map.put("有效标记默认值", familyDTO.getEnabledFlag());
            map.put("id", familyDTO.getId());
            map.put("updateTime", familyDTO.getUpdateTime());
            map.put("createTime", familyDTO.getCreateTime());
            map.put("updateBy", familyDTO.getUpdateBy());
            map.put("createBy", familyDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<ReFamilyDTO> batchInsert(List<ReFamily> reFamilies, Long reId) {
        for (ReFamily rf : reFamilies) {
            if (reId != null) {
                if (rf.getRecruitment() == null) {
                    rf.setRecruitment(new ReRecruitment());
                }
                rf.getRecruitment().setId(reId);
            }
            if (rf.getRecruitment() == null || rf.getRecruitment().getId() == null) {
                throw new InfoCheckWarningMessException("招聘id不能为空");
            }
            if (rf.getId() != null) {
                if (rf.getId().equals(-1L)) {
                    reFamilyDao.insertAllColumn(rf);
                } else {
                    reFamilyDao.updateAllColumnByKey(rf);
                }
            } else {
                throw new InfoCheckWarningMessException("招聘模块批量新增家庭情况时id不能为空");
            }
        }
        return reFamilyMapper.toDto(reFamilies);
    }
}
