package com.sectong.domain;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 创建用户字段POJO定义
 * 
 * @author jiekechoo
 *
 */
public class UserCreateForm {

	@NotEmpty
	private String username;

	@NotEmpty
	private String password;

    private String deviceToken;

    private String verficationCode;

    private String mobileType;

    public String getMobileType() {
        return mobileType;
    }

    public void setMobileType(String mobileType) {
        this.mobileType = mobileType;
    }

    public String getVerficationCode() {
        return verficationCode;
    }

    public void setVerficationCode(String verficationCode) {
        this.verficationCode = verficationCode;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    // @NotEmpty
	// private String passwordRepeat;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// public String getPasswordRepeat() {
	// return passwordRepeat;
	// }
	//
	// public void setPasswordRepeat(String passwordRepeat) {
	// this.passwordRepeat = passwordRepeat;
	// }

	@Override
	public String toString() {
		return "UserCreateForm [username=" + username + ", password=" + password + "]";
	}

}
