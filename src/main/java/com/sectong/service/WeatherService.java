package com.sectong.service;

import com.sectong.domain.mongomodle.WeatherModle;
import com.sectong.domain.objectmodle.WeatherStation;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;

/**
 * Created by xueyong on 16/6/28.
 * demo.
 */
public interface WeatherService {

    void insertWeather(WeatherStation weatherStation);

    WeatherStation getWeatherByMac(String mac);

    List<WeatherStation> getWeatherPm25(String mac, String date) throws ParseException, Exception;

    Page<WeatherModle> getWeatherByPage(String deviceMAC, Integer pageNumber);
}
