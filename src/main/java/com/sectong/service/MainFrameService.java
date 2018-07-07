package com.sectong.service;

import com.sectong.domain.mongomodle.MainFrame;

import java.util.Collection;

/**
 * Created by xueyong on 16/7/14.
 * mobileeasy.
 */
public interface MainFrameService {

    MainFrame insertMainframe(MainFrame mainFrame);

    void remove(String user);

    Collection<MainFrame> findByUserAndDeviceMac(String user, String deviceMac);

    Collection<MainFrame> findByUser(String user);

    Collection<MainFrame> findByDeviceMac(String deviceMac);

}
