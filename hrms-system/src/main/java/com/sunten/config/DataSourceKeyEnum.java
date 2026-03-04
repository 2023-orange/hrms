package com.sunten.config;

import com.sunten.config.annotation.DataSource;
import lombok.Getter;

public enum DataSourceKeyEnum {

    HRMS("hrms"),
    /**
     * 表示 所有的SLAVE, 随机选择一个SLAVE0， 或者SLAVE1
     */
    ERP("erp"),

    OA("oa");

    @Getter
    private String value;

    DataSourceKeyEnum(String value) {
        this.value = value;
    }
//
//    public static List<DataSourceKeyEnum> getSlaveList() {
//        return Arrays.asList(SLAVE0, SLAVE1);
//    }

    /**
     * 根据注解获取数据源
     *
     * @param dataSource
     * @return
     */
    public static DataSourceKeyEnum getDataSourceKey(DataSource dataSource) {
        if (dataSource == null) {
            return HRMS;
        }
        return dataSource.value();
//        if (dataSource.value() == DataSourceKeyEnum.SLAVE) {
//            List<DataSourceKeyEnum> dataSourceKeyList = DataSourceKeyEnum.getSlaveList();
//            // FIXME 目前乱序
//            Collections.shuffle(dataSourceKeyList);
//            return dataSourceKeyList.get(0);
//        } else {
//            return dataSource.value();
//        }
    }
}
