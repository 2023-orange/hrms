package com.sunten.hrms.cm.service;

import com.sunten.hrms.cm.domain.CmDetail;
import com.sunten.hrms.cm.dto.CmDetailDTO;
import com.sunten.hrms.cm.dto.CmDetailQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.cm.vo.CmExcelVo;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 工衣明细表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2022-03-24
 */
public interface CmDetailService extends IService<CmDetail> {

    CmDetailDTO insert(CmDetail detailNew);

    void delete(Long id);

    void delete(CmDetail detail);

    void update(CmDetail detailNew);

    CmDetailDTO getByKey(Long id);

    List<CmDetailDTO> listAll(CmDetailQueryCriteria criteria);

    List<CmDetailDTO> listAllForSearch(CmDetailQueryCriteria criteria);

    Map<String, Object> listAll(CmDetailQueryCriteria criteria, Pageable pageable);

    void download(List<CmDetailDTO> detailDTOS, HttpServletResponse response) throws IOException;

    void downloadForYear(HttpServletResponse response)  throws IOException;

    void downloadForSearch(List<CmDetailDTO> detailDTOS, HttpServletResponse response)  throws IOException;

    Boolean checkBeforeDownloadCmReport();

    void cmBatchDisposeForDoc(List<CmDetail> cmDetails);

    void generateCmList();


}
