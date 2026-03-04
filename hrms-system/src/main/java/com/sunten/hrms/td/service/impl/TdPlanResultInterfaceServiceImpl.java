package com.sunten.hrms.td.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.td.domain.TdPlanResult;
import com.sunten.hrms.td.domain.TdPlanResultInterface;
import com.sunten.hrms.td.dao.TdPlanResultInterfaceDao;
import com.sunten.hrms.td.dto.TdPlanResultQueryCriteria;
import com.sunten.hrms.td.service.TdPlanAgreementService;
import com.sunten.hrms.td.service.TdPlanResultInterfaceService;
import com.sunten.hrms.td.dto.TdPlanResultInterfaceDTO;
import com.sunten.hrms.td.dto.TdPlanResultInterfaceQueryCriteria;
import com.sunten.hrms.td.mapper.TdPlanResultInterfaceMapper;
import com.sunten.hrms.td.service.TdPlanResultService;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;

/**
 * <p>
 * 培训结果接口表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-06-17
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdPlanResultInterfaceServiceImpl extends ServiceImpl<TdPlanResultInterfaceDao, TdPlanResultInterface> implements TdPlanResultInterfaceService {
    private final TdPlanResultInterfaceDao tdPlanResultInterfaceDao;
    private final TdPlanResultInterfaceMapper tdPlanResultInterfaceMapper;
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;
    private final FndUserService fndUserService;
    private final TdPlanResultService tdPlanResultService;
    private final TdPlanAgreementService tdPlanAgreementService;
    @Autowired
    private TdPlanResultInterfaceService instance;

    public TdPlanResultInterfaceServiceImpl(TdPlanResultInterfaceDao tdPlanResultInterfaceDao, TdPlanResultInterfaceMapper tdPlanResultInterfaceMapper,
                                            FndInterfaceOperationRecordService fndInterfaceOperationRecordService, FndUserService fndUserService,
                                            TdPlanResultService tdPlanResultService, TdPlanAgreementService tdPlanAgreementService) {
        this.tdPlanResultInterfaceDao = tdPlanResultInterfaceDao;
        this.tdPlanResultInterfaceMapper = tdPlanResultInterfaceMapper;
        this.fndInterfaceOperationRecordService = fndInterfaceOperationRecordService;
        this.fndUserService = fndUserService;
        this.tdPlanResultService = tdPlanResultService;
        this.tdPlanAgreementService = tdPlanAgreementService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdPlanResultInterfaceDTO insert(TdPlanResultInterface planResultInterfaceNew) {
        tdPlanResultInterfaceDao.insertAllColumn(planResultInterfaceNew);
        return tdPlanResultInterfaceMapper.toDto(planResultInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdPlanResultInterface planResultInterface = new TdPlanResultInterface();
        planResultInterface.setId(id);
        this.delete(planResultInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdPlanResultInterface planResultInterface) {
        // TODO    确认删除前是否需要做检查
        tdPlanResultInterfaceDao.deleteByEntityKey(planResultInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdPlanResultInterface planResultInterfaceNew) {
        TdPlanResultInterface planResultInterfaceInDb = Optional.ofNullable(tdPlanResultInterfaceDao.getByKey(planResultInterfaceNew.getId())).orElseGet(TdPlanResultInterface::new);
        ValidationUtil.isNull(planResultInterfaceInDb.getId() ,"PlanResultInterface", "id", planResultInterfaceNew.getId());
        planResultInterfaceNew.setId(planResultInterfaceInDb.getId());
        tdPlanResultInterfaceDao.updateAllColumnByKey(planResultInterfaceNew);
    }

    @Override
    public TdPlanResultInterfaceDTO getByKey(Long id) {
        TdPlanResultInterface planResultInterface = Optional.ofNullable(tdPlanResultInterfaceDao.getByKey(id)).orElseGet(TdPlanResultInterface::new);
        ValidationUtil.isNull(planResultInterface.getId() ,"PlanResultInterface", "id", id);
        return tdPlanResultInterfaceMapper.toDto(planResultInterface);
    }

    @Override
    public List<TdPlanResultInterfaceDTO> listAll(TdPlanResultInterfaceQueryCriteria criteria) {
        return tdPlanResultInterfaceMapper.toDto(tdPlanResultInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdPlanResultInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<TdPlanResultInterface> page = PageUtil.startPage(pageable);
        List<TdPlanResultInterface> planResultInterfaces = tdPlanResultInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdPlanResultInterfaceMapper.toDto(planResultInterfaces), page.getTotal());
    }

    @Override
    public void download(List<TdPlanResultInterfaceDTO> planResultInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdPlanResultInterfaceDTO planResultInterfaceDTO : planResultInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("工牌号", planResultInterfaceDTO.getWorkCard());
            map.put("培训计划id", planResultInterfaceDTO.getPlanId());
            map.put("员工出勤情况", planResultInterfaceDTO.getAttendance());
            map.put("培训时长", planResultInterfaceDTO.getDuration());
            map.put("成绩", planResultInterfaceDTO.getGrade());
            map.put("满意度", planResultInterfaceDTO.getEvaluate());
            map.put("是否签订培训协议书", planResultInterfaceDTO.getNeedFlag());
            map.put("生效标记", planResultInterfaceDTO.getEnabledFlag());
            map.put("操作码", planResultInterfaceDTO.getOperationCode());
            map.put("错误信息", planResultInterfaceDTO.getErrorMsg());
            map.put("数据状态", planResultInterfaceDTO.getDataStatus());
            map.put("数组分组ID", planResultInterfaceDTO.getGroupId());
            map.put("姓名", planResultInterfaceDTO.getName());
            map.put("id", planResultInterfaceDTO.getId());
            map.put("创建时间", planResultInterfaceDTO.getCreateTime());
            map.put("创建人ID", planResultInterfaceDTO.getCreateBy());
            map.put("修改时间", planResultInterfaceDTO.getUpdateTime());
            map.put("修改人ID", planResultInterfaceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<TdPlanResultInterfaceDTO> insertExcel(List<TdPlanResultInterface> tdPlanResultInterfaces) {
        if (null == tdPlanResultInterfaces || tdPlanResultInterfaces.size() <= 0) {
            throw new InfoCheckWarningMessException("Excel无内容， 不允许导入");
        }
        FndInterfaceOperationRecord fndInterfaceOperationRecord = new FndInterfaceOperationRecord();
        fndInterfaceOperationRecord.setOperationValue("insertTdPlanResult");
        fndInterfaceOperationRecord.setOperationDescription("培训结果导入");
        fndInterfaceOperationRecordService.insert(fndInterfaceOperationRecord);
        Long tempGroupId = fndInterfaceOperationRecord.getId();
        try {
            FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
            instance.insertMainAndSon(tdPlanResultInterfaces, fndInterfaceOperationRecord.getId(), user.getId());
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
        TdPlanResultInterfaceQueryCriteria tdPlanResultInterfaceQueryCriteria = new TdPlanResultInterfaceQueryCriteria();
        tdPlanResultInterfaceQueryCriteria.setGroupId(tempGroupId);
        tdPlanResultInterfaceQueryCriteria.setEnabledFlag(true);
        return instance.listAll(tdPlanResultInterfaceQueryCriteria);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void insertMainAndSon(List<TdPlanResultInterface> tdPlanResultInterfaces, Long groupId, Long userId) {

        for (TdPlanResultInterface tdPlanResultInterface : tdPlanResultInterfaces
        ) {
            tdPlanResultInterface.setCreateBy(userId);
            tdPlanResultInterface.setUpdateBy(userId);
            tdPlanResultInterface.setGroupId(groupId);
            tdPlanResultInterfaceDao.insertByInterface(tdPlanResultInterface);
        }
        TdPlanResult tdPlanResult = new TdPlanResult();
        tdPlanResult.setPlanId(tdPlanResultInterfaces.get(0).getPlanId());
        tdPlanResult.setEnabledFlag(true);
        tdPlanResult.setCheckMethod(tdPlanResultInterfaces.get(0).getCheckMethod());
        // 删除原有结果
        tdPlanResultService.removeByPlanId(tdPlanResult);
        // 删除相关协议
        tdPlanAgreementService.disabledByCheckMethodAndPlanID(tdPlanResultInterfaces.get(0).getPlanId(), tdPlanResultInterfaces.get(0).getCheckMethod());
        tdPlanResultService.interfaceToMain(groupId);
    }
}
