package com.sunten.hrms.ac.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunten.config.DataSourceKeyEnum;
import com.sunten.config.annotation.DataSource;
import com.sunten.hrms.ac.domain.AcLeaveForm;
import com.sunten.hrms.ac.domain.AcReqFlowdata;
import com.sunten.hrms.ac.domain.AcReqParameter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 打卡记录表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Mapper
@Repository
@DataSource(DataSourceKeyEnum.OA)
public interface AcLeaveFormDao extends BaseMapper<AcLeaveForm> {

    List<AcLeaveForm> getLeaveFormByDate(@Param(value = "dateFrom") LocalDate dateFrom, @Param(value = "dateTo") LocalDate dateTo);
    //检查下拉表格
    List<AcReqParameter> getReqParameter(@Param(value = "workcard") String workcard);
    //更新下拉表格
    void updateReqParameter(@Param(value = "id") Long id);
    //检查固定表格
    List<AcReqFlowdata> getReqFlowdata(@Param(value = "workcard") String workcard);
    //更新固定表格
    void updateReqFlowdata(@Param(value = "id") Long id);
    //检查在走流程

}
