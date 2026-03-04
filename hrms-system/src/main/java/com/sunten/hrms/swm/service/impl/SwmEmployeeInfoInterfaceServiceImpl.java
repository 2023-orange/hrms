package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.domain.SwmEmployeeInfoInterface;
import com.sunten.hrms.swm.dao.SwmEmployeeInfoInterfaceDao;
import com.sunten.hrms.swm.service.SwmEmployeeInfoInterfaceService;
import com.sunten.hrms.swm.dto.SwmEmployeeInfoInterfaceDTO;
import com.sunten.hrms.swm.dto.SwmEmployeeInfoInterfaceQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmEmployeeInfoInterfaceMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;

/**
 * <p>
 * 薪酬员工基本信息接口表 服务实现类
 * </p>
 *
 * @author zhoujy
 * @since 2023-03-23
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmEmployeeInfoInterfaceServiceImpl extends ServiceImpl<SwmEmployeeInfoInterfaceDao, SwmEmployeeInfoInterface> implements SwmEmployeeInfoInterfaceService {
    private final SwmEmployeeInfoInterfaceDao swmEmployeeInfoInterfaceDao;
    private final SwmEmployeeInfoInterfaceMapper swmEmployeeInfoInterfaceMapper;
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;
    private final FndUserService fndUserService;

    public SwmEmployeeInfoInterfaceServiceImpl(SwmEmployeeInfoInterfaceDao swmEmployeeInfoInterfaceDao, SwmEmployeeInfoInterfaceMapper swmEmployeeInfoInterfaceMapper, FndInterfaceOperationRecordService fndInterfaceOperationRecordService, FndUserService fndUserService) {
        this.swmEmployeeInfoInterfaceDao = swmEmployeeInfoInterfaceDao;
        this.swmEmployeeInfoInterfaceMapper = swmEmployeeInfoInterfaceMapper;
        this.fndInterfaceOperationRecordService = fndInterfaceOperationRecordService;
        this.fndUserService = fndUserService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmEmployeeInfoInterfaceDTO insert(SwmEmployeeInfoInterface employeeInfoInterfaceNew) {
        swmEmployeeInfoInterfaceDao.insertAllColumn(employeeInfoInterfaceNew);
        return swmEmployeeInfoInterfaceMapper.toDto(employeeInfoInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Double id) {
        SwmEmployeeInfoInterface employeeInfoInterface = new SwmEmployeeInfoInterface();
        employeeInfoInterface.setId(id);
        this.delete(employeeInfoInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmEmployeeInfoInterface employeeInfoInterface) {
        // TODO    确认删除前是否需要做检查
        swmEmployeeInfoInterfaceDao.deleteByEntityKey(employeeInfoInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmEmployeeInfoInterface employeeInfoInterfaceNew) {
        SwmEmployeeInfoInterface employeeInfoInterfaceInDb = Optional.ofNullable(swmEmployeeInfoInterfaceDao.getByKey(employeeInfoInterfaceNew.getId())).orElseGet(SwmEmployeeInfoInterface::new);
        ValidationUtil.isNull(employeeInfoInterfaceInDb.getId() ,"EmployeeInfoInterface", "id", employeeInfoInterfaceNew.getId());
        employeeInfoInterfaceNew.setId(employeeInfoInterfaceInDb.getId());
        swmEmployeeInfoInterfaceDao.updateAllColumnByKey(employeeInfoInterfaceNew);
    }

    @Override
    public SwmEmployeeInfoInterfaceDTO getByKey(Double id) {
        SwmEmployeeInfoInterface employeeInfoInterface = Optional.ofNullable(swmEmployeeInfoInterfaceDao.getByKey(id)).orElseGet(SwmEmployeeInfoInterface::new);
        ValidationUtil.isNull(employeeInfoInterface.getId() ,"EmployeeInfoInterface", "id", id);
        return swmEmployeeInfoInterfaceMapper.toDto(employeeInfoInterface);
    }

    @Override
    public List<SwmEmployeeInfoInterfaceDTO> listAll(SwmEmployeeInfoInterfaceQueryCriteria criteria) {
        return swmEmployeeInfoInterfaceMapper.toDto(swmEmployeeInfoInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmEmployeeInfoInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<SwmEmployeeInfoInterface> page = PageUtil.startPage(pageable);
        List<SwmEmployeeInfoInterface> employeeInfoInterfaces = swmEmployeeInfoInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmEmployeeInfoInterfaceMapper.toDto(employeeInfoInterfaces), page.getTotal());
    }

    @Override
    public void download(List<SwmEmployeeInfoInterfaceDTO> employeeInfoInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmEmployeeInfoInterfaceDTO employeeInfoInterfaceDTO : employeeInfoInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", employeeInfoInterfaceDTO.getId());
            map.put("employeeId", employeeInfoInterfaceDTO.getEmployeeId());
            map.put("workCard", employeeInfoInterfaceDTO.getWorkCard());
            map.put("name", employeeInfoInterfaceDTO.getName());
            map.put("数据分组id", employeeInfoInterfaceDTO.getGroupId());
            map.put("操作码", employeeInfoInterfaceDTO.getOperationCode());
            map.put("错误信息", employeeInfoInterfaceDTO.getErrorMsg());
            map.put("数据状态", employeeInfoInterfaceDTO.getDataStatus());
            map.put("银行账号", employeeInfoInterfaceDTO.getBankAccount());
            map.put("银行名称", employeeInfoInterfaceDTO.getBankName());
            map.put("部门", employeeInfoInterfaceDTO.getDepartment());
            map.put("科室", employeeInfoInterfaceDTO.getAdministrativeOffice());
            map.put("班组", employeeInfoInterfaceDTO.getTeam());
            map.put("岗位", employeeInfoInterfaceDTO.getStation());
            map.put("成本中心号", employeeInfoInterfaceDTO.getCostCenterNum());
            map.put("成本中心名称", employeeInfoInterfaceDTO.getCostCenter());
            map.put("服务部门", employeeInfoInterfaceDTO.getServiceDepartment());
            map.put("createTime", employeeInfoInterfaceDTO.getCreateTime());
            map.put("createBy", employeeInfoInterfaceDTO.getCreateBy());
            map.put("updateTime", employeeInfoInterfaceDTO.getUpdateTime());
            map.put("updateBy", employeeInfoInterfaceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


    @Override
    public List<SwmEmployeeInfoInterfaceDTO> importExcel(List<SwmEmployeeInfoInterface> swmEmployeeInfoInterfaces) {
        FndInterfaceOperationRecord fndInterfaceOperationRecord = new FndInterfaceOperationRecord();
        fndInterfaceOperationRecord.setOperationValue("insertSwmEmployeeInfo");
        fndInterfaceOperationRecord.setOperationDescription("薪酬人员定义条目导入");
        fndInterfaceOperationRecordService.insert(fndInterfaceOperationRecord);
        Long tempGroupId = fndInterfaceOperationRecord.getId();
        try {
            FndUserDTO user;
            try {
                user = fndUserService.getByName(SecurityUtils.getUsername());
            } catch (Exception ex){
                user = new FndUserDTO();
                user.setId(-1l);
            }
            for (SwmEmployeeInfoInterface swmEmployeeInfoInterface : swmEmployeeInfoInterfaces
            ) {
                swmEmployeeInfoInterface.setCreateBy(user.getId());
                swmEmployeeInfoInterface.setUpdateBy(user.getId());
                swmEmployeeInfoInterface.setGroupId(tempGroupId);
                swmEmployeeInfoInterface.setDataStatus("I");
                swmEmployeeInfoInterface.setOperationCode("U");
                swmEmployeeInfoInterfaceDao.insertAllColumn(swmEmployeeInfoInterface);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("groupId", tempGroupId);
            map.put("resultStr", "");
            swmEmployeeInfoInterfaceDao.checkSwmEmployeeInfoInterface(map);
            swmEmployeeInfoInterfaceDao.insertSwmEmployeeInfoInterfaceToMain(map);
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
        SwmEmployeeInfoInterfaceQueryCriteria swmEmployeeInfoInterfaceQueryCriteria = new SwmEmployeeInfoInterfaceQueryCriteria();
        swmEmployeeInfoInterfaceQueryCriteria.setGroupId(tempGroupId);
        swmEmployeeInfoInterfaceQueryCriteria.setDataStatus("E");
        List<SwmEmployeeInfoInterface> swmEmployeeInterfaceList = swmEmployeeInfoInterfaceDao.listAllByCriteria(swmEmployeeInfoInterfaceQueryCriteria);
        if (swmEmployeeInterfaceList.size() == 0) {
            swmEmployeeInterfaceList.add(new SwmEmployeeInfoInterface().setGroupId(fndInterfaceOperationRecord.getId()));
        }
        return swmEmployeeInfoInterfaceMapper.toDto(swmEmployeeInterfaceList);
    }

    @Override
    public List<SwmEmployeeInfoInterface> getSwmEmployeeInfoSummaryByImportList(Set<String> workCards, Set<Long> groupIds) {
        return swmEmployeeInfoInterfaceDao.getSwmEmployeeInfoSummaryByImportList(workCards, groupIds);
    }
}
