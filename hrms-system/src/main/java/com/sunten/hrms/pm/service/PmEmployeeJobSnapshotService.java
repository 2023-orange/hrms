package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeJobSnapshot;
import com.sunten.hrms.pm.dto.PmEmployeeJobSnapshotDTO;
import com.sunten.hrms.pm.dto.PmEmployeeJobSnapshotQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 部门科室岗位关系快照 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
public interface PmEmployeeJobSnapshotService extends IService<PmEmployeeJobSnapshot> {

    PmEmployeeJobSnapshotDTO insert(PmEmployeeJobSnapshot employeeJobSnapshotNew);

    void delete(Long id);

    void delete(PmEmployeeJobSnapshot employeeJobSnapshot);

    void update(PmEmployeeJobSnapshot employeeJobSnapshotNew);

    PmEmployeeJobSnapshotDTO getByKey(Long id);

    List<PmEmployeeJobSnapshotDTO> listAll(PmEmployeeJobSnapshotQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeJobSnapshotQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeJobSnapshotDTO> employeeJobSnapshotDTOS, HttpServletResponse response) throws IOException;

    void generateSnapShot();
}
