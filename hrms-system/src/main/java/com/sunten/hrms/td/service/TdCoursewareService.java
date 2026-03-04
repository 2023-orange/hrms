package com.sunten.hrms.td.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.td.domain.TdCourseware;
import com.sunten.hrms.td.dto.TdCoursewareDTO;
import com.sunten.hrms.td.dto.TdCoursewareQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * <p>
 * 课件资料表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-06-18
 */
public interface TdCoursewareService extends IService<TdCourseware> {

    TdCoursewareDTO insert(TdCourseware coursewareNew);

    void delete(Long id);

    void delete(TdCourseware courseware);

    void update(TdCourseware coursewareNew);

    TdCoursewareDTO getByKey(Long id);

    List<TdCoursewareDTO> listAll(TdCoursewareQueryCriteria criteria);

    Map<String, Object> listAll(TdCoursewareQueryCriteria criteria, Pageable pageable);

    void download(List<TdCoursewareDTO> coursewareDTOS, HttpServletResponse response) throws IOException;

    TdCoursewareDTO attestationCourseware(TdCourseware tdCourseware, MultipartFile multipartFile);

    void approval(TdCourseware tdCourseware); // 审批

    TdCoursewareDTO getByOaOrder(String oaOrder);
}
