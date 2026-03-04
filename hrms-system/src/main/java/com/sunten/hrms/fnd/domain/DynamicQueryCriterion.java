package com.sunten.hrms.fnd.domain;

import com.sunten.hrms.utils.DateUtil;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class DynamicQueryCriterion {
    private String prefix; // 前缀
    private String queryTable;
    private String field;
    private String operator;
    private Object value;
    private String jdbcType;
    private String suffix; // 后缀
    private String specialSql; // 特殊sql

    public Object getValueWithDataType() {
        if (null == this.value) {
            return null;
        }
        switch (this.jdbcType) {
            case "CHAR":
            case "VARCHAR":
            case "LONGVARCHAR":
                return this.value.toString();
            case "DECIMAL":
            case "NUMERIC":
                return new BigDecimal(this.value.toString());
            case "BIT":
            case "BOOLEAN":
                return Boolean.parseBoolean(this.value.toString());
            case "TINYINT":
                return Byte.parseByte(this.value.toString());
            case "SMALLINT":
                return Short.parseShort(this.value.toString());
            case "INTEGER":
                return Integer.parseInt(this.value.toString());
            case "BIGINT":
                return Long.parseLong(this.value.toString());
            case "REAL":
                return Float.parseFloat(this.value.toString());
            case "FLOAT":
            case "DOUBLE":
                return Double.parseDouble(this.value.toString());
            case "BINARY":
            case "VARBINARY":
            case "LONGVARBINARY":
                return this.value.toString().getBytes();
            case "DATE":
                return DateUtil.strToLocalDate(this.value.toString());
            case "TIME":
                // return DateUtil.strToLocalTime(this.value.toString());
                return this.value.toString();
            case "TIMESTAMP":
                return DateUtil.strToLocalDateTime(this.value.toString());
            default:
                return this.value;
        }
    }
}
