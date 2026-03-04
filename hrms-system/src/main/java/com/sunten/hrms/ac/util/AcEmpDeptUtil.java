package com.sunten.hrms.ac.util;

import com.sunten.hrms.ac.dto.AcEmpDeptsDTO;
import com.sunten.hrms.ac.dto.AcEmpDeptsQueryCriteria;
import com.sunten.hrms.ac.service.AcEmpDeptsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AcEmpDeptUtil {
    private final AcEmpDeptsService acEmpDeptsService;
    @Value("${role.authDocumenter}")
    private String authDocumenter;

    public AcEmpDeptUtil(AcEmpDeptsService acEmpDeptsService) {
        this.acEmpDeptsService = acEmpDeptsService;
    }

    public Set<Long> getDocAuth(Long employeeId) {
        AcEmpDeptsQueryCriteria acEmpDeptsQueryCriteria = new AcEmpDeptsQueryCriteria();
        acEmpDeptsQueryCriteria.setDataType(authDocumenter);
        acEmpDeptsQueryCriteria.setEnabledFlag(true);
        acEmpDeptsQueryCriteria.setEmployeeId(employeeId);
        return acEmpDeptsService.listAll(acEmpDeptsQueryCriteria).stream().map(AcEmpDeptsDTO::getDeptId).collect(Collectors.toSet());
    }
}
