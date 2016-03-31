package com.shizhan.bookclub.app.model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

public class MyUsers extends BmobUser {
	
	private String userId;       //�˺�
	private String imageUrl;     //ͷ��ͼƬ·��
	private BmobRelation likes;  //��Զ��ϵ�����ڴ洢ϲ�������ӵ������û�

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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
}
