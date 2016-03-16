package com.shizhan.bookclub.app.model;

import cn.bmob.v3.BmobObject;

public class Library extends BmobObject {
	
	private String name;     //图书馆名称
	private String url;      //网站链接
	private String hot;      //访问量
	
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
	
	public String getHot() {
		return hot;
	}
	
	public void setHot(String hot) {
		this.hot = hot;
	}
	
	

}
