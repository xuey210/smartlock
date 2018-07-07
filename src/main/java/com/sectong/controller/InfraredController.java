package com.sectong.controller;

import com.sectong.constant.APIEm;
import com.sectong.domain.Commands;
import com.sectong.domain.LazyCommand;
import com.sectong.domain.RemoteCommand;
import com.sectong.domain.User;
import com.sectong.domain.mongomodle.InfraCommand;
import com.sectong.domain.objectmodle.CommandResult;
import com.sectong.domain.objectmodle.JsonPushObject;
import com.sectong.infrared.InfraredBO;
import com.sectong.message.Message;
import com.sectong.service.RedService;
import com.sectong.service.UserService;
import com.sectong.utils.JsonUtil;
import com.sectong.utils.PushUtils;
import com.sectong.utils.RedisUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 处理用户类接口
 *
 * @author jiekechoo
 */
@RestController
@Api(basePath = "/red", value = "红外码相关API", description = "设备", produces = "application/json")
@RequestMapping("/red")
public class InfraredController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfraredController.class);
    @Autowired
    private RedService redService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserService userService;
    @Autowired
    PushUtils pushUtils;

    @ApiOperation(httpMethod = "GET", value = "获取测试红外码",notes = "获取某设备和品牌下的所有红外码，仅包括开和关的红外码，具体可参数ACCESS数据表")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "device", value = "设备", required = true, dataType = "String")
//            ,@ApiImplicitParam(name = "brands", value = "品牌", required = true, dataType = "String")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/tryModel/{device}/{brands}")
    public ResponseEntity<Message> tryModel(HttpServletRequest request, @PathVariable String brands,
                                              @PathVariable String device) {
        Message message = new Message();
        try {
            Assert.notNull(brands);
            Assert.notNull(device);
            InfraredBO infraredBO = redService.getAllModelOrders(brands, device);
            message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), infraredBO);
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), e.getMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET", value = "获取红外码",notes = "获取某设备和品牌下的一种型号下的所有红外码，具体可参数ACCESS数据表")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "device", value = "设备", required = true, dataType = "String")
//            ,@ApiImplicitParam(name = "brands", value = "品牌", required = true, dataType = "String")
//            ,@ApiImplicitParam(name = "model", value = "型号", required = true, dataType = "String")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/model/{device}/{brands}/{model}")
    public ResponseEntity<Message> ordersByModel(HttpServletRequest request, @PathVariable String brands,
                                              @PathVariable String device, @PathVariable String model) {
        Message message = new Message();
        try {
            Assert.notNull(model);
            Assert.notNull(brands);
            Assert.notNull(device);
            message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), redService.ordersByModel(brands, device, model));
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), e.getMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "POST", value = "APP发送 远程控制指令")
    @RequestMapping(method = RequestMethod.POST, value = "/remoteCMD")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "command", value = "需要发送的指令", required = true, dataType = "String")
            , @ApiImplicitParam(name = "type", value = "指令的类型，可选：remote | lazy", required = true, dataType = "String")
            , @ApiImplicitParam(name = "deviceMac", value = "主机的MAC地址", required = true, dataType = "String")
            , @ApiImplicitParam(name = "user", value = "用户", required = true, dataType = "String")
    })
    public ResponseEntity<Message> remoteCMD(HttpServletRequest request, InfraCommand command) {
        Message message = new Message();
        try {
            //用户名、mac、房间名、模式名
            Assert.notNull(command, "command must not be null!");
            Assert.notNull(command.getDeviceMac(), "deviceMac must not be null!");
            Assert.notNull(command.getType(), "type must not be null!");
            Assert.notNull(command.getUser(), "type must not be null!");
            command.setCreateTime(new Date());
            InfraCommand ic = redService.savingRemoteCMD(command);
            Commands commands = null;
            if (command.getType().equals("remote")) {
                commands = new RemoteCommand();
            }
            if (command.getType().equals("lazy")) {
                commands = new LazyCommand();
            }
            commands.savingRedis(ic, redisUtil);
            message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), ic);
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), e.getMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET", value = "红外主机获取远程控制指令", notes = "此处获取2种指令，1远程指令，2延时指定。返回内容为JSON")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceMac", value = "deviceMac", required = true, dataType = "String")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/getRemoteAndLazyCMD")
    public ResponseEntity<Message> getRemoteAndLazyCMD(HttpServletRequest request, InfraCommand infraCommand) {

        Assert.notNull(infraCommand);
        Assert.notNull(infraCommand.getDeviceMac());
        Message message = new Message();

        List<Object> remote = new LinkedList<>();
        try {
            while (true) {
                String obj = (String) redisUtil.lpop(infraCommand.getDeviceMac() + "_remoteCommandQuene");
                if (StringUtils.isNotBlank(obj)) {
                    remote.add(obj);
                }else{
                    break;
                }
            }
            List<Object> lazy = new LinkedList<>();
            while (true) {
                String obj = (String) redisUtil.lpop(infraCommand.getDeviceMac() + "_lazyCommandQuene");
                if (StringUtils.isNotBlank(obj)) {
                    lazy.add(obj);
                }else{
                    break;
                }
            }
            CommandResult commandResult = new CommandResult(remote, lazy, infraCommand.getDeviceMac());
            message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), commandResult);
        } catch (Exception e) {
            LOGGER.error("get exception :{}", e);
            e.printStackTrace();
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), e.getMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "POST", value = "主机执行指令结果接口", notes = "主机完成指令后，把执行后的接口传到云端，云端负责推送到用户的手机上.\n参数,id:xxxx\n deviceMac:xxx \n result:success/fail")
    @RequestMapping(method = RequestMethod.POST, value = "/notifyClient")
    public ResponseEntity<Message> notifyClient(HttpServletRequest request) {
        Message message = new Message();
        try {
            InfraCommand infraCommand = redService.loadCommand(request.getParameter("id"));
            User userInfo = userService.getUserByUsername(infraCommand.getUser());
            Map<String, Object> map = new HashMap<>(1);
            JsonPushObject jsonPushObject = new JsonPushObject(
                    "command_" + infraCommand.getId() + "_" + request.getParameter("deviceMac")
                    , request.getParameter("result"));
            map.put("custom", JsonUtil.toJSONString(jsonPushObject));
            pushUtils.pushMessage2Andriod(userInfo.getDeviceToken(), map);
            message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage());
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), e.getMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
