package com.intelliguru.simplora.repository;

import com.intelliguru.simplora.entity.ConfigEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends MongoRepository<ConfigEntity, ObjectId> {
}
