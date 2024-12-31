package org.execution_service.services;

import jakarta.annotation.PreDestroy;
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

    @PreDestroy
    public void stopProcessingQueue() {
        executorService.shutdown();
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
        Process process = null;

        try {
            ProcessBuilder pb = new ProcessBuilder("python3", "-c", script);
            process = pb.start();

            System.out.println("\n\nStarted process with PID: " + process.pid());

            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            StringBuilder output = new StringBuilder();
            StringBuilder errors = new StringBuilder();

            Thread stdoutThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = stdoutReader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                } catch (IOException ignored) {
                }
            });

            Thread stderrThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = stderrReader.readLine()) != null) {
                        errors.append(line).append("\n");
                    }
                } catch (IOException ignored) {
                }
            });

            stdoutThread.start();
            stderrThread.start();

            boolean finishedInTime = process.waitFor(2, TimeUnit.SECONDS);
            if (!finishedInTime) {
                process.destroyForcibly();
                stdoutThread.join();
                stderrThread.join();
                throw new RuntimeException("process timeout, check for infinite loops, and infinite recursion");
            }

            stdoutThread.join();
            stderrThread.join();

            int exitCode = process.exitValue();
            if (exitCode != 0) {
                throw new RuntimeException("errors on execution, execution code: " + exitCode + ", errors: " + errors);
            }

            return output.toString();
        }finally {
            if(process != null && process.isAlive()){
                process.destroyForcibly();
            }
        }
    }
}
