package com.sectong.service;

import com.sectong.domain.mongomodle.*;
import com.sectong.domain.objectmodle.MainFrameRequest;

import java.util.Collection;
import java.util.List;

/**
 * Created by xueyong on 16/7/8.
 * demo.
 */
public interface MacService {
    UserMac insertUserMac(UserMac userMac);

    void remove(String id);

    Collection<UserMac> findByNameAndMac(String name, String mac);
    Collection<UserMac> findByName(String name);

    UserGroup insertUserGroup(UserGroup userGroup);

    void deleteUserGroup(UserGroup userGroup);

    List<UserGroup> findSubUsers(UserGroup userGroup);

    List<UserGroup> findUsernameAndSubUsername(UserGroup userGroup);

    List<UserGroup> findSubUsernameAndUsername(UserGroup userGroup);

    Version insertVersion(Version version);

    Orders inserOrders(MainFrameRequest mainFrameRequest);

    MainFrame deviceMac(String user);

    RoomAndEq insertRoomAndEq(RoomAndEq roomAndEq);

    Collection<RoomAndEq> findEqByUser(String user);

    void deleteByUser(String user) throws Exception;
}
