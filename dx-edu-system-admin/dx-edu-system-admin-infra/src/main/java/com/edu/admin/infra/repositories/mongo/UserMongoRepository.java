package com.edu.admin.infra.repositories.mongo;

import com.edu.admin.infra.bean.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 人员mongo仓库
 *
 * @author hjw
 * @since 2025-10-24
 */
@Repository
public interface UserMongoRepository extends MongoRepository<UserDocument, String> {

    List<UserDocument> findByAge(Integer age);

    @Query("{'age': {$gt: ?0}, 'name': {$eq: ?1}}")
    List<UserDocument> findByAgeAndName(Integer age, String name);

}