package org.file_service.controllers;

import org.bson.types.ObjectId;
import org.file_service.services.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fileAPI")
public class FileController {

    FileService fileService;

    @PostMapping("/createNewFile")
    ResponseEntity<?> createNewFile(){
        ObjectId fileId = fileService.createEmptyFile();
        return ResponseEntity.ok().body(fileId);
    }

    @GetMapping("/getFileContent")
    ResponseEntity<?> getFileContent(@RequestParam("file_id") ObjectId fileId){
        try{
            byte[] content = fileService.getFileContents(fileId);
            return ResponseEntity.ok().body(content);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    ResponseEntity<?> createNewFile(@RequestParam("file_id") ObjectId fileId){
        try{
            fileService.deleteFileContents(fileId);
            return ResponseEntity.ok().body("file was removed successfully");
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
