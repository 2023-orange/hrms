package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmTransferRequest;
import com.sunten.hrms.pm.dto.PmTransferRequestDTO;
import com.sunten.hrms.pm.dto.PmTransferRequestQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.tool.dto.ToolLocalStorageDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 岗位调动申请表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-05-24
 */
public interface PmTransferRequestService extends IService<PmTransferRequest> {

    PmTransferRequestDTO insert(PmTransferRequest transferRequestNew);

    void delete(Long id);

    void delete(PmTransferRequest transferRequest);

    void update(PmTransferRequest transferRequestNew);

    PmTransferRequestDTO getByKey(Long id);

    List<PmTransferRequestDTO> listAll(PmTransferRequestQueryCriteria criteria);

    Map<String, Object> listAll(PmTransferRequestQueryCriteria criteria, Pageable pageable);

    void download(List<PmTransferRequestDTO> transferRequestDTOS, HttpServletResponse response) throws IOException;
    // 根据OA申请单号获取信息
    PmTransferRequestDTO getByReqCode(String reqCode);
    // OA数据反写统一接口
    void writeOaApprovalResult(PmTransferRequest transferRequestNew);
    // 签署个人意见接口
    void updateSelfVerify(PmTransferRequest transferRequestNew);

    String getNowTransferCode(String transferType);
}
