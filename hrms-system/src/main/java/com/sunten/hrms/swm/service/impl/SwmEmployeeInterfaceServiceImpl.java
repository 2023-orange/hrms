package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.dto.FndInterfaceOperationRecordDTO;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.domain.SwmEmployeeInterface;
import com.sunten.hrms.swm.dao.SwmEmployeeInterfaceDao;
import com.sunten.hrms.swm.service.SwmEmployeeInterfaceService;
import com.sunten.hrms.swm.dto.SwmEmployeeInterfaceDTO;
import com.sunten.hrms.swm.dto.SwmEmployeeInterfaceQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmEmployeeInterfaceMapper;
import com.sunten.hrms.swm.service.SwmEmployeeService;
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
 * 薪酬员工信息接口表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-09-13
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmEmployeeInterfaceServiceImpl extends ServiceImpl<SwmEmployeeInterfaceDao, SwmEmployeeInterface> implements SwmEmployeeInterfaceService {
    private final SwmEmployeeInterfaceDao swmEmployeeInterfaceDao;
    private final SwmEmployeeInterfaceMapper swmEmployeeInterfaceMapper;
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;
    private final FndUserService fndUserService;
    private final SwmEmployeeService swmEmployeeService;
    @Autowired
    private SwmEmployeeInterfaceService instance;

    public SwmEmployeeInterfaceServiceImpl(SwmEmployeeInterfaceDao swmEmployeeInterfaceDao, SwmEmployeeInterfaceMapper swmEmployeeInterfaceMapper,
                                           FndInterfaceOperationRecordService fndInterfaceOperationRecordService, FndUserService fndUserService,
                                           SwmEmployeeService swmEmployeeService) {
        this.swmEmployeeInterfaceDao = swmEmployeeInterfaceDao;
        this.swmEmployeeInterfaceMapper = swmEmployeeInterfaceMapper;
        this.fndInterfaceOperationRecordService = fndInterfaceOperationRecordService;
        this.fndUserService = fndUserService;
        this.swmEmployeeService = swmEmployeeService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmEmployeeInterfaceDTO insert(SwmEmployeeInterface employeeInterfaceNew) {
        swmEmployeeInterfaceDao.insertAllColumn(employeeInterfaceNew);
        return swmEmployeeInterfaceMapper.toDto(employeeInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Double id) {
        SwmEmployeeInterface employeeInterface = new SwmEmployeeInterface();
        employeeInterface.setId(id);
        this.delete(employeeInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmEmployeeInterface employeeInterface) {
        // TODO    确认删除前是否需要做检查
        swmEmployeeInterfaceDao.deleteByEntityKey(employeeInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmEmployeeInterface employeeInterfaceNew) {
        SwmEmployeeInterface employeeInterfaceInDb = Optional.ofNullable(swmEmployeeInterfaceDao.getByKey(employeeInterfaceNew.getId())).orElseGet(SwmEmployeeInterface::new);
        ValidationUtil.isNull(employeeInterfaceInDb.getId() ,"EmployeeInterface", "id", employeeInterfaceNew.getId());
        employeeInterfaceNew.setId(employeeInterfaceInDb.getId());
        swmEmployeeInterfaceDao.updateAllColumnByKey(employeeInterfaceNew);
    }

    @Override
    public SwmEmployeeInterfaceDTO getByKey(Double id) {
        SwmEmployeeInterface employeeInterface = Optional.ofNullable(swmEmployeeInterfaceDao.getByKey(id)).orElseGet(SwmEmployeeInterface::new);
        ValidationUtil.isNull(employeeInterface.getId() ,"EmployeeInterface", "id", id);
        return swmEmployeeInterfaceMapper.toDto(employeeInterface);
    }

    @Override
    public List<SwmEmployeeInterfaceDTO> listAll(SwmEmployeeInterfaceQueryCriteria criteria) {
        return swmEmployeeInterfaceMapper.toDto(swmEmployeeInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmEmployeeInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<SwmEmployeeInterface> page = PageUtil.startPage(pageable);
        List<SwmEmployeeInterface> employeeInterfaces = swmEmployeeInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmEmployeeInterfaceMapper.toDto(employeeInterfaces), page.getTotal());
    }

    @Override
    public void download(List<SwmEmployeeInterfaceDTO> employeeInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmEmployeeInterfaceDTO employeeInterfaceDTO : employeeInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("人员信息上的人员id", employeeInterfaceDTO.getEmployeeId());
            map.put("工牌号", employeeInterfaceDTO.getWorkCard());
            map.put("姓名", employeeInterfaceDTO.getName());
            map.put("数据分组id", employeeInterfaceDTO.getGroupId());
            map.put("操作码", employeeInterfaceDTO.getOperationCode());
            map.put("错误信息", employeeInterfaceDTO.getErrorMsg());
            map.put("数据状态", employeeInterfaceDTO.getDataStatus());
            map.put("包干工资", employeeInterfaceDTO.getLumpSumWage());
            map.put("岗位技能工资", employeeInterfaceDTO.getPostSkillSalary());
            map.put("目标绩效工资", employeeInterfaceDTO.getTargetPerformancePay());
            map.put("基本工资", employeeInterfaceDTO.getBasePay());
            map.put("扣除保险(个人)", employeeInterfaceDTO.getPersonalDeductibles());
            map.put("扣除保险(公司)", employeeInterfaceDTO.getCompanyDeductibles());
            map.put("扣除公积金（个人）", employeeInterfaceDTO.getPersonalDeductAccumulationFund());
            map.put("扣除公积金（公司）", employeeInterfaceDTO.getCompanyDeductAccumulationFund());
            map.put("高温补贴", employeeInterfaceDTO.getHighTemperatureSubsidy());
            map.put("id", employeeInterfaceDTO.getId());
            map.put("创建时间", employeeInterfaceDTO.getCreateTime());
            map.put("创建人ID", employeeInterfaceDTO.getCreateBy());
            map.put("修改时间", employeeInterfaceDTO.getUpdateTime());
            map.put("修改人ID", employeeInterfaceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SwmEmployeeInterfaceDTO> importExcel(List<SwmEmployeeInterface> swmEmployeeInterfaces, Boolean reImportFlag) {
        FndInterfaceOperationRecord fndInterfaceOperationRecord = new FndInterfaceOperationRecord();
        fndInterfaceOperationRecord.setOperationValue("insertSwmEmployee");
        fndInterfaceOperationRecord.setOperationDescription("薪酬人员基本信息条目导入");
        fndInterfaceOperationRecordService.insert(fndInterfaceOperationRecord);
        Long tempGroupId = fndInterfaceOperationRecord.getId();
        try {
            FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
            instance.insertMainAndSon(swmEmployeeInterfaces, tempGroupId, user.getId(), reImportFlag);
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
        SwmEmployeeInterfaceQueryCriteria swmEmployeeInterfaceQueryCriteria = new SwmEmployeeInterfaceQueryCriteria();
        swmEmployeeInterfaceQueryCriteria.setGroupId(tempGroupId);
        swmEmployeeInterfaceQueryCriteria.setDataStatus("F");
        List<SwmEmployeeInterface> swmEmployeeInterfaceList = swmEmployeeInterfaceDao.listAllByCriteria(swmEmployeeInterfaceQueryCriteria);
        if (swmEmployeeInterfaceList.size() == 0) {
            swmEmployeeInterfaceList.add(new SwmEmployeeInterface().setGroupId(fndInterfaceOperationRecord.getId()));
        }
        return swmEmployeeInterfaceMapper.toDto(swmEmployeeInterfaceList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void insertMainAndSon(List<SwmEmployeeInterface> swmEmployeeInterfaces, Long groupId, Long userId, Boolean reImportFlag) {
        for (SwmEmployeeInterface swmEmployeeInterface : swmEmployeeInterfaces
             ) {
            if (reImportFlag) {
                swmEmployeeInterface.setDataStatus("F");
                swmEmployeeInterface.setErrorMsg("");
            }
            swmEmployeeInterface.setCreateBy(userId);
            swmEmployeeInterface.setUpdateBy(userId);
            swmEmployeeInterface.setGroupId(groupId);
            swmEmployeeInterfaceDao.insertSwmEmployeeInterface(swmEmployeeInterface);
        }
        swmEmployeeInterfaceDao.interfaceToHistory(groupId);
        swmEmployeeService.interfaceToMain(groupId);
    }

    @Override
    public List<SwmEmployeeInterface> getSwmEmployeeSummaryByImportList(Set<String> workCards, Set<Long> groupIds) {
        return swmEmployeeInterfaceDao.getSwmEmployeeSummaryByImportList(workCards, groupIds);
    }
}
