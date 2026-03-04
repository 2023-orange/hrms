package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReEmpMesMonthly;
import com.sunten.hrms.re.dto.ReEmpMesMonthlyQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.re.vo.ThreeYearCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 每月人员情况存档 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2022-01-07
 */
@Mapper
@Repository
public interface ReEmpMesMonthlyDao extends BaseMapper<ReEmpMesMonthly> {

    int insertAllColumn(ReEmpMesMonthly empMesMonthly);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReEmpMesMonthly empMesMonthly);

    int updateAllColumnByKey(ReEmpMesMonthly empMesMonthly);

    ReEmpMesMonthly getByKey(@Param(value = "id") Long id);

    List<ReEmpMesMonthly> listAllByCriteria(@Param(value = "criteria") ReEmpMesMonthlyQueryCriteria criteria);

    List<ReEmpMesMonthly> listAllByCriteriaPage(@Param(value = "page") Page<ReEmpMesMonthly> page, @Param(value = "criteria") ReEmpMesMonthlyQueryCriteria criteria);

    void autoInsertEmpMesMonthly();

    // 取近三年的部门需求人数
    ThreeYearCount getThreeYearByDeptId(@Param(value = "deptId") Long deptId);
    // 取近三年的岗位需求人数
    ThreeYearCount getThreeYearByJobId(@Param(value = "jobId") Long jobId);

    Boolean checkBeforeAutoInsertEmpMesMonthly(@Param(value = "year") Integer year, @Param(value = "month")Integer month);


}
