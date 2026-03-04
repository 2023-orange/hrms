package com.sunten.hrms.fnd.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.fnd.dto.FndUserQueryCriteria;
import com.sunten.hrms.fnd.vo.DeptChargeEmail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@Mapper
@Repository
public interface FndUserDao extends BaseMapper<FndUser> {

    int insertAllColumn(FndUser user);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndUser user);

    int updateAllColumnByKey(FndUser user);

    FndUser getByKey(@Param(value = "id") Long id);

    List<FndUser> listAllByCriteria(@Param(value = "criteria") FndUserQueryCriteria criteria);

    List<FndUser> listAllByCriteriaPage(@Param(value = "page") Page<FndUser> page, @Param(value = "criteria") FndUserQueryCriteria criteria);

    FndUser getByUsername(String username);

    FndUser getByEmail(String email);

    void updatePass(@Param(value = "username") String username, @Param(value = "pass") String pass, @Param(value = "updateTime") LocalDateTime updateTime, @Param(value = "updateBy") Long updateBy);

    void updateEmail(@Param(value = "username") String username, @Param(value = "email") String email, @Param(value = "updateTime") LocalDateTime updateTime, @Param(value = "updateBy") Long updateBy);

    int updateAvatar(FndUser user);

    Boolean checkManagerByUserId(@Param(value="userId") Long userId);

    List<DeptChargeEmail> selectDeptChargeEmailList(@Param(value = "deptId") Long deptId);

    List<String> selectEmailListByRole(@Param(value="permission") String permission);

    String getEmailByEmployeeId(@Param(value = "employeeId") Long employeeId);

    void updateUserName(FndUser fndUser);

    void updateDescription(FndUser fndUser);

    String getNameByUserId(@Param(value = "id")Long id);

    List<FndUser> getuserNameAndAttribute();

    List<FndUser> getuserByNameOrWorkCard(@Param(value = "str")String str);

    void updateUserEmail(@Param(value = "employeeId")Long employeeId, @Param(value = "email") String email);

    Boolean checkUserHaveRole(@Param(value = "permission")String permission, @Param(value = "employeeId")Long employeeId);

    Long getDeptIdByWorkCard(@Param(value = "workCard")String workCard);

    Long getDepartmentIdByWorkCard(@Param(value = "workCard")String workCard);

    String getEmailByWorkCard(@Param(value = "workCard")String workCard);

    FndUser getByKeyEmployeeId(@Param(value = "id") Long employeeId);
}
