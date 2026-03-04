package com.sunten.hrms.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    public static void listComp(List<Long> idsOld, List<Long> idsNew) {
        //取交集
        List<Long> intersection = new ArrayList<>();
        intersection.addAll(idsNew);
        intersection.retainAll(idsOld);
        //原集-交集=删除集
        idsOld.removeAll(intersection);
        //新集-交集=新增集
        idsNew.removeAll(intersection);
    }

    public static void listCompForStr(List<String> StrsOld, List<String> StrsNew) {
        List<String> intersection = new ArrayList<>();
        intersection.addAll(StrsNew);
        intersection.retainAll(StrsOld);
        //原集-交集=删除集
        StrsOld.removeAll(intersection);
        //新集-交集=新增集
        StrsNew.removeAll(intersection);
    }

}
