package com.sunten.hrms.re.service.impl;

import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.re.dao.ReDemandDao;
import com.sunten.hrms.re.dao.ReDemandJobDescribesDao;
import com.sunten.hrms.re.dao.ReEmpMesMonthlyDao;
import com.sunten.hrms.re.domain.ReDemandJob;
import com.sunten.hrms.re.dao.ReDemandJobDao;
import com.sunten.hrms.re.domain.ReDemandJobDescribes;
import com.sunten.hrms.re.dto.QueryUsedByTrialApprovalCriteria;
import com.sunten.hrms.re.service.ReDemandJobService;
import com.sunten.hrms.re.dto.ReDemandJobDTO;
import com.sunten.hrms.re.dto.ReDemandJobQueryCriteria;
import com.sunten.hrms.re.mapper.ReDemandJobMapper;
import com.sunten.hrms.re.vo.ThreeYearCount;
import com.sunten.hrms.re.vo.UsedByTrialApprovalDemandVo;
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
import java.util.stream.Collectors;

/**
 * <p>
 * 用人需求岗位子表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-04-23
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReDemandJobServiceImpl extends ServiceImpl<ReDemandJobDao, ReDemandJob> implements ReDemandJobService {
    private final ReDemandJobDao reDemandJobDao;
    private final ReDemandJobMapper reDemandJobMapper;
    private final ReEmpMesMonthlyDao reEmpMesMonthlyDao;
    private final FndUserService fndUserService;
    private final ReDemandJobDescribesDao reDemandJobDescribesDao;
    private final ReDemandDao reDemandDao;

    public ReDemandJobServiceImpl(ReDemandJobDao reDemandJobDao, ReDemandJobMapper reDemandJobMapper,
                                  ReEmpMesMonthlyDao reEmpMesMonthlyDao, FndUserService fndUserService, ReDemandJobDescribesDao reDemandJobDescribesDao,
                                  ReDemandDao reDemandDao) {
        this.reDemandJobDao = reDemandJobDao;
        this.reDemandJobMapper = reDemandJobMapper;
        this.reEmpMesMonthlyDao = reEmpMesMonthlyDao;
        this.fndUserService = fndUserService;
        this.reDemandJobDescribesDao = reDemandJobDescribesDao;
        this.reDemandDao = reDemandDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReDemandJobDTO insert(ReDemandJob demandJobNew) {
        ThreeYearCount jobCount = reEmpMesMonthlyDao.getThreeYearByJobId(demandJobNew.getJobId());
        if (null != jobCount) {
            if (null != jobCount.getFirstTotal()) {
                demandJobNew.setJobFirstCount(jobCount.getFirstTotal());
            }
            if (null != jobCount.getSecondTotal()) {
                demandJobNew.setJobSecondCount(jobCount.getSecondTotal());
            }
            if (null != jobCount.getThirdTotal()) {
                demandJobNew.setJobThirdCount(jobCount.getThirdTotal());
            }
            if (null != jobCount.getCurrentTotal()) {
                demandJobNew.setJobCurrentCount(jobCount.getCurrentTotal());
            }
        }
        reDemandJobDao.insertAllColumn(demandJobNew);
        return reDemandJobMapper.toDto(demandJobNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReDemandJob demandJob = new ReDemandJob();
        demandJob.setId(id);
        this.delete(demandJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReDemandJob demandJob) {
        // TODO    确认删除前是否需要做检查
        reDemandJobDao.deleteByEntityKey(demandJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReDemandJob demandJobNew) {
        ReDemandJob demandJobInDb = Optional.ofNullable(reDemandJobDao.getByKey(demandJobNew.getId())).orElseGet(ReDemandJob::new);
        ValidationUtil.isNull(demandJobInDb.getId() ,"DemandJob", "id", demandJobNew.getId());
        demandJobNew.setId(demandJobInDb.getId());
        reDemandJobDao.updateAllColumnByKey(demandJobNew);
    }

    @Override
    public ReDemandJobDTO getByKey(Long id) {
        ReDemandJob demandJob = Optional.ofNullable(reDemandJobDao.getByKey(id)).orElseGet(ReDemandJob::new);
        ValidationUtil.isNull(demandJob.getId() ,"DemandJob", "id", id);
        return reDemandJobMapper.toDto(demandJob);
    }

    @Override
    public List<ReDemandJobDTO> listAll(ReDemandJobQueryCriteria criteria) {
        return reDemandJobMapper.toDto(reDemandJobDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReDemandJobQueryCriteria criteria, Pageable pageable) {
        Page<ReDemandJob> page = PageUtil.startPage(pageable);
        List<ReDemandJob> demandJobs = reDemandJobDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reDemandJobMapper.toDto(demandJobs), page.getTotal());
    }

    @Override
    public void download(List<ReDemandJobDTO> demandJobDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReDemandJobDTO demandJobDTO : demandJobDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("用人需求id", demandJobDTO.getDemandId());
            map.put("岗位名称", demandJobDTO.getJobName());
            map.put("岗位人数", demandJobDTO.getQuantity());
            map.put("岗位素质要求", demandJobDTO.getBaseCondition());
            map.put("是否特殊岗位", demandJobDTO.getSpecialFlag());
            map.put("特殊入职体检项目", demandJobDTO.getSpecialMidecal());
            map.put("是否需要岗位说明书", demandJobDTO.getNeedFlag());
            map.put("生效标记", demandJobDTO.getEnabledFlag());
            map.put("id", demandJobDTO.getId());
            map.put("创建时间", demandJobDTO.getCreateTime());
            map.put("创建人ID", demandJobDTO.getCreateBy());
            map.put("修改时间", demandJobDTO.getUpdateTime());
            map.put("修改人ID", demandJobDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disabledByEnabled(Long id) {
        reDemandJobDao.disabledByEnabled(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateColumnByValue(ReDemandJob demandJob) {
        ReDemandJob demandJobOld = reDemandJobDao.getByKey(demandJob.getId());
        if (!demandJobOld.getNeedFlag() && demandJob.getNeedFlag()) {
            demandJob.setNeedCheckFlag(true);
        } else {
            demandJob.setNeedCheckFlag(false);
        }
        reDemandJobDao.updateColumnByValue(demandJob);
        // 检测需要检测标记是否与确认标记一致
        if (null != demandJob.getCheckAfterUpdateFlag() && demandJob.getCheckAfterUpdateFlag()) {
            Integer needCheckCount = reDemandJobDao.checkNeedCountAfterUpdate(demandJobOld.getDemandId());
            ReDemandJobQueryCriteria reDemandJobQueryCriteria = new ReDemandJobQueryCriteria();
            reDemandJobQueryCriteria.setDemandId(demandJobOld.getDemandId());
            reDemandJobQueryCriteria.setEnabledFlag(true);
            List<Long> jobIds = reDemandJobDao.listAllByCriteria(reDemandJobQueryCriteria).stream().map(ReDemandJob::getId).collect(Collectors.toList());
            Integer checkCount = reDemandJobDescribesDao.checkCheckFlagAfterUpdate(jobIds);
            if (checkCount.equals(needCheckCount)) {
                // 更新
                reDemandDao.updateAfterCompleteEditFlag(false, demandJobOld.getDemandId());
            } else {
                reDemandDao.updateAfterCompleteEditFlag(true, demandJobOld.getDemandId());
            }
        }

    }

    /**
     *  @author liangjw
     *  @since 2022/1/17 11:26
     *  试用审批专用的需求查询
     */
    @Override
    public List<UsedByTrialApprovalDemandVo> queryUsedByTrialApproval(QueryUsedByTrialApprovalCriteria criteria) {
        return reDemandJobDao.queryUsedByTrialApproval(criteria);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInUsedQuantityAfterUsed(Long id) {
        reDemandJobDao.updateInUsedQuantityAfterUsed(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassQuantityAfterUsed(Long id) {
        reDemandJobDao.updatePassQuantityAfterUsed(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInUsedQuantityAfterCancel(Long id) {
        reDemandJobDao.updateInUsedQuantityAfterCancel(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassQuantityByCharge(List<ReDemandJob> reDemandJobs) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        ReDemandJobQueryCriteria reDemandJobQueryCriteria = new ReDemandJobQueryCriteria();
        reDemandJobQueryCriteria.setEnabledFlag(true);
        reDemandJobQueryCriteria.setDemandId(reDemandJobs.get(0).getDemandId());
        List<ReDemandJob> oldReDemandJobList = reDemandJobDao.listAllByCriteria(reDemandJobQueryCriteria);
        List<ReDemandJob> sortOldList = oldReDemandJobList.stream().sorted(Comparator.comparing(ReDemandJob::getId)).collect(Collectors.toList());
        List<ReDemandJob> sortNewList = reDemandJobs.stream().sorted(Comparator.comparing(ReDemandJob::getId)).collect(Collectors.toList());
        for (int i = 0; i < sortOldList.size(); i++) {
            if (null != sortOldList.get(i).getPassQuantity() && !sortOldList.get(i).getPassQuantity().equals(sortNewList.get(i).getPassQuantity())) {
                // 发生变化更新在用及pass
                ReDemandJob reDemandJob = new ReDemandJob();
                reDemandJob.setId(sortOldList.get(i).getId());
                reDemandJob.setPassQuantity(sortNewList.get(i).getPassQuantity());
                reDemandJob.setNewInUsedQuantity(sortOldList.get(i).getInUsedQuantity() - (sortOldList.get(i).getPassQuantity() - sortNewList.get(i).getPassQuantity()));
                reDemandJob.setDemandOpenBy(user.getId());
                reDemandJobDao.updatePassQuantityByCharge(reDemandJob);
            }
        }
    }

    @Override
    public Boolean checkResidueQuantityBeforeTrialCommit(Long demandJobId) {
        return reDemandJobDao.checkResidueQuantityBeforeTrialCommit(demandJobId) > 0;
    }

    //    @Override
//    public List<ReDemandJobDTO> ListByDemandId(Long id) {
//        return null;
//    }
}
