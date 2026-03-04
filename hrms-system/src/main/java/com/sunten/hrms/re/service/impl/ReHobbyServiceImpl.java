package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.re.dao.ReHobbyDao;
import com.sunten.hrms.re.domain.ReHobby;
import com.sunten.hrms.re.domain.ReRecruitment;
import com.sunten.hrms.re.dto.ReHobbyDTO;
import com.sunten.hrms.re.dto.ReHobbyQueryCriteria;
import com.sunten.hrms.re.mapper.ReHobbyMapper;
import com.sunten.hrms.re.service.ReHobbyService;
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
 * 技术爱好表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReHobbyServiceImpl extends ServiceImpl<ReHobbyDao, ReHobby> implements ReHobbyService {
    private final ReHobbyDao reHobbyDao;
    private final ReHobbyMapper reHobbyMapper;

    public ReHobbyServiceImpl(ReHobbyDao reHobbyDao, ReHobbyMapper reHobbyMapper) {
        this.reHobbyDao = reHobbyDao;
        this.reHobbyMapper = reHobbyMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReHobbyDTO insert(ReHobby hobbyNew) {
        reHobbyDao.insertAllColumn(hobbyNew);
        return reHobbyMapper.toDto(hobbyNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReHobby hobby = new ReHobby();
        hobby.setId(id);
        hobby.setEnabledFlag(false);
        this.delete(hobby);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReHobby hobby) {
        // 确认删除前是否需要做检查，只失效，不删除
        reHobbyDao.updateEnableFlag(hobby);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReHobby hobbyNew) {
        ReHobby hobbyInDb = Optional.ofNullable(reHobbyDao.getByKey(hobbyNew.getId())).orElseGet(ReHobby::new);
        ValidationUtil.isNull(hobbyInDb.getId(), "Hobby", "id", hobbyNew.getId());
        hobbyNew.setId(hobbyInDb.getId());
        reHobbyDao.updateAllColumnByKey(hobbyNew);
    }

    @Override
    public ReHobbyDTO getByKey(Long id) {
        ReHobby hobby = Optional.ofNullable(reHobbyDao.getByKey(id)).orElseGet(ReHobby::new);
        ValidationUtil.isNull(hobby.getId(), "Hobby", "id", id);
        return reHobbyMapper.toDto(hobby);
    }

    @Override
    public List<ReHobbyDTO> listAll(ReHobbyQueryCriteria criteria) {
        return reHobbyMapper.toDto(reHobbyDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReHobbyQueryCriteria criteria, Pageable pageable) {
        Page<ReHobby> page = PageUtil.startPage(pageable);
        List<ReHobby> hobbys = reHobbyDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reHobbyMapper.toDto(hobbys), page.getTotal());
    }

    @Override
    public void download(List<ReHobbyDTO> hobbyDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReHobbyDTO hobbyDTO : hobbyDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("招骋ID", hobbyDTO.getRecruitment().getId());
            map.put("技能爱好", hobbyDTO.getHobby());
            map.put("级别", hobbyDTO.getLevelMyself());
            map.put("认证等级", hobbyDTO.getLevelMechanism());
            map.put("备注", hobbyDTO.getRemarks());
            map.put("弹性域1", hobbyDTO.getAttribute1());
            map.put("弹性域2", hobbyDTO.getAttribute2());
            map.put("弹性域3", hobbyDTO.getAttribute3());
            map.put("有效标记默认值", hobbyDTO.getEnabledFlag());
            map.put("id", hobbyDTO.getId());
            map.put("updateTime", hobbyDTO.getUpdateTime());
            map.put("createTime", hobbyDTO.getCreateTime());
            map.put("updateBy", hobbyDTO.getUpdateBy());
            map.put("createBy", hobbyDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<ReHobbyDTO> batchInsert(List<ReHobby> reHobbies, Long reId) {
        for (ReHobby rh : reHobbies) {
            if (reId != null) {
                if (rh.getRecruitment() == null) {
                    rh.setRecruitment(new ReRecruitment());
                }
                rh.getRecruitment().setId(reId);
            }
            if (rh.getRecruitment() == null || rh.getRecruitment().getId() == null) {
                throw new InfoCheckWarningMessException("招聘id不能为空");
            }
            if (rh.getId() != null) {
                if (rh.getId().equals(-1L)) {
                    reHobbyDao.insertAllColumn(rh);
                } else {
                    reHobbyDao.updateAllColumnByKey(rh);
                }
            } else {
                throw new InfoCheckWarningMessException("招聘模块技术爱好批量新增时ID不能为空");
            }
        }
        return reHobbyMapper.toDto(reHobbies);
    }
}
