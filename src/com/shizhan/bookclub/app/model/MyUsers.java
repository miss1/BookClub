package com.shizhan.bookclub.app.model;

import cn.bmob.v3.BmobUser;

public class MyUsers extends BmobUser {
	
	private String userId;    //�˺�

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
