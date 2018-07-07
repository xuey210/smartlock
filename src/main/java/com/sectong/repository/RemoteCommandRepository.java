package com.sectong.repository;

import com.sectong.domain.mongomodle.InfraCommand;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by xueyong on 16/7/14.
 * mobileeasy.
 */

@Repository("remoteCommandRePository")
public interface RemoteCommandRepository extends MongoRepository<InfraCommand, String> {

}
