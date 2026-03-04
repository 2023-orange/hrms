package com.sunten.hrms.fnd.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author batan
 * @since 2019-12-05
 */
@Data
@ToString
public class FndRoleSmallDTO implements Serializable {

    private Long id;

    private String name;

    private Integer level;

    private String dataScope;
}
