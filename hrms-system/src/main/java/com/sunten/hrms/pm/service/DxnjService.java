package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.Dxnj;
import com.sunten.hrms.pm.dto.DxnjDTO;
import com.sunten.hrms.pm.dto.DxnjQueryCriteria;
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
 * @since 2021-10-08
 */
public interface DxnjService extends IService<Dxnj> {

    DxnjDTO insert(Dxnj dxnjNew);

    void delete(Integer id);

    void delete(Dxnj dxnj);

    void update(Dxnj dxnjNew);

    DxnjDTO getByKey(Integer id);

    List<DxnjDTO> listAll(DxnjQueryCriteria criteria);

    Map<String, Object> listAll(DxnjQueryCriteria criteria, Pageable pageable);

    void download(List<DxnjDTO> dxnjDTOS, HttpServletResponse response) throws IOException;

    void autoInsertDXNJEveryDay();

    void dxnjToFile();

    void batchUpdate(List<Dxnj> dxnjs);

    void updateTempEmployeeDXNJ();
}
