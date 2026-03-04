package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.vo.FndAgencyVo;

import java.util.List;
import java.util.Map;

public interface FndAgencyService {
    Map<String, Object> buildTree(List<FndAgencyVo> menuDTOS);

    List<FndAgencyVo> getList();
}
