package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.re.dao.ReEducationInterfaceDao;
import com.sunten.hrms.re.domain.ReEducationInterface;
import com.sunten.hrms.re.dto.ReEducationInterfaceDTO;
import com.sunten.hrms.re.dto.ReEducationInterfaceQueryCriteria;
import com.sunten.hrms.re.mapper.ReEducationInterfaceMapper;
import com.sunten.hrms.re.service.ReEducationInterfaceService;
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
 * 受教育经历临时表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReEducationInterfaceServiceImpl extends ServiceImpl<ReEducationInterfaceDao, ReEducationInterface> implements ReEducationInterfaceService {
    private final ReEducationInterfaceDao reEducationInterfaceDao;
    private final ReEducationInterfaceMapper reEducationInterfaceMapper;

    public ReEducationInterfaceServiceImpl(ReEducationInterfaceDao reEducationInterfaceDao, ReEducationInterfaceMapper reEducationInterfaceMapper) {
        this.reEducationInterfaceDao = reEducationInterfaceDao;
        this.reEducationInterfaceMapper = reEducationInterfaceMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReEducationInterfaceDTO insert(ReEducationInterface educationInterfaceNew) {
        reEducationInterfaceDao.insertAllColumn(educationInterfaceNew);
        return reEducationInterfaceMapper.toDto(educationInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReEducationInterface educationInterface = new ReEducationInterface();
        educationInterface.setId(id);
        this.delete(educationInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReEducationInterface educationInterface) {
        // TODO    确认删除前是否需要做检查
        reEducationInterfaceDao.deleteByEntityKey(educationInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReEducationInterface educationInterfaceNew) {
        ReEducationInterface educationInterfaceInDb = Optional.ofNullable(reEducationInterfaceDao.getByKey(educationInterfaceNew.getId())).orElseGet(ReEducationInterface::new);
        ValidationUtil.isNull(educationInterfaceInDb.getId(), "EducationInterface", "id", educationInterfaceNew.getId());
        educationInterfaceNew.setId(educationInterfaceInDb.getId());
        reEducationInterfaceDao.updateAllColumnByKey(educationInterfaceNew);
    }

    @Override
    public ReEducationInterfaceDTO getByKey(Long id) {
        ReEducationInterface educationInterface = Optional.ofNullable(reEducationInterfaceDao.getByKey(id)).orElseGet(ReEducationInterface::new);
        ValidationUtil.isNull(educationInterface.getId(), "EducationInterface", "id", id);
        return reEducationInterfaceMapper.toDto(educationInterface);
    }

    @Override
    public List<ReEducationInterfaceDTO> listAll(ReEducationInterfaceQueryCriteria criteria) {
        return reEducationInterfaceMapper.toDto(reEducationInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReEducationInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<ReEducationInterface> page = PageUtil.startPage(pageable);
        List<ReEducationInterface> educationInterfaces = reEducationInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reEducationInterfaceMapper.toDto(educationInterfaces), page.getTotal());
    }

    @Override
    public void download(List<ReEducationInterfaceDTO> educationInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReEducationInterfaceDTO educationInterfaceDTO : educationInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("招骋ID", educationInterfaceDTO.getRecruitmentInterface().getId());
            map.put("毕业学校", educationInterfaceDTO.getSchool());
            map.put("学历", educationInterfaceDTO.getEducation());
            map.put("入学时间", educationInterfaceDTO.getEnrollmentTime());
            map.put("毕业时间", educationInterfaceDTO.getGraduationTime());
            map.put("是否最高学历", educationInterfaceDTO.getNewEducationFlag());
            map.put("专业", educationInterfaceDTO.getSpecializedSubject());
            map.put("入学性质", educationInterfaceDTO.getEnrollment());
            map.put("弹性域1", educationInterfaceDTO.getAttribute1());
            map.put("弹性域2", educationInterfaceDTO.getAttribute2());
            map.put("弹性域3", educationInterfaceDTO.getAttribute3());
            map.put("有效标记默认值", educationInterfaceDTO.getEnabledFlag());
            map.put("id", educationInterfaceDTO.getId());
            map.put("createBy", educationInterfaceDTO.getCreateBy());
            map.put("updateTime", educationInterfaceDTO.getUpdateTime());
            map.put("updateBy", educationInterfaceDTO.getUpdateBy());
            map.put("createTime", educationInterfaceDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
