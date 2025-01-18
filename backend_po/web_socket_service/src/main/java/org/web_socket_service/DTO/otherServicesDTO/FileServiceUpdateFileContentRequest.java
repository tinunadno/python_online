package org.web_socket_service.DTO.otherServicesDTO;



public class FileServiceUpdateFileContentRequest {

    private String fileId;

    private String content;

    public FileServiceUpdateFileContentRequest(String fileId, String content) {
        this.fileId = fileId;
        this.content = content;
    }

    public String getFileId() {
        return fileId;
    }

    public String getContent() {
        return content;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
