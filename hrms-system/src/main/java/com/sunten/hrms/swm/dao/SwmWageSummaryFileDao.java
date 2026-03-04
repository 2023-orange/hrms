package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmWageSummaryFile;
import com.sunten.hrms.swm.dto.SwmWageSummaryFileDTO;
import com.sunten.hrms.swm.dto.SwmWageSummaryFileQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.swm.vo.CostCenterForSummaryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 工资汇总归档表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-12-25
 */
@Mapper
@Repository
public interface SwmWageSummaryFileDao extends BaseMapper<SwmWageSummaryFile> {

    int insertAllColumn(SwmWageSummaryFile wageSummaryFile);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmWageSummaryFile wageSummaryFile);

    int updateAllColumnByKey(SwmWageSummaryFile wageSummaryFile);

    SwmWageSummaryFile getByKey(@Param(value = "id") Long id);

    List<SwmWageSummaryFile> listAllByCriteria(@Param(value = "criteria") SwmWageSummaryFileQueryCriteria criteria);

    List<SwmWageSummaryFile> listAllByCriteriaPage(@Param(value = "page") Page<SwmWageSummaryFile> page, @Param(value = "criteria") SwmWageSummaryFileQueryCriteria criteria);

    int wageSummaryToFile(@Param(value = "incomePeriod")String incomePeriod);

    List<CostCenterForSummaryVo> listByCostCenterForSummary(@Param(value = "criteria")SwmWageSummaryFileQueryCriteria criteria);

    List<SwmWageSummaryFile> listByPerson(@Param(value = "incomePeriod") String incomePeriod);

    // 查看个人工资明细
    List<SwmWageSummaryFile> getGrantDetail(@Param(value = "employeeId") Long employeeId, @Param(value="year") String year);

    List<SwmWageSummaryFile> listStatistics(@Param(value = "workCard") String workCard);
}
