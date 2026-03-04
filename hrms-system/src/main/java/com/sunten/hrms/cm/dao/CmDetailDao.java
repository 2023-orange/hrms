package com.sunten.hrms.cm.dao;

import com.sunten.hrms.cm.domain.CmDetail;
import com.sunten.hrms.cm.dto.CmDetailQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.cm.vo.CmExcelVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工衣明细表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2022-03-24
 */
@Mapper
@Repository
public interface CmDetailDao extends BaseMapper<CmDetail> {

    int insertAllColumn(CmDetail detail);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(CmDetail detail);

    int updateAllColumnByKey(CmDetail detail);

    List<CmExcelVo> getCmExportList();

    CmDetail getByKey(@Param(value = "id") Long id);

    List<CmDetail> listAllByCriteria(@Param(value = "criteria") CmDetailQueryCriteria criteria);

    List<CmDetail> listAllByCriteriaPage(@Param(value = "page") Page<CmDetail> page, @Param(value = "criteria") CmDetailQueryCriteria criteria);

    void generateCmList(Map<String, Object> map);

    void updateAfterExport(@Param(value = "userId")Long userId);

    void deleteByEnabledFlag(@Param(value = "id")Long id);
}
