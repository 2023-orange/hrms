package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReTitle;
import com.sunten.hrms.re.dto.ReTitleDTO;
import com.sunten.hrms.re.dto.ReTitleQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 职称情况表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
public interface ReTitleService extends IService<ReTitle> {

    ReTitleDTO insert(ReTitle titleNew);

    void delete(Long id);

    void delete(ReTitle title);

    void update(ReTitle titleNew);

    ReTitleDTO getByKey(Long id);

    List<ReTitleDTO> listAll(ReTitleQueryCriteria criteria);

    Map<String, Object> listAll(ReTitleQueryCriteria criteria, Pageable pageable);

    void download(List<ReTitleDTO> titleDTOS, HttpServletResponse response) throws IOException;

    List<ReTitleDTO> batchInsert(List<ReTitle> reTitles,Long reId);

    void insertByTempInterface(Long groupId);
}
