package com.sunten.hrms.re.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.re.domain.ReDemandAgreeNum;
import com.sunten.hrms.re.dto.ReDemandAgreeNumDTO;
import com.sunten.hrms.re.dto.ReDemandAgreeNumQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujy
 * @since 2022-11-22
 */
public interface ReDemandAgreeNumService extends IService<ReDemandAgreeNum> {

    ReDemandAgreeNumDTO insert(ReDemandAgreeNum reDemandAgreeNumNew);

    void delete(Long id);

    void delete(ReDemandAgreeNum reDemandAgreeNum);

    ReDemandAgreeNumDTO getByKey(Long id);

    List<ReDemandAgreeNumDTO> listAll(ReDemandAgreeNumQueryCriteria criteria);

    Map<String, Object> listAll(ReDemandAgreeNumQueryCriteria criteria, Pageable pageable);

    void download(List<ReDemandAgreeNumDTO> reDemandAgreeNumDTOS, HttpServletResponse response) throws IOException;
}
