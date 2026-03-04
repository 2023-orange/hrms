package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmTransferComment;
import com.sunten.hrms.pm.dto.PmTransferCommentDTO;
import com.sunten.hrms.pm.dto.PmTransferCommentQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 调动人员流转记录表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-05-24
 */
public interface PmTransferCommentService extends IService<PmTransferComment> {

    PmTransferCommentDTO insert(PmTransferComment transferCommentNew);

    void delete(Long id);

    void delete(PmTransferComment transferComment);

    void update(PmTransferComment transferCommentNew);

    PmTransferCommentDTO getByKey(Long id);

    List<PmTransferCommentDTO> listAll(PmTransferCommentQueryCriteria criteria);

    Map<String, Object> listAll(PmTransferCommentQueryCriteria criteria, Pageable pageable);

    void download(List<PmTransferCommentDTO> transferCommentDTOS, HttpServletResponse response) throws IOException;
}
