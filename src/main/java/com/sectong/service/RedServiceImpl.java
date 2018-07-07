package com.sectong.service;

import com.sectong.domain.mongomodle.InfraCommand;
import com.sectong.infrared.*;
import com.sectong.repository.RemoteCommandRepository;
import com.sectong.utils.SerializationObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xueyong on 16/9/9.
 * mobileeasy.
 */
@Service
public class RedServiceImpl implements RedService {

    @Autowired
    @Qualifier(value = "remoteCommandRePository")
    RemoteCommandRepository remoteCommandRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(RedServiceImpl.class);

    @Override
    public InfraredBO getAllModelOrders(String brands, String device) throws Exception {
        DeviceManager deviceManager = instance(device);
        deviceManager.init(brands, device);
        String models = deviceManager.queryModels().getModels();
        InfraredBO infraredBO = new InfraredBO();
        infraredBO.setBrands(brands);
        infraredBO.setDevice(device);
        List<Codes> codesList = new LinkedList<>();
        if (StringUtils.isNotBlank(models)) {
            for (String s : models.split(",")) {
                ModelEntity modelEntity = deviceManager.getKeyFileAndSquency(s);
                if (modelEntity == null) return null;
                LOGGER.info("modelEntity is :{},s :{}", new Object[]{SerializationObject.serialize(modelEntity), s});
                FormatEntity formatEntity = deviceManager.getFormat(modelEntity.getFormatId());
                if (formatEntity == null) return null;
                if (StringUtils.isBlank(s)) continue;
                Codes codes = new Codes();
                codes.setModel(s);
                String[] orders = deviceManager.createTestCode(modelEntity, formatEntity);
                codes.setOreders(orders);
                codesList.add(codes);
            }
            infraredBO.setModels(codesList);
        }
        return infraredBO;
    }

    @Override
    public List<DeviceFunction> ordersByModel(String brands, String device, String model) throws Exception {
        DeviceManager deviceManager = instance(device);
        deviceManager.init(brands, device);
        List<DeviceFunction> deviceFunctions = deviceManager.allStatus(model);
        return deviceFunctions;
    }

    @Override
    public InfraCommand savingRemoteCMD(InfraCommand infraCommand) throws Exception {
        return remoteCommandRepository.save(infraCommand);
    }

    @Override
    public InfraCommand loadCommand(String id) throws Exception {
        return remoteCommandRepository.findOne(id);
    }

    private DeviceManager instance(String device) {
        if ("AC".equals(device)) return new DeviceAC();
        if ("TV".equals(device)) return new DeviceTV();
        if ("STB".equals(device)) return new DeviceSTB();
        if ("DVD".equals(device)) return new DeviceDVD();
        if ("FAN".equals(device)) return new DeviceFAN();
        if ("ACL".equals(device)) return new DeviceACL();
        if ("IPTV".equals(device)) return new DeviceIPTV();
        return null;
    }
}
