package com.sectong.controller;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.WriteResult;
import com.sectong.constant.APIEm;
import com.sectong.domain.User;
import com.sectong.domain.mongomodle.*;
import com.sectong.domain.objectmodle.DevicesOfRoomResult;
import com.sectong.domain.objectmodle.JsonPushObject;
import com.sectong.domain.objectmodle.MainFrameRequest;
import com.sectong.message.Message;
import com.sectong.service.MacService;
import com.sectong.service.MainFrameService;
import com.sectong.service.UmengService;
import com.sectong.service.UserService;
import com.sectong.utils.JsonUtil;
import com.sectong.utils.PushUtils;
import com.sectong.utils.RedisUtil;
import com.sectong.utils.SerializationObject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

/**
 * 处理用户类接口
 *
 * @author jiekechoo
 */
@RestController
@Api(basePath = "/mac", value = "主机设备API", description = "描述主机的相关操作,包括绑定、解除、子账号等操作", produces = "application/json")
@RequestMapping("/mac")
public class MacController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MacController.class);

    @Autowired
    private MacService macService;
    @Autowired
    UserService userService;
    @Autowired
    private MainFrameService mainFrameService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    MongoOperations mongoTemplate;
    @Autowired
    PushUtils pushUtils;
    @Autowired
    UmengService umengService;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    @ApiOperation(httpMethod = "POST", value = "绑定mac",
            notes = "用户绑定mac")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户名", required = true, dataType = "String")
            ,@ApiImplicitParam(name = "deviceMac", value = "主机的MAC地址", required = true, dataType = "String")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/bindingMac")
    public ResponseEntity<Message> bindingMac(HttpServletRequest request, MainFrame mainFrame) {
        Message message = new Message();
        try {
            //查询是否绑定。绑定
            Assert.notNull(mainFrame.getUser());
            Assert.notNull(mainFrame.getDeviceMac());
            User user_c = userService.getUserByUsername(mainFrame.getUser());//是注册用户
            Collection<MainFrame> userframes = mainFrameService.findByUser(mainFrame.getUser());
            Collection<MainFrame> mainFrames = mainFrameService.findByDeviceMac(mainFrame.getDeviceMac());
            if (user_c != null && !(mainFrames != null && mainFrames.size() > 0) && !(userframes != null && userframes.size() > 0)) {
                MainFrame user = mainFrameService.insertMainframe(mainFrame);
                message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), user);
            } else {
                message.setMsg(APIEm.SUBUSERFILE.getCode(), APIEm.SUBUSERFILE.getMessage(), null);
            }
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), e.getMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET", value = "给定一个用户，检查是否已经绑定了MAC")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户名", required = true, dataType = "String")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/isbinding")
    public ResponseEntity<Message> isbinding(HttpServletRequest request, MainFrame mainFrame) {
        Message message = new Message();
        Object result;
        try {
            Assert.notNull(mainFrame.getUser(), "mainframe.user must not be null!");
            Collection<MainFrame> linkedList = mainFrameService.findByUser(mainFrame.getUser());
            message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), linkedList);
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            result = e.getMessage();
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), result);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * 服务端处理主机发送的请求
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/post")
    @ApiOperation(httpMethod = "POST", value = "主机发送BODY请求，根据请求的action属性不同返回不同的内容，具体可咨询子志")
    public ResponseEntity<String> post(HttpServletRequest request) {
        String result = "ok";
        try {
            String param = IOUtils.toString(request.getInputStream());
            MainFrameRequest mainFrameRequest = JsonUtil.parseObject(param, MainFrameRequest.class);
            Assert.notNull(mainFrameRequest);
            Assert.notNull(mainFrameRequest.getAction(), "action must not be null!");
            LOGGER.info("post:{}", new Object[]{param});
            switch (mainFrameRequest.getAction()) {
                case "getuserlist": {
                    StringBuilder sb = new StringBuilder("userlist,[");
                    Assert.notNull(mainFrameRequest.getDeviceMac());
                    Collection<MainFrame> collection = mainFrameService.findByDeviceMac(mainFrameRequest.getDeviceMac());
                    String main_user = "";
                    for (Iterator<MainFrame> it = collection.iterator(); it.hasNext(); ) {
                        main_user = it.next().getUser();
                        sb.append(main_user).append(",");
                    }
                    UserGroup userGroup = new UserGroup();
                    userGroup.setUsername(main_user);
                    List<UserGroup> userGroups = macService.findSubUsers(userGroup);
                    for (UserGroup u : userGroups) {
                        sb.append(u.getSubUsername()).append(",");
                    }
                    sb.append("]");
                    result = sb.toString();
                    break;
                }
                case "getcommand": {
                    Assert.notNull(mainFrameRequest.getDeviceMac());
                    result = (String) redisUtil.lpop(mainFrameRequest.getDeviceMac() + "-order-list");
                    break;
                }
                case "postdevice": {
                    Assert.notNull(mainFrameRequest.getDeviceMac(), "deviceMac must not be null!");
                    Assert.notNull(mainFrameRequest.getRoom(), "room must not be null!");
//                    Assert.notNull(mainFrameRequest.getStatus(), "status must not be null!");
                    Assert.notNull(mainFrameRequest.getDevice(), "device must not be null!");
                    Assert.notNull(mainFrameRequest.getIsNeedCallBack(), "isNeedCallBack must be not null.");

                    Query check_qurty = query(new Criteria().andOperator(
                            where("deviceMac").is(mainFrameRequest.getDeviceMac())
                                    .and("roomList.name").is(mainFrameRequest.getRoom())
//                                    .and("roomList.name.equipment." + mainFrameRequest.getDevice() + ".status").is(mainFrameRequest.getStatus())
                            )
                    );
                    List<MainFrame> checkList = mongoTemplate.find(check_qurty, MainFrame.class);
                    LOGGER.info("params:device:{},status:{},checkList size:{}", new Object[]{mainFrameRequest.getDevice(), mainFrameRequest.getStatus(), checkList.size()});
                    LOGGER.info("condition1-isEmpty:{},condition2:{}", new Object[]{CollectionUtils.isEmpty(checkList), StringUtils.isNotBlank(mainFrameRequest.getStatus())});
                    if (StringUtils.isNotBlank(mainFrameRequest.getStatus())) {
                        MainFrame mainFrame = checkList.get(0);
                        List<Room> roomList = mainFrame.getRoomList();
                        for (Room room : roomList) {
                            if(!room.getName().equals(mainFrameRequest.getRoom())) continue;
                            LOGGER.info("room:{},eq:{}", new Object[]{room.getName(), SerializationObject.serialize(room.getEquipment())});
                            if(room.getEquipment() == null || room.getEquipment().isEmpty()) continue;
                            if (room.getEquipment().get(mainFrameRequest.getDevice()) != null && !room.getEquipment().get(mainFrameRequest.getDevice()).getStatus().equals(mainFrameRequest.getStatus())) {
                                LOGGER.info("update success :{},:{},{}", new Object[]{room.getName(), mainFrameRequest.getDevice(), mainFrameRequest.getStatus()});
                                Query q = query(new Criteria().andOperator(
                                        where("deviceMac").is(mainFrameRequest.getDeviceMac()),
                                        where("roomList.name").is(mainFrameRequest.getRoom())));
                                EquipMent equipMent = new EquipMent(mainFrameRequest.getDevice(), mainFrameRequest.getStatus());
                                Update u = update("roomList.$.equipment." + mainFrameRequest.getDevice(), equipMent);
                                WriteResult writeResult = mongoTemplate.updateMulti(q, u, MainFrame.class);
                                LOGGER.info(writeResult.toString());
                                if (isNeedToPush(mainFrameRequest.getIsNeedCallBack())) {
                                    LOGGER.info("push message ok");
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(mainFrameRequest.getDeviceMac()).append(",")
                                            .append(mainFrameRequest.getRoom()).append(",")
                                            .append(mainFrameRequest.getDevice()).append(",")
                                            .append(mainFrameRequest.getStatus());
                                    JsonPushObject jsonPushObject = new JsonPushObject("updateStatus", sb.toString());
                                    sendMsg(mainFrameRequest.getDeviceMac(), jsonPushObject);
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                    break;
                }
                case "postresult": {
                    Assert.notNull(mainFrameRequest.getDeviceMac());
                    Assert.notNull(mainFrameRequest.getRoom());
                    Assert.notNull(mainFrameRequest.getModel());
                    Query q = query(new Criteria().andOperator(
                            where("deviceMac").is(mainFrameRequest.getDeviceMac()),
                            where("roomList.name").is(mainFrameRequest.getRoom())));
                    Update u = update("roomList.$.currentModel", mainFrameRequest.getModel());
                    WriteResult writeResult = mongoTemplate.upsert(q, u, MainFrame.class);
                    LOGGER.info("postresult:" + writeResult.toString());
                    break;
                }
                case "postsecurity": {
                    Assert.notNull(mainFrameRequest.getDeviceMac());
                    Assert.notNull(mainFrameRequest.getModel());
                    Query q = query(new Criteria().andOperator(
                            where("deviceMac").is(mainFrameRequest.getDeviceMac())));
                    Update u = update("security", mainFrameRequest.getModel());
                    WriteResult writeResult = mongoTemplate.upsert(q, u, MainFrame.class);
                    LOGGER.info("postresult:" + writeResult.toString());
                    break;
                }
                case "postWeatherData": {
                    Assert.notNull(mainFrameRequest.getDeviceMac());
                    Query q = query(new Criteria().andOperator(
                            where("deviceMac").is(mainFrameRequest.getDeviceMac())));
                    Update u = update("weatherData", mainFrameRequest.getWeatherData());
                    WriteResult writeResult = mongoTemplate.upsert(q, u, MainFrame.class);
                    LOGGER.info("postWeatherData:" + writeResult.toString());
                    break;
                }
                case "retrivalOrder": {
                    Assert.notNull(mainFrameRequest.getOrderId());
                    Orders orders = mongoTemplate.findById(mainFrameRequest.getOrderId(), Orders.class);
                    result = orders != null ? orders.getOrder() : result;
                    break;
                }
                case "postadddevice":{
                    Assert.notNull(mainFrameRequest.getDeviceMac());
                    Assert.notNull(mainFrameRequest.getRoom());
                    Assert.notNull(mainFrameRequest.getDevice());
                    Assert.notNull(mainFrameRequest.getIsNeedCallBack());
                    Query q = query(new Criteria().andOperator(
                            where("deviceMac").is(mainFrameRequest.getDeviceMac()),
                            where("roomList.name").is(mainFrameRequest.getRoom())));
                    EquipMent equipMent = new EquipMent(mainFrameRequest.getDevice(), "");
                    Update u = update("roomList.$.equipment." + mainFrameRequest.getDevice(), equipMent);
                    WriteResult writeResult = mongoTemplate.upsert(q, u, MainFrame.class);
                    LOGGER.info(writeResult.toString());
                    if (isNeedToPush(mainFrameRequest.getIsNeedCallBack())) {
                        JsonPushObject jsonPushObject = new JsonPushObject("addDevice", mainFrameRequest.getRoom() + "," + mainFrameRequest.getDevice());
                        sendMsg(mainFrameRequest.getDeviceMac(), jsonPushObject);
                    }
                    break;
                }
                case "postremovedevice":{
                    Assert.notNull(mainFrameRequest.getDeviceMac());
                    Assert.notNull(mainFrameRequest.getRoom());
                    Assert.notNull(mainFrameRequest.getDevice());
                    Assert.notNull(mainFrameRequest.getIsNeedCallBack());
                    Query q = query(new Criteria().andOperator(
                            where("deviceMac").is(mainFrameRequest.getDeviceMac()),
                            where("roomList.name").is(mainFrameRequest.getRoom())));
                            Update u = new Update().unset("roomList.$.equipment." + mainFrameRequest.getDevice());
                    WriteResult writeResult = mongoTemplate.updateMulti(q, u, MainFrame.class);
                    LOGGER.info(writeResult.toString());
                    if(isNeedToPush(mainFrameRequest.getIsNeedCallBack())) {
                        JsonPushObject jsonPushObject = new JsonPushObject("removeDevice", mainFrameRequest.getRoom() + "," + mainFrameRequest.getDevice());
                        sendMsg(mainFrameRequest.getDeviceMac(), jsonPushObject);
                    }
                    break;
                }
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "error";
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private boolean isNeedToPush(String isNeedCallBack) {
        return "y".endsWith(isNeedCallBack);
    }

    private void sendMsg(String deviceMac, JsonPushObject jsonPushObject) {
        try {
            executorService.execute(() -> {
                MainFrame mainFrame = mainFrameService.findByDeviceMac(deviceMac).iterator().next();
                User userInfo = userService.getUserByUsername(mainFrame.getUser());
                List<UserGroup> userGroups = macService.findSubUsers(new UserGroup(userInfo.getUsername()));
                Map<String, Object> map = new HashMap<>(1);
                map.put("custom", JSONObject.toJSON(jsonPushObject));
                umengService.WhenInfraOrderFinished(userInfo, "设置已经完成", "宅生活", JsonUtil.toJSONString(map));
                //通知所有子账号
                for (UserGroup subusers : userGroups) {
                    User subUser = userService.getUserByUsername(subusers.getSubUsername());
                    LOGGER.info("通知用户{}", subUser.getUsername());
                    umengService.WhenInfraOrderFinished(subUser, "设置已经完成", "宅生活", JsonUtil.toJSONString(map));
                }
            });
        } catch (Exception e) {
            LOGGER.error("推送失败:{}", new Object[]{e});
            e.printStackTrace();
        }
    }

    @ApiOperation(httpMethod = "POST", value = "解除绑定的设备")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户名", required = true, dataType = "String")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/removeBindindMac")
    public ResponseEntity<Message> removeBindindMac(HttpServletRequest request, MainFrame mainFrame) {
        Message message = new Message();
        String result = "success";
        try {
            Assert.notNull(mainFrame.getUser(), "user is null");
            mainFrameService.remove(mainFrame.getUser());
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            result = e.getMessage();
        }
        message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), result);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "POST", value = "绑定子账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
            ,@ApiImplicitParam(name = "subusername", value = "子用户名", required = true, dataType = "String")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/subUserAdd")
    public ResponseEntity<Message> subUser(HttpServletRequest request, UserGroup userGroup) {
        Message message = new Message();
        String result = "";
        try {
            Assert.notNull(userGroup.getUsername(), "username must not be null!");
            Assert.notNull(userGroup.getSubUsername(), "subusername must not be null!");
            String subName = userGroup.getSubUsername();
            User user = userService.getUserByUsername(subName);
            boolean subUserisAuser = (user != null);
            Collection<MainFrame> collection = mainFrameService.findByUser(subName);//绑定过设备
            boolean subuserIsNotBinded = collection.isEmpty();
            List<UserGroup> userGroupList = macService.findSubUsernameAndUsername(userGroup);//绑定子账户
            boolean subuserIsNotBindedToUser = userGroupList.isEmpty();
            LOGGER.info("status is:{},{},{}", new Object[]{subUserisAuser, subuserIsNotBinded, subuserIsNotBindedToUser});
            if(subUserisAuser && subuserIsNotBinded && subuserIsNotBindedToUser){
                macService.insertUserGroup(userGroup);
                umengService.WhenInfraOrderFinished(user,
                        "主账号已添加您到家庭系统中", "宅生活", "主账号已添加您到家庭系统中");
                notifyChangeOfUserList(userGroup.getUsername());
                message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), result);
            } else {
                message.setMsg(APIEm.SUBUSERFILE.getCode(), APIEm.SUBUSERFILE.getMessage(), result);
            }
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), e.getMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }



    private void notifyChangeOfUserList(String username) {
        MainFrame mainFrame = getMainFrame(username);
        MainFrameRequest mainFrameRequest = new MainFrameRequest();
        mainFrameRequest.setUser(mainFrame.getUser());
        mainFrameRequest.setDeviceMac(mainFrame.getDeviceMac());
        mainFrameRequest.setOrder("[userlistchanged]");
        Orders orders = macService.inserOrders(mainFrameRequest);
        redisUtil.rpush(mainFrame.getDeviceMac() + "-order-list", orders.trancefer());
    }

    private MainFrame getMainFrame(String username) {
        Collection<MainFrame> mainframes = mainFrameService.findByUser(username);
        MainFrame mainFrame = null;
        while (mainframes.iterator().hasNext()) {
            mainFrame = mainframes.iterator().next();
            break;
        }
        return mainFrame;
    }

    @ApiOperation(httpMethod = "POST", value = "解除绑定子账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
            ,@ApiImplicitParam(name = "subusername", value = "子用户名", required = true, dataType = "String")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/subUserRemove")
    public ResponseEntity<Message> subUserRemove(HttpServletRequest request, UserGroup userGroup) {
        Message message = new Message();
        String result = "success";
        try {
            Assert.notNull(userGroup.getUsername(), "username must not be null!");
            Assert.notNull(userGroup.getSubUsername(), "subusername must not be null!");
            User mainuser = userService.getUserByUsername(userGroup.getUsername());
            User user = userService.getUserByUsername(userGroup.getSubUsername());
            Assert.notNull(mainuser, "username is not founded");
            Assert.notNull(user, "subusername is not founded");
            macService.deleteUserGroup(userGroup);
            notifyChangeOfUserList(userGroup.getUsername());
            umengService.WhenInfraOrderFinished(user,
                    "您已被主账号从家庭系统中解除", "宅生活", "您已被主账号从家庭系统中解除");
            message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), result);
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), e.getMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET", value = "查询一个用户名下的子用户（可能多个）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/subUsers")
    public ResponseEntity<Message> subUsers(HttpServletRequest request, UserGroup userGroup) {
        Message message = new Message();
        try {
            Assert.notNull(userGroup.getUsername(), "username must not be null!");
            message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), macService.findSubUsers(userGroup));
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), e.getMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * app 获取所有房间的当前模式
     *
     * @param request
     * @param mainFrameRequest
     * @return
     */
    @ApiOperation(httpMethod = "GET", value = "查询一个用户和给定房间的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户名", required = true, dataType = "String")
            ,@ApiImplicitParam(name = "room", value = "房间名", required = true, dataType = "String")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/getRooms")
    public ResponseEntity<Message> getRooms(HttpServletRequest request, MainFrameRequest mainFrameRequest) {
        Message message = new Message();
        Object result;
        try {
            Assert.notNull(mainFrameRequest.getUser(), "username must not be null!");
//            Assert.notNull(mainFrameRequest.getRoom(), "room must not be null!");
            MainFrame mf = macService.deviceMac(mainFrameRequest.getUser());
            if (mf == null) {
                message.setMsg(APIEm.NOTFOUNDUSERMAC.getCode(), APIEm.NOTFOUNDUSERMAC.getMessage(), null);
            } else {
                if (mainFrameRequest.getRoom() != null) {
                    result = mf.getRoomList().stream().filter(room -> room.getName().equals(mainFrameRequest.getRoom()))
                            .map(rooms -> new RoomConvter(rooms))
                            .collect(Collectors.toList());
                }else{
                    result = mf.getRoomList().stream()
                            .map(rooms -> new RoomConvter(rooms))
                            .collect(Collectors.toList());
                }
                message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), result);
            }
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), e.getMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * app 获取所有房间的当前模式
     *
     * @param request
     * @param roomAndEq
     * @return
     */
    @ApiOperation(httpMethod = "POST", value = "设置房间和设备绑定",notes = "设置房间和设备绑定")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户名", required = true, dataType = "String")
            ,@ApiImplicitParam(name = "room", value = "房间名", required = true, dataType = "String")
            ,@ApiImplicitParam(name = "eq", value = "设备名", required = true, dataType = "String")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/setEq")
    public ResponseEntity<Message> setEq(HttpServletRequest request, RoomAndEq roomAndEq) {
        Message message = new Message();
        try {
            Assert.notNull(roomAndEq.getUser(), "username must not be null!");
            Assert.notNull(roomAndEq.getRoom(), "room must not be null!");
            Assert.notNull(roomAndEq.getEq(), "eq must not be null!");
            MainFrame mainFrame = macService.deviceMac(roomAndEq.getUser());
            if (mainFrame == null) {
                message.setMsg(APIEm.NOTFOUNDUSERMAC.getCode(), APIEm.NOTFOUNDUSERMAC.getMessage(), null);
            }else{
                Query q = query(new Criteria().andOperator(
                        where("user").is(mainFrame.getUser()),
                        where("roomList.name").is(roomAndEq.getRoom())));
                EquipMent equipMent = new EquipMent(roomAndEq.getEq(), "");
                Update u = update("roomList.$.equipment." + roomAndEq.getEq(), equipMent);
                WriteResult writeResult = mongoTemplate.upsert(q, u, MainFrame.class);
                message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), writeResult.getN());
            }
//            RoomAndEq ra = macService.insertRoomAndEq(roomAndEq);
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), e.getMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "POST", value = "设置房间的模式",notes = "设置房间的模式")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户名", required = true, dataType = "String")
            ,@ApiImplicitParam(name = "room", value = "房间名", required = true, dataType = "String")
            ,@ApiImplicitParam(name = "model", value = "模式名", required = true, dataType = "String")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/setModel")
    public ResponseEntity<Message> setModel(HttpServletRequest request, RoomAndEq roomAndEq) {
        Message message = new Message();
        try {
            Assert.notNull(roomAndEq.getUser(), "username must not be null!");
            Assert.notNull(roomAndEq.getRoom(), "room must not be null!");
            Assert.notNull(roomAndEq.getModel(), "model must not be null!");
            MainFrame mainFrame = macService.deviceMac(roomAndEq.getUser());
            if (mainFrame == null) {
                message.setMsg(APIEm.NOTFOUNDUSERMAC.getCode(), APIEm.NOTFOUNDUSERMAC.getMessage(), null);
            }else{
                Query q = query(new Criteria().andOperator(
                        where("user").is(mainFrame.getUser()),
                        where("roomList.name").is(roomAndEq.getRoom())));
                Update u = new Update();
                WriteResult writeResult = mongoTemplate.upsert(q, u.addToSet("roomList.$.models", roomAndEq.getModel()), MainFrame.class);
                message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), writeResult.getN());
            }
//            RoomAndEq ra = macService.insertRoomAndEq(roomAndEq);
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), e.getMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "POST", value = "设置取消房间模式",notes = "设置取消房间的模式")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户名", required = true, dataType = "String")
            ,@ApiImplicitParam(name = "room", value = "房间名", required = true, dataType = "String")
            ,@ApiImplicitParam(name = "model", value = "模式名", required = true, dataType = "String")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/unSetModel")
    public ResponseEntity<Message> unSetModel(HttpServletRequest request, RoomAndEq roomAndEq) {
        Message message = new Message();
        try {
            Assert.notNull(roomAndEq.getUser(), "username must not be null!");
            Assert.notNull(roomAndEq.getRoom(), "room must not be null!");
            Assert.notNull(roomAndEq.getModel(), "model must not be null!");
            MainFrame mainFrame = macService.deviceMac(roomAndEq.getUser());
            if (mainFrame == null) {
                message.setMsg(APIEm.NOTFOUNDUSERMAC.getCode(), APIEm.NOTFOUNDUSERMAC.getMessage(), null);
            }else{
                Query q = query(new Criteria().andOperator(
                        where("user").is(mainFrame.getUser()),
                        where("roomList.name").is(roomAndEq.getRoom())));
                Update u = new Update();
                WriteResult writeResult = mongoTemplate.upsert(q, u.pull("roomList.$.models", roomAndEq.getModel()), MainFrame.class);
                message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), writeResult.getN());
            }
//            RoomAndEq ra = macService.insertRoomAndEq(roomAndEq);
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), e.getMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "POST", value = "初始化房间",notes = "本接口删除了房间的配置，视为初始化")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户名", required = true, dataType = "String")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/initEq")
    public ResponseEntity<Message> initEq(HttpServletRequest request, RoomAndEq roomAndEq) {
        Message message = new Message();
        try {
            MainFrame mainFrame = macService.deviceMac(roomAndEq.getUser());
            Assert.notNull(roomAndEq.getUser(), "username must not be null!");
            if (mainFrame == null) {
                message.setMsg(APIEm.NOTFOUNDUSERMAC.getCode(), APIEm.NOTFOUNDUSERMAC.getMessage(), null);
            }else{
                mainFrame.getRoomList().stream().forEach((room) ->
                        mongoTemplate.updateFirst(query(new Criteria().andOperator(
                                where("user").is(roomAndEq.getUser()),
//                                where("roomList.$.equipment").exists(true),
                                where("roomList.name").is(room.getName()))), new Update().unset("roomList.$.equipment"),MainFrame.class));
                message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), null);
            }
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), e.getMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET", value = "查询用户设置的房间和设备")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户名", required = true, dataType = "String")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/getEq")
    public ResponseEntity<Message> getEq(HttpServletRequest request, String user) {
        Message message = new Message();
        try {
            //用户名、mac、房间名、模式名
            Assert.notNull(user, "user must not be null!");
            MainFrame mf = macService.deviceMac(user);
            if (mf == null) {
                message.setMsg(APIEm.NOTFOUNDUSERMAC.getCode(), APIEm.NOTFOUNDUSERMAC.getMessage(), null);
            } else {
//                Collection<RoomAndEq> roomAndEqCollection = macService.findEqByUser(user);
//                mf.getRoomList().stream().collect(Collectors.groupingBy(Room::getName, Collectors.mapping(Room::getEquipment, Collectors.toList())))
                List<DevicesOfRoomResult> devicesOfRoomResultList = new LinkedList<>();
                if (mf.getRoomList() != null && mf.getRoomList().size() > 0) {
                    mf.getRoomList().stream().filter(m -> m.getEquipment() != null).forEach((room) -> devicesOfRoomResultList
                            .add(new DevicesOfRoomResult(room.getName(), room.getEquipment().entrySet().stream().map(Map.Entry::getKey)
                                    .collect(Collectors.toList()))));
                }
                message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), devicesOfRoomResultList);
            }
        } catch (Exception e) {
            LOGGER.error("get exception :{}", e);
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), e.getMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    /**
     * app 发送一条指令
     *
     * @param request
     * @param mainFrameRequest
     * @return
     */
    @ApiOperation(httpMethod = "POST", value = "向服务器发送一个order指定，服务器会通知主机")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户名", required = true, dataType = "String")
            ,@ApiImplicitParam(name = "order", value = "指定", required = true, dataType = "String")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/order")
    public ResponseEntity<Message> order(HttpServletRequest request, MainFrameRequest mainFrameRequest) {
        Message message = new Message();
        try {
            //用户名、mac、房间名、模式名
            Assert.notNull(mainFrameRequest, "mainframe must not be null!");
            Assert.notNull(mainFrameRequest.getUser(), "user must not be null!");
            Assert.notNull(mainFrameRequest.getOrder(), "deviceMac must not be null!");
            MainFrame mf = macService.deviceMac(mainFrameRequest.getUser());
            if (mf == null) {
                message.setMsg(APIEm.NOTFOUNDUSERMAC.getCode(), APIEm.NOTFOUNDUSERMAC.getMessage(), null);
            } else {
                mainFrameRequest.setDeviceMac(mf.getDeviceMac());
                Orders orders = macService.inserOrders(mainFrameRequest);
                redisUtil.rpush(mainFrameRequest.getDeviceMac() + "-order-list", orders.trancefer());
                message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), null);
            }
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), e.getMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET", value = "查询用户的天气数据，返回天气数据和安防设置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户名", required = true, dataType = "String")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/getWeather/{user}")
    public ResponseEntity<Message> getWeather(HttpServletRequest request, @PathVariable String user) {
        Message message = new Message();
        try {
            //用户名、mac、房间名、模式名
            Assert.notNull(user, "deviceMac must not be null!");
            MainFrame mainFrame = macService.deviceMac(user);
            if (mainFrame == null) {
                message.setMsg(APIEm.NOTFOUNDUSERMAC.getCode(), APIEm.NOTFOUNDUSERMAC.getMessage(), null);
            } else {
                message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), mainFrame.getWeatherData() + "," + mainFrame.getSecurity());
            }
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage(), e.getMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
