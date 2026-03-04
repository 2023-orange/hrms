package com.sunten.hrms.fnd.vo;

import lombok.Data;

/**
 * 修改密码的 Vo 类
 * @author batan
 * @since 2019-12-04
 */
@Data
public class FndUserPassVo {

    private String oldPass;

    private String newPass;
}
