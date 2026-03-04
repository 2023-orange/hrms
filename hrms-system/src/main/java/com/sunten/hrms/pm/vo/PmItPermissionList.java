package com.sunten.hrms.pm.vo;

import com.sunten.hrms.pm.domain.PmItPermissions;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class PmItPermissionList {
    // 信息基础
    private List<String> useMsgList;

    // 系统应用
    private List<String> useSysList;

}
