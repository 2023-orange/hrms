package com.sunten.hrms.fnd.dto;

import com.sunten.hrms.fnd.domain.AdvancedQuery;
import lombok.Data;

import java.util.List;

@Data
public class AdvancedCriteriaQuery {
    private String colName;

    private String symbol;

    private String value;

    // 解析后的高级查询内容
    private List<AdvancedQuery> advancedQuerys;
    // 前台传过来时的高级查询内容的JSON串
    private String advancedQuerysStr;
}
