package com.sunten.hrms.config;

import org.springframework.stereotype.Component;

@Component
public class GlobalVariablesSingleton {
    private static GlobalVariablesSingleton instance;

    private String serverCheckStatus;
    private String serverCheckMessage;

    private GlobalVariablesSingleton() {

    }

    public static GlobalVariablesSingleton getInstance() {
        if (instance == null) {
            instance = new GlobalVariablesSingleton();
        }
        return instance;
    }

    public void setServerCheckStatus(String serverCheckStatus) {
        this.serverCheckStatus = serverCheckStatus;
    }

    public void setServerCheckMessage(String serverCheckMessage) {
        this.serverCheckMessage = serverCheckMessage;
    }

    public String getServerCheckStatus() {
        return this.serverCheckStatus;
    }

    public String getServerCheckMessage() {
        return this.serverCheckMessage;
    }
}
