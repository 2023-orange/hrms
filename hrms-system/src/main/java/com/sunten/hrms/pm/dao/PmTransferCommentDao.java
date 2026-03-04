package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmTransferComment;
import com.sunten.hrms.pm.dto.PmTransferCommentQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 调动人员流转记录表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-05-24
 */
@Mapper
@Repository
public interface PmTransferCommentDao extends BaseMapper<PmTransferComment> {

    int insertAllColumn(PmTransferComment transferComment);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmTransferComment transferComment);

    int updateAllColumnByKey(PmTransferComment transferComment);

    PmTransferComment getByKey(@Param(value = "id") Long id);

    List<PmTransferComment> listAllByCriteria(@Param(value = "criteria") PmTransferCommentQueryCriteria criteria);

    List<PmTransferComment> listAllByCriteriaPage(@Param(value = "page") Page<PmTransferComment> page, @Param(value = "criteria") PmTransferCommentQueryCriteria criteria);
}
