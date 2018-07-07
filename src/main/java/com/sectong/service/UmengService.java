package com.sectong.service;

import com.sectong.domain.User;

/**
 * Created by xueyong on 16/12/27.
 * mobileeasy-referal.
 */
public interface UmengService {
    void WhenInfraOrderFinished(User user, String ticker, String title, String text);
    void WhenInfraOrderFinished(String user, String ticker, String title, String text);
}
