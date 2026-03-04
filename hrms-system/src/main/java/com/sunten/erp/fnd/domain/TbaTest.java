package com.sunten.erp.fnd.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class TbaTest implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String description;
	
}