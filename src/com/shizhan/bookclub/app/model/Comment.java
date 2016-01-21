package com.shizhan.bookclub.app.model;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {
	
	private String content;     //评论内容
	private String time;     //评论的时间
	private MyUsers user;     //评论的用户
	private Post post;      //评论的帖子
	
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
	
	public Post getPost() {
		return post;
	}
	
	public void setPost(Post post) {
		this.post = post;
	}
	
}
