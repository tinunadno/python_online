package org.file_service.repositories;

import org.bson.types.ObjectId;
import org.file_service.entities.FileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends MongoRepository <FileEntity, ObjectId>{
    FileEntity findByFileId(ObjectId fileId);
    void deleteByFileId(ObjectId fileId);
}
