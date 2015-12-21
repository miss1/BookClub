package com.shizhan.bookclub.app.model;

import cn.bmob.v3.BmobObject;

public class Information extends BmobObject{
	
	private MyUsers user;
	private String nicheng;
	private String zhanghao;
	private String sex;
	private String age;
	private String city;
	private String geqian;
	private String lovebook;
	private String loveauthor;
	private String bookstyle;
	
	public MyUsers getUser() {
		return user;
	}
	
	
	public String getNicheng() {
		return nicheng;
	}

	public void setNicheng(String nicheng) {
		this.nicheng = nicheng;
	}

	public void setUser(MyUsers user) {
		this.user = user;
	}
	
	public String getZhanghao() {
		return zhanghao;
	}
	
	public void setZhanghao(String zhanghao) {
		this.zhanghao = zhanghao;
	}
	
	public String getSex() {
		return sex;
	}
	
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public String getAge() {
		return age;
	}
	
	public void setAge(String age) {
		this.age = age;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getGeqian() {
		return geqian;
	}
	
	public void setGeqian(String geqian) {
		this.geqian = geqian;
	}
	
	public String getLovebook() {
		return lovebook;
	}
	
	public void setLovebook(String lovebook) {
		this.lovebook = lovebook;
	}
	
	public String getLoveauthor() {
		return loveauthor;
	}
	
	public void setLoveauthor(String loveauthor) {
		this.loveauthor = loveauthor;
	}
	
	public String getBookstyle() {
		return bookstyle;
	}
	
	public void setBookstyle(String bookstyle) {
		this.bookstyle = bookstyle;
	}
	
	

}
