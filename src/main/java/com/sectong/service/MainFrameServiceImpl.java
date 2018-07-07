package com.sectong.service;

import com.sectong.domain.mongomodle.MainFrame;
import com.sectong.domain.mongomodle.MainFrameFactory;
import com.sectong.repository.MainFrameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * Created by xueyong on 16/7/14.
 * mobileeasy.
 */
@Service
public class MainFrameServiceImpl implements MainFrameService {


    @Autowired
    @Qualifier(value = "mainframeRePository")
    MainFrameRepository mainFrameRepository;

    @Override
    public MainFrame insertMainframe(MainFrame mainFrame) {
        Assert.notNull(mainFrame, "mainFrame must not be null!");
        MainFrameFactory.initMainFrame(mainFrame);
        return mainFrameRepository.insert(mainFrame);
    }

    @Override
    public void remove(String user) {
//        mainFrameRepository.delete(id);
        mainFrameRepository.delete(mainFrameRepository.findByUser(user).stream().findFirst().get());
    }

    @Override
    public Collection<MainFrame> findByUserAndDeviceMac(String user, String deviceMac) {
        return mainFrameRepository.findByUserAndDeviceMac(user, deviceMac);
    }

    @Override
    public Collection<MainFrame> findByUser(String user) {
        return mainFrameRepository.findByUser(user);
    }

    @Override
    public Collection<MainFrame> findByDeviceMac(String deviceMac) {
        return mainFrameRepository.findByDeviceMac(deviceMac);
    }
}
