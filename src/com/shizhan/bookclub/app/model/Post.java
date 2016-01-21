package com.shizhan.bookclub.app.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class Post extends BmobObject {
	
	private String title;     //帖子标题
	private String content;     //帖子内容
	private String time;     //发帖时间 
	private MyUsers user;     //帖子的发布者
	private BmobFile image;    //帖子图片
	private BmobRelation likes;  //多对多关系，用于存储喜欢该帖子的所有用户
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public MyUsers getUser() {
		return user;
	}
	
	public void setUser(MyUsers user) {
		this.user = user;
	}
	
	public BmobFile getImage() {
		return image;
	}
	
	public void setImage(BmobFile image) {
		this.image = image;
	}

	public BmobRelation getLikes() {
		return likes;
	}

	public void setLikes(BmobRelation likes) {
		this.likes = likes;
	}
	
}
