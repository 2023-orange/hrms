package com.sunten.hrms.fnd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author batan
 * @since 2019-12-04
 */
@Data
@AllArgsConstructor
public class FndMenuMetaVo implements Serializable {

    private String title;

    private String icon;

    private Boolean iFrame;

    private Boolean noCache;

    private Boolean blank;

    private Boolean sendToken;
}
