package com.sectong.repository;

import com.sectong.domain.mongomodle.MainFrame;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by xueyong on 16/7/14.
 * mobileeasy.
 */

@Repository("mainframeRePository")
public interface MainFrameRepository extends MongoRepository<MainFrame, String> {

    Collection<MainFrame> findByUserAndDeviceMac(String user, String deviceMac);

    Collection<MainFrame> findByUser(String user);

    Collection<MainFrame> findByDeviceMac(String deviceMac);

}
