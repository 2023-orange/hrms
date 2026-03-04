package com.sunten.hrms.pm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.pm.domain.PmEmployeeJobTransfer;
import com.sunten.hrms.pm.dto.PmEmployeeJobTransferDTO;
import com.sunten.hrms.pm.dto.PmEmployeeJobTransferQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 岗位调动表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeJobTransferService extends IService<PmEmployeeJobTransfer> {

    PmEmployeeJobTransferDTO insert(PmEmployeeJobTransfer employeeJobTransferNew);

    void delete(Long id);

    void delete(PmEmployeeJobTransfer employeeJobTransfer);

    void update(PmEmployeeJobTransfer employeeJobTransferNew);

    PmEmployeeJobTransferDTO getByKey(Long id);

    List<PmEmployeeJobTransferDTO> listAll(PmEmployeeJobTransferQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeJobTransferQueryCriteria criteria, Pageable pageable);

    void download(PmEmployeeJobTransferQueryCriteria criteria,Pageable pageable, HttpServletResponse response) throws IOException;

    boolean isPlanOrTransferring(Long employeeId, Long groupId);

    void updateEndTime(PmEmployeeJobTransfer employeeJobTransferNew);

    void finishTransfer(LocalDate date);
}
