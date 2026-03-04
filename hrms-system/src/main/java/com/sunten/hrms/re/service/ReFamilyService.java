package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReFamily;
import com.sunten.hrms.re.dto.ReFamilyDTO;
import com.sunten.hrms.re.dto.ReFamilyQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 家庭情况表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
public interface ReFamilyService extends IService<ReFamily> {

    ReFamilyDTO insert(ReFamily familyNew);

    void delete(Long id);

    void delete(ReFamily family);

    void update(ReFamily familyNew);

    ReFamilyDTO getByKey(Long id);

    List<ReFamilyDTO> listAll(ReFamilyQueryCriteria criteria);

    Map<String, Object> listAll(ReFamilyQueryCriteria criteria, Pageable pageable);

    void download(List<ReFamilyDTO> familyDTOS, HttpServletResponse response) throws IOException;

    List<ReFamilyDTO> batchInsert(List<ReFamily> reFamilies,Long reId);
}
