package com.sectong.service;

import com.sectong.domain.mongomodle.*;
import com.sectong.domain.objectmodle.MainFrameRequest;
import com.sectong.repository.MacRepository;
import com.sectong.repository.RoomAndEqRepository;
import com.sectong.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by xueyong on 16/7/8.
 */

@Service
public class MacServiceImpl implements MacService {

    @Autowired
    @Qualifier(value = "macRep")
    MacRepository macRepository;

    @Autowired
    @Qualifier(value = "groupRep")
    UserGroupRepository userGroupRepository;

    @Autowired
    @Qualifier(value = "roomAndEqRepository")
    RoomAndEqRepository roomAndEqRepository;


    @Autowired
    MongoOperations mongoOperations;

    private ThreadLocal<MainFrame> mainFrameThreadLocal = new ThreadLocal<>();

    @Override
    public UserMac insertUserMac(UserMac userMac) {
        return macRepository.insert(userMac);
    }

    @Override
    public void remove(String id) {
        macRepository.delete(id);
    }

    @Override
    public Collection<UserMac> findByNameAndMac(String name, String mac) {
        return macRepository.findByNameAndMac(name, mac);
    }

    @Override
    public Collection<UserMac> findByName(String name) {
        return macRepository.findByName(name);
    }

    @Override
    public UserGroup insertUserGroup(UserGroup userGroup) {
        return userGroupRepository.insert(userGroup);
    }

    @Override
    public void deleteUserGroup(UserGroup userGroup) {
        userGroupRepository.deleteByUsernameAndSubUsername(userGroup.getUsername(), userGroup.getSubUsername());
    }

    @Override
    public List<UserGroup> findSubUsers(UserGroup userGroup) {
        return userGroupRepository.findByUsername(userGroup.getUsername());
    }

    @Override
    public List<UserGroup> findUsernameAndSubUsername(UserGroup userGroup) {
        return userGroupRepository.findByUsernameAndSubUsername(userGroup.getUsername(), userGroup.getSubUsername());
    }

    @Override
    public List<UserGroup> findSubUsernameAndUsername(UserGroup userGroup) {
        return userGroupRepository.findBySubUsernameAndUsername(userGroup.getSubUsername(), userGroup.getUsername());
    }

    @Override
    public Version insertVersion(Version version) {
        mongoOperations.insert(version);
        return version;
    }

    @Override
    public Orders inserOrders(MainFrameRequest mainFrameRequest) {
        Orders orders = new Orders();
        orders.setCreateTime(new Date());
        orders.setDeviceMac(mainFrameRequest.getDeviceMac());
        orders.setUser(mainFrameRequest.getUser());
        orders.setOrder(mainFrameRequest.getOrder());
        mongoOperations.insert(orders);
        return orders;
    }

    @Override
    public MainFrame deviceMac(String user) {
        Collection<MainFrame> mainFrames = mongoOperations.find(query(where("user").is(user)), MainFrame.class);
        if (!mainFrames.isEmpty()) {
            mainFrameThreadLocal.set(mainFrames.stream().findFirst().get());
        } else {
            List<UserGroup> userGroups = mongoOperations.find(query(where("subUsername").is(user)), UserGroup.class);
            if (!userGroups.isEmpty()) {
                UserGroup subuser = userGroups.stream().filter(userGroup -> userGroup.getSubUsername().equals(user)).findFirst().get();
                String username = subuser.getUsername();
                deviceMac(username);
            }
        }
        return mainFrameThreadLocal.get();
    }

    @Override
    public RoomAndEq insertRoomAndEq(RoomAndEq roomAndEq) {
        return roomAndEqRepository.save(roomAndEq);
    }

    @Override
    public Collection<RoomAndEq> findEqByUser(String user) {
        return roomAndEqRepository.findByUser(user);
    }

    @Override
    public void deleteByUser(String user) throws Exception{
        roomAndEqRepository.deleteByUser(user);
    }
}

