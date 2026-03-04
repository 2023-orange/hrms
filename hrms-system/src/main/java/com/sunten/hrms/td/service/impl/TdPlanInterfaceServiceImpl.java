package com.sunten.hrms.td.service.impl;

import cn.hutool.json.JSON;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.td.dao.TdPlanDao;
import com.sunten.hrms.td.domain.TdPlanInterface;
import com.sunten.hrms.td.dao.TdPlanInterfaceDao;
import com.sunten.hrms.td.service.TdPlanInterfaceService;
import com.sunten.hrms.td.dto.TdPlanInterfaceDTO;
import com.sunten.hrms.td.dto.TdPlanInterfaceQueryCriteria;
import com.sunten.hrms.td.mapper.TdPlanInterfaceMapper;
import com.sunten.hrms.td.service.TdPlanService;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.DecimalFormat;
import java.time.LocalDate;
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
 * 培训计划接口表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-05-23
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdPlanInterfaceServiceImpl extends ServiceImpl<TdPlanInterfaceDao, TdPlanInterface> implements TdPlanInterfaceService {
    private final TdPlanInterfaceDao tdPlanInterfaceDao;
    private final TdPlanInterfaceMapper tdPlanInterfaceMapper;
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;
    private final FndUserService fndUserService;
    private final TdPlanService tdPlanService;
    private final TdPlanDao tdPlanDao;
    @Autowired
    private TdPlanInterfaceService instance;

    public TdPlanInterfaceServiceImpl(TdPlanInterfaceDao tdPlanInterfaceDao, TdPlanInterfaceMapper tdPlanInterfaceMapper,
                                      FndInterfaceOperationRecordService fndInterfaceOperationRecordService, FndUserService fndUserService,
                                      TdPlanService tdPlanService, TdPlanDao tdPlanDao) {
        this.tdPlanInterfaceDao = tdPlanInterfaceDao;
        this.tdPlanInterfaceMapper = tdPlanInterfaceMapper;
        this.fndInterfaceOperationRecordService = fndInterfaceOperationRecordService;
        this.fndUserService = fndUserService;
        this.tdPlanService = tdPlanService;
        this.tdPlanDao = tdPlanDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdPlanInterfaceDTO insert(TdPlanInterface planInterfaceNew) {
        tdPlanInterfaceDao.insertAllColumn(planInterfaceNew);
        return tdPlanInterfaceMapper.toDto(planInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdPlanInterface planInterface = new TdPlanInterface();
        planInterface.setId(id);
        this.delete(planInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdPlanInterface planInterface) {
        // TODO    确认删除前是否需要做检查
        tdPlanInterfaceDao.deleteByEntityKey(planInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdPlanInterface planInterfaceNew) {
        TdPlanInterface planInterfaceInDb = Optional.ofNullable(tdPlanInterfaceDao.getByKey(planInterfaceNew.getId())).orElseGet(TdPlanInterface::new);
        ValidationUtil.isNull(planInterfaceInDb.getId() ,"PlanInterface", "id", planInterfaceNew.getId());
        planInterfaceNew.setId(planInterfaceInDb.getId());
        tdPlanInterfaceDao.updateAllColumnByKey(planInterfaceNew);
    }

    @Override
    public TdPlanInterfaceDTO getByKey(Long id) {
        TdPlanInterface planInterface = Optional.ofNullable(tdPlanInterfaceDao.getByKey(id)).orElseGet(TdPlanInterface::new);
        ValidationUtil.isNull(planInterface.getId() ,"PlanInterface", "id", id);
        return tdPlanInterfaceMapper.toDto(planInterface);
    }

    @Override
    public List<TdPlanInterfaceDTO> listAll(TdPlanInterfaceQueryCriteria criteria) {
        return tdPlanInterfaceMapper.toDto(tdPlanInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdPlanInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<TdPlanInterface> page = PageUtil.startPage(pageable);
        List<TdPlanInterface> planInterfaces = tdPlanInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdPlanInterfaceMapper.toDto(planInterfaces), page.getTotal());
    }

    @Override
    public void download(List<TdPlanInterfaceDTO> planInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdPlanInterfaceDTO planInterfaceDTO : planInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("培训名称", planInterfaceDTO.getTrainingName());
            map.put("培训方式", planInterfaceDTO.getTrainingMethod());
            map.put("培训内容", planInterfaceDTO.getTrainingContent());
            map.put("培训目的", planInterfaceDTO.getTrainingPurpose());
            map.put("培训级别", planInterfaceDTO.getTrainingLevel());
            map.put("专业分类", planInterfaceDTO.getProfessionClassify());
            map.put("所属部门", planInterfaceDTO.getDependenceDept());
            map.put("主办部门", planInterfaceDTO.getHostDept());
            map.put("讲师", planInterfaceDTO.getTeacher());
            map.put("参加人员", planInterfaceDTO.getEmployeeDescribes());
            map.put("时间", planInterfaceDTO.getPlanDate());
            map.put("参训人数", planInterfaceDTO.getEmployeeQuantity());
            map.put("预算(元)", planInterfaceDTO.getPlanMoney());
            map.put("是否线上审批", planInterfaceDTO.getOnlineFlag());
            map.put("备注", planInterfaceDTO.getRemark());
            map.put("有效标记", planInterfaceDTO.getEnabledFlag());
            map.put("操作码", planInterfaceDTO.getOperationCode());
            map.put("错误信息", planInterfaceDTO.getErrorMsg());
            map.put("数据状态", planInterfaceDTO.getDataStatus());
            map.put("数组分组ID", planInterfaceDTO.getGroupId());
            map.put("id", planInterfaceDTO.getId());
            map.put("创建时间", planInterfaceDTO.getCreateTime());
            map.put("创建人ID", planInterfaceDTO.getCreateBy());
            map.put("修改时间", planInterfaceDTO.getUpdateTime());
            map.put("修改人ID", planInterfaceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<TdPlanInterfaceDTO> insertExcel(List<TdPlanInterface> tdPlanInterfaces) {
        if (null == tdPlanInterfaces || tdPlanInterfaces.size() <= 0) {
            throw new InfoCheckWarningMessException("Excel无内容， 不允许导入");
        }
        FndInterfaceOperationRecord fndInterfaceOperationRecord = new FndInterfaceOperationRecord();
        fndInterfaceOperationRecord.setOperationValue("insertTdPlan");
        fndInterfaceOperationRecord.setOperationDescription("年度培训计划导入");
        fndInterfaceOperationRecordService.insert(fndInterfaceOperationRecord);
        Long tempGroupId = fndInterfaceOperationRecord.getId();
        try {
            FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
            instance.insertMainAndSon(tdPlanInterfaces, fndInterfaceOperationRecord.getId(), user.getId());
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
        TdPlanInterfaceQueryCriteria tdPlanInterfaceQueryCriteria = new TdPlanInterfaceQueryCriteria();
        tdPlanInterfaceQueryCriteria.setGroupId(tempGroupId);
        return instance.listAll(tdPlanInterfaceQueryCriteria);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void insertMainAndSon(List<TdPlanInterface> tdPlanInterfaces, Long groupId, Long userId) {
        for (TdPlanInterface tdPlanInterface : tdPlanInterfaces
             ) {
            tdPlanInterface.setUserId(userId);
            tdPlanInterface.setGroupId(groupId);
            tdPlanInterfaceDao.insertByInterface(tdPlanInterface);
        }
        DecimalFormat two = new DecimalFormat("00");
        DecimalFormat three = new DecimalFormat("000");
        TdPlanInterfaceQueryCriteria tdPlanInterfaceQueryCriteria = new TdPlanInterfaceQueryCriteria();
        tdPlanInterfaceQueryCriteria.setGroupId(groupId);
        String dateStr = LocalDate.now().getYear() + two.format(Integer.valueOf(LocalDate.now().getMonthValue()))
                + two.format(Integer.valueOf(LocalDate.now().getDayOfMonth()));
        List<TdPlanInterface> tdPlanInterfaceList = tdPlanInterfaceDao.listAllByCriteria(tdPlanInterfaceQueryCriteria);
        Integer wNumber = tdPlanDao.getNumberByMethod("外派培训");
        Integer yNumber = tdPlanDao.getNumberByMethod("引入内训");
        Integer nNumber = tdPlanDao.getNumberByMethod("内部培训");
        for (TdPlanInterface tpi : tdPlanInterfaceList
             ) {
            if (tpi.getTrainingMethod().equals("外派培训")) {
                tpi.setTrainingNo("W" + dateStr + three.format(++wNumber));
            }
            if (tpi.getTrainingMethod().equals("引入内训")) {
                tpi.setTrainingNo("Y" + dateStr + three.format(++yNumber));
            }
            if (tpi.getTrainingMethod().equals("内部培训")) {
                tpi.setTrainingNo("N" + dateStr + three.format(++nNumber));
            }
            tdPlanService.interfaceToMainByObj(tpi);
        }
//        tdPlanService.interfaceToMain(groupId);
    }
}
