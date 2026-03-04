package com.sunten.hrms.re.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.re.domain.ReDemand;
import com.sunten.hrms.re.dto.ReDemandDTO;
import com.sunten.hrms.re.dto.ReDemandJobDTO;
import com.sunten.hrms.re.dto.ReDemandQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * <p>
 * 用人需求表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-04-22
 */
public interface ReDemandService extends IService<ReDemand> {

    ReDemandDTO insert(ReDemand demandNew);

    void delete(Long id);

    void delete(ReDemand demand);

    void update(ReDemand demandNew);

    void updateDemandByValue(ReDemand demandNew);

    ReDemandDTO getByKey(Long id);

    List<ReDemandDTO> listAll(ReDemandQueryCriteria criteria);

    Map<String, Object> listAll(ReDemandQueryCriteria criteria, Pageable pageable);

    void download(List<ReDemandDTO> demandDTOS, HttpServletResponse response) throws IOException;

    ReDemandDTO getLastRemand(String type);

    void disabledByEnabled(Long id);

    void repealDemand(Long id, String repealReason, String oaOrder);

    void updateOaReqAndCurrentNode(ReDemand demand);

    ReDemandDTO getDemandAndDemandJobWithOrder(ReDemandQueryCriteria demandQueryCriteria);

    void checkDemandJobIsAllPassByDemandId(Long demandId);

    ReDemandDTO getByDemandCode(String demandCode);

    List<ReDemandDTO> getDemandByPass();

    List<ReDemandJobDTO>  getCurrentStatusList(Long deptId,String demandCode);
}
