package org.file_service.DTO;

import org.bson.types.ObjectId;

public class OkFileIdResponse {

    String fileId;

    public OkFileIdResponse(String fileId) {
        this.fileId = fileId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
