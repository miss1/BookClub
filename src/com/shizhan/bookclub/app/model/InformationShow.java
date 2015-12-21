package com.shizhan.bookclub.app.model;

public class InformationShow {
	
	private String infoHead;
	private String infoContent;
	
	public InformationShow(){
		
	}
	
	public InformationShow(String infoHead, String infoContent) {
		this.infoHead = infoHead;
		this.infoContent = infoContent;
	}

	public String getInfoHead() {
		return infoHead;
	}
	
	public String getInfoContent() {
		return infoContent;
	}		

}
