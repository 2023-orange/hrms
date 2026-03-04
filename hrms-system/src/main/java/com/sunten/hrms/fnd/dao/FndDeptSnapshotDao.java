package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndDeptSnapshot;
import com.sunten.hrms.fnd.dto.FndDeptSnapshotQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 组织架构快照 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Mapper
@Repository
public interface FndDeptSnapshotDao extends BaseMapper<FndDeptSnapshot> {

    int insertAllColumn(FndDeptSnapshot deptSnapshot);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndDeptSnapshot deptSnapshot);

    int updateAllColumnByKey(FndDeptSnapshot deptSnapshot);

    FndDeptSnapshot getByKey(@Param(value = "id") Long id);

    List<FndDeptSnapshot> listAllByCriteria(@Param(value = "criteria") FndDeptSnapshotQueryCriteria criteria);

    List<FndDeptSnapshot> listAllByCriteriaPage(@Param(value = "page") Page<FndDeptSnapshot> page, @Param(value = "criteria") FndDeptSnapshotQueryCriteria criteria);

    void insertSnapShot(FndDeptSnapshot fndDeptSnapshot);

    Integer countByDate(@Param(value = "date") LocalDate date);

    FndDeptSnapshot getTopByDate(LocalDate date);

    FndDeptSnapshot getByKeyAndDate(@Param(value = "id") Long id, @Param(value = "date") LocalDate date);

    List<FndDeptSnapshot> listAllWithAttendanceByCriteria(@Param(value = "date") LocalDate date);

    void updateAttendanceId(FndDeptSnapshot fndDeptSnapshot);



    List<FndDeptSnapshot> getSnapShotByCriteria(@Param(value = "criteria") FndDeptSnapshotQueryCriteria criteria);
}
