package com.shizhan.bookclub.app.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class Post extends BmobObject {
	
	private String title;     //���ӱ���
	private String content;     //��������
	private String time;     //����ʱ�� 
	private MyUsers user;     //���ӵķ�����
	private BmobFile image;    //����ͼƬ
	private BmobRelation likes;  //��Զ��ϵ�����ڴ洢ϲ�������ӵ������û�
	
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
