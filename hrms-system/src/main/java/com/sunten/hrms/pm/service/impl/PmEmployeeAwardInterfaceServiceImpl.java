package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dao.PmEmployeeAwardDao;
import com.sunten.hrms.pm.domain.PmEmployeeAwardInterface;
import com.sunten.hrms.pm.dao.PmEmployeeAwardInterfaceDao;
import com.sunten.hrms.pm.dto.PmEmployeeAwardQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeAwardInterfaceService;
import com.sunten.hrms.pm.dto.PmEmployeeAwardInterfaceDTO;
import com.sunten.hrms.pm.dto.PmEmployeeAwardInterfaceQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeAwardInterfaceMapper;
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
 *  服务实现类
 * </p>
 *
 * @author xk
 * @since 2021-09-23
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeAwardInterfaceServiceImpl extends ServiceImpl<PmEmployeeAwardInterfaceDao, PmEmployeeAwardInterface> implements PmEmployeeAwardInterfaceService {
    private final PmEmployeeAwardInterfaceDao pmEmployeeAwardInterfaceDao;
    private final PmEmployeeAwardInterfaceMapper pmEmployeeAwardInterfaceMapper;
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;
    private final PmEmployeeAwardDao pmEmployeeAwardDao;
    private final FndUserService fndUserService;
    @Autowired
    private PmEmployeeAwardInterfaceServiceImpl instance;

    public PmEmployeeAwardInterfaceServiceImpl(PmEmployeeAwardInterfaceDao pmEmployeeAwardInterfaceDao, PmEmployeeAwardInterfaceMapper pmEmployeeAwardInterfaceMapper, FndInterfaceOperationRecordService fndInterfaceOperationRecordService, PmEmployeeAwardDao pmEmployeeAwardDao, FndUserService fndUserService) {
        this.pmEmployeeAwardInterfaceDao = pmEmployeeAwardInterfaceDao;
        this.pmEmployeeAwardInterfaceMapper = pmEmployeeAwardInterfaceMapper;
        this.fndInterfaceOperationRecordService = fndInterfaceOperationRecordService;
        this.pmEmployeeAwardDao = pmEmployeeAwardDao;
        this.fndUserService = fndUserService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeAwardInterfaceDTO insert(PmEmployeeAwardInterface employeeAwardInterfaceNew) {
        pmEmployeeAwardInterfaceDao.insertAllColumn(employeeAwardInterfaceNew);
        return pmEmployeeAwardInterfaceMapper.toDto(employeeAwardInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeAwardInterface employeeAwardInterface = new PmEmployeeAwardInterface();
        employeeAwardInterface.setId(id);
        this.delete(employeeAwardInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeAwardInterface employeeAwardInterface) {
        // TODO    确认删除前是否需要做检查
        pmEmployeeAwardInterfaceDao.deleteByEntityKey(employeeAwardInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeAwardInterface employeeAwardInterfaceNew) {
        PmEmployeeAwardInterface employeeAwardInterfaceInDb = Optional.ofNullable(pmEmployeeAwardInterfaceDao.getByKey(employeeAwardInterfaceNew.getId())).orElseGet(PmEmployeeAwardInterface::new);
        ValidationUtil.isNull(employeeAwardInterfaceInDb.getId() ,"EmployeeAwardInterface", "id", employeeAwardInterfaceNew.getId());
        employeeAwardInterfaceNew.setId(employeeAwardInterfaceInDb.getId());
        pmEmployeeAwardInterfaceDao.updateAllColumnByKey(employeeAwardInterfaceNew);
    }

    @Override
    public PmEmployeeAwardInterfaceDTO getByKey(Long id) {
        PmEmployeeAwardInterface employeeAwardInterface = Optional.ofNullable(pmEmployeeAwardInterfaceDao.getByKey(id)).orElseGet(PmEmployeeAwardInterface::new);
        ValidationUtil.isNull(employeeAwardInterface.getId() ,"EmployeeAwardInterface", "id", id);
        return pmEmployeeAwardInterfaceMapper.toDto(employeeAwardInterface);
    }

    @Override
    public List<PmEmployeeAwardInterfaceDTO> listAll(PmEmployeeAwardInterfaceQueryCriteria criteria) {
        return pmEmployeeAwardInterfaceMapper.toDto(pmEmployeeAwardInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeAwardInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeAwardInterface> page = PageUtil.startPage(pageable);
        List<PmEmployeeAwardInterface> employeeAwardInterfaces = pmEmployeeAwardInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeAwardInterfaceMapper.toDto(employeeAwardInterfaces), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeAwardInterfaceDTO> employeeAwardInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeAwardInterfaceDTO employeeAwardInterfaceDTO : employeeAwardInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("数据分组id", employeeAwardInterfaceDTO.getGroupId());
            map.put("操作码", employeeAwardInterfaceDTO.getOperationCode());
            map.put("错误信息", employeeAwardInterfaceDTO.getErrorMsg());
            map.put("数据状态", employeeAwardInterfaceDTO.getDataStatus());
            map.put("员工id", employeeAwardInterfaceDTO.getEmployeeId());
            map.put("奖励：Reward；扣罚：Fine", employeeAwardInterfaceDTO.getType());
            map.put("奖罚名称", employeeAwardInterfaceDTO.getAwardName());
            map.put("奖罚处理开始时间", employeeAwardInterfaceDTO.getAwardStarTime());
            map.put("奖罚处理结束时间", employeeAwardInterfaceDTO.getAwardEndTime());
            map.put("奖罚单位", employeeAwardInterfaceDTO.getAwardCompany());
            map.put("奖罚内容", employeeAwardInterfaceDTO.getAwardContent());
            map.put("奖罚结果", employeeAwardInterfaceDTO.getAwardResult());
            map.put("奖罚金额", employeeAwardInterfaceDTO.getAwardMoney());
            map.put("是否有备查资料", employeeAwardInterfaceDTO.getReferenceBackupFlag());
            map.put("备注", employeeAwardInterfaceDTO.getRemarks());
            map.put("id", employeeAwardInterfaceDTO.getId());
            map.put("创建时间", employeeAwardInterfaceDTO.getCreateTime());
            map.put("创建人id", employeeAwardInterfaceDTO.getCreateBy());
            map.put("修改时间", employeeAwardInterfaceDTO.getUpdateTime());
            map.put("修改人id", employeeAwardInterfaceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<PmEmployeeAwardInterfaceDTO> importAwardByExcel(List<PmEmployeeAwardInterface> awardInterfaces, Boolean reImportFlag) {
        if (null == awardInterfaces || awardInterfaces.size() == 0) {
            throw new InfoCheckWarningMessException("不允许上传空的文件");
        } else {
            FndInterfaceOperationRecord fndInterfaceOperationRecord = new FndInterfaceOperationRecord();
            fndInterfaceOperationRecord.setOperationValue("insertEmployeeAward");
            fndInterfaceOperationRecord.setOperationDescription("奖罚情况导入");
            fndInterfaceOperationRecordService.insert(fndInterfaceOperationRecord);
            Long tempGroupId = fndInterfaceOperationRecord.getId();
            try {
                instance.insertMainAndSon(awardInterfaces, tempGroupId, reImportFlag);
            } catch (Exception e) {
                fndInterfaceOperationRecord.setDataProcessingDescription(ThrowableUtil.getStackTrace(e));
                fndInterfaceOperationRecord.setSuccessFlag(false);
                fndInterfaceOperationRecordService.update(fndInterfaceOperationRecord);
                throw new InfoCheckWarningMessException("Excel导入失败，请联系管理员。");
            } finally {
                fndInterfaceOperationRecord.setSuccessFlag(true);
                fndInterfaceOperationRecord.setDataProcessingDescription("导入正常");
                fndInterfaceOperationRecordService.update(fndInterfaceOperationRecord);
            }
            // 返回数据
            PmEmployeeAwardInterfaceQueryCriteria pmEmployeeAwardInterfaceQueryCriteria = new PmEmployeeAwardInterfaceQueryCriteria();
            pmEmployeeAwardInterfaceQueryCriteria.setGroupId(tempGroupId);
            pmEmployeeAwardInterfaceQueryCriteria.setDataStatus("F");
            List<PmEmployeeAwardInterface> pmEmployeeAwardInterfaces = pmEmployeeAwardInterfaceDao.listAllByCriteria(pmEmployeeAwardInterfaceQueryCriteria);
            return pmEmployeeAwardInterfaceMapper.toDto(pmEmployeeAwardInterfaces);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void insertMainAndSon(List<PmEmployeeAwardInterface> pmEmployeeAwardInterfaces, Long groupId, Boolean reImportFlag) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        for (PmEmployeeAwardInterface pmAward : pmEmployeeAwardInterfaces
        ) {
            if (reImportFlag) {
                pmAward.setDataStatus("F");
                pmAward.setErrorMsg("");
            }
            pmAward.setGroupId(groupId);
            pmEmployeeAwardInterfaceDao.insertToInterface(pmAward);
        }
        pmEmployeeAwardDao.interfaceToMain(user.getId(), groupId);
    }
}
