package com.shizhan.bookclub.app.util;

import cn.bmob.newim.BmobIM;
import android.app.Application;

public class BmobIMApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		BmobIM.init(this);
	}

}
