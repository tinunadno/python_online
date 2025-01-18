package org.web_socket_service.DTO;

public class FileContentResponse {

    private String fileContent;

    public FileContentResponse(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

}
