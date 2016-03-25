package com.shizhan.bookclub.app.util;

import com.baidu.mapapi.SDKInitializer;
import com.shizhan.bookclub.app.service.LocationService;

import cn.bmob.newim.BmobIM;
import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

public class BmobIMApplication extends Application {
	
	public LocationService locationService;
    public Vibrator mVibrator;
	
	@Override
	public void onCreate() {
		super.onCreate();
		BmobIM.init(this);
		
		//��ʼ����λ��ͼsdk��������Application�д���
		locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());  
	}

}
