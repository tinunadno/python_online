package org.file_service.DTO;

import jakarta.validation.constraints.Pattern;
import org.bson.types.ObjectId;

public class DeleteFileRequest {

    @Pattern(regexp = "^[a-fA-F0-9]{24}$", message = "Invalid ObjectId format")
    private ObjectId fileId;

    public DeleteFileRequest(ObjectId fileId) {
        this.fileId = fileId;
    }

    public ObjectId getFileId() {
        return fileId;
    }

    public void setFileId(ObjectId fileId) {
        this.fileId = fileId;
    }
}
