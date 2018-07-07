package com.sectong.utils;

import com.sectong.config.PushConfig;
import com.sectong.push.AndroidNotification;
import com.sectong.push.PushClient;
import com.sectong.push.android.AndroidUnicast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xueyong on 16/7/21.
 * mobileeasy.
 */
public class PushUtils {

    private static Logger logger = LoggerFactory.getLogger(PushUtils.class);

    private static String appKey;
    private static String appMasterSecret;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    private PushClient pushClient;

    public PushUtils(String appKey, String appMasterSecret) {
        this.appKey = appKey;
        this.appMasterSecret = appMasterSecret;
    }

    public void pushMessage2Andriod(String token,Map<String,Object> field) {
        pushTextMessage2Andriod(token, AndroidNotification.DisplayType.MESSAGE, field);
    }

    private void pushTextMessage2Andriod(String token, AndroidNotification.DisplayType type, Map<String, Object> field) {
        try {
            AndroidUnicast unicast = new AndroidUnicast(appKey, appMasterSecret);
            unicast.setDeviceToken(token);
            unicast.goAppAfterOpen();
            unicast.setDisplayType(type);
            unicast.setProductionMode();
            // Set customized fields
            for (Iterator it = field.keySet().iterator(); it.hasNext();) {
                String key = (String) it.next();
                Object value = field.get(key);
                unicast.setPredefinedKeyValue(key, value);
            }
            logger.info("发送了推送消息:{}", new Object[]{SerializationObject.serialize(unicast)});
            this.pushClient.send(unicast);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext();
        ctx.register(PushConfig.class);
        ctx.refresh();
        PushUtils pushUtils = ctx.getBean(PushUtils.class);
        Map<String, Object> map = new HashMap<>(1);
        map.put("custom", "{\"abc\":\"123\"}");
        pushUtils.pushMessage2Andriod("AhnyyBFcfQqBO6a7mY2Z25ry9-kvLKDdy6m6xoeVzPhI", map);

    }
}
