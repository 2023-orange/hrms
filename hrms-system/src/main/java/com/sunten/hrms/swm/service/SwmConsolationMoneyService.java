package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmConsolationMoney;
import com.sunten.hrms.swm.dto.SwmConsolationMoneyDTO;
import com.sunten.hrms.swm.dto.SwmConsolationMoneyQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 慰问金表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-08-04
 */
public interface SwmConsolationMoneyService extends IService<SwmConsolationMoney> {

    SwmConsolationMoneyDTO insert(SwmConsolationMoney consolationMoneyNew);

    void delete(Long id);

    void delete(SwmConsolationMoney consolationMoney);

    void update(SwmConsolationMoney consolationMoneyNew);

    SwmConsolationMoneyDTO getByKey(Long id);

    List<SwmConsolationMoneyDTO> listAll(SwmConsolationMoneyQueryCriteria criteria);

    Map<String, Object> listAll(SwmConsolationMoneyQueryCriteria criteria, Pageable pageable);

    void download(List<SwmConsolationMoneyDTO> consolationMoneyDTOS, HttpServletResponse response) throws IOException;

    void removeConsolationMoney(Long id);

    void exportForApproval(List<SwmConsolationMoneyDTO> consolationMoneyDTOS, HttpServletResponse response, Long userId, SwmConsolationMoneyQueryCriteria swmConsolationMoneyQueryCriteria) throws  IOException;

    void importInterfaceToMain(Long groupId);

    void releasedInterfaceToMain(Long groupId);

    void releasedMoney(SwmConsolationMoney swmConsolationMoney);

    void notReleasedMoney(SwmConsolationMoney swmConsolationMoney);

    void BatchReleasedMoney(List<SwmConsolationMoney> swmConsolationMoney, Long updateBy);
//    Boolean checkHaveBornAfterChildPass(Long id);
//
//    void insertBornAfterChildPass(SwmConsolationMoney swmConsolationMoney);

    void sendReleasedEmail(List<SwmConsolationMoney> swmConsolationMoneys);

    SwmConsolationMoneyDTO getSwmConsolationMoneyByOaOrder(String oaOrder);

    void reBackExport(Long updateBy, Long id);

    void batchReBackExport(List<SwmConsolationMoney> swmConsolationMoneys, Long updateBy);

    void updateDateByChild(Long groupId);

    // 定时任务
    void autoCreateChildConsolation();

    List<SwmConsolationMoneyDTO> listForExportApproval(List<Long> ids);
}
