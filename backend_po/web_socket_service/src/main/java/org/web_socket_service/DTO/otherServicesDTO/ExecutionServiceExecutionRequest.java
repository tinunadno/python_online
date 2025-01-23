package org.web_socket_service.DTO.otherServicesDTO;

public class ExecutionServiceExecutionRequest {

    private String executableFile;

    private String responseUrl;

    private String callbackToken;

    public ExecutionServiceExecutionRequest(String executableFile, String responseUrl, String callbackToken) {
        this.executableFile = executableFile;
        this.responseUrl = responseUrl;
        this.callbackToken = callbackToken;
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


    public String getCallbackToken() { return callbackToken; }

    public void setCallbackToken(String callbackToken) { this.callbackToken = callbackToken; }

}
