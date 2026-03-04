package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmTransferRequestDao;
import com.sunten.hrms.pm.domain.PmTransferRequest;
import com.sunten.hrms.pm.dto.PmTransferRequestDTO;
import com.sunten.hrms.pm.dto.PmTransferRequestQueryCriteria;
import com.sunten.hrms.pm.mapper.PmTransferRequestMapper;
import com.sunten.hrms.pm.service.PmTransferRequestService;
import com.sunten.hrms.re.service.ReDemandJobService;
import com.sunten.hrms.swm.dao.SwmEmployeeDao;
import com.sunten.hrms.swm.domain.SwmEmployee;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 岗位调动申请表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-05-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmTransferRequestServiceImpl extends ServiceImpl<PmTransferRequestDao, PmTransferRequest> implements PmTransferRequestService {
    private final PmTransferRequestDao pmTransferRequestDao;
    private final PmTransferRequestMapper pmTransferRequestMapper;
    private final SwmEmployeeDao swmEmployeeDao;
    private final ReDemandJobService reDemandJobService;

    @Value("${file.maxSize}")
    private long maxSize;
    @Value("${file.path}")
    private String path;

    public PmTransferRequestServiceImpl(PmTransferRequestDao pmTransferRequestDao, PmTransferRequestMapper pmTransferRequestMapper,
                                        SwmEmployeeDao swmEmployeeDao, ReDemandJobService reDemandJobService) {
        this.pmTransferRequestDao = pmTransferRequestDao;
        this.pmTransferRequestMapper = pmTransferRequestMapper;
        this.swmEmployeeDao = swmEmployeeDao;
        this.reDemandJobService = reDemandJobService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmTransferRequestDTO insert(PmTransferRequest transferRequestNew) {
        PmTransferRequest old = pmTransferRequestDao.getByTransferCode(transferRequestNew.getTransferCode());
        if (old != null) throw new InfoCheckWarningMessException("该需求编号已存在，请重新提交");

        if (null != transferRequestNew.getSubmitFlag() && transferRequestNew.getSubmitFlag()
            && "对应员工需求审批表".equals(transferRequestNew.getReason())
            && !"".equals(transferRequestNew.getDemandCode())
            && transferRequestNew.getDemandJobId() != null  ) {
            // re_demand_job数据表内的占用人数加1
            reDemandJobService.updateInUsedQuantityAfterUsed(transferRequestNew.getDemandJobId());
        }

        pmTransferRequestDao.insertAllColumn(transferRequestNew);
        return pmTransferRequestMapper.toDto(transferRequestNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmTransferRequest transferRequest = new PmTransferRequest();
        transferRequest.setId(id);
        this.delete(transferRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmTransferRequest transferRequest) {
        // TODO    确认删除前是否需要做检查
        pmTransferRequestDao.deleteByEntityKey(transferRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmTransferRequest transferRequestNew) {
        PmTransferRequest transferRequestInDb = Optional.ofNullable(pmTransferRequestDao.getByKey(transferRequestNew.getId())).orElseGet(PmTransferRequest::new);
        ValidationUtil.isNull(transferRequestInDb.getId() ,"TransferRequest", "id", transferRequestNew.getId());
        transferRequestNew.setId(transferRequestInDb.getId());

        if (null != transferRequestInDb.getSubmitFlag() && !transferRequestInDb.getSubmitFlag()
                && null != transferRequestNew.getSubmitFlag() && transferRequestNew.getSubmitFlag()
                && "对应员工需求审批表".equals(transferRequestNew.getReason())
                && !"".equals(transferRequestNew.getDemandCode())
                && transferRequestNew.getDemandJobId() != null  ) {
            // 从保存(从编辑)转提交, re_demand_job数据表内的占用人数加1
            reDemandJobService.updateInUsedQuantityAfterUsed(transferRequestNew.getDemandJobId());
        }

        pmTransferRequestDao.updateAllColumnByKey(transferRequestNew);
    }

    @Override
    public PmTransferRequestDTO getByKey(Long id) {
        PmTransferRequest transferRequest = Optional.ofNullable(pmTransferRequestDao.getByKey(id)).orElseGet(PmTransferRequest::new);
        ValidationUtil.isNull(transferRequest.getId() ,"TransferRequest", "id", id);
        return pmTransferRequestMapper.toDto(transferRequest);
    }

    @Override
    public List<PmTransferRequestDTO> listAll(PmTransferRequestQueryCriteria criteria) {
        return pmTransferRequestMapper.toDto(pmTransferRequestDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmTransferRequestQueryCriteria criteria, Pageable pageable) {
        Page<PmTransferRequest> page = PageUtil.startPage(pageable);
        List<PmTransferRequest> transferRequests = pmTransferRequestDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmTransferRequestMapper.toDto(transferRequests), page.getTotal());
    }

    @Override
    public void download(List<PmTransferRequestDTO> transferRequestDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmTransferRequestDTO transferRequestDTO : transferRequestDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("调动编号", transferRequestDTO.getTransferCode());
            map.put("人员", transferRequestDTO.getPmEmployee().getName());
            map.put("调出岗薪资(永久调动用)", transferRequestDTO.getFromSalary());
            map.put("调入部门名称", transferRequestDTO.getToDeptName());
            map.put("调入科室名称", transferRequestDTO.getToOfficeName());
            map.put("调入部门id", transferRequestDTO.getToDeptId());
            map.put("调入岗位", transferRequestDTO.getToJobName());
            map.put("调入岗位id", transferRequestDTO.getToJobId());
            map.put("调入岗实习工资(永久调动用)", transferRequestDTO.getInternshipSalary());
            map.put("调入岗上岗工资(永久调动用)", transferRequestDTO.getFormalSalary());
            map.put("调岗原因", transferRequestDTO.getReason());
            map.put("本人意见", transferRequestDTO.getSelfIdea());
            map.put("本人意见填写日期", transferRequestDTO.getIdeaDate());
            map.put("最终审批结果", transferRequestDTO.getApprovalResult());
            map.put("OA单号", transferRequestDTO.getOaOrder());
            map.put("审批结束日期", transferRequestDTO.getApprovalDate());
            map.put("生效标记", transferRequestDTO.getEnabledFlag());
            map.put("当前审批节点", transferRequestDTO.getApprovalNode());
            map.put("审批人", transferRequestDTO.getApprovalEmployee());
            map.put("提交标识，0为保存，1为提交审批", transferRequestDTO.getSubmitFlag());
            map.put("对应员工需求审批表编号(永久调动用)", transferRequestDTO.getDemandCode());
            map.put("调岗其它原因描述", transferRequestDTO.getReasonOtherDescribes());
            map.put("借调起始日期", transferRequestDTO.getBeginDate());
            map.put("借调结束日期", transferRequestDTO.getEndDate());
            map.put("调出部门名称", transferRequestDTO.getFromDeptName());
            map.put("调出科室名称", transferRequestDTO.getFromOfficeName());
            map.put("调出部门id", transferRequestDTO.getFromDeptId());
            map.put("调出岗位", transferRequestDTO.getFromJobName());
            map.put("调出岗位id", transferRequestDTO.getFromJobId());
            map.put("long: 永久调动，short：借调", transferRequestDTO.getTransferType());
            map.put("体检类型，安委会审批时填写", transferRequestDTO.getMedicalType());
            map.put("体检结果", transferRequestDTO.getMedicalResult());
            map.put("attribute3", transferRequestDTO.getAttribute3());
            map.put("attribute4", transferRequestDTO.getAttribute4());
            map.put("attribute1", transferRequestDTO.getAttribute1());
            map.put("attribute5", transferRequestDTO.getAttribute5());
            map.put("attribute2", transferRequestDTO.getAttribute2());
            map.put("id", transferRequestDTO.getId());
            map.put("创建时间", transferRequestDTO.getCreateTime());
            map.put("创建人", transferRequestDTO.getCreateBy());
            map.put("最后修改时间", transferRequestDTO.getUpdateTime());
            map.put("最后修改人", transferRequestDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public PmTransferRequestDTO getByReqCode(String reqCode) {
        PmTransferRequest transferRequest = pmTransferRequestDao.getByReqCode(reqCode);
        SwmEmployee swmEmployee = swmEmployeeDao.getSwmEmpByEmpId(transferRequest.getPmEmployee().getId());
        transferRequest.setSwmEmployee(swmEmployee);
        return pmTransferRequestMapper.toDto(pmTransferRequestDao.getByReqCode(reqCode));
    }

    @Override
    public void writeOaApprovalResult(PmTransferRequest transferRequestNew) {
        PmTransferRequest transferRequest = Optional.ofNullable(pmTransferRequestDao.getByKey(transferRequestNew.getId())).orElseGet(PmTransferRequest::new);
        ValidationUtil.isNull(transferRequest.getId() ,"TransferRequest", "id", transferRequestNew.getId());
        transferRequestNew.setId(transferRequest.getId());
        // 如果最终审批意见栏有数据，说明该审批流程已结束
        if (transferRequestNew.getApprovalResult() != null && !"".equals(transferRequestNew.getApprovalResult())) {
//            transferRequest.setApprovalResult(transferRequestNew.getApprovalResult());
            // TODO 如果审批通过时，该申请单存在相关用人需求，则更新需求单的相关人数
            if (transferRequestNew.getDemandCode() != null && !"".equals(transferRequestNew.getDemandCode())) {

            }

            if (transferRequestNew.getApprovalResult().equals("pass")
                    && "对应员工需求审批表".equals(transferRequestNew.getReason())
                    && !"".equals(transferRequestNew.getDemandCode())
                    && transferRequestNew.getDemandJobId() != null
            ) {
                //re_demand_job数据表内的OA通过人数（或者叫达成人数）加1
                reDemandJobService.updatePassQuantityAfterUsed(transferRequestNew.getDemandJobId());
            }
            if (transferRequestNew.getApprovalResult().equals("notPass")
                    && "对应员工需求审批表".equals(transferRequestNew.getReason())
                    && !"".equals(transferRequestNew.getDemandCode())
                    && transferRequestNew.getDemandJobId() != null
            ) {
                //re_demand_job数据表内的占用人数减1
                reDemandJobService.updateInUsedQuantityAfterCancel(transferRequestNew.getDemandJobId());
            }

            pmTransferRequestDao.updateByApprovalEnd(transferRequestNew);
        } else {
            if (null == transferRequestNew.getApprovalNode() || "".equals(transferRequestNew.getApprovalNode())) {
                throw new InfoCheckWarningMessException("OA当前审批节点不能为空！");
            }
//            transferRequest.setMedicalType(transferRequestNew.getMedicalType());
//            transferRequest.setFromSalary(transferRequestNew.getFromSalary());
//            transferRequest.setFormalSalary(transferRequestNew.getFormalSalary());
//            transferRequest.setInternshipSalary(transferRequestNew.getInternshipSalary());
//            transferRequest.setApprovalNode(transferRequestNew.get)
            pmTransferRequestDao.updateByApprovalUnderwar(transferRequestNew);
        }
    }

    @Override
    public void updateSelfVerify(PmTransferRequest transferRequestNew) {
        pmTransferRequestDao.updateBySelfVerify(transferRequestNew);
    }

    @Override
    public String getNowTransferCode(String transferType) {
        // 生成规则，调动类型（L/S）+ 年月日 + 三位随机数,如果多次（设定999次）获取仍存在
        PmTransferRequest pmTransferRequest = null;
        String transferCode = null;
        int count = 0;
        int num = 3;
        do{
            if(count > 999){
                num++;
                count = 0;
            }
            transferCode = getTransferCode(transferType,num);
            pmTransferRequest = pmTransferRequestDao.getByTransferCode(transferCode);
            if (pmTransferRequest != null) transferCode = null;
            count++;
        } while (transferCode == null);

        return transferCode;
    }

    /**
     *
     * @param transferType
     * @param num
     * @return
     */
    public String getTransferCode(String transferType, int num){
        LocalDate nowDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateStr = nowDate.format(formatter);
        String randStr = "";
        for (int i=0; i<num; i++) {
            randStr += (int)(10*Math.random());
        }
        String transferCode = transferType + dateStr + randStr;
        return transferCode;
    }
}
