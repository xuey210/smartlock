package com.sectong.repository;

import com.sectong.domain.mongomodle.WeatherModle;
import com.sectong.domain.objectmodle.WeatherStation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by xueyong on 16/6/28.
 */

@RestResource(exported = false) // 禁止暴露REST
@Repository
public interface WeatherRepository extends MongoRepository<WeatherStation, String> {

    WeatherStation findFirstByDeviceMACOrderByCreateDateDesc(String deviceMAC);

    WeatherModle findFirstByDeviceMACAndCreateDateBetweenOrderByCreateDateDesc(String deviceMAC, Date start, Date end);

    Page<WeatherModle> findByDeviceMAC(String deviceMAC, Pageable pageable);
}
