package com.sectong.repository;

import com.sectong.domain.mongomodle.RoomAndEq;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by xueyong on 16/7/2.
 */

@Repository("roomAndEqRepository")
public interface RoomAndEqRepository extends MongoRepository<RoomAndEq, String> {
    Collection<RoomAndEq> findByUser(String user);
    void deleteByUser(String user);
}
