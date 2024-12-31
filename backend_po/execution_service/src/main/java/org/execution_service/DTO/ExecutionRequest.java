package org.execution_service.DTO;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class ExecutionRequest {

    @NotBlank(message = "executable file is required")
    private String executableFile;

    @NotBlank(message = "response url is required")
    @URL(message = "response url is invalid")
    private String responseUrl;

    public ExecutionRequest(String executableFile, String responseUrl) {
        this.executableFile = executableFile;
        this.responseUrl = responseUrl;
    }

    public @NotBlank(message = "executable file is required") String getExecutableFile() {
        return executableFile;
    }

    public void setExecutableFile(@NotBlank(message = "executable file is required") String executableFile) {
        this.executableFile = executableFile;
    }

    public @NotBlank(message = "response url is required") String getResponseUrl() {
        return responseUrl;
    }

    public void setResponseUrl(@NotBlank(message = "response url is required") String responseUrl) {
        this.responseUrl = responseUrl;
    }

}
