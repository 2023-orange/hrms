package com.sunten.hrms.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @atuthor xukai
 * @date 2020/9/4 13:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElTreeBaseVo {

    private Long id;
    private String label;
    private Object content;
    private List<ElTreeBaseVo> children;
}
