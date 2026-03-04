package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdTeacher;
import com.sunten.hrms.td.dto.TdTeacherDTO;
import com.sunten.hrms.td.dto.TdTeacherQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.td.vo.TeacherVo;
import com.sunten.hrms.td.dto.TeachingReportQueryCriteria;
import com.sunten.hrms.td.vo.TeachingReportVo;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 培训讲师列表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-06-16
 */
public interface TdTeacherService extends IService<TdTeacher> {

    TdTeacherDTO insert(TdTeacher teacherNew);

    void delete(Long id);

    void delete(TdTeacher teacher);

    void update(TdTeacher teacherNew);

    TdTeacherDTO getByKey(Long id);

    List<TdTeacherDTO> listAll(TdTeacherQueryCriteria criteria);

    Map<String, Object> listAll(TdTeacherQueryCriteria criteria, Pageable pageable);

    void download(List<TdTeacherDTO> teacherDTOS, HttpServletResponse response) throws IOException;

    List<TeacherVo> listTeacherVo(TdTeacherQueryCriteria tdTeacherQueryCriteria);

    void downloadReport(List<TeachingReportVo> teacherDTOS, HttpServletResponse response) throws IOException;

    List<TeachingReportVo> listTeachingByCriteria(TeachingReportQueryCriteria criteria);
}
