package com.shizhan.bookclub.app.model;

import cn.bmob.v3.BmobObject;

public class Library extends BmobObject {
	
	private String name;     //ͼ�������
	private String url;      //��վ����
	private Number hot;      //������
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Number getHot() {
		return hot;
	}
	
	public void setHot(Number hot) {
		this.hot = hot;
	}
	
	// Ϊ�˷���ʹ��List��contains����,������дequals����
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Library) {
			Library library = (Library) obj;
			return this.getObjectId().equals(library.getObjectId());
		}
		return false;
	}

}
