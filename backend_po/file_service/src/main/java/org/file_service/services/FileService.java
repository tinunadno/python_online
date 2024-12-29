package org.file_service.services;

import org.bson.types.ObjectId;
import org.file_service.DTO.DeleteFileRequest;
import org.file_service.DTO.UpdateFileContentRequest;
import org.file_service.entities.FileEntity;
import org.file_service.repositories.FileRepository;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    private final FileRepository fileRepository;


    public FileService(FileRepository fileRepository)
    {
        this.fileRepository = fileRepository;
    }

    public ObjectId createEmptyFile() {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setContents(new byte[0]);

        fileEntity = fileRepository.save(fileEntity);

        return fileEntity.getFileId();
    }

    public byte[] getFileContents(String id) throws IllegalArgumentException{
        ObjectId fileID = new ObjectId(id);
        FileEntity fileEntity = fileRepository.findByFileId(fileID);
        if(fileEntity == null){
            throw new IllegalArgumentException("file with this id doesn't exist");
        }
        return fileEntity.getContents();
    }

    public void deleteFileContents(DeleteFileRequest deleteFileRequest) throws IllegalArgumentException{
        ObjectId fileId = new ObjectId(deleteFileRequest.getFileId());
        if(!fileRepository.existsById(fileId)){
            throw new IllegalArgumentException("file with this id doesn't exist");
        }
        fileRepository.deleteById(fileId);
    }

    public void updateFileContents(UpdateFileContentRequest updateFileContentRequest) throws IllegalArgumentException{
        FileEntity fileEntity = fileRepository.findByFileId(new ObjectId(updateFileContentRequest.getFileId()));
        if(fileEntity == null){
            throw new IllegalArgumentException("file with this id doesn't exist");
        }
        fileEntity.setContents(updateFileContentRequest.getContent().getBytes());
        fileEntity = fileRepository.save(fileEntity);
        if(fileEntity == null){
            throw new RuntimeException("file wasn't saved");
        }
    }

}
