package org.web_socket_service.DTO.otherServicesDTO;

public class GetFileContentRequest {

    private String fileId;

    public GetFileContentRequest(String fileId) {
        this.fileId = fileId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

}
