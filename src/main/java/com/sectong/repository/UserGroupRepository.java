package com.sectong.repository;

import com.sectong.domain.mongomodle.UserGroup;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xueyong on 16/7/2.
 */

@Repository("groupRep")
public interface UserGroupRepository extends MongoRepository<UserGroup, String> {

    void deleteByUsernameAndSubUsername(String username, String subUsername);

    List<UserGroup> findByUsernameAndSubUsername(String username, String subUsername);

    List<UserGroup> findByUsername(String username);

    List<UserGroup> findBySubUsernameAndUsername(String subUsername,String username);
}
