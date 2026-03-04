package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.pm.domain.PmPermissionLeaveApproval;
import com.sunten.hrms.pm.dao.PmPermissionLeaveApprovalDao;
import com.sunten.hrms.pm.service.PmPermissionLeaveApprovalService;
import com.sunten.hrms.pm.dto.PmPermissionLeaveApprovalDTO;
import com.sunten.hrms.pm.dto.PmPermissionLeaveApprovalQueryCriteria;
import com.sunten.hrms.pm.mapper.PmPermissionLeaveApprovalMapper;
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

/**
 * <p>
 * 离职申请与IT权限关联表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-05-10
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmPermissionLeaveApprovalServiceImpl extends ServiceImpl<PmPermissionLeaveApprovalDao, PmPermissionLeaveApproval> implements PmPermissionLeaveApprovalService {
    @Value("${itPmPermission.sysDept}")
    private String sysDept;
    @Value("${itPmPermission.msgDept}")
    private String msgDept;
    private final PmPermissionLeaveApprovalDao pmPermissionLeaveApprovalDao;
    private final PmPermissionLeaveApprovalMapper pmPermissionLeaveApprovalMapper;

    public PmPermissionLeaveApprovalServiceImpl(PmPermissionLeaveApprovalDao pmPermissionLeaveApprovalDao, PmPermissionLeaveApprovalMapper pmPermissionLeaveApprovalMapper) {
        this.pmPermissionLeaveApprovalDao = pmPermissionLeaveApprovalDao;
        this.pmPermissionLeaveApprovalMapper = pmPermissionLeaveApprovalMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmPermissionLeaveApprovalDTO insert(PmPermissionLeaveApproval permissionLeaveApprovalNew) {
        pmPermissionLeaveApprovalDao.insertAllColumn(permissionLeaveApprovalNew);
        return pmPermissionLeaveApprovalMapper.toDto(permissionLeaveApprovalNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmPermissionLeaveApproval permissionLeaveApproval = new PmPermissionLeaveApproval();
        permissionLeaveApproval.setId(id);
        this.delete(permissionLeaveApproval);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmPermissionLeaveApproval permissionLeaveApproval) {
        // TODO    确认删除前是否需要做检查
        pmPermissionLeaveApprovalDao.deleteByEntityKey(permissionLeaveApproval);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmPermissionLeaveApproval permissionLeaveApprovalNew) {
        PmPermissionLeaveApproval permissionLeaveApprovalInDb = Optional.ofNullable(pmPermissionLeaveApprovalDao.getByKey(permissionLeaveApprovalNew.getId())).orElseGet(PmPermissionLeaveApproval::new);
        ValidationUtil.isNull(permissionLeaveApprovalInDb.getId() ,"PermissionLeaveApproval", "id", permissionLeaveApprovalNew.getId());
        permissionLeaveApprovalNew.setId(permissionLeaveApprovalInDb.getId());
        pmPermissionLeaveApprovalDao.updateAllColumnByKey(permissionLeaveApprovalNew);
    }

    @Override
    public PmPermissionLeaveApprovalDTO getByKey(Long id) {
        PmPermissionLeaveApproval permissionLeaveApproval = Optional.ofNullable(pmPermissionLeaveApprovalDao.getByKey(id)).orElseGet(PmPermissionLeaveApproval::new);
        ValidationUtil.isNull(permissionLeaveApproval.getId() ,"PermissionLeaveApproval", "id", id);
        return pmPermissionLeaveApprovalMapper.toDto(permissionLeaveApproval);
    }

    @Override
    public List<PmPermissionLeaveApprovalDTO> listAll(PmPermissionLeaveApprovalQueryCriteria criteria) {
        return pmPermissionLeaveApprovalMapper.toDto(pmPermissionLeaveApprovalDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmPermissionLeaveApprovalQueryCriteria criteria, Pageable pageable) {
        Page<PmPermissionLeaveApproval> page = PageUtil.startPage(pageable);
        List<PmPermissionLeaveApproval> permissionLeaveApprovals = pmPermissionLeaveApprovalDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmPermissionLeaveApprovalMapper.toDto(permissionLeaveApprovals), page.getTotal());
    }

    @Override
    public void download(List<PmPermissionLeaveApprovalDTO> permissionLeaveApprovalDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmPermissionLeaveApprovalDTO permissionLeaveApprovalDTO : permissionLeaveApprovalDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("离职审批id", permissionLeaveApprovalDTO.getLeaveApprovalId());
            map.put("IT权限id", permissionLeaveApprovalDTO.getPermissionId());
            map.put("停用日期", permissionLeaveApprovalDTO.getStopDate());
            map.put("有效标记", permissionLeaveApprovalDTO.getEnabledFlag());
            map.put("id", permissionLeaveApprovalDTO.getId());
            map.put("创建时间", permissionLeaveApprovalDTO.getCreateTime());
            map.put("创建人ID", permissionLeaveApprovalDTO.getCreateBy());
            map.put("修改时间", permissionLeaveApprovalDTO.getUpdateTime());
            map.put("修改人ID", permissionLeaveApprovalDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public PmItPermissionList getPermissionList(PmPermissionLeaveApprovalQueryCriteria criteria) {
        criteria.setBelongType(sysDept);
        List<PmPermissionLeaveApproval> pmPermissionLeaveApprovalSysList = pmPermissionLeaveApprovalDao.listAllByCriteria(criteria);
        criteria.setBelongType(msgDept);
        List<PmPermissionLeaveApproval> pmPermissionLeaveApprovalMsgList = pmPermissionLeaveApprovalDao.listAllByCriteria(criteria);
        List<String> sysList = new ArrayList<>();
        List<String> msgList = new ArrayList<>();
        pmPermissionLeaveApprovalSysList.forEach(pmPermissionLeaveApproval -> {
                sysList.add(pmPermissionLeaveApproval.getPmItPermissions().getPermission() + "(" + DateUtil.localDateToStr(pmPermissionLeaveApproval.getStopDate()) + ")");
        });
        pmPermissionLeaveApprovalMsgList.forEach(pmPermissionLeaveApproval -> {
            msgList.add(pmPermissionLeaveApproval.getPmItPermissions().getPermission() + "(" + DateUtil.localDateToStr(pmPermissionLeaveApproval.getStopDate()) + ")");
        });
        PmItPermissionList pmItPermissionList = new PmItPermissionList();
        pmItPermissionList.setUseMsgList(msgList);
        pmItPermissionList.setUseSysList(sysList);
        return pmItPermissionList;
    }

}
