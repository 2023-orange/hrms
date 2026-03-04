package com.sunten.hrms.tool.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.tool.domain.ToolVerificationCode;
import com.sunten.hrms.tool.dto.ToolVerificationCodeQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2019-12-25
 */
@Mapper
@Repository
public interface ToolVerificationCodeDao extends BaseMapper<ToolVerificationCode> {

    int insertAllColumn(ToolVerificationCode verificationCode);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ToolVerificationCode verificationCode);

    int updateAllColumnByKey(ToolVerificationCode verificationCode);

    ToolVerificationCode getByKey(@Param(value = "id") Long id);

    List<ToolVerificationCode> listAllByCriteria(@Param(value = "criteria") ToolVerificationCodeQueryCriteria criteria);

    List<ToolVerificationCode> listAllByCriteriaPage(@Param(value = "page") Page<ToolVerificationCode> page, @Param(value = "criteria") ToolVerificationCodeQueryCriteria criteria);

    /**
     * 获取有效的验证码
     * @param scenes 业务场景，如重置密码，重置邮箱等等
     * @param type 类型
     * @param value 值
     * @return VerificationCode
     */
    ToolVerificationCode getEnableCode(@Param(value = "scenes") String scenes, @Param(value = "type") String type, @Param(value = "value") String value);

}
