package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeePhoto;
import com.sunten.hrms.pm.dto.PmEmployeePhotoDTO;
import com.sunten.hrms.pm.dto.PmEmployeePhotoQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 人员图像表 服务类
 * </p>
 *
 * @author xukai
 * @since 2020-09-09
 */
public interface PmEmployeePhotoService extends IService<PmEmployeePhoto> {

    PmEmployeePhotoDTO insert(PmEmployeePhoto employeePhotoNew);

    void delete(Long id);

    void delete(PmEmployeePhoto employeePhoto);

    void update(PmEmployeePhoto employeePhotoNew);

    PmEmployeePhotoDTO getByKey(Long id);

    List<PmEmployeePhotoDTO> listAll(PmEmployeePhotoQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeePhotoQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeePhotoDTO> employeePhotoDTOS, HttpServletResponse response) throws IOException;

    void createPhotoAuto() throws IOException;
}
