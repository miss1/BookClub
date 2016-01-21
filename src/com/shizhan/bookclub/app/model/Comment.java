package com.shizhan.bookclub.app.model;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {
	
	private String content;     //��������
	private String time;     //���۵�ʱ��
	private MyUsers user;     //���۵��û�
	private Post post;      //���۵�����
	
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
