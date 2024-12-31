package org.execution_service.controllers;


import jakarta.validation.Valid;
import org.execution_service.DTO.ExecutionRequest;
import org.execution_service.services.PythonExecutionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/executionAPI")
public class ExecutionController {

    private final PythonExecutionService pythonExecutionService;

    public ExecutionController(PythonExecutionService pythonExecutionService) {
        this.pythonExecutionService = pythonExecutionService;
    }

    @PostMapping("/executeFile")
    void executeFile(@Valid @RequestBody ExecutionRequest executionRequest) {
        pythonExecutionService.startExecution(executionRequest);
    }
}
