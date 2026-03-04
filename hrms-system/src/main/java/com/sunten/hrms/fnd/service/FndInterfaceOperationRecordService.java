package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.dto.FndInterfaceOperationRecordDTO;
import com.sunten.hrms.fnd.dto.FndInterfaceOperationRecordQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 接口操作记录表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-26
 */
public interface FndInterfaceOperationRecordService extends IService<FndInterfaceOperationRecord> {

    FndInterfaceOperationRecordDTO insert(FndInterfaceOperationRecord interfaceOperationRecordNew);

    void delete(Long id);

    void delete(FndInterfaceOperationRecord interfaceOperationRecord);

    void update(FndInterfaceOperationRecord interfaceOperationRecordNew);

    FndInterfaceOperationRecordDTO getByKey(Long id);

    List<FndInterfaceOperationRecordDTO> listAll(FndInterfaceOperationRecordQueryCriteria criteria);

    Map<String, Object> listAll(FndInterfaceOperationRecordQueryCriteria criteria, Pageable pageable);

    void download(List<FndInterfaceOperationRecordDTO> interfaceOperationRecordDTOS, HttpServletResponse response) throws IOException;

    List<FndInterfaceOperationRecordDTO> getListByOperationValue(String operationValue);
}
