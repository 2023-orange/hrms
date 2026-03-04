package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.KqCrjsj;
import com.sunten.hrms.ac.dto.KqCrjsjDTO;
import com.sunten.hrms.ac.dto.KqCrjsjQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-19
 */
public interface KqCrjsjService extends IService<KqCrjsj> {

    KqCrjsjDTO insert(KqCrjsj kqCrjsjNew);

    void delete();

    void delete(KqCrjsj kqCrjsj);

    void update(KqCrjsj kqCrjsjNew);

    KqCrjsjDTO getByKey();

    List<KqCrjsjDTO> listAll(KqCrjsjQueryCriteria criteria);

    Map<String, Object> listAll(KqCrjsjQueryCriteria criteria, Pageable pageable);

    void download(List<KqCrjsjDTO> kqCrjsjDTOS, HttpServletResponse response) throws IOException;
}
