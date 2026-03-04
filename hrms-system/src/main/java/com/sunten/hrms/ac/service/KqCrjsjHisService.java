package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.KqCrjsjHis;
import com.sunten.hrms.ac.dto.KqCrjsjHisDTO;
import com.sunten.hrms.ac.dto.KqCrjsjHisQueryCriteria;
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
public interface KqCrjsjHisService extends IService<KqCrjsjHis> {

    KqCrjsjHisDTO insert(KqCrjsjHis kqCrjsjHisNew);

    void delete();

    void delete(KqCrjsjHis kqCrjsjHis);

    void update(KqCrjsjHis kqCrjsjHisNew);

    KqCrjsjHisDTO getByKey();

    List<KqCrjsjHisDTO> listAll(KqCrjsjHisQueryCriteria criteria);

    Map<String, Object> listAll(KqCrjsjHisQueryCriteria criteria, Pageable pageable);

    void download(List<KqCrjsjHisDTO> kqCrjsjHisDTOS, HttpServletResponse response) throws IOException;
}
