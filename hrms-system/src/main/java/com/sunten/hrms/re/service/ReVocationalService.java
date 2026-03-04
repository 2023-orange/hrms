package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReVocational;
import com.sunten.hrms.re.dto.ReVocationalDTO;
import com.sunten.hrms.re.dto.ReVocationalQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 招聘职业资格表 服务类
 * </p>
 *
 * @author xukai
 * @since 2020-08-28
 */
public interface ReVocationalService extends IService<ReVocational> {

    ReVocationalDTO insert(ReVocational vocationalNew);

    void delete(Long id);

    void delete(ReVocational vocational);

    void update(ReVocational vocationalNew);

    ReVocationalDTO getByKey(Long id);

    List<ReVocationalDTO> listAll(ReVocationalQueryCriteria criteria);

    Map<String, Object> listAll(ReVocationalQueryCriteria criteria, Pageable pageable);

    void download(List<ReVocationalDTO> vocationalDTOS, HttpServletResponse response) throws IOException;

    List<ReVocationalDTO> batchInsert(List<ReVocational> reVocationals,Long reId);
}
