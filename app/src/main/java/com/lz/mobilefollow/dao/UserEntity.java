package com.lz.mobilefollow.dao;

import java.io.Serializable;

/**用户基本信息类*/
public class UserEntity implements Serializable{

	/**用户id*/
	private Integer id;
	/**token*/
	private String token;
	/**姓名*/
	private String user_name;
	/**用户性别*/
	private String user_gender;
	/**用户年龄*/
	private Integer user_age;
	/**密码*/
	private String user_password;
	/**用户在线状态*/
	private int user_status;
	/**更新时间*/
	private Long update_time;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String userName) {
		user_name = userName;
	}
	public String getUser_gender() {
		return user_gender;
	}
	public void setUser_gender(String userGender) {
		user_gender = userGender;
	}
	public Integer getUser_age() {
		return user_age;
	}
	public void setUser_age(Integer userAge) {
		user_age = userAge;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String userPassword) {
		user_password = userPassword;
	}
	public int getUser_status() {
		return user_status;
	}
	public void setUser_status(int userStatus) {
		user_status = userStatus;
	}
	public Long getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Long updateTime) {
		update_time = updateTime;
	}
	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", token=" + token + ", update_time="
				+ update_time + ", user_age=" + user_age + ", user_gender="
				+ user_gender + ", user_name=" + user_name + ", user_password="
				+ user_password + ", user_status=" + user_status + "]";
	}
	
}
