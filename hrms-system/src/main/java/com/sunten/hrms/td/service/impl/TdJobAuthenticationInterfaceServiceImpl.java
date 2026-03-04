package com.sunten.hrms.td.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.td.dao.TdJobAuthenticationDao;
import com.sunten.hrms.td.domain.TdJobAuthenticationInterface;
import com.sunten.hrms.td.dao.TdJobAuthenticationInterfaceDao;
import com.sunten.hrms.td.service.TdJobAuthenticationInterfaceService;
import com.sunten.hrms.td.dto.TdJobAuthenticationInterfaceDTO;
import com.sunten.hrms.td.dto.TdJobAuthenticationInterfaceQueryCriteria;
import com.sunten.hrms.td.mapper.TdJobAuthenticationInterfaceMapper;
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
 * 上岗认证接口表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-10-11
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdJobAuthenticationInterfaceServiceImpl extends ServiceImpl<TdJobAuthenticationInterfaceDao, TdJobAuthenticationInterface> implements TdJobAuthenticationInterfaceService {
    private final TdJobAuthenticationInterfaceDao tdJobAuthenticationInterfaceDao;
    private final TdJobAuthenticationInterfaceMapper tdJobAuthenticationInterfaceMapper;
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;
    private final FndUserService fndUserService;
    private final TdJobAuthenticationDao tdJobAuthenticationDao;

    public TdJobAuthenticationInterfaceServiceImpl(TdJobAuthenticationInterfaceDao tdJobAuthenticationInterfaceDao, TdJobAuthenticationInterfaceMapper tdJobAuthenticationInterfaceMapper, FndInterfaceOperationRecordService fndInterfaceOperationRecordService, FndUserService fndUserService, TdJobAuthenticationDao tdJobAuthenticationDao) {
        this.tdJobAuthenticationInterfaceDao = tdJobAuthenticationInterfaceDao;
        this.tdJobAuthenticationInterfaceMapper = tdJobAuthenticationInterfaceMapper;
        this.fndInterfaceOperationRecordService = fndInterfaceOperationRecordService;
        this.fndUserService = fndUserService;
        this.tdJobAuthenticationDao = tdJobAuthenticationDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdJobAuthenticationInterfaceDTO insert(TdJobAuthenticationInterface jobAuthenticationInterfaceNew) {
        tdJobAuthenticationInterfaceDao.insertAllColumn(jobAuthenticationInterfaceNew);
        return tdJobAuthenticationInterfaceMapper.toDto(jobAuthenticationInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdJobAuthenticationInterface jobAuthenticationInterface = new TdJobAuthenticationInterface();
        jobAuthenticationInterface.setId(id);
        this.delete(jobAuthenticationInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdJobAuthenticationInterface jobAuthenticationInterface) {
        // TODO    确认删除前是否需要做检查
        tdJobAuthenticationInterfaceDao.deleteByEntityKey(jobAuthenticationInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdJobAuthenticationInterface jobAuthenticationInterfaceNew) {
        TdJobAuthenticationInterface jobAuthenticationInterfaceInDb = Optional.ofNullable(tdJobAuthenticationInterfaceDao.getByKey(jobAuthenticationInterfaceNew.getId())).orElseGet(TdJobAuthenticationInterface::new);
        ValidationUtil.isNull(jobAuthenticationInterfaceInDb.getId() ,"JobAuthenticationInterface", "id", jobAuthenticationInterfaceNew.getId());
        jobAuthenticationInterfaceNew.setId(jobAuthenticationInterfaceInDb.getId());
        tdJobAuthenticationInterfaceDao.updateAllColumnByKey(jobAuthenticationInterfaceNew);
    }

    @Override
    public TdJobAuthenticationInterfaceDTO getByKey(Long id) {
        TdJobAuthenticationInterface jobAuthenticationInterface = Optional.ofNullable(tdJobAuthenticationInterfaceDao.getByKey(id)).orElseGet(TdJobAuthenticationInterface::new);
        ValidationUtil.isNull(jobAuthenticationInterface.getId() ,"JobAuthenticationInterface", "id", id);
        return tdJobAuthenticationInterfaceMapper.toDto(jobAuthenticationInterface);
    }

    @Override
    public List<TdJobAuthenticationInterfaceDTO> listAll(TdJobAuthenticationInterfaceQueryCriteria criteria) {
        return tdJobAuthenticationInterfaceMapper.toDto(tdJobAuthenticationInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdJobAuthenticationInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<TdJobAuthenticationInterface> page = PageUtil.startPage(pageable);
        List<TdJobAuthenticationInterface> jobAuthenticationInterfaces = tdJobAuthenticationInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdJobAuthenticationInterfaceMapper.toDto(jobAuthenticationInterfaces), page.getTotal());
    }

    @Override
    public void download(List<TdJobAuthenticationInterfaceDTO> jobAuthenticationInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdJobAuthenticationInterfaceDTO jobAuthenticationInterfaceDTO : jobAuthenticationInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("主键", jobAuthenticationInterfaceDTO.getId());
            map.put("数据分组id", jobAuthenticationInterfaceDTO.getGroupId());
            map.put("操作码", jobAuthenticationInterfaceDTO.getOperationCode());
            map.put("错误信息", jobAuthenticationInterfaceDTO.getErrorMsg());
            map.put("数据状态", jobAuthenticationInterfaceDTO.getDataStatus());
            map.put("人事ID", jobAuthenticationInterfaceDTO.getEmployeeId());
            map.put("工序", jobAuthenticationInterfaceDTO.getProcess());
            map.put("认证岗位", jobAuthenticationInterfaceDTO.getJob());
            map.put("认证岗位类别", jobAuthenticationInterfaceDTO.getAuthenticationLevel());
            map.put("岗位级别", jobAuthenticationInterfaceDTO.getLevel());
            map.put("培训认证负责人", jobAuthenticationInterfaceDTO.getSurety());
            map.put("第一次通用考试成绩", jobAuthenticationInterfaceDTO.getFirstGeneralGrade());
            map.put("第二次通用考试成绩", jobAuthenticationInterfaceDTO.getSecondGeneralGrade());
//            map.put("第三次通用考试成绩", jobAuthenticationInterfaceDTO.getThreeGeneralGrade());
            map.put("第一次岗位理论考试成绩", jobAuthenticationInterfaceDTO.getFirstTheoryGrade());
            map.put("第二次岗位理论考试成绩", jobAuthenticationInterfaceDTO.getSecondTheoryGrade());
//            map.put("第三次岗位理论考试成绩", jobAuthenticationInterfaceDTO.getThreeTheoryGrade());
            map.put("第一次岗位实操评估成绩", jobAuthenticationInterfaceDTO.getFirstOperationGrade());
            map.put("第二次岗位实操评估成绩", jobAuthenticationInterfaceDTO.getSecondOperationGrade());
//            map.put("第三次岗位实操评估成绩", jobAuthenticationInterfaceDTO.getThreeOperationGrade());
            map.put("上岗认证证书发放日期", jobAuthenticationInterfaceDTO.getCredentialGrantDate());
            map.put("证书生效截止日期", jobAuthenticationInterfaceDTO.getEnabledTime());
            map.put("部门", jobAuthenticationInterfaceDTO.getDeptName());
            map.put("科室", jobAuthenticationInterfaceDTO.getDepartment());
            map.put("班组", jobAuthenticationInterfaceDTO.getTeam());
            map.put("工号", jobAuthenticationInterfaceDTO.getWorkCard());
            map.put("姓名", jobAuthenticationInterfaceDTO.getName());
            map.put("创建时间", jobAuthenticationInterfaceDTO.getCreateTime());
            map.put("创建人id", jobAuthenticationInterfaceDTO.getCreateBy());
            map.put("修改时间", jobAuthenticationInterfaceDTO.getUpdateTime());
            map.put("修改人id", jobAuthenticationInterfaceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<TdJobAuthenticationInterface> importAuthenticationByExcel(List<TdJobAuthenticationInterface> authenticationInterfaces) {
        FndInterfaceOperationRecord fndInterfaceOperationRecord = new FndInterfaceOperationRecord();
        fndInterfaceOperationRecord.setOperationValue("insertEmployeeAward");
        fndInterfaceOperationRecord.setOperationDescription("奖罚情况导入");
        fndInterfaceOperationRecordService.insert(fndInterfaceOperationRecord);
        try {
            FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
            if (authenticationInterfaces.size() > 0) {
                for (TdJobAuthenticationInterface awardInterface : authenticationInterfaces) {
                    awardInterface.setGroupId(fndInterfaceOperationRecord.getId());
                    tdJobAuthenticationInterfaceDao.insertToInterface(awardInterface);
                }
                tdJobAuthenticationDao.interfaceToMain(user.getId(),fndInterfaceOperationRecord.getId());
            }
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
        return authenticationInterfaces;
    }
}
