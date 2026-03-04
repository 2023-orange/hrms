package com.sunten.config;

import com.baomidou.mybatisplus.extension.plugins.pagination.DialectModel;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;

public class MySQLServerDialect implements IDialect {
    public MySQLServerDialect() {
    }

    public DialectModel buildPaginationSql(String originalSql, long offset, long limit) {
//        int lastIndexOf = originalSql.lastIndexOf("ORDER BY");
//        String sqlTemp = originalSql.substring(0, lastIndexOf) + " AND ? = ? " + originalSql.substring(lastIndexOf);
//        String sql = sqlTemp + " OFFSET " + offset + " ROWS FETCH NEXT " + limit + " ROWS ONLY";
//        //  OPTION(querytraceon 8649)
//        return (new DialectModel(sql, 1L, 1L)).setConsumerChain();
        String sql = originalSql + " OFFSET " + offset + " ROWS FETCH NEXT " + limit + " ROWS ONLY";
        return (new DialectModel(sql));
    }
}
