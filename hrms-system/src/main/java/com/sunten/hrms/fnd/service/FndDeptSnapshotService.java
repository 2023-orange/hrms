package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndDeptSnapshot;
import com.sunten.hrms.fnd.dto.FndDeptSnapshotDTO;
import com.sunten.hrms.fnd.dto.FndDeptSnapshotQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 组织架构快照 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
public interface FndDeptSnapshotService extends IService<FndDeptSnapshot> {

    FndDeptSnapshotDTO insert(FndDeptSnapshot deptSnapshotNew);

    void delete(Long id);

    void delete(FndDeptSnapshot deptSnapshot);

    void update(FndDeptSnapshot deptSnapshotNew);

    FndDeptSnapshotDTO getByKey(Long id);

    List<FndDeptSnapshotDTO> listAll(FndDeptSnapshotQueryCriteria criteria);

    Map<String, Object> listAll(FndDeptSnapshotQueryCriteria criteria, Pageable pageable);

    void download(List<FndDeptSnapshotDTO> deptSnapshotDTOS, HttpServletResponse response) throws IOException;

    void generateSnapShot();


}
