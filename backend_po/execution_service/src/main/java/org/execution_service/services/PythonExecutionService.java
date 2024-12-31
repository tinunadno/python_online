package org.execution_service.services;

import org.execution_service.DTO.ExecutionRequest;
import org.execution_service.DTO.ExecutionResponse;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class PythonExecutionService {

    private final ResponseSenderService responseSenderService;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public PythonExecutionService(ResponseSenderService responseSenderService) {
        this.responseSenderService = responseSenderService;
    }

    public void startExecution(ExecutionRequest executionRequest) {
        executorService.submit(() -> {
            ExecutionResponse executionResponse;
            try {
                String executionResult = executePythonScript(executionRequest.getExecutableFile());
                executionResponse = new ExecutionResponse("executed successfully", executionResult);
            }catch (RuntimeException e){
                executionResponse = new ExecutionResponse("error", e.getMessage());
            }catch (IOException e){
                executionResponse = new ExecutionResponse("troubles with execution appeared", e.getMessage());
            }catch (InterruptedException e){
                executionResponse = new ExecutionResponse("server stopped working on execution", e.getMessage());
            }
            responseSenderService.sendResponse(executionResponse, executionRequest.getResponseUrl());
        });
    }

    private String executePythonScript(String script) throws RuntimeException, IOException, InterruptedException {



        ProcessBuilder pb = new ProcessBuilder("python3", "-c", script);
        Process process = pb.start();

        BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = stdoutReader.readLine()) != null) {
            output.append(line);
        }

        BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder errors = new StringBuilder();
        while ((line = stderrReader.readLine()) != null) {
            errors.append(line);
        }

        boolean exitCode = process.waitFor(2, TimeUnit.SECONDS);
        if (!exitCode) {
            throw new RuntimeException("Ошибка выполнения скрипта: " + errors);
        }

        return output.toString();
    }
}
