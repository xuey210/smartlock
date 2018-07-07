package com.home.junit;

import com.sectong.MobileEasyApplication;
import com.sectong.domain.objectmodle.WeatherStation;
import com.sectong.service.WeatherService;
import com.sectong.utils.SerializationObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by xueyong on 16/9/12.
 * mobileeasy.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MobileEasyApplication.class)
public class RedServiceImplTest {

    @Autowired
    WeatherService weatherService;

    @Test
    public void testGetAllModelOrders() throws Exception {
        List<WeatherStation> list = weatherService.getWeatherPm25("D8B04CF00EA0", "2016-10-14");
        System.out.println(SerializationObject.serialize(list));
    }
}