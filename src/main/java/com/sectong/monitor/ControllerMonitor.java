package com.sectong.monitor;

import com.sectong.domain.mongomodle.InfraCommand;
import com.sectong.utils.JsonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xueyong on 16/7/1.
 * demo.
 */

@Aspect
@Component
public class ControllerMonitor {

    private static final Logger logger = LoggerFactory.getLogger(ControllerMonitor.class);

    @Around("execution(public * com.sectong.controller.*Controller.*(..))")
    public Object accessController(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        StringBuilder sb = new StringBuilder("|request|").append(joinPoint.getTarget().getClass().getName())
                .append(".").append(joinPoint.getSignature().getName());
        StringBuilder sp = new StringBuilder("");
        for (Object object : objects) {
            if (!(object instanceof HttpServletRequest)
                    && !(object instanceof HttpServletResponse)
                    && !(object instanceof BindingResult)
                    && !(object instanceof WebDataBinder)
                    && !(object instanceof InfraCommand)
                    && !(object instanceof MultipartFile)) {
                sb.append("|").append(JsonUtil.toJSONString(object));
            }
        }
        logger.info(sb.toString());
        Object response = joinPoint.proceed();
        sp.append("|response|").append(joinPoint.getTarget().getClass().getName())
                .append(".").append(joinPoint.getSignature().getName())
                .append("|").append(JsonUtil.toJSONString(response));
        logger.info(sp.toString());
        return response;
    }
}
