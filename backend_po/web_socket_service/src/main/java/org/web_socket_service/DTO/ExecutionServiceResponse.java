package org.web_socket_service.DTO;

public class ExecutionServiceResponse {

    private String exitCode;

    private String executionOutput;

    public ExecutionServiceResponse(String exitCode, String executionOutput) {
        this.exitCode = exitCode;
        this.executionOutput = executionOutput;
    }

    public String getExitCode() {
        return exitCode;
    }

    public String getExecutionOutput() {
        return executionOutput;
    }

    public void setExitCode(String exitCode) {
        this.exitCode = exitCode;
    }

    public void setExecutionOutput(String executionOutput) {
        this.executionOutput = executionOutput;
    }

}
