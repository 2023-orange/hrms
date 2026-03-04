package com.sunten.hrms.fnd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.fnd.domain.FndAttachedDocument;
import com.sunten.hrms.fnd.dto.FndAttachedDocumentDTO;
import com.sunten.hrms.fnd.dto.FndAttachedDocumentQueryCriteria;
import com.sunten.hrms.fnd.vo.FndAttachedDocumentVo;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 附件表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-09-25
 */
public interface FndAttachedDocumentService extends IService<FndAttachedDocument> {

    FndAttachedDocumentDTO insert(FndAttachedDocument attachedDocumentNew);

    void delete(Long id);

    void delete(FndAttachedDocument attachedDocument);

    void update(FndAttachedDocument attachedDocumentNew);

    FndAttachedDocumentDTO getByKey(Long id);

    List<FndAttachedDocumentDTO> listAll(FndAttachedDocumentQueryCriteria criteria);

    Map<String, Object> listAll(FndAttachedDocumentQueryCriteria criteria, Pageable pageable);

    List<FndAttachedDocumentVo> listAttachedDoc(FndAttachedDocumentQueryCriteria criteria);

    void download(List<FndAttachedDocumentDTO> attachedDocumentDTOS, HttpServletResponse response) throws IOException;

    void uploadAttachedDoc(MultipartFile multipartFile, String source, Long sourceId, String type);
}
