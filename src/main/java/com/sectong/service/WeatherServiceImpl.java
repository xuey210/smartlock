package com.sectong.service;

import com.sectong.domain.mongomodle.WeatherModle;
import com.sectong.domain.objectmodle.WeatherStation;
import com.sectong.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by xueyong on 16/6/28.
 */

@Service
public class WeatherServiceImpl implements WeatherService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    MongoTemplate mongoTemplate;

//    @Autowired
//    private WeatherSpecRepository weatherSpecRepository;


    @Override
    public void insertWeather(WeatherStation weatherStation) {
        weatherRepository.insert(weatherStation);
    }

    @Override
    public WeatherStation getWeatherByMac(String mac) {
        return weatherRepository.findFirstByDeviceMACOrderByCreateDateDesc(mac);
    }

    @Override
    public List<WeatherStation> getWeatherPm25(String mac, String date) throws Exception {
        String[] dateary = date.split("\\-");
        LocalDateTime localDateTime_start = LocalDateTime.of(
                Integer.valueOf(dateary[0]).intValue(),
                Integer.valueOf(dateary[1]).intValue(),
                Integer.valueOf(dateary[2]).intValue(), 00, 00, 00);
        LocalDateTime localDateTime_end = LocalDateTime.of(
                Integer.valueOf(dateary[0]).intValue(),
                Integer.valueOf(dateary[1]).intValue(),
                Integer.valueOf(dateary[2]).intValue(), 23, 59, 59);
        ZonedDateTime zonedDateTime_start = localDateTime_start.atZone(ZoneId.of("Asia/Shanghai"));
        ZonedDateTime zonedDateTime_end = localDateTime_end.atZone(ZoneId.of("Asia/Shanghai"));
        Date start = Date.from(zonedDateTime_start.toInstant());
        Date end = Date.from(zonedDateTime_end.toInstant());

        Query query = new Query(where("createDate").gte(start).lte(end)
                .and("deviceMAC").is(mac))
                .with(new Sort(Sort.Direction.DESC, "createDate"))
                .limit(1);
        return mongoTemplate.find(query, WeatherStation.class);
//        return weatherRepository.findFirstByDeviceMACAndCreateDateBetweenOrderByCreateDateDesc(mac, start, end);
    }

    @Override
    public Page<WeatherModle> getWeatherByPage(String deviceMAC, Integer pageNumber) {
        PageRequest request =
                new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.DESC, "createDate");
        Specification<WeatherModle> specification = (root, criteriaQuery, criteriaBuilder) -> {
            if ("".equals(deviceMAC) && null != deviceMAC) {
                Predicate predicateMac = criteriaBuilder.equal(root.get("deviceMac").as(String.class), deviceMAC);
                criteriaBuilder.and(predicateMac);
            }
            return criteriaQuery.getRestriction();
        };
        return weatherRepository.findByDeviceMAC(deviceMAC, request);
    }
}
