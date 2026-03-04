package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.dao.SwmBonusPaymentDao;
import com.sunten.hrms.swm.domain.SwmBonus;
import com.sunten.hrms.swm.domain.SwmBonusPayment;
import com.sunten.hrms.swm.domain.SwmBonusPaymentInterface;
import com.sunten.hrms.swm.dao.SwmBonusPaymentInterfaceDao;
import com.sunten.hrms.swm.dto.SwmBonusPaymentQueryCriteria;
import com.sunten.hrms.swm.service.SwmBonusPaymentInterfaceService;
import com.sunten.hrms.swm.dto.SwmBonusPaymentInterfaceDTO;
import com.sunten.hrms.swm.dto.SwmBonusPaymentInterfaceQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmBonusPaymentInterfaceMapper;
import com.sunten.hrms.swm.service.SwmBonusPaymentService;
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
 * 奖金发放接口表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmBonusPaymentInterfaceServiceImpl extends ServiceImpl<SwmBonusPaymentInterfaceDao, SwmBonusPaymentInterface> implements SwmBonusPaymentInterfaceService {
    private final SwmBonusPaymentInterfaceDao swmBonusPaymentInterfaceDao;
    private final SwmBonusPaymentInterfaceMapper swmBonusPaymentInterfaceMapper;
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;
    private final SwmBonusPaymentDao swmBonusPaymentDao;
    private final FndUserService fndUserService;

    @Autowired
    private SwmBonusPaymentInterfaceService instance;

    public SwmBonusPaymentInterfaceServiceImpl(SwmBonusPaymentInterfaceDao swmBonusPaymentInterfaceDao, SwmBonusPaymentInterfaceMapper swmBonusPaymentInterfaceMapper,
            FndInterfaceOperationRecordService fndInterfaceOperationRecordService,SwmBonusPaymentDao swmBonusPaymentDao, FndUserService fndUserService) {
        this.fndInterfaceOperationRecordService = fndInterfaceOperationRecordService;
        this.swmBonusPaymentInterfaceDao = swmBonusPaymentInterfaceDao;
        this.swmBonusPaymentInterfaceMapper = swmBonusPaymentInterfaceMapper;
        this.swmBonusPaymentDao = swmBonusPaymentDao;
        this.fndUserService = fndUserService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmBonusPaymentInterfaceDTO insert(SwmBonusPaymentInterface bonusPaymentInterfaceNew) {
        swmBonusPaymentInterfaceDao.insertAllColumn(bonusPaymentInterfaceNew);
        return swmBonusPaymentInterfaceMapper.toDto(bonusPaymentInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmBonusPaymentInterface bonusPaymentInterface = new SwmBonusPaymentInterface();
        bonusPaymentInterface.setId(id);
        this.delete(bonusPaymentInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmBonusPaymentInterface bonusPaymentInterface) {
        // TODO    确认删除前是否需要做检查
        swmBonusPaymentInterfaceDao.deleteByEntityKey(bonusPaymentInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmBonusPaymentInterface bonusPaymentInterfaceNew) {
        SwmBonusPaymentInterface bonusPaymentInterfaceInDb = Optional.ofNullable(swmBonusPaymentInterfaceDao.getByKey(bonusPaymentInterfaceNew.getId())).orElseGet(SwmBonusPaymentInterface::new);
        ValidationUtil.isNull(bonusPaymentInterfaceInDb.getId() ,"BonusPaymentInterface", "id", bonusPaymentInterfaceNew.getId());
        bonusPaymentInterfaceNew.setId(bonusPaymentInterfaceInDb.getId());
        swmBonusPaymentInterfaceDao.updateAllColumnByKey(bonusPaymentInterfaceNew);
    }

    @Override
    public SwmBonusPaymentInterfaceDTO getByKey(Long id) {
        SwmBonusPaymentInterface bonusPaymentInterface = Optional.ofNullable(swmBonusPaymentInterfaceDao.getByKey(id)).orElseGet(SwmBonusPaymentInterface::new);
        ValidationUtil.isNull(bonusPaymentInterface.getId() ,"BonusPaymentInterface", "id", id);
        return swmBonusPaymentInterfaceMapper.toDto(bonusPaymentInterface);
    }

    @Override
    public List<SwmBonusPaymentInterfaceDTO> listAll(SwmBonusPaymentInterfaceQueryCriteria criteria) {
        return swmBonusPaymentInterfaceMapper.toDto(swmBonusPaymentInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmBonusPaymentInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<SwmBonusPaymentInterface> page = PageUtil.startPage(pageable);
        List<SwmBonusPaymentInterface> bonusPaymentInterfaces = swmBonusPaymentInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmBonusPaymentInterfaceMapper.toDto(bonusPaymentInterfaces), page.getTotal());
    }

    @Override
    public void download(List<SwmBonusPaymentInterfaceDTO> bonusPaymentInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmBonusPaymentInterfaceDTO bonusPaymentInterfaceDTO : bonusPaymentInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id主键", bonusPaymentInterfaceDTO.getId());
            map.put("数据分组id", bonusPaymentInterfaceDTO.getGroupId());
            map.put("奖金名称", bonusPaymentInterfaceDTO.getBonusName());
            map.put("员工id", bonusPaymentInterfaceDTO.getEmployeeId());
            map.put("工牌号", bonusPaymentInterfaceDTO.getWorkCard());
            map.put("员工姓名", bonusPaymentInterfaceDTO.getEmployeeName());
            map.put("银行账户", bonusPaymentInterfaceDTO.getBankAccount());
            map.put("银行名称", bonusPaymentInterfaceDTO.getBankName());
            map.put("应发金额", bonusPaymentInterfaceDTO.getPayableAmount());
            map.put("实发金额_税前", bonusPaymentInterfaceDTO.getAmountPreTax());
            map.put("扣除所得税", bonusPaymentInterfaceDTO.getDeductIncomeTax());
            map.put("实发金额_税后", bonusPaymentInterfaceDTO.getAmountAfterTax());
            map.put("操作码", bonusPaymentInterfaceDTO.getOperationCode());
            map.put("错误信息", bonusPaymentInterfaceDTO.getErrorMsg());
            map.put("数据状态", bonusPaymentInterfaceDTO.getDataStatus());
            map.put("创建时间", bonusPaymentInterfaceDTO.getCreateTime());
            map.put("创建人id", bonusPaymentInterfaceDTO.getCreateBy());
            map.put("修改时间", bonusPaymentInterfaceDTO.getUpdateTime());
            map.put("修改人id", bonusPaymentInterfaceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<SwmBonusPaymentInterfaceDTO> insertExcel(List<SwmBonusPaymentInterface> swmBonusPaymentInterfaces, Boolean reImportFlag) {
        FndInterfaceOperationRecord fndInterfaceOperationRecord = new FndInterfaceOperationRecord();
        fndInterfaceOperationRecord.setOperationValue("insertBonusPayment");
        fndInterfaceOperationRecord.setOperationDescription("奖金发放导入");
        fndInterfaceOperationRecordService.insert(fndInterfaceOperationRecord);
        Long tempGroupId = fndInterfaceOperationRecord.getId();
        try {
            instance.instaceInsert(swmBonusPaymentInterfaces, fndInterfaceOperationRecord.getId(), reImportFlag);
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
        SwmBonusPaymentInterfaceQueryCriteria swmBonusPaymentInterfaceQueryCriteria = new SwmBonusPaymentInterfaceQueryCriteria();
        swmBonusPaymentInterfaceQueryCriteria.setGroupId(tempGroupId);
        swmBonusPaymentInterfaceQueryCriteria.setDataStatus("F");
        List<SwmBonusPaymentInterface> swmBonusPaymentInterfaceList = swmBonusPaymentInterfaceDao.listAllByCriteria(swmBonusPaymentInterfaceQueryCriteria);
        if (swmBonusPaymentInterfaceList.size() == 0) {
            swmBonusPaymentInterfaceList.add(new SwmBonusPaymentInterface().setGroupId(fndInterfaceOperationRecord.getId()));
        }
        return swmBonusPaymentInterfaceMapper.toDto(swmBonusPaymentInterfaceDao.listAllByCriteria(swmBonusPaymentInterfaceQueryCriteria));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void instaceInsert(List<SwmBonusPaymentInterface> swmBonusPaymentInterfaces, Long groupId, Boolean reImportFlag) {
        if (swmBonusPaymentInterfaces.size() > 0) {
            FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
            for (SwmBonusPaymentInterface swm : swmBonusPaymentInterfaces
            ) {
                swm.setGroupId(groupId);
                if (reImportFlag) {
                    swm.setErrorMsg("");
                    swm.setDataStatus("F");
                }
                //数据验证，该人员奖金信息是否已经存在于奖金人员表中，是则U，不是则C
                if(swmBonusPaymentInterfaceDao.ifExistsRecord(swm.getWorkCard(),swm.getBonusId())){
                swm.setOperationCode("U");
                }else swm.setOperationCode("C");
                swmBonusPaymentInterfaceDao.insertByExcel(swm);
            }
            // 获取当前bonuseId 是否已经存在数据
            SwmBonusPaymentQueryCriteria swmBonusPaymentQueryCriteria = new SwmBonusPaymentQueryCriteria();
            swmBonusPaymentQueryCriteria.setBonusId(swmBonusPaymentInterfaces.get(0).getBonusId());
            swmBonusPaymentDao.updateByInterface(groupId, user.getId());
            swmBonusPaymentDao.insertByInterface(groupId, user.getId(), swmBonusPaymentInterfaces.get(0).getBonusId());
        } else {
            throw new InfoCheckWarningMessException("Excel内没有检测到数据，不允许导入");
        }
    }

    @Override
    public List<SwmBonusPaymentInterface> getBonusPaymentSummaryByImportList(Set<String> workCards, Set<Long> groupIds) {
        return swmBonusPaymentInterfaceDao.getBonusPaymentSummaryByImportList(workCards, groupIds);
    }
}
