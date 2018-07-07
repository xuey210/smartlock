package com.sectong.domain;

import com.sectong.domain.mongomodle.InfraCommand;
import com.sectong.utils.RedisUtil;

/**
 * Created by xueyong on 16/12/9.
 * mobileeasy-referal.
 */
public interface Commands {
    void savingRedis(InfraCommand infraCommand, RedisUtil redisUtil);
}
