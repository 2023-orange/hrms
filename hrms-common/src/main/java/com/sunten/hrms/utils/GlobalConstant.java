package com.sunten.hrms.utils;

import org.springframework.beans.factory.annotation.Value;

/**
 * 常用静态常量
 *
 * @author batan
 * @since 2019-12-26
 */
public class GlobalConstant {

    public static final String RESET_PASS = "重置密码";

    public static final String RESET_MAIL = "重置邮箱";

    public static final String COMPANY_NAME = "顺特电气设备有限公司";
    public static final String COMPANY_CODE = "SUNTEN";

    public static final String SYSTEM_NAME = "人力资源管理系统";
    public static final String SYSTEM_VERSION = "1.0";

    public static final String SYSTEM_FULL_NAME = COMPANY_CODE + SYSTEM_NAME + SYSTEM_VERSION;

    /**
     * 用于IP定位转换
     */
    static final String REGION = "内网IP|内网IP";

    /**
     * 常用接口
     */
    public static class Url {
        public static final String SM_MS_URL = "https://sm.ms/api/upload";
    }
}
