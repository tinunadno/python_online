package org.file_service.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "files")
public class FileEntity {

    @Id
    private ObjectId fileId;
    private byte[] contents;

    public FileEntity() {}

    public FileEntity(byte[] contents) {
        this.contents = contents;
    }

    public ObjectId getFileId() {
        return fileId;
    }

    public byte[] getContents() {
        return contents;
    }

    public void setFileId(ObjectId fileId) {
        this.fileId = fileId;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }
}
