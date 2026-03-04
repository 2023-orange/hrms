package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.pm.domain.PmItPermissions;
import com.sunten.hrms.pm.dao.PmItPermissionsDao;
import com.sunten.hrms.pm.service.PmItPermissionsService;
import com.sunten.hrms.pm.dto.PmItPermissionsDTO;
import com.sunten.hrms.pm.dto.PmItPermissionsQueryCriteria;
import com.sunten.hrms.pm.mapper.PmItPermissionsMapper;
import com.sunten.hrms.pm.vo.PmItPermissionList;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * it权限清单 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-05-10
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmItPermissionsServiceImpl extends ServiceImpl<PmItPermissionsDao, PmItPermissions> implements PmItPermissionsService {
    @Value("${itPmPermission.sysDept}")
    private String sysDept;
    @Value("${itPmPermission.msgDept}")
    private String msgDept;
    private final PmItPermissionsDao pmItPermissionsDao;
    private final PmItPermissionsMapper pmItPermissionsMapper;

    public PmItPermissionsServiceImpl(PmItPermissionsDao pmItPermissionsDao, PmItPermissionsMapper pmItPermissionsMapper) {
        this.pmItPermissionsDao = pmItPermissionsDao;
        this.pmItPermissionsMapper = pmItPermissionsMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmItPermissionsDTO insert(PmItPermissions itPermissionsNew) {
        pmItPermissionsDao.insertAllColumn(itPermissionsNew);
        return pmItPermissionsMapper.toDto(itPermissionsNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmItPermissions itPermissions = new PmItPermissions();
        itPermissions.setId(id);
        this.delete(itPermissions);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmItPermissions itPermissions) {
        // TODO    确认删除前是否需要做检查
        pmItPermissionsDao.deleteByEntityKey(itPermissions);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmItPermissions itPermissionsNew) {
        PmItPermissions itPermissionsInDb = Optional.ofNullable(pmItPermissionsDao.getByKey(itPermissionsNew.getId())).orElseGet(PmItPermissions::new);
        ValidationUtil.isNull(itPermissionsInDb.getId() ,"ItPermissions", "id", itPermissionsNew.getId());
        itPermissionsNew.setId(itPermissionsInDb.getId());
        pmItPermissionsDao.updateAllColumnByKey(itPermissionsNew);
    }

    @Override
    public PmItPermissionsDTO getByKey(Long id) {
        PmItPermissions itPermissions = Optional.ofNullable(pmItPermissionsDao.getByKey(id)).orElseGet(PmItPermissions::new);
        ValidationUtil.isNull(itPermissions.getId() ,"ItPermissions", "id", id);
        return pmItPermissionsMapper.toDto(itPermissions);
    }

    @Override
    public List<PmItPermissionsDTO> listAll(PmItPermissionsQueryCriteria criteria) {
        return pmItPermissionsMapper.toDto(pmItPermissionsDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmItPermissionsQueryCriteria criteria, Pageable pageable) {
        Page<PmItPermissions> page = PageUtil.startPage(pageable);
        List<PmItPermissions> itPermissionss = pmItPermissionsDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmItPermissionsMapper.toDto(itPermissionss), page.getTotal());
    }

    @Override
    public void download(List<PmItPermissionsDTO> itPermissionsDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmItPermissionsDTO itPermissionsDTO : itPermissionsDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("权限负责部门id", itPermissionsDTO.getPermissionBelong());
            map.put("权限名称", itPermissionsDTO.getPermission());
            map.put("有效标记", itPermissionsDTO.getEnabledFlag());
            map.put("id", itPermissionsDTO.getId());
            map.put("创建时间", itPermissionsDTO.getCreateTime());
            map.put("创建人ID", itPermissionsDTO.getCreateBy());
            map.put("修改时间", itPermissionsDTO.getUpdateTime());
            map.put("修改人ID", itPermissionsDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public PmItPermissionList getPermissionListByBelong() {
        PmItPermissionsQueryCriteria pmItPermissionsQueryCriteria = new PmItPermissionsQueryCriteria();
        pmItPermissionsQueryCriteria.setEnabledFlag(true);
        pmItPermissionsQueryCriteria.setPermissionBelong(87L);
        PmItPermissionList pmItPermissionList = new PmItPermissionList();
        pmItPermissionList.setUseSysList(pmItPermissionsDao.listAllByCriteria(pmItPermissionsQueryCriteria).stream().map(PmItPermissions::getPermission).collect(Collectors.toList()));
        pmItPermissionsQueryCriteria.setPermissionBelong(88L);
        pmItPermissionList.setUseMsgList(pmItPermissionsDao.listAllByCriteria(pmItPermissionsQueryCriteria).stream().map(PmItPermissions::getPermission).collect(Collectors.toList()));
        return pmItPermissionList;
    }
}
