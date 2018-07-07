package com.sectong.service;

import com.sectong.domain.User;
import com.sectong.push.AndroidNotification;
import com.sectong.push.PushClient;
import com.sectong.push.android.AndroidCustomizedcast;
import com.sectong.push.ios.IOSCustomizedcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by xueyong on 16/12/27.
 * mobileeasy-referal.
 */
@Service
public class UmengServiceImpl implements UmengService {

    private static final Logger logger = LoggerFactory.getLogger(UmengServiceImpl.class);

    private final String appkey_android = "57ac2af167e58e4a45003a83";
    private final String appkey_ios = "58afa368c62dca150800034a";
    private final String appMasterSecret_android = "aswgtpq4rqwr6aqc7z05lliy7zcgqfkl";
    private final String appMasterSecret_ios = "mrsb241m64oeivwgwhpgdtixn8ity7d4";
    private String timestamp = null;
    private PushClient client = new PushClient();

    @Override
    public void WhenInfraOrderFinished(User user, String ticker, String title, String text) {
        if ("IOS".equals(user.getMobileType())) {
//            IOSUnicast unicast;
            try {
                IOSCustomizedcast customizedcast = new IOSCustomizedcast(appkey_ios, appMasterSecret_ios);
                customizedcast.setAlias(user.getDeviceToken(), "ZSH_IOS");
                customizedcast.setAlert(ticker);
                customizedcast.setBadge(0);
                customizedcast.setSound("default");
                customizedcast.setTestMode();
                customizedcast.setCustomizedField("customMessage", text);
                customizedcast.setCustomizedField("body", text);
//                customizedcast.setPredefinedKeyValue("badge", text);
                customizedcast.setDescription(title);
//                unicast = new IOSUnicast(appkey_android, appMasterSecret_android);
//                unicast.setAlert(user.getDeviceToken());
//                unicast.setBadge(0);
//                unicast.setSound("default");
//                unicast.setProductionMode();
//                unicast.setCustomizedField("customMessage", text);
//                unicast.setPredefinedKeyValue("badge", text);
                boolean bl = client.send(customizedcast);
                logger.info("IOS user:{} unicast:{}", new Object[]{user.getUsername(), bl});
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if ("Android".equals(user.getMobileType())) {
            try {
                AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(appkey_android, appMasterSecret_android);
                customizedcast.setAlias(user.getDeviceToken(), "ZSH_Android");
                customizedcast.setTicker(ticker);
                customizedcast.setTitle(title);
                customizedcast.setText(title);
                customizedcast.goAppAfterOpen();
                customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
                customizedcast.setProductionMode();
                customizedcast.setCustomField(text);
                Boolean bl = client.send(customizedcast);
//                unicast = new AndroidUnicast(appkey_android, appMasterSecret_android);
//                unicast.setDeviceToken(user.getDeviceToken());
//                unicast.setTicker(ticker);
//                unicast.setTitle(title);
//                unicast.setText(title);
//                unicast.goAppAfterOpen();
//                unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
//                unicast.setProductionMode();
//                unicast.setCustomField(text);
//                Boolean bl = client.send(unicast);
                logger.info("Android user:{} unicast:{}", new Object[]{user.getUsername(), bl});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void WhenInfraOrderFinished(String user, String ticker, String title, String text) {

    }
}
