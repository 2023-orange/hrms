package com.sunten.hrms.pm.domain;

import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Blob;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmPhoto extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String name;

    private String workCard;

    private byte[] photo;

    private Long employeeId;

    private Long imageSize;
}
