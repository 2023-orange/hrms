package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.re.dao.ReFamilyInterfaceDao;
import com.sunten.hrms.re.domain.ReFamilyInterface;
import com.sunten.hrms.re.dto.ReFamilyInterfaceDTO;
import com.sunten.hrms.re.dto.ReFamilyInterfaceQueryCriteria;
import com.sunten.hrms.re.mapper.ReFamilyInterfaceMapper;
import com.sunten.hrms.re.service.ReFamilyInterfaceService;
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
 * 家庭情况临时表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReFamilyInterfaceServiceImpl extends ServiceImpl<ReFamilyInterfaceDao, ReFamilyInterface> implements ReFamilyInterfaceService {
    private final ReFamilyInterfaceDao reFamilyInterfaceDao;
    private final ReFamilyInterfaceMapper reFamilyInterfaceMapper;

    public ReFamilyInterfaceServiceImpl(ReFamilyInterfaceDao reFamilyInterfaceDao, ReFamilyInterfaceMapper reFamilyInterfaceMapper) {
        this.reFamilyInterfaceDao = reFamilyInterfaceDao;
        this.reFamilyInterfaceMapper = reFamilyInterfaceMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReFamilyInterfaceDTO insert(ReFamilyInterface familyInterfaceNew) {
        reFamilyInterfaceDao.insertAllColumn(familyInterfaceNew);
        return reFamilyInterfaceMapper.toDto(familyInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReFamilyInterface familyInterface = new ReFamilyInterface();
        familyInterface.setId(id);
        this.delete(familyInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReFamilyInterface familyInterface) {
        // TODO    确认删除前是否需要做检查
        reFamilyInterfaceDao.deleteByEntityKey(familyInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReFamilyInterface familyInterfaceNew) {
        ReFamilyInterface familyInterfaceInDb = Optional.ofNullable(reFamilyInterfaceDao.getByKey(familyInterfaceNew.getId())).orElseGet(ReFamilyInterface::new);
        ValidationUtil.isNull(familyInterfaceInDb.getId(), "FamilyInterface", "id", familyInterfaceNew.getId());
        familyInterfaceNew.setId(familyInterfaceInDb.getId());
        reFamilyInterfaceDao.updateAllColumnByKey(familyInterfaceNew);
    }

    @Override
    public ReFamilyInterfaceDTO getByKey(Long id) {
        ReFamilyInterface familyInterface = Optional.ofNullable(reFamilyInterfaceDao.getByKey(id)).orElseGet(ReFamilyInterface::new);
        ValidationUtil.isNull(familyInterface.getId(), "FamilyInterface", "id", id);
        return reFamilyInterfaceMapper.toDto(familyInterface);
    }

    @Override
    public List<ReFamilyInterfaceDTO> listAll(ReFamilyInterfaceQueryCriteria criteria) {
        return reFamilyInterfaceMapper.toDto(reFamilyInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReFamilyInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<ReFamilyInterface> page = PageUtil.startPage(pageable);
        List<ReFamilyInterface> familyInterfaces = reFamilyInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reFamilyInterfaceMapper.toDto(familyInterfaces), page.getTotal());
    }

    @Override
    public void download(List<ReFamilyInterfaceDTO> familyInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReFamilyInterfaceDTO familyInterfaceDTO : familyInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("招骋ID", familyInterfaceDTO.getRecruitmentInterface().getId());
            map.put("姓名", familyInterfaceDTO.getName());
            map.put("关系", familyInterfaceDTO.getRelationship());
            map.put("单位", familyInterfaceDTO.getCompany());
            map.put("职务", familyInterfaceDTO.getPost());
            map.put("电话", familyInterfaceDTO.getTele());
            map.put("性别", familyInterfaceDTO.getGender());
            map.put("出生日期", familyInterfaceDTO.getBirthday());
            map.put("手机", familyInterfaceDTO.getMobilePhone());
            map.put("弹性域1", familyInterfaceDTO.getAttribute1());
            map.put("弹性域2", familyInterfaceDTO.getAttribute2());
            map.put("弹性域3", familyInterfaceDTO.getAttribute3());
            map.put("有效标记默认值", familyInterfaceDTO.getEnabledFlag());
            map.put("id", familyInterfaceDTO.getId());
            map.put("updateBy", familyInterfaceDTO.getUpdateBy());
            map.put("createTime", familyInterfaceDTO.getCreateTime());
            map.put("createBy", familyInterfaceDTO.getCreateBy());
            map.put("updateTime", familyInterfaceDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
