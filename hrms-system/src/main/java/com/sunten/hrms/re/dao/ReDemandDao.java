package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReDemand;
import com.sunten.hrms.re.domain.ReDemandJob;
import com.sunten.hrms.re.dto.ReDemandQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 用人需求表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-04-22
 */
@Mapper
@Repository
public interface ReDemandDao extends BaseMapper<ReDemand> {

    int insertAllColumn(ReDemand demand);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReDemand demand);

    int updateAllColumnByKey(ReDemand demand);

    int updateByValue(ReDemand demand);

    ReDemand getByKey(@Param(value = "id") Long id);

    List<ReDemand> listAllByCriteria(@Param(value = "criteria") ReDemandQueryCriteria criteria);

    List<ReDemand> listAllByCriteriaPage(@Param(value = "page") Page<ReDemand> page, @Param(value = "criteria") ReDemandQueryCriteria criteria);

    ReDemand getLastRemand(@Param(value = "type") String type);

    int disabledByEnabled(@Param(value = "id")Long id);

    int updateOaReqAndCurrentNode(ReDemand reDemand);

    ReDemand getDemandAndDemandJobWithOrder(@Param(value = "criteria") ReDemandQueryCriteria criteria);

    Boolean checkDemandJobExistNotPassByDemandId(@Param(value = "id") Long id);

    void updateStatusDescribesAfterCheck(@Param(value = "id")Long id);

    void updateAfterCompleteEditFlag(@Param(value = "afterCompleteEditFlag")Boolean afterCompleteEditFlag, @Param(value = "id")Long id);

    ReDemand getByDemandCode(@Param(value = "demandCode") String demandCode); // 根据需求编号获取

    List<ReDemand> getDemandByPass(); // 获取所有通过的用人需求

}
