package org.web_socket_service.DTO;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class ExecutionServiceExecutionRequest {

    private String executableFile;

    private String responseUrl;

    public ExecutionServiceExecutionRequest(String executableFile, String responseUrl) {
        this.executableFile = executableFile;
        this.responseUrl = responseUrl;
    }

    public String getExecutableFile() {
        return executableFile;
    }

    public void setExecutableFile(String executableFile) {
        this.executableFile = executableFile;
    }

    public String getResponseUrl() {
        return responseUrl;
    }

    public void setResponseUrl(String responseUrl) {
        this.responseUrl = responseUrl;
    }


}
