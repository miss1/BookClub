package com.shizhan.bookclub.app.model;

import cn.bmob.v3.BmobObject;

public class Library extends BmobObject {
	
	private String name;     //图书馆名称
	private String url;      //网站链接
	private Number hot;      //访问量
	
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
	
	// 为了方便使用List的contains方法,必须重写equals方法
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
