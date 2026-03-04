package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdTeacherApply;
import com.sunten.hrms.td.dto.TdTeacherApplyDTO;
import com.sunten.hrms.td.dto.TdTeacherApplyQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 讲师身份（申请）表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-06-15
 */
public interface TdTeacherApplyService extends IService<TdTeacherApply> {

    TdTeacherApplyDTO insert(TdTeacherApply teacherApplyNew);

    void delete(Long id);

    void delete(TdTeacherApply teacherApply);

    void update(TdTeacherApply teacherApplyNew);

    TdTeacherApplyDTO getByKey(Long id);

    List<TdTeacherApplyDTO> listAll(TdTeacherApplyQueryCriteria criteria);

    Map<String, Object> listAll(TdTeacherApplyQueryCriteria criteria, Pageable pageable);

    void download(List<TdTeacherApplyDTO> teacherApplyDTOS, HttpServletResponse response) throws IOException;

    TdTeacherApplyDTO attestationTeacher(TdTeacherApply teacherApplyNew, MultipartFile multipartFile);

    TdTeacherApplyDTO getByReqCode(String reqCode);

    void writeOaApprovalResult(TdTeacherApply teacherApply);
}
