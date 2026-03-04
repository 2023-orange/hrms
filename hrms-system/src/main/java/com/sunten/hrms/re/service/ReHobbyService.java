package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReHobby;
import com.sunten.hrms.re.dto.ReHobbyDTO;
import com.sunten.hrms.re.dto.ReHobbyQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 技术爱好表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
public interface ReHobbyService extends IService<ReHobby> {

    ReHobbyDTO insert(ReHobby hobbyNew);

    void delete(Long id);

    void delete(ReHobby hobby);

    void update(ReHobby hobbyNew);

    ReHobbyDTO getByKey(Long id);

    List<ReHobbyDTO> listAll(ReHobbyQueryCriteria criteria);

    Map<String, Object> listAll(ReHobbyQueryCriteria criteria, Pageable pageable);

    void download(List<ReHobbyDTO> hobbyDTOS, HttpServletResponse response) throws IOException;

    List<ReHobbyDTO> batchInsert(List<ReHobby> reHobbies,Long reId);
}
