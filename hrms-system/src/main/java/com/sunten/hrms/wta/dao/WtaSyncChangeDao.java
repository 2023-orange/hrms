package com.sunten.hrms.wta.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WtaSyncChangeDao {
    void autoSyncChange();

    void autoSyncFndChange();
}
