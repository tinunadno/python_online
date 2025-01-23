package org.session_service.components;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "services")
public class ServiceProperties {

    private String fileServiceName;
    private String createFileEndpoint;
    private String deleteFileEndpoint;

    public String getFileServiceName() {
        return fileServiceName;
    }

    public void setFileServiceName(String fileServiceName) {
        this.fileServiceName = fileServiceName;
    }

    public String getCreateFileEndpoint() {
        return createFileEndpoint;
    }

    public void setCreateFileEndpoint(String createFileEndpoint) {
        this.createFileEndpoint = createFileEndpoint;
    }

    public String getDeleteFileEndpoint() {
        return deleteFileEndpoint;
    }

    public void setDeleteFileEndpoint(String deleteFileEndpoint) {
        this.deleteFileEndpoint = deleteFileEndpoint;
    }

}
