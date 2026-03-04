package com.sunten.hrms.fnd.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author batan
 * @since 2019-12-06
*/
@Data
@NoArgsConstructor
@ToString
public class FndJobSmallDTO implements Serializable {

    private Long id;

    private String jobName;
}
