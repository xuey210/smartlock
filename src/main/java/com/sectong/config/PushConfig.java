package com.sectong.config;

import com.sectong.push.PushClient;
import com.sectong.utils.PushUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by xueyong on 16/7/21.
 * mobileeasy.
 */
@Configuration
public class PushConfig {

    private static final String APPKEY = "578eeccd67e58e8ef3002757";
    private static final String APPMASTERSECRETKEY = "7yuodmqwe1g6ivnwj4lcj8eoq2rlrf03";

    @Bean
    public PushUtils pushUtils() {
        return new PushUtils(APPKEY, APPMASTERSECRETKEY);
    }

    @Bean
    public PushClient pushClient() {
        return new PushClient();
    }


}
