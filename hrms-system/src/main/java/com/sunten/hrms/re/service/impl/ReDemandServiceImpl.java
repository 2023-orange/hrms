package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.fnd.dao.FndDeptDao;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.re.dao.ReDemandDao;
import com.sunten.hrms.re.dao.ReDemandJobDao;
import com.sunten.hrms.re.dao.ReEmpMesMonthlyDao;
import com.sunten.hrms.re.domain.ReDemand;
import com.sunten.hrms.re.dto.ReDemandDTO;
import com.sunten.hrms.re.dto.ReDemandJobDTO;
import com.sunten.hrms.re.dto.ReDemandQueryCriteria;
import com.sunten.hrms.re.mapper.ReDemandJobMapper;
import com.sunten.hrms.re.mapper.ReDemandMapper;
import com.sunten.hrms.re.service.ReDemandService;
import com.sunten.hrms.re.vo.ThreeYearCount;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 用人需求表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-04-22
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReDemandServiceImpl extends ServiceImpl<ReDemandDao, ReDemand> implements ReDemandService {
    private final ReDemandDao reDemandDao;
    private final ReDemandMapper reDemandMapper;
    private final ReDemandJobDao reDemandJobDao;
    private final ReDemandJobMapper reDemandJobMapper;
    private final FndDeptDao fndDeptDao;
    private final ReEmpMesMonthlyDao reEmpMesMonthlyDao;

    public ReDemandServiceImpl(ReDemandDao reDemandDao, ReDemandMapper reDemandMapper, ReDemandJobDao reDemandJobDao, ReDemandJobMapper reDemandJobMapper, FndDeptDao fndDeptDao,
                               ReEmpMesMonthlyDao reEmpMesMonthlyDao) {
        this.reDemandDao = reDemandDao;
        this.reDemandMapper = reDemandMapper;
        this.reDemandJobDao = reDemandJobDao;
        this.reDemandJobMapper = reDemandJobMapper;
        this.fndDeptDao = fndDeptDao;
        this.reEmpMesMonthlyDao = reEmpMesMonthlyDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReDemandDTO insert(ReDemand demandNew) {
        // 获取近三年部门需求人数
        ThreeYearCount threeYearCount = reEmpMesMonthlyDao.getThreeYearByDeptId(demandNew.getDeptId());
        if (null != threeYearCount) {
            if (null != threeYearCount.getFirstTotal()) {
                demandNew.setDeptFirstCount(threeYearCount.getFirstTotal());
            }
            if (null != threeYearCount.getSecondTotal()) {
                demandNew.setDeptSecondCount(threeYearCount.getSecondTotal());
            }
            if (null != threeYearCount.getThirdTotal()) {
                demandNew.setDeptThirdCount(threeYearCount.getThirdTotal());
            }
            if (null != threeYearCount.getCurrentTotal()) {
                demandNew.setDeptCurrentCount(threeYearCount.getCurrentTotal());
            }
        }
        reDemandDao.insertAllColumn(demandNew);
        return reDemandMapper.toDto(demandNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReDemand demand = new ReDemand();
        demand.setId(id);
        this.delete(demand);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReDemand demand) {
        // TODO    确认删除前是否需要做检查
        reDemandDao.deleteByEntityKey(demand);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReDemand demandNew) {
        ReDemand demandInDb = Optional.ofNullable(reDemandDao.getByKey(demandNew.getId())).orElseGet(ReDemand::new);
        ValidationUtil.isNull(demandInDb.getId() ,"Demand", "id", demandNew.getId());
        demandNew.setId(demandInDb.getId());
        log.debug(demandNew.toString());
        FndDept dept = fndDeptDao.getByKey(demandNew.getDeptId());
        demandNew.setDeptName(dept.getExtDeptName());
        if (null != dept.getExtDepartmentName()) {
            demandNew.setOfficeName(dept.getExtDepartmentName());
        }
        if (null != dept.getExtTeamName()) {
            demandNew.setTeam(dept.getExtTeamName());
        }
        reDemandDao.updateAllColumnByKey(demandNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDemandByValue(ReDemand demandNew) {
        reDemandDao.updateByValue(demandNew);
    }

    @Override
    public ReDemandDTO getByKey(Long id) {
        ReDemand demand = Optional.ofNullable(reDemandDao.getByKey(id)).orElseGet(ReDemand::new);
        ValidationUtil.isNull(demand.getId() ,"Demand", "id", id);
        return reDemandMapper.toDto(demand);
    }

    @Override
    public List<ReDemandDTO> listAll(ReDemandQueryCriteria criteria) {
        return reDemandMapper.toDto(reDemandDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReDemandQueryCriteria criteria, Pageable pageable) {
        Page<ReDemand> page = PageUtil.startPage(pageable);
        List<ReDemand> demands = reDemandDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reDemandMapper.toDto(demands), page.getTotal());
    }

    @Override
    public void download(List<ReDemandDTO> demandDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReDemandDTO demandDTO : demandDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("需求编号", demandDTO.getDemandCode());
            map.put("部门名称", demandDTO.getDeptName());
            map.put("科室名称", demandDTO.getOfficeName());
            map.put("部门科室id", demandDTO.getDeptId());
            map.put("招聘类别", demandDTO.getDemandClass());
            map.put("往期人数", demandDTO.getPastQuantity());
            map.put("当前需求情况", demandDTO.getCurrentStatus());
            map.put("招聘来源", demandDTO.getRecruitmentSource());
            map.put("申报理由", demandDTO.getDemandReason());
            map.put("发起人id", demandDTO.getDemandBy());
            map.put("撤销标识", demandDTO.getRepealFlag());
            map.put("撤销时间", demandDTO.getRepealDate());
            map.put("撤销意见", demandDTO.getRepealReason());
            map.put("审批意见", demandDTO.getApprovalResult());
            map.put("OA单号", demandDTO.getOaOrder());
            map.put("审批日期", demandDTO.getApprovalDate());
            map.put("需求状态", demandDTO.getDemandStatus());
            map.put("需求状态描述", demandDTO.getStatusDescribes());
            map.put("招聘过程记录", demandDTO.getReRemake());
            map.put("有效标记", demandDTO.getEnabledFlag());
            map.put("申报类别", demandDTO.getDemandReasonType());
            map.put("用人需求状态（用于控制页面，notCommit、inApproval、notPass、complete）", demandDTO.getRealDemandStatus());
            map.put("id", demandDTO.getId());
            map.put("创建时间", demandDTO.getCreateTime());
            map.put("创建人ID", demandDTO.getCreateBy());
            map.put("修改时间", demandDTO.getUpdateTime());
            map.put("修改人ID", demandDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public ReDemandDTO getLastRemand(String type) {
        return reDemandMapper.toDto(reDemandDao.getLastRemand(type));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disabledByEnabled(Long id) {
        reDemandDao.disabledByEnabled(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void repealDemand(Long id, String repealReason, String oaOrder) {
        ReDemand demand = new ReDemand();
        demand.setId(id);
        demand.setRealDemandStatus("repeal");
        demand.setRepealReason(repealReason);
        demand.setRepealFlag(true);
        LocalDateTime now = LocalDateTime.now();
        demand.setRepealDate(now);
        demand.setOaOrder(oaOrder);
        reDemandDao.updateByValue(demand);
        //TODO 请求OA流程撤销
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOaReqAndCurrentNode(ReDemand demand) {
        reDemandDao.updateOaReqAndCurrentNode(demand);
    }

    @Override
    public ReDemandDTO getDemandAndDemandJobWithOrder(ReDemandQueryCriteria reDemandQueryCriteria) {
        ReDemand reDemand = reDemandDao.getDemandAndDemandJobWithOrder(reDemandQueryCriteria);
//        ThreeYearCount deptThreeYearCount;
//        // 根据部门id获取三年、根据岗位获取三年
//        if (null != reDemand) {
//            if (null != reDemand.getDeptId()){
//                deptThreeYearCount = reEmpMesMonthlyDao.getThreeYearByDeptId(reDemand.getDeptId());
//                if (null != deptThreeYearCount) {
//                    if (null != deptThreeYearCount.getFirstTotal()) {
//                        reDemand.setDeptFirstCount(deptThreeYearCount.getFirstTotal());
//                    }
//                    if (null != deptThreeYearCount.getSecondTotal()) {
//                        reDemand.setDeptSecondCount(deptThreeYearCount.getSecondTotal());
//                    }
//                    if (null != deptThreeYearCount.getThirdTotal()) {
//                        reDemand.setDeptThirdCount(deptThreeYearCount.getThirdTotal());
//                    }
//                }
//            }
//            if (null != reDemand.getDemandJobList() && reDemand.getDemandJobList().size() > 0) {
//                ThreeYearCount jobCount;
//                for (ReDemandJob reJob : reDemand.getDemandJobList()
//                     ) {
//                    jobCount = reEmpMesMonthlyDao.getThreeYearByJobId(reJob.getJobId());
//                    if (null != jobCount) {
//                        if (null != jobCount.getFirstTotal()) {
//                            reJob.setJobFirstCount(jobCount.getFirstTotal());
//                        }
//                        if (null != jobCount.getSecondTotal()) {
//                            reJob.setJobSecondCount(jobCount.getSecondTotal());
//                        }
//                        if (null != jobCount.getThirdTotal()) {
//                            reJob.setJobThirdCount(jobCount.getThirdTotal());
//                        }
//                    }
//                }
//            }
//        }
        return reDemandMapper.toDto(reDemandDao.getDemandAndDemandJobWithOrder(reDemandQueryCriteria));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkDemandJobIsAllPassByDemandId(Long demandId) {
        if (!reDemandDao.checkDemandJobExistNotPassByDemandId(demandId)) {
            // 更新需求状态描述
            reDemandDao.updateStatusDescribesAfterCheck(demandId);
        }
    }

    @Override
    public ReDemandDTO getByDemandCode(String demandCode) {
        return reDemandMapper.toDto(reDemandDao.getByDemandCode(demandCode));
    }

    @Override
    public List<ReDemandDTO> getDemandByPass() {
        return reDemandMapper.toDto(reDemandDao.getDemandByPass());
    }

    @Override
    public List<ReDemandJobDTO> getCurrentStatusList(Long deptId,String demandCode) {
        return reDemandJobMapper.toDto(reDemandJobDao.getCurrentStatusList(deptId,demandCode));
    }
}
