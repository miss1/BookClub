package com.shizhan.bookclub.app.event;

import cn.bmob.newim.bean.BmobIMUserInfo;

public class ChatEvent {
	
	public BmobIMUserInfo info;
	
	public ChatEvent(BmobIMUserInfo info){
		this.info = info;
	}

}
