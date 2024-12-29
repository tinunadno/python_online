package org.file_service.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.bson.types.ObjectId;

public class UpdateFileContentRequest {

    @NotBlank(message = "file id is required")
    @Pattern(regexp = "^[a-fA-F0-9]{24}$", message = "Invalid ObjectId format")
    private ObjectId fileId;
    @NotBlank(message = "content is required")
    private String content;


    public UpdateFileContentRequest(ObjectId fileId, String content) {
        this.fileId = fileId;
        this.content = content;
    }

    public ObjectId getFileId() {
        return fileId;
    }

    public String getContent() {
        return content;
    }

    public void setFileId(ObjectId fileId) {
        this.fileId = fileId;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
