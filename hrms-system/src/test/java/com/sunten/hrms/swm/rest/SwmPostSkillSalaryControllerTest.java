package com.sunten.hrms.swm.rest;

import org.junit.jupiter.api.Test;

public class SwmPostSkillSalaryControllerTest {

    @Test
    public void create() {
        String topPeriod="2022.12";
        String[] s = topPeriod.split("\\.");
        System.out.println("topPeriod++++++ " + topPeriod);
        System.out.println("s++++++++++++" + s);
        String addTopPeriod = s[1].equals("12") ?
                Integer.parseInt(s[0]) + 1 + ".01" : s[0] + "." + (Integer.parseInt(s[1]) + 1 < 10 ? "0" + (Integer.parseInt(s[1]) + 1) : (Integer.parseInt(s[1]) + 1));
        String subtractPeriod = s[1].equals("01") ?
                Integer.parseInt(s[0]) - 1 + ".12" : s[0] + "." + (Integer.parseInt(s[1]) - 1 < 10 ? "0" + (Integer.parseInt(s[1]) - 1) : (Integer.parseInt(s[1]) - 1));
        System.out.println("addTopPeriod++++ " + addTopPeriod);
        System.out.println("subtractPeriod++++ " + subtractPeriod);
        }
}
