package com.sectong.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.mongodb.util.JSON;
import com.sectong.constant.APIEm;
import com.sectong.domain.mongomodle.MainFrame;
import com.sectong.domain.mongomodle.WeatherModle;
import com.sectong.domain.objectmodle.Pm25Object;
import com.sectong.domain.objectmodle.WeatherReturn;
import com.sectong.domain.objectmodle.WeatherStation;
import com.sectong.message.Message;
import com.sectong.service.MacService;
import com.sectong.service.WeatherService;
import com.sectong.utils.DateTools;
import com.sectong.utils.JsonUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 处理用户类接口
 * 
 * @author jiekechoo
 *
 */
@RestController
@PropertySource("classpath:message.properties")
@Api(basePath = "/weatherstation", value = "气象站相关API", description = "气象站", produces = "application/json")
@RequestMapping("/weatherstation")
public class WeatherStationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WeatherStationController.class);

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private MacService macService;

	@ApiOperation(httpMethod = "POST", value = "提交天气数据",
            notes = "post weather 数据到 server。")
	@RequestMapping(method = RequestMethod.POST, value = "/postWeather")
	public ResponseEntity<Message> postWeather(HttpServletRequest request) {
        Message message = new Message();
        WeatherStation weatherStation;
        String _tempString;
        WeatherReturn weatherReturn = new WeatherReturn();
        try {
            String requestString = IOUtils.toString(request.getInputStream());
            LOGGER.info("收到请求 :{}", new Object[]{requestString});
            Assert.notNull(requestString, "can not be null");
            weatherStation = JsonUtil.parseObject(requestString, WeatherStation.class);
            _tempString = JsonUtil.toJSONString(weatherStation);
            weatherStation.setDeviceMAC(weatherStation.getDevice_MAC());
            weatherStation.setCreateDate(new Date());
            weatherService.insertWeather(weatherStation);

        } catch (IOException e) {
            LOGGER.error("post exception :{}", e);
            e.printStackTrace();
            weatherStation = new WeatherStation();
            weatherReturn.setDevice_MAC(weatherStation.getDevice_MAC());
            weatherReturn.setDevice_software_updateflag("fault");
            _tempString = JSON.serialize(weatherStation);
        }
        message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), JSON.parse(_tempString));
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/postDemo")
    public ResponseEntity<Message> postDemo(HttpServletRequest request) {
        Message message = new Message();
        WeatherStation weatherStation;
//        String _tempString;
        WeatherReturn weatherReturn = new WeatherReturn();
        try {
            String requestString = IOUtils.toString(request.getInputStream());
            LOGGER.info("收到请求 :{}", new Object[]{requestString});
            Assert.notNull(requestString, "can not be null");
        } catch (IOException e) {
            LOGGER.error("post exception :{}", e);
            e.printStackTrace();
            weatherStation = new WeatherStation();
            weatherReturn.setDevice_MAC(weatherStation.getDevice_MAC());
            weatherReturn.setDevice_software_updateflag("fault");
        }
        message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), "success");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET", value = "获取天气数据",
            notes = "获取某个mac的天气数据。")
    @RequestMapping(method = RequestMethod.GET, value = "/getWeather/{user}")
    public ResponseEntity<Message> getWeather(@PathVariable String user,HttpServletRequest request) {
        Message message = new Message();
        try {
            Assert.notNull(user, "user cant be null");
            MainFrame mf = macService.deviceMac(user);
            if (mf == null) {
                message.setMsg(APIEm.NOTFOUNDUSERMAC.getCode(), APIEm.NOTFOUNDUSERMAC.getMessage());
            } else {
                WeatherStation weatherStation = weatherService.getWeatherByMac(mf.getDeviceMac());
                message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), weatherStation);
            }
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e.getMessage());
            e.printStackTrace();
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), "");
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET", value = "获取PM2.5数据",
            notes = "获取一周PM2.5数据。")
    @RequestMapping(method = RequestMethod.GET, value = "/getPM/{user}")
    public ResponseEntity<Message> getPm(@PathVariable String user, HttpServletRequest request, HttpServletResponse response) {

        Message message = new Message();
        try {
            Assert.notNull(user, "user cant be null");
            MainFrame mf = macService.deviceMac(user);
            if (mf == null) {
                message.setMsg(APIEm.NOTFOUNDUSERMAC.getCode(), APIEm.NOTFOUNDUSERMAC.getMessage());
            }else{
                String[] weeks = DateTools.printWeekdays();
                List<Pm25Object> pm25Objects = new ArrayList<>(7);
                LOGGER.info("get pm of weeks is :{}", new Object[]{weeks});
                for (String week : weeks) {
                    List<WeatherStation> weatherStations = weatherService.getWeatherPm25(mf.getDeviceMac(), week);
                    Pm25Object pm25Object;
                    if (!weatherStations.isEmpty()) {
                        WeatherStation weatherStation = weatherStations.get(0);
                        pm25Object = new Pm25Object(week, weatherStation.getPM());
                    } else {
                        pm25Object = new Pm25Object(week, "0");
                    }
                    pm25Objects.add(pm25Object);
                }
                message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), pm25Objects);
            }
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e.getMessage());
            e.printStackTrace();
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), "");
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiIgnore
    @ApiOperation(httpMethod = "GET", value = "获取天气数据(<font color='blue'>release</font>)",
            notes = "获取某个mac的天气数据。")
    @RequestMapping(method = RequestMethod.GET, value = "/getWeather/{pageNumber}/{mac}")
    public ResponseEntity<Message> getWeatherByPage(@PathVariable String mac, @PathVariable Integer pageNumber, HttpServletRequest request) {
        Message message = new Message();
        try {
            Assert.notNull(mac, "mac cant be null");
//            WeatherModle weatherModle = weatherService.getWeatherByMac(mac);
//            message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), weatherModle.getWeatherData());

            Page<WeatherModle> page = weatherService.getWeatherByPage(mac, pageNumber);
            int current = page.getNumber() + 1;
            int begin = Math.max(1, current - 5);
            int end = Math.min(begin + 10, page.getTotalPages());
            for (WeatherModle weatherModle : page.getContent()) {
                LOGGER.info("date :{}", new Object[]{weatherModle.getCreateDate()});
            }

            LOGGER.info("end:{}", new Object[]{end});
            LOGGER.info("totalNum:{}", new Object[]{page.getTotalElements()});
            LOGGER.info("totalPage:{}", new Object[]{page.getTotalPages()});
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e.getMessage());
            e.printStackTrace();
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), "");
        }
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

}
