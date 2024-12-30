package org.file_service.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.bson.types.ObjectId;

public class DeleteFileRequest {

    @NotBlank(message = "file id is required")
    @Pattern(regexp = "^[a-fA-F0-9]{24}$", message = "Invalid ObjectId format")
    private String fileId;

    public DeleteFileRequest(String fileId) {
        this.fileId = fileId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
