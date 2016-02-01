package com.shizhan.bookclub.app.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Post extends BmobObject {
	
	private String title;     //���ӱ���
	private String content;     //��������
	private String time;     //����ʱ�� 
	private MyUsers user;     //���ӵķ�����
	private BmobFile image;    //����ͼƬ
	
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

	//Ϊ�˷���ʹ��List��contains����,������дequals����
	public boolean equals(Object obj){
		if(obj==this){
			return true;
		}
		if(obj instanceof Post){
			Post post = (Post) obj;
			return this.getObjectId().equals(post.getObjectId());
		}
		return false;	
	}
	
}
