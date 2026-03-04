package com.sunten.hrms.utils.enums;

/**
 * 作者 CG
 * 时间 2021/4/12 14:15
 * 备注 命名空间属性
 * 参数
 */
public enum NamespaceName {
    activiti("activiti","http://activiti.org/bpmn"),
    camunda("camunda","http://camunda.org/schema/1.0/bpmn"),
    ;
    private String name;
    public String url;

    NamespaceName(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
