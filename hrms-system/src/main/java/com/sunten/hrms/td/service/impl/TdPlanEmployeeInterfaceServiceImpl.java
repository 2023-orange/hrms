package com.sunten.hrms.td.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.td.domain.TdPlanEmployeeInterface;
import com.sunten.hrms.td.dao.TdPlanEmployeeInterfaceDao;
import com.sunten.hrms.td.service.TdPlanEmployeeInterfaceService;
import com.sunten.hrms.td.dto.TdPlanEmployeeInterfaceDTO;
import com.sunten.hrms.td.dto.TdPlanEmployeeInterfaceQueryCriteria;
import com.sunten.hrms.td.mapper.TdPlanEmployeeInterfaceMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;

/**
 * <p>
 * 培训参训人员接口表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-09-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdPlanEmployeeInterfaceServiceImpl extends ServiceImpl<TdPlanEmployeeInterfaceDao, TdPlanEmployeeInterface> implements TdPlanEmployeeInterfaceService {
    private final TdPlanEmployeeInterfaceDao tdPlanEmployeeInterfaceDao;
    private final TdPlanEmployeeInterfaceMapper tdPlanEmployeeInterfaceMapper;
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;
    private final FndUserService fndUserService;
    @Autowired
    private TdPlanEmployeeInterfaceService instance;

    public TdPlanEmployeeInterfaceServiceImpl(TdPlanEmployeeInterfaceDao tdPlanEmployeeInterfaceDao, TdPlanEmployeeInterfaceMapper tdPlanEmployeeInterfaceMapper,
                                              FndInterfaceOperationRecordService fndInterfaceOperationRecordService, FndUserService fndUserService) {
        this.tdPlanEmployeeInterfaceDao = tdPlanEmployeeInterfaceDao;
        this.tdPlanEmployeeInterfaceMapper = tdPlanEmployeeInterfaceMapper;
        this.fndInterfaceOperationRecordService = fndInterfaceOperationRecordService;
        this.fndUserService = fndUserService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdPlanEmployeeInterfaceDTO insert(TdPlanEmployeeInterface planEmployeeInterfaceNew) {
        tdPlanEmployeeInterfaceDao.insertAllColumn(planEmployeeInterfaceNew);
        return tdPlanEmployeeInterfaceMapper.toDto(planEmployeeInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdPlanEmployeeInterface planEmployeeInterface = new TdPlanEmployeeInterface();
        planEmployeeInterface.setId(id);
        this.delete(planEmployeeInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdPlanEmployeeInterface planEmployeeInterface) {
        // TODO    确认删除前是否需要做检查
        tdPlanEmployeeInterfaceDao.deleteByEntityKey(planEmployeeInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdPlanEmployeeInterface planEmployeeInterfaceNew) {
        TdPlanEmployeeInterface planEmployeeInterfaceInDb = Optional.ofNullable(tdPlanEmployeeInterfaceDao.getByKey(planEmployeeInterfaceNew.getId())).orElseGet(TdPlanEmployeeInterface::new);
        ValidationUtil.isNull(planEmployeeInterfaceInDb.getId() ,"PlanEmployeeInterface", "id", planEmployeeInterfaceNew.getId());
        planEmployeeInterfaceNew.setId(planEmployeeInterfaceInDb.getId());
        tdPlanEmployeeInterfaceDao.updateAllColumnByKey(planEmployeeInterfaceNew);
    }

    @Override
    public TdPlanEmployeeInterfaceDTO getByKey(Long id) {
        TdPlanEmployeeInterface planEmployeeInterface = Optional.ofNullable(tdPlanEmployeeInterfaceDao.getByKey(id)).orElseGet(TdPlanEmployeeInterface::new);
        ValidationUtil.isNull(planEmployeeInterface.getId() ,"PlanEmployeeInterface", "id", id);
        return tdPlanEmployeeInterfaceMapper.toDto(planEmployeeInterface);
    }

    @Override
    public List<TdPlanEmployeeInterfaceDTO> listAll(TdPlanEmployeeInterfaceQueryCriteria criteria) {
        return tdPlanEmployeeInterfaceMapper.toDto(tdPlanEmployeeInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdPlanEmployeeInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<TdPlanEmployeeInterface> page = PageUtil.startPage(pageable);
        List<TdPlanEmployeeInterface> planEmployeeInterfaces = tdPlanEmployeeInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdPlanEmployeeInterfaceMapper.toDto(planEmployeeInterfaces), page.getTotal());
    }

    @Override
    public void download(List<TdPlanEmployeeInterfaceDTO> planEmployeeInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdPlanEmployeeInterfaceDTO planEmployeeInterfaceDTO : planEmployeeInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("数据分组id", planEmployeeInterfaceDTO.getGroupId());
            map.put("操作码", planEmployeeInterfaceDTO.getOperationCode());
            map.put("错误信息", planEmployeeInterfaceDTO.getErrorMsg());
            map.put("数据状态", planEmployeeInterfaceDTO.getDataStatus());
            map.put("姓名", planEmployeeInterfaceDTO.getName());
            map.put("工牌号", planEmployeeInterfaceDTO.getWorkCard());
            map.put("id", planEmployeeInterfaceDTO.getId());
            map.put("创建时间", planEmployeeInterfaceDTO.getCreateTime());
            map.put("创建人ID", planEmployeeInterfaceDTO.getCreateBy());
            map.put("修改时间", planEmployeeInterfaceDTO.getUpdateTime());
            map.put("修改人ID", planEmployeeInterfaceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<TdPlanEmployeeInterfaceDTO> insertExcel(List<TdPlanEmployeeInterface> tdPlanEmployeeInterfaces) {
        if (null == tdPlanEmployeeInterfaces || tdPlanEmployeeInterfaces.size() <= 0) {
            throw new InfoCheckWarningMessException("Excel无内容， 不允许导入");
        }
        FndInterfaceOperationRecord fndInterfaceOperationRecord = new FndInterfaceOperationRecord();
        fndInterfaceOperationRecord.setOperationValue("insertTdPlanEmployee");
        fndInterfaceOperationRecord.setOperationDescription("参训人员导入");
        fndInterfaceOperationRecordService.insert(fndInterfaceOperationRecord);
        Long tempGroupId = fndInterfaceOperationRecord.getId();
        try {
            FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
            instance.insertMainAndSon(tdPlanEmployeeInterfaces, fndInterfaceOperationRecord.getId(), user.getId());
        } catch (Exception e) {
            fndInterfaceOperationRecord.setDataProcessingDescription(ThrowableUtil.getStackTrace(e));
            fndInterfaceOperationRecord.setSuccessFlag(false);
            fndInterfaceOperationRecordService.update(fndInterfaceOperationRecord);
            throw new InfoCheckWarningMessException("导入失败，请联系管理员");
        } finally {
            fndInterfaceOperationRecord.setDataProcessingDescription("导入正常");
            fndInterfaceOperationRecord.setSuccessFlag(true);
            fndInterfaceOperationRecordService.update(fndInterfaceOperationRecord);
        }
        TdPlanEmployeeInterfaceQueryCriteria tdPlanEmployeeInterfaceQueryCriteria = new TdPlanEmployeeInterfaceQueryCriteria();
        tdPlanEmployeeInterfaceQueryCriteria.setGroupId(tempGroupId);
        return tdPlanEmployeeInterfaceMapper.toDto(tdPlanEmployeeInterfaceDao.listAllByCriteria(tdPlanEmployeeInterfaceQueryCriteria));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void insertMainAndSon(List<TdPlanEmployeeInterface> tdPlanEmployeeInterfaces, Long groupId, Long userId) {
        for (TdPlanEmployeeInterface tdPlanEmployeeInterface : tdPlanEmployeeInterfaces
             ) {
            tdPlanEmployeeInterface.setCreateBy(userId);
            tdPlanEmployeeInterface.setUpdateBy(userId);
            tdPlanEmployeeInterface.setGroupId(groupId);
            tdPlanEmployeeInterfaceDao.insertByInterface(tdPlanEmployeeInterface);
        }
    }
}
