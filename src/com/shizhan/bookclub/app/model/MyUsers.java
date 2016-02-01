package com.shizhan.bookclub.app.model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

public class MyUsers extends BmobUser {
	
	private String userId;    //账号
	private BmobRelation likes;  //多对多关系，用于存储喜欢该帖子的所有用户

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BmobRelation getLikes() {
		return likes;
	}

	public void setLikes(BmobRelation likes) {
		this.likes = likes;
	}

	
}
