package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.pm.dao.PmEmployeeDao;
import com.sunten.hrms.pm.dao.PmSalesApprovalRelationsDao;
import com.sunten.hrms.pm.domain.PmLeaveApproval;
import com.sunten.hrms.pm.dao.PmLeaveApprovalDao;
import com.sunten.hrms.pm.domain.PmPermissionLeaveApproval;
import com.sunten.hrms.pm.domain.PmSalesApprovalRelations;
import com.sunten.hrms.pm.dto.*;
import com.sunten.hrms.pm.mapper.PmEmployeeMapper;
import com.sunten.hrms.pm.service.PmLeaveApprovalService;
import com.sunten.hrms.pm.mapper.PmLeaveApprovalMapper;
import com.sunten.hrms.pm.service.PmPermissionLeaveApprovalService;
import com.sunten.hrms.pm.vo.SalesAreaEtcVo;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 离职审批表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-05-07
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmLeaveApprovalServiceImpl extends ServiceImpl<PmLeaveApprovalDao, PmLeaveApproval> implements PmLeaveApprovalService {
    private final PmLeaveApprovalDao pmLeaveApprovalDao;
    private final PmLeaveApprovalMapper pmLeaveApprovalMapper;
    private final PmEmployeeDao pmEmployeeDao;
    private final PmEmployeeMapper pmEmployeeMapper;
    private final PmPermissionLeaveApprovalService pmPermissionLeaveApprovalService;
    private final PmSalesApprovalRelationsDao pmSalesApprovalRelationsDao;
    private static final Logger logger = LoggerFactory.getLogger(PmLeaveApprovalServiceImpl.class);

    public PmLeaveApprovalServiceImpl(PmLeaveApprovalDao pmLeaveApprovalDao, PmLeaveApprovalMapper pmLeaveApprovalMapper, PmPermissionLeaveApprovalService pmPermissionLeaveApprovalService,
                                      PmEmployeeDao pmEmployeeDao, PmEmployeeMapper pmEmployeeMapper, PmSalesApprovalRelationsDao pmSalesApprovalRelationsDao) {
        this.pmLeaveApprovalDao = pmLeaveApprovalDao;
        this.pmLeaveApprovalMapper = pmLeaveApprovalMapper;
        this.pmPermissionLeaveApprovalService = pmPermissionLeaveApprovalService;
        this.pmEmployeeDao = pmEmployeeDao;
        this.pmEmployeeMapper = pmEmployeeMapper;
        this.pmSalesApprovalRelationsDao = pmSalesApprovalRelationsDao;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmLeaveApprovalDTO insert(PmLeaveApproval leaveApprovalNew) {
        pmLeaveApprovalDao.insertAllColumn(leaveApprovalNew);
        return pmLeaveApprovalMapper.toDto(leaveApprovalNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmLeaveApproval leaveApproval = new PmLeaveApproval();
        leaveApproval.setId(id);
        this.delete(leaveApproval);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmLeaveApproval leaveApproval) {
        // TODO    确认删除前是否需要做检查
        pmLeaveApprovalDao.deleteByEntityKey(leaveApproval);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmLeaveApproval leaveApprovalNew) {
        PmLeaveApproval leaveApprovalInDb = Optional.ofNullable(pmLeaveApprovalDao.getByKey(leaveApprovalNew.getId())).orElseGet(PmLeaveApproval::new);
        ValidationUtil.isNull(leaveApprovalInDb.getId() ,"LeaveApproval", "id", leaveApprovalNew.getId());
        leaveApprovalNew.setId(leaveApprovalInDb.getId());
        // 检测权限关系
        if (null != leaveApprovalNew.getPmPermissionLeaveApprovals() && leaveApprovalNew.getPmPermissionLeaveApprovals().size() > 0){ // 有传
            for (PmPermissionLeaveApproval pmPermisionLeaveApproval: leaveApprovalNew.getPmPermissionLeaveApprovals()
                 ) {
                // 插入IT权限集合
                pmPermissionLeaveApprovalService.insert(pmPermisionLeaveApproval);
            }
        }
        pmLeaveApprovalDao.updateAllColumnByKey(leaveApprovalNew);
    }

    @Override
    public PmLeaveApprovalDTO getByKey(Long id) {
        PmLeaveApproval leaveApproval = Optional.ofNullable(pmLeaveApprovalDao.getByKey(id)).orElseGet(PmLeaveApproval::new);
        ValidationUtil.isNull(leaveApproval.getId() ,"LeaveApproval", "id", id);
        return pmLeaveApprovalMapper.toDto(leaveApproval);
    }

    @Override
    public List<PmLeaveApprovalDTO> listAll(PmLeaveApprovalQueryCriteria criteria) {
        return pmLeaveApprovalMapper.toDto(pmLeaveApprovalDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmLeaveApprovalQueryCriteria criteria, Pageable pageable) {
        Page<PmLeaveApproval> page = PageUtil.startPage(pageable);
        List<PmLeaveApproval> leaveApprovals = pmLeaveApprovalDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmLeaveApprovalMapper.toDto(leaveApprovals), page.getTotal());
    }

    @Override
    public void download(List<PmLeaveApprovalDTO> leaveApprovalDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmLeaveApprovalDTO leaveApprovalDTO : leaveApprovalDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("离职人员填写表单的专用判定类别", leaveApprovalDTO.getEmployeeClass());
            map.put("离职原因", leaveApprovalDTO.getLeaveReason());
            map.put("区域（销售）", leaveApprovalDTO.getDeptAddress());
            map.put("联系方式及常住地址（销售）", leaveApprovalDTO.getPhotoAndAddress());
            map.put("工作日加班工时（非销售）", leaveApprovalDTO.getWorkOvertime());
            map.put("休息日加班工时（非销售）", leaveApprovalDTO.getRestOvertime());
            map.put("调休工时（非销售）", leaveApprovalDTO.getOffHours());
            map.put("个人申请离职日期", null != leaveApprovalDTO.getSelfLeaveDate() ? DateUtil.localDateToStr(leaveApprovalDTO.getSelfLeaveDate()) : "");
            map.put("准许离职日期", null != leaveApprovalDTO.getApproveLeaveDate() ? DateUtil.localDateToStr(leaveApprovalDTO.getApproveLeaveDate()) : "");
            map.put("工卡是否上交", leaveApprovalDTO.getCardFlag() ? "是" : "否");
            map.put("未上交原因", leaveApprovalDTO.getNotSubmitReason());
            map.put("最后离职日期", null != leaveApprovalDTO.getLastLeaveDate() ? DateUtil.localDateToStr(leaveApprovalDTO.getLastLeaveDate()) : "");
            map.put("人资人事管理", leaveApprovalDTO.getPmAdmin());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disabledById(Long id) {
        pmLeaveApprovalDao.disabledById(id);
    }

    @Override
    public PmLeaveApprovalDTO getByOaOrder(String oaOrder) {
        return pmLeaveApprovalMapper.toDto(pmLeaveApprovalDao.getByOaOrder(oaOrder));
    }

    @Override
    public SalesAreaEtcVo getSalesAreaEtcByDeptId(Long deptId) {
        return pmLeaveApprovalDao.getSalesAreaEtcByDeptId(deptId);
    }

    @Override
    public List<PmEmployeeDTO> getPmsBySalesRelations(PmEmployeeQueryCriteria pmEmployeeQueryCriteria) {
        PmSalesApprovalRelationsQueryCriteria pmSalesApprovalRelationsQueryCriteria = new PmSalesApprovalRelationsQueryCriteria();
        pmSalesApprovalRelationsQueryCriteria.setEnabledFlag(true);
        List<PmSalesApprovalRelations> pmSalesApprovalRelations = pmSalesApprovalRelationsDao.listAllByCriteria(pmSalesApprovalRelationsQueryCriteria);
        Set<Long> deptIds = pmSalesApprovalRelations.stream().map(PmSalesApprovalRelations::getDeptId).collect(Collectors.toSet());
        pmEmployeeQueryCriteria.setDeptIds(deptIds);
        pmEmployeeQueryCriteria.setEnabledFlag(true);
        pmEmployeeQueryCriteria.setLeaveFlag(false);
        logger.info(pmEmployeeQueryCriteria.getNameAndWork());
        return pmEmployeeMapper.toDto(pmEmployeeDao.listAllByCriteria(pmEmployeeQueryCriteria));
    }

    @Override
    public Boolean checkIsExistLeaveInApproval(Long employeeId) {
        return pmLeaveApprovalDao.checkIsExistLeaveInApproval(employeeId);
    }

    @Override
    public List<PmLeaveApproval> listALLByStatus(String approvalStatus) {
        return pmLeaveApprovalDao.listALLByStatus(approvalStatus);
    }
}
