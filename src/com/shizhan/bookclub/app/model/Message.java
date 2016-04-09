package com.shizhan.bookclub.app.model;

import cn.bmob.v3.BmobObject;

public class Message extends BmobObject {
	
	private MyUsers fromuser;
	private String content;
	private MyUsers touser;
	
	public MyUsers getFromuser() {
		return fromuser;
	}
	public String getContent() {
		return content;
	}
	public MyUsers getTouser() {
		return touser;
	}
	public void setFromuser(MyUsers fromuser) {
		this.fromuser = fromuser;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setTouser(MyUsers touser) {
		this.touser = touser;
	}
	
}
