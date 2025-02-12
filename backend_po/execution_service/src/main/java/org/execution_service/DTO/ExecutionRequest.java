package org.execution_service.DTO;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class ExecutionRequest {

    @NotBlank(message = "executable file is required")
    private String executableFile;

    @NotBlank(message = "response service name is required")
    private String responseServiceName;

    @NotBlank(message = "response service endpoint is required")
    private String responseServiceEndpoint;

    @NotBlank(message = "callback token is required")
    private String callbackToken;

    public ExecutionRequest(String executableFile, String responseServiceName, String responseServiceEndpoint, String callbackToken) {
        this.executableFile = executableFile;
        this.responseServiceName = responseServiceName;
        this.responseServiceEndpoint = responseServiceEndpoint;
        this.callbackToken = callbackToken;
    }



    public @NotBlank(message = "executable file is required") String getExecutableFile() {
        return executableFile;
    }

    public @NotBlank(message = "response service name is required") String getResponseServiceName() {
        return responseServiceName;
    }

    public void setResponseServiceName(@NotBlank(message = "response service name is required") String responseServiceName) {
        this.responseServiceName = responseServiceName;
    }

    public @NotBlank(message = "response service endpoint is required") String getResponseServiceEndpoint() {
        return responseServiceEndpoint;
    }

    public void setResponseServiceEndpoint(@NotBlank(message = "response service endpoint is required") String responseServiceEndpoint) {
        this.responseServiceEndpoint = responseServiceEndpoint;
    }

    public void setExecutableFile(@NotBlank(message = "executable file is required") String executableFile) {
        this.executableFile = executableFile;
    }

    public @NotBlank(message = "callback token is required") String getCallbackToken() {
        return callbackToken;
    }

    public void setCallbackToken(@NotBlank(message = "callback token is required") String callbackToken) {
        this.callbackToken = callbackToken;
    }

}
