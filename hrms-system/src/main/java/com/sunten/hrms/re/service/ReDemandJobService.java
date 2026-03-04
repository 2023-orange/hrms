package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReDemandJob;
import com.sunten.hrms.re.dto.QueryUsedByTrialApprovalCriteria;
import com.sunten.hrms.re.dto.ReDemandJobDTO;
import com.sunten.hrms.re.dto.ReDemandJobQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.re.vo.UsedByTrialApprovalDemandVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 用人需求岗位子表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-04-23
 */
public interface ReDemandJobService extends IService<ReDemandJob> {

    ReDemandJobDTO insert(ReDemandJob demandJobNew);

    void delete(Long id);

    void delete(ReDemandJob demandJob);

    void update(ReDemandJob demandJobNew);

    ReDemandJobDTO getByKey(Long id);

    List<ReDemandJobDTO> listAll(ReDemandJobQueryCriteria criteria);

    Map<String, Object> listAll(ReDemandJobQueryCriteria criteria, Pageable pageable);

    void download(List<ReDemandJobDTO> demandJobDTOS, HttpServletResponse response) throws IOException;

    void disabledByEnabled(Long id);

    void updateColumnByValue(ReDemandJob demandJob);
//    List<ReDemandJobDTO> ListByDemandId(Long id);
    List<UsedByTrialApprovalDemandVo> queryUsedByTrialApproval(QueryUsedByTrialApprovalCriteria criteria);
    // 其它关联模块申请单生成时调用
    void updateInUsedQuantityAfterUsed(Long id);
    // 其它关联模块申请单通过时调用
    void updatePassQuantityAfterUsed(Long id);
    // 其它关联模块申请单不通过或取消时调用
    void updateInUsedQuantityAfterCancel(Long id);
    // 开放用人需求
    void updatePassQuantityByCharge(List<ReDemandJob> reDemandJobs);

    Boolean checkResidueQuantityBeforeTrialCommit(Long demandJobId);


}
