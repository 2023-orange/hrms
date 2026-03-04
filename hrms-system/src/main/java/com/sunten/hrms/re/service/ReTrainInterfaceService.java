package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReTrainInterface;
import com.sunten.hrms.re.dto.ReTrainInterfaceDTO;
import com.sunten.hrms.re.dto.ReTrainInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 培训记录临时表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
public interface ReTrainInterfaceService extends IService<ReTrainInterface> {

    ReTrainInterfaceDTO insert(ReTrainInterface trainInterfaceNew);

    void delete(Long id);

    void delete(ReTrainInterface trainInterface);

    void update(ReTrainInterface trainInterfaceNew);

    ReTrainInterfaceDTO getByKey(Long id);

    List<ReTrainInterfaceDTO> listAll(ReTrainInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(ReTrainInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<ReTrainInterfaceDTO> trainInterfaceDTOS, HttpServletResponse response) throws IOException;
}
