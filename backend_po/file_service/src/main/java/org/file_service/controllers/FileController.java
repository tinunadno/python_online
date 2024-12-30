package org.file_service.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.bson.types.ObjectId;
import org.file_service.DTO.*;
import org.file_service.services.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/fileAPI")
@Validated
public class FileController {

    FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/createNewFile")
    ResponseEntity<?> createNewFile(){
        ObjectId fileId = fileService.createEmptyFile();
        OkFileIdResponse response = new OkFileIdResponse(fileId.toHexString());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/getFileContent")
    ResponseEntity<?> getFileContent(@RequestParam(value = "fileId", required = true) @NotBlank(message = "file id is required") @Pattern(regexp = "^[a-fA-F0-9]{24}$", message = "Invalid file id format") String fileId){
        try{
            byte[] content = fileService.getFileContents(fileId);
            OkContentResponse response = new OkContentResponse(new String(content, StandardCharsets.UTF_8));
            return ResponseEntity.ok().body(response);
        }catch (IllegalArgumentException e){
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/deleteFile")
    ResponseEntity<?> deleteFile(@Valid @RequestBody DeleteFileRequest deleteFileRequest){
        try{
            fileService.deleteFileContents(deleteFileRequest);
            OkResponse response = new OkResponse("file was removed successfully");
            return ResponseEntity.ok().body(response);
        }catch (IllegalArgumentException e){
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/updateFileContent")
    ResponseEntity<?> updateFileContent(@Valid @RequestBody UpdateFileContentRequest updateFileContentRequest){
        try{
            fileService.updateFileContents(updateFileContentRequest);
            OkResponse response = new OkResponse("file was updated successfully");
            return ResponseEntity.ok().body(response);
        }catch (IllegalArgumentException e){
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
