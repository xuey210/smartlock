package com.sectong.controller;

import com.sectong.constant.APIEm;
import com.sectong.domain.User;
import com.sectong.domain.UserCreateForm;
import com.sectong.message.Message;
import com.sectong.repository.UserRepository;
import com.sectong.service.UserService;
import com.sectong.utils.RedisUtil;
import com.sectong.validator.UserCreateFormValidator;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理用户类接口
 * 
 * @author jiekechoo
 *
 */
@RestController
@PropertySource("classpath:message.properties")
@Api(basePath = "/api", value = "用户API", description = "用户相关", produces = "application/json")
@RequestMapping("/api")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	private UserService userService;
	private UserCreateFormValidator userCreateFormValidator;
	private UserRepository userRepository;
    private RedisUtil redisUtil;

	private Message message = new Message();

	@Value("${myapp.value}")
	private String myvalue;

	@Autowired
	private Environment env;

	@Autowired
	public UserController(UserService userService, UserCreateFormValidator userCreateFormValidator,
			UserRepository userRepository,RedisUtil redisUtil) {
		this.userService = userService;
		this.userCreateFormValidator = userCreateFormValidator;
		this.userRepository = userRepository;
        this.redisUtil = redisUtil;
    }

	@InitBinder("userCreateForm")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(userCreateFormValidator);
	}

	@ApiOperation(httpMethod = "POST", value = "用户登录(<font color='blue'>release</font>)", notes = "使用<a href='http://zh.wikipedia.org/wiki/HTTP%E5%9F%BA%E6%9C%AC%E8%AE%A4%E8%AF%81'>httpBasic验证</a>，"
			+ "提交手机号码和用户密码登录，登录成功返回1，其他失败。<br>"
			+ "1、将'Authorization: Basic <base64(username:password)>'放入http header；<br>"
			+ "2、成功后从http header中获得的x-auth-token将作为以后登录的凭证。<br><br>"
			+ "本次登录成功后，前一个以此用户凭证登录的用户将被<font color='red'>超时踢出</font>。")
	@RequestMapping(method = RequestMethod.POST, value = "i/userLogin")
	public ResponseEntity<Message> userLogin(HttpServletRequest request) {
		User user = userService.getCurrentUser();
        if (user == null) {
            message.setMsg(0, "用户登录失败,用户名或者密码错误", null);
        }else{
            message.setMsg(1, "用户登录成功", user);
        }
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

    @ApiOperation(httpMethod = "POST", value = "用户修改密码(<font color='blue'>release</font>)",
            notes = "用户输入原有密码，和新密码")
    @RequestMapping(method = RequestMethod.POST, value = "i/updatepwd")
    public ResponseEntity<Message> updatePwd(HttpServletRequest request) {
        User user = userService.getCurrentUser();
        user.setPassword(new BCryptPasswordEncoder(10).encode(request.getParameter("repwd")));
        userRepository.save(user);
        message.setMsg(1, "用户修改密码成功", user);
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "POST", value = "创建用户(<font color='blue'>release</font>)", notes = "说明：\n deviceToken参数是客户端注册了环信的用户之后拿到的，此后要传给服务器，以后做消息推送时使用.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
            , @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "deviceToken", value = "环信的token", required = true, dataType = "String")
    })
    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Message> handleUserCreateForm(HttpServletRequest request, UserCreateForm form) {
        Message message = new Message();
//        if (bindingResult.hasErrors()) {
//			 failed validation
//            message.setMsg(APIEm.FAIL.getCode(), "user_create error: failed validation");
//            return new ResponseEntity<Message>(message, HttpStatus.OK);
//		}
        Assert.notNull(form.getDeviceToken(), "deviceToken is must be not null!");
        Assert.notNull(form.getVerficationCode(), "verficationCode is must be not null!");
        Assert.notNull(form.getUsername(), "username is must be not null!");
        Assert.notNull(form.getPassword(), "password is must be not null!");
        Assert.notNull(form.getMobileType(), "mobileType is must be not null!");

        try {
            String redis_verficationCode = (String) redisUtil.get(form.getUsername());
            if (StringUtils.isBlank(redis_verficationCode)) {
                message.setMsg(APIEm.INVALIDCODE.getCode(), APIEm.INVALIDCODE.getMessage());
                return new ResponseEntity<>(message, HttpStatus.OK);
            }
            if (!redis_verficationCode.equals(form.getVerficationCode())) {
                message.setMsg(APIEm.VERIFICATIONCODEINCORRECT.getCode(), APIEm.VERIFICATIONCODEINCORRECT.getMessage());
                return new ResponseEntity<>(message, HttpStatus.OK);
            }
            userService.create(form);
        } catch (Exception e) {
            e.printStackTrace();
            // probably email already exists - very rare case when multiple
            // admins are adding same user
            // at the same time and form validation has passed for more than one
            // of them.
            LOGGER.warn("Exception occurred when trying to save the user, assuming duplicate username", e);
            message.setMsg(APIEm.FAIL.getCode(), "user_create error: username already exists");
            return new ResponseEntity<>(message, HttpStatus.OK);

        }
        // ok, redirect
        message.setMsg(APIEm.SUCCESS.getCode(), "create user success");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET", value = "获取验证码(<font color='blue'>release</font>)", notes = "GET请求")
    @ResponseBody
    @RequestMapping(value = "/verificationCode/{tel}", method = RequestMethod.GET)
    public ResponseEntity<Message> verificationCode(@PathVariable String tel) {
        try {
            Assert.notNull(tel, "tel must not be null!");
            userService.getVerificationCode(tel);
            message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage());
        } catch (Exception e) {
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage());
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "POST", value = "忘记密码(<font color='blue'>release</font>)", notes = "POST请求")
    @ResponseBody
    @RequestMapping(value = "/forgottenPwd", method = RequestMethod.POST)
    public ResponseEntity<Message> forgottenPwd(UserCreateForm user, HttpServletRequest request) {
        Message message = new Message();
        try {
            Assert.notNull(user.getUsername(), "tel must not be null!");
            String redis_verficationCode = (String) redisUtil.get(user.getUsername());
            LOGGER.info("获取到的verfication code:{}", new Object[]{redis_verficationCode});
            if (StringUtils.isBlank(redis_verficationCode)) {
                message.setMsg(APIEm.INVALIDCODE.getCode(), APIEm.INVALIDCODE.getMessage());
                return new ResponseEntity<>(message, HttpStatus.OK);
            }
            if (!redis_verficationCode.equals(user.getVerficationCode())) {
                message.setMsg(APIEm.VERIFICATIONCODEINCORRECT.getCode(), APIEm.VERIFICATIONCODEINCORRECT.getMessage());
                return new ResponseEntity<>(message, HttpStatus.OK);
            }
            userService.reSetPassword(user.getUsername(), new BCryptPasswordEncoder(10).encode(user.getPassword()));
            message.setMsg(APIEm.SUCCESS.getCode(), APIEm.SUCCESS.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            message.setMsg(APIEm.FAIL.getCode(), APIEm.FAIL.getMessage());
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

	/**
	 * 使用 ResponseBody作为结果 200
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(httpMethod = "GET", value = "查看用户信息-HTTP200(<font color='blue'>release</font>)", notes = "使用用户id进行查看，如果用户不存在，也返回200。返回用户类实体")
	@ResponseBody
	@RequestMapping(value = "/i/user/{id}", method = RequestMethod.GET)
	public User findByUserId(@PathVariable long id) {
		User user = userRepository.findOne(id);
		// HttpStatus status = user != null ? HttpStatus.OK :
		// HttpStatus.NOT_FOUND;
		return user;
	}

	/**
	 * 使用ResponseEntity作为返回结果 404
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(httpMethod = "GET", value = "查看用户信息-HTTP404(<font color='blue'>release</font>)", notes = "使用用户id进行查看，如果用户不存在，返回404错误。返回用户类实体")
	@RequestMapping(value = "/i/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> findById(@PathVariable long id) {
		User user = userRepository.findOne(id);
		HttpStatus status = user != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<User>(user, status);
	}

	/**
	 * 使用 ResponseEntity 返回自定义错误结果
	 * 
	 * @return
	 */
	@ApiOperation(httpMethod = "GET", value = "查看用户信息(<font color='blue'>release</font>)", notes = "使用用户id进行查看，自定义返回消息类：code，msg，content")
	@RequestMapping(value = "/i/users/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findByUsersId(@PathVariable long id) {
		User user = userRepository.findOne(id);
		if (user == null) {
			message.setMsg(106, env.getProperty("106"));
			return new ResponseEntity<Message>(message, HttpStatus.NOT_FOUND);
		}
		message.setMsg(101, "Get user info", user);
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}

	@ApiOperation(httpMethod = "GET", value = "用户信息列表(<font color='blue'>release</font>)", notes = "查看用户信息列表，可分页")
	@ResponseBody
	@RequestMapping(value = "/i/users/list", method = RequestMethod.GET)
	public ResponseEntity<Message> list(Pageable p) {

		message.setMsg(1, "List Users", userService.listAllUsers(p));
		return new ResponseEntity<Message>(message, HttpStatus.OK);

	}

	@ApiOperation(httpMethod = "POST", value = "上传用户头像(<font color='blue'>release</font>)", notes = "使用MultipartFile方式")
	@RequestMapping(value = "/i/uploadImage", method = RequestMethod.POST)
	public ResponseEntity<?> uploadImage(@RequestParam MultipartFile file, HttpServletRequest request) {
		message.setMsg(1, "upload user image", userService.uploadImage(file, request));
		return new ResponseEntity<Message>(message, HttpStatus.OK);

	}

	@ApiOperation(httpMethod = "POST", value = "用户信息列表(<font color='blue'>供管理界面使用</font>)", notes = "查看用户信息列表，可分页")
	@RequestMapping(value = "/getUserList", method = RequestMethod.POST)
	public Object getUserList(@RequestParam(required = false) int current, @RequestParam(required = false) int rowCount,
			@RequestParam(required = false) String searchPhrase) {
		return userService.getUserList(current, rowCount, searchPhrase);

	}

}
