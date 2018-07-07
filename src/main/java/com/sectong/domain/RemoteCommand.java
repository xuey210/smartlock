package com.sectong.domain;

import com.sectong.domain.mongomodle.InfraCommand;
import com.sectong.utils.JsonUtil;
import com.sectong.utils.RedisUtil;

/**
 * Created by xueyong on 16/12/9.
 * mobileeasy-referal.
 */
public class RemoteCommand implements Commands {
    @Override
    public void savingRedis(InfraCommand infraCommand, RedisUtil redisUtil) {
        redisUtil.rpush(infraCommand.getDeviceMac() + "_remoteCommandQuene", JsonUtil.toJSONString(infraCommand));
    }
}
