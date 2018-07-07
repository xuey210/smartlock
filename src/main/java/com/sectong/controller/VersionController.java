package com.sectong.controller;

import com.sectong.constant.APIEm;
import com.sectong.domain.mongomodle.Version;
import com.sectong.domain.mongomodle.VersionFactory;
import com.sectong.message.Message;
import com.sectong.service.MacService;
import com.sectong.utils.JsonUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * 处理用户类接口
 * 
 * @author jiekechoo
 *
 */
@RestController
@PropertySource("classpath:message.properties")
@Api(basePath = "/v", value = "版本相关API", description = "版本信息", produces = "application/json")
@RequestMapping("/v")
public class VersionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(VersionController.class);

    @Autowired
    private MacService macService;


    @ApiOperation(httpMethod = "GET", value = "获取版本更新前信息",
            notes = "获取本次更新的问题，包数等信息。")
    @RequestMapping(method = RequestMethod.GET, value = "/preUpdate")
    public ResponseEntity<Message> preUpdate(HttpServletRequest request) {
        Message message = new Message();
        Object result;
        try {
            result = VersionFactory.getV();
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            result = e.getMessage();
        }
        message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), result);
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET", value = "更新版本",
            notes = "获取更新包")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "v", value = "版本号", required = true, dataType = "String")
            ,@ApiImplicitParam(name = "num", value = "更新文件的号", required = true, dataType = "String")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/update/{v}/{num}")
    public ResponseEntity<Message> update(HttpServletRequest request, @PathVariable String v,
                                          @PathVariable Integer num,HttpServletResponse response) {
        try {
            Assert.notNull(v);
            Assert.notNull(num);
            byte[] result = VersionFactory.versionSegment(v, num);
            OutputStream out = response.getOutputStream();
            out.write(result);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("post exception :{}", e);
        }
        return null;
    }

    @ApiOperation(httpMethod = "POST", value = "更新结束",notes = "主机更新结束后，把本次的更新结果提交，保存到服务器")
    @ApiImplicitParams({
    })
    @RequestMapping(method = RequestMethod.POST, value = "/afterUpdate")
    public ResponseEntity<Message> afterUpdate(HttpServletRequest request) {
        Message message = new Message();
        try {
            Version version = JsonUtil.parseObject(IOUtils.toString(request.getInputStream()), Version.class);
            Version v = macService.insertVersion(version);
            message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), v);
        } catch (Exception e) {
            LOGGER.error("post exception :{}", e);
            message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage(), null);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
