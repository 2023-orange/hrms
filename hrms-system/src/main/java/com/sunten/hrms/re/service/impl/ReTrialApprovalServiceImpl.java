package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.re.dao.ReTrialApprovalDao;
import com.sunten.hrms.re.domain.ReTrialApproval;
import com.sunten.hrms.re.dto.ReTrialApprovalDTO;
import com.sunten.hrms.re.dto.ReTrialApprovalQueryCriteria;
import com.sunten.hrms.re.mapper.ReTrialApprovalMapper;
import com.sunten.hrms.re.service.ReDemandJobService;
import com.sunten.hrms.re.service.ReTrialApprovalService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.SecurityUtils;
import com.sunten.hrms.utils.ValidationUtil;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 试用审批表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-04-25
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReTrialApprovalServiceImpl extends ServiceImpl<ReTrialApprovalDao, ReTrialApproval> implements ReTrialApprovalService {
    private final ReTrialApprovalDao reTrialApprovalDao;
    private final ReTrialApprovalMapper reTrialApprovalMapper;
    private final ReDemandJobService reDemandJobService;

    public ReTrialApprovalServiceImpl(ReTrialApprovalDao reTrialApprovalDao, ReTrialApprovalMapper reTrialApprovalMapper,
                                      ReDemandJobService reDemandJobService) {
        this.reTrialApprovalDao = reTrialApprovalDao;
        this.reTrialApprovalMapper = reTrialApprovalMapper;
        this.reDemandJobService = reDemandJobService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReTrialApprovalDTO insert(ReTrialApproval trialApprovalNew) {

        if (null == trialApprovalNew.getDemandJob() || null == trialApprovalNew.getDemandJob().getId()) {
            throw new InfoCheckWarningMessException("新增失败，用人需求岗位子表id缺失");
        } else {
            if (null != trialApprovalNew.getSubmitFlag() && trialApprovalNew.getSubmitFlag()) {
                // 岗位人数在用数量更新
                reDemandJobService.updateInUsedQuantityAfterUsed(trialApprovalNew.getDemandJob().getId());
            }
            reTrialApprovalDao.insertAllColumn(trialApprovalNew);
            return reTrialApprovalMapper.toDto(trialApprovalNew);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReTrialApproval trialApproval = new ReTrialApproval();
        trialApproval.setId(id);
        this.delete(trialApproval);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReTrialApproval trialApproval) {
        // TODO    确认删除前是否需要做检查
        reTrialApprovalDao.deleteByEntityKey(trialApproval);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReTrialApproval trialApprovalNew) {
        ReTrialApproval trialApprovalInDb = Optional.ofNullable(reTrialApprovalDao.getByKey(trialApprovalNew.getId())).orElseGet(ReTrialApproval::new);
        ValidationUtil.isNull(trialApprovalInDb.getId() ,"TrialApproval", "id", trialApprovalNew.getId());
        trialApprovalNew.setId(trialApprovalInDb.getId());
        if (!trialApprovalInDb.getSubmitFlag() && null != trialApprovalNew.getSubmitFlag() && trialApprovalNew.getSubmitFlag()) {
            // 保存转提交, 更新用人需求岗位在用数量
            if (null == trialApprovalNew.getDemandJob() || null == trialApprovalNew.getDemandJob().getId()) {
                throw new InfoCheckWarningMessException("新增失败，用人需求岗位子表id缺失");
            } else {
                reDemandJobService.updateInUsedQuantityAfterUsed(trialApprovalNew.getDemandJob().getId());
            }
        }
        reTrialApprovalDao.updateAllColumnByKey(trialApprovalNew);
    }

    @Override
    public ReTrialApprovalDTO getByKey(Long id) {
        ReTrialApproval trialApproval = Optional.ofNullable(reTrialApprovalDao.getByKey(id)).orElseGet(ReTrialApproval::new);
        ValidationUtil.isNull(trialApproval.getId() ,"TrialApproval", "id", id);
        return reTrialApprovalMapper.toDto(trialApproval);
    }

    @Override
    public List<ReTrialApprovalDTO> listAll(ReTrialApprovalQueryCriteria criteria) {
        return reTrialApprovalMapper.toDto(reTrialApprovalDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReTrialApprovalQueryCriteria criteria, Pageable pageable) {
        Page<ReTrialApproval> page = PageUtil.startPage(pageable);
        List<ReTrialApproval> trialApprovals = reTrialApprovalDao.listAllByCriteriaPage(page, criteria);
        if (trialApprovals.size() >0) {
        }
        List<ReTrialApprovalDTO> reTrialApprovalDTOS =  reTrialApprovalMapper.toDto(trialApprovals);
        if (trialApprovals.size() >0) {
        }
        return PageUtil.toPage(reTrialApprovalMapper.toDto(trialApprovals), page.getTotal());
    }

    @Override
    public void download(List<ReTrialApprovalDTO> trialApprovalDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReTrialApprovalDTO trialApprovalDTO : trialApprovalDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("用人需求编号", trialApprovalDTO.getDemand().getDemandCode());
            map.put("招聘人员", trialApprovalDTO.getTrialEmployee().getName());
            map.put("部门面试人", trialApprovalDTO.getInterviewBy());
            map.put("当前需求情况", trialApprovalDTO.getCurrentStatus());
            map.put("总评", trialApprovalDTO.getAppraise());
            map.put("面试详细评价", trialApprovalDTO.getInterviewContent());
            map.put("拟试用部门", trialApprovalDTO.getDemand().getDeptName());
            map.put("拟试用科室", trialApprovalDTO.getDemand().getOfficeName());
            map.put("拟试用班组", trialApprovalDTO.getDemand().getTeam());
            map.put("拟试用岗位", trialApprovalDTO.getDemandJob().getJobName());
            map.put("试用期工资", trialApprovalDTO.getSalary());
            map.put("试用时间（月）", trialApprovalDTO.getTrialTime());
            map.put("体检类型（普通、特殊）", trialApprovalDTO.getMedicalClass());
            map.put("撤销标识", trialApprovalDTO.getRepealFlag());
            map.put("撤销时间", trialApprovalDTO.getRepealDate());
            map.put("撤销意见", trialApprovalDTO.getRepealReason());
            map.put("最终审批意见", trialApprovalDTO.getApprovalResult());
            map.put("OA审批单号", trialApprovalDTO.getOaOrder());
            map.put("审批结束日期", trialApprovalDTO.getApprovalDate());
            map.put("试用情况（试用、未试用）", trialApprovalDTO.getTrialStatus());
            map.put("未试用情况描述", trialApprovalDTO.getFailDescribes());
            map.put("有效标识", trialApprovalDTO.getEnabledFlag());
            map.put("候选人入职时间（试用时填写）", trialApprovalDTO.getTrialEntryTime());
            map.put("未试用原因：个人原因、体检不合格、编制原因、其他原因", trialApprovalDTO.getTrialFailReason());
            map.put("id", trialApprovalDTO.getId());
            map.put("创建时间", trialApprovalDTO.getCreateTime());
            map.put("创建人", trialApprovalDTO.getCreateBy());
            map.put("最后修改时间", trialApprovalDTO.getUpdateTime());
            map.put("最后修改人", trialApprovalDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public ReTrialApprovalDTO getByReqCode(String reqCode) {
        ReTrialApproval reTrialApproval = reTrialApprovalDao.getByReqCode(reqCode);
        ValidationUtil.isNull(reTrialApproval.getOaOrder() ,"TrialApproval", "oa_order", reqCode);
        return reTrialApprovalMapper.toDto(reTrialApproval);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void repealTrialApproval(ReTrialApproval trialApprovalNew) {
        ReTrialApproval old = reTrialApprovalDao.getByKey(trialApprovalNew.getId());
        if (null == old) {
            throw new InfoCheckWarningMessException("当前记录不存在");
        } else if (old.getRepealFlag()) {
            throw new InfoCheckWarningMessException("当前记录已撤销，请勿重复操作");
        } else if (old.getApprovalResult() != null && !"".equals(old.getApprovalResult())) {
            throw new InfoCheckWarningMessException("当前记录已审批结束，无法执行撤销");
        }
        if (!old.getRepealFlag() && trialApprovalNew.getRepealFlag()) {
            // 释放在用数量
            reDemandJobService.updateInUsedQuantityAfterCancel(old.getDemandJob().getId());
        }
        reTrialApprovalDao.updateByRepealOperation(trialApprovalNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void writeOaApprovalResult(ReTrialApproval trialApproval) {
        ReTrialApproval oldReTrialApproval = reTrialApprovalDao.getByReqCode(trialApproval.getOaOrder());
        ValidationUtil.isNull(oldReTrialApproval.getOaOrder() ,"TrialApproval", "oa_order", trialApproval.getOaOrder());
        if (null != trialApproval.getApprovalResult() && !"".equals(trialApproval.getApprovalResult().trim())) {
            if (trialApproval.getApprovalResult().equals("pass")) {
                // 更新岗位pass的数量
                reDemandJobService.updatePassQuantityAfterUsed(trialApproval.getDemandJob().getId());
            }
            if (trialApproval.getApprovalResult().equals("notPass")) {
                reDemandJobService.updateInUsedQuantityAfterCancel(trialApproval.getDemandJob().getId());
            }
            oldReTrialApproval.setApprovalResult(trialApproval.getApprovalResult());
            reTrialApprovalDao.updateByApprovalEnd(oldReTrialApproval);
        } else {
            oldReTrialApproval.setApprovalNode(trialApproval.getApprovalNode());
            oldReTrialApproval.setApprovalEmployee(trialApproval.getApprovalEmployee());
//            oldReTrialApproval.setPastQuantity(trialApproval.getPastQuantity());
            oldReTrialApproval.setCurrentStatus(trialApproval.getCurrentStatus());
            oldReTrialApproval.setSalary(trialApproval.getSalary());
            reTrialApprovalDao.updateByApprovalUnderwar(oldReTrialApproval);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTrialAfterPass(ReTrialApproval trialApproval) {
        if (null != trialApproval.getTrialStatus()) {
            if (trialApproval.getTrialStatus().equals("试用")) {
                trialApproval.setFailDescribes(null);
                trialApproval.setTrialFailReason(null);
            } else {
                trialApproval.setTrialEntryTime(null);
            }
            trialApproval.setUpdateBy(SecurityUtils.getUserId());
            reTrialApprovalDao.updateTrialAfterPass(trialApproval);
        }
    }
}
