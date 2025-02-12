package org.web_socket_service.DTO.otherServicesDTO;

public class ExecutionServiceExecutionRequest {

    private String executableFile;

    private String responseServiceName;

    private String responseServiceEndpoint;

    private String callbackToken;

    public ExecutionServiceExecutionRequest(String executableFile, String responseServiceName, String responseServiceEndpoint, String callbackToken) {
        this.executableFile = executableFile;
        this.responseServiceName = responseServiceName;
        this.responseServiceEndpoint = responseServiceEndpoint;
        this.callbackToken = callbackToken;
    }

    public String getExecutableFile() {
        return executableFile;
    }

    public void setExecutableFile(String executableFile) {
        this.executableFile = executableFile;
    }

    public String getResponseServiceName() {
        return responseServiceName;
    }

    public void setResponseServiceName(String responseServiceName) {
        this.responseServiceName = responseServiceName;
    }

    public String getResponseServiceEndpoint() {
        return responseServiceEndpoint;
    }

    public void setResponseServiceEndpoint(String responseServiceEndpoint) {
        this.responseServiceEndpoint = responseServiceEndpoint;
    }

    public String getCallbackToken() { return callbackToken; }

    public void setCallbackToken(String callbackToken) { this.callbackToken = callbackToken; }

}
