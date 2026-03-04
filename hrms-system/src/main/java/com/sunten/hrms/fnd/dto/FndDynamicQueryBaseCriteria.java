package com.sunten.hrms.fnd.dto;

import com.alibaba.fastjson.JSON;
import com.sunten.hrms.fnd.domain.DynamicQuery;
import com.sunten.hrms.fnd.domain.DynamicQueryCriterion;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.utils.SpringContextHolder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author batan
 * @since 2022-07-26
 */
@Data
@Slf4j
public class FndDynamicQueryBaseCriteria implements Serializable {
    private List<DynamicQueryCriterion> dynamicQueryCriteria;
    // 前台传过来时的高级查询内容的JSON串
    private String dynamicQueryCriteriaStr;

    public void setDynamicQueryCriteriaStr(String dynamicQueryCriteriaStr) {
        log.debug("FndDynamicQueryBaseCriteria.setDynamicQueryCriteriaStr");
        this.dynamicQueryCriteriaStr = dynamicQueryCriteriaStr;
        this.dynamicQueryCriteria = generateDynamicQueryCriteria(dynamicQueryCriteriaStr);
    }


    public List<DynamicQueryCriterion> generateDynamicQueryCriteria(String dynamicQueryCriteriaStr) {
        if (null != dynamicQueryCriteriaStr && !"".equals(dynamicQueryCriteriaStr)) {
            DynamicQuery dynamicQuery = JSON.parseObject(dynamicQueryCriteriaStr, DynamicQuery.class);
            log.debug(dynamicQuery.toString());
            List<DynamicQueryCriterion> dynamicQueryCriteria = new ArrayList<>();
            String parentPrefix = "";
            generateDynamicQueryCriteria(dynamicQuery, dynamicQueryCriteria, parentPrefix);
            log.debug(dynamicQueryCriteria.toString());
            return dynamicQueryCriteria;
        } else {
            return null;
        }
    }

    public boolean generateDynamicQueryCriteria(DynamicQuery dynamicQuery, List<DynamicQueryCriterion> dynamicQueryCriteria, String parentPrefix) {
        AtomicBoolean haveGeneratedQueryCriterion = new AtomicBoolean(false);
        if (null != dynamicQuery) {
            if (null != dynamicQuery.getCondition()) {
                for (DynamicQuery dq : dynamicQuery.getRules()) {
                    if (null != dq) {
                        if (null == dq.getCondition()) {
                            if (null != dq.getField() && null != dq.getOperator() && null != dq.getValue()) {
                                String prefix = generatePrefix(dynamicQuery, parentPrefix, haveGeneratedQueryCriterion.get());
                                toDynamicQueryCriterion(dynamicQueryCriteria, dq, prefix);
                                haveGeneratedQueryCriterion.set(true);
                            }
                        } else {
                            String parentPre = generatePrefix(dynamicQuery, parentPrefix, haveGeneratedQueryCriterion.get());
                            boolean recHaveGeneratedQueryCriterion = generateDynamicQueryCriteria(dq, dynamicQueryCriteria, parentPre);
                            haveGeneratedQueryCriterion.set(haveGeneratedQueryCriterion.get() || recHaveGeneratedQueryCriterion);
                        }
                    }
                }
                if (haveGeneratedQueryCriterion.get() && null != dynamicQueryCriteria && dynamicQueryCriteria.size() > 0) {
                    DynamicQueryCriterion dynamicQueryCriterion = dynamicQueryCriteria.get(dynamicQueryCriteria.size() - 1);
                    dynamicQueryCriterion.setSuffix((null == dynamicQueryCriterion.getSuffix() ? "" : dynamicQueryCriterion.getSuffix()) + ")");
                }
            } else {
                if (null == dynamicQuery.getLevels()) {
                    dynamicQueryCriteria.removeAll(dynamicQueryCriteria);
                    if (null != dynamicQuery.getField() && null != dynamicQuery.getOperator() && null != dynamicQuery.getValue()) {
                        toDynamicQueryCriterion(dynamicQueryCriteria, dynamicQuery, null);
                    }
                }
            }
        }
        return haveGeneratedQueryCriterion.get();
    }

    public void toDynamicQueryCriterion(List<DynamicQueryCriterion> dynamicQueryCriteria, DynamicQuery dynamicQuery, String prefix) {
        DynamicQueryCriterion dynamicQueryCriterion = new DynamicQueryCriterion();
        dynamicQueryCriterion.setPrefix(prefix);
        dynamicQueryCriterion.setQueryTable(dynamicQuery.getQueryTable());
        dynamicQueryCriterion.setField(dynamicQuery.getField());
        dynamicQueryCriterion.setOperator(dynamicQuery.getOperator());
        dynamicQueryCriterion.setValue(dynamicQuery.getValue());
        dynamicQueryCriterion.setJdbcType(dynamicQuery.getJdbcType());
        if (null != dynamicQuery.getFieldType() && "DEPT".equals(dynamicQuery.getFieldType())) {
            if (null != dynamicQuery.getSpecialSql()) {
                Set<Long> deptIds = new HashSet<>();
                FndDeptService fndDeptService = SpringContextHolder.getBean("fndDeptServiceImpl");
                if ("dept_id".equals(dynamicQuery.getField())) {
                    deptIds.add(Long.parseLong(dynamicQuery.getValue()));
                } else {
                    FndDeptQueryCriteria queryCriteria = new FndDeptQueryCriteria();
                    queryCriteria.setName(dynamicQuery.getValue());
                    queryCriteria.setEnabled(true);
                    queryCriteria.setDeleted(false);
                    List<FndDeptDTO> fndDeptDTOS = fndDeptService.listAll(queryCriteria);
                    for (FndDeptDTO dept : fndDeptDTOS) {
                        deptIds.add(dept.getId());
                    }
                }
                if (deptIds.size() > 0 && "EXPAND".equals(dynamicQuery.getOperator())) {
                    Set<Long> allChildrenDeptIds = new HashSet<>();
                    for (Long deptId : deptIds) {
                        Set<Long> childrenDeptIds = new HashSet<>(fndDeptService.listAllEnableChildrenIdByPid(deptId));
                        allChildrenDeptIds.addAll(childrenDeptIds);
                    }
                    deptIds.addAll(allChildrenDeptIds);
                }
                if (deptIds.size() == 0) {
                    dynamicQueryCriterion.setSpecialSql(" 1 = 2 ");
                } else {
                    String specialSql = dynamicQuery.getSpecialSql()
                            .replace("{{" + dynamicQuery.getField() + "}}", StringUtils.join(deptIds, ","));
                    dynamicQueryCriterion.setSpecialSql(this.replaceSpecialSql(specialSql, dynamicQuery));
                }
            }
        } else {
            dynamicQueryCriterion.setSpecialSql(this.replaceSpecialSql(dynamicQuery.getSpecialSql(), dynamicQuery));
        }
        dynamicQueryCriteria.add(dynamicQueryCriterion);
    }

    private String replaceSpecialSql(String specialSql, DynamicQuery dynamicQuery) {
        if (null != specialSql) {
            return specialSql
                    .replace("${item.queryTable}", dynamicQuery.getQueryTable())
                    .replace("${item.field}", dynamicQuery.getField())
                    .replace("${item.operator}", dynamicQuery.getOperator())
                    .replace("${item.jdbcType}", dynamicQuery.getJdbcType());
        }
        return null;
    }

    public String generatePrefix(DynamicQuery dynamicQuery, String parentPrefix, boolean haveGeneratedQueryCriterion) {
        String prefix = "";
        if (haveGeneratedQueryCriterion) {
            prefix = dynamicQuery.getCondition();
        } else {
            prefix = parentPrefix + (dynamicQuery.getNotCondition() ? " NOT" : "") + " (";
        }
        prefix = prefix.replace("( ", "(");
        return prefix;
    }
}
