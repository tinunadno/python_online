package org.web_socket_service.components;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "services")
public class ServiceProperties {

    private String fileServiceName;
    private String updateFileContentEndpoint;
    private String getFileContentEndpoint;

    private String sessionServiceName;
    private String getSessionFileDescriptorEndpoint;

    private String executionServiceName;
    private String executeFileEndpoint;

    public String getExecuteFileEndpoint() {
        return executeFileEndpoint;
    }

    public void setExecuteFileEndpoint(String executeFileEndpoint) {
        this.executeFileEndpoint = executeFileEndpoint;
    }

    public String getExecutionServiceName() {
        return executionServiceName;
    }

    public void setExecutionServiceName(String executionServiceName) {
        this.executionServiceName = executionServiceName;
    }

    public String getGetSessionFileDescriptorEndpoint() {
        return getSessionFileDescriptorEndpoint;
    }

    public void setGetSessionFileDescriptorEndpoint(String getSessionFileDescriptorEndpoint) {
        this.getSessionFileDescriptorEndpoint = getSessionFileDescriptorEndpoint;
    }

    public String getSessionServiceName() {
        return sessionServiceName;
    }

    public void setSessionServiceName(String sessionServiceName) {
        this.sessionServiceName = sessionServiceName;
    }

    public String getFileServiceName() {
        return fileServiceName;
    }

    public void setFileServiceName(String fileServiceName) {
        this.fileServiceName = fileServiceName;
    }

    public String getUpdateFileContentEndpoint() {
        return updateFileContentEndpoint;
    }

    public void setUpdateFileContentEndpoint(String updateFileContentEndpoint) {
        this.updateFileContentEndpoint = updateFileContentEndpoint;
    }

    public String getGetFileContentEndpoint() {
        return getFileContentEndpoint;
    }

    public void setGetFileContentEndpoint(String getFileContentEndpoint) {
        this.getFileContentEndpoint = getFileContentEndpoint;
    }

}
