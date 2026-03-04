package com.sunten.hrms.re.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author batan
 * @since 2020-08-05
 */
@Data
public class ReTrainQueryCriteria implements Serializable {
    private Long reId;
    private Boolean enabled;

}
