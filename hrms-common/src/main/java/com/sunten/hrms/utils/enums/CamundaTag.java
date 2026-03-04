package com.sunten.hrms.utils.enums;
/**
 * 作者 CG
 * 时间 2021/4/12 14:11
 * 备注 需要处理的标签名字
 * 参数
 */
public enum CamundaTag {
    assignee("assignee", "camunda:assignee","activiti:assignee","指派人"),
    candidateUsers("candidateUsers", "camunda:candidateUsers","activiti:candidateUsers","候选人"),
    candidateGroups("candidateGroups", "camunda:candidateGroups","activiti:candidateGroups","候选组"),
    exclusive("exclusive", "camunda:exclusive","activiti:exclusive","独家"),
    taskListener("taskListener", "camunda:taskListener","activiti:taskListener","监听"),
    collection("collection", "camunda:collection", "activiti:collection", "队列"),
    elementVariable("elementVariable", "camunda:elementVariable", "activiti:elementVariable", "参数值")
    ;
    private String attribute;
    private String camundaName;
    private String activitiName;
    private String note;


    CamundaTag(String attribute, String camundaName, String activitiName, String note) {
        this.attribute = attribute;
        this.camundaName = camundaName;
        this.activitiName = activitiName;
        this.note = note;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getCamundaName() {
        return camundaName;
    }

    public void setCamundaName(String camundaName) {
        this.camundaName = camundaName;
    }

    public String getActivitiName() {
        return activitiName;
    }

    public void setActivitiName(String activitiName) {
        this.activitiName = activitiName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

