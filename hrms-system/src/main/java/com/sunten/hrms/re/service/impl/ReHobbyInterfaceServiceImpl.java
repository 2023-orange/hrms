package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.re.dao.ReHobbyInterfaceDao;
import com.sunten.hrms.re.domain.ReHobbyInterface;
import com.sunten.hrms.re.dto.ReHobbyInterfaceDTO;
import com.sunten.hrms.re.dto.ReHobbyInterfaceQueryCriteria;
import com.sunten.hrms.re.mapper.ReHobbyInterfaceMapper;
import com.sunten.hrms.re.service.ReHobbyInterfaceService;
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
 * 技术爱好临时表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReHobbyInterfaceServiceImpl extends ServiceImpl<ReHobbyInterfaceDao, ReHobbyInterface> implements ReHobbyInterfaceService {
    private final ReHobbyInterfaceDao reHobbyInterfaceDao;
    private final ReHobbyInterfaceMapper reHobbyInterfaceMapper;

    public ReHobbyInterfaceServiceImpl(ReHobbyInterfaceDao reHobbyInterfaceDao, ReHobbyInterfaceMapper reHobbyInterfaceMapper) {
        this.reHobbyInterfaceDao = reHobbyInterfaceDao;
        this.reHobbyInterfaceMapper = reHobbyInterfaceMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReHobbyInterfaceDTO insert(ReHobbyInterface hobbyInterfaceNew) {
        reHobbyInterfaceDao.insertAllColumn(hobbyInterfaceNew);
        return reHobbyInterfaceMapper.toDto(hobbyInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReHobbyInterface hobbyInterface = new ReHobbyInterface();
        hobbyInterface.setId(id);
        this.delete(hobbyInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReHobbyInterface hobbyInterface) {
        // TODO    确认删除前是否需要做检查
        reHobbyInterfaceDao.deleteByEntityKey(hobbyInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReHobbyInterface hobbyInterfaceNew) {
        ReHobbyInterface hobbyInterfaceInDb = Optional.ofNullable(reHobbyInterfaceDao.getByKey(hobbyInterfaceNew.getId())).orElseGet(ReHobbyInterface::new);
        ValidationUtil.isNull(hobbyInterfaceInDb.getId(), "HobbyInterface", "id", hobbyInterfaceNew.getId());
        hobbyInterfaceNew.setId(hobbyInterfaceInDb.getId());
        reHobbyInterfaceDao.updateAllColumnByKey(hobbyInterfaceNew);
    }

    @Override
    public ReHobbyInterfaceDTO getByKey(Long id) {
        ReHobbyInterface hobbyInterface = Optional.ofNullable(reHobbyInterfaceDao.getByKey(id)).orElseGet(ReHobbyInterface::new);
        ValidationUtil.isNull(hobbyInterface.getId(), "HobbyInterface", "id", id);
        return reHobbyInterfaceMapper.toDto(hobbyInterface);
    }

    @Override
    public List<ReHobbyInterfaceDTO> listAll(ReHobbyInterfaceQueryCriteria criteria) {
        return reHobbyInterfaceMapper.toDto(reHobbyInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReHobbyInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<ReHobbyInterface> page = PageUtil.startPage(pageable);
        List<ReHobbyInterface> hobbyInterfaces = reHobbyInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reHobbyInterfaceMapper.toDto(hobbyInterfaces), page.getTotal());
    }

    @Override
    public void download(List<ReHobbyInterfaceDTO> hobbyInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReHobbyInterfaceDTO hobbyInterfaceDTO : hobbyInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("招骋ID", hobbyInterfaceDTO.getRecruitmentInterface().getId());
            map.put("技能爱好", hobbyInterfaceDTO.getHobby());
            map.put("级别", hobbyInterfaceDTO.getLevelMyself());
            map.put("认证等级", hobbyInterfaceDTO.getLevelMechanism());
            map.put("备注", hobbyInterfaceDTO.getRemarks());
            map.put("弹性域1", hobbyInterfaceDTO.getAttribute1());
            map.put("弹性域2", hobbyInterfaceDTO.getAttribute2());
            map.put("弹性域3", hobbyInterfaceDTO.getAttribute3());
            map.put("有效标记默认值", hobbyInterfaceDTO.getEnabledFlag());
            map.put("id", hobbyInterfaceDTO.getId());
            map.put("updateTime", hobbyInterfaceDTO.getUpdateTime());
            map.put("createBy", hobbyInterfaceDTO.getCreateBy());
            map.put("createTime", hobbyInterfaceDTO.getCreateTime());
            map.put("updateBy", hobbyInterfaceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
