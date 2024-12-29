package org.file_service.services;

import org.bson.types.ObjectId;
import org.file_service.entities.FileEntity;
import org.file_service.repositories.FileRepository;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public ObjectId createEmptyFile() {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setContents(new byte[0]);

        fileEntity = fileRepository.save(fileEntity);

        return fileEntity.getFileId();
    }

    public byte[] getFileContents(ObjectId id) throws IllegalArgumentException{
        FileEntity fileEntity = fileRepository.findByFileId(id);
        if(fileEntity == null){
            throw new IllegalArgumentException("invalid file id");
        }
        return fileEntity.getContents();
    }

    public void deleteFileContents(ObjectId id) throws IllegalArgumentException{
        if(!fileRepository.existsById(id)){
            throw new IllegalArgumentException("invalid file id");
        }
        fileRepository.deleteById(id);
    }

}
