package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReAward;
import com.sunten.hrms.re.dto.ReAwardDTO;
import com.sunten.hrms.re.dto.ReAwardQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 奖罚情况表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
public interface ReAwardService extends IService<ReAward> {

    ReAwardDTO insert(ReAward awardNew);

    void delete(Long id);

    void delete(ReAward award);

    void update(ReAward awardNew);

    ReAwardDTO getByKey(Long id);

    List<ReAwardDTO> listAll(ReAwardQueryCriteria criteria);

    Map<String, Object> listAll(ReAwardQueryCriteria criteria, Pageable pageable);

    void download(List<ReAwardDTO> awardDTOS, HttpServletResponse response) throws IOException;

    List<ReAwardDTO> batchInsert(List<ReAward> reAwards,Long reId);
}
