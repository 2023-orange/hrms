package com.sunten.hrms.pm.dao;

import com.sunten.config.DataSourceKeyEnum;
import com.sunten.config.annotation.DataSource;
import com.sunten.hrms.pm.domain.Dxnj;
import com.sunten.hrms.pm.domain.PmUsedAnnualLeave;
import com.sunten.hrms.pm.dto.DxnjQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-10-08
 */
@Mapper
@Repository
public interface DxnjDao extends BaseMapper<Dxnj> {

    int insertAllColumn(Dxnj dxnj);

    int deleteByKey(@Param(value = "id") Integer id);

    int deleteByEntityKey(Dxnj dxnj);

    int updateAllColumnByKey(Dxnj dxnj);

    Dxnj getByKey(@Param(value = "id") Integer id);

    List<Dxnj> listAllByCriteria(@Param(value = "criteria") DxnjQueryCriteria criteria);

    List<Dxnj> listAllByCriteriaPage(@Param(value = "page") Page<Dxnj> page, @Param(value = "criteria") DxnjQueryCriteria criteria);

    void autoInsertDXNJEveryDay();

    @DataSource(DataSourceKeyEnum.OA)
    List<PmUsedAnnualLeave> getYXJTSFromOA(@Param(value = "workCards") Set<String> workCards);

    void updateDurationAnnualLeave(Dxnj dxnj);

    // 每年年度写死带薪年假
    void dxnjToFile();

    // 更新临时工的带薪年假为0
    void updateTempEmployeeDXNJ();

    void updateDxnjForTest(@Param(value="yxjts")Float yxjts, @Param(value="gph")String gph, @Param(value = "ND")Integer ND);

    @DataSource(DataSourceKeyEnum.OA)
    List<PmUsedAnnualLeave> getOaForTest();
}
