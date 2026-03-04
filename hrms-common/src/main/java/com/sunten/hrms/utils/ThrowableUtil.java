package com.sunten.hrms.utils;

import com.sunten.hrms.exception.InfoCheckWarningMessException;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具 2019-01-06
 * @author batan
 */
public class ThrowableUtil {

    /**
     * 获取堆栈信息
     */
    public static String getStackTrace(Throwable throwable){
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

    public static void throwForeignKeyException(Throwable e, String msg){
        Throwable t = e.getCause();
//        while ((t != null) && !(t instanceof ConstraintViolationException)) {
//            t = t.getCause();
//        }
        if (t == null) {
            throw new InfoCheckWarningMessException(e.getMessage() + "\n" + msg);
        }
        assert false;
        throw new InfoCheckWarningMessException("删除失败：" + t.getMessage());
    }
}
