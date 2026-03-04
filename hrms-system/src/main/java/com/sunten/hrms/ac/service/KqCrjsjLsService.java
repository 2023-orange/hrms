package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.KqCrjsjLs;
import com.sunten.hrms.ac.dto.KqCrjsjLsDTO;
import com.sunten.hrms.ac.dto.KqCrjsjLsQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
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
public interface KqCrjsjLsService extends IService<KqCrjsjLs> {

    KqCrjsjLsDTO insert(KqCrjsjLs kqCrjsjLsNew);

    void delete(LocalDateTime date8, String kh, String mjkzqbh, LocalDateTime time8);

    void delete(KqCrjsjLs kqCrjsjLs);

    void update(KqCrjsjLs kqCrjsjLsNew);

    KqCrjsjLsDTO getByKey(LocalDateTime date8, String kh, String mjkzqbh, LocalDateTime time8);

    List<KqCrjsjLsDTO> listAll(KqCrjsjLsQueryCriteria criteria);

    Map<String, Object> listAll(KqCrjsjLsQueryCriteria criteria, Pageable pageable);

    void download(List<KqCrjsjLsDTO> kqCrjsjLsDTOS, HttpServletResponse response) throws IOException;
}
