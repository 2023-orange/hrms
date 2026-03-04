package com.sunten.hrms.fnd.dao;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FndSimEmailDao {
    void simEmailSend();
}
