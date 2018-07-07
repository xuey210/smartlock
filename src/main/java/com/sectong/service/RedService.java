package com.sectong.service;

import com.sectong.domain.mongomodle.InfraCommand;
import com.sectong.infrared.DeviceFunction;
import com.sectong.infrared.InfraredBO;

import java.util.List;

/**
 * Created by xueyong on 16/9/9.
 * mobileeasy.
 */
public interface RedService {

    InfraredBO getAllModelOrders(String brands, String device) throws Exception;

    List<DeviceFunction> ordersByModel(String brands, String device, String model) throws Exception;

    InfraCommand savingRemoteCMD(InfraCommand infraCommand) throws Exception;

    InfraCommand loadCommand(String id) throws Exception;
}
