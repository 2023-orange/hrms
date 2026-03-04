package com.sunten.hrms.re.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author xukai
 * @since 2020-08-28
 */
@Data
public class ReVocationalQueryCriteria implements Serializable {
    private Boolean enabled;
    private Long reId;
}
